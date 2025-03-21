package business_objects.db.clickhouse.aggr_credit_risk_free_revenue_ratio;

import helpers.data.ClientHelper;
import io.qameta.allure.Step;

import static utils.Constants.TIME_2024_12_31_00_00_00;

public class AggrCreditRiskFreeRevenueRatioObjectFactory {
    @Step("Generate credit risk free revenue ratio for given account")
    public static AggrCreditRiskFreeRevenueRatioObject generateAggrCreditRiskFreeRevenueRatioObject(
            ClientHelper client) {
        return new AggrCreditRiskFreeRevenueRatioObject(client.getServerId().toString(), client.getTradingAccount().toString(), TIME_2024_12_31_00_00_00, 1d, TIME_2024_12_31_00_00_00, 2d, TIME_2024_12_31_00_00_00, 3d);
    }
}
