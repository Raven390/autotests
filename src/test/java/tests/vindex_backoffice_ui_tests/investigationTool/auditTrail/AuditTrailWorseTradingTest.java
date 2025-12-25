package tests.vindex_backoffice_ui_tests.investigationTool.auditTrail;

import static business_objects.api.mitigation_service.CorrelationType.RESTRICTION_MANAGEMENT;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static helpers.api.MitigationHelper.deleteTradingEnvironmentRestrictions;
import static helpers.api.RestrictionHelper.*;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.Restriction.WORSE_TRADING;
import static helpers.database.AuHelper.cleanClientAudit;
import static helpers.database.BoHelper.deleteUserBO;
import static helpers.database.CleanTableHelper.cleanCrmUserTableByClient;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.*;

import business_objects.api.mitigation_service.*;
import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.ui.audit_trail.AuditTrailItemV2;
import helpers.data.ClientHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_WEB)
@Feature("BMS-2891 Store data in audit trail (worse trading)")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuditTrailWorseTradingTest extends TestBaseWeb {

    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final CrmTbAccountObject account = generateCrmTbAccountDataForUi(client);
    private static final String USER = "Autotest User";
    private static final String RESTRICTION_MANAGEMENT_UI = "Restriction management";
    private static final String RULE_ENGINE_SYSTEM = "Rule engine";

    @BeforeAll
    static void setup() {
        insertObjectToDb(CRM_USER_TABLE_NAME, generateUserByClient(client));
        insertCrmAccountsToDb(account);
    }

    @AfterAll
    static void teardown() throws Exception {
        deleteUserBO(client.getUcid());
        cleanClientAudit(client.getUcid());
        deleteTradingEnvironmentRestrictions(client.getUcid());
        cleanCrmUserTableByClient(client.getUcid());
    }

    @Test
    @Order(1)
    @AllureId("1997")
    @DisplayName("Audit trail. Verify worse trading put event")
    void worseTradingAuditTest1() throws IOException {
        NewTradingEnvRestrictionRequestBody putRestrictionLow = new NewTradingEnvRestrictionRequestBody();
        putRestrictionLow.setType(RestrictionType.TRADING_ENVIRONMENT);
        putRestrictionLow.setUcid(client.getUcid());
        putRestrictionLow.setCode(WORSE_TRADING.getCode());
        String comment = "Initial test comment";
        putRestrictionLow.setComment(comment);
        putRestrictionLow.setUpdatedBy(new UpdatedBy().system(RULE_ENGINE_SYSTEM));
        putRestrictionLow.setAccountId(BigInteger.valueOf(account.account));
        putRestrictionLow.setServerId(account.getServerIdSt());
        putRestrictionLow.setLevel("LOW");
        putRestrictionV3(putRestrictionLow);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        auditTrailPage.navigate(client.getUcid());
        List<AuditTrailItemV2> auditTrailItems = auditTrailPage.getAuditTrailItemsV2();
        assertThat("Verify amount of audit trail items", auditTrailItems, hasSize(1));
        AuditTrailItemV2 item = auditTrailItems.getFirst();
        assertThat(
                "Verify audit trail item header",
                item.getHeader(),
                is(String.format("%s%n%s", RESTRICTION_MANAGEMENT_UI, RULE_ENGINE_SYSTEM)));
        assertThat(
                "Verify audit trail item details",
                item.getDetails(),
                is(String.format("%s%n%s", comment, WORSE_TRADING.getName())));
    }

    @Test
    @Order(2)
    @AllureId("1998")
    @DisplayName("Audit trail. Verify worse trading put change level event")
    void worseTradingAuditTest2() throws IOException {
        NewTradingEnvRestrictionRequestBody putRestrictionHigh = new NewTradingEnvRestrictionRequestBody();
        putRestrictionHigh.setType(RestrictionType.TRADING_ENVIRONMENT);
        putRestrictionHigh.setUcid(client.getUcid());
        putRestrictionHigh.setCode(WORSE_TRADING.getCode());
        String comment = "Change level test comment";
        putRestrictionHigh.setComment(comment);
        putRestrictionHigh.setUpdatedBy(new UpdatedBy().system(RULE_ENGINE_SYSTEM));
        putRestrictionHigh.setAccountId(BigInteger.valueOf(account.account));
        putRestrictionHigh.setServerId(account.getServerIdSt());
        putRestrictionHigh.setLevel("HIGH");
        putRestrictionV3(putRestrictionHigh);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        auditTrailPage.navigate(client.getUcid());
        List<AuditTrailItemV2> auditTrailItems = auditTrailPage.getAuditTrailItemsV2();
        assertThat("Verify amount of audit trail items", auditTrailItems, hasSize(2));
        AuditTrailItemV2 item = auditTrailItems.getFirst();
        assertThat(
                "Verify audit trail item header",
                item.getHeader(),
                is(String.format("%s%n%s", RESTRICTION_MANAGEMENT_UI, RULE_ENGINE_SYSTEM)));
        assertThat(
                "Verify audit trail item details",
                item.getDetails(),
                is(String.format("%s%n%s", comment, WORSE_TRADING.getName())));
    }

    @Test
    @Order(3)
    @AllureId("1999")
    @DisplayName("Audit trail. Verify worse trading cancel event")
    void worseTradingAuditTest3() throws IOException {
        String comment = "Cancel test comment";
        DeleteTradingEnvRestrictionRequestBody deleteRestriction = DeleteTradingEnvRestrictionRequestBody.builder()
                .type(RestrictionType.TRADING_ENVIRONMENT)
                .ucid(client.getUcid())
                .code(WORSE_TRADING.getCode())
                .cancelReason(comment)
                .correlationType(RESTRICTION_MANAGEMENT)
                .correlationId(getRandomUuidString())
                .updatedBy(new UpdatedBy().system(VINDEX_BO_SYSTEM).user(USER))
                .accountId(BigInteger.valueOf(account.getAccount()))
                .serverId(account.getServerIdSt())
                .build();
        deleteRestrictionV3(deleteRestriction);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        auditTrailPage.navigate(client.getUcid());
        List<AuditTrailItemV2> auditTrailItems = auditTrailPage.getAuditTrailItemsV2();
        assertThat("Verify amount of audit trail items", auditTrailItems, hasSize(3));
        AuditTrailItemV2 item = auditTrailItems.getFirst();
        assertThat(
                "Verify audit trail item header",
                item.getHeader(),
                is(String.format("%s%n%s", RESTRICTION_MANAGEMENT_UI, USER)));
        assertThat(
                "Verify audit trail item details",
                item.getDetails(),
                is(String.format("%s%n%s", comment, WORSE_TRADING.getName())));
    }

    @Test
    @Order(4)
    @AllureId("2000")
    @DisplayName("Audit trail. Verify worse trading post event")
    void worseTradingAuditTest4() throws IOException {
        NewTradingEnvRestrictionRequestBody postRestriction = new NewTradingEnvRestrictionRequestBody();
        postRestriction.setType(RestrictionType.TRADING_ENVIRONMENT);
        postRestriction.setUcid(client.getUcid());
        postRestriction.setCode(WORSE_TRADING.getCode());
        String comment = "Post test comment";
        postRestriction.setComment(comment);
        postRestriction.setCorrelationType(RESTRICTION_MANAGEMENT);
        postRestriction.setCorrelationId(getRandomUuidString());
        postRestriction.setUpdatedBy(new UpdatedBy().system(VINDEX_BO_SYSTEM).user(USER));
        postRestriction.setAccountId(BigInteger.valueOf(account.account));
        postRestriction.setServerId(account.getServerIdSt());
        postRestriction.setLevel("MEDIUM");
        postRestrictionV3(postRestriction);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        auditTrailPage.navigate(client.getUcid());
        List<AuditTrailItemV2> auditTrailItems = auditTrailPage.getAuditTrailItemsV2();
        assertThat("Verify amount of audit trail items", auditTrailItems, hasSize(4));
        AuditTrailItemV2 item = auditTrailItems.getFirst();
        assertThat(
                "Verify audit trail item header",
                item.getHeader(),
                is(String.format("%s%n%s", RESTRICTION_MANAGEMENT_UI, USER)));
        assertThat(
                "Verify audit trail item details",
                item.getDetails(),
                is(String.format("%s%n%s", comment, WORSE_TRADING.getName())));
    }
}
