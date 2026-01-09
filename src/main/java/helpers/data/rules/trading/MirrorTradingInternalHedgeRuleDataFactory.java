package helpers.data.rules.trading;

import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.DataHelper.createClient;
import static helpers.database.DbHelper.startSshTunnel;
import static utils.Utils.convertTimestampToIsoFormat;
import static utils.Utils.getCurrentTimestampMillis;

import business_objects.kafka.InternalHedgeEvent;
import generator.annotations.RuleTestData;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import io.qameta.allure.Step;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import utils.Utils;

@RuleTestData("mirror-trading")
public class MirrorTradingInternalHedgeRuleDataFactory {

    private static final ClientHelper testClient1 = getRandomVantageClientAllFields();
    private static final ClientHelper testClient2 = getRandomVantageClientAllFields();
    private static final ClientHelper testClient21 = getRandomVantageClientAllFields();

    @Step("Create data for Mirror trading internal hedge rule")
    private static DataHelper getMirrorTradingInternalHedgeRuleData(ClientHelper client) {
        DataHelper data = new DataHelper();

        createClient(data, client);
        data.internalHedgeEvent = new InternalHedgeEvent();
        data.internalHedgeEvent.setType("internalHedge");
        data.internalHedgeEvent.setId(Utils.getRandomUuidString());
        data.internalHedgeEvent.setSchemaVersion("1.0");
        data.internalHedgeEvent.setTimestamp(Instant.now().getNano());
        data.internalHedgeEvent.setSymbolUnderlying("EURUSD");
        data.internalHedgeEvent.setPositiveLeg(new InternalHedgeEvent.Leg());
        data.internalHedgeEvent.getPositiveLeg().setUcid(client.getUcid());
        data.internalHedgeEvent.getPositiveLeg().setTradingAccount(client.getTradingAccount());
        data.internalHedgeEvent.getPositiveLeg().setServerId(client.getServerId());
        data.internalHedgeEvent.getPositiveLeg().setCloseTime(convertTimestampToIsoFormat(getCurrentTimestampMillis()));
        data.internalHedgeEvent
                .getPositiveLeg()
                .setCloseTimeUtc(convertTimestampToIsoFormat(getCurrentTimestampMillis()));
        data.internalHedgeEvent.getPositiveLeg().setShortProfitUsd(0d);
        data.internalHedgeEvent.setNegativeLeg(new InternalHedgeEvent.Leg());
        data.internalHedgeEvent.getNegativeLeg().setUcid(client.getUcid());
        data.internalHedgeEvent.getNegativeLeg().setTradingAccount(client.getTradingAccount());
        data.internalHedgeEvent.getNegativeLeg().setServerId(client.getServerId());
        data.internalHedgeEvent.getNegativeLeg().setCloseTime(convertTimestampToIsoFormat(getCurrentTimestampMillis()));
        data.internalHedgeEvent
                .getNegativeLeg()
                .setCloseTimeUtc(convertTimestampToIsoFormat(getCurrentTimestampMillis()));
        data.internalHedgeEvent.getNegativeLeg().setShortProfitUsd(0d);

        return data;
    }

    private static DataHelper getMirrorTradingMLModelTest1Data() {
        DataHelper data = getMirrorTradingInternalHedgeRuleData(testClient1);
        data.internalHedgeEvent.getPositiveLeg().setShortProfitUsd(50d);
        return data;
    }

    private static DataHelper getMirrorTradingMLModelTest2Data() {
        DataHelper data = getMirrorTradingInternalHedgeRuleData(testClient2);
        DataHelper data2 = getMirrorTradingInternalHedgeRuleData(testClient21);
        data.internalHedgeEvent.getPositiveLeg().setShortProfitUsd(50.01);
        data.internalHedgeEvent.getNegativeLeg().setServerId(data2.clientHelper.getServerId());
        data.internalHedgeEvent.getNegativeLeg().setTradingAccount(data2.clientHelper.getTradingAccount());
        data.internalHedgeEvent.getNegativeLeg().setUcid(data2.clientHelper.getUcid());
        return data;
    }

    public static Map<String, DataHelper> setupMirrorTradingInternalHedgeRuleData() throws InterruptedException {
        startSshTunnel();
        Map<String, DataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("1", getMirrorTradingMLModelTest1Data());
        map.put("2", getMirrorTradingMLModelTest2Data());
        return map;
    }
}
