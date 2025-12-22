package business_objects.db.clickhouse.mt_mt5_deals;

import static utils.Utils.*;

import helpers.data.ClientHelper;
import helpers.data.enums.Symbol;
import io.qameta.allure.Step;
import java.util.Locale;

public class Mt5DealsFactory {

    @Step("Generate mt5 deals by client")
    public static Mt5DealsObject generateMt5DealsObject(ClientHelper client, Integer action, Integer entry) {
        Mt5DealsObject deal = new Mt5DealsObject();
        deal.setBrand(client.getBrand());
        deal.setRegulator(client.getRegulator());
        deal.setUserId(client.getUserId().longValue());
        deal.setUcid(client.getUcid());
        deal.setAccount(client.getTradingAccount().longValue());
        deal.setPlatform("MT5");
        deal.setServerId(client.getServerId());
        deal.setServerName("testServerName");
        deal.setAccountType("testAccountType");
        deal.setAccountGroup("testAccountName");
        deal.setAccountCurrency("USD");
        deal.setDeal(getRandomIntPositive().longValue());
        deal.setDealer(getRandomIntPositive().longValue());
        deal.setOrder(getRandomIntPositive().longValue());
        deal.setAction(action);
        deal.setEntry(entry);
        deal.setReason(0);
        deal.setContractSize(1L);
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
        deal.setVolume(getRandomIntPositive().longValue());
        deal.setVolumeLots(getRandomRoundedDouble(0.00, 5000));
        deal.setNotionalValueUsd(getRandomRoundedDouble(0.00, 5000));
        deal.setProfit(getRandomRoundedDouble(0.00, 5000));
        deal.setStorage(getRandomRoundedDouble(0.00, 5000));
        deal.setCommission(getRandomRoundedDouble(0.00, 5000));
        deal.setProfitUsd(getRandomRoundedDouble(0.00, 5000));
        deal.setStorageUsd(getRandomRoundedDouble(0.00, 5000));
        deal.setCommissionUsd(getRandomRoundedDouble(0.00, 5000));
        deal.setExpertId(getRandomIntPositive().longValue());
        deal.setPositionId(getRandomIntPositive().longValue());
        deal.setComment("autotestComment");
        deal.setSl(getRandomRoundedDouble(0.00, 5000));
        deal.setTp(getRandomRoundedDouble(0.00, 5000));
        deal.setVolumeClosed(getRandomIntPositive().longValue());
        deal.setPriceGateway(getRandomRoundedDouble(0.00, 5000));
        deal.setMarketBid(getRandomRoundedDouble(0.00, 5000));
        deal.setMarketAsk(getRandomRoundedDouble(0.00, 5000));
        deal.setRateProfit(getRandomRoundedDouble(0.00, 5000));
        deal.setIsDeleted(0);
        deal.setLastUpdated(getCurrentTimestampDbFormat());
        deal.setInternalComment("autotestInternalComment");
        return deal;
    }
}
