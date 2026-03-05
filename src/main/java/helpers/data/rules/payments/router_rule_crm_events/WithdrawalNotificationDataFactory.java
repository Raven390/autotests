package helpers.data.rules.payments.router_rule_crm_events;

import static business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient;
import static business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient;
import static business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedFactory.generateMt5DealsCoercedObject;
import static business_objects.db.clickhouse.mt_tb_credits.MtTbCreditsObjectFactory.generateCreditsByClient;
import static business_objects.db.data_science.ucid_general_score.UcidGeneralScoreFactory.generateUcidGeneralScoreObject;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.rules.MirrorTradeOnWithdrawalDataInserter.insertMirrorTradeOnWithdrawalData;
import static helpers.database.DbHelper.startSshTunnel;
import static utils.Constants.PAYMENT_METHOD_CODE_CREDIT_CARD;
import static utils.Constants.PAYMENT_PROVIDER_FASAPAY;
import static utils.Utils.getRandomIntPositive;

import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntity;
import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalEntity;
import business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedObject;
import business_objects.db.clickhouse.mt_tb_credits.MtTbCreditsObject;
import business_objects.db.data_science.ucid_general_score.UcidGeneralScore;
import business_objects.kafka.crm_events.CrmWithdrawalEvent;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import helpers.data.enums.rule_engine.Event;
import io.qameta.allure.Description;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
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
    private static final ClientHelper withdrawalNotificationRuleClient8 = getRandomVantageClientAllFields();
    private static final ClientHelper withdrawalNotificationRuleClient9 = getRandomVantageClientAllFields();
    private static final ClientHelper withdrawalNotificationRuleClient10 = getRandomVantageClientAllFields();
    private static final ClientHelper withdrawalNotificationRuleClient11 = getRandomVantageClientAllFields();

    private static final String checkNameCryptoRisk = "Crypto_Risk";
    private static final String checkNameNotCryptoRisk = "Not_Crypto_Risk";
    private static final String time2025 = "2025-01-11T11:11:11.111+00:00";
    private static final String time2026 = "2026-01-11T11:11:11.111+00:00";

    @Description("Create data for Withdrawal Notification rule")
    private static DataHelper getWithdrawalNotificationRuleData(ClientHelper client) {
        DataHelper data = new DataHelper();
        data.createClient(client);
        data.setCrmWithdrawalEvent(CrmWithdrawalEvent.builder()
                .id(UUID.randomUUID().toString())
                .accountType("MT4")
                .binNumber(Utils.getRandomIntPositive().toString())
                .brand(data.clientHelper.getBrand().toLowerCase())
                .checkName("")
                .clientId(Long.valueOf(data.clientHelper.getUserId()))
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
                .build());
        return data;
    }

    private static DataHelper getWithdrawalNotificationTest1Data() {
        DataHelper data = getWithdrawalNotificationRuleData(withdrawalNotificationRuleClient1);
        data.getCrmWithdrawalEvent().setCheckName("");
        return data;
    }

    private static DataHelper getWithdrawalNotificationTest2Data() {
        DataHelper data = getWithdrawalNotificationRuleData(withdrawalNotificationRuleClient2);
        data.getCrmWithdrawalEvent().setCheckName(null);
        return data;
    }

    private static DataHelper getWithdrawalNotificationTest3Data() {
        DataHelper data = getWithdrawalNotificationRuleData(withdrawalNotificationRuleClient3);
        data.getCrmWithdrawalEvent().setCheckName(checkNameCryptoRisk);

        return data;
    }

    private static DataHelper getWithdrawalNotificationTest4Data() {
        DataHelper data = getWithdrawalNotificationRuleData(withdrawalNotificationRuleClient4);
        data.getCrmWithdrawalEvent().setCheckName(checkNameCryptoRisk);
        data.crmTbAccountObject.sourceIdSt = 9;
        data.crmTbAccountObject.brandUid = 4;
        insertMirrorTradeOnWithdrawalData(data.clientHelper);
        return data;
    }

    private static DataHelper getWithdrawalNotificationTest5Data() {
        DataHelper data = getWithdrawalNotificationRuleData(withdrawalNotificationRuleClient5);
        data.getCrmWithdrawalEvent().setCheckName(checkNameCryptoRisk);
        data.crmTbAccountObject.sourceIdSt = 9;
        data.crmTbAccountObject.brandUid = 4;
        CrmTbWithdrawalEntity wd1 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        wd1.setCreateTime(OffsetDateTime.parse(time2025));
        wd1.setCreateTimeUtc(OffsetDateTime.parse(time2025));
        wd1.setUpdateTime(OffsetDateTime.parse(time2025));
        wd1.setUpdateTimeUtc(OffsetDateTime.parse(time2025));
        CrmTbWithdrawalEntity wd2 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        wd2.setCreateTime(OffsetDateTime.parse(time2026));
        wd2.setCreateTimeUtc(OffsetDateTime.parse(time2026));
        wd2.setUpdateTime(OffsetDateTime.parse(time2026));
        wd2.setUpdateTimeUtc(OffsetDateTime.parse(time2026));
        data.setCrmTbWithdrawalObjects(List.of(wd1, wd2));

        UcidGeneralScore sc1 = generateUcidGeneralScoreObject(data, OffsetDateTime.parse(time2025), 0.1, 0.1);
        UcidGeneralScore sc2 = generateUcidGeneralScoreObject(data, OffsetDateTime.parse(time2026), 0.8, 0.8);
        data.setUcidGeneralScores(List.of(sc1, sc2));
        insertMirrorTradeOnWithdrawalData(data.clientHelper);
        return data;
    }

    private static DataHelper getWithdrawalNotificationTest6Data() throws IOException {
        DataHelper data = getWithdrawalNotificationRuleData(withdrawalNotificationRuleClient6);
        data.getCrmWithdrawalEvent().setCheckName(checkNameNotCryptoRisk);
        return data;
    }

    private static DataHelper getWithdrawalNotificationTest7Data() throws IOException {
        DataHelper data = getWithdrawalNotificationRuleData(withdrawalNotificationRuleClient7);
        data.getCrmWithdrawalEvent().setCheckName(checkNameNotCryptoRisk);
        return data;
    }
    //
    private static DataHelper getWithdrawalNotificationTest8Data() throws IOException {
        DataHelper data = getWithdrawalNotificationRuleData(withdrawalNotificationRuleClient8);
        data.getCrmWithdrawalEvent().setCheckName(checkNameNotCryptoRisk);

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

    private static DataHelper getWithdrawalNotificationTest9Data() {
        DataHelper data = getWithdrawalNotificationRuleData(withdrawalNotificationRuleClient9);
        data.getCrmWithdrawalEvent().setCheckName(checkNameNotCryptoRisk);

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

    private static DataHelper getWithdrawalNotificationTest10Data() {
        DataHelper data = getWithdrawalNotificationRuleData(withdrawalNotificationRuleClient10);
        data.getCrmWithdrawalEvent().setCheckName("NT_Blacklist");
        return data;
    }

    private static DataHelper getWithdrawalNotificationTest11Data() {
        String fraudId = (Utils.getRandomIntPositive()
                        .toString()
                        .substring(0, Utils.getRandomIntPositive().toString().length() - 2)
                + "99");

        if (fraudId.length() > 9) {
            fraudId = fraudId.substring(fraudId.length() - 8, fraudId.length());
        }
        withdrawalNotificationRuleClient11.setUserId(Integer.valueOf(fraudId));
        DataHelper data = getWithdrawalNotificationRuleData(withdrawalNotificationRuleClient11);
        data.getCrmWithdrawalEvent().setCheckName(checkNameNotCryptoRisk);
        data.crmWithdrawalEvent.setClientId(Long.valueOf(data.clientHelper.getUserId()));
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
        map.put("8", getWithdrawalNotificationTest8Data());
        map.put("9", getWithdrawalNotificationTest9Data());
        map.put("10", getWithdrawalNotificationTest10Data());
        map.put("11", getWithdrawalNotificationTest11Data());
        return map;
    }
}
