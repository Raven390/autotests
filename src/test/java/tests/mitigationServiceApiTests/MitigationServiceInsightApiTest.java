package tests.mitigationServiceApiTests;

import businessObjects.api.mitigationService.*;
import businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObject;
import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.data.ClientHelper;
import io.qameta.allure.AllureId;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

import static businessObjects.api.mitigationService.MitigationServiceRequest.*;
import static businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObjectFactory.generateUserByClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.CleanTableHelper.cleanCrmUserTableByClient;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_API)
@Tag(SUITE_MITIGATION_SERVICE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MitigationServiceInsightApiTest extends TestBaseWeb {

    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(client);
    private static final CrmTbAccountObject account = generateCrmTbAccountDataForUi(client);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static String token;
    private static Integer restrictionId;

    @BeforeAll
    public static void setup() throws ReflectiveOperationException, SQLException, JsonProcessingException {
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertObjectToDb(CRM_ACCOUNT_TABLE_NAME, account);
    }

    @Order(1)
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Get access token  for Insight mitigation service API")
    public void getAccessTokenInsightTest() throws JsonProcessingException {
        investigationPage.navigateEnterPage();
        com.microsoft.playwright.Response response = page.waitForResponse(
                responseObj -> responseObj.url().contains("openid-connect/token") && responseObj.status() == 200, () -> keycloackPage.loginAsAutotestUser()
        );
        if (response != null) {
            String responseBody = response.text();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            if (jsonNode.has("access_token")) {
                token = jsonNode.get("access_token").asText();
            } else {
                System.out.println("\"access_token\" not found in the response.");
            }
        } else {
            System.out.println("No matching response found.");
        }
    }

    @Order(2)
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1035")
    @DisplayName("Verify response of get restriction catalog insight endpoint")
    public void mitigationServiceInsight1Test() throws IOException {
        Response response = getRestrictionCatalogInsight(token);
        assertThat("Verify 200 response code", response.code(), is(200));
        assertThat(response.body(), notNullValue());
        RestrictionCatalogEntry[] catalog = objectMapper.readValue(response.body().string(), RestrictionCatalogEntry[].class);
        assertThat("Verify catalog length", catalog.length, is(20));
    }

    @Order(3)
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1036")
    @DisplayName("Verify response of get restriction insight endpoint (200 empty)")
    public void mitigationServiceInsight3Test() throws IOException {
        Response response = getRestrictionsByAccountServerIdInsight(token, client.getTradingAccount(), client.getServerId());
        assertThat("Verify 200 response code", response.code(), is(200));
        assertThat(response.body(), notNullValue());
        assertThat("Verify response is empty", response.body().string(), is("[]"));
    }

    @Order(4)
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1037")
    @DisplayName("Verify response of post GENERAL restriction insight endpoint (200)")
    public void mitigationServiceInsight4Test() throws IOException {
        PostRestrictionRequestBody postRestriction = new PostRestrictionRequestBody(null, "05", "GENERAL", client.getTradingAccount(), client.getServerId(), "test", new PostRestrictionRequestBody.UpdatedBy("autotest", "autotest"));
        Response response = postRestrictionInsight(token, postRestriction);
        assertThat("Verify 200 response code", response.code(), is(200));
        assertThat(response.body(), notNullValue());
        PostRestrictionResponse responseBody = objectMapper.readValue(response.body().string(), PostRestrictionResponse.class);
        assertThat("Verify id is in the response", responseBody.id, notNullValue());
        restrictionId = responseBody.id;
        assertThat("Verify ucid is in the response", responseBody.ucid, is(client.getUcid()));
    }

    @Order(5)
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1038")
    @DisplayName("Verify response of cancel restriction insight endpoint (200)")
    public void mitigationServiceInsight5Test() throws IOException {
        CancelRestrictionRequestBody cancelRestriction = new CancelRestrictionRequestBody("test", new CancelRestrictionRequestBody.UpdatedBy("autotest", "autotest"));
        Response response = cancelRestrictionInsight(token, restrictionId, cancelRestriction);
        assertThat("Verify 204 response code", response.code(), is(204));
    }

    @Order(6)
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1039")
    @DisplayName("Verify response of get GENERAL restriction insight endpoint (200)")
    public void mitigationServiceInsight6Test() throws IOException {
        Response response = getRestrictionsByAccountServerIdInsight(token, client.getTradingAccount(), client.getServerId());
        assertThat("Verify 200 response code", response.code(), is(200));
        assertThat(response.body(), notNullValue());
        GetRestrictionResponseBody[] responseBody = objectMapper.readValue(response.body().string(), GetRestrictionResponseBody[].class);
        assertThat(responseBody.length, is(1));
        GetRestrictionResponseBody restriction = responseBody[0];
        assertThat(restriction.id, is(restrictionId));
        assertThat(restriction.ucid, is(client.getUcid()));
        assertThat(restriction.code, is("05"));
        assertThat(restriction.type, is("GENERAL"));
        assertThat(restriction.status, is("CANCELLED"));
        assertThat(restriction.applyReason, is("test"));
        assertThat(restriction.cancelReason, is("test"));
        assertThat(restriction.updatedAt, notNullValue());
        assertThat(restriction.createdAt, notNullValue());
        assertThat(restriction.updatedBy, is(new GetRestrictionResponseBody.UpdatedBy("autotest", "autotest")));
        assertThat(restriction.account, nullValue());
        assertThat(restriction.serverId, nullValue());
    }

    @Order(7)
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1053")
    @DisplayName("Verify response of post TRADING restriction insight endpoint (200)")
    public void mitigationServiceInsight7Test() throws IOException {
        PostRestrictionRequestBody postRestriction = new PostRestrictionRequestBody(null, "08", "TRADING", client.getTradingAccount(), client.getServerId(), "test", new PostRestrictionRequestBody.UpdatedBy("autotest", "autotest"));
        Response response = postRestrictionInsight(token, postRestriction);
        assertThat("Verify 200 response code", response.code(), is(200));
        assertThat(response.body(), notNullValue());
        PostRestrictionResponse responseBody = objectMapper.readValue(response.body().string(), PostRestrictionResponse.class);
        assertThat("Verify id is in the response", responseBody.id, notNullValue());
        restrictionId = responseBody.id;
        assertThat("Verify ucid is in the response", responseBody.ucid, is(client.getUcid()));
    }

    @Order(8)
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1054")
    @DisplayName("Verify response of get TRADING restriction insight endpoint (200)")
    public void mitigationServiceInsight8Test() throws IOException {
        Response response = getRestrictionsByAccountServerIdInsight(token, client.getTradingAccount(), client.getServerId());
        assertThat("Verify 200 response code", response.code(), is(200));
        assertThat(response.body(), notNullValue());
        GetRestrictionResponseBody[] responseBody = objectMapper.readValue(response.body().string(), GetRestrictionResponseBody[].class);
        assertThat(responseBody.length, is(2));
        GetRestrictionResponseBody restriction = null;
        for (GetRestrictionResponseBody item : responseBody) {
            if (Objects.equals(item.code, "08")) {
                restriction = item;
            }
        }
        assertThat(restriction, notNullValue());
        assertThat(restriction.id, is(restrictionId));
        assertThat(restriction.ucid, is(client.getUcid()));
        assertThat(restriction.code, is("08"));
        assertThat(restriction.type, is("TRADING"));
        assertThat(restriction.status, is("APPLIED"));
        assertThat(restriction.applyReason, is("test"));
        assertThat(restriction.cancelReason, nullValue());
        assertThat(restriction.updatedAt, notNullValue());
        assertThat(restriction.createdAt, notNullValue());
        assertThat(restriction.updatedBy, is(new GetRestrictionResponseBody.UpdatedBy("autotest", "autotest")));
        assertThat(restriction.account, is(client.getTradingAccount().toString()));
        assertThat(restriction.serverId, is(client.getServerId().toString()));
    }

    @AfterAll
    public static void teardown() throws Exception {
        cleanCrmUserTableByClient(crmTbUser.ucid);
        closeAlert(crmTbUser.ucid);
    }
}
