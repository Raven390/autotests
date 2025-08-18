package helpers.data.rules.latency_arbitrage_rule;

import business_objects.kafka.mt_events.CloseTradeMtEvent;
import business_objects.kafka.mt_events.TradeEventMetadata;
import generator.annotations.RuleTestData;
import helpers.data.ClientHelper;
import helpers.data.rules.RuleDataHelper;
import io.qameta.allure.Step;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateAccountByClient;
import static business_objects.db.clickhouse.crm_tb_account_for_mt.crm_tb_account.CrmTbAccountForMtObjectFactory.generateAccountForMtByClient;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.dict_active_trading_days_by_ucid.dict_is_test.DictIsTestDictActiveTradingDaysByUcidObjectFactory.generateTradingDaysByClient;
import static business_objects.db.clickhouse.dict_is_test.DictIsTestObjectFactory.generateDictIsTestByClientFalse;
import static business_objects.db.clickhouse.dict_is_test.DictIsTestObjectFactory.generateDictIsTestByClientTrue;
import static business_objects.db.clickhouse.mt_balance_orders_table.MtBalanceOrdersObjectFactory.generateMtBalanceOrders;
import static business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedFactory.generateMt5DealsCoercedObject;
import static business_objects.db.clickhouse.s3_fact_ib_sales_commissions.S3FactIbSalesCommissionsFactory.generateS3FactIbSalesCommissionsClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.rules.RuleDataHelper.setupRuleData;
import static helpers.database.DbHelper.*;
import static utils.Constants.*;
import static utils.Utils.*;


@RuleTestData("latency-arbitrage")
public class LatencyArbitrageRuleDataFactory {

    private static final ClientHelper latencyArbitrageTest1Client = getRandomVantageClientAllFields();
    private static final ClientHelper latencyArbitrageTest2Client = getRandomVantageClientAllFields();
    private static final ClientHelper latencyArbitrageTest3Client = getRandomVantageClientAllFields();
    private static final ClientHelper latencyArbitrageTest4Client = getRandomVantageClientAllFields();
    private static final ClientHelper latencyArbitrageTest5Client = getRandomVantageClientAllFields();
    private static final ClientHelper latencyArbitrageTest6Client = getRandomVantageClientAllFields();
    private static final ClientHelper latencyArbitrageTest7Client = getRandomVantageClientAllFields();

    @Step("Create base test data for Latency arbitrage rule")
    public static RuleDataHelper getLatencyArbitrageRuleData(ClientHelper client) {
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


    @Step("Exit from rule without alert if Platform is not MT5")
    public static RuleDataHelper getLatencyArbitrageRuleTest1Data() {
        RuleDataHelper data = getLatencyArbitrageRuleData(latencyArbitrageTest1Client);
        data.closeTradeMtEvent.metadata = new TradeEventMetadata("MT4");
        return data;
    }

    @Step("Exit from rule without alert if user is test or social trader user")
    public static RuleDataHelper getLatencyArbitrageRuleTest2Data() {
        RuleDataHelper data = getLatencyArbitrageRuleData(latencyArbitrageTest2Client);
        data.dictIsTestObject = generateDictIsTestByClientTrue(data.clientHelper);
        return data;
    }

    @Step("Latency arbitrage. Exit without alert if user has less that 10 trading days")
    public static RuleDataHelper getLatencyArbitrageRuleTest3Data() {
        RuleDataHelper data = getLatencyArbitrageRuleData(latencyArbitrageTest3Client);
        data.dictIsTestObject = generateDictIsTestByClientFalse(data.clientHelper);
        data.dictActiveTradingDaysByUcidObject = generateTradingDaysByClient(data.clientHelper, 11);
        return data;
    }

    @Step("Latency arbitrage. Exit without alert if user has less that 100 trades")
    public static RuleDataHelper getLatencyArbitrageRuleTest4Data() {
        RuleDataHelper data = getLatencyArbitrageRuleData(latencyArbitrageTest4Client);
        data.dictIsTestObject = generateDictIsTestByClientFalse(data.clientHelper);
        data.dictActiveTradingDaysByUcidObject = generateTradingDaysByClient(data.clientHelper, 9);
        data.mt5DealsCoercedObjects = generateMt5DealsCoercedObject(data.clientHelper, 10, getCurrentTimestampDbFormat());
        return data;
    }

    @Step("Latency arbitrage. Exit without alert if netProfit + rebatesAmount not >= 500?")
    public static RuleDataHelper getLatencyArbitrageRuleTest5Data() {
        RuleDataHelper data = getLatencyArbitrageRuleData(latencyArbitrageTest5Client);
        data.dictIsTestObject = generateDictIsTestByClientFalse(data.clientHelper);
        data.dictActiveTradingDaysByUcidObject = generateTradingDaysByClient(data.clientHelper, 9);
        data.mt5DealsCoercedObjects = generateMt5DealsCoercedObject(data.clientHelper, 100, getCurrentTimestampDbFormat());
        data.s3FactIbSalesCommissionsObject = List.of(generateS3FactIbSalesCommissionsClient(data.clientHelper));
        data.s3FactIbSalesCommissionsObject.getFirst().setSalesCommission(1d);
        data.s3FactIbSalesCommissionsObject.getFirst().setIbCommission(1d);
        return data;
    }

    @Step("Latency arbitrage. Exit without alert if Total Profit / Cumulative deposit not >= 0.3")
    public static RuleDataHelper getLatencyArbitrageRuleTest6Data() {
        RuleDataHelper data = getLatencyArbitrageRuleData(latencyArbitrageTest6Client);
        data.dictIsTestObject = generateDictIsTestByClientFalse(data.clientHelper);
        data.dictActiveTradingDaysByUcidObject = generateTradingDaysByClient(data.clientHelper, 1);
        data.mt5DealsCoercedObjects = generateMt5DealsCoercedObject(data.clientHelper, 101, getCurrentTimestampDbFormat());
        data.s3FactIbSalesCommissionsObject = List.of(generateS3FactIbSalesCommissionsClient(data.clientHelper));
        data.s3FactIbSalesCommissionsObject.getFirst().setSalesCommission(300d);
        data.s3FactIbSalesCommissionsObject.getFirst().setIbCommission(300d);
        return data;
    }

    @Step("Latency arbitrage. Exit without alert if shortToxicity / ((netProfit + rebatesAmount) * 100) not >= 80")
    public static RuleDataHelper getLatencyArbitrageRuleTest7Data() {
        RuleDataHelper data = getLatencyArbitrageRuleData(latencyArbitrageTest7Client);
        data.dictIsTestObject = generateDictIsTestByClientFalse(data.clientHelper);
        data.dictActiveTradingDaysByUcidObject = generateTradingDaysByClient(data.clientHelper, 1);
        // Generate deals
        data.mt5DealsCoercedObjects = generateMt5DealsCoercedObject(data.clientHelper, 101, getCurrentTimestampDbFormat());
        // Generate ib commission
        data.s3FactIbSalesCommissionsObject = List.of(generateS3FactIbSalesCommissionsClient(data.clientHelper));
        data.s3FactIbSalesCommissionsObject.getFirst().setSalesCommission(300d);
        data.s3FactIbSalesCommissionsObject.getFirst().setIbCommission(300d);
        // Generate cumulative deposit data
        data.mtBalanceOrdersObjects = List.of(generateMtBalanceOrders(data.clientHelper, 0d, 0d, getCurrentTimestampDbFormat()));
        data.mtBalanceOrdersObjects.getFirst().comment = "deposit";
        data.mtBalanceOrdersObjects.getFirst().amount = 100d;
        data.mtBalanceOrdersObjects.getFirst().amountUsd = 100d;
        return data;
    }

    public static Map<String, RuleDataHelper> setupLatencyArbitrageData() {
        startSshTunnel();
        Map<String, RuleDataHelper> map = new HashMap<>();
        // Put all the db data for setup in a list
        map.put("1", getLatencyArbitrageRuleTest1Data());
        map.put("2", getLatencyArbitrageRuleTest2Data());
        map.put("3", getLatencyArbitrageRuleTest3Data());
        map.put("4", getLatencyArbitrageRuleTest4Data());
        map.put("5", getLatencyArbitrageRuleTest5Data());
        map.put("6", getLatencyArbitrageRuleTest6Data());
        map.put("7", getLatencyArbitrageRuleTest7Data());

        setupRuleData(map);

        return map;
    }

}
