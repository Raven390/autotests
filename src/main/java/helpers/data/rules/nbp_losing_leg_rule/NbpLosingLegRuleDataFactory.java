package helpers.data.rules.nbp_losing_leg_rule;

import business_objects.db.clickhouse.crm_tb_bonus_table.CrmTbBonusObject;
import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositObject;
import business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedObject;
import business_objects.kafka.mt_events.CloseTradeMtEvent;
import generator.annotations.RuleTestData;
import helpers.data.ClientHelper;
import helpers.data.rules.RuleDataHelper;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountData;
import static business_objects.db.clickhouse.crm_tb_bonus_table.CrmTbBonusObjectFactory.generateBonusByClient;
import static business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositObjectFactory.generateDepositByClient;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.mirror_ucid_table.MirrorUcidObjectFactory.generateMirrorUcidObjectByClients;
import static business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedFactory.generateTradeByClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.DateTimeFormat.DATE_AND_TIME;
import static helpers.data.rules.RuleDataHelper.deleteRuleData;
import static helpers.data.rules.RuleDataHelper.setupRuleData;
import static utils.Utils.*;

@RuleTestData("nbp-losing-leg")
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
                getRandomUuidString(), Instant.now().toString(), getRandomIntPositive().longValue(), client.getTradingAccount(), 100d, "USDCZK", client.getServerId(), "closeTrade"
        );
        data.crmTbAccountObject = generateCrmTbAccountData(client);
        CrmTbBonusObject bonus = generateBonusByClient(client);
        bonus.type = "NEGATIVE_BALANCE_ADJUSTMENT";
        bonus.amountUsd = 100.0;
        data.crmTbBonusObjects.add(bonus);
        Mt5DealsCoercedObject trade1 = generateTradeByClient(client);
        trade1.setTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 1, 0, 0));
        trade1.setTimeUtc(trade1.getTime());
        Mt5DealsCoercedObject trade2 = generateTradeByClient(client);
        trade2.setTime(getCurrentTimestampDbFormat());
        trade2.setTimeUtc(trade2.getTime());
        data.mt5DealsCoercedObjects.add(trade1);
        data.mt5DealsCoercedObjects.add(trade2);
        return data;
    }

    private static RuleDataHelper getNbpLosingLegExit1Data() {
        RuleDataHelper data = getNbpLosingLegRuleData(nbpLosingLegExit1Client);
        Mt5DealsCoercedObject stopoutTrade = generateTradeByClient(data.clientHelper);
        stopoutTrade.setTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 1, 0, 0, 0));
        stopoutTrade.setTimeUtc(stopoutTrade.getTime());
        stopoutTrade.setComment("S/O");
        data.mt5DealsCoercedObjects.add(stopoutTrade);
        return data;
    }

    private static RuleDataHelper getNbpLosingLegExit2Data() {
        RuleDataHelper data = getNbpLosingLegRuleData(nbpLosingLegExit2Client);
        for (int i = 1; i <= 8; i++) {
            Mt5DealsCoercedObject stopoutTrade = generateTradeByClient(data.clientHelper);
            stopoutTrade.setTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, i, 0, 0, 0));
            stopoutTrade.setTimeUtc(stopoutTrade.getTime());
            stopoutTrade.setComment("S/O");
            data.mt5DealsCoercedObjects.add(stopoutTrade);
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
            stopoutTrade.setTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, i, 0, 0, 0));
            stopoutTrade.setTimeUtc(stopoutTrade.getTime());
            stopoutTrade.setComment("S/O");
            data.mt5DealsCoercedObjects.add(stopoutTrade);
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
            stopoutTrade.setTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, i, 0, 0, 0));
            stopoutTrade.setTimeUtc(stopoutTrade.getTime());
            stopoutTrade.setComment("S/O");
            data.mt5DealsCoercedObjects.add(stopoutTrade);
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
            stopoutTrade.setTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, i, 0, 0, 0));
            stopoutTrade.setTimeUtc(stopoutTrade.getTime());
            stopoutTrade.setComment("S/O");
            data.mt5DealsCoercedObjects.add(stopoutTrade);
        }
        CrmTbDepositObject deposit = generateDepositByClient(data.clientHelper);
        deposit.amountUsd = 110.0;
        data.crmTbDepositObjects.add(deposit);
        ClientHelper mirrorClient = getRandomVantageClientAllFields();
        data.mirrorUcidObjects.add(generateMirrorUcidObjectByClients(data.clientHelper, mirrorClient));
        Mt5DealsCoercedObject mirrorTrade = generateTradeByClient(mirrorClient);
        mirrorTrade.setProfitUsd(4000.0);
        data.mt5DealsCoercedObjects.add(mirrorTrade);
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
        map.put("1", getNbpLosingLegExit1Data());
        map.put("2", getNbpLosingLegExit2Data());
        map.put("3v1", getNbpLosingLegExit3v1Data());
        map.put("3v2", getNbpLosingLegExit3v2Data());
        map.put("4", getNbpLosingLegExit4Data());
        setupRuleData(map);
        return map;
    }

    public static void deleteNbpLosingLegRuleData(Map<String, RuleDataHelper> map) throws Exception {
        deleteRuleData(map);
    }
}
