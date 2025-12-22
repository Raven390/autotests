package business_objects.db.clickhouse.mt_mt5_positions;

import static utils.Utils.*;

import business_objects.db.clickhouse.mt_mt4_trades.MtMt4TradesObject;
import business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedObject;
import helpers.data.ClientHelper;
import helpers.data.enums.Symbol;
import io.qameta.allure.Step;
import java.util.Locale;

public class MtMt5PositionsObjectFactory {
    @Step("Generate mt5 position object")
    public static MtMt5PositionsObject generatePositionByOrder(Mt5DealsCoercedObject order) {
        return new MtMt5PositionsObject(
                order.getBrand(),
                order.getRegulator(),
                order.getUserId(),
                order.getUcid(),
                order.getAccount(),
                order.getPlatform(),
                order.getServerId(),
                order.getServerName(),
                order.getAccountType(),
                order.getAccountGroup(),
                order.getAccountCurrency(),
                order.getDeal(),
                order.getAction(),
                order.getReason(),
                order.getContractSize(),
                order.getTime(),
                order.getTimeUtc(),
                order.getTime(),
                order.getTimeUtc(),
                order.getSymbol(),
                order.getSymbolUnderlying(),
                order.getBaseCurrency(),
                order.getQuoteCurrency(),
                order.getRateUsdBase(),
                order.getRateUsdQuote(),
                order.getRateUsdAcc(),
                1d,
                1d,
                order.getVolume(),
                order.getVolumeLots(),
                order.getNotionalValueUsd(),
                order.getProfit(),
                order.getStorage(),
                order.getProfitUsd(),
                order.getStorageUsd(),
                order.getComment(),
                order.getSl(),
                order.getTp(),
                1L,
                1L,
                1,
                1,
                1,
                "",
                order.getIsDeleted(),
                order.getLastUpdated(),
                order.getInternalComment());
    }

    @Step("Generate mt5 position object")
    public static MtMt5PositionsObject generateMtMt5PositionsObject(MtMt4TradesObject trade) {
        MtMt5PositionsObject position = new MtMt5PositionsObject();

        position.setBrand(trade.getBrand());
        position.setRegulator(trade.getRegulator());
        position.setUserId(trade.getUserId());
        position.setUcid(trade.getUcid());
        position.setAccount(trade.getAccount());
        position.setPlatform("MT5");
        position.setServerId(trade.getServerId());
        position.setServerName(trade.getServerName());
        position.setAccountType(trade.getAccountType());
        position.setAccountGroup(trade.getAccountGroup());
        position.setAccountCurrency(trade.getAccountCurrency());
        position.setPositionId(trade.getTicket());
        position.setAction(0);
        position.setReason(trade.getReason());
        position.setContractSize(1.0);
        position.setTimeCreate(trade.getOpenTime());
        position.setTimeCreateUtc(trade.getOpenTimeUtc());
        position.setTimeUpdate(trade.getLastUpdated());
        position.setTimeUpdateUtc(trade.getLastUpdated());
        position.setSymbol(trade.getSymbol());
        position.setSymbolUnderlying(trade.getSymbolUnderlying());
        position.setBaseCurrency(trade.getBaseCurrency());
        position.setQuoteCurrency(trade.getQuoteCurrency());
        position.setRateUsdBase(trade.getOpenRateUsdBase());
        position.setRateUsdQuote(trade.getCloseRateUsdQuote());
        position.setRateUsdAcc(trade.getOpenRateUsdAcc());
        position.setPriceOpen(trade.getOpenPrice());
        position.setPriceCurrent(trade.getClosePrice());
        position.setVolume(trade.getVolume());
        position.setVolumeLots(trade.getVolumeLots());
        position.setNotionalValueUsd(trade.getOpenNotionalValueUsd());
        position.setProfit(trade.getProfit());
        position.setStorage(trade.getStorage());
        position.setProfitUsd(trade.getProfitUsd());
        position.setStorageUsd(trade.getStorageUsd());
        position.setComment(trade.getComment());
        position.setSl(trade.getSl());
        position.setTp(trade.getTp());
        position.setExpertId(424_242);
        position.setExpertPositionId(424_242);
        position.setDealer(424_242);
        position.setRateProfit(0);
        position.setRateMargin(0);
        position.setOp("testOp");
        position.setIsDeleted(0);
        position.setLastUpdated(trade.getLastUpdated());
        position.setInternalComment(trade.getInternalComment());

        return position;
    }

    @Step("Generate mt5 position object")
    public static MtMt5PositionsObject generateMtMt5PositionsObject(ClientHelper client) {
        MtMt5PositionsObject position = new MtMt5PositionsObject();

        position.setBrand(client.getBrand());
        position.setRegulator(client.getRegulator());
        position.setUserId(client.getUserId());
        position.setUcid(client.getUcid());
        position.setAccount(client.getTradingAccount());
        position.setPlatform("MT5");
        position.setServerId(client.getServerId());
        position.setServerName("testServerName");
        position.setAccountType("testAccountType");
        position.setAccountGroup("testAccountGroup");
        position.setAccountCurrency("USD");
        position.setPositionId(getRandomIntPositive());
        position.setAction(0);
        position.setReason(0);
        position.setContractSize(1.0);
        position.setTimeCreate(getCurrentTimestampDbFormat());
        position.setTimeCreateUtc(getCurrentTimestampDbFormat());
        position.setTimeUpdate(getCurrentTimestampDbFormat());
        position.setTimeUpdateUtc(getCurrentTimestampDbFormat());
        position.setSymbol(Symbol.getRandomSymbol().getSymbolCode());
        position.setSymbolUnderlying(Symbol.getRandomSymbol().getSymbolCode().toLowerCase(Locale.ROOT));
        position.setBaseCurrency("USD");
        position.setQuoteCurrency("USD");
        position.setRateUsdBase(getRandomRoundedDouble(0.00, 5000));
        position.setRateUsdQuote(getRandomRoundedDouble(0.00, 5000));
        position.setRateUsdAcc(getRandomRoundedDouble(0.00, 5000));
        position.setPriceOpen(getRandomRoundedDouble(0.00, 5000));
        position.setPriceCurrent(getRandomRoundedDouble(0.00, 5000));
        position.setVolume(getRandomRoundedDouble(0.00, 5000));
        position.setVolumeLots(getRandomRoundedDouble(0.00, 5000));
        position.setNotionalValueUsd(getRandomRoundedDouble(0.00, 5000));
        position.setProfit(getRandomRoundedDouble(0.00, 5000));
        position.setStorage(getRandomRoundedDouble(0.00, 5000));
        position.setProfitUsd(getRandomRoundedDouble(0.00, 5000));
        position.setStorageUsd(getRandomRoundedDouble(0.00, 5000));
        position.setComment("testComment");
        position.setSl(getRandomRoundedDouble(0.00, 5000));
        position.setTp(getRandomRoundedDouble(0.00, 5000));
        position.setExpertId(client.getUserId());
        position.setExpertPositionId(client.getUserId());
        position.setDealer(client.getUserId());
        position.setRateProfit(0);
        position.setRateMargin(0);
        position.setOp("testOp");
        position.setIsDeleted(0);
        position.setLastUpdated(getCurrentTimestampDbFormat());
        position.setInternalComment("testInternalComment" + getCurrentTimestampDbFormat());

        return position;
    }
}
