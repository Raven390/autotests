package helpers.data.rules.loss_voucher_rule;

import business_objects.db.clickhouse.crm_tb_bonus_table.CrmTbBonusObject;
import business_objects.db.clickhouse.mt_balance_orders_table.MtBalanceOrdersObject;
import business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedObject;
import business_objects.kafka.crm_events.EgWithdrawalEvent;
import generator.annotations.RuleTestData;
import helpers.data.ClientHelper;
import helpers.data.rules.RuleDataHelper;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountData;
import static business_objects.db.clickhouse.crm_tb_bonus_table.CrmTbBonusObjectFactory.generateBonusByClient;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByClient;
import static business_objects.db.clickhouse.mt_balance_orders_table.MtBalanceOrdersObjectFactory.generateMtBalanceOrders;
import static business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedFactory.generateTradeByClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.rules.RuleDataHelper.deleteRuleData;
import static helpers.data.rules.RuleDataHelper.setupRuleData;
import static helpers.database.DbHelper.startSshTunnel;
import static utils.Utils.*;

@RuleTestData("loss-voucher")
public class LossVoucherRuleDataFactory {
    private static final ClientHelper lossVoucherRuleExitEventEnd1_1Client = getRandomVantageClientAllFields();
    private static final ClientHelper lossVoucherRuleExitEventEnd1_2Client = getRandomVantageClientAllFields();
    private static final ClientHelper lossVoucherRuleExitEventEnd2Client = getRandomVantageClientAllFields();
    private static final ClientHelper lossVoucherRuleExitEventEnd3Client = getRandomVantageClientAllFields();

    @Step("Create data for Mirror trading rule")
    private static RuleDataHelper getLossVoucherProfitRuleData(ClientHelper client) {
        RuleDataHelper ruleData = new RuleDataHelper();
        ruleData.crmTbUserObject = generateUserByClient(client);
        ruleData.crmTbAccountObject = generateCrmTbAccountData(client);
        ruleData.mtAccountObject = generateMtAccountByClient(client);
        ruleData.withdrawalEvent = new EgWithdrawalEvent(getRandomUuidString(), Instant.now().toString(), getRandomIntPositive(), client.getUserId(), client.getTradingAccount(), client.getBrand(), client.getRegulator(), "FASAPAY", 1, 1d, 1d, 1d, 1d, "555555**** **6666", 1, Instant.now().toString(), "", "", 1, "", 1d, 1, 1, "", 1, 1, 1d, 2, 1d, "egWithdrawal");
        return ruleData;
    }

    public static RuleDataHelper getLossVoucherRuleExitEventEnd11Data() {
        Allure.step("Get client data");
        RuleDataHelper data = getLossVoucherProfitRuleData(lossVoucherRuleExitEventEnd1_1Client);

        return data;
    }

    public static RuleDataHelper getLossVoucherRuleExitEventEnd12Data() {
        Allure.step("Get client data");
        RuleDataHelper data = getLossVoucherProfitRuleData(lossVoucherRuleExitEventEnd1_2Client);
        CrmTbBonusObject bonus = generateBonusByClient(lossVoucherRuleExitEventEnd1_2Client);
        MtBalanceOrdersObject balanceOrder = generateMtBalanceOrders(lossVoucherRuleExitEventEnd1_2Client, 100d, 100d, getCurrentTimestampDbFormat());
        balanceOrder.comment = "Trade Loss";
        data.mtBalanceOrdersObjects.add(balanceOrder);

        bonus.type = "Cash Adjustment - Debt W/O";
        Allure.step("-200 < Lifetime PnL < 200 USD = true");
        data.mtAccountObject.balance = 199d;
        data.mtAccountObject.balanceUsd = 199d;

        Mt5DealsCoercedObject deal = generateTradeByClient(lossVoucherRuleExitEventEnd1_2Client);
        deal.setServerId(data.clientHelper.getServerId());
        deal.setAccount(data.clientHelper.getTradingAccount());
        deal.setProfit(201d);
        deal.setStorage(1d);
        deal.setProfitUsd(201d);
        deal.setStorageUsd(1d);
        deal.setCommission(1d);
        deal.setCommissionUsd(1d);
        data.mt5DealsCoercedObjects.add(deal);

        System.out.println(data.clientHelper.getServerId());
        System.out.println(data.clientHelper.getUcid());
        System.out.println(data.mtAccountObject.account);

//        data.loyaltyObjects.add(loyalty);
        data.crmTbBonusObjects.add(bonus);
        return data;
    }

    public static RuleDataHelper getLossVoucherRuleExitEventEnd2Data() {
        Allure.step("Get client data");
        RuleDataHelper data = getLossVoucherProfitRuleData(lossVoucherRuleExitEventEnd2Client);
        CrmTbBonusObject bonus = generateBonusByClient(lossVoucherRuleExitEventEnd2Client);
        MtBalanceOrdersObject balanceOrder = generateMtBalanceOrders(lossVoucherRuleExitEventEnd2Client, 100d, 100d, getCurrentTimestampDbFormat());
        balanceOrder.comment = "Trade Loss";
        data.mtBalanceOrdersObjects.add(balanceOrder);
        bonus.type = "Cash Adjustment - Debt W/O";

        Allure.step("-200 < Lifetime PnL < 200 USD = true");
        data.mtAccountObject.balance = 1d;
        data.mtAccountObject.balanceUsd = 1d;

        Mt5DealsCoercedObject deal = generateTradeByClient(lossVoucherRuleExitEventEnd2Client);
        deal.setServerId(data.clientHelper.getServerId());
        deal.setAccount(data.clientHelper.getTradingAccount());
        deal.setProfit(-100d);
        deal.setStorage(1d);
        deal.setProfitUsd(-100d);
        deal.setStorageUsd(1d);
        deal.setCommission(1d);
        deal.setCommissionUsd(1d);
        data.mt5DealsCoercedObjects.add(deal);

        Allure.step("Sum Loss Vouchers amount = +/-20% |PnL| = false");

        data.crmTbBonusObjects.add(bonus);
        data.mt5DealsCoercedObjects.add(deal);
        return data;
    }

    public static RuleDataHelper getLossVoucherRuleExitEventEnd3Data() {
        Allure.step("Get client data");
        RuleDataHelper data = getLossVoucherProfitRuleData(lossVoucherRuleExitEventEnd3Client);
        CrmTbBonusObject bonus = generateBonusByClient(lossVoucherRuleExitEventEnd3Client);
        MtBalanceOrdersObject balanceOrder = generateMtBalanceOrders(lossVoucherRuleExitEventEnd3Client, 100d, 100d, getCurrentTimestampDbFormat());
        balanceOrder.comment = "Trade Loss";
        data.mtBalanceOrdersObjects.add(balanceOrder);
        bonus.type = "Cash Adjustment - Debt W/O";

        Allure.step("-200 < Lifetime PnL < 200 USD = true");
        data.mtAccountObject.balance = 1d;
        data.mtAccountObject.balanceUsd = 1d;

        Mt5DealsCoercedObject deal = generateTradeByClient(lossVoucherRuleExitEventEnd3Client);
        deal.setServerId(data.clientHelper.getServerId());
        deal.setAccount(data.clientHelper.getTradingAccount());
        deal.setProfit(110d);
        deal.setStorage(1d);
        deal.setProfitUsd(110d);
        deal.setStorageUsd(1d);
        deal.setCommission(1d);
        deal.setCommissionUsd(1d);
        data.mt5DealsCoercedObjects.add(deal);

        Allure.step("Sum Loss Vouchers amount = +/-20% |PnL| = false");

        data.crmTbBonusObjects.add(bonus);
        data.mt5DealsCoercedObjects.add(deal);
        return data;
    }

    public static Map<String, RuleDataHelper> setupLossVoucherRuleData() {
        startSshTunnel();
        Map<String, RuleDataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("11", getLossVoucherRuleExitEventEnd11Data());
        map.put("12", getLossVoucherRuleExitEventEnd12Data());
        map.put("2", getLossVoucherRuleExitEventEnd2Data());
        map.put("3", getLossVoucherRuleExitEventEnd3Data());

        // Loop through the list with data and insert all the data into the according tables
        setupRuleData(map);
        return map;
    }

    public static void deleteLossVoucherRuleData(Map<String, RuleDataHelper> map) throws Exception {
        deleteRuleData(map);
    }
}
