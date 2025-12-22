package helpers.data.rules.payments.router_rule_crm_payment.connection_search;

import static business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient;
import static business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.DataHelper.*;
import static helpers.data.DataSetupHelper.setupData;
import static helpers.database.DbHelper.startSshTunnel;
import static utils.Constants.*;
import static utils.Utils.getRandomIntPositive;
import static utils.Utils.getRandomUuidString;

import business_objects.kafka.crm_events.CrmWithdrawalEventV2;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import io.qameta.allure.Description;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import utils.Utils;

public class ConnectionSearchDataFactory {
    private static final ClientHelper connectionSearchRuleClient1 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient2 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient3 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient4 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient4_1 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient5 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient5_1 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient6 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient7 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient8 = getRandomVantageClientAllFields();

    @Description("Create data for Connection search rule")
    private static DataHelper getConnectionSearchRuleData(ClientHelper client) {
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

    private static DataHelper getConnectionSearchTest1Data() {
        DataHelper data = getConnectionSearchRuleData(connectionSearchRuleClient1);

        return data;
    }

    private static DataHelper getConnectionSearchTest2Data() {
        DataHelper data = getConnectionSearchRuleData(connectionSearchRuleClient2);

        return data;
    }

    private static DataHelper getConnectionSearchTest3Data() {
        DataHelper data = getConnectionSearchRuleData(connectionSearchRuleClient3);
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
        return data;
    }

    private static DataHelper getConnectionSearchTest4Data() {
        DataHelper data = getConnectionSearchRuleData(connectionSearchRuleClient4);
        DataHelper data2 = getConnectionSearchRuleData(connectionSearchRuleClient4_1);

        addConnectionByEmailPhoneAttribute(data, data2.clientHelper, 1d);
        return data;
    }

    private static DataHelper getConnectionSearchTest5Data() {
        DataHelper data = getConnectionSearchRuleData(connectionSearchRuleClient5);
        DataHelper data2 = getConnectionSearchRuleData(connectionSearchRuleClient5_1);

        addConnectionByEmailPhoneAttribute(data, data2.clientHelper, 1d);

        // add deposit exactly as in the provided INSERT

        data.crmTbDepositObjects = List.of(generateCrmTbDepositEntityByClient(data2.clientHelper));
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
        return data;
    }

    private static DataHelper getConnectionSearchTest6Data() {
        DataHelper data = getConnectionSearchRuleData(connectionSearchRuleClient6);

        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_CREDIT_CARD);

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
        data.crmTbWithdrawalObjects.getFirst().setPaymentTypeId(5);
        data.crmTbWithdrawalObjects.getFirst().setAmount(BigDecimal.valueOf(10_001d));
        data.crmTbWithdrawalObjects.getFirst().setAmountUsd(BigDecimal.valueOf(10_001d));
        data.crmTbWithdrawalObjects.getFirst().setStatusId(7);
        data.crmTbWithdrawalObjects.getFirst().setStatus("Complete");
        data.crmTbWithdrawalObjects.getFirst().setPaymentType("5");
        data.crmTbWithdrawalObjects.getFirst().setPaymentChannel("web");

        return data;
    }

    private static DataHelper getConnectionSearchTest7Data() {
        DataHelper data = getConnectionSearchRuleData(connectionSearchRuleClient7);

        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_CRYPTO);

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

        // add withdrawal exactly as in the provided INSERT
        data.crmTbWithdrawalObjects = List.of(generateCrmTbWithdrawalEntityByClient(data.clientHelper));
        data.crmTbWithdrawalObjects.getFirst().setSourceIdSt(1);
        data.crmTbWithdrawalObjects.getFirst().setPaymentTypeId(4);
        data.crmTbWithdrawalObjects.getFirst().setAmount(BigDecimal.valueOf(50_001d));
        data.crmTbWithdrawalObjects.getFirst().setAmountUsd(BigDecimal.valueOf(50_001d));
        data.crmTbWithdrawalObjects.getFirst().setStatusId(7);
        data.crmTbWithdrawalObjects.getFirst().setStatus("Complete");
        data.crmTbWithdrawalObjects.getFirst().setPaymentType("4");
        data.crmTbWithdrawalObjects.getFirst().setPaymentChannel("web");

        return data;
    }

    private static DataHelper getConnectionSearchTest8Data() {
        DataHelper data = getConnectionSearchRuleData(connectionSearchRuleClient8);

        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_CRYPTO);

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

        // add withdrawal exactly as in the provided INSERT
        data.crmTbWithdrawalObjects = List.of(generateCrmTbWithdrawalEntityByClient(data.clientHelper));
        data.crmTbWithdrawalObjects.getFirst().setSourceIdSt(1);
        data.crmTbWithdrawalObjects.getFirst().setPaymentTypeId(4);
        data.crmTbWithdrawalObjects.getFirst().setAmount(BigDecimal.valueOf(9000));
        data.crmTbWithdrawalObjects.getFirst().setAmountUsd(BigDecimal.valueOf(9000));
        data.crmTbWithdrawalObjects.getFirst().setStatusId(7);
        data.crmTbWithdrawalObjects.getFirst().setStatus("Complete");
        data.crmTbWithdrawalObjects.getFirst().setPaymentType("4");
        data.crmTbWithdrawalObjects.getFirst().setPaymentChannel("web");

        return data;
    }

    public static Map<String, DataHelper> setupConnectionSearchRuleData() {
        startSshTunnel();
        Map<String, DataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("1", getConnectionSearchTest1Data());
        map.put("2", getConnectionSearchTest2Data());
        map.put("3", getConnectionSearchTest3Data());
        map.put("4", getConnectionSearchTest4Data());
        map.put("5", getConnectionSearchTest5Data());
        map.put("6", getConnectionSearchTest6Data());
        map.put("7", getConnectionSearchTest7Data());
        map.put("8", getConnectionSearchTest8Data());

        setupData(map);

        return map;
    }
}
