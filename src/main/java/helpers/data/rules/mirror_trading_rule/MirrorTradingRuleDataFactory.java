package helpers.data.rules.mirror_trading_rule;

import business_objects.db.clickhouse.aggr_credit_equity_rate.AggrCreditEquityRateObject;
import business_objects.db.clickhouse.aggr_mirror_accounts_by_trades.MirrorLoginObject;
import business_objects.db.clickhouse.bo_client_fraud_types.ClientFraudTypesObject;
import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.connection_table.ConnectionTableEntry;
import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalObject;
import business_objects.db.clickhouse.ln_session_parsed.LnSessionParsedObject;
import business_objects.db.clickhouse.mt_balance_orders_table.MtBalanceOrdersObject;
import business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedObject;
import business_objects.db.clickhouse.mt_tb_credits.MtTbCreditsObject;
import business_objects.kafka.mt_events.CloseTradeMtEvent;
import generator.annotations.RuleTestData;
import helpers.data.ClientHelper;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static business_objects.db.clickhouse.aggr_credit_equity_rate.AggrCreditEquityRateObjectFactory.generateCreditEquityRatioAccount;
import static business_objects.db.clickhouse.aggr_mirror_accounts_by_trades.MirrorLoginObjectFactory.generateMirrorTradesByAccount;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountData;
import static business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositObjectFactory.generateDepositByClient;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalObjectFactory.generateWithdrawalByClient;
import static business_objects.db.clickhouse.ln_session_parsed.LnSessionParsedObjectFactory.generateLexisNexisDataForUserId;
import static business_objects.db.clickhouse.mt_balance_orders_table.MtBalanceOrdersObjectFactory.generateBalanceOrders;
import static business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedFactory.generateTradeByClient;
import static business_objects.db.clickhouse.mt_tb_credits.MtTbCreditsObjectFactory.generateCreditsByClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.DbHelper.*;
import static helpers.database.CleanTableHelper.*;
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
        lexisNexisObjectRegistration.setBrand(client.getBrand());
        lexisNexisObjectRegistration.setEventType("account_creation");
        lexisNexisObjectRegistration.setUserId(client.getUserId());
        lexisNexisObjectRegistration.setRiskRating("low");
        LnSessionParsedObject lexisNexisObjectLogin = generateLexisNexisDataForUserId(client.getUid(), client.getUserId(), getRandomIntPositive());
        lexisNexisObjectLogin.setBrand(client.getBrand());
        lexisNexisObjectLogin.setEventType("login");
        lexisNexisObjectLogin.setUserId(client.getUserId());
        lexisNexisObjectLogin.setRiskRating("low");
        lexisNexisObjectLogin.setTrueIpGeo("CY");
        CrmTbAccountObject crmTbAccountObject = generateCrmTbAccountData(client);
        CloseTradeMtEvent closeTradeMtEvent = new CloseTradeMtEvent(
                getRandomUuidString(), Instant.now().toString(), getRandomIntPositive().longValue(), crmTbAccountObject.account, 100d, "EURUSD", crmTbAccountObject.serverIdSt, "closeTrade"
        );
        return new MirrorTradingRuleData(client, userObject, lexisNexisObjectRegistration, lexisNexisObjectLogin, new ArrayList<>(), new ArrayList<>(), closeTradeMtEvent, new ArrayList<>(), crmTbAccountObject, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), null, null, new ArrayList<>(), new ArrayList<>());
    }

    private static ConnectionTableEntry getConnection(ClientHelper fromClient, ClientHelper toClient) {
        return new ConnectionTableEntry(
                fromClient.getUcid(), toClient.getUcid(), CONNECTION_TYPE_SAME_IDENTITY, 1d, List.of(
                        new ConnectionTableEntry.ConnectionInfo(
                                CONNECTION_ATTRIBUTE_NAME_PAYOUT, CONNECTION_SEARCH_DATA_CARD_NUMBER, CONNECTION_SEARCH_DATA_CARD_NUMBER, CONNECTION_TYPE_RELATION_TYPE_EXACT
                        )), getCurrentTimestampDbFormat()
        );
    }

    public static MirrorTradingRuleData getMirrorTradingRuleExitEventEnd2Data() {
        Allure.step("Create user");
        MirrorTradingRuleData data = getMirrorTradingRuleData(mirrorTradingRuleExitEventEnd2Client);
        Allure.step("Client has previous restrictions");
        ClientFraudTypesObject clientFraudTypesObject = new ClientFraudTypesObject(
                data.clientHelper.getUcid(), "HEDGING", "VINDEX", 0, getCurrentTimestampDbFormat()
        );
        data.clientFraudTypes.add(clientFraudTypesObject);
        return data;
    }

    public static MirrorTradingRuleData getMirrorTradingRuleExitEventEnd31Data() {
        MirrorTradingRuleData data = getMirrorTradingRuleData(mirrorTradingRuleExitEventEnd3_1Client);
        Allure.step("Client has no previous restrictions");
        Allure.step("Client has mirror trading abuse connected account");
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        data.connections.add(getConnection(data.clientHelper, connectedClient));
        ClientFraudTypesObject clientFraudTypesObject = new ClientFraudTypesObject(
                connectedClient.getUcid(), "HEDGING", "VINDEX", 0, getCurrentTimestampDbFormat()
        );
        Allure.step("Set restriction");
        Allure.step("Send alert");
        data.clientFraudTypes.add(clientFraudTypesObject);
        return data;
    }

    public static MirrorTradingRuleData getMirrorTradingRuleExitEventEnd32Data() {
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

    public static MirrorTradingRuleData getMirrorTradingRuleExitEventEnd41Data() {
        Allure.step("Client has no previous restrictions");
        Allure.step("Client has no connected account (or have, bu connected account has no bonuses)");
        Allure.step("Account has a credit is False");
        Allure.step("Exit without alert");
        return getMirrorTradingRuleData(mirrorTradingRuleExitEventEnd4_1Client);
    }

    public static MirrorTradingRuleData getMirrorTradingRuleExitEventEnd42Data() {
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

    public static MirrorTradingRuleData getMirrorTradingRuleExitEventEnd43Data() {
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

    public static MirrorTradingRuleData getMirrorTradingRuleExitEventEnd44Data() {
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

    public static MirrorTradingRuleData getMirrorTradingRuleExitEventEnd51Data() {
        Allure.step("Client has no previous restrictions");
        Allure.step("Client has no connected account (or have, bu connected account has no bonuses)");
        Allure.step("Account has a credit");
        Allure.step("Sum of abuse score > 4 is False");
        Allure.step("Exit without alert");
        MirrorTradingRuleData data = getMirrorTradingRuleData(mirrorTradingRuleExitEventEnd5_1Client);
        MtTbCreditsObject credit = generateCreditsByClient(mirrorTradingRuleExitEventEnd5_1Client);
        data.lnSessionParsedObjectLogin.setTrueIpGeo("CY");
        data.lnSessionParsedObjectRegistration.setTrueIpGeo("CY");
        data.lnSessionParsedObjectLogin.setTrueIpCountryConfidence(86);
        credit.amount = 10D;
        credit.regulator = "VFSC";
        data.mtTbCreditsObjects.add(credit);
        return data;
    }

    public static MirrorTradingRuleData getMirrorTradingRuleExitEventEnd52Data() {
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
        data.lnSessionParsedObjectRegistration.setRiskRating("medium");
        CrmTbDepositObject crmTbDepositObject = generateDepositByClient(data.clientHelper);
        crmTbDepositObject.createTime = data.crmTbUserObject.createTime;
        crmTbDepositObject.paymentChannel = "DebitCard";
        data.crmTbDepositObjects.add(crmTbDepositObject);
        data.lnSessionParsedObjectLogin.setTrueIpGeo("US");
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
        data.lnSessionParsedObjectRegistration.setRiskRating("medium");
        CrmTbDepositObject crmTbDepositObject = generateDepositByClient(data.clientHelper);
        crmTbDepositObject.createTime = data.crmTbUserObject.createTime;
        data.crmTbDepositObjects.add(crmTbDepositObject);
        data.lnSessionParsedObjectLogin.setTrueIpGeo("US");
        // TODO add data for risk free revenue ratio > 0.5
        return data;
    }

    public static MirrorTradingRuleData getMirrorTradingRuleExitEventEnd11Data() {
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
        trade1.setSymbol("EURUSD");
        Allure.step("lexisNexis.riskRating in ('high', 'medium')");
        data.lnSessionParsedObjectRegistration.setRiskRating("medium");
        Allure.step("now() -firstDeposit < 7 days");
        Allure.step("lexisNexis.country = client.country? = false");
        data.lnSessionParsedObjectLogin.setTrueIpCountryConfidence(50);
        Allure.step("dummy trade is true");
        mirrorTrade1.setVolume(0.01);
        mirrorTrade1.setVolumeLots(0.01);
        mirrorTrade2.setVolume(1d);
        mirrorTrade2.setVolumeLots(1d);
        mirrorTrade3.setVolume(1d);
        mirrorTrade3.setVolumeLots(1d);
        mirrorTrade4.setVolume(1d);
        mirrorTrade4.setVolumeLots(1d);
        mirrorTrade5.setVolume(1d);
        mirrorTrade5.setVolumeLots(1d);
        mirrorTrade6.setVolume(1d);
        mirrorTrade6.setVolumeLots(1d);
        Allure.step("count(tradesWithStopouts)/count(trades) > 0.8 = true");
        trade2.setComment("S/O");
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

    public static MirrorTradingRuleData getMirrorTradingRuleExitEventEnd12Data() {
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
        data.lnSessionParsedObjectRegistration.setRiskRating("medium");
        CrmTbDepositObject crmTbDepositObject = generateDepositByClient(data.clientHelper);
        crmTbDepositObject.createTime = data.crmTbUserObject.createTime;
        data.crmTbDepositObjects.add(crmTbDepositObject);
        data.lnSessionParsedObjectLogin.setTrueIpGeo("US");
        // TODO add data for risk free revenue ratio > 0.5
        // TODO add data for trading on news periods?
        data.mt5DealsObjects.add(generateTradeByClient(data.clientHelper));
        MirrorLoginObject mirrorAccountsByTrades = generateMirrorTradesByAccount(data.clientHelper);
        mirrorAccountsByTrades.lots_1 = 3d;
        data.aggrMirrorAccountsByTrades = mirrorAccountsByTrades;
        return data;
    }

    public static MirrorTradingRuleData getMirrorTradingRuleExitEventEnd71Data() {
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
        trade1.setSymbol("EURUSD");
        Allure.step("lexisNexis.riskRating in ('high', 'medium')");
        data.lnSessionParsedObjectRegistration.setRiskRating("medium");
        Allure.step("now() -firstDeposit < 7 days");
        Allure.step("lexisNexis.country = client.country? = false");
        data.lnSessionParsedObjectLogin.setTrueIpCountryConfidence(50);
        Allure.step("count(tradesWithStopouts)/count(trades) > 0.8 = false");
        Allure.step("count(balanceOrdersWithTypeWO) > 0 = false");
        data.crmTbDepositObjects.add(deposit);
        data.mtTbCreditsObjects.add(credit);
        data.mt5DealsObjects.add(trade1);
        return data;
    }

    public static MirrorTradingRuleData getMirrorTradingRuleExitEventEnd72Data() {
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
        trade1.setSymbol("EURUSD");
        Allure.step("lexisNexis.riskRating in ('high', 'medium')");
        data.lnSessionParsedObjectRegistration.setRiskRating("high");
        Allure.step("now() -clientDateRegistration < 7 days = true");
        System.out.println(data.crmTbUserObject.registrationDate);
        System.out.println(data.closeTradeMtEvent.closeTime);
        Allure.step("lexisNexis.country = client.country? = false");
        data.lnSessionParsedObjectLogin.setTrueIpCountryConfidence(50);
        Allure.step("count(tradesWithStopouts)/count(trades) > 0.8 = true");
        trade2.setComment("S/O");
        trade3.setComment("S/O");
        trade4.setComment("S/O");
        trade5.setComment("S/O");
        trade6.setComment("S/O");
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

    public static MirrorTradingRuleData getMirrorTradingRuleExitEventEnd73Data() {
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
        trade1.setSymbol("EURUSD");
        Allure.step("lexisNexis.riskRating in ('high', 'medium')");
        data.lnSessionParsedObjectRegistration.setRiskRating("medium");
        Allure.step("now() -firstDeposit < 7 days");
        Allure.step("lexisNexis.country = client.country? = false");
        data.lnSessionParsedObjectLogin.setTrueIpCountryConfidence(50);
        Allure.step("count(tradesWithStopouts)/count(trades) > 0.8 = true");
        trade2.setComment("S/O");
        Allure.step("count(balanceOrdersWithTypeWO) > 0 = true");
        balanceOrder.comment = "W/O";
        Allure.step("are there mirror deals? = false");
        data.crmTbDepositObjects.add(deposit);
        data.mtTbCreditsObjects.add(credit);
        data.mt5DealsObjects.add(trade1);
        data.mtBalanceOrdersObjects.add(balanceOrder);
        return data;
    }

    public static MirrorTradingRuleData getMirrorTradingRuleExitEventEnd74Data() {
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
        data.lnSessionParsedObjectRegistration.setRiskRating("medium");
        CrmTbDepositObject crmTbDepositObject = generateDepositByClient(data.clientHelper);
        crmTbDepositObject.createTime = data.crmTbUserObject.createTime;
        data.crmTbDepositObjects.add(crmTbDepositObject);
        data.lnSessionParsedObjectLogin.setTrueIpGeo("US");
        // TODO add data for risk free revenue ratio > 0.5
        // TODO add data for no trading on news periods?
        // TODO add data for no dummy trades?
        Mt5DealsCoercedObject mt5DealsObject = generateTradeByClient(data.clientHelper);
        mt5DealsObject.setComment("S/O");
        data.mt5DealsObjects.add(mt5DealsObject);
        data.aggrMirrorAccountsByTrades = generateMirrorTradesByAccount(data.clientHelper);
        return data;
    }

    public static MirrorTradingRuleData getMirrorTradingRuleExitEventEnd75Data() {
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
        data.lnSessionParsedObjectRegistration.setRiskRating("medium");
        CrmTbDepositObject crmTbDepositObject = generateDepositByClient(data.clientHelper);
        crmTbDepositObject.createTime = data.crmTbUserObject.createTime;
        data.crmTbDepositObjects.add(crmTbDepositObject);
        data.lnSessionParsedObjectLogin.setTrueIpGeo("US");
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

        map.put("11", getMirrorTradingRuleExitEventEnd11Data());


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
            deleteEntryFromDb(LEXIS_NEXIS_TABLE_NAME, String.format("user_id = %s", data.lnSessionParsedObjectRegistration.getUserId()));
            deleteEntryFromDb(LEXIS_NEXIS_TABLE_NAME, String.format("user_id = %s", data.lnSessionParsedObjectLogin.getUserId()));
            data.clientFraudTypes.forEach(fraud -> {
                deleteEntryFromDb(BO_CLIENT_FRAUD_TYPES_TABLE_NAME, String.format("ucid = '%s'", fraud.getUcid()));
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
                deleteEntryFromDb(MT5_DEALS_COERCED_TABLE_NAME, String.format("server_id = %s and account = %s", deal.getServerId(), deal.getAccount()));
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
