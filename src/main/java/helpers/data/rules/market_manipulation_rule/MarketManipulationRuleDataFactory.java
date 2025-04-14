package helpers.data.rules.market_manipulation_rule;


import business_objects.db.clickhouse.aggr_credit_equity_rate.AggrCreditEquityRateObject;
import business_objects.db.clickhouse.aggr_floating_trades_group_by.AggrFloatingTradesGroupBy;
import business_objects.db.clickhouse.connection_table.ConnectionTableEntry;
import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.ln_session_parsed.LnSessionParsedObject;
import business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedObject;
import business_objects.db.clickhouse.mt_mt5_positions.MtMt5PositionsObject;
import business_objects.kafka.mt_events.CloseTradeMtEvent;
import generator.annotations.RuleTestData;
import helpers.data.ClientHelper;
import helpers.data.enums.DateTimeFormat;
import helpers.data.rules.RuleDataHelper;
import net.datafaker.Faker;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static business_objects.db.clickhouse.aggr_credit_equity_rate.AggrCreditEquityRateObjectFactory.generateCreditEquityRatioAccount;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountData;
import static business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositObjectFactory.generateDepositByClient;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.dict_account_to_ucid.DictAccountToUcidObjectFactory.generateDictByClient;
import static business_objects.db.clickhouse.ln_session_parsed.LnSessionParsedObjectFactory.generateLexisNexisDataByClient;
import static business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedFactory.generateTradeByClient;
import static business_objects.db.clickhouse.mt_mt5_positions.MtMt5PositionsObjectFactory.generatePositionByOrder;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.DateTimeFormat.DATE_AND_TIME;
import static helpers.data.rules.RuleDataHelper.deleteRuleData;
import static helpers.data.rules.RuleDataHelper.setupRuleData;
import static utils.Constants.*;
import static utils.Utils.*;
import static utils.Utils.getCurrentTimestampDbFormat;

@RuleTestData("market-manipulation")
public class MarketManipulationRuleDataFactory {

    // Clients
    private static final ClientHelper marketManipulationExit1Client = getRandomVantageClientAllFields();
    private static final ClientHelper marketManipulationExit2v1Client = getRandomVantageClientAllFields();
    private static final ClientHelper marketManipulationExit2v2Client = getRandomVantageClientAllFields();
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
        data.dictAccountToUcidObject = generateDictByClient(client);
        data.aggrCreditEquityRate = creditEquityRate;
        Long tradeId = getRandomLongPositive();
        Mt5DealsCoercedObject order = new Mt5DealsCoercedObject(client.getBrand(), client.getRegulator(), client.getUserId(), client.getUcid(), client.getTradingAccount(), "MT5", client.getServerId(), "MT5", "accountType", "accountGroup", "USD", tradeId, getRandomIntPositive().longValue(), 0, 0, 1, 1d, getCurrentTimestampDbFormat(), getCurrentTimestampDbFormat(), EURUSD, EURUSD, "EUR", "USD", 1d, 1d, 1d, 1d, 1d, 1d, 1d, 2502.0, 1d, 1d, 2502.0, 1d, 1d, 1L, tradeId.longValue(), COMMENT_AUTOMATION_TESTS, 1d, 2d, 1d, 1d, 1d, 1d, 0, getCurrentTimestampDbFormat(), COMMENT_AUTOMATION_TESTS);
        data.mtMt5PositionsObjects.add(generatePositionByOrder(order));
        data.mt5DealsCoercedObjects.add(order);
        data.closeTradeEvent = new CloseTradeMtEvent(
                getRandomUuidString(), Instant.now().toString(), order.getDeal().longValue(), client.getTradingAccount(), 100d, order.getSymbol(), client.getServerId(), "closeTrade"
        );
        System.out.println("Order id: " + order.getDeal());
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
                fromClient.getUcid(), toClient.getUcid(), CONNECTION_TYPE_SAME_IDENTITY, 1d, List.of(
                        new ConnectionTableEntry.ConnectionInfo(
                                CONNECTION_ATTRIBUTE_NAME_PAYOUT, CONNECTION_SEARCH_DATA_CARD_NUMBER, CONNECTION_SEARCH_DATA_CARD_NUMBER, CONNECTION_TYPE_RELATION_TYPE_EXACT
                        )), getCurrentTimestampDbFormat()
        );

        return new ConnectionAndConnectedUser(
                connectionTableEntry, generateUserByClient(toClient), toClient, generateCrmTbAccountData(toClient)
        );
    }

    private static RuleDataHelper getMarketManipulationRuleExit5Data() {
        RuleDataHelper data = getMarketManipulatorRuleData(marketManipulationExit5Client);
        data.mt5DealsCoercedObjects.get(0).setProfit(2499.0);
        data.mt5DealsCoercedObjects.get(0).setProfitUsd(2499.0);
        return data;
    }

    private static RuleDataHelper getMarketManipulationRuleExit1Data() {
        return getMarketManipulatorRuleData(marketManipulationExit1Client);
    }

    private static RuleDataHelper getMarketManipulationRuleExit2v1Data() {
        RuleDataHelper data = getMarketManipulatorRuleData(marketManipulationExit2v1Client);
        data.mt5DealsCoercedObjects.get(0).setProfit(2750D);
        data.mt5DealsCoercedObjects.get(0).setProfitUsd(2750D);

        ClientHelper client = marketManipulationExit2v1Client;
        data.mt5DealsCoercedObjects.getFirst().setTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 60));
        data.mt5DealsCoercedObjects.getFirst().setTimeUtc(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 60));
        data.mt5DealsCoercedObjects.getFirst().setLastUpdated(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 60));
        data.aggrCreditEquityRate.currentEquity = 2501.0;
        Mt5DealsCoercedObject order = new Mt5DealsCoercedObject(client.getBrand(), client.getRegulator(), client.getUserId(), client.getUcid(), client.getTradingAccount(), "MT5", client.getServerId(), "MT5", "accountType", "accountGroup", "USD", getRandomLongPositive(), getRandomLongPositive(), 0, 0, 1, 1d, getCurrentTimestampDbFormatMinusDays(1), getCurrentTimestampDbFormat(), EURGBP, EURUSD, "EUR", "USD", 1d, 1d, 1d, 1d, 1d, 1d, 1d, -1.0, 1d, 1d, -1.0, 1d, 1d, 1L, getRandomLongPositive(), COMMENT_AUTOMATION_TESTS, 1d, 2d, 1d, 1d, 1d, 1d, 0, getCurrentTimestampDbFormat(), COMMENT_AUTOMATION_TESTS);
        data.mt5DealsCoercedObjects.add(order);
        return data;
    }

    private static RuleDataHelper getMarketManipulationRuleExit2v2Data() {
        RuleDataHelper data = getMarketManipulatorRuleData(marketManipulationExit2v2Client);
        data.aggrCreditEquityRate.currentEquity = 2501.0;
        ClientHelper client = marketManipulationExit2v1Client;
        data.mt5DealsCoercedObjects.getFirst().setTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 60));
        data.mt5DealsCoercedObjects.getFirst().setTimeUtc(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 60));
        data.mt5DealsCoercedObjects.getFirst().setLastUpdated(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 60));
        data.aggrCreditEquityRate.currentEquity = 2501.0;
        Mt5DealsCoercedObject order = new Mt5DealsCoercedObject(client.getBrand(), client.getRegulator(), client.getUserId(), client.getUcid(), client.getTradingAccount(), "MT5", client.getServerId(), "MT5", "accountType", "accountGroup", "USD", getRandomLongPositive(), getRandomLongPositive(), 0, 0, 1, 1d, getCurrentTimestampDbFormatMinusDays(1), getCurrentTimestampDbFormat(), EURGBP, EURUSD, "EUR", "USD", 1d, 1d, 1d, 1d, 1d, 1d, 1d, -1.0, 1d, 1d, -1.0, 1d, 1d, 1L, getRandomLongPositive(), COMMENT_AUTOMATION_TESTS, 1d, 2d, 1d, 1d, 1d, 1d, 0, getCurrentTimestampDbFormat(), COMMENT_AUTOMATION_TESTS);
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
        data.mt5DealsCoercedObjects.get(0).setProfit(3000.0);
        data.mt5DealsCoercedObjects.get(0).setProfitUsd(3000.0);

        // add toxicity data
        Mt5DealsCoercedObject consecutiveTrade1 = generateTradeByClient(data.clientHelper);
        Mt5DealsCoercedObject consecutiveTrade2 = generateTradeByClient(data.clientHelper);
        Mt5DealsCoercedObject consecutiveTrade3 = generateTradeByClient(data.clientHelper);
        Mt5DealsCoercedObject consecutiveTrade4 = generateTradeByClient(data.clientHelper);
        Mt5DealsCoercedObject consecutiveTrade5 = generateTradeByClient(data.clientHelper);

        consecutiveTrade1.setTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 0, 1));
        consecutiveTrade1.setTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 0, 1));
        consecutiveTrade1.setTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 0, 1));
        consecutiveTrade1.setTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 0, 1));
        consecutiveTrade1.setTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 0, 1));

        data.mt5DealsCoercedObjects.add(consecutiveTrade1);
        data.mt5DealsCoercedObjects.add(consecutiveTrade2);
        data.mt5DealsCoercedObjects.add(consecutiveTrade3);
        data.mt5DealsCoercedObjects.add(consecutiveTrade4);
        data.mt5DealsCoercedObjects.add(consecutiveTrade5);
        // add toxicity data

        data.floatingTrades.add(new AggrFloatingTradesGroupBy(data.clientHelper.getTradingAccount(), data.clientHelper.getServerId(), getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 0, 0, 20, 0), 0, 0, "USDEUR", 123.45, 2501.0, 123.45));
        Mt5DealsCoercedObject trade1Open;
        Mt5DealsCoercedObject trade1Close;
        trade1Open = generateTradeByClient(data.clientHelper);
        trade1Close = generateTradeByClient(data.clientHelper);
        trade1Open.setTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 2));
        trade1Open.setTimeUtc(trade1Open.getTime());
        trade1Open.setEntry(0);
        trade1Close.setPositionId(trade1Open.getPositionId());
        trade1Close.setComment("trade 1 close");
        trade1Close.setTime(getCurrentTimestampDbFormat());
        trade1Close.setTimeUtc(trade1Close.getTime());
        trade1Close.setEntry(1);
        trade1Close.setSymbol("USDEUR");
        trade1Close.setProfit(111.11);
        trade1Close.setProfitUsd(123.12);
        data.mt5DealsCoercedObjects.add(trade1Open);
        data.mt5DealsCoercedObjects.add(trade1Close);
        return data;
    }

    private static RuleDataHelper getMarketManipulationRuleExit4v1Data() {
        RuleDataHelper data = getMarketManipulatorRuleData(marketManipulationExit4v1Client);
        Mt5DealsCoercedObject dealWithProfit = generateTradeByClient(marketManipulationExit4v1Client);
        dealWithProfit.setProfit(2501.0);
        dealWithProfit.setProfitUsd(2501.0);
        dealWithProfit.setSymbol(EURUSD);
        data.mt5DealsCoercedObjects.add(dealWithProfit);
        data.aggrCreditEquityRate.currentEquity = 2501.0;
        data.mt5DealsCoercedObjects.get(0).setProfit(3000.0);
        data.mt5DealsCoercedObjects.get(0).setProfitUsd(3000.0);

        // add toxicity data
        Mt5DealsCoercedObject consecutiveTrade1 = generateTradeByClient(data.clientHelper);
        Mt5DealsCoercedObject consecutiveTrade2 = generateTradeByClient(data.clientHelper);
        Mt5DealsCoercedObject consecutiveTrade3 = generateTradeByClient(data.clientHelper);
        Mt5DealsCoercedObject consecutiveTrade4 = generateTradeByClient(data.clientHelper);
        Mt5DealsCoercedObject consecutiveTrade5 = generateTradeByClient(data.clientHelper);

        consecutiveTrade1.setTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 0, 1));
        consecutiveTrade1.setTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 0, 1));
        consecutiveTrade1.setTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 0, 1));
        consecutiveTrade1.setTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 0, 1));
        consecutiveTrade1.setTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 0, 1));

        consecutiveTrade1.setNotionalValueUsd(1_000_000d);
        consecutiveTrade2.setNotionalValueUsd(1_000_000d);

        MtMt5PositionsObject positionsObject1 = generatePositionByOrder(consecutiveTrade1);
        positionsObject1.setNotionalValueUsd(1_000_000d);

        data.mt5DealsCoercedObjects.add(consecutiveTrade1);
        data.mt5DealsCoercedObjects.add(consecutiveTrade2);
        data.mt5DealsCoercedObjects.add(consecutiveTrade3);
        data.mt5DealsCoercedObjects.add(consecutiveTrade4);
        data.mt5DealsCoercedObjects.add(consecutiveTrade5);
        data.mtMt5PositionsObjects.add(positionsObject1);
        // add toxicity data
        data.floatingTrades.add(new AggrFloatingTradesGroupBy(data.clientHelper.getTradingAccount(), data.clientHelper.getServerId(), getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 0, 0, 20, 0), 0, 0, "USDEUR", 123.45, 2499.0, 123.45));
        ConnectionAndConnectedUser connectionAndConnectedUser = getConnectionAndConnectedUser(data.clientHelper);
        connectionAndConnectedUser.connectionTableEntry.degreeConnection = CONNECTION_TYPE_SAME_PERSON;
        data.connectedUsers.add(connectionAndConnectedUser.crmTbUserObject);
        data.connections.add(connectionAndConnectedUser.connectionTableEntry);
        data.crmTbAccountObjectConnections.add(connectionAndConnectedUser.account);
        data.floatingTrades.add(new AggrFloatingTradesGroupBy(connectionAndConnectedUser.clientHelper.getTradingAccount(), connectionAndConnectedUser.clientHelper.getServerId(), getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 0, 0, 20, 0), 0, 0, "USDEUR", 123.45, 2501.0, 123.45));
        data.floatingTrades.add(new AggrFloatingTradesGroupBy(data.clientHelper.getTradingAccount(), data.clientHelper.getServerId(), getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 0, 0, 20, 0), 0, 0, "GBPJPY", 123.45, 2000.0, 123.45));
        Mt5DealsCoercedObject trade = data.mt5DealsCoercedObjects.getFirst();
        trade.setTimeUtc(trade.getTimeUtc().substring(0, 11) + "23" + trade.getTimeUtc().substring(13));
        data.mt5DealsCoercedObjects.add(trade);
        LnSessionParsedObject lexisNexisObject = generateLexisNexisDataByClient(data.clientHelper);
        lexisNexisObject.setBrand(data.clientHelper.getBrand());
        lexisNexisObject.setEventType("account_creation");
        lexisNexisObject.setEmail(data.clientHelper.getEmail());
        lexisNexisObject.setRiskRating("high");
        lexisNexisObject.setTrueIpGeo("HK");
        lexisNexisObject.setTrueIpIsp("1234");
        data.lnSessionParsedObjectRegistration = lexisNexisObject;
        Mt5DealsCoercedObject firstTrade = generateTradeByClient(data.clientHelper);
        firstTrade.setTimeUtc(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 10, 0, 0, 0));
        data.mt5DealsCoercedObjects.add(firstTrade);
        Mt5DealsCoercedObject tradeForDummyTrades = generateTradeByClient(data.clientHelper);
        tradeForDummyTrades.setVolumeLots(1000.0);
        data.mt5DealsCoercedObjects.add(tradeForDummyTrades);
        data.crmTbUserObject.poiCompleteTs = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 2, 0, 0, 0, 0);
        CrmTbDepositObject deposit = generateDepositByClient(data.clientHelper);
        deposit.amountUsd = 10_001.0;
        deposit.paymentChannel = "crypto";
        data.crmTbDepositObjects.add(deposit);
        data.closeTradeEvent.symbol = EURUSD;
        LnSessionParsedObject lexisNexisObjectLogin = generateLexisNexisDataByClient(data.clientHelper);
        lexisNexisObjectLogin.setEventType("login");
        lexisNexisObjectLogin.setTrueIp(new Faker().internet().ipV4Address());
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
        Mt5DealsCoercedObject dealWithProfit = generateTradeByClient(data.clientHelper);
        dealWithProfit.setProfit(2501.0);
        dealWithProfit.setProfitUsd(2501.0);
        dealWithProfit.setSymbol(EURUSD);
        data.mt5DealsCoercedObjects.add(dealWithProfit);
        data.aggrCreditEquityRate.currentEquity = 2501.0;
        data.mt5DealsCoercedObjects.getFirst().setProfit(3000.0);
        data.mt5DealsCoercedObjects.getFirst().setProfitUsd(3000.0);

        // add toxicity data
        Mt5DealsCoercedObject consecutiveTrade1 = generateTradeByClient(data.clientHelper);
        Mt5DealsCoercedObject consecutiveTrade2 = generateTradeByClient(data.clientHelper);
        Mt5DealsCoercedObject consecutiveTrade3 = generateTradeByClient(data.clientHelper);
        Mt5DealsCoercedObject consecutiveTrade4 = generateTradeByClient(data.clientHelper);
        Mt5DealsCoercedObject consecutiveTrade5 = generateTradeByClient(data.clientHelper);

        consecutiveTrade1.setTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 0, 1));
        consecutiveTrade1.setTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 0, 1));
        consecutiveTrade1.setTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 0, 1));
        consecutiveTrade1.setTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 0, 1));
        consecutiveTrade1.setTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 0, 1));

        consecutiveTrade1.setNotionalValueUsd(1_000_000d);
        consecutiveTrade2.setNotionalValueUsd(1_000_000d);

        MtMt5PositionsObject positionsObject1 = generatePositionByOrder(consecutiveTrade1);
        positionsObject1.setNotionalValueUsd(1_000_000d);

        data.mt5DealsCoercedObjects.add(consecutiveTrade1);
        data.mt5DealsCoercedObjects.add(consecutiveTrade2);
        data.mt5DealsCoercedObjects.add(consecutiveTrade3);
        data.mt5DealsCoercedObjects.add(consecutiveTrade4);
        data.mt5DealsCoercedObjects.add(consecutiveTrade5);
        data.mtMt5PositionsObjects.add(positionsObject1);
        // add toxicity data
        data.floatingTrades.add(new AggrFloatingTradesGroupBy(data.clientHelper.getTradingAccount(), data.clientHelper.getServerId(), getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 0, 0, 20, 0), 0, 0, "USDEUR", 123.45, 2499.0, 123.45));

        ConnectionAndConnectedUser connectionAndConnectedUser = getConnectionAndConnectedUser(data.clientHelper);
        connectionAndConnectedUser.connectionTableEntry.degreeConnection = CONNECTION_TYPE_NOT_SAME_PERSON;
        data.connectedUsers.add(connectionAndConnectedUser.crmTbUserObject);
        data.connections.add(connectionAndConnectedUser.connectionTableEntry);
        data.crmTbAccountObjectConnections.add(connectionAndConnectedUser.account);

        data.floatingTrades.add(new AggrFloatingTradesGroupBy(connectionAndConnectedUser.clientHelper.getTradingAccount(), connectionAndConnectedUser.clientHelper.getServerId(), getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 0, 0, 20, 0), 0, 0, "USDEUR", 123.45, 2501.0, 123.45));
        data.floatingTrades.add(new AggrFloatingTradesGroupBy(data.clientHelper.getTradingAccount(), data.clientHelper.getServerId(), getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 0, 0, 20, 0), 0, 0, "GBPJPY", 123.45, 2000.0, 123.45));
        Mt5DealsCoercedObject trade = data.mt5DealsCoercedObjects.getFirst();
        trade.setTimeUtc(trade.getTimeUtc().substring(0, 11) + "23" + trade.getTimeUtc().substring(13));
        data.mt5DealsCoercedObjects.add(trade);

        LnSessionParsedObject lexisNexisObject = generateLexisNexisDataByClient(data.clientHelper);
        lexisNexisObject.setBrand(data.clientHelper.getBrand());
        lexisNexisObject.setEventType("account_creation");
        lexisNexisObject.setEmail(data.clientHelper.getEmail());
        lexisNexisObject.setRiskRating("low");
        lexisNexisObject.setTrueIpGeo("HK1");
        lexisNexisObject.setTrueIpIsp("1234");
        data.lnSessionParsedObjectRegistration = lexisNexisObject;

        Mt5DealsCoercedObject firstTrade = generateTradeByClient(data.clientHelper);
        firstTrade.setTimeUtc(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 1, 0, 0, 0));
        data.mt5DealsCoercedObjects.add(firstTrade);

        data.crmTbUserObject.poiCompleteTs = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 2, 0, 0, 0, 0);

        CrmTbDepositObject deposit = generateDepositByClient(data.clientHelper);
        deposit.amountUsd = 9999.0;
        deposit.paymentChannel = "crypto";
        data.crmTbDepositObjects.add(deposit);

        data.closeTradeEvent.symbol = "EURUSD1";

        LnSessionParsedObject lexisNexisObjectLogin = generateLexisNexisDataByClient(data.clientHelper);
        lexisNexisObjectLogin.setEventType("login");
        lexisNexisObjectLogin.setTrueIp(new Faker().internet().ipV4Address());
        data.lnSessionParsedObjectLogin = lexisNexisObjectLogin;

        data.aggrCreditEquityRate.sumCreditOrder = 499.0;

        CrmTbDepositObject deposit2 = generateDepositByClient(data.clientHelper);
        deposit2.amountUsd = 10_000.0;
        deposit2.paymentChannel = "crypto";
        data.crmTbDepositObjects.add(deposit2);

        CrmTbDepositObject deposit3 = generateDepositByClient(data.clientHelper);
        deposit3.amountUsd = 10_001.0;
        deposit3.paymentChannel = "crypto";
        data.crmTbDepositObjects.add(deposit3);
        return data;
    }

    private static RuleDataHelper getMarketManipulationRuleExit4v3Data() {
        RuleDataHelper data = getMarketManipulatorRuleData(marketManipulationExit4v3Client);
        Mt5DealsCoercedObject dealWithProfit = generateTradeByClient(data.clientHelper);
        dealWithProfit.setProfit(2501.0);
        dealWithProfit.setProfitUsd(2501.0);
        dealWithProfit.setSymbol(EURUSD);
        data.mt5DealsCoercedObjects.add(dealWithProfit);
        data.aggrCreditEquityRate.currentEquity = 2501.0;
        data.mt5DealsCoercedObjects.getFirst().setProfit(3000.0);
        data.mt5DealsCoercedObjects.getFirst().setProfitUsd(3000.0);

        // add toxicity data
        Mt5DealsCoercedObject consecutiveTrade1 = generateTradeByClient(data.clientHelper);
        Mt5DealsCoercedObject consecutiveTrade2 = generateTradeByClient(data.clientHelper);
        Mt5DealsCoercedObject consecutiveTrade3 = generateTradeByClient(data.clientHelper);
        Mt5DealsCoercedObject consecutiveTrade4 = generateTradeByClient(data.clientHelper);
        Mt5DealsCoercedObject consecutiveTrade5 = generateTradeByClient(data.clientHelper);

        consecutiveTrade1.setTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 0, 1));
        consecutiveTrade1.setTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 0, 1));
        consecutiveTrade1.setTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 0, 1));
        consecutiveTrade1.setTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 0, 1));
        consecutiveTrade1.setTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 0, 1));

        consecutiveTrade1.setNotionalValueUsd(1_000_000d);
        consecutiveTrade2.setNotionalValueUsd(1_000_000d);

        MtMt5PositionsObject positionsObject1 = generatePositionByOrder(consecutiveTrade1);
        positionsObject1.setNotionalValueUsd(1_000_000d);

        data.mt5DealsCoercedObjects.add(consecutiveTrade1);
        data.mt5DealsCoercedObjects.add(consecutiveTrade2);
        data.mt5DealsCoercedObjects.add(consecutiveTrade3);
        data.mt5DealsCoercedObjects.add(consecutiveTrade4);
        data.mt5DealsCoercedObjects.add(consecutiveTrade5);
        data.mtMt5PositionsObjects.add(positionsObject1);
        // add toxicity data
        data.floatingTrades.add(new AggrFloatingTradesGroupBy(data.clientHelper.getTradingAccount(), data.clientHelper.getServerId(), getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 0, 0, 20, 0), 0, 0, "USDEUR", 123.45, 2499.0, 123.45));

        ConnectionAndConnectedUser connectionAndConnectedUser = getConnectionAndConnectedUser(data.clientHelper);
        connectionAndConnectedUser.connectionTableEntry.degreeConnection = "Not Same Person";
        data.connectedUsers.add(connectionAndConnectedUser.crmTbUserObject);
        data.connections.add(connectionAndConnectedUser.connectionTableEntry);
        data.crmTbAccountObjectConnections.add(connectionAndConnectedUser.account);

        data.floatingTrades.add(new AggrFloatingTradesGroupBy(connectionAndConnectedUser.clientHelper.getTradingAccount(), connectionAndConnectedUser.clientHelper.getServerId(), getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 0, 0, 20, 0), 0, 0, "USDEUR", 123.45, 2501.0, 123.45));
        data.floatingTrades.add(new AggrFloatingTradesGroupBy(data.clientHelper.getTradingAccount(), data.clientHelper.getServerId(), getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 0, 0, 20, 0), 0, 0, "GBPJPY", 123.45, 2000.0, 123.45));

        Mt5DealsCoercedObject trade = data.mt5DealsCoercedObjects.getFirst();
        trade.setTimeUtc(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 10, 0));
        data.mt5DealsCoercedObjects.add(trade);

        LnSessionParsedObject lexisNexisObject = generateLexisNexisDataByClient(data.clientHelper);
        lexisNexisObject.setBrand(data.clientHelper.getBrand());
        lexisNexisObject.setEventType("account_creation");
        lexisNexisObject.setEmail(data.clientHelper.getEmail());
        lexisNexisObject.setRiskRating("low");
        lexisNexisObject.setTrueIpGeo("HK1");
        lexisNexisObject.setTrueIpIsp("1234");
        data.lnSessionParsedObjectRegistration = lexisNexisObject;

        Mt5DealsCoercedObject firstTrade = generateTradeByClient(data.clientHelper);
        firstTrade.setTimeUtc(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 1, 0, 0, 0));
        data.mt5DealsCoercedObjects.add(firstTrade);

        data.crmTbUserObject.poiCompleteTs = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 1, 0, 0);

        CrmTbDepositObject deposit = generateDepositByClient(data.clientHelper);
        deposit.amountUsd = 999.0;
        deposit.paymentChannel = "crypto";
        data.crmTbDepositObjects.add(deposit);

        data.closeTradeEvent.symbol = "EURUSD1";

        LnSessionParsedObject lexisNexisObjectLogin = generateLexisNexisDataByClient(data.clientHelper);
        lexisNexisObjectLogin.setEventType("login");
        lexisNexisObjectLogin.setTrueIp(new Faker().internet().ipV4Address());
        data.lnSessionParsedObjectLogin = lexisNexisObjectLogin;

        data.aggrCreditEquityRate.sumCreditOrder = 499.0;

        CrmTbDepositObject deposit2 = generateDepositByClient(data.clientHelper);
        deposit2.amountUsd = 1000.0;
        deposit2.paymentChannel = "crypto1";
        data.crmTbDepositObjects.add(deposit2);

        CrmTbDepositObject deposit3 = generateDepositByClient(data.clientHelper);
        deposit3.amountUsd = 1000.0;
        deposit3.paymentChannel = "crypto1";
        data.crmTbDepositObjects.add(deposit3);
        return data;
    }

    public static Map<String, RuleDataHelper> setupMarketManipulationRuleData() {
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
