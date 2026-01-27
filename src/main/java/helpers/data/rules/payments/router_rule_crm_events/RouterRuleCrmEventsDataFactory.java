package helpers.data.rules.payments.router_rule_crm_events;

import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.DataHelper.createClient;
import static helpers.data.rules.MirrorFlagDataInserter.insertMirrorFlagData;
import static helpers.database.DbHelper.startSshTunnel;
import static utils.Constants.*;
import static utils.Utils.getRandomIntPositive;
import static utils.Utils.getRandomUuidString;

import business_objects.kafka.crm_events.CrmWithdrawalEvent;
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

    @Description("Create data for Router rule")
    private static DataHelper getRouterRuleData(ClientHelper client) {
        DataHelper data = new DataHelper();
        createClient(data, client);

        data.crmWithdrawalEvent = new CrmWithdrawalEvent(
                "MT4", // accountType
                Utils.getRandomIntPositive().toString(), // binNumber
                data.clientHelper.getBrand().toLowerCase(), // brand
                "", // checkName
                data.clientHelper.getUserId(), // clientId
                Instant.now().toString(), // eventDate (you can format if you need +03:00)
                "4", // expMonth
                "2030", // expYear
                data.clientHelper.getFirstName(), // fullName
                getRandomUuidString(), // id
                "VTSG" + getRandomIntPositive(), // merchantOrderId (example)
                data.clientHelper.getTradingAccount(), // mt4Account
                PAYMENT_PROVIDER_FASAPAY, // paymentChannelCode
                "-", // paymentChannelName
                PAYMENT_METHOD_CODE_CREDIT_CARD, // paymentMethodCode
                "WEB", // platform
                data.clientHelper.getRegulator(), // regulator
                "1.0", // schemaVersion
                Event.CRM_WITHDRAWAL_EVENT.getName(), // type
                1, // withdrawalAmount
                Instant.now().toString(), // withdrawalApplicationTime
                "EUR", // withdrawalCurrency
                getRandomIntPositive() // withdrawalId
                );
        return data;
    }

    private static DataHelper getRouterRuleTest1Data() {
        DataHelper data = getRouterRuleData(routerRuleClient1);
        data.crmWithdrawalEvent.setWithdrawalAmount(1d);
        return data;
    }

    private static DataHelper getRouterRuleTest2Data() {
        DataHelper data = getRouterRuleData(routerRuleClient2);
        data.crmWithdrawalEvent.setCheckName("Checkname");

        return data;
    }

    private static DataHelper getRouterRuleTest3Data() {
        DataHelper data = getRouterRuleData(routerRuleClient3);
        data.crmWithdrawalEvent.setCheckName("Checkname");
        return data;
    }

    private static DataHelper getRouterRuleTest4Data() {
        DataHelper data = getRouterRuleData(routerRuleClient4);
        data.crmWithdrawalEvent.setCheckName("Crypto_Risk");
        insertMirrorFlagData(data.clientHelper);
        return data;
    }

    private static DataHelper getRouterRuleTest5Data() {
        DataHelper data = getRouterRuleData(routerRuleClient5);
        data.crmWithdrawalEvent.setCheckName("Crypto_Risk");
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
        return map;
    }
}
