package helpers.data.rules.cpa_abuse_rule;

import business_objects.db.clickhouse.bo_client_fraud_types.BoClientFraudTypesObject;
import business_objects.db.clickhouse.connection_table.ConnectionTableEntry;
import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;

import business_objects.db.clickhouse.mirror_ucid_table.MirrorUcidObject;
import business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedObject;
import business_objects.kafka.crm_events.WithdrawalEvent;
import generator.annotations.RuleTestData;
import helpers.data.ClientHelper;
import helpers.data.enums.FraudType;
import helpers.data.rules.RuleDataHelper;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountData;
import static business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositObjectFactory.generateDepositByClient;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.mirror_ucid_table.MirrorUcidObjectFactory.generateMirrorUcidObjectByClient;
import static business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedFactory.generateTradeByClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.ClientFactory.getRandomVantageClientNoCpaIbRef;
import static helpers.data.enums.DateTimeFormat.DATE_AND_TIME;
import static helpers.data.rules.RuleDataHelper.deleteRuleData;
import static helpers.data.rules.RuleDataHelper.setupRuleData;
import static helpers.database.DbHelper.*;
import static utils.Constants.*;
import static utils.Utils.*;

@RuleTestData("cpa-abuse")
public class CpaAbuseRuleDataFactory {
    private static final ClientHelper cpaAbuseRuleExitEventEnd1Client = getRandomVantageClientNoCpaIbRef();
    private static final ClientHelper cpaAbuseRuleExitEventEnd2_1Client = getRandomVantageClientAllFields();
    private static final ClientHelper cpaAbuseRuleExitEventEnd2_2Client = getRandomVantageClientAllFields();
    private static final ClientHelper cpaAbuseRuleExitEventEnd3Client = getRandomVantageClientAllFields();
    private static final ClientHelper cpaAbuseRuleExitEventEnd4_1Client = getRandomVantageClientAllFields();
    private static final ClientHelper cpaAbuseRuleExitEventEnd4_2Client = getRandomVantageClientAllFields();
    private static final ClientHelper cpaAbuseRuleExitEventEnd4_3Client = getRandomVantageClientAllFields();

    @Step("Create data for Mirror trading rule")
    private static RuleDataHelper getCpaAbuseRuleData(ClientHelper client) {
        CrmTbUserObject userObject = generateUserByClient(client);
        CrmTbAccountObject crmTbAccountObject = generateCrmTbAccountData(client);
        WithdrawalEvent withdrawalEvent = new WithdrawalEvent(getRandomUuidString(), Instant.now().toString(), getRandomIntPositive(), client.getUserId(), client.getTradingAccount(), client.getBrand(), "vfsc", "FASAPAY", 1, 1d, 1d, 1d, 1d, "555555**** **6666", 1, Instant.now().toString(), "", "", 1, "", 1d, 1, 1, "", 1, 1, 1d, 2, 1d, "withdrawal");
        return new RuleDataHelper(client, userObject, null, null, new ArrayList<>(), new ArrayList<>(), withdrawalEvent, null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), crmTbAccountObject, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), null, null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), null, new ArrayList<>(), new ArrayList<>());
    }

    private static ConnectionTableEntry getConnection(ClientHelper fromClient, ClientHelper toClient) {
        return new ConnectionTableEntry(
                fromClient.getUcid(), toClient.getUcid(), CONNECTION_TYPE_SAME_IDENTITY, 1d, List.of(
                        new ConnectionTableEntry.ConnectionInfo(
                                CONNECTION_ATTRIBUTE_NAME_PAYOUT_ID, CONNECTION_SEARCH_DATA_CARD_NUMBER, CONNECTION_SEARCH_DATA_CARD_NUMBER, CONNECTION_TYPE_RELATION_TYPE_EXACT
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
        RuleDataHelper data = getCpaAbuseRuleData(cpaAbuseRuleExitEventEnd1Client);
        Allure.step("Create user object with no CPA");
        data.clientHelper.setCpaId(8888);

        Mt5DealsCoercedObject trade1Close = generateTradeByClient(cpaAbuseRuleExitEventEnd1Client);
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

    public static RuleDataHelper getCpaAbuseRuleExitEventEnd21Data() {
        Allure.step("Get client data");
        RuleDataHelper data = getCpaAbuseRuleData(cpaAbuseRuleExitEventEnd2_1Client);
        Allure.step("Client has cpa abuse connected account");
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        data.connections.add(getConnection(data.clientHelper, connectedClient));
        BoClientFraudTypesObject boClientFraudTypesObject = new BoClientFraudTypesObject(
                connectedClient.getUcid(), FraudType.CPA_ABUSE.getFraudTypeId(), FraudType.CPA_ABUSE.getKey()
        );
        data.clientFraudTypes.add(boClientFraudTypesObject);
        Allure.step("Set restriction");
        Allure.step("Send alert");

        Mt5DealsCoercedObject trade1Close = generateTradeByClient(cpaAbuseRuleExitEventEnd2_1Client);
        trade1Close.setComment("trade 1 close");
        trade1Close.setTime(getCurrentTimestampDbFormat());
        trade1Close.setTimeUtc(trade1Close.getTime());
        trade1Close.setEntry(1);
        trade1Close.setSymbol("USDEUR");
        trade1Close.setProfit(111.11);
        trade1Close.setProfitUsd(123.12);
        trade1Close.setVolumeLots(0.1);
        data.mt5DealsCoercedObjects.add(trade1Close);
        return data;
    }

    public static RuleDataHelper getCpaAbuseRuleExitEventEnd22Data() {
        Allure.step("Get client data");
        RuleDataHelper data = getCpaAbuseRuleData(cpaAbuseRuleExitEventEnd2_2Client);
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
        connectedUserCrmTbUserObject4.cpaId = data.clientHelper.getCpaId();
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
        return data;
    }

    public static RuleDataHelper getCpaAbuseRuleExitEventEnd3Data() {
        Allure.step("Get client data");
        RuleDataHelper data = getCpaAbuseRuleData(cpaAbuseRuleExitEventEnd3Client);
        Allure.step("Client has not cpa abuse connected account");
        Allure.step("At least 70% have any CPA value");
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        ClientHelper connectedClient2 = getRandomVantageClientAllFields();
        CrmTbUserObject connectedUserCrmTbUserObject = generateUserByClient(connectedClient);
        CrmTbUserObject connectedUserCrmTbUserObject2 = generateUserByClient(connectedClient2);
        connectedUserCrmTbUserObject.cpaId = 0;
        Allure.step("Send alert");
        Allure.step("Send restriction");
        data.connectedUsers.add(connectedUserCrmTbUserObject);
        data.connectedUsers.add(connectedUserCrmTbUserObject2);
        data.connections.add(getConnection(data.clientHelper, connectedClient));
        data.connections.add(getConnection(data.clientHelper, connectedClient2));
        return data;
    }

    public static RuleDataHelper getCpaAbuseRuleExitEventEnd41Data() {
        Allure.step("Get client data");
        RuleDataHelper data = getCpaAbuseRuleData(cpaAbuseRuleExitEventEnd4_1Client);
        Allure.step("Client has not cpa abuse connected account");
        Allure.step("At least 70% have any CPA value");
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        CrmTbUserObject connectedUserCrmTbUserObject = generateUserByClient(connectedClient);
        connectedUserCrmTbUserObject.cpaId = 0;
        Allure.step("Deposit is crypto = true");
        Allure.step("1 deposit, value 10%+-. 500 USD = true");
        CrmTbDepositObject deposit = generateDepositByClient(cpaAbuseRuleExitEventEnd4_1Client);
        deposit.paymentChannel = "Crypto";
        deposit.amountUsd = 500d;
        Allure.step("Approx. 2-7 lots traded = true");
        Mt5DealsCoercedObject deal = generateTradeByClient(cpaAbuseRuleExitEventEnd4_1Client);
        Mt5DealsCoercedObject deal2 = generateTradeByClient(cpaAbuseRuleExitEventEnd4_1Client);
        Mt5DealsCoercedObject deal3 = generateTradeByClient(cpaAbuseRuleExitEventEnd4_1Client);
        Allure.step("Any mirror trades? = true");
        MirrorUcidObject mirrorUcid = generateMirrorUcidObjectByClient(cpaAbuseRuleExitEventEnd4_1Client);
        Allure.step("HFT trades = true");
        Mt5DealsCoercedObject trade1Open;
        Mt5DealsCoercedObject trade1Close;
        Mt5DealsCoercedObject trade2Open;
        Mt5DealsCoercedObject trade2Close;
        trade1Open = generateTradeByClient(cpaAbuseRuleExitEventEnd4_1Client);
        trade1Close = generateTradeByClient(cpaAbuseRuleExitEventEnd4_1Client);
        trade1Open.setTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 2));
        trade1Open.setTimeUtc(trade1Open.getTime());
        trade1Open.setEntry(0);
        trade1Open.setVolumeLots(0.1);
        trade1Close.setPositionId(trade1Open.getPositionId());
        trade1Close.setComment("trade 1 close");
        trade1Close.setTime(getCurrentTimestampDbFormat());
        trade1Close.setTimeUtc(trade1Close.getTime());
        trade1Close.setEntry(1);
        trade1Close.setSymbol("USDEUR");
        trade1Close.setProfit(111.11);
        trade1Close.setProfitUsd(123.12);
        trade1Close.setVolumeLots(0.1);
        trade2Open = generateTradeByClient(cpaAbuseRuleExitEventEnd4_1Client);
        trade2Close = generateTradeByClient(cpaAbuseRuleExitEventEnd4_1Client);
        trade2Open.setTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 1, 0, 1));
        trade2Open.setTimeUtc(trade2Open.getTime());
        trade2Open.setEntry(0);
        trade2Close.setVolumeLots(0.1);
        trade2Open.setVolumeLots(0.1);
        trade2Close.setPositionId(trade2Open.getPositionId());
        trade2Close.setComment("trade 2 close");
        trade2Close.setTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 1, 0, 0));
        trade2Close.setTimeUtc(trade2Close.getTime());
        trade2Close.setEntry(1);
        trade2Close.setSymbol("GBPJPY");
        trade2Close.setProfit(222.22);
        trade2Close.setProfitUsd(234.15);
        Mt5DealsCoercedObject trade3 = generateTradeByClient(cpaAbuseRuleExitEventEnd4_1Client);
        trade3.setEntry(0);
        trade3.setVolumeLots(0.1);
        Mt5DealsCoercedObject trade4 = generateTradeByClient(cpaAbuseRuleExitEventEnd4_1Client);
        trade4.setEntry(1);
        trade4.setVolumeLots(0.1);
        Allure.step("Send alert");
        data.mirrorUcidObjects.add(mirrorUcid);
        data.mt5DealsCoercedObjects.add(deal);
        data.mt5DealsCoercedObjects.add(deal2);
        data.mt5DealsCoercedObjects.add(deal3);
        data.mt5DealsCoercedObjects.add(trade1Open);
        data.mt5DealsCoercedObjects.add(trade1Close);
        data.mt5DealsCoercedObjects.add(trade2Open);
        data.mt5DealsCoercedObjects.add(trade2Close);
        data.mt5DealsCoercedObjects.add(trade3);
        data.mt5DealsCoercedObjects.add(trade4);
        data.crmTbDepositObjects.add(deposit);
        data.connectedUsers.add(connectedUserCrmTbUserObject);
        data.connections.add(getConnection(data.clientHelper, connectedClient));
        return data;
    }

    public static RuleDataHelper getCpaAbuseRuleExitEventEnd42Data() {
        Allure.step("Get client data");
        RuleDataHelper data = getCpaAbuseRuleData(cpaAbuseRuleExitEventEnd4_2Client);
        Allure.step("Client has not cpa abuse connected account");
        Allure.step("At least 70% have any CPA value");
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        ClientHelper connectedClient2 = getRandomVantageClientAllFields();
        CrmTbUserObject connectedUserCrmTbUserObject = generateUserByClient(connectedClient);
        CrmTbUserObject connectedUserCrmTbUserObject2 = generateUserByClient(connectedClient2);
        connectedUserCrmTbUserObject.cpaId = 0;
        connectedUserCrmTbUserObject2.cpaId = cpaAbuseRuleExitEventEnd4_2Client.getCpaId();
        Allure.step("Deposit is crypto = true");
        Allure.step("1 deposit, value 10%+-. 500 USD = true");
        CrmTbDepositObject deposit = generateDepositByClient(cpaAbuseRuleExitEventEnd4_2Client);
        deposit.paymentChannel = "Crypto";
        deposit.amountUsd = 500d;
        Allure.step("Approx. 2-7 lots traded = true");
        Mt5DealsCoercedObject deal = generateTradeByClient(cpaAbuseRuleExitEventEnd4_2Client);
        Mt5DealsCoercedObject deal2 = generateTradeByClient(cpaAbuseRuleExitEventEnd4_2Client);
        Mt5DealsCoercedObject deal3 = generateTradeByClient(cpaAbuseRuleExitEventEnd4_2Client);
        Allure.step("Any mirror trades? = true");
        MirrorUcidObject mirrorUcid = generateMirrorUcidObjectByClient(cpaAbuseRuleExitEventEnd4_2Client);
        Allure.step("HFT trades = true");
        Mt5DealsCoercedObject trade1Open;
        Mt5DealsCoercedObject trade1Close;
        Mt5DealsCoercedObject trade2Open;
        Mt5DealsCoercedObject trade2Close;
        trade1Open = generateTradeByClient(cpaAbuseRuleExitEventEnd4_2Client);
        trade1Close = generateTradeByClient(cpaAbuseRuleExitEventEnd4_2Client);
        trade1Open.setTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 2));
        trade1Open.setTimeUtc(trade1Open.getTime());
        trade1Open.setEntry(0);
        trade1Open.setVolumeLots(0.1);
        trade1Close.setPositionId(trade1Open.getPositionId());
        trade1Close.setComment("trade 1 close");
        trade1Close.setTime(getCurrentTimestampDbFormat());
        trade1Close.setTimeUtc(trade1Close.getTime());
        trade1Close.setEntry(1);
        trade1Close.setSymbol("USDEUR");
        trade1Close.setProfit(111.11);
        trade1Close.setProfitUsd(123.12);
        trade1Close.setVolumeLots(0.1);
        trade2Open = generateTradeByClient(cpaAbuseRuleExitEventEnd4_2Client);
        trade2Close = generateTradeByClient(cpaAbuseRuleExitEventEnd4_2Client);
        trade2Open.setTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 1, 0, 1));
        trade2Open.setTimeUtc(trade2Open.getTime());
        trade2Open.setEntry(0);
        trade2Close.setVolumeLots(0.1);
        trade2Open.setVolumeLots(0.1);
        trade2Close.setPositionId(trade2Open.getPositionId());
        trade2Close.setComment("trade 2 close");
        trade2Close.setTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 1, 0, 0));
        trade2Close.setTimeUtc(trade2Close.getTime());
        trade2Close.setEntry(1);
        trade2Close.setSymbol("GBPJPY");
        trade2Close.setProfit(222.22);
        trade2Close.setProfitUsd(234.15);
        Mt5DealsCoercedObject trade3 = generateTradeByClient(cpaAbuseRuleExitEventEnd4_2Client);
        trade3.setEntry(0);
        trade3.setVolumeLots(0.1);
        Mt5DealsCoercedObject trade4 = generateTradeByClient(cpaAbuseRuleExitEventEnd4_2Client);
        trade4.setEntry(1);
        trade4.setVolumeLots(0.1);
        Allure.step("Connection has same cpa id = true");
        connectedUserCrmTbUserObject.cpaId = 0;

        Allure.step("Send alert");
        Allure.step("Send restriction");

        data.mirrorUcidObjects.add(mirrorUcid);
        data.mt5DealsCoercedObjects.add(deal);
        data.mt5DealsCoercedObjects.add(deal2);
        data.mt5DealsCoercedObjects.add(deal3);
        data.mt5DealsCoercedObjects.add(trade1Open);
        data.mt5DealsCoercedObjects.add(trade1Close);
        data.mt5DealsCoercedObjects.add(trade2Open);
        data.mt5DealsCoercedObjects.add(trade2Close);
        data.mt5DealsCoercedObjects.add(trade3);
        data.mt5DealsCoercedObjects.add(trade4);
        data.crmTbDepositObjects.add(deposit);
        data.connectedUsers.add(connectedUserCrmTbUserObject);
        data.connectedUsers.add(connectedUserCrmTbUserObject2);
        data.connections.add(getConnection(data.clientHelper, connectedClient));
        data.connections.add(getConnection(data.clientHelper, connectedClient2));
        return data;
    }

    public static RuleDataHelper getCpaAbuseRuleExitEventEnd43Data() {
        Allure.step("Get client data");
        RuleDataHelper data = getCpaAbuseRuleData(cpaAbuseRuleExitEventEnd4_3Client);
        Allure.step("Client has not cpa abuse connected account");
        Allure.step("At least 70% have any CPA value");
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        ClientHelper connectedClient2 = getRandomVantageClientAllFields();
        CrmTbUserObject connectedUserCrmTbUserObject = generateUserByClient(connectedClient);
        CrmTbUserObject connectedUserCrmTbUserObject2 = generateUserByClient(connectedClient2);
        connectedUserCrmTbUserObject.cpaId = 0;
        connectedUserCrmTbUserObject2.cpaId = cpaAbuseRuleExitEventEnd4_3Client.getCpaId();
        Allure.step("Send alert");
        Allure.step("Send restriction");
        data.connectedUsers.add(connectedUserCrmTbUserObject);
        data.connectedUsers.add(connectedUserCrmTbUserObject2);
        data.connections.add(getConnection(data.clientHelper, connectedClient));
        data.connections.add(getConnection(data.clientHelper, connectedClient2));
        return data;
    }

    public static Map<String, RuleDataHelper> setupCpaAbuseRuleData() {
        startSshTunnel();
        Map<String, RuleDataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("1", getCpaAbuseRuleExitEventEnd1Data());
        map.put("2", getCpaAbuseRuleExitEventEnd2Data());
        map.put("21", getCpaAbuseRuleExitEventEnd21Data());
        map.put("22", getCpaAbuseRuleExitEventEnd22Data());
        map.put("3", getCpaAbuseRuleExitEventEnd3Data());
        map.put("41", getCpaAbuseRuleExitEventEnd41Data());
        map.put("42", getCpaAbuseRuleExitEventEnd42Data());
        map.put("43", getCpaAbuseRuleExitEventEnd43Data());

        // Loop through the list with data and insert all the data into the according tables
        setupRuleData(map);
        return map;
    }

    public static void deleteCpaAbuseRuleData(Map<String, RuleDataHelper> map) throws Exception {
        deleteRuleData(map);
    }
}
