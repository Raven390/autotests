package helpers.data.rules.trading.mirror_trading_close_trade;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateAccountByClient;
import static business_objects.db.clickhouse.crm_tb_account_for_mt.crm_tb_account.CrmTbAccountForMtObjectFactory.generateAccountForMtByClient;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.dict_is_test.DictIsTestObjectFactory.generateDictIsTestByClientFalse;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByClient;
import static business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedFactory.generateMt5DealsCoercedObject;
import static business_objects.db.clickhouse.mt_tb_credits.MtTbCreditsObjectFactory.generateCreditsByClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.DataSetupHelper.setupData;
import static helpers.data.rules.MaxUsedLeverageInserter.insertMaxUsedLeverageData;
import static helpers.database.DbHelper.startSshTunnel;
import static utils.Constants.MT_CLOSE_TRADE_EVENT;
import static utils.Utils.getRandomUuidString;

import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntityFactory;
import business_objects.kafka.mt_events.CloseTradeMtEvent;
import business_objects.kafka.mt_events.TradeEventMetadata;
import generator.annotations.RuleTestData;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RuleTestData("mirror-trading")
public class MirrorTradingScotlandDataFactory {

    private static final ClientHelper getMirrorTradingScotlandClient1 = getRandomVantageClientAllFields();
    private static final ClientHelper getMirrorTradingScotlandClient2 = getRandomVantageClientAllFields();
    private static final ClientHelper getMirrorTradingScotlandClient3 = getRandomVantageClientAllFields();
    private static final ClientHelper getMirrorTradingScotlandClient4 = getRandomVantageClientAllFields();

    @Step("Create data for Mirror trading rule")
    private static DataHelper getMirrorTradingRuleData(ClientHelper client) {
        DataHelper data = new DataHelper();

        data.clientHelper = client;
        data.dictIsTestObject = generateDictIsTestByClientFalse(data.clientHelper);
        data.crmTbUserObject = generateUserByClient(client);
        data.crmTbAccountObject = generateAccountByClient(client, false);
        data.crmTbAccountForMtObject = generateAccountForMtByClient(client, false);
        data.mt5DealsCoercedObjects = List.of(generateMt5DealsCoercedObject(client));
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

    @Description("Mirror trading. Scotland. Exit without alert if trades count > 5. Event_end_8")
    private static DataHelper getMirrorTradingScotlandTest1Data() {
        DataHelper data = getMirrorTradingRuleData(getMirrorTradingScotlandClient1);
        data.mtTbCreditsObjects = List.of(generateCreditsByClient(data.clientHelper));
        data.mt5DealsCoercedObjects = generateMt5DealsCoercedObject(data.clientHelper, 5);
        return data;
    }

    @Description("Mirror trading. Scotland. Exit without alert if profit/(deposit+credit) < 0.6. Event_end_8")
    private static DataHelper getMirrorTradingScotlandTest2Data() {
        DataHelper data = getMirrorTradingRuleData(getMirrorTradingScotlandClient2);
        data.mtTbCreditsObjects = List.of(generateCreditsByClient(data.clientHelper));
        data.mtTbCreditsObjects.getFirst().amount = 1000d;
        data.mtTbCreditsObjects.getFirst().amountUsd = 1000d;
        data.crmTbDepositObjects =
                List.of(CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(data.clientHelper));
        data.crmTbDepositObjects.getFirst().setAmount(BigDecimal.ONE);
        data.crmTbDepositObjects.getFirst().setAmountUsd(BigDecimal.ONE);
        return data;
    }

    @Description("Mirror trading. Scotland. Exit without alert if Leverage < 200. Event_12inxex")
    private static DataHelper getMirrorTradingScotlandTest3Data() {
        DataHelper data = getMirrorTradingRuleData(getMirrorTradingScotlandClient3);
        data.mtTbCreditsObjects = List.of(generateCreditsByClient(data.clientHelper));
        data.mtTbCreditsObjects.getFirst().amount = 1d;
        data.mtTbCreditsObjects.getFirst().amountUsd = 1d;
        data.crmTbDepositObjects =
                List.of(CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(data.clientHelper));
        data.crmTbDepositObjects.getFirst().setAmount(BigDecimal.ONE);
        data.crmTbDepositObjects.getFirst().setAmountUsd(BigDecimal.ONE);
        // leverage
        data.mt5DealsCoercedObjects.getFirst().setNotionalValueUsd(1000d);
        data.mtAccountObject = generateMtAccountByClient(data.clientHelper);
        data.mtAccountObject.equityUsd = 1000d;
        return data;
    }

    @Description(
            "Mirror trading. Scotland. Exit with alert and restriction if Leverage > 200. ElementId: Event_1k86ppo")
    private static DataHelper getMirrorTradingScotlandTest4Data() {
        DataHelper data = getMirrorTradingRuleData(getMirrorTradingScotlandClient4);
        data.mtTbCreditsObjects = List.of(generateCreditsByClient(data.clientHelper));
        data.mtTbCreditsObjects.getFirst().amount = 1d;
        data.mtTbCreditsObjects.getFirst().amountUsd = 1d;
        data.crmTbDepositObjects =
                List.of(CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(data.clientHelper));
        data.crmTbDepositObjects.getFirst().setAmount(BigDecimal.ONE);
        data.crmTbDepositObjects.getFirst().setAmountUsd(BigDecimal.ONE);
        // leverage
        data.mt5DealsCoercedObjects.getFirst().setNotionalValueUsd(1000d);
        data.mtAccountObject = generateMtAccountByClient(data.clientHelper);
        data.mtAccountObject.equityUsd = 3d;
        data.mtAccountObject.equity = 3d;
        insertMaxUsedLeverageData(data.clientHelper);

        return data;
    }

    public static Map<String, DataHelper> setupMirrorTradingScotlandRuleData() throws InterruptedException {
        startSshTunnel();
        Map<String, DataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("1", getMirrorTradingScotlandTest1Data());
        map.put("2", getMirrorTradingScotlandTest2Data());
        map.put("3", getMirrorTradingScotlandTest3Data());
        map.put("4", getMirrorTradingScotlandTest4Data());

        setupData(map);

        return map;
    }
}
