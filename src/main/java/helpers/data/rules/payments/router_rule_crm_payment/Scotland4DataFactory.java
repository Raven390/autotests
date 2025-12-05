package helpers.data.rules.payments.router_rule_crm_payment;

import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntity;
import business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedObject;
import business_objects.db.clickhouse.mt_tb_credits.MtTbCreditsObject;
import business_objects.kafka.crm_events.CrmWithdrawalEvent;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import io.qameta.allure.Description;
import utils.Utils;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient;
import static business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedFactory.generateMt5DealsCoercedObject;
import static business_objects.db.clickhouse.mt_tb_credits.MtTbCreditsObjectFactory.generateCreditsByClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.DataHelper.createClient;
import static helpers.data.DataHelper.setupData;
import static helpers.database.DbHelper.startSshTunnel;
import static utils.Constants.CRM_WITHDRAWAL_EVENT;
import static utils.Constants.PAYMENT_PROVIDER_FASAPAY;
import static utils.Utils.getRandomIntPositive;
import static utils.Utils.getRandomUuidString;

public class Scotland4DataFactory {

    private static final ClientHelper scotland4RuleClient1 = getRandomVantageClientAllFields();
    private static final ClientHelper scotland4RuleClient2 = getRandomVantageClientAllFields();
    private static final ClientHelper scotland4RuleClient3 = getRandomVantageClientAllFields();
    private static final ClientHelper scotland4RuleClient4 = getRandomVantageClientAllFields();

    @Description("Create data for Scotland4 rule")
    private static DataHelper getScotland4RuleData(ClientHelper client) {
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

    private static DataHelper getScotland4Test1Data() throws IOException {
        DataHelper data = getScotland4RuleData(scotland4RuleClient1);
        data.crmWithdrawalEvent.setCheckName("Not_Crypto_Risk");
        return data;
    }

    private static DataHelper getScotland4Test2Data() throws IOException {
        DataHelper data = getScotland4RuleData(scotland4RuleClient2);
        data.crmWithdrawalEvent.setCheckName("Not_Crypto_Risk");
        return data;
    }

    private static DataHelper getScotland4Test3Data() throws IOException {
        DataHelper data = getScotland4RuleData(scotland4RuleClient3);
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

    private static DataHelper getScotland4Test4Data() throws IOException {
        DataHelper data = getScotland4RuleData(scotland4RuleClient4);
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

    public static Map<String, DataHelper> setupScotland4RuleData() throws IOException {
        startSshTunnel();
        Map<String, DataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("1", getScotland4Test1Data());
        map.put("2", getScotland4Test2Data());
        map.put("3", getScotland4Test3Data());
        map.put("4", getScotland4Test4Data());

        setupData(map);

        return map;
    }
}
