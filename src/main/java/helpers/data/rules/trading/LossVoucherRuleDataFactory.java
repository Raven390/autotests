package helpers.data.rules.trading;

import business_objects.db.clickhouse.crm_tb_bonus_table.CrmTbBonusObject;
import business_objects.db.clickhouse.mt_balance_orders_table.MtBalanceOrdersObject;
import business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedObject;
import generator.annotations.RuleTestData;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;

import java.util.HashMap;
import java.util.Map;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountData;
import static business_objects.db.clickhouse.crm_tb_bonus_table.CrmTbBonusObjectFactory.generateBonusByClient;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByClient;
import static business_objects.db.clickhouse.mt_balance_orders_table.MtBalanceOrdersObjectFactory.generateMtBalanceOrder;
import static business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedFactory.generateTradeByClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.DataHelper.setupData;
import static helpers.database.DbHelper.startSshTunnel;
import static utils.Utils.*;

@RuleTestData("loss-voucher")
public class LossVoucherRuleDataFactory {
    private static final ClientHelper lossVoucherRuleExitEventEnd1_1Client = getRandomVantageClientAllFields();
    private static final ClientHelper lossVoucherRuleExitEventEnd1_2Client = getRandomVantageClientAllFields();
    private static final ClientHelper lossVoucherRuleExitEventEnd2Client = getRandomVantageClientAllFields();
    private static final ClientHelper lossVoucherRuleExitEventEnd3Client = getRandomVantageClientAllFields();

    @Step("Create data for Mirror trading rule")
    private static DataHelper getLossVoucherProfitRuleData(ClientHelper client) {
        DataHelper ruleData = new DataHelper();
        ruleData.crmTbUserObject = generateUserByClient(client);
        ruleData.crmTbAccountObject = generateCrmTbAccountData(client);
        ruleData.mtAccountObject = generateMtAccountByClient(client);
        return ruleData;
    }

    private static DataHelper getLossVoucherRuleExitEventEnd11Data() {
        Allure.step("Get client data");
        DataHelper data = getLossVoucherProfitRuleData(lossVoucherRuleExitEventEnd1_1Client);

        return data;
    }

    private static DataHelper getLossVoucherRuleExitEventEnd12Data() {
        Allure.step("Get client data");
        DataHelper data = getLossVoucherProfitRuleData(lossVoucherRuleExitEventEnd1_2Client);
        CrmTbBonusObject bonus = generateBonusByClient(lossVoucherRuleExitEventEnd1_2Client);
        MtBalanceOrdersObject balanceOrder = generateMtBalanceOrder(lossVoucherRuleExitEventEnd1_2Client, 100d, 100d, getCurrentTimestampDbFormat());
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

        writeLog(data.clientHelper.getServerId());
        writeLog(data.clientHelper.getUcid());
        writeLog(data.mtAccountObject.account);

        data.crmTbBonusObjects.add(bonus);
        return data;
    }

    private static DataHelper getLossVoucherRuleExitEventEnd2Data() {
        Allure.step("Get client data");
        DataHelper data = getLossVoucherProfitRuleData(lossVoucherRuleExitEventEnd2Client);
        CrmTbBonusObject bonus = generateBonusByClient(lossVoucherRuleExitEventEnd2Client);
        MtBalanceOrdersObject balanceOrder = generateMtBalanceOrder(lossVoucherRuleExitEventEnd2Client, 100d, 100d, getCurrentTimestampDbFormat());
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

    private static DataHelper getLossVoucherRuleExitEventEnd3Data() {
        Allure.step("Get client data");
        DataHelper data = getLossVoucherProfitRuleData(lossVoucherRuleExitEventEnd3Client);
        CrmTbBonusObject bonus = generateBonusByClient(lossVoucherRuleExitEventEnd3Client);
        MtBalanceOrdersObject balanceOrder = generateMtBalanceOrder(lossVoucherRuleExitEventEnd3Client, 100d, 100d, getCurrentTimestampDbFormat());
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

    public static Map<String, DataHelper> setupLossVoucherRuleData() {
        startSshTunnel();
        Map<String, DataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("11", getLossVoucherRuleExitEventEnd11Data());
        map.put("12", getLossVoucherRuleExitEventEnd12Data());
        map.put("2", getLossVoucherRuleExitEventEnd2Data());
        map.put("3", getLossVoucherRuleExitEventEnd3Data());

        // Loop through the list with data and insert all the data into the according tables
        setupData(map);
        return map;
    }
}
