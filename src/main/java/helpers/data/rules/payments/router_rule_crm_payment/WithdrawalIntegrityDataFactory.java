package helpers.data.rules.payments.router_rule_crm_payment;

import static business_objects.db.data_science.ucid_general_score.UcidGeneralScoreFactory.generateUcidGeneralScoreObject;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.DbHelper.startSshTunnel;
import static utils.Constants.*;
import static utils.Utils.getRandomIntPositive;

import business_objects.kafka.crm_events.CrmWithdrawalEventV2;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import helpers.data.enums.rule_engine.Event;
import io.qameta.allure.Description;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import utils.Utils;

public class WithdrawalIntegrityDataFactory {
    private static final ClientHelper withdrawalIntegrityRuleClient1 = getRandomVantageClientAllFields();
    private static final ClientHelper withdrawalIntegrityRuleClient2 = getRandomVantageClientAllFields();
    private static final ClientHelper withdrawalIntegrityRuleClient3 = getRandomVantageClientAllFields();

    @Description("Create data for Withdrawal Integrity check rule")
    private static DataHelper getWithdrawalIntegrityCheckRuleData(ClientHelper client) {
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
                .merchantOrderId("VTSG" + getRandomIntPositive())
                .mt4Account(data.clientHelper.getTradingAccount())
                .paymentChannelCode(PAYMENT_PROVIDER_FASAPAY)
                .paymentChannelName("-")
                .paymentMethodCode(PAYMENT_METHOD_CODE_CREDIT_CARD)
                .platform("WEB")
                .regulator(data.clientHelper.getRegulator())
                .schemaVersion("1.0")
                .type(Event.CRM_WITHDRAWAL_EVENT.getName())
                .withdrawalAmount(1.0)
                .withdrawalApplicationTime(Instant.now().toString())
                .withdrawalCurrency("EUR")
                .withdrawalId(Long.valueOf(getRandomIntPositive()))
                .status("Risk audit")
                .build();
        return data;
    }

    private static DataHelper getWithdrawalIntegrityCheckTest1Data() {
        DataHelper data = getWithdrawalIntegrityCheckRuleData(withdrawalIntegrityRuleClient1);
        data.crmWithdrawalEventV2.setWithdrawalAmount(1d);
        return data;
    }

    private static DataHelper getWithdrawalIntegrityCheckTest2Data() {
        DataHelper data = getWithdrawalIntegrityCheckRuleData(withdrawalIntegrityRuleClient1);
        data.crmWithdrawalEventV2.setWithdrawalAmount(50_000d);
        return data;
    }

    private static DataHelper getWithdrawalIntegrityCheckTest3Data() {
        DataHelper data = getWithdrawalIntegrityCheckRuleData(withdrawalIntegrityRuleClient2);
        data.crmWithdrawalEventV2.setWithdrawalAmount(101d);
        data.ucidGeneralScore = generateUcidGeneralScoreObject(data.clientHelper, 0.91, 0.91);

        return data;
    }

    private static DataHelper getWithdrawalIntegrityCheckTest4Data() {
        DataHelper data = getWithdrawalIntegrityCheckRuleData(withdrawalIntegrityRuleClient3);
        data.crmWithdrawalEventV2.setWithdrawalAmount(2000d);
        data.ucidGeneralScore = generateUcidGeneralScoreObject(data.clientHelper, 0.8, 0.8);
        return data;
    }

    public static Map<String, DataHelper> setupWithdrawalIntegrityCheckRuleData() {
        startSshTunnel();
        Map<String, DataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("1", getWithdrawalIntegrityCheckTest1Data());
        map.put("2", getWithdrawalIntegrityCheckTest2Data());
        map.put("3", getWithdrawalIntegrityCheckTest3Data());
        map.put("4", getWithdrawalIntegrityCheckTest4Data());
        return map;
    }
}
