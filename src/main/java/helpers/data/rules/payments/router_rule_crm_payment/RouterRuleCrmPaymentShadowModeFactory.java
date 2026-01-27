package helpers.data.rules.payments.router_rule_crm_payment;

import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.DbHelper.startSshTunnel;
import static utils.Constants.PAYMENT_PROVIDER_FASAPAY;
import static utils.Utils.*;

import business_objects.kafka.crm_events.CrmWithdrawalEventV2;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import helpers.data.enums.rule_engine.Event;
import io.qameta.allure.Description;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import utils.Utils;

public class RouterRuleCrmPaymentShadowModeFactory {
    private static final ClientHelper testClient1 = getRandomVantageClientAllFields();
    private static final ClientHelper testClient2 = getRandomVantageClientAllFields();
    private static final ClientHelper testClient3 = getRandomVantageClientAllFields();

    @Description("Create data for Shadow mode Router rule on withdrawal event")
    private static DataHelper getTest(ClientHelper client) {
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
                .paymentChannelName("CRYPTO_CHANNEL")
                .paymentMethodCode("CRYPTO")
                .platform("MT4")
                .regulator(data.clientHelper.getRegulator())
                .schemaVersion("2.0")
                .type(Event.CRM_DEPOSIT_EVENT.getName())
                .withdrawalAmount(1.1)
                .withdrawalAmountUSD(1.2)
                .withdrawalApplicationTime(Instant.now().toString())
                .withdrawalCurrency("EUR")
                .withdrawalId(getRandomLongPositive())
                .status("Risk Audit")
                .build();
        return data;
    }

    private static DataHelper getTest1Data() {
        DataHelper data = getTest(testClient1);
        data.crmWithdrawalEventV2.setCheckName("Crypto_Risk");
        data.crmWithdrawalEventV2.setWithdrawalAmountUSD(1d);
        return data;
    }

    private static DataHelper getTest2Data() {
        DataHelper data = getTest(testClient2);
        data.crmWithdrawalEventV2.setCheckName("Crypto_Risk");
        data.crmWithdrawalEventV2.setWithdrawalAmountUSD(1d);
        return data;
    }

    private static DataHelper getTest3Data() {
        DataHelper data = getTest(testClient3);
        data.crmWithdrawalEventV2.setWithdrawalAmountUSD(1d);
        return data;
    }

    public static Map<String, DataHelper> setupRouterRuleShadowModeWithdrawalData() {
        startSshTunnel();
        Map<String, DataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("1", getTest1Data());
        map.put("2", getTest2Data());
        map.put("3", getTest3Data());
        return map;
    }
}
