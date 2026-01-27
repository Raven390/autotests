package helpers.data.rules.trading;

import static business_objects.db.clickhouse.app_tb_finindex_data.AppTbFinindexDataFactory.generateAppFinindexData;
import static business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient;
import static business_objects.db.clickhouse.dict_is_test.DictIsTestObjectFactory.generateDictIsTestByClientFalse;
import static business_objects.db.clickhouse.dict_is_test.DictIsTestObjectFactory.generateDictIsTestByClientTrue;
import static business_objects.db.clickhouse.mt___mt5_deals_coerced_dd.Mt5DealsCoercedDdFactoryV2.generateMt5DealsCoercedDDObject;
import static business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedFactory.generateMt5DealsCoercedObject;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.DataHelper.*;
import static helpers.database.DbHelper.startSshTunnel;
import static utils.Utils.*;

import business_objects.db.clickhouse.app_tb_finindex_data.AppTbFinindexData;
import business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedObject;
import business_objects.kafka.mt_events.TradeEvent;
import business_objects.kafka.mt_events.TradeEventMetadata;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import helpers.data.enums.DateTimeFormat;
import io.qameta.allure.Description;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnlimitedLeverageRuleDataFactory {
    private static final ClientHelper client1 = getRandomVantageClientAllFields();
    private static final ClientHelper client2 = getRandomVantageClientAllFields();
    private static final ClientHelper client3 = getRandomVantageClientAllFields();
    private static final ClientHelper client4 = getRandomVantageClientAllFields();
    private static final ClientHelper client5 = getRandomVantageClientAllFields();
    private static final ClientHelper client6 = getRandomVantageClientAllFields();
    private static final ClientHelper client7 = getRandomVantageClientAllFields();
    private static final ClientHelper client8 = getRandomVantageClientAllFields();
    private static final ClientHelper client9 = getRandomVantageClientAllFields();
    private static final ClientHelper client10 = getRandomVantageClientAllFields();

    private static final String oldTime =
            getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 1);

    @Description("Create data for Connection search rule")
    private static DataHelper getRuleData(ClientHelper client) {
        DataHelper data = new DataHelper();
        data.createClient(client);

        data.tradeEvent = new TradeEvent();
        data.tradeEvent.type = "closeTrade";
        data.tradeEvent.openTime = convertTimestampToIsoFormat(getCurrentTimestampMillis());
        data.tradeEvent.openTimeUtc = convertTimestampToIsoFormat(getCurrentTimestampMillis());
        data.tradeEvent.closeTime = convertTimestampToIsoFormat(getCurrentTimestampMillis());
        data.tradeEvent.closeTimeUtc = convertTimestampToIsoFormat(getCurrentTimestampMillis());
        data.tradeEvent.tradeId = getRandomIntPositive();
        data.tradeEvent.volume = 1;
        data.tradeEvent.symbol = "EURUSD";
        data.tradeEvent.equity = 100d;
        data.tradeEvent.margin = 10d;
        data.tradeEvent.freeMargin = 10d;
        data.tradeEvent.balance = 10d;
        data.tradeEvent.leverage = 10d;
        data.tradeEvent.eventDate = convertTimestampToIsoFormat(getCurrentTimestampMillis());
        data.tradeEvent.id = getRandomUuidString();
        data.tradeEvent.metadata = new TradeEventMetadata("MT5");
        data.tradeEvent.metadata.created = convertTimestampToIsoFormat(getCurrentTimestampMillis());
        data.tradeEvent.serverId = client.getServerId();
        data.tradeEvent.tradingAccount = client.getTradingAccount();
        return data;
    }

    private static DataHelper getTest1Data() {
        DataHelper data = getRuleData(client1);

        generateDictIsTestByClientTrue(data);

        return data;
    }

    private static DataHelper getTest2Data() {
        DataHelper data = getRuleData(client2);

        generateDictIsTestByClientFalse(data);

        data.getCrmTbAccountObject().setAccountGroup("S_VFX_EUR");
        data.getMtAccountObject().setAccountGroup("S_VFX_EUR");

        return data;
    }

    private static DataHelper getTest3Data() {
        DataHelper data = getRuleData(client3);

        generateDictIsTestByClientFalse(data);

        data.getCrmTbAccountObject().setAccountGroup("M_VUR_.EUR");
        data.getMtAccountObject().setAccountGroup("M_VUR_.EUR");
        return data;
    }

    private static DataHelper getTest4Data() {
        DataHelper data = getRuleData(client4);

        generateDictIsTestByClientFalse(data);

        data.getCrmTbAccountObject().setAccountGroup("M_VUR_.EUR");
        data.getMtAccountObject().setAccountGroup("M_VUR_.EUR");

        data.setMt5DealsCoercedObjects(generateMt5DealsCoercedObject(data.clientHelper, 1));
        data.getMt5DealsCoercedObjects().getFirst().setProfit(200d);
        data.getMt5DealsCoercedObjects().getFirst().setProfitUsd(200d);
        return data;
    }

    private static DataHelper getTest5Data() {
        DataHelper data = getRuleData(client5);

        generateDictIsTestByClientFalse(data);

        data.getCrmTbAccountObject().setAccountGroup("M_VUR_.EUR");
        data.getMtAccountObject().setAccountGroup("M_VUR_.EUR");

        data.setMt5DealsCoercedObjects(generateMt5DealsCoercedObject(data.clientHelper, 1));
        data.getMt5DealsCoercedObjects().getFirst().setProfit(301d);
        data.getMt5DealsCoercedObjects().getFirst().setProfitUsd(301d);

        return data;
    }

    private static DataHelper getTest6Data() {
        DataHelper data = getRuleData(client6);

        generateDictIsTestByClientFalse(data);

        data.getCrmTbAccountObject().setAccountGroup("M_VUR_.EUR");
        data.getMtAccountObject().setAccountGroup("M_VUR_.EUR");

        data.setMt5DealsCoercedObjects(generateMt5DealsCoercedObject(data.clientHelper, 1));
        data.getMt5DealsCoercedObjects().getFirst().setProfit(301d);
        data.getMt5DealsCoercedObjects().getFirst().setProfitUsd(301d);

        data.setCrmTbDepositObjects(List.of(generateCrmTbDepositEntityByClient(data.clientHelper)));
        data.getCrmTbDepositObjects().getFirst().setAmount(BigDecimal.valueOf(100));
        data.getCrmTbDepositObjects().getFirst().setAmountUsd(BigDecimal.valueOf(100));
        return data;
    }

    private static DataHelper getTest7Data() {
        DataHelper data = getRuleData(client7);

        generateDictIsTestByClientFalse(data);

        data.getCrmTbAccountObject().setAccountGroup("M_VUR_.EUR");
        data.getMtAccountObject().setAccountGroup("M_VUR_.EUR");

        data.setCrmTbDepositObjects(List.of(generateCrmTbDepositEntityByClient(data.clientHelper)));
        data.getCrmTbDepositObjects().getFirst().setAmount(BigDecimal.valueOf(100));
        data.getCrmTbDepositObjects().getFirst().setAmountUsd(BigDecimal.valueOf(100));

        data.mt5DealsCoercedObjects = generateMt5DealsCoercedObject(data.clientHelper, 2);
        data.mt5DealsCoercedObjects.forEach(deal -> deal.setTimeUtc(oldTime));
        String time = getCurrentTimestampDbFormat();
        AppTbFinindexData news = generateAppFinindexData(time);
        data.AppTbFinindexData = List.of(news);
        List<Mt5DealsCoercedObject> newsDeals = generateMt5DealsCoercedObject(data.clientHelper, 9);
        newsDeals.forEach(deal -> deal.setTimeUtc(time));
        newsDeals.forEach(deal -> deal.setTime(oldTime));
        data.mt5DealsCoercedObjects.addAll(newsDeals);
        data.mt5DealsCoercedObjects.forEach(deal -> deal.setProfitUsd(100.0));
        return data;
    }

    private static DataHelper getTest8Data() {
        DataHelper data = getRuleData(client8);

        generateDictIsTestByClientFalse(data);

        data.getCrmTbAccountObject().setAccountGroup("M_VUR_.EUR");
        data.getMtAccountObject().setAccountGroup("M_VUR_.EUR");

        data.setCrmTbDepositObjects(List.of(generateCrmTbDepositEntityByClient(data.clientHelper)));
        data.getCrmTbDepositObjects().getFirst().setAmount(BigDecimal.valueOf(100));
        data.getCrmTbDepositObjects().getFirst().setAmountUsd(BigDecimal.valueOf(100));

        data.mt5DealsCoercedObjects = generateMt5DealsCoercedObject(data.clientHelper, 2);
        data.mt5DealsCoercedObjects.forEach(deal -> deal.setTimeUtc(oldTime));
        String time = getCurrentTimestampDbFormat();
        AppTbFinindexData news = generateAppFinindexData(time);
        data.AppTbFinindexData = List.of(news);
        List<Mt5DealsCoercedObject> newsDeals = generateMt5DealsCoercedObject(data.clientHelper, 9);
        newsDeals.forEach(deal -> deal.setTimeUtc(time));
        newsDeals.forEach(deal -> deal.setTime(oldTime));
        data.mt5DealsCoercedObjects.addAll(newsDeals);
        data.mt5DealsCoercedObjects.forEach(deal -> deal.setProfitUsd(100.0));
        data.mt5DealsCoercedObjects.getFirst().setNotionalValueUsd(100d);

        data.mt5DealsCoercedDdObjectsV2 = List.of(generateMt5DealsCoercedDDObject(data.clientHelper));
        data.mt5DealsCoercedDdObjectsV2.forEach(deal -> {
            deal.setEquityUsd(100.0);
            deal.setFreeMarginUsd(0.0);
            deal.setLeverage(100);
        });

        return data;
    }

    private static DataHelper getTest9Data() {
        DataHelper data = getRuleData(client9);

        generateDictIsTestByClientFalse(data);

        data.getCrmTbAccountObject().setAccountGroup("M_VUR_.EUR");
        data.getMtAccountObject().setAccountGroup("M_VUR_.EUR");

        data.setCrmTbDepositObjects(List.of(generateCrmTbDepositEntityByClient(data.clientHelper)));
        data.getCrmTbDepositObjects().getFirst().setAmount(BigDecimal.valueOf(100));
        data.getCrmTbDepositObjects().getFirst().setAmountUsd(BigDecimal.valueOf(100));

        data.mt5DealsCoercedObjects = generateMt5DealsCoercedObject(data.clientHelper, 2);
        data.mt5DealsCoercedObjects.forEach(deal -> deal.setTimeUtc(oldTime));
        String time = getCurrentTimestampDbFormat();
        AppTbFinindexData news = generateAppFinindexData(time);
        data.AppTbFinindexData = List.of(news);
        List<Mt5DealsCoercedObject> newsDeals = generateMt5DealsCoercedObject(data.clientHelper, 9);
        newsDeals.forEach(deal -> deal.setTimeUtc(time));
        newsDeals.forEach(deal -> deal.setTime(oldTime));
        data.mt5DealsCoercedObjects.addAll(newsDeals);
        data.mt5DealsCoercedObjects.forEach(deal -> deal.setProfitUsd(100.0));
        data.mt5DealsCoercedObjects.getFirst().setNotionalValueUsd(100d);

        data.mt5DealsCoercedDdObjectsV2 = List.of(generateMt5DealsCoercedDDObject(data.clientHelper));
        data.mt5DealsCoercedDdObjectsV2.forEach(deal -> {
            deal.setEquityUsd(100.0);
            deal.setFreeMarginUsd(0.0);
            deal.setLeverage(250);
        });

        data.addAlert("News Trading", "CLOSED");

        return data;
    }

    private static DataHelper getTest10Data() {
        DataHelper data = getRuleData(client10);

        generateDictIsTestByClientFalse(data);

        data.getCrmTbAccountObject().setAccountGroup("M_VUR_.EUR");
        data.getMtAccountObject().setAccountGroup("M_VUR_.EUR");

        data.setCrmTbDepositObjects(List.of(generateCrmTbDepositEntityByClient(data.clientHelper)));
        data.getCrmTbDepositObjects().getFirst().setAmount(BigDecimal.valueOf(100));
        data.getCrmTbDepositObjects().getFirst().setAmountUsd(BigDecimal.valueOf(100));

        data.mt5DealsCoercedObjects = generateMt5DealsCoercedObject(data.clientHelper, 2);
        data.mt5DealsCoercedObjects.forEach(deal -> deal.setTimeUtc(oldTime));
        String time = getCurrentTimestampDbFormat();
        AppTbFinindexData news = generateAppFinindexData(time);
        data.AppTbFinindexData = List.of(news);
        List<Mt5DealsCoercedObject> newsDeals = generateMt5DealsCoercedObject(data.clientHelper, 9);
        newsDeals.forEach(deal -> deal.setTimeUtc(time));
        newsDeals.forEach(deal -> deal.setTime(oldTime));
        data.mt5DealsCoercedObjects.addAll(newsDeals);
        data.mt5DealsCoercedObjects.forEach(deal -> deal.setProfitUsd(28d));
        data.mt5DealsCoercedObjects.getFirst().setNotionalValueUsd(100d);

        data.mt5DealsCoercedDdObjectsV2 = List.of(generateMt5DealsCoercedDDObject(data.clientHelper));
        data.mt5DealsCoercedDdObjectsV2.forEach(deal -> {
            deal.setEquityUsd(100.0);
            deal.setFreeMarginUsd(0.0);
            deal.setLeverage(250);
        });

        return data;
    }

    public static Map<String, DataHelper> setupUnlimitedLeverageRuleData() {
        startSshTunnel();
        Map<String, DataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("1", getTest1Data());
        map.put("2", getTest2Data());
        map.put("3", getTest3Data());
        map.put("4", getTest4Data());
        map.put("5", getTest5Data());
        map.put("6", getTest6Data());
        map.put("7", getTest7Data());
        map.put("8", getTest8Data());
        map.put("9", getTest9Data());
        map.put("10", getTest10Data());
        return map;
    }
}
