package helpers.data.rules.general;

import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.DataHelper.*;
import static helpers.database.DbHelper.startSshTunnel;

import business_objects.kafka.CustomEvent;
import generator.annotations.RuleTestData;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import helpers.data.enums.FraudType;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import utils.Utils;

@RuleTestData("custom-rule")
public class CustomRuleDataFactory {

    private static final ClientHelper customRuleTest1Client = getRandomVantageClientAllFields();
    private static final ClientHelper customRuleTest2Client = getRandomVantageClientAllFields();
    private static final ClientHelper customRuleTest3Client = getRandomVantageClientAllFields();
    private static final ClientHelper customRuleTest4Client = getRandomVantageClientAllFields();
    private static final ClientHelper customRuleTest5Client = getRandomVantageClientAllFields();
    private static final ClientHelper customRuleTest6Client = getRandomVantageClientAllFields();

    @Step("Create base test data for Custom rule")
    private static DataHelper getCustomRuleData(ClientHelper client) {
        DataHelper data = new DataHelper();

        createClient(data, client);

        data.customEvent = new CustomEvent();
        data.customEvent.setId(Utils.getRandomUuidString());
        data.customEvent.setType("custom_rule");
        data.customEvent.setTimestamp(Instant.now().toString());
        data.customEvent.setServerId(null);
        data.customEvent.setTradingAccount(null);
        data.customEvent.setBrand(null);
        data.customEvent.setClientId(null);
        data.customEvent.setAlert("");
        data.customEvent.setFraudType("");
        data.customEvent.setRestriction("");
        data.customEvent.setSource("autotest launch");
        data.customEvent.setMessage("");
        return data;
    }

    @Description("Custom rule. ucid -> fraud type + restriction + alert")
    private static DataHelper getCustomRuleTest1Data() {
        DataHelper data = getCustomRuleData(customRuleTest1Client);
        data.customEvent.setClientId(data.clientHelper.getUserId().toString());
        data.customEvent.setBrand(data.clientHelper.getBrand());
        data.customEvent.setFraudType(FraudType.HEDGING.getCode());
        data.customEvent.setAlert(
                "Client repeatedly opens opposite-direction trades using known hedging EA comments ('vef', 'My Order').");
        data.customEvent.setRestriction("WR");
        return data;
    }

    @Description("Custom rule. trading account + server -> fraud type + restriction + alert")
    private static DataHelper getCustomRuleTest2Data() {
        DataHelper data = getCustomRuleData(customRuleTest2Client);
        data.customEvent.setServerId(data.clientHelper.getServerId().toString());
        data.customEvent.setTradingAccount(data.clientHelper.getTradingAccount().toString());
        data.customEvent.setFraudType(FraudType.HEDGING.getCode());
        data.customEvent.setAlert(
                "Client repeatedly opens opposite-direction trades using known hedging EA comments ('vef', 'My Order').");
        data.customEvent.setRestriction("WR");
        return data;
    }

    @Description("Custom rule. trading account + server -> fraud type")
    private static DataHelper getCustomRuleTest3Data() {
        DataHelper data = getCustomRuleData(customRuleTest3Client);
        data.customEvent.setServerId(data.clientHelper.getServerId().toString());
        data.customEvent.setTradingAccount(data.clientHelper.getTradingAccount().toString());
        data.customEvent.setFraudType(FraudType.HEDGING.getCode());
        return data;
    }

    @Description("Custom rule. trading account + server -> restriction")
    private static DataHelper getCustomRuleTest4Data() {
        DataHelper data = getCustomRuleData(customRuleTest4Client);
        data.customEvent.setServerId(data.clientHelper.getServerId().toString());
        data.customEvent.setTradingAccount(data.clientHelper.getTradingAccount().toString());
        data.customEvent.setRestriction("WR");
        return data;
    }

    @Description("Custom rule. trading account + server -> alert")
    private static DataHelper getCustomRuleTest5Data() {
        DataHelper data = getCustomRuleData(customRuleTest5Client);
        data.customEvent.setServerId(data.clientHelper.getServerId().toString());
        data.customEvent.setTradingAccount(data.clientHelper.getTradingAccount().toString());
        data.customEvent.setAlert(
                "Client repeatedly opens opposite-direction trades using known hedging EA comments ('vef', 'My Order').");
        return data;
    }

    @Description("Custom rule. trading account + server -> restriction")
    private static DataHelper getCustomRuleTest6Data() {
        DataHelper data = getCustomRuleData(customRuleTest6Client);
        data.customEvent.setServerId(data.clientHelper.getServerId().toString());
        data.customEvent.setTradingAccount(data.clientHelper.getTradingAccount().toString());
        data.customEvent.setRestriction("WT");
        data.customEvent.setMessage("WT comment");
        data.customEvent.setLevel("LOW");
        return data;
    }

    public static Map<String, DataHelper> setupCustomRuleData() {
        startSshTunnel();
        Map<String, DataHelper> map = new HashMap<>();
        // Put all the db data for setup in a list
        map.put("1", getCustomRuleTest1Data());
        map.put("2", getCustomRuleTest2Data());
        map.put("3", getCustomRuleTest3Data());
        map.put("4", getCustomRuleTest4Data());
        map.put("5", getCustomRuleTest5Data());
        map.put("6", getCustomRuleTest6Data());
        return map;
    }
}
