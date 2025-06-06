package tests.vindex_backoffice_ui_tests.investigationTool;

import business_objects.db.audit_service_db.Event;
import business_objects.db.clickhouse.connection_table.ConnectionTableEntry;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import helpers.data.ClientHelper;
import helpers.database.DbName;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.util.List;

import static business_objects.db.clickhouse.connection_table.ConnectionTableEntryFactory.*;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.ui.user.UserFactory.autotestUserOne;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.*;

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
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
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

    @AfterAll
    static void teardown() {
        deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("ucid IN ('%s', '%s', '%s')", client.getUcid(), connectedClient1.getUcid(), connectedClient2.getUcid()));
        deleteEntryFromDb(CONNECTIONS_TABLE_NAME, String.format("user_from IN ('%s', '%s', '%s')", client.getUcid(), connectedClient1.getUcid(), connectedClient2.getUcid()));
    }
}
