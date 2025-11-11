package helpers.data.rules.payments;

import business_objects.db.clickhouse.client_fraud_types.ClientFraudTypes;
import business_objects.db.clickhouse.data_science_test.connection_table.ConnectionTableEntry;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.mt_tb_credits.MtTbCreditsObject;
import business_objects.kafka.crm_events.CrmWithdrawalEvent;
import generator.annotations.RuleTestData;
import helpers.data.ClientHelper;
import helpers.data.enums.DateTimeFormat;
import helpers.data.enums.FraudTypeOld;
import helpers.data.DataHelper;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import utils.Utils;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountData;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.ln_session_parsed.LnSessionParsedObjectFactory.generateLexisNexisDataByClient;
import static business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedFactory.generateMt5DealsCoercedObject;
import static business_objects.db.clickhouse.mt_tb_credits.MtTbCreditsObjectFactory.*;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.ClientFactory.getRandomVantageClientNoCpaIbRef;
import static helpers.data.DataHelper.createClient;
import static helpers.data.DataHelper.setupData;
import static helpers.data.enums.NbdComment.getRandomNbdComment;
import static helpers.database.CleanTableHelper.cleanBoFraudTypesTableByUcid;
import static helpers.database.CleanTableHelper.cleanFraudTypeTableByClient;
import static helpers.database.DbHelper.startSshTunnel;
import static utils.Constants.*;
import static utils.Utils.*;

@RuleTestData("no-deposit-bonus")
public class NdbRuleDataFactory {

    private static final ClientHelper ndbRuleClient1 = getRandomVantageClientNoCpaIbRef();
    private static final ClientHelper ndbRuleClient2 = getRandomVantageClientNoCpaIbRef();
    private static final ClientHelper ndbRuleClient3 = getRandomVantageClientNoCpaIbRef();
    private static final ClientHelper ndbRuleClient4 = getRandomVantageClientNoCpaIbRef();
    private static final ClientHelper ndbRuleClient5 = getRandomVantageClientNoCpaIbRef();
    private static final ClientHelper ndbRuleClient6 = getRandomVantageClientNoCpaIbRef();
    private static final ClientHelper ndbRuleClient7 = getRandomVantageClientNoCpaIbRef();
    private static final ClientHelper ndbRuleClient8 = getRandomVantageClientNoCpaIbRef();
    private static final ClientHelper ndbRuleExitEventEnd8Client = getRandomVantageClientNoCpaIbRef();
    private static final ClientHelper ndbRuleExitEventEnd9Client = getRandomVantageClientNoCpaIbRef();

    private static final String sameEmail = "VGlhbRQlxOaLfl/CgrjL1CfZEIYLXEQL";
    private static final int sameIb = 6667;
    private static final int firstIb = 6668;
    private static final int secondIb = 6669;

    @Step("Create data for Mirror trading rule")
    private static DataHelper getNdbRuleData(ClientHelper client) {
        DataHelper data = new DataHelper();
        createClient(data, client);

        client.setIbId(1);
        data.crmWithdrawalEvent = new CrmWithdrawalEvent(
                "MT4",                            // accountType
                Utils.getRandomIntPositive().toString(),      // binNumber
                data.clientHelper.getBrand().toLowerCase(),   // brand
                "",                                           // checkName
                data.clientHelper.getUserId(),                // clientId
                Instant.now().toString(),                  // eventDate (you can format if you need +03:00)
                "4",                                          // expMonth
                "2030",                                       // expYear
                data.clientHelper.getFirstName(),             // fullName
                getRandomUuidString(),                        // id
                "VTSG" + getRandomIntPositive(),              // merchantOrderId (example)
                data.clientHelper.getTradingAccount(),        // mt4Account
                PAYMENT_PROVIDER_FASAPAY,            // paymentChannelCode
                "-",                                 // paymentChannelName
                "CREDIT_CARD",                       // paymentMethodCode
                "WEB",                               // platform
                data.clientHelper.getRegulator(),    // regulator
                "1.0",                               // schemaVersion
                CRM_WITHDRAWAL_EVENT,                // type
                1,                                   // withdrawalAmount
                Instant.now().toString(),               // withdrawalApplicationTime
                "EUR",                               // withdrawalCurrency
                getRandomIntPositive()               // withdrawalId
        );

        return data;
    }

    private static ConnectionTableEntry getConnection(ClientHelper fromClient, ClientHelper toClient) {
        return new ConnectionTableEntry(
                fromClient.getUcid(), toClient.getUcid(), CONNECTION_TYPE_SAME_IDENTITY, 1d, List.of(
                        new ConnectionTableEntry.ConnectionInfo(
                                CONNECTION_ATTRIBUTE_NAME_PAYOUT, CONNECTION_SEARCH_DATA_CARD_NUMBER, CONNECTION_SEARCH_DATA_CARD_NUMBER, CONNECTION_TYPE_RELATION_TYPE_EXACT
                        )), getCurrentTimestampDbFormat()
        );
    }

    private static DataHelper getNdbRuleExitEventEnd1Data() {
        DataHelper data = getNdbRuleData(ndbRuleClient1);
        return data;
    }

    private static DataHelper getNdbRuleExitEventEnd2Data() {
        DataHelper data = getNdbRuleData(ndbRuleClient2);
        data.mtTbCreditsObjects = List.of(generateCreditsByClient(data.clientHelper));
        data.mtTbCreditsObjects.getFirst().comment = "Promo-NDB-Credit In";
        return data;
    }

    private static DataHelper getNdbRuleExitEventEnd3Data() {
        DataHelper data = getNdbRuleData(ndbRuleClient3);
        data.mtTbCreditsObjects = List.of(generateCreditsByClient(data.clientHelper));
        data.mtTbCreditsObjects.getFirst().comment = "Promo-NDB-Credit In";
        data.mt5DealsCoercedObjects = generateMt5DealsCoercedObject(data.clientHelper, 49, getCurrentTimestampMinusOffsetFormatted(
                DateTimeFormat.DATE_AND_TIME, 1, 0, 0, 0));
        return data;
    }

    private static DataHelper getNdbRuleExitEventEnd4Data() {
        DataHelper data = getNdbRuleData(ndbRuleClient6);


        Allure.step("Get credits with ndb comment");
        data.mtTbCreditsObjects = List.of(generateCreditsByClient(data.clientHelper));
        data.mtTbCreditsObjects.getFirst().comment = "Promo-NDB-Credit In";

        Allure.step("Add 50 trades");
        data.mt5DealsCoercedObjects = generateMt5DealsCoercedObject(data.clientHelper, 49, getCurrentTimestampDbFormat());
        data.mt5DealsCoercedObjects.getFirst().setTime(getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 0, 0));
        return data;

//        Allure.step("Get credits with ndb comment");
//        MtTbCreditsObject credit = generateCreditsByClient(ndbRuleExitEventEnd4_1Client);
//        credit.comment = "Credit In - No Deposit Bonus";
//        Allure.step("Get connections and abuse types");
//        Allure.step("Linked other fraud cases");
//        ClientHelper connectedClient = getRandomVantageClientAllFields();
//        data.connections.add(getConnection(data.clientHelper, connectedClient));
//        ClientFraudTypes clientFraudTypes = new ClientFraudTypes(
//                connectedClient.getUcid(), FraudTypeOld.LOSS_VOUCHER_ABUSE.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat()
//        );
//        Allure.step("Add data");
//        data.mtTbCreditsObjects.add(credit);
//        data.clientFraudTypes.add(clientFraudTypes);
//        Allure.step("Add 49 trades with time less than 2 weeks");
//        data.mt5DealsCoercedObjects.addAll(generateMt5DealsCoercedObject(data.clientHelper, 49, getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 13, 0, 0, 0)));
//        return data;
    }

    private static DataHelper getNdbRuleExitEventEnd42Data() {
        Allure.step("Get client data");
        DataHelper data = getNdbRuleData(ndbRuleClient7);
        Allure.step("Get credits with ndb comment");
        MtTbCreditsObject credit = generateCreditsByClient(ndbRuleClient7);
        credit.comment = "credit in-JP NDB";
        Allure.step("Get connections and abuse types");
        Allure.step("Linked other fraud cases");
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        data.connections.add(getConnection(data.clientHelper, connectedClient));
        ClientFraudTypes clientFraudTypes = new ClientFraudTypes(
                connectedClient.getUcid(), FraudTypeOld.HEDGING.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat()
        );
        Allure.step("Add data");
        data.mtTbCreditsObjects.add(credit);
        data.clientFraudTypes.add(clientFraudTypes);
        Allure.step("Add 49 trades with time less than 2 weeks");
        data.mt5DealsCoercedObjects.addAll(generateMt5DealsCoercedObject(data.clientHelper, 49, getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 13, 0, 0, 0)));
        return data;
    }

    private static DataHelper getNdbRuleExitEventEnd5Data() {
        Allure.step("Get client data");
        DataHelper data = getNdbRuleData(ndbRuleClient4);
        Allure.step("Get credits with ndb comment");
        MtTbCreditsObject credit = generateCreditsByClient(ndbRuleClient4);
        credit.comment = "Promo-NDB-Credit In";
        Allure.step("Get connections and abuse types");
        Allure.step("Linked other fraud cases");
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        ConnectionTableEntry connection = getConnection(data.clientHelper, connectedClient);
        connection.connectionInfo = "[{\"connectionAttributeName\": \"email\", \"connectionAttributeValue\": \"D1Rud4qkIAuHeGI3vIAsa5/WaBiHSPPa\", \"sourceAttributeValue\": \"D1Rud4qkIAuHeGI3vIAsa5/WaBiHSPPa\", \"relationType\": \"exact\"}]";
        data.connections.add(connection);
        ClientFraudTypes clientFraudTypes = new ClientFraudTypes(
                connectedClient.getUcid(), FraudTypeOld.getRandomFraudType(FraudTypeOld.LOSS_VOUCHER_ABUSE, FraudTypeOld.HEDGING).getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat()
        );
        Allure.step("Add data");
        data.mtTbCreditsObjects.add(credit);
        data.clientFraudTypes.add(clientFraudTypes);
        Allure.step("Add data");
        data.mtTbCreditsObjects.add(credit);
        data.clientFraudTypes.add(clientFraudTypes);
        Allure.step("Add 49 trades with time less than 2 weeks");
        data.mt5DealsCoercedObjects.addAll(generateMt5DealsCoercedObject(data.clientHelper, 49, getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 13, 0, 0, 0)));
        return data;
    }

    private static DataHelper getNdbRuleExitEventEnd6Data() {
        Allure.step("Get client data");
        DataHelper data = getNdbRuleData(ndbRuleClient5);
        Allure.step("Get credits with ndb comment");
        MtTbCreditsObject credit = generateCreditsByClient(ndbRuleClient5);
        credit.comment = "Promo-NDB-Credit In";
        Allure.step("Linked active accounts not with same email AND NDB from the last 1 week?");
        Allure.step("Add data");
        data.mtTbCreditsObjects.add(credit);
        Allure.step("Add 49 trades with time less than 2 weeks");
        data.mt5DealsCoercedObjects.addAll(generateMt5DealsCoercedObject(data.clientHelper, 49, getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 13, 0, 0, 0)));
        return data;
    }

    private static DataHelper getNdbRuleExitEventEnd7Data() throws Exception {
        Allure.step("Get client data");
        DataHelper data = getNdbRuleData(ndbRuleClient8);

        Allure.step("Setup client data in DB");
        Allure.step("set IB id that will be the same for connected user");
        CrmTbUserObject client = generateUserByClient(data.clientHelper);
        client.ibId = sameIb;
        client.email = sameEmail;
        data.crmTbUserObject = client;

        Allure.step("Get credits with ndb comment");
        MtTbCreditsObject credit = generateCreditsByClient(ndbRuleClient8);
        credit.comment = getRandomNbdComment().getDisplayName();

        Allure.step("Get connections and abuse types");
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        CrmTbUserObject connectedUser = generateUserByClient(connectedClient);
        connectedUser.ibId = sameIb;
        connectedUser.email = sameEmail;
        cleanFraudTypeTableByClient(connectedClient.getUcid());
        cleanBoFraudTypesTableByUcid(connectedClient.getUcid());

        Allure.step("Linked active accounts not with same email AND NDB from the last 1 week? - true");
        Allure.step("Any under the same IB? - true");
        data.mtTbCreditsObjects.add(credit);
        data.crmTbAccountObjectConnections.add(generateCrmTbAccountData(connectedClient));
        ConnectionTableEntry connection = getConnection(data.clientHelper, connectedClient);
        connectedUser.email = sameEmail;
        data.connections.add(connection);
        data.connectedUsers.add(connectedUser);
        Allure.step("Add 49 trades with time less than 2 weeks");
        data.mt5DealsCoercedObjects.addAll(generateMt5DealsCoercedObject(data.clientHelper, 49, getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 13, 0, 0, 0)));

        Allure.step("Add NDB credits for connected client");
        data.mtTbCreditsObjects.addAll(generateNdbCredits(connectedClient, 5));

        return data;
    }

    private static DataHelper getNdbRuleExitEventEnd8Data() {
        Allure.step("Get client data");
        DataHelper data = getNdbRuleData(ndbRuleExitEventEnd8Client);

        Allure.step("Client and connected client have same email");
        Allure.step("Client and connected client have different IB");

        Allure.step("Setup client data in DB");
        Allure.step("set IB id that will be the same for connected user");
        CrmTbUserObject client = generateUserByClient(data.clientHelper);
        client.ibId = firstIb;
        client.email = sameEmail;
        data.crmTbUserObject = client;

        Allure.step("Get credits with ndb comment");
        MtTbCreditsObject credit = generateCreditsByClient(ndbRuleExitEventEnd8Client);
        credit.comment = getRandomNbdComment().getDisplayName();

        Allure.step("Get connections and abuse types");
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        CrmTbUserObject connectedUser = generateUserByClient(connectedClient);
        connectedUser.ibId = secondIb;
        connectedUser.email = sameEmail;

        Allure.step("Linked active accounts not with same email AND NDB from the last 1 week? - true");

        Allure.step("Any under the same IB? - false");

        data.mtTbCreditsObjects.add(credit);
        data.crmTbAccountObjectConnections.add(generateCrmTbAccountData(connectedClient));
        ConnectionTableEntry connection = getConnection(data.clientHelper, connectedClient);
        connectedUser.email = sameEmail;
        data.connections.add(connection);
        data.connectedUsers.add(connectedUser);

        Allure.step("Add less than 50 trades with time less than 2 weeks");
        data.mt5DealsCoercedObjects.addAll(generateMt5DealsCoercedObject(data.clientHelper, 49, getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 13, 0, 0, 0)));

        Allure.step("Add NDB credits for connected client");
        data.mtTbCreditsObjects.addAll(generateNdbCredits(connectedClient, 5));

        Allure.step("Lexis registration score high? = false");
        data.lnSessionParsedObjectRegistration.setRiskRating("low");

        return data;
    }

    private static DataHelper getNdbRuleExitEventEnd9Data() {
        Allure.step("Get client data");
        DataHelper data = getNdbRuleData(ndbRuleExitEventEnd9Client);

        Allure.step("Client and connected client have same email");
        Allure.step("Client and connected client have different IB");
        Allure.step("Setup client data in DB");
        Allure.step("set IB id that will be the same for connected user");

        data.clientHelper.setIbId(firstIb);
        data.clientHelper.setEmail(sameEmail);

        Allure.step("Get credits with ndb comment");
        data.mtTbCreditsObjects = List.of(generateCreditsByClient(ndbRuleExitEventEnd9Client));
        data.mtTbCreditsObjects.getFirst().comment = getRandomNbdComment().getDisplayName();

        Allure.step("Get connections and abuse types");
        data.connectedClientHelpers = List.of(getRandomVantageClientAllFields());
        data.connectedUsers = List.of(generateUserByClient(data.connectedClientHelpers.getFirst()));
        data.connectedUsers.getFirst().ibId = secondIb;
        data.connectedUsers.getFirst().email = sameEmail;

        Allure.step("Linked active accounts not with same email AND NDB from the last 1 week? - true");

        Allure.step("Any under the same IB? - false");

        //data.crmTbAccountObjectConnections=List.of(generateCrmTbAccountData(data.connectedUsers.getFirst().));
//        ConnectionTableEntry connection = getConnection(data.clientHelper, connectedClient);
//        connectedUser.email = sameEmail;
        //data.connections.add(connection);
        //data.connectedUsers.add(connectedUser);

        Allure.step("Add less than 50 trades with time less than 2 weeks");
        data.mt5DealsCoercedObjects = (generateMt5DealsCoercedObject(data.clientHelper, 49, getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 13, 0, 0, 0)));

        Allure.step("Add NDB credits for connected client");
        //data.mtTbCreditsObjects= (generateNdbCredits(connectedClient, 5));

        Allure.step("Lexis registration score high? = true");
        data.lnSessionParsedObjectRegistration = generateLexisNexisDataByClient(data.clientHelper);
        data.lnSessionParsedObjectRegistration.setRiskRating("high");

        return data;
    }

    public static Map<String, DataHelper> setupNdbRuleData() throws Exception {
        startSshTunnel();
        Map<String, DataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("1", getNdbRuleExitEventEnd1Data());
        map.put("2", getNdbRuleExitEventEnd2Data());
        map.put("3", getNdbRuleExitEventEnd3Data());
//        map.put("4", getNdbRuleExitEventEnd4Data());
//        map.put("42", getNdbRuleExitEventEnd42Data());
//        map.put("5", getNdbRuleExitEventEnd5Data());
//        map.put("6", getNdbRuleExitEventEnd6Data());
//        map.put("7", getNdbRuleExitEventEnd7Data());
//        map.put("8", getNdbRuleExitEventEnd8Data());
//        map.put("9", getNdbRuleExitEventEnd9Data());

        // Loop through the list with data and insert all the data into the according tables
        setupData(map);
        return map;
    }
}
