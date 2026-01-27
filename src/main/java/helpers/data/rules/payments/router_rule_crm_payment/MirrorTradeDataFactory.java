package helpers.data.rules.payments.router_rule_crm_payment;

import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.DataHelper.createClient;
import static helpers.data.rules.MirrorFlagDataInserter.insertMirrorFlagData;
import static helpers.database.DbHelper.startSshTunnel;
import static utils.Constants.*;
import static utils.Utils.getRandomIntPositive;
import static utils.Utils.getRandomUuidString;

import business_objects.kafka.crm_events.CrmWithdrawalEventV2;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import helpers.data.enums.rule_engine.Event;
import io.qameta.allure.Description;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import utils.Utils;

public class MirrorTradeDataFactory {
    private static final ClientHelper mirrorTradeRuleClient1 = getRandomVantageClientAllFields();
    private static final ClientHelper mirrorTradeRuleClient2 = getRandomVantageClientAllFields();

    @Description("Create data for Mirror Trade rule")
    private static DataHelper getMirrorTradeRuleData(ClientHelper client) {
        DataHelper data = new DataHelper();
        createClient(data, client);

        data.crmWithdrawalEventV2 = CrmWithdrawalEventV2.builder()
                .accountType("MT4") // accountType
                .binNumber(Utils.getRandomIntPositive().toString()) // binNumber
                .brand(data.clientHelper.getBrand().toLowerCase()) // brand
                .checkName("") // checkName
                .clientId(data.clientHelper.getUserId()) // clientId
                .eventDate(Instant.now().toString()) // eventDate (you can format if you need +03:00)
                .expMonth("4") // expMonth
                .expYear("2030") // expYear
                .fullName(data.clientHelper.getFirstName()) // fullName
                .id(getRandomUuidString()) // id
                .merchantOrderId("VTSG" + getRandomIntPositive()) // merchantOrderId (example)
                .mt4Account(data.clientHelper.getTradingAccount()) // mt4Account
                .paymentChannelCode(PAYMENT_PROVIDER_FASAPAY) // paymentChannelCode
                .paymentChannelName("-") // paymentChannelName
                .paymentMethodCode(PAYMENT_METHOD_CODE_CREDIT_CARD) // paymentMethodCode
                .platform("WEB") // platform
                .regulator(data.clientHelper.getRegulator()) // regulator
                .schemaVersion("1.0") // schemaVersion
                .type(Event.CRM_WITHDRAWAL_EVENT.getName()) // type
                .withdrawalAmount(1.0) // withdrawalAmount
                .withdrawalApplicationTime(Instant.now().toString()) // withdrawalApplicationTime
                .withdrawalCurrency("EUR") // withdrawalCurrency
                .withdrawalId(Long.valueOf(getRandomIntPositive())) // withdrawalId
                .status("Risk audit")
                .build();
        return data;
    }

    private static DataHelper getMirrorTradeTest1Data() {
        DataHelper data = getMirrorTradeRuleData(mirrorTradeRuleClient1);

        return data;
    }

    private static DataHelper getMirrorTradeTest2Data() {
        DataHelper data = getMirrorTradeRuleData(mirrorTradeRuleClient2);
        insertMirrorFlagData(data.clientHelper);
        return data;
    }

    public static Map<String, DataHelper> setupMirrorTradeRuleData() {
        startSshTunnel();
        Map<String, DataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("1", getMirrorTradeTest1Data());
        map.put("2", getMirrorTradeTest2Data());
        return map;
    }
}
