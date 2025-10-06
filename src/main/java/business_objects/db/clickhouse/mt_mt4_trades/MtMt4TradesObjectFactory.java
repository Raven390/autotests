package business_objects.db.clickhouse.mt_mt4_trades;

import helpers.data.ClientHelper;
import helpers.data.enums.DateTimeFormat;
import helpers.data.enums.Symbol;
import io.qameta.allure.Step;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static utils.Utils.*;

public class MtMt4TradesObjectFactory {

    private MtMt4TradesObjectFactory() {
    }

    @Step("Generate mt___mt4_trades_coerced object by client object")//todo add enums for types, currencies(to create pairs), and logic based on type to select limits for values
    public static MtMt4TradesObject generateMt4TradesObject(ClientHelper client) {
        MtMt4TradesObject trade = new MtMt4TradesObject();
        trade.brand = client.getBrand();
        trade.regulator = client.getRegulator();
        trade.userId = Long.valueOf(client.getUserId());
        trade.ucid = client.getUcid();
        trade.account = Long.valueOf(client.getTradingAccount());
        trade.platform = "MT4";
        trade.serverId = Long.valueOf(client.getServerId());
        trade.serverName = "test server";
        trade.accountType = "autoTestType";
        trade.accountGroup = "autoTestGroup";
        trade.accountCurrency = "autoTestGroup";
        trade.ticket = getRandomLongPositive();
        trade.cmd = 1;
        trade.reason = 0L;
        trade.contractSize = 1L;
        trade.openTime = getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 0, 2, 12, 0);
        trade.openTimeUtc = getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 0, 2, 12, 0);
        trade.openPrice = getRandomRoundedDouble(0.00, 5000);
        trade.sl = getRandomRoundedDouble(0.00, 5000);
        trade.tp = getRandomRoundedDouble(0.00, 5000);
        trade.symbol = Symbol.getRandomSymbol().getSymbolCode();
        trade.symbolUnderlying = Symbol.getRandomSymbol().getSymbolCode().toLowerCase(Locale.ROOT);
        trade.baseCurrency = "USD";
        trade.quoteCurrency = "USD";
        trade.openRateUsdBase = getRandomRoundedDouble(0.00, 5000);
        trade.openRateUsdQuote = getRandomRoundedDouble(0.00, 5000);
        trade.openRateUsdAcc = getRandomRoundedDouble(0.00, 5000);
        trade.volume = getRandomIntPositive().longValue();
        trade.volumeLots = getRandomRoundedDouble(0.00, 5000);
        trade.openNotionalValueUsd = getRandomRoundedDouble(0.00, 5000);
        trade.closeNotionalValueUsd = getRandomRoundedDouble(0.00, 5000);
        trade.profit = getRandomRoundedDouble(0.00, 5000);
        trade.storage = getRandomRoundedDouble(0.00, 5000);
        trade.commission = getRandomRoundedDouble(0.00, 5000);
        trade.profitUsd = getRandomRoundedDouble(0.00, 5000);
        trade.storageUsd = getRandomRoundedDouble(0.00, 5000);
        trade.commissionUsd = getRandomRoundedDouble(0.00, 5000);
        trade.closeTime = "1970-01-01 00:00:00";
        trade.closeTimeUtc = "1970-01-01 00:00:00";
        trade.closePrice = getRandomRoundedDouble(0.00, 5000);
        trade.closeRateUsdBase = getRandomRoundedDouble(0.00, 5000);
        trade.closeRateUsdQuote = getRandomRoundedDouble(0.00, 5000);
        trade.closeRateUsdAcc = getRandomRoundedDouble(0.00, 5000);
        trade.convRate1 = getRandomRoundedDouble(0.00, 5000);
        trade.convRate2 = getRandomRoundedDouble(0.00, 5000);
        trade.comment = "autoTestComment";
        trade.isDeleted = 0;
        trade.lastUpdated = getCurrentTimestampDbFormat();
        trade.internalComment = "autoTestInternalComment" + getCurrentTimestampDbFormat();
        return trade;
    }

    @Step("Generate mt___mt4_trades_coerced objects by client object")
    public static List<MtMt4TradesObject> generateBunchMt4TradesCoerced(ClientHelper client, int size) {
        if (size <= 0) {

            throw new IllegalArgumentException("int size MUST BE GRATER THAN ZERO. CURRENT VALUE IS: " + size);
        }
        ArrayList<MtMt4TradesObject> tradesCoercedObjects = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            MtMt4TradesObject transaction = generateMt4TradesObject(client);
            tradesCoercedObjects.add(transaction);
        }
        return tradesCoercedObjects;
    }
}