package helpers.data.rules.trading;

import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntity;
import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntityFactory;
import business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedObject;
import business_objects.kafka.crm_events.EgWithdrawalEvent;
import generator.annotations.RuleTestData;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountData;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedFactory.generateTradeByClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.DataHelper.setupData;
import static utils.Utils.*;

@RuleTestData("nbp-winning-leg")
public class NbpWinningLegRuleDataFactory {

    // Clients
    private static final ClientHelper nbpWinningLegExit1Client = getRandomVantageClientAllFields();
    private static final ClientHelper nbpLosingLegExit2Client = getRandomVantageClientAllFields();
    private static final ClientHelper nbpLosingLegExit3Client = getRandomVantageClientAllFields();

    private static DataHelper getNbpWinningLegRuleData(ClientHelper client) {
        DataHelper data = new DataHelper();
        data.clientHelper = client;
        data.crmTbUserObject = generateUserByClient(client);
        data.withdrawalEvent = new EgWithdrawalEvent(getRandomUuidString(), Instant.now().toString(), getRandomIntPositive(), client.getUserId(), client.getTradingAccount(), client.getBrand(), client.getRegulator(), "FASAPAY", 1, 1d, 1d, 1d, 1d, "555555**** **6666", 1, Instant.now().toString(), "", "", 1, "", 1d, 1, 1, "", 1, 1, 1d, 2, 1d, "egWithdrawal");
        CrmTbDepositEntity deposit = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client);
        deposit.setAmountUsd(BigDecimal.valueOf(199.0));
        data.crmTbDepositObjects.add(deposit);
        return data;
    }

    private static DataHelper getNbpWinningLegExit1Data() {
        return getNbpWinningLegRuleData(nbpWinningLegExit1Client);
    }

    private static DataHelper getNbpWinningLegExit2Data() {
        DataHelper data = getNbpWinningLegRuleData(nbpLosingLegExit2Client);
        CrmTbDepositEntity deposit = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(data.clientHelper);
        deposit.setAmountUsd(BigDecimal.valueOf(200.0));
        data.crmTbDepositObjects.add(deposit);
        return data;
    }

    private static DataHelper getNbpWinningLegExit3Data() {
        DataHelper data = getNbpWinningLegRuleData(nbpLosingLegExit3Client);
        CrmTbDepositEntity deposit = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(data.clientHelper);
        deposit.setAmountUsd(BigDecimal.valueOf(200.0));
        deposit.setStatus("Success");
        data.crmTbDepositObjects.add(deposit);
        Mt5DealsCoercedObject trade = generateTradeByClient(data.clientHelper);
        trade.setProfitUsd(4000.0);
        data.mt5DealsCoercedObjects.add(trade);
        data.crmTbAccountObject = generateCrmTbAccountData(data.clientHelper);
        return data;
    }

    public static Map<String, DataHelper> setupNbpWinningLegRuleData() {
        Map<String, DataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("1", getNbpWinningLegExit1Data());
        map.put("2", getNbpWinningLegExit2Data());
        map.put("3", getNbpWinningLegExit3Data());
        setupData(map);
        return map;
    }
}
