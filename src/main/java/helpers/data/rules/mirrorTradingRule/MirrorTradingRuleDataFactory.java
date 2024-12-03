package helpers.data.rules.mirrorTradingRule;

import businessObjects.db.clickhouse.aggrCreditEquityRate.AggrCreditEquityRateObject;
import businessObjects.db.clickhouse.aggrMirrorAccountsByTrades.AggrMirrorAccountsByTradesObject;
import businessObjects.db.clickhouse.boClientFraudTypes.BoClientFraudTypesObject;
import businessObjects.db.clickhouse.crmTbBonusTable.CrmTbBonusObject;
import businessObjects.db.clickhouse.crmTbDepositTable.CrmTbDepositObject;
import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;
import businessObjects.db.clickhouse.csTbConnectionTableV3.ConnectionTableEntryV3;
import businessObjects.db.clickhouse.lnSessionParsedTable.LnSessionParsedObject;
import businessObjects.db.clickhouse.mtMt5DealsTable.Mt5DealsObject;
import businessObjects.db.clickhouse.mtTbUserTable.MtTbUserObject;
import businessObjects.kafka.mtEvents.CloseTradeMtEvent;
import helpers.data.ClientHelper;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;

import java.time.Instant;
import java.util.ArrayList;

import static businessObjects.db.clickhouse.aggrCreditEquityRate.AggrCreditEquityRateObjectFactory.generateCreditEquityRatioAccount;
import static businessObjects.db.clickhouse.aggrMirrorAccountsByTrades.AggrMirrorAccountsByTradesObjectFactory.generateMirrorTradesByAccount;
import static businessObjects.db.clickhouse.crmTbBonusTable.CrmTbBonusObjectFactory.generateBonusByClient;
import static businessObjects.db.clickhouse.crmTbDepositTable.CrmTbDepositObjectFactory.generateDepositByClient;
import static businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObjectFactory.generateUserByClient;
import static businessObjects.db.clickhouse.crmTbWithdrawalTable.CrmTbWithdrawalObjectFactory.generateWithdrawalByClient;
import static businessObjects.db.clickhouse.lnSessionParsedTable.LnSessionParsedObjectFactory.generateLexisNexisDataForUserId;
import static businessObjects.db.clickhouse.mtMt5DealsTable.Mt5DealsFactory.generateTradeByAccountServerId;
import static businessObjects.db.clickhouse.mtTbCreditsTable.MtTbCreditsObjectFactory.generateCreditsByClient;
import static businessObjects.db.clickhouse.mtTbUserTable.MtTbUserObjectFactory.generateMtTbUserData;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static utils.Utils.*;

public class MirrorTradingRuleDataFactory {
    private static final ClientHelper mirrorTradingRuleExitEventEnd2Client = getRandomVantageClientAllFields();
    private static final ClientHelper mirrorTradingRuleExitEventEnd3_1Client = getRandomVantageClientAllFields();
    private static final ClientHelper mirrorTradingRuleExitEventEnd3_2Client = getRandomVantageClientAllFields();
    private static final ClientHelper mirrorTradingRuleExitEventEnd4_1Client = getRandomVantageClientAllFields();
    private static final ClientHelper mirrorTradingRuleExitEventEnd4_2Client = getRandomVantageClientAllFields();
    private static final ClientHelper mirrorTradingRuleExitEventEnd5_1Client = getRandomVantageClientAllFields();
    private static final ClientHelper mirrorTradingRuleExitEventEnd5_2Client = getRandomVantageClientAllFields();
    private static final ClientHelper mirrorTradingRuleExitEventEnd6Client = getRandomVantageClientAllFields();
    private static final ClientHelper mirrorTradingRuleExitEventEnd7_1Client = getRandomVantageClientAllFields();
    private static final ClientHelper mirrorTradingRuleExitEventEnd7_2Client = getRandomVantageClientAllFields();
    private static final ClientHelper mirrorTradingRuleExitEventEnd7_3Client = getRandomVantageClientAllFields();
    private static final ClientHelper mirrorTradingRuleExitEventEnd7_4Client = getRandomVantageClientAllFields();
    private static final ClientHelper mirrorTradingRuleExitEventEnd7_5Client = getRandomVantageClientAllFields();
    private static final ClientHelper mirrorTradingRuleExitEventEnd1_1Client = getRandomVantageClientAllFields();
    private static final ClientHelper mirrorTradingRuleExitEventEnd1_2Client = getRandomVantageClientAllFields();

    @Step("Create data for Mirror trading rule")
    private static MirrorTradingRuleData getMirrorTradingRuleData(ClientHelper client) {
        CrmTbUserObject userObject = generateUserByClient(client);
        LnSessionParsedObject lexisNexisObjectRegistration = generateLexisNexisDataForUserId(client.getUuid(), client.getUserId(), getRandomIntPositive());
        lexisNexisObjectRegistration.brand = client.getBrand();
        lexisNexisObjectRegistration.eventType = "account_creation";
        lexisNexisObjectRegistration.userId = client.getUserId();
        lexisNexisObjectRegistration.riskRating = "low";
        LnSessionParsedObject lexisNexisObjectLogin = generateLexisNexisDataForUserId(client.getUuid(), client.getUserId(), getRandomIntPositive());
        lexisNexisObjectLogin.brand = client.getBrand();
        lexisNexisObjectLogin.eventType = "login";
        lexisNexisObjectLogin.userId = client.getUserId();
        lexisNexisObjectLogin.riskRating = "low";
        lexisNexisObjectLogin.trueIpGeo = "CY";
        MtTbUserObject mtTbUserObject = generateMtTbUserData(client.getUcid(), client.getTradingAccount(), client.getServerId());
        CloseTradeMtEvent closeTradeMtEvent = new CloseTradeMtEvent(
                getRandomUuidString(),
                Instant.now().toString(),
                getRandomIntPositive(),
                mtTbUserObject.account,
                100d,
                "USDEUR",
                mtTbUserObject.serverId,
                "closeTrade"
        );
        return new MirrorTradingRuleData(client, userObject, lexisNexisObjectRegistration, lexisNexisObjectLogin, new ArrayList<>(), new ArrayList<>(), closeTradeMtEvent, new ArrayList<>(), null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), null, null);
    }

    private static ConnectionTableEntryV3 getConnection(ClientHelper fromClient, ClientHelper toClient) {
        return new ConnectionTableEntryV3(
                fromClient.getUcid(),
                toClient.getUcid(),
                "Same Identity",
                1d,
                "{\"payout\": \"463344**** **5603\"}",
                getCurrentTimestampDbFormat());
    }

    public static MirrorTradingRuleData getMirrorTradingRuleExitEventEnd2Data() {
        Allure.step("Create user");
        MirrorTradingRuleData data = getMirrorTradingRuleData(mirrorTradingRuleExitEventEnd2Client);
        Allure.step("Client has previous restrictions");
        BoClientFraudTypesObject boClientFraudTypesObject = new BoClientFraudTypesObject(
                data.clientHelper.getUcid(),
                6,
                "HEDGING"
        );
        data.clientFraudTypes.add(boClientFraudTypesObject);
        return data;
    }

    public static MirrorTradingRuleData getMirrorTradingRuleExitEventEnd3_1Data() {
        Allure.step("Client has no previous restrictions");
        Allure.step("Client has mirror trading abuse connected account");
        Allure.step("Set restriction");
        Allure.step("Send alert");
        MirrorTradingRuleData data = getMirrorTradingRuleData(mirrorTradingRuleExitEventEnd3_1Client);
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        data.connections.add(getConnection(data.clientHelper, connectedClient));
        BoClientFraudTypesObject boClientFraudTypesObject = new BoClientFraudTypesObject(
                connectedClient.getUcid(),
                6,
                "HEDGING"
        );
        data.clientFraudTypes.add(boClientFraudTypesObject);
        return data;
    }

    public static MirrorTradingRuleData getMirrorTradingRuleExitEventEnd3_2Data() {
        Allure.step("Client has no previous restrictions");
        Allure.step("Client doesn't has mirror trading abuse connected account");
        Allure.step("Client has connected account with bonuses");
        Allure.step("Set restriction");
        Allure.step("Send alert");
        MirrorTradingRuleData data = getMirrorTradingRuleData(mirrorTradingRuleExitEventEnd3_2Client);
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        data.connections.add(getConnection(data.clientHelper, connectedClient));
        CrmTbBonusObject connectionBonus = generateBonusByClient(connectedClient);
        connectionBonus.regulator = "VFSC";
        data.crmTbBonusObjects.add(connectionBonus);
        return data;
    }

    public static MirrorTradingRuleData getMirrorTradingRuleExitEventEnd4_1Data() {
        Allure.step("Client has no previous restrictions");
        Allure.step("Client has no connected account (or have, bu connected account has no bonuses)");
        Allure.step("Account has a credit is False");
        Allure.step("Exit without alert");
        return getMirrorTradingRuleData(mirrorTradingRuleExitEventEnd4_1Client);
    }

    public static MirrorTradingRuleData getMirrorTradingRuleExitEventEnd4_2Data() {
        Allure.step("Client has no previous restrictions");
        Allure.step("Client has no connected account (or have, bu connected account has no bonuses)");
        Allure.step("Account has a credit");
        Allure.step("CreditEquityRatio > 0.7 is False");
        Allure.step("Exit without alert");
        MirrorTradingRuleData data = getMirrorTradingRuleData(mirrorTradingRuleExitEventEnd4_2Client);
        data.mtTbCreditsObjects.add(generateCreditsByClient(data.clientHelper));
        AggrCreditEquityRateObject creditEquityRate = generateCreditEquityRatioAccount(data.clientHelper);
        creditEquityRate.creditEquityRatio = 0.4d;
        data.aggrCreditEquityRate = creditEquityRate;
        return data;
    }

    public static MirrorTradingRuleData getMirrorTradingRuleExitEventEnd5_1Data() {
        Allure.step("Client has no previous restrictions");
        Allure.step("Client has no connected account (or have, bu connected account has no bonuses)");
        Allure.step("Account has a credit");
        Allure.step("CreditEquityRatio > 0.7");
        Allure.step("Client use payment method from a gray list");
        Allure.step("Payment method is on the gray list. + 1 abuse score");
        Allure.step("tdBonus.amount/ ftdDeposit.amount >= 0.3. + 1 abuse score");
        Allure.step("trades.groupBySymbol < 5. + 1 abuse score");
        Allure.step("now() - ftddeposit.createTime < 168h. + 1 abuse score");
        Allure.step("Sum of abuse score > 4 is False");
        Allure.step("Exit without alert");
        MirrorTradingRuleData data = getMirrorTradingRuleData(mirrorTradingRuleExitEventEnd5_1Client);
        data.mtTbCreditsObjects.add(generateCreditsByClient(data.clientHelper));
        data.aggrCreditEquityRate = generateCreditEquityRatioAccount(data.clientHelper);
        data.crmTbWithdrawalObjects.add(generateWithdrawalByClient(data.clientHelper));
        CrmTbDepositObject crmTbDepositObject = generateDepositByClient(data.clientHelper);
        crmTbDepositObject.paymentChannel = "DebitCard";
        data.crmTbDepositObjects.add(crmTbDepositObject);
        data.crmTbBonusObjects.add(generateBonusByClient(data.clientHelper));
        data.crmTbDepositObjects.add(generateDepositByClient(data.clientHelper));
        data.mt5DealsObjects.add(generateTradeByAccountServerId(data.clientHelper.getTradingAccount(), data.clientHelper.getServerId()));
        return data;
    }

    public static MirrorTradingRuleData getMirrorTradingRuleExitEventEnd5_2Data() {
        Allure.step("Client has no previous restrictions");
        Allure.step("Client has no connected account (or have, bu connected account has no bonuses)");
        Allure.step("Account has a credit");
        Allure.step("CreditEquityRatio > 0.7");
        Allure.step("lexisNexisReg.riskRating in ('high', 'medium'). + 2 abuse score");
        Allure.step("Registration country != last login login country. + 2 abuse score");
        Allure.step("Sum of abuse score > 4 is False");
        Allure.step("Exit without alert");
        MirrorTradingRuleData data = getMirrorTradingRuleData(mirrorTradingRuleExitEventEnd5_2Client);
        data.mtTbCreditsObjects.add(generateCreditsByClient(data.clientHelper));
        data.aggrCreditEquityRate = generateCreditEquityRatioAccount(data.clientHelper);
        data.lnSessionParsedObjectRegistration.riskRating = "medium";
        CrmTbDepositObject crmTbDepositObject = generateDepositByClient(data.clientHelper);
        crmTbDepositObject.createTime = data.crmTbUserObject.registrationDate;
        crmTbDepositObject.paymentChannel = "DebitCard";
        data.crmTbDepositObjects.add(crmTbDepositObject);
        data.lnSessionParsedObjectLogin.trueIpGeo = "US";
        return data;
    }

    public static MirrorTradingRuleData getMirrorTradingRuleExitEventEnd6Data() {
        Allure.step("Client has no previous restrictions");
        Allure.step("Client has no connected account (or have, bu connected account has no bonuses)");
        Allure.step("Account has a credit");
        Allure.step("CreditEquityRatio > 0.7");
        Allure.step("Client use payment method from a gray list");
        Allure.step("Sum of abuse score > 4");
        Allure.step("RiskFreeRevenueRatio > 0.5 is False");
        Allure.step("Exit without alert");
        MirrorTradingRuleData data = getMirrorTradingRuleData(mirrorTradingRuleExitEventEnd6Client);
        data.mtTbCreditsObjects.add(generateCreditsByClient(data.clientHelper));
        data.aggrCreditEquityRate = generateCreditEquityRatioAccount(data.clientHelper);
        data.lnSessionParsedObjectRegistration.riskRating = "medium";
        CrmTbDepositObject crmTbDepositObject = generateDepositByClient(data.clientHelper);
        crmTbDepositObject.createTime = data.crmTbUserObject.registrationDate;
        data.crmTbDepositObjects.add(crmTbDepositObject);
        data.lnSessionParsedObjectLogin.trueIpGeo = "US";
        // TODO add data for risk free revenue ratio > 0.5
        return data;
    }

    public static MirrorTradingRuleData getMirrorTradingRuleExitEventEnd1_1Data() {
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
        MirrorTradingRuleData data = getMirrorTradingRuleData(mirrorTradingRuleExitEventEnd1_1Client);
        data.mtTbCreditsObjects.add(generateCreditsByClient(data.clientHelper));
        data.aggrCreditEquityRate = generateCreditEquityRatioAccount(data.clientHelper);
        data.lnSessionParsedObjectRegistration.riskRating = "medium";
        CrmTbDepositObject crmTbDepositObject = generateDepositByClient(data.clientHelper);
        crmTbDepositObject.createTime = data.crmTbUserObject.registrationDate;
        data.crmTbDepositObjects.add(crmTbDepositObject);
        data.lnSessionParsedObjectLogin.trueIpGeo = "US";
        // TODO add data for risk free revenue ratio > 0.5
        // TODO add data for no trading on news periods?
        // TODO add data for no dummy trades?
        data.mt5DealsObjects.add(generateTradeByAccountServerId(data.clientHelper.getTradingAccount(), data.clientHelper.getServerId()));
        // TODO add balanceOrders with comment "WO"
        AggrMirrorAccountsByTradesObject mirrorAccountsByTrades = generateMirrorTradesByAccount(data.clientHelper);
        mirrorAccountsByTrades.requestVolumeInLots = 3d;
        data.aggrMirrorAccountsByTrades = mirrorAccountsByTrades;
        return data;
    }

    public static MirrorTradingRuleData getMirrorTradingRuleExitEventEnd1_2Data() {
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
        Allure.step("count(balanceOrdersWithTypeWO) > 0 is False");
        Allure.step("SUM(mirrorAccountsByTradesClient.Volime)/SUM(mirrorAccountsByTradesDoppelganger) > 0.9 is True");
        Allure.step("Set restriction");
        Allure.step("Send alert");
        MirrorTradingRuleData data = getMirrorTradingRuleData(mirrorTradingRuleExitEventEnd1_2Client);
        data.mtTbCreditsObjects.add(generateCreditsByClient(data.clientHelper));
        data.aggrCreditEquityRate = generateCreditEquityRatioAccount(data.clientHelper);
        data.lnSessionParsedObjectRegistration.riskRating = "medium";
        CrmTbDepositObject crmTbDepositObject = generateDepositByClient(data.clientHelper);
        crmTbDepositObject.createTime = data.crmTbUserObject.registrationDate;
        data.crmTbDepositObjects.add(crmTbDepositObject);
        data.lnSessionParsedObjectLogin.trueIpGeo = "US";
        // TODO add data for risk free revenue ratio > 0.5
        // TODO add data for trading on news periods?
        data.mt5DealsObjects.add(generateTradeByAccountServerId(data.clientHelper.getTradingAccount(), data.clientHelper.getServerId()));
        AggrMirrorAccountsByTradesObject mirrorAccountsByTrades = generateMirrorTradesByAccount(data.clientHelper);
        mirrorAccountsByTrades.requestVolumeInLots = 3d;
        data.aggrMirrorAccountsByTrades = mirrorAccountsByTrades;
        return data;
    }

    public static MirrorTradingRuleData getMirrorTradingRuleExitEventEnd7_1Data() {
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
        MirrorTradingRuleData data = getMirrorTradingRuleData(mirrorTradingRuleExitEventEnd7_1Client);
        data.mtTbCreditsObjects.add(generateCreditsByClient(data.clientHelper));
        data.aggrCreditEquityRate = generateCreditEquityRatioAccount(data.clientHelper);
        data.lnSessionParsedObjectRegistration.riskRating = "medium";
        CrmTbDepositObject crmTbDepositObject = generateDepositByClient(data.clientHelper);
        crmTbDepositObject.createTime = data.crmTbUserObject.registrationDate;
        data.crmTbDepositObjects.add(crmTbDepositObject);
        data.lnSessionParsedObjectLogin.trueIpGeo = "US";
        // TODO add data for risk free revenue ratio > 0.5
        // TODO add data for no trading on news periods?
        // TODO add data for no dummy trades?
        data.mt5DealsObjects.add(generateTradeByAccountServerId(data.clientHelper.getTradingAccount(), data.clientHelper.getServerId()));
        // TODO add balanceOrders with comment "WO"
        data.aggrMirrorAccountsByTrades = generateMirrorTradesByAccount(data.clientHelper);
        return data;
    }

    public static MirrorTradingRuleData getMirrorTradingRuleExitEventEnd7_2Data() {
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
        MirrorTradingRuleData data = getMirrorTradingRuleData(mirrorTradingRuleExitEventEnd7_2Client);
        data.mtTbCreditsObjects.add(generateCreditsByClient(data.clientHelper));
        data.aggrCreditEquityRate = generateCreditEquityRatioAccount(data.clientHelper);
        data.lnSessionParsedObjectRegistration.riskRating = "medium";
        CrmTbDepositObject crmTbDepositObject = generateDepositByClient(data.clientHelper);
        crmTbDepositObject.createTime = data.crmTbUserObject.registrationDate;
        data.crmTbDepositObjects.add(crmTbDepositObject);
        data.lnSessionParsedObjectLogin.trueIpGeo = "US";
        // TODO add data for risk free revenue ratio > 0.5
        // TODO add data for trading on news periods?
        data.aggrMirrorAccountsByTrades = generateMirrorTradesByAccount(data.clientHelper);
        return data;
    }

    public static MirrorTradingRuleData getMirrorTradingRuleExitEventEnd7_3Data() {
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
        MirrorTradingRuleData data = getMirrorTradingRuleData(mirrorTradingRuleExitEventEnd7_3Client);
        data.mtTbCreditsObjects.add(generateCreditsByClient(data.clientHelper));
        data.aggrCreditEquityRate = generateCreditEquityRatioAccount(data.clientHelper);
        data.lnSessionParsedObjectRegistration.riskRating = "medium";
        CrmTbDepositObject crmTbDepositObject = generateDepositByClient(data.clientHelper);
        crmTbDepositObject.createTime = data.crmTbUserObject.registrationDate;
        data.crmTbDepositObjects.add(crmTbDepositObject);
        data.lnSessionParsedObjectLogin.trueIpGeo = "US";
        // TODO add data for risk free revenue ratio > 0.5
        // TODO add data for no trading on news periods?
        // TODO add data for dummy trades?
        data.aggrMirrorAccountsByTrades = generateMirrorTradesByAccount(data.clientHelper);
        return data;
    }

    public static MirrorTradingRuleData getMirrorTradingRuleExitEventEnd7_4Data() {
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
        MirrorTradingRuleData data = getMirrorTradingRuleData(mirrorTradingRuleExitEventEnd7_4Client);
        data.mtTbCreditsObjects.add(generateCreditsByClient(data.clientHelper));
        data.aggrCreditEquityRate = generateCreditEquityRatioAccount(data.clientHelper);
        data.lnSessionParsedObjectRegistration.riskRating = "medium";
        CrmTbDepositObject crmTbDepositObject = generateDepositByClient(data.clientHelper);
        crmTbDepositObject.createTime = data.crmTbUserObject.registrationDate;
        data.crmTbDepositObjects.add(crmTbDepositObject);
        data.lnSessionParsedObjectLogin.trueIpGeo = "US";
        // TODO add data for risk free revenue ratio > 0.5
        // TODO add data for no trading on news periods?
        // TODO add data for no dummy trades?
        Mt5DealsObject mt5DealsObject = generateTradeByAccountServerId(data.clientHelper.getTradingAccount(), data.clientHelper.getServerId());
        mt5DealsObject.comment = "S/O";
        data.mt5DealsObjects.add(mt5DealsObject);
        data.aggrMirrorAccountsByTrades = generateMirrorTradesByAccount(data.clientHelper);
        return data;
    }

    public static MirrorTradingRuleData getMirrorTradingRuleExitEventEnd7_5Data() {
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
        MirrorTradingRuleData data = getMirrorTradingRuleData(mirrorTradingRuleExitEventEnd7_1Client);
        data.mtTbCreditsObjects.add(generateCreditsByClient(data.clientHelper));
        data.aggrCreditEquityRate = generateCreditEquityRatioAccount(data.clientHelper);
        data.lnSessionParsedObjectRegistration.riskRating = "medium";
        CrmTbDepositObject crmTbDepositObject = generateDepositByClient(data.clientHelper);
        crmTbDepositObject.createTime = data.crmTbUserObject.registrationDate;
        data.crmTbDepositObjects.add(crmTbDepositObject);
        data.lnSessionParsedObjectLogin.trueIpGeo = "US";
        // TODO add data for risk free revenue ratio > 0.5
        // TODO add data for no trading on news periods?
        // TODO add data for no dummy trades?
        data.mt5DealsObjects.add(generateTradeByAccountServerId(data.clientHelper.getTradingAccount(), data.clientHelper.getServerId()));
        return data;
    }
}
