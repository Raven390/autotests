package helpers.data.rules.no_slippage_rule;

import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import helpers.data.ClientHelper;
import helpers.data.rules.RuleDataHelper;

import java.util.HashMap;
import java.util.Map;

import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.rules.RuleDataHelper.deleteRuleData;
import static helpers.data.rules.RuleDataHelper.setupRuleData;
import static helpers.database.DbHelper.startSshTunnel;

public class NoSlippageRuleDataFactory {

    private static final ClientHelper noSlippageRuleTest1Client = getRandomVantageClientAllFields();
    private static final ClientHelper noSlippageRuleTest2Client = getRandomVantageClientAllFields();
    private static final ClientHelper noSlippageRuleTest3Client = getRandomVantageClientAllFields();
    private static final ClientHelper noSlippageRuleTest4Client = getRandomVantageClientAllFields();
    private static final ClientHelper noSlippageRuleTest5Client = getRandomVantageClientAllFields();
    private static final ClientHelper noSlippageRuleTest6Client = getRandomVantageClientAllFields();
    private static final ClientHelper noSlippageRuleTest7Client = getRandomVantageClientAllFields();
    private static final ClientHelper noSlippageRuleTest8Client = getRandomVantageClientAllFields();
    private static final ClientHelper noSlippageRuleTest9Client = getRandomVantageClientAllFields();
    private static final ClientHelper noSlippageRuleTest10Client = getRandomVantageClientAllFields();
    private static final ClientHelper noSlippageRuleTest11Client = getRandomVantageClientAllFields();

    public static Map<String, RuleDataHelper> setupNoSlippageRuleData() {
        startSshTunnel();
        Map<String, RuleDataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("1", getNoSlippageRuleTest1Data());
        map.put("2", getNoSlippageRuleTest2Data());
        map.put("3", getNoSlippageRuleTest3Data());
        map.put("4", getNoSlippageRuleTest4Data());
        map.put("5", getNoSlippageRuleTest5Data());
        map.put("6", getNoSlippageRuleTest6Data());
        map.put("7", getNoSlippageRuleTest7Data());
        map.put("8", getNoSlippageRuleTest8Data());
        map.put("9", getNoSlippageRuleTest9Data());
        map.put("10", getNoSlippageRuleTest10Data());
        map.put("11", getNoSlippageRuleTest11Data());

        setupRuleData(map);

        return map;
    }

    public static void deleteNoSlippageRuleData(Map<String, RuleDataHelper> map) throws Exception {
        deleteRuleData(map);
    }

    private static RuleDataHelper getNoSlippageRuleData(ClientHelper client) {
        RuleDataHelper ruleData = new RuleDataHelper();
        CrmTbUserObject userObject = generateUserByClient(client);
        return ruleData;
    }

    public static RuleDataHelper getNoSlippageRuleTest1Data() {
        RuleDataHelper data = getNoSlippageRuleData(noSlippageRuleTest1Client);
        return data;
    }

    public static RuleDataHelper getNoSlippageRuleTest2Data() {
        RuleDataHelper data = getNoSlippageRuleData(noSlippageRuleTest2Client);
        return data;
    }

    public static RuleDataHelper getNoSlippageRuleTest3Data() {
        RuleDataHelper data = getNoSlippageRuleData(noSlippageRuleTest3Client);
        return data;
    }

    public static RuleDataHelper getNoSlippageRuleTest4Data() {
        RuleDataHelper data = getNoSlippageRuleData(noSlippageRuleTest4Client);
        return data;
    }

    public static RuleDataHelper getNoSlippageRuleTest5Data() {
        RuleDataHelper data = getNoSlippageRuleData(noSlippageRuleTest5Client);
        return data;
    }

    public static RuleDataHelper getNoSlippageRuleTest6Data() {
        RuleDataHelper data = getNoSlippageRuleData(noSlippageRuleTest6Client);
        return data;
    }

    public static RuleDataHelper getNoSlippageRuleTest7Data() {
        RuleDataHelper data = getNoSlippageRuleData(noSlippageRuleTest7Client);
        return data;
    }

    public static RuleDataHelper getNoSlippageRuleTest8Data() {
        RuleDataHelper data = getNoSlippageRuleData(noSlippageRuleTest8Client);
        return data;
    }

    public static RuleDataHelper getNoSlippageRuleTest9Data() {
        RuleDataHelper data = getNoSlippageRuleData(noSlippageRuleTest9Client);
        return data;
    }

    public static RuleDataHelper getNoSlippageRuleTest10Data() {
        RuleDataHelper data = getNoSlippageRuleData(noSlippageRuleTest10Client);
        return data;
    }

    public static RuleDataHelper getNoSlippageRuleTest11Data() {
        RuleDataHelper data = getNoSlippageRuleData(noSlippageRuleTest11Client);
        return data;
    }
}
