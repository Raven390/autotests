package helpers.data.rules.payments.router_rule_crm_payment;

import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.DbHelper.startSshTunnel;
import static utils.Constants.*;
import static utils.Utils.getRandomIntPositive;
import static utils.Utils.getRandomUuidString;

import business_objects.kafka.crm_events.CrmWithdrawalEventV2;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import helpers.data.enums.rule_engine.Event;
import io.qameta.allure.Description;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import utils.Utils;

public class ClearanceRuleWithdrawalEventDataFactory {
    private static final ClientHelper client1 = getRandomVantageClientAllFields();
    private static final ClientHelper client2 = getRandomVantageClientAllFields();
    private static final ClientHelper client3 = getRandomVantageClientAllFields();
    private static final ClientHelper client4 = getRandomVantageClientAllFields();
    private static final ClientHelper client5 = getRandomVantageClientAllFields();
    private static final ClientHelper client6 = getRandomVantageClientAllFields();
    private static final ClientHelper client7 = getRandomVantageClientAllFields();
    private static final ClientHelper client8 = getRandomVantageClientAllFields();

    @Description("Create data for Clearance rule")
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
                .paymentMethodCode("CREDIT_CARD")
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

        return data;
    }

    private static DataHelper getTest2Data() {
        DataHelper data = getRuleData(client2);

        data.createDeposit();
        data.getCrmTbDepositObjects().getFirst().setAmount(BigDecimal.valueOf(10_001));
        data.getCrmTbDepositObjects().getFirst().setAmountUsd(BigDecimal.valueOf(10_001));

        return data;
    }

    private static DataHelper getTest3Data() {
        DataHelper data = getRuleData(client3);

        data.crmWithdrawalEventV2.setWithdrawalAmountUSD(100d);

        data.createDeposit();
        data.getCrmTbDepositObjects().getFirst().setAmount(BigDecimal.valueOf(9999));
        data.getCrmTbDepositObjects().getFirst().setAmountUsd(BigDecimal.valueOf(9999));

        return data;
    }

    private static DataHelper getTest4Data() {
        DataHelper data = getRuleData(client4);

        data.crmWithdrawalEventV2.setWithdrawalAmountUSD(0d);

        data.createDeposit();
        data.getCrmTbDepositObjects().getFirst().setAmount(BigDecimal.valueOf(9999));
        data.getCrmTbDepositObjects().getFirst().setAmountUsd(BigDecimal.valueOf(9999));

        return data;
    }

    private static DataHelper getTest5Data() {
        DataHelper data = getRuleData(client5);

        data.crmWithdrawalEventV2.setWithdrawalAmountUSD(50d);

        data.createDeposit();
        data.getCrmTbDepositObjects().getFirst().setAmount(BigDecimal.valueOf(100));
        data.getCrmTbDepositObjects().getFirst().setAmountUsd(BigDecimal.valueOf(100));

        data.createWithdrawal();
        data.getCrmTbWithdrawalObjects().getFirst().setAmount(BigDecimal.valueOf(20));
        data.getCrmTbWithdrawalObjects().getFirst().setAmountUsd(BigDecimal.valueOf(20));

        return data;
    }

    private static DataHelper getTest6Data() {
        DataHelper data = getRuleData(client6);

        return data;
    }

    private static DataHelper getTest7Data() {
        DataHelper data = getRuleData(client7);

        return data;
    }

    private static DataHelper getTest8Data() {
        DataHelper data = getRuleData(client8);
        data.crmWithdrawalEventV2.setWithdrawalAmountUSD(null);

        data.createDeposit();
        data.getCrmTbDepositObjects().getFirst().setAmount(BigDecimal.valueOf(9999));
        data.getCrmTbDepositObjects().getFirst().setAmountUsd(BigDecimal.valueOf(9999));

        return data;
    }

    public static Map<String, DataHelper> setupClearanceWithdrawalEventRuleData() {
        startSshTunnel();
        Map<String, DataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("1", getTest1Data());
        map.put("2", getTest2Data());
        map.put("3", getTest3Data());
        map.put("4", getTest4Data());
        map.put("5", getTest5Data());
        map.put("6", getTest6Data());
        map.put("7", getTest7Data());
        map.put("8", getTest8Data());
        return map;
    }
}
