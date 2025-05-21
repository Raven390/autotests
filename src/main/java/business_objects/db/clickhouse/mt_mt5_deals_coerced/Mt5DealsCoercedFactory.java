package business_objects.db.clickhouse.mt_mt5_deals_coerced;

import business_objects.db.clickhouse.mt_mt5_positions.MtMt5PositionsObject;
import helpers.data.ClientHelper;
import helpers.data.enums.DateTimeFormat;
import helpers.data.enums.Symbol;
import io.qameta.allure.Step;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static utils.Constants.*;
import static utils.Utils.*;

public class Mt5DealsCoercedFactory {

    @Step("Generate mt5 deals by client")
    public static Mt5DealsCoercedObject generateTradeByClient(ClientHelper client) {
        return new Mt5DealsCoercedObject(client.getBrand(), client.getRegulator(), client.getUserId(), client.getUcid(), client.getTradingAccount(), PLATFORM_MT_5, client.getServerId(), "MT5", "accountType", "accountGroup", "USD", getRandomLongPositive(), getRandomLongPositive(), 0, 0, 1, 1d, getCurrentTimestampMinusOffsetFormatted(
                DateTimeFormat.DATE_AND_TIME, 0, 0, 1, 0, 0), getCurrentTimestampDbFormat(), EURUSD, EURUSD, "EUR", "USD", 1d, 1d, 1d, 1d, 1d, 1.25d, 1d, 1d, 1d, 1d, 1d, 1d, 1d, 1L, getRandomLongPositive(), COMMENT_AUTOMATION_TESTS, 1d, 2d, 1d, 1d, 1d, 1d, 0, getCurrentTimestampDbFormat(), COMMENT_AUTOMATION_TESTS);
    }

    @Step("Generate mt5 deals by client")
    public static Mt5DealsCoercedObject generateTradeByClient(ClientHelper client, Integer action, Integer entry,
            Integer days, Long order) {
        return new Mt5DealsCoercedObject(client.getBrand(), client.getRegulator(), client.getUserId(), client.getUcid(), client.getTradingAccount(), PLATFORM_MT_5, client.getServerId(), "MT5", "accountType", "accountGroup", "USD", getRandomLongPositive(), order, action, entry, 1, 1d, getCurrentTimestampMinusOffsetFormatted(
                DateTimeFormat.DATE_AND_TIME, 0, 0, days, 0, 0), getCurrentTimestampDbFormat(), EURUSD, EURUSD, "EUR", "USD", 1d, 1d, 1d, 1d, 1d, 1d, 1d, 1d, 1d, 1d, 1d, 1d, 1d, 1L, getRandomLongPositive(), COMMENT_AUTOMATION_TESTS, 1d, 2d, 1d, 1d, 1d, 1d, 0, getCurrentTimestampDbFormat(), COMMENT_AUTOMATION_TESTS);
    }

    @Step("Generate mt5 deals by MT5 position")
    public static Mt5DealsCoercedObject generateMt5DealsCoercedObject(MtMt5PositionsObject position) {
        Mt5DealsCoercedObject deal = new Mt5DealsCoercedObject();
        deal.setBrand(position.getBrand());
        deal.setRegulator(position.getRegulator());
        deal.setUserId(position.getUserId());
        deal.setUcid(position.getUcid());
        deal.setAccount(position.getAccount());
        deal.setPlatform(position.getPlatform());
        deal.setServerId(position.getServerId());
        deal.setServerName(position.getServerName());
        deal.setAccountType(position.getAccountType());
        deal.setAccountGroup(position.getAccountGroup());
        deal.setAccountCurrency(position.getAccountCurrency());
        deal.setDeal(position.getPositionId());
        deal.setOrder(position.getPositionId());
        deal.setAction(position.getAction());
        deal.setEntry(0);
        deal.setReason(position.getReason());
        deal.setContractSize(position.getContractSize());
        deal.setTime(position.getTimeCreate());
        deal.setTimeUtc(position.getTimeCreateUtc());
        deal.setSymbol(position.getSymbol());
        deal.setSymbolUnderlying(position.getSymbolUnderlying());
        deal.setBaseCurrency(position.getBaseCurrency());
        deal.setQuoteCurrency(position.getQuoteCurrency());
        deal.setRateUsdBase(position.getRateUsdBase());
        deal.setRateUsdQuote(position.getRateUsdQuote());
        deal.setRateUsdAcc(position.getRateUsdAcc());
        deal.setPrice(position.getPriceCurrent());
        deal.setVolume(position.getVolume());
        deal.setVolumeLots(position.getVolumeLots());
        deal.setNotionalValueUsd(position.getNotionalValueUsd());
        deal.setProfit(position.getProfit());
        deal.setStorage(position.getStorage());
        deal.setCommission(getRandomRoundedDouble(0.00, 5000));
        deal.setProfitUsd(position.getProfitUsd());
        deal.setStorageUsd(position.getStorageUsd());
        deal.setCommissionUsd(getRandomRoundedDouble(0.00, 5000));
        deal.setExpertId(position.getExpertId());
        deal.setPositionId(position.getPositionId());
        deal.setComment(position.getComment());
        deal.setSl(position.getSl());
        deal.setTp(position.getTp());
        deal.setPriceGateway(position.getPriceCurrent());
        deal.setMarketBid(getRandomRoundedDouble(0.00, 5000));
        deal.setMarketAsk(getRandomRoundedDouble(0.00, 5000));
        deal.setRateProfit((double) position.getRateProfit());
        deal.setIsDeleted(position.getIsDeleted());
        deal.setLastUpdated(position.getLastUpdated());
        deal.setInternalComment(position.getInternalComment());
        return deal;
    }

    @Step("Generate mt5 deals by client")
    public static Mt5DealsCoercedObject generateMt5DealsCoercedObject(ClientHelper client) {
        Mt5DealsCoercedObject deal = new Mt5DealsCoercedObject();
        deal.setBrand(client.getBrand());
        deal.setRegulator(client.getRegulator());
        deal.setUserId(client.getUserId());
        deal.setUcid(client.getUcid());
        deal.setAccount(client.getTradingAccount());
        deal.setPlatform("MT5");
        deal.setServerId(client.getServerId());
        deal.setServerName("testServerName");
        deal.setAccountType("testAccountType");
        deal.setAccountGroup("testAccountName");
        deal.setAccountCurrency("USD");
        deal.setDeal(424242L);
        deal.setOrder(424242L);
        deal.setAction(0);
        deal.setEntry(0);
        deal.setReason(0);
        deal.setContractSize(1.0);
        deal.setTime(getCurrentTimestampDbFormat());
        deal.setTimeUtc(getCurrentTimestampDbFormat());
        deal.setSymbol(Symbol.getRandomSymbol().getSymbolCode());
        deal.setSymbolUnderlying(Symbol.getRandomSymbol().getSymbolCode().toLowerCase(Locale.ROOT));
        deal.setBaseCurrency("USD");
        deal.setQuoteCurrency("USD");
        deal.setRateUsdBase(getRandomRoundedDouble(0.00, 5000));
        deal.setRateUsdQuote(getRandomRoundedDouble(0.00, 5000));
        deal.setRateUsdAcc(getRandomRoundedDouble(0.00, 5000));
        deal.setPrice(getRandomRoundedDouble(0.00, 5000));
        deal.setVolume(getRandomRoundedDouble(0.00, 5000));
        deal.setVolumeLots(getRandomRoundedDouble(0.00, 5000));
        deal.setNotionalValueUsd(getRandomRoundedDouble(0.00, 5000));
        deal.setProfit(getRandomRoundedDouble(0.00, 5000));
        deal.setStorage(getRandomRoundedDouble(0.00, 5000));
        deal.setCommission(getRandomRoundedDouble(0.00, 5000));
        deal.setProfitUsd(getRandomRoundedDouble(0.00, 5000));
        deal.setStorageUsd(getRandomRoundedDouble(0.00, 5000));
        deal.setCommissionUsd(getRandomRoundedDouble(0.00, 5000));
        deal.setExpertId(42424L);
        deal.setPositionId(424242L);
        deal.setComment("autotestComment");
        deal.setSl(getRandomRoundedDouble(0.00, 5000));
        deal.setTp(getRandomRoundedDouble(0.00, 5000));
        deal.setPriceGateway(getRandomRoundedDouble(0.00, 5000));
        deal.setMarketBid(getRandomRoundedDouble(0.00, 5000));
        deal.setMarketAsk(getRandomRoundedDouble(0.00, 5000));
        deal.setRateProfit(getRandomRoundedDouble(0.00, 5000));
        deal.setIsDeleted(0);
        deal.setLastUpdated(getCurrentTimestampDbFormat());
        deal.setInternalComment("autotestInternalComment");
        return deal;
    }

    public static List<Mt5DealsCoercedObject> generateMt5DealsCoercedObject(ClientHelper client, int number,
            String date) {
        List<Mt5DealsCoercedObject> deals = new ArrayList<>();
        for (int i = 1; i <= number; i++) {
            Mt5DealsCoercedObject trade = generateTradeByClient(client);
            trade.setTime(date);
            deals.add(trade);
        }
        return deals;
    }
}