package helpers.data.rules.mirror_trading_close_trade_event_rule;

import business_objects.kafka.mt_events.CloseTradeMtEvent;
import business_objects.kafka.mt_events.TradeEventMetadata;
import generator.annotations.RuleTestData;
import helpers.data.ClientHelper;
import helpers.data.rules.RuleDataHelper;
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
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByClient;
import static business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedFactory.generateMt5DealsCoercedObject;
import static business_objects.db.clickhouse.mt_tb_credits.MtTbCreditsObjectFactory.generateCreditsByClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.rules.RuleDataHelper.setupRuleData;
import static helpers.database.DbHelper.*;
import static helpers.database.CleanTableHelper.*;
import static utils.Constants.*;
import static utils.Utils.*;

@RuleTestData("mirror-trading")
public class MirrorTradingOpenTradeEventRuleDataFactory {
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

    public static RuleDataHelper getMirrorTradingCloseTradeTest10Data() {
        RuleDataHelper data = getMirrorTradingRuleData(getMirrorTradingCloseTradeTest10Data);
        data.mtTbCreditsObjects = List.of(generateCreditsByClient(data.clientHelper));
        data.mt5DealsCoercedObjects = generateMt5DealsCoercedObject(data.clientHelper, 5);
        return data;
    }

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

    public static Map<String, RuleDataHelper> setupMirrorTradingCloseTradeRuleData() {
        startSshTunnel();
        Map<String, RuleDataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("1", getMirrorTradingCloseTradeTest1Data());
        map.put("10", getMirrorTradingCloseTradeTest10Data());
        map.put("11", getMirrorTradingCloseTradeTest11Data());
        map.put("12", getMirrorTradingCloseTradeTest12Data());
        map.put("13", getMirrorTradingCloseTradeTest13Data());

        setupRuleData(map);

        return map;
    }
}
