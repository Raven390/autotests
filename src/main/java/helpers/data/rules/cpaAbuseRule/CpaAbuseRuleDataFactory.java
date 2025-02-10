package helpers.data.rules.cpaAbuseRule;

import businessObjects.db.clickhouse.boClientFraudTypes.BoClientFraudTypesObject;
import businessObjects.db.clickhouse.connectionTable.ConnectionTableEntry;
import businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObject;
import businessObjects.db.clickhouse.crmTbDepositTable.CrmTbDepositObject;
import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;

import businessObjects.db.clickhouse.mirrorUcidTable.MirrorUcidObject;
import businessObjects.db.clickhouse.mtMt5DealsCoerced.Mt5DealsCoercedObject;
import businessObjects.kafka.crmEvents.WithdrawalEvent;
import generator.annotations.RuleTestData;
import helpers.data.ClientHelper;
import helpers.data.enums.FraudType;
import helpers.data.rules.RuleDataHelper;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;

import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObjectFactory.generateCrmTbAccountData;
import static businessObjects.db.clickhouse.crmTbDepositTable.CrmTbDepositObjectFactory.generateDepositByClient;
import static businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObjectFactory.generateUserByClient;
import static businessObjects.db.clickhouse.mirrorUcidTable.MirrorUcidObjectFactory.generateMirrorUcidObjectByClient;
import static businessObjects.db.clickhouse.mtMt5DealsCoerced.Mt5DealsCoercedFactory.generateTradeByClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.ClientFactory.getRandomVantageClientNoCpaIbRef;
import static helpers.data.enums.DateTimeFormat.DATE_AND_TIME;
import static helpers.data.rules.RuleDataHelper.deleteRuleData;
import static helpers.data.rules.RuleDataHelper.setupRuleData;
import static helpers.database.DbHelper.*;
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
        return new RuleDataHelper(client, userObject, null, null, new ArrayList<>(), new ArrayList<>(), withdrawalEvent, null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), crmTbAccountObject, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), null, null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    private static ConnectionTableEntry getConnection(ClientHelper fromClient, ClientHelper toClient) {
        return new ConnectionTableEntry(
                fromClient.getUcid(), toClient.getUcid(), "Same Identity", 1d, "[{\"connectionAttributeName\": \"payout\", \"connectionAttributeValue\": \"535456**** **0344\", \"sourceAttributeValue\": \"535456**** **0344\", \"relationType\": \"exact\"}]");
    }

    public static RuleDataHelper getCpaAbuseRuleExitEventEnd1Data() {
        Allure.step("Get client data");
        RuleDataHelper data = getCpaAbuseRuleData(cpaAbuseRuleExitEventEnd1Client);
        Allure.step("Create user object with no CPA");
        data.clientHelper.setCpaId(null);
        return data;
    }

    public static RuleDataHelper getCpaAbuseRuleExitEventEnd2_1Data() {
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
        return data;
    }

    public static RuleDataHelper getCpaAbuseRuleExitEventEnd2_2Data() {
        Allure.step("Get client data");
        RuleDataHelper data = getCpaAbuseRuleData(cpaAbuseRuleExitEventEnd2_2Client);
        Allure.step("Client has not cpa abuse connected account");
        Allure.step("At least 70% have any CPA value");
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        CrmTbUserObject connectedUserCrmTbUserObject = generateUserByClient(connectedClient);
        connectedUserCrmTbUserObject.cpaId = 1;
        data.connectedUsers.add(connectedUserCrmTbUserObject);
        data.connections.add(getConnection(data.clientHelper, connectedClient));
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

    public static RuleDataHelper getCpaAbuseRuleExitEventEnd4_1Data() {
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
        trade1Open.time = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 2);
        trade1Open.timeUtc = trade1Open.time;
        trade1Open.entry = 0;
        trade1Open.volumeLots = 0.1;
        trade1Close.positionId = trade1Open.positionId;
        trade1Close.comment = "trade 1 close";
        trade1Close.time = getCurrentTimestampDbFormat();
        trade1Close.timeUtc = trade1Close.time;
        trade1Close.entry = 1;
        trade1Close.symbol = "USDEUR";
        trade1Close.profit = 111.11;
        trade1Close.profitUsd = 123.12;
        trade1Close.volumeLots = 0.1;
        trade2Open = generateTradeByClient(cpaAbuseRuleExitEventEnd4_1Client);
        trade2Close = generateTradeByClient(cpaAbuseRuleExitEventEnd4_1Client);
        trade2Open.time = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 1, 0, 1);
        trade2Open.timeUtc = trade2Open.time;
        trade2Open.entry = 0;
        trade2Close.volumeLots = 0.1;
        trade2Open.volumeLots = 0.1;
        trade2Close.positionId = trade2Open.positionId;
        trade2Close.comment = "trade 2 close";
        trade2Close.time = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 1, 0, 0);
        trade2Close.timeUtc = trade2Close.time;
        trade2Close.entry = 1;
        trade2Close.symbol = "GBPJPY";
        trade2Close.profit = 222.22;
        trade2Close.profitUsd = 234.15;
        Mt5DealsCoercedObject trade3 = generateTradeByClient(cpaAbuseRuleExitEventEnd4_1Client);
        trade3.entry = 0;
        trade3.volumeLots = 0.1;
        Mt5DealsCoercedObject trade4 = generateTradeByClient(cpaAbuseRuleExitEventEnd4_1Client);
        trade4.entry = 1;
        trade4.volumeLots = 0.1;
        Allure.step("Send alert");
        data.mirrorUcidObjects.add(mirrorUcid);
        data.mt5DealsObjects.add(deal);
        data.mt5DealsObjects.add(deal2);
        data.mt5DealsObjects.add(deal3);
        data.mt5DealsObjects.add(trade1Open);
        data.mt5DealsObjects.add(trade1Close);
        data.mt5DealsObjects.add(trade2Open);
        data.mt5DealsObjects.add(trade2Close);
        data.mt5DealsObjects.add(trade3);
        data.mt5DealsObjects.add(trade4);
        data.crmTbDepositObjects.add(deposit);
        data.connectedUsers.add(connectedUserCrmTbUserObject);
        data.connections.add(getConnection(data.clientHelper, connectedClient));
        return data;
    }

    public static RuleDataHelper getCpaAbuseRuleExitEventEnd4_2Data() {
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
        trade1Open.time = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 2);
        trade1Open.timeUtc = trade1Open.time;
        trade1Open.entry = 0;
        trade1Open.volumeLots = 0.1;
        trade1Close.positionId = trade1Open.positionId;
        trade1Close.comment = "trade 1 close";
        trade1Close.time = getCurrentTimestampDbFormat();
        trade1Close.timeUtc = trade1Close.time;
        trade1Close.entry = 1;
        trade1Close.symbol = "USDEUR";
        trade1Close.profit = 111.11;
        trade1Close.profitUsd = 123.12;
        trade1Close.volumeLots = 0.1;
        trade2Open = generateTradeByClient(cpaAbuseRuleExitEventEnd4_2Client);
        trade2Close = generateTradeByClient(cpaAbuseRuleExitEventEnd4_2Client);
        trade2Open.time = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 1, 0, 1);
        trade2Open.timeUtc = trade2Open.time;
        trade2Open.entry = 0;
        trade2Close.volumeLots = 0.1;
        trade2Open.volumeLots = 0.1;
        trade2Close.positionId = trade2Open.positionId;
        trade2Close.comment = "trade 2 close";
        trade2Close.time = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 1, 0, 0);
        trade2Close.timeUtc = trade2Close.time;
        trade2Close.entry = 1;
        trade2Close.symbol = "GBPJPY";
        trade2Close.profit = 222.22;
        trade2Close.profitUsd = 234.15;
        Mt5DealsCoercedObject trade3 = generateTradeByClient(cpaAbuseRuleExitEventEnd4_2Client);
        trade3.entry = 0;
        trade3.volumeLots = 0.1;
        Mt5DealsCoercedObject trade4 = generateTradeByClient(cpaAbuseRuleExitEventEnd4_2Client);
        trade4.entry = 1;
        trade4.volumeLots = 0.1;
        Allure.step("Connection has same cpa id = true");
        connectedUserCrmTbUserObject.cpaId = 0;

        Allure.step("Send alert");
        Allure.step("Send restriction");

        data.mirrorUcidObjects.add(mirrorUcid);
        data.mt5DealsObjects.add(deal);
        data.mt5DealsObjects.add(deal2);
        data.mt5DealsObjects.add(deal3);
        data.mt5DealsObjects.add(trade1Open);
        data.mt5DealsObjects.add(trade1Close);
        data.mt5DealsObjects.add(trade2Open);
        data.mt5DealsObjects.add(trade2Close);
        data.mt5DealsObjects.add(trade3);
        data.mt5DealsObjects.add(trade4);
        data.crmTbDepositObjects.add(deposit);
        data.connectedUsers.add(connectedUserCrmTbUserObject);
        data.connectedUsers.add(connectedUserCrmTbUserObject2);
        data.connections.add(getConnection(data.clientHelper, connectedClient));
        data.connections.add(getConnection(data.clientHelper, connectedClient2));
        return data;
    }

    public static RuleDataHelper getCpaAbuseRuleExitEventEnd4_3Data() {
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

    public static Map<String, RuleDataHelper> setupCpaAbuseRuleData() throws ReflectiveOperationException,
            SQLException {
        startSshTunnel();
        Map<String, RuleDataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("1", getCpaAbuseRuleExitEventEnd1Data());
        map.put("2_1", getCpaAbuseRuleExitEventEnd2_1Data());
        map.put("2_2", getCpaAbuseRuleExitEventEnd2_2Data());
        map.put("3", getCpaAbuseRuleExitEventEnd3Data());
        map.put("4_1", getCpaAbuseRuleExitEventEnd4_1Data());
        map.put("4_2", getCpaAbuseRuleExitEventEnd4_2Data());
        map.put("4_3", getCpaAbuseRuleExitEventEnd4_3Data());

        // Loop through the list with data and insert all the data into the according tables
        setupRuleData(map);
        return map;
    }

    public static void deleteCpaAbuseRuleData(Map<String, RuleDataHelper> map) throws Exception {
        deleteRuleData(map);
    }
}
