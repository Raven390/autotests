package helpers.data.rules.trading.mirror_trading_close_trade;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateAccountByClient;
import static business_objects.db.clickhouse.crm_tb_account_for_mt.crm_tb_account.CrmTbAccountForMtObjectFactory.generateAccountForMtByClient;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.dict_is_test.DictIsTestObjectFactory.generateDictIsTestByClientFalse;
import static business_objects.db.clickhouse.ln_session_parsed.LnSessionParsedObjectFactory.generateLexisNexisDataByClient;
import static business_objects.db.clickhouse.mt_balance_orders_table.MtBalanceOrdersObjectFactory.generateMtBalanceOrder;
import static business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedFactory.generateMt5DealsCoercedObject;
import static business_objects.db.clickhouse.mt_tb_credits.MtTbCreditsObjectFactory.generateCreditsByClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.DataHelper.addAlert;
import static helpers.database.DbHelper.startSshTunnel;
import static utils.Utils.getCurrentTimestampDbFormat;
import static utils.Utils.getRandomUuidString;

import business_objects.kafka.mt_events.CloseTradeMtEvent;
import business_objects.kafka.mt_events.TradeEventMetadata;
import generator.annotations.RuleTestData;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import helpers.data.enums.rule_engine.Event;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RuleTestData("mirror-trading")
public class MirrorTradingWebHedgeDataFactory {
    private static final ClientHelper getMirrorTradingWebHedgeTestClient1 = getRandomVantageClientAllFields();
    private static final ClientHelper getMirrorTradingWebHedgeTestClient2 = getRandomVantageClientAllFields();
    private static final ClientHelper getMirrorTradingWebHedgeTestClient3 = getRandomVantageClientAllFields();
    private static final ClientHelper getMirrorTradingWebHedgeTestClient4 = getRandomVantageClientAllFields();
    private static final ClientHelper getMirrorTradingWebHedgeTestClient5 = getRandomVantageClientAllFields();
    private static final ClientHelper getMirrorTradingWebHedgeTestClient6 = getRandomVantageClientAllFields();

    @Step("Create data for Mirror trading rule")
    private static DataHelper getMirrorTradingRuleData(ClientHelper client) {
        DataHelper data = new DataHelper();

        data.clientHelper = client;
        data.dictIsTestObject = generateDictIsTestByClientFalse(data.clientHelper);
        data.crmTbUserObject = generateUserByClient(client);
        data.crmTbAccountObject = generateAccountByClient(client, false);
        data.crmTbAccountForMtObject = generateAccountForMtByClient(client, false);
        data.mt5DealsCoercedObjects = List.of(generateMt5DealsCoercedObject(client));
        TradeEventMetadata metadata = new TradeEventMetadata("MT5");
        data.closeTradeMtEvent = new CloseTradeMtEvent(
                getRandomUuidString(),
                Instant.now().toString(),
                data.mt5DealsCoercedObjects.getFirst().getPositionId(),
                client.getTradingAccount(),
                data.mt5DealsCoercedObjects.getFirst().getVolumeLots(),
                data.mt5DealsCoercedObjects.getFirst().getSymbol(),
                data.clientHelper.getServerId(),
                Event.MT_CLOSE_TRADE_EVENT.getName(),
                Instant.now().toString(),
                metadata,
                Instant.now().toString());
        return data;
    }

    @Description("Mirror trading. Web hedge. Exit without alert if user geo is not vietnam. ElementId: Event_1t7mktu")
    private static DataHelper getMirrorTradingWebHedgeTest1Data() {
        DataHelper data = getMirrorTradingRuleData(getMirrorTradingWebHedgeTestClient1);
        data.mtTbCreditsObjects = List.of(generateCreditsByClient(data.clientHelper));
        return data;
    }

    @Description(
            "Mirror trading. Web hedge. Exit without alert if user has no crypto deposits. ElementId: Event_06qi81c")
    private static DataHelper getMirrorTradingWebHedgeTest2Data() {
        DataHelper data = getMirrorTradingRuleData(getMirrorTradingWebHedgeTestClient2);
        data.lnSessionParsedObject = generateLexisNexisDataByClient(data.clientHelper);
        data.mtTbCreditsObjects = List.of(generateCreditsByClient(data.clientHelper));
        data.lnSessionParsedObject.setInputIpGeo("vn");
        data.lnSessionParsedObject.setTrueIpGeo("vn");
        data.lnSessionParsedObject.setBrowserLanguage("vn");
        return data;
    }

    @Description(
            "Mirror trading. Web hedge. Exit without alert if user has country != vietnam. ElementId: Event_06qi81c")
    private static DataHelper getMirrorTradingWebHedgeTest3Data() {
        DataHelper data = getMirrorTradingRuleData(getMirrorTradingWebHedgeTestClient3);
        data.lnSessionParsedObject = generateLexisNexisDataByClient(data.clientHelper);
        data.mtTbCreditsObjects = List.of(generateCreditsByClient(data.clientHelper));
        data.lnSessionParsedObject.setInputIpGeo("vn");
        data.lnSessionParsedObject.setTrueIpGeo("vn");
        data.lnSessionParsedObject.setBrowserLanguage("vn");
        data.mtBalanceOrdersObjects =
                List.of(generateMtBalanceOrder(data.clientHelper, 1d, 1d, getCurrentTimestampDbFormat()));
        data.mtBalanceOrdersObjects.getFirst().comment = "crypto";
        return data;
    }

    @Description(
            "Mirror trading. Web hedge. Exit without alert if user has not all trades from web trader. ElementId: Event_06qi81c")
    private static DataHelper getMirrorTradingWebHedgeTest4Data() {
        DataHelper data = getMirrorTradingRuleData(getMirrorTradingWebHedgeTestClient4);
        data.crmTbUserObject.isoCountryCode = "vn";
        data.lnSessionParsedObject = generateLexisNexisDataByClient(data.clientHelper);
        data.mtTbCreditsObjects = List.of(generateCreditsByClient(data.clientHelper));
        data.lnSessionParsedObject.setInputIpGeo("vn");
        data.lnSessionParsedObject.setTrueIpGeo("vn");
        data.lnSessionParsedObject.setBrowserLanguage("vn");
        data.mtBalanceOrdersObjects =
                List.of(generateMtBalanceOrder(data.clientHelper, 1d, 1d, getCurrentTimestampDbFormat()));
        data.mtBalanceOrdersObjects.getFirst().comment = "crypto";
        data.crmTbUserObject.country = "vn";
        data.mt5DealsCoercedObjects.getFirst().setReason(2);
        data.mt5DealsCoercedObjects = generateMt5DealsCoercedObject(data.clientHelper, 5);
        return data;
    }

    @Description("Mirror trading. Web hedge. Exit without alert if user has resolved alerts. ElementId: Event_06qi81c")
    private static DataHelper getMirrorTradingWebHedgeTest5Data() {
        DataHelper data = getMirrorTradingRuleData(getMirrorTradingWebHedgeTestClient5);
        data.crmTbUserObject.isoCountryCode = "vn";
        data.lnSessionParsedObject = generateLexisNexisDataByClient(data.clientHelper);
        data.mtTbCreditsObjects = List.of(generateCreditsByClient(data.clientHelper));
        data.lnSessionParsedObject.setInputIpGeo("vn");
        data.lnSessionParsedObject.setTrueIpGeo("vn");
        data.lnSessionParsedObject.setBrowserLanguage("vn");
        data.mtBalanceOrdersObjects =
                List.of(generateMtBalanceOrder(data.clientHelper, 1d, 1d, getCurrentTimestampDbFormat()));
        data.mtBalanceOrdersObjects.getFirst().comment = "crypto";
        data.crmTbUserObject.country = "vn";
        data.mt5DealsCoercedObjects.getFirst().setReason(2);

        addAlert(data, "Mirror Trading", "CLOSED");

        return data;
    }

    @Description(
            "Mirror trading. Web hedge. Exit with restriction and alert if user doesn't has resolved alerts. ElementId: Event_06qi81c")
    private static DataHelper getMirrorTradingWebHedgeTest6Data() {
        DataHelper data = getMirrorTradingRuleData(getMirrorTradingWebHedgeTestClient6);
        data.crmTbUserObject.isoCountryCode = "vn";
        data.lnSessionParsedObject = generateLexisNexisDataByClient(data.clientHelper);
        data.mtTbCreditsObjects = List.of(generateCreditsByClient(data.clientHelper));
        data.lnSessionParsedObject.setInputIpGeo("vn");
        data.lnSessionParsedObject.setTrueIpGeo("vn");
        data.lnSessionParsedObject.setBrowserLanguage("vn");
        data.mtBalanceOrdersObjects =
                List.of(generateMtBalanceOrder(data.clientHelper, 1d, 1d, getCurrentTimestampDbFormat()));
        data.mtBalanceOrdersObjects.getFirst().comment = "crypto";
        data.crmTbUserObject.country = "vn";
        data.mt5DealsCoercedObjects.getFirst().setReason(2);
        return data;
    }

    public static Map<String, DataHelper> setupMirrorTradingWebHedgeRuleData() throws InterruptedException {
        startSshTunnel();
        Map<String, DataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("1", getMirrorTradingWebHedgeTest1Data());
        map.put("2", getMirrorTradingWebHedgeTest2Data());
        map.put("3", getMirrorTradingWebHedgeTest3Data());
        map.put("4", getMirrorTradingWebHedgeTest4Data());
        map.put("5", getMirrorTradingWebHedgeTest5Data());
        map.put("6", getMirrorTradingWebHedgeTest6Data());
        return map;
    }
}
