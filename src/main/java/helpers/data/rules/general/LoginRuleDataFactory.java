package helpers.data.rules.general;

import business_objects.kafka.crm_events.LoginEvent;
import generator.annotations.RuleTestData;
import helpers.data.ClientHelper;
import helpers.data.enums.*;
import helpers.data.DataHelper;
import io.qameta.allure.Description;
import io.qameta.allure.Step;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static business_objects.db.clickhouse.client_fraud_types.ClientFraudTypesFactory.createClientFraudTypeCh;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.ln_session_parsed.LnSessionParsedObjectFactory.generateLexisNexisDataByClient;
import static business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedFactory.generateMt5DealsCoercedObject;
import static business_objects.db.data_science.ucid_general_score.UcidGeneralScoreFactory.generateUcidGeneralScoreObject;
import static helpers.api.AbuseRegistryHelper.addFraudsForClient;
import static helpers.data.ClientFactory.getRandomClientByBrandAndCountry;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.FraudType.*;
import static helpers.data.DataHelper.*;
import static helpers.database.DbHelper.*;
import static utils.Constants.*;
import static utils.Utils.*;

@RuleTestData("login-rule")
public class LoginRuleDataFactory {

    private static final ClientHelper loginRuleTest2Client = getRandomVantageClientAllFields();
    private static final ClientHelper loginRuleTest3Client = getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));
    private static final ClientHelper loginRuleTest6Client = getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));
    private static final ClientHelper loginRuleTest7Client = getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));
    private static final ClientHelper loginRuleTest8Client = getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));
    private static final ClientHelper loginRuleTest9Client = getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));
    private static final ClientHelper loginRuleTest10Client = getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));
    private static final ClientHelper loginRuleTest11Client = getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));
    private static final ClientHelper loginRuleTest12Client = getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));
    private static final ClientHelper loginRuleTest13Client = getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));
    private static final ClientHelper loginRuleTest15Client = getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));
    private static final ClientHelper loginRuleTest16Client = getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));
    private static final ClientHelper loginRuleTest17Client = getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));

    @Step("Create base test data for Login rule")
    private static DataHelper getLoginRuleData(ClientHelper client) {
        DataHelper data = new DataHelper();

        createClient(data, client);
        data.mt5DealsCoercedObjects = List.of(generateMt5DealsCoercedObject(data.clientHelper));
        data.lnSessionParsedObject = generateLexisNexisDataByClient(data.clientHelper);

        data.loginEvent = new LoginEvent(client.getBrand(), client.getUserId(), convertTimestampToIsoFormat(getCurrentTimestampMillis()), getRandomUuidString(), client.getIpAddress(), client.getUserId().toString(), "webAccount", client.getRegulator(), "1.0", CRM_LOGIN_EVENT);
        return data;
    }

    @Description("Login rule. Connection search sub-process. No toxic connections for non VT or PU users. Event.id end_cs_no_toxic")
    private static DataHelper getLoginRuleTest2Data() {
        DataHelper data = getLoginRuleData(loginRuleTest2Client);
        //Add connection
        data.lnSessionParsedObject = generateLexisNexisDataByClient(data.clientHelper);
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        connectedClient.setEmail(data.crmTbUserObject.email);
        connectedClient.setPhoneNumber(data.crmTbUserObject.phoneNum);
        data.connectedUsers = new ArrayList<>();
        data.connectedClientHelpers = new ArrayList<>();
        data.connectedUsers.add(generateUserByClient(connectedClient));
        data.connectedClientHelpers.add(connectedClient);

        addConnectionByEmailPhoneAttribute(data, connectedClient, 0.75);
        //
        return data;
    }

    @Description("Login rule. Connection search sub-process. No connections for VT or PU users. Event.id end_connections_not_found2")
    private static DataHelper getLoginRuleTest3Data() {
        DataHelper data = getLoginRuleData(loginRuleTest3Client);
        //Add connection
        data.lnSessionParsedObject = generateLexisNexisDataByClient(data.clientHelper);
        ClientHelper connectedClient = getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));
        connectedClient.setEmail(data.clientHelper.getEmail());
        connectedClient.setPhoneNumber(data.clientHelper.getPhoneNumber());
        data.connectedUsers = new ArrayList<>();
        data.connectedClientHelpers = new ArrayList<>();
        data.connectedUsers.add(generateUserByClient(connectedClient));
        data.connectedClientHelpers.add(connectedClient);
        data.clientFraudTypes = new ArrayList<>();
        data.clientFraudTypes.add(createClientFraudTypeCh(connectedClient.getUcid(), FraudTypeOld.CPA_ABUSE.getKey()));
        addConnectionByEmailPhoneAttribute(data, connectedClient, 0.76);
        //
        return data;
    }

    @Description("Login rule. Connection search sub-process. Model score<0.7, user is mirror trader without connections. Event.id end_no_str1_hedge")
    private static DataHelper getLoginRuleTest6Data() throws IOException, InterruptedException {
        DataHelper data = getLoginRuleData(loginRuleTest6Client);
        //Add connection
        ClientHelper connectedClient = getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));
        connectedClient.setEmail(data.clientHelper.getEmail());
        connectedClient.setPhoneNumber(data.clientHelper.getPhoneNumber());

        data.connectedUsers = new ArrayList<>();
        data.connectedClientHelpers = new ArrayList<>();

        data.connectedUsers.add(generateUserByClient(connectedClient));
        data.connectedClientHelpers.add(connectedClient);

        data.clientFraudTypes = new ArrayList<>();
        data.clientFraudTypes.add(createClientFraudTypeCh(connectedClient.getUcid(), FraudTypeOld.CPA_ABUSE.getKey()));

        addConnectionByEmailPhoneAttribute(data, connectedClient, 0.86);

        //add abuse
        insertObjectToDb(CRM_USER_TABLE_NAME, data.connectedUsers.getFirst());
        addFraudsForClient(data.connectedClientHelpers.getFirst(), List.of(HEDGING), FraudTypeStatus.CONFIRMED);

        //add model score
        data.ucidGeneralScore = generateUcidGeneralScoreObject(data.clientHelper, 0.71, 0.71);

        return data;
    }

    @Description("Login rule. Connection search sub-process. Model score> 0.7, user is mirror trader with strong connections. Event.id Event_1o2qu8z")
    private static DataHelper getLoginRuleTest7Data() throws IOException, InterruptedException {
        DataHelper data = getLoginRuleData(loginRuleTest7Client);

        ClientHelper connectedClient = getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));
        addConnectionByEmailPhoneAttribute(data, connectedClient, 0.86);

        //add abuse
        insertObjectToDb(CRM_USER_TABLE_NAME, data.connectedUsers.getFirst());
        addFraudsForClient(data.connectedClientHelpers.getFirst(), List.of(HEDGING), FraudTypeStatus.CONFIRMED);

        //Add second connection with abuse type no equal to hedging
        ClientHelper connectedClient2 = getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));
        addConnectionByEmailPhoneAttribute(data, connectedClient2, 1d);

        //add abuse
        insertObjectToDb(CRM_USER_TABLE_NAME, data.connectedUsers.getLast());
        addFraudsForClient(data.connectedClientHelpers.getLast(), List.of(CPA_ABUSE), FraudTypeStatus.CONFIRMED);

        // set model score
        data.ucidGeneralScore = generateUcidGeneralScoreObject(data.clientHelper, 0.71, 0.71);
        return data;
    }

    @Description("Login rule. Strong connection with HEDGING fraud. Event.id end_cs_abuse")
    private static DataHelper getLoginRuleTest8Data() throws IOException, InterruptedException {
        DataHelper data = getLoginRuleData(loginRuleTest8Client);

        //Add connection with abuse type equal to hedging
        ClientHelper connectedClient = getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));
        addConnectionByEmailPhoneAttribute(data, connectedClient, 0.86);
        //add abuse
        insertObjectToDb(CRM_USER_TABLE_NAME, data.connectedUsers.get(0));
        addFraudsForClient(data.connectedClientHelpers.get(0), List.of(HEDGING), FraudTypeStatus.CONFIRMED);

        //Add second connection with abuse type no equal to hedging
        ClientHelper connectedClient2 = getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));
        addConnectionByEmailPhoneAttribute(data, connectedClient2, 1d);
        //add abuse
        insertObjectToDb(CRM_USER_TABLE_NAME, data.connectedUsers.get(1));
        addFraudsForClient(data.connectedClientHelpers.get(1), List.of(HEDGING), FraudTypeStatus.CONFIRMED);

        // set model score
        data.ucidGeneralScore = generateUcidGeneralScoreObject(data.clientHelper, 0.71, 0.71);
        return data;
    }

    @Description("Login rule. Connection search sub-process. Model score> 0.7, fraud type is uknown. Event.id end_unknown_fraud_type")
    private static DataHelper getLoginRuleTest9Data() throws IOException, InterruptedException {
        DataHelper data = getLoginRuleData(loginRuleTest9Client);

        ClientHelper connectedClient = getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));
        addConnectionByEmailPhoneAttribute(data, connectedClient, 0.86);
        //add abuse
        insertObjectToDb(CRM_USER_TABLE_NAME, data.connectedUsers.getFirst());
        addFraudsForClient(data.connectedClientHelpers.getFirst(), List.of(MONEY_LAUNDRY), FraudTypeStatus.CONFIRMED);

        // set model score
        data.ucidGeneralScore = generateUcidGeneralScoreObject(data.clientHelper, 0.71, 0.71);
        return data;
    }

    @Description("Login rule. Connection search sub-process. Model score> 0.7, fraud type is Market manipulation. end_cs_abuse.id end_unknown_fraud_type")
    private static DataHelper getLoginRuleTest10Data() throws IOException, InterruptedException {
        DataHelper data = getLoginRuleData(loginRuleTest10Client);

        //Add connection with abuse type equal to uknown
        ClientHelper connectedClient = getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));
        addConnectionByEmailPhoneAttribute(data, connectedClient, 0.86);
        //add abuse
        insertObjectToDb(CRM_USER_TABLE_NAME, data.connectedUsers.get(0));
        addFraudsForClient(data.connectedClientHelpers.get(0), List.of(MARKET_MANIPULATION), FraudTypeStatus.CONFIRMED);

        // set model score
        data.ucidGeneralScore = generateUcidGeneralScoreObject(data.clientHelper, 0.71, 0.71);
        return data;
    }

    @Description("Login rule. Connection search sub-process. Model score> 0.7, fraud type is Bonus abuser and toxic account linked. end_cs_abuse.id")
    private static DataHelper getLoginRuleTest15Data() throws IOException, InterruptedException {
        DataHelper data = getLoginRuleData(loginRuleTest15Client);

        //Add connection with abuse type equal to uknown
        ClientHelper connectedClient = getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));
        addConnectionByPayoutIdAttribute(data, connectedClient);
        //add abuse
        insertObjectToDb(CRM_USER_TABLE_NAME, data.connectedUsers.get(0));
        addFraudsForClient(data.connectedClientHelpers.get(0), List.of(BONUS_ABUSE), FraudTypeStatus.CONFIRMED);

        // set model score
        data.ucidGeneralScore = generateUcidGeneralScoreObject(data.clientHelper, 0.71, 0.71);
        return data;
    }

    @Description("Login rule. Connection search sub-process. Model score> 0.7, fraud type is Chargeback. Event.id end_cs_abuse")
    private static DataHelper getLoginRuleTest11Data() throws IOException, InterruptedException {
        DataHelper data = getLoginRuleData(loginRuleTest11Client);

        //Add connection with abuse type equal to uknown
        ClientHelper connectedClient = getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));
        addConnectionByEmailPhoneAttribute(data, connectedClient, 0.86);
        //add abuse
        insertObjectToDb(CRM_USER_TABLE_NAME, data.connectedUsers.get(0));
        addFraudsForClient(data.connectedClientHelpers.get(0), List.of(CHARGEBACK), FraudTypeStatus.CONFIRMED);

        // set model score
        data.ucidGeneralScore = generateUcidGeneralScoreObject(data.clientHelper, 0.71, 0.71);
        return data;
    }

    @Description("Login rule. Connection search sub-process. Model score> 0.7, fraud type is Chargeback. Event.id end_no_mitigation")
    private static DataHelper getLoginRuleTest12Data() throws IOException, InterruptedException {
        DataHelper data = getLoginRuleData(loginRuleTest12Client);

        //Add connection with abuse type equal to unknown
        ClientHelper connectedClient = getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));
        addConnectionByEmailPhoneAttribute(data, connectedClient, 0.86);
        //add abuse
        insertObjectToDb(CRM_USER_TABLE_NAME, data.connectedUsers.get(0));
        addFraudsForClient(data.connectedClientHelpers.get(0), List.of(CPA_ABUSE), FraudTypeStatus.CONFIRMED);

        // set model score
        data.ucidGeneralScore = generateUcidGeneralScoreObject(data.clientHelper, 0.71, 0.71);
        return data;
    }

    @Description("Login rule. Strong connection with HEDGING fraud and has bonus restriction. Event.id end_hedge_ald_no_bonus")
    private static DataHelper getLoginRuleTest13Data() throws IOException, InterruptedException {
        DataHelper data = getLoginRuleData(loginRuleTest13Client);

        //Add connection with abuse type equal to hedging
        ClientHelper connectedClient = getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));
        addConnectionByEmailPhoneAttribute(data, connectedClient, 0.86);
        //add abuse
        insertObjectToDb(CRM_USER_TABLE_NAME, data.connectedUsers.getFirst());
        addFraudsForClient(data.connectedClientHelpers.getFirst(), List.of(HEDGING), FraudTypeStatus.CONFIRMED);

        //Add second connection with abuse type no equal to hedging
        ClientHelper connectedClient2 = getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));
        addConnectionByEmailPhoneAttribute(data, connectedClient2, 1d);
        //add abuse
        insertObjectToDb(CRM_USER_TABLE_NAME, data.connectedUsers.get(1));
        addFraudsForClient(data.connectedClientHelpers.get(1), List.of(HEDGING), FraudTypeStatus.CONFIRMED);

        // set model score
        data.ucidGeneralScore = generateUcidGeneralScoreObject(data.clientHelper, 0.71, 0.71);

        return data;
    }

    @Description("Login rule. Connection search sub-process. Exit if general score < 0.7. ElementId: end_gs_low")
    private static DataHelper getLoginRuleTest16Data() throws IOException, InterruptedException {
        DataHelper data = getLoginRuleData(loginRuleTest16Client);

        //Add connection with abuse type equal to hedging
        ClientHelper connectedClient = getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));
        addConnectionByEmailPhoneAttribute(data, connectedClient, 0.86);
        //add abuse
        insertObjectToDb(CRM_USER_TABLE_NAME, data.connectedUsers.getFirst());
        addFraudsForClient(data.connectedClientHelpers.getFirst(), List.of(HEDGING), FraudTypeStatus.CONFIRMED);

        //Add second connection with abuse type no equal to hedging
        ClientHelper connectedClient2 = getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));
        addConnectionByEmailPhoneAttribute(data, connectedClient2, 1d);
        //add abuse
        insertObjectToDb(CRM_USER_TABLE_NAME, data.connectedUsers.get(1));
        addFraudsForClient(data.connectedClientHelpers.get(1), List.of(HEDGING), FraudTypeStatus.CONFIRMED);

        // set model score
        data.ucidGeneralScore = generateUcidGeneralScoreObject(data.clientHelper, 0.69, 0.69);

        return data;
    }

    @Description("Login rule. Connection search sub-process. Exit if has WR that 24OP removed and current <= previous average generalScore. ElementId: Event_1fdy7w1")
    private static DataHelper getLoginRuleTest17Data() throws IOException, InterruptedException {
        DataHelper data = getLoginRuleData(loginRuleTest17Client);

        //Add connection with abuse type equal to hedging
        ClientHelper connectedClient = getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));
        addConnectionByEmailPhoneAttribute(data, connectedClient, 0.86);
        //add abuse
        insertObjectToDb(CRM_USER_TABLE_NAME, data.connectedUsers.getFirst());
        addFraudsForClient(data.connectedClientHelpers.getFirst(), List.of(HEDGING), FraudTypeStatus.CONFIRMED);

        //Add second connection with abuse type no equal to hedging
        ClientHelper connectedClient2 = getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));
        addConnectionByEmailPhoneAttribute(data, connectedClient2, 1d);
        //add abuse
        insertObjectToDb(CRM_USER_TABLE_NAME, data.connectedUsers.get(1));
        addFraudsForClient(data.connectedClientHelpers.get(1), List.of(HEDGING), FraudTypeStatus.CONFIRMED);

        // set model score
        data.ucidGeneralScore = generateUcidGeneralScoreObject(data.clientHelper, 0.99, 0.99);
        data.ucidGeneralScore = generateUcidGeneralScoreObject(data.clientHelper, 0.71, 0.71);
        return data;
    }

    public static Map<String, DataHelper> setupLoginRuleData() throws IOException, InterruptedException {
        startSshTunnel();
        Map<String, DataHelper> map = new HashMap<>();
        // Put all the db data for setup in a list
        map.put("2", getLoginRuleTest2Data());
        map.put("3", getLoginRuleTest3Data());
        map.put("6", getLoginRuleTest6Data());
        map.put("7", getLoginRuleTest7Data());
        map.put("8", getLoginRuleTest8Data());
        map.put("9", getLoginRuleTest9Data());
        map.put("10", getLoginRuleTest10Data());
        map.put("11", getLoginRuleTest11Data());
        map.put("12", getLoginRuleTest12Data());
        map.put("13", getLoginRuleTest13Data());
        map.put("15", getLoginRuleTest15Data());
        map.put("16", getLoginRuleTest16Data());
        map.put("17", getLoginRuleTest17Data());

        setupData(map);

        return map;
    }

}
