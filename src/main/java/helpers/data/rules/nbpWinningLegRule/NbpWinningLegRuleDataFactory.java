package helpers.data.rules.nbpWinningLegRule;

import businessObjects.db.clickhouse.crmTbDepositTable.CrmTbDepositObject;
import businessObjects.db.clickhouse.mtMt5DealsCoerced.Mt5DealsCoercedObject;
import businessObjects.kafka.crmEvents.WithdrawalEvent;
import generator.annotations.RuleTestData;
import helpers.data.ClientHelper;
import helpers.data.rules.RuleDataHelper;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObjectFactory.generateCrmTbAccountData;
import static businessObjects.db.clickhouse.crmTbDepositTable.CrmTbDepositObjectFactory.generateDepositByClient;
import static businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObjectFactory.generateUserByClient;
import static businessObjects.db.clickhouse.mtMt5DealsCoerced.Mt5DealsCoercedFactory.generateTradeByClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.rules.RuleDataHelper.deleteRuleData;
import static helpers.data.rules.RuleDataHelper.setupRuleData;
import static utils.Utils.*;

@RuleTestData("nbp-winning-leg")
public class NbpWinningLegRuleDataFactory {

    // Clients
    private static final ClientHelper nbpWinningLegExit1Client = getRandomVantageClientAllFields();
    private static final ClientHelper nbpLosingLegExit2Client = getRandomVantageClientAllFields();
    private static final ClientHelper nbpLosingLegExit3Client = getRandomVantageClientAllFields();

    private static RuleDataHelper getNbpWinningLegRuleData(ClientHelper client) {
        RuleDataHelper data = new RuleDataHelper();
        data.clientHelper = client;
        data.crmTbUserObject = generateUserByClient(client);
        data.withdrawalEvent = new WithdrawalEvent(getRandomUuidString(), Instant.now().toString(), getRandomIntPositive(), client.getUserId(), client.getTradingAccount(), client.getBrand(), client.getRegulator(), "FASAPAY", 1, 1d, 1d, 1d, 1d, "555555**** **6666", 1, Instant.now().toString(), "", "", 1, "", 1d, 1, 1, "", 1, 1, 1d, 2, 1d, "withdrawal");
        CrmTbDepositObject deposit = generateDepositByClient(client);
        deposit.amountUsd = 199.0;
        data.crmTbDepositObjects.add(deposit);
        return data;
    }

    private static RuleDataHelper getNbpWinningLegExit1Data() {
        return getNbpWinningLegRuleData(nbpWinningLegExit1Client);
    }

    private static RuleDataHelper getNbpWinningLegExit2Data() {
        RuleDataHelper data = getNbpWinningLegRuleData(nbpLosingLegExit2Client);
        CrmTbDepositObject deposit = generateDepositByClient(data.clientHelper);
        deposit.amountUsd = 200.0;
        data.crmTbDepositObjects.add(deposit);
        return data;
    }

    private static RuleDataHelper getNbpWinningLegExit3Data() {
        RuleDataHelper data = getNbpWinningLegRuleData(nbpLosingLegExit3Client);
        CrmTbDepositObject deposit = generateDepositByClient(data.clientHelper);
        deposit.amountUsd = 200.0;
        deposit.status = "Success";
        data.crmTbDepositObjects.add(deposit);
        Mt5DealsCoercedObject trade = generateTradeByClient(data.clientHelper);
        trade.profitUsd = 4000.0;
        data.mt5DealsObjects.add(trade);
        data.crmTbAccountObject = generateCrmTbAccountData(data.clientHelper);
        return data;
    }

    public static Map<String, RuleDataHelper> setupNbpWinningLegRuleData() {
        Map<String, RuleDataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("1", getNbpWinningLegExit1Data());
        map.put("2", getNbpWinningLegExit2Data());
        map.put("3", getNbpWinningLegExit3Data());
        setupRuleData(map);
        return map;
    }

    public static void deleteNbpWinningLegRuleData(Map<String, RuleDataHelper> map) throws Exception {
        deleteRuleData(map);
    }
}
