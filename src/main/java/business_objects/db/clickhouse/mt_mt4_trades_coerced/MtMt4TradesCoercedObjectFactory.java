package business_objects.db.clickhouse.mt_mt4_trades_coerced;

import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static utils.Utils.*;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import helpers.data.ClientHelper;
import helpers.data.enums.DateTimeFormat;
import helpers.data.enums.Reason;
import helpers.data.enums.Symbol;
import helpers.data.enums.TicketType;
import io.qameta.allure.Step;
import java.util.ArrayList;
import java.util.List;

public class MtMt4TradesCoercedObjectFactory {

    @Step("Generate mt___mt4_trades_coerced object by client object")
    public static MtMt4TradesCoercedObject generateMt4TradesCoerced(ClientHelper client) {
        return new MtMt4TradesCoercedObject(
                client.getBrand(),
                client.getRegulator(),
                client.getUserId().longValue(),
                client.getUcid(),
                client.getTradingAccount().longValue(),
                "MT4",
                client.getServerId().longValue(),
                "serverName",
                "accountType",
                "accountGroup",
                "USD",
                getRandomIntPositive().longValue(),
                1L,
                "Buy",
                1L,
                "Client",
                1L,
                getCurrentTimestampDbFormat(),
                getCurrentTimestampDbFormat(),
                1234.55,
                33.44,
                55.66,
                "EURUSD",
                "GBPJPY",
                "EUR",
                "GBP",
                1.1,
                1.1,
                1.1,
                1.1,
                1.1,
                1.1,
                1L,
                87.43,
                76.54,
                1.1,
                1.1,
                1.1,
                1.1,
                1.1,
                78.99,
                22.33,
                11.22,
                getCurrentTimestampDbFormat(),
                getCurrentTimestampDbFormat(),
                1234.66,
                1L,
                "comment_autotest",
                123.78,
                32.56,
                12.98,
                0L,
                getCurrentTimestampDbFormat(),
                "internalComment");
    }

    @Step("Generate mt___mt4_trades_coerced object by client object")
    public static MtMt4TradesCoercedObject generateMt4TradesCoercedBalance(
            ClientHelper client, double profit, String comment) {
        MtMt4TradesCoercedObject mtMt4TradesCoercedObject = generateMt4TradesCoerced(client);
        mtMt4TradesCoercedObject.setProfit(profit);
        mtMt4TradesCoercedObject.setProfitUsd(profit);
        mtMt4TradesCoercedObject.setComment(comment);
        mtMt4TradesCoercedObject.setTicketType(TicketType.BALANCE.getDisplayName());
        mtMt4TradesCoercedObject.setCommission(0.0);
        mtMt4TradesCoercedObject.setStorage(0.0);
        return mtMt4TradesCoercedObject;
    }

    @Step("Generate mt___mt4_trades_coerced object by client object and profit")
    public static MtMt4TradesCoercedObject generateMt4TradesCoercedAccountProfitComment(
            CrmTbAccountObject account, Double profit, String comment) {
        MtMt4TradesCoercedObject trade = generateMt4TradesCoerced(getRandomVantageClientAllFields());
        trade.setBrand(account.brand);
        trade.setRegulator(account.regulator);
        trade.setUserId(account.userId.longValue());
        trade.setUcid(account.ucid);
        trade.setAccount(account.account.longValue());
        trade.setServerId(account.serverIdSt.longValue());
        trade.setTicketType("Balance");
        trade.setProfit(profit);
        trade.setStorage(0d);
        trade.setCommission(0d);
        trade.setComment(comment);
        return trade;
    }

    @Step("Generate mt___mt4_trades_coerced object by client object and profit")
    public static MtMt4TradesCoercedObject generateMt4TradesCoercedAccountProfitCommentBuy(
            CrmTbAccountObject account, Double profit, String comment) {
        MtMt4TradesCoercedObject trade = generateMt4TradesCoercedAccountProfitComment(account, profit, comment);
        trade.setTicketType("Buy");
        return trade;
    }

    @Step("Generate mt___mt4_trades_coerced object by client object")
    public static MtMt4TradesCoercedObject generateMt4TradesCoercedForConnectionSearch(
            ClientHelper client, double profitUsd, String closeTime) {
        MtMt4TradesCoercedObject trade = generateMt4TradesCoerced(client);
        trade.setStorageUsd(0d);
        trade.setCommissionUsd(0d);
        trade.setProfitUsd(profitUsd);
        trade.setCloseTime(closeTime);
        return trade;
    }

    @Step("Generate mt___mt4_trades_coerced object by client object") // todo add enums for types, currencies(to
    // create pairs), and logic based on type to
    // select limits for values
    public static MtMt4TradesCoercedObject generateMt4TradesCoercedRandomized(ClientHelper client) {
        String reason = (randomEnum(Reason.class).getDisplayName());
        String type = (randomEnum(TicketType.class).getDisplayName());
        MtMt4TradesCoercedObject trade = generateMt4TradesCoerced(client);
        trade.setSymbol(Symbol.getRandomSymbol().getSymbolCode());
        trade.setReasonName(reason);
        trade.setTicketType(type);
        trade.setOpenPrice(getRandomRoundedDouble(0.00, 5000));
        trade.setStopLoss(getRandomRoundedDouble(0.00, 5000));
        trade.setTakeProfit(getRandomRoundedDouble(0.00, 5000));
        trade.setOpenRateUsdBase(getRandomRoundedDouble(0.00, 5000));
        trade.setOpenRateUsdQuote(getRandomRoundedDouble(0.00, 5000));
        trade.setOpenRateUsdAcc(getRandomRoundedDouble(0.00, 5000));
        trade.setVolumeLots(getRandomRoundedDouble(0.00, 5000));
        trade.setNotionalValueUsd(getRandomRoundedDouble(0.00, 50_000));
        trade.setOpenNotionalValueUsd(getRandomRoundedDouble(0.00, 5000));
        trade.setCloseNotionalValueUsd(getRandomRoundedDouble(0.00, 5000));
        trade.setProfit(getRandomRoundedDouble(0.00, 5000));
        trade.setStorage(getRandomRoundedDouble(0.00, 5000));
        trade.setCommission(getRandomRoundedDouble(0.00, 5000));
        trade.setProfitUsd(getRandomRoundedDouble(-5000, 5000));
        trade.setStorageUsd(getRandomRoundedDouble(-5000, 5000));
        trade.setCommissionUsd(getRandomRoundedDouble(-5000, 5000));
        trade.setClosePrice(getRandomRoundedDouble(0.00, 5000));
        trade.setSpreadRevenueUsd(getRandomRoundedDouble(0.00, 5000));
        trade.setTaxesUsd(getRandomRoundedDouble(0.00, 5000));
        trade.setFeeUsd(getRandomRoundedDouble(0.00, 5000));
        trade.setCloseTime(getCurrentTimestampDbFormat());
        trade.setCloseTimeUtc(getCurrentTimestampDbFormat());
        trade.setOpenTimeUtc(getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 0, 2, 12, 0));
        trade.setOpenTimeUtc(getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 0, 2, 12, 0));
        return trade;
    }

    @Step("Generate mt___mt4_trades_coerced objects by client object")
    public static List<MtMt4TradesCoercedObject> generateBunchMt4TradesCoerced(ClientHelper client, int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("size CANT BE NEGATIVE. CURRENT VALUE IS " + size);
        }
        ArrayList<MtMt4TradesCoercedObject> tradesCoercedObjects = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            MtMt4TradesCoercedObject transaction = generateMt4TradesCoercedRandomized(client);
            tradesCoercedObjects.add(transaction);
        }
        return tradesCoercedObjects;
    }
}
