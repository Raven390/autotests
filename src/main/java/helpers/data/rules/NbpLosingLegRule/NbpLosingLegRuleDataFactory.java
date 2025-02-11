package helpers.data.rules.NbpLosingLegRule;

import businessObjects.db.clickhouse.crmTbBonusTable.CrmTbBonusObject;
import businessObjects.db.clickhouse.crmTbDepositTable.CrmTbDepositObject;
import businessObjects.db.clickhouse.mtMt5DealsCoerced.Mt5DealsCoercedObject;
import businessObjects.kafka.mtEvents.CloseTradeMtEvent;
import generator.annotations.RuleTestData;
import helpers.data.ClientHelper;
import helpers.data.rules.RuleDataHelper;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObjectFactory.generateCrmTbAccountData;
import static businessObjects.db.clickhouse.crmTbBonusTable.CrmTbBonusObjectFactory.generateBonusByClient;
import static businessObjects.db.clickhouse.crmTbDepositTable.CrmTbDepositObjectFactory.generateDepositByClient;
import static businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObjectFactory.generateUserByClient;
import static businessObjects.db.clickhouse.mirrorUcidTable.MirrorUcidObjectFactory.generateMirrorUcidObjectByClients;
import static businessObjects.db.clickhouse.mtMt5DealsCoerced.Mt5DealsCoercedFactory.generateTradeByClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.DateTimeFormat.DATE_AND_TIME;
import static helpers.data.rules.RuleDataHelper.deleteRuleData;
import static helpers.data.rules.RuleDataHelper.setupRuleData;
import static utils.Utils.*;

@RuleTestData("market-manipulation")
public class NbpLosingLegRuleDataFactory {

    // Clients
    private static final ClientHelper nbpLosingLegExit1Client = getRandomVantageClientAllFields();
    private static final ClientHelper nbpLosingLegExit2Client = getRandomVantageClientAllFields();
    private static final ClientHelper nbpLosingLegExit3v1Client = getRandomVantageClientAllFields();
    private static final ClientHelper nbpLosingLegExit3v2Client = getRandomVantageClientAllFields();
    private static final ClientHelper nbpLosingLegExit4Client = getRandomVantageClientAllFields();

    private static RuleDataHelper getNbpLosingLegRuleData(ClientHelper client) {
        RuleDataHelper data = new RuleDataHelper();
        data.clientHelper = client;
        data.crmTbUserObject = generateUserByClient(client);
        data.closeTradeEvent = new CloseTradeMtEvent(
                getRandomUuidString(), Instant.now().toString(), getRandomIntPositive(), client.getTradingAccount(), 100d, "USDCZK", client.getServerId(), "closeTrade"
        );
        data.crmTbAccountObject = generateCrmTbAccountData(client);
        CrmTbBonusObject bonus = generateBonusByClient(client);
        bonus.type = "NEGATIVE_BALANCE_ADJUSTMENT";
        bonus.amountUsd = 100.0;
        data.crmTbBonusObjects.add(bonus);
        Mt5DealsCoercedObject trade1 = generateTradeByClient(client);
        trade1.time = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 1, 0, 0);
        trade1.timeUtc = trade1.time;
        Mt5DealsCoercedObject trade2 = generateTradeByClient(client);
        trade2.time = getCurrentTimestampDbFormat();
        trade2.timeUtc = trade2.time;
        data.mt5DealsObjects.add(trade1);
        data.mt5DealsObjects.add(trade2);
        return data;
    }

    private static RuleDataHelper getNbpLosingLegExit1Data() {
        RuleDataHelper data = getNbpLosingLegRuleData(nbpLosingLegExit1Client);
        Mt5DealsCoercedObject stopoutTrade = generateTradeByClient(data.clientHelper);
        stopoutTrade.time = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 1, 0, 0, 0);
        stopoutTrade.timeUtc = stopoutTrade.time;
        stopoutTrade.comment = "S/O";
        data.mt5DealsObjects.add(stopoutTrade);
        return data;
    }

    private static RuleDataHelper getNbpLosingLegExit2Data() {
        RuleDataHelper data = getNbpLosingLegRuleData(nbpLosingLegExit2Client);
        for (int i = 1; i <= 8; i++) {
            Mt5DealsCoercedObject stopoutTrade = generateTradeByClient(data.clientHelper);
            stopoutTrade.time = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, i, 0, 0, 0);
            stopoutTrade.timeUtc = stopoutTrade.time;
            stopoutTrade.comment = "S/O";
            data.mt5DealsObjects.add(stopoutTrade);
        }
        CrmTbDepositObject deposit = generateDepositByClient(data.clientHelper);
        deposit.amountUsd = 200.0;
        data.crmTbDepositObjects.add(deposit);
        return data;
    }

    private static RuleDataHelper getNbpLosingLegExit3v1Data() {
        RuleDataHelper data = getNbpLosingLegRuleData(nbpLosingLegExit3v1Client);
        for (int i = 1; i <= 8; i++) {
            Mt5DealsCoercedObject stopoutTrade = generateTradeByClient(data.clientHelper);
            stopoutTrade.time = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, i, 0, 0, 0);
            stopoutTrade.timeUtc = stopoutTrade.time;
            stopoutTrade.comment = "S/O";
            data.mt5DealsObjects.add(stopoutTrade);
        }
        CrmTbDepositObject deposit = generateDepositByClient(data.clientHelper);
        deposit.amountUsd = 110.0;
        data.crmTbDepositObjects.add(deposit);
        return data;
    }

    private static RuleDataHelper getNbpLosingLegExit3v2Data() {
        RuleDataHelper data = getNbpLosingLegRuleData(nbpLosingLegExit3v2Client);
        for (int i = 1; i <= 8; i++) {
            Mt5DealsCoercedObject stopoutTrade = generateTradeByClient(data.clientHelper);
            stopoutTrade.time = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, i, 0, 0, 0);
            stopoutTrade.timeUtc = stopoutTrade.time;
            stopoutTrade.comment = "S/O";
            data.mt5DealsObjects.add(stopoutTrade);
        }
        CrmTbDepositObject deposit = generateDepositByClient(data.clientHelper);
        deposit.amountUsd = 110.0;
        data.crmTbDepositObjects.add(deposit);
        ClientHelper mirrorClient = getRandomVantageClientAllFields();
        data.mirrorUcidObjects.add(generateMirrorUcidObjectByClients(data.clientHelper, mirrorClient));
        return data;
    }

    private static RuleDataHelper getNbpLosingLegExit4Data() {
        RuleDataHelper data = getNbpLosingLegRuleData(nbpLosingLegExit4Client);
        for (int i = 1; i <= 8; i++) {
            Mt5DealsCoercedObject stopoutTrade = generateTradeByClient(data.clientHelper);
            stopoutTrade.time = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, i, 0, 0, 0);
            stopoutTrade.timeUtc = stopoutTrade.time;
            stopoutTrade.comment = "S/O";
            data.mt5DealsObjects.add(stopoutTrade);
        }
        CrmTbDepositObject deposit = generateDepositByClient(data.clientHelper);
        deposit.amountUsd = 110.0;
        data.crmTbDepositObjects.add(deposit);
        ClientHelper mirrorClient = getRandomVantageClientAllFields();
        data.mirrorUcidObjects.add(generateMirrorUcidObjectByClients(data.clientHelper, mirrorClient));
        Mt5DealsCoercedObject mirrorTrade = generateTradeByClient(mirrorClient);
        mirrorTrade.profitUsd = 4000.0;
        data.mt5DealsObjects.add(mirrorTrade);
        data.connectedClientHelpers.add(mirrorClient);
        CrmTbDepositObject mirrorDeposit = generateDepositByClient(mirrorClient);
        mirrorDeposit.amountUsd = 100.0;
        mirrorDeposit.status = "Success";
        data.crmTbDepositObjects.add(mirrorDeposit);
        data.crmTbAccountObjectConnections.add(generateCrmTbAccountData(mirrorClient));
        data.connectedUsers.add(generateUserByClient(mirrorClient));
        return data;
    }

    public static Map<String, RuleDataHelper> setupNbpLosingLegRuleData() {
        Map<String, RuleDataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
//        map.put("1", getNbpLosingLegExit1Data());
//        map.put("2", getNbpLosingLegExit2Data());
//        map.put("3v1", getNbpLosingLegExit3v1Data());
        map.put("3v2", getNbpLosingLegExit3v2Data());
//        map.put("4", getNbpLosingLegExit4Data());
        setupRuleData(map);
        return map;
    }

    public static void deleteNbpLosingLegRuleData(Map<String, RuleDataHelper> map) throws Exception {
        deleteRuleData(map);
    }
}
