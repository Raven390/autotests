package helpers.data.rules.trading;

import static business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient;
import static business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedFactory.generateTradeByClient;
import static business_objects.db.clickhouse.mt_tb_credits.MtTbCreditsObjectFactory.generateCreditsByClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.DataHelper.createClient;
import static helpers.database.DbHelper.startSshTunnel;
import static utils.Utils.getCurrentTimestampDbFormatMinusDays;

import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntity;
import business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedObject;
import business_objects.kafka.MirrorScoreEvent;
import generator.annotations.RuleTestData;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import utils.Utils;

@RuleTestData("mirror-trading")
public class MlMirrorTradeRuleDataFactory {

    private static final ClientHelper mirrorTradeMLModelClient1 = getRandomVantageClientAllFields();
    private static final ClientHelper mirrorTradeMLModelClient2 = getRandomVantageClientAllFields();
    private static final ClientHelper mirrorTradeMLModelClient3 = getRandomVantageClientAllFields();
    private static final ClientHelper mirrorTradeMLModelClient4 = getRandomVantageClientAllFields();

    @Step("Create data for Mirror trading rule")
    private static DataHelper getMirrorTradingRuleData(ClientHelper client) {
        DataHelper data = new DataHelper();

        createClient(data, client);

        data.mirrorScoreEvent = new MirrorScoreEvent();
        data.mirrorScoreEvent.setType("mirrorScore");
        data.mirrorScoreEvent.setId(Utils.getRandomUuidString());
        data.mirrorScoreEvent.setSchemaVersion("2.0");
        data.mirrorScoreEvent.setTimestamp(Instant.now().getNano());
        data.mirrorScoreEvent.setActionTimeUtc("123");
        data.mirrorScoreEvent.setUcid(data.clientHelper.getUcid());
        data.mirrorScoreEvent.setCountAction(10);
        data.mirrorScoreEvent.setActionId("MT5-CLOSE-1734973200-01");
        data.mirrorScoreEvent.setUcidScore(1.1);
        data.mirrorScoreEvent.setAccount(client.getTradingAccount().longValue());
        data.mirrorScoreEvent.setServerId(Long.valueOf(client.getServerId()));
        return data;
    }

    @Description("ML Mirror trade rule. no credits")
    private static DataHelper getMirrorTradingMLModelTest1Data() {
        DataHelper data = getMirrorTradingRuleData(mirrorTradeMLModelClient1);
        data.addAlert("Mirror Trading", "CLOSED");
        return data;
    }

    @Description(
            "ML Mirror trade rule. ML Mirror trade rule.  user has at least 1 closed alert currentPnl - lastPnl < min(5000, 0.8 * depositsUcid)")
    private static DataHelper getMirrorTradingMLModelTest2Data() {
        DataHelper data = getMirrorTradingRuleData(mirrorTradeMLModelClient2);
        data.mtTbCreditsObjects = List.of(generateCreditsByClient(data.clientHelper));
        data.addAlert("Mirror Trading", "CLOSED");
        data.boAlertsObjects.getFirst().setResolvedAt(getCurrentTimestampDbFormatMinusDays(2));
        CrmTbDepositEntity deposit = generateCrmTbDepositEntityByClient(data.clientHelper);
        deposit.setAmountUsd(BigDecimal.valueOf(500));
        data.crmTbDepositObjects = List.of(deposit);
        Mt5DealsCoercedObject deal1 = generateTradeByClient(data.clientHelper);
        deal1.setTime(getCurrentTimestampDbFormatMinusDays(1));
        deal1.setProfitUsd(100d);
        deal1.setSymbol("EURUSD");
        Mt5DealsCoercedObject deal2 = generateTradeByClient(data.clientHelper);
        deal2.setTime(getCurrentTimestampDbFormatMinusDays(1));
        deal2.setProfitUsd(100d);
        deal2.setSymbol("BLW");
        Mt5DealsCoercedObject deal3 = generateTradeByClient(data.clientHelper);
        deal3.setTime(getCurrentTimestampDbFormatMinusDays(3));
        deal3.setProfitUsd(500d);
        deal3.setSymbol("BLW");
        data.mt5DealsCoercedObjects = List.of(deal1, deal2, deal3);

        return data;
    }

    @Description(
            "ML Mirror trade rule. ML Mirror trade rule.  user has at least 1 closed alert currentPnl - lastPnl > min(5000, 0.8 * depositsUcid) marked hedger")
    private static DataHelper getMirrorTradingMLModelTest3Data() throws IOException {
        DataHelper data = getMirrorTradingRuleData(mirrorTradeMLModelClient3);
        data.mtTbCreditsObjects = List.of(generateCreditsByClient(data.clientHelper));
        data.addAlert("Mirror Trading", "CLOSED");
        data.boAlertsObjects.getFirst().setResolvedAt(getCurrentTimestampDbFormatMinusDays(2));
        CrmTbDepositEntity deposit = generateCrmTbDepositEntityByClient(data.clientHelper);
        deposit.setAmountUsd(BigDecimal.valueOf(500));
        data.crmTbDepositObjects = List.of(deposit);
        Mt5DealsCoercedObject deal1 = generateTradeByClient(data.clientHelper);
        deal1.setTime(getCurrentTimestampDbFormatMinusDays(1));
        deal1.setProfitUsd(500d);
        deal1.setSymbol("EURUSD");
        Mt5DealsCoercedObject deal2 = generateTradeByClient(data.clientHelper);
        deal2.setTime(getCurrentTimestampDbFormatMinusDays(1));
        deal2.setProfitUsd(500d);
        deal2.setSymbol("BLW");
        Mt5DealsCoercedObject deal3 = generateTradeByClient(data.clientHelper);
        deal3.setTime(getCurrentTimestampDbFormatMinusDays(3));
        deal3.setProfitUsd(500d);
        deal3.setSymbol("BLW");
        data.mt5DealsCoercedObjects = List.of(deal1, deal2, deal3);
        return data;
    }

    @Description(
            "ML Mirror trade rule. ML Mirror trade rule.  user has at least 1 closed alert currentPnl - lastPnl > min(5000, 0.8 * depositsUcid) not marked as hedger")
    private static DataHelper getMirrorTradingMLModelTest4Data() {
        DataHelper data = getMirrorTradingRuleData(mirrorTradeMLModelClient4);
        data.mtTbCreditsObjects = List.of(generateCreditsByClient(data.clientHelper));
        data.addAlert("Mirror Trading", "CLOSED");
        data.boAlertsObjects.getFirst().setResolvedAt(getCurrentTimestampDbFormatMinusDays(2));
        CrmTbDepositEntity deposit = generateCrmTbDepositEntityByClient(data.clientHelper);
        deposit.setAmountUsd(BigDecimal.valueOf(500));
        data.crmTbDepositObjects = List.of(deposit);
        Mt5DealsCoercedObject deal1 = generateTradeByClient(data.clientHelper);
        deal1.setTime(getCurrentTimestampDbFormatMinusDays(1));
        deal1.setProfitUsd(500d);
        deal1.setSymbol("EURUSD");
        Mt5DealsCoercedObject deal2 = generateTradeByClient(data.clientHelper);
        deal2.setTime(getCurrentTimestampDbFormatMinusDays(1));
        deal2.setProfitUsd(500d);
        deal2.setSymbol("BLW");
        Mt5DealsCoercedObject deal3 = generateTradeByClient(data.clientHelper);
        deal3.setTime(getCurrentTimestampDbFormatMinusDays(3));
        deal3.setProfitUsd(500d);
        deal3.setSymbol("BLW");
        data.mt5DealsCoercedObjects = List.of(deal1, deal2, deal3);
        return data;
    }

    public static Map<String, DataHelper> setupMlMirrorTradeRuleData() throws IOException {
        startSshTunnel();
        Map<String, DataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("1", getMirrorTradingMLModelTest1Data());
        map.put("2", getMirrorTradingMLModelTest2Data());
        map.put("3", getMirrorTradingMLModelTest3Data());
        map.put("4", getMirrorTradingMLModelTest4Data());
        return map;
    }
}
