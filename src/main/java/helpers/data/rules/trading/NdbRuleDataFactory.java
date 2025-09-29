package helpers.data.rules.trading;

import business_objects.db.clickhouse.client_fraud_types.ClientFraudTypes;
import business_objects.db.clickhouse.data_science_test.connection_table.ConnectionTableEntry;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.mt_tb_credits.MtTbCreditsObject;
import business_objects.kafka.crm_events.EgWithdrawalEvent;
import generator.annotations.RuleTestData;
import helpers.data.ClientHelper;
import helpers.data.enums.DateTimeFormat;
import helpers.data.enums.FraudTypeOld;
import helpers.data.DataHelper;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;

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
import static helpers.data.enums.NbdComment.getRandomNbdComment;
import static helpers.data.DataHelper.setupData;
import static helpers.database.CleanTableHelper.cleanBoFraudTypesTableByUcid;
import static helpers.database.CleanTableHelper.cleanFraudTypeTableByClient;
import static helpers.database.DbHelper.startSshTunnel;
import static utils.Constants.*;
import static utils.Utils.*;

@RuleTestData("no-deposit-bonus")
public class NdbRuleDataFactory {

    private static final ClientHelper ndbRuleExitEventEnd1Client = getRandomVantageClientNoCpaIbRef();
    private static final ClientHelper ndbRuleExitEventEnd2Client = getRandomVantageClientNoCpaIbRef();
    private static final ClientHelper ndbRuleExitEventEnd3Client = getRandomVantageClientNoCpaIbRef();
    private static final ClientHelper ndbRuleExitEventEnd5Client = getRandomVantageClientNoCpaIbRef();
    private static final ClientHelper ndbRuleExitEventEnd6Client = getRandomVantageClientNoCpaIbRef();
    private static final ClientHelper ndbRuleExitEventEnd4_1Client = getRandomVantageClientNoCpaIbRef();
    private static final ClientHelper ndbRuleExitEventEnd4_2Client = getRandomVantageClientNoCpaIbRef();
    private static final ClientHelper ndbRuleExitEventEnd7Client = getRandomVantageClientNoCpaIbRef();
    private static final ClientHelper ndbRuleExitEventEnd8Client = getRandomVantageClientNoCpaIbRef();
    private static final ClientHelper ndbRuleExitEventEnd9Client = getRandomVantageClientNoCpaIbRef();

    private static final String sameEmail = "VGlhbRQlxOaLfl/CgrjL1CfZEIYLXEQL";
    private static final int sameIb = 6667;
    private static final int firstIb = 6668;
    private static final int secondIb = 6669;

    @Step("Create data for Mirror trading rule")
    private static DataHelper getNdbRuleData(ClientHelper client) {
        DataHelper ruleData = new DataHelper();
        CrmTbUserObject userObject = generateUserByClient(client);
        userObject.ibId = 1;
        ruleData.crmTbAccountObject = generateCrmTbAccountData(client);
        ruleData.withdrawalEvent = new EgWithdrawalEvent(getRandomUuidString(), Instant.now().toString(), getRandomIntPositive(), client.getUserId(), client.getTradingAccount(), client.getBrand(), "vfsc", "FASAPAY", 1, 1d, 1d, 1d, 1d, "555555**** **6666", 1, Instant.now().toString(), "", "", 1, "", 1d, 1, 1, "", 1, 1, 1d, 2, 1d, "egWithdrawal");
        ruleData.lnSessionParsedObject = generateLexisNexisDataByClient(client);
        ruleData.lnSessionParsedObject.setBrand(client.getBrand());
        ruleData.lnSessionParsedObject.setEventType("account_creation");
        ruleData.lnSessionParsedObject.setRiskRating("low");

        return ruleData;
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
        Allure.step("Get client data");
        DataHelper data = getNdbRuleData(ndbRuleExitEventEnd1Client);
        return data;
    }

    private static DataHelper getNdbRuleExitEventEnd2Data() {
        Allure.step("Get client data");
        DataHelper data = getNdbRuleData(ndbRuleExitEventEnd2Client);
        Allure.step("Get credits with ndb comment");
        MtTbCreditsObject credit = generateCreditsByClient(ndbRuleExitEventEnd2Client);
        credit.comment = "Promo-NDB-Credit In";

        Allure.step("Add 50 trades");
        data.mt5DealsCoercedObjects.addAll(generateMt5DealsCoercedObject(data.clientHelper, 50, getCurrentTimestampDbFormat()));


        Allure.step("Add data");
        data.mtTbCreditsObjects.add(credit);
        return data;
    }

    private static DataHelper getNdbRuleExitEventEnd3Data() {
        Allure.step("Get client data");
        DataHelper data = getNdbRuleData(ndbRuleExitEventEnd3Client);
        Allure.step("Get credits with ndb comment");
        MtTbCreditsObject credit = generateCreditsByClient(ndbRuleExitEventEnd3Client);
        credit.comment = "Promo-NDB-Credit In";
        Allure.step("Add 49 trades with time more than 2 weeks");
        data.mt5DealsCoercedObjects.addAll(generateMt5DealsCoercedObject(data.clientHelper, 49, getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 14, 0, 1)));


        Allure.step("Add data");
        data.mtTbCreditsObjects.add(credit);
        return data;
    }

    private static DataHelper getNdbRuleExitEventEnd41Data() {
        Allure.step("Get client data");
        DataHelper data = getNdbRuleData(ndbRuleExitEventEnd4_1Client);
        Allure.step("Get credits with ndb comment");
        MtTbCreditsObject credit = generateCreditsByClient(ndbRuleExitEventEnd4_1Client);
        credit.comment = "Credit In - No Deposit Bonus";
        Allure.step("Get connections and abuse types");
        Allure.step("Linked other fraud cases");
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        data.connections.add(getConnection(data.clientHelper, connectedClient));
        ClientFraudTypes clientFraudTypes = new ClientFraudTypes(
                connectedClient.getUcid(), FraudTypeOld.LOSS_VOUCHER_ABUSE.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat()
        );
        Allure.step("Add data");
        data.mtTbCreditsObjects.add(credit);
        data.clientFraudTypes.add(clientFraudTypes);
        Allure.step("Add 49 trades with time less than 2 weeks");
        data.mt5DealsCoercedObjects.addAll(generateMt5DealsCoercedObject(data.clientHelper, 49, getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 13, 0, 0, 0)));
        return data;
    }

    private static DataHelper getNdbRuleExitEventEnd42Data() {
        Allure.step("Get client data");
        DataHelper data = getNdbRuleData(ndbRuleExitEventEnd4_2Client);
        Allure.step("Get credits with ndb comment");
        MtTbCreditsObject credit = generateCreditsByClient(ndbRuleExitEventEnd4_2Client);
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
        DataHelper data = getNdbRuleData(ndbRuleExitEventEnd5Client);
        Allure.step("Get credits with ndb comment");
        MtTbCreditsObject credit = generateCreditsByClient(ndbRuleExitEventEnd5Client);
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
        DataHelper data = getNdbRuleData(ndbRuleExitEventEnd6Client);
        Allure.step("Get credits with ndb comment");
        MtTbCreditsObject credit = generateCreditsByClient(ndbRuleExitEventEnd6Client);
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
        DataHelper data = getNdbRuleData(ndbRuleExitEventEnd7Client);

        Allure.step("Setup client data in DB");
        Allure.step("set IB id that will be the same for connected user");
        CrmTbUserObject client = generateUserByClient(data.clientHelper);
        client.ibId = sameIb;
        client.email = sameEmail;
        data.crmTbUserObject = client;

        Allure.step("Get credits with ndb comment");
        MtTbCreditsObject credit = generateCreditsByClient(ndbRuleExitEventEnd7Client);
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
        CrmTbUserObject client = generateUserByClient(data.clientHelper);
        client.ibId = firstIb;
        client.email = sameEmail;
        data.crmTbUserObject = client;

        Allure.step("Get credits with ndb comment");
        MtTbCreditsObject credit = generateCreditsByClient(ndbRuleExitEventEnd9Client);
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


        Allure.step("Lexis registration score high? = true");
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
        map.put("41", getNdbRuleExitEventEnd41Data());
        map.put("42", getNdbRuleExitEventEnd42Data());
        map.put("5", getNdbRuleExitEventEnd5Data());
        map.put("6", getNdbRuleExitEventEnd6Data());
        map.put("7", getNdbRuleExitEventEnd7Data());
        map.put("8", getNdbRuleExitEventEnd8Data());
        map.put("9", getNdbRuleExitEventEnd9Data());

        // Loop through the list with data and insert all the data into the according tables
        setupData(map);
        return map;
    }
}
