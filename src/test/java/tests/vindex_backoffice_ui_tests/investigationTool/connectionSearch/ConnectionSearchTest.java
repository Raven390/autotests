package tests.vindex_backoffice_ui_tests.investigationTool.connectionSearch;

import static business_objects.api.mitigation_service.MitigationServiceRequest.postRestriction;
import static business_objects.db.clickhouse.account_ib_relation.AccountIbRelationFactory.generateAccountIbRelationObjectByClient;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.data_science_test.connection_table.ConnectionTableEntryFactory.getConnectionTableEntry;
import static business_objects.db.clickhouse.data_science_test.connection_table.ConnectionTableEntryFactory.getConnectionTableEntryForUi;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObjectFactory.generateMt4TradesCoercedForConnectionSearch;
import static business_objects.kafka.alerts.RuleAlertFactory.generateRuleAlertByUcid;
import static helpers.api.AbuseRegistryHelper.addFraudsForClient;
import static helpers.api.AbuseRegistryHelper.setClientStatus;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.ConnectedClientStatus.UNDER_INVESTIGATION;
import static helpers.data.enums.FraudType.*;
import static helpers.data.enums.FraudTypeStatus.CONFIRMED;
import static helpers.data.enums.FraudTypeStatus.POTENTIAL;
import static helpers.database.ArHelper.deleteUserFromAbuseRegistry;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.CleanTableHelper.cleanUserRestrictionGeneral;
import static helpers.database.DbHelper.*;
import static helpers.kafka.alerts.CreateSimpleAlert.sendSimpleAlert;
import static helpers.kafka.alerts.CreateSimpleAlert.sendSimplePaymentAlert;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.*;

import business_objects.api.mitigation_service.PostRestrictionRequestBody;
import business_objects.db.clickhouse.account_ib_relation.AccountIbRelationObject;
import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntity;
import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntityFactory;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalEntity;
import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalEntityFactory;
import business_objects.db.clickhouse.data_science_test.connection_table.ConnectionTableEntry;
import business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObject;
import business_objects.kafka.alerts.RuleAlert;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.data.ClientHelper;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.AllureId;
import java.text.DecimalFormat;
import java.util.List;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_WEB)
class ConnectionSearchTest extends TestBaseWeb {

    private static final KafkaHelper kafka = new KafkaHelper();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final ClientHelper connectedClient1 = getRandomVantageClientAllFields(); // lvl 1 normal
    private static final ClientHelper connectedClient2 = getRandomVantageClientAllFields(); // lvl 1 suspicious
    private static final ClientHelper connectedClient3 =
            getRandomVantageClientAllFields(); // lvl 2 potential status client
    private static final ClientHelper connectedClient4 =
            getRandomVantageClientAllFields(); // lvl 2 potential with abuse
    private static final ClientHelper connectedClient5 = getRandomVantageClientAllFields(); // lvl 3 confirmed fraud
    private static final ClientHelper connectedClient6 =
            getRandomVantageClientAllFields(); // lvl 3 alert in investigation
    private static final DecimalFormat formatter = new DecimalFormat("#,###.##");
    private static AccountIbRelationObject relation;
    private static MtMt4TradesCoercedObject trade;
    private static CrmTbUserObject connectedCrmTbUser1;
    private static CrmTbUserObject connectedCrmTbUser2;
    private static CrmTbUserObject connectedCrmTbUser3;
    private static CrmTbUserObject connectedCrmTbUser4;
    private static CrmTbUserObject connectedCrmTbUser5;
    private static CrmTbUserObject connectedCrmTbUser6;
    private static CrmTbDepositEntity deposit;
    private static CrmTbWithdrawalEntity withdrawal;

    @BeforeAll
    static void setup() throws Exception {
        // Users
        CrmTbUserObject crmTbUser = generateUserByClient(client);
        connectedCrmTbUser1 = generateUserByClient(connectedClient1);
        connectedCrmTbUser2 = generateUserByClient(connectedClient2);
        connectedCrmTbUser3 = generateUserByClient(connectedClient3);
        connectedCrmTbUser3.cpaId = getRandomIntPositive();
        connectedCrmTbUser4 = generateUserByClient(connectedClient4);
        connectedCrmTbUser5 = generateUserByClient(connectedClient5);
        connectedCrmTbUser6 = generateUserByClient(connectedClient6);
        insertObjectsToDb(
                CRM_USER_TABLE_NAME,
                List.of(
                        crmTbUser,
                        connectedCrmTbUser1,
                        connectedCrmTbUser2,
                        connectedCrmTbUser3,
                        connectedCrmTbUser4,
                        connectedCrmTbUser5,
                        connectedCrmTbUser6));
        // Connections
        ConnectionTableEntry connectionTableEntry1 = getConnectionTableEntry(client, connectedClient1);
        ConnectionTableEntry connectionTableEntry2 = getConnectionTableEntry(client, connectedClient2);
        ConnectionTableEntry connectionTableEntry3 = getConnectionTableEntry(connectedClient1, connectedClient3);
        ConnectionTableEntry connectionTableEntry4 = getConnectionTableEntry(connectedClient2, connectedClient4);
        ConnectionTableEntry connectionTableEntry5 = getConnectionTableEntryForUi(connectedClient3, connectedClient5);
        ConnectionTableEntry connectionTableEntry6 = getConnectionTableEntryForUi(connectedClient4, connectedClient6);
        insertObjectsToDb(
                CONNECTIONS_TABLE_NAME,
                List.of(
                        connectionTableEntry1,
                        connectionTableEntry2,
                        connectionTableEntry3,
                        connectionTableEntry4,
                        connectionTableEntry5,
                        connectionTableEntry6));
        // Restrictions
        postRestriction(new PostRestrictionRequestBody(
                connectedClient1.getUcid(),
                "03",
                "GENERAL",
                null,
                null,
                "Automation test",
                new PostRestrictionRequestBody.UpdatedBy("Auto", "Test")));
        postRestriction(new PostRestrictionRequestBody(
                connectedClient3.getUcid(),
                "03",
                "GENERAL",
                null,
                null,
                "Automation test",
                new PostRestrictionRequestBody.UpdatedBy("Auto", "Test")));
        // Frauds
        setClientStatus(connectedClient3, POTENTIAL);
        addFraudsForClient(connectedClient4, List.of(MARKET_MANIPULATION), POTENTIAL);
        addFraudsForClient(connectedClient5, List.of(GAP_TRADING), CONFIRMED);
        // Alerts
        RuleAlert alert1 = generateRuleAlertByUcid(connectedClient2.getUcid());
        kafka.produceMessage(alert1.alertId, objectMapper.writeValueAsString(alert1), KAFKA_TOPIC_ALERTS);
        sendSimpleAlert(connectedClient6.getUcid());
        sendSimplePaymentAlert(connectedClient6.getUcid());
        sendSimpleAlert(connectedClient4.getUcid());
        sendSimpleAlert(connectedClient5.getUcid());

        // Other data
        // PNL related
        CrmTbAccountObject account = generateCrmTbAccountDataForUi(connectedClient3);
        insertCrmAccountsToDb(account);
        insertObjectToDb(MT_ACCOUNT_TABLE_NAME, generateMtAccountByCrmTbAccount(account));
        trade = generateMt4TradesCoercedForConnectionSearch(connectedClient3, 123.45, getCurrentTimestampDbFormat());
        insertObjectToDb(MT4_TRADES_COERCED_TABLE_NAME, trade);
        // IB
        relation = generateAccountIbRelationObjectByClient(connectedClient3);
        relation.setDirectIbRebateAccount(getRandomIntPositive());
        relation.setRecordDeletedFlag("N");
        relation.setDirectIbLevel(1);
        relation.setDirectIb(getRandomIntPositive());
        insertObjectToDb(ACCOUNT_IB_RELATION_TABLE_NAME, relation);
        // Withdrawal, Deposit
        deposit = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(connectedClient3);
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, deposit);
        withdrawal = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(connectedClient3);
        insertObjectToDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, withdrawal);
        waitForConnectionSearchToUpdate(client);
    }

    @AfterAll
    static void tearDown() throws Exception {
        deleteObjectFromDb(
                CRM_USER_TABLE_NAME,
                String.format(
                        "ucid IN ('%s', '%s', '%s', '%s', '%s', '%s')",
                        client.getUcid(),
                        connectedClient1.getUcid(),
                        connectedClient2.getUcid(),
                        connectedClient3.getUcid(),
                        connectedClient4.getUcid(),
                        connectedClient5.getUcid()));
        deleteObjectFromDb(
                CONNECTIONS_TABLE_NAME,
                String.format(
                        "user_from IN ('%s', '%s', '%s', '%s')",
                        client.getUcid(),
                        connectedClient1.getUcid(),
                        connectedClient2.getUcid(),
                        connectedClient3.getUcid()));
        deleteObjectFromDb(MT4_TRADES_COERCED_TABLE_NAME, String.format("ucid IN ('%s')", connectedClient3.getUcid()));
        deleteUserFromAbuseRegistry(connectedClient3.getUcid(), connectedClient4.getUcid(), connectedClient5.getUcid());
        cleanUserRestrictionGeneral(connectedClient1.getUcid());
        closeAlert(client.getUcid());
        closeAlert(connectedClient2.getUcid());
        closeAlert(connectedClient4.getUcid());
        closeAlert(connectedClient6.getUcid());
        closeAlert(connectedClient5.getUcid());
        deleteObjectFromDb(ACCOUNT_IB_RELATION_TABLE_NAME, String.format("ucid = '%s'", connectedClient3.getUcid()));
        deleteObjectFromDb(CRM_DEPOSIT_TABLE_NAME, String.format("ucid = '%s'", connectedClient3.getUcid()));
        deleteObjectFromDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, String.format("ucid = '%s'", connectedClient3.getUcid()));
    }

    @BeforeEach
    void goToConnectionSearchPage() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(client.getUcid());
        alertsPage.waitForPageToLoad();
        connectionPage.clickConnectionTabButton();
    }

    @Test
    @AllureId("315")
    @DisplayName("Verify connection search ucids, statuses, order")
    void connectionSearchTest1() {
        investigationPage.navigateToClient(connectedClient6.getUcid());
        resolvePage.openResolveSuspicious();
        connectionPage.navigate(client.getUcid());

        // Statuses
        var statusReason = "Verify status shown in the node";
        assertThat(statusReason, connectionPage.getStatusByNodeUcid(client.getUcid()), is(STATUS_NORMAL));
        assertThat(statusReason, connectionPage.getStatusByNodeUcid(connectedClient1.getUcid()), is(STATUS_NORMAL));
        assertThat(statusReason, connectionPage.getStatusByNodeUcid(connectedClient2.getUcid()), is(STATUS_SUSPICIOUS));
        assertThat(statusReason, connectionPage.getStatusByNodeUcid(connectedClient3.getUcid()), is(POTENTIAL_ABUSE));
        assertThat(
                statusReason,
                connectionPage.getStatusByNodeUcid(connectedClient4.getUcid()),
                is(String.format("%s %s", POTENTIAL.getDisplayName(), MARKET_MANIPULATION.getName())));
        assertThat(
                statusReason,
                connectionPage.getStatusByNodeUcid(connectedClient5.getUcid()),
                is(GAP_TRADING.getName()));
        assertThat(
                statusReason,
                connectionPage.getStatusByNodeUcid(connectedClient6.getUcid()),
                is(UNDER_INVESTIGATION.getDisplayName()));

        // Order
        var orderReason = "Verify order of the node in the graph";
        assertThat(orderReason, connectionPage.getOrderByNodeUcid(client.getUcid()), is("0"));
        assertThat(orderReason, connectionPage.getOrderByNodeUcid(connectedClient1.getUcid()), anyOf(is("1"), is("2")));
        assertThat(orderReason, connectionPage.getOrderByNodeUcid(connectedClient2.getUcid()), anyOf(is("1"), is("2")));
        assertThat(orderReason, connectionPage.getOrderByNodeUcid(connectedClient3.getUcid()), anyOf(is("3"), is("4")));
        assertThat(orderReason, connectionPage.getOrderByNodeUcid(connectedClient4.getUcid()), anyOf(is("3"), is("4")));
        assertThat(orderReason, connectionPage.getOrderByNodeUcid(connectedClient5.getUcid()), is("5"));
    }

    @Test
    @AllureId("311")
    @DisplayName("Verify connection attributes in node")
    void connectionSearchTest2() {
        connectionPage.clickExpandNodeByUcid(connectedClient3.getUcid());
        assertThat(
                connectionPage.getNodeAttributesByUcid(connectedClient3.getUcid()),
                containsInAnyOrder(
                        CONNECTION_ATTRIBUTE_NAME_PAYOUT_ID,
                        CONNECTION_ATTRIBUTE_NAME_IP_ADDRESS,
                        CONNECTION_ATTRIBUTE_NAME_DIGITAL,
                        CONNECTION_ATTRIBUTE_NAME_DOCUMENT_NUMBER,
                        CONNECTION_ATTRIBUTE_NAME_PHONE_NUMBER,
                        CONNECTION_ATTRIBUTE_NAME_EMAIL_ADDRESS,
                        CONNECTION_ATTRIBUTE_NAME_NAME_BIRTH,
                        CONNECTION_ATTRIBUTE_NAME_SESSION,
                        CONNECTION_ATTRIBUTE_NAME_DEVICE));
    }

    @Test
    @AllureId("311")
    @DisplayName("Verify data in card")
    void connectionSearchTest3() {
        connectionPage.clickNodeByUcid(connectedClient3.getUcid());
        assertThat(
                connectionPage.getCardClientName(),
                is(String.format("%s %s", connectedClient3.getFirstName(), connectedClient3.getLastName())));
        assertThat(connectionPage.isCardShowHiddenButtonVisible(), is(true));
        assertThat(connectionPage.isCardOpenInNewTabButtonVisible(), is(true));
        assertThat(
                connectionPage.getCardOpenInNewTabUrl(),
                is(String.format(
                        "https://backoffice.vindex-test.risk-vantagefx.com/investigation/%s",
                        connectedClient3.getUcid())));
        assertThat(
                connectionPage.getCardClientId(),
                is(connectedClient3.getUserId().toString()));
        assertThat(connectionPage.getCardConnectionLevel(), is("2 level"));
        assertThat(connectionPage.getCardConnectionScore(), is("1 to initial client"));
        assertThat(connectionPage.isCardBrandIconVisible(), is(true));
        assertThat(connectionPage.getCardBrand(), is(connectedClient3.getBrand()));
        assertThat(connectionPage.isCardCountryIconVisible(), is(true));
        assertThat(connectionPage.getCardCountry(), is(client.getCountry()));
        assertThat(connectionPage.getCardEmail(), is("t***4@example.com"));
        assertThat(connectionPage.getCardCpa().trim(), is(connectedCrmTbUser3.cpaId.toString()));
        assertThat(
                connectionPage.getCardIb().trim(),
                is(relation.getDirectIbRebateAccount().toString()));
        assertThat(connectionPage.getCardRegistered(), is(connectedCrmTbUser3.registrationDate));
        assertThat(
                connectionPage.getCardLastLogin(),
                is(trade.getCloseTime().substring(0, trade.getCloseTime().length() - 3)));
        assertThat(connectionPage.getCardTrading(), is("1 closed deal"));
        assertThat(
                connectionPage.getCardTotalPnl(), is(String.format("%s USD", formatter.format(trade.getProfitUsd()))));
        assertThat(
                connectionPage.getCardDeposit(), is(String.format("%s USD", formatter.format(deposit.getAmountUsd()))));
        assertThat(connectionPage.getCardWithdrawal(), is("0"));
        assertThat(connectionPage.getCardFraud(), is(POTENTIAL_ABUSE));
        assertThat(connectionPage.getDirectConnectionsAmount(), is("2"));
        String client5Name = String.format("%s %s", connectedClient5.getFirstName(), connectedClient5.getLastName());
        assertThat(connectionPage.getDirectConnectionType(client5Name), is(CONNECTION_TYPE_SAME_PERSON));
        assertThat(connectionPage.getDirectConnectionScore(client5Name), is("1"));
        assertThat(connectionPage.getDirectConnectionPayoutId(client5Name), is(CONNECTION_SEARCH_DATA_CARD_NUMBER));
        assertThat(connectionPage.getDirectConnectionIpAddress(client5Name), is(CONNECTION_SEARCH_DATA_IP1));
        assertThat(connectionPage.getDirectConnectionDevice(client5Name), is(CONNECTION_SEARCH_DATA_DEVICE));
        assertThat(connectionPage.getDirectConnectionNameBirth(client5Name), is(CONNECTION_SEARCH_DATA_NAME_BIRTH));
        assertThat(connectionPage.getDirectConnectionSession(client5Name), is(CONNECTION_SEARCH_DATA_SESSION));
        assertThat(connectionPage.getDirectConnectionDigital(client5Name), is(CONNECTION_SEARCH_DATA_DIGITAL));
        assertThat(
                connectionPage.getDirectConnectionDocumentNumber(client5Name),
                is(CONNECTION_SEARCH_DATA_DOCUMENT_HIDDEN));
        assertThat(
                connectionPage.getDirectConnectionEmailAddress(client5Name), is(CONNECTION_SEARCH_DATA_EMAIL_HIDDEN));
        assertThat(connectionPage.getDirectConnectionPhoneNumber(client5Name), is(CONNECTION_SEARCH_DATA_PHONE_HIDDEN));
        assertThat(connectionPage.getDirectConnectionFraud(client5Name), is(GAP_TRADING.getName()));
        String client1Name = String.format("%s %s", connectedClient1.getFirstName(), connectedClient1.getLastName());
        assertThat(connectionPage.getDirectConnectionType(client1Name), is(CONNECTION_TYPE_SAME_PERSON));
        assertThat(connectionPage.getDirectConnectionScore(client1Name), is("1"));
        assertThat(connectionPage.getDirectConnectionPayoutId(client1Name), is(CONNECTION_SEARCH_DATA_CARD_NUMBER));
    }

    @Test
    @AllureId("518")
    @DisplayName("Verify CPA and IB overview can be opened from the card")
    void connectionSearchTest4() {
        connectionPage.clickNodeByUcid(connectedClient3.getUcid());
        connectionPage.clickCardCpa();
        assertThat(ibCpaOverviewPage.getOverviewTitle(), is("CPA overview"));
        ibCpaOverviewPage.clickCloseDrawerButton();
        connectionPage.clickCardIb();
        assertThat(ibCpaOverviewPage.getOverviewTitle(), is("IB overview"));
    }

    @Test
    @AllureId("526")
    @DisplayName("Verify zoom functionality")
    void connectionSearchTest5() {
        assertThat(connectionPage.getZoomValue(), is("60%"));
        connectionPage.clickZoomInButton();
        assertThat(connectionPage.getZoomValue(), is("70%"));
        connectionPage.clickZoomValue();
        assertThat(
                connectionPage.getZoomPresetOptions(),
                containsInAnyOrder("Zoom to fit", "Zoom to 50%", "Zoom to 100%"));
    }

    @Test
    @AllureId("520")
    @DisplayName("Verify connection attribute card")
    void connectionSearchTest6() {
        connectionPage.clickExpandNodeByUcid(connectedClient3.getUcid());
        connectionPage.clickNodeAttributeByName(connectedClient3.getUcid(), CONNECTION_ATTRIBUTE_NAME_PAYOUT_ID);
        assertThat(connectionPage.getConnectionCardAttributeName(), is(CONNECTION_ATTRIBUTE_NAME_PAYOUT_ID));
        assertThat(
                connectionPage.getConnectionCardAttributeClient(),
                is(String.format("%s %s", connectedClient3.getFirstName(), connectedClient3.getLastName())));
        assertThat(connectionPage.getConnectionCardAttributeValue(), is(CONNECTION_SEARCH_DATA_CARD_NUMBER));
        String client5Name = String.format("%s %s", connectedClient5.getFirstName(), connectedClient5.getLastName());
        assertThat(connectionPage.getConnectionMatch(client5Name), is(CONNECTION_TYPE_RELATION_TYPE_EXACT));
        assertThat(connectionPage.getConnectionValue(client5Name), is(CONNECTION_SEARCH_DATA_CARD_NUMBER));
        assertThat(connectionPage.getConnectionFraud(client5Name), is(GAP_TRADING.getName()));
        String client1Name = String.format("%s %s", connectedClient1.getFirstName(), connectedClient1.getLastName());
        assertThat(connectionPage.getConnectionMatch(client1Name), is(CONNECTION_TYPE_RELATION_TYPE_EXACT));
        assertThat(connectionPage.getConnectionValue(client1Name), is(CONNECTION_SEARCH_DATA_CARD_NUMBER));
    }

    @Test
    @AllureId("521")
    @DisplayName("Verify connection table")
    void connectionSearchTest7() {
        investigationPage.navigateToClient(connectedClient6.getUcid());
        resolvePage.openResolveSuspicious();
        connectionPage.navigate(client.getUcid());

        connectionPage.openConnectionTable();
        connectionPage.verifyConnectionTableIsRendered();
        connectionPage.openConnectionTable();
        var connectionScore = "1";
        List<String> row1Data = List.of(
                "2",
                String.format("%s %s", connectedClient4.getFirstName(), connectedClient4.getLastName()),
                connectedClient4.getUserId().toString(),
                CONNECTION_TYPE_INDIRECT,
                connectionScore,
                CONNECTION_ATTRIBUTE_NAME_PAYOUT_ID,
                CONNECTION_SEARCH_DATA_CARD_NUMBER,
                "",
                String.format("%s %s", POTENTIAL.getDisplayName(), MARKET_MANIPULATION.getName()),
                String.format("CPA %s", connectedClient4.getCpaId()),
                connectedCrmTbUser4.registrationDate);
        List<String> row2Data = List.of(
                "3",
                String.format("%s %s", connectedClient5.getFirstName(), connectedClient5.getLastName()),
                connectedClient5.getUserId().toString(),
                CONNECTION_TYPE_INDIRECT,
                connectionScore,
                CONNECTION_ATTRIBUTE_NAME_DIGITAL,
                CONNECTION_SEARCH_DATA_DIGITAL,
                CONNECTION_ATTRIBUTE_NAME_EMAIL_ADDRESS,
                CONNECTION_SEARCH_DATA_EMAIL_HIDDEN,
                CONNECTION_ATTRIBUTE_NAME_SESSION,
                CONNECTION_SEARCH_DATA_SESSION,
                CONNECTION_ATTRIBUTE_NAME_NAME_BIRTH,
                CONNECTION_SEARCH_DATA_NAME_BIRTH,
                CONNECTION_ATTRIBUTE_NAME_PAYOUT_ID,
                CONNECTION_SEARCH_DATA_CARD_NUMBER,
                CONNECTION_ATTRIBUTE_NAME_DEVICE,
                CONNECTION_SEARCH_DATA_DEVICE,
                CONNECTION_ATTRIBUTE_NAME_DOCUMENT_NUMBER,
                CONNECTION_SEARCH_DATA_DOCUMENT_HIDDEN,
                CONNECTION_ATTRIBUTE_NAME_IP_ADDRESS,
                CONNECTION_SEARCH_DATA_IP1,
                CONNECTION_ATTRIBUTE_NAME_PHONE_NUMBER,
                CONNECTION_SEARCH_DATA_PHONE_HIDDEN,
                "",
                GAP_TRADING.getName(),
                String.format("CPA %s", connectedClient5.getCpaId()),
                connectedCrmTbUser5.registrationDate);
        List<String> row3Data = List.of(
                "1",
                String.format("%s %s", connectedClient2.getFirstName(), connectedClient2.getLastName()),
                connectedClient2.getUserId().toString(),
                CONNECTION_TYPE_SAME_PERSON,
                connectionScore,
                CONNECTION_ATTRIBUTE_NAME_PAYOUT_ID,
                CONNECTION_SEARCH_DATA_CARD_NUMBER,
                "",
                STATUS_SUSPICIOUS,
                String.format("CPA %s", connectedClient2.getCpaId()),
                connectedCrmTbUser2.registrationDate);
        List<String> row4Data = List.of(
                "1",
                String.format("%s %s", connectedClient1.getFirstName(), connectedClient1.getLastName()),
                connectedClient1.getUserId().toString(),
                CONNECTION_TYPE_SAME_PERSON,
                connectionScore,
                CONNECTION_ATTRIBUTE_NAME_PAYOUT_ID,
                CONNECTION_SEARCH_DATA_CARD_NUMBER,
                "",
                STATUS_NORMAL,
                String.format("CPA %s", connectedClient1.getCpaId()),
                connectedCrmTbUser1.registrationDate);
        List<String> row5Data = List.of(
                "2",
                String.format("%s %s", connectedClient3.getFirstName(), connectedClient3.getLastName()),
                connectedClient3.getUserId().toString(),
                CONNECTION_TYPE_INDIRECT,
                connectionScore,
                CONNECTION_ATTRIBUTE_NAME_PAYOUT_ID,
                CONNECTION_SEARCH_DATA_CARD_NUMBER,
                "",
                POTENTIAL_ABUSE,
                formatter.format(trade.getProfitUsd()),
                "1 deal",
                String.format("+%s", formatter.format(deposit.getAmountUsd())),
                String.format("CPA %s", connectedCrmTbUser3.cpaId),
                String.format("IB %s", relation.getDirectIbRebateAccount()),
                connectedCrmTbUser3.registrationDate,
                trade.getCloseTime().split(" ")[0],
                trade.getCloseTime().split(" ")[1].substring(0, 5));
        List<String> row6Data = List.of(
                "3",
                String.format("%s %s", connectedClient6.getFirstName(), connectedClient6.getLastName()),
                connectedClient6.getUserId().toString(),
                CONNECTION_TYPE_INDIRECT,
                connectionScore,
                CONNECTION_ATTRIBUTE_NAME_DIGITAL,
                CONNECTION_SEARCH_DATA_DIGITAL,
                CONNECTION_ATTRIBUTE_NAME_EMAIL_ADDRESS,
                CONNECTION_SEARCH_DATA_EMAIL_HIDDEN,
                CONNECTION_ATTRIBUTE_NAME_SESSION,
                CONNECTION_SEARCH_DATA_SESSION,
                CONNECTION_ATTRIBUTE_NAME_NAME_BIRTH,
                CONNECTION_SEARCH_DATA_NAME_BIRTH,
                CONNECTION_ATTRIBUTE_NAME_PAYOUT_ID,
                CONNECTION_SEARCH_DATA_CARD_NUMBER,
                CONNECTION_ATTRIBUTE_NAME_DEVICE,
                CONNECTION_SEARCH_DATA_DEVICE,
                CONNECTION_ATTRIBUTE_NAME_DOCUMENT_NUMBER,
                CONNECTION_SEARCH_DATA_DOCUMENT_HIDDEN,
                CONNECTION_ATTRIBUTE_NAME_IP_ADDRESS,
                CONNECTION_SEARCH_DATA_IP1,
                CONNECTION_ATTRIBUTE_NAME_PHONE_NUMBER,
                CONNECTION_SEARCH_DATA_PHONE_HIDDEN,
                "",
                UNDER_INVESTIGATION.getDisplayName(),
                String.format("CPA %s", connectedClient6.getCpaId()),
                connectedCrmTbUser6.registrationDate,
                "backoffice-test backoffice-test");
        assertThat(
                connectionPage.getConnectionTableDataByRows(),
                contains(
                        is(row1Data),
                        containsInAnyOrder(row2Data.toArray()),
                        is(row3Data),
                        containsInAnyOrder(row6Data.toArray()),
                        is(row4Data),
                        is(row5Data)));
    }

    @Test
    @AllureId("527")
    @DisplayName("Verify CPA and IB overview can be opened from the table")
    void connectionSearchTest8() {
        connectionPage.openConnectionTable();
        connectionPage.clickTableCpa(connectedCrmTbUser3.cpaId.toString());
        assertThat(ibCpaOverviewPage.getOverviewTitle(), is("CPA overview"));
        ibCpaOverviewPage.clickCloseDrawerButton();
        connectionPage.clickTableIb(relation.getDirectIbRebateAccount().toString());
        assertThat(ibCpaOverviewPage.getOverviewTitle(), is("IB overview"));
    }
}
