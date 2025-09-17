package helpers.data.rules.mirror_trading_close_trade_event_rule;

import business_objects.kafka.mt_events.CloseTradeMtEvent;
import business_objects.kafka.mt_events.TradeEventMetadata;
import generator.annotations.RuleTestData;
import helpers.data.ClientHelper;
import helpers.data.rules.RuleDataHelper;
import io.qameta.allure.Description;
import io.qameta.allure.Step;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateAccountByClient;
import static business_objects.db.clickhouse.crm_tb_account_for_mt.crm_tb_account.CrmTbAccountForMtObjectFactory.generateAccountForMtByClient;
import static business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositObjectFactory.generateDepositByClient;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.dict_is_test.DictIsTestObjectFactory.generateDictIsTestByClientFalse;
import static business_objects.db.clickhouse.dict_is_test.DictIsTestObjectFactory.generateDictIsTestByClientTrue;
import static business_objects.db.clickhouse.ln_session_parsed.LnSessionParsedObjectFactory.generateLexisNexisDataByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByClient;
import static business_objects.db.clickhouse.mt_balance_orders_table.MtBalanceOrdersObjectFactory.generateMtBalanceOrder;
import static business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedFactory.generateMt5DealsCoercedObject;
import static business_objects.db.clickhouse.mt_tb_credits.MtTbCreditsObjectFactory.generateCreditsByClient;
import static business_objects.db.data_science.ucid_general_score.UcidGeneralScoreFactory.generateUcidGeneralScoreObject;
import static business_objects.db.data_science.ucid_mirror_score_python.UcidMirrorScorePythonFactory.generateUcidMirrorScorePythonObject;
import static helpers.data.ClientFactory.*;
import static helpers.data.rules.RuleDataHelper.addAlert;
import static helpers.data.rules.RuleDataHelper.setupRuleData;
import static helpers.database.DbHelper.*;
import static utils.Constants.*;
import static utils.Utils.*;

@RuleTestData("mirror-trading")
public class MirrorTradingCloseTradeEventRuleDataFactory {
    private static final ClientHelper getMirrorTradingCloseTradeTest1Data = getRandomVantageClientAllFields();
    private static final ClientHelper getMirrorTradingCloseTradeTest2Data = getRandomVantageClientAllFields();
    private static final ClientHelper getMirrorTradingCloseTradeTest3Data = getRandomVantageClientAllFields();
    private static final ClientHelper getMirrorTradingCloseTradeTest4Data = getRandomVantageClientAllFields();
    private static final ClientHelper getMirrorTradingCloseTradeTest5Data = getRandomVantageClientAllFields();
    private static final ClientHelper getMirrorTradingCloseTradeTest6Data = getRandomVantageClientAllFields();
    private static final ClientHelper getMirrorTradingCloseTradeTest7Data = getRandomVantageClientAllFields();
    private static final ClientHelper getMirrorTradingCloseTradeTest8Data = getRandomVantageClientAllFields();
    private static final ClientHelper getMirrorTradingCloseTradeTest9Data = getRandomVantageClientAllFields();
    private static final ClientHelper getMirrorTradingCloseTradeTest10Data = getRandomVantageClientAllFields();
    private static final ClientHelper getMirrorTradingCloseTradeTest11Data = getRandomVantageClientAllFields();
    private static final ClientHelper getMirrorTradingCloseTradeTest12Data = getRandomVantageClientAllFields();
    private static final ClientHelper getMirrorTradingCloseTradeTest13Data = getRandomVantageClientAllFields();
    private static final ClientHelper getMirrorTradingCloseTradeTest14Data = getRandomVantageClientAllFields();
    private static final ClientHelper getMirrorTradingCloseTradeTest15Data = getRandomVantageClientAllFields();
    private static final ClientHelper getMirrorTradingCloseTradeTest16Data = getRandomVantageClientAllFields();
    private static final ClientHelper getMirrorTradingCloseTradeTest18Data = getRandomVantageClientAllFields();
    private static final ClientHelper getMirrorTradingCloseTradeTest19Data = getRandomVantageClientAllFields();
    private static final ClientHelper getMirrorTradingCloseTradeTest20Data = getRandomVantageClientAllFields();
    private static final ClientHelper getMirrorTradingCloseTradeTest21Data = getRandomVantageClientAllFields();
    private static final ClientHelper getMirrorTradingCloseTradeTest22Data = getRandomVantageClientAllFields();
    private static final ClientHelper getMirrorTradingCloseTradeTest23Data = getRandomVantageClientAllFields();

    @Step("Create data for Mirror trading rule")
    private static RuleDataHelper getMirrorTradingRuleData(ClientHelper client) {
        RuleDataHelper data = new RuleDataHelper();

        data.clientHelper = client;
        data.dictIsTestObject = generateDictIsTestByClientFalse(data.clientHelper);
        data.crmTbUserObject = generateUserByClient(client);
        data.crmTbAccountObject = generateAccountByClient(client, false);
        data.crmTbAccountForMtObject = generateAccountForMtByClient(client, false);
        data.mt5DealsCoercedObjects = List.of(generateMt5DealsCoercedObject(client));
        TradeEventMetadata metadata = new TradeEventMetadata("MT5");
        data.closeTradeMtEvent = new CloseTradeMtEvent(
                getRandomUuidString(), Instant.now().toString(), data.mt5DealsCoercedObjects.getFirst().getPositionId(), client.getTradingAccount(), data.mt5DealsCoercedObjects.getFirst().getVolumeLots(), data.mt5DealsCoercedObjects.getFirst().getSymbol(), data.clientHelper.getServerId(), MT_CLOSE_TRADE_EVENT, Instant.now().toString(), metadata, Instant.now().toString());
        return data;
    }


    public static RuleDataHelper getMirrorTradingCloseTradeTest1Data() {
        RuleDataHelper data = getMirrorTradingRuleData(getMirrorTradingCloseTradeTest1Data);
        data.dictIsTestObject = generateDictIsTestByClientTrue(data.clientHelper);
        return data;
    }

    @Description("Mirror trading. Ml model. Post alert and restriction if no previously resolved alerts. ElementId: Event_1m3mqdr")
    public static RuleDataHelper getMirrorTradingCloseTradeTest7Data() {
        RuleDataHelper data = getMirrorTradingRuleData(getMirrorTradingCloseTradeTest7Data);
        data.mtTbCreditsObjects = List.of(generateCreditsByClient(data.clientHelper));
        data.dictIsTestObject = generateDictIsTestByClientFalse(data.clientHelper);
        data.mt5DealsCoercedObjects = generateMt5DealsCoercedObject(data.clientHelper, 6);
        data.ucidMirrorScore = generateUcidMirrorScorePythonObject(data.clientHelper, 0.91d, 0.91d);
        data.ucidGeneralScore = generateUcidGeneralScoreObject(data.clientHelper, 0.91d, 0.91d);
        return data;
    }

    @Description("Mirror trading. Scotland. Exit without alert if trades count > 5. Event_end_8")
    public static RuleDataHelper getMirrorTradingCloseTradeTest10Data() {
        RuleDataHelper data = getMirrorTradingRuleData(getMirrorTradingCloseTradeTest10Data);
        data.mtTbCreditsObjects = List.of(generateCreditsByClient(data.clientHelper));
        data.mt5DealsCoercedObjects = generateMt5DealsCoercedObject(data.clientHelper, 5);
        return data;
    }

    @Description("Mirror trading. Scotland. Exit without alert if profit/(deposit+credit) < 0.6. Event_end_8")
    public static RuleDataHelper getMirrorTradingCloseTradeTest11Data() {
        RuleDataHelper data = getMirrorTradingRuleData(getMirrorTradingCloseTradeTest11Data);
        data.mtTbCreditsObjects = List.of(generateCreditsByClient(data.clientHelper));
        data.mtTbCreditsObjects.getFirst().amount = 1000d;
        data.mtTbCreditsObjects.getFirst().amountUsd = 1000d;
        data.crmTbDepositObjects = List.of(generateDepositByClient(data.clientHelper));
        data.crmTbDepositObjects.getFirst().amount = 1d;
        data.crmTbDepositObjects.getFirst().amountUsd = 1d;
        return data;
    }

    @Description("Mirror trading. Scotland. Exit without alert if Leverage < 200. Event_12inxex")
    public static RuleDataHelper getMirrorTradingCloseTradeTest12Data() {
        RuleDataHelper data = getMirrorTradingRuleData(getMirrorTradingCloseTradeTest12Data);
        data.mtTbCreditsObjects = List.of(generateCreditsByClient(data.clientHelper));
        data.mtTbCreditsObjects.getFirst().amount = 1d;
        data.mtTbCreditsObjects.getFirst().amountUsd = 1d;
        data.crmTbDepositObjects = List.of(generateDepositByClient(data.clientHelper));
        data.crmTbDepositObjects.getFirst().amount = 1d;
        data.crmTbDepositObjects.getFirst().amountUsd = 1d;
        // leverage
        data.mt5DealsCoercedObjects.getFirst().setNotionalValueUsd(1000d);
        data.mtAccountObject = generateMtAccountByClient(data.clientHelper);
        data.mtAccountObject.equityUsd = 1000d;
        return data;
    }

    @Description("Mirror trading. Scotland. Exit with alert and restriction if Leverage > 200. Event_end_4")
    public static RuleDataHelper getMirrorTradingCloseTradeTest13Data() {
        RuleDataHelper data = getMirrorTradingRuleData(getMirrorTradingCloseTradeTest13Data);
        data.mtTbCreditsObjects = List.of(generateCreditsByClient(data.clientHelper));
        data.mtTbCreditsObjects.getFirst().amount = 1d;
        data.mtTbCreditsObjects.getFirst().amountUsd = 1d;
        data.crmTbDepositObjects = List.of(generateDepositByClient(data.clientHelper));
        data.crmTbDepositObjects.getFirst().amount = 1d;
        data.crmTbDepositObjects.getFirst().amountUsd = 1d;
        // leverage
        data.mt5DealsCoercedObjects.getFirst().setNotionalValueUsd(1000d);
        data.mtAccountObject = generateMtAccountByClient(data.clientHelper);
        data.mtAccountObject.equityUsd = 3d;
        data.mtAccountObject.equity = 3d;
        return data;
    }

    @Description("Mirror trading. Web hedge. Exit without alert if user geo is not vietnam. ElementId: Event_1t7mktu")
    public static RuleDataHelper getMirrorTradingCloseTradeTest18Data() {
        RuleDataHelper data = getMirrorTradingRuleData(getMirrorTradingCloseTradeTest18Data);
        data.mtTbCreditsObjects = List.of(generateCreditsByClient(data.clientHelper));
        return data;
    }

    @Description("Mirror trading. Web hedge. Exit without alert if user has no crypto deposits. ElementId: Event_06qi81c")
    public static RuleDataHelper getMirrorTradingCloseTradeTest19Data() {
        RuleDataHelper data = getMirrorTradingRuleData(getMirrorTradingCloseTradeTest19Data);
        data.lnSessionParsedObject = generateLexisNexisDataByClient(data.clientHelper);
        data.mtTbCreditsObjects = List.of(generateCreditsByClient(data.clientHelper));
        data.lnSessionParsedObject.setInputIpGeo("vn");
        data.lnSessionParsedObject.setTrueIpGeo("vn");
        data.lnSessionParsedObject.setBrowserLanguage("vn");
        return data;
    }

    @Description("Mirror trading. Web hedge. Exit without alert if user has country != vietnam. ElementId: Event_06qi81c")
    public static RuleDataHelper getMirrorTradingCloseTradeTest20Data() {
        RuleDataHelper data = getMirrorTradingRuleData(getMirrorTradingCloseTradeTest20Data);
        data.lnSessionParsedObject = generateLexisNexisDataByClient(data.clientHelper);
        data.mtTbCreditsObjects = List.of(generateCreditsByClient(data.clientHelper));
        data.lnSessionParsedObject.setInputIpGeo("vn");
        data.lnSessionParsedObject.setTrueIpGeo("vn");
        data.lnSessionParsedObject.setBrowserLanguage("vn");
        data.mtBalanceOrdersObjects = List.of(generateMtBalanceOrder(data.clientHelper, 1d, 1d, getCurrentTimestampDbFormat()));
        data.mtBalanceOrdersObjects.getFirst().comment = "crypto";
        return data;
    }

    @Description("Mirror trading. Web hedge. Exit without alert if user has not all trades from web trader. ElementId: Event_06qi81c")
    public static RuleDataHelper getMirrorTradingCloseTradeTest21Data() {
        RuleDataHelper data = getMirrorTradingRuleData(getMirrorTradingCloseTradeTest21Data);
        data.crmTbUserObject.isoCountryCode = "vn";
        data.lnSessionParsedObject = generateLexisNexisDataByClient(data.clientHelper);
        data.mtTbCreditsObjects = List.of(generateCreditsByClient(data.clientHelper));
        data.lnSessionParsedObject.setInputIpGeo("vn");
        data.lnSessionParsedObject.setTrueIpGeo("vn");
        data.lnSessionParsedObject.setBrowserLanguage("vn");
        data.mtBalanceOrdersObjects = List.of(generateMtBalanceOrder(data.clientHelper, 1d, 1d, getCurrentTimestampDbFormat()));
        data.mtBalanceOrdersObjects.getFirst().comment = "crypto";
        data.crmTbUserObject.country = "vn";
        data.mt5DealsCoercedObjects.getFirst().setReason(2);
        data.mt5DealsCoercedObjects = generateMt5DealsCoercedObject(data.clientHelper, 5);
        return data;
    }

    @Description("Mirror trading. Web hedge. Exit without alert if user has resolved alerts. ElementId: Event_06qi81c")
    public static RuleDataHelper getMirrorTradingCloseTradeTest22Data() {
        RuleDataHelper data = getMirrorTradingRuleData(getMirrorTradingCloseTradeTest22Data);
        data.crmTbUserObject.isoCountryCode = "vn";
        data.lnSessionParsedObject = generateLexisNexisDataByClient(data.clientHelper);
        data.mtTbCreditsObjects = List.of(generateCreditsByClient(data.clientHelper));
        data.lnSessionParsedObject.setInputIpGeo("vn");
        data.lnSessionParsedObject.setTrueIpGeo("vn");
        data.lnSessionParsedObject.setBrowserLanguage("vn");
        data.mtBalanceOrdersObjects = List.of(generateMtBalanceOrder(data.clientHelper, 1d, 1d, getCurrentTimestampDbFormat()));
        data.mtBalanceOrdersObjects.getFirst().comment = "crypto";
        data.crmTbUserObject.country = "vn";
        data.mt5DealsCoercedObjects.getFirst().setReason(2);

        addAlert(data, "Mirror Trading", "CLOSED");

        return data;
    }

    @Description("Mirror trading. Web hedge. Exit with restriction and alert if user doesn't has resolved alerts. ElementId: Event_06qi81c")
    public static RuleDataHelper getMirrorTradingCloseTradeTest23Data() {
        RuleDataHelper data = getMirrorTradingRuleData(getMirrorTradingCloseTradeTest23Data);
        data.crmTbUserObject.isoCountryCode = "vn";
        data.lnSessionParsedObject = generateLexisNexisDataByClient(data.clientHelper);
        data.mtTbCreditsObjects = List.of(generateCreditsByClient(data.clientHelper));
        data.lnSessionParsedObject.setInputIpGeo("vn");
        data.lnSessionParsedObject.setTrueIpGeo("vn");
        data.lnSessionParsedObject.setBrowserLanguage("vn");
        data.mtBalanceOrdersObjects = List.of(generateMtBalanceOrder(data.clientHelper, 1d, 1d, getCurrentTimestampDbFormat()));
        data.mtBalanceOrdersObjects.getFirst().comment = "crypto";
        data.crmTbUserObject.country = "vn";
        data.mt5DealsCoercedObjects.getFirst().setReason(2);
        return data;
    }

    public static Map<String, RuleDataHelper> setupMirrorTradingCloseTradeRuleData() {
        startSshTunnel();
        Map<String, RuleDataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("1", getMirrorTradingCloseTradeTest1Data());
        map.put("7", getMirrorTradingCloseTradeTest7Data());
        map.put("10", getMirrorTradingCloseTradeTest10Data());
        map.put("11", getMirrorTradingCloseTradeTest11Data());
        map.put("12", getMirrorTradingCloseTradeTest12Data());
        map.put("13", getMirrorTradingCloseTradeTest13Data());
        map.put("18", getMirrorTradingCloseTradeTest18Data());
        map.put("19", getMirrorTradingCloseTradeTest19Data());
        map.put("20", getMirrorTradingCloseTradeTest20Data());
        map.put("21", getMirrorTradingCloseTradeTest21Data());
        map.put("22", getMirrorTradingCloseTradeTest22Data());
        map.put("23", getMirrorTradingCloseTradeTest23Data());

        setupRuleData(map);

        return map;
    }
}
