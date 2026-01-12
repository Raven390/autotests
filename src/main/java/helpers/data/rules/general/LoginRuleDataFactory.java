package helpers.data.rules.general;

import static business_objects.db.clickhouse.client_fraud_types.ClientFraudTypesFactory.createClientFraudTypeCh;
import static business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.ln_session_parsed.LnSessionParsedObjectFactory.generateLexisNexisDataByClient;
import static business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedFactory.generateMt5DealsCoercedObject;
import static business_objects.db.data_science.ucid_general_score.UcidGeneralScoreFactory.generateUcidGeneralScoreObject;
import static helpers.api.AbuseRegistryHelper.addFraudsForClient;
import static helpers.data.ClientFactory.*;
import static helpers.data.ClientFactory.getRandomVantageClient;
import static helpers.data.DataHelper.*;
import static helpers.data.enums.FraudType.*;
import static helpers.database.DbHelper.*;
import static utils.Constants.*;
import static utils.Utils.*;

import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntity;
import business_objects.kafka.crm_events.LoginEvent;
import generator.annotations.RuleTestData;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import helpers.data.enums.*;
import io.qameta.allure.Step;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RuleTestData("login-rule")
public class LoginRuleDataFactory {

    private static final ClientHelper loginRuleTest1Client = getRandomVantageClientAllFields();
    private static final ClientHelper loginRuleTest3Client =
            getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));
    private static final ClientHelper loginRuleTest6Client =
            getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));
    private static final ClientHelper loginRuleTest7Client =
            getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));
    private static final ClientHelper loginRuleTest8Client = getRandomVantageClient();
    private static final ClientHelper loginRuleTest9Client = getRandomVantageClient();
    private static final ClientHelper loginRuleTest10Client =
            getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));
    private static final ClientHelper loginRuleTest11Client =
            getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));
    private static final ClientHelper loginRuleTest12Client =
            getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));
    private static final ClientHelper loginRuleTest13Client =
            getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));
    private static final ClientHelper loginRuleTest15Client =
            getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));
    private static final ClientHelper loginRuleTest16Client =
            getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));
    private static final ClientHelper loginRuleTest17Client =
            getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));

    @Step("Create base test data for Login rule")
    private static DataHelper getLoginRuleData(ClientHelper client) {
        DataHelper data = new DataHelper();

        createClient(data, client);
        data.mt5DealsCoercedObjects = List.of(generateMt5DealsCoercedObject(data.clientHelper));
        data.lnSessionParsedObject = generateLexisNexisDataByClient(data.clientHelper);

        data.loginEvent = new LoginEvent(
                client.getBrand(),
                client.getUserId(),
                convertTimestampToIsoFormat(getCurrentTimestampMillis()),
                getRandomUuidString(),
                client.getIpAddress(),
                client.getUserId().toString(),
                "webAccount",
                client.getRegulator(),
                "1.0",
                CRM_LOGIN_EVENT);
        return data;
    }

    private static DataHelper getLoginRuleTest1Data() {
        DataHelper data = getLoginRuleData(loginRuleTest1Client);
        // Add connection
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

    private static DataHelper getLoginRuleTest3Data() {
        DataHelper data = getLoginRuleData(loginRuleTest3Client);
        // Add connection
        data.lnSessionParsedObject = generateLexisNexisDataByClient(data.clientHelper);
        ClientHelper connectedClient =
                getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));
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

    private static DataHelper getLoginRuleTest6Data() throws IOException, InterruptedException {
        DataHelper data = getLoginRuleData(loginRuleTest6Client);
        // Add connection
        ClientHelper connectedClient =
                getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));
        connectedClient.setEmail(data.clientHelper.getEmail());
        connectedClient.setPhoneNumber(data.clientHelper.getPhoneNumber());

        data.connectedUsers = new ArrayList<>();
        data.connectedClientHelpers = new ArrayList<>();

        data.connectedUsers.add(generateUserByClient(connectedClient));
        data.connectedClientHelpers.add(connectedClient);

        data.clientFraudTypes = new ArrayList<>();
        data.clientFraudTypes.add(createClientFraudTypeCh(connectedClient.getUcid(), FraudTypeOld.CPA_ABUSE.getKey()));

        addConnectionByEmailPhoneAttribute(data, connectedClient, 0.86);

        // add abuse
        insertObjectToDb(CRM_USER_TABLE_NAME, data.connectedUsers.getFirst());
        addFraudsForClient(data.connectedClientHelpers.getFirst(), List.of(HEDGING), FraudTypeStatus.CONFIRMED);

        // add model score
        data.ucidGeneralScore = generateUcidGeneralScoreObject(data.clientHelper, 0.71, 0.71);

        return data;
    }

    private static DataHelper getLoginRuleTest7Data() throws IOException, InterruptedException {
        DataHelper data = getLoginRuleData(loginRuleTest7Client);

        ClientHelper connectedClient =
                getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));
        addConnectionByEmailPhoneAttribute(data, connectedClient, 0.86);

        // add abuse
        insertObjectToDb(CRM_USER_TABLE_NAME, data.connectedUsers.getFirst());
        addFraudsForClient(data.connectedClientHelpers.getFirst(), List.of(HEDGING), FraudTypeStatus.CONFIRMED);

        // Add second connection with abuse type no equal to hedging
        ClientHelper connectedClient2 =
                getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));
        addConnectionByEmailPhoneAttribute(data, connectedClient2, 1d);

        // add abuse
        insertObjectToDb(CRM_USER_TABLE_NAME, data.connectedUsers.getLast());
        addFraudsForClient(data.connectedClientHelpers.getLast(), List.of(CPA_ABUSE), FraudTypeStatus.CONFIRMED);

        // set model score
        data.ucidGeneralScore = generateUcidGeneralScoreObject(data.clientHelper, 0.71, 0.71);
        return data;
    }

    private static DataHelper getLoginRuleTest8Data() throws IOException, InterruptedException {
        DataHelper data = getLoginRuleData(loginRuleTest8Client);

        // Add connection with abuse type equal to hedging
        ClientHelper connectedClient =
                getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));
        addConnectionByEmailPhoneAttribute(data, connectedClient, 0.86);
        // add abuse
        insertObjectToDb(CRM_USER_TABLE_NAME, data.connectedUsers.getFirst());
        addFraudsForClient(data.connectedClientHelpers.getFirst(), List.of(HEDGING), FraudTypeStatus.CONFIRMED);

        // Add second connection with abuse type no equal to hedging
        ClientHelper connectedClient2 =
                getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));
        addConnectionByEmailPhoneAttribute(data, connectedClient2, 1d);
        // add abuse
        insertObjectToDb(CRM_USER_TABLE_NAME, data.connectedUsers.get(1));
        addFraudsForClient(data.connectedClientHelpers.get(1), List.of(HEDGING), FraudTypeStatus.CONFIRMED);

        // set model score
        data.ucidGeneralScore = generateUcidGeneralScoreObject(data.clientHelper, 0.71, 0.71);
        return data;
    }

    private static DataHelper getLoginRuleTest9Data() throws IOException, InterruptedException {
        DataHelper data = getLoginRuleData(loginRuleTest9Client);

        ClientHelper connectedClient =
                getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));
        addConnectionByEmailPhoneAttribute(data, connectedClient, 0.86);
        // add abuse
        insertObjectToDb(CRM_USER_TABLE_NAME, data.connectedUsers.getFirst());
        addFraudsForClient(data.connectedClientHelpers.getFirst(), List.of(HEDGING), FraudTypeStatus.CONFIRMED);

        // Add second connection with abuse type no equal to hedging
        ClientHelper connectedClient2 =
                getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));
        addConnectionByEmailPhoneAttribute(data, connectedClient2, 1d);
        // add abuse
        insertObjectToDb(CRM_USER_TABLE_NAME, data.connectedUsers.get(1));
        addFraudsForClient(data.connectedClientHelpers.get(1), List.of(HEDGING), FraudTypeStatus.CONFIRMED);

        // set model score
        data.ucidGeneralScore = generateUcidGeneralScoreObject(data.clientHelper, 0.71, 0.71);

        CrmTbDepositEntity deposit = generateCrmTbDepositEntityByClient(data.clientHelper);
        deposit.setCreateTime(OffsetDateTime.parse("2026-01-01T01:01:01.111Z"));
        deposit.setCreateTimeUtc(OffsetDateTime.parse("2026-01-01T01:01:01.111Z"));
        data.crmTbDepositObjects = List.of(deposit);
        return data;
    }

    private static DataHelper getLoginRuleTest10Data() throws IOException, InterruptedException {
        DataHelper data = getLoginRuleData(loginRuleTest10Client);

        // Add connection with abuse type equal to uknown
        ClientHelper connectedClient =
                getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));
        addConnectionByEmailPhoneAttribute(data, connectedClient, 0.86);
        // add abuse
        insertObjectToDb(CRM_USER_TABLE_NAME, data.connectedUsers.get(0));
        addFraudsForClient(data.connectedClientHelpers.get(0), List.of(MARKET_MANIPULATION), FraudTypeStatus.CONFIRMED);

        // set model score
        data.ucidGeneralScore = generateUcidGeneralScoreObject(data.clientHelper, 0.71, 0.71);
        return data;
    }

    private static DataHelper getLoginRuleTest15Data() throws IOException, InterruptedException {
        DataHelper data = getLoginRuleData(loginRuleTest15Client);

        // Add connection with abuse type equal to uknown
        ClientHelper connectedClient =
                getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));
        addConnectionByPayoutIdAttribute(data, connectedClient);
        // add abuse
        insertObjectToDb(CRM_USER_TABLE_NAME, data.connectedUsers.get(0));
        addFraudsForClient(data.connectedClientHelpers.get(0), List.of(BONUS_ABUSE), FraudTypeStatus.CONFIRMED);

        // set model score
        data.ucidGeneralScore = generateUcidGeneralScoreObject(data.clientHelper, 0.71, 0.71);
        return data;
    }

    private static DataHelper getLoginRuleTest11Data() throws IOException, InterruptedException {
        DataHelper data = getLoginRuleData(loginRuleTest11Client);

        // Add connection with abuse type equal to uknown
        ClientHelper connectedClient =
                getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));
        addConnectionByEmailPhoneAttribute(data, connectedClient, 0.86);
        // add abuse
        insertObjectToDb(CRM_USER_TABLE_NAME, data.connectedUsers.get(0));
        addFraudsForClient(data.connectedClientHelpers.get(0), List.of(CHARGEBACK), FraudTypeStatus.CONFIRMED);

        // set model score
        data.ucidGeneralScore = generateUcidGeneralScoreObject(data.clientHelper, 0.71, 0.71);
        return data;
    }

    private static DataHelper getLoginRuleTest12Data() throws IOException, InterruptedException {
        DataHelper data = getLoginRuleData(loginRuleTest12Client);

        // Add connection with abuse type equal to unknown
        ClientHelper connectedClient =
                getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));
        addConnectionByEmailPhoneAttribute(data, connectedClient, 0.86);
        // add abuse
        insertObjectToDb(CRM_USER_TABLE_NAME, data.connectedUsers.get(0));
        addFraudsForClient(data.connectedClientHelpers.get(0), List.of(CPA_ABUSE), FraudTypeStatus.CONFIRMED);

        // set model score
        data.ucidGeneralScore = generateUcidGeneralScoreObject(data.clientHelper, 0.71, 0.71);
        return data;
    }

    private static DataHelper getLoginRuleTest13Data() throws IOException, InterruptedException {
        DataHelper data = getLoginRuleData(loginRuleTest13Client);

        // Add connection with abuse type equal to hedging
        ClientHelper connectedClient =
                getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));
        addConnectionByEmailPhoneAttribute(data, connectedClient, 0.86);
        // add abuse
        insertObjectToDb(CRM_USER_TABLE_NAME, data.connectedUsers.getFirst());
        addFraudsForClient(data.connectedClientHelpers.getFirst(), List.of(HEDGING), FraudTypeStatus.CONFIRMED);

        // Add second connection with abuse type no equal to hedging
        ClientHelper connectedClient2 =
                getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));
        addConnectionByEmailPhoneAttribute(data, connectedClient2, 1d);
        // add abuse
        insertObjectToDb(CRM_USER_TABLE_NAME, data.connectedUsers.get(1));
        addFraudsForClient(data.connectedClientHelpers.get(1), List.of(HEDGING), FraudTypeStatus.CONFIRMED);

        // set model score
        data.ucidGeneralScore = generateUcidGeneralScoreObject(data.clientHelper, 0.71, 0.71);

        return data;
    }

    private static DataHelper getLoginRuleTest16Data() throws IOException, InterruptedException {
        DataHelper data = getLoginRuleData(loginRuleTest16Client);

        // Add connection with abuse type equal to hedging
        ClientHelper connectedClient =
                getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));
        addConnectionByEmailPhoneAttribute(data, connectedClient, 0.86);
        // add abuse
        insertObjectToDb(CRM_USER_TABLE_NAME, data.connectedUsers.getFirst());
        addFraudsForClient(data.connectedClientHelpers.getFirst(), List.of(HEDGING), FraudTypeStatus.CONFIRMED);

        // Add second connection with abuse type no equal to hedging
        ClientHelper connectedClient2 =
                getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));
        addConnectionByEmailPhoneAttribute(data, connectedClient2, 1d);
        // add abuse
        insertObjectToDb(CRM_USER_TABLE_NAME, data.connectedUsers.get(1));
        addFraudsForClient(data.connectedClientHelpers.get(1), List.of(HEDGING), FraudTypeStatus.CONFIRMED);

        // set model score
        data.ucidGeneralScore = generateUcidGeneralScoreObject(data.clientHelper, 0.69, 0.69);

        return data;
    }

    private static DataHelper getLoginRuleTest17Data() throws IOException, InterruptedException {
        DataHelper data = getLoginRuleData(loginRuleTest17Client);

        // Add connection with abuse type equal to hedging
        ClientHelper connectedClient =
                getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));
        addConnectionByEmailPhoneAttribute(data, connectedClient, 0.86);
        // add abuse
        insertObjectToDb(CRM_USER_TABLE_NAME, data.connectedUsers.getFirst());
        addFraudsForClient(data.connectedClientHelpers.getFirst(), List.of(HEDGING), FraudTypeStatus.CONFIRMED);

        // Add second connection with abuse type no equal to hedging
        ClientHelper connectedClient2 =
                getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));
        addConnectionByEmailPhoneAttribute(data, connectedClient2, 1d);
        // add abuse
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
        map.put("1", getLoginRuleTest1Data());
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

        return map;
    }
}
