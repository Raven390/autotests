package helpers.data.rules.registration_rule;


import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.connection_table.ConnectionTableEntry;
import business_objects.kafka.crm_events.RegistrationEvent;
import helpers.data.ClientHelper;
import helpers.data.enums.Brand;
import generator.annotations.RuleTestData;
import helpers.data.enums.FraudTypeOld;
import helpers.data.rules.RuleDataHelper;
import net.datafaker.Faker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static business_objects.db.clickhouse.client_fraud_types.ClientFraudTypesFactory.createClientFraudTypeCh;
import static business_objects.db.clickhouse.connection_table.ConnectionTableEntry.ConnectionInfo.connectionInfoToString;
import static business_objects.db.clickhouse.connection_table.ConnectionTableEntryFactory.getConnection;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateAccountByClient;
import static business_objects.db.clickhouse.crm_tb_account_for_mt.crm_tb_account.CrmTbAccountForMtObjectFactory.generateAccountForMtByClient;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.email_table.EmailTableEntryFactory.emailTableEntryForConnectionSearch;
import static business_objects.db.clickhouse.ip_table.IpTableEntryFactory.ipTableEntryForConnectionSearch;
import static business_objects.db.clickhouse.ln_session_parsed.LnSessionParsedObjectFactory.generateLexisNexisDataByClient;
import static business_objects.db.clickhouse.phone.PhoneTableEntryFactory.phoneTableEntryForConnectionSearch;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.FraudTypeOld.*;
import static helpers.data.rules.RuleDataHelper.deleteRuleData;
import static helpers.data.rules.RuleDataHelper.setupRuleData;
import static helpers.database.DbHelper.*;
import static utils.Constants.*;
import static utils.Utils.*;

@RuleTestData("registration")
public class RegistrationRuleDataFactory {

    // Clients
    private static final ClientHelper registrationRuleClient1 = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleClient2 = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleExitEventEnd3p1Client = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleExitEventEnd3p2Client = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleExitEventEnd3p3Client = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleExitEventEnd3p4Client = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleExitEventEnd3p5Client = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleExitEventEnd3p6Client = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleExitEventEnd3p7Client = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleExitEventEnd3p8Client = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleExitEventEnd3p9Client = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleExitEventEnd3p10Client = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleExitEventEnd3p11Client = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleExitEventEnd3p12Client = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleExitEventEnd3p13Client = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleExitEventEnd3p14Client = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleExitEventEnd3p15Client = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleExitEventEnd3p16Client = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleExitEventEnd3p17Client = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleExitEventEnd3p18Client = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleExitEventEnd3p19Client = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleExitEventEnd3p20Client = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleExitEventEnd3p21Client = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleExitEventEnd3p22Client = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleExitEventEnd3p23Client = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleExitEventEnd3p24Client = getRandomVantageClientAllFields();

    static Faker faker = new Faker();

    private static RuleDataHelper getRegistrationRuleData(ClientHelper client) {
        RuleDataHelper ruleData = new RuleDataHelper();
        ruleData.clientHelper = client;
        ruleData.crmTbUserObject = generateUserByClient(ruleData.clientHelper);
        ruleData.crmTbAccountObject = generateAccountByClient(ruleData.clientHelper, false);
        ruleData.crmTbAccountForMtObject = generateAccountForMtByClient(ruleData.clientHelper, false);
        ruleData.crmTbUserObject.countryCode = client.getCountryCode();
        ruleData.crmTbUserObject.isoCountryCode = client.getCountryCode();

        ruleData.lnSessionParsedObject = generateLexisNexisDataByClient(client);
        ruleData.lnSessionParsedObject.setBrand(client.getBrand());
        ruleData.lnSessionParsedObject.setEventType("account_creation");
        ruleData.lnSessionParsedObject.setEmail(client.getEmail());
        ruleData.lnSessionParsedObject.setMobile(client.getPhoneNumber());
        ruleData.lnSessionParsedObject.setUserId(client.getUserId());
        ruleData.lnSessionParsedObject.setProxyIp(client.getIpAddress());
        ruleData.lnSessionParsedObject.setTrueIpGeo(client.getCountryCode());
        ruleData.lnSessionParsedObject.setSessionId(client.getSessionId());
        ruleData.lnSessionParsedObject.setPolicyScore(-49);
        ruleData.lnSessionParsedObject.setRiskRating("low");

        ruleData.registrationEvent = new RegistrationEvent();
        ruleData.registrationEvent.setId(getRandomUuidString());
        ruleData.registrationEvent.setBrand(client.getBrand());
        ruleData.registrationEvent.setClientId(client.getUserId());
        ruleData.registrationEvent.setEmail(client.getEmail());
        ruleData.registrationEvent.setType(REGISTRATION_EVENT);
        ruleData.registrationEvent.setLexisNexis(new RegistrationEvent.LexisNexis());

        return ruleData;
    }

    private static class ConnectionAndConnectedUser {
        public ConnectionTableEntry connectionTableEntry;
        public CrmTbUserObject crmTbUserObject;
        public ClientHelper clientHelper;

        public ConnectionAndConnectedUser(ConnectionTableEntry connectionTableEntry,
                CrmTbUserObject crmTbUserObject, ClientHelper clientHelper) {
            this.connectionTableEntry = connectionTableEntry;
            this.crmTbUserObject = crmTbUserObject;
            this.clientHelper = clientHelper;
        }
    }

    private static ConnectionAndConnectedUser getConnectionAndConnectedUser(ClientHelper fromClient,
            ClientHelper toClient) {
        ConnectionTableEntry connectionTableEntry = new ConnectionTableEntry(fromClient.getUcid(), toClient.getUcid(), CONNECTION_TYPE_SAME_IDENTITY, 1d, List.of(
                new ConnectionTableEntry.ConnectionInfo(CONNECTION_ATTRIBUTE_NAME_PAYOUT, CONNECTION_SEARCH_DATA_CARD_NUMBER, CONNECTION_SEARCH_DATA_CARD_NUMBER, CONNECTION_TYPE_RELATION_TYPE_EXACT)), getCurrentTimestampDbFormat());
        // Create connected user
        CrmTbUserObject connectedCrmTbUserObject = generateUserByClient(toClient);
        connectedCrmTbUserObject.isoCountryCode = fromClient.getCountryCode();
        connectedCrmTbUserObject.rafReferrerId = 22;
        connectedCrmTbUserObject.ibId = 33;
        return new ConnectionAndConnectedUser(connectionTableEntry, connectedCrmTbUserObject, toClient);
    }

    public static RuleDataHelper getRegistrationRuleExitEventEnd1Data() {
        RuleDataHelper data = getRegistrationRuleData(registrationRuleClient1);
        //No toxic accounts linked
        data.connections = null;
        //client have low risk in LN
        data.registrationEvent.getLexisNexis().setRiskRating("low");
        return data;
    }

    public static RuleDataHelper getRegistrationRuleExitEventEnd2Data() {
        RuleDataHelper data = getRegistrationRuleData(registrationRuleClient2);
        //No toxic accounts linked
        data.connections = null;
        //client have low high in LN
        data.registrationEvent.getLexisNexis().setRiskRating("high");
        return data;
    }

    public static RuleDataHelper getRegistrationRuleExitEventEnd3p1Data() {
        RuleDataHelper data = getRegistrationRuleData(registrationRuleExitEventEnd3p1Client);
        data.clientHelper.setBrand(Brand.VANTAGE);
        data.crmTbUserObject = generateUserByClient(registrationRuleExitEventEnd3p1Client);
        //add connected client
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        connectedClient.setEmail(data.crmTbUserObject.email);
        connectedClient.setPhoneNumber(data.crmTbUserObject.phoneNum);
        connectedClient.setBrand(Brand.VANTAGE);
        data.connectedUsers = new ArrayList<>();
        data.connectedClientHelpers = new ArrayList<>();
        data.connectedUsers.add(generateUserByClient(connectedClient));
        data.connectedClientHelpers.add(connectedClient);
        //add frauds for connected client
        data.clientFraudTypes = new ArrayList<>();
        data.clientFraudTypes.add(createClientFraudTypeCh(connectedClient.getUcid(), FraudTypeOld.CPA_ABUSE.getKey()));
        //add connection with connected client
        setupAttrConnection075(data);
        return data;
    }

    public static RuleDataHelper getRegistrationRuleExitEventEnd3p2Data() {
        RuleDataHelper data = getRegistrationRuleData(registrationRuleExitEventEnd3p2Client);
        data.clientHelper.setBrand(Brand.VANTAGE);
        data.crmTbUserObject = generateUserByClient(registrationRuleExitEventEnd3p2Client);
        //add connected client
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        connectedClient.setEmail(data.crmTbUserObject.email);
        connectedClient.setPhoneNumber(data.crmTbUserObject.phoneNum);
        connectedClient.setIpAddress(data.clientHelper.getIpAddress());
        connectedClient.setBrand(Brand.VANTAGE);
        data.connectedUsers = new ArrayList<>();
        data.connectedClientHelpers = new ArrayList<>();
        data.connectedUsers.add(generateUserByClient(connectedClient));
        data.connectedClientHelpers.add(connectedClient);
        //add frauds for connected client
        data.clientFraudTypes = new ArrayList<>();
        data.clientFraudTypes.add(createClientFraudTypeCh(connectedClient.getUcid(), FraudTypeOld.CPA_ABUSE.getKey()));
        //add connection with connected client
        setupAttrConnection06(data);
        return data;
    }

    public static RuleDataHelper getRegistrationRuleExitEventEnd3p3Data() {
        RuleDataHelper data = getRegistrationRuleData(registrationRuleExitEventEnd3p3Client);
        data.clientHelper.setBrand(Brand.VANTAGE);
        data.crmTbUserObject = generateUserByClient(registrationRuleExitEventEnd3p3Client);
        //add connected client
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        connectedClient.setEmail(data.crmTbUserObject.email);
        connectedClient.setPhoneNumber(data.crmTbUserObject.phoneNum);
        connectedClient.setBrand(Brand.VANTAGE);
        data.connectedUsers = new ArrayList<>();
        data.connectedClientHelpers = new ArrayList<>();
        data.connectedUsers.add(generateUserByClient(connectedClient));
        data.connectedClientHelpers.add(connectedClient);
        //add frauds for connected client
        data.clientFraudTypes = new ArrayList<>();
        data.clientFraudTypes.add(createClientFraudTypeCh(connectedClient.getUcid(), FraudTypeOld.HEDGING.getKey()));
        //add connection with connected client
        setupAttrConnection075(data);
        return data;
    }

    public static RuleDataHelper getRegistrationRuleExitEventEnd3p4Data() {
        RuleDataHelper data = getRegistrationRuleData(registrationRuleExitEventEnd3p4Client);
        data.clientHelper.setBrand(Brand.VANTAGE);
        data.crmTbUserObject = generateUserByClient(registrationRuleExitEventEnd3p4Client);
        //add connected client
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        connectedClient.setEmail(data.crmTbUserObject.email);
        connectedClient.setPhoneNumber(data.crmTbUserObject.phoneNum);
        connectedClient.setBrand(Brand.VANTAGE);
        data.connectedUsers = new ArrayList<>();
        data.connectedClientHelpers = new ArrayList<>();
        data.connectedUsers.add(generateUserByClient(connectedClient));
        data.connectedClientHelpers.add(connectedClient);
        //add frauds for connected client
        data.clientFraudTypes = new ArrayList<>();
        data.clientFraudTypes.add(createClientFraudTypeCh(connectedClient.getUcid(), FraudTypeOld.HEDGING.getKey()));
        //add connection with connected client
        setupAttrConnection06(data);
        return data;
    }

    public static RuleDataHelper getRegistrationRuleExitEventEnd3p5Data() {
        RuleDataHelper data = getRegistrationRuleData(registrationRuleExitEventEnd3p5Client);
        data.crmTbUserObject = generateUserByClient(registrationRuleExitEventEnd3p5Client);
        //add connected client
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        connectedClient.setEmail(data.crmTbUserObject.email);
        connectedClient.setPhoneNumber(data.crmTbUserObject.phoneNum);
        connectedClient.setBrand(Brand.VANTAGE);
        data.connectedUsers = new ArrayList<>();
        data.connectedClientHelpers = new ArrayList<>();
        data.connectedUsers.add(generateUserByClient(connectedClient));
        data.connectedClientHelpers.add(connectedClient);
        //add frauds for connected client
        data.clientFraudTypes = new ArrayList<>();
        data.clientFraudTypes.add(createClientFraudTypeCh(connectedClient.getUcid(), FraudTypeOld.LOSS_VOUCHER_ABUSE.getKey()));
        //add connection with connected client
        setupAttrConnection075(data);
        return data;
    }

    public static RuleDataHelper getRegistrationRuleExitEventEnd3p6Data() {
        RuleDataHelper data = getRegistrationRuleData(registrationRuleExitEventEnd3p6Client);
        data.crmTbUserObject = generateUserByClient(registrationRuleExitEventEnd3p6Client);
        //add connected client
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        connectedClient.setEmail(data.crmTbUserObject.email);
        connectedClient.setPhoneNumber(data.crmTbUserObject.phoneNum);
        connectedClient.setBrand(Brand.VANTAGE);
        data.connectedUsers = new ArrayList<>();
        data.connectedClientHelpers = new ArrayList<>();
        data.connectedUsers.add(generateUserByClient(connectedClient));
        data.connectedClientHelpers.add(connectedClient);
        //add frauds for connected client
        data.clientFraudTypes = new ArrayList<>();
        data.clientFraudTypes.add(createClientFraudTypeCh(connectedClient.getUcid(), FraudTypeOld.LOSS_VOUCHER_ABUSE.getKey()));
        //add connection with connected client
        setupAttrConnection06(data);
        return data;
    }

    public static RuleDataHelper getRegistrationRuleExitEventEnd3p7Data() {
        RuleDataHelper data = getRegistrationRuleData(registrationRuleExitEventEnd3p7Client);
        data.crmTbUserObject = generateUserByClient(registrationRuleExitEventEnd3p7Client);
        //add connected client
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        connectedClient.setEmail(data.crmTbUserObject.email);
        connectedClient.setPhoneNumber(data.crmTbUserObject.phoneNum);
        connectedClient.setBrand(Brand.VANTAGE);
        data.connectedUsers = new ArrayList<>();
        data.connectedClientHelpers = new ArrayList<>();
        data.connectedUsers.add(generateUserByClient(connectedClient));
        data.connectedClientHelpers.add(connectedClient);
        //add frauds for connected client
        data.clientFraudTypes = new ArrayList<>();
        data.clientFraudTypes.add(createClientFraudTypeCh(connectedClient.getUcid(), FraudTypeOld.NEWS_TRADER.getKey()));
        //add connection with connected client
        setupAttrConnection075(data);
        return data;
    }

    public static RuleDataHelper getRegistrationRuleExitEventEnd3p8Data() {
        RuleDataHelper data = getRegistrationRuleData(registrationRuleExitEventEnd3p8Client);
        data.crmTbUserObject = generateUserByClient(registrationRuleExitEventEnd3p8Client);
        //add connected client
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        connectedClient.setEmail(data.crmTbUserObject.email);
        connectedClient.setPhoneNumber(data.crmTbUserObject.phoneNum);
        connectedClient.setBrand(Brand.VANTAGE);
        data.connectedUsers = new ArrayList<>();
        data.connectedClientHelpers = new ArrayList<>();
        data.connectedUsers.add(generateUserByClient(connectedClient));
        data.connectedClientHelpers.add(connectedClient);
        //add frauds for connected client
        data.clientFraudTypes = new ArrayList<>();
        data.clientFraudTypes.add(createClientFraudTypeCh(connectedClient.getUcid(), FraudTypeOld.NEWS_TRADER.getKey()));
        //add connection with connected client
        setupAttrConnection06(data);
        return data;
    }

    public static RuleDataHelper getRegistrationRuleExitEventEnd3p9Data() {
        RuleDataHelper data = getRegistrationRuleData(registrationRuleExitEventEnd3p9Client);
        data.crmTbUserObject = generateUserByClient(registrationRuleExitEventEnd3p9Client);
        //add connected client
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        connectedClient.setEmail(data.crmTbUserObject.email);
        connectedClient.setPhoneNumber(data.crmTbUserObject.phoneNum);
        connectedClient.setBrand(Brand.VANTAGE);
        data.connectedUsers = new ArrayList<>();
        data.connectedClientHelpers = new ArrayList<>();
        data.connectedUsers.add(generateUserByClient(connectedClient));
        data.connectedClientHelpers.add(connectedClient);
        //add frauds for connected client
        data.clientFraudTypes = new ArrayList<>();
        data.clientFraudTypes.add(createClientFraudTypeCh(connectedClient.getUcid(), FraudTypeOld.TLS_ABUSE.getKey()));
        //add connection with connected client
        setupAttrConnection075(data);
        return data;
    }

    public static RuleDataHelper getRegistrationRuleExitEventEnd3p10Data() {
        RuleDataHelper data = getRegistrationRuleData(registrationRuleExitEventEnd3p10Client);
        data.crmTbUserObject = generateUserByClient(registrationRuleExitEventEnd3p10Client);
        //add connected client
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        connectedClient.setEmail(data.crmTbUserObject.email);
        connectedClient.setPhoneNumber(data.crmTbUserObject.phoneNum);
        connectedClient.setBrand(Brand.VANTAGE);
        data.connectedUsers = new ArrayList<>();
        data.connectedClientHelpers = new ArrayList<>();
        data.connectedUsers.add(generateUserByClient(connectedClient));
        data.connectedClientHelpers.add(connectedClient);
        //add frauds for connected client
        data.clientFraudTypes = new ArrayList<>();
        data.clientFraudTypes.add(createClientFraudTypeCh(connectedClient.getUcid(), FraudTypeOld.TLS_ABUSE.getKey()));
        //add connection with connected client
        setupAttrConnection06(data);
        return data;
    }

    public static RuleDataHelper getRegistrationRuleExitEventEnd3p11Data() {
        RuleDataHelper data = getRegistrationRuleData(registrationRuleExitEventEnd3p11Client);
        data.crmTbUserObject = generateUserByClient(registrationRuleExitEventEnd3p11Client);
        //add connected client
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        connectedClient.setEmail(data.crmTbUserObject.email);
        connectedClient.setPhoneNumber(data.crmTbUserObject.phoneNum);
        connectedClient.setBrand(Brand.VANTAGE);
        data.connectedUsers = new ArrayList<>();
        data.connectedClientHelpers = new ArrayList<>();
        data.connectedUsers.add(generateUserByClient(connectedClient));
        data.connectedClientHelpers.add(connectedClient);
        //add frauds for connected client
        data.clientFraudTypes = new ArrayList<>();
        data.clientFraudTypes.add(createClientFraudTypeCh(connectedClient.getUcid(), FraudTypeOld.SWAP_ARBITRAGE.getKey()));
        //add connection with connected client
        setupAttrConnection075(data);
        return data;
    }

    public static RuleDataHelper getRegistrationRuleExitEventEnd3p12Data() {
        RuleDataHelper data = getRegistrationRuleData(registrationRuleExitEventEnd3p12Client);
        data.crmTbUserObject = generateUserByClient(registrationRuleExitEventEnd3p12Client);
        //add connected client
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        connectedClient.setEmail(data.crmTbUserObject.email);
        connectedClient.setPhoneNumber(data.crmTbUserObject.phoneNum);
        connectedClient.setBrand(Brand.VANTAGE);
        data.connectedUsers = new ArrayList<>();
        data.connectedClientHelpers = new ArrayList<>();
        data.connectedUsers.add(generateUserByClient(connectedClient));
        data.connectedClientHelpers.add(connectedClient);
        //add frauds for connected client
        data.clientFraudTypes = new ArrayList<>();
        data.clientFraudTypes.add(createClientFraudTypeCh(connectedClient.getUcid(), FraudTypeOld.SWAP_ARBITRAGE.getKey()));
        //add connection with connected client
        setupAttrConnection06(data);
        return data;
    }

    public static RuleDataHelper getRegistrationRuleExitEventEnd3p13Data() {
        RuleDataHelper data = getRegistrationRuleData(registrationRuleExitEventEnd3p13Client);
        data.crmTbUserObject = generateUserByClient(registrationRuleExitEventEnd3p13Client);
        //add connected client
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        connectedClient.setEmail(data.crmTbUserObject.email);
        connectedClient.setPhoneNumber(data.crmTbUserObject.phoneNum);
        connectedClient.setBrand(Brand.VANTAGE);
        data.connectedUsers = new ArrayList<>();
        data.connectedClientHelpers = new ArrayList<>();
        data.connectedUsers.add(generateUserByClient(connectedClient));
        data.connectedClientHelpers.add(connectedClient);
        //add frauds for connected client
        data.clientFraudTypes = new ArrayList<>();
        data.clientFraudTypes.add(createClientFraudTypeCh(connectedClient.getUcid(), FraudTypeOld.GAP_TRADING.getKey()));
        //add connection with connected client
        setupAttrConnection075(data);
        return data;
    }

    public static RuleDataHelper getRegistrationRuleExitEventEnd3p14Data() {
        RuleDataHelper data = getRegistrationRuleData(registrationRuleExitEventEnd3p14Client);
        data.crmTbUserObject = generateUserByClient(registrationRuleExitEventEnd3p14Client);
        //add connected client
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        connectedClient.setEmail(data.crmTbUserObject.email);
        connectedClient.setPhoneNumber(data.crmTbUserObject.phoneNum);
        connectedClient.setBrand(Brand.VANTAGE);
        data.connectedUsers = new ArrayList<>();
        data.connectedClientHelpers = new ArrayList<>();
        data.connectedUsers.add(generateUserByClient(connectedClient));
        data.connectedClientHelpers.add(connectedClient);
        //add frauds for connected client
        data.clientFraudTypes = new ArrayList<>();
        data.clientFraudTypes.add(createClientFraudTypeCh(connectedClient.getUcid(), FraudTypeOld.GAP_TRADING.getKey()));
        //add connection with connected client
        setupAttrConnection06(data);
        return data;
    }

    public static RuleDataHelper getRegistrationRuleExitEventEnd3p15Data() {
        RuleDataHelper data = getRegistrationRuleData(registrationRuleExitEventEnd3p15Client);
        data.crmTbUserObject = generateUserByClient(registrationRuleExitEventEnd3p15Client);
        //add connected client
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        connectedClient.setEmail(data.crmTbUserObject.email);
        connectedClient.setPhoneNumber(data.crmTbUserObject.phoneNum);
        connectedClient.setBrand(Brand.VANTAGE);
        data.connectedUsers = new ArrayList<>();
        data.connectedClientHelpers = new ArrayList<>();
        data.connectedUsers.add(generateUserByClient(connectedClient));
        data.connectedClientHelpers.add(connectedClient);
        //add frauds for connected client
        data.clientFraudTypes = new ArrayList<>();
        data.clientFraudTypes.add(createClientFraudTypeCh(connectedClient.getUcid(), FraudTypeOld.LATENCY_ARBITRAGE.getKey()));
        //add connection with connected client
        setupAttrConnection075(data);
        return data;
    }

    public static RuleDataHelper getRegistrationRuleExitEventEnd3p16Data() {
        RuleDataHelper data = getRegistrationRuleData(registrationRuleExitEventEnd3p16Client);
        data.crmTbUserObject = generateUserByClient(registrationRuleExitEventEnd3p16Client);
        //add connected client
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        connectedClient.setEmail(data.crmTbUserObject.email);
        connectedClient.setPhoneNumber(data.crmTbUserObject.phoneNum);
        connectedClient.setBrand(Brand.VANTAGE);
        data.connectedUsers = new ArrayList<>();
        data.connectedClientHelpers = new ArrayList<>();
        data.connectedUsers.add(generateUserByClient(connectedClient));
        data.connectedClientHelpers.add(connectedClient);
        //add frauds for connected client
        data.clientFraudTypes = new ArrayList<>();
        data.clientFraudTypes.add(createClientFraudTypeCh(connectedClient.getUcid(), FraudTypeOld.LATENCY_ARBITRAGE.getKey()));
        //add connection with connected client
        setupAttrConnection06(data);
        return data;
    }

    public static RuleDataHelper getRegistrationRuleExitEventEnd3p17Data() {
        RuleDataHelper data = getRegistrationRuleData(registrationRuleExitEventEnd3p17Client);
        data.crmTbUserObject = generateUserByClient(registrationRuleExitEventEnd3p17Client);
        //add connected client
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        connectedClient.setEmail(data.crmTbUserObject.email);
        connectedClient.setPhoneNumber(data.crmTbUserObject.phoneNum);
        connectedClient.setBrand(Brand.VANTAGE);
        data.connectedUsers = new ArrayList<>();
        data.connectedClientHelpers = new ArrayList<>();
        data.connectedUsers.add(generateUserByClient(connectedClient));
        data.connectedClientHelpers.add(connectedClient);
        //add frauds for connected client
        data.clientFraudTypes = new ArrayList<>();
        data.clientFraudTypes.add(createClientFraudTypeCh(connectedClient.getUcid(), FraudTypeOld.PRICING_ERROR.getKey()));
        //add connection with connected client
        setupAttrConnection075(data);
        return data;
    }

    public static RuleDataHelper getRegistrationRuleExitEventEnd3p18Data() {
        RuleDataHelper data = getRegistrationRuleData(registrationRuleExitEventEnd3p18Client);
        data.crmTbUserObject = generateUserByClient(registrationRuleExitEventEnd3p18Client);
        //add connected client
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        connectedClient.setEmail(data.crmTbUserObject.email);
        connectedClient.setPhoneNumber(data.crmTbUserObject.phoneNum);
        connectedClient.setBrand(Brand.VANTAGE);
        data.connectedUsers = new ArrayList<>();
        data.connectedClientHelpers = new ArrayList<>();
        data.connectedUsers.add(generateUserByClient(connectedClient));
        data.connectedClientHelpers.add(connectedClient);
        //add frauds for connected client
        data.clientFraudTypes = new ArrayList<>();
        data.clientFraudTypes.add(createClientFraudTypeCh(connectedClient.getUcid(), FraudTypeOld.PRICING_ERROR.getKey()));
        //add connection with connected client
        setupAttrConnection06(data);
        return data;
    }

    public static RuleDataHelper getRegistrationRuleExitEventEnd3p19Data() {
        RuleDataHelper data = getRegistrationRuleData(registrationRuleExitEventEnd3p19Client);
        data.crmTbUserObject = generateUserByClient(registrationRuleExitEventEnd3p19Client);
        //add connected client
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        connectedClient.setEmail(data.crmTbUserObject.email);
        connectedClient.setPhoneNumber(data.crmTbUserObject.phoneNum);
        connectedClient.setBrand(Brand.VANTAGE);
        data.connectedUsers = new ArrayList<>();
        data.connectedClientHelpers = new ArrayList<>();
        data.connectedUsers.add(generateUserByClient(connectedClient));
        data.connectedClientHelpers.add(connectedClient);
        //add frauds for connected client
        data.clientFraudTypes = new ArrayList<>();
        data.clientFraudTypes.add(createClientFraudTypeCh(connectedClient.getUcid(), FraudTypeOld.NBP_ABUSE.getKey()));
        //add connection with connected client
        setupAttrConnection075(data);
        return data;
    }

    public static RuleDataHelper getRegistrationRuleExitEventEnd3p20Data() {
        RuleDataHelper data = getRegistrationRuleData(registrationRuleExitEventEnd3p20Client);
        data.crmTbUserObject = generateUserByClient(registrationRuleExitEventEnd3p20Client);
        //add connected client
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        connectedClient.setEmail(data.crmTbUserObject.email);
        connectedClient.setPhoneNumber(data.crmTbUserObject.phoneNum);
        connectedClient.setBrand(Brand.VANTAGE);
        data.connectedUsers = new ArrayList<>();
        data.connectedClientHelpers = new ArrayList<>();
        data.connectedUsers.add(generateUserByClient(connectedClient));
        data.connectedClientHelpers.add(connectedClient);
        //add frauds for connected client
        data.clientFraudTypes = new ArrayList<>();
        data.clientFraudTypes.add(createClientFraudTypeCh(connectedClient.getUcid(), FraudTypeOld.NBP_ABUSE.getKey()));
        //add connection with connected client
        setupAttrConnection06(data);
        return data;
    }

    public static RuleDataHelper getRegistrationRuleExitEventEnd3p21Data() {
        RuleDataHelper data = getRegistrationRuleData(registrationRuleExitEventEnd3p21Client);
        data.crmTbUserObject = generateUserByClient(registrationRuleExitEventEnd3p21Client);
        //add connected client
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        connectedClient.setEmail(data.crmTbUserObject.email);
        connectedClient.setPhoneNumber(data.crmTbUserObject.phoneNum);
        connectedClient.setBrand(Brand.VANTAGE);
        data.connectedUsers = new ArrayList<>();
        data.connectedClientHelpers = new ArrayList<>();
        data.connectedUsers.add(generateUserByClient(connectedClient));
        data.connectedClientHelpers.add(connectedClient);
        //add frauds for connected client
        data.clientFraudTypes = new ArrayList<>();
        data.clientFraudTypes.add(createClientFraudTypeCh(connectedClient.getUcid(), FraudTypeOld.HFT_ABUSE.getKey()));
        //add connection with connected client
        setupAttrConnection075(data);
        return data;
    }

    public static RuleDataHelper getRegistrationRuleExitEventEnd3p22Data() {
        RuleDataHelper data = getRegistrationRuleData(registrationRuleExitEventEnd3p22Client);
        data.crmTbUserObject = generateUserByClient(registrationRuleExitEventEnd3p22Client);
        //add connected client
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        connectedClient.setEmail(data.crmTbUserObject.email);
        connectedClient.setPhoneNumber(data.crmTbUserObject.phoneNum);
        connectedClient.setBrand(Brand.VANTAGE);
        data.connectedUsers = new ArrayList<>();
        data.connectedClientHelpers = new ArrayList<>();
        data.connectedUsers.add(generateUserByClient(connectedClient));
        data.connectedClientHelpers.add(connectedClient);
        //add frauds for connected client
        data.clientFraudTypes = new ArrayList<>();
        data.clientFraudTypes.add(createClientFraudTypeCh(connectedClient.getUcid(), FraudTypeOld.HFT_ABUSE.getKey()));
        //add connection with connected client
        setupAttrConnection06(data);
        return data;
    }

    public static RuleDataHelper getRegistrationRuleExitEventEnd3p23Data() {
        RuleDataHelper data = getRegistrationRuleData(registrationRuleExitEventEnd3p23Client);
        data.crmTbUserObject = generateUserByClient(registrationRuleExitEventEnd3p23Client);
        //add connected client
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        connectedClient.setEmail(data.crmTbUserObject.email);
        connectedClient.setPhoneNumber(data.crmTbUserObject.phoneNum);
        connectedClient.setBrand(Brand.VANTAGE);
        data.connectedUsers = new ArrayList<>();
        data.connectedClientHelpers = new ArrayList<>();
        data.connectedUsers.add(generateUserByClient(connectedClient));
        data.connectedClientHelpers.add(connectedClient);
        //add frauds for connected client
        data.clientFraudTypes = new ArrayList<>();
        data.clientFraudTypes.add(createClientFraudTypeCh(connectedClient.getUcid(), FraudTypeOld.LOOPHOLE_ABUSE.getKey()));
        //add connection with connected client
        setupAttrConnection075(data);
        return data;
    }

    public static RuleDataHelper getRegistrationRuleExitEventEnd3p24Data() {
        RuleDataHelper data = getRegistrationRuleData(registrationRuleExitEventEnd3p24Client);
        data.crmTbUserObject = generateUserByClient(registrationRuleExitEventEnd3p24Client);
        //add connected client
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        connectedClient.setEmail(data.crmTbUserObject.email);
        connectedClient.setPhoneNumber(data.crmTbUserObject.phoneNum);
        connectedClient.setBrand(Brand.VANTAGE);
        data.connectedUsers = new ArrayList<>();
        data.connectedClientHelpers = new ArrayList<>();
        data.connectedUsers.add(generateUserByClient(connectedClient));
        data.connectedClientHelpers.add(connectedClient);
        //add frauds for connected client
        data.clientFraudTypes = new ArrayList<>();
        data.clientFraudTypes.add(createClientFraudTypeCh(connectedClient.getUcid(), FraudTypeOld.LOOPHOLE_ABUSE.getKey()));
        //add connection with connected client
        setupAttrConnection06(data);
        return data;
    }

    public static RuleDataHelper getRegistrationRuleExitEventEnd3p25Data() {
        RuleDataHelper data = getRegistrationRuleData(registrationRuleExitEventEnd3p24Client);
        data.crmTbUserObject = generateUserByClient(registrationRuleExitEventEnd3p24Client);
        //add connected client
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        connectedClient.setEmail(data.crmTbUserObject.email);
        connectedClient.setPhoneNumber(data.crmTbUserObject.phoneNum);
        connectedClient.setBrand(Brand.VANTAGE);
        data.connectedUsers = new ArrayList<>();
        data.connectedClientHelpers = new ArrayList<>();
        data.connectedUsers.add(generateUserByClient(connectedClient));
        data.connectedClientHelpers.add(connectedClient);
        //add frauds for connected client
        data.clientFraudTypes = new ArrayList<>();
        data.clientFraudTypes.add(createClientFraudTypeCh(connectedClient.getUcid(), getRandomFraudType(CPA_ABUSE, HEDGING, BONUS_ABUSE, LOSS_VOUCHER_ABUSE, NEWS_TRADER, TLS_ABUSE, SWAP_ARBITRAGE, MARKET_MANIPULATION, GAP_TRADING, LATENCY_ARBITRAGE, PRICING_ERROR, NBP_ABUSE, HFT_ABUSE, LOOPHOLE_ABUSE).getKey()));
        //add connection with connected client
        setupAttrConnection075(data);
        return data;
    }

    public static Map<String, RuleDataHelper> setupRegistrationRuleData() {
        startSshTunnel();
        Map<String, RuleDataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("1", getRegistrationRuleExitEventEnd1Data());
        map.put("2", getRegistrationRuleExitEventEnd2Data());

        setupRuleData(map);

        return map;
    }

    public static void deleteRegistrationRuleData(Map<String, RuleDataHelper> map) throws Exception {
        deleteRuleData(map);
    }

    protected static void setupAttrConnection075(RuleDataHelper data, ClientHelper connectedClient) {

        data.crmTbUserObject.email = faker.internet().emailAddress();
        data.crmTbUserObject.phoneNum = faker.phoneNumber().cellPhone();

        //add connection with connected client
        ConnectionTableEntry connection = getConnection(data.clientHelper, connectedClient);
        ConnectionTableEntry.ConnectionInfo connectionInfo1 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo1.connectionAttributeName = "email";
        connectionInfo1.connectionAttributeValue = data.crmTbUserObject.email;
        connectionInfo1.sourceAttributeValue = data.crmTbUserObject.email;
        connectionInfo1.relationType = "exact";
        ConnectionTableEntry.ConnectionInfo connectionInfo2 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo2.connectionAttributeName = "phone";
        connectionInfo2.connectionAttributeValue = data.crmTbUserObject.phoneNum;
        connectionInfo2.sourceAttributeValue = data.crmTbUserObject.phoneNum;
        connectionInfo2.relationType = "exact";
        connection.connectionInfo = connectionInfoToString(List.of(connectionInfo1, connectionInfo2));
        connection.connectionScore = 0.75;
        data.connections = new ArrayList<>();
        data.connections.add(connection);
        //add email to LN record
        data.lnSessionParsedObject.setEmail(data.crmTbUserObject.email);
        data.lnSessionParsedObject.setMobile(data.crmTbUserObject.phoneNum);

        //add to emails table records with same email for initial and connected clients
        data.emailTableEntries = new ArrayList<>();
        data.emailTableEntries.add(emailTableEntryForConnectionSearch(data.clientHelper, data.crmTbUserObject.email));
        data.emailTableEntries.add(emailTableEntryForConnectionSearch(connectedClient, data.crmTbUserObject.email));

        //add to phone table records with same email for initial and connected clients
        data.phoneTableEntries = new ArrayList<>();
        data.phoneTableEntries.add(phoneTableEntryForConnectionSearch(data.clientHelper, data.crmTbUserObject.phoneNum));
        data.phoneTableEntries.add(phoneTableEntryForConnectionSearch(connectedClient, data.crmTbUserObject.phoneNum));
    }

    protected static void setupAttrConnection06(RuleDataHelper data, ClientHelper connectedClient) {
        String ip = faker.internet().ipV4Address();
        data.crmTbUserObject.email = faker.internet().emailAddress();

        //add connection with connected client
        ConnectionTableEntry connection = getConnection(data.clientHelper, connectedClient);
        ConnectionTableEntry.ConnectionInfo connectionInfo1 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo1.connectionAttributeName = "email";
        connectionInfo1.connectionAttributeValue = data.crmTbUserObject.email;
        connectionInfo1.sourceAttributeValue = data.crmTbUserObject.email;
        connectionInfo1.relationType = "exact";
        ConnectionTableEntry.ConnectionInfo connectionInfo2 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo2.connectionAttributeName = "ip";
        connectionInfo2.connectionAttributeValue = ip;
        connectionInfo2.sourceAttributeValue = ip;
        connectionInfo2.relationType = "exact";
        connection.connectionInfo = connectionInfoToString(List.of(connectionInfo1, connectionInfo2));
        connection.connectionScore = 0.6;
        data.connections = new ArrayList<>();
        data.connections.add(connection);
        //add email and IP to LN record
        data.lnSessionParsedObject.setEmail(data.crmTbUserObject.email);
        data.lnSessionParsedObject.setTrueIp(ip);

        //add to emails table records with same email for initial and connected clients
        data.emailTableEntries = new ArrayList<>();
        data.emailTableEntries.add(emailTableEntryForConnectionSearch(data.clientHelper, data.crmTbUserObject.email));
        data.emailTableEntries.add(emailTableEntryForConnectionSearch(connectedClient, data.crmTbUserObject.email));

        //add to emails table records with same email for initial and connected clients
        data.ipTableEntries = new ArrayList<>();
        data.ipTableEntries.add(ipTableEntryForConnectionSearch(data.clientHelper, ip));
        data.ipTableEntries.add(ipTableEntryForConnectionSearch(connectedClient, ip));
    }

    protected static void setupAttrConnection06(RuleDataHelper data) {
        setupAttrConnection06(data, data.connectedClientHelpers.getFirst());
    }

    protected static void setupAttrConnection075(RuleDataHelper data) {
        setupAttrConnection075(data, data.connectedClientHelpers.getFirst());
    }
}
