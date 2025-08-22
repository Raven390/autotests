package tests.vindex_backoffice_ui_tests.investigationTool.connectionSearch;

import business_objects.db.abuse_registry_db.AbuserFraudType;
import business_objects.db.audit_service_db.Event;
import business_objects.db.clickhouse.connection_table.ConnectionTableEntry;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import helpers.data.ClientHelper;
import helpers.data.enums.FraudType;
import helpers.data.enums.FraudTypeStatus;
import helpers.database.ArHelper;
import helpers.database.DbName;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.util.List;

import static business_objects.db.clickhouse.connection_table.ConnectionTableEntryFactory.*;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.ui.user.UserFactory.autotestUserOne;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.FraudType.getRandomFraudType;
import static helpers.data.enums.FraudTypeStatus.getRandomFraudStatusUi;
import static helpers.database.AuHelper.cleanClientAudit;
import static helpers.database.BoHelper.deleteUserBO;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.Constants.*;
import static utils.Utils.*;


@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_WEB)
public class ConnectionSearchBatchOperationsTest extends TestBaseWeb {

    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final ClientHelper connectedClient1 = getRandomVantageClientAllFields();
    private static final ClientHelper connectedClient2 = getRandomVantageClientAllFields();

    @BeforeAll
    static void setup() throws Exception {
        CrmTbUserObject crmTbUser = generateUserByClient(client);
        CrmTbUserObject connectedCrmTbUser1 = generateUserByClient(connectedClient1);
        CrmTbUserObject connectedCrmTbUser2 = generateUserByClient(connectedClient2);
        insertObjectsToDb(CRM_USER_TABLE_NAME, List.of(crmTbUser, connectedCrmTbUser1, connectedCrmTbUser2));
        ConnectionTableEntry connectionTableEntry1 = getConnectionTableEntryForUiFiltration1(client, connectedClient1);
        ConnectionTableEntry connectionTableEntry2 = getConnectionTableEntryForUiFiltration2(client, connectedClient2);
        insertObjectsToDb(CONNECTIONS_TABLE_NAME, List.of(connectionTableEntry1, connectionTableEntry2));
        waitForConnectionSearchToUpdate(client);
    }

    @BeforeEach
    public void goToConnectionSearchPage() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(client.getUcid());
        alertsPage.waitForPageToLoad();
        connectionPage.clickConnectionTabButton();
    }

    @Test
    @AllureId("1147")
    @Feature("BMS-1312")
    @DisplayName("Verify connection batch comment")
    public void verifyConnectionSearchFiltration1Test() throws Exception {
        connectionPage.openConnectionTable();
        connectionPage.clickMultiselectButton();
        connectionPage.clickMultiselectSelectAllCheckbox();
        assertThat("Verify multiselect counter", connectionPage.getMultiselectCounterText(), is("2 selected"));
        connectionPage.clickMultiselectCommentButton();
        String comment = "Connection batch comment";
        connectionPage.fillMultiselectComment(comment);
        connectionPage.clickMultiselectAddCommentButton();
        List<Event> events = getObjectsFromDB(DbName.AUDIT, AUDIT_EVENT, String.format("ucid IN ('%s', '%s') AND type = '%s' ORDER BY created_at ASC", connectedClient1.getUcid(), connectedClient2.getUcid(), COMMENT_ADDED_TYPE), Event.class);
        assertThat("Verify comments amount", events.size(), is(2));
        Event commentEvent1 = new Event();
        commentEvent1.setUcid(connectedClient1.getUcid());
        commentEvent1.setType(COMMENT_ADDED_TYPE);
        commentEvent1.setInitiatedBySystem(VINDEX_BO_SYSTEM);
        commentEvent1.setInitiatedByUser(String.format("%s %s", autotestUserOne().getFirstName(), autotestUserOne().getLastName()));
        commentEvent1.setComment(comment);
        Event commentEvent2 = new Event();
        commentEvent2.setUcid(connectedClient2.getUcid());
        commentEvent2.setType(COMMENT_ADDED_TYPE);
        commentEvent2.setInitiatedBySystem(VINDEX_BO_SYSTEM);
        commentEvent2.setInitiatedByUser(String.format("%s %s", autotestUserOne().getFirstName(), autotestUserOne().getLastName()));
        commentEvent2.setComment(comment);
        assertThat("Verify audit events", events, containsInAnyOrder(commentEvent1, commentEvent2));
    }

    @Test
    @AllureId("1502")
    @Feature("BMS-1502")
    @DisplayName("User can add mass fraud from CS")
    public void addFraudTest() throws Exception {
        connectionPage.openConnectionTable();
        connectionPage.clickMultiselectButton();
        connectionPage.clickMultiselectSelectAllCheckbox();
        connectionPage.openFraudRestrictionsForm();
        FraudType fraudType = getRandomFraudType();
        String commentary = "Commentary";
        FraudTypeStatus statusUi = getRandomFraudStatusUi();
        connectionPage.addFraud(fraudType.getName(), statusUi.getDisplayName(), commentary);

        page.waitForTimeout(1000);

        List<AbuserFraudType> frauds = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_FRAUD_TYPE_TABLE_NAME, "ucid='" + connectedClient1.getUcid() + "'", AbuserFraudType.class);
        Allure.step("Assert that there only one record in ar.abuser_fraud_type");
        assertEquals(1, frauds.size());
        AbuserFraudType fraud = frauds.getFirst();
        Allure.step("Assert that record in ar.abuser_fraud_type have right status");
        assertEquals(statusUi.getStatus(), fraud.getStatus());
        Allure.step("Assert that record in ar.abuser_fraud_type have right fraud");
        assertEquals(fraudType.getCode(), fraud.getFraudTypeCode());
        Allure.step("Assert that record in ar.abuser_fraud_type have commentary that you used in upload form");
        assertEquals(commentary, fraud.getComment());
        Allure.step("Assert that source in ar.abuser_fraud_type is default value: Vindex");
        assertEquals("Vindex", fraud.getFraudSource());
    }


    @AfterAll
    static void teardown() throws Exception {
        deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("ucid IN ('%s', '%s', '%s')", client.getUcid(), connectedClient1.getUcid(), connectedClient2.getUcid()));
        deleteEntryFromDb(CONNECTIONS_TABLE_NAME, String.format("user_from IN ('%s', '%s', '%s')", client.getUcid(), connectedClient1.getUcid(), connectedClient2.getUcid()));
        cleanClientAudit(client.getUcid(), connectedClient1.getUcid(), connectedClient2.getUcid());
        deleteUserBO(client.getUcid());
        deleteUserBO(connectedClient1.getUcid());
        deleteUserBO(connectedClient2.getUcid());
        ArHelper.deleteUserAR(client.getUcid(), connectedClient1.getUcid(), connectedClient2.getUcid());

    }
}
