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

import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.DbHelper.*;
import static helpers.database.MitigationHelper.cleanUserRestriction;
import static utils.Constants.*;
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
                getRandomUuidString(), Instant.now().toString(), getRandomIntPositive(), mtTbUserObject.account, 100d, "USDEUR", mtTbUserObject.serverId, "closeTrade"
        );
        return new MirrorTradingRuleData(client, userObject, lexisNexisObjectRegistration, lexisNexisObjectLogin, new ArrayList<>(), new ArrayList<>(), closeTradeMtEvent, new ArrayList<>(), null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), null, null);
    }

    private static ConnectionTableEntryV3 getConnection(ClientHelper fromClient, ClientHelper toClient) {
        return new ConnectionTableEntryV3(
                fromClient.getUcid(), toClient.getUcid(), "Same Identity", 1d, "{\"payout\": \"463344**** **5603\"}", getCurrentTimestampDbFormat());
    }

    public static MirrorTradingRuleData getMirrorTradingRuleExitEventEnd2Data() {
        Allure.step("Create user");
        MirrorTradingRuleData data = getMirrorTradingRuleData(mirrorTradingRuleExitEventEnd2Client);
        Allure.step("Client has previous restrictions");
        BoClientFraudTypesObject boClientFraudTypesObject = new BoClientFraudTypesObject(
                data.clientHelper.getUcid(), 6, "HEDGING"
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
                connectedClient.getUcid(), 6, "HEDGING"
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
        MirrorTradingRuleData data = getMirrorTradingRuleData(mirrorTradingRuleExitEventEnd7_5Client);
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

    public static Map<String, MirrorTradingRuleData> setupMirrorTradingRuleData() throws ReflectiveOperationException,
            SQLException {
        startSshTunnel();
        Map<String, MirrorTradingRuleData> map = new HashMap<>();
        // Put all the db data for setup in a list
        map.put("2", getMirrorTradingRuleExitEventEnd2Data());
        map.put("3_1", getMirrorTradingRuleExitEventEnd3_1Data());
        map.put("3_2", getMirrorTradingRuleExitEventEnd3_2Data());
        map.put("4_1", getMirrorTradingRuleExitEventEnd4_1Data());
        map.put("4_2", getMirrorTradingRuleExitEventEnd4_2Data());
        map.put("5_1", getMirrorTradingRuleExitEventEnd5_1Data());
        map.put("5_2", getMirrorTradingRuleExitEventEnd5_2Data());
        map.put("6", getMirrorTradingRuleExitEventEnd6Data());
        map.put("1_1", getMirrorTradingRuleExitEventEnd1_1Data());
        map.put("1_2", getMirrorTradingRuleExitEventEnd1_2Data());
        map.put("7_1", getMirrorTradingRuleExitEventEnd7_1Data());
        map.put("7_2", getMirrorTradingRuleExitEventEnd7_2Data());
        map.put("7_3", getMirrorTradingRuleExitEventEnd7_3Data());
        map.put("7_4", getMirrorTradingRuleExitEventEnd7_4Data());
        map.put("7_5", getMirrorTradingRuleExitEventEnd7_5Data());

        // Loop through the list with data and insert all the data into the according tables
        for (MirrorTradingRuleData data : map.values()) {
            insertObjectToDb(CRM_USER_TABLE_NAME, data.crmTbUserObject);
            data.connections.forEach(connection -> {
                try {
                    insertObjectToDb(CONNECTIONS_V3_TABLE_NAME, connection);
                } catch (SQLException | ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            });
            if (data.lnSessionParsedObjectRegistration != null) {
                insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, data.lnSessionParsedObjectRegistration);
            }
            if (data.lnSessionParsedObjectLogin != null) {
                insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, data.lnSessionParsedObjectLogin);
            }
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
            data.mtTbCreditsObjects.forEach(credit -> {
                try {
                    insertObjectToDb(MT_CREDITS_TABLE_NAME, credit);
                } catch (SQLException | ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            });
            data.crmTbWithdrawalObjects.forEach(withdrawal -> {
                try {
                    insertObjectToDb(CRM_WITHDRAWAL_TABLE_NAME, withdrawal);
                } catch (SQLException | ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            });
            data.crmTbDepositObjects.forEach(deposit -> {
                try {
                    insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, deposit);
                } catch (SQLException | ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            });
            data.crmTbBonusObjects.forEach(bonus -> {
                try {
                    insertObjectToDb(CRM_BONUS_TABLE_NAME, bonus);
                } catch (SQLException | ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            });
            data.mt5DealsObjects.forEach(deal -> {
                try {
                    insertObjectToDb(MT5_DEALS_TABLE_NAME, deal);
                } catch (SQLException | ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            });
            if (data.aggrCreditEquityRate != null) {
                insertObjectToDb(AGGR_CREDIT_EQUITY_RATE, data.aggrCreditEquityRate);
            }
            if (data.aggrMirrorAccountsByTrades != null) {
                insertObjectToDb(AGGR_MIRROR_ACCOUNTS_BY_TRADES, data.aggrMirrorAccountsByTrades);
            }
        }
        return map;
    }

    public static void deleteMirrorTradingRuleData(Map<String, MirrorTradingRuleData> map) throws Exception {
        // Loop through the list with data and delete all the previously created data into the according tables
        for (MirrorTradingRuleData data : map.values()) {
            deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("user_id = %s", data.crmTbUserObject.userId));
            data.connections.forEach(connection -> {
                try {
                    deleteEntryFromDb(CONNECTIONS_V3_TABLE_NAME, String.format("user_from = '%s'", connection.userFrom));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            deleteEntryFromDb(LEXIS_NEXIS_TABLE_NAME, String.format("user_id = %s", data.lnSessionParsedObjectRegistration.userId));
            deleteEntryFromDb(LEXIS_NEXIS_TABLE_NAME, String.format("user_id = %s", data.lnSessionParsedObjectLogin.userId));
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
            data.mtTbCreditsObjects.forEach(credit -> {
                try {
                    deleteEntryFromDb(MT_CREDITS_TABLE_NAME, String.format("ucid = '%s'", credit.ucid));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            data.crmTbWithdrawalObjects.forEach(withdrawal -> {
                try {
                    deleteEntryFromDb(CRM_WITHDRAWAL_TABLE_NAME, String.format("ucid = '%s'", withdrawal.ucid));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            data.crmTbDepositObjects.forEach(deposit -> {
                try {
                    deleteEntryFromDb(CRM_DEPOSIT_TABLE_NAME, String.format("ucid = '%s'", deposit.ucid));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            data.crmTbBonusObjects.forEach(bonus -> {
                try {
                    deleteEntryFromDb(CRM_BONUS_TABLE_NAME, String.format("ucid = '%s'", bonus.ucid));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            data.mt5DealsObjects.forEach(deal -> {
                try {
                    deleteEntryFromDb(MT5_DEALS_TABLE_NAME, String.format("server_id = %s and login = %s", deal.serverId, deal.login));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            cleanUserRestriction(data.clientHelper.getUcid());
            closeAlert(data.clientHelper.getUcid());
        }
        stopSshTunnel();
    }
}
