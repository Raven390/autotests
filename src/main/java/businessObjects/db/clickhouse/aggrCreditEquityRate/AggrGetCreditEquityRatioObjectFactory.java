package businessObjects.db.clickhouse.aggrCreditEquityRate;

import helpers.data.ClientHelper;
import io.qameta.allure.Step;

public class AggrGetCreditEquityRatioObjectFactory {
    @Step("Generate mirror trades by account object")
    public static AggrGetCreditEquityRatioObject generateCreditEquityRatioAccount(ClientHelper client) {
        return new AggrGetCreditEquityRatioObject(client.getServerId().toString(),client.getTradingAccount().toString(),"2024-12-31 00:00:00","1","2","3");
    }
}
