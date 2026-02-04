package helpers.data.rules.payments.router_rule_crm_events;

import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.rules.MirrorFlagDataInserter.insertMirrorFlagData;
import static helpers.database.DbHelper.startSshTunnel;
import static utils.Constants.*;
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

public class RouterRuleCrmEventsDataFactory {
    private static final ClientHelper routerRuleClient1 = getRandomVantageClientAllFields();
    private static final ClientHelper routerRuleClient2 = getRandomVantageClientAllFields();
    private static final ClientHelper routerRuleClient3 = getRandomVantageClientAllFields();
    private static final ClientHelper routerRuleClient4 = getRandomVantageClientAllFields();
    private static final ClientHelper routerRuleClient5 = getRandomVantageClientAllFields();
    private static final ClientHelper routerRuleClient6 = getRandomVantageClientAllFields();
    private static final ClientHelper routerRuleClient7 = getRandomVantageClientAllFields();

    @Description("Create data for Router rule")
    private static DataHelper getRouterRuleData(ClientHelper client) {
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
                .type(Event.CRM_WITHDRAWAL_EVENT.getName())
                .withdrawalAmount(1.1)
                .withdrawalAmountUSD(1.2)
                .withdrawalApplicationTime(Instant.now().toString())
                .withdrawalCurrency("EUR")
                .withdrawalId(getRandomLongPositive())
                .status("Risk Audit")
                .needReprocessing(false)
                .build();
        return data;
    }

    private static DataHelper getRouterRuleTest1Data() {
        DataHelper data = getRouterRuleData(routerRuleClient1);
        data.crmWithdrawalEventV2.setWithdrawalAmount(1d);
        return data;
    }

    private static DataHelper getRouterRuleTest2Data() {
        DataHelper data = getRouterRuleData(routerRuleClient2);
        data.crmWithdrawalEventV2.setCheckName("Checkname");

        return data;
    }

    private static DataHelper getRouterRuleTest3Data() {
        DataHelper data = getRouterRuleData(routerRuleClient3);
        data.crmWithdrawalEventV2.setCheckName("Checkname");
        return data;
    }

    private static DataHelper getRouterRuleTest4Data() {
        DataHelper data = getRouterRuleData(routerRuleClient4);
        data.crmWithdrawalEventV2.setCheckName("Crypto_Risk");
        insertMirrorFlagData(data.clientHelper);
        return data;
    }

    private static DataHelper getRouterRuleTest5Data() {
        DataHelper data = getRouterRuleData(routerRuleClient5);
        data.crmWithdrawalEventV2.setCheckName("Crypto_Risk");
        return data;
    }

    private static DataHelper getRouterRuleTest6Data() {
        DataHelper data = getRouterRuleData(routerRuleClient6);
        data.crmWithdrawalEventV2.setCheckName("Crypto_Risk");
        return data;
    }

    private static DataHelper getRouterRuleTest7Data() {
        DataHelper data = getRouterRuleData(routerRuleClient7);
        data.crmWithdrawalEventV2.setCheckName("Crypto_Risk");
        return data;
    }

    public static Map<String, DataHelper> setupRouterRuleData() {
        startSshTunnel();
        Map<String, DataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("1", getRouterRuleTest1Data());
        map.put("2", getRouterRuleTest2Data());
        map.put("3", getRouterRuleTest3Data());
        map.put("4", getRouterRuleTest4Data());
        map.put("5", getRouterRuleTest5Data());
        map.put("6", getRouterRuleTest5Data());
        map.put("7", getRouterRuleTest5Data());
        return map;
    }
}
