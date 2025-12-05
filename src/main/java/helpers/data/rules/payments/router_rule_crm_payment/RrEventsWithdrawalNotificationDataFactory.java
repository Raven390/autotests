package helpers.data.rules.payments.router_rule_crm_payment;

import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntity;
import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalEntity;
import business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedObject;
import business_objects.db.clickhouse.mt_tb_credits.MtTbCreditsObject;
import business_objects.db.data_science.ucid_general_score.UcidGeneralScore;
import business_objects.kafka.crm_events.CrmWithdrawalEvent;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import helpers.data.enums.DateTimeFormat;
import io.qameta.allure.Description;
import utils.Utils;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient;
import static business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient;
import static business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedFactory.generateMt5DealsCoercedObject;
import static business_objects.db.clickhouse.mt_tb_credits.MtTbCreditsObjectFactory.generateCreditsByClient;
import static business_objects.db.data_science.ucid_general_score.UcidGeneralScoreFactory.generateUcidGeneralScoreObject;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.DataHelper.createClient;
import static helpers.data.DataHelper.setupData;
import static helpers.data.rules.MirrorFlagDataInserter.insertMirrorFlagData;
import static helpers.data.rules.MirrorTradeOnLastWithdrawalDataInserter.insertMirrorTradeOnLastWithdrawalData;
import static helpers.database.DbHelper.startSshTunnel;
import static utils.Constants.CRM_WITHDRAWAL_EVENT;
import static utils.Constants.PAYMENT_PROVIDER_FASAPAY;
import static utils.Utils.*;

public class RrEventsWithdrawalNotificationDataFactory {
    private static final ClientHelper withdrawalNotificationRuleClient1 = getRandomVantageClientAllFields();
    private static final ClientHelper withdrawalNotificationRuleClient2 = getRandomVantageClientAllFields();
    private static final ClientHelper withdrawalNotificationRuleClient5 = getRandomVantageClientAllFields();
    private static final ClientHelper withdrawalNotificationRuleClient6 = getRandomVantageClientAllFields();
    private static final ClientHelper withdrawalNotificationRuleClient7 = getRandomVantageClientAllFields();
    private static final ClientHelper withdrawalNotificationRuleClient8 = getRandomVantageClientAllFields();
    private static final ClientHelper withdrawalNotificationRuleClient9 = getRandomVantageClientAllFields();
    private static final ClientHelper withdrawalNotificationRuleClient10 = getRandomVantageClientAllFields();
    private static final ClientHelper withdrawalNotificationRuleClient11 = getRandomVantageClientAllFields();
    private static final ClientHelper withdrawalNotificationRuleClient12 = getRandomVantageClientAllFields();
    private static final ClientHelper withdrawalNotificationRuleClient13 = getRandomVantageClientAllFields();

    @Description("Create data for Withdrawal Notification rule")
    private static DataHelper getWithdrawalNotificationRuleData(ClientHelper client) {
        DataHelper data = new DataHelper();
        createClient(data, client);

        data.crmWithdrawalEvent = new CrmWithdrawalEvent(
                "MT4",                            // accountType
                Utils.getRandomIntPositive().toString(),      // binNumber
                data.clientHelper.getBrand().toLowerCase(),   // brand
                "",                                           // Name
                data.clientHelper.getUserId(),                // clientId
                Instant.now().toString(),                  // eventDate (you can format if you need +03:00)
                "4",                                          // expMonth
                "2030",                                       // expYear
                data.clientHelper.getFirstName(),             // fullName
                getRandomUuidString(),                        // id
                "VTSG" + getRandomIntPositive(),              // merchantOrderId (example)
                data.clientHelper.getTradingAccount(),        // mt4Account
                PAYMENT_PROVIDER_FASAPAY,            // paymentChannelCode
                "-",                                 // paymentChannelName
                "CREDIT_CARD",                       // paymentMethodCode
                "WEB",                               // platform
                data.clientHelper.getRegulator(),    // regulator
                "1.0",                               // schemaVersion
                CRM_WITHDRAWAL_EVENT,                // type
                1,                                   // withdrawalAmount
                Instant.now().toString(),               // withdrawalApplicationTime
                "EUR",                               // withdrawalCurrency
                getRandomIntPositive()               // withdrawalId
        );
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

    private static DataHelper getWithdrawalNotificationTest5Data() throws IOException {
        DataHelper data = getWithdrawalNotificationRuleData(withdrawalNotificationRuleClient5);
        data.crmWithdrawalEvent.setCheckName("Not_Crypto_Risk");
        return data;
    }

    private static DataHelper getWithdrawalNotificationTest6Data() throws IOException {
        DataHelper data = getWithdrawalNotificationRuleData(withdrawalNotificationRuleClient6);
        data.crmWithdrawalEvent.setCheckName("Not_Crypto_Risk");
        return data;
    }

    private static DataHelper getWithdrawalNotificationTest7Data() throws IOException {
        DataHelper data = getWithdrawalNotificationRuleData(withdrawalNotificationRuleClient7);
        data.crmWithdrawalEvent.setCheckName("Not_Crypto_Risk");

        //set credits
        MtTbCreditsObject credit = generateCreditsByClient(data.clientHelper);
        credit.setAmountUsd(100.0);
        data.mtTbCreditsObjects = List.of(credit);

        //set deposit
        CrmTbDepositEntity deposit1 = generateCrmTbDepositEntityByClient(data.clientHelper);
        deposit1.setAmountUsd(BigDecimal.valueOf(100.0));
        data.crmTbDepositObjects = List.of(deposit1);

        //set profit
        Mt5DealsCoercedObject deal = generateMt5DealsCoercedObject(data.clientHelper);
        deal.setProfitUsd(1000.0);
        deal.setCommissionUsd(1000.0);
        data.mt5DealsCoercedObjects = List.of(deal);

        data.crmTbDepositObjects = List.of(deposit1);

        return data;
    }

    private static DataHelper getWithdrawalNotificationTest8Data() throws IOException {
        DataHelper data = getWithdrawalNotificationRuleData(withdrawalNotificationRuleClient8);
        data.crmWithdrawalEvent.setCheckName("Not_Crypto_Risk");

        //set credits
        MtTbCreditsObject credit = generateCreditsByClient(data.clientHelper);
        credit.setAmountUsd(100.0);
        data.mtTbCreditsObjects = List.of(credit);

        //set deposit
        CrmTbDepositEntity deposit1 = generateCrmTbDepositEntityByClient(data.clientHelper);
        deposit1.setAmountUsd(BigDecimal.valueOf(100.0));
        data.crmTbDepositObjects = List.of(deposit1);

        //set profit
        Mt5DealsCoercedObject deal = generateMt5DealsCoercedObject(data.clientHelper);
        deal.setProfitUsd(100.0);
        deal.setCommissionUsd(100.0);
        data.mt5DealsCoercedObjects = List.of(deal);

        data.crmTbDepositObjects = List.of(deposit1);

        return data;
    }

    private static DataHelper getWithdrawalNotificationTest9Data() throws IOException {
        DataHelper data = getWithdrawalNotificationRuleData(withdrawalNotificationRuleClient9);
        data.crmWithdrawalEvent.setCheckName("Crypto_Risk");

        return data;
    }

    private static DataHelper getWithdrawalNotificationTest10Data() throws IOException {
        DataHelper data = getWithdrawalNotificationRuleData(withdrawalNotificationRuleClient10);
        data.crmWithdrawalEvent.setCheckName("Crypto_Risk");
        insertMirrorFlagData(data.clientHelper);

        CrmTbWithdrawalEntity wd1 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        wd1.setAmountUsd(BigDecimal.valueOf(100.0));
        OffsetDateTime dateTime1 = OffsetDateTime.now();
        wd1.setCreateTimeUtc(dateTime1);

        CrmTbWithdrawalEntity wd2 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        wd2.setAmountUsd(BigDecimal.valueOf(100.0));
        OffsetDateTime dateTime2 = OffsetDateTime.now().minusHours(3);
        wd2.setCreateTimeUtc(dateTime2);

        data.crmTbWithdrawalObjects = List.of(wd1, wd2);

        UcidGeneralScore score1 = generateUcidGeneralScoreObject(data.clientHelper, 0.11, 0.12);
        String timestamp2 = getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 0, 2);
        score1.setTimeUtc(timestamp2);

        UcidGeneralScore score2 = generateUcidGeneralScoreObject(data.clientHelper, 0.21, 0.22);
        String timestamp3 = getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 0, 4);
        score2.setTimeUtc(timestamp3);
        data.ucidGeneralScores = List.of(score1, score2);

        return data;
    }

    private static DataHelper getWithdrawalNotificationTest11Data() throws IOException {
        DataHelper data = getWithdrawalNotificationRuleData(withdrawalNotificationRuleClient11);
        data.crmWithdrawalEvent.setCheckName("Crypto_Risk");
        insertMirrorFlagData(data.clientHelper);

        CrmTbWithdrawalEntity wd1 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        wd1.setAmountUsd(BigDecimal.valueOf(100.0));
        OffsetDateTime dateTime1 = OffsetDateTime.now();
        wd1.setCreateTime(dateTime1);

        CrmTbWithdrawalEntity wd2 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        wd2.setAmountUsd(BigDecimal.valueOf(100.0));
        OffsetDateTime dateTime2 = OffsetDateTime.now().minusHours(3);
        wd2.setCreateTime(dateTime2);

        data.crmTbWithdrawalObjects = List.of(wd1, wd2);

        UcidGeneralScore score1 = generateUcidGeneralScoreObject(data.clientHelper, 0.21, .22);
        String timestamp2 = getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 0, 2);
        score1.setTimeUtc(timestamp2);

        UcidGeneralScore score2 = generateUcidGeneralScoreObject(data.clientHelper, .11, .12);
        String timestamp3 = getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 0, 4);
        score2.setTimeUtc(timestamp3);
        data.ucidGeneralScores = List.of(score1, score2);

        return data;
    }

    private static DataHelper getWithdrawalNotificationTest12Data() throws IOException {
        DataHelper data = getWithdrawalNotificationRuleData(withdrawalNotificationRuleClient12);
        data.crmWithdrawalEvent.setCheckName("Crypto_Risk");

        return data;
    }

    private static DataHelper getWithdrawalNotificationTest13Data() throws IOException {
        DataHelper data = getWithdrawalNotificationRuleData(withdrawalNotificationRuleClient13);
        data.crmWithdrawalEvent.setCheckName("Crypto_Risk");
        insertMirrorTradeOnLastWithdrawalData(data.clientHelper);

        return data;
    }

    public static Map<String, DataHelper> setupRrEventsWithdrawalNotificationData() throws IOException {
        startSshTunnel();
        Map<String, DataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("1", getWithdrawalNotificationTest1Data());
        map.put("2", getWithdrawalNotificationTest2Data());
        map.put("5", getWithdrawalNotificationTest5Data());
        map.put("6", getWithdrawalNotificationTest6Data());
        map.put("7", getWithdrawalNotificationTest7Data());
        map.put("8", getWithdrawalNotificationTest8Data());
        map.put("9", getWithdrawalNotificationTest9Data());
        map.put("10", getWithdrawalNotificationTest10Data());
        map.put("11", getWithdrawalNotificationTest11Data());
        map.put("12", getWithdrawalNotificationTest12Data());
        map.put("13", getWithdrawalNotificationTest13Data());

        setupData(map);

        return map;
    }
}
