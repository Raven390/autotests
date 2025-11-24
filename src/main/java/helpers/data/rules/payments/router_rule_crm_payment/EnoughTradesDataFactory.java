package helpers.data.rules.payments.router_rule_crm_payment;

import business_objects.api.payment_gate.rule_executions.PostRuleExecutionsBody;
import business_objects.db.clickhouse.crm_tb_deposit_channel.CrmTbDepositChannelObject;
import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntity;
import business_objects.db.clickhouse.crm_tb_deposit_type.CrmTbDepositTypeObject;
import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalEntity;
import business_objects.db.clickhouse.crm_tb_withdrawal_type.CrmTbWithdrawalTypeObject;
import business_objects.db.clickhouse.mt_mt4_trades.MtMt4TradesObject;
import business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedObject;
import business_objects.db.clickhouse.mt_tb_credits.MtTbCreditsObject;
import business_objects.db.clickhouse.s3_fact_login_metrics.S3FactLoginMetricsObject;
import business_objects.db.payment_gate.payment_details.PaymentDetailsObject;
import business_objects.db.payment_gate.payment_events.PaymentEventsObject;
import business_objects.db.payment_gate.payment_rule_executions.PaymentRuleExecutionsObject;
import business_objects.kafka.crm_events.CrmWithdrawalEvent;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import helpers.database.DbName;
import io.qameta.allure.Description;
import utils.Utils;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static business_objects.api.payment_gate.rule_executions.RuleExecutionsRequestBodyFactory.generatePostRuleExecutionsBody;
import static business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient;
import static business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient;
import static business_objects.db.clickhouse.mt_mt4_trades.MtMt4TradesObjectFactory.generateMt4TradesObject;
import static business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedFactory.generateMt5DealsCoercedObject;
import static business_objects.db.clickhouse.mt_tb_credits.MtTbCreditsObjectFactory.generateCreditsByClient;
import static business_objects.db.clickhouse.s3_fact_login_metrics.S3FactLoginMetricsFactory.generateS3FactLoginMetricsClientZero;
import static business_objects.db.payment_gate.payment_details.PaymentDetailsObjectFactory.generatePaymentDetailsObject;
import static business_objects.db.payment_gate.payment_events.PaymentEventsObjectFactory.generatePaymentEventsObject;
import static business_objects.db.payment_gate.payment_rule_executions.PaymentRuleExecutionsObjectFactory.generatePaymentRuleExecutionsObject;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.DataHelper.createClient;
import static helpers.data.DataHelper.setupData;
import static helpers.database.DbHelper.*;
import static utils.Constants.*;
import static utils.Utils.*;

public class EnoughTradesDataFactory {
    private static final ClientHelper enoughTradesRuleClient1 = getRandomVantageClientAllFields();
    private static final ClientHelper enoughTradesRuleClient2 = getRandomVantageClientAllFields();
    private static final ClientHelper enoughTradesRuleClient3 = getRandomVantageClientAllFields();
    private static final ClientHelper enoughTradesRuleClient4 = getRandomVantageClientAllFields();
    private static final ClientHelper enoughTradesRuleClientAlert1 = getRandomVantageClientAllFields();
    private static final ClientHelper enoughTradesRuleClientAlert2 = getRandomVantageClientAllFields();

    @Description("Create data for Withdrawal Integrity check rule")
    private static DataHelper getEnoughTradesRuleData(ClientHelper client) {
        DataHelper data = new DataHelper();
        createClient(data, client);

        data.crmWithdrawalEvent = new CrmWithdrawalEvent(
                "MT4",                            // accountType
                Utils.getRandomIntPositive().toString(),      // binNumber
                data.clientHelper.getBrand().toLowerCase(),   // brand
                "",                                           // checkName
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

    private static DataHelper getEnoughTradesTest1Data() {
        DataHelper data = getEnoughTradesRuleData(enoughTradesRuleClient1);
        data.crmWithdrawalEvent.setAccountCategory("IB");
        return data;
    }

    private static DataHelper getEnoughTradesTest2Data() {
        DataHelper data = getEnoughTradesRuleData(enoughTradesRuleClient2);
        data.crmWithdrawalEvent.setFundType("PAMM");

        return data;
    }

    private static DataHelper getEnoughTradesTest3Data() {
        DataHelper data = getEnoughTradesRuleData(enoughTradesRuleClient3);
        data.crmWithdrawalEvent.setFundType("MAM");
        return data;
    }

    private static DataHelper getEnoughTradesTest4Data() {
        DataHelper data = getEnoughTradesRuleData(enoughTradesRuleClient4);
        return data;
    }

    private static DataHelper getEnoughTradesTestAlert1Data() {
        DataHelper data = getEnoughTradesRuleData(enoughTradesRuleClientAlert1);
        data.crmWithdrawalEvent.setWithdrawalAmountUSD(1000.0);
        CrmTbWithdrawalEntity withdrawal1 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        CrmTbWithdrawalEntity withdrawal2 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        CrmTbWithdrawalEntity withdrawal3 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        data.crmTbWithdrawalObjects = (List.of(withdrawal1, withdrawal2, withdrawal3));
        int wdTypeId = getRandomBytePositive();
        int sourceId = getRandomBytePositive();
        int pcId = getRandomBytePositive();
        String catName = "International cat";
        CrmTbWithdrawalTypeObject wdType = CrmTbWithdrawalTypeObject.builder().id(wdTypeId).sourceIdSt(sourceId).category(1).enName(catName).lastUpdated(getCurrentTimestampDbFormat()).build();
        data.crmTbWithdrawalTypeObjects = List.of(wdType);
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setSourceIdSt(sourceId));
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setPaymentTypeId(wdTypeId));
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setAmountUsd(BigDecimal.valueOf(200.0)));
        CrmTbDepositEntity deposit1 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit2 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit3 = generateCrmTbDepositEntityByClient(data.clientHelper);
        data.crmTbDepositObjects = List.of(deposit1, deposit2, deposit3);
        data.crmTbDepositObjects.forEach(d -> d.setSourceIdSt(sourceId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentTypeId(wdTypeId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentChannelId(pcId));
        data.crmTbDepositObjects.forEach(d -> d.setAmountUsd(BigDecimal.valueOf(250.0)));
        CrmTbDepositTypeObject dType = CrmTbDepositTypeObject.builder().id(wdTypeId).sourceIdSt(sourceId).category(1).name(catName).lastUpdated(getCurrentTimestampDbFormat()).build();
        data.crmTbDepositTypeObjects = List.of(dType);
        CrmTbDepositChannelObject dChannel = CrmTbDepositChannelObject.builder().id(pcId).sourceIdSt(sourceId).channelId(pcId).typeId(wdTypeId).name(catName).isMobileChannel(0).lastUpdated(getCurrentTimestampDbFormat()).build();
        data.crmTbDepositChannelObjects = List.of(dChannel);

        MtTbCreditsObject credit = generateCreditsByClient(data.clientHelper);//set credits
        credit.setAmountUsd(100.0);
        data.mtTbCreditsObjects = List.of(credit);
        Mt5DealsCoercedObject deal = generateMt5DealsCoercedObject(data.clientHelper);//set profit
        deal.setProfitUsd(1000.0);
        deal.setCommissionUsd(1000.0);
        data.mt5DealsCoercedObjects = List.of(deal);

        S3FactLoginMetricsObject loginMetrics = generateS3FactLoginMetricsClientZero(data.clientHelper);
        loginMetrics.setDailyTakerSpreadRevenueOz(1000.0);
        data.S3FactLoginMetricsObjects = List.of(loginMetrics);

        MtMt4TradesObject trade = generateMt4TradesObject(data.clientHelper);
        trade.setCloseTime(null);
        trade.setCloseTimeUtc(null);
        trade.setProfitUsd(1.0);
        trade.setCommissionUsd(1.0);
        trade.setStorageUsd(1.0);
        data.MtMt4TradesObjects = List.of(trade);

        return data;

    }

    private static DataHelper getEnoughTradesTestAlert2Data() {
        DataHelper data = getEnoughTradesRuleData(enoughTradesRuleClientAlert2);
        data.crmWithdrawalEvent.setWithdrawalAmountUSD(1000.0);
        CrmTbWithdrawalEntity withdrawal1 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        CrmTbWithdrawalEntity withdrawal2 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        CrmTbWithdrawalEntity withdrawal3 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        data.crmTbWithdrawalObjects = (List.of(withdrawal1, withdrawal2, withdrawal3));
        int wdTypeId = getRandomBytePositive();
        int sourceId = getRandomBytePositive();
        int pcId = getRandomBytePositive();
        String catName = "International cat";
        CrmTbWithdrawalTypeObject wdType = CrmTbWithdrawalTypeObject.builder().id(wdTypeId).sourceIdSt(sourceId).category(1).enName(catName).lastUpdated(getCurrentTimestampDbFormat()).build();
        data.crmTbWithdrawalTypeObjects = List.of(wdType);
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setSourceIdSt(sourceId));
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setPaymentTypeId(wdTypeId));
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setAmountUsd(BigDecimal.valueOf(200.0)));
        CrmTbDepositEntity deposit1 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit2 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit3 = generateCrmTbDepositEntityByClient(data.clientHelper);
        data.crmTbDepositObjects = List.of(deposit1, deposit2, deposit3);
        data.crmTbDepositObjects.forEach(d -> d.setSourceIdSt(sourceId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentTypeId(wdTypeId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentChannelId(pcId));
        data.crmTbDepositObjects.forEach(d -> d.setAmountUsd(BigDecimal.valueOf(250.0)));
        CrmTbDepositTypeObject dType = CrmTbDepositTypeObject.builder().id(wdTypeId).sourceIdSt(sourceId).category(1).name(catName).lastUpdated(getCurrentTimestampDbFormat()).build();
        data.crmTbDepositTypeObjects = List.of(dType);
        CrmTbDepositChannelObject dChannel = CrmTbDepositChannelObject.builder().id(pcId).sourceIdSt(sourceId).channelId(pcId).typeId(wdTypeId).name(catName).isMobileChannel(0).lastUpdated(getCurrentTimestampDbFormat()).build();
        data.crmTbDepositChannelObjects = List.of(dChannel);

        MtTbCreditsObject credit = generateCreditsByClient(data.clientHelper);//set credits
        credit.setAmountUsd(100.0);
        data.mtTbCreditsObjects = List.of(credit);
        Mt5DealsCoercedObject deal = generateMt5DealsCoercedObject(data.clientHelper);//set profit
        deal.setProfitUsd(1000.0);
        deal.setCommissionUsd(1000.0);
        data.mt5DealsCoercedObjects = List.of(deal);

        S3FactLoginMetricsObject loginMetrics = generateS3FactLoginMetricsClientZero(data.clientHelper);
        loginMetrics.setDailyTakerSpreadRevenueOz(1000.0);
        data.S3FactLoginMetricsObjects = List.of(loginMetrics);

        MtMt4TradesObject trade = generateMt4TradesObject(data.clientHelper);
        trade.setCloseTime(null);
        trade.setCloseTimeUtc(null);
        trade.setProfitUsd(1.0);
        trade.setCommissionUsd(1.0);
        trade.setStorageUsd(1.0);
        data.MtMt4TradesObjects = List.of(trade);

        PaymentEventsObject paymentEventsObject1 = generatePaymentEventsObject(data.clientHelper);
        PaymentDetailsObject paymentDetailsObject1 = generatePaymentDetailsObject(paymentEventsObject1, data.clientHelper);
        PostRuleExecutionsBody postRuleExecutionsBody1 = generatePostRuleExecutionsBody(paymentEventsObject1);
        PaymentRuleExecutionsObject paymentRuleExecutionsObject1 = generatePaymentRuleExecutionsObject(paymentEventsObject1);
        paymentRuleExecutionsObject1.setRuleEndId(112);
        paymentRuleExecutionsObject1.setRuleId(3);


        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE, List.of(paymentEventsObject1));
        insertObjectToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_RULE_EXECUTIONS_TABLE, paymentRuleExecutionsObject1);
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE, List.of(paymentDetailsObject1));


        return data;

    }

    public static Map<String, DataHelper> setupEnoughTradesRuleData() {
        startSshTunnel();
        Map<String, DataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("1", getEnoughTradesTest1Data());
        map.put("2", getEnoughTradesTest2Data());
        map.put("3", getEnoughTradesTest3Data());
        map.put("4", getEnoughTradesTest4Data());
        map.put("770", getEnoughTradesTestAlert1Data());
        map.put("771", getEnoughTradesTestAlert2Data());

        setupData(map);

        return map;
    }
}
