package helpers.data.rules.trading.mirror_trading_close_trade;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateAccountByClient;
import static business_objects.db.clickhouse.crm_tb_account_for_mt.crm_tb_account.CrmTbAccountForMtObjectFactory.generateAccountForMtByClient;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.dict_is_test.DictIsTestObjectFactory.generateDictIsTestByClientFalse;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByClient;
import static business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedFactory.generateMt5DealsCoercedObject;
import static business_objects.db.clickhouse.mt_tb_credits.MtTbCreditsObjectFactory.generateCreditsByClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.DataHelper.addAlert;
import static helpers.data.rules.WaveFlagInserterV2.insertMirrorWaveV2Data;
import static helpers.database.DbHelper.startSshTunnel;
import static utils.Utils.getRandomUuidString;

import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntityFactory;
import business_objects.kafka.mt_events.CloseTradeMtEvent;
import business_objects.kafka.mt_events.TradeEventMetadata;
import generator.annotations.RuleTestData;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import helpers.data.enums.rule_engine.Event;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RuleTestData("mirror-trading")
public class MirrorTradingWavesDataFactory {

    private static final ClientHelper getMirrorTradingWavesTestClient1 = getRandomVantageClientAllFields();
    private static final ClientHelper getMirrorTradingWavesTestClient2 = getRandomVantageClientAllFields();
    private static final ClientHelper getMirrorTradingWavesTestClient3 = getRandomVantageClientAllFields();

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
                Event.MT_CLOSE_TRADE_EVENT.getName(),
                Instant.now().toString(),
                metadata,
                Instant.now().toString());
        return data;
    }

    @Description("Mirror trading. Waves. Exit without alerts if pattern not matched. ElementId: Event_end_9")
    public static DataHelper getMirrorTradingWavesTest1Data() {
        DataHelper data = getMirrorTradingRuleData(getMirrorTradingWavesTestClient1);
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
        return data;
    }

    @Description(
            "Mirror trading. Waves. Exit without alert if pattern matched and at least 1 resolved alerts. ElementId: Event_end_9")
    public static DataHelper getMirrorTradingWavesTest2Data() throws InterruptedException {
        DataHelper data = getMirrorTradingRuleData(getMirrorTradingWavesTestClient2);
        data.mtTbCreditsObjects = null;
        data.crmTbDepositObjects = null;
        // leverage
        data.mt5DealsCoercedObjects = null;
        data.mtAccountObject = generateMtAccountByClient(data.clientHelper);
        data.mtAccountObject.equityUsd = 3d;
        data.mtAccountObject.equity = 3d;
        addAlert(data, "Mirror Trading", "CLOSED");
        insertMirrorWaveV2Data(data.clientHelper);
        return data;
    }

    @Description(
            "Mirror trading. Waves. Exit with alert and MWR if pattern matched and at no resolved alerts. ElementId: Event_end_9")
    public static DataHelper getMirrorTradingWavesTest3Data() throws InterruptedException {
        DataHelper data = getMirrorTradingRuleData(getMirrorTradingWavesTestClient3);
        data.mtTbCreditsObjects = null;
        data.crmTbDepositObjects = null;
        // leverage
        data.mt5DealsCoercedObjects = null;
        data.mtAccountObject = generateMtAccountByClient(data.clientHelper);
        data.mtAccountObject.equityUsd = 3d;
        data.mtAccountObject.equity = 3d;
        insertMirrorWaveV2Data(data.clientHelper);
        return data;
    }

    public static Map<String, DataHelper> setupMirrorTradingWavesRuleData() throws InterruptedException {
        startSshTunnel();
        Map<String, DataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map

        map.put("1", getMirrorTradingWavesTest1Data());
        map.put("2", getMirrorTradingWavesTest2Data());
        map.put("3", getMirrorTradingWavesTest3Data());
        return map;
    }
}
