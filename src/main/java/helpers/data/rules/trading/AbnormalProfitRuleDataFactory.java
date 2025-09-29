package helpers.data.rules.trading;

import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositObject;
import business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedObject;
import business_objects.kafka.mt_events.CloseTradeMtEvent;
import generator.annotations.RuleTestData;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountData;
import static business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositObjectFactory.generateDepositByClient;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedFactory.generateTradeByClient;
import static helpers.data.ClientFactory.getRandomVantageClientNoCpaIbRef;
import static helpers.data.DataHelper.setupData;
import static helpers.database.DbHelper.startSshTunnel;
import static utils.Utils.*;

@RuleTestData("abnormal-profit")
public class AbnormalProfitRuleDataFactory {

    private static final ClientHelper abnormalProfitRuleExitEventEnd1Client = getRandomVantageClientNoCpaIbRef();
    private static final ClientHelper abnormalProfitRuleExitEventEnd2Client = getRandomVantageClientNoCpaIbRef();

    @Step("Create data for Mirror trading rule")
    private static DataHelper getAbnormalProfitRuleData(ClientHelper client) {
        DataHelper ruleData = new DataHelper();
        ruleData.crmTbUserObject = generateUserByClient(client);
        ruleData.crmTbAccountObject = generateCrmTbAccountData(client);
        ruleData.closeTradeEvent = new CloseTradeMtEvent(getRandomUuidString(), Instant.now().toString(), getRandomIntPositive().longValue(), ruleData.crmTbAccountObject.account, 100d, "EURUSD", ruleData.crmTbAccountObject.serverIdSt, "closeTrade");
        return ruleData;
    }

    private static DataHelper getAbnormalProfitRuleExitEventEnd1Data() {
        Allure.step("Get client data");
        DataHelper data = getAbnormalProfitRuleData(abnormalProfitRuleExitEventEnd1Client);
        CrmTbDepositObject deposit = generateDepositByClient(abnormalProfitRuleExitEventEnd1Client);
        deposit.setAmountUsd(10d);

        Mt5DealsCoercedObject trade = generateTradeByClient(abnormalProfitRuleExitEventEnd1Client);
        trade.setProfitUsd(2001d);

        data.crmTbDepositObjects.add(deposit);
        data.mt5DealsCoercedObjects.add(trade);
        return data;
    }

    private static DataHelper getAbnormalProfitRuleExitEventEnd2Data() {
        Allure.step("Get client data");
        DataHelper data = getAbnormalProfitRuleData(abnormalProfitRuleExitEventEnd2Client);
        CrmTbDepositObject deposit = generateDepositByClient(abnormalProfitRuleExitEventEnd2Client);
        deposit.setAmountUsd(10d);
        Mt5DealsCoercedObject trade = generateTradeByClient(abnormalProfitRuleExitEventEnd2Client);
        trade.setProfitUsd(1999d);

        data.crmTbDepositObjects.add(deposit);
        data.mt5DealsCoercedObjects.add(trade);
        return data;
    }

    private static Map<String, DataHelper> setupAbnormalProfitRuleData() {
        startSshTunnel();
        Map<String, DataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("1", getAbnormalProfitRuleExitEventEnd1Data());
        map.put("2", getAbnormalProfitRuleExitEventEnd2Data());

        // Loop through the list with data and insert all the data into the according tables
        setupData(map);
        return map;
    }
}
