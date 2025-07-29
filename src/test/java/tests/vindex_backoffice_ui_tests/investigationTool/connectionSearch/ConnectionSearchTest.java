package tests.vindex_backoffice_ui_tests.investigationTool.connectionSearch;

import business_objects.api.mitigation_service.PostRestrictionRequestBody;
import business_objects.db.clickhouse.account_ib_relation.AccountIbRelationObject;
import business_objects.db.clickhouse.connection_table.ConnectionTableEntry;
import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalObject;
import business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObject;
import business_objects.kafka.alerts.RuleAlert;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.data.ClientHelper;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.text.DecimalFormat;
import java.util.List;

import static business_objects.api.mitigation_service.MitigationServiceRequest.postRestriction;
import static business_objects.db.clickhouse.account_ib_relation.AccountIbRelationFactory.generateAccountIbRelationObjectByClient;
import static business_objects.db.clickhouse.connection_table.ConnectionTableEntryFactory.*;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositObjectFactory.generateDepositByClient;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserWithUcidFirstName;
import static business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalObjectFactory.generateWithdrawalByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObjectFactory.generateMt4TradesCoercedForConnectionSearch;
import static business_objects.kafka.alerts.RuleAlertFactory.generateRuleAlertByUcid;
import static helpers.api.AbuseRegistryHelper.addFraudsForClient;
import static helpers.api.AbuseRegistryHelper.setClientStatus;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.FraudType.*;
import static helpers.data.enums.FraudTypeStatus.CONFIRMED;
import static helpers.data.enums.FraudTypeStatus.POTENTIAL;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.BoHelper.deleteUserAR;
import static helpers.database.CleanTableHelper.cleanUserRestrictionGeneral;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.*;

class ConnectionSearchTest extends TestBaseWeb {


    private static final KafkaHelper kafka = new KafkaHelper();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final ClientHelper connectedClient1 = getRandomVantageClientAllFields(); // lvl 1 normal
    private static final ClientHelper connectedClient2 = getRandomVantageClientAllFields(); // lvl 1 suspicious
    private static final ClientHelper connectedClient3 = getRandomVantageClientAllFields(); // lvl 2 potential status client
    private static final ClientHelper connectedClient4 = getRandomVantageClientAllFields(); // lvl 2 potential with abuse
    private static final ClientHelper connectedClient5 = getRandomVantageClientAllFields(); // lvl 3 confirmed fraud
    private static final DecimalFormat formatter = new DecimalFormat("#,###.##");
    private static AccountIbRelationObject relation;
    private static MtMt4TradesCoercedObject trade;
    private static CrmTbUserObject connectedCrmTbUser1;
    private static CrmTbUserObject connectedCrmTbUser2;
    private static CrmTbUserObject connectedCrmTbUser3;
    private static CrmTbUserObject connectedCrmTbUser4;
    private static CrmTbUserObject connectedCrmTbUser5;
    private static CrmTbDepositObject deposit;
    private static CrmTbWithdrawalObject withdrawal;


    @BeforeAll
    static void setup() throws Exception {
        // Users
        CrmTbUserObject crmTbUser = generateUserWithUcidFirstName(client);
        connectedCrmTbUser1 = generateUserWithUcidFirstName(connectedClient1);
        connectedCrmTbUser2 = generateUserWithUcidFirstName(connectedClient2);
        connectedCrmTbUser3 = generateUserWithUcidFirstName(connectedClient3);
        connectedCrmTbUser3.cpaId = getRandomIntPositive();
        connectedCrmTbUser4 = generateUserWithUcidFirstName(connectedClient4);
        connectedCrmTbUser5 = generateUserWithUcidFirstName(connectedClient5);
        insertObjectsToDb(CRM_USER_TABLE_NAME, List.of(crmTbUser, connectedCrmTbUser1, connectedCrmTbUser2, connectedCrmTbUser3, connectedCrmTbUser4, connectedCrmTbUser5));
        // Connections
        ConnectionTableEntry connectionTableEntry1 = getConnectionTableEntry(client, connectedClient1);
        ConnectionTableEntry connectionTableEntry2 = getConnectionTableEntry(client, connectedClient2);
        ConnectionTableEntry connectionTableEntry3 = getConnectionTableEntry(connectedClient1, connectedClient3);
        ConnectionTableEntry connectionTableEntry4 = getConnectionTableEntry(connectedClient2, connectedClient4);
        ConnectionTableEntry connectionTableEntry5 = getConnectionTableEntryForUi(connectedClient3, connectedClient5);
        insertObjectsToDb(CONNECTIONS_TABLE_NAME, List.of(connectionTableEntry1, connectionTableEntry2, connectionTableEntry3, connectionTableEntry4, connectionTableEntry5));
        // Frauds
        setClientStatus(connectedClient3, POTENTIAL);
        addFraudsForClient(connectedClient4, List.of(MARKET_MANIPULATION), POTENTIAL);
        addFraudsForClient(connectedClient5, List.of(GAP_TRADING), CONFIRMED);
        // Restrictions
        postRestriction(new PostRestrictionRequestBody(
                connectedClient1.getUcid(), "03", "GENERAL", null, null, "Automation test", new PostRestrictionRequestBody.UpdatedBy("Auto", "Test")
        ));
        // Alerts
        RuleAlert alert = generateRuleAlertByUcid(connectedClient2.getUcid());
        kafka.produceMessage(alert.alertId, objectMapper.writeValueAsString(alert), KAFKA_TOPIC_ALERTS);
        // Other data
        // PNL related
        CrmTbAccountObject account = generateCrmTbAccountDataForUi(connectedClient3);
        insertObjectToDb(CRM_TB_ACCOUNT_TABLE_NAME, account);
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
        deposit = generateDepositByClient(connectedClient3);
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, deposit);
        withdrawal = generateWithdrawalByClient(connectedClient3);
        insertObjectToDb(CRM_WITHDRAWAL_TABLE_NAME, withdrawal);
        waitForConnectionSearchToUpdate(client);
    }

    @AfterAll
    static void tearDown() throws Exception {
        deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("ucid IN ('%s', '%s', '%s', '%s', '%s', '%s')", client.getUcid(), connectedClient1.getUcid(), connectedClient2.getUcid(), connectedClient3.getUcid(), connectedClient4.getUcid(), connectedClient5.getUcid()));
        deleteEntryFromDb(CONNECTIONS_TABLE_NAME, String.format("user_from IN ('%s', '%s', '%s', '%s')", client.getUcid(), connectedClient1.getUcid(), connectedClient2.getUcid(), connectedClient3.getUcid()));
        deleteEntryFromDb(MT4_TRADES_COERCED_TABLE_NAME, String.format("ucid IN ('%s')", connectedClient3.getUcid()));
        deleteUserAR(connectedClient3.getUcid(), connectedClient4.getUcid(), connectedClient5.getUcid());
        cleanUserRestrictionGeneral(connectedClient1.getUcid());
        closeAlert(client.getUcid());
        closeAlert(connectedClient2.getUcid());
        deleteEntryFromDb(ACCOUNT_IB_RELATION_TABLE_NAME, String.format("ucid = '%s'", connectedClient3.getUcid()));
        deleteEntryFromDb(CRM_DEPOSIT_TABLE_NAME, String.format("ucid = '%s'", connectedClient3.getUcid()));
        deleteEntryFromDb(CRM_WITHDRAWAL_TABLE_NAME, String.format("ucid = '%s'", connectedClient3.getUcid()));
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
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("315")
    @DisplayName("Verify connection search ucids, statuses, order")
    void connectionSearchTest1() {
        // Statuses
        String statusReason = "Verify status shown in the node";
        assertThat(statusReason, connectionPage.getStatusByNodeTitle(client.getUcid()), is(STATUS_NORMAL));
        assertThat(statusReason, connectionPage.getStatusByNodeTitle(connectedClient1.getUcid()), is(STATUS_NORMAL));
        assertThat(statusReason, connectionPage.getStatusByNodeTitle(connectedClient2.getUcid()), is(STATUS_SUSPICIOUS));
        assertThat(statusReason, connectionPage.getStatusByNodeTitle(connectedClient3.getUcid()), is(POTENTIAL_ABUSE));
        assertThat(statusReason, connectionPage.getStatusByNodeTitle(connectedClient4.getUcid()), is(String.format("%s %s", POTENTIAL.getDisplayName(), MARKET_MANIPULATION.getName())));
        assertThat(statusReason, connectionPage.getStatusByNodeTitle(connectedClient5.getUcid()), is(GAP_TRADING.getName()));
        // Order
        String orderReason = "Verify order of the node in the graph";
        assertThat(orderReason, connectionPage.getOrderByNodeTitle(client.getUcid()), is("0"));
        assertThat(orderReason, connectionPage.getOrderByNodeTitle(connectedClient1.getUcid()), anyOf(is("1"), is("2")));
        assertThat(orderReason, connectionPage.getOrderByNodeTitle(connectedClient2.getUcid()), anyOf(is("1"), is("2")));
        assertThat(orderReason, connectionPage.getOrderByNodeTitle(connectedClient3.getUcid()), anyOf(is("3"), is("4")));
        assertThat(orderReason, connectionPage.getOrderByNodeTitle(connectedClient4.getUcid()), anyOf(is("3"), is("4")));
        assertThat(orderReason, connectionPage.getOrderByNodeTitle(connectedClient5.getUcid()), is("5"));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("311")
    @DisplayName("Verify connection attributes in node")
    void connectionSearchTest2() {
        connectionPage.clickExpandNodeByTitle(connectedClient3.getUcid());
        assertThat(connectionPage.getNodeAttributesByTitle(connectedClient3.getUcid()), containsInAnyOrder(CONNECTION_ATTRIBUTE_NAME_PAYOUT_ID, CONNECTION_ATTRIBUTE_NAME_IP_ADDRESS, CONNECTION_ATTRIBUTE_NAME_DIGITAL, CONNECTION_ATTRIBUTE_NAME_DOCUMENT_NUMBER, CONNECTION_ATTRIBUTE_NAME_PHONE_NUMBER, CONNECTION_ATTRIBUTE_NAME_EMAIL_ADDRESS, CONNECTION_ATTRIBUTE_NAME_NAME_BIRTH, CONNECTION_ATTRIBUTE_NAME_SESSION, CONNECTION_ATTRIBUTE_NAME_DEVICE));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("311")
    @DisplayName("Verify data in card")
    void connectionSearchTest3() {
        connectionPage.clickNodeByTitle(connectedClient3.getUcid());
        assertThat(connectionPage.getCardClientName(), is(String.format("%s ", connectedClient3.getUcid())));
        assertThat(connectionPage.isCardShowHiddenButtonVisible(), is(true));
        assertThat(connectionPage.isCardOpenInNewTabButtonVisible(), is(true));
        assertThat(connectionPage.getCardOpenInNewTabUrl(), is(String.format("https://backoffice.vindex-test.risk-vantagefx.com/investigation/%s", connectedClient3.getUcid())));
        assertThat(connectionPage.getCardClientId(), is(connectedClient3.getUserId().toString()));
        assertThat(connectionPage.getCardConnectionLevel(), is("2 level"));
        assertThat(connectionPage.getCardConnectionScore(), is("1 to initial client"));
        assertThat(connectionPage.isCardBrandIconVisible(), is(true));
        assertThat(connectionPage.getCardBrand(), is(connectedClient3.getBrand()));
        assertThat(connectionPage.isCardCountryIconVisible(), is(true));
        assertThat(connectionPage.getCardCountry(), is("Cyprus"));
        assertThat(connectionPage.getCardEmail(), is("t***4@example.com"));
        assertThat(connectionPage.getCardCpa().trim(), is(connectedCrmTbUser3.cpaId.toString()));
        assertThat(connectionPage.getCardIb().trim(), is(relation.getDirectIbRebateAccount().toString()));
        assertThat(connectionPage.getCardRegistered(), is(connectedCrmTbUser3.registrationDate));
        assertThat(connectionPage.getCardLastLogin(), is(trade.closeTime.substring(0, trade.closeTime.length() - 3)));
        assertThat(connectionPage.getCardTrading(), is("1 closed deal"));
        assertThat(connectionPage.getCardTotalPnl(), is(String.format("%s USD", formatter.format(trade.profitUsd))));
        assertThat(connectionPage.getCardDeposit(), is(String.format("%s USD", formatter.format(deposit.amountUsd))));
        assertThat(connectionPage.getCardWithdrawal(), is(String.format("%s USD", formatter.format(withdrawal.amountUsd - withdrawal.reversedAmountUsd))));
        assertThat(connectionPage.getCardFraud(), is(POTENTIAL_ABUSE));
        assertThat(connectionPage.getDirectConnectionsAmount(), is("2"));
        assertThat(connectionPage.getDirectConnectionType(connectedClient5.getUcid()), is(CONNECTION_TYPE_SAME_PERSON));
        assertThat(connectionPage.getDirectConnectionScore(connectedClient5.getUcid()), is("1"));
        assertThat(connectionPage.getDirectConnectionPayoutId(connectedClient5.getUcid()), is(CONNECTION_SEARCH_DATA_CARD_NUMBER));
        assertThat(connectionPage.getDirectConnectionIpAddress(connectedClient5.getUcid()), is(CONNECTION_SEARCH_DATA_IP1));
        assertThat(connectionPage.getDirectConnectionDevice(connectedClient5.getUcid()), is(CONNECTION_SEARCH_DATA_DEVICE));
        assertThat(connectionPage.getDirectConnectionNameBirth(connectedClient5.getUcid()), is(CONNECTION_SEARCH_DATA_NAME_BIRTH));
        assertThat(connectionPage.getDirectConnectionSession(connectedClient5.getUcid()), is(CONNECTION_SEARCH_DATA_SESSION));
        assertThat(connectionPage.getDirectConnectionDigital(connectedClient5.getUcid()), is(CONNECTION_SEARCH_DATA_DIGITAL));
        assertThat(connectionPage.getDirectConnectionDocumentNumber(connectedClient5.getUcid()), is(CONNECTION_SEARCH_DATA_DOCUMENT_HIDDEN));
        assertThat(connectionPage.getDirectConnectionEmailAddress(connectedClient5.getUcid()), is(CONNECTION_SEARCH_DATA_EMAIL_HIDDEN));
        assertThat(connectionPage.getDirectConnectionPhoneNumber(connectedClient5.getUcid()), is(CONNECTION_SEARCH_DATA_PHONE_HIDDEN));
        assertThat(connectionPage.getDirectConnectionFraud(connectedClient5.getUcid()), is(GAP_TRADING.getName()));
        assertThat(connectionPage.getDirectConnectionType(connectedClient1.getUcid()), is(CONNECTION_TYPE_SAME_PERSON));
        assertThat(connectionPage.getDirectConnectionScore(connectedClient1.getUcid()), is("1"));
        assertThat(connectionPage.getDirectConnectionPayoutId(connectedClient1.getUcid()), is(CONNECTION_SEARCH_DATA_CARD_NUMBER));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("518")
    @DisplayName("Verify CPA and IB overview can be opened from the card")
    void connectionSearchTest4() {
        connectionPage.clickNodeByTitle(connectedClient3.getUcid());
        connectionPage.clickCardCpa();
        assertThat(ibCpaOverviewPage.getOverviewTitle(), is("CPA overview"));
        ibCpaOverviewPage.clickCloseDrawerButton();
        connectionPage.clickCardIb();
        assertThat(ibCpaOverviewPage.getOverviewTitle(), is("IB overview"));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("526")
    @DisplayName("Verify zoom functionality")
    void connectionSearchTest5() {
        assertThat(connectionPage.getZoomValue(), is("56%"));
        connectionPage.clickZoomInButton();
        assertThat(connectionPage.getZoomValue(), is("60%"));
        connectionPage.clickZoomValue();
        assertThat(connectionPage.getZoomPresetOptions(), containsInAnyOrder("Zoom to fit", "Zoom to 50%", "Zoom to 100%"));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("520")
    @DisplayName("Verify connection attribute card")
    void connectionSearchTest6() {
        connectionPage.clickExpandNodeByTitle(connectedClient3.getUcid());
        connectionPage.clickNodeAttributeByName(connectedClient3.getUcid(), CONNECTION_ATTRIBUTE_NAME_PAYOUT_ID);
        assertThat(connectionPage.getConnectionCardAttributeName(), is(CONNECTION_ATTRIBUTE_NAME_PAYOUT_ID));
        assertThat(connectionPage.getConnectionCardAttributeClient(), containsString(connectedClient3.getUcid()));
        assertThat(connectionPage.getConnectionCardAttributeValue(), is(CONNECTION_SEARCH_DATA_CARD_NUMBER));
        assertThat(connectionPage.getConnectionMatch(connectedClient5.getUcid()), is(CONNECTION_TYPE_RELATION_TYPE_EXACT));
        assertThat(connectionPage.getConnectionValue(connectedClient5.getUcid()), is(CONNECTION_SEARCH_DATA_CARD_NUMBER));
        assertThat(connectionPage.getConnectionFraud(connectedClient5.getUcid()), is(GAP_TRADING.getName()));
        assertThat(connectionPage.getConnectionMatch(connectedClient1.getUcid()), is(CONNECTION_TYPE_RELATION_TYPE_EXACT));
        assertThat(connectionPage.getConnectionValue(connectedClient1.getUcid()), is(CONNECTION_SEARCH_DATA_CARD_NUMBER));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("521")
    @DisplayName("Verify connection table")
    void connectionSearchTest7() {
        connectionPage.openConnectionTable();
        connectionPage.verifyConnectionTableIsRendered();
        connectionPage.openConnectionTable();
        String connectionScore = "1";
        List<String> row1Data = List.of("2", String.format("%s ", connectedClient4.getUcid()), connectedClient4.getUserId().toString(), CONNECTION_TYPE_INDIRECT, connectionScore, CONNECTION_ATTRIBUTE_NAME_PAYOUT_ID, CONNECTION_SEARCH_DATA_CARD_NUMBER, "", String.format("%s %s", POTENTIAL.getDisplayName(), MARKET_MANIPULATION.getName()), String.format("CPA %s", connectedClient4.getCpaId()), connectedCrmTbUser4.registrationDate);
        List<String> row2Data = List.of("3", String.format("%s ", connectedClient5.getUcid()), connectedClient5.getUserId().toString(), CONNECTION_TYPE_INDIRECT, connectionScore, CONNECTION_ATTRIBUTE_NAME_DIGITAL, CONNECTION_SEARCH_DATA_DIGITAL, CONNECTION_ATTRIBUTE_NAME_EMAIL_ADDRESS, CONNECTION_SEARCH_DATA_EMAIL_HIDDEN, CONNECTION_ATTRIBUTE_NAME_SESSION, CONNECTION_SEARCH_DATA_SESSION, CONNECTION_ATTRIBUTE_NAME_NAME_BIRTH, CONNECTION_SEARCH_DATA_NAME_BIRTH, CONNECTION_ATTRIBUTE_NAME_PAYOUT_ID, CONNECTION_SEARCH_DATA_CARD_NUMBER, CONNECTION_ATTRIBUTE_NAME_DEVICE, CONNECTION_SEARCH_DATA_DEVICE, CONNECTION_ATTRIBUTE_NAME_DOCUMENT_NUMBER, CONNECTION_SEARCH_DATA_DOCUMENT_HIDDEN, CONNECTION_ATTRIBUTE_NAME_IP_ADDRESS, CONNECTION_SEARCH_DATA_IP1, CONNECTION_ATTRIBUTE_NAME_PHONE_NUMBER, CONNECTION_SEARCH_DATA_PHONE_HIDDEN, "", GAP_TRADING.getName(), String.format("CPA %s", connectedClient5.getCpaId()), connectedCrmTbUser5.registrationDate);
        List<String> row3Data = List.of("1", String.format("%s ", connectedClient2.getUcid()), connectedClient2.getUserId().toString(), CONNECTION_TYPE_SAME_PERSON, connectionScore, CONNECTION_ATTRIBUTE_NAME_PAYOUT_ID, CONNECTION_SEARCH_DATA_CARD_NUMBER, "", STATUS_SUSPICIOUS, String.format("CPA %s", connectedClient2.getCpaId()), connectedCrmTbUser2.registrationDate);
        List<String> row4Data = List.of("1", String.format("%s ", connectedClient1.getUcid()), connectedClient1.getUserId().toString(), CONNECTION_TYPE_SAME_PERSON, connectionScore, CONNECTION_ATTRIBUTE_NAME_PAYOUT_ID, CONNECTION_SEARCH_DATA_CARD_NUMBER, "", STATUS_NORMAL, String.format("CPA %s", connectedClient1.getCpaId()), connectedCrmTbUser1.registrationDate);
        List<String> row5Data = List.of("2", String.format("%s ", connectedClient3.getUcid()), connectedClient3.getUserId().toString(), CONNECTION_TYPE_INDIRECT, connectionScore, CONNECTION_ATTRIBUTE_NAME_PAYOUT_ID, CONNECTION_SEARCH_DATA_CARD_NUMBER, "", POTENTIAL_ABUSE, formatter.format(trade.profitUsd), "1 deal", String.format("+%s", formatter.format(deposit.amountUsd)), String.format("-%s", formatter.format(withdrawal.amountUsd - withdrawal.reversedAmountUsd)), String.format("CPA %s", connectedCrmTbUser3.cpaId), String.format("IB %s", relation.getDirectIbRebateAccount()), connectedCrmTbUser3.registrationDate, trade.closeTime.split(" ")[0], trade.closeTime.split(" ")[1].substring(0, 5));
        assertThat(connectionPage.getConnectionTableDataByRows(), contains(is(row1Data), containsInAnyOrder(row2Data.toArray()), is(row3Data), is(row4Data), is(row5Data)));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
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
