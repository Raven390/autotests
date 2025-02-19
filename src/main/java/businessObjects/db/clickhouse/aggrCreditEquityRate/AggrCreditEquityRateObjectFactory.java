package businessObjects.db.clickhouse.aggrCreditEquityRate;

import helpers.data.ClientHelper;
import io.qameta.allure.Step;

public class AggrCreditEquityRateObjectFactory {
    @Step("Generate data for given account")
    public static AggrCreditEquityRateObject generateCreditEquityRatioAccount(ClientHelper client) {
        return new AggrCreditEquityRateObject(client.getServerId().toString(), client.getTradingAccount().toString(), "2024-12-31 00:00:00", 1d, "2024-12-31 00:00:00", 2d, "2024-12-31 00:00:00", 3d);
    }
}
