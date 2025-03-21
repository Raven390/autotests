package business_objects.db.clickhouse.mt_mt5_positions;

import business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedObject;
import io.qameta.allure.Step;

public class MtMt5PositionsObjectFactory {
    @Step("Generate mt5 position object")
    public static MtMt5PositionsObject generatePositionByOrder(Mt5DealsCoercedObject order) {
        return new MtMt5PositionsObject(
                order.getBrand(), order.getRegulator(), order.getUserId(), order.getUcid(), order.getAccount(), order.getPlatform(), order.getServerId(), order.getServerName(), order.getAccountType(), order.getAccountGroup(), order.getAccountCurrency(), order.getDeal(), order.getAction(), order.getReason(), order.getContractSize(), order.getTime(), order.getTimeUtc(), order.getTime(), order.getTimeUtc(), order.getSymbol(), order.getSymbolUnderlying(), order.getBaseCurrency(), order.getQuoteCurrency(), order.getRateUsdBase(), order.getRateUsdQuote(), order.getRateUsdAcc(), 1d, 1d, order.getVolume(), order.getVolumeLots(), order.getNotionalValueUsd(), order.getProfit(), order.getStorage(), order.getProfitUsd(), order.getStorageUsd(), order.getComment(), order.getSl(), order.getTp(), 1d, 1d, 1, 1, 1, "", order.getIsDeleted(), order.getLastUpdated(), order.getInternalComment()
        );
    }
}
