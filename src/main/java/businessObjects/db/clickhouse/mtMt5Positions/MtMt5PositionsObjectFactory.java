package businessObjects.db.clickhouse.mtMt5Positions;

import businessObjects.db.clickhouse.mtMt5DealsCoerced.Mt5DealsCoercedObject;
import io.qameta.allure.Step;


import static utils.Utils.*;

public class MtMt5PositionsObjectFactory {
    @Step("Generate mt5 position object")
    public static MtMt5PositionsObject generatePositionByOrder(Mt5DealsCoercedObject order) {
        return new MtMt5PositionsObject(
                order.brand, order.regulator, order.userId, order.ucid, order.account, order.platform, order.serverId, order.serverName, order.accountType, order.accountGroup, order.accountCurrency, order.deal.longValue(), order.action, order.reason, order.contractSize, order.time, order.timeUtc, order.time, order.timeUtc, order.symbol, order.symbolUnderlying, order.baseCurrency, order.quoteCurrency, order.rateUsdBase, order.rateUsdQuote, order.rateUsdAcc, 1d, 1d, order.volume, order.volumeLots, order.notionalValueUsd, order.profit, order.storage, order.profitUsd, order.storageUsd, order.comment, order.sl, order.tp, 1d, 1d, 1, 1, 1, "", order.isDeleted, order.lastUpdated, order.internalComment
        );
    }
}
