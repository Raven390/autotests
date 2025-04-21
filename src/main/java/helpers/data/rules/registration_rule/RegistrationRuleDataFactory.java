package helpers.data.rules.registration_rule;


import business_objects.db.clickhouse.bo_client_fraud_types.ClientFraudTypesObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.connection_table.ConnectionTableEntry;
import business_objects.db.clickhouse.ln_session_parsed.LnSessionParsedObject;
import business_objects.kafka.crm_events.RegistrationEvent;
import helpers.data.ClientHelper;
import helpers.data.enums.Brand;
import generator.annotations.RuleTestData;
import helpers.data.enums.FraudType;
import helpers.data.enums.Regulator;
import utils.Utils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountData;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.device_id_table.DeviceIdTableEntryFactory.deviceIdTableEntryForConnectionSearch;
import static business_objects.db.clickhouse.email_table.EmailTableEntryFactory.emailTableEntryForConnectionSearch;
import static business_objects.db.clickhouse.email_table.EmailTableEntryFactory.getEmailTableEntryByClient;
import static business_objects.db.clickhouse.ln_session_parsed.LnSessionParsedObjectFactory.generateLexisNexisDataByClient;
import static business_objects.db.clickhouse.session_id.SessionIdTableEntryFactory.sessionIdTableEntryForConnectionSearch;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.DbHelper.*;
import static helpers.database.CleanTableHelper.*;
import static utils.Constants.*;
import static utils.Utils.*;

@RuleTestData("registration")
public class RegistrationRuleDataFactory {

    // Clients
    private static final ClientHelper registrationRuleExitEventEnd1Client = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleExitEventEnd2Client = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleExitEventEnd3p1Client = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleExitEventEnd3p2Client = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleExitEventEnd4p1Client = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleExitEventEnd4p2Client = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleExitEventEnd5Client = getRandomVantageClientAllFields();

    private static final ClientHelper registrationRuleExitEventEnd6Client = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleExitEventEnd7Version1Client = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleExitEventEnd7Version2Client = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleExitEventEnd7Version3Client = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleExitEventEnd7Version4Client = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleExitEventEnd7Version5Client = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleExitEventEnd7Version6Client = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleExitEventEnd7Version7Client = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleExitEventEnd7Version8Client = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleExitEventEnd7Version9Client = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleExitEventEnd7Version10Client = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleExitEventEnd7Version11Client = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleExitEventEnd7Version12Client = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleExitEventEnd7Version13Client = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleExitEventEnd7Version14Client = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleExitEventEnd7Version15Client = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleExitEventEnd7Version16Client = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleExitEventEnd7Version17Client = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleExitEventEnd7Version18Client = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleExitEventEnd7Version19Client = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleExitEventEnd7Version20Client = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleExitEventEnd7Version21Client = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleExitEventEnd7Version22Client = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleExitEventEnd7Version23Client = getRandomVantageClientAllFields();

    private static RegistrationRuleData getRegistrationRuleData(ClientHelper client) {
        CrmTbUserObject userObject = generateUserByClient(client);
        userObject.countryCode = client.getCountryCode();
        userObject.isoCountryCode = client.getCountryCode();
        LnSessionParsedObject lexisNexisObject = generateLexisNexisDataByClient(client);
        lexisNexisObject.setBrand(client.getBrand());
        lexisNexisObject.setEventType("account_creation");
        lexisNexisObject.setEmail(client.getEmail());
        lexisNexisObject.setUserId(client.getUserId());
        lexisNexisObject.setProxyIp(client.getIpAddress());
        lexisNexisObject.setTrueIpGeo(client.getCountryCode());
        lexisNexisObject.setSessionId(client.getSessionId());
        lexisNexisObject.setPolicyScore(-49);
        lexisNexisObject.setRiskRating("low");
        RegistrationEvent registrationEvent = new RegistrationEvent();
        registrationEvent.clientId = client.getUserId();
        registrationEvent.brand = client.getBrand();
        registrationEvent.regulator = Regulator.VFSC.getDisplayName();
        registrationEvent.metaTraderAccount = 1;
        registrationEvent.id = getRandomUuidString();
        registrationEvent.createTime = Instant.now().toString();
        registrationEvent.type = EG_REGISTRATION_EVENT;
        return new RegistrationRuleData(client, userObject, lexisNexisObject, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), registrationEvent, new ArrayList<>(), null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
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

    public static RegistrationRuleData getRegistrationRuleExitEventEnd1Data() {
        return getRegistrationRuleData(registrationRuleExitEventEnd1Client);
    }

    public static RegistrationRuleData getRegistrationRuleExitEventEnd2Data() {
        RegistrationRuleData registrationRuleData = getRegistrationRuleData(registrationRuleExitEventEnd2Client);
        LnSessionParsedObject lexisNexisObject = registrationRuleData.lnSessionParsedObject;
        lexisNexisObject.setPolicyScore(-50);
        lexisNexisObject.setRiskRating("high");
        return registrationRuleData;
    }

    public static RegistrationRuleData getRegistrationRuleExitEventEnd3p1Data() {
        RegistrationRuleData registrationRuleData = getRegistrationRuleData(registrationRuleExitEventEnd3p1Client);
        registrationRuleData.clientHelper.setBrand(Brand.VANTAGE);
        CrmTbUserObject crmTbUserObject = registrationRuleData.crmTbUserObject;
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        connectedClient.setSessionId(registrationRuleData.clientHelper.getSessionId());
        connectedClient.setBrand(Brand.VANTAGE);

        ConnectionAndConnectedUser connectionAndConnectedUser = getConnectionAndConnectedUser(registrationRuleExitEventEnd3p1Client, connectedClient);
        CrmTbUserObject crmTbUserToObject = connectionAndConnectedUser.crmTbUserObject;
        crmTbUserObject.rafReferrerId = crmTbUserToObject.rafReferrerId;

        registrationRuleData.connectedUsers.add(crmTbUserToObject);
        registrationRuleData.connections.add(connectionAndConnectedUser.connectionTableEntry);
        registrationRuleData.connectedClientHelpers.add(connectedClient);
        registrationRuleData.sessionIdTableEntries.add(sessionIdTableEntryForConnectionSearch(registrationRuleData.clientHelper));
        registrationRuleData.sessionIdTableEntries.add(sessionIdTableEntryForConnectionSearch(connectedClient));
        return registrationRuleData;
    }

    public static RegistrationRuleData getRegistrationRuleExitEventEnd3p2Data() {
        RegistrationRuleData registrationRuleData = getRegistrationRuleData(registrationRuleExitEventEnd3p2Client);
        registrationRuleData.clientHelper.setBrand(Brand.VANTAGE);
        CrmTbUserObject crmTbUserObject = registrationRuleData.crmTbUserObject;
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        connectedClient.setSessionId(registrationRuleData.clientHelper.getSessionId());
        connectedClient.setBrand(Brand.VANTAGE);

        ConnectionAndConnectedUser connectionAndConnectedUser = getConnectionAndConnectedUser(registrationRuleExitEventEnd3p2Client, connectedClient);
        CrmTbUserObject crmTbUserToObject = connectionAndConnectedUser.crmTbUserObject;
        crmTbUserObject.ibId = crmTbUserToObject.ibId;
        registrationRuleData.connectedUsers.add(crmTbUserToObject);
        registrationRuleData.connections.add(connectionAndConnectedUser.connectionTableEntry);
        registrationRuleData.connectedClientHelpers.add(connectedClient);
        registrationRuleData.sessionIdTableEntries.add(sessionIdTableEntryForConnectionSearch(registrationRuleData.clientHelper));
        registrationRuleData.sessionIdTableEntries.add(sessionIdTableEntryForConnectionSearch(connectedClient));
        return registrationRuleData;
    }

    public static RegistrationRuleData getRegistrationRuleExitEventEnd4p1Data() {
        RegistrationRuleData registrationRuleData = getRegistrationRuleData(registrationRuleExitEventEnd4p1Client);
        registrationRuleData.clientHelper.setBrand(Brand.VANTAGE);
        registrationRuleData.lnSessionParsedObject.setRiskRating("medium");

        ClientHelper connectedClient = getRandomVantageClientAllFields();
        connectedClient.setSessionId(registrationRuleData.clientHelper.getSessionId());
        connectedClient.setBrand(Brand.VANTAGE);

        ConnectionAndConnectedUser connectionAndConnectedUser = getConnectionAndConnectedUser(registrationRuleExitEventEnd4p1Client, connectedClient);
        CrmTbUserObject crmTbUserToObject = connectionAndConnectedUser.crmTbUserObject;

        registrationRuleData.connectedUsers.add(crmTbUserToObject);
        registrationRuleData.connections.add(connectionAndConnectedUser.connectionTableEntry);
        registrationRuleData.connectedClientHelpers.add(connectedClient);
        registrationRuleData.sessionIdTableEntries.add(sessionIdTableEntryForConnectionSearch(registrationRuleData.clientHelper));
        registrationRuleData.sessionIdTableEntries.add(sessionIdTableEntryForConnectionSearch(connectedClient));
        return registrationRuleData;
    }

    public static RegistrationRuleData getRegistrationRuleExitEventEnd4p2Data() {
        RegistrationRuleData registrationRuleData = getRegistrationRuleData(registrationRuleExitEventEnd4p2Client);
        registrationRuleData.clientHelper.setBrand(Brand.VANTAGE);
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        connectedClient.setSessionId(registrationRuleData.clientHelper.getSessionId());
        connectedClient.setBrand(Brand.VANTAGE);
        ConnectionAndConnectedUser connectionAndConnectedUser = getConnectionAndConnectedUser(registrationRuleExitEventEnd4p2Client, connectedClient);
        CrmTbUserObject crmTbUserToObject = connectionAndConnectedUser.crmTbUserObject;

        registrationRuleData.connectedUsers.add(crmTbUserToObject);
        registrationRuleData.connections.add(connectionAndConnectedUser.connectionTableEntry);
        registrationRuleData.connectedClientHelpers.add(connectedClient);
        registrationRuleData.lnSessionParsedObject.setRiskRating("high");

        return registrationRuleData;
    }

    public static RegistrationRuleData getRegistrationRuleExitEventEnd5Data() {
        RegistrationRuleData registrationRuleData = getRegistrationRuleData(registrationRuleExitEventEnd5Client);
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        connectedClient.setSessionId(registrationRuleData.clientHelper.getSessionId());
        ConnectionAndConnectedUser connectionAndConnectedUser = getConnectionAndConnectedUser(registrationRuleExitEventEnd5Client, connectedClient);
        CrmTbUserObject crmTbUserToObject = connectionAndConnectedUser.crmTbUserObject;
        registrationRuleData.lnSessionParsedObject.setPolicyScore(-19);
        registrationRuleData.connectedUsers.add(crmTbUserToObject);
        registrationRuleData.connections.add(connectionAndConnectedUser.connectionTableEntry);
        registrationRuleData.connectedClientHelpers.add(connectedClient);
        registrationRuleData.sessionIdTableEntries.add(sessionIdTableEntryForConnectionSearch(registrationRuleData.clientHelper));
        registrationRuleData.sessionIdTableEntries.add(sessionIdTableEntryForConnectionSearch(connectedClient));
        return registrationRuleData;
    }

    public static RegistrationRuleData getRegistrationRuleExitEventEnd6Data() {
        RegistrationRuleData registrationRuleData = getRegistrationRuleData(registrationRuleExitEventEnd6Client);
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        connectedClient.setSessionId(registrationRuleData.clientHelper.getSessionId());
        ConnectionAndConnectedUser connectionAndConnectedUser = getConnectionAndConnectedUser(registrationRuleExitEventEnd6Client, connectedClient);
        registrationRuleData.connectedUsers.add(connectionAndConnectedUser.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUser.connectionTableEntry);
        registrationRuleData.lnSessionParsedObject.setRiskRating("high");
        registrationRuleData.connectedClientHelpers.add(connectedClient);
        registrationRuleData.sessionIdTableEntries.add(sessionIdTableEntryForConnectionSearch(registrationRuleData.clientHelper));
        registrationRuleData.sessionIdTableEntries.add(sessionIdTableEntryForConnectionSearch(connectedClient));
        return registrationRuleData;
    }

    public static RegistrationRuleData getRegistrationRuleExitEventEnd7Version1Data() {
        RegistrationRuleData registrationRuleData = getRegistrationRuleData(registrationRuleExitEventEnd7Version1Client);

        // Abuser connected clients
        ClientHelper connectedClientCpa = getRandomVantageClientAllFields();
        ClientHelper connectedClientBonusAbuser = getRandomVantageClientAllFields();
        ClientHelper connectedClientVoucherAbuser = getRandomVantageClientAllFields();
        ClientHelper connectedClientNewsTrader = getRandomVantageClientAllFields();
        ClientHelper connectedClientTls = getRandomVantageClientAllFields();
        ClientHelper connectedClientSwapAbuse = getRandomVantageClientAllFields();
        ClientHelper connectedClientMarketManipulator = getRandomVantageClientAllFields();
        ClientHelper connectedClientUnknownAbuser = getRandomVantageClientAllFields();
        ClientHelper connectedClientGapAbuser = getRandomVantageClientAllFields();
        ClientHelper connectedClientLatencyAbuser = getRandomVantageClientAllFields();
        ClientHelper connectedClientPricingErrorAbuser = getRandomVantageClientAllFields();
        ClientHelper connectedClientNbpAbuser = getRandomVantageClientAllFields();
        ClientHelper connectedClientHftAbuser = getRandomVantageClientAllFields();
        ClientHelper connectedClientLoopholeAbuser = getRandomVantageClientAllFields();
        connectedClientCpa.setSessionId(registrationRuleData.clientHelper.getSessionId());
        connectedClientBonusAbuser.setEmail(registrationRuleData.clientHelper.getEmail());
        connectedClientVoucherAbuser.setSessionId(registrationRuleData.clientHelper.getSessionId());
        connectedClientNewsTrader.setSessionId(registrationRuleData.clientHelper.getSessionId());
        connectedClientTls.setSessionId(registrationRuleData.clientHelper.getSessionId());
        connectedClientSwapAbuse.setSessionId(registrationRuleData.clientHelper.getSessionId());
        connectedClientMarketManipulator.setSessionId(registrationRuleData.clientHelper.getSessionId());
        connectedClientUnknownAbuser.setSessionId(registrationRuleData.clientHelper.getSessionId());
        connectedClientGapAbuser.setSessionId(registrationRuleData.clientHelper.getSessionId());
        connectedClientLatencyAbuser.setSessionId(registrationRuleData.clientHelper.getSessionId());
        connectedClientPricingErrorAbuser.setSessionId(registrationRuleData.clientHelper.getSessionId());
        connectedClientNbpAbuser.setSessionId(registrationRuleData.clientHelper.getSessionId());
        connectedClientHftAbuser.setSessionId(registrationRuleData.clientHelper.getSessionId());
        connectedClientLoopholeAbuser.setSessionId(registrationRuleData.clientHelper.getSessionId());

        ConnectionAndConnectedUser connectionAndConnectedUserCpa = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version1Client, connectedClientCpa);
        ConnectionAndConnectedUser connectionAndConnectedUserBonusAbuser = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version1Client, connectedClientBonusAbuser);
        ConnectionAndConnectedUser connectionAndConnectedUserVoucherAbuser = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version1Client, connectedClientVoucherAbuser);
        ConnectionAndConnectedUser connectionAndConnectedUserNewsTrader = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version1Client, connectedClientNewsTrader);
        ConnectionAndConnectedUser connectionAndConnectedUserTls = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version1Client, connectedClientTls);
        ConnectionAndConnectedUser connectionAndConnectedUserSwapAbuse = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version1Client, connectedClientSwapAbuse);
        ConnectionAndConnectedUser connectionAndConnectedUserMarketManipulator = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version1Client, connectedClientMarketManipulator);
        ConnectionAndConnectedUser connectionAndConnectedUserUnknownFraudster = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version1Client, connectedClientUnknownAbuser);
        ConnectionAndConnectedUser connectionAndConnectedUserGap = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version1Client, connectedClientGapAbuser);
        ConnectionAndConnectedUser connectionAndConnectedUserLatency = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version1Client, connectedClientLatencyAbuser);
        ConnectionAndConnectedUser connectionAndConnectedUserPricingError = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version1Client, connectedClientPricingErrorAbuser);
        ConnectionAndConnectedUser connectionAndConnectedUserNbp = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version1Client, connectedClientNbpAbuser);
        ConnectionAndConnectedUser connectionAndConnectedUserHft = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version1Client, connectedClientHftAbuser);
        ConnectionAndConnectedUser connectionAndConnectedUserLoophole = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version1Client, connectedClientLoopholeAbuser);

        registrationRuleData.clientFraudTypes.add(new ClientFraudTypesObject(connectedClientCpa.getUcid(), FraudType.CPA_ABUSE.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat()));
        registrationRuleData.clientFraudTypes.add(new ClientFraudTypesObject(connectedClientBonusAbuser.getUcid(), FraudType.HEDGING.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat()));
        registrationRuleData.clientFraudTypes.add(new ClientFraudTypesObject(connectedClientVoucherAbuser.getUcid(), FraudType.LOSS_VOUCHER_ABUSE.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat()));
        registrationRuleData.clientFraudTypes.add(new ClientFraudTypesObject(connectedClientNewsTrader.getUcid(), FraudType.NEWS_TRADER.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat()));
        registrationRuleData.clientFraudTypes.add(new ClientFraudTypesObject(connectedClientTls.getUcid(), FraudType.TLS_ABUSE.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat()));
        registrationRuleData.clientFraudTypes.add(new ClientFraudTypesObject(connectedClientSwapAbuse.getUcid(), FraudType.SWAP_ARBITRAGE.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat()));
        registrationRuleData.clientFraudTypes.add(new ClientFraudTypesObject(connectedClientMarketManipulator.getUcid(), FraudType.MARKET_MANIPULATION.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat()));
        registrationRuleData.clientFraudTypes.add(new ClientFraudTypesObject(connectedClientUnknownAbuser.getUcid(), FRAUD_TYPE_UNKNOWN, FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat()));
        registrationRuleData.clientFraudTypes.add(new ClientFraudTypesObject(connectedClientGapAbuser.getUcid(), FraudType.GAP_TRADING.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat()));
        registrationRuleData.clientFraudTypes.add(new ClientFraudTypesObject(connectedClientLatencyAbuser.getUcid(), FraudType.LATENCY_ARBITRAGE.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat()));
        registrationRuleData.clientFraudTypes.add(new ClientFraudTypesObject(connectedClientPricingErrorAbuser.getUcid(), FraudType.PRICING_ERROR.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat()));
        registrationRuleData.clientFraudTypes.add(new ClientFraudTypesObject(connectedClientNbpAbuser.getUcid(), FraudType.NBP_ABUSE.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat()));
        registrationRuleData.clientFraudTypes.add(new ClientFraudTypesObject(connectedClientHftAbuser.getUcid(), FraudType.HFT_ABUSE.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat()));
        registrationRuleData.clientFraudTypes.add(new ClientFraudTypesObject(connectedClientLoopholeAbuser.getUcid(), FraudType.LOOPHOLE_ABUSE.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat()));

        registrationRuleData.lnSessionParsedObject.setPolicyScore(-21);
        registrationRuleData.lnSessionParsedObject.setRiskRating("high");
        registrationRuleData.connectedUsers.add(connectionAndConnectedUserCpa.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserCpa.connectionTableEntry);
        registrationRuleData.connectedClientHelpers.add(connectedClientCpa);
        registrationRuleData.connectedUsers.add(connectionAndConnectedUserBonusAbuser.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserBonusAbuser.connectionTableEntry);
        registrationRuleData.connectedClientHelpers.add(connectedClientBonusAbuser);
        registrationRuleData.connectedUsers.add(connectionAndConnectedUserVoucherAbuser.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserVoucherAbuser.connectionTableEntry);
        registrationRuleData.connectedClientHelpers.add(connectedClientVoucherAbuser);
        registrationRuleData.connectedUsers.add(connectionAndConnectedUserNewsTrader.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserNewsTrader.connectionTableEntry);
        registrationRuleData.connectedClientHelpers.add(connectedClientNewsTrader);
        registrationRuleData.connectedUsers.add(connectionAndConnectedUserTls.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserTls.connectionTableEntry);
        registrationRuleData.connectedClientHelpers.add(connectedClientTls);
        registrationRuleData.connectedUsers.add(connectionAndConnectedUserSwapAbuse.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserSwapAbuse.connectionTableEntry);
        registrationRuleData.connectedClientHelpers.add(connectedClientSwapAbuse);
        registrationRuleData.connectedUsers.add(connectionAndConnectedUserMarketManipulator.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserMarketManipulator.connectionTableEntry);
        registrationRuleData.connectedClientHelpers.add(connectedClientMarketManipulator);
        registrationRuleData.connectedUsers.add(connectionAndConnectedUserUnknownFraudster.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserUnknownFraudster.connectionTableEntry);
        registrationRuleData.connectedClientHelpers.add(connectedClientUnknownAbuser);
        registrationRuleData.connectedUsers.add(connectionAndConnectedUserGap.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserGap.connectionTableEntry);
        registrationRuleData.connectedClientHelpers.add(connectedClientGapAbuser);
        registrationRuleData.connectedUsers.add(connectionAndConnectedUserLatency.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserLatency.connectionTableEntry);
        registrationRuleData.connectedClientHelpers.add(connectedClientLatencyAbuser);
        registrationRuleData.connectedUsers.add(connectionAndConnectedUserPricingError.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserPricingError.connectionTableEntry);
        registrationRuleData.connectedClientHelpers.add(connectedClientPricingErrorAbuser);
        registrationRuleData.connectedUsers.add(connectionAndConnectedUserNbp.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserNbp.connectionTableEntry);
        registrationRuleData.connectedClientHelpers.add(connectedClientNbpAbuser);
        registrationRuleData.connectedUsers.add(connectionAndConnectedUserHft.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserHft.connectionTableEntry);
        registrationRuleData.connectedClientHelpers.add(connectedClientHftAbuser);
        registrationRuleData.connectedUsers.add(connectionAndConnectedUserLoophole.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserLoophole.connectionTableEntry);
        registrationRuleData.connectedClientHelpers.add(connectedClientLoopholeAbuser);

        registrationRuleData.sessionIdTableEntries.add(sessionIdTableEntryForConnectionSearch(registrationRuleData.clientHelper));
        registrationRuleData.sessionIdTableEntries.add(sessionIdTableEntryForConnectionSearch(connectedClientCpa));
        registrationRuleData.emailTableEntries.add(emailTableEntryForConnectionSearch(connectedClientBonusAbuser));
        registrationRuleData.sessionIdTableEntries.add(sessionIdTableEntryForConnectionSearch(connectedClientVoucherAbuser));
        registrationRuleData.sessionIdTableEntries.add(sessionIdTableEntryForConnectionSearch(connectedClientNewsTrader));
        registrationRuleData.sessionIdTableEntries.add(sessionIdTableEntryForConnectionSearch(connectedClientSwapAbuse));
        registrationRuleData.sessionIdTableEntries.add(sessionIdTableEntryForConnectionSearch(connectedClientMarketManipulator));
        registrationRuleData.sessionIdTableEntries.add(sessionIdTableEntryForConnectionSearch(connectedClientUnknownAbuser));
        registrationRuleData.sessionIdTableEntries.add(sessionIdTableEntryForConnectionSearch(connectedClientGapAbuser));
        registrationRuleData.sessionIdTableEntries.add(sessionIdTableEntryForConnectionSearch(connectedClientLatencyAbuser));
        registrationRuleData.sessionIdTableEntries.add(sessionIdTableEntryForConnectionSearch(connectedClientPricingErrorAbuser));
        registrationRuleData.sessionIdTableEntries.add(sessionIdTableEntryForConnectionSearch(connectedClientNbpAbuser));
        registrationRuleData.sessionIdTableEntries.add(sessionIdTableEntryForConnectionSearch(connectedClientHftAbuser));
        registrationRuleData.sessionIdTableEntries.add(sessionIdTableEntryForConnectionSearch(connectedClientLoopholeAbuser));
        return registrationRuleData;
    }

    public static RegistrationRuleData getRegistrationRuleExitEventEnd7Version2Data() {
        RegistrationRuleData registrationRuleData = getRegistrationRuleData(registrationRuleExitEventEnd7Version2Client);

        // Abuser connected clients
        ClientHelper connectedClientCpa = getRandomVantageClientAllFields();
        connectedClientCpa.setSessionId(registrationRuleData.clientHelper.getSessionId());
        ConnectionAndConnectedUser connectionAndConnectedUserCpa = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version2Client, connectedClientCpa);

        registrationRuleData.clientFraudTypes.add(new ClientFraudTypesObject(connectedClientCpa.getUcid(), FraudType.CPA_ABUSE.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat()));

        registrationRuleData.lnSessionParsedObject.setPolicyScore(-19);
        registrationRuleData.connectedUsers.add(connectionAndConnectedUserCpa.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserCpa.connectionTableEntry);
        registrationRuleData.connectedClientHelpers.add(connectedClientCpa);
        registrationRuleData.sessionIdTableEntries.add(sessionIdTableEntryForConnectionSearch(registrationRuleData.clientHelper));
        registrationRuleData.sessionIdTableEntries.add(sessionIdTableEntryForConnectionSearch(connectedClientCpa));
        return registrationRuleData;
    }

    public static RegistrationRuleData getRegistrationRuleExitEventEnd7Version4Data() {
        registrationRuleExitEventEnd7Version4Client.setBrand(Brand.STAR_TRADER);
        RegistrationRuleData registrationRuleData = getRegistrationRuleData(registrationRuleExitEventEnd7Version4Client);

        // Abuser connected clients
        ClientHelper connectedClientBonusAbuser = getRandomVantageClientAllFields();
        connectedClientBonusAbuser.setEmail(registrationRuleData.clientHelper.getEmail());
        connectedClientBonusAbuser.setBrand(Brand.STAR_TRADER);

        ConnectionAndConnectedUser connectionAndConnectedUserBonusAbuser = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version4Client, connectedClientBonusAbuser);
        connectionAndConnectedUserBonusAbuser.connectionTableEntry.connectionScore = 1d;

        registrationRuleData.clientFraudTypes.add(new ClientFraudTypesObject(connectedClientBonusAbuser.getUcid(), FraudType.HEDGING.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat()));
        registrationRuleData.connectedUsers.add(connectionAndConnectedUserBonusAbuser.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserBonusAbuser.connectionTableEntry);
        registrationRuleData.connectedClientHelpers.add(connectedClientBonusAbuser);

        registrationRuleData.emailTableEntries.add(emailTableEntryForConnectionSearch(registrationRuleData.clientHelper, registrationRuleData.clientHelper.getEmail()));

        String deviceId = Utils.getRandomUuidString();
        registrationRuleData.lnSessionParsedObject.setDeviceId(deviceId);
        registrationRuleData.deviceIdTableEntries.add(deviceIdTableEntryForConnectionSearch(registrationRuleData.clientHelper, deviceId));
        return registrationRuleData;

    }

    public static RegistrationRuleData getRegistrationRuleExitEventEnd7Version5Data() {
        registrationRuleExitEventEnd7Version5Client.setBrand(Brand.STAR_TRADER);
        RegistrationRuleData registrationRuleData = getRegistrationRuleData(registrationRuleExitEventEnd7Version5Client);

        // Abuser connected clients
        ClientHelper connectedClientBonusAbuser = getRandomVantageClientAllFields();
        connectedClientBonusAbuser.setEmail(registrationRuleData.clientHelper.getEmail());
        connectedClientBonusAbuser.setBrand(Brand.STAR_TRADER);

        ConnectionAndConnectedUser connectionAndConnectedUserBonusAbuser = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version5Client, connectedClientBonusAbuser);
        connectionAndConnectedUserBonusAbuser.connectionTableEntry.connectionScore = 1d;

        registrationRuleData.clientFraudTypes.add(new ClientFraudTypesObject(connectedClientBonusAbuser.getUcid(), FraudType.HEDGING.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat()));
        registrationRuleData.clientFraudTypes.add(new ClientFraudTypesObject(connectedClientBonusAbuser.getUcid(), FraudType.BONUS_ABUSE.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat()));
        registrationRuleData.connectedUsers.add(connectionAndConnectedUserBonusAbuser.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserBonusAbuser.connectionTableEntry);
        registrationRuleData.connectedClientHelpers.add(connectedClientBonusAbuser);

        registrationRuleData.emailTableEntries.add(emailTableEntryForConnectionSearch(registrationRuleData.clientHelper, registrationRuleData.clientHelper.getEmail()));

        String deviceId = Utils.getRandomUuidString();
        registrationRuleData.lnSessionParsedObject.setDeviceId(deviceId);
        registrationRuleData.deviceIdTableEntries.add(deviceIdTableEntryForConnectionSearch(registrationRuleData.clientHelper, deviceId));
        return registrationRuleData;
    }

    public static RegistrationRuleData getRegistrationRuleExitEventEnd7Version7Data() {
        RegistrationRuleData registrationRuleData = getRegistrationRuleData(registrationRuleExitEventEnd7Version7Client);

        // Abuser connected clients
        ClientHelper connectedClientVoucherAbuser = getRandomVantageClientAllFields();
        connectedClientVoucherAbuser.setSessionId(registrationRuleData.clientHelper.getSessionId());
        ConnectionAndConnectedUser connectionAndConnectedUserVoucherAbuser = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version7Client, connectedClientVoucherAbuser);

        registrationRuleData.clientFraudTypes.add(new ClientFraudTypesObject(connectedClientVoucherAbuser.getUcid(), FraudType.LOSS_VOUCHER_ABUSE.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat()));

        registrationRuleData.lnSessionParsedObject.setPolicyScore(-19);
        registrationRuleData.connectedUsers.add(connectionAndConnectedUserVoucherAbuser.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserVoucherAbuser.connectionTableEntry);
        registrationRuleData.connectedClientHelpers.add(connectedClientVoucherAbuser);
        registrationRuleData.sessionIdTableEntries.add(sessionIdTableEntryForConnectionSearch(registrationRuleData.clientHelper));
        registrationRuleData.sessionIdTableEntries.add(sessionIdTableEntryForConnectionSearch(connectedClientVoucherAbuser));
        return registrationRuleData;
    }

    public static RegistrationRuleData getRegistrationRuleExitEventEnd7Version9Data() {
        registrationRuleExitEventEnd7Version9Client.setBrand(Brand.VJP);
        RegistrationRuleData registrationRuleData = getRegistrationRuleData(registrationRuleExitEventEnd7Version9Client);

        // Abuser connected clients
        ClientHelper connectedClientNewsTrader = getRandomVantageClientAllFields();
        connectedClientNewsTrader.setSessionId(registrationRuleData.clientHelper.getSessionId());
        connectedClientNewsTrader.setBrand(Brand.VJP);
        ConnectionAndConnectedUser connectionAndConnectedUserNewsTrader = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version9Client, connectedClientNewsTrader);

        registrationRuleData.clientFraudTypes.add(new ClientFraudTypesObject(connectedClientNewsTrader.getUcid(), FraudType.NEWS_TRADER.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat()));


        registrationRuleData.connectedUsers.add(connectionAndConnectedUserNewsTrader.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserNewsTrader.connectionTableEntry);
        registrationRuleData.connectedClientHelpers.add(connectedClientNewsTrader);
        registrationRuleData.sessionIdTableEntries.add(sessionIdTableEntryForConnectionSearch(registrationRuleData.clientHelper));
        registrationRuleData.sessionIdTableEntries.add(sessionIdTableEntryForConnectionSearch(connectedClientNewsTrader));
        return registrationRuleData;
    }

    public static RegistrationRuleData getRegistrationRuleExitEventEnd7Version10Data() {
        RegistrationRuleData registrationRuleData = getRegistrationRuleData(registrationRuleExitEventEnd7Version10Client);

        // Abuser connected clients
        ClientHelper connectedClientNewsTrader = getRandomVantageClientAllFields();
        connectedClientNewsTrader.setSessionId(registrationRuleData.clientHelper.getSessionId());
        ConnectionAndConnectedUser connectionAndConnectedUserNewsTrader = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version10Client, connectedClientNewsTrader);

        registrationRuleData.clientFraudTypes.add(new ClientFraudTypesObject(connectedClientNewsTrader.getUcid(), FraudType.NEWS_TRADER.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat()));


        registrationRuleData.connectedUsers.add(connectionAndConnectedUserNewsTrader.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserNewsTrader.connectionTableEntry);
        registrationRuleData.connectedClientHelpers.add(connectedClientNewsTrader);
        registrationRuleData.sessionIdTableEntries.add(sessionIdTableEntryForConnectionSearch(registrationRuleData.clientHelper));
        registrationRuleData.sessionIdTableEntries.add(sessionIdTableEntryForConnectionSearch(connectedClientNewsTrader));
        return registrationRuleData;
    }

    public static RegistrationRuleData getRegistrationRuleExitEventEnd7Version11Data() {
        RegistrationRuleData registrationRuleData = getRegistrationRuleData(registrationRuleExitEventEnd7Version11Client);

        // Abuser connected clients
        ClientHelper connectedClientTls = getRandomVantageClientAllFields();
        connectedClientTls.setSessionId(registrationRuleData.clientHelper.getSessionId());
        ConnectionAndConnectedUser connectionAndConnectedUserTls = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version11Client, connectedClientTls);

        registrationRuleData.clientFraudTypes.add(new ClientFraudTypesObject(connectedClientTls.getUcid(), FraudType.TLS_ABUSE.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat()));

        registrationRuleData.connectedUsers.add(connectionAndConnectedUserTls.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserTls.connectionTableEntry);
        registrationRuleData.connectedClientHelpers.add(connectedClientTls);
        registrationRuleData.sessionIdTableEntries.add(sessionIdTableEntryForConnectionSearch(registrationRuleData.clientHelper));
        registrationRuleData.sessionIdTableEntries.add(sessionIdTableEntryForConnectionSearch(connectedClientTls));
        return registrationRuleData;
    }

    public static RegistrationRuleData getRegistrationRuleExitEventEnd7Version12Data() {
        RegistrationRuleData registrationRuleData = getRegistrationRuleData(registrationRuleExitEventEnd7Version12Client);

        // Abuser connected clients
        ClientHelper connectedClientSwapAbuse = getRandomVantageClientAllFields();
        connectedClientSwapAbuse.setSessionId(registrationRuleData.clientHelper.getSessionId());
        ConnectionAndConnectedUser connectionAndConnectedUserSwapAbuse = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version12Client, connectedClientSwapAbuse);

        registrationRuleData.lnSessionParsedObject.setRiskRating("high");

        registrationRuleData.clientFraudTypes.add(new ClientFraudTypesObject(connectedClientSwapAbuse.getUcid(), FraudType.SWAP_ARBITRAGE.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat()));

        registrationRuleData.connectedUsers.add(connectionAndConnectedUserSwapAbuse.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserSwapAbuse.connectionTableEntry);
        registrationRuleData.connectedClientHelpers.add(connectedClientSwapAbuse);
        registrationRuleData.sessionIdTableEntries.add(sessionIdTableEntryForConnectionSearch(registrationRuleData.clientHelper));
        registrationRuleData.sessionIdTableEntries.add(sessionIdTableEntryForConnectionSearch(connectedClientSwapAbuse));
        return registrationRuleData;
    }

    public static RegistrationRuleData getRegistrationRuleExitEventEnd7Version13Data() {
        RegistrationRuleData registrationRuleData = getRegistrationRuleData(registrationRuleExitEventEnd7Version13Client);

        // Abuser connected clients
        ClientHelper connectedClientMarketManipulator = getRandomVantageClientAllFields();
        connectedClientMarketManipulator.setSessionId(registrationRuleData.clientHelper.getSessionId());
        ConnectionAndConnectedUser connectionAndConnectedUserMarketManipulator = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version13Client, connectedClientMarketManipulator);

        registrationRuleData.clientFraudTypes.add(new ClientFraudTypesObject(connectedClientMarketManipulator.getUcid(), FraudType.MARKET_MANIPULATION.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat()));

        registrationRuleData.connectedUsers.add(connectionAndConnectedUserMarketManipulator.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserMarketManipulator.connectionTableEntry);
        registrationRuleData.connectedClientHelpers.add(connectedClientMarketManipulator);

        registrationRuleData.crmTbAccountObject = generateCrmTbAccountData(registrationRuleData.clientHelper);
        registrationRuleData.sessionIdTableEntries.add(sessionIdTableEntryForConnectionSearch(registrationRuleData.clientHelper));
        registrationRuleData.sessionIdTableEntries.add(sessionIdTableEntryForConnectionSearch(connectedClientMarketManipulator));
        return registrationRuleData;
    }

    public static RegistrationRuleData getRegistrationRuleExitEventEnd7Version14Data() {
        RegistrationRuleData registrationRuleData = getRegistrationRuleData(registrationRuleExitEventEnd7Version14Client);

        // Abuser connected clients
        ClientHelper connectedClientUnknownAbuser = getRandomVantageClientAllFields();
        connectedClientUnknownAbuser.setSessionId(registrationRuleData.clientHelper.getSessionId());
        ConnectionAndConnectedUser connectionAndConnectedUserUnknownAbuser = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version14Client, connectedClientUnknownAbuser);

        registrationRuleData.clientFraudTypes.add(new ClientFraudTypesObject(connectedClientUnknownAbuser.getUcid(), FRAUD_TYPE_UNKNOWN, FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat()));
        registrationRuleData.clientFraudTypes.add(new ClientFraudTypesObject(connectedClientUnknownAbuser.getUcid(), FRAUD_TYPE_MOREUNKNOWN, FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat()));


        registrationRuleData.connectedUsers.add(connectionAndConnectedUserUnknownAbuser.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserUnknownAbuser.connectionTableEntry);
        registrationRuleData.connectedClientHelpers.add(connectedClientUnknownAbuser);
        registrationRuleData.sessionIdTableEntries.add(sessionIdTableEntryForConnectionSearch(registrationRuleData.clientHelper));
        registrationRuleData.sessionIdTableEntries.add(sessionIdTableEntryForConnectionSearch(connectedClientUnknownAbuser));
        return registrationRuleData;
    }

    public static RegistrationRuleData getRegistrationRuleExitEventEnd7Version15Data() {
        RegistrationRuleData registrationRuleData = getRegistrationRuleData(registrationRuleExitEventEnd7Version15Client);

        // Abuser connected clients
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        connectedClient.setSessionId(registrationRuleData.clientHelper.getSessionId());
        ConnectionAndConnectedUser connectionAndConnectedUser = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version15Client, connectedClient);

        registrationRuleData.clientFraudTypes.add(new ClientFraudTypesObject(connectedClient.getUcid(), FraudType.GAP_TRADING.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat()));

        registrationRuleData.connectedUsers.add(connectionAndConnectedUser.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUser.connectionTableEntry);
        registrationRuleData.connectedClientHelpers.add(connectedClient);
        registrationRuleData.sessionIdTableEntries.add(sessionIdTableEntryForConnectionSearch(registrationRuleData.clientHelper));
        registrationRuleData.sessionIdTableEntries.add(sessionIdTableEntryForConnectionSearch(connectedClient));
        return registrationRuleData;
    }

    public static RegistrationRuleData getRegistrationRuleExitEventEnd7Version16Data() {
        RegistrationRuleData registrationRuleData = getRegistrationRuleData(registrationRuleExitEventEnd7Version16Client);

        // Abuser connected clients
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        connectedClient.setSessionId(registrationRuleData.clientHelper.getSessionId());
        ConnectionAndConnectedUser connectionAndConnectedUser = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version16Client, connectedClient);

        registrationRuleData.clientFraudTypes.add(new ClientFraudTypesObject(connectedClient.getUcid(), FraudType.LATENCY_ARBITRAGE.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat()));

        registrationRuleData.connectedUsers.add(connectionAndConnectedUser.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUser.connectionTableEntry);
        registrationRuleData.connectedClientHelpers.add(connectedClient);
        registrationRuleData.sessionIdTableEntries.add(sessionIdTableEntryForConnectionSearch(registrationRuleData.clientHelper));
        registrationRuleData.sessionIdTableEntries.add(sessionIdTableEntryForConnectionSearch(connectedClient));
        return registrationRuleData;
    }

    public static RegistrationRuleData getRegistrationRuleExitEventEnd7Version17Data() {
        RegistrationRuleData registrationRuleData = getRegistrationRuleData(registrationRuleExitEventEnd7Version17Client);

        // Abuser connected clients
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        connectedClient.setSessionId(registrationRuleData.clientHelper.getSessionId());
        ConnectionAndConnectedUser connectionAndConnectedUser = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version17Client, connectedClient);

        registrationRuleData.clientFraudTypes.add(new ClientFraudTypesObject(connectedClient.getUcid(), FraudType.PRICING_ERROR.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat()));

        registrationRuleData.connectedUsers.add(connectionAndConnectedUser.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUser.connectionTableEntry);
        registrationRuleData.connectedClientHelpers.add(connectedClient);
        registrationRuleData.sessionIdTableEntries.add(sessionIdTableEntryForConnectionSearch(registrationRuleData.clientHelper));
        registrationRuleData.sessionIdTableEntries.add(sessionIdTableEntryForConnectionSearch(connectedClient));
        return registrationRuleData;
    }

    public static RegistrationRuleData getRegistrationRuleExitEventEnd7Version18Data() {
        RegistrationRuleData registrationRuleData = getRegistrationRuleData(registrationRuleExitEventEnd7Version18Client);

        // Abuser connected clients
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        ConnectionAndConnectedUser connectionAndConnectedUser = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version18Client, connectedClient);

        registrationRuleData.clientFraudTypes.add(new ClientFraudTypesObject(connectedClient.getUcid(), FraudType.NBP_ABUSE.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat()));

        registrationRuleData.connectedUsers.add(connectionAndConnectedUser.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUser.connectionTableEntry);
        registrationRuleData.connectedClientHelpers.add(connectedClient);
        registrationRuleData.sessionIdTableEntries.add(sessionIdTableEntryForConnectionSearch(registrationRuleData.clientHelper));
        registrationRuleData.sessionIdTableEntries.add(sessionIdTableEntryForConnectionSearch(connectedClient));
        return registrationRuleData;
    }

    public static RegistrationRuleData getRegistrationRuleExitEventEnd7Version19Data() {
        RegistrationRuleData registrationRuleData = getRegistrationRuleData(registrationRuleExitEventEnd7Version19Client);

        // Abuser connected clients
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        connectedClient.setSessionId(registrationRuleData.clientHelper.getSessionId());
        ConnectionAndConnectedUser connectionAndConnectedUser = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version19Client, connectedClient);

        registrationRuleData.clientFraudTypes.add(new ClientFraudTypesObject(connectedClient.getUcid(), FraudType.HFT_ABUSE.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat()));

        registrationRuleData.connectedUsers.add(connectionAndConnectedUser.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUser.connectionTableEntry);
        registrationRuleData.connectedClientHelpers.add(connectedClient);
        registrationRuleData.sessionIdTableEntries.add(sessionIdTableEntryForConnectionSearch(registrationRuleData.clientHelper));
        registrationRuleData.sessionIdTableEntries.add(sessionIdTableEntryForConnectionSearch(connectedClient));
        return registrationRuleData;
    }

    public static RegistrationRuleData getRegistrationRuleExitEventEnd7Version20Data() {
        RegistrationRuleData registrationRuleData = getRegistrationRuleData(registrationRuleExitEventEnd7Version20Client);

        // Abuser connected clients
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        connectedClient.setSessionId(registrationRuleData.clientHelper.getSessionId());
        ConnectionAndConnectedUser connectionAndConnectedUserTls = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version20Client, connectedClient);

        registrationRuleData.clientFraudTypes.add(new ClientFraudTypesObject(connectedClient.getUcid(), FraudType.LOOPHOLE_ABUSE.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat()));

        registrationRuleData.connectedUsers.add(connectionAndConnectedUserTls.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserTls.connectionTableEntry);
        registrationRuleData.connectedClientHelpers.add(connectedClient);
        registrationRuleData.sessionIdTableEntries.add(sessionIdTableEntryForConnectionSearch(registrationRuleData.clientHelper));
        registrationRuleData.sessionIdTableEntries.add(sessionIdTableEntryForConnectionSearch(connectedClient));
        return registrationRuleData;
    }

    public static RegistrationRuleData getRegistrationRuleExitEventEnd7Version23Data() {
        RegistrationRuleData registrationRuleData = getRegistrationRuleData(registrationRuleExitEventEnd7Version23Client);

        // Abuser connected clients
        ClientHelper connectedClientCpa = getRandomVantageClientAllFields();
        ClientHelper connectedClientBonusAbuser = getRandomVantageClientAllFields();
        ClientHelper connectedClientVoucherAbuser = getRandomVantageClientAllFields();
        ClientHelper connectedClientNewsTrader = getRandomVantageClientAllFields();
        ClientHelper connectedClientTls = getRandomVantageClientAllFields();
        ClientHelper connectedClientSwapAbuse = getRandomVantageClientAllFields();
        ClientHelper connectedClientMarketManipulator = getRandomVantageClientAllFields();
        ClientHelper connectedClientUnknownAbuser = getRandomVantageClientAllFields();
        ClientHelper connectedClientGapAbuser = getRandomVantageClientAllFields();
        ClientHelper connectedClientLatencyAbuser = getRandomVantageClientAllFields();
        ClientHelper connectedClientPricingErrorAbuser = getRandomVantageClientAllFields();
        ClientHelper connectedClientNbpAbuser = getRandomVantageClientAllFields();
        ClientHelper connectedClientHftAbuser = getRandomVantageClientAllFields();
        ClientHelper connectedClientLoopholeAbuser = getRandomVantageClientAllFields();
        connectedClientCpa.setEmail(registrationRuleData.clientHelper.getEmail());
        connectedClientBonusAbuser.setSessionId(registrationRuleData.clientHelper.getSessionId());
        connectedClientVoucherAbuser.setEmail(registrationRuleData.clientHelper.getEmail());
        connectedClientNewsTrader.setEmail(registrationRuleData.clientHelper.getEmail());
        connectedClientTls.setEmail(registrationRuleData.clientHelper.getEmail());
        connectedClientSwapAbuse.setEmail(registrationRuleData.clientHelper.getEmail());
        connectedClientMarketManipulator.setEmail(registrationRuleData.clientHelper.getEmail());
        connectedClientUnknownAbuser.setEmail(registrationRuleData.clientHelper.getEmail());
        connectedClientGapAbuser.setEmail(registrationRuleData.clientHelper.getEmail());
        connectedClientLatencyAbuser.setEmail(registrationRuleData.clientHelper.getEmail());
        connectedClientPricingErrorAbuser.setEmail(registrationRuleData.clientHelper.getEmail());
        connectedClientNbpAbuser.setEmail(registrationRuleData.clientHelper.getEmail());
        connectedClientHftAbuser.setEmail(registrationRuleData.clientHelper.getEmail());
        connectedClientLoopholeAbuser.setEmail(registrationRuleData.clientHelper.getEmail());

        ConnectionAndConnectedUser connectionAndConnectedUserCpa = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version1Client, connectedClientCpa);
        ConnectionAndConnectedUser connectionAndConnectedUserBonusAbuser = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version1Client, connectedClientBonusAbuser);
        ConnectionAndConnectedUser connectionAndConnectedUserVoucherAbuser = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version1Client, connectedClientVoucherAbuser);
        ConnectionAndConnectedUser connectionAndConnectedUserNewsTrader = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version1Client, connectedClientNewsTrader);
        ConnectionAndConnectedUser connectionAndConnectedUserTls = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version1Client, connectedClientTls);
        ConnectionAndConnectedUser connectionAndConnectedUserSwapAbuse = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version1Client, connectedClientSwapAbuse);
        ConnectionAndConnectedUser connectionAndConnectedUserMarketManipulator = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version1Client, connectedClientMarketManipulator);
        ConnectionAndConnectedUser connectionAndConnectedUserUnknownFraudster = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version1Client, connectedClientUnknownAbuser);
        ConnectionAndConnectedUser connectionAndConnectedUserGap = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version1Client, connectedClientGapAbuser);
        ConnectionAndConnectedUser connectionAndConnectedUserLatency = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version1Client, connectedClientLatencyAbuser);
        ConnectionAndConnectedUser connectionAndConnectedUserPricingError = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version1Client, connectedClientPricingErrorAbuser);
        ConnectionAndConnectedUser connectionAndConnectedUserNbp = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version1Client, connectedClientNbpAbuser);
        ConnectionAndConnectedUser connectionAndConnectedUserHft = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version1Client, connectedClientHftAbuser);
        ConnectionAndConnectedUser connectionAndConnectedUserLoophole = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version1Client, connectedClientLoopholeAbuser);

        registrationRuleData.clientFraudTypes.add(new ClientFraudTypesObject(connectedClientCpa.getUcid(), FraudType.CPA_ABUSE.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat()));
        registrationRuleData.clientFraudTypes.add(new ClientFraudTypesObject(connectedClientBonusAbuser.getUcid(), FraudType.HEDGING.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat()));
        registrationRuleData.clientFraudTypes.add(new ClientFraudTypesObject(connectedClientVoucherAbuser.getUcid(), FraudType.LOSS_VOUCHER_ABUSE.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat()));
        registrationRuleData.clientFraudTypes.add(new ClientFraudTypesObject(connectedClientNewsTrader.getUcid(), FraudType.NEWS_TRADER.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat()));
        registrationRuleData.clientFraudTypes.add(new ClientFraudTypesObject(connectedClientTls.getUcid(), FraudType.TLS_ABUSE.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat()));
        registrationRuleData.clientFraudTypes.add(new ClientFraudTypesObject(connectedClientSwapAbuse.getUcid(), FraudType.SWAP_ARBITRAGE.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat()));
        registrationRuleData.clientFraudTypes.add(new ClientFraudTypesObject(connectedClientMarketManipulator.getUcid(), FraudType.MARKET_MANIPULATION.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat()));
        registrationRuleData.clientFraudTypes.add(new ClientFraudTypesObject(connectedClientUnknownAbuser.getUcid(), FRAUD_TYPE_UNKNOWN, FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat()));
        registrationRuleData.clientFraudTypes.add(new ClientFraudTypesObject(connectedClientGapAbuser.getUcid(), FraudType.GAP_TRADING.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat()));
        registrationRuleData.clientFraudTypes.add(new ClientFraudTypesObject(connectedClientLatencyAbuser.getUcid(), FraudType.LATENCY_ARBITRAGE.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat()));
        registrationRuleData.clientFraudTypes.add(new ClientFraudTypesObject(connectedClientPricingErrorAbuser.getUcid(), FraudType.PRICING_ERROR.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat()));
        registrationRuleData.clientFraudTypes.add(new ClientFraudTypesObject(connectedClientNbpAbuser.getUcid(), FraudType.NBP_ABUSE.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat()));
        registrationRuleData.clientFraudTypes.add(new ClientFraudTypesObject(connectedClientHftAbuser.getUcid(), FraudType.HFT_ABUSE.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat()));
        registrationRuleData.clientFraudTypes.add(new ClientFraudTypesObject(connectedClientLoopholeAbuser.getUcid(), FraudType.LOOPHOLE_ABUSE.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat()));

        registrationRuleData.lnSessionParsedObject.setPolicyScore(-21);
        registrationRuleData.lnSessionParsedObject.setRiskRating("high");
        registrationRuleData.connectedUsers.add(connectionAndConnectedUserCpa.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserCpa.connectionTableEntry);
        registrationRuleData.connectedClientHelpers.add(connectedClientCpa);
        registrationRuleData.connectedUsers.add(connectionAndConnectedUserBonusAbuser.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserBonusAbuser.connectionTableEntry);
        registrationRuleData.connectedClientHelpers.add(connectedClientBonusAbuser);
        registrationRuleData.connectedUsers.add(connectionAndConnectedUserVoucherAbuser.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserVoucherAbuser.connectionTableEntry);
        registrationRuleData.connectedClientHelpers.add(connectedClientVoucherAbuser);
        registrationRuleData.connectedUsers.add(connectionAndConnectedUserNewsTrader.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserNewsTrader.connectionTableEntry);
        registrationRuleData.connectedClientHelpers.add(connectedClientNewsTrader);
        registrationRuleData.connectedUsers.add(connectionAndConnectedUserTls.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserTls.connectionTableEntry);
        registrationRuleData.connectedClientHelpers.add(connectedClientTls);
        registrationRuleData.connectedUsers.add(connectionAndConnectedUserSwapAbuse.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserSwapAbuse.connectionTableEntry);
        registrationRuleData.connectedClientHelpers.add(connectedClientSwapAbuse);
        registrationRuleData.connectedUsers.add(connectionAndConnectedUserMarketManipulator.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserMarketManipulator.connectionTableEntry);
        registrationRuleData.connectedClientHelpers.add(connectedClientMarketManipulator);
        registrationRuleData.connectedUsers.add(connectionAndConnectedUserUnknownFraudster.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserUnknownFraudster.connectionTableEntry);
        registrationRuleData.connectedClientHelpers.add(connectedClientUnknownAbuser);
        registrationRuleData.connectedUsers.add(connectionAndConnectedUserGap.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserGap.connectionTableEntry);
        registrationRuleData.connectedClientHelpers.add(connectedClientGapAbuser);
        registrationRuleData.connectedUsers.add(connectionAndConnectedUserLatency.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserLatency.connectionTableEntry);
        registrationRuleData.connectedClientHelpers.add(connectedClientLatencyAbuser);
        registrationRuleData.connectedUsers.add(connectionAndConnectedUserPricingError.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserPricingError.connectionTableEntry);
        registrationRuleData.connectedClientHelpers.add(connectedClientPricingErrorAbuser);
        registrationRuleData.connectedUsers.add(connectionAndConnectedUserNbp.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserNbp.connectionTableEntry);
        registrationRuleData.connectedClientHelpers.add(connectedClientNbpAbuser);
        registrationRuleData.connectedUsers.add(connectionAndConnectedUserHft.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserHft.connectionTableEntry);
        registrationRuleData.connectedClientHelpers.add(connectedClientHftAbuser);
        registrationRuleData.connectedUsers.add(connectionAndConnectedUserLoophole.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserLoophole.connectionTableEntry);
        registrationRuleData.connectedClientHelpers.add(connectedClientLoopholeAbuser);

        registrationRuleData.emailTableEntries.add(emailTableEntryForConnectionSearch(registrationRuleData.clientHelper));
        registrationRuleData.emailTableEntries.add(emailTableEntryForConnectionSearch(connectedClientCpa));
        registrationRuleData.sessionIdTableEntries.add(sessionIdTableEntryForConnectionSearch(connectedClientBonusAbuser));
        registrationRuleData.emailTableEntries.add(emailTableEntryForConnectionSearch(connectedClientVoucherAbuser));
        registrationRuleData.emailTableEntries.add(emailTableEntryForConnectionSearch(connectedClientNewsTrader));
        registrationRuleData.emailTableEntries.add(emailTableEntryForConnectionSearch(connectedClientSwapAbuse));
        registrationRuleData.emailTableEntries.add(emailTableEntryForConnectionSearch(connectedClientMarketManipulator));
        registrationRuleData.emailTableEntries.add(emailTableEntryForConnectionSearch(connectedClientUnknownAbuser));
        registrationRuleData.emailTableEntries.add(emailTableEntryForConnectionSearch(connectedClientGapAbuser));
        registrationRuleData.emailTableEntries.add(emailTableEntryForConnectionSearch(connectedClientLatencyAbuser));
        registrationRuleData.emailTableEntries.add(emailTableEntryForConnectionSearch(connectedClientPricingErrorAbuser));
        registrationRuleData.emailTableEntries.add(emailTableEntryForConnectionSearch(connectedClientNbpAbuser));
        registrationRuleData.emailTableEntries.add(emailTableEntryForConnectionSearch(connectedClientHftAbuser));
        registrationRuleData.emailTableEntries.add(emailTableEntryForConnectionSearch(connectedClientLoopholeAbuser));
        return registrationRuleData;
    }

    public static Map<String, RegistrationRuleData> setupRegistrationRuleData() {
        startSshTunnel();
        Map<String, RegistrationRuleData> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("1", getRegistrationRuleExitEventEnd1Data());
        map.put("2", getRegistrationRuleExitEventEnd2Data());
        map.put("3p1", getRegistrationRuleExitEventEnd3p1Data());
        map.put("3p2", getRegistrationRuleExitEventEnd3p2Data());
        map.put("4p1", getRegistrationRuleExitEventEnd4p1Data());
        map.put("4p2", getRegistrationRuleExitEventEnd4p2Data());
        map.put("5", getRegistrationRuleExitEventEnd5Data());
        map.put("6", getRegistrationRuleExitEventEnd6Data());
        map.put("7v1", getRegistrationRuleExitEventEnd7Version1Data());
        map.put("7v2", getRegistrationRuleExitEventEnd7Version2Data());
        map.put("7v4", getRegistrationRuleExitEventEnd7Version4Data());
        map.put("7v5", getRegistrationRuleExitEventEnd7Version5Data());
        map.put("7v7", getRegistrationRuleExitEventEnd7Version7Data());
        map.put("7v9", getRegistrationRuleExitEventEnd7Version9Data());
        map.put("7v10", getRegistrationRuleExitEventEnd7Version10Data());
        map.put("7v11", getRegistrationRuleExitEventEnd7Version11Data());
        map.put("7v12", getRegistrationRuleExitEventEnd7Version12Data());
        map.put("7v13", getRegistrationRuleExitEventEnd7Version13Data());
        map.put("7v14", getRegistrationRuleExitEventEnd7Version14Data());
        map.put("7v15", getRegistrationRuleExitEventEnd7Version15Data());
        map.put("7v16", getRegistrationRuleExitEventEnd7Version16Data());
        map.put("7v17", getRegistrationRuleExitEventEnd7Version17Data());
        map.put("7v18", getRegistrationRuleExitEventEnd7Version18Data());
        map.put("7v19", getRegistrationRuleExitEventEnd7Version19Data());
        map.put("7v20", getRegistrationRuleExitEventEnd7Version20Data());
        map.put("7v23", getRegistrationRuleExitEventEnd7Version23Data());

        // Loop through the map with data and insert all the data into the according tables
        for (RegistrationRuleData data : map.values()) {
            insertObjectToDb(CRM_USER_TABLE_NAME, data.crmTbUserObject);
            data.sessionIdTableEntries.forEach(sessionIdTableEntry -> insertObjectToDb(SESSION_ID_TABLE_NAME, sessionIdTableEntry));
            data.emailTableEntries.forEach(emailTableEntry -> insertObjectToDb(EMAIL_TABLE_NAME, emailTableEntry));
            data.connectedUsers.forEach(user -> insertObjectToDb(CRM_USER_TABLE_NAME, user));
            data.connectedClientHelpers.forEach(user -> insertObjectToDb(EMAIL_TABLE_NAME, getEmailTableEntryByClient(user)));
            data.connections.forEach(connection -> insertObjectToDb(CONNECTIONS_TABLE_NAME, connection));
            insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, data.lnSessionParsedObject);
            data.clientFraudTypes.forEach(fraud -> insertObjectToDb(BO_CLIENT_FRAUD_TYPES_TABLE_NAME, fraud));
            data.deviceIdTableEntries.forEach(payout -> insertObjectToDb(DEVICE_ID_TABLE_NAME, payout));
            if (data.crmTbAccountObject != null) {
                insertObjectToDb(CRM_ACCOUNT_TABLE_NAME, data.crmTbAccountObject);
            }
        }
        return map;
    }

    public static void deleteRegistrationRuleData(Map<String, RegistrationRuleData> map) throws Exception {
        // Loop through the map with data and delete all the previously created data into the according tables
        for (RegistrationRuleData data : map.values()) {
            deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("user_id = %s", data.crmTbUserObject.userId));
            data.sessionIdTableEntries.forEach(sessionIdTableEntry -> deleteEntryFromDb(SESSION_ID_TABLE_NAME, String.format("session_id = '%s'", sessionIdTableEntry.sessionId)));
            data.emailTableEntries.forEach(emailTableEntry -> deleteEntryFromDb(EMAIL_TABLE_NAME, String.format("email = '%s'", emailTableEntry.email)));
            data.deviceIdTableEntries.forEach(deviceIdTableEntry -> deleteEntryFromDb(DEVICE_ID_TABLE_NAME, String.format("device_id = '%s'", deviceIdTableEntry.deviceId)));
            data.connectedUsers.forEach(user -> deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("user_id = %s", user.userId)));
            data.connectedUsers.forEach(user -> {
                deleteEntryFromDb(EMAIL_TABLE_NAME, String.format("user_id = %s", user.userId));
            });
            data.connections.forEach(connection -> deleteEntryFromDb(CONNECTIONS_TABLE_NAME, String.format("user_from = '%s'", connection.userFrom)));
            deleteEntryFromDb(LEXIS_NEXIS_TABLE_NAME, String.format("user_id = %s", data.lnSessionParsedObject.getUserId()));
            data.clientFraudTypes.forEach(fraud -> deleteEntryFromDb(BO_CLIENT_FRAUD_TYPES_TABLE_NAME, String.format("ucid = '%s'", fraud.getUcid())));
            cleanUserRestriction(data.clientHelper.getUcid());
            closeAlert(data.clientHelper.getUcid());
        }
        stopSshTunnel();
    }
}
