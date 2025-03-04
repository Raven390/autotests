package helpers.data.rules.mirrorTradingRule;

import businessObjects.db.clickhouse.aggrCreditEquityRate.AggrCreditEquityRateObject;
import businessObjects.db.clickhouse.aggrMirrorAccountsByTrades.MirrorLoginObject;
import businessObjects.db.clickhouse.boClientFraudTypes.BoClientFraudTypesObject;
import businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObject;
import businessObjects.db.clickhouse.crmTbDepositTable.CrmTbDepositObject;
import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;
import businessObjects.db.clickhouse.connectionTable.ConnectionTableEntry;
import businessObjects.db.clickhouse.crmTbWithdrawal.CrmTbWithdrawalObject;
import businessObjects.db.clickhouse.lnSessionParsed.LnSessionParsedObject;
import businessObjects.db.clickhouse.mtBalanceOrdersTable.MtBalanceOrdersObject;
import businessObjects.db.clickhouse.mtMt5DealsCoerced.Mt5DealsCoercedObject;
import businessObjects.db.clickhouse.mtTbCredits.MtTbCreditsObject;
import businessObjects.kafka.mtEvents.CloseTradeMtEvent;
import generator.annotations.RuleTestData;
import helpers.data.ClientHelper;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static businessObjects.db.clickhouse.aggrCreditEquityRate.AggrCreditEquityRateObjectFactory.generateCreditEquityRatioAccount;
import static businessObjects.db.clickhouse.aggrMirrorAccountsByTrades.MirrorLoginObjectFactory.generateMirrorTradesByAccount;
import static businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObjectFactory.generateCrmTbAccountData;
import static businessObjects.db.clickhouse.crmTbDepositTable.CrmTbDepositObjectFactory.generateDepositByClient;
import static businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObjectFactory.generateUserByClient;
import static businessObjects.db.clickhouse.crmTbWithdrawal.CrmTbWithdrawalObjectFactory.generateWithdrawalByClient;
import static businessObjects.db.clickhouse.lnSessionParsed.LnSessionParsedObjectFactory.generateLexisNexisDataForUserId;
import static businessObjects.db.clickhouse.mtBalanceOrdersTable.MtBalanceOrdersObjectFactory.generateBalanceOrders;
import static businessObjects.db.clickhouse.mtMt5DealsCoerced.Mt5DealsCoercedFactory.generateTradeByClient;
import static businessObjects.db.clickhouse.mtTbCredits.MtTbCreditsObjectFactory.generateCreditsByClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.DbHelper.*;
import static helpers.database.MitigationHelper.cleanUserRestriction;
import static utils.Constants.*;
import static utils.Utils.*;
import static utils.Utils.getCurrentTimestampDbFormat;

@RuleTestData("mirror-trading")
public class MirrorTradingRuleDataFactory {
    private static final ClientHelper mirrorTradingRuleExitEventEnd2Client = getRandomVantageClientAllFields();
    private static final ClientHelper mirrorTradingRuleExitEventEnd3_1Client = getRandomVantageClientAllFields();
    private static final ClientHelper mirrorTradingRuleExitEventEnd3_2Client = getRandomVantageClientAllFields();
    private static final ClientHelper mirrorTradingRuleExitEventEnd4_1Client = getRandomVantageClientAllFields();
    private static final ClientHelper mirrorTradingRuleExitEventEnd4_2Client = getRandomVantageClientAllFields();
    private static final ClientHelper mirrorTradingRuleExitEventEnd4_3Client = getRandomVantageClientAllFields();
    private static final ClientHelper mirrorTradingRuleExitEventEnd4_4Client = getRandomVantageClientAllFields();
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
        LnSessionParsedObject lexisNexisObjectRegistration = generateLexisNexisDataForUserId(client.getUid(), client.getUserId(), getRandomIntPositive());
        lexisNexisObjectRegistration.brand = client.getBrand();
        lexisNexisObjectRegistration.eventType = "account_creation";
        lexisNexisObjectRegistration.userId = client.getUserId();
        lexisNexisObjectRegistration.riskRating = "low";
        LnSessionParsedObject lexisNexisObjectLogin = generateLexisNexisDataForUserId(client.getUid(), client.getUserId(), getRandomIntPositive());
        lexisNexisObjectLogin.brand = client.getBrand();
        lexisNexisObjectLogin.eventType = "login";
        lexisNexisObjectLogin.userId = client.getUserId();
        lexisNexisObjectLogin.riskRating = "low";
        lexisNexisObjectLogin.trueIpGeo = "CY";
        CrmTbAccountObject crmTbAccountObject = generateCrmTbAccountData(client);
        CloseTradeMtEvent closeTradeMtEvent = new CloseTradeMtEvent(
                getRandomUuidString(), Instant.now().toString(), getRandomIntPositive().longValue(), crmTbAccountObject.account, 100d, "EURUSD", crmTbAccountObject.serverIdSt, "closeTrade"
        );
        return new MirrorTradingRuleData(client, userObject, lexisNexisObjectRegistration, lexisNexisObjectLogin, new ArrayList<>(), new ArrayList<>(), closeTradeMtEvent, new ArrayList<>(), crmTbAccountObject, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), null, null, new ArrayList<>(), new ArrayList<>());
    }

    private static ConnectionTableEntry getConnection(ClientHelper fromClient, ClientHelper toClient) {
        return new ConnectionTableEntry(
                fromClient.getUcid(), toClient.getUcid(), "Same Identity", 1d, "[{\"connectionAttributeName\": \"payout\", \"connectionAttributeValue\": \"535456**** **0344\", \"sourceAttributeValue\": \"535456**** **0344\", \"relationType\": \"exact\"}]", getCurrentTimestampDbFormat());
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
        MirrorTradingRuleData data = getMirrorTradingRuleData(mirrorTradingRuleExitEventEnd3_1Client);
        Allure.step("Client has no previous restrictions");
        Allure.step("Client has mirror trading abuse connected account");
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        data.connections.add(getConnection(data.clientHelper, connectedClient));
        BoClientFraudTypesObject boClientFraudTypesObject = new BoClientFraudTypesObject(
                connectedClient.getUcid(), 1, "HEDGING"
        );
        Allure.step("Set restriction");
        Allure.step("Send alert");
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
        MtTbCreditsObject credit = generateCreditsByClient(connectedClient);
        credit.amount = 10D;
        credit.regulator = "VFSC";
        data.mtTbCreditsObjects.add(credit);
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

    public static MirrorTradingRuleData getMirrorTradingRuleExitEventEnd4_3Data() {
        Allure.step("Client has no previous restrictions");
        Allure.step("Client has connected account, not a mirror abuser");
        Allure.step("Clone has a credit");
        Allure.step("User has no credit");
        Allure.step("Exit without alert");
        MirrorTradingRuleData data = getMirrorTradingRuleData(mirrorTradingRuleExitEventEnd4_3Client);
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        data.connections.add(getConnection(data.clientHelper, connectedClient));
        MtTbCreditsObject credit = generateCreditsByClient(connectedClient);
        credit.amount = 10D;
        credit.regulator = "VFSC";
        data.mtTbCreditsObjects.add(credit);
        data.crmTbAccountObjectConnections.add(generateCrmTbAccountData(connectedClient));
        return data;
    }

    public static MirrorTradingRuleData getMirrorTradingRuleExitEventEnd4_4Data() {
        Allure.step("Client has no previous restrictions");
        Allure.step("Client has connected account, not a mirror abuser");
        Allure.step("Clone has no credits");
        Allure.step("User has no credits");
        Allure.step("Exit without alert");
        MirrorTradingRuleData data = getMirrorTradingRuleData(mirrorTradingRuleExitEventEnd4_4Client);
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        data.connections.add(getConnection(data.clientHelper, connectedClient));
        return data;
    }

    public static MirrorTradingRuleData getMirrorTradingRuleExitEventEnd5_1Data() {
        Allure.step("Client has no previous restrictions");
        Allure.step("Client has no connected account (or have, bu connected account has no bonuses)");
        Allure.step("Account has a credit");
        Allure.step("Sum of abuse score > 4 is False");
        Allure.step("Exit without alert");
        MirrorTradingRuleData data = getMirrorTradingRuleData(mirrorTradingRuleExitEventEnd5_1Client);
        MtTbCreditsObject credit = generateCreditsByClient(mirrorTradingRuleExitEventEnd5_1Client);
        data.lnSessionParsedObjectLogin.trueIpGeo = "CY";
        data.lnSessionParsedObjectRegistration.trueIpGeo = "CY";
        data.lnSessionParsedObjectLogin.trueIpCountryConfidence = 86;
        credit.amount = 10D;
        credit.regulator = "VFSC";
        data.mtTbCreditsObjects.add(credit);
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
        crmTbDepositObject.createTime = data.crmTbUserObject.createTime;
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
        crmTbDepositObject.createTime = data.crmTbUserObject.createTime;
        data.crmTbDepositObjects.add(crmTbDepositObject);
        data.lnSessionParsedObjectLogin.trueIpGeo = "US";
        // TODO add data for risk free revenue ratio > 0.5
        return data;
    }

    public static MirrorTradingRuleData getMirrorTradingRuleExitEventEnd1_1Data() {
        MirrorTradingRuleData data = getMirrorTradingRuleData(mirrorTradingRuleExitEventEnd1_1Client);
        MtTbCreditsObject credit = generateCreditsByClient(mirrorTradingRuleExitEventEnd1_1Client);
        CrmTbDepositObject deposit = generateDepositByClient(mirrorTradingRuleExitEventEnd1_1Client);
        Mt5DealsCoercedObject trade1 = generateTradeByClient(mirrorTradingRuleExitEventEnd1_1Client);
        Mt5DealsCoercedObject trade2 = generateTradeByClient(mirrorTradingRuleExitEventEnd1_1Client);
        Mt5DealsCoercedObject mirrorTrade1 = generateTradeByClient(mirrorTradingRuleExitEventEnd1_1Client);
        Mt5DealsCoercedObject mirrorTrade2 = generateTradeByClient(mirrorTradingRuleExitEventEnd1_1Client);
        Mt5DealsCoercedObject mirrorTrade3 = generateTradeByClient(mirrorTradingRuleExitEventEnd1_1Client);
        Mt5DealsCoercedObject mirrorTrade4 = generateTradeByClient(mirrorTradingRuleExitEventEnd1_1Client);
        Mt5DealsCoercedObject mirrorTrade5 = generateTradeByClient(mirrorTradingRuleExitEventEnd1_1Client);
        Mt5DealsCoercedObject mirrorTrade6 = generateTradeByClient(mirrorTradingRuleExitEventEnd1_1Client);
        MtBalanceOrdersObject balanceOrder = generateBalanceOrders(mirrorTradingRuleExitEventEnd1_1Client, 1d, 2d, getCurrentTimestampDbFormat());
        Allure.step("Client has no previous restrictions");
        Allure.step("Client has no connected account (or have, bu connected account has no bonuses)");
        Allure.step("Account has a credit");
        credit.regulator = "VFSC";
        Allure.step("Payment channel is in the gray list");
        deposit.paymentChannel = "CreditCard";
        Allure.step("ftdCredit.amount/ ftdDeposit.amount >= 0.4");
        credit.amountUsd = 10D;
        deposit.amountUsd = 2D;
        Allure.step("number of traded symbols < 3");
        trade1.symbol = "EURUSD";
        Allure.step("lexisNexis.riskRating in ('high', 'medium')");
        data.lnSessionParsedObjectRegistration.riskRating = "medium";
        Allure.step("now() -firstDeposit < 7 days");
        Allure.step("lexisNexis.country = client.country? = false");
        data.lnSessionParsedObjectLogin.trueIpCountryConfidence = 50;
        Allure.step("dummy trade is true");
        mirrorTrade1.volume = 0.01;
        mirrorTrade1.volumeLots = 0.01;
        mirrorTrade2.volume = 1d;
        mirrorTrade2.volumeLots = 1d;
        mirrorTrade3.volume = 1d;
        mirrorTrade3.volumeLots = 1d;
        mirrorTrade4.volume = 1d;
        mirrorTrade4.volumeLots = 1d;
        mirrorTrade5.volume = 1d;
        mirrorTrade5.volumeLots = 1d;
        mirrorTrade6.volume = 1d;
        mirrorTrade6.volumeLots = 1d;
        Allure.step("count(tradesWithStopouts)/count(trades) > 0.8 = true");
        trade2.comment = "S/O";
        Allure.step("count(balanceOrdersWithTypeWO) > 0 = true");
        balanceOrder.comment = "W/O";
        Allure.step("are there mirror deals? = true");
        MirrorLoginObject mirrorTrade = generateMirrorTradesByAccount(mirrorTradingRuleExitEventEnd1_1Client);
        System.out.println(data.clientHelper.getServerId());
        System.out.println(mirrorTrade.server_id_1);
        System.out.println(data.crmTbAccountObject.serverIdSt);

        data.mirrorLoginObjects.add(mirrorTrade);
        data.crmTbDepositObjects.add(deposit);
        data.mtTbCreditsObjects.add(credit);
        data.mt5DealsObjects.add(trade1);
        data.mtBalanceOrdersObjects.add(balanceOrder);
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
        crmTbDepositObject.createTime = data.crmTbUserObject.createTime;
        data.crmTbDepositObjects.add(crmTbDepositObject);
        data.lnSessionParsedObjectLogin.trueIpGeo = "US";
        // TODO add data for risk free revenue ratio > 0.5
        // TODO add data for trading on news periods?
        data.mt5DealsObjects.add(generateTradeByClient(data.clientHelper));
        MirrorLoginObject mirrorAccountsByTrades = generateMirrorTradesByAccount(data.clientHelper);
        mirrorAccountsByTrades.lots_1 = 3d;
        data.aggrMirrorAccountsByTrades = mirrorAccountsByTrades;
        return data;
    }

    public static MirrorTradingRuleData getMirrorTradingRuleExitEventEnd7_1Data() {
        MirrorTradingRuleData data = getMirrorTradingRuleData(mirrorTradingRuleExitEventEnd7_1Client);
        MtTbCreditsObject credit = generateCreditsByClient(mirrorTradingRuleExitEventEnd7_1Client);
        CrmTbDepositObject deposit = generateDepositByClient(data.clientHelper);
        Mt5DealsCoercedObject trade1 = generateTradeByClient(mirrorTradingRuleExitEventEnd7_1Client);
        Allure.step("Client has no previous restrictions");
        Allure.step("Client has no connected account (or have, bu connected account has no bonuses)");
        Allure.step("Account has a credit");
        credit.regulator = "VFSC";
        Allure.step("Payment channel is in the gray list");
        deposit.paymentChannel = "CreditCard";
        Allure.step("ftdCredit.amount/ ftdDeposit.amount >= 0.4");
        credit.amountUsd = 10D;
        deposit.amountUsd = 2D;
        Allure.step("number of traded symbols < 3");
        trade1.symbol = "EURUSD";
        Allure.step("lexisNexis.riskRating in ('high', 'medium')");
        data.lnSessionParsedObjectRegistration.riskRating = "medium";
        Allure.step("now() -firstDeposit < 7 days");
        Allure.step("lexisNexis.country = client.country? = false");
        data.lnSessionParsedObjectLogin.trueIpCountryConfidence = 50;
        Allure.step("count(tradesWithStopouts)/count(trades) > 0.8 = false");
        Allure.step("count(balanceOrdersWithTypeWO) > 0 = false");
        data.crmTbDepositObjects.add(deposit);
        data.mtTbCreditsObjects.add(credit);
        data.mt5DealsObjects.add(trade1);
        return data;
    }

    public static MirrorTradingRuleData getMirrorTradingRuleExitEventEnd7_2Data() {
        MirrorTradingRuleData data = getMirrorTradingRuleData(mirrorTradingRuleExitEventEnd7_2Client);
        MtTbCreditsObject credit = generateCreditsByClient(mirrorTradingRuleExitEventEnd7_2Client);
        CrmTbWithdrawalObject withdrawal = generateWithdrawalByClient(data.clientHelper);
        CrmTbDepositObject deposit = generateDepositByClient(data.clientHelper);
        Mt5DealsCoercedObject trade1 = generateTradeByClient(mirrorTradingRuleExitEventEnd7_2Client);
        Mt5DealsCoercedObject trade2 = generateTradeByClient(mirrorTradingRuleExitEventEnd7_2Client);
        Mt5DealsCoercedObject trade3 = generateTradeByClient(mirrorTradingRuleExitEventEnd7_2Client);
        Mt5DealsCoercedObject trade4 = generateTradeByClient(mirrorTradingRuleExitEventEnd7_2Client);
        Mt5DealsCoercedObject trade5 = generateTradeByClient(mirrorTradingRuleExitEventEnd7_2Client);
        Mt5DealsCoercedObject trade6 = generateTradeByClient(mirrorTradingRuleExitEventEnd7_2Client);
        Allure.step("Client has no previous restrictions");
        Allure.step("Client has no connected account (or have, bu connected account has no bonuses)");
        Allure.step("Account has a credit");
        credit.regulator = "VFSC";
        Allure.step("Payment channel is in the gray list");
        withdrawal.paymentChannel = "CreditCard";
        Allure.step("ftdCredit.amount/ ftdDeposit.amount >= 0.4");
        credit.amountUsd = 10D;
        withdrawal.amountUsd = 2D;
        deposit.amountUsd = 1D;
        Allure.step("number of traded symbols < 3");
        trade1.symbol = "EURUSD";
        Allure.step("lexisNexis.riskRating in ('high', 'medium')");
        data.lnSessionParsedObjectRegistration.riskRating = "high";
        Allure.step("now() -clientDateRegistration < 7 days = true");
        System.out.println(data.crmTbUserObject.registrationDate);
        System.out.println(data.closeTradeMtEvent.closeTime);
        Allure.step("lexisNexis.country = client.country? = false");
        data.lnSessionParsedObjectLogin.trueIpCountryConfidence = 50;
        Allure.step("count(tradesWithStopouts)/count(trades) > 0.8 = true");
        trade2.comment = "S/O";
        trade3.comment = "S/O";
        trade4.comment = "S/O";
        trade5.comment = "S/O";
        trade6.comment = "S/O";
        Allure.step("are there mirror deals? = false");
        data.crmTbWithdrawalObjects.add(withdrawal);
        data.crmTbDepositObjects.add(deposit);
        data.mtTbCreditsObjects.add(credit);
        data.mt5DealsObjects.add(trade1);
        data.mt5DealsObjects.add(trade2);
        data.mt5DealsObjects.add(trade3);
        data.mt5DealsObjects.add(trade4);
        data.mt5DealsObjects.add(trade5);
        data.mt5DealsObjects.add(trade6);
        return data;
    }

    public static MirrorTradingRuleData getMirrorTradingRuleExitEventEnd7_3Data() {
        MirrorTradingRuleData data = getMirrorTradingRuleData(mirrorTradingRuleExitEventEnd7_3Client);
        MtTbCreditsObject credit = generateCreditsByClient(mirrorTradingRuleExitEventEnd7_3Client);
        CrmTbDepositObject deposit = generateDepositByClient(data.clientHelper);
        Mt5DealsCoercedObject trade1 = generateTradeByClient(mirrorTradingRuleExitEventEnd7_3Client);
        Mt5DealsCoercedObject trade2 = generateTradeByClient(mirrorTradingRuleExitEventEnd7_3Client);
        MtBalanceOrdersObject balanceOrder = generateBalanceOrders(mirrorTradingRuleExitEventEnd7_3Client, 1d, 2d, getCurrentTimestampDbFormat());
        Allure.step("Client has no previous restrictions");
        Allure.step("Client has no connected account (or have, bu connected account has no bonuses)");
        Allure.step("Account has a credit");
        credit.regulator = "VFSC";
        Allure.step("Payment channel is in the gray list");
        deposit.paymentChannel = "CreditCard";
        Allure.step("ftdCredit.amount/ ftdDeposit.amount >= 0.4");
        credit.amountUsd = 10D;
        deposit.amountUsd = 2D;
        Allure.step("number of traded symbols < 3");
        trade1.symbol = "EURUSD";
        Allure.step("lexisNexis.riskRating in ('high', 'medium')");
        data.lnSessionParsedObjectRegistration.riskRating = "medium";
        Allure.step("now() -firstDeposit < 7 days");
        Allure.step("lexisNexis.country = client.country? = false");
        data.lnSessionParsedObjectLogin.trueIpCountryConfidence = 50;
        Allure.step("count(tradesWithStopouts)/count(trades) > 0.8 = true");
        trade2.comment = "S/O";
        Allure.step("count(balanceOrdersWithTypeWO) > 0 = true");
        balanceOrder.comment = "W/O";
        Allure.step("are there mirror deals? = false");
        data.crmTbDepositObjects.add(deposit);
        data.mtTbCreditsObjects.add(credit);
        data.mt5DealsObjects.add(trade1);
        data.mtBalanceOrdersObjects.add(balanceOrder);
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
        crmTbDepositObject.createTime = data.crmTbUserObject.createTime;
        data.crmTbDepositObjects.add(crmTbDepositObject);
        data.lnSessionParsedObjectLogin.trueIpGeo = "US";
        // TODO add data for risk free revenue ratio > 0.5
        // TODO add data for no trading on news periods?
        // TODO add data for no dummy trades?
        Mt5DealsCoercedObject mt5DealsObject = generateTradeByClient(data.clientHelper);
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
        crmTbDepositObject.createTime = data.crmTbUserObject.createTime;
        data.crmTbDepositObjects.add(crmTbDepositObject);
        data.lnSessionParsedObjectLogin.trueIpGeo = "US";
        // TODO add data for risk free revenue ratio > 0.5
        // TODO add data for no trading on news periods?
        // TODO add data for no dummy trades?
        data.mt5DealsObjects.add(generateTradeByClient(data.clientHelper));
        return data;
    }

    public static Map<String, MirrorTradingRuleData> setupMirrorTradingRuleData() {
        startSshTunnel();
        Map<String, MirrorTradingRuleData> map = new HashMap<>();
        // Put all the db data for setup in a list
        //map.put("2", getMirrorTradingRuleExitEventEnd2Data());

        map.put("1_1", getMirrorTradingRuleExitEventEnd1_1Data());


        // Loop through the list with data and insert all the data into the according tables
        for (MirrorTradingRuleData data : map.values()) {
            insertObjectToDb(CRM_USER_TABLE_NAME, data.crmTbUserObject);
            data.connections.forEach(connection -> {
                insertObjectToDb(CONNECTIONS_TABLE_NAME, connection);
            });
            if (data.lnSessionParsedObjectRegistration != null) {
                insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, data.lnSessionParsedObjectRegistration);
            }
            if (data.lnSessionParsedObjectLogin != null) {
                insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, data.lnSessionParsedObjectLogin);
            }
            data.clientFraudTypes.forEach(fraud -> {
                insertObjectToDb(BO_CLIENT_FRAUD_TYPES_TABLE_NAME, fraud);
            });
            if (data.crmTbAccountObject != null) {
                insertObjectToDb(CRM_ACCOUNT_TABLE_NAME, data.crmTbAccountObject);
            }
            data.crmTbAccountObjectConnections.forEach(credit -> {
                insertObjectToDb(CRM_ACCOUNT_TABLE_NAME, credit);
            });
            data.mtTbCreditsObjects.forEach(credit -> {
                insertObjectToDb(MT_CREDITS_TABLE_NAME, credit);
            });
            data.crmTbWithdrawalObjects.forEach(withdrawal -> {
                insertObjectToDb(CRM_WITHDRAWAL_TABLE_NAME, withdrawal);
            });
            data.crmTbDepositObjects.forEach(deposit -> {
                insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, deposit);
            });
            data.crmTbBonusObjects.forEach(bonus -> {
                insertObjectToDb(CRM_BONUS_TABLE_NAME, bonus);
            });
            data.mt5DealsObjects.forEach(deal -> {
                insertObjectToDb(MT5_DEALS_COERCED_TABLE_NAME, deal);
            });
            data.mtBalanceOrdersObjects.forEach(deal -> {
                insertObjectToDb(MT_BALANCE_ORDERS_TABLE_NAME, deal);
            });
            data.mirrorLoginObjects.forEach(deal -> {
                insertObjectToDb(MIRROR_LOGIN_TABLE_NAME, deal);
            });
            if (data.aggrCreditEquityRate != null) {
                insertObjectToDb(AGGR_CREDIT_EQUITY_RATE, data.aggrCreditEquityRate);
            }
            if (data.aggrMirrorAccountsByTrades != null) {
                insertObjectToDb(MIRROR_LOGIN_TABLE_NAME, data.aggrMirrorAccountsByTrades);
            }
        }
        return map;
    }

    public static void deleteMirrorTradingRuleData(Map<String, MirrorTradingRuleData> map) throws Exception {
        // Loop through the list with data and delete all the previously created data into the according tables
        for (MirrorTradingRuleData data : map.values()) {
            deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("user_id = %s", data.crmTbUserObject.userId));
            data.connections.forEach(connection -> {
                deleteEntryFromDb(CONNECTIONS_TABLE_NAME, String.format("user_from = '%s'", connection.userFrom));
            });
            deleteEntryFromDb(LEXIS_NEXIS_TABLE_NAME, String.format("user_id = %s", data.lnSessionParsedObjectRegistration.userId));
            deleteEntryFromDb(LEXIS_NEXIS_TABLE_NAME, String.format("user_id = %s", data.lnSessionParsedObjectLogin.userId));
            data.clientFraudTypes.forEach(fraud -> {
                deleteEntryFromDb(BO_CLIENT_FRAUD_TYPES_TABLE_NAME, String.format("ucid = '%s'", fraud.ucid));
            });
            data.mtTbCreditsObjects.forEach(credit -> {
                deleteEntryFromDb(MT_CREDITS_TABLE_NAME, String.format("ucid = '%s'", credit.ucid));
            });
            data.crmTbWithdrawalObjects.forEach(withdrawal -> {
                deleteEntryFromDb(CRM_WITHDRAWAL_TABLE_NAME, String.format("ucid = '%s'", withdrawal.ucid));
            });
            data.crmTbDepositObjects.forEach(deposit -> {
                deleteEntryFromDb(CRM_DEPOSIT_TABLE_NAME, String.format("ucid = '%s'", deposit.ucid));
            });
            data.crmTbBonusObjects.forEach(bonus -> {
                deleteEntryFromDb(CRM_BONUS_TABLE_NAME, String.format("ucid = '%s'", bonus.ucid));
            });
            data.mtBalanceOrdersObjects.forEach(bonus -> {
                deleteEntryFromDb(MT_BALANCE_ORDERS_TABLE_NAME, String.format("ucid = '%s'", bonus.ucid));
            });
            data.mt5DealsObjects.forEach(deal -> {
                deleteEntryFromDb(MT5_DEALS_COERCED_TABLE_NAME, String.format("server_id = %s and account = %s", deal.serverId, deal.account));
            });
            data.mirrorLoginObjects.forEach(mirrorLoginObject -> {
                deleteEntryFromDb(MIRROR_LOGIN_TABLE_NAME, String.format("login_1 = %s", mirrorLoginObject.login_1));
            });
            cleanUserRestriction(data.clientHelper.getUcid());
            closeAlert(data.clientHelper.getUcid());
        }
        stopSshTunnel();
    }
}
