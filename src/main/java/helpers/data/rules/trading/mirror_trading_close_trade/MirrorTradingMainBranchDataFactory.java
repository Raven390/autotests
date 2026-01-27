package helpers.data.rules.trading.mirror_trading_close_trade;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateAccountByClient;
import static business_objects.db.clickhouse.crm_tb_account_for_mt.crm_tb_account.CrmTbAccountForMtObjectFactory.generateAccountForMtByClient;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.dict_is_test.DictIsTestObjectFactory.generateDictIsTestByClientFalse;
import static business_objects.db.clickhouse.dict_is_test.DictIsTestObjectFactory.generateDictIsTestByClientTrue;
import static business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedFactory.generateMt5DealsCoercedObject;
import static business_objects.db.clickhouse.mt_tb_credits.MtTbCreditsObjectFactory.generateCreditsByClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.DbHelper.startSshTunnel;
import static utils.Utils.getRandomUuidString;

import business_objects.kafka.mt_events.CloseTradeMtEvent;
import business_objects.kafka.mt_events.TradeEventMetadata;
import generator.annotations.RuleTestData;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import helpers.data.enums.rule_engine.Event;
import io.qameta.allure.Step;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RuleTestData("mirror-trading")
public class MirrorTradingMainBranchDataFactory {
    private static final ClientHelper getMirrorTradingMainBranchClientTest1 = getRandomVantageClientAllFields();
    private static final ClientHelper getMirrorTradingMainBranchClientTest2 = getRandomVantageClientAllFields();
    private static final ClientHelper getMirrorTradingMainBranchClientTest3 = getRandomVantageClientAllFields();
    private static final ClientHelper getMirrorTradingMainBranchClientTest4 = getRandomVantageClientAllFields();
    private static final ClientHelper getMirrorTradingMainBranchClientTest5 = getRandomVantageClientAllFields();

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

    private static DataHelper getMirrorTradingMainBranchTest1Data() {
        DataHelper data = getMirrorTradingRuleData(getMirrorTradingMainBranchClientTest1);
        data.dictIsTestObject = generateDictIsTestByClientTrue(data.clientHelper);
        return data;
    }

    private static DataHelper getMirrorTradingMainBranchTest2Data() {
        DataHelper data = getMirrorTradingRuleData(getMirrorTradingMainBranchClientTest2);
        data.dictIsTestObject = generateDictIsTestByClientFalse(data.clientHelper);
        return data;
    }

    private static DataHelper getMirrorTradingMainBranchTest3Data() {
        DataHelper data = getMirrorTradingRuleData(getMirrorTradingMainBranchClientTest3);
        data.dictIsTestObject = generateDictIsTestByClientFalse(data.clientHelper);
        data.mtTbCreditsObjects = List.of(generateCreditsByClient(data.clientHelper));
        return data;
    }

    private static DataHelper getMirrorTradingMainBranchTest4Data() {
        DataHelper data = getMirrorTradingRuleData(getMirrorTradingMainBranchClientTest4);
        data.dictIsTestObject = generateDictIsTestByClientTrue(data.clientHelper);
        return data;
    }

    private static DataHelper getMirrorTradingMainBranchTest5Data() {
        DataHelper data = getMirrorTradingRuleData(getMirrorTradingMainBranchClientTest5);
        data.dictIsTestObject = generateDictIsTestByClientTrue(data.clientHelper);
        return data;
    }

    public static Map<String, DataHelper> setupMirrorTradingMainBranchRuleData() throws InterruptedException {
        startSshTunnel();
        Map<String, DataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("1", getMirrorTradingMainBranchTest1Data());
        map.put("2", getMirrorTradingMainBranchTest2Data());
        map.put("3", getMirrorTradingMainBranchTest3Data());
        map.put("4", getMirrorTradingMainBranchTest4Data());
        map.put("5", getMirrorTradingMainBranchTest5Data());
        return map;
    }
}
