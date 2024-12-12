package businessObjects.db.clickhouse.aggrCreditRiskFreeRevenueRatio;

import helpers.data.ClientHelper;
import io.qameta.allure.Step;

public class AggrCreditRiskFreeRevenueRatioObjectFactory {
    @Step("Generate credit risk free revenue ratio for given account")
    public static AggrCreditRiskFreeRevenueRatioObject generateAggrCreditRiskFreeRevenueRatioObject(
            ClientHelper client) {
        return new AggrCreditRiskFreeRevenueRatioObject(client.getServerId().toString(), client.getTradingAccount().toString(), "2024-12-31 00:00:00", 1d, "2024-12-31 00:00:00", 2d, "2024-12-31 00:00:00", 3d);
    }
}
