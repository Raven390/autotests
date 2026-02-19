package helpers.data.rules.payments.router_rule_crm_payment.connection_search;

import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.DataHelper.*;
import static helpers.data.DataSetupHelper.setupData;
import static helpers.data.enums.FraudType.*;
import static helpers.database.DbHelper.startSshTunnel;
import static utils.Constants.*;
import static utils.Utils.getRandomIntPositive;
import static utils.Utils.getRandomUuidString;

import business_objects.kafka.crm_events.CrmWithdrawalEventV2;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import helpers.data.enums.FraudTypeStatus;
import helpers.data.enums.rule_engine.Event;
import io.qameta.allure.Description;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import utils.Utils;

public class ConnectionSearchPaymentAbuseDataFactory {
    private static final ClientHelper client1 = getRandomVantageClientAllFields();
    private static final ClientHelper client2 = getRandomVantageClientAllFields();
    private static final ClientHelper client2_1 = getRandomVantageClientAllFields();
    private static final ClientHelper client3 = getRandomVantageClientAllFields();
    private static final ClientHelper client3_1 = getRandomVantageClientAllFields();

    @Description("Create data for Connection search rule")
    private static DataHelper getRuleData(ClientHelper client) {
        DataHelper data = new DataHelper();
        data.createClient(client);

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
                .paymentMethodCode(PAYMENT_METHOD_CODE_CRYPTO)
                .platform("WEB")
                .regulator(data.clientHelper.getRegulator())
                .schemaVersion("1.0")
                .type(Event.CRM_WITHDRAWAL_EVENT.getName())
                .withdrawalAmount(1.0)
                .withdrawalApplicationTime(Instant.now().toString())
                .withdrawalCurrency("EUR")
                .withdrawalId(Long.valueOf(getRandomIntPositive()))
                .status("Risk audit")
                .withdrawalAmountUSD(100d)
                .build();

        return data;
    }

    private static DataHelper getTest1Data() {
        DataHelper data = getRuleData(client1);
        data.addCreditCard();

        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_CRYPTO);
        data.crmWithdrawalEventV2.setCrypto(
                CrmWithdrawalEventV2.Crypto.builder().walletAddress("1234").build());

        data.addDepositSumByCategory(501d);

        data.addWithdrawalSumByCategory(10_001d, 4);
        return data;
    }

    private static DataHelper getTest2Data() {
        DataHelper data = getRuleData(client2);
        DataHelper data2 = getRuleData(client2_1);

        String paymentProfileKey = getRandomUuidString();
        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_CRYPTO);
        data.crmWithdrawalEventV2.setCrypto(CrmWithdrawalEventV2.Crypto.builder()
                .walletAddress(paymentProfileKey)
                .build());

        addConnectionByEmailPhoneAttribute(data, data2.clientHelper, 1d);

        data.addDepositSumByCategory(501d);

        data.addWithdrawalSumByCategory(10_001d, 4);

        data2.createClient(client2_1).createWithdrawal();
        data2.getCrmTbWithdrawalObjects().getFirst().setCryptoWalletAddress(paymentProfileKey);
        data2.getCrmTbWithdrawalObjects().getFirst().setStatus("7");
        setupData(data2);
        return data;
    }

    private static DataHelper getTest3Data() throws IOException, InterruptedException {
        String paymentProfileKey = getRandomUuidString();
        DataHelper data2 = getRuleData(client3_1);
        DataHelper data = getRuleData(client3);

        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_CRYPTO);
        data.crmWithdrawalEventV2.setCrypto(CrmWithdrawalEventV2.Crypto.builder()
                .walletAddress(paymentProfileKey)
                .build());

        data.addDepositSumByCategory(501d);

        data.addWithdrawalSumByCategory(9000d, 5);

        data2.addWithdrawalSumByCategory(9000d, 5);
        data2.getCrmTbWithdrawalObjects().getFirst().setCryptoWalletAddress(paymentProfileKey);

        addConnectionByEmailPhoneAttribute(data, data2.clientHelper, 1d);
        addFraudTypeToConnectedUser(data, FraudTypeStatus.CONFIRMED, EXCHANGER);
        setupData(data2);

        data2.createClient(client3_1).createWithdrawal();
        data2.getCrmTbWithdrawalObjects().getFirst().setCryptoWalletAddress(paymentProfileKey);
        data2.getCrmTbWithdrawalObjects().getFirst().setStatus("7");
        return data;
    }

    public static Map<String, DataHelper> setupConnectionSearchPaymentRuleData()
            throws IOException, InterruptedException {
        startSshTunnel();
        Map<String, DataHelper> map = new HashMap<>();
        map.put("1", getTest1Data());
        map.put("2", getTest2Data());
        map.put("3", getTest3Data());
        return map;
    }
}
