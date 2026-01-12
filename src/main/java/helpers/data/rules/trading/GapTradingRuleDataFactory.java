package helpers.data.rules.trading;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountData;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.dict_is_test.DictIsTestObjectFactory.generateDictIsTestByClientFalse;
import static business_objects.db.clickhouse.dict_is_test.DictIsTestObjectFactory.generateDictIsTestByClientTrue;
import static business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedFactory.generateMt5DealsCoercedObject;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.DataSetupHelper.setupData;
import static helpers.database.DbHelper.startSshTunnel;
import static utils.Utils.*;

import business_objects.db.clickhouse.mt___symbol_session.MtSymbolSessionObject;
import business_objects.db.clickhouse.mt___symbol_session.MtSymbolSessionObjectFactory;
import business_objects.kafka.mt_events.TradeEvent;
import business_objects.kafka.mt_events.TradeEventMetadata;
import generator.annotations.RuleTestData;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import io.qameta.allure.Step;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RuleTestData("gap-trading")
public class GapTradingRuleDataFactory {

    private static final ClientHelper gapTradingRuleExitEventEnd1Client = getRandomVantageClientAllFields();
    private static final ClientHelper gapTradingRuleExitEventEnd2Client = getRandomVantageClientAllFields();
    private static final ClientHelper gapTradingRuleExitEventEnd3Client = getRandomVantageClientAllFields();
    private static final ClientHelper gapTradingRuleExitEventEnd4Client = getRandomVantageClientAllFields();

    private GapTradingRuleDataFactory() {}

    @Step("Create data for Gap Trading rule")
    private static DataHelper getGapTradingRuleData(ClientHelper client) {
        DataHelper ruleData = new DataHelper();
        ruleData.clientHelper = client;
        ruleData.crmTbUserObject = generateUserByClient(client);
        ruleData.crmTbAccountObject = generateCrmTbAccountData(client);
        ruleData.dictIsTestObject = generateDictIsTestByClientFalse(ruleData.clientHelper);

        ruleData.tradeEvent = new TradeEvent();
        ruleData.tradeEvent.type = "openTrade";
        ruleData.tradeEvent.openTime = convertTimestampToIsoFormatLocal();
        ruleData.tradeEvent.openTimeUtc = convertTimestampToIsoFormat(getCurrentTimestampMillis());
        ruleData.tradeEvent.tradeId = 123;
        ruleData.tradeEvent.volume = 1;
        ruleData.tradeEvent.symbol = "EURUSD";
        ruleData.tradeEvent.equity = 100d;
        ruleData.tradeEvent.margin = 10d;
        ruleData.tradeEvent.freeMargin = 10d;
        ruleData.tradeEvent.balance = 10d;
        ruleData.tradeEvent.leverage = 10d;
        ruleData.tradeEvent.eventDate = convertTimestampToIsoFormat(getCurrentTimestampMillis());
        ruleData.tradeEvent.id = getRandomUuidString();
        ruleData.tradeEvent.metadata = new TradeEventMetadata("MT5");
        ruleData.tradeEvent.serverId = client.getServerId();
        ruleData.tradeEvent.tradingAccount = client.getTradingAccount();

        LocalTime timePlus = LocalTime.now().plusMinutes(20);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        MtSymbolSessionObject symbolSession = MtSymbolSessionObjectFactory.generateMtSymbolSessionObjectByClient(
                ruleData.clientHelper, ruleData.tradeEvent.symbol, "00:00-" + timePlus.format(formatter));

        ruleData.setMtSymbolSessionObjects(List.of(symbolSession));

        return ruleData;
    }

    @Step("Exit from rule without alert if user is test or social trader user")
    private static DataHelper getGapTradingRuleExitEventEnd1Data() {
        DataHelper data = getGapTradingRuleData(gapTradingRuleExitEventEnd1Client);
        data.dictIsTestObject = generateDictIsTestByClientTrue(data.clientHelper);
        return data;
    }

    private static DataHelper getGapTradingRuleExitEventEnd2Data() {
        DataHelper data = getGapTradingRuleData(gapTradingRuleExitEventEnd2Client);
        data.tradeEvent.symbol = "GBPUSD";

        LocalTime timePlusFive = LocalTime.now().plusMinutes(50);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        MtSymbolSessionObject symbolSession = MtSymbolSessionObjectFactory.generateMtSymbolSessionObjectByClient(
                data.clientHelper, data.tradeEvent.symbol, "00:00-" + timePlusFive.format(formatter));

        data.setMtSymbolSessionObjects(List.of(symbolSession));

        return data;
    }

    private static DataHelper getGapTradingRuleExitEventEnd3Data() {
        return getGapTradingRuleData(gapTradingRuleExitEventEnd3Client);
    }

    private static DataHelper getGapTradingRuleExitEventEnd4Data() {
        DataHelper data = getGapTradingRuleData(gapTradingRuleExitEventEnd4Client);

        var trade = generateMt5DealsCoercedObject(data.clientHelper);
        trade.setTime(getCurrentTimestampMinusOffsetFormatted("yyyy-MM-dd_HH-mm-ss", 0, 0, 0, 0, 30));

        data.mt5DealsCoercedObjects = new ArrayList<>(List.of(generateMt5DealsCoercedObject(data.clientHelper)));

        return data;
    }

    public static Map<String, DataHelper> setupGapTradingRuleData() {
        startSshTunnel();
        Map<String, DataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("1", getGapTradingRuleExitEventEnd1Data());
        map.put("2", getGapTradingRuleExitEventEnd2Data());
        map.put("3", getGapTradingRuleExitEventEnd3Data());
        map.put("4", getGapTradingRuleExitEventEnd4Data());

        // Loop through the list with data and insert all the data into the according tables
        setupData(map);
        return map;
    }
}
