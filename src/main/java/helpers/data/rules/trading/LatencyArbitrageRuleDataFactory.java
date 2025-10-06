package helpers.data.rules.trading;

import business_objects.kafka.mt_events.CloseTradeMtEvent;
import business_objects.kafka.mt_events.TradeEventMetadata;
import generator.annotations.RuleTestData;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import io.qameta.allure.Step;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
import static helpers.data.DataHelper.setupData;
import static helpers.data.rules.ShortToxicityInserter.insertShortToxicityOrdersData;
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
    private static DataHelper getLatencyArbitrageRuleData(ClientHelper client) {
        DataHelper ruleData = new DataHelper();

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


    @Step("Latency arbitrage rule rule. Exit without alert if user is a test/st user")
    private static DataHelper getLatencyArbitrageRuleTest1Data() {
        DataHelper data = getLatencyArbitrageRuleData(latencyArbitrageTest1Client);
        data.dictIsTestObject = generateDictIsTestByClientTrue(data.clientHelper);
        return data;
    }

    @Step("Latency arbitrage rule. Exit without alert if user has resolved alerts. ElementId: Event_end_12")
    private static DataHelper getLatencyArbitrageRuleTest2Data() throws Exception {
        DataHelper data = getLatencyArbitrageRuleData(latencyArbitrageTest2Client);

        data.dictIsTestObject = generateDictIsTestByClientFalse(data.clientHelper);
        data.dictActiveTradingDaysByUcidObject = generateTradingDaysByClient(data.clientHelper, 1);
        // Generate deals
        data.mt5DealsCoercedObjects = generateMt5DealsCoercedObject(data.clientHelper, 101, getCurrentTimestampDbFormat());
        data.mt5DealsCoercedObjects.getFirst().setProfit(500d);
        data.mt5DealsCoercedObjects.getFirst().setProfitUsd(500d);
        // Generate ib commission
        data.s3FactIbSalesCommissionsObject = List.of(generateS3FactIbSalesCommissionsClient(data.clientHelper));
        data.s3FactIbSalesCommissionsObject.getFirst().setSalesCommission(300d);
        data.s3FactIbSalesCommissionsObject.getFirst().setIbCommission(300d);
        // Generate cumulative deposit data
        data.mtBalanceOrdersObjects = List.of(
                generateMtBalanceOrder(data.clientHelper, 0d, 0d, getCurrentTimestampDbFormat()));
        data.mtBalanceOrdersObjects.getFirst().comment = "deposit";
        data.mtBalanceOrdersObjects.getFirst().amount = 100d;
        data.mtBalanceOrdersObjects.getFirst().amountUsd = 100d;

        insertShortToxicityOrdersData(data.clientHelper);

        data.boAlertsObjects = List.of(generateAlert(data.clientHelper));
        data.boAlertsObjects.getFirst().setRule("Latency Arbitrage");
        data.boAlertsObjects.getFirst().setStatus("CLOSED");
        return data;
    }

    @Step("Latency arbitrage. Exit without alert if user has less that 10 trading days")
    private static DataHelper getLatencyArbitrageRuleTest3Data() throws InterruptedException {
        DataHelper data = getLatencyArbitrageRuleData(latencyArbitrageTest3Client);

        data.dictIsTestObject = generateDictIsTestByClientFalse(data.clientHelper);
        data.dictActiveTradingDaysByUcidObject = generateTradingDaysByClient(data.clientHelper, 1);
        // Generate deals
        data.mt5DealsCoercedObjects = generateMt5DealsCoercedObject(data.clientHelper, 101, getCurrentTimestampDbFormat());
        data.mt5DealsCoercedObjects.getFirst().setProfit(500d);
        data.mt5DealsCoercedObjects.getFirst().setProfitUsd(500d);
        // Generate ib commission
        data.s3FactIbSalesCommissionsObject = List.of(generateS3FactIbSalesCommissionsClient(data.clientHelper));
        data.s3FactIbSalesCommissionsObject.getFirst().setSalesCommission(300d);
        data.s3FactIbSalesCommissionsObject.getFirst().setIbCommission(300d);
        // Generate cumulative deposit data
        data.mtBalanceOrdersObjects = List.of(
                generateMtBalanceOrder(data.clientHelper, 0d, 0d, getCurrentTimestampDbFormat()));
        data.mtBalanceOrdersObjects.getFirst().comment = "deposit";
        data.mtBalanceOrdersObjects.getFirst().amount = 100d;
        data.mtBalanceOrdersObjects.getFirst().amountUsd = 100d;

        insertShortToxicityOrdersData(data.clientHelper);

        return data;
    }

    public static Map<String, DataHelper> setupLatencyArbitrageData() throws Exception {
        startSshTunnel();
        Map<String, DataHelper> map = new HashMap<>();
        // Put all the db data for setup in a list
        map.put("1", getLatencyArbitrageRuleTest1Data());
        map.put("2", getLatencyArbitrageRuleTest2Data());
        map.put("3", getLatencyArbitrageRuleTest3Data());

        setupData(map);

        return map;
    }

}
