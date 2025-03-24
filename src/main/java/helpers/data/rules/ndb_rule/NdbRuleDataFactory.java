package helpers.data.rules.ndb_rule;

import business_objects.db.clickhouse.bo_client_fraud_types.BoClientFraudTypesObject;
import business_objects.db.clickhouse.connection_table.ConnectionTableEntry;
import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.ln_session_parsed.LnSessionParsedObject;
import business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedObject;
import business_objects.db.clickhouse.mt_tb_credits.MtTbCreditsObject;
import business_objects.kafka.crm_events.WithdrawalEvent;
import generator.annotations.RuleTestData;
import helpers.data.ClientHelper;
import helpers.data.enums.FraudType;
import helpers.data.rules.RuleDataHelper;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;

import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountData;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.ln_session_parsed.LnSessionParsedObjectFactory.generateLexisNexisDataByClient;
import static business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedFactory.generateTradeByClient;
import static business_objects.db.clickhouse.mt_tb_credits.MtTbCreditsObjectFactory.generateCreditsByClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.ClientFactory.getRandomVantageClientNoCpaIbRef;
import static helpers.data.rules.RuleDataHelper.deleteRuleData;
import static helpers.data.rules.RuleDataHelper.setupRuleData;
import static helpers.database.DbHelper.startSshTunnel;
import static utils.Constants.*;
import static utils.Utils.*;

@RuleTestData("no-deposit-bonus")
public class NdbRuleDataFactory {

    private static final ClientHelper ndbRuleExitEventEnd1Client = getRandomVantageClientNoCpaIbRef();
    private static final ClientHelper ndbRuleExitEventEnd2Client = getRandomVantageClientNoCpaIbRef();
    private static final ClientHelper ndbRuleExitEventEnd3Client = getRandomVantageClientNoCpaIbRef();
    private static final ClientHelper ndbRuleExitEventEnd4_1Client = getRandomVantageClientNoCpaIbRef();
    private static final ClientHelper ndbRuleExitEventEnd4_2Client = getRandomVantageClientNoCpaIbRef();
    private static final ClientHelper ndbRuleExitEventEnd4_3Client = getRandomVantageClientNoCpaIbRef();
    private static final ClientHelper ndbRuleExitEventEnd5Client = getRandomVantageClientNoCpaIbRef();
    private static final ClientHelper ndbRuleExitEventEnd6Client = getRandomVantageClientNoCpaIbRef();

    @Step("Create data for Mirror trading rule")
    private static RuleDataHelper getNdbRuleData(ClientHelper client) {
        CrmTbUserObject userObject = generateUserByClient(client);
        userObject.ibId = 1;
        CrmTbAccountObject crmTbAccountObject = generateCrmTbAccountData(client);
        WithdrawalEvent withdrawalEvent = new WithdrawalEvent(getRandomUuidString(), Instant.now().toString(), getRandomIntPositive(), client.getUserId(), client.getTradingAccount(), client.getBrand(), "vfsc", "FASAPAY", 1, 1d, 1d, 1d, 1d, "555555**** **6666", 1, Instant.now().toString(), "", "", 1, "", 1d, 1, 1, "", 1, 1, 1d, 2, 1d, "withdrawal");
        LnSessionParsedObject lexisNexisObject = generateLexisNexisDataByClient(client);
        lexisNexisObject.setBrand(client.getBrand());
        lexisNexisObject.setEventType("account_creation");
        lexisNexisObject.setRiskRating("low");
        return new RuleDataHelper(client, userObject, lexisNexisObject, null, new ArrayList<>(), new ArrayList<>(), withdrawalEvent, null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), crmTbAccountObject, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), null, null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), null, new ArrayList<>(), new ArrayList<>());
    }

    private static ConnectionTableEntry getConnection(ClientHelper fromClient, ClientHelper toClient) {
        return new ConnectionTableEntry(
                fromClient.getUcid(), toClient.getUcid(), CONNECTION_TYPE_SAME_IDENTITY, 1d, List.of(
                        new ConnectionTableEntry.ConnectionInfo(
                                CONNECTION_ATTRIBUTE_NAME_PAYOUT_ID, CONNECTION_SEARCH_DATA_CARD_NUMBER, CONNECTION_SEARCH_DATA_CARD_NUMBER, CONNECTION_TYPE_RELATION_TYPE_EXACT
                        )), getCurrentTimestampDbFormat()
        );
    }

    public static RuleDataHelper getNdbRuleExitEventEnd1Data() {
        Allure.step("Get client data");
        RuleDataHelper data = getNdbRuleData(ndbRuleExitEventEnd1Client);
        return data;
    }

    public static RuleDataHelper getNdbRuleExitEventEnd2Data() {
        Allure.step("Get client data");
        RuleDataHelper data = getNdbRuleData(ndbRuleExitEventEnd2Client);
        Allure.step("Get credits with ndb comment");
        MtTbCreditsObject credit = generateCreditsByClient(ndbRuleExitEventEnd2Client);
        credit.comment = "Promo-NDB-Credit In";
        Allure.step("Get connections and abuse types");
        Allure.step("Linked other fraud cases");
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        data.connections.add(getConnection(data.clientHelper, connectedClient));
        BoClientFraudTypesObject boClientFraudTypesObject = new BoClientFraudTypesObject(
                connectedClient.getUcid(), FraudType.GAP_TRADING.getFraudTypeId(), FraudType.GAP_TRADING.getKey()
        );
        Allure.step("Add data");
        data.mtTbCreditsObjects.add(credit);
        data.clientFraudTypes.add(boClientFraudTypesObject);
        return data;
    }

    public static RuleDataHelper getNdbRuleExitEventEnd3Data() {
        Allure.step("Get client data");
        RuleDataHelper data = getNdbRuleData(ndbRuleExitEventEnd3Client);
        Allure.step("Get credits with ndb comment");
        MtTbCreditsObject credit = generateCreditsByClient(ndbRuleExitEventEnd3Client);
        credit.comment = "Promo-NDB-Credit In";
        Allure.step("Linked active accounts not with same email AND NDB from the last 1 week?");
        Allure.step("Add data");
        data.mtTbCreditsObjects.add(credit);
        return data;
    }

    public static RuleDataHelper getNdbRuleExitEventEnd4_1Data() {
        Allure.step("Get client data");
        RuleDataHelper data = getNdbRuleData(ndbRuleExitEventEnd4_1Client);
        Allure.step("Get credits with ndb comment");
        MtTbCreditsObject credit = generateCreditsByClient(ndbRuleExitEventEnd4_1Client);
        credit.comment = "Credit In - No Deposit Bonus";
        Allure.step("Get connections and abuse types");
        Allure.step("Linked other fraud cases");
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        data.connections.add(getConnection(data.clientHelper, connectedClient));
        BoClientFraudTypesObject boClientFraudTypesObject = new BoClientFraudTypesObject(
                connectedClient.getUcid(), FraudType.LOSS_VOUCHER_ABUSE.getFraudTypeId(), FraudType.LOSS_VOUCHER_ABUSE.getKey()
        );
        Allure.step("Add data");
        data.mtTbCreditsObjects.add(credit);
        data.clientFraudTypes.add(boClientFraudTypesObject);
        return data;
    }

    public static RuleDataHelper getNdbRuleExitEventEnd4_2Data() {
        Allure.step("Get client data");
        RuleDataHelper data = getNdbRuleData(ndbRuleExitEventEnd4_2Client);
        Allure.step("Get credits with ndb comment");
        MtTbCreditsObject credit = generateCreditsByClient(ndbRuleExitEventEnd4_2Client);
        credit.comment = "credit in-JP NDB";
        Allure.step("Get connections and abuse types");
        Allure.step("Linked other fraud cases");
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        data.connections.add(getConnection(data.clientHelper, connectedClient));
        BoClientFraudTypesObject boClientFraudTypesObject = new BoClientFraudTypesObject(
                connectedClient.getUcid(), FraudType.HEDGING.getFraudTypeId(), FraudType.HEDGING.getKey()
        );
        Allure.step("Add data");
        data.mtTbCreditsObjects.add(credit);
        data.clientFraudTypes.add(boClientFraudTypesObject);
        return data;
    }

    public static RuleDataHelper getNdbRuleExitEventEnd4_3Data() {
        Allure.step("Get client data");
        RuleDataHelper data = getNdbRuleData(ndbRuleExitEventEnd4_3Client);

        Allure.step("Get credits with ndb comment");
        MtTbCreditsObject credit = generateCreditsByClient(ndbRuleExitEventEnd4_3Client);
        credit.comment = "Credit in - APAC No Dep. Bonus";

        Allure.step("Get connections and abuse types");
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        MtTbCreditsObject credit2 = generateCreditsByClient(connectedClient);
        credit2.internalComment = "Credit in - JAP No Dep. Bonus";
        credit2.comment = "Credit in - JAP No Dep. Bonus";
        CrmTbUserObject connectedUser = generateUserByClient(connectedClient);
        connectedUser.ibId = 1;

        Allure.step("Linked active accounts not with same email AND NDB from the last 1 week? - true");
        Allure.step("Any under the same IB? - true");
        data.mtTbCreditsObjects.add(credit);
        data.mtTbCreditsObjects.add(credit2);
        data.crmTbAccountObjectConnections.add(generateCrmTbAccountData(connectedClient));
        data.connections.add(getConnection(data.clientHelper, connectedClient));
        data.connectedUsers.add(connectedUser);

        System.out.println(data.clientHelper.getIbId());
        System.out.println(connectedClient.getIbId());
        return data;
    }

    public static RuleDataHelper getNdbRuleExitEventEnd5Data() {
        Allure.step("Get client data");
        RuleDataHelper data = getNdbRuleData(ndbRuleExitEventEnd5Client);
        Allure.step("Get credits with ndb comment");
        MtTbCreditsObject credit = generateCreditsByClient(ndbRuleExitEventEnd5Client);
        credit.comment = "Credit out - APAC No Dep. Bonus";

        Allure.step("Get connections and abuse types");
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        MtTbCreditsObject credit2 = generateCreditsByClient(connectedClient);
        credit2.internalComment = "credit in-PMT103 -NDB-JP";
        credit2.comment = "credit in-PMT103 -NDB-JP";
        CrmTbUserObject connectedUser = generateUserByClient(connectedClient);

        Allure.step("Linked active accounts not with same email AND NDB from the last 1 week? - true");
        Allure.step("Any under the same IB? - true");
        Allure.step("Lexis registration score high? = false");
        data.lnSessionParsedObjectRegistration.setRiskRating("low");

        data.mtTbCreditsObjects.add(credit);
        data.mtTbCreditsObjects.add(credit2);
        data.crmTbAccountObjectConnections.add(generateCrmTbAccountData(connectedClient));
        data.connections.add(getConnection(data.clientHelper, connectedClient));
        data.connections.getFirst().connectionScore = 0.76;
        data.connectedUsers.add(connectedUser);
        return data;
    }

    public static RuleDataHelper getNdbRuleExitEventEnd6Data() {
        Allure.step("Get client data");
        RuleDataHelper data = getNdbRuleData(ndbRuleExitEventEnd6Client);
        Allure.step("Get credits with ndb comment");
        MtTbCreditsObject credit = generateCreditsByClient(ndbRuleExitEventEnd6Client);
        //credit.comment = "credit in-JP NDB2406"; is not recognized as a proper comment
        credit.comment = "Promo-NDB-Credit In";
        Allure.step("Get connections and abuse types");
        ClientHelper connectedClient = getRandomVantageClientNoCpaIbRef();
        MtTbCreditsObject credit2 = generateCreditsByClient(connectedClient);
        credit2.internalComment = "Credit In - VN $30 NDB IBs";
        credit2.comment = "Credit In - VN $30 NDB IBs";
        CrmTbUserObject connectedUser = generateUserByClient(connectedClient);

        Mt5DealsCoercedObject trade = generateTradeByClient(data.clientHelper);

        Allure.step("Linked active accounts not with same email AND NDB from the last 1 week? - true");
        Allure.step("Any under the same IB? - false");
        Allure.step("Lexis registration score high? = true");
        data.lnSessionParsedObjectRegistration.setRiskRating("high");

        data.mtTbCreditsObjects.add(credit);
        data.mtTbCreditsObjects.add(credit2);
        data.crmTbAccountObjectConnections.add(generateCrmTbAccountData(connectedClient));
        data.connections.add(getConnection(data.clientHelper, connectedClient));
        data.connectedUsers.add(connectedUser);
        data.mt5DealsCoercedObjects.add(trade);
        return data;
    }

    public static Map<String, RuleDataHelper> setupNdbRuleData() throws ReflectiveOperationException,
            SQLException {
        startSshTunnel();
        Map<String, RuleDataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("1", getNdbRuleExitEventEnd1Data());
        map.put("2", getNdbRuleExitEventEnd2Data());
        map.put("3", getNdbRuleExitEventEnd3Data());
        map.put("4_1", getNdbRuleExitEventEnd4_1Data());
        map.put("4_2", getNdbRuleExitEventEnd4_2Data());
        map.put("4_3", getNdbRuleExitEventEnd4_3Data());
        map.put("5", getNdbRuleExitEventEnd5Data());
        map.put("6", getNdbRuleExitEventEnd6Data());

        // Loop through the list with data and insert all the data into the according tables
        setupRuleData(map);
        return map;
    }

    public static void deleteNdbRuleData(Map<String, RuleDataHelper> map) throws Exception {
        deleteRuleData(map);
    }
}
