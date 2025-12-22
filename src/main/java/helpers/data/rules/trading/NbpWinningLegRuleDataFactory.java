package helpers.data.rules.trading;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountData;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedFactory.generateTradeByClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.DataSetupHelper.setupData;
import static utils.Utils.*;

import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntity;
import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntityFactory;
import business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedObject;
import generator.annotations.RuleTestData;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

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
