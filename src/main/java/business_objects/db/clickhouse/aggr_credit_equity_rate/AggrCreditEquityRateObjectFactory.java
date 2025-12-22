package business_objects.db.clickhouse.aggr_credit_equity_rate;

import static utils.Constants.TIME_2024_12_31_00_00_00;

import helpers.data.ClientHelper;
import io.qameta.allure.Step;

public class AggrCreditEquityRateObjectFactory {
    @Step("Generate data for given account")
    public static AggrCreditEquityRateObject generateCreditEquityRatioAccount(ClientHelper client) {
        return new AggrCreditEquityRateObject(
                client.getServerId().toString(),
                client.getTradingAccount().toString(),
                TIME_2024_12_31_00_00_00,
                1d,
                TIME_2024_12_31_00_00_00,
                2d,
                TIME_2024_12_31_00_00_00,
                3d);
    }
}
