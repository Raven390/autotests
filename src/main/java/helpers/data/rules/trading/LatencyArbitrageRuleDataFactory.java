package helpers.data.rules.trading;

import static business_objects.db.clickhouse.bo_alerts.BoAlertsFactory.generateAlert;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateAccountByClient;
import static business_objects.db.clickhouse.crm_tb_account_for_mt.crm_tb_account.CrmTbAccountForMtObjectFactory.generateAccountForMtByClient;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.dict_active_trading_days_by_ucid.dict_is_test.DictIsTestDictActiveTradingDaysByUcidObjectFactory.generateTradingDaysByClient;
import static business_objects.db.clickhouse.dict_is_test.DictIsTestObjectFactory.generateDictIsTestByClientFalse;
import static business_objects.db.clickhouse.dict_is_test.DictIsTestObjectFactory.generateDictIsTestByClientTrue;
import static business_objects.db.clickhouse.mt_balance_orders_table.MtBalanceOrdersObjectFactory.generateMtBalanceOrder;
import static business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedFactory.generateMt5DealsCoercedObject;
import static business_objects.db.clickhouse.s3_fact_ib_sales_commissions.S3FactIbSalesCommissionsFactory.generateS3FactIbSalesCommissionsClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.DataHelper.addAlert;
import static helpers.data.rules.ShortToxicityInserter.insertShortToxicityOrdersData;
import static helpers.database.DbHelper.*;
import static utils.Utils.*;

import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntityFactory;
import business_objects.kafka.mt_events.CloseTradeMtEvent;
import business_objects.kafka.mt_events.TradeEventMetadata;
import generator.annotations.RuleTestData;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import helpers.data.enums.rule_engine.Event;
import io.qameta.allure.Description;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RuleTestData("latency-arbitrage")
public class LatencyArbitrageRuleDataFactory {

    private static final ClientHelper latencyArbitrageTest1Client = getRandomVantageClientAllFields();
    private static final ClientHelper latencyArbitrageTest2Client = getRandomVantageClientAllFields();
    private static final ClientHelper latencyArbitrageTest3Client = getRandomVantageClientAllFields();
    private static final ClientHelper latencyArbitrageTest4Client = getRandomVantageClientAllFields();
    private static final ClientHelper latencyArbitrageTest5Client = getRandomVantageClientAllFields();
    private static final ClientHelper latencyArbitrageTest6Client = getRandomVantageClientAllFields();
    private static final ClientHelper latencyArbitrageTest7Client = getRandomVantageClientAllFields();
    private static final ClientHelper latencyArbitrageTest8Client = getRandomVantageClientAllFields();
    private static final ClientHelper latencyArbitrageTest9Client = getRandomVantageClientAllFields();

    @Description("Create base test data for Latency arbitrage rule")
    private static DataHelper getLatencyArbitrageRuleData(ClientHelper client) {
        DataHelper ruleData = new DataHelper();
        ruleData.clientHelper = client;
        ruleData.crmTbUserObject = generateUserByClient(client);
        ruleData.crmTbAccountObject = generateAccountByClient(client, false);
        ruleData.crmTbAccountForMtObject = generateAccountForMtByClient(client, false);
        ruleData.mt5DealsCoercedObjects = List.of(generateMt5DealsCoercedObject(client));
        TradeEventMetadata metadata = new TradeEventMetadata("MT5");
        ruleData.closeTradeMtEvent = new CloseTradeMtEvent(
                getRandomUuidString(),
                Instant.now().toString(),
                ruleData.mt5DealsCoercedObjects.getFirst().getPositionId(),
                client.getTradingAccount(),
                ruleData.mt5DealsCoercedObjects.getFirst().getVolumeLots(),
                ruleData.mt5DealsCoercedObjects.getFirst().getSymbol(),
                ruleData.clientHelper.getServerId(),
                Event.MT_CLOSE_TRADE_EVENT.getName(),
                Instant.now().toString(),
                metadata,
                Instant.now().toString());
        return ruleData;
    }

    @Description("Latency arbitrage rule rule. Exit without alert if user is a test/st user")
    private static DataHelper getLatencyArbitrageRuleTest1Data() {
        DataHelper data = getLatencyArbitrageRuleData(latencyArbitrageTest1Client);
        data.dictIsTestObject = generateDictIsTestByClientTrue(data.clientHelper);
        return data;
    }

    @Description("Latency arbitrage rule. Exit without alert if user has resolved alerts. ElementId: Event_end_12")
    private static DataHelper getLatencyArbitrageRuleTest2Data() throws Exception {
        DataHelper data = getLatencyArbitrageRuleData(latencyArbitrageTest2Client);

        data.dictIsTestObject = generateDictIsTestByClientFalse(data.clientHelper);
        data.dictActiveTradingDaysByUcidObject = generateTradingDaysByClient(data.clientHelper, 1);
        // Generate deals
        data.mt5DealsCoercedObjects =
                generateMt5DealsCoercedObject(data.clientHelper, 101, getCurrentTimestampDbFormat());
        data.mt5DealsCoercedObjects.getFirst().setProfit(500d);
        data.mt5DealsCoercedObjects.getFirst().setProfitUsd(500d);
        // Generate ib commission
        data.s3FactIbSalesCommissionsObject = List.of(generateS3FactIbSalesCommissionsClient(data.clientHelper));
        data.s3FactIbSalesCommissionsObject.getFirst().setSalesCommission(300d);
        data.s3FactIbSalesCommissionsObject.getFirst().setIbCommission(300d);
        // Generate cumulative deposit data
        data.mtBalanceOrdersObjects =
                List.of(generateMtBalanceOrder(data.clientHelper, 0d, 0d, getCurrentTimestampDbFormat()));
        data.mtBalanceOrdersObjects.getFirst().comment = "deposit";
        data.mtBalanceOrdersObjects.getFirst().amount = 100d;
        data.mtBalanceOrdersObjects.getFirst().amountUsd = 100d;

        insertShortToxicityOrdersData(data.clientHelper);

        data.boAlertsObjects = List.of(generateAlert(data.clientHelper));
        data.boAlertsObjects.getFirst().setRule("Latency Arbitrage");
        data.boAlertsObjects.getFirst().setStatus("CLOSED");
        return data;
    }

    @Description("Latency arbitrage. Exit without alert if user has less that 10 trading days")
    private static DataHelper getLatencyArbitrageRuleTest3Data() throws InterruptedException {
        DataHelper data = getLatencyArbitrageRuleData(latencyArbitrageTest3Client);

        data.dictIsTestObject = generateDictIsTestByClientFalse(data.clientHelper);
        data.dictActiveTradingDaysByUcidObject = generateTradingDaysByClient(data.clientHelper, 1);
        // Generate deals
        data.mt5DealsCoercedObjects =
                generateMt5DealsCoercedObject(data.clientHelper, 101, getCurrentTimestampDbFormat());
        data.mt5DealsCoercedObjects.getFirst().setProfit(500d);
        data.mt5DealsCoercedObjects.getFirst().setProfitUsd(500d);
        // Generate ib commission
        data.s3FactIbSalesCommissionsObject = List.of(generateS3FactIbSalesCommissionsClient(data.clientHelper));
        data.s3FactIbSalesCommissionsObject.getFirst().setSalesCommission(300d);
        data.s3FactIbSalesCommissionsObject.getFirst().setIbCommission(300d);
        // Generate cumulative deposit data
        data.mtBalanceOrdersObjects =
                List.of(generateMtBalanceOrder(data.clientHelper, 0d, 0d, getCurrentTimestampDbFormat()));
        data.mtBalanceOrdersObjects.getFirst().comment = "deposit";
        data.mtBalanceOrdersObjects.getFirst().amount = 100d;
        data.mtBalanceOrdersObjects.getFirst().amountUsd = 100d;

        insertShortToxicityOrdersData(data.clientHelper);

        return data;
    }

    @Description(
            "Latency arbitrage rule. Rebate Latency Branch. Total Profit / Cumulative deposit =< 0.2. ElementId: Event_1jau96v")
    private static DataHelper getLatencyArbitrageRuleTest4Data() {
        DataHelper data = getLatencyArbitrageRuleData(latencyArbitrageTest4Client);
        data.dictIsTestObject = generateDictIsTestByClientFalse(data.clientHelper);
        data.dictActiveTradingDaysByUcidObject = generateTradingDaysByClient(data.clientHelper, 11);
        return data;
    }

    @Description(
            "Latency arbitrage rule. Rebate Latency Branch. rebates(client) / profit(client) < 0.3?. ElementId: Event_0cyuekk")
    private static DataHelper getLatencyArbitrageRuleTest5Data() {
        DataHelper data = getLatencyArbitrageRuleData(latencyArbitrageTest5Client);

        data.dictIsTestObject = generateDictIsTestByClientFalse(data.clientHelper);
        data.dictActiveTradingDaysByUcidObject = generateTradingDaysByClient(data.clientHelper, 1);
        data.mt5DealsCoercedObjects = generateMt5DealsCoercedObject(data.clientHelper, 101);
        data.mt5DealsCoercedObjects.getFirst().setProfit(500d);
        data.mt5DealsCoercedObjects.getFirst().setProfitUsd(500d);
        data.crmTbDepositObjects =
                List.of(CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(data.clientHelper));
        // Generate cumulative deposit data
        data.mtBalanceOrdersObjects =
                List.of(generateMtBalanceOrder(data.clientHelper, 0d, 0d, getCurrentTimestampDbFormat()));
        data.mtBalanceOrdersObjects.getFirst().comment = "deposit";
        data.mtBalanceOrdersObjects.getFirst().amount = 100d;
        data.mtBalanceOrdersObjects.getFirst().amountUsd = 100d;

        return data;
    }

    @Description("Latency arbitrage rule. notionalValue(ucid) < 10 000 000. ElementId: Event_04k2uu4")
    private static DataHelper getLatencyArbitrageRuleTest6Data() {
        DataHelper data = getLatencyArbitrageRuleData(latencyArbitrageTest6Client);

        data.dictIsTestObject = generateDictIsTestByClientFalse(data.clientHelper);
        data.dictActiveTradingDaysByUcidObject = generateTradingDaysByClient(data.clientHelper, 1);
        data.mt5DealsCoercedObjects = generateMt5DealsCoercedObject(data.clientHelper, 101);
        data.mt5DealsCoercedObjects.getFirst().setProfit(500d);
        data.mt5DealsCoercedObjects.getFirst().setProfitUsd(500d);
        data.crmTbDepositObjects =
                List.of(CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(data.clientHelper));
        // Generate cumulative deposit data
        data.mtBalanceOrdersObjects =
                List.of(generateMtBalanceOrder(data.clientHelper, 0d, 0d, getCurrentTimestampDbFormat()));
        data.mtBalanceOrdersObjects.getFirst().comment = "deposit";
        data.mtBalanceOrdersObjects.getFirst().amount = 100d;
        data.mtBalanceOrdersObjects.getFirst().amountUsd = 100d;
        data.s3FactIbSalesCommissionsObject = List.of(generateS3FactIbSalesCommissionsClient(data.clientHelper));
        data.s3FactIbSalesCommissionsObject.getFirst().setIbCommission(100_000.0);

        return data;
    }

    @Description(
            "Latency arbitrage rule. max(maxNotionalValue.maxDailyNotionalValueUSD) / sum(notionalValue.notionalValueAmountUSD) > 0.6. ElementId: Event_0byoyft")
    private static DataHelper getLatencyArbitrageRuleTest7Data() {
        DataHelper data = getLatencyArbitrageRuleData(latencyArbitrageTest7Client);

        data.dictIsTestObject = generateDictIsTestByClientFalse(data.clientHelper);
        data.dictActiveTradingDaysByUcidObject = generateTradingDaysByClient(data.clientHelper, 1);
        data.mt5DealsCoercedObjects = generateMt5DealsCoercedObject(data.clientHelper, 101);
        data.mt5DealsCoercedObjects.getFirst().setProfit(500d);
        data.mt5DealsCoercedObjects.getFirst().setProfitUsd(500d);
        data.crmTbDepositObjects =
                List.of(CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(data.clientHelper));
        // Generate cumulative deposit data
        data.mtBalanceOrdersObjects =
                List.of(generateMtBalanceOrder(data.clientHelper, 0d, 0d, getCurrentTimestampDbFormat()));
        data.mtBalanceOrdersObjects.getFirst().comment = "deposit";
        data.mtBalanceOrdersObjects.getFirst().amount = 100d;
        data.mtBalanceOrdersObjects.getFirst().amountUsd = 100d;
        data.s3FactIbSalesCommissionsObject = List.of(generateS3FactIbSalesCommissionsClient(data.clientHelper));
        data.s3FactIbSalesCommissionsObject.getFirst().setIbCommission(100_000.0);
        data.mt5DealsCoercedObjects.getFirst().setNotionalValueUsd(30_000_001.00);
        data.mt5DealsCoercedObjects.get(1).setNotionalValueUsd(20_000_001.00);

        return data;
    }

    @Description("Latency arbitrage rule. Rebate Latency Branch. At least 1 resolved Alert. ElementId: Event_0u8x3op")
    private static DataHelper getLatencyArbitrageRuleTest8Data() {
        DataHelper data = getLatencyArbitrageRuleData(latencyArbitrageTest8Client);

        data.dictIsTestObject = generateDictIsTestByClientFalse(data.clientHelper);
        data.dictActiveTradingDaysByUcidObject = generateTradingDaysByClient(data.clientHelper, 1);
        data.mt5DealsCoercedObjects = generateMt5DealsCoercedObject(data.clientHelper, 101);
        data.mt5DealsCoercedObjects.getFirst().setProfit(500d);
        data.mt5DealsCoercedObjects.getFirst().setProfitUsd(500d);
        data.crmTbDepositObjects =
                List.of(CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(data.clientHelper));
        // Generate cumulative deposit data
        data.mtBalanceOrdersObjects =
                List.of(generateMtBalanceOrder(data.clientHelper, 0d, 0d, getCurrentTimestampDbFormat()));
        data.mtBalanceOrdersObjects.getFirst().comment = "deposit";
        data.mtBalanceOrdersObjects.getFirst().amount = 100d;
        data.mtBalanceOrdersObjects.getFirst().amountUsd = 100d;
        data.s3FactIbSalesCommissionsObject = List.of(generateS3FactIbSalesCommissionsClient(data.clientHelper));
        data.s3FactIbSalesCommissionsObject.getFirst().setIbCommission(100_000.0);
        data.mt5DealsCoercedObjects.getFirst().setNotionalValueUsd(1_000_000_100.00);
        addAlert(data, "Latency Arbitrage", "CLOSED");

        return data;
    }

    @Description("Latency arbitrage rule. Rebate Latency Branch. No resolved Alerts. ElementId: Event_04a1zpc")
    private static DataHelper getLatencyArbitrageRuleTest9Data() {
        DataHelper data = getLatencyArbitrageRuleData(latencyArbitrageTest9Client);

        data.dictIsTestObject = generateDictIsTestByClientFalse(data.clientHelper);
        data.dictActiveTradingDaysByUcidObject = generateTradingDaysByClient(data.clientHelper, 1);
        data.mt5DealsCoercedObjects = generateMt5DealsCoercedObject(data.clientHelper, 101);
        data.mt5DealsCoercedObjects.getFirst().setProfit(500d);
        data.mt5DealsCoercedObjects.getFirst().setProfitUsd(500d);
        data.crmTbDepositObjects =
                List.of(CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(data.clientHelper));
        // Generate cumulative deposit data
        data.mtBalanceOrdersObjects =
                List.of(generateMtBalanceOrder(data.clientHelper, 0d, 0d, getCurrentTimestampDbFormat()));
        data.mtBalanceOrdersObjects.getFirst().comment = "deposit";
        data.mtBalanceOrdersObjects.getFirst().amount = 100d;
        data.mtBalanceOrdersObjects.getFirst().amountUsd = 100d;
        data.s3FactIbSalesCommissionsObject = List.of(generateS3FactIbSalesCommissionsClient(data.clientHelper));
        data.s3FactIbSalesCommissionsObject.getFirst().setIbCommission(100_000.0);
        data.mt5DealsCoercedObjects.getFirst().setNotionalValueUsd(1_000_000_100.00);

        return data;
    }

    public static Map<String, DataHelper> setupLatencyArbitrageData() throws Exception {
        startSshTunnel();
        Map<String, DataHelper> map = new HashMap<>();
        // Put all the db data for setup in a list
        map.put("1", getLatencyArbitrageRuleTest1Data());
        map.put("2", getLatencyArbitrageRuleTest2Data());
        map.put("3", getLatencyArbitrageRuleTest3Data());
        map.put("4", getLatencyArbitrageRuleTest4Data());
        map.put("5", getLatencyArbitrageRuleTest5Data());
        map.put("6", getLatencyArbitrageRuleTest6Data());
        map.put("7", getLatencyArbitrageRuleTest7Data());
        map.put("8", getLatencyArbitrageRuleTest8Data());
        map.put("9", getLatencyArbitrageRuleTest9Data());
        return map;
    }
}
