package helpers.data.rules.payments.router_rule_crm_payment;

import static business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient;
import static business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
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
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import utils.Utils;

public class RouterRuleCrmPaymentDataFactory {
    private static final ClientHelper routerRuleClient1 = getRandomVantageClientAllFields();
    private static final ClientHelper routerRuleClient2 = getRandomVantageClientAllFields();
    private static final ClientHelper routerRuleClient3 = getRandomVantageClientAllFields();
    private static final ClientHelper routerRuleClient4 = getRandomVantageClientAllFields();
    private static final ClientHelper routerRuleClient5 = getRandomVantageClientAllFields();
    private static final ClientHelper routerRuleClient6 = getRandomVantageClientAllFields();
    private static final ClientHelper routerRuleClient7 = getRandomVantageClientAllFields();
    private static final ClientHelper routerRuleClient8 = getRandomVantageClientAllFields();
    private static final ClientHelper routerRuleClient9 = getRandomVantageClientAllFields();
    private static final ClientHelper routerRuleClient10 = getRandomVantageClientAllFields();
    private static final ClientHelper routerRuleClient11 = getRandomVantageClientAllFields();
    private static final ClientHelper routerRuleClient12 = getRandomVantageClientAllFields();
    private static final ClientHelper routerRuleClient13 = getRandomVantageClientAllFields();
    private static final ClientHelper routerRuleClient14 = getRandomVantageClientAllFields();
    private static final ClientHelper routerRuleClient15 = getRandomVantageClientAllFields();
    private static final ClientHelper routerRuleClient16 = getRandomVantageClientAllFields();
    private static final ClientHelper routerRuleClient17 = getRandomVantageClientAllFields();
    private static final ClientHelper routerRuleClient18 = getRandomVantageClientAllFields();

    @Description("Create data for Router rule")
    private static DataHelper getRouterRuleData(ClientHelper client) {
        DataHelper data = new DataHelper();
        data.createClient(client);
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
                "MT4", // platform
                data.clientHelper.getRegulator(), // regulator
                "2.0", // schemaVersion
                Event.CRM_WITHDRAWAL_EVENT.getName(), // type
                1.1, // withdrawalAmount
                1.2,
                Instant.now().toString(), // withdrawalApplicationTime
                "EUR", // withdrawalCurrency
                getRandomIntPositive().longValue() // withdrawalId
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
        return data;
    }

    private static DataHelper getRouterRuleTest6Data() {
        DataHelper data = getRouterRuleData(routerRuleClient6);
        return data;
    }

    private static DataHelper getRouterRuleTest7Data() {
        DataHelper data = getRouterRuleData(routerRuleClient7);

        return data;
    }

    private static DataHelper getRouterRuleTest8Data() {
        DataHelper data = getRouterRuleData(routerRuleClient8);
        insertMirrorFlagData(data.clientHelper);
        data.crmWithdrawalEvent.setPaymentMethodCode("CRYPTO");

        // add deposit
        data.crmTbDepositObjects = List.of(generateCrmTbDepositEntityByClient(data.clientHelper));
        data.crmTbDepositObjects.getFirst().setAmountUsd(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setAmount(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setPaymentTypeId(43);
        data.crmTbDepositObjects.getFirst().setPaymentChannelId(44);
        data.crmTbDepositObjects.getFirst().setSourceIdSt(7);
        data.crmTbDepositObjects.getFirst().setBrandUid(3);
        data.crmTbDepositObjects.getFirst().setStatusId(5);

        // add CRYPTO withdrawal
        data.crmTbWithdrawalObjects = List.of(generateCrmTbWithdrawalEntityByClient(data.clientHelper));
        data.crmTbWithdrawalObjects.getFirst().setAmountUsd(BigDecimal.valueOf(10_001));
        data.crmTbWithdrawalObjects.getFirst().setAmount(BigDecimal.valueOf(10_001));
        data.crmTbWithdrawalObjects.getFirst().setPaymentTypeId(43);
        data.crmTbWithdrawalObjects.getFirst().setPaymentChannelId(4);
        data.crmTbWithdrawalObjects.getFirst().setSourceIdSt(8);
        data.crmTbWithdrawalObjects.getFirst().setBrandUid(3);
        data.crmTbWithdrawalObjects.getFirst().setStatusId(5);
        return data;
    }

    private static DataHelper getRouterRuleTest9Data() {
        DataHelper data = getRouterRuleData(routerRuleClient9);
        return data;
    }

    private static DataHelper getRouterRuleTest10Data() {
        DataHelper data = getRouterRuleData(routerRuleClient10);
        return data;
    }

    private static DataHelper getRouterRuleTest11Data() {
        DataHelper data = getRouterRuleData(routerRuleClient11);
        return data;
    }

    private static DataHelper getRouterRuleTest12Data() {
        DataHelper data = getRouterRuleData(routerRuleClient12);
        insertMirrorFlagData(data.clientHelper);
        data.crmWithdrawalEvent.setPaymentMethodCode("CRYPTO");

        // add deposit
        data.crmTbDepositObjects = List.of(generateCrmTbDepositEntityByClient(data.clientHelper));
        data.crmTbDepositObjects.getFirst().setAmountUsd(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setAmount(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setPaymentTypeId(43);
        data.crmTbDepositObjects.getFirst().setPaymentChannelId(44);
        data.crmTbDepositObjects.getFirst().setSourceIdSt(7);
        data.crmTbDepositObjects.getFirst().setBrandUid(3);
        data.crmTbDepositObjects.getFirst().setStatusId(5);

        // add CRYPTO withdrawal
        data.crmTbWithdrawalObjects = List.of(generateCrmTbWithdrawalEntityByClient(data.clientHelper));
        data.crmTbWithdrawalObjects.getFirst().setAmountUsd(BigDecimal.valueOf(10_001));
        data.crmTbWithdrawalObjects.getFirst().setAmount(BigDecimal.valueOf(10_001));
        data.crmTbWithdrawalObjects.getFirst().setPaymentTypeId(43);
        data.crmTbWithdrawalObjects.getFirst().setPaymentChannelId(4);
        data.crmTbWithdrawalObjects.getFirst().setSourceIdSt(8);
        data.crmTbWithdrawalObjects.getFirst().setBrandUid(3);
        data.crmTbWithdrawalObjects.getFirst().setStatusId(5);
        return data;
    }

    private static DataHelper getRouterRuleTest13Data() {
        DataHelper data = getRouterRuleData(routerRuleClient13);
        insertMirrorFlagData(data.clientHelper);
        data.crmWithdrawalEvent.setPaymentMethodCode("CRYPTO");

        // add deposit
        data.crmTbDepositObjects = List.of(generateCrmTbDepositEntityByClient(data.clientHelper));
        data.crmTbDepositObjects.getFirst().setAmountUsd(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setAmount(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setPaymentTypeId(43);
        data.crmTbDepositObjects.getFirst().setPaymentChannelId(44);
        data.crmTbDepositObjects.getFirst().setSourceIdSt(7);
        data.crmTbDepositObjects.getFirst().setBrandUid(3);
        data.crmTbDepositObjects.getFirst().setStatusId(5);

        // add CRYPTO withdrawal
        data.crmTbWithdrawalObjects = List.of(generateCrmTbWithdrawalEntityByClient(data.clientHelper));
        data.crmTbWithdrawalObjects.getFirst().setAmountUsd(BigDecimal.valueOf(10_001));
        data.crmTbWithdrawalObjects.getFirst().setAmount(BigDecimal.valueOf(10_001));
        data.crmTbWithdrawalObjects.getFirst().setPaymentTypeId(43);
        data.crmTbWithdrawalObjects.getFirst().setPaymentChannelId(4);
        data.crmTbWithdrawalObjects.getFirst().setSourceIdSt(8);
        data.crmTbWithdrawalObjects.getFirst().setBrandUid(3);
        data.crmTbWithdrawalObjects.getFirst().setStatusId(5);
        return data;
    }

    private static DataHelper getRouterRuleTest14Data() {
        DataHelper data = getRouterRuleData(routerRuleClient14);
        insertMirrorFlagData(data.clientHelper);
        data.crmWithdrawalEvent.setPaymentMethodCode("CRYPTO");

        // add deposit
        data.crmTbDepositObjects = List.of(generateCrmTbDepositEntityByClient(data.clientHelper));
        data.crmTbDepositObjects.getFirst().setAmountUsd(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setAmount(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setPaymentTypeId(43);
        data.crmTbDepositObjects.getFirst().setPaymentChannelId(44);
        data.crmTbDepositObjects.getFirst().setSourceIdSt(7);
        data.crmTbDepositObjects.getFirst().setBrandUid(3);
        data.crmTbDepositObjects.getFirst().setStatusId(5);

        // add CRYPTO withdrawal
        data.crmTbWithdrawalObjects = List.of(generateCrmTbWithdrawalEntityByClient(data.clientHelper));
        data.crmTbWithdrawalObjects.getFirst().setAmountUsd(BigDecimal.valueOf(10_001));
        data.crmTbWithdrawalObjects.getFirst().setAmount(BigDecimal.valueOf(10_001));
        data.crmTbWithdrawalObjects.getFirst().setPaymentTypeId(43);
        data.crmTbWithdrawalObjects.getFirst().setPaymentChannelId(4);
        data.crmTbWithdrawalObjects.getFirst().setSourceIdSt(8);
        data.crmTbWithdrawalObjects.getFirst().setBrandUid(3);
        data.crmTbWithdrawalObjects.getFirst().setStatusId(5);
        return data;
    }

    private static DataHelper getRouterRuleTest15Data() {
        DataHelper data = getRouterRuleData(routerRuleClient15);
        insertMirrorFlagData(data.clientHelper);
        data.crmWithdrawalEvent.setPaymentMethodCode("CRYPTO");

        // add deposit
        data.crmTbDepositObjects = List.of(generateCrmTbDepositEntityByClient(data.clientHelper));
        data.crmTbDepositObjects.getFirst().setAmountUsd(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setAmount(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setPaymentTypeId(43);
        data.crmTbDepositObjects.getFirst().setPaymentChannelId(44);
        data.crmTbDepositObjects.getFirst().setSourceIdSt(7);
        data.crmTbDepositObjects.getFirst().setBrandUid(3);
        data.crmTbDepositObjects.getFirst().setStatusId(5);

        // add CRYPTO withdrawal
        data.crmTbWithdrawalObjects = List.of(generateCrmTbWithdrawalEntityByClient(data.clientHelper));
        data.crmTbWithdrawalObjects.getFirst().setAmountUsd(BigDecimal.valueOf(10_001));
        data.crmTbWithdrawalObjects.getFirst().setAmount(BigDecimal.valueOf(10_001));
        data.crmTbWithdrawalObjects.getFirst().setPaymentTypeId(43);
        data.crmTbWithdrawalObjects.getFirst().setPaymentChannelId(4);
        data.crmTbWithdrawalObjects.getFirst().setSourceIdSt(8);
        data.crmTbWithdrawalObjects.getFirst().setBrandUid(3);
        data.crmTbWithdrawalObjects.getFirst().setStatusId(5);
        return data;
    }

    private static DataHelper getRouterRuleTest16Data() {
        DataHelper data = getRouterRuleData(routerRuleClient16);
        data.crmWithdrawalEvent.setCheckName("Crypto_Risk");
        data.crmWithdrawalEvent.setWithdrawalAmount(1d);
        return data;
    }

    private static DataHelper getRouterRuleTest17Data() {
        DataHelper data = getRouterRuleData(routerRuleClient17);
        data.crmWithdrawalEvent.setCheckName("Crypto_Risk");
        data.crmWithdrawalEvent.setWithdrawalAmount(1d);
        return data;
    }

    private static DataHelper getRouterRuleTest18Data() {
        DataHelper data = getRouterRuleData(routerRuleClient18);
        data.crmWithdrawalEvent.setWithdrawalAmount(1d);
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
        map.put("6", getRouterRuleTest6Data());
        map.put("7", getRouterRuleTest7Data());
        map.put("8", getRouterRuleTest8Data());
        map.put("9", getRouterRuleTest9Data());
        map.put("10", getRouterRuleTest10Data());
        map.put("11", getRouterRuleTest11Data());
        map.put("12", getRouterRuleTest12Data());
        map.put("13", getRouterRuleTest13Data());
        map.put("14", getRouterRuleTest14Data());
        map.put("15", getRouterRuleTest15Data());
        map.put("16", getRouterRuleTest16Data());
        map.put("17", getRouterRuleTest17Data());
        map.put("18", getRouterRuleTest18Data());
        return map;
    }
}
