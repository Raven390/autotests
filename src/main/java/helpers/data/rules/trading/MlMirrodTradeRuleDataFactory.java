package helpers.data.rules.trading;

import business_objects.kafka.MirrorScoreEvent;
import generator.annotations.RuleTestData;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import utils.Utils;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static business_objects.db.clickhouse.mt_tb_credits.MtTbCreditsObjectFactory.generateCreditsByClient;
import static business_objects.db.data_science.ucid_general_score.UcidGeneralScoreFactory.generateUcidGeneralScoreObject;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.DataHelper.createClient;
import static helpers.data.DataHelper.setupData;
import static helpers.database.DbHelper.startSshTunnel;

@RuleTestData("mirror-trading")
public class MlMirrodTradeRuleDataFactory {

    private static final ClientHelper mirrorTradeMLModelClient1 = getRandomVantageClientAllFields();
    private static final ClientHelper mirrorTradeMLModelClient2 = getRandomVantageClientAllFields();
    private static final ClientHelper mirrorTradeMLModelClient3 = getRandomVantageClientAllFields();
    private static final ClientHelper mirrorTradeMLModelClient4 = getRandomVantageClientAllFields();

    @Step("Create data for Mirror trading rule")
    private static DataHelper getMirrorTradingRuleData(ClientHelper client) {
        DataHelper data = new DataHelper();

        createClient(data, client);

        data.mirrorScoreEvent = new MirrorScoreEvent();
        data.mirrorScoreEvent.setType("mirrorScore");
        data.mirrorScoreEvent.setId(Utils.getRandomUuidString());
        data.mirrorScoreEvent.setSchemaVersion("2.0");
        data.mirrorScoreEvent.setTimestamp(Instant.now().getNano());
        data.mirrorScoreEvent.setActionTimeUtc("123");
        data.mirrorScoreEvent.setUcid(data.clientHelper.getUcid());
        data.mirrorScoreEvent.setCountAction(10);
        data.mirrorScoreEvent.setActionId("MT5-CLOSE-1734973200-01");
        data.mirrorScoreEvent.setUcidScore(1.1);
        return data;
    }

    @Description("ML Mirror trade rule. Exit if user has at least 1 closed alert. ElementId: Event_1flqa1d")
    private static DataHelper getMirrorTradingMLModelTest1Data() {
        DataHelper data = getMirrorTradingRuleData(mirrorTradeMLModelClient1);
        data.addAlert("Mirror Trading", "CLOSED");
        return data;
    }

    @Description("ML Mirror trade rule. Exit if user has at least 1 closed alert. ElementId: Event_1flqa1d")
    private static DataHelper getMirrorTradingMLModelTest2Data() {
        DataHelper data = getMirrorTradingRuleData(mirrorTradeMLModelClient2);
        data.mtTbCreditsObjects = List.of(generateCreditsByClient(data.clientHelper));
        data.addAlert("Mirror Trading", "CLOSED");
        return data;
    }

    @Description("ML Mirror trade rule. Exit if user has ucidScore < 0.7. ElementId: Event_09pix7t")
    private static DataHelper getMirrorTradingMLModelTest3Data() {
        DataHelper data = getMirrorTradingRuleData(mirrorTradeMLModelClient3);
        data.mtTbCreditsObjects = List.of(generateCreditsByClient(data.clientHelper));
        data.ucidGeneralScore = generateUcidGeneralScoreObject(data.clientHelper, 0.7d, 0.7d);
        return data;
    }

    @Description("ML Mirror trade rule. alert and restriction if user has ucidScore > 0.7. ElementId: Event_1flqa1d")
    private static DataHelper getMirrorTradingMLModelTest4Data() {
        DataHelper data = getMirrorTradingRuleData(mirrorTradeMLModelClient4);
        data.mtTbCreditsObjects = List.of(generateCreditsByClient(data.clientHelper));
        data.ucidGeneralScore = generateUcidGeneralScoreObject(data.clientHelper, 0.71d, 0.71d);
        return data;
    }

    public static Map<String, DataHelper> setupMlMirrorTradeRuleData() throws InterruptedException {
        startSshTunnel();
        Map<String, DataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("1", getMirrorTradingMLModelTest1Data());
        map.put("2", getMirrorTradingMLModelTest2Data());
        map.put("3", getMirrorTradingMLModelTest3Data());
        map.put("4", getMirrorTradingMLModelTest4Data());

        setupData(map);

        return map;
    }
}
