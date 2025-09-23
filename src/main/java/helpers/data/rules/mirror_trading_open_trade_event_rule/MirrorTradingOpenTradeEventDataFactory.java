package helpers.data.rules.mirror_trading_open_trade_event_rule;

import business_objects.kafka.mt_events.TradeEvent;
import business_objects.kafka.mt_events.TradeEventMetadata;
import com.fasterxml.jackson.databind.ObjectMapper;
import generator.annotations.RuleTestData;
import helpers.data.ClientHelper;
import helpers.data.enums.DateTimeFormat;
import helpers.data.DataHelper;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.Step;

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
import static business_objects.db.clickhouse.mt_tb_credits.MtTbCreditsObjectFactory.generateCreditsByClient;
import static business_objects.db.data_science.ucid_mirror_score_python.UcidMirrorScorePythonFactory.generateUcidMirrorScorePythonObject;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.DataHelper.setupData;
import static helpers.database.DbHelper.*;
import static utils.Utils.*;

@RuleTestData("mirror-trading-open-trade")
public class MirrorTradingOpenTradeEventDataFactory {
    public static KafkaHelper kafka = new KafkaHelper();
    public static ObjectMapper objectMapper = new ObjectMapper();
    private static final ClientHelper mirrorTradingOpenTradeEventTest1Client = getRandomVantageClientAllFields();
    private static final ClientHelper mirrorTradingOpenTradeEventTest2Client = getRandomVantageClientAllFields();
    private static final ClientHelper mirrorTradingOpenTradeEventTest3Client = getRandomVantageClientAllFields();
    private static final ClientHelper mirrorTradingOpenTradeEventTest4Client = getRandomVantageClientAllFields();
    private static final ClientHelper mirrorTradingOpenTradeEventTest5Client = getRandomVantageClientAllFields();
    private static final ClientHelper mirrorTradingOpenTradeEventTest6Client = getRandomVantageClientAllFields();
    private static final ClientHelper mirrorTradingOpenTradeEventTest7Client = getRandomVantageClientAllFields();


    @Step("Create base test data for Latency arbitrage rule")
    public static DataHelper getMirrorTradingOpenTradeEventRuleData(ClientHelper client) {
        DataHelper ruleData = new DataHelper();
        ruleData.clientHelper = client;
        ruleData.crmTbUserObject = generateUserByClient(ruleData.clientHelper);
        ruleData.crmTbAccountObject = generateAccountByClient(ruleData.clientHelper, false);
        ruleData.crmTbAccountForMtObject = generateAccountForMtByClient(ruleData.clientHelper, false);

        ruleData.tradeEvent = new TradeEvent();
        ruleData.tradeEvent.type = "openTrade";
        ruleData.tradeEvent.openTime = convertTimestampToIsoFormat(getCurrentTimestampMillis());
        ruleData.tradeEvent.openTimeUtc = convertTimestampToIsoFormat(getCurrentTimestampMillis());
        ruleData.tradeEvent.tradeId = 123;
        ruleData.tradeEvent.volume = 1;
        ruleData.tradeEvent.symbol = "EURUSD";
        ruleData.tradeEvent.equity = 100d;
        ruleData.tradeEvent.margin = 10d;
        ruleData.tradeEvent.freeMargin = 10d;
        ruleData.tradeEvent.balance = 10d;
        ruleData.tradeEvent.leverage = 10d;
        ruleData.tradeEvent.eventDate = convertTimestampToIsoFormat(getCurrentTimestampMillis());
        ruleData.tradeEvent.id = getRandomUuidString();
        ruleData.tradeEvent.metadata = new TradeEventMetadata("MT5");
        ruleData.tradeEvent.serverId = client.getServerId();
        ruleData.tradeEvent.tradingAccount = client.getTradingAccount();

        return ruleData;
    }

    @Step("Exit from rule without alert if user is test or social trader user")
    public static DataHelper getMirrorTradingOpenTradeEventRuleTest1Data() {
        DataHelper data = getMirrorTradingOpenTradeEventRuleData(mirrorTradingOpenTradeEventTest1Client);
        data.dictIsTestObject = generateDictIsTestByClientTrue(data.clientHelper);
        return data;
    }

    @Step("Exit from rule without alert if user has no credits")
    public static DataHelper getMirrorTradingOpenTradeEventRuleTest2Data() {
        DataHelper data = getMirrorTradingOpenTradeEventRuleData(mirrorTradingOpenTradeEventTest2Client);
        data.dictIsTestObject = generateDictIsTestByClientFalse(data.clientHelper);
        return data;
    }

    @Step("Mirror trading with open trade event. Exit without alert if trades count < 5")
    public static DataHelper getMirrorTradingOpenTradeEventRuleTest3Data() {
        DataHelper data = getMirrorTradingOpenTradeEventRuleData(mirrorTradingOpenTradeEventTest3Client);
        data.dictIsTestObject = generateDictIsTestByClientFalse(data.clientHelper);
        data.mtTbCreditsObjects = List.of(generateCreditsByClient(data.clientHelper));
        data.mtTbCreditsObjects.getFirst().createTime = getCurrentTimestampMinusOffsetFormatted(
                DateTimeFormat.DATE_AND_TIME, 0, 0, 1, 0, 0);
        data.mt5DealsCoercedObjects = generateMt5DealsCoercedObject(data.clientHelper, 4, getCurrentTimestampDbFormat());
        return data;
    }

    @Step("Mirror trading with open trade event. Exit without alert if trades count > 200")
    public static DataHelper getMirrorTradingOpenTradeEventRuleTest4Data() {
        DataHelper data = getMirrorTradingOpenTradeEventRuleData(mirrorTradingOpenTradeEventTest4Client);
        data.dictIsTestObject = generateDictIsTestByClientFalse(data.clientHelper);
        data.mtTbCreditsObjects = List.of(generateCreditsByClient(data.clientHelper));
        data.mtTbCreditsObjects.getFirst().createTime = getCurrentTimestampMinusOffsetFormatted(
                DateTimeFormat.DATE_AND_TIME, 0, 0, 1, 0, 0);
        data.mt5DealsCoercedObjects = generateMt5DealsCoercedObject(data.clientHelper, 201, getCurrentTimestampDbFormat());
        return data;
    }

    @Step("Mirror trading with open trade event. Exit without alert if ucidScore < 0.9")
    public static DataHelper getMirrorTradingOpenTradeEventRuleTest5Data() {
        DataHelper data = getMirrorTradingOpenTradeEventRuleData(mirrorTradingOpenTradeEventTest5Client);
        data.dictIsTestObject = generateDictIsTestByClientFalse(data.clientHelper);
        data.mtTbCreditsObjects = List.of(generateCreditsByClient(data.clientHelper));
        data.mtTbCreditsObjects.getFirst().createTime = getCurrentTimestampMinusOffsetFormatted(
                DateTimeFormat.DATE_AND_TIME, 0, 0, 1, 0, 0);
        data.mt5DealsCoercedObjects = generateMt5DealsCoercedObject(data.clientHelper, 10, getCurrentTimestampDbFormat());
        data.ucidMirrorScore = generateUcidMirrorScorePythonObject(data.clientHelper, 0.89d, 0.89d);

        return data;
    }

    @Step("Mirror trading with open trade event. Exit without alert user have at least 1 resolved alerts")
    public static DataHelper getMirrorTradingOpenTradeEventRuleTest6Data() {
        DataHelper data = getMirrorTradingOpenTradeEventRuleData(mirrorTradingOpenTradeEventTest6Client);
        data.dictIsTestObject = generateDictIsTestByClientFalse(data.clientHelper);
        data.mtTbCreditsObjects = List.of(generateCreditsByClient(data.clientHelper));
        data.mtTbCreditsObjects.getFirst().createTime = getCurrentTimestampMinusOffsetFormatted(
                DateTimeFormat.DATE_AND_TIME, 0, 0, 1, 0, 0);
        data.mt5DealsCoercedObjects = generateMt5DealsCoercedObject(data.clientHelper, 10, getCurrentTimestampDbFormat());
        data.ucidMirrorScore = generateUcidMirrorScorePythonObject(data.clientHelper, 0.91d, 0.91d);
        data.boAlertsObjects = List.of(generateAlert(data.clientHelper));
        data.boAlertsObjects.getFirst().setRule("Mirror Trading");
        data.boAlertsObjects.getFirst().setStatus("CLOSED");
        return data;
    }

    @Step("")
    public static DataHelper getMirrorTradingOpenTradeEventRuleTest7Data() {
        DataHelper data = getMirrorTradingOpenTradeEventRuleData(mirrorTradingOpenTradeEventTest7Client);
        data.dictIsTestObject = generateDictIsTestByClientFalse(data.clientHelper);
        data.mtTbCreditsObjects = List.of(generateCreditsByClient(data.clientHelper));
        data.mtTbCreditsObjects.getFirst().createTime = getCurrentTimestampMinusOffsetFormatted(
                DateTimeFormat.DATE_AND_TIME, 0, 0, 1, 0, 0);
        data.mt5DealsCoercedObjects = generateMt5DealsCoercedObject(data.clientHelper, 10, getCurrentTimestampDbFormat());
        data.ucidMirrorScore = generateUcidMirrorScorePythonObject(data.clientHelper, 0.91d, 0.91d);
        return data;
    }

    public static Map<String, DataHelper> setupMirrorTradingOpenTradeEventRuleData() {
        startSshTunnel();
        Map<String, DataHelper> map = new HashMap<>();
        // Put all the db data for setup in a list
        map.put("1", getMirrorTradingOpenTradeEventRuleTest1Data());
        map.put("2", getMirrorTradingOpenTradeEventRuleTest2Data());
        map.put("3", getMirrorTradingOpenTradeEventRuleTest3Data());
        map.put("4", getMirrorTradingOpenTradeEventRuleTest4Data());
        map.put("5", getMirrorTradingOpenTradeEventRuleTest5Data());
        map.put("6", getMirrorTradingOpenTradeEventRuleTest6Data());
        map.put("7", getMirrorTradingOpenTradeEventRuleTest7Data());

        setupData(map);

        return map;
    }
}
