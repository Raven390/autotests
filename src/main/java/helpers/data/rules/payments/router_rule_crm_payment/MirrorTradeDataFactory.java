package helpers.data.rules.payments.router_rule_crm_payment;

import static business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient;
import static business_objects.db.data_science.ucid_general_score.UcidGeneralScoreFactory.generateUcidGeneralScoreObject;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.rules.MirrorTradeOnWithdrawalDataInserter.insertMirrorTradeOnWithdrawalData;
import static helpers.database.DbHelper.startSshTunnel;
import static utils.Constants.*;
import static utils.Utils.getRandomIntPositive;
import static utils.Utils.getRandomUuidString;

import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalEntity;
import business_objects.db.data_science.ucid_general_score.UcidGeneralScore;
import business_objects.kafka.crm_events.CrmWithdrawalEventV2;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import helpers.data.enums.rule_engine.Event;
import io.qameta.allure.Description;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import utils.Utils;

public class MirrorTradeDataFactory {
    private static final ClientHelper mirrorTradeRuleClient1 = getRandomVantageClientAllFields();
    private static final ClientHelper mirrorTradeRuleClient2 = getRandomVantageClientAllFields();
    private static final ClientHelper mirrorTradeRuleClient3 = getRandomVantageClientAllFields();

    private static final String time2025 = "2025-01-11T11:11:11.111+00:00";
    private static final String time2026 = "2026-01-11T11:11:11.111+00:00";

    @Description("Create data for Mirror Trade rule")
    private static DataHelper getMirrorTradeRuleData(ClientHelper client) {
        DataHelper data = new DataHelper();
        data.createClient(client);
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
                .withdrawalAmountUSD(1.0) // withdrawalAmount
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

    private static DataHelper getMirrorTradeTest3Data() {
        DataHelper data = getMirrorTradeRuleData(mirrorTradeRuleClient3);
        data.crmTbAccountObject.sourceIdSt = 9;
        data.crmTbAccountObject.brandUid = 4;
        insertMirrorTradeOnWithdrawalData(data.clientHelper);
        return data;
    }

    public static Map<String, DataHelper> setupMirrorTradeRuleData() {
        startSshTunnel();
        Map<String, DataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("1", getMirrorTradeTest1Data());
        map.put("2", getMirrorTradeTest2Data());
        map.put("3", getMirrorTradeTest3Data());
        return map;
    }
}
