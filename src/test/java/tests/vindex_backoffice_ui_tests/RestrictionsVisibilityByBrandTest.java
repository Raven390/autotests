package tests.vindex_backoffice_ui_tests;

import business_objects.api.mitigation_service.PostRestrictionRequestBody;
import business_objects.api.mitigation_service.PostRestrictionResponse;
import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
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

import static business_objects.api.mitigation_service.MitigationServiceRequest.*;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static helpers.data.ClientFactory.getRandomStarTraderClientAllFields;
import static helpers.data.enums.Restriction.CLOSE_ONLY_MODE;
import static helpers.database.CleanTableHelper.cleanCrmUserTableByClient;
import static helpers.database.DbHelper.*;
import static helpers.database.CleanTableHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

public class RestrictionsVisibilityByBrandTest extends TestBaseWeb {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final ClientHelper client = getRandomStarTraderClientAllFields();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(client);
    private static final CrmTbAccountObject account = generateCrmTbAccountDataForUi(client);
    private static final String RESTRICTION_NOT_AVAILABLE_FOR_BRAND = "RESTRICTION_NOT_AVAILABLE_FOR_BRAND";

    @BeforeAll
    public static void setup() throws ReflectiveOperationException, SQLException, JsonProcessingException {
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertObjectToDb(CRM_ACCOUNT_TABLE_NAME, account);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1081")
    @DisplayName("Verify disabled restriction is not visible in UI")
    public void verifyDisabledRestrictionIsNotVisibleInUiTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(client.getUcid());
        alertsPage.waitForPageToLoad();
        restrictionPage.openRestrictionsTab();
        restrictionPage.waitForPageToLoad();
        assertThat("Verify restriction is not visible", restrictionPage.isRestrictionPresent(CLOSE_ONLY_MODE.getName()), is(false));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1082")
    @DisplayName("Verify disabled restriction can not be applied with POST")
    public void verifyDisabledRestrictionIsNotApplied1Test() throws IOException {
        Response response = postRestriction(new PostRestrictionRequestBody(
                client.getUcid(), CLOSE_ONLY_MODE.getCode(), CLOSE_ONLY_MODE.getType(), account.account, account.serverIdSt, "Automation test", new PostRestrictionRequestBody.UpdatedBy("Auto", "Test")
        ));
        assertThat("Assert response code is 400", response.code(), equalTo(400));
        assert response.body() != null;
        PostRestrictionResponse responseBody = objectMapper.readValue(
                response.body().string(), PostRestrictionResponse.class
        );
        assertThat("Assert response body", responseBody.code, equalTo(RESTRICTION_NOT_AVAILABLE_FOR_BRAND));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1083")
    @DisplayName("Verify disabled restriction can not be applied with PUT")
    public void verifyDisabledRestrictionIsNotApplied2Test() throws IOException {
        Response response = putRestriction(new PostRestrictionRequestBody(
                client.getUcid(), CLOSE_ONLY_MODE.getCode(), CLOSE_ONLY_MODE.getType(), account.account, account.serverIdSt, "Automation test", new PostRestrictionRequestBody.UpdatedBy("Auto", "Test")
        ));
        assertThat("Assert response code is 400", response.code(), equalTo(400));
        assert response.body() != null;
        PostRestrictionResponse responseBody = objectMapper.readValue(
                response.body().string(), PostRestrictionResponse.class
        );
        assertThat("Assert response body", responseBody.code, equalTo(RESTRICTION_NOT_AVAILABLE_FOR_BRAND));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1084")
    @DisplayName("Verify disabled restriction can be applied with the Insight endpoint")
    public void verifyDisabledRestrictionIsAppliedWithInsightTest() throws IOException {
        String token = "";
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        com.microsoft.playwright.Response tokenResponse = page.waitForResponse(
                responseObj -> responseObj.url().contains("openid-connect/token") && responseObj.status() == 200, () -> investigationPage.navigateToClient(crmTbUser.ucid)
        );
        if (tokenResponse != null) {
            String responseBody = tokenResponse.text();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            if (jsonNode.has("access_token")) {
                token = jsonNode.get("access_token").asText();
            } else {
                System.out.println("\"access_token\" not found in the response.");
            }
        } else {
            System.out.println("No matching response found.");
        }
        Response response = postRestrictionInsight(token, new PostRestrictionRequestBody(
                null, CLOSE_ONLY_MODE.getCode(), CLOSE_ONLY_MODE.getType(), account.account, account.serverIdSt, "Automation test", new PostRestrictionRequestBody.UpdatedBy("Auto", "Test")
        ));
        assertThat("Assert response code is 200", response.code(), equalTo(200));
        assert response.body() != null;
        PostRestrictionResponse responseBody = objectMapper.readValue(
                response.body().string(), PostRestrictionResponse.class
        );
        assertThat("Assert response id not null", responseBody.id, notNullValue());
        assertThat("Assert response ucid", responseBody.ucid, is(client.getUcid()));
    }


    @AfterAll
    public static void teardown() throws Exception {
        cleanCrmUserTableByClient(client.getUcid());
        cleanUserRestriction(client.getUcid());
    }
}
