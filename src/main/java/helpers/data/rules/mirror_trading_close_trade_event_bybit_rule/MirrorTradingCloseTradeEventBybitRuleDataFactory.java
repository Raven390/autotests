package helpers.data.rules.mirror_trading_close_trade_event_bybit_rule;

import business_objects.kafka.mt_events.TradeEvent;
import business_objects.kafka.mt_events.TradeEventMetadata;
import generator.annotations.RuleTestData;
import helpers.data.ClientHelper;
import helpers.data.enums.Brand;
import helpers.data.rules.RuleDataHelper;
import io.qameta.allure.Step;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static business_objects.db.clickhouse.bo_alerts.BoAlertsFactory.generateAlert;
import static business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositObjectFactory.generateDepositByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByClient;
import static business_objects.db.clickhouse.mt_balance_orders_table.MtBalanceOrdersObjectFactory.generateMtBalanceOrder;
import static business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedFactory.generateMt5DealsCoercedObject;
import static business_objects.db.clickhouse.mt_tb_credits.MtTbCreditsObjectFactory.generateCreditsByClient;
import static business_objects.db.data_science.ucid_mirror_score_python.UcidMirrorScorePythonFactory.generateUcidMirrorScorePythonObject;
import static helpers.data.ClientFactory.getRandomClientByBrandAndCountry;
import static helpers.data.rules.RuleDataHelper.createClient;
import static helpers.data.rules.RuleDataHelper.setupRuleData;
import static helpers.database.DbHelper.startSshTunnel;
import static utils.Utils.*;

@RuleTestData("mirror-trading-bybit")
public class MirrorTradingCloseTradeEventBybitRuleDataFactory {
    private static final ClientHelper mirrorTradingCloseTradeBybitClient1 = getRandomClientByBrandAndCountry(Brand.BYBIT, "CYPRUS");
    private static final ClientHelper mirrorTradingCloseTradeBybitClient2 = getRandomClientByBrandAndCountry(Brand.BYBIT, "CYPRUS");
    private static final ClientHelper mirrorTradingCloseTradeBybitClient3 = getRandomClientByBrandAndCountry(Brand.BYBIT, "CYPRUS");
    private static final ClientHelper mirrorTradingCloseTradeBybitClient4 = getRandomClientByBrandAndCountry(Brand.BYBIT, "CYPRUS");
    private static final ClientHelper mirrorTradingCloseTradeBybitClient5 = getRandomClientByBrandAndCountry(Brand.BYBIT, "CYPRUS");
    private static final ClientHelper mirrorTradingCloseTradeBybitClient6 = getRandomClientByBrandAndCountry(Brand.BYBIT, "CYPRUS");
    private static final ClientHelper mirrorTradingCloseTradeBybitClient7 = getRandomClientByBrandAndCountry(Brand.BYBIT, "CYPRUS");
    private static final ClientHelper mirrorTradingCloseTradeBybitClient8 = getRandomClientByBrandAndCountry(Brand.BYBIT, "CYPRUS");
    private static final ClientHelper mirrorTradingCloseTradeBybitClient9 = getRandomClientByBrandAndCountry(Brand.BYBIT, "CYPRUS");
    private static final ClientHelper mirrorTradingCloseTradeBybitClient10 = getRandomClientByBrandAndCountry(Brand.BYBIT, "CYPRUS");
    private static final ClientHelper mirrorTradingCloseTradeBybitClient11 = getRandomClientByBrandAndCountry(Brand.BYBIT, "CYPRUS");
    private static final ClientHelper mirrorTradingCloseTradeBybitClient12 = getRandomClientByBrandAndCountry(Brand.BYBIT, "CYPRUS");
    private static final ClientHelper mirrorTradingCloseTradeBybitClient13 = getRandomClientByBrandAndCountry(Brand.BYBIT, "CYPRUS");
    private static final ClientHelper mirrorTradingCloseTradeBybitClient14 = getRandomClientByBrandAndCountry(Brand.BYBIT, "CYPRUS");
    private static final ClientHelper mirrorTradingCloseTradeBybitClient15 = getRandomClientByBrandAndCountry(Brand.BYBIT, "CYPRUS");
    private static final ClientHelper mirrorTradingCloseTradeBybitClient16 = getRandomClientByBrandAndCountry(Brand.BYBIT, "CYPRUS");

    @Step("Create data for Mirror trading rule")
    private static RuleDataHelper getMirrorTradingBybitRuleData(ClientHelper client) {
        RuleDataHelper data = new RuleDataHelper();
        client.setServerId(64);
        createClient(data, client);

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

    public static RuleDataHelper getMirrorTradingCloseTradeBybitTest1Data() {
        RuleDataHelper data = getMirrorTradingBybitRuleData(mirrorTradingCloseTradeBybitClient1);
        return data;
    }

    public static RuleDataHelper getMirrorTradingCloseTradeBybitTest2Data() {
        RuleDataHelper data = getMirrorTradingBybitRuleData(mirrorTradingCloseTradeBybitClient2);
        data.mtTbCreditsObjects = List.of(generateCreditsByClient(data.clientHelper, 1d));
        data.mt5DealsCoercedObjects = generateMt5DealsCoercedObject(data.clientHelper, 1);

        return data;
    }

    public static RuleDataHelper getMirrorTradingCloseTradeBybitTest3Data() {
        RuleDataHelper data = getMirrorTradingBybitRuleData(mirrorTradingCloseTradeBybitClient3);
        data.mtTbCreditsObjects = List.of(generateCreditsByClient(data.clientHelper, 1d));
        data.mt5DealsCoercedObjects = generateMt5DealsCoercedObject(data.clientHelper, 201);

        return data;
    }

    public static RuleDataHelper getMirrorTradingCloseTradeBybitTest4Data() {
        RuleDataHelper data = getMirrorTradingBybitRuleData(mirrorTradingCloseTradeBybitClient4);
        data.mtTbCreditsObjects = List.of(generateCreditsByClient(data.clientHelper, 1d));
        data.mt5DealsCoercedObjects = generateMt5DealsCoercedObject(data.clientHelper, 11);
        data.ucidMirrorScore = generateUcidMirrorScorePythonObject(data.clientHelper, 0.89d, 0.89d);
        return data;
    }

    public static RuleDataHelper getMirrorTradingCloseTradeBybitTest5Data() {
        RuleDataHelper data = getMirrorTradingBybitRuleData(mirrorTradingCloseTradeBybitClient5);
        data.mtTbCreditsObjects = List.of(generateCreditsByClient(data.clientHelper, 1d));
        data.mt5DealsCoercedObjects = generateMt5DealsCoercedObject(data.clientHelper, 11);
        data.ucidMirrorScore = generateUcidMirrorScorePythonObject(data.clientHelper, 0.91d, 0.91d);
        data.boAlertsObjects = List.of(generateAlert(data.clientHelper));
        data.boAlertsObjects.getFirst().setRule("Mirror Trading");
        data.boAlertsObjects.getFirst().setStatus("CLOSED");
        return data;
    }

    public static RuleDataHelper getMirrorTradingCloseTradeBybitTest6Data() {
        RuleDataHelper data = getMirrorTradingBybitRuleData(mirrorTradingCloseTradeBybitClient6);
        data.mtTbCreditsObjects = List.of(generateCreditsByClient(data.clientHelper, 1d));
        data.mt5DealsCoercedObjects = generateMt5DealsCoercedObject(data.clientHelper, 11);
        data.ucidMirrorScore = generateUcidMirrorScorePythonObject(data.clientHelper, 0.91d, 0.91d);
        return data;
    }

    public static RuleDataHelper getMirrorTradingCloseTradeBybitTest7Data() {
        RuleDataHelper data = getMirrorTradingBybitRuleData(mirrorTradingCloseTradeBybitClient7);
        data.mtTbCreditsObjects = List.of(generateCreditsByClient(data.clientHelper, 1d));
        data.mt5DealsCoercedObjects = generateMt5DealsCoercedObject(data.clientHelper, 1);
        data.mtBalanceOrdersObjects = List.of(
                generateMtBalanceOrder(data.clientHelper, 0d, 0d, getCurrentTimestampDbFormat()));
        data.mtBalanceOrdersObjects.getFirst().comment = "deposit";
        data.mtBalanceOrdersObjects.getFirst().amount = 5001d;
        data.mtBalanceOrdersObjects.getFirst().amountUsd = 5001d;
        return data;
    }

    public static RuleDataHelper getMirrorTradingCloseTradeBybitTest8Data() {
        RuleDataHelper data = getMirrorTradingBybitRuleData(mirrorTradingCloseTradeBybitClient8);

        data.mtTbCreditsObjects = List.of(generateCreditsByClient(data.clientHelper, 1d));
        data.mt5DealsCoercedObjects = generateMt5DealsCoercedObject(data.clientHelper, 301);
        data.mtBalanceOrdersObjects = List.of(
                generateMtBalanceOrder(data.clientHelper, 0d, 0d, getCurrentTimestampDbFormat()));
        data.mtBalanceOrdersObjects.getFirst().comment = "deposit";
        data.mtBalanceOrdersObjects.getFirst().amount = 500d;
        data.mtBalanceOrdersObjects.getFirst().amountUsd = 500d;

        return data;
    }

    public static RuleDataHelper getMirrorTradingCloseTradeBybitTest9Data() {
        RuleDataHelper data = getMirrorTradingBybitRuleData(mirrorTradingCloseTradeBybitClient9);

        data.mtTbCreditsObjects = List.of(generateCreditsByClient(data.clientHelper));
        data.mt5DealsCoercedObjects = generateMt5DealsCoercedObject(data.clientHelper, 5);

        return data;
    }

    public static RuleDataHelper getMirrorTradingCloseTradeBybitTest10Data() {
        RuleDataHelper data = getMirrorTradingBybitRuleData(mirrorTradingCloseTradeBybitClient10);

        data.mtTbCreditsObjects = List.of(generateCreditsByClient(data.clientHelper));
        data.mtTbCreditsObjects.getFirst().amount = 1000d;
        data.mtTbCreditsObjects.getFirst().amountUsd = 1000d;
        data.crmTbDepositObjects = List.of(generateDepositByClient(data.clientHelper));
        data.crmTbDepositObjects.getFirst().amount = 1d;
        data.crmTbDepositObjects.getFirst().amountUsd = 1d;

        return data;
    }

    public static RuleDataHelper getMirrorTradingCloseTradeBybitTest11Data() {
        RuleDataHelper data = getMirrorTradingBybitRuleData(mirrorTradingCloseTradeBybitClient11);

        data.mtTbCreditsObjects = List.of(generateCreditsByClient(data.clientHelper));
        data.mtTbCreditsObjects.getFirst().amount = 1000d;
        data.mtTbCreditsObjects.getFirst().amountUsd = 1000d;
        data.mtBalanceOrdersObjects = List.of(
                generateMtBalanceOrder(data.clientHelper, 1000d, 1000d, getCurrentTimestampDbFormat()));
        // leverage
        data.mt5DealsCoercedObjects = List.of(generateMt5DealsCoercedObject(data.clientHelper));
        data.mt5DealsCoercedObjects.getFirst().setNotionalValueUsd(1000d);
        data.mt5DealsCoercedObjects.getFirst().setProfit(10_000d);
        data.mt5DealsCoercedObjects.getFirst().setProfitUsd(10_000d);
        data.mtAccountObject = generateMtAccountByClient(data.clientHelper);
        data.mtAccountObject.equityUsd = 1000d;

        return data;
    }

    public static RuleDataHelper getMirrorTradingCloseTradeBybitTest12Data() {
        RuleDataHelper data = getMirrorTradingBybitRuleData(mirrorTradingCloseTradeBybitClient12);

        data.mtTbCreditsObjects = List.of(generateCreditsByClient(data.clientHelper));
        data.mtTbCreditsObjects.getFirst().amount = 1000d;
        data.mtTbCreditsObjects.getFirst().amountUsd = 1000d;
        data.mtBalanceOrdersObjects = List.of(
                generateMtBalanceOrder(data.clientHelper, 1000d, 1000d, getCurrentTimestampDbFormat()));
        // leverage
        data.mt5DealsCoercedObjects = List.of(generateMt5DealsCoercedObject(data.clientHelper));
        data.mt5DealsCoercedObjects.getFirst().setNotionalValueUsd(1000d);
        data.mt5DealsCoercedObjects.getFirst().setProfit(10_000d);
        data.mt5DealsCoercedObjects.getFirst().setProfitUsd(10_000d);
        data.mtAccountObject = generateMtAccountByClient(data.clientHelper);
        data.mtAccountObject.equityUsd = 3d;
        data.mtAccountObject.equity = 3d;

        return data;
    }

    public static RuleDataHelper getMirrorTradingCloseTradeBybitTest13Data() {
        RuleDataHelper data = getMirrorTradingBybitRuleData(mirrorTradingCloseTradeBybitClient13);

        data.mtTbCreditsObjects = List.of(generateCreditsByClient(data.clientHelper));
        data.mtTbCreditsObjects.getFirst().amount = 1000d;
        data.mtTbCreditsObjects.getFirst().amountUsd = 1000d;
        data.mtBalanceOrdersObjects = List.of(
                generateMtBalanceOrder(data.clientHelper, 1000d, 1000d, getCurrentTimestampDbFormat()));
        // leverage
        data.mt5DealsCoercedObjects = List.of(generateMt5DealsCoercedObject(data.clientHelper));
        data.mt5DealsCoercedObjects.getFirst().setNotionalValueUsd(1000d);
        data.mt5DealsCoercedObjects.getFirst().setProfit(10_000d);
        data.mt5DealsCoercedObjects.getFirst().setProfitUsd(10_000d);

        data.mtAccountObject = generateMtAccountByClient(data.clientHelper);
        data.mtAccountObject.equityUsd = 3d;
        data.mtAccountObject.equity = 3d;

        data.boAlertsObjects = List.of(generateAlert(data.clientHelper));
        data.boAlertsObjects.getFirst().setRule("Mirror Trading");
        data.boAlertsObjects.getFirst().setStatus("CLOSED");

        return data;
    }

    public static RuleDataHelper getMirrorTradingCloseTradeBybitTest14Data() {
        RuleDataHelper data = getMirrorTradingBybitRuleData(mirrorTradingCloseTradeBybitClient14);

        return data;
    }

    public static RuleDataHelper getMirrorTradingCloseTradeBybitTest15Data() {
        RuleDataHelper data = getMirrorTradingBybitRuleData(mirrorTradingCloseTradeBybitClient15);

        return data;
    }

    public static RuleDataHelper getMirrorTradingCloseTradeBybitTest16Data() {
        RuleDataHelper data = getMirrorTradingBybitRuleData(mirrorTradingCloseTradeBybitClient16);

        return data;
    }

    public static Map<String, RuleDataHelper> setupMirrorTradingCloseTradeBybitRuleData() {
        startSshTunnel();
        Map<String, RuleDataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("1", getMirrorTradingCloseTradeBybitTest1Data());
        map.put("2", getMirrorTradingCloseTradeBybitTest2Data());
        map.put("3", getMirrorTradingCloseTradeBybitTest3Data());
        map.put("4", getMirrorTradingCloseTradeBybitTest4Data());
        map.put("5", getMirrorTradingCloseTradeBybitTest5Data());
        map.put("6", getMirrorTradingCloseTradeBybitTest6Data());
        map.put("7", getMirrorTradingCloseTradeBybitTest7Data());
        map.put("8", getMirrorTradingCloseTradeBybitTest8Data());
        map.put("9", getMirrorTradingCloseTradeBybitTest9Data());
        map.put("10", getMirrorTradingCloseTradeBybitTest10Data());
        map.put("11", getMirrorTradingCloseTradeBybitTest11Data());
        map.put("12", getMirrorTradingCloseTradeBybitTest12Data());
        map.put("13", getMirrorTradingCloseTradeBybitTest13Data());
        map.put("14", getMirrorTradingCloseTradeBybitTest14Data());
        map.put("15", getMirrorTradingCloseTradeBybitTest15Data());
        map.put("16", getMirrorTradingCloseTradeBybitTest16Data());

        setupRuleData(map);

        return map;
    }
}
