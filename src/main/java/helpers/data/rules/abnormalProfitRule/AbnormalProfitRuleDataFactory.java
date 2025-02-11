package helpers.data.rules.abnormalProfitRule;

import businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObject;
import businessObjects.db.clickhouse.crmTbDepositTable.CrmTbDepositObject;
import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;
import businessObjects.db.clickhouse.mtMt5DealsCoerced.Mt5DealsCoercedObject;
import businessObjects.kafka.mtEvents.CloseTradeMtEvent;
import generator.annotations.RuleTestData;
import helpers.data.ClientHelper;
import helpers.data.rules.RuleDataHelper;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;

import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObjectFactory.generateCrmTbAccountData;
import static businessObjects.db.clickhouse.crmTbDepositTable.CrmTbDepositObjectFactory.generateDepositByClient;
import static businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObjectFactory.generateUserByClient;
import static businessObjects.db.clickhouse.mtMt5DealsCoerced.Mt5DealsCoercedFactory.generateTradeByClient;
import static helpers.data.ClientFactory.getRandomVantageClientNoCpaIbRef;
import static helpers.data.rules.RuleDataHelper.deleteRuleData;
import static helpers.data.rules.RuleDataHelper.setupRuleData;
import static helpers.database.DbHelper.startSshTunnel;
import static utils.Utils.*;

@RuleTestData("abnormal-profit")
public class AbnormalProfitRuleDataFactory {

    private static final ClientHelper abnormalProfitRuleExitEventEnd1Client = getRandomVantageClientNoCpaIbRef();
    private static final ClientHelper abnormalProfitRuleExitEventEnd2Client = getRandomVantageClientNoCpaIbRef();

    @Step("Create data for Mirror trading rule")
    private static RuleDataHelper getAbnormalProfitRuleData(ClientHelper client) {
        CrmTbUserObject userObject = generateUserByClient(client);
        CrmTbAccountObject crmTbAccountObject = generateCrmTbAccountData(client);
        CloseTradeMtEvent closeTradeMtEvent = new CloseTradeMtEvent(getRandomUuidString(), Instant.now().toString(), getRandomIntPositive(), crmTbAccountObject.account, 100d, "EURUSD", crmTbAccountObject.serverIdSt, "closeTrade");
        return new RuleDataHelper(client, userObject, null, null, new ArrayList<>(), new ArrayList<>(), null, closeTradeMtEvent, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), crmTbAccountObject, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), null, null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    public static RuleDataHelper getAbnormalProfitRuleExitEventEnd1Data() {
        Allure.step("Get client data");
        RuleDataHelper data = getAbnormalProfitRuleData(abnormalProfitRuleExitEventEnd1Client);
        CrmTbDepositObject deposit = generateDepositByClient(abnormalProfitRuleExitEventEnd1Client);
        deposit.amountUsd = 10d;

        Mt5DealsCoercedObject trade = generateTradeByClient(abnormalProfitRuleExitEventEnd1Client);
        trade.profitUsd = 2001d;

        data.crmTbDepositObjects.add(deposit);
        data.mt5DealsObjects.add(trade);
        return data;
    }

    public static RuleDataHelper getAbnormalProfitRuleExitEventEnd2Data() {
        Allure.step("Get client data");
        RuleDataHelper data = getAbnormalProfitRuleData(abnormalProfitRuleExitEventEnd2Client);
        CrmTbDepositObject deposit = generateDepositByClient(abnormalProfitRuleExitEventEnd2Client);
        deposit.amountUsd = 10d;
        Mt5DealsCoercedObject trade = generateTradeByClient(abnormalProfitRuleExitEventEnd2Client);
        trade.profitUsd = 1999d;

        data.crmTbDepositObjects.add(deposit);
        data.mt5DealsObjects.add(trade);
        return data;
    }

    public static Map<String, RuleDataHelper> setupAbnormalProfitRuleData() throws ReflectiveOperationException,
            SQLException {
        startSshTunnel();
        Map<String, RuleDataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("1", getAbnormalProfitRuleExitEventEnd1Data());
        map.put("2", getAbnormalProfitRuleExitEventEnd2Data());

        // Loop through the list with data and insert all the data into the according tables
        setupRuleData(map);
        return map;
    }

    public static void deleteAbnormalProfitRuleData(Map<String, RuleDataHelper> map) throws Exception {
        deleteRuleData(map);
    }
}
