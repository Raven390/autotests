package helpers.data.rules.mirrorTradingRule;

import businessObjects.api.mitigationService.PostRestrictionRequestBody;
import businessObjects.api.mitigationService.PostRestrictionResponse;
import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;
import businessObjects.db.clickhouse.csTbConnectionTableV2.ConnectionTableEntry;
import businessObjects.db.clickhouse.lnSessionParsedTable.LnSessionParsedObject;
import helpers.data.ClientHelper;
import helpers.data.rules.registrationRule.RegistrationRuleDataFactory;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;

import static businessObjects.api.clickhouseApiService.getClient.GetClientRequest.objectMapper;
import static businessObjects.api.mitigationService.MitigationServiceRequest.postRestriction;
import static businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObjectFactory.generateUserByClient;
import static businessObjects.db.clickhouse.lnSessionParsedTable.LnSessionParsedObjectFactory.generateLexisNexisDataForUserId;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static utils.Utils.getCurrentTimestampDbFormat;
import static utils.Utils.getRandomIntPositive;

public class MirrorTradingRuleDataFactory {
    private static final ClientHelper mirrorTradingRuleExitEventEnd1_1Client = getRandomVantageClientAllFields();
    private static final ClientHelper mirrorTradingRuleExitEventEnd1_2Client = getRandomVantageClientAllFields();
    private static final ClientHelper mirrorTradingRuleExitEventEnd7_1Client = getRandomVantageClientAllFields();
    private static final ClientHelper mirrorTradingRuleExitEventEnd7_2Client = getRandomVantageClientAllFields();
    private static final ClientHelper mirrorTradingRuleExitEventEnd7_3Client = getRandomVantageClientAllFields();
    private static final ClientHelper mirrorTradingRuleExitEventEnd7_4Client = getRandomVantageClientAllFields();
    private static final ClientHelper mirrorTradingRuleExitEventEnd7_5Client = getRandomVantageClientAllFields();
    private static final ClientHelper mirrorTradingRuleExitEventEnd7_6Client = getRandomVantageClientAllFields();
    private static final ClientHelper mirrorTradingRuleExitEventEnd6_1Client = getRandomVantageClientAllFields();
    private static final ClientHelper mirrorTradingRuleExitEventEnd5_1Client = getRandomVantageClientAllFields();
    private static final ClientHelper mirrorTradingRuleExitEventEnd5_2Client = getRandomVantageClientAllFields();
    private static final ClientHelper mirrorTradingRuleExitEventEnd4_1Client = getRandomVantageClientAllFields();
    private static final ClientHelper mirrorTradingRuleExitEventEnd4_2Client = getRandomVantageClientAllFields();
    private static final ClientHelper mirrorTradingRuleExitEventEnd3_1Client = getRandomVantageClientAllFields();
    private static final ClientHelper mirrorTradingRuleExitEventEnd3_2Client = getRandomVantageClientAllFields();
    private static final ClientHelper mirrorTradingRuleExitEventEnd2_1Client = getRandomVantageClientAllFields();

    @Step("Create data for Mirror trading rule")
    public static MirrorTradingRuleData getMirrorTradingRuleData(ClientHelper client){
        CrmTbUserObject userObject = generateUserByClient(client);
        userObject.phoneNum = client.getPhoneNumber();
        userObject.email = client.getEmail();
        userObject.countryCode = client.getCountryCode();
        LnSessionParsedObject lexisNexisObject = generateLexisNexisDataForUserId(client.getUuid(), client.getUserId(), getRandomIntPositive());
        lexisNexisObject.brand = client.getBrand();
        lexisNexisObject.eventType = "account_creation";
        lexisNexisObject.userId = client.getUserId();
        lexisNexisObject.proxyIp = client.getIpAddress();
        lexisNexisObject.trueIpGeo = client.getCountryCode();
        lexisNexisObject.policyScore = -49;
        return new MirrorTradingRuleData(client, userObject, lexisNexisObject, new ArrayList<>(), new ArrayList<>());
    }

    public static class ConnectionAndConnectedUser {
        public ConnectionTableEntry connectionTableEntry;
        public CrmTbUserObject crmTbUserObject;

        public ConnectionAndConnectedUser(ConnectionTableEntry connectionTableEntry, CrmTbUserObject crmTbUserObject) {
            this.connectionTableEntry = connectionTableEntry;
            this.crmTbUserObject = crmTbUserObject;
        }
    }

    public static RegistrationRuleDataFactory.ConnectionAndConnectedUser getConnectionAndConnectedUser(ClientHelper fromClient, ClientHelper toClient) {
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
        connectedCrmTbUserObject.phoneNum = toClient.getPhoneNumber();
        connectedCrmTbUserObject.email = toClient.getEmail();
        connectedCrmTbUserObject.countryCode = toClient.getCountryCode();
        return new RegistrationRuleDataFactory.ConnectionAndConnectedUser(connectionTableEntry, connectedCrmTbUserObject);
    }

    public static void getMirrorTradingRuleExitEventEnd1_1Data() {
        Allure.step("Client has no previous restrictions");
        Allure.step("Client has no connected account");
        Allure.step("Account has a credit");
        Allure.step("CreditEquityRatio > 0.7");
        Allure.step("Client use payment method from a gray list");
        Allure.step("Sum of abuse score > 4");
        Allure.step("RiskFreeRevenueRatio > 0.5");
        Allure.step("TradingOnNewsPeriods is False");
        Allure.step("Dummy trades is False");
        Allure.step("count(tradesWithStopouts)/count(trades) > 0.8 is False");
        Allure.step("count(balanceOrdersWithTypeWO) > 0 is True");
        Allure.step("SUM(mirrorAccountsByTradesClient.Volime)/SUM(mirrorAccountsByTradesDoppelganger) > 0.9 is True");
        Allure.step("Set restriction");
        Allure.step("Send alert");
    }

    public static void getMirrorTradingRuleExitEventEnd1_2Data() {
        Allure.step("Client has no previous restrictions");
        Allure.step("Client has connected account and connected account has no bonuses");
        Allure.step("Account has a credit");
        Allure.step("CreditEquityRatio > 0.7");
        Allure.step("Client use payment method from a gray list");
        Allure.step("Sum of abuse score > 4");
        Allure.step("RiskFreeRevenueRatio > 0.5");
        Allure.step("TradingOnNewsPeriods is False");
        Allure.step("Dummy trades is False");
        Allure.step("count(tradesWithStopouts)/count(trades) > 0.8 is False");
        Allure.step("count(balanceOrdersWithTypeWO) > 0 is True");
        Allure.step("SUM(mirrorAccountsByTradesClient.Volime)/SUM(mirrorAccountsByTradesDoppelganger) > 0.9 is True");
        Allure.step("Set restriction");
        Allure.step("Send alert");
    }

    public static void getMirrorTradingRuleExitEventEnd7_1Data() {
        Allure.step("Client has no previous restrictions");
        Allure.step("Client has no connected account (or have, bu connected account has no bonuses)");
        Allure.step("Account has a credit");
        Allure.step("CreditEquityRatio > 0.7");
        Allure.step("Client use payment method from a gray list");
        Allure.step("Sum of abuse score > 4");
        Allure.step("RiskFreeRevenueRatio > 0.5");
        Allure.step("TradingOnNewsPeriods is False");
        Allure.step("Dummy trades is False");
        Allure.step("count(tradesWithStopouts)/count(trades) > 0.8 is False");
        Allure.step("count(balanceOrdersWithTypeWO) > 0 is True");
        Allure.step("SUM(mirrorAccountsByTradesClient.Volime)/SUM(mirrorAccountsByTradesDoppelganger) > 0.9 is False");
        Allure.step("Set restriction");
        Allure.step("Send alert");
    }

    public static void getMirrorTradingRuleExitEventEnd7_2Data() {
        Allure.step("Client has no previous restrictions");
        Allure.step("Client has no connected account (or have, bu connected account has no bonuses)");
        Allure.step("Account has a credit");
        Allure.step("CreditEquityRatio > 0.7");
        Allure.step("Client use payment method from a gray list");
        Allure.step("Sum of abuse score > 4");
        Allure.step("RiskFreeRevenueRatio > 0.5");
        Allure.step("TradingOnNewsPeriods is True");
        Allure.step("SUM(mirrorAccountsByTradesClient.Volime)/SUM(mirrorAccountsByTradesDoppelganger) > 0.9 is False");
        Allure.step("Set restriction");
        Allure.step("Send alert");
    }

    public static void getMirrorTradingRuleExitEventEnd7_3Data() {
        Allure.step("Client has no previous restrictions");
        Allure.step("Client has no connected account (or have, bu connected account has no bonuses)");
        Allure.step("Account has a credit");
        Allure.step("CreditEquityRatio > 0.7");
        Allure.step("Client use payment method from a gray list");
        Allure.step("Sum of abuse score > 4");
        Allure.step("RiskFreeRevenueRatio > 0.5");
        Allure.step("TradingOnNewsPeriods is False");
        Allure.step("Dummy trades is True");
        Allure.step("SUM(mirrorAccountsByTradesClient.Volime)/SUM(mirrorAccountsByTradesDoppelganger) > 0.9 is False");
        Allure.step("Set restriction");
        Allure.step("Send alert");
    }

    public static void getMirrorTradingRuleExitEventEnd7_4Data() {
        Allure.step("Client has no previous restrictions");
        Allure.step("Client has no connected account (or have, bu connected account has no bonuses)");
        Allure.step("Account has a credit");
        Allure.step("CreditEquityRatio > 0.7");
        Allure.step("Client use payment method from a gray list");
        Allure.step("Sum of abuse score > 4");
        Allure.step("RiskFreeRevenueRatio > 0.5");
        Allure.step("TradingOnNewsPeriods is False");
        Allure.step("Dummy trades is False");
        Allure.step("count(tradesWithStopouts)/count(trades) > 0.8 is True");
        Allure.step("SUM(mirrorAccountsByTradesClient.Volime)/SUM(mirrorAccountsByTradesDoppelganger) > 0.9 is False");
        Allure.step("Set restriction");
        Allure.step("Send alert");
    }

    public static void getMirrorTradingRuleExitEventEnd7_5Data() {
        Allure.step("Client has no previous restrictions");
        Allure.step("Client has no connected account (or have, bu connected account has no bonuses)");
        Allure.step("Account has a credit");
        Allure.step("CreditEquityRatio > 0.7");
        Allure.step("Client use payment method from a gray list");
        Allure.step("Sum of abuse score > 4");
        Allure.step("RiskFreeRevenueRatio > 0.5");
        Allure.step("TradingOnNewsPeriods is False");
        Allure.step("Dummy trades is False");
        Allure.step("count(tradesWithStopouts)/count(trades) > 0.8 is False");
        Allure.step("count(balanceOrdersWithTypeWO) > 0 is False");
        Allure.step("Set restriction");
        Allure.step("Send alert");
    }

    public static void getMirrorTradingRuleExitEventEnd6_1Data() {
        Allure.step("Client has no previous restrictions");
        Allure.step("Client has no connected account (or have, bu connected account has no bonuses)");
        Allure.step("Account has a credit");
        Allure.step("CreditEquityRatio > 0.7");
        Allure.step("Client use payment method from a gray list");
        Allure.step("Sum of abuse score > 4");
        Allure.step("RiskFreeRevenueRatio > 0.5 is False");
        Allure.step("Exit without alert");
    }

    public static void getMirrorTradingRuleExitEventEnd5_1Data() {
        Allure.step("Client has no previous restrictions");
        Allure.step("Client has no connected account (or have, bu connected account has no bonuses)");
        Allure.step("Account has a credit");
        Allure.step("CreditEquityRatio > 0.7");
        Allure.step("Client use payment method from a gray list");
        Allure.step("Payment method is on the gray list. + 1 abuse score");
        Allure.step("tdBonus.amount/ ftdDeposit.amount >= 0.3. + 1 abuse score");
        Allure.step("trades.groupBySymbol < 5. + 1 abuse score");
        Allure.step("Registration country != last login login country. + 1 abuse score");
        Allure.step("Sum of abuse score > 4 is False");
        Allure.step("Exit without alert");
    }

    public static void getMirrorTradingRuleExitEventEnd5_2Data() {
        Allure.step("Client has no previous restrictions");
        Allure.step("Client has no connected account (or have, bu connected account has no bonuses)");
        Allure.step("Account has a credit");
        Allure.step("CreditEquityRatio > 0.7");
        Allure.step("Client use payment method from a gray list");
        Allure.step("lexisNexis.riskRating in ('high', 'medium'). + 2 abuse score");
        Allure.step("now() -client.dateRegistration < 168h. + 1 abuse score");
        Allure.step("Sum of abuse score > 4 is False");
        Allure.step("Exit without alert");
    }

    public static void getMirrorTradingRuleExitEventEnd4_1Data() {
        Allure.step("Client has no previous restrictions");
        Allure.step("Client has no connected account (or have, bu connected account has no bonuses)");
        Allure.step("Account has a credit is False");
        Allure.step("Exit without alert");
    }

    public static void getMirrorTradingRuleExitEventEnd4_2Data() {
        Allure.step("Client has no previous restrictions");
        Allure.step("Client has no connected account (or have, bu connected account has no bonuses)");
        Allure.step("Account has a credit");
        Allure.step("CreditEquityRatio > 0.7 is False");
        Allure.step("Exit without alert");
    }

    public static void getMirrorTradingRuleExitEventEnd3_1Data() {
        Allure.step("Client has no previous restrictions");
        Allure.step("Client has mirror trading abuse connected account");
        Allure.step("Set restriction");
        Allure.step("Send alert");
    }

    public static MirrorTradingRuleData getMirrorTradingRuleExitEventEnd3_2Data() {
        Allure.step("Create user");
        MirrorTradingRuleData data = getMirrorTradingRuleData(mirrorTradingRuleExitEventEnd3_2Client);
        Allure.step("Client has no previous restrictions");
        Allure.step("Client doesn't has mirror trading abuse connected account");
        Allure.step("Client has connected account with bonuses");
        Allure.step("Set restriction");
        Allure.step("Send alert");
        return data;
    }

    public static MirrorTradingRuleData getMirrorTradingRuleExitEventEnd2_1Data() throws IOException {
        Allure.step("Create user");
            MirrorTradingRuleData data = getMirrorTradingRuleData(mirrorTradingRuleExitEventEnd2_1Client);
        Allure.step("Client has previous restrictions");
            PostRestrictionRequestBody postRestrictionRequestBody = new PostRestrictionRequestBody(
                     data.clientHelper.getUcid(),
                     "05",
                     "GENERAL",
                     null,
                     null,
                     "Integration test",
                     new PostRestrictionRequestBody.UpdatedBy("string", "string")
             );

             Response response = postRestriction(postRestrictionRequestBody);
             PostRestrictionResponse restrictionBody = objectMapper.readValue(
                     response.body().string(),
                     PostRestrictionResponse.class
             );
             System.out.println(restrictionBody);
             System.out.println(response.code());

            return data;
    }
}
