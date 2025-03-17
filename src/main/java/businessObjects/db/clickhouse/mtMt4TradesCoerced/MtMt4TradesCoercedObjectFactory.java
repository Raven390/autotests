package businessObjects.db.clickhouse.mtMt4TradesCoerced;

import helpers.data.ClientHelper;
import helpers.data.enums.DateTimeFormat;
import helpers.data.enums.Reason;
import helpers.data.enums.Symbol;
import helpers.data.enums.TicketType;
import io.qameta.allure.Step;

import java.util.ArrayList;

import static utils.Utils.*;

public class MtMt4TradesCoercedObjectFactory {


    @Step("Generate mt___mt4_trades_coerced object by client object")
    public static MtMt4TradesCoercedObject generateMt4TradesCoerced(ClientHelper client) {
        return new MtMt4TradesCoercedObject(client.getBrand(), client.getRegulator(), client.getUserId().longValue(), client.getUcid(), client.getTradingAccount().longValue(), "MT4", client.getServerId().longValue(), "serverName", "accountType", "accountGroup", "USD", getRandomIntPositive().longValue(), 1L, "Buy", 1L, "Client", 1L, getCurrentTimestampDbFormat(), getCurrentTimestampDbFormat(), 1234.55, 33.44, 55.66, "EURUSD", "GBPJPY", "EUR", "GBP", 1.1, 1.1, 1.1, 1.1, 1.1, 1.1, 1L, 87.43, 76.54, 1.1, 1.1, 1.1, 1.1, 1.1, 78.99, 22.33, 11.22, getCurrentTimestampDbFormat(), getCurrentTimestampDbFormat(), 1234.66, 1L, "comment_autotest", 123.78, 32.56, 12.98, 0L, getCurrentTimestampDbFormat(), "internalComment");
    }

    @Step("Generate mt___mt4_trades_coerced object by client object")
    public static MtMt4TradesCoercedObject generateMt4TradesCoercedForConnectionSearch(ClientHelper client,
            double profit, String closeTime) {
        MtMt4TradesCoercedObject trade = generateMt4TradesCoerced(client);
        trade.storageUsd = 0d;
        trade.commissionUsd = 0d;
        trade.profitUsd = profit;
        trade.closeTimeUtc = closeTime;
        return trade;
    }


    @Step("Generate mt___mt4_trades_coerced object by client object")//todo add enums for types, currencies(to create pairs), and logic based on type to select limits for values
    public static MtMt4TradesCoercedObject generateMt4TradesCoercedRandomized(ClientHelper client) {
        String reason = (randomEnum(Reason.class).getDisplayName());
        String type = (randomEnum(TicketType.class).getDisplayName());
        MtMt4TradesCoercedObject trade = generateMt4TradesCoerced(client);
        trade.symbol = Symbol.getRandomSymbol().getSymbolCode();
        trade.reasonName = reason;
        trade.ticketType = type;
        trade.openPrice = getRandomRoundedDouble(0.00, 5000);
        trade.stopLoss = getRandomRoundedDouble(0.00, 5000);
        trade.takeProfit = getRandomRoundedDouble(0.00, 5000);
        trade.openRateUsdBase = getRandomRoundedDouble(0.00, 5000);
        trade.openRateUsdQuote = getRandomRoundedDouble(0.00, 5000);
        trade.openRateUsdAcc = getRandomRoundedDouble(0.00, 5000);
        trade.volumeLots = getRandomRoundedDouble(0.00, 5000);
        trade.notionalValueUsd = getRandomRoundedDouble(0.00, 50_000);
        trade.openNotionalValueUsd = getRandomRoundedDouble(0.00, 5000);
        trade.closeNotionalValueUsd = getRandomRoundedDouble(0.00, 5000);
        trade.profit = getRandomRoundedDouble(0.00, 5000);
        trade.storage = getRandomRoundedDouble(0.00, 5000);
        trade.commission = getRandomRoundedDouble(0.00, 5000);
        trade.profitUsd = getRandomRoundedDouble(-5000, 5000);
        trade.storageUsd = getRandomRoundedDouble(-5000, 5000);
        trade.commissionUsd = getRandomRoundedDouble(-5000, 5000);
        trade.closePrice = getRandomRoundedDouble(0.00, 5000);
        trade.spreadRevenueUsd = getRandomRoundedDouble(0.00, 5000);
        trade.taxesUsd = getRandomRoundedDouble(0.00, 5000);
        trade.feeUsd = getRandomRoundedDouble(0.00, 5000);
        trade.closeTime = getCurrentTimestampDbFormat();
        trade.closeTimeUtc = getCurrentTimestampDbFormat();
        trade.openTimeUtc = getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 0, 2, 12, 0);
        trade.openTimeUtc = getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 0, 2, 12, 0);
        return trade;
    }

    @Step("Generate mt___mt4_trades_coerced objects by client object")
    public static ArrayList<MtMt4TradesCoercedObject> generateBunchMt4TradesCoerced(ClientHelper client, int size) {
        if (size <= 0) {
            System.out.println("int size CANT BE NEGATIVE. CURRENT VALUE IS " + size);
            return null;
        }
        ArrayList<MtMt4TradesCoercedObject> tradesCoercedObjects = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            MtMt4TradesCoercedObject transaction = generateMt4TradesCoercedRandomized(client);
            tradesCoercedObjects.add(transaction);
        }
        return tradesCoercedObjects;
    }
}