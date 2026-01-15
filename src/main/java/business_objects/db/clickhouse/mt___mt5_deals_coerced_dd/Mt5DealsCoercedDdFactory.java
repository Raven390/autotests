package business_objects.db.clickhouse.mt___mt5_deals_coerced_dd;

import static utils.Utils.*;

import helpers.data.ClientHelper;
import io.qameta.allure.Step;

public class Mt5DealsCoercedDdFactory {

    private Mt5DealsCoercedDdFactory() {}

    @Step("Generate mt5 deals by client")
    public static Mt5DealsCoercedDd generateTradeByClient(ClientHelper client, Double leverage) {
        return generateTradeByClient(client, leverage, 5000d, 1000d, getCurrentTimestampDbFormat());
    }

    @Step("Generate mt5 deals by client")
    public static Mt5DealsCoercedDd generateTradeByClient(
            ClientHelper client, Double leverage, Double equityUsd, Double freeMarginUsd, String timeUtc) {
        return new Mt5DealsCoercedDd(
                client.getServerId(),
                client.getTradingAccount(),
                getRandomLongPositive(),
                0,
                timeUtc,
                freeMarginUsd,
                equityUsd,
                leverage);
    }
}
