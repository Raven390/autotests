package helpers.data.rules.registrationRule;


import businessObjects.db.clickhouse.boClientFraudTypes.BoClientFraudTypesObject;
import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;
import businessObjects.db.clickhouse.csTbConnectionTableV2.ConnectionTableEntry;
import businessObjects.db.clickhouse.lnSessionParsedTable.LnSessionParsedObject;
import businessObjects.kafka.crmEvents.RegistrationEvent;
import helpers.data.ClientHelper;
import helpers.data.enums.Brand;

import java.time.Instant;
import java.util.ArrayList;

import static businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObjectFactory.generateUserByClient;
import static businessObjects.db.clickhouse.lnSessionParsedTable.LnSessionParsedObjectFactory.generateLexisNexisDataForUserId;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static utils.Utils.*;

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

    public static RegistrationRuleData getRegistrationRuleData(ClientHelper client) {
        CrmTbUserObject userObject = generateUserByClient(client);
        userObject.phoneNum = client.getPhoneNumber();
        userObject.email = client.getEmail();
        userObject.countryCode = client.getCountryCode();
        userObject.regulator = "VFSC2";
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
        registrationEvent.regulator = "VFSC2";
        registrationEvent.metaTraderAccount = 1;
        registrationEvent.id = getRandomUuidString();
        registrationEvent.createTime = Instant.now().toString();
        registrationEvent.type = "clientRegistration";
        return new RegistrationRuleData(client, userObject, lexisNexisObject, new ArrayList<>(), new ArrayList<>(), registrationEvent, new ArrayList<>());
    }

    public static class ConnectionAndConnectedUser {
        public ConnectionTableEntry connectionTableEntry;
        public CrmTbUserObject crmTbUserObject;

        public ConnectionAndConnectedUser(ConnectionTableEntry connectionTableEntry, CrmTbUserObject crmTbUserObject) {
            this.connectionTableEntry = connectionTableEntry;
            this.crmTbUserObject = crmTbUserObject;
        }
    }

    public static ConnectionAndConnectedUser getConnectionAndConnectedUser(ClientHelper fromClient, ClientHelper toClient) {
        ConnectionTableEntry connectionTableEntry = new ConnectionTableEntry(
                fromClient.getUcid(),
                toClient.getUcid(),
                1,
                String.format("""
                    {
                        "connect_info":{
                            "user_1":{"user_id": "%s","brand": "%s"},
                            "connection_1":{
                                "attr_info":{"payout": "463344**** **5603"},
                                "degree_connection": "sameIdentity",
                                "connection_score": 1
                            },
                            "user_2":{"user_id": "%s","brand": "%s"}
                        }
                    }""", fromClient.getUserId(), fromClient.getBrand().toLowerCase(), toClient.getUserId(), toClient.getBrand().toLowerCase()),
                getCurrentTimestampDbFormat());
        // Create connected user
        CrmTbUserObject connectedCrmTbUserObject = generateUserByClient(toClient);
        connectedCrmTbUserObject.phoneNum = fromClient.getPhoneNumber();
        connectedCrmTbUserObject.email = fromClient.getEmail();
        connectedCrmTbUserObject.countryCode = fromClient.getCountryCode();
        connectedCrmTbUserObject.regulator = "VFSC2";
        connectedCrmTbUserObject.rafReferrerId = 22;
        connectedCrmTbUserObject.ibId = 33;
        return new ConnectionAndConnectedUser(connectionTableEntry, connectedCrmTbUserObject);
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
        registrationRuleData.connections.add(connectionAndConnectedUser.connectionTableEntry);
        return registrationRuleData;
    }

    public static RegistrationRuleData getRegistrationRuleExitEventEnd5Data() {
        RegistrationRuleData registrationRuleData = getRegistrationRuleData(registrationRuleExitEventEnd5Client);
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        ConnectionAndConnectedUser connectionAndConnectedUser = getConnectionAndConnectedUser(registrationRuleExitEventEnd5Client, connectedClient);
        CrmTbUserObject crmTbUserToObject = connectionAndConnectedUser.crmTbUserObject;
        registrationRuleData.lnSessionParsedObject.policyScore = -19;
        registrationRuleData.connectedUsers.add(crmTbUserToObject);
        registrationRuleData.connections.add(connectionAndConnectedUser.connectionTableEntry);
        return registrationRuleData;
    }

    public static RegistrationRuleData getRegistrationRuleExitEventEnd6Data() {
        RegistrationRuleData registrationRuleData = getRegistrationRuleData(registrationRuleExitEventEnd6Client);
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        ConnectionAndConnectedUser connectionAndConnectedUser = getConnectionAndConnectedUser(registrationRuleExitEventEnd6Client, connectedClient);
        registrationRuleData.connectedUsers.add(connectionAndConnectedUser.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUser.connectionTableEntry);
        registrationRuleData.lnSessionParsedObject.riskRating = "high";
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
        registrationRuleData.connections.add(connectionAndConnectedUserCpa.connectionTableEntry);
        registrationRuleData.connectedUsers.add(connectionAndConnectedUserBonusAbuser.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserBonusAbuser.connectionTableEntry);
        registrationRuleData.connectedUsers.add(connectionAndConnectedUserVoucherAbuser.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserVoucherAbuser.connectionTableEntry);
        registrationRuleData.connectedUsers.add(connectionAndConnectedUserNewsTrader.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserNewsTrader.connectionTableEntry);
        registrationRuleData.connectedUsers.add(connectionAndConnectedUserTls.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserTls.connectionTableEntry);
        registrationRuleData.connectedUsers.add(connectionAndConnectedUserSwapAbuse.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserSwapAbuse.connectionTableEntry);
        registrationRuleData.connectedUsers.add(connectionAndConnectedUserMarketManipulator.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserMarketManipulator.connectionTableEntry);
        registrationRuleData.connectedUsers.add(connectionAndConnectedUserUnknownFraudster.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserUnknownFraudster.connectionTableEntry);
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
        registrationRuleData.connections.add(connectionAndConnectedUserCpa.connectionTableEntry);
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
        registrationRuleData.connections.add(connectionAndConnectedUserCpa.connectionTableEntry);
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
        registrationRuleData.connections.add(connectionAndConnectedUserBonusAbuser.connectionTableEntry);
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
        registrationRuleData.connections.add(connectionAndConnectedUserBonusAbuser.connectionTableEntry);
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
        registrationRuleData.connections.add(connectionAndConnectedUserBonusAbuser.connectionTableEntry);
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
        registrationRuleData.connections.add(connectionAndConnectedUserVoucherAbuser.connectionTableEntry);
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
        registrationRuleData.connections.add(connectionAndConnectedUserVoucherAbuser.connectionTableEntry);
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
        registrationRuleData.connections.add(connectionAndConnectedUserNewsTrader.connectionTableEntry);
        return registrationRuleData;
    }

    public static RegistrationRuleData getRegistrationRuleExitEventEnd7Version10Data() {
        RegistrationRuleData registrationRuleData = getRegistrationRuleData(registrationRuleExitEventEnd7Version10Client);

        // Abuser connected clients
        ClientHelper connectedClientNewsTrader = getRandomVantageClientAllFields();
        ConnectionAndConnectedUser connectionAndConnectedUserNewsTrader = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version10Client, connectedClientNewsTrader);

        registrationRuleData.clientFraudTypes.add(new BoClientFraudTypesObject(connectedClientNewsTrader.getUcid(), 1, "NEWS_TRADER"));


        registrationRuleData.connectedUsers.add(connectionAndConnectedUserNewsTrader.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserNewsTrader.connectionTableEntry);
        return registrationRuleData;
    }

    public static RegistrationRuleData getRegistrationRuleExitEventEnd7Version11Data() {
        RegistrationRuleData registrationRuleData = getRegistrationRuleData(registrationRuleExitEventEnd7Version11Client);

        // Abuser connected clients
        ClientHelper connectedClientTls = getRandomVantageClientAllFields();
        ConnectionAndConnectedUser connectionAndConnectedUserTls = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version11Client, connectedClientTls);

        registrationRuleData.clientFraudTypes.add(new BoClientFraudTypesObject(connectedClientTls.getUcid(), 1, "TLS"));

        registrationRuleData.connectedUsers.add(connectionAndConnectedUserTls.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserTls.connectionTableEntry);
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
        registrationRuleData.connections.add(connectionAndConnectedUserSwapAbuse.connectionTableEntry);
        return registrationRuleData;
    }

    public static RegistrationRuleData getRegistrationRuleExitEventEnd7Version13Data() {
        RegistrationRuleData registrationRuleData = getRegistrationRuleData(registrationRuleExitEventEnd7Version13Client);

        // Abuser connected clients
        ClientHelper connectedClientMarketManipulator = getRandomVantageClientAllFields();
        ConnectionAndConnectedUser connectionAndConnectedUserMarketManipulator = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version13Client, connectedClientMarketManipulator);

        registrationRuleData.clientFraudTypes.add(new BoClientFraudTypesObject(connectedClientMarketManipulator.getUcid(), 1, "MARKET_MANIPULATION"));

        registrationRuleData.connectedUsers.add(connectionAndConnectedUserMarketManipulator.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserMarketManipulator.connectionTableEntry);
        return registrationRuleData;
    }

    public static RegistrationRuleData getRegistrationRuleExitEventEnd7Version14Data() {
        RegistrationRuleData registrationRuleData = getRegistrationRuleData(registrationRuleExitEventEnd7Version14Client);

        // Abuser connected clients
        ClientHelper connectedClientUnknownAbuser = getRandomVantageClientAllFields();
        ConnectionAndConnectedUser connectionAndConnectedUserUnknownAbuser = getConnectionAndConnectedUser(registrationRuleExitEventEnd7Version14Client, connectedClientUnknownAbuser);

        registrationRuleData.clientFraudTypes.add(new BoClientFraudTypesObject(connectedClientUnknownAbuser.getUcid(), 1, "UNKNOWN"));


        registrationRuleData.connectedUsers.add(connectionAndConnectedUserUnknownAbuser.crmTbUserObject);
        registrationRuleData.connections.add(connectionAndConnectedUserUnknownAbuser.connectionTableEntry);
        return registrationRuleData;
    }
}
