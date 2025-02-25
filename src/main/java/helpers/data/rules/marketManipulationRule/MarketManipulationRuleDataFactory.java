package helpers.data.rules.marketManipulationRule;


import businessObjects.db.clickhouse.aggrCreditEquityRate.AggrCreditEquityRateObject;
import businessObjects.db.clickhouse.aggrFloatingTradesGroupBy.AggrFloatingTradesGroupBy;
import businessObjects.db.clickhouse.connectionTable.ConnectionTableEntry;
import businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObject;
import businessObjects.db.clickhouse.crmTbDepositTable.CrmTbDepositObject;
import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;
import businessObjects.db.clickhouse.lnSessionParsed.LnSessionParsedObject;
import businessObjects.db.clickhouse.mtMt5DealsCoerced.Mt5DealsCoercedObject;
import businessObjects.kafka.mtEvents.CloseTradeMtEvent;
import generator.annotations.RuleTestData;
import helpers.data.ClientHelper;
import helpers.data.enums.DateTimeFormat;
import helpers.data.rules.RuleDataHelper;
import net.datafaker.Faker;

import java.sql.SQLException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static businessObjects.db.clickhouse.aggrCreditEquityRate.AggrCreditEquityRateObjectFactory.generateCreditEquityRatioAccount;
import static businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObjectFactory.generateCrmTbAccountData;
import static businessObjects.db.clickhouse.crmTbDepositTable.CrmTbDepositObjectFactory.generateDepositByClient;
import static businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObjectFactory.generateUserByClient;
import static businessObjects.db.clickhouse.lnSessionParsed.LnSessionParsedObjectFactory.generateLexisNexisDataByClient;
import static businessObjects.db.clickhouse.mtMt5DealsCoerced.Mt5DealsCoercedFactory.generateTradeByClient;
import static businessObjects.db.clickhouse.mtMt5Positions.MtMt5PositionsObjectFactory.generatePositionByOrder;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.DateTimeFormat.DATE_AND_TIME;
import static helpers.data.rules.RuleDataHelper.deleteRuleData;
import static helpers.data.rules.RuleDataHelper.setupRuleData;
import static utils.Utils.*;

@RuleTestData("market-manipulation")
public class MarketManipulationRuleDataFactory {

    // Clients
    private static final ClientHelper marketManipulationExit1Client = getRandomVantageClientAllFields();
    private static final ClientHelper marketManipulationExit2v1Client = getRandomVantageClientAllFields();
    private static final ClientHelper marketManipulationExit2v2Client = getRandomVantageClientAllFields();
    private static final ClientHelper marketManipulationExit2v3Client = getRandomVantageClientAllFields();
    private static final ClientHelper marketManipulationExit3Client = getRandomVantageClientAllFields();
    private static final ClientHelper marketManipulationExit4v1Client = getRandomVantageClientAllFields();
    private static final ClientHelper marketManipulationExit4v2Client = getRandomVantageClientAllFields();
    private static final ClientHelper marketManipulationExit4v3Client = getRandomVantageClientAllFields();
    private static final ClientHelper marketManipulationExit5Client = getRandomVantageClientAllFields();

    private static RuleDataHelper getMarketManipulatorRuleData(ClientHelper client) {
        AggrCreditEquityRateObject creditEquityRate = generateCreditEquityRatioAccount(client);
        creditEquityRate.currentEquity = 2499.0;
        RuleDataHelper data = new RuleDataHelper();
        data.clientHelper = client;
        data.crmTbUserObject = generateUserByClient(client);
        data.crmTbAccountObject = generateCrmTbAccountData(client);
        data.aggrCreditEquityRate = creditEquityRate;
        Integer tradeId = getRandomIntPositive();
        Mt5DealsCoercedObject order = new Mt5DealsCoercedObject(client.getBrand(), client.getRegulator(), client.getUserId(), client.getUcid(), client.getTradingAccount(), "MT5", client.getServerId(), "MT5", "accountType", "accountGroup", "USD", tradeId, getRandomIntPositive().longValue(), 0, 0, 1, 1d, getCurrentTimestampDbFormat(), getCurrentTimestampDbFormat(), "EURUSD", "EURUSD", "EUR", "USD", 1d, 1d, 1d, 1d, 1d, 1d, 1d, 2502.0, 1, 1d, 2502.0, 1, 1d, 1L, tradeId.longValue(), "comment", 1d, 2d, 1d, 1d, 1d, 1d, 0, getCurrentTimestampDbFormat(), "Automation tests");
        data.mtMt5PositionsObjects.add(generatePositionByOrder(order));
        data.mt5DealsCoercedObjects.add(order);
        data.closeTradeEvent = new CloseTradeMtEvent(
                getRandomUuidString(), Instant.now().toString(), order.deal.longValue(), client.getTradingAccount(), 100d, order.symbol, client.getServerId(), "closeTrade"
        );
        System.out.println("Order id: " + order.deal);
        System.out.println("Client ucid: " + client.getUcid());
        System.out.println("Client trading account: " + client.getTradingAccount());
        return data;
    }

    private static class ConnectionAndConnectedUser {
        public ConnectionTableEntry connectionTableEntry;
        public CrmTbUserObject crmTbUserObject;
        public ClientHelper clientHelper;
        public CrmTbAccountObject account;

        public ConnectionAndConnectedUser(ConnectionTableEntry connectionTableEntry, CrmTbUserObject crmTbUserObject,
                ClientHelper clientHelper, CrmTbAccountObject account) {
            this.connectionTableEntry = connectionTableEntry;
            this.crmTbUserObject = crmTbUserObject;
            this.clientHelper = clientHelper;
            this.account = account;
        }
    }

    private static ConnectionAndConnectedUser getConnectionAndConnectedUser(ClientHelper fromClient) {
        ClientHelper toClient = getRandomVantageClientAllFields();
        ConnectionTableEntry connectionTableEntry = new ConnectionTableEntry(
                fromClient.getUcid(), toClient.getUcid(), "Same Identity", 1d, "[{\"connectionAttributeName\": \"payout\", \"connectionAttributeValue\": \"535456**** **0344\", \"sourceAttributeValue\": \"535456**** **0344\", \"relationType\": \"exact\"}]"
        );
        return new ConnectionAndConnectedUser(connectionTableEntry, generateUserByClient(toClient), toClient, generateCrmTbAccountData(toClient));
    }

    private static RuleDataHelper getMarketManipulationRuleExit5Data() {
        RuleDataHelper data = getMarketManipulatorRuleData(marketManipulationExit5Client);
        data.mt5DealsCoercedObjects.get(0).profit = 2499.0;
        data.mt5DealsCoercedObjects.get(0).profitUsd = 2499.0;
        return data;
    }

    private static RuleDataHelper getMarketManipulationRuleExit1Data() {
        return getMarketManipulatorRuleData(marketManipulationExit1Client);
    }

    private static RuleDataHelper getMarketManipulationRuleExit2v1Data() {
        RuleDataHelper data = getMarketManipulatorRuleData(marketManipulationExit2v1Client);
        // TODO add low toxicity data
        ClientHelper client = marketManipulationExit2v1Client;
        data.mt5DealsCoercedObjects.getFirst().time = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 60);
        data.mt5DealsCoercedObjects.getFirst().timeUtc = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 60);
        data.mt5DealsCoercedObjects.getFirst().lastUpdated = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 60);
        data.aggrCreditEquityRate.currentEquity = 2501.0;
        Mt5DealsCoercedObject order = new Mt5DealsCoercedObject(client.getBrand(), client.getRegulator(), client.getUserId(), client.getUcid(), client.getTradingAccount(), "MT5", client.getServerId(), "MT5", "accountType", "accountGroup", "USD", getRandomIntPositive(), getRandomLongPositive(), 0, 0, 1, 1d, getCurrentTimestampDbFormatMinusDays(1), getCurrentTimestampDbFormat(), "EURGBP", "EURUSD", "EUR", "USD", 1d, 1d, 1d, 1d, 1d, 1d, 1d, -1.0, 1, 1d, -1.0, 1, 1d, 1L, getRandomLongPositive(), "comment", 1d, 2d, 1d, 1d, 1d, 1d, 0, getCurrentTimestampDbFormat(), "Automation tests");
        data.mt5DealsCoercedObjects.add(order);
        return data;
    }

    private static RuleDataHelper getMarketManipulationRuleExit2v2Data() {
        RuleDataHelper data = getMarketManipulatorRuleData(marketManipulationExit2v2Client);
        // TODO add low toxicity data
        data.aggrCreditEquityRate.currentEquity = 2501.0;
        ClientHelper client = marketManipulationExit2v1Client;
        data.mt5DealsCoercedObjects.getFirst().time = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 60);
        data.mt5DealsCoercedObjects.getFirst().timeUtc = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 60);
        data.mt5DealsCoercedObjects.getFirst().lastUpdated = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 60);
        data.aggrCreditEquityRate.currentEquity = 2501.0;
        Mt5DealsCoercedObject order = new Mt5DealsCoercedObject(client.getBrand(), client.getRegulator(), client.getUserId(), client.getUcid(), client.getTradingAccount(), "MT5", client.getServerId(), "MT5", "accountType", "accountGroup", "USD", getRandomIntPositive(), getRandomLongPositive(), 0, 0, 1, 1d, getCurrentTimestampDbFormatMinusDays(1), getCurrentTimestampDbFormat(), "EURGBP", "EURUSD", "EUR", "USD", 1d, 1d, 1d, 1d, 1d, 1d, 1d, -1.0, 1, 1d, -1.0, 1, 1d, 1L, getRandomLongPositive(), "comment", 1d, 2d, 1d, 1d, 1d, 1d, 0, getCurrentTimestampDbFormat(), "Automation tests");
        data.mt5DealsCoercedObjects.add(order);

        data.floatingTrades.add(new AggrFloatingTradesGroupBy(data.clientHelper.getTradingAccount(), data.clientHelper.getServerId(), getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 0, 0, 20, 0), 0, 0, "USDEUR", 123.45, 2499.0, 123.45));
        ConnectionAndConnectedUser connectionAndConnectedUser = getConnectionAndConnectedUser(data.clientHelper);
        data.connectedUsers.add(connectionAndConnectedUser.crmTbUserObject);
        data.connections.add(connectionAndConnectedUser.connectionTableEntry);
        data.crmTbAccountObjectConnections.add(connectionAndConnectedUser.account);
        return data;
    }

    private static RuleDataHelper getMarketManipulationRuleExit3Data() {
        RuleDataHelper data = getMarketManipulatorRuleData(marketManipulationExit3Client);
        data.aggrCreditEquityRate.currentEquity = 2501.0;
        // TODO add low toxicity data
        data.floatingTrades.add(new AggrFloatingTradesGroupBy(data.clientHelper.getTradingAccount(), data.clientHelper.getServerId(), getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 0, 0, 20, 0), 0, 0, "USDEUR", 123.45, 2501.0, 123.45));
        Mt5DealsCoercedObject trade1Open;
        Mt5DealsCoercedObject trade1Close;
        trade1Open = generateTradeByClient(data.clientHelper);
        trade1Close = generateTradeByClient(data.clientHelper);
        trade1Open.time = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 2);
        trade1Open.timeUtc = trade1Open.time;
        trade1Open.entry = 0;
        trade1Close.positionId = trade1Open.positionId;
        trade1Close.comment = "trade 1 close";
        trade1Close.time = getCurrentTimestampDbFormat();
        trade1Close.timeUtc = trade1Close.time;
        trade1Close.entry = 1;
        trade1Close.symbol = "USDEUR";
        trade1Close.profit = 111.11;
        trade1Close.profitUsd = 123.12;
        data.mt5DealsCoercedObjects.add(trade1Open);
        data.mt5DealsCoercedObjects.add(trade1Close);
        return data;
    }

    private static RuleDataHelper getMarketManipulationRuleExit4v1Data() {
        RuleDataHelper data = getMarketManipulatorRuleData(marketManipulationExit4v1Client);
        Mt5DealsCoercedObject dealWithProfit = generateTradeByClient(data.clientHelper);
        dealWithProfit.profit = 2501.0;
        dealWithProfit.profitUsd = 2501.0;
        dealWithProfit.symbol = "EURUSD";
        data.mt5DealsCoercedObjects.add(dealWithProfit);
        data.aggrCreditEquityRate.currentEquity = 2501.0;
        // TODO add low toxicity data
        data.floatingTrades.add(new AggrFloatingTradesGroupBy(data.clientHelper.getTradingAccount(), data.clientHelper.getServerId(), getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 0, 0, 20, 0), 0, 0, "USDEUR", 123.45, 2499.0, 123.45));
        ConnectionAndConnectedUser connectionAndConnectedUser = getConnectionAndConnectedUser(data.clientHelper);
        connectionAndConnectedUser.connectionTableEntry.degreeConnection = "Same Person";
        data.connectedUsers.add(connectionAndConnectedUser.crmTbUserObject);
        data.connections.add(connectionAndConnectedUser.connectionTableEntry);
        data.crmTbAccountObjectConnections.add(connectionAndConnectedUser.account);
        data.floatingTrades.add(new AggrFloatingTradesGroupBy(connectionAndConnectedUser.clientHelper.getTradingAccount(), connectionAndConnectedUser.clientHelper.getServerId(), getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 0, 0, 20, 0), 0, 0, "USDEUR", 123.45, 2501.0, 123.45));
        data.floatingTrades.add(new AggrFloatingTradesGroupBy(data.clientHelper.getTradingAccount(), data.clientHelper.getServerId(), getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 0, 0, 20, 0), 0, 0, "GBPJPY", 123.45, 2000.0, 123.45));
        Mt5DealsCoercedObject trade = data.mt5DealsCoercedObjects.getFirst();
//        trade.deal = data.closeTradeEvent.tradeId;
        trade.timeUtc = trade.timeUtc.substring(0, 11) + "23" + trade.timeUtc.substring(13);
        data.mt5DealsCoercedObjects.add(trade);
        LnSessionParsedObject lexisNexisObject = generateLexisNexisDataByClient(data.clientHelper);
        lexisNexisObject.brand = data.clientHelper.getBrand();
        lexisNexisObject.eventType = "account_creation";
        lexisNexisObject.email = data.clientHelper.getEmail();
        lexisNexisObject.riskRating = "high";
        lexisNexisObject.trueIpGeo = "HK";
        lexisNexisObject.trueIpIsp = "1234";
        data.lnSessionParsedObjectRegistration = lexisNexisObject;
        Mt5DealsCoercedObject firstTrade = generateTradeByClient(data.clientHelper);
        firstTrade.timeUtc = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 10, 0, 0, 0);
        data.mt5DealsCoercedObjects.add(firstTrade);
        Mt5DealsCoercedObject tradeForDummyTrades = generateTradeByClient(data.clientHelper);
        tradeForDummyTrades.volumeLots = 1000.0;
        data.mt5DealsCoercedObjects.add(tradeForDummyTrades);
        data.crmTbUserObject.poiCompleteTs = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 2, 0, 0, 0, 0);
        CrmTbDepositObject deposit = generateDepositByClient(data.clientHelper);
        deposit.amountUsd = 10_001.0;
        deposit.paymentChannel = "crypto";
        data.crmTbDepositObjects.add(deposit);
        data.closeTradeEvent.symbol = "EURUSD";
        LnSessionParsedObject lexisNexisObjectLogin = generateLexisNexisDataByClient(data.clientHelper);
        lexisNexisObjectLogin.eventType = "login";
        lexisNexisObjectLogin.trueIp = new Faker().internet().ipV4Address();
        data.lnSessionParsedObjectLogin = lexisNexisObjectLogin;
        data.aggrCreditEquityRate.sumCreditOrder = 499.0;
        //TODO fix Illiquid period block
        //TODO fix tiny/huge deposit block
        CrmTbDepositObject deposit2 = generateDepositByClient(data.clientHelper);
        deposit2.amountUsd = 1.0;
        deposit2.paymentChannel = "crypto";
        data.crmTbDepositObjects.add(deposit2);

        CrmTbDepositObject deposit3 = generateDepositByClient(data.clientHelper);
        deposit3.amountUsd = 10_001.0;
        deposit3.paymentChannel = "crypto";
        data.crmTbDepositObjects.add(deposit3);
        //TODO fix number of different IP countries and diff devices used
        return data;
    }

    private static RuleDataHelper getMarketManipulationRuleExit4v2Data() {
        RuleDataHelper data = getMarketManipulatorRuleData(marketManipulationExit4v2Client);
        data.aggrCreditEquityRate.currentEquity = 2501.0;
        // TODO add low toxicity data
        data.floatingTrades.add(new AggrFloatingTradesGroupBy(data.clientHelper.getTradingAccount(), data.clientHelper.getServerId(), getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 0, 0, 20, 0), 0, 0, "USDEUR", 123.45, 2501.0, 123.45));
        Mt5DealsCoercedObject trade = generateTradeByClient(data.clientHelper);
        //trade.deal = data.closeTradeEvent.tradeId;
        trade.timeUtc = trade.timeUtc.substring(0, 11) + "23" + trade.timeUtc.substring(13);
        trade.symbol = "JPYUSD";
        data.mt5DealsCoercedObjects.add(trade);
        LnSessionParsedObject lexisNexisObject = generateLexisNexisDataByClient(data.clientHelper);
        lexisNexisObject.brand = data.clientHelper.getBrand();
        lexisNexisObject.eventType = "account_creation";
        lexisNexisObject.email = data.clientHelper.getEmail();
        lexisNexisObject.riskRating = "low";
        data.lnSessionParsedObjectRegistration = lexisNexisObject;
        CrmTbDepositObject deposit = generateDepositByClient(data.clientHelper);
        deposit.amountUsd = 9999.0;
        deposit.paymentChannel = "123";
        data.crmTbDepositObjects.add(deposit);
        ConnectionAndConnectedUser connectionAndConnectedUser = getConnectionAndConnectedUser(data.clientHelper);
        connectionAndConnectedUser.connectionTableEntry.degreeConnection = "Same Person";
        data.connectedUsers.add(connectionAndConnectedUser.crmTbUserObject);
        data.connections.add(connectionAndConnectedUser.connectionTableEntry);
        data.crmTbAccountObjectConnections.add(connectionAndConnectedUser.account);
        Mt5DealsCoercedObject firstTrade = generateTradeByClient(data.clientHelper);
        firstTrade.timeUtc = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 10, 0, 0, 0);
        data.mt5DealsCoercedObjects.add(firstTrade);
        return data;
    }

    private static RuleDataHelper getMarketManipulationRuleExit4v3Data() {
        RuleDataHelper data = getMarketManipulatorRuleData(marketManipulationExit4v3Client);
        data.aggrCreditEquityRate.currentEquity = 2501.0;
        // TODO add high toxicity data
        CrmTbDepositObject deposit = generateDepositByClient(data.clientHelper);
        deposit.amountUsd = 1.0;
        deposit.paymentChannel = "123";
        data.crmTbDepositObjects.add(deposit);
        data.mt5DealsCoercedObjects.getFirst().symbol = "123GHD";
        LnSessionParsedObject lexisNexisObject = generateLexisNexisDataByClient(data.clientHelper);
        lexisNexisObject.brand = data.clientHelper.getBrand();
        lexisNexisObject.eventType = "account_creation";
        lexisNexisObject.email = data.clientHelper.getEmail();
        lexisNexisObject.riskRating = "low";
        lexisNexisObject.trueIpIsp = "KZ";
        data.lnSessionParsedObjectRegistration = lexisNexisObject;
        data.closeTradeEvent.symbol = "JPYUSD+";
        return data;
    }

    public static Map<String, RuleDataHelper> setupMarketManipulationRuleData()
            throws ReflectiveOperationException, SQLException {
        Map<String, RuleDataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("1", getMarketManipulationRuleExit1Data());
        map.put("2v1", getMarketManipulationRuleExit2v1Data());
        map.put("2v2", getMarketManipulationRuleExit2v2Data());
        map.put("3", getMarketManipulationRuleExit3Data());
        map.put("4v1", getMarketManipulationRuleExit4v1Data());
        map.put("4v2", getMarketManipulationRuleExit4v2Data());
        map.put("4v3", getMarketManipulationRuleExit4v3Data());
        map.put("5", getMarketManipulationRuleExit5Data());
        setupRuleData(map);
        return map;
    }

    public static void deleteMarketManipulationRuleData(Map<String, RuleDataHelper> map) throws Exception {
        deleteRuleData(map);
    }
}
