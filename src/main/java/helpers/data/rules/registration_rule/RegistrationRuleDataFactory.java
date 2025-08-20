package helpers.data.rules.registration_rule;


import business_objects.db.clickhouse.ln_session_parsed.LnSessionParsedObject;
import business_objects.kafka.crm_events.RegistrationEvent;
import helpers.data.ClientHelper;
import helpers.data.enums.Brand;
import generator.annotations.RuleTestData;
import helpers.data.enums.Country;
import helpers.data.enums.FraudTypeStatus;
import helpers.data.rules.RuleDataHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateAccountByClient;
import static business_objects.db.clickhouse.crm_tb_account_for_mt.crm_tb_account.CrmTbAccountForMtObjectFactory.generateAccountForMtByClient;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static helpers.api.AbuseRegistryHelper.addFraudsForClient;
import static helpers.data.ClientFactory.getRandomClientByBrandAndCountry;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.FraudType.HEDGING;
import static helpers.data.rules.RuleDataHelper.*;
import static helpers.database.DbHelper.*;
import static utils.Constants.*;
import static utils.Utils.*;

@RuleTestData("registration")
public class RegistrationRuleDataFactory {

    // Clients
    private static final ClientHelper registrationRuleClient1 = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleClient2 = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleClient3 = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleClient4 = getRandomVantageClientAllFields();

    private static RuleDataHelper getRegistrationRuleData(ClientHelper client) {
        RuleDataHelper data = new RuleDataHelper();
        data.clientHelper = client;
        data.crmTbUserObject = generateUserByClient(data.clientHelper);
        data.crmTbAccountObject = generateAccountByClient(data.clientHelper, false);
        data.crmTbAccountForMtObject = generateAccountForMtByClient(data.clientHelper, false);
        data.crmTbUserObject.countryCode = data.clientHelper.getCountryCode();
        data.crmTbUserObject.isoCountryCode = data.clientHelper.getCountryCode();

        data.lnSessionParsedObject = new LnSessionParsedObject();
        data.lnSessionParsedObject.setPolicyScore(-49);
        data.lnSessionParsedObject.setRiskRating("low");

        data.registrationEvent = new RegistrationEvent();
        data.registrationEvent.setLexisNexis(new RegistrationEvent.LexisNexis());
        data.registrationEvent.setId(getRandomUuidString());
        data.registrationEvent.setBrand(data.clientHelper.getBrand());
        data.registrationEvent.setClientId(data.clientHelper.getUserId());
        data.registrationEvent.setEmail(data.clientHelper.getEmail());
        data.registrationEvent.setPhoneNumber(data.clientHelper.getPhoneNumber());
        data.registrationEvent.setType(REGISTRATION_EVENT);

        return data;
    }

    public static RuleDataHelper getRegistrationRuleData1() {
        RuleDataHelper data = getRegistrationRuleData(registrationRuleClient1);
        //No toxic accounts linked
        data.connections = null;
        //client have low risk in LN
        data.registrationEvent.getLexisNexis().setRiskRating("low");
        return data;
    }

    public static RuleDataHelper getRegistrationRuleData2() {
        RuleDataHelper data = getRegistrationRuleData(registrationRuleClient2);
        //No toxic accounts linked
        data.connections = null;
        //client have high in LN
        data.registrationEvent.getLexisNexis().setRiskRating("high");
        return data;
    }

    public static RuleDataHelper getRegistrationRuleData3() throws Exception {
        RuleDataHelper data = getRegistrationRuleData(registrationRuleClient3);
        //set true ip and device id
        data.registrationEvent.getLexisNexis().setTrueIp("");
        data.registrationEvent.getLexisNexis().setDevice("");

        //Add connection with abuse type equal to unknown
        ClientHelper connectedClient = getRandomClientByBrandAndCountry(Brand.VANTAGE, Country.getCountryNameByCodeUppercase("CY"));
        connectedClient.setIbId(data.clientHelper.getIbId());
        addConnectionByEmailPhoneAttribute(data, connectedClient, 0.76);
        //add abuse
        insertObjectToDb(CRM_USER_TABLE_NAME, data.connectedUsers.get(0));
        addFraudsForClient(data.connectedClientHelpers.get(0), List.of(HEDGING), FraudTypeStatus.CONFIRMED);
        return data;
    }

    public static RuleDataHelper getRegistrationRuleData4() throws Exception {
        RuleDataHelper data = getRegistrationRuleData(registrationRuleClient4);
        //set true ip and device id
        data.registrationEvent.getLexisNexis().setTrueIp("123");
        data.registrationEvent.getLexisNexis().setDevice(data.clientHelper.getDeviceId());

        //Add connection with abuse type equal to unknown
        ClientHelper connectedClient = getRandomClientByBrandAndCountry(Brand.VANTAGE, Country.getCountryNameByCodeUppercase("CY"));
        addConnectionByDeviceAttribute(data, connectedClient);
        //add abuse
        insertObjectToDb(CRM_USER_TABLE_NAME, data.connectedUsers.get(0));
        addFraudsForClient(data.connectedClientHelpers.get(0), List.of(HEDGING), FraudTypeStatus.POTENTIAL);
        return data;
    }

    public static Map<String, RuleDataHelper> setupRegistrationRuleData() throws Exception {
        startSshTunnel();
        Map<String, RuleDataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("1", getRegistrationRuleData1());
        map.put("2", getRegistrationRuleData2());
        map.put("3", getRegistrationRuleData3());
        map.put("4", getRegistrationRuleData4());

        setupRuleData(map);

        return map;
    }
}
