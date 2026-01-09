package helpers.data.rules.trading;

import static business_objects.db.clickhouse.bo_alerts.BoAlertsFactory.generateAlert;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateAccountByClient;
import static business_objects.db.clickhouse.crm_tb_account_for_mt.crm_tb_account.CrmTbAccountForMtObjectFactory.generateAccountForMtByClient;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.dict_is_test.DictIsTestObjectFactory.generateDictIsTestByClientFalse;
import static business_objects.db.clickhouse.dict_is_test.DictIsTestObjectFactory.generateDictIsTestByClientTrue;
import static business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedFactory.generateMt5DealsCoercedObject;
import static business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedFactory.generateTradeByClient;
import static business_objects.db.clickhouse.oz_trades.OzTradesTableEntryFactory.generateOzTradesTableEntryByClient;
import static business_objects.db.clickhouse.s3_fact_ib_sales_commissions.S3FactIbSalesCommissionsFactory.generateS3FactIbSalesCommissionsClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.DbHelper.startSshTunnel;
import static utils.Constants.EURUSD;
import static utils.Constants.MT_CLOSE_TRADE_EVENT;
import static utils.Utils.getCurrentTimestampMinusOffsetFormatted;
import static utils.Utils.getRandomUuidString;

import business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedObject;
import business_objects.kafka.mt_events.CloseTradeMtEvent;
import business_objects.kafka.mt_events.TradeEventMetadata;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import helpers.data.enums.DateTimeFormat;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private static final ClientHelper noSlippageRuleClient12 = getRandomVantageClientAllFields();

    private static DataHelper getNoSlippageRuleData(ClientHelper client) {
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
                MT_CLOSE_TRADE_EVENT,
                Instant.now().toString(),
                metadata,
                Instant.now().toString());
        return ruleData;
    }

    private static DataHelper getNoSlippageRuleTest1Data() {
        DataHelper data = getNoSlippageRuleData(noSlippageRuleClient1);
        data.dictIsTestObject = generateDictIsTestByClientTrue(data.clientHelper);
        return data;
    }

    private static DataHelper getNoSlippageRuleTest2Data() {
        DataHelper data = getNoSlippageRuleData(noSlippageRuleClient2);
        data.dictIsTestObject = generateDictIsTestByClientFalse(data.clientHelper);
        data.crmTbAccountForMtObject.setCurrency("USC");
        return data;
    }

    private static DataHelper getNoSlippageRuleTest3Data() {
        DataHelper data = getNoSlippageRuleData(noSlippageRuleClient3);
        data.dictIsTestObject = generateDictIsTestByClientFalse(data.clientHelper);
        data.ozTradesTableObjects = List.of(generateOzTradesTableEntryByClient(data.clientHelper));
        data.ozTradesTableObjects.getFirst().setSlippage(400d);
        data.mt5DealsCoercedObjects.getFirst().setProfitUsd(200d);
        data.mt5DealsCoercedObjects.getFirst().setProfit(200d);
        data.s3FactIbSalesCommissionsObject = List.of(generateS3FactIbSalesCommissionsClient(data.clientHelper));
        data.s3FactIbSalesCommissionsObject.getFirst().setIbCommission(200d);
        data.boAlertsObjects = List.of(generateAlert(data.clientHelper));
        data.boAlertsObjects.getFirst().setRule("No Slippage");
        data.boAlertsObjects.getFirst().setStatus("CLOSED");
        return data;
    }

    private static DataHelper getNoSlippageRuleTest4Data() {
        DataHelper data = getNoSlippageRuleData(noSlippageRuleClient4);
        data.dictIsTestObject = generateDictIsTestByClientFalse(data.clientHelper);
        data.ozTradesTableObjects = List.of(generateOzTradesTableEntryByClient(data.clientHelper));
        data.ozTradesTableObjects.getFirst().setSlippage(400d);
        data.mt5DealsCoercedObjects.getFirst().setProfitUsd(200d);
        data.mt5DealsCoercedObjects.getFirst().setProfit(200d);
        data.s3FactIbSalesCommissionsObject = List.of(generateS3FactIbSalesCommissionsClient(data.clientHelper));
        data.s3FactIbSalesCommissionsObject.getFirst().setIbCommission(200d);
        return data;
    }

    private static DataHelper getNoSlippageRuleTest5Data() {
        DataHelper data = getNoSlippageRuleData(noSlippageRuleClient5);
        data.closeTradeMtEvent.symbol = EURUSD;
        data.dictIsTestObject = generateDictIsTestByClientFalse(data.clientHelper);
        data.ozTradesTableObjects = List.of(generateOzTradesTableEntryByClient(data.clientHelper));
        data.ozTradesTableObjects.getFirst().setSlippage(395d);
        return data;
    }

    private static DataHelper getNoSlippageRuleTest6Data() {
        DataHelper data = getNoSlippageRuleData(noSlippageRuleClient6);
        data.closeTradeMtEvent.symbol = EURUSD;
        data.dictIsTestObject = generateDictIsTestByClientFalse(data.clientHelper);
        data.ozTradesTableObjects = List.of(generateOzTradesTableEntryByClient(data.clientHelper));
        data.ozTradesTableObjects.getFirst().setSlippage(395d);
        data.mt5DealsCoercedObjects = null;
        return data;
    }

    private static DataHelper getNoSlippageRuleTest7Data() {
        DataHelper data = getNoSlippageRuleData(noSlippageRuleClient7);
        data.closeTradeMtEvent.symbol = EURUSD;
        data.dictIsTestObject = generateDictIsTestByClientFalse(data.clientHelper);
        data.ozTradesTableObjects = List.of(generateOzTradesTableEntryByClient(data.clientHelper));
        data.ozTradesTableObjects.getFirst().setSlippage(395d);
        // Deal 1
        Mt5DealsCoercedObject deal1Open = generateTradeByClient(data.clientHelper);
        deal1Open.setEntry(0);
        deal1Open.setTime(getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 0, 0, 0));
        Mt5DealsCoercedObject deal1Close = generateTradeByClient(data.clientHelper);
        deal1Close.setEntry(1);
        deal1Close.setTime(getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 0, 0, 1));
        return data;
    }

    private static DataHelper getNoSlippageRuleTest8Data() {
        DataHelper data = getNoSlippageRuleData(noSlippageRuleClient8);
        data.closeTradeMtEvent.symbol = EURUSD;
        data.dictIsTestObject = generateDictIsTestByClientFalse(data.clientHelper);
        data.ozTradesTableObjects = List.of(generateOzTradesTableEntryByClient(data.clientHelper));
        data.ozTradesTableObjects.getFirst().setSlippage(395d);
        data.mt5DealsCoercedObjects = List.of(
                generateMt5DealsCoercedObject(data.clientHelper), generateMt5DealsCoercedObject(data.clientHelper));
        data.mt5DealsCoercedObjects.get(1).setComment("so");
        data.mt5DealsCoercedObjects.get(1).setEntry(3);
        return data;
    }

    private static DataHelper getNoSlippageRuleTest9Data() {
        DataHelper data = getNoSlippageRuleData(noSlippageRuleClient9);
        data.closeTradeMtEvent.symbol = EURUSD;
        data.dictIsTestObject = generateDictIsTestByClientFalse(data.clientHelper);
        data.ozTradesTableObjects = List.of(generateOzTradesTableEntryByClient(data.clientHelper));
        data.ozTradesTableObjects.getFirst().setSlippage(395d);
        data.mt5DealsCoercedObjects = List.of(
                generateMt5DealsCoercedObject(data.clientHelper), generateMt5DealsCoercedObject(data.clientHelper));
        data.mt5DealsCoercedObjects.get(1).setComment("so");
        data.mt5DealsCoercedObjects.get(1).setEntry(3);
        data.mt5DealsCoercedObjects.get(1).setNotionalValueUsd(3_000_002d);
        return data;
    }

    private static DataHelper getNoSlippageRuleTest10Data() {
        DataHelper data = getNoSlippageRuleData(noSlippageRuleClient10);
        data.closeTradeMtEvent.symbol = EURUSD;
        data.dictIsTestObject = generateDictIsTestByClientFalse(data.clientHelper);
        data.ozTradesTableObjects = List.of(generateOzTradesTableEntryByClient(data.clientHelper));
        data.ozTradesTableObjects.getFirst().setSlippage(395d);
        data.mt5DealsCoercedObjects = generateMt5DealsCoercedObject(data.clientHelper, 31);
        data.mt5DealsCoercedObjects.get(1).setComment("so");
        data.mt5DealsCoercedObjects.get(1).setEntry(3);
        data.mt5DealsCoercedObjects.get(1).setNotionalValueUsd(3_000_002d);
        data.s3FactIbSalesCommissionsObject = List.of(generateS3FactIbSalesCommissionsClient(data.clientHelper));
        data.s3FactIbSalesCommissionsObject.getFirst().setSalesCommission(-20_000d);
        data.s3FactIbSalesCommissionsObject.getFirst().setIbCommission(-20_000d);
        return data;
    }

    private static DataHelper getNoSlippageRuleTest11Data() {
        DataHelper data = getNoSlippageRuleData(noSlippageRuleClient11);
        data.closeTradeMtEvent.symbol = EURUSD;
        data.dictIsTestObject = generateDictIsTestByClientFalse(data.clientHelper);
        data.ozTradesTableObjects = List.of(generateOzTradesTableEntryByClient(data.clientHelper));
        data.ozTradesTableObjects.getFirst().setSlippage(395d);
        data.mt5DealsCoercedObjects = generateMt5DealsCoercedObject(data.clientHelper, 31);
        data.mt5DealsCoercedObjects.get(1).setComment("so");
        data.mt5DealsCoercedObjects.get(1).setEntry(3);
        data.mt5DealsCoercedObjects.get(1).setNotionalValueUsd(3_000_002d);
        data.s3FactIbSalesCommissionsObject = List.of(generateS3FactIbSalesCommissionsClient(data.clientHelper));
        data.s3FactIbSalesCommissionsObject.getFirst().setSalesCommission(1d);
        data.s3FactIbSalesCommissionsObject.getFirst().setIbCommission(1d);
        data.boAlertsObjects = List.of(generateAlert(data.clientHelper));
        data.boAlertsObjects.getFirst().setRule("No Slippage");
        data.boAlertsObjects.getFirst().setStatus("CLOSED");
        return data;
    }

    private static DataHelper getNoSlippageRuleTest12Data() {
        DataHelper data = getNoSlippageRuleData(noSlippageRuleClient12);
        data.closeTradeMtEvent.symbol = EURUSD;
        data.dictIsTestObject = generateDictIsTestByClientFalse(data.clientHelper);
        data.ozTradesTableObjects = List.of(generateOzTradesTableEntryByClient(data.clientHelper));
        data.ozTradesTableObjects.getFirst().setSlippage(395d);
        data.mt5DealsCoercedObjects = generateMt5DealsCoercedObject(data.clientHelper, 31);
        data.mt5DealsCoercedObjects.get(1).setComment("so");
        data.mt5DealsCoercedObjects.get(1).setEntry(3);
        data.mt5DealsCoercedObjects.get(1).setNotionalValueUsd(3_000_002d);
        data.s3FactIbSalesCommissionsObject = List.of(generateS3FactIbSalesCommissionsClient(data.clientHelper));
        data.s3FactIbSalesCommissionsObject.getFirst().setSalesCommission(1d);
        data.s3FactIbSalesCommissionsObject.getFirst().setIbCommission(1d);
        return data;
    }

    public static Map<String, DataHelper> setupNoSlippageRuleData() {
        startSshTunnel();
        Map<String, DataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("1", getNoSlippageRuleTest1Data());
        map.put("2", getNoSlippageRuleTest2Data());
        map.put("3", getNoSlippageRuleTest3Data());
        map.put("4", getNoSlippageRuleTest4Data());
        map.put("5", getNoSlippageRuleTest5Data());
        map.put("6", getNoSlippageRuleTest6Data());
        map.put("7", getNoSlippageRuleTest7Data());
        map.put("8", getNoSlippageRuleTest8Data());
        map.put("9", getNoSlippageRuleTest9Data());
        map.put("10", getNoSlippageRuleTest10Data());
        map.put("11", getNoSlippageRuleTest11Data());
        map.put("12", getNoSlippageRuleTest12Data());
        return map;
    }
}
