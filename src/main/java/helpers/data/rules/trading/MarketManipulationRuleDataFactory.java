package helpers.data.rules.trading;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateAccountByClient;
import static business_objects.db.clickhouse.crm_tb_account_for_mt.crm_tb_account.CrmTbAccountForMtObjectFactory.generateAccountForMtByClient;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.dict_is_test.DictIsTestObjectFactory.generateDictIsTestByClientFalse;
import static business_objects.db.clickhouse.dict_is_test.DictIsTestObjectFactory.generateDictIsTestByClientTrue;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByClient;
import static business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedFactory.generateMt5DealsCoercedObject;
import static business_objects.db.data_science.ucid_general_score.UcidGeneralScoreFactory.generateUcidGeneralScoreObject;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.DataSetupHelper.setupData;
import static utils.Constants.*;
import static utils.Utils.*;

import business_objects.kafka.mt_events.CloseTradeMtEvent;
import business_objects.kafka.mt_events.TradeEventMetadata;
import generator.annotations.RuleTestData;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RuleTestData("market-manipulation")
public class MarketManipulationRuleDataFactory {

    // Clients
    private static final ClientHelper marketManipulationTest1Client = getRandomVantageClientAllFields();
    private static final ClientHelper marketManipulationTest2Client = getRandomVantageClientAllFields();

    private static DataHelper getMarketManipulatorRuleData(ClientHelper client) {
        DataHelper data = new DataHelper();
        client.setServerId(4);
        data.clientHelper = client;
        data.crmTbUserObject = generateUserByClient(client);
        data.crmTbAccountObject = generateAccountByClient(client, false);
        data.crmTbAccountForMtObject = generateAccountForMtByClient(client, false);
        data.dictIsTestObject = generateDictIsTestByClientFalse(data.clientHelper);
        data.mt5DealsCoercedObjects = new java.util.ArrayList<>(List.of(generateMt5DealsCoercedObject(client)));
        TradeEventMetadata metadata = new TradeEventMetadata("MT5");
        data.closeTradeMtEvent = new CloseTradeMtEvent(
                getRandomUuidString(),
                Instant.now().toString(),
                data.mt5DealsCoercedObjects.getFirst().getPositionId(),
                client.getTradingAccount(),
                data.mt5DealsCoercedObjects.getFirst().getVolumeLots(),
                data.mt5DealsCoercedObjects.getFirst().getSymbol(),
                data.clientHelper.getServerId(),
                MT_CLOSE_TRADE_EVENT,
                Instant.now().toString(),
                metadata,
                Instant.now().toString());
        return data;
    }

    private static DataHelper getMarketManipulationRuleTest1Data() {
        DataHelper data = getMarketManipulatorRuleData(marketManipulationTest1Client);
        data.dictIsTestObject = generateDictIsTestByClientTrue(data.clientHelper);
        return data;
    }

    private static DataHelper getMarketManipulationRuleTest2Data() {
        DataHelper data = getMarketManipulatorRuleData(marketManipulationTest2Client);
        data.mt5DealsCoercedObjects.getFirst().setProfit(3400d);
        data.mt5DealsCoercedObjects.getFirst().setProfitUsd(3400d);
        data.mtAccountObject = generateMtAccountByClient(data.clientHelper);
        data.mtAccountObject.equity = 7600d;
        data.mtAccountObject.equityUsd = 7600d;
        // add toxicity data

        // add toxicity data
        data.ucidGeneralScore = generateUcidGeneralScoreObject(data.clientHelper, 0.71, 0.71);
        return data;
    }

    public static Map<String, DataHelper> setupMarketManipulationRuleData() {
        Map<String, DataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("1", getMarketManipulationRuleTest1Data());
        map.put("2", getMarketManipulationRuleTest2Data());
        setupData(map);
        return map;
    }
}
