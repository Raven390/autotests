package helpers.data.rules.no_slippage_rule;

import business_objects.kafka.mt_events.CloseTradeMtEvent;
import business_objects.kafka.mt_events.TradeEventMetadata;
import helpers.data.ClientHelper;
import helpers.data.rules.RuleDataHelper;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static business_objects.db.clickhouse.bo_alerts.BoAlertsFactory.generateAlert;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateAccountByClient;
import static business_objects.db.clickhouse.crm_tb_account_for_mt.crm_tb_account.CrmTbAccountForMtObjectFactory.generateAccountForMtByClient;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.dict_is_test.DictIsTestObjectFactory.generateDictIsTestByClientFalse;
import static business_objects.db.clickhouse.dict_is_test.DictIsTestObjectFactory.generateDictIsTestByClientTrue;
import static business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedFactory.generateMt5DealsCoercedObject;
import static business_objects.db.clickhouse.oz_trades.OzTradesTableEntryFactory.generateOzTradesTableEntryByClient;
import static business_objects.db.clickhouse.s3_fact_ib_sales_commissions.S3FactIbSalesCommissionsFactory.generateS3FactIbSalesCommissionsClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.rules.RuleDataHelper.deleteRuleData;
import static helpers.data.rules.RuleDataHelper.setupRuleData;
import static helpers.database.DbHelper.startSshTunnel;
import static utils.Constants.MT_CLOSE_TRADE_EVENT;
import static utils.Utils.getRandomUuidString;

public class NoSlippageRuleDataFactory {

    private static final ClientHelper noSlippageRuleClient1 = getRandomVantageClientAllFields();
    private static final ClientHelper noSlippageRuleClient2 = getRandomVantageClientAllFields();
    private static final ClientHelper noSlippageRuleClient3 = getRandomVantageClientAllFields();
    private static final ClientHelper noSlippageRuleClient4 = getRandomVantageClientAllFields();
    private static final ClientHelper noSlippageRuleClient5 = getRandomVantageClientAllFields();
    private static final ClientHelper noSlippageRuleClient6 = getRandomVantageClientAllFields();
    private static final ClientHelper noSlippageRuleClient7 = getRandomVantageClientAllFields();
    private static final ClientHelper noSlippageRuleClient8 = getRandomVantageClientAllFields();
    private static final ClientHelper noSlippageRuleClient9 = getRandomVantageClientAllFields();
    private static final ClientHelper noSlippageRuleClient10 = getRandomVantageClientAllFields();
    private static final ClientHelper noSlippageRuleClient11 = getRandomVantageClientAllFields();

    private static RuleDataHelper getNoSlippageRuleData(ClientHelper client) {
        RuleDataHelper ruleData = new RuleDataHelper();

        ruleData.clientHelper = client;
        ruleData.crmTbUserObject = generateUserByClient(client);
        ruleData.crmTbAccountObject = generateAccountByClient(client, false);
        ruleData.crmTbAccountForMtObject = generateAccountForMtByClient(client, false);
        ruleData.mt5DealsCoercedObjects = List.of(generateMt5DealsCoercedObject(client));
        TradeEventMetadata metadata = new TradeEventMetadata("MT5");
        ruleData.closeTradeMtEvent = new CloseTradeMtEvent(
                getRandomUuidString(), Instant.now().toString(), ruleData.mt5DealsCoercedObjects.getFirst().getPositionId(), client.getTradingAccount(), ruleData.mt5DealsCoercedObjects.getFirst().getVolumeLots(), ruleData.mt5DealsCoercedObjects.getFirst().getSymbol(), ruleData.clientHelper.getServerId(), MT_CLOSE_TRADE_EVENT, Instant.now().toString(), metadata, Instant.now().toString());
        return ruleData;
    }

    public static RuleDataHelper getNoSlippageRuleTest1Data() {
        RuleDataHelper data = getNoSlippageRuleData(noSlippageRuleClient1);
        data.dictIsTestObject = generateDictIsTestByClientTrue(data.clientHelper);
        return data;
    }

    public static RuleDataHelper getNoSlippageRuleTest2Data() {
        RuleDataHelper data = getNoSlippageRuleData(noSlippageRuleClient2);
        data.dictIsTestObject = generateDictIsTestByClientFalse(data.clientHelper);
        data.crmTbAccountForMtObject.currency = "USC";
        return data;
    }

    public static RuleDataHelper getNoSlippageRuleTest3Data() {
        RuleDataHelper data = getNoSlippageRuleData(noSlippageRuleClient3);
        data.dictIsTestObject = generateDictIsTestByClientFalse(data.clientHelper);
        data.ozTradesTableObjets = List.of(generateOzTradesTableEntryByClient(data.clientHelper));
        data.ozTradesTableObjets.getFirst().setSlippage(400d);
        data.mt5DealsCoercedObjects.getFirst().setProfitUsd(200d);
        data.mt5DealsCoercedObjects.getFirst().setProfit(200d);
        data.s3FactIbSalesCommissionsObject = List.of(generateS3FactIbSalesCommissionsClient(data.clientHelper));
        data.s3FactIbSalesCommissionsObject.getFirst().setIbCommission(200d);
        data.boAlertsObjects = List.of(generateAlert(data.clientHelper));
        data.boAlertsObjects.getFirst().setRule("No Slippage");
        data.boAlertsObjects.getFirst().setStatus("CLOSED");
        return data;
    }

    public static RuleDataHelper getNoSlippageRuleTest4Data() {
        RuleDataHelper data = getNoSlippageRuleData(noSlippageRuleClient4);
        data.dictIsTestObject = generateDictIsTestByClientFalse(data.clientHelper);
        data.ozTradesTableObjets = List.of(generateOzTradesTableEntryByClient(data.clientHelper));
        data.ozTradesTableObjets.getFirst().setSlippage(400d);
        data.mt5DealsCoercedObjects.getFirst().setProfitUsd(200d);
        data.mt5DealsCoercedObjects.getFirst().setProfit(200d);
        data.s3FactIbSalesCommissionsObject = List.of(generateS3FactIbSalesCommissionsClient(data.clientHelper));
        data.s3FactIbSalesCommissionsObject.getFirst().setIbCommission(200d);
        return data;
    }

    public static RuleDataHelper getNoSlippageRuleTest5Data() {
        RuleDataHelper data = getNoSlippageRuleData(noSlippageRuleClient5);
        return data;
    }

    public static RuleDataHelper getNoSlippageRuleTest6Data() {
        RuleDataHelper data = getNoSlippageRuleData(noSlippageRuleClient6);
        return data;
    }

    public static RuleDataHelper getNoSlippageRuleTest7Data() {
        RuleDataHelper data = getNoSlippageRuleData(noSlippageRuleClient7);
        return data;
    }

    public static RuleDataHelper getNoSlippageRuleTest8Data() {
        RuleDataHelper data = getNoSlippageRuleData(noSlippageRuleClient8);
        return data;
    }

    public static RuleDataHelper getNoSlippageRuleTest9Data() {
        RuleDataHelper data = getNoSlippageRuleData(noSlippageRuleClient9);
        return data;
    }

    public static RuleDataHelper getNoSlippageRuleTest10Data() {
        RuleDataHelper data = getNoSlippageRuleData(noSlippageRuleClient10);
        return data;
    }

    public static RuleDataHelper getNoSlippageRuleTest11Data() {
        RuleDataHelper data = getNoSlippageRuleData(noSlippageRuleClient11);
        return data;
    }

    public static Map<String, RuleDataHelper> setupNoSlippageRuleData() {
        startSshTunnel();
        Map<String, RuleDataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("1", getNoSlippageRuleTest1Data());
        map.put("2", getNoSlippageRuleTest2Data());
        map.put("3", getNoSlippageRuleTest3Data());
        map.put("4", getNoSlippageRuleTest4Data());
//        map.put("5", getNoSlippageRuleTest5Data());
//        map.put("6", getNoSlippageRuleTest6Data());
//        map.put("7", getNoSlippageRuleTest7Data());
//        map.put("8", getNoSlippageRuleTest8Data());
//        map.put("9", getNoSlippageRuleTest9Data());
//        map.put("10", getNoSlippageRuleTest10Data());
//        map.put("11", getNoSlippageRuleTest11Data());

        setupRuleData(map);

        return map;
    }

    public static void deleteNoSlippageRuleData(Map<String, RuleDataHelper> map) throws Exception {
        deleteRuleData(map);
    }
}
