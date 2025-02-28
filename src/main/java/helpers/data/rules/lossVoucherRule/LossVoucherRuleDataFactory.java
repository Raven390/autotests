package helpers.data.rules.lossVoucherRule;

import businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObject;
import businessObjects.db.clickhouse.crmTbBonusTable.CrmTbBonusObject;
import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;
import businessObjects.db.clickhouse.mtAccount.MtAccountObject;
import businessObjects.db.clickhouse.mtBalanceOrdersTable.MtBalanceOrdersObject;
import businessObjects.db.clickhouse.mtMt5DealsCoerced.Mt5DealsCoercedObject;
import businessObjects.kafka.crmEvents.WithdrawalEvent;
import generator.annotations.RuleTestData;
import helpers.data.ClientHelper;
import helpers.data.rules.RuleDataHelper;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObjectFactory.generateCrmTbAccountData;
import static businessObjects.db.clickhouse.crmTbBonusTable.CrmTbBonusObjectFactory.generateBonusByClient;
import static businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObjectFactory.generateUserByClient;
import static businessObjects.db.clickhouse.mtAccount.MtAccountObjectFactory.generateMtAccountByClient;
import static businessObjects.db.clickhouse.mtBalanceOrdersTable.MtBalanceOrdersObjectFactory.generateBalanceOrders;
import static businessObjects.db.clickhouse.mtMt5DealsCoerced.Mt5DealsCoercedFactory.generateTradeByClient;
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
        CrmTbUserObject userObject = generateUserByClient(client);
        CrmTbAccountObject crmTbAccountObject = generateCrmTbAccountData(client);
        MtAccountObject account = generateMtAccountByClient(client);
        WithdrawalEvent withdrawalEvent = new WithdrawalEvent(getRandomUuidString(), Instant.now().toString(), getRandomIntPositive(), client.getUserId(), client.getTradingAccount(), client.getBrand(), client.getRegulator(), "FASAPAY", 1, 1d, 1d, 1d, 1d, "555555**** **6666", 1, Instant.now().toString(), "", "", 1, "", 1d, 1, 1, "", 1, 1, 1d, 2, 1d, "withdrawal");
        return new RuleDataHelper(client, userObject, null, null, new ArrayList<>(), new ArrayList<>(), withdrawalEvent, null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), crmTbAccountObject, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), null, null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), account, new ArrayList<>(), new ArrayList<>());
    }

    public static RuleDataHelper getLossVoucherRuleExitEventEnd1_1Data() {
        Allure.step("Get client data");
        RuleDataHelper data = getLossVoucherProfitRuleData(lossVoucherRuleExitEventEnd1_1Client);

        return data;
    }

    public static RuleDataHelper getLossVoucherRuleExitEventEnd1_2Data() {
        Allure.step("Get client data");
        RuleDataHelper data = getLossVoucherProfitRuleData(lossVoucherRuleExitEventEnd1_2Client);
        CrmTbBonusObject bonus = generateBonusByClient(lossVoucherRuleExitEventEnd1_2Client);
        MtBalanceOrdersObject balanceOrder = generateBalanceOrders(lossVoucherRuleExitEventEnd1_2Client, 100d, 100d, getCurrentTimestampDbFormat());
        balanceOrder.comment = "Trade Loss";
        data.mtBalanceOrdersObjects.add(balanceOrder);

        bonus.type = "Cash Adjustment - Debt W/O";
        Allure.step("-200 < Lifetime PnL < 200 USD = true");
        data.mtAccountObject.balance = 199d;
        data.mtAccountObject.balanceUsd = 199d;

        Mt5DealsCoercedObject deal = generateTradeByClient(lossVoucherRuleExitEventEnd1_2Client);
        deal.serverId = data.clientHelper.getServerId();
        deal.account = data.clientHelper.getTradingAccount();
        deal.profit = 201d;
        deal.storage = 1;
        deal.profitUsd = 201d;
        deal.storageUsd = 1;
        deal.commission = 1d;
        deal.commissionUsd = 1d;
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
        MtBalanceOrdersObject balanceOrder = generateBalanceOrders(lossVoucherRuleExitEventEnd2Client, 100d, 100d, getCurrentTimestampDbFormat());
        balanceOrder.comment = "Trade Loss";
        data.mtBalanceOrdersObjects.add(balanceOrder);
        bonus.type = "Cash Adjustment - Debt W/O";

        Allure.step("-200 < Lifetime PnL < 200 USD = true");
        data.mtAccountObject.balance = 1d;
        data.mtAccountObject.balanceUsd = 1d;

        Mt5DealsCoercedObject deal = generateTradeByClient(lossVoucherRuleExitEventEnd2Client);
        deal.serverId = data.clientHelper.getServerId();
        deal.account = data.clientHelper.getTradingAccount();
        deal.profit = -100d;
        deal.storage = 1;
        deal.profitUsd = -100d;
        deal.storageUsd = 1;
        deal.commission = 1d;
        deal.commissionUsd = 1d;
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
        MtBalanceOrdersObject balanceOrder = generateBalanceOrders(lossVoucherRuleExitEventEnd3Client, 100d, 100d, getCurrentTimestampDbFormat());
        balanceOrder.comment = "Trade Loss";
        data.mtBalanceOrdersObjects.add(balanceOrder);
        bonus.type = "Cash Adjustment - Debt W/O";

        Allure.step("-200 < Lifetime PnL < 200 USD = true");
        data.mtAccountObject.balance = 1d;
        data.mtAccountObject.balanceUsd = 1d;

        Mt5DealsCoercedObject deal = generateTradeByClient(lossVoucherRuleExitEventEnd3Client);
        deal.serverId = data.clientHelper.getServerId();
        deal.account = data.clientHelper.getTradingAccount();
        deal.profit = 110d;
        deal.storage = 1;
        deal.profitUsd = 110d;
        deal.storageUsd = 1;
        deal.commission = 1d;
        deal.commissionUsd = 1d;
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
        map.put("1_1", getLossVoucherRuleExitEventEnd1_1Data());
        map.put("1_2", getLossVoucherRuleExitEventEnd1_2Data());
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
