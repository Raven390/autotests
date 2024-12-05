package businessObjects.api.clickhouseApiService.getClientTradingAccounts;

import helpers.httpHelper.HttpHelper;
import io.qameta.allure.Step;
import okhttp3.Response;

import java.io.IOException;

import static utils.ConfigFactory.*;

public class GetClientTradingAccountsRequest {

    @Step("Get client Trading Accounts by ucid")
    public static Response getClientTradingAccounts(String ucid) throws IOException {
        return new HttpHelper().sendGetRequest(CLICKHOUSE_API_BASE_PATH + CLICKHOUSE_API_GET_CLIENT_TRADING_ACCOUNTS_PATH.replace("{clientId}", ucid), null, null);
    }
}
