package helpers.data.rules.registrationRule;


import businessObjects.db.clickhouse.boClientFraudTypes.BoClientFraudTypesObject;
import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;
import businessObjects.db.clickhouse.csTbConnectionTableV3.ConnectionTableEntryV3;
import businessObjects.db.clickhouse.lnSessionParsedTable.LnSessionParsedObject;
import businessObjects.kafka.crmEvents.RegistrationEvent;
import helpers.data.ClientHelper;
import helpers.data.enums.Brand;
import generator.annotations.RuleTestData;

import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObjectFactory.generateUserByClient;
import static businessObjects.db.clickhouse.csTbEmailTable.EmailTableEntryFactory.getEmailTableEntryByClient;
import static businessObjects.db.clickhouse.lnSessionParsedTable.LnSessionParsedObjectFactory.generateLexisNexisDataForUserId;
import static businessObjects.db.clickhouse.mtTbUserTable.MtTbUserObjectFactory.generateMtTbUserData;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.DbHelper.*;
import static helpers.database.MitigationHelper.cleanUserRestriction;
import static utils.Constants.*;
import static utils.Constants.MT_USER_TABLE_NAME;
import static utils.Utils.*;

@RuleTestData("registration")
public class RegistrationRuleDataFactory {

    // Clients
    private static final ClientHelper registrationRuleExitEventEnd1Client = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleExitEventEnd2Client = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleExitEventEnd3Client = getRandomVantageClientAllFields();
    private static final ClientHelper registrationRuleExitEventEnd4Client = getRandomVantageClientAllFields();
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

    private static RegistrationRuleData getRegistrationRuleData(ClientHelper client) {
        CrmTbUserObject userObject = generateUserByClient(client);
        userObject.countryCode = client.getCountryCode();
        LnSessionParsedObject lexisNexisObject = generateLexisNexisDataForUserId(client.getUuid(), client.getUserId(), getRandomIntPositive());
        lexisNexisObject.brand = client.getBrand();
        lexisNexisObject.eventType = "account_creation";
        lexisNexisObject.email = client.getEmail();
        lexisNexisObject.userId = client.getUserId();
        lexisNexisObject.proxyIp = client.getIpAddress();
        lexisNexisObject.trueIpGeo = client.getCountryCode();
        lexisNexisObject.policyScore = -49;
        lexisNexisObject.riskRating = "low";
        RegistrationEvent registrationEvent = new RegistrationEvent();
        registrationEvent.clientId = client.getUserId();
        registrationEvent.brand = client.getBrand();
        registrationEvent.regulator = "VFSC";
        registrationEvent.metaTraderAccount = 1;
        registrationEvent.id = getRandomUuidString();
        registrationEvent.createTime = Instant.now().toString();
        registrationEvent.type = "clientRegistration";
        return new RegistrationRuleData(client, userObject, lexisNexisObject, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), registrationEvent, new ArrayList<>(), null);
    }

    private static class ConnectionAndConnectedUser {
        public ConnectionTableEntryV3 connectionTableEntryV3;
        public CrmTbUserObject crmTbUserObject;
        public ClientHelper clientHelper;

        public ConnectionAndConnectedUser(ConnectionTableEntryV3 connectionTableEntryV3,
                CrmTbUserObject crmTbUserObject, ClientHelper clientHelper) {
            this.connectionTableEntryV3 = connectionTableEntryV3;
            this.crmTbUserObject = crmTbUserObject;
            this.clientHelper = clientHelper;
        }
    }

    private static ConnectionAndConnectedUser getConnectionAndConnectedUser(ClientHelper fromClient,
            ClientHelper toClient) {
        ConnectionTableEntryV3 connectionTableEntryV3 = new ConnectionTableEntryV3(
                fromClient.getUcid(), toClient.getUcid(), "Same Identity", 1d, "{\"payoutId\": \"463344**** **5603\"}", getCurrentTimestampDbFormat()
        );
        // Create connected user
        CrmTbUserObject connectedCrmTbUserObject = generateUserByClient(toClient);
        connectedCrmTbUserObject.countryCode = fromClient.getCountryCode();
        connectedCrmTbUserObject.rafReferrerId = 22;
        connectedCrmTbUserObject.ibId = 33;
        return new ConnectionAndConnectedUser(connectionTableEntryV3, connectedCrmTbUserObject, toClient);
    }

    public static RegistrationRuleData getRegistrationRuleExitEventEnd1Data() {
        return getRegistrationRuleData(registrationRuleExitEventEnd1Client);
    }

    public static RegistrationRuleData getRegistrationRuleExitEventEnd2Data() {
        RegistrationRuleData registrationRuleData = getRegistrationRuleData(registrationRuleExitEventEnd2Client);
        LnSessionParsedObject lexisNexisObject = registrationRuleData.lnSessionParsedObject;
        lexisNexisObject.policyScore = -50;
        lexisNexisObject.riskRating = "high";
        return registrationRuleData;
    }

    public static RegistrationRuleData getRegistrationRuleExitEventEnd3Data() {
        RegistrationRuleData registrationRuleData = getRegistrationRuleData(registrationRuleExitEventEnd3Client);
        LnSessionParsedObject lexisNexisObject = registrationRuleData.lnSessionParsedObject;
        lexisNexisObject.trueIpGeo = "US";
        CrmTbUserObject crmTbUserObject = registrationRuleData.crmTbUserObject;
        crmTbUserObject.countryCode = "CY";
        return registrationRuleData;
    }

    public static RegistrationRuleData getRegistrationRuleExitEventEnd4Data() {
        RegistrationRuleData registrationRuleData = getRegistrationRuleData(registrationRuleExitEventEnd4Client);
        CrmTbUserObject crmTbUserObject = registrationRuleData.crmTbUserObject;
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        ConnectionAndConnectedUser connectionAndConnectedUser = getConnectionAndConnectedUser(registrationRuleExitEventEnd4Client, connectedClient);
        CrmTbUserObject crmTbUserToObject = connectionAndConnectedUser.crmTbUserObject;
        crmTbUserObject.rafReferrerId = crmTbUserToObject.rafReferrerId;
        crmTbUserObject.ibId = crmTbUserToObject.ibId;
        registrationRuleData.connectedUsers.add(crmTbUserToObject);
        registrationRuleData.connections.add(connectionAndConnectedUser.connectionTableEntryV3);
        registrationRuleData.connectedClientHelpers.add(connectedClient);
        return registrationRuleData;
    }

    public static RegistrationRuleData getRegistrationRuleExitEventEnd5Data() {
        RegistrationRuleData registrationRuleData = getRegistrationRuleData(registrationRuleExitEventEnd5Client);
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        ConnectionAndConnectedUser connectionAndConnectedUser = getConnectionAndConnectedUser(registrationRuleExitEventEnd5Client, connectedClient);
        CrmTbUserObject crmTbUserToObject = connectionAndConnectedUser.crmTbUserObject;
        registrationRuleData.lnSessionParsedObject.policyScore = -19;
        registrationRuleData.connectedUsers.add(crmTbUserToObject);
        registrationRuleData.connections.add(connectionAndConnectedUser.connectionTableEntryV3);
        registrationRuleData.connectedClientHelpers.add(connectedClient);
        return registrationRuleData;
    }

    public static RegistrationRuleData getRegistrationRuleExitEventEnd6Data() {
        RegistrationRuleData registrationRuleData = getRegistrationRuleData(registrationRuleExitEventEnd6Client);
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        ConnectionAndConnectedUser connectionAndConnectedUser = getConnectionAndConnectedUser(registrationRuleExitEventEnd6Client, connectedClient);
        registrationRuleData.connectedUsers.add(connectionAndConnectedUser.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUser.connectionTableEntryV3);
        registrationRuleData.lnSessionParsedObject.riskRating = "high";
        registrationRuleData.connectedClientHelpers.add(connectedClient);
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

        ConnectionAndConnectedUser connectionAndConnectedUserCpa = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version1Client, connectedClientCpa);
        ConnectionAndConnectedUser connectionAndConnectedUserBonusAbuser = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version1Client, connectedClientBonusAbuser);
        ConnectionAndConnectedUser connectionAndConnectedUserVoucherAbuser = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version1Client, connectedClientVoucherAbuser);
        ConnectionAndConnectedUser connectionAndConnectedUserNewsTrader = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version1Client, connectedClientNewsTrader);
        ConnectionAndConnectedUser connectionAndConnectedUserTls = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version1Client, connectedClientTls);
        ConnectionAndConnectedUser connectionAndConnectedUserSwapAbuse = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version1Client, connectedClientSwapAbuse);
        ConnectionAndConnectedUser connectionAndConnectedUserMarketManipulator = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version1Client, connectedClientMarketManipulator);
        ConnectionAndConnectedUser connectionAndConnectedUserUnknownFraudster = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version1Client, connectedClientUnknownAbuser);

        registrationRuleData.clientFraudTypes.add(new BoClientFraudTypesObject(connectedClientCpa.getUcid(), 1, "CPA"));
        registrationRuleData.clientFraudTypes.add(new BoClientFraudTypesObject(connectedClientBonusAbuser.getUcid(), 1, "HEDGING"));
        registrationRuleData.clientFraudTypes.add(new BoClientFraudTypesObject(connectedClientVoucherAbuser.getUcid(), 1, "LOSS_VOUCHER_ABUSE"));
        registrationRuleData.clientFraudTypes.add(new BoClientFraudTypesObject(connectedClientNewsTrader.getUcid(), 1, "NEWS_TRADER"));
        registrationRuleData.clientFraudTypes.add(new BoClientFraudTypesObject(connectedClientTls.getUcid(), 1, "TLS"));
        registrationRuleData.clientFraudTypes.add(new BoClientFraudTypesObject(connectedClientSwapAbuse.getUcid(), 1, "SWAP_ARBITRAGE"));
        registrationRuleData.clientFraudTypes.add(new BoClientFraudTypesObject(connectedClientMarketManipulator.getUcid(), 1, "MARKET_MANIPULATION"));
        registrationRuleData.clientFraudTypes.add(new BoClientFraudTypesObject(connectedClientUnknownAbuser.getUcid(), 1, "UNKNOWN"));

        registrationRuleData.lnSessionParsedObject.policyScore = -21;
        registrationRuleData.lnSessionParsedObject.riskRating = "high";
        registrationRuleData.connectedUsers.add(connectionAndConnectedUserCpa.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserCpa.connectionTableEntryV3);
        registrationRuleData.connectedClientHelpers.add(connectedClientCpa);
        registrationRuleData.connectedUsers.add(connectionAndConnectedUserBonusAbuser.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserBonusAbuser.connectionTableEntryV3);
        registrationRuleData.connectedClientHelpers.add(connectedClientBonusAbuser);
        registrationRuleData.connectedUsers.add(connectionAndConnectedUserVoucherAbuser.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserVoucherAbuser.connectionTableEntryV3);
        registrationRuleData.connectedClientHelpers.add(connectedClientVoucherAbuser);
        registrationRuleData.connectedUsers.add(connectionAndConnectedUserNewsTrader.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserNewsTrader.connectionTableEntryV3);
        registrationRuleData.connectedClientHelpers.add(connectedClientNewsTrader);
        registrationRuleData.connectedUsers.add(connectionAndConnectedUserTls.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserTls.connectionTableEntryV3);
        registrationRuleData.connectedClientHelpers.add(connectedClientTls);
        registrationRuleData.connectedUsers.add(connectionAndConnectedUserSwapAbuse.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserSwapAbuse.connectionTableEntryV3);
        registrationRuleData.connectedClientHelpers.add(connectedClientSwapAbuse);
        registrationRuleData.connectedUsers.add(connectionAndConnectedUserMarketManipulator.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserMarketManipulator.connectionTableEntryV3);
        registrationRuleData.connectedClientHelpers.add(connectedClientMarketManipulator);
        registrationRuleData.connectedUsers.add(connectionAndConnectedUserUnknownFraudster.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserUnknownFraudster.connectionTableEntryV3);
        registrationRuleData.connectedClientHelpers.add(connectedClientUnknownAbuser);

        return registrationRuleData;
    }

    public static RegistrationRuleData getRegistrationRuleExitEventEnd7Version2Data() {
        RegistrationRuleData registrationRuleData = getRegistrationRuleData(registrationRuleExitEventEnd7Version2Client);

        // Abuser connected clients
        ClientHelper connectedClientCpa = getRandomVantageClientAllFields();
        ConnectionAndConnectedUser connectionAndConnectedUserCpa = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version2Client, connectedClientCpa);

        registrationRuleData.clientFraudTypes.add(new BoClientFraudTypesObject(connectedClientCpa.getUcid(), 1, "CPA"));

        registrationRuleData.lnSessionParsedObject.policyScore = -19;
        registrationRuleData.connectedUsers.add(connectionAndConnectedUserCpa.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserCpa.connectionTableEntryV3);
        registrationRuleData.connectedClientHelpers.add(connectedClientCpa);
        return registrationRuleData;
    }

    public static RegistrationRuleData getRegistrationRuleExitEventEnd7Version3Data() {
        RegistrationRuleData registrationRuleData = getRegistrationRuleData(registrationRuleExitEventEnd7Version3Client);

        // Abuser connected clients
        ClientHelper connectedClientCpa = getRandomVantageClientAllFields();
        ConnectionAndConnectedUser connectionAndConnectedUserCpa = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version3Client, connectedClientCpa);

        registrationRuleData.clientFraudTypes.add(new BoClientFraudTypesObject(connectedClientCpa.getUcid(), 1, "CPA"));

        registrationRuleData.lnSessionParsedObject.policyScore = -21;
        registrationRuleData.lnSessionParsedObject.riskRating = "high";
        registrationRuleData.connectedUsers.add(connectionAndConnectedUserCpa.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserCpa.connectionTableEntryV3);
        registrationRuleData.connectedClientHelpers.add(connectedClientCpa);
        return registrationRuleData;
    }

    public static RegistrationRuleData getRegistrationRuleExitEventEnd7Version4Data() {
        registrationRuleExitEventEnd7Version4Client.setBrand(Brand.VJP);
        RegistrationRuleData registrationRuleData = getRegistrationRuleData(registrationRuleExitEventEnd7Version4Client);

        // Abuser connected clients
        ClientHelper connectedClientBonusAbuser = getRandomVantageClientAllFields();
        connectedClientBonusAbuser.setBrand(Brand.VJP);
        ConnectionAndConnectedUser connectionAndConnectedUserBonusAbuser = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version4Client, connectedClientBonusAbuser);

        registrationRuleData.clientFraudTypes.add(new BoClientFraudTypesObject(connectedClientBonusAbuser.getUcid(), 1, "HEDGING"));

        registrationRuleData.connectedUsers.add(connectionAndConnectedUserBonusAbuser.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserBonusAbuser.connectionTableEntryV3);
        registrationRuleData.connectedClientHelpers.add(connectedClientBonusAbuser);
        return registrationRuleData;
    }

    public static RegistrationRuleData getRegistrationRuleExitEventEnd7Version5Data() {
        RegistrationRuleData registrationRuleData = getRegistrationRuleData(registrationRuleExitEventEnd7Version5Client);

        // Abuser connected clients
        ClientHelper connectedClientBonusAbuser = getRandomVantageClientAllFields();
        ConnectionAndConnectedUser connectionAndConnectedUserBonusAbuser = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version5Client, connectedClientBonusAbuser);

        registrationRuleData.clientFraudTypes.add(new BoClientFraudTypesObject(connectedClientBonusAbuser.getUcid(), 1, "HEDGING"));

        registrationRuleData.lnSessionParsedObject.policyScore = -19;
        registrationRuleData.connectedUsers.add(connectionAndConnectedUserBonusAbuser.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserBonusAbuser.connectionTableEntryV3);
        registrationRuleData.connectedClientHelpers.add(connectedClientBonusAbuser);
        return registrationRuleData;
    }

    public static RegistrationRuleData getRegistrationRuleExitEventEnd7Version6Data() {
        RegistrationRuleData registrationRuleData = getRegistrationRuleData(registrationRuleExitEventEnd7Version6Client);

        // Abuser connected clients
        ClientHelper connectedClientBonusAbuser = getRandomVantageClientAllFields();
        ConnectionAndConnectedUser connectionAndConnectedUserBonusAbuser = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version6Client, connectedClientBonusAbuser);

        registrationRuleData.clientFraudTypes.add(new BoClientFraudTypesObject(connectedClientBonusAbuser.getUcid(), 1, "HEDGING"));

        registrationRuleData.lnSessionParsedObject.policyScore = -21;
        registrationRuleData.lnSessionParsedObject.riskRating = "high";
        registrationRuleData.connectedUsers.add(connectionAndConnectedUserBonusAbuser.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserBonusAbuser.connectionTableEntryV3);
        registrationRuleData.connectedClientHelpers.add(connectedClientBonusAbuser);
        return registrationRuleData;
    }

    public static RegistrationRuleData getRegistrationRuleExitEventEnd7Version7Data() {
        RegistrationRuleData registrationRuleData = getRegistrationRuleData(registrationRuleExitEventEnd7Version7Client);

        // Abuser connected clients
        ClientHelper connectedClientVoucherAbuser = getRandomVantageClientAllFields();
        ConnectionAndConnectedUser connectionAndConnectedUserVoucherAbuser = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version7Client, connectedClientVoucherAbuser);

        registrationRuleData.clientFraudTypes.add(new BoClientFraudTypesObject(connectedClientVoucherAbuser.getUcid(), 1, "LOSS_VOUCHER_ABUSE"));

        registrationRuleData.lnSessionParsedObject.policyScore = -19;
        registrationRuleData.connectedUsers.add(connectionAndConnectedUserVoucherAbuser.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserVoucherAbuser.connectionTableEntryV3);
        registrationRuleData.connectedClientHelpers.add(connectedClientVoucherAbuser);
        return registrationRuleData;
    }

    public static RegistrationRuleData getRegistrationRuleExitEventEnd7Version8Data() {
        RegistrationRuleData registrationRuleData = getRegistrationRuleData(registrationRuleExitEventEnd7Version8Client);

        // Abuser connected clients
        ClientHelper connectedClientVoucherAbuser = getRandomVantageClientAllFields();
        ConnectionAndConnectedUser connectionAndConnectedUserVoucherAbuser = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version8Client, connectedClientVoucherAbuser);

        registrationRuleData.clientFraudTypes.add(new BoClientFraudTypesObject(connectedClientVoucherAbuser.getUcid(), 1, "LOSS_VOUCHER_ABUSE"));


        registrationRuleData.lnSessionParsedObject.policyScore = -21;
        registrationRuleData.lnSessionParsedObject.riskRating = "high";
        registrationRuleData.connectedUsers.add(connectionAndConnectedUserVoucherAbuser.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserVoucherAbuser.connectionTableEntryV3);
        registrationRuleData.connectedClientHelpers.add(connectedClientVoucherAbuser);
        return registrationRuleData;
    }

    public static RegistrationRuleData getRegistrationRuleExitEventEnd7Version9Data() {
        registrationRuleExitEventEnd7Version9Client.setBrand(Brand.VJP);
        RegistrationRuleData registrationRuleData = getRegistrationRuleData(registrationRuleExitEventEnd7Version9Client);

        // Abuser connected clients
        ClientHelper connectedClientNewsTrader = getRandomVantageClientAllFields();
        connectedClientNewsTrader.setBrand(Brand.VJP);
        ConnectionAndConnectedUser connectionAndConnectedUserNewsTrader = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version9Client, connectedClientNewsTrader);

        registrationRuleData.clientFraudTypes.add(new BoClientFraudTypesObject(connectedClientNewsTrader.getUcid(), 1, "NEWS_TRADER"));


        registrationRuleData.connectedUsers.add(connectionAndConnectedUserNewsTrader.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserNewsTrader.connectionTableEntryV3);
        registrationRuleData.connectedClientHelpers.add(connectedClientNewsTrader);
        return registrationRuleData;
    }

    public static RegistrationRuleData getRegistrationRuleExitEventEnd7Version10Data() {
        RegistrationRuleData registrationRuleData = getRegistrationRuleData(registrationRuleExitEventEnd7Version10Client);

        // Abuser connected clients
        ClientHelper connectedClientNewsTrader = getRandomVantageClientAllFields();
        ConnectionAndConnectedUser connectionAndConnectedUserNewsTrader = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version10Client, connectedClientNewsTrader);

        registrationRuleData.clientFraudTypes.add(new BoClientFraudTypesObject(connectedClientNewsTrader.getUcid(), 1, "NEWS_TRADER"));


        registrationRuleData.connectedUsers.add(connectionAndConnectedUserNewsTrader.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserNewsTrader.connectionTableEntryV3);
        registrationRuleData.connectedClientHelpers.add(connectedClientNewsTrader);
        return registrationRuleData;
    }

    public static RegistrationRuleData getRegistrationRuleExitEventEnd7Version11Data() {
        RegistrationRuleData registrationRuleData = getRegistrationRuleData(registrationRuleExitEventEnd7Version11Client);

        // Abuser connected clients
        ClientHelper connectedClientTls = getRandomVantageClientAllFields();
        ConnectionAndConnectedUser connectionAndConnectedUserTls = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version11Client, connectedClientTls);

        registrationRuleData.clientFraudTypes.add(new BoClientFraudTypesObject(connectedClientTls.getUcid(), 1, "TLS"));

        registrationRuleData.connectedUsers.add(connectionAndConnectedUserTls.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserTls.connectionTableEntryV3);
        registrationRuleData.connectedClientHelpers.add(connectedClientTls);
        return registrationRuleData;
    }

    public static RegistrationRuleData getRegistrationRuleExitEventEnd7Version12Data() {
        RegistrationRuleData registrationRuleData = getRegistrationRuleData(registrationRuleExitEventEnd7Version12Client);

        // Abuser connected clients
        ClientHelper connectedClientSwapAbuse = getRandomVantageClientAllFields();
        ConnectionAndConnectedUser connectionAndConnectedUserSwapAbuse = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version12Client, connectedClientSwapAbuse);

        registrationRuleData.lnSessionParsedObject.riskRating = "high";

        registrationRuleData.clientFraudTypes.add(new BoClientFraudTypesObject(connectedClientSwapAbuse.getUcid(), 1, "SWAP_ARBITRAGE"));

        registrationRuleData.connectedUsers.add(connectionAndConnectedUserSwapAbuse.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserSwapAbuse.connectionTableEntryV3);
        registrationRuleData.connectedClientHelpers.add(connectedClientSwapAbuse);
        return registrationRuleData;
    }

    public static RegistrationRuleData getRegistrationRuleExitEventEnd7Version13Data() {
        RegistrationRuleData registrationRuleData = getRegistrationRuleData(registrationRuleExitEventEnd7Version13Client);

        // Abuser connected clients
        ClientHelper connectedClientMarketManipulator = getRandomVantageClientAllFields();
        ConnectionAndConnectedUser connectionAndConnectedUserMarketManipulator = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version13Client, connectedClientMarketManipulator);

        registrationRuleData.clientFraudTypes.add(new BoClientFraudTypesObject(connectedClientMarketManipulator.getUcid(), 1, "MARKET_MANIPULATION"));

        registrationRuleData.connectedUsers.add(connectionAndConnectedUserMarketManipulator.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserMarketManipulator.connectionTableEntryV3);
        registrationRuleData.connectedClientHelpers.add(connectedClientMarketManipulator);

        registrationRuleData.mtTbUserObject = generateMtTbUserData(registrationRuleData.clientHelper.getUcid(), getRandomIntPositive(), 188);
        return registrationRuleData;
    }

    public static RegistrationRuleData getRegistrationRuleExitEventEnd7Version14Data() {
        RegistrationRuleData registrationRuleData = getRegistrationRuleData(registrationRuleExitEventEnd7Version14Client);

        // Abuser connected clients
        ClientHelper connectedClientUnknownAbuser = getRandomVantageClientAllFields();
        ConnectionAndConnectedUser connectionAndConnectedUserUnknownAbuser = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version14Client, connectedClientUnknownAbuser);

        registrationRuleData.clientFraudTypes.add(new BoClientFraudTypesObject(connectedClientUnknownAbuser.getUcid(), 1, "UNKNOWN"));
        registrationRuleData.clientFraudTypes.add(new BoClientFraudTypesObject(connectedClientUnknownAbuser.getUcid(), 2, "MOREUNKNOWN"));


        registrationRuleData.connectedUsers.add(connectionAndConnectedUserUnknownAbuser.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserUnknownAbuser.connectionTableEntryV3);
        registrationRuleData.connectedClientHelpers.add(connectedClientUnknownAbuser);
        return registrationRuleData;
    }

    public static Map<String, RegistrationRuleData> setupRegistrationRuleData() throws ReflectiveOperationException,
            SQLException {
        startSshTunnel();
        Map<String, RegistrationRuleData> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("1", getRegistrationRuleExitEventEnd1Data());
        map.put("2", getRegistrationRuleExitEventEnd2Data());
        map.put("3", getRegistrationRuleExitEventEnd3Data());
        map.put("4", getRegistrationRuleExitEventEnd4Data());
        map.put("5", getRegistrationRuleExitEventEnd5Data());
        map.put("6", getRegistrationRuleExitEventEnd6Data());
//        map.put("7v1", getRegistrationRuleExitEventEnd7Version1Data());
        map.put("7v2", getRegistrationRuleExitEventEnd7Version2Data());
        map.put("7v3", getRegistrationRuleExitEventEnd7Version3Data());
        map.put("7v4", getRegistrationRuleExitEventEnd7Version4Data());
        map.put("7v5", getRegistrationRuleExitEventEnd7Version5Data());
        map.put("7v6", getRegistrationRuleExitEventEnd7Version6Data());
        map.put("7v7", getRegistrationRuleExitEventEnd7Version7Data());
        map.put("7v8", getRegistrationRuleExitEventEnd7Version8Data());
        map.put("7v9", getRegistrationRuleExitEventEnd7Version9Data());
        map.put("7v10", getRegistrationRuleExitEventEnd7Version10Data());
        map.put("7v11", getRegistrationRuleExitEventEnd7Version11Data());
        map.put("7v12", getRegistrationRuleExitEventEnd7Version12Data());
        map.put("7v13", getRegistrationRuleExitEventEnd7Version13Data());
        map.put("7v14", getRegistrationRuleExitEventEnd7Version14Data());

        // Loop through the map with data and insert all the data into the according tables
        for (RegistrationRuleData data : map.values()) {
            insertObjectToDb(CRM_USER_TABLE_NAME, data.crmTbUserObject);
            insertObjectToDb(EMAIL_TABLE_NAME, getEmailTableEntryByClient(data.clientHelper));
            data.connectedUsers.forEach(user -> {
                try {
                    insertObjectToDb(CRM_USER_TABLE_NAME, user);
                } catch (SQLException | ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            });
            data.connectedClientHelpers.forEach(user -> {
                try {
                    insertObjectToDb(EMAIL_TABLE_NAME, getEmailTableEntryByClient(user));
                } catch (SQLException | ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            });
            data.connections.forEach(connection -> {
                try {
                    insertObjectToDb(CONNECTIONS_V3_TABLE_NAME, connection);
                } catch (SQLException | ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            });
            insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, data.lnSessionParsedObject);
            data.clientFraudTypes.forEach(fraud -> {
                try {
                    insertObjectToDb(BO_CLIENT_FRAUD_TYPES_TABLE_NAME, fraud);
                } catch (SQLException | ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            });
            if (data.mtTbUserObject != null) {
                insertObjectToDb(MT_USER_TABLE_NAME, data.mtTbUserObject);
            }
        }
        return map;
    }

    public static void deleteRegistrationRuleData(Map<String, RegistrationRuleData> map) throws Exception {
        // Loop through the map with data and delete all the previously created data into the according tables
        for (RegistrationRuleData data : map.values()) {
            deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("user_id = %s", data.crmTbUserObject.userId));
            deleteEntryFromDb(EMAIL_TABLE_NAME, String.format("user_id = %s", data.crmTbUserObject.userId));
            data.connectedUsers.forEach(user -> {
                try {
                    deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("user_id = %s", user.userId));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            data.connectedUsers.forEach(user -> {
                try {
                    deleteEntryFromDb(EMAIL_TABLE_NAME, String.format("user_id = %s", user.userId));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            data.connections.forEach(connection -> {
                try {
                    deleteEntryFromDb(CONNECTIONS_V3_TABLE_NAME, String.format("user_from = '%s'", connection.userFrom));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            deleteEntryFromDb(LEXIS_NEXIS_TABLE_NAME, String.format("user_id = %s", data.lnSessionParsedObject.userId));
            data.clientFraudTypes.forEach(fraud -> {
                try {
                    deleteEntryFromDb(BO_CLIENT_FRAUD_TYPES_TABLE_NAME, String.format("ucid = '%s'", fraud.ucid));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            if (data.mtTbUserObject != null) {
                deleteEntryFromDb(MT_USER_TABLE_NAME, String.format("ucid = '%s'", data.mtTbUserObject.ucid));
            }
            cleanUserRestriction(data.clientHelper.getUcid());
            closeAlert(data.clientHelper.getUcid());
        }
        stopSshTunnel();
    }
}
