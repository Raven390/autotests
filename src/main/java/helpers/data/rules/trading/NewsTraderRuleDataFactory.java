package helpers.data.rules.trading;

import static business_objects.db.clickhouse.app_tb_finindex_data.AppTbFinindexDataFactory.generateAppFinindexData;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateAccountByClient;
import static business_objects.db.clickhouse.crm_tb_account_for_mt.crm_tb_account.CrmTbAccountForMtObjectFactory.generateAccountForMtByClient;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.dict_is_test.DictIsTestObjectFactory.generateDictIsTestByClientFalse;
import static business_objects.db.clickhouse.dict_is_test.DictIsTestObjectFactory.generateDictIsTestByClientTrue;
import static business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedFactory.generateMt5DealsCoercedObject;
import static business_objects.db.clickhouse.mt_tb_credits.MtTbCreditsObjectFactory.generateCreditsByClient;
import static business_objects.db.data_science.ucid_general_score.UcidGeneralScoreFactory.generateUcidGeneralScoreObject;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.DbHelper.startSshTunnel;
import static utils.Constants.MT_CLOSE_TRADE_EVENT;
import static utils.Utils.*;

import business_objects.db.clickhouse.app_tb_finindex_data.AppTbFinindexData;
import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntityFactory;
import business_objects.db.clickhouse.mt___mt5_deals_coerced_dd.Mt5DealsCoercedDdFactory;
import business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedObject;
import business_objects.db.data_science.ucid_general_score.UcidGeneralScore;
import business_objects.kafka.mt_events.CloseTradeMtEvent;
import business_objects.kafka.mt_events.TradeEventMetadata;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import helpers.data.enums.DateTimeFormat;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewsTraderRuleDataFactory {
    private static final ClientHelper client1 = getRandomVantageClientAllFields();
    private static final ClientHelper client2 = getRandomVantageClientAllFields();
    private static final ClientHelper client21 = getRandomVantageClientAllFields();
    private static final ClientHelper client22 = getRandomVantageClientAllFields();
    private static final ClientHelper client23 = getRandomVantageClientAllFields();
    private static final ClientHelper client3 = getRandomVantageClientAllFields();
    private static final ClientHelper client4 = getRandomVantageClientAllFields();
    private static final ClientHelper client5 = getRandomVantageClientAllFields();
    private static final String oldTime =
            getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 1);

    @Step("Create data for News Trader rule is test account=true")
    private static DataHelper getNewsTraderRuleData(ClientHelper client) {
        DataHelper data = new DataHelper();
        client.setServerId(10);
        data.clientHelper = client;
        data.dictIsTestObject = generateDictIsTestByClientFalse(data.clientHelper);
        data.crmTbUserObject = generateUserByClient(client);
        data.crmTbAccountObject = generateAccountByClient(client, false);
        data.crmTbAccountForMtObject = generateAccountForMtByClient(client, false);
        data.mt5DealsCoercedObjects = List.of(generateMt5DealsCoercedObject(client));
        data.mt5DealsCoercedObjects.forEach(deal -> deal.setTimeUtc(oldTime));
        TradeEventMetadata metadata = new TradeEventMetadata("MT5");
        data.closeTradeMtEvent = new CloseTradeMtEvent(
                getRandomUuidString(),
                Instant.now().toString(),
                data.mt5DealsCoercedObjects.getFirst().getPositionId(),
                client.getTradingAccount(),
                data.mt5DealsCoercedObjects.getFirst().getVolumeLots(),
                data.mt5DealsCoercedObjects.getFirst().getSymbol(),
                data.clientHelper.getServerId(),
                MT_CLOSE_TRADE_EVENT,
                Instant.now().toString(),
                metadata,
                Instant.now().toString());
        return data;
    }

    @Description("News Trader. Exit without alert if account is test . Event_end_1")
    private static DataHelper getNewsTraderCloseTradeTest1Data() {
        DataHelper data = getNewsTraderRuleData(client1);
        data.dictIsTestObject = generateDictIsTestByClientTrue(data.clientHelper);
        return data;
    }

    @Description(
            "News Trader. Scotland. Exit without alert if news deals < 0.7. profitTotal/profitNews=0.5 Event_end_5")
    private static DataHelper getNewsTraderCloseTradeTest2Data() {
        DataHelper data = getNewsTraderRuleData(client2);
        data.mtTbCreditsObjects = List.of(generateCreditsByClient(data.clientHelper));
        data.mt5DealsCoercedObjects = generateMt5DealsCoercedObject(data.clientHelper, 5);
        data.mt5DealsCoercedObjects.forEach(deal -> deal.setTimeUtc(oldTime));
        String time = getCurrentTimestampDbFormat();
        AppTbFinindexData news = generateAppFinindexData(time);
        data.AppTbFinindexData = List.of(news);
        List<Mt5DealsCoercedObject> newsDeals = generateMt5DealsCoercedObject(data.clientHelper, 5);
        newsDeals.forEach(deal -> deal.setTimeUtc(time));
        newsDeals.forEach(deal -> deal.setTime(oldTime));
        data.mt5DealsCoercedObjects.addAll(newsDeals);
        data.mt5DealsCoercedObjects.forEach(deal -> deal.setProfitUsd(4.0));
        return data;
    }

    @Description(
            "News Trader. Scotland. Exit without alert if news deals < 0.7. profitTotal/profitNews< 0.6 leverage < 50")
    private static DataHelper getNewsTraderCloseTradeTest21Data() {
        DataHelper data = getNewsTraderRuleData(client21);
        data.mtTbCreditsObjects = List.of(generateCreditsByClient(data.clientHelper));
        data.mt5DealsCoercedObjects = generateMt5DealsCoercedObject(data.clientHelper, 5);
        data.mt5DealsCoercedObjects.forEach(deal -> deal.setTimeUtc(oldTime));
        data.mt5DealsCoercedObjects.forEach(deal -> deal.setProfitUsd(4.0));
        String time = getCurrentTimestampDbFormat();
        AppTbFinindexData news = generateAppFinindexData(time);
        data.AppTbFinindexData = List.of(news);
        List<Mt5DealsCoercedObject> newsDeals = generateMt5DealsCoercedObject(data.clientHelper, 5);
        newsDeals.forEach(deal -> deal.setTimeUtc(time));
        newsDeals.forEach(deal -> deal.setTime(oldTime));
        newsDeals.forEach(deal -> deal.setProfitUsd(40.0));
        data.mt5DealsCoercedObjects.addAll(newsDeals);

        data.setMt5DealsCoercedDdObjects(new ArrayList<>(List.of(Mt5DealsCoercedDdFactory.generateTradeByClient(
                data.getClientHelper(), 60d, 5d, 4d, getCurrentTimestampDbFormat()))));

        return data;
    }

    @Description(
            "News Trader. Scotland. Exit without alert if news deals < 0.7.  profitTotal/profitNews< 0.6 leverage > 50. ucidScore < 0.7.")
    private static DataHelper getNewsTraderCloseTradeTest22Data() {
        DataHelper data = getNewsTraderRuleData(client22);
        data.mtTbCreditsObjects = List.of(generateCreditsByClient(data.clientHelper));
        data.mt5DealsCoercedObjects = generateMt5DealsCoercedObject(data.clientHelper, 5);
        data.mt5DealsCoercedObjects.forEach(deal -> deal.setTimeUtc(oldTime));
        data.mt5DealsCoercedObjects.forEach(deal -> deal.setProfitUsd(4.0));
        String time = getCurrentTimestampDbFormat();
        AppTbFinindexData news = generateAppFinindexData(time);
        data.AppTbFinindexData = List.of(news);
        List<Mt5DealsCoercedObject> newsDeals = generateMt5DealsCoercedObject(data.clientHelper, 5);
        newsDeals.forEach(deal -> deal.setTimeUtc(time));
        newsDeals.forEach(deal -> deal.setTime(oldTime));
        newsDeals.forEach(deal -> deal.setProfitUsd(40.0));
        data.mt5DealsCoercedObjects.addAll(newsDeals);

        data.setMt5DealsCoercedDdObjects(new ArrayList<>(List.of(Mt5DealsCoercedDdFactory.generateTradeByClient(
                data.getClientHelper(), 300d, 5d, 4d, getCurrentTimestampDbFormat()))));

        UcidGeneralScore score = generateUcidGeneralScoreObject(data.clientHelper, 0.6, 0.6);
        data.ucidGeneralScores = List.of(score);

        return data;
    }

    @Description(
            "News Trader. Scotland. Exit without alert if news deals < 0.7.  profitTotal/profitNews< 0.6 leverage > 50. ucidScore < 0.7.")
    private static DataHelper getNewsTraderCloseTradeTest23Data() {
        DataHelper data = getNewsTraderRuleData(client23);
        data.mtTbCreditsObjects = List.of(generateCreditsByClient(data.clientHelper));
        data.mt5DealsCoercedObjects = generateMt5DealsCoercedObject(data.clientHelper, 5);
        data.mt5DealsCoercedObjects.forEach(deal -> deal.setTimeUtc(oldTime));
        data.mt5DealsCoercedObjects.forEach(deal -> deal.setProfitUsd(4.0));
        String time = getCurrentTimestampDbFormat();
        AppTbFinindexData news = generateAppFinindexData(time);
        data.AppTbFinindexData = List.of(news);
        List<Mt5DealsCoercedObject> newsDeals = generateMt5DealsCoercedObject(data.clientHelper, 5);
        newsDeals.forEach(deal -> deal.setTimeUtc(time));
        newsDeals.forEach(deal -> deal.setTime(oldTime));
        newsDeals.forEach(deal -> deal.setProfitUsd(40.0));
        data.mt5DealsCoercedObjects.addAll(newsDeals);

        data.setMt5DealsCoercedDdObjects(new ArrayList<>(List.of(Mt5DealsCoercedDdFactory.generateTradeByClient(
                data.getClientHelper(), 300d, 5d, 4d, getCurrentTimestampDbFormat()))));

        UcidGeneralScore score = generateUcidGeneralScoreObject(data.clientHelper, 0.8, 0.8);
        data.ucidGeneralScores = List.of(score);

        return data;
    }

    @Description("News trader on close trade. Exit without alert if user have profit USD <350. Event_end_3")
    private static DataHelper getNewsTraderCloseTradeTest3Data() {
        DataHelper data = getNewsTraderRuleData(client3);
        data.mtTbCreditsObjects = List.of(generateCreditsByClient(data.clientHelper));
        data.mt5DealsCoercedObjects = generateMt5DealsCoercedObject(data.clientHelper, 2);
        data.mt5DealsCoercedObjects.forEach(deal -> deal.setTimeUtc(oldTime));
        String time = getCurrentTimestampDbFormat();
        AppTbFinindexData news = generateAppFinindexData(time);
        data.AppTbFinindexData = List.of(news);
        List<Mt5DealsCoercedObject> newsDeals = generateMt5DealsCoercedObject(data.clientHelper, 9);
        newsDeals.forEach(deal -> deal.setTimeUtc(time));
        newsDeals.forEach(deal -> deal.setTime(oldTime));
        data.mt5DealsCoercedObjects.addAll(newsDeals);
        data.mt5DealsCoercedObjects.forEach(deal -> deal.setProfitUsd(4.0));
        return data;
    }

    @Description("News Trader. Exit without alert if profit/deposit < 0.5. Event_4")
    private static DataHelper getNewsTraderCloseTradeTest4Data() {
        DataHelper data = getNewsTraderRuleData(client4);
        data.mtTbCreditsObjects = List.of(generateCreditsByClient(data.clientHelper));
        data.mt5DealsCoercedObjects = generateMt5DealsCoercedObject(data.clientHelper, 2);
        data.mt5DealsCoercedObjects.forEach(deal -> deal.setTimeUtc(oldTime));
        String time = getCurrentTimestampDbFormat();
        AppTbFinindexData news = generateAppFinindexData(time);
        data.AppTbFinindexData = List.of(news);
        List<Mt5DealsCoercedObject> newsDeals = generateMt5DealsCoercedObject(data.clientHelper, 9);
        newsDeals.forEach(deal -> deal.setTimeUtc(time));
        newsDeals.forEach(deal -> deal.setTime(oldTime));
        data.mt5DealsCoercedObjects.addAll(newsDeals);
        writeLog("count of deals is: " + data.mt5DealsCoercedObjects.size());
        data.mt5DealsCoercedObjects.forEach(deal -> deal.setProfitUsd(100.0));
        data.crmTbDepositObjects =
                List.of(CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(data.clientHelper));
        data.crmTbDepositObjects
                .getFirst()
                .setAmountUsd(BigDecimal.valueOf(1100.0).divide(BigDecimal.valueOf(0.4), 2, RoundingMode.HALF_UP));
        return data;
    }

    @Description("News Trader. Exit with alert if profit/deposit > 0.5. Event_end_5")
    private static DataHelper getNewsTraderCloseTradeTest5Data() {
        DataHelper data = getNewsTraderRuleData(client5);
        data.mtTbCreditsObjects = List.of(generateCreditsByClient(data.clientHelper));
        data.mt5DealsCoercedObjects = generateMt5DealsCoercedObject(data.clientHelper, 2);
        data.mt5DealsCoercedObjects.forEach(deal -> deal.setTimeUtc(oldTime));
        String time = getCurrentTimestampDbFormat();
        AppTbFinindexData news = generateAppFinindexData(time);
        data.AppTbFinindexData = List.of(news);
        List<Mt5DealsCoercedObject> newsDeals = generateMt5DealsCoercedObject(data.clientHelper, 9);
        newsDeals.forEach(deal -> deal.setTimeUtc(time));
        newsDeals.forEach(deal -> deal.setTime(oldTime));
        data.mt5DealsCoercedObjects.addAll(newsDeals);
        writeLog("count of deals is: " + data.mt5DealsCoercedObjects.size());
        data.mt5DealsCoercedObjects.forEach(deal -> deal.setProfitUsd(100.0));
        data.crmTbDepositObjects =
                List.of(CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(data.clientHelper));
        data.crmTbDepositObjects
                .getFirst()
                .setAmountUsd(BigDecimal.valueOf(1100.0).divide(BigDecimal.valueOf(0.6), 2, RoundingMode.HALF_UP));
        return data;
    }

    public static Map<String, DataHelper> setupNewsTraderCloseTradeRuleData() {
        startSshTunnel();
        Map<String, DataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("1", getNewsTraderCloseTradeTest1Data());
        map.put("2", getNewsTraderCloseTradeTest2Data());
        map.put("21", getNewsTraderCloseTradeTest21Data());
        map.put("22", getNewsTraderCloseTradeTest22Data());
        map.put("23", getNewsTraderCloseTradeTest23Data());
        map.put("3", getNewsTraderCloseTradeTest3Data());
        map.put("4", getNewsTraderCloseTradeTest4Data());
        map.put("5", getNewsTraderCloseTradeTest5Data());
        return map;
    }
}
