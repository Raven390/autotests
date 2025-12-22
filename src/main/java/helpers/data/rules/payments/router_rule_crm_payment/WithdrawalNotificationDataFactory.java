package helpers.data.rules.payments.router_rule_crm_payment;

import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.DataHelper.createClient;
import static helpers.data.DataSetupHelper.setupData;
import static helpers.database.DbHelper.startSshTunnel;
import static utils.Constants.*;
import static utils.Utils.getRandomIntPositive;

import business_objects.kafka.crm_events.CrmWithdrawalEventV2;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import io.qameta.allure.Description;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import utils.Utils;

public class WithdrawalNotificationDataFactory {
    private static final ClientHelper withdrawalNotificationRuleClient1 = getRandomVantageClientAllFields();
    private static final ClientHelper withdrawalNotificationRuleClient2 = getRandomVantageClientAllFields();
    private static final ClientHelper withdrawalNotificationRuleClient3 = getRandomVantageClientAllFields();
    private static final ClientHelper withdrawalNotificationRuleClient4 = getRandomVantageClientAllFields();

    @Description("Create data for Withdrawal Notification rule")
    private static DataHelper getWithdrawalNotificationRuleData(ClientHelper client) {
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
                .merchantOrderId("VTSG" + getRandomIntPositive())
                .mt4Account(data.clientHelper.getTradingAccount())
                .paymentChannelCode(PAYMENT_PROVIDER_FASAPAY)
                .paymentChannelName("-")
                .paymentMethodCode(PAYMENT_METHOD_CODE_CREDIT_CARD)
                .platform("WEB")
                .regulator(data.clientHelper.getRegulator())
                .schemaVersion("1.0")
                .type(CRM_WITHDRAWAL_EVENT)
                .withdrawalAmount(1.0)
                .withdrawalApplicationTime(Instant.now().toString())
                .withdrawalCurrency("EUR")
                .withdrawalId(Long.valueOf(getRandomIntPositive()))
                .status("Risk audit")
                .build();
        return data;
    }

    private static DataHelper getWithdrawalNotificationTest1Data() {
        DataHelper data = getWithdrawalNotificationRuleData(withdrawalNotificationRuleClient1);
        data.crmWithdrawalEvent.setCheckName("");
        return data;
    }

    private static DataHelper getWithdrawalNotificationTest2Data() {
        DataHelper data = getWithdrawalNotificationRuleData(withdrawalNotificationRuleClient2);
        data.crmWithdrawalEvent.setCheckName(null);
        return data;
    }

    private static DataHelper getWithdrawalNotificationTest3Data() {
        DataHelper data = getWithdrawalNotificationRuleData(withdrawalNotificationRuleClient3);
        data.crmWithdrawalEvent.setCheckName("Crypto_Risk");

        return data;
    }

    private static DataHelper getWithdrawalNotificationTest4Data() throws IOException {
        DataHelper data = getWithdrawalNotificationRuleData(withdrawalNotificationRuleClient4);
        data.crmWithdrawalEvent.setCheckName("");
        return data;
    }

    public static Map<String, DataHelper> setupWithdrawalNotificationRuleData() throws IOException {
        startSshTunnel();
        Map<String, DataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("1", getWithdrawalNotificationTest1Data());
        map.put("2", getWithdrawalNotificationTest2Data());
        map.put("3", getWithdrawalNotificationTest3Data());
        map.put("4", getWithdrawalNotificationTest4Data());

        setupData(map);

        return map;
    }
}
