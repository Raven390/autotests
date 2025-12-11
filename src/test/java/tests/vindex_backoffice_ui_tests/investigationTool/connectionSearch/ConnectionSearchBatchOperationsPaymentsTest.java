package tests.vindex_backoffice_ui_tests.investigationTool.connectionSearch;

import business_objects.db.abuse_registry_db.AbuserDeduction;
import business_objects.db.abuse_registry_db.AbuserFraudType;
import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.data_science_test.connection_table.ConnectionTableEntry;
import helpers.data.ClientHelper;
import helpers.data.enums.FraudType;
import helpers.data.enums.FraudTypeStatus;
import helpers.database.ArHelper;
import helpers.database.DbName;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.util.List;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.data_science_test.connection_table.ConnectionTableEntryFactory.getConnectionTableEntryForUiFiltration1;
import static business_objects.db.clickhouse.data_science_test.connection_table.ConnectionTableEntryFactory.getConnectionTableEntryForUiFiltration2;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.FraudType.CHARGEBACK;
import static helpers.data.enums.FraudType.EXCHANGER;
import static helpers.data.enums.FraudTypeStatus.CONFIRMED;
import static helpers.database.AuHelper.cleanClientAudit;
import static helpers.database.BoHelper.deleteUserBO;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.insertCrmAccountsToDb;
import static utils.Utils.waitForConnectionSearchToUpdate;


@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_WEB)
@Feature("BMS-2987 Allow payment team to apply restrictions and fraud types in Connection search")
class ConnectionSearchBatchOperationsPaymentsTest extends TestBaseWeb {

    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final ClientHelper connectedClient1 = getRandomVantageClientAllFields();
    private static final ClientHelper connectedClient2 = getRandomVantageClientAllFields();
    private static final CrmTbAccountObject account1 = generateCrmTbAccountDataForUi(connectedClient1);
    private static final CrmTbAccountObject account2 = generateCrmTbAccountDataForUi(connectedClient2);
    private static final String COMMENT = "test comment";

    @BeforeAll
    static void setup() throws Exception {
        CrmTbUserObject crmTbUser = generateUserByClient(client);
        CrmTbUserObject connectedCrmTbUser1 = generateUserByClient(connectedClient1);
        CrmTbUserObject connectedCrmTbUser2 = generateUserByClient(connectedClient2);
        insertObjectsToDb(CRM_USER_TABLE_NAME, List.of(crmTbUser, connectedCrmTbUser1, connectedCrmTbUser2));
        ConnectionTableEntry connectionTableEntry1 = getConnectionTableEntryForUiFiltration1(client, connectedClient1);
        ConnectionTableEntry connectionTableEntry2 = getConnectionTableEntryForUiFiltration2(client, connectedClient2);
        insertObjectsToDb(CONNECTIONS_TABLE_NAME, List.of(connectionTableEntry1, connectionTableEntry2));
        insertCrmAccountsToDb(account1, account2);
        waitForConnectionSearchToUpdate(client);
    }

    @Test
    @AllureId("1935")
    @DisplayName("Verify adding frauds in connection search for payment team user")
    void verifyFraudRestrictionInConnectionSearchPaymentsTest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsPaymentTeamUser();
        investigationPage.navigateToClient(client.getUcid());
        alertsPage.waitForPageToLoad();
        connectionPage.clickConnectionTabButton();
        connectionPage.openConnectionTable();
        connectionPage.clickMultiselectButton();
        connectionPage.clickMultiselectCheckboxByUcid(connectedClient1.getUcid());
        connectionPage.openFraudRestrictionsForm();
        FraudType fraudType = EXCHANGER;
        FraudTypeStatus fraudTypeStatus = CONFIRMED;
        assertThat("Verify only payment fraud types are visible", connectionPage.getFraudTypesList(), everyItem(is(in(FraudType.getVisibleFraudTypeNamesList()))));
        connectionPage.addFraud(fraudType.getName(), fraudTypeStatus.getDisplayName(), COMMENT);
        page.waitForTimeout(1000);

        List<AbuserFraudType> frauds = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_FRAUD_TYPE_TABLE_NAME, String.format("ucid = '%s'", connectedClient1.getUcid()), AbuserFraudType.class);
        assertThat("Verify there is one record in ar.abuser_fraud_type", frauds.size(), is(1));
        AbuserFraudType fraud = frauds.getFirst();
        assertThat("Verify record in ar.abuser_fraud_type has correct fraud", fraud.getFraudTypeCode(), is(fraudType.getCode()));
        assertThat("Verify record in ar.abuser_fraud_type has correct status", fraud.getStatus(), is(fraudTypeStatus.getStatus()));
        assertThat("Verify record in ar.abuser_fraud_type has correct comment", fraud.getComment(), is(COMMENT));
        assertThat("Verify source in ar.abuser_fraud_type is default value: Vindex", fraud.getFraudSource(), is("Vindex"));
        assertThat("Verify no deductions were created", getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_DEDUCTION_TABLE_NAME, String.format("ucid = '%s'", connectedClient1.getUcid()), AbuserDeduction.class), hasSize(0));
    }

    @Test
    @AllureId("1936")
    @DisplayName("Verify adding frauds in connection search for payment senior user")
    void verifyFraudRestrictionInConnectionSearchPaymentsSeniorTest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsPaymentSeniorUser();
        investigationPage.navigateToClient(client.getUcid());
        alertsPage.waitForPageToLoad();
        connectionPage.clickConnectionTabButton();
        connectionPage.openConnectionTable();
        connectionPage.clickMultiselectButton();
        connectionPage.clickMultiselectCheckboxByUcid(connectedClient2.getUcid());
        connectionPage.openFraudRestrictionsForm();
        FraudType fraudType = CHARGEBACK;
        FraudTypeStatus fraudTypeStatus = CONFIRMED;
        assertThat("Verify only payment fraud types are visible", connectionPage.getFraudTypesList(), everyItem(is(in(FraudType.getVisibleFraudTypeNamesList()))));
        connectionPage.addFraud(fraudType.getName(), fraudTypeStatus.getDisplayName(), COMMENT);
        page.waitForTimeout(1000);

        List<AbuserFraudType> frauds = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_FRAUD_TYPE_TABLE_NAME, String.format("ucid = '%s'", connectedClient2.getUcid()), AbuserFraudType.class);
        assertThat("Verify there is one record in ar.abuser_fraud_type", frauds.size(), is(1));
        AbuserFraudType fraud = frauds.getFirst();
        assertThat("Verify record in ar.abuser_fraud_type has correct fraud", fraud.getFraudTypeCode(), is(fraudType.getCode()));
        assertThat("Verify record in ar.abuser_fraud_type has correct status", fraud.getStatus(), is(fraudTypeStatus.getStatus()));
        assertThat("Verify record in ar.abuser_fraud_type has correct comment", fraud.getComment(), is(COMMENT));
        assertThat("Verify source in ar.abuser_fraud_type is default value: Vindex", fraud.getFraudSource(), is("Vindex"));
        assertThat("Verify no deductions were created", getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_DEDUCTION_TABLE_NAME, String.format("ucid = '%s'", connectedClient2.getUcid()), AbuserDeduction.class), hasSize(0));
    }

    @AfterAll
    static void teardown() throws Exception {
        deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("ucid IN ('%s', '%s', '%s')", client.getUcid(), connectedClient1.getUcid(), connectedClient2.getUcid()));
        deleteEntryFromDb(CONNECTIONS_TABLE_NAME, String.format("user_from IN ('%s', '%s', '%s')", client.getUcid(), connectedClient1.getUcid(), connectedClient2.getUcid()));
        cleanClientAudit(client.getUcid(), connectedClient1.getUcid(), connectedClient2.getUcid());
        deleteUserBO(client.getUcid());
        deleteUserBO(connectedClient1.getUcid());
        deleteUserBO(connectedClient2.getUcid());
        ArHelper.deleteUserFromAbuseRegistry(client.getUcid(), connectedClient1.getUcid(), connectedClient2.getUcid());
    }
}
