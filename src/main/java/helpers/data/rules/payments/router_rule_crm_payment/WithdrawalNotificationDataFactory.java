package helpers.data.rules.payments.router_rule_crm_payment;

import static business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient;
import static business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedFactory.generateMt5DealsCoercedObject;
import static business_objects.db.clickhouse.mt_tb_credits.MtTbCreditsObjectFactory.generateCreditsByClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.DataHelper.createClient;
import static helpers.database.DbHelper.startSshTunnel;
import static utils.Constants.*;
import static utils.Utils.getRandomIntPositive;

import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntity;
import business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedObject;
import business_objects.db.clickhouse.mt_tb_credits.MtTbCreditsObject;
import business_objects.kafka.crm_events.CrmWithdrawalEventV2;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import io.qameta.allure.Description;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import utils.Utils;

public class WithdrawalNotificationDataFactory {
    private static final ClientHelper withdrawalNotificationRuleClient1 = getRandomVantageClientAllFields();
    private static final ClientHelper withdrawalNotificationRuleClient2 = getRandomVantageClientAllFields();
    private static final ClientHelper withdrawalNotificationRuleClient3 = getRandomVantageClientAllFields();
    private static final ClientHelper withdrawalNotificationRuleClient4 = getRandomVantageClientAllFields();
    private static final ClientHelper withdrawalNotificationRuleClient5 = getRandomVantageClientAllFields();
    private static final ClientHelper withdrawalNotificationRuleClient6 = getRandomVantageClientAllFields();
    private static final ClientHelper withdrawalNotificationRuleClient7 = getRandomVantageClientAllFields();

    @Description("Create data for Withdrawal Notification rule")
    private static DataHelper getWithdrawalNotificationRuleData(ClientHelper client) {
        DataHelper data = new DataHelper();
        createClient(data, client);

        data.setCrmWithdrawalEventV2(CrmWithdrawalEventV2.builder()
                .id(UUID.randomUUID().toString())
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
                .build());
        return data;
    }

    private static DataHelper getWithdrawalNotificationTest1Data() {
        DataHelper data = getWithdrawalNotificationRuleData(withdrawalNotificationRuleClient1);
        data.getCrmWithdrawalEventV2().setCheckName("");
        return data;
    }

    private static DataHelper getWithdrawalNotificationTest2Data() {
        DataHelper data = getWithdrawalNotificationRuleData(withdrawalNotificationRuleClient2);
        data.getCrmWithdrawalEventV2().setCheckName(null);
        return data;
    }

    private static DataHelper getWithdrawalNotificationTest3Data() {
        DataHelper data = getWithdrawalNotificationRuleData(withdrawalNotificationRuleClient3);
        data.getCrmWithdrawalEventV2().setCheckName("Crypto_Risk");

        return data;
    }

    private static DataHelper getWithdrawalNotificationTest4Data() throws IOException {
        DataHelper data = getWithdrawalNotificationRuleData(withdrawalNotificationRuleClient4);
        data.getCrmWithdrawalEventV2().setCheckName("");
        return data;
    }

    private static DataHelper getWithdrawalNotificationTest5Data() throws IOException {
        DataHelper data = getWithdrawalNotificationRuleData(withdrawalNotificationRuleClient5);
        data.getCrmWithdrawalEventV2().setCheckName("Not_Crypto_Risk");
        return data;
    }

    private static DataHelper getWithdrawalNotificationTest6Data() throws IOException {
        DataHelper data = getWithdrawalNotificationRuleData(withdrawalNotificationRuleClient6);
        data.getCrmWithdrawalEventV2().setCheckName("Not_Crypto_Risk");

        // set credits
        MtTbCreditsObject credit = generateCreditsByClient(data.clientHelper);
        credit.setAmountUsd(100.0);
        data.mtTbCreditsObjects = List.of(credit);

        // set deposit
        CrmTbDepositEntity deposit1 = generateCrmTbDepositEntityByClient(data.clientHelper);
        deposit1.setAmountUsd(BigDecimal.valueOf(100.0));
        data.crmTbDepositObjects = List.of(deposit1);

        // set profit
        Mt5DealsCoercedObject deal = generateMt5DealsCoercedObject(data.clientHelper);
        deal.setProfitUsd(130.0);
        deal.setCommissionUsd(1000.0);
        data.mt5DealsCoercedObjects = List.of(deal);

        data.crmTbDepositObjects = List.of(deposit1);

        return data;
    }

    private static DataHelper getWithdrawalNotificationTest7Data() {
        DataHelper data = getWithdrawalNotificationRuleData(withdrawalNotificationRuleClient7);
        data.getCrmWithdrawalEventV2().setCheckName("Not_Crypto_Risk");

        // set credits
        MtTbCreditsObject credit = generateCreditsByClient(data.clientHelper);
        credit.setAmountUsd(100.0);
        data.mtTbCreditsObjects = List.of(credit);

        // set deposit
        CrmTbDepositEntity deposit1 = generateCrmTbDepositEntityByClient(data.clientHelper);
        deposit1.setAmountUsd(BigDecimal.valueOf(100.0));
        data.crmTbDepositObjects = List.of(deposit1);

        // set profit
        Mt5DealsCoercedObject deal = generateMt5DealsCoercedObject(data.clientHelper);
        deal.setProfitUsd(100.0);
        deal.setCommissionUsd(100.0);
        data.mt5DealsCoercedObjects = List.of(deal);

        data.crmTbDepositObjects = List.of(deposit1);

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
        map.put("5", getWithdrawalNotificationTest5Data());
        map.put("6", getWithdrawalNotificationTest6Data());
        map.put("7", getWithdrawalNotificationTest7Data());
        return map;
    }
}
