package business_objects.db.clickhouse.mt___mt5_deals_coerced_dd;

import static utils.Constants.*;
import static utils.Utils.*;

import helpers.data.ClientHelper;
import helpers.data.enums.Symbol;
import io.qameta.allure.Step;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Mt5DealsCoercedDdFactoryV2 {

    @Step("Generate mt5 deals coerced dd by client")
    public static Mt5DealsCoercedDdObjectV2 generateMt5DealsCoercedDDObject(ClientHelper client) {
        Mt5DealsCoercedDdObjectV2 deal = new Mt5DealsCoercedDdObjectV2();
        deal.setBrand(client.getBrand());
        deal.setRegulator(client.getRegulator());
        deal.setUserId(client.getUserId());
        deal.setUcid(client.getUcid());
        deal.setAccount(Long.valueOf(client.getTradingAccount()));
        deal.setPlatform("MT5");
        deal.setServerId(client.getServerId());
        deal.setServerName("testServerName");
        deal.setAccountType("testAccountType");
        deal.setAccountGroup("testAccountName");
        deal.setAccountCurrency("USD");
        deal.setDeal(getRandomLongPositive());
        deal.setOrder(getRandomLongPositive());
        deal.setAction(0);
        deal.setEntry(0);
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
        deal.setVolume(getRandomLongPositive());
        deal.setVolumeLots(getRandomRoundedDouble(0.00, 5000));
        deal.setNotionalValueUsd(getRandomRoundedDouble(0.00, 5000));
        deal.setProfit(getRandomRoundedDouble(0.00, 5000));
        deal.setStorage(getRandomRoundedDouble(0.00, 5000));
        deal.setCommission(getRandomRoundedDouble(0.00, 5000));
        deal.setProfitUsd(getRandomRoundedDouble(0.00, 5000));
        deal.setStorageUsd(getRandomRoundedDouble(0.00, 5000));
        deal.setCommissionUsd(getRandomRoundedDouble(0.00, 5000));
        deal.setExpertId(getRandomLongPositive());
        deal.setPositionId(getRandomLongPositive());
        deal.setComment(COMMENT_AUTOMATION_TESTS);
        deal.setSl(getRandomRoundedDouble(0.00, 5000));
        deal.setTp(getRandomRoundedDouble(0.00, 5000));
        deal.setPriceGateway(getRandomRoundedDouble(0.00, 5000));
        deal.setMarketBid(getRandomRoundedDouble(0.00, 5000));
        deal.setMarketAsk(getRandomRoundedDouble(0.00, 5000));
        deal.setRateProfit(getRandomRoundedDouble(0.00, 5000));
        deal.setIsDeleted(0);
        deal.setLastUpdated(getCurrentTimestampDbFormat());
        deal.setInternalComment(COMMENT_AUTOMATION_TESTS);
        deal.setIsAbnormalTime(0);
        deal.setLeverage(100);
        deal.setBalance(getRandomRoundedDouble(0.00, 5000));
        deal.setEquity(getRandomRoundedDouble(0.00, 5000));
        deal.setMargin(getRandomRoundedDouble(0.00, 5000));
        deal.setFreeMargin(getRandomRoundedDouble(0.00, 5000));
        deal.setBalanceUsd(getRandomRoundedDouble(0.00, 5000));
        deal.setEquityUsd(getRandomRoundedDouble(0.00, 5000));
        deal.setMarginUsd(getRandomRoundedDouble(0.00, 5000));
        deal.setFreeMarginUsd(getRandomRoundedDouble(0.00, 5000));
        deal.setOpenPositionsNvUsd(getRandomRoundedDouble(0.00, 5000));
        return deal;
    }

    public static List<Mt5DealsCoercedDdObjectV2> generateMt5DealsCoercedDdObject(
            ClientHelper client, int numberOfDeals) {
        List<Mt5DealsCoercedDdObjectV2> deals = new ArrayList<>();
        for (int i = 1; i <= numberOfDeals; i++) {
            deals.add(generateMt5DealsCoercedDDObject(client));
        }
        return deals;
    }
}
