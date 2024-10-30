package tests.clickHouseApiServiceTests;

import businessObjects.api.clickhouseApiService.getClients.GetClientsResponse;
import businessObjects.db.crmTbUserTable.CrmTbUserObject;
import businessObjects.db.mtTbUser.MtTbUserObject;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import okhttp3.Response;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBaseApi;
import utils.Utils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static businessObjects.api.clickhouseApiService.getClients.GetClientsRequest.getClientsIdByTradingAccountServerId;
import static businessObjects.db.crmTbUserTable.CrmTbUserObjectFactory.generateUserByUserId;
import static businessObjects.db.mtTbUser.MtTbUserObjectFactory.generateMtTbUserData;
import static helpers.database.DbHelper.insertObjectToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static utils.Constants.*;

@Feature(FEATURE_CLICKHOUSE_API_SERVICE)
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_CLIENTS)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
public class GetClientsTests extends TestBaseApi {

    @Disabled
    @Test
    @DisplayName("Clickhouse Api. Get client by trading account & server ID")
    @AllureId("")
    public void getClientSuccessTest() throws IOException, ReflectiveOperationException, SQLException {
        // Insert in users tb
        Integer userId = Utils.getRandomIntPositive();
        String brand = "vantage";
        String ucid = brand+"-"+userId;
        CrmTbUserObject crmObject = generateUserByUserId(userId);
        insertObjectToDb(CRM_USER_TABLE_NAME, crmObject);
        // Insert object in
        int tradingAccount = Utils.getRandomIntPositive();
        int serverId = Utils.getRandomIntPositive();
        MtTbUserObject mtObject = generateMtTbUserData(Utils.getRandomUuidString(), ucid,tradingAccount,serverId);
        insertObjectToDb(MT_USER_TABLE_NAME, mtObject);

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", tradingAccount);
        queryParams.put("serverId", serverId);
        Response response = getClientsIdByTradingAccountServerId(queryParams);

        GetClientsResponse clients = objectMapper.readValue(response.body().string(), GetClientsResponse.class);
        System.out.println(response);
        assertThat("Check response code", response.code(), is(200));
    }
}
