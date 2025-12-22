package business_objects.api.clickhouse_api_service.get_client_trading_accounts;

import static utils.ConfigFactory.*;

import helpers.http_helper.HttpHelper;
import io.qameta.allure.Step;
import java.io.IOException;
import okhttp3.Response;

public class GetClientTradingAccountsRequest {

    @Step("Get client Trading Accounts by ucid")
    public static Response getClientTradingAccounts(String ucid) throws IOException {
        return new HttpHelper()
                .sendGetRequest(
                        CLICKHOUSE_API_BASE_TEST
                                + CLICKHOUSE_API_GET_CLIENT_TRADING_ACCOUNTS.replace("{clientId}", ucid),
                        null,
                        null);
    }
}
