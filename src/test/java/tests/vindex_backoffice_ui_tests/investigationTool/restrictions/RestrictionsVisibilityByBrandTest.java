package tests.vindex_backoffice_ui_tests.investigationTool.restrictions;

import business_objects.api.mitigation_service.PostRestrictionRequestBody;
import business_objects.api.mitigation_service.PostRestrictionResponse;
import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import static helpers.data.ClientFactory.getRandomInfinoxClientAllFields;
import static helpers.data.enums.Restriction.CLOSE_ONLY_MODE;
import static helpers.database.CleanTableHelper.cleanCrmUserTableByClient;
import static helpers.database.DbHelper.*;
import static helpers.database.CleanTableHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.insertCrmAccountsToDb;

public class RestrictionsVisibilityByBrandTest extends TestBaseWeb {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final ClientHelper client = getRandomInfinoxClientAllFields();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(client);
    private static final CrmTbAccountObject account = generateCrmTbAccountDataForUi(client);
    private static final String RESTRICTION_NOT_AVAILABLE_FOR_BRAND = "RESTRICTION_NOT_AVAILABLE_FOR_BRAND";

    @BeforeAll
    public static void setup() throws ReflectiveOperationException, SQLException, JsonProcessingException {
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertCrmAccountsToDb(account);
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
        assertThat("Verify restriction with visibility by brand = false is not displayed", restrictionPage.getDisplayedRestrictionsList(), not(contains(CLOSE_ONLY_MODE.getName())));
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


    @AfterAll
    public static void teardown() throws Exception {
        cleanCrmUserTableByClient(client.getUcid());
        cleanUserRestrictionGeneral(client.getUcid());
    }
}
