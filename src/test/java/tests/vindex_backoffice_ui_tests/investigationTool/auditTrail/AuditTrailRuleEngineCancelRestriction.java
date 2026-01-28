package tests.vindex_backoffice_ui_tests.investigationTool.auditTrail;

import static business_objects.api.mitigation_service.CorrelationType.RULE_ENGINE;
import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static helpers.api.MitigationHelper.deleteTradingEnvironmentRestrictions;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.AuHelper.cleanClientAudit;
import static helpers.database.BoHelper.deleteUserBO;
import static helpers.database.CleanTableHelper.cleanCrmUserTableByClient;
import static helpers.database.DbHelper.insertObjectToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static utils.Constants.*;
import static utils.Utils.insertCrmAccountsToDb;

import business_objects.api.mitigation_service.*;
import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.ui.audit_trail.AuditTrailItemV2;
import helpers.api.MitigationHelper;
import helpers.api.RestrictionHelper;
import helpers.data.ClientHelper;
import helpers.database.CleanTableHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.UUID;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_WEB)
@Feature("BMS-3123 [BE] Support restriction cancellation event for correlation type RULE_ENGINE")
class AuditTrailRuleEngineCancelRestriction extends TestBaseWeb {

    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final CrmTbAccountObject account = generateCrmTbAccountDataForUi(client);
    private static final String RESTRICTION_MANAGEMENT_UI = "Restriction management";
    private static final String RULE_ENGINE_SYSTEM = "Rule Engine";

    @BeforeAll
    static void setup() throws InterruptedException, IOException {
        insertObjectToDb(CRM_USER_TABLE_NAME, generateUserByClient(client));
        insertCrmAccountsToDb(account);
        Response response = enableCRMEmulator();
        assertNotNull(response);
        Thread.sleep(5000);
    }

    @BeforeEach
    void before() throws Exception {
        CleanTableHelper.cleanUserRestrictionGeneral(client.getUcid());
        CleanTableHelper.cleanUserRestrictionTrading(client.getUcid());
        CleanTableHelper.cleanUserAudit(client.getUcid());
    }

    @AfterAll
    static void teardown() throws Exception {
        deleteUserBO(client.getUcid());
        cleanClientAudit(client.getUcid());
        deleteTradingEnvironmentRestrictions(client.getUcid());
        cleanCrmUserTableByClient(client.getUcid());
        CleanTableHelper.cleanUserRestrictionGeneral(client.getUcid());
        CleanTableHelper.cleanUserRestrictionTrading(client.getUcid());
    }

    @Test
    @AllureId("2101")
    @DisplayName("Audit Trail: Rule Engine cancel GENERAL restriction")
    void applyAndCancelGeneralTest() throws Exception {
        var generalRestrictionRq = (NewGeneralRestriction) new NewGeneralRestriction()
                .type(RestrictionType.GENERAL)
                .correlationType(RULE_ENGINE)
                .correlationId(UUID.randomUUID().toString())
                .ucid(client.getUcid())
                .code("05")
                .comment("Comment general 1")
                .updatedBy(new UpdatedBy().system("Rule Engine"));
        RestrictionHelper.addGeneralRestrictionV3(generalRestrictionRq);

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
                is(String.format("%s%n%s", "Comment general 1", "Login CRM")));

        var restrictions = MitigationHelper.getClientGeneralRestrictionListFromDb(client);

        assertThat(restrictions, hasSize(1));
        var generalRestriction = restrictions.getFirst();

        var cancelRestrictionRq = (CancelGeneralRestriction) new CancelGeneralRestriction()
                .ucid(client.getUcid())
                .restrictionId(generalRestriction.getId())
                .type(RestrictionType.GENERAL)
                .cancelReason("Comment general 2")
                .correlationType(RULE_ENGINE)
                .correlationId(UUID.randomUUID().toString())
                .updatedBy(new UpdatedBy().system(RULE_ENGINE_SYSTEM));

        RestrictionHelper.cancelGeneralRestrictionV3(cancelRestrictionRq);
        auditTrailPage.navigate(client.getUcid());
        List<AuditTrailItemV2> auditTrailItemsAfter = auditTrailPage.getAuditTrailItemsV2();
        assertThat("Verify amount of audit trail items", auditTrailItems, hasSize(1));
        AuditTrailItemV2 item2 = auditTrailItemsAfter.getFirst();
        assertThat(
                "Verify audit trail item header",
                item2.getHeader(),
                is(String.format("%s%n%s", RESTRICTION_MANAGEMENT_UI, RULE_ENGINE_SYSTEM)));
        assertThat(
                "Verify audit trail item details",
                item2.getDetails(),
                is(String.format("%s%n%s", "Comment general 2", "Login CRM")));
    }

    @Test
    @AllureId("2102")
    @DisplayName("Audit Trail: Rule Engine cancel TRADING restriction")
    void applyAndCancelTradingTest() throws Exception {
        var tradingRestrictionRq = (NewTradingRestriction) new NewTradingRestriction()
                .accountId(BigInteger.valueOf(account.account))
                .serverId(account.serverIdSt)
                .type(RestrictionType.TRADING)
                .correlationType(RULE_ENGINE)
                .correlationId(UUID.randomUUID().toString())
                .ucid(client.getUcid())
                .code("06")
                .comment("Comment trading 1")
                .updatedBy(new UpdatedBy().system("Rule Engine"));
        RestrictionHelper.addTradingRestrictionV3(tradingRestrictionRq);

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
                is(String.format("%s%n%s", "Comment trading 1", "Close only mode")));

        var restrictions = MitigationHelper.getClientTradingRestrictionListFromDb(client);

        assertThat(restrictions, hasSize(1));
        var tradingRestriction = restrictions.getFirst();

        var cancelRestrictionRq = (CancelTradingRestriction) new CancelTradingRestriction()
                .ucid(client.getUcid())
                .restrictionId(tradingRestriction.getId())
                .type(RestrictionType.TRADING)
                .cancelReason("Comment trading 2")
                .correlationType(RULE_ENGINE)
                .correlationId(UUID.randomUUID().toString())
                .updatedBy(new UpdatedBy().system(RULE_ENGINE_SYSTEM));

        RestrictionHelper.cancelTradingRestrictionV3(cancelRestrictionRq);
        auditTrailPage.navigate(client.getUcid());
        List<AuditTrailItemV2> auditTrailItemsAfter = auditTrailPage.getAuditTrailItemsV2();
        assertThat("Verify amount of audit trail items", auditTrailItems, hasSize(1));
        AuditTrailItemV2 item2 = auditTrailItemsAfter.getFirst();
        assertThat(
                "Verify audit trail item header",
                item2.getHeader(),
                is(String.format("%s%n%s", RESTRICTION_MANAGEMENT_UI, RULE_ENGINE_SYSTEM)));
        assertThat(
                "Verify audit trail item details",
                item2.getDetails(),
                is(String.format("%s%n%s", "Comment trading 2", "Close only mode")));
    }
}
