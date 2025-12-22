package helpers.data.rules.payments.router_rule_crm_payment.connection_search;

import static business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient;
import static business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient;
import static helpers.data.ClientFactory.getRandomClientByBrandAndCountry;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.DataHelper.*;
import static helpers.data.DataSetupHelper.setupData;
import static helpers.database.DbHelper.startSshTunnel;
import static utils.Constants.*;
import static utils.Utils.*;

import business_objects.kafka.crm_events.CrmWithdrawalEventV2;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import helpers.data.enums.Brand;
import helpers.data.enums.Country;
import io.qameta.allure.Description;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import utils.Utils;

public class ConnectionSearchIdAndPmSharingDataFactory {
    private static final ClientHelper connectionSearchRuleClient1 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient2 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient2_1 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient2_2 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient2_3 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient2_4 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient3 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient3_1 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient3_2 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient3_3 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient3_4 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient4 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient5 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient5_1 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient5_2 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient5_3 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient5_4 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient5_5 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient6 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient6_1 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient6_2 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient7 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient8 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient9 = getRandomVantageClientAllFields();

    @Description("Create data for Connection search rule")
    private static DataHelper getConnectionSearchPmAndIdSharingRuleData(ClientHelper client) {
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
                .paymentChannelName("-")
                .paymentMethodCode("CREDIT_CARD")
                .platform("WEB")
                .regulator(data.clientHelper.getRegulator())
                .schemaVersion("1.0")
                .type(CRM_WITHDRAWAL_EVENT)
                .withdrawalAmount(1.0)
                .withdrawalApplicationTime(Instant.now().toString())
                .withdrawalCurrency("EUR")
                .withdrawalId(Long.valueOf(getRandomIntPositive()))
                .status("Risk audit")
                .withdrawalAmountUSD(100d)
                .build();
        return data;
    }

    private static DataHelper getConnectionSearchPmAndIdSharingTest1Data() {
        DataHelper data = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient1);

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
                .paymentChannelName("-")
                .paymentMethodCode(PAYMENT_METHOD_CODE_EWALLET)
                .platform("WEB")
                .regulator(data.clientHelper.getRegulator())
                .schemaVersion("1.0")
                .type(CRM_WITHDRAWAL_EVENT)
                .withdrawalAmount(1.0)
                .withdrawalApplicationTime(Instant.now().toString())
                .withdrawalCurrency("EUR")
                .withdrawalId(Long.valueOf(getRandomIntPositive()))
                .status("Risk audit")
                .withdrawalAmountUSD(100d)
                .eWallet(CrmWithdrawalEventV2.EWallet.builder()
                        .accountName(data.clientHelper.getEmail())
                        .accountNumber("test14@example.com")
                        .build())
                .build();

        // add deposit
        data.crmTbDepositObjects = List.of(generateCrmTbDepositEntityByClient(data.clientHelper));
        data.crmTbDepositObjects.getFirst().setSourceIdSt(1);
        data.crmTbDepositObjects.getFirst().setBrandUid(0);
        data.crmTbDepositObjects.getFirst().setAmount(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setAmountUsd(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setStatusId(5);
        data.crmTbDepositObjects.getFirst().setStatus("Success");
        data.crmTbDepositObjects.getFirst().setPaymentTypeId(1);
        data.crmTbDepositObjects.getFirst().setPaymentType("Credit Card");
        data.crmTbDepositObjects.getFirst().setPaymentChannelId(1);
        data.crmTbDepositObjects.getFirst().setPaymentChannel("web");
        data.crmTbDepositObjects.getFirst().setPaymentSystemAccount("Credit card");

        // add CRYPTO withdrawal
        data.crmTbWithdrawalObjects = List.of(generateCrmTbWithdrawalEntityByClient(data.clientHelper));
        data.crmTbWithdrawalObjects.getFirst().setSourceIdSt(1);
        data.crmTbWithdrawalObjects.getFirst().setPaymentTypeId(4);
        data.crmTbWithdrawalObjects.getFirst().setAmount(BigDecimal.valueOf(10_001d));
        data.crmTbWithdrawalObjects.getFirst().setAmountUsd(BigDecimal.valueOf(10_001d));
        data.crmTbWithdrawalObjects.getFirst().setStatusId(7);
        data.crmTbWithdrawalObjects.getFirst().setStatus("Complete");
        data.crmTbWithdrawalObjects.getFirst().setPaymentType("4");
        data.crmTbWithdrawalObjects.getFirst().setPaymentChannel("web");
        return data;
    }

    private static DataHelper getConnectionSearchPmAndIdSharingTest2Data() {
        DataHelper data = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient2);
        DataHelper data2 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient2_1);
        DataHelper data3 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient2_2);
        DataHelper data4 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient2_3);
        DataHelper data5 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient2_4);

        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_CREDIT_CARD);

        addConnectionByPayoutIdAttribute(data, data2.clientHelper);
        addConnectionByPayoutIdAttribute(data, data3.clientHelper);
        addConnectionByPayoutIdAttribute(data, data4.clientHelper);
        addConnectionByPayoutIdAttribute(data, data5.clientHelper);

        // add deposit
        data.crmTbDepositObjects = List.of(generateCrmTbDepositEntityByClient(data.clientHelper));
        data.crmTbDepositObjects.getFirst().setSourceIdSt(1);
        data.crmTbDepositObjects.getFirst().setBrandUid(0);
        data.crmTbDepositObjects.getFirst().setAmount(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setAmountUsd(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setStatusId(5);
        data.crmTbDepositObjects.getFirst().setStatus("Success");
        data.crmTbDepositObjects.getFirst().setPaymentTypeId(1);
        data.crmTbDepositObjects.getFirst().setPaymentType("Credit Card");
        data.crmTbDepositObjects.getFirst().setPaymentChannelId(1);
        data.crmTbDepositObjects.getFirst().setPaymentChannel("web");
        data.crmTbDepositObjects.getFirst().setPaymentSystemAccount("Credit card");

        // add CRYPTO withdrawal
        data.crmTbWithdrawalObjects = List.of(generateCrmTbWithdrawalEntityByClient(data.clientHelper));
        data.crmTbWithdrawalObjects.getFirst().setSourceIdSt(1);
        data.crmTbWithdrawalObjects.getFirst().setPaymentTypeId(5);
        data.crmTbWithdrawalObjects.getFirst().setAmount(BigDecimal.valueOf(10_001d));
        data.crmTbWithdrawalObjects.getFirst().setAmountUsd(BigDecimal.valueOf(10_001d));
        data.crmTbWithdrawalObjects.getFirst().setStatusId(7);
        data.crmTbWithdrawalObjects.getFirst().setStatus("Complete");
        data.crmTbWithdrawalObjects.getFirst().setPaymentType("5");
        data.crmTbWithdrawalObjects.getFirst().setPaymentChannel("web");

        return data;
    }

    private static DataHelper getConnectionSearchPmAndIdSharingTest3Data() {
        DataHelper data = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient3);
        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_CREDIT_CARD);

        for (int i = 0; i < 6; i++) {
            ClientHelper connectedClient =
                    getRandomClientByBrandAndCountry(Brand.VT, Country.getCountryNameByCodeUppercase("CN"));
            addConnectionByPayoutAndNameBirthAttribute(data, connectedClient);
        }

        // add deposit
        data.crmTbDepositObjects = List.of(generateCrmTbDepositEntityByClient(data.clientHelper));
        data.crmTbDepositObjects = List.of(generateCrmTbDepositEntityByClient(data.clientHelper));
        data.crmTbDepositObjects.getFirst().setSourceIdSt(1);
        data.crmTbDepositObjects.getFirst().setBrandUid(0);
        data.crmTbDepositObjects.getFirst().setAmountSubmitted(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setAmountSubmittedUsd(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setAmount(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setAmountUsd(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setStatusId(5);
        data.crmTbDepositObjects.getFirst().setStatus("Success");
        data.crmTbDepositObjects.getFirst().setPaymentTypeId(1);
        data.crmTbDepositObjects.getFirst().setPaymentType("Credit Card");
        data.crmTbDepositObjects.getFirst().setPaymentChannelId(1);
        data.crmTbDepositObjects.getFirst().setPaymentChannel("web");
        data.crmTbDepositObjects.getFirst().setPaymentSystemAccount("Credit card");

        // add CRYPTO withdrawal
        data.crmTbWithdrawalObjects = List.of(generateCrmTbWithdrawalEntityByClient(data.clientHelper));
        data.crmTbWithdrawalObjects.getFirst().setSourceIdSt(1);
        data.crmTbWithdrawalObjects.getFirst().setPaymentTypeId(4);
        data.crmTbWithdrawalObjects.getFirst().setAmount(BigDecimal.valueOf(10_001d));
        data.crmTbWithdrawalObjects.getFirst().setAmountUsd(BigDecimal.valueOf(10_001d));
        data.crmTbWithdrawalObjects.getFirst().setStatusId(7);
        data.crmTbWithdrawalObjects.getFirst().setStatus("Complete");
        data.crmTbWithdrawalObjects.getFirst().setPaymentType("4");
        data.crmTbWithdrawalObjects.getFirst().setPaymentChannel("web");

        // add CRYPTO withdrawals (6 records) with identical parameters
        data.crmTbWithdrawalObjects = new java.util.ArrayList<>();
        for (int i = 0; i < 6; i++) {
            data.crmTbWithdrawalObjects.add(generateCrmTbWithdrawalEntityByClient(data.clientHelper));
            data.crmTbWithdrawalObjects.getFirst().setSourceIdSt(1);
            data.crmTbWithdrawalObjects.getFirst().setPaymentTypeId(4);
            data.crmTbWithdrawalObjects.getFirst().setAmount(BigDecimal.valueOf(9999d));
            data.crmTbWithdrawalObjects.getFirst().setAmountUsd(BigDecimal.valueOf(9999d));
            data.crmTbWithdrawalObjects.getFirst().setStatusId(7);
            data.crmTbWithdrawalObjects.getFirst().setStatus("Complete");
            data.crmTbWithdrawalObjects.getFirst().setPaymentType("4");
            data.crmTbWithdrawalObjects.getFirst().setPaymentChannel("web");
        }

        return data;
    }

    private static DataHelper getConnectionSearchPmAndIdSharingTest4Data() {
        DataHelper data = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient4);
        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_CREDIT_CARD);

        // add deposit
        data.crmTbDepositObjects = List.of(generateCrmTbDepositEntityByClient(data.clientHelper));
        data.crmTbDepositObjects.getFirst().setSourceIdSt(1);
        data.crmTbDepositObjects.getFirst().setBrandUid(0);
        data.crmTbDepositObjects.getFirst().setAmount(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setAmountUsd(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setStatusId(5);
        data.crmTbDepositObjects.getFirst().setStatus("Success");
        data.crmTbDepositObjects.getFirst().setPaymentTypeId(1);
        data.crmTbDepositObjects.getFirst().setPaymentType("Credit Card");
        data.crmTbDepositObjects.getFirst().setPaymentChannelId(1);
        data.crmTbDepositObjects.getFirst().setPaymentChannel("web");
        data.crmTbDepositObjects.getFirst().setPaymentSystemAccount("Credit card");

        // add non withdrawal
        data.crmTbWithdrawalObjects = List.of(generateCrmTbWithdrawalEntityByClient(data.clientHelper));
        data.crmTbWithdrawalObjects.getFirst().setSourceIdSt(1);
        data.crmTbWithdrawalObjects.getFirst().setPaymentTypeId(5);
        data.crmTbWithdrawalObjects.getFirst().setAmount(BigDecimal.valueOf(10_001d));
        data.crmTbWithdrawalObjects.getFirst().setAmountUsd(BigDecimal.valueOf(10_001d));
        data.crmTbWithdrawalObjects.getFirst().setStatusId(7);
        data.crmTbWithdrawalObjects.getFirst().setStatus("Complete");
        data.crmTbWithdrawalObjects.getFirst().setPaymentType("5");
        data.crmTbWithdrawalObjects.getFirst().setPaymentChannel("web");

        return data;
    }

    private static DataHelper getConnectionSearchPmAndIdSharingTest5Data() {
        DataHelper data = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient5);
        DataHelper data2 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient5_1);
        DataHelper data3 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient5_2);
        DataHelper data4 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient5_3);
        DataHelper data5 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient5_4);
        DataHelper data6 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient5_5);

        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_CRYPTO);

        addConnectionByPayoutIdAttribute(data, data2.clientHelper);
        addConnectionByPayoutIdAttribute(data, data3.clientHelper);
        addConnectionByPayoutIdAttribute(data, data4.clientHelper);
        addConnectionByPayoutIdAttribute(data, data5.clientHelper);
        addConnectionByPayoutIdAttribute(data, data6.clientHelper);

        // add deposit
        data.crmTbDepositObjects = List.of(generateCrmTbDepositEntityByClient(data.clientHelper));
        data.crmTbDepositObjects.getFirst().setSourceIdSt(1);
        data.crmTbDepositObjects.getFirst().setBrandUid(0);
        data.crmTbDepositObjects.getFirst().setAmount(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setAmountUsd(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setStatusId(5);
        data.crmTbDepositObjects.getFirst().setStatus("Success");
        data.crmTbDepositObjects.getFirst().setPaymentTypeId(1);
        data.crmTbDepositObjects.getFirst().setPaymentType("Credit Card");
        data.crmTbDepositObjects.getFirst().setPaymentChannelId(1);
        data.crmTbDepositObjects.getFirst().setPaymentChannel("web");
        data.crmTbDepositObjects.getFirst().setPaymentSystemAccount("Credit card");

        // add CRYPTO withdrawal
        data.crmTbWithdrawalObjects = List.of(generateCrmTbWithdrawalEntityByClient(data.clientHelper));
        data.crmTbWithdrawalObjects.getFirst().setSourceIdSt(1);
        data.crmTbWithdrawalObjects.getFirst().setPaymentTypeId(5);
        data.crmTbWithdrawalObjects.getFirst().setAmount(BigDecimal.valueOf(10_001d));
        data.crmTbWithdrawalObjects.getFirst().setAmountUsd(BigDecimal.valueOf(10_001d));
        data.crmTbWithdrawalObjects.getFirst().setStatusId(7);
        data.crmTbWithdrawalObjects.getFirst().setStatus("Complete");
        data.crmTbWithdrawalObjects.getFirst().setPaymentType("5");
        data.crmTbWithdrawalObjects.getFirst().setPaymentChannel("web");

        return data;
    }

    private static DataHelper getConnectionSearchPmAndIdSharingTest6Data() {
        DataHelper data = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient6);
        DataHelper data2 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient6_1);
        DataHelper data3 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient6_2);

        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_CRYPTO);

        addConnectionByPayoutIdAttribute(data, data2.clientHelper);
        addConnectionByPayoutIdAttribute(data, data3.clientHelper);

        // add deposit
        data.crmTbDepositObjects = List.of(generateCrmTbDepositEntityByClient(data.clientHelper));
        data.crmTbDepositObjects.getFirst().setSourceIdSt(1);
        data.crmTbDepositObjects.getFirst().setBrandUid(0);
        data.crmTbDepositObjects.getFirst().setAmount(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setAmountUsd(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setStatusId(5);
        data.crmTbDepositObjects.getFirst().setStatus("Success");
        data.crmTbDepositObjects.getFirst().setPaymentTypeId(1);
        data.crmTbDepositObjects.getFirst().setPaymentType("Credit Card");
        data.crmTbDepositObjects.getFirst().setPaymentChannelId(1);
        data.crmTbDepositObjects.getFirst().setPaymentChannel("web");
        data.crmTbDepositObjects.getFirst().setPaymentSystemAccount("Credit card");

        // add CRYPTO withdrawal
        data.crmTbWithdrawalObjects = List.of(generateCrmTbWithdrawalEntityByClient(data.clientHelper));
        data.crmTbWithdrawalObjects.getFirst().setSourceIdSt(1);
        data.crmTbWithdrawalObjects.getFirst().setPaymentTypeId(5);
        data.crmTbWithdrawalObjects.getFirst().setAmount(BigDecimal.valueOf(10_001d));
        data.crmTbWithdrawalObjects.getFirst().setAmountUsd(BigDecimal.valueOf(10_001d));
        data.crmTbWithdrawalObjects.getFirst().setStatusId(7);
        data.crmTbWithdrawalObjects.getFirst().setStatus("Complete");
        data.crmTbWithdrawalObjects.getFirst().setPaymentType("5");
        data.crmTbWithdrawalObjects.getFirst().setPaymentChannel("web");
        return data;
    }

    private static DataHelper getConnectionSearchPmAndIdSharingTest7Data() {
        DataHelper data = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient7);
        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_CRYPTO);

        // add deposit
        data.crmTbDepositObjects = List.of(generateCrmTbDepositEntityByClient(data.clientHelper));
        data.crmTbDepositObjects.getFirst().setSourceIdSt(1);
        data.crmTbDepositObjects.getFirst().setBrandUid(0);
        data.crmTbDepositObjects.getFirst().setAmountSubmitted(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setAmountSubmittedUsd(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setAmount(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setAmountUsd(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setStatusId(5);
        data.crmTbDepositObjects.getFirst().setStatus("Success");
        data.crmTbDepositObjects.getFirst().setPaymentTypeId(1);
        data.crmTbDepositObjects.getFirst().setPaymentType("Credit Card");
        data.crmTbDepositObjects.getFirst().setPaymentChannelId(1);
        data.crmTbDepositObjects.getFirst().setPaymentChannel("web");
        data.crmTbDepositObjects.getFirst().setPaymentSystemAccount("Credit card");

        // add CRYPTO withdrawal
        data.crmTbWithdrawalObjects = List.of(generateCrmTbWithdrawalEntityByClient(data.clientHelper));
        data.crmTbWithdrawalObjects.getFirst().setSourceIdSt(1);
        data.crmTbWithdrawalObjects.getFirst().setPaymentTypeId(4);
        data.crmTbWithdrawalObjects.getFirst().setAmount(BigDecimal.valueOf(10_001d));
        data.crmTbWithdrawalObjects.getFirst().setAmountUsd(BigDecimal.valueOf(10_001d));
        data.crmTbWithdrawalObjects.getFirst().setStatusId(7);
        data.crmTbWithdrawalObjects.getFirst().setStatus("Complete");
        data.crmTbWithdrawalObjects.getFirst().setPaymentType("4");
        data.crmTbWithdrawalObjects.getFirst().setPaymentChannel("web");

        return data;
    }

    private static DataHelper getConnectionSearchPmAndIdSharingTest8Data() {
        DataHelper data = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient8);

        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_CRYPTO);

        // add deposit
        data.crmTbDepositObjects = List.of(generateCrmTbDepositEntityByClient(data.clientHelper));
        data.crmTbDepositObjects.getFirst().setSourceIdSt(1);
        data.crmTbDepositObjects.getFirst().setBrandUid(0);
        data.crmTbDepositObjects.getFirst().setAmountSubmitted(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setAmountSubmittedUsd(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setAmount(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setAmountUsd(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setStatusId(5);
        data.crmTbDepositObjects.getFirst().setStatus("Success");
        data.crmTbDepositObjects.getFirst().setPaymentTypeId(1);
        data.crmTbDepositObjects.getFirst().setPaymentType("Credit Card");
        data.crmTbDepositObjects.getFirst().setPaymentChannelId(1);
        data.crmTbDepositObjects.getFirst().setPaymentChannel("web");
        data.crmTbDepositObjects.getFirst().setPaymentSystemAccount("Credit card");

        // add CRYPTO withdrawals (6 records) with identical parameters
        data.crmTbWithdrawalObjects = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            var withdrawal = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
            withdrawal.setSourceIdSt(1);
            withdrawal.setPaymentTypeId(4); // CRYPTO
            withdrawal.setAmount(BigDecimal.valueOf(9999));
            withdrawal.setAmountUsd(BigDecimal.valueOf(9999));
            withdrawal.setStatusId(7);
            withdrawal.setStatus("Complete");
            withdrawal.setPaymentType("4");
            withdrawal.setPaymentChannel("web");
            data.crmTbWithdrawalObjects.add(withdrawal);
        }

        return data;
    }

    private static DataHelper getConnectionSearchPmAndIdSharingTest9Data() {
        DataHelper data = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient9);

        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_CRYPTO);

        // add deposit
        data.crmTbDepositObjects = List.of(generateCrmTbDepositEntityByClient(data.clientHelper));
        data.crmTbDepositObjects.getFirst().setSourceIdSt(1);
        data.crmTbDepositObjects.getFirst().setBrandUid(0);
        data.crmTbDepositObjects.getFirst().setAmountSubmitted(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setAmountSubmittedUsd(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setAmount(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setAmountUsd(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setStatusId(5);
        data.crmTbDepositObjects.getFirst().setStatus("Success");
        data.crmTbDepositObjects.getFirst().setPaymentTypeId(1);
        data.crmTbDepositObjects.getFirst().setPaymentType("Credit Card");
        data.crmTbDepositObjects.getFirst().setPaymentChannelId(1);
        data.crmTbDepositObjects.getFirst().setPaymentChannel("web");
        data.crmTbDepositObjects.getFirst().setPaymentSystemAccount("Credit card");

        // add CRYPTO withdrawal
        data.crmTbWithdrawalObjects = List.of(generateCrmTbWithdrawalEntityByClient(data.clientHelper));
        data.crmTbWithdrawalObjects.getFirst().setSourceIdSt(1);
        data.crmTbWithdrawalObjects.getFirst().setPaymentTypeId(4);
        data.crmTbWithdrawalObjects.getFirst().setAmount(BigDecimal.valueOf(9999));
        data.crmTbWithdrawalObjects.getFirst().setAmountUsd(BigDecimal.valueOf(9999));
        data.crmTbWithdrawalObjects.getFirst().setStatusId(7);
        data.crmTbWithdrawalObjects.getFirst().setStatus("Complete");
        data.crmTbWithdrawalObjects.getFirst().setPaymentType("4");
        data.crmTbWithdrawalObjects.getFirst().setPaymentChannel("web");

        return data;
    }

    public static Map<String, DataHelper> setupConnectionSearchPmAndIdSharingRuleData() {
        startSshTunnel();
        Map<String, DataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("1", getConnectionSearchPmAndIdSharingTest1Data());
        map.put("2", getConnectionSearchPmAndIdSharingTest2Data());
        map.put("3", getConnectionSearchPmAndIdSharingTest3Data());
        map.put("4", getConnectionSearchPmAndIdSharingTest4Data());
        map.put("5", getConnectionSearchPmAndIdSharingTest5Data());
        map.put("6", getConnectionSearchPmAndIdSharingTest6Data());
        map.put("7", getConnectionSearchPmAndIdSharingTest7Data());
        map.put("8", getConnectionSearchPmAndIdSharingTest8Data());
        map.put("9", getConnectionSearchPmAndIdSharingTest9Data());

        setupData(map);

        return map;
    }
}
