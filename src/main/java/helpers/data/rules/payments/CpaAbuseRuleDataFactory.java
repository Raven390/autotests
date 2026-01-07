package helpers.data.rules.payments;

import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClients;
import static business_objects.db.clickhouse.mirror_ucid_table.MirrorUcidObjectFactory.generateMirrorUcidObjectByClient;
import static business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedFactory.generateTradeByClient;
import static helpers.data.ClientFactory.*;
import static helpers.data.DataHelper.createClient;
import static helpers.data.DataSetupHelper.setupData;
import static helpers.data.enums.DateTimeFormat.DATE_AND_TIME;
import static helpers.database.DbHelper.*;
import static utils.Constants.*;
import static utils.Utils.*;

import business_objects.db.clickhouse.client_fraud_types.ClientFraudTypes;
import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntity;
import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntityFactory;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.mirror_ucid_table.MirrorUcidObject;
import business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedObject;
import business_objects.kafka.crm_events.CrmWithdrawalEventV2;
import generator.annotations.RuleTestData;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import helpers.data.enums.FraudTypeOld;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import utils.Utils;

@RuleTestData("cpa-abuse")
public class CpaAbuseRuleDataFactory {
    private static final ClientHelper cpaAbuseRuleClient1 = getRandomVantageClientNoCpaIbRef();
    private static final ClientHelper cpaAbuseRuleClient2 = getRandomVantageClientNoCpaIbRef();
    private static final ClientHelper cpaAbuseRuleClient3 = getRandomVantageClientNoCpaIbRef();
    private static final ClientHelper cpaAbuseRuleClient4 = getRandomVantageClientAllFields();
    private static final ClientHelper cpaAbuseRuleClient5 = getRandomVantageClientAllFields();
    private static final ClientHelper cpaAbuseRuleClient6 = getRandomVantageClientAllFields();
    private static final ClientHelper cpaAbuseRuleClient7 = getRandomVantageClientAllFields();
    private static final ClientHelper cpaAbuseRuleClient8 = getRandomVantageClientAllFields();
    private static final ClientHelper cpaAbuseRuleClient9 = getRandomVantageClientAllFields();
    private static final ClientHelper cpaAbuseRuleClient10 = getRandomVantageClientAllFields();
    private static final ClientHelper cpaAbuseRuleClient11 = getRandomVantageClientAllFields();
    private static final ClientHelper cpaAbuseRuleClient12 = getRandomVantageClientAllFields();
    private static final ClientHelper cpaAbuseRuleClient13 = getRandomVantageClientAllFields();

    @Step("Create data for Mirror trading rule")
    private static DataHelper getCpaAbuseRuleData(ClientHelper client) {
        DataHelper data = new DataHelper();
        createClient(data, client);
        data.crmWithdrawalEventV2 = CrmWithdrawalEventV2.builder()
                .accountType("MT4")
                .binNumber(Utils.getRandomIntPositive().toString())
                .brand(data.clientHelper.getBrand().toLowerCase())
                .checkName("")
                .clientId(data.clientHelper.getUserId())
                .eventDate(Instant.now().toString())
                .expMonth("4")
                .expYear("2030")
                .fullName(data.clientHelper.getFirstName())
                .id(getRandomUuidString())
                .merchantOrderId("VTSG" + getRandomIntPositive())
                .mt4Account(data.clientHelper.getTradingAccount())
                .paymentChannelCode(PAYMENT_PROVIDER_FASAPAY)
                .paymentChannelName("CRYPTO_CHANNEL")
                .paymentMethodCode("CRYPTO")
                .platform("MT4")
                .regulator(data.clientHelper.getRegulator())
                .schemaVersion("2.0")
                .type(CRM_WITHDRAWAL_EVENT)
                .withdrawalAmount(1.1)
                .withdrawalAmountUSD(1.2)
                .withdrawalApplicationTime(Instant.now().toString())
                .withdrawalCurrency("EUR")
                .withdrawalId(getRandomLongPositive())
                .status("Risk Audit")
                .build();
        return data;
    }

    private static DataHelper getCpaAbuseRuleExitEventEnd1Data() {
        DataHelper data = getCpaAbuseRuleData(cpaAbuseRuleClient1);
        data.clientHelper.setCpaId(null);
        return data;
    }

    private static DataHelper getCpaAbuseRuleExitEventEnd2Data() {
        Allure.step("Get client data");
        cpaAbuseRuleClient2.setCpaId(8888);
        DataHelper data = getCpaAbuseRuleData(cpaAbuseRuleClient2);
        Allure.step("Create user object with no CPA");

        Mt5DealsCoercedObject trade1Close = generateTradeByClient(cpaAbuseRuleClient2);
        trade1Close.setComment("trade 1 close");
        trade1Close.setTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 60, 0, 1));
        trade1Close.setTimeUtc(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 60, 0, 1));
        trade1Close.setEntry(1);
        trade1Close.setSymbol("USDEUR");
        trade1Close.setProfit(111.11);
        trade1Close.setProfitUsd(123.12);
        trade1Close.setVolumeLots(0.1);
        data.mt5DealsCoercedObjects.add(trade1Close);

        return data;
    }

    private static DataHelper getCpaAbuseRuleExitEventEnd3Data() {
        Allure.step("Get client data");
        var cpa = 8888;
        cpaAbuseRuleClient3.setCpaId(cpa);
        DataHelper data = getCpaAbuseRuleData(cpaAbuseRuleClient3);
        Allure.step("Create user object with no CPA");

        // MT5 trade with current time
        Mt5DealsCoercedObject trade1Close = generateTradeByClient(cpaAbuseRuleClient3);
        trade1Close.setComment("trade 1 close");
        trade1Close.setTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 59, 13, 59));
        trade1Close.setTimeUtc(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 59, 13, 59));
        trade1Close.setEntry(1);
        trade1Close.setSymbol("USDEUR");
        trade1Close.setProfit(111.11);
        trade1Close.setProfitUsd(123.12);
        trade1Close.setVolumeLots(0.1);
        data.mt5DealsCoercedObjects.add(trade1Close);
        data.connectedUsers = (generateUserByClients(getRandomClientWithCpa(500, cpa)));

        return data;
    }

    private static DataHelper getCpaAbuseRuleExitEventEnd4Data() {
        Allure.step("Get client data");
        DataHelper data = getCpaAbuseRuleData(cpaAbuseRuleClient4);

        Allure.step("Client has cpa abuse connected account");
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        // data.connections.add(getConnection(data.clientHelper, connectedClient));
        ClientFraudTypes clientFraudTypes = new ClientFraudTypes(
                connectedClient.getUcid(),
                FraudTypeOld.CPA_ABUSE.getKey(),
                FRAUD_TYPE_SOURCE_VINDEX,
                0,
                getCurrentTimestampDbFormat());
        CrmTbUserObject connectedUserCrmTbUserObject = generateUserByClient(connectedClient);
        data.connectedUsers.add(connectedUserCrmTbUserObject);

        data.clientFraudTypes.add(clientFraudTypes);

        // MT5 trade with current time
        Mt5DealsCoercedObject trade1Close = generateTradeByClient(cpaAbuseRuleClient4);
        trade1Close.setComment("trade 1 close");
        trade1Close.setTime(getCurrentTimestampDbFormat());
        trade1Close.setTimeUtc(trade1Close.getTime());
        trade1Close.setEntry(1);
        trade1Close.setSymbol("USDEUR");
        trade1Close.setProfit(111.11);
        trade1Close.setProfitUsd(123.12);
        trade1Close.setVolumeLots(0.1);
        data.mt5DealsCoercedObjects.add(trade1Close);
        Allure.step("Set restriction");
        Allure.step("Send alert");
        return data;
    }

    private static DataHelper getCpaAbuseRuleExitEventEnd5p1Data() {
        Allure.step("Get client data");
        DataHelper data = getCpaAbuseRuleData(cpaAbuseRuleClient5);
        Allure.step("Send alert");

        // MT5 trade with current time
        Mt5DealsCoercedObject trade1Close = generateTradeByClient(cpaAbuseRuleClient5);
        trade1Close.setComment("trade 1 close");
        trade1Close.setTime(getCurrentTimestampDbFormat());
        trade1Close.setTimeUtc(trade1Close.getTime());
        trade1Close.setEntry(1);
        trade1Close.setSymbol("USDEUR");
        trade1Close.setProfit(2500.01);
        trade1Close.setProfitUsd(2500.01);
        trade1Close.setVolumeLots(0.1);
        data.mt5DealsCoercedObjects.add(trade1Close);
        return data;
    }

    private static DataHelper getCpaAbuseRuleExitEventEnd5p2Data() {
        Allure.step("Get client data");
        DataHelper data = getCpaAbuseRuleData(cpaAbuseRuleClient6);
        Allure.step("Send alert");

        // MT5 trade with current time
        Mt5DealsCoercedObject trade1Close = generateTradeByClient(cpaAbuseRuleClient6);
        trade1Close.setComment("trade 1 close");
        trade1Close.setTime(getCurrentTimestampDbFormat());
        trade1Close.setTimeUtc(trade1Close.getTime());
        trade1Close.setEntry(1);
        trade1Close.setSymbol("USDEUR");
        trade1Close.setProfit(-700.01);
        trade1Close.setProfitUsd(-700.01);
        trade1Close.setVolumeLots(0.1);
        data.mt5DealsCoercedObjects.add(trade1Close);
        return data;
    }

    private static DataHelper getCpaAbuseRuleExitEventEnd6Data() {
        Allure.step("Get client data");
        DataHelper data = getCpaAbuseRuleData(cpaAbuseRuleClient7);
        Allure.step("Client has not cpa abuse connected account");
        Allure.step("At least 70% have any CPA value");
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        ClientHelper connectedClient2 = getRandomVantageClientAllFields();
        ClientHelper connectedClient3 = getRandomVantageClientAllFields();
        ClientHelper connectedClient4 = getRandomVantageClientAllFields();
        CrmTbUserObject connectedUserCrmTbUserObject = generateUserByClient(connectedClient);
        CrmTbUserObject connectedUserCrmTbUserObject2 = generateUserByClient(connectedClient2);
        CrmTbUserObject connectedUserCrmTbUserObject3 = generateUserByClient(connectedClient3);
        CrmTbUserObject connectedUserCrmTbUserObject4 = generateUserByClient(connectedClient4);
        connectedUserCrmTbUserObject.cpaId = data.clientHelper.getCpaId();
        connectedUserCrmTbUserObject2.cpaId = data.clientHelper.getCpaId();
        connectedUserCrmTbUserObject3.cpaId = data.clientHelper.getCpaId();
        connectedUserCrmTbUserObject4.cpaId = 8889;
        data.connectedUsers.add(connectedUserCrmTbUserObject);
        data.connectedUsers.add(connectedUserCrmTbUserObject2);
        data.connectedUsers.add(connectedUserCrmTbUserObject3);
        data.connectedUsers.add(connectedUserCrmTbUserObject4);
        //        data.connections.add(getConnection(data.clientHelper, connectedClient));
        //        data.connections.add(getConnection(data.clientHelper, connectedClient2));
        //        data.connections.add(getConnection(data.clientHelper, connectedClient3));
        //        data.connections.add(getConnection(data.clientHelper, connectedClient4));
        Allure.step("Set restriction");
        Allure.step("Send alert");

        // MT5 trade with current time
        Mt5DealsCoercedObject trade1Close = generateTradeByClient(cpaAbuseRuleClient7);
        trade1Close.setComment("trade 1 close");
        trade1Close.setTime(getCurrentTimestampDbFormat());
        trade1Close.setTimeUtc(trade1Close.getTime());
        trade1Close.setEntry(1);
        trade1Close.setSymbol("USDEUR");
        trade1Close.setProfit(2501.01);
        trade1Close.setProfitUsd(2499.99);
        trade1Close.setVolumeLots(0.1);
        data.mt5DealsCoercedObjects.add(trade1Close);

        return data;
    }

    private static DataHelper getCpaAbuseRuleExitEventEnd7p1Data() {
        Allure.step("Get client data");
        DataHelper data = getCpaAbuseRuleData(cpaAbuseRuleClient8);
        Allure.step("Client has not cpa abuse connected account");
        Allure.step("100% have same CPA value");
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        ClientHelper connectedClient2 = getRandomVantageClientAllFields();
        ClientHelper connectedClient3 = getRandomVantageClientAllFields();
        CrmTbUserObject connectedUserCrmTbUserObject = generateUserByClient(connectedClient);
        CrmTbUserObject connectedUserCrmTbUserObject2 = generateUserByClient(connectedClient2);
        CrmTbUserObject connectedUserCrmTbUserObject3 = generateUserByClient(connectedClient3);
        connectedUserCrmTbUserObject.cpaId = data.clientHelper.getCpaId();
        connectedUserCrmTbUserObject2.cpaId = data.clientHelper.getCpaId();
        connectedUserCrmTbUserObject3.cpaId = data.clientHelper.getCpaId();
        data.connectedUsers.add(connectedUserCrmTbUserObject);
        data.connectedUsers.add(connectedUserCrmTbUserObject2);
        data.connectedUsers.add(connectedUserCrmTbUserObject3);
        //        data.connections.add(getConnection(data.clientHelper, connectedClient));
        //        data.connections.add(getConnection(data.clientHelper, connectedClient2));
        //        data.connections.add(getConnection(data.clientHelper, connectedClient3));
        Allure.step("Set restriction");
        Allure.step("Send alert");

        // MT5 trade with current time
        Mt5DealsCoercedObject trade1Close = generateTradeByClient(cpaAbuseRuleClient8);
        trade1Close.setComment("trade 1 close");
        trade1Close.setTime(getCurrentTimestampDbFormat());
        trade1Close.setTimeUtc(trade1Close.getTime());
        trade1Close.setEntry(1);
        trade1Close.setSymbol("USDEUR");
        trade1Close.setProfit(2501.01);
        trade1Close.setProfitUsd(2499.99);
        trade1Close.setVolumeLots(0.1);
        data.mt5DealsCoercedObjects.add(trade1Close);

        CrmTbDepositEntity deposit = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(cpaAbuseRuleClient8);
        deposit.setPaymentChannel("Crypto");
        deposit.setAmountUsd(BigDecimal.valueOf(500d));
        data.crmTbDepositObjects.add(deposit);

        return data;
    }

    private static DataHelper getCpaAbuseRuleExitEventEnd7p2Data() {
        Allure.step("Get client data");
        DataHelper data = getCpaAbuseRuleData(cpaAbuseRuleClient9);
        Allure.step("Client has not cpa abuse connected account");
        Allure.step("100% have same CPA value");
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        ClientHelper connectedClient2 = getRandomVantageClientAllFields();
        ClientHelper connectedClient3 = getRandomVantageClientAllFields();
        ClientHelper connectedClient4 = getRandomVantageClientAllFields();
        CrmTbUserObject connectedUserCrmTbUserObject = generateUserByClient(connectedClient);
        CrmTbUserObject connectedUserCrmTbUserObject2 = generateUserByClient(connectedClient2);
        CrmTbUserObject connectedUserCrmTbUserObject3 = generateUserByClient(connectedClient3);
        CrmTbUserObject connectedUserCrmTbUserObject4 = generateUserByClient(connectedClient4);
        connectedUserCrmTbUserObject.cpaId = data.clientHelper.getCpaId();
        connectedUserCrmTbUserObject2.cpaId = data.clientHelper.getCpaId();
        connectedUserCrmTbUserObject3.cpaId = 8881;
        connectedUserCrmTbUserObject4.cpaId = 8881;
        data.connectedUsers.add(connectedUserCrmTbUserObject);
        data.connectedUsers.add(connectedUserCrmTbUserObject2);
        data.connectedUsers.add(connectedUserCrmTbUserObject3);
        data.connectedUsers.add(connectedUserCrmTbUserObject4);
        //        data.connections.add(getConnection(data.clientHelper, connectedClient));
        //        data.connections.add(getConnection(data.clientHelper, connectedClient2));
        //        data.connections.add(getConnection(data.clientHelper, connectedClient3));
        //        data.connections.add(getConnection(data.clientHelper, connectedClient4));
        Allure.step("Set restriction");
        Allure.step("Send alert");

        // MT5 trade with current time
        Mt5DealsCoercedObject trade1Close = generateTradeByClient(cpaAbuseRuleClient9);
        trade1Close.setComment("trade 1 close");
        trade1Close.setTime(getCurrentTimestampDbFormat());
        trade1Close.setTimeUtc(trade1Close.getTime());
        trade1Close.setEntry(1);
        trade1Close.setSymbol("USDEUR");
        trade1Close.setProfit(2501.01);
        trade1Close.setProfitUsd(2499.99);
        trade1Close.setVolumeLots(0.1);
        data.mt5DealsCoercedObjects.add(trade1Close);

        CrmTbDepositEntity deposit = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(cpaAbuseRuleClient9);
        deposit.setPaymentChannel("Crypto");
        deposit.setAmountUsd(BigDecimal.valueOf(500d));
        data.crmTbDepositObjects.add(deposit);

        return data;
    }

    private static DataHelper getCpaAbuseRuleExitEventEnd8p1Data() {
        Allure.step("Get client data");
        DataHelper data = getCpaAbuseRuleData(cpaAbuseRuleClient10);
        Allure.step("Client has not cpa abuse connected account");
        Allure.step("100% have same CPA value");
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        ClientHelper connectedClient2 = getRandomVantageClientAllFields();
        ClientHelper connectedClient3 = getRandomVantageClientAllFields();
        ClientHelper connectedClient4 = getRandomVantageClientAllFields();
        CrmTbUserObject connectedUserCrmTbUserObject = generateUserByClient(connectedClient);
        CrmTbUserObject connectedUserCrmTbUserObject2 = generateUserByClient(connectedClient2);
        CrmTbUserObject connectedUserCrmTbUserObject3 = generateUserByClient(connectedClient3);
        CrmTbUserObject connectedUserCrmTbUserObject4 = generateUserByClient(connectedClient4);
        connectedUserCrmTbUserObject.cpaId = data.clientHelper.getCpaId();
        connectedUserCrmTbUserObject2.cpaId = data.clientHelper.getCpaId();
        connectedUserCrmTbUserObject3.cpaId = 8881;
        connectedUserCrmTbUserObject4.cpaId = 8881;
        data.connectedUsers.add(connectedUserCrmTbUserObject);
        data.connectedUsers.add(connectedUserCrmTbUserObject2);
        data.connectedUsers.add(connectedUserCrmTbUserObject3);
        data.connectedUsers.add(connectedUserCrmTbUserObject4);
        //        data.connections.add(getConnection(data.clientHelper, connectedClient));
        //        data.connections.add(getConnection(data.clientHelper, connectedClient2));
        //        data.connections.add(getConnection(data.clientHelper, connectedClient3));
        //        data.connections.add(getConnection(data.clientHelper, connectedClient4));
        Allure.step("Set restriction");
        Allure.step("Send alert");

        // MT5 trade with current time
        Mt5DealsCoercedObject trade1Close = generateTradeByClient(cpaAbuseRuleClient10);
        trade1Close.setComment("trade 1 close");
        trade1Close.setTime(getCurrentTimestampDbFormat());
        trade1Close.setTimeUtc(trade1Close.getTime());
        trade1Close.setEntry(1);
        trade1Close.setSymbol("USDEUR");
        trade1Close.setProfit(2501.01);
        trade1Close.setProfitUsd(2499.99);
        trade1Close.setVolumeLots(0.1);
        data.mt5DealsCoercedObjects.add(trade1Close);

        CrmTbDepositEntity deposit = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(cpaAbuseRuleClient10);
        deposit.setPaymentChannel("Bank");
        deposit.setAmountUsd(BigDecimal.valueOf(550.01));
        deposit.setCreateTime(OffsetDateTime.now().minusDays(60));
        data.crmTbDepositObjects.add(deposit);

        return data;
    }

    private static DataHelper getCpaAbuseRuleExitEventEnd8p2Data() {
        Allure.step("Get client data");
        DataHelper data = getCpaAbuseRuleData(cpaAbuseRuleClient11);
        Allure.step("Client has not cpa abuse connected account");
        Allure.step("50% have same CPA value");
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        ClientHelper connectedClient2 = getRandomVantageClientAllFields();
        ClientHelper connectedClient3 = getRandomVantageClientAllFields();
        ClientHelper connectedClient4 = getRandomVantageClientAllFields();
        CrmTbUserObject connectedUserCrmTbUserObject = generateUserByClient(connectedClient);
        CrmTbUserObject connectedUserCrmTbUserObject2 = generateUserByClient(connectedClient2);
        CrmTbUserObject connectedUserCrmTbUserObject3 = generateUserByClient(connectedClient3);
        CrmTbUserObject connectedUserCrmTbUserObject4 = generateUserByClient(connectedClient4);
        connectedUserCrmTbUserObject.cpaId = data.clientHelper.getCpaId();
        connectedUserCrmTbUserObject2.cpaId = data.clientHelper.getCpaId();
        connectedUserCrmTbUserObject3.cpaId = 8881;
        connectedUserCrmTbUserObject4.cpaId = 8881;
        data.connectedUsers.add(connectedUserCrmTbUserObject);
        data.connectedUsers.add(connectedUserCrmTbUserObject2);
        data.connectedUsers.add(connectedUserCrmTbUserObject3);
        data.connectedUsers.add(connectedUserCrmTbUserObject4);
        //        data.connections.add(getConnection(data.clientHelper, connectedClient));
        //        data.connections.add(getConnection(data.clientHelper, connectedClient2));
        //        data.connections.add(getConnection(data.clientHelper, connectedClient3));
        //        data.connections.add(getConnection(data.clientHelper, connectedClient4));

        Allure.step("Any mirror trades? = true");
        MirrorUcidObject mirrorUcid = generateMirrorUcidObjectByClient(cpaAbuseRuleClient11);
        data.mirrorUcidObjects.add(mirrorUcid);

        Allure.step("HFT trades = true");
        Mt5DealsCoercedObject trade1Open = generateTradeByClient(cpaAbuseRuleClient11);
        Mt5DealsCoercedObject trade1Close = generateTradeByClient(cpaAbuseRuleClient11);

        trade1Open.setTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 2));
        trade1Open.setTimeUtc(trade1Open.getTime());
        trade1Open.setEntry(0);
        trade1Open.setVolumeLots(8.1);
        trade1Close.setPositionId(trade1Open.getPositionId());
        trade1Close.setComment("trade 1 close");
        trade1Close.setTime(getCurrentTimestampDbFormat());
        trade1Close.setTimeUtc(trade1Close.getTime());
        trade1Close.setEntry(1);
        trade1Close.setSymbol("USDEUR");
        trade1Close.setProfit(111.11);
        trade1Close.setProfitUsd(123.12);
        trade1Close.setVolumeLots(8.1);

        data.mt5DealsCoercedObjects.add(trade1Close);
        data.mt5DealsCoercedObjects.add(trade1Open);

        Allure.step("Deposit paymentType = Cryptocurrency , value 449.99");
        CrmTbDepositEntity deposit = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(cpaAbuseRuleClient11);
        deposit.setPaymentChannel("Cryptocurrency");
        deposit.setAmountUsd(BigDecimal.valueOf(449.99));
        deposit.setCreateTime(OffsetDateTime.now().minusDays(60));
        data.crmTbDepositObjects.add(deposit);

        Allure.step("Send alert");
        return data;
    }

    private static DataHelper getCpaAbuseRuleExitEventEnd9p1Data() {
        Allure.step("Get client data");
        DataHelper data = getCpaAbuseRuleData(cpaAbuseRuleClient12);
        Allure.step("Client has not cpa abuse connected account");
        Allure.step("50% have same CPA value");
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        ClientHelper connectedClient2 = getRandomVantageClientAllFields();
        ClientHelper connectedClient3 = getRandomVantageClientAllFields();
        ClientHelper connectedClient4 = getRandomVantageClientAllFields();
        CrmTbUserObject connectedUserCrmTbUserObject = generateUserByClient(connectedClient);
        CrmTbUserObject connectedUserCrmTbUserObject2 = generateUserByClient(connectedClient2);
        CrmTbUserObject connectedUserCrmTbUserObject3 = generateUserByClient(connectedClient3);
        CrmTbUserObject connectedUserCrmTbUserObject4 = generateUserByClient(connectedClient4);
        Allure.step("Have connection to clients with the same CPA");
        connectedUserCrmTbUserObject.cpaId = data.clientHelper.getCpaId();
        connectedUserCrmTbUserObject2.cpaId = data.clientHelper.getCpaId();
        connectedUserCrmTbUserObject3.cpaId = 8881;
        connectedUserCrmTbUserObject4.cpaId = 8881;
        data.connectedUsers.add(connectedUserCrmTbUserObject);
        data.connectedUsers.add(connectedUserCrmTbUserObject2);
        data.connectedUsers.add(connectedUserCrmTbUserObject3);
        data.connectedUsers.add(connectedUserCrmTbUserObject4);
        //        data.connections.add(getConnection(data.clientHelper, connectedClient));
        //        data.connections.add(getConnection(data.clientHelper, connectedClient2));
        //        data.connections.add(getConnection(data.clientHelper, connectedClient3));
        //        data.connections.add(getConnection(data.clientHelper, connectedClient4));

        Allure.step("Any mirror trades? = false");

        Allure.step("HFT trades = false");
        Mt5DealsCoercedObject trade1Open = generateTradeByClient(cpaAbuseRuleClient12);
        Mt5DealsCoercedObject trade1Close = generateTradeByClient(cpaAbuseRuleClient12);

        trade1Open.setTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 1, 2));
        trade1Open.setTimeUtc(trade1Open.getTime());
        trade1Open.setEntry(0);
        trade1Open.setVolumeLots(4.0);
        trade1Close.setPositionId(trade1Open.getPositionId());
        trade1Close.setComment("trade 1 close");
        trade1Close.setTime(getCurrentTimestampDbFormat());
        trade1Close.setTimeUtc(trade1Close.getTime());
        trade1Close.setEntry(1);
        trade1Close.setSymbol("USDEUR");
        trade1Close.setProfit(111.11);
        trade1Close.setProfitUsd(123.12);
        trade1Close.setVolumeLots(4.0);

        data.mt5DealsCoercedObjects.add(trade1Close);
        data.mt5DealsCoercedObjects.add(trade1Open);

        Allure.step("Deposit paymentType = Other , value 500.00");
        CrmTbDepositEntity deposit = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(cpaAbuseRuleClient12);
        deposit.setPaymentChannel("Other");
        deposit.setAmountUsd(BigDecimal.valueOf(500d));
        deposit.setCreateTime(OffsetDateTime.now().minusDays(60));
        data.crmTbDepositObjects.add(deposit);

        Allure.step("Send alert");
        return data;
    }

    private static DataHelper getCpaAbuseRuleExitEventEnd9p2Data() {
        Allure.step("Get client data");
        DataHelper data = getCpaAbuseRuleData(cpaAbuseRuleClient13);
        Allure.step("Client has not cpa abuse connected account");
        Allure.step("50% have same CPA value");
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        ClientHelper connectedClient2 = getRandomVantageClientAllFields();
        ClientHelper connectedClient3 = getRandomVantageClientAllFields();
        ClientHelper connectedClient4 = getRandomVantageClientAllFields();
        CrmTbUserObject connectedUserCrmTbUserObject = generateUserByClient(connectedClient);
        CrmTbUserObject connectedUserCrmTbUserObject2 = generateUserByClient(connectedClient2);
        CrmTbUserObject connectedUserCrmTbUserObject3 = generateUserByClient(connectedClient3);
        CrmTbUserObject connectedUserCrmTbUserObject4 = generateUserByClient(connectedClient4);
        Allure.step("Have no connection to clients with the same CPA");
        connectedUserCrmTbUserObject.cpaId = 8881;
        connectedUserCrmTbUserObject2.cpaId = 8881;
        connectedUserCrmTbUserObject3.cpaId = 8881;
        connectedUserCrmTbUserObject4.cpaId = 8881;
        data.connectedUsers.add(connectedUserCrmTbUserObject);
        data.connectedUsers.add(connectedUserCrmTbUserObject2);
        data.connectedUsers.add(connectedUserCrmTbUserObject3);
        data.connectedUsers.add(connectedUserCrmTbUserObject4);
        //        data.connections.add(getConnection(data.clientHelper, connectedClient));
        //        data.connections.add(getConnection(data.clientHelper, connectedClient2));
        //        data.connections.add(getConnection(data.clientHelper, connectedClient3));
        //        data.connections.add(getConnection(data.clientHelper, connectedClient4));

        Allure.step("Any mirror trades? = false");

        Allure.step("HFT trades = false");
        Mt5DealsCoercedObject trade1Open = generateTradeByClient(cpaAbuseRuleClient13);
        Mt5DealsCoercedObject trade1Close = generateTradeByClient(cpaAbuseRuleClient13);

        trade1Open.setTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 1, 2));
        trade1Open.setTimeUtc(trade1Open.getTime());
        trade1Open.setEntry(0);
        trade1Open.setVolumeLots(4.0);
        trade1Close.setPositionId(trade1Open.getPositionId());
        trade1Close.setComment("trade 1 close");
        trade1Close.setTime(getCurrentTimestampDbFormat());
        trade1Close.setTimeUtc(trade1Close.getTime());
        trade1Close.setEntry(1);
        trade1Close.setSymbol("USDEUR");
        trade1Close.setProfit(111.11);
        trade1Close.setProfitUsd(123.12);
        trade1Close.setVolumeLots(4.0);

        data.mt5DealsCoercedObjects.add(trade1Close);
        data.mt5DealsCoercedObjects.add(trade1Open);

        Allure.step("Deposit paymentType = Other , value 500.00");
        CrmTbDepositEntity deposit = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(cpaAbuseRuleClient13);
        deposit.setPaymentChannel("Other");
        deposit.setAmountUsd(BigDecimal.valueOf(500.00));
        deposit.setCreateTime(OffsetDateTime.now().minusDays(60));
        data.crmTbDepositObjects.add(deposit);

        Allure.step("Send alert");
        return data;
    }

    public static Map<String, DataHelper> setupCpaAbuseRuleData() {
        startSshTunnel();
        Map<String, DataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("1", getCpaAbuseRuleExitEventEnd1Data());
        //        map.put("2", getCpaAbuseRuleExitEventEnd2Data());
        //        map.put("3", getCpaAbuseRuleExitEventEnd3Data());
        //        map.put("4", getCpaAbuseRuleExitEventEnd4Data());
        //        map.put("5", getCpaAbuseRuleExitEventEnd5p1Data());
        //        map.put("6", getCpaAbuseRuleExitEventEnd5p2Data());
        //        map.put("7", getCpaAbuseRuleExitEventEnd6Data());
        //        map.put("8", getCpaAbuseRuleExitEventEnd7p1Data());
        //        map.put("9", getCpaAbuseRuleExitEventEnd7p2Data());
        //        map.put("10", getCpaAbuseRuleExitEventEnd8p1Data());
        //        map.put("11", getCpaAbuseRuleExitEventEnd8p2Data());
        //        map.put("12", getCpaAbuseRuleExitEventEnd9p1Data());
        //        map.put("13", getCpaAbuseRuleExitEventEnd9p2Data());

        // Loop through the list with data and insert all the data into the according tables
        setupData(map);
        return map;
    }
}
