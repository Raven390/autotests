package helpers.data.rules.cpa_abuse_rule;

import business_objects.db.clickhouse.client_fraud_types.ClientFraudTypes;
import business_objects.db.clickhouse.connection_table.ConnectionTableEntry;
import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;

import business_objects.db.clickhouse.mirror_ucid_table.MirrorUcidObject;
import business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedObject;
import business_objects.kafka.crm_events.EgWithdrawalEvent;
import generator.annotations.RuleTestData;
import helpers.data.ClientHelper;
import helpers.data.enums.FraudTypeOld;
import helpers.data.rules.RuleDataHelper;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountData;
import static business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositObjectFactory.generateDepositByClient;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClients;
import static business_objects.db.clickhouse.mirror_ucid_table.MirrorUcidObjectFactory.generateMirrorUcidObjectByClient;
import static business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedFactory.generateTradeByClient;
import static helpers.data.ClientFactory.*;
import static helpers.data.enums.DateTimeFormat.DATE_AND_TIME;
import static helpers.data.rules.RuleDataHelper.deleteRuleData;
import static helpers.data.rules.RuleDataHelper.setupRuleData;
import static helpers.database.DbHelper.*;
import static utils.Constants.*;
import static utils.Utils.*;

@RuleTestData("cpa-abuse")
public class CpaAbuseRuleDataFactory {
    private static final ClientHelper cpaAbuseRuleExitEventEnd1Client = getRandomVantageClientNoCpaIbRef();
    private static final ClientHelper cpaAbuseRuleExitEventEnd2Client = getRandomVantageClientNoCpaIbRef();
    private static final ClientHelper cpaAbuseRuleExitEventEnd3Client = getRandomVantageClientNoCpaIbRef();
    private static final ClientHelper cpaAbuseRuleExitEventEnd4Client = getRandomVantageClientAllFields();
    private static final ClientHelper cpaAbuseRuleExitEventEnd5p1Client = getRandomVantageClientAllFields();
    private static final ClientHelper cpaAbuseRuleExitEventEnd5p2Client = getRandomVantageClientAllFields();
    private static final ClientHelper cpaAbuseRuleExitEventEnd6Client = getRandomVantageClientAllFields();
    private static final ClientHelper cpaAbuseRuleExitEventEnd7p1Client = getRandomVantageClientAllFields();
    private static final ClientHelper cpaAbuseRuleExitEventEnd7p2Client = getRandomVantageClientAllFields();
    private static final ClientHelper cpaAbuseRuleExitEventEnd8p1Client = getRandomVantageClientAllFields();
    private static final ClientHelper cpaAbuseRuleExitEventEnd8p2Client = getRandomVantageClientAllFields();
    private static final ClientHelper cpaAbuseRuleExitEventEnd9p1Client = getRandomVantageClientAllFields();
    private static final ClientHelper cpaAbuseRuleExitEventEnd9p2Client = getRandomVantageClientAllFields();

    @Step("Create data for Mirror trading rule")
    private static RuleDataHelper getCpaAbuseRuleData(ClientHelper client) {
        RuleDataHelper ruleData = new RuleDataHelper();
        ruleData.crmTbUserObject = generateUserByClient(client);
        ruleData.crmTbAccountObject = generateCrmTbAccountData(client);
        ruleData.withdrawalEvent = new EgWithdrawalEvent(getRandomUuidString(), Instant.now().toString(), getRandomIntPositive(), client.getUserId(), client.getTradingAccount(), client.getBrand(), "vfsc", "FASAPAY", 1, 1d, 1d, 1d, 1d, "555555**** **6666", 1, Instant.now().toString(), "", "", 1, "", 1d, 1, 1, "", 1, 1, 1d, 2, 1d, "egWithdrawal");
        return ruleData;
    }

    private static ConnectionTableEntry getConnection(ClientHelper fromClient, ClientHelper toClient) {
        return new ConnectionTableEntry(
                fromClient.getUcid(), toClient.getUcid(), CONNECTION_TYPE_SAME_IDENTITY, 1d, List.of(
                        new ConnectionTableEntry.ConnectionInfo(
                                CONNECTION_ATTRIBUTE_NAME_PAYOUT, CONNECTION_SEARCH_DATA_CARD_NUMBER, CONNECTION_SEARCH_DATA_CARD_NUMBER, CONNECTION_TYPE_RELATION_TYPE_EXACT
                        )), getCurrentTimestampDbFormat()
        );
    }

    public static RuleDataHelper getCpaAbuseRuleExitEventEnd1Data() {
        Allure.step("Get client data");
        RuleDataHelper data = getCpaAbuseRuleData(cpaAbuseRuleExitEventEnd1Client);
        Allure.step("Create user object with no CPA");
        data.clientHelper.setCpaId(null);
        return data;
    }

    public static RuleDataHelper getCpaAbuseRuleExitEventEnd2Data() {
        Allure.step("Get client data");
        cpaAbuseRuleExitEventEnd2Client.setCpaId(8888);
        RuleDataHelper data = getCpaAbuseRuleData(cpaAbuseRuleExitEventEnd2Client);
        Allure.step("Create user object with no CPA");

        Mt5DealsCoercedObject trade1Close = generateTradeByClient(cpaAbuseRuleExitEventEnd2Client);
        trade1Close.setComment("trade 1 close");
        trade1Close.setTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 60, 0, 1));
        trade1Close.setTimeUtc(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 60, 0, 1));
        trade1Close.setEntry(1);
        trade1Close.setSymbol("USDEUR");
        trade1Close.setProfit(111.11);
        trade1Close.setProfitUsd(123.12);
        trade1Close.setVolumeLots(0.1);
        data.mt5DealsCoercedObjects.add(trade1Close);

        return data;
    }

    public static RuleDataHelper getCpaAbuseRuleExitEventEnd3Data() {
        Allure.step("Get client data");
        int cpa = 8888;
        cpaAbuseRuleExitEventEnd3Client.setCpaId(cpa);
        RuleDataHelper data = getCpaAbuseRuleData(cpaAbuseRuleExitEventEnd3Client);
        Allure.step("Create user object with no CPA");


        //MT5 trade with current time
        Mt5DealsCoercedObject trade1Close = generateTradeByClient(cpaAbuseRuleExitEventEnd3Client);
        trade1Close.setComment("trade 1 close");
        trade1Close.setTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 59, 13, 59));
        trade1Close.setTimeUtc(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 59, 13, 59));
        trade1Close.setEntry(1);
        trade1Close.setSymbol("USDEUR");
        trade1Close.setProfit(111.11);
        trade1Close.setProfitUsd(123.12);
        trade1Close.setVolumeLots(0.1);
        data.mt5DealsCoercedObjects.add(trade1Close);
        data.connectedUsers = (generateUserByClients(getRandomClientWithCpa(500, cpa)));

        return data;
    }

    public static RuleDataHelper getCpaAbuseRuleExitEventEnd4Data() {
        Allure.step("Get client data");
        RuleDataHelper data = getCpaAbuseRuleData(cpaAbuseRuleExitEventEnd4Client);

        Allure.step("Client has cpa abuse connected account");
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        data.connections.add(getConnection(data.clientHelper, connectedClient));
        ClientFraudTypes clientFraudTypes = new ClientFraudTypes(
                connectedClient.getUcid(), FraudTypeOld.CPA_ABUSE.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat()
        );
        CrmTbUserObject connectedUserCrmTbUserObject = generateUserByClient(connectedClient);
        data.connectedUsers.add(connectedUserCrmTbUserObject);

        data.clientFraudTypes.add(clientFraudTypes);

        //MT5 trade with current time
        Mt5DealsCoercedObject trade1Close = generateTradeByClient(cpaAbuseRuleExitEventEnd4Client);
        trade1Close.setComment("trade 1 close");
        trade1Close.setTime(getCurrentTimestampDbFormat());
        trade1Close.setTimeUtc(trade1Close.getTime());
        trade1Close.setEntry(1);
        trade1Close.setSymbol("USDEUR");
        trade1Close.setProfit(111.11);
        trade1Close.setProfitUsd(123.12);
        trade1Close.setVolumeLots(0.1);
        data.mt5DealsCoercedObjects.add(trade1Close);
        Allure.step("Set restriction");
        Allure.step("Send alert");
        return data;
    }

    public static RuleDataHelper getCpaAbuseRuleExitEventEnd5p1Data() {
        Allure.step("Get client data");
        RuleDataHelper data = getCpaAbuseRuleData(cpaAbuseRuleExitEventEnd5p1Client);
        Allure.step("Send alert");

        //MT5 trade with current time
        Mt5DealsCoercedObject trade1Close = generateTradeByClient(cpaAbuseRuleExitEventEnd5p1Client);
        trade1Close.setComment("trade 1 close");
        trade1Close.setTime(getCurrentTimestampDbFormat());
        trade1Close.setTimeUtc(trade1Close.getTime());
        trade1Close.setEntry(1);
        trade1Close.setSymbol("USDEUR");
        trade1Close.setProfit(2500.01);
        trade1Close.setProfitUsd(2500.01);
        trade1Close.setVolumeLots(0.1);
        data.mt5DealsCoercedObjects.add(trade1Close);
        return data;
    }

    public static RuleDataHelper getCpaAbuseRuleExitEventEnd5p2Data() {
        Allure.step("Get client data");
        RuleDataHelper data = getCpaAbuseRuleData(cpaAbuseRuleExitEventEnd5p2Client);
        Allure.step("Send alert");

        //MT5 trade with current time
        Mt5DealsCoercedObject trade1Close = generateTradeByClient(cpaAbuseRuleExitEventEnd5p2Client);
        trade1Close.setComment("trade 1 close");
        trade1Close.setTime(getCurrentTimestampDbFormat());
        trade1Close.setTimeUtc(trade1Close.getTime());
        trade1Close.setEntry(1);
        trade1Close.setSymbol("USDEUR");
        trade1Close.setProfit(-700.01);
        trade1Close.setProfitUsd(-700.01);
        trade1Close.setVolumeLots(0.1);
        data.mt5DealsCoercedObjects.add(trade1Close);
        return data;
    }

    public static RuleDataHelper getCpaAbuseRuleExitEventEnd6Data() {
        Allure.step("Get client data");
        RuleDataHelper data = getCpaAbuseRuleData(cpaAbuseRuleExitEventEnd6Client);
        Allure.step("Client has not cpa abuse connected account");
        Allure.step("At least 70% have any CPA value");
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        ClientHelper connectedClient2 = getRandomVantageClientAllFields();
        ClientHelper connectedClient3 = getRandomVantageClientAllFields();
        ClientHelper connectedClient4 = getRandomVantageClientAllFields();
        CrmTbUserObject connectedUserCrmTbUserObject = generateUserByClient(connectedClient);
        CrmTbUserObject connectedUserCrmTbUserObject2 = generateUserByClient(connectedClient2);
        CrmTbUserObject connectedUserCrmTbUserObject3 = generateUserByClient(connectedClient3);
        CrmTbUserObject connectedUserCrmTbUserObject4 = generateUserByClient(connectedClient4);
        connectedUserCrmTbUserObject.cpaId = data.clientHelper.getCpaId();
        connectedUserCrmTbUserObject2.cpaId = data.clientHelper.getCpaId();
        connectedUserCrmTbUserObject3.cpaId = data.clientHelper.getCpaId();
        connectedUserCrmTbUserObject4.cpaId = 8889;
        data.connectedUsers.add(connectedUserCrmTbUserObject);
        data.connectedUsers.add(connectedUserCrmTbUserObject2);
        data.connectedUsers.add(connectedUserCrmTbUserObject3);
        data.connectedUsers.add(connectedUserCrmTbUserObject4);
        data.connections.add(getConnection(data.clientHelper, connectedClient));
        data.connections.add(getConnection(data.clientHelper, connectedClient2));
        data.connections.add(getConnection(data.clientHelper, connectedClient3));
        data.connections.add(getConnection(data.clientHelper, connectedClient4));
        Allure.step("Set restriction");
        Allure.step("Send alert");

        //MT5 trade with current time
        Mt5DealsCoercedObject trade1Close = generateTradeByClient(cpaAbuseRuleExitEventEnd6Client);
        trade1Close.setComment("trade 1 close");
        trade1Close.setTime(getCurrentTimestampDbFormat());
        trade1Close.setTimeUtc(trade1Close.getTime());
        trade1Close.setEntry(1);
        trade1Close.setSymbol("USDEUR");
        trade1Close.setProfit(2501.01);
        trade1Close.setProfitUsd(2499.99);
        trade1Close.setVolumeLots(0.1);
        data.mt5DealsCoercedObjects.add(trade1Close);

        return data;
    }

    public static RuleDataHelper getCpaAbuseRuleExitEventEnd7p1Data() {
        Allure.step("Get client data");
        RuleDataHelper data = getCpaAbuseRuleData(cpaAbuseRuleExitEventEnd7p1Client);
        Allure.step("Client has not cpa abuse connected account");
        Allure.step("100% have same CPA value");
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        ClientHelper connectedClient2 = getRandomVantageClientAllFields();
        ClientHelper connectedClient3 = getRandomVantageClientAllFields();
        CrmTbUserObject connectedUserCrmTbUserObject = generateUserByClient(connectedClient);
        CrmTbUserObject connectedUserCrmTbUserObject2 = generateUserByClient(connectedClient2);
        CrmTbUserObject connectedUserCrmTbUserObject3 = generateUserByClient(connectedClient3);
        connectedUserCrmTbUserObject.cpaId = data.clientHelper.getCpaId();
        connectedUserCrmTbUserObject2.cpaId = data.clientHelper.getCpaId();
        connectedUserCrmTbUserObject3.cpaId = data.clientHelper.getCpaId();
        data.connectedUsers.add(connectedUserCrmTbUserObject);
        data.connectedUsers.add(connectedUserCrmTbUserObject2);
        data.connectedUsers.add(connectedUserCrmTbUserObject3);
        data.connections.add(getConnection(data.clientHelper, connectedClient));
        data.connections.add(getConnection(data.clientHelper, connectedClient2));
        data.connections.add(getConnection(data.clientHelper, connectedClient3));
        Allure.step("Set restriction");
        Allure.step("Send alert");

        //MT5 trade with current time
        Mt5DealsCoercedObject trade1Close = generateTradeByClient(cpaAbuseRuleExitEventEnd7p1Client);
        trade1Close.setComment("trade 1 close");
        trade1Close.setTime(getCurrentTimestampDbFormat());
        trade1Close.setTimeUtc(trade1Close.getTime());
        trade1Close.setEntry(1);
        trade1Close.setSymbol("USDEUR");
        trade1Close.setProfit(2501.01);
        trade1Close.setProfitUsd(2499.99);
        trade1Close.setVolumeLots(0.1);
        data.mt5DealsCoercedObjects.add(trade1Close);

        CrmTbDepositObject deposit = generateDepositByClient(cpaAbuseRuleExitEventEnd7p1Client);
        deposit.paymentChannel = "Crypto";
        deposit.amountUsd = 500d;
        deposit.setCreateTime(getCurrentTimestampDbFormat());
        data.crmTbDepositObjects.add(deposit);

        return data;
    }

    public static RuleDataHelper getCpaAbuseRuleExitEventEnd7p2Data() {
        Allure.step("Get client data");
        RuleDataHelper data = getCpaAbuseRuleData(cpaAbuseRuleExitEventEnd7p2Client);
        Allure.step("Client has not cpa abuse connected account");
        Allure.step("100% have same CPA value");
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        ClientHelper connectedClient2 = getRandomVantageClientAllFields();
        ClientHelper connectedClient3 = getRandomVantageClientAllFields();
        ClientHelper connectedClient4 = getRandomVantageClientAllFields();
        CrmTbUserObject connectedUserCrmTbUserObject = generateUserByClient(connectedClient);
        CrmTbUserObject connectedUserCrmTbUserObject2 = generateUserByClient(connectedClient2);
        CrmTbUserObject connectedUserCrmTbUserObject3 = generateUserByClient(connectedClient3);
        CrmTbUserObject connectedUserCrmTbUserObject4 = generateUserByClient(connectedClient4);
        connectedUserCrmTbUserObject.cpaId = data.clientHelper.getCpaId();
        connectedUserCrmTbUserObject2.cpaId = data.clientHelper.getCpaId();
        connectedUserCrmTbUserObject3.cpaId = 8881;
        connectedUserCrmTbUserObject4.cpaId = 8881;
        data.connectedUsers.add(connectedUserCrmTbUserObject);
        data.connectedUsers.add(connectedUserCrmTbUserObject2);
        data.connectedUsers.add(connectedUserCrmTbUserObject3);
        data.connectedUsers.add(connectedUserCrmTbUserObject4);
        data.connections.add(getConnection(data.clientHelper, connectedClient));
        data.connections.add(getConnection(data.clientHelper, connectedClient2));
        data.connections.add(getConnection(data.clientHelper, connectedClient3));
        data.connections.add(getConnection(data.clientHelper, connectedClient4));
        Allure.step("Set restriction");
        Allure.step("Send alert");

        //MT5 trade with current time
        Mt5DealsCoercedObject trade1Close = generateTradeByClient(cpaAbuseRuleExitEventEnd7p2Client);
        trade1Close.setComment("trade 1 close");
        trade1Close.setTime(getCurrentTimestampDbFormat());
        trade1Close.setTimeUtc(trade1Close.getTime());
        trade1Close.setEntry(1);
        trade1Close.setSymbol("USDEUR");
        trade1Close.setProfit(2501.01);
        trade1Close.setProfitUsd(2499.99);
        trade1Close.setVolumeLots(0.1);
        data.mt5DealsCoercedObjects.add(trade1Close);

        CrmTbDepositObject deposit = generateDepositByClient(cpaAbuseRuleExitEventEnd7p2Client);
        deposit.paymentChannel = "Crypto";
        deposit.amountUsd = 500d;
        deposit.setCreateTime(getCurrentTimestampDbFormat());
        data.crmTbDepositObjects.add(deposit);

        return data;
    }

    public static RuleDataHelper getCpaAbuseRuleExitEventEnd8p1Data() {
        Allure.step("Get client data");
        RuleDataHelper data = getCpaAbuseRuleData(cpaAbuseRuleExitEventEnd8p1Client);
        Allure.step("Client has not cpa abuse connected account");
        Allure.step("100% have same CPA value");
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        ClientHelper connectedClient2 = getRandomVantageClientAllFields();
        ClientHelper connectedClient3 = getRandomVantageClientAllFields();
        ClientHelper connectedClient4 = getRandomVantageClientAllFields();
        CrmTbUserObject connectedUserCrmTbUserObject = generateUserByClient(connectedClient);
        CrmTbUserObject connectedUserCrmTbUserObject2 = generateUserByClient(connectedClient2);
        CrmTbUserObject connectedUserCrmTbUserObject3 = generateUserByClient(connectedClient3);
        CrmTbUserObject connectedUserCrmTbUserObject4 = generateUserByClient(connectedClient4);
        connectedUserCrmTbUserObject.cpaId = data.clientHelper.getCpaId();
        connectedUserCrmTbUserObject2.cpaId = data.clientHelper.getCpaId();
        connectedUserCrmTbUserObject3.cpaId = 8881;
        connectedUserCrmTbUserObject4.cpaId = 8881;
        data.connectedUsers.add(connectedUserCrmTbUserObject);
        data.connectedUsers.add(connectedUserCrmTbUserObject2);
        data.connectedUsers.add(connectedUserCrmTbUserObject3);
        data.connectedUsers.add(connectedUserCrmTbUserObject4);
        data.connections.add(getConnection(data.clientHelper, connectedClient));
        data.connections.add(getConnection(data.clientHelper, connectedClient2));
        data.connections.add(getConnection(data.clientHelper, connectedClient3));
        data.connections.add(getConnection(data.clientHelper, connectedClient4));
        Allure.step("Set restriction");
        Allure.step("Send alert");

        //MT5 trade with current time
        Mt5DealsCoercedObject trade1Close = generateTradeByClient(cpaAbuseRuleExitEventEnd8p1Client);
        trade1Close.setComment("trade 1 close");
        trade1Close.setTime(getCurrentTimestampDbFormat());
        trade1Close.setTimeUtc(trade1Close.getTime());
        trade1Close.setEntry(1);
        trade1Close.setSymbol("USDEUR");
        trade1Close.setProfit(2501.01);
        trade1Close.setProfitUsd(2499.99);
        trade1Close.setVolumeLots(0.1);
        data.mt5DealsCoercedObjects.add(trade1Close);

        CrmTbDepositObject deposit = generateDepositByClient(cpaAbuseRuleExitEventEnd8p1Client);
        deposit.paymentChannel = "Bank";
        deposit.amountUsd = 550.01;
        deposit.setCreateTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 60));
        data.crmTbDepositObjects.add(deposit);

        return data;
    }

    public static RuleDataHelper getCpaAbuseRuleExitEventEnd8p2Data() {
        Allure.step("Get client data");
        RuleDataHelper data = getCpaAbuseRuleData(cpaAbuseRuleExitEventEnd8p2Client);
        Allure.step("Client has not cpa abuse connected account");
        Allure.step("50% have same CPA value");
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        ClientHelper connectedClient2 = getRandomVantageClientAllFields();
        ClientHelper connectedClient3 = getRandomVantageClientAllFields();
        ClientHelper connectedClient4 = getRandomVantageClientAllFields();
        CrmTbUserObject connectedUserCrmTbUserObject = generateUserByClient(connectedClient);
        CrmTbUserObject connectedUserCrmTbUserObject2 = generateUserByClient(connectedClient2);
        CrmTbUserObject connectedUserCrmTbUserObject3 = generateUserByClient(connectedClient3);
        CrmTbUserObject connectedUserCrmTbUserObject4 = generateUserByClient(connectedClient4);
        connectedUserCrmTbUserObject.cpaId = data.clientHelper.getCpaId();
        connectedUserCrmTbUserObject2.cpaId = data.clientHelper.getCpaId();
        connectedUserCrmTbUserObject3.cpaId = 8881;
        connectedUserCrmTbUserObject4.cpaId = 8881;
        data.connectedUsers.add(connectedUserCrmTbUserObject);
        data.connectedUsers.add(connectedUserCrmTbUserObject2);
        data.connectedUsers.add(connectedUserCrmTbUserObject3);
        data.connectedUsers.add(connectedUserCrmTbUserObject4);
        data.connections.add(getConnection(data.clientHelper, connectedClient));
        data.connections.add(getConnection(data.clientHelper, connectedClient2));
        data.connections.add(getConnection(data.clientHelper, connectedClient3));
        data.connections.add(getConnection(data.clientHelper, connectedClient4));

        Allure.step("Any mirror trades? = true");
        MirrorUcidObject mirrorUcid = generateMirrorUcidObjectByClient(cpaAbuseRuleExitEventEnd8p2Client);
        data.mirrorUcidObjects.add(mirrorUcid);

        Allure.step("HFT trades = true");
        Mt5DealsCoercedObject trade1Open = generateTradeByClient(cpaAbuseRuleExitEventEnd8p2Client);
        Mt5DealsCoercedObject trade1Close = generateTradeByClient(cpaAbuseRuleExitEventEnd8p2Client);

        trade1Open.setTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 2));
        trade1Open.setTimeUtc(trade1Open.getTime());
        trade1Open.setEntry(0);
        trade1Open.setVolumeLots(8.1);
        trade1Close.setPositionId(trade1Open.getPositionId());
        trade1Close.setComment("trade 1 close");
        trade1Close.setTime(getCurrentTimestampDbFormat());
        trade1Close.setTimeUtc(trade1Close.getTime());
        trade1Close.setEntry(1);
        trade1Close.setSymbol("USDEUR");
        trade1Close.setProfit(111.11);
        trade1Close.setProfitUsd(123.12);
        trade1Close.setVolumeLots(8.1);

        data.mt5DealsCoercedObjects.add(trade1Close);
        data.mt5DealsCoercedObjects.add(trade1Open);

        Allure.step("Deposit paymentType = Cryptocurrency , value 449.99");
        CrmTbDepositObject deposit = generateDepositByClient(cpaAbuseRuleExitEventEnd8p2Client);
        deposit.paymentType = "Cryptocurrency";
        deposit.amountUsd = 449.99;
        deposit.setCreateTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 60));
        data.crmTbDepositObjects.add(deposit);

        Allure.step("Send alert");
        return data;
    }

    public static RuleDataHelper getCpaAbuseRuleExitEventEnd9p1Data() {
        Allure.step("Get client data");
        RuleDataHelper data = getCpaAbuseRuleData(cpaAbuseRuleExitEventEnd9p1Client);
        Allure.step("Client has not cpa abuse connected account");
        Allure.step("50% have same CPA value");
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        ClientHelper connectedClient2 = getRandomVantageClientAllFields();
        ClientHelper connectedClient3 = getRandomVantageClientAllFields();
        ClientHelper connectedClient4 = getRandomVantageClientAllFields();
        CrmTbUserObject connectedUserCrmTbUserObject = generateUserByClient(connectedClient);
        CrmTbUserObject connectedUserCrmTbUserObject2 = generateUserByClient(connectedClient2);
        CrmTbUserObject connectedUserCrmTbUserObject3 = generateUserByClient(connectedClient3);
        CrmTbUserObject connectedUserCrmTbUserObject4 = generateUserByClient(connectedClient4);
        Allure.step("Have connection to clients with the same CPA");
        connectedUserCrmTbUserObject.cpaId = data.clientHelper.getCpaId();
        connectedUserCrmTbUserObject2.cpaId = data.clientHelper.getCpaId();
        connectedUserCrmTbUserObject3.cpaId = 8881;
        connectedUserCrmTbUserObject4.cpaId = 8881;
        data.connectedUsers.add(connectedUserCrmTbUserObject);
        data.connectedUsers.add(connectedUserCrmTbUserObject2);
        data.connectedUsers.add(connectedUserCrmTbUserObject3);
        data.connectedUsers.add(connectedUserCrmTbUserObject4);
        data.connections.add(getConnection(data.clientHelper, connectedClient));
        data.connections.add(getConnection(data.clientHelper, connectedClient2));
        data.connections.add(getConnection(data.clientHelper, connectedClient3));
        data.connections.add(getConnection(data.clientHelper, connectedClient4));

        Allure.step("Any mirror trades? = false");

        Allure.step("HFT trades = false");
        Mt5DealsCoercedObject trade1Open = generateTradeByClient(cpaAbuseRuleExitEventEnd9p1Client);
        Mt5DealsCoercedObject trade1Close = generateTradeByClient(cpaAbuseRuleExitEventEnd9p1Client);

        trade1Open.setTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 1, 2));
        trade1Open.setTimeUtc(trade1Open.getTime());
        trade1Open.setEntry(0);
        trade1Open.setVolumeLots(4.0);
        trade1Close.setPositionId(trade1Open.getPositionId());
        trade1Close.setComment("trade 1 close");
        trade1Close.setTime(getCurrentTimestampDbFormat());
        trade1Close.setTimeUtc(trade1Close.getTime());
        trade1Close.setEntry(1);
        trade1Close.setSymbol("USDEUR");
        trade1Close.setProfit(111.11);
        trade1Close.setProfitUsd(123.12);
        trade1Close.setVolumeLots(4.0);

        data.mt5DealsCoercedObjects.add(trade1Close);
        data.mt5DealsCoercedObjects.add(trade1Open);

        Allure.step("Deposit paymentType = Other , value 500.00");
        CrmTbDepositObject deposit = generateDepositByClient(cpaAbuseRuleExitEventEnd9p1Client);
        deposit.paymentType = "Other";
        deposit.amountUsd = 500.00;
        deposit.setCreateTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 60));
        data.crmTbDepositObjects.add(deposit);

        Allure.step("Send alert");
        return data;
    }

    public static RuleDataHelper getCpaAbuseRuleExitEventEnd9p2Data() {
        Allure.step("Get client data");
        RuleDataHelper data = getCpaAbuseRuleData(cpaAbuseRuleExitEventEnd9p2Client);
        Allure.step("Client has not cpa abuse connected account");
        Allure.step("50% have same CPA value");
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        ClientHelper connectedClient2 = getRandomVantageClientAllFields();
        ClientHelper connectedClient3 = getRandomVantageClientAllFields();
        ClientHelper connectedClient4 = getRandomVantageClientAllFields();
        CrmTbUserObject connectedUserCrmTbUserObject = generateUserByClient(connectedClient);
        CrmTbUserObject connectedUserCrmTbUserObject2 = generateUserByClient(connectedClient2);
        CrmTbUserObject connectedUserCrmTbUserObject3 = generateUserByClient(connectedClient3);
        CrmTbUserObject connectedUserCrmTbUserObject4 = generateUserByClient(connectedClient4);
        Allure.step("Have no connection to clients with the same CPA");
        connectedUserCrmTbUserObject.cpaId = 8881;
        connectedUserCrmTbUserObject2.cpaId = 8881;
        connectedUserCrmTbUserObject3.cpaId = 8881;
        connectedUserCrmTbUserObject4.cpaId = 8881;
        data.connectedUsers.add(connectedUserCrmTbUserObject);
        data.connectedUsers.add(connectedUserCrmTbUserObject2);
        data.connectedUsers.add(connectedUserCrmTbUserObject3);
        data.connectedUsers.add(connectedUserCrmTbUserObject4);
        data.connections.add(getConnection(data.clientHelper, connectedClient));
        data.connections.add(getConnection(data.clientHelper, connectedClient2));
        data.connections.add(getConnection(data.clientHelper, connectedClient3));
        data.connections.add(getConnection(data.clientHelper, connectedClient4));

        Allure.step("Any mirror trades? = false");

        Allure.step("HFT trades = false");
        Mt5DealsCoercedObject trade1Open = generateTradeByClient(cpaAbuseRuleExitEventEnd9p2Client);
        Mt5DealsCoercedObject trade1Close = generateTradeByClient(cpaAbuseRuleExitEventEnd9p2Client);

        trade1Open.setTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 1, 2));
        trade1Open.setTimeUtc(trade1Open.getTime());
        trade1Open.setEntry(0);
        trade1Open.setVolumeLots(4.0);
        trade1Close.setPositionId(trade1Open.getPositionId());
        trade1Close.setComment("trade 1 close");
        trade1Close.setTime(getCurrentTimestampDbFormat());
        trade1Close.setTimeUtc(trade1Close.getTime());
        trade1Close.setEntry(1);
        trade1Close.setSymbol("USDEUR");
        trade1Close.setProfit(111.11);
        trade1Close.setProfitUsd(123.12);
        trade1Close.setVolumeLots(4.0);

        data.mt5DealsCoercedObjects.add(trade1Close);
        data.mt5DealsCoercedObjects.add(trade1Open);

        Allure.step("Deposit paymentType = Other , value 500.00");
        CrmTbDepositObject deposit = generateDepositByClient(cpaAbuseRuleExitEventEnd9p2Client);
        deposit.paymentType = "Other";
        deposit.amountUsd = 500.00;
        deposit.setCreateTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 60));
        data.crmTbDepositObjects.add(deposit);

        Allure.step("Send alert");
        return data;
    }

    public static Map<String, RuleDataHelper> setupCpaAbuseRuleData() {
        startSshTunnel();
        Map<String, RuleDataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("1", getCpaAbuseRuleExitEventEnd1Data());
        map.put("2", getCpaAbuseRuleExitEventEnd2Data());
        map.put("3", getCpaAbuseRuleExitEventEnd3Data());
        map.put("4", getCpaAbuseRuleExitEventEnd4Data());
        map.put("5", getCpaAbuseRuleExitEventEnd5p1Data());
        map.put("6", getCpaAbuseRuleExitEventEnd5p2Data());
        map.put("7", getCpaAbuseRuleExitEventEnd6Data());
        map.put("8", getCpaAbuseRuleExitEventEnd7p1Data());
        map.put("9", getCpaAbuseRuleExitEventEnd7p2Data());
        map.put("10", getCpaAbuseRuleExitEventEnd8p1Data());
        map.put("11", getCpaAbuseRuleExitEventEnd8p2Data());
        map.put("12", getCpaAbuseRuleExitEventEnd9p1Data());
        map.put("13", getCpaAbuseRuleExitEventEnd9p2Data());

        // Loop through the list with data and insert all the data into the according tables
        setupRuleData(map);
        return map;
    }

    public static void deleteCpaAbuseRuleData(Map<String, RuleDataHelper> map) throws Exception {
        deleteRuleData(map);
    }
}
