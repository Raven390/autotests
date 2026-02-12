package helpers.data.rules.trading;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountData;
import static business_objects.db.clickhouse.crm_tb_account_for_mt.crm_tb_account.CrmTbAccountForMtObjectFactory.generateAccountForMtByClient;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.dict_is_test.DictIsTestObjectFactory.generateDictIsTestByClientFalse;
import static business_objects.db.clickhouse.dict_is_test.DictIsTestObjectFactory.generateDictIsTestByClientTrue;
import static business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedFactory.generateMt5DealsCoercedObject;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.DbHelper.startSshTunnel;
import static utils.Utils.*;

import business_objects.db.clickhouse.mt___mt5_deals_coerced_dd.Mt5DealsCoercedDd;
import business_objects.db.clickhouse.mt___mt5_deals_coerced_dd.Mt5DealsCoercedDdFactory;
import business_objects.db.clickhouse.mt___symbol_session.MtSymbolSession;
import business_objects.db.clickhouse.mt___symbol_session.MtSymbolSessionObjectFactory;
import business_objects.kafka.mt_events.TradeEvent;
import business_objects.kafka.mt_events.TradeEventMetadata;
import generator.annotations.RuleTestData;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import helpers.data.enums.DateTimeFormat;
import io.qameta.allure.Step;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RuleTestData("gap-trading")
public class GapTradingRuleDataFactory {

    private static final ClientHelper gapTradingRuleExitEventEnd1Client = getRandomVantageClientAllFields();
    private static final ClientHelper gapTradingRuleExitEventEnd2Client = getRandomVantageClientAllFields();
    private static final ClientHelper gapTradingRuleExitEventEnd3Client = getRandomVantageClientAllFields();
    private static final ClientHelper gapTradingRuleExitEventEnd4Client = getRandomVantageClientAllFields();
    private static final ClientHelper gapTradingRuleExitEventEnd5Client = getRandomVantageClientAllFields();
    private static final ClientHelper gapTradingRuleExitEventEnd6Client = getRandomVantageClientAllFields();
    private static final ClientHelper gapTradingRuleExitEventEnd7Client = getRandomVantageClientAllFields();
    private static final ClientHelper gapTradingRuleExitEventEnd8Client = getRandomVantageClientAllFields();
    private static final ClientHelper gapTradingRuleExitEventEndAlertClient = getRandomVantageClientAllFields();

    private GapTradingRuleDataFactory() {}

    @Step("Create data for Gap Trading rule")
    private static DataHelper getGapTradingRuleData(ClientHelper client) {
        DataHelper ruleData = new DataHelper();

        ruleData.setClientHelper(client);
        ruleData.setCrmTbUserObject(generateUserByClient(client));
        ruleData.setCrmTbAccountObject(generateCrmTbAccountData(client));
        ruleData.setCrmTbAccountForMtObject(generateAccountForMtByClient(client, false));
        ruleData.setDictIsTestObject(generateDictIsTestByClientFalse(client));

        TradeEvent tradeEvent = new TradeEvent();
        tradeEvent.setType("openTrade");
        tradeEvent.setOpenTime(getLocalTimeIsoFormat());
        tradeEvent.setOpenTimeUtc(convertTimestampToIsoFormat(getCurrentTimestampMillis()));
        tradeEvent.setTradeId(123);
        tradeEvent.setVolume(1);
        tradeEvent.setSymbol("EURUSD");
        tradeEvent.setEquity(100d);
        tradeEvent.setMargin(10d);
        tradeEvent.setFreeMargin(10d);
        tradeEvent.setBalance(10d);
        tradeEvent.setLeverage(10d);
        tradeEvent.setEventDate(convertTimestampToIsoFormat(getCurrentTimestampMillis()));
        tradeEvent.setId(getRandomUuidString());
        tradeEvent.setMetadata(new TradeEventMetadata("MT5"));
        tradeEvent.setServerId(client.getServerId());
        tradeEvent.setTradingAccount(client.getTradingAccount());

        ruleData.setTradeEvent(tradeEvent);

        LocalTime timePlus = LocalTime.now().plusMinutes(20);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        MtSymbolSession symbolSession = MtSymbolSessionObjectFactory.generateMtSymbolSessionObjectByClient(
                client, tradeEvent.getSymbol(), "00:00-" + timePlus.format(formatter));

        ruleData.setMtSymbolSessions(List.of(symbolSession));

        return ruleData;
    }

    @Step("Exit from rule without alert if user is test or social trader user")
    private static DataHelper getGapTradingRuleExitEventEnd1Data() {
        DataHelper data = getGapTradingRuleData(gapTradingRuleExitEventEnd1Client);
        data.setDictIsTestObject(generateDictIsTestByClientTrue(data.getClientHelper()));
        return data;
    }

    private static DataHelper getGapTradingRuleExitEventEnd2Data() {
        DataHelper data = getGapTradingRuleData(gapTradingRuleExitEventEnd2Client);
        data.getTradeEvent().setSymbol("GBPUSD");

        LocalTime timePlusFive = LocalTime.now().plusMinutes(50);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        MtSymbolSession symbolSession = MtSymbolSessionObjectFactory.generateMtSymbolSessionObjectByClient(
                data.getClientHelper(), data.getTradeEvent().getSymbol(), "00:00-" + timePlusFive.format(formatter));

        data.setMtSymbolSessions(List.of(symbolSession));
        return data;
    }

    private static DataHelper getGapTradingRuleExitEventEnd3Data() {
        return getGapTradingRuleData(gapTradingRuleExitEventEnd3Client);
    }

    private static DataHelper getGapTradingRuleExitEventEnd4Data() {
        DataHelper data = getGapTradingRuleData(gapTradingRuleExitEventEnd4Client);
        data.setMt5DealsCoercedObjects(new ArrayList<>(List.of(generateMt5DealsCoercedObject(data.getClientHelper()))));
        return data;
    }

    private static DataHelper getGapTradingRuleExitEventEnd5Data() {
        DataHelper data = getGapTradingRuleData(gapTradingRuleExitEventEnd5Client);
        data.setMt5DealsCoercedObjects(new ArrayList<>(List.of(generateMt5DealsCoercedObject(data.getClientHelper()))));
        data.setMt5DealsCoercedDdObjects(new ArrayList<>(List.of(Mt5DealsCoercedDdFactory.generateTradeByClient(
                data.getClientHelper(), 200d, 10d, -100d, getCurrentTimestampDbFormat()))));
        return data;
    }

    private static DataHelper getGapTradingRuleExitEventEnd6Data() {
        DataHelper data = getGapTradingRuleData(gapTradingRuleExitEventEnd6Client);
        data.setMt5DealsCoercedObjects(new ArrayList<>(List.of(generateMt5DealsCoercedObject(data.getClientHelper()))));

        data.setMt5DealsCoercedDdObjects(new ArrayList<>(List.of(Mt5DealsCoercedDdFactory.generateTradeByClient(
                data.getClientHelper(),
                200d,
                10d,
                -100d,
                getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 0, 0, 40)))));

        data.getMt5DealsCoercedDdObjects()
                .add(Mt5DealsCoercedDdFactory.generateTradeByClient(
                        data.getClientHelper(), 200d, 10d, 50d, getCurrentTimestampDbFormat()));

        return data;
    }

    private static DataHelper getGapTradingRuleExitEventEnd7Data() {
        DataHelper data = getGapTradingRuleData(gapTradingRuleExitEventEnd7Client);
        data.setMt5DealsCoercedObjects(new ArrayList<>(List.of(generateMt5DealsCoercedObject(data.getClientHelper()))));
        data.getMt5DealsCoercedObjects().getFirst().setNotionalValueUsd(60_000d);

        data.setMt5DealsCoercedDdObjects(new ArrayList<>(List.of(Mt5DealsCoercedDdFactory.generateTradeByClient(
                data.getClientHelper(),
                1000d,
                100d,
                -2000d,
                getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 0, 0, 40)))));

        Mt5DealsCoercedDd mt5DealsCoercedDd = Mt5DealsCoercedDdFactory.generateTradeByClient(
                data.getClientHelper(), 1000d, 100d, 200d, getCurrentTimestampDbFormat());
        data.getMt5DealsCoercedDdObjects().add(mt5DealsCoercedDd);
        return data;
    }

    private static DataHelper getGapTradingRuleExitEventEnd8Data() {
        DataHelper data = getGapTradingRuleData(gapTradingRuleExitEventEnd8Client);
        data.setMt5DealsCoercedObjects(generateMt5DealsCoercedObject(data.getClientHelper(), 2));
        data.getMt5DealsCoercedObjects().getLast().setSymbol("BTCUSD");

        data.setMt5DealsCoercedDdObjects(new ArrayList<>(List.of(Mt5DealsCoercedDdFactory.generateTradeByClient(
                data.getClientHelper(),
                1000d,
                100d,
                -2000d,
                getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 0, 0, 40)))));

        Mt5DealsCoercedDd mt5DealsCoercedDd = Mt5DealsCoercedDdFactory.generateTradeByClient(
                data.getClientHelper(), 1000d, 100d, 200d, getCurrentTimestampDbFormat());
        data.getMt5DealsCoercedDdObjects().add(mt5DealsCoercedDd);

        return data;
    }

    private static DataHelper getGapTradingRuleExitEventEndAlertData() {
        DataHelper data = getGapTradingRuleData(gapTradingRuleExitEventEndAlertClient);
        data.setMt5DealsCoercedObjects(new ArrayList<>(List.of(generateMt5DealsCoercedObject(data.getClientHelper()))));

        data.setMt5DealsCoercedDdObjects(new ArrayList<>(List.of(Mt5DealsCoercedDdFactory.generateTradeByClient(
                data.getClientHelper(),
                1000d,
                100d,
                -2000d,
                getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 0, 0, 40)))));

        Mt5DealsCoercedDd mt5DealsCoercedDd = Mt5DealsCoercedDdFactory.generateTradeByClient(
                data.getClientHelper(), 1000d, 100d, 200d, getCurrentTimestampDbFormat());
        data.getMt5DealsCoercedDdObjects().add(mt5DealsCoercedDd);
        return data;
    }

    public static Map<String, DataHelper> setupGapTradingRuleData() {
        startSshTunnel();
        Map<String, DataHelper> map = new HashMap<>();
        map.put("1", getGapTradingRuleExitEventEnd1Data());
        map.put("2", getGapTradingRuleExitEventEnd2Data());
        map.put("3", getGapTradingRuleExitEventEnd3Data());
        map.put("4", getGapTradingRuleExitEventEnd4Data());
        map.put("5", getGapTradingRuleExitEventEnd5Data());
        map.put("6", getGapTradingRuleExitEventEnd6Data());
        map.put("7", getGapTradingRuleExitEventEnd7Data());
        map.put("8", getGapTradingRuleExitEventEnd8Data());
        map.put("alert", getGapTradingRuleExitEventEndAlertData());
        return map;
    }
}
