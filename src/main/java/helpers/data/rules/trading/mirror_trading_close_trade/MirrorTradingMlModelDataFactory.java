package helpers.data.rules.trading.mirror_trading_close_trade;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateAccountByClient;
import static business_objects.db.clickhouse.crm_tb_account_for_mt.crm_tb_account.CrmTbAccountForMtObjectFactory.generateAccountForMtByClient;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.dict_is_test.DictIsTestObjectFactory.generateDictIsTestByClientFalse;
import static business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedFactory.generateMt5DealsCoercedObject;
import static business_objects.db.clickhouse.mt_tb_credits.MtTbCreditsObjectFactory.generateCreditsByClient;
import static business_objects.db.data_science.ucid_general_score.UcidGeneralScoreFactory.generateUcidGeneralScoreObject;
import static business_objects.db.data_science.ucid_mirror_score_python.UcidMirrorScorePythonFactory.generateUcidMirrorScorePythonObject;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.DbHelper.startSshTunnel;
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
public class MirrorTradingMlModelDataFactory {

    private static final ClientHelper mirrorTradeMLModelClient1 = getRandomVantageClientAllFields();
    private static final ClientHelper mirrorTradeMLModelClient2 = getRandomVantageClientAllFields();
    private static final ClientHelper mirrorTradeMLModelClient3 = getRandomVantageClientAllFields();
    private static final ClientHelper mirrorTradeMLModelClient4 = getRandomVantageClientAllFields();
    private static final ClientHelper mirrorTradeMLModelClient5 = getRandomVantageClientAllFields();

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

    @Description("")
    private static DataHelper getMirrorTradingMLModelTest1Data() {
        DataHelper data = getMirrorTradingRuleData(mirrorTradeMLModelClient1);
        return data;
    }

    @Description("")
    private static DataHelper getMirrorTradingMLModelTest2Data() {
        DataHelper data = getMirrorTradingRuleData(mirrorTradeMLModelClient2);
        return data;
    }

    @Description("")
    private static DataHelper getMirrorTradingMLModelTest3Data() {
        DataHelper data = getMirrorTradingRuleData(mirrorTradeMLModelClient3);
        return data;
    }

    @Description("")
    private static DataHelper getMirrorTradingMLModelTest4Data() {
        DataHelper data = getMirrorTradingRuleData(mirrorTradeMLModelClient4);
        return data;
    }

    @Description(
            "Mirror trading. Ml model. Post alert and restriction if no previously resolved alerts. ElementId: Event_1m3mqdr")
    private static DataHelper getMirrorTradingMLModelTest5Data() {
        DataHelper data = getMirrorTradingRuleData(mirrorTradeMLModelClient5);
        data.mtTbCreditsObjects = List.of(generateCreditsByClient(data.clientHelper));
        data.dictIsTestObject = generateDictIsTestByClientFalse(data.clientHelper);
        data.mt5DealsCoercedObjects = generateMt5DealsCoercedObject(data.clientHelper, 6);
        data.ucidMirrorScore = generateUcidMirrorScorePythonObject(data.clientHelper, 0.91d, 0.91d);
        data.ucidGeneralScore = generateUcidGeneralScoreObject(data.clientHelper, 0.91d, 0.91d);
        return data;
    }

    public static Map<String, DataHelper> setupMirrorTradingMLModelRuleData() throws InterruptedException {
        startSshTunnel();
        Map<String, DataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("1", getMirrorTradingMLModelTest1Data());
        map.put("2", getMirrorTradingMLModelTest2Data());
        map.put("3", getMirrorTradingMLModelTest3Data());
        map.put("4", getMirrorTradingMLModelTest4Data());
        map.put("5", getMirrorTradingMLModelTest5Data());
        return map;
    }
}
