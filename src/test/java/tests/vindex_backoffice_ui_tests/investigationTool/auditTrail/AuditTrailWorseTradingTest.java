package tests.vindex_backoffice_ui_tests.investigationTool.auditTrail;

import static business_objects.api.mitigation_service.CorrelationType.RESTRICTION_MANAGEMENT;
import static business_objects.api.mitigation_service.RestrictionStatus.APPLIED;
import static business_objects.api.mitigation_service.RestrictionStatus.CANCELLED;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static helpers.api.MitigationHelper.deleteTradingEnvironmentRestrictions;
import static helpers.api.RestrictionHelper.*;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.Restriction.WORSE_TRADING;
import static helpers.data.enums.TradingEnvironmentLevel.*;
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
import helpers.data.enums.TradingEnvironmentLevel;
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
    private static final CrmTbAccountObject account2 = generateCrmTbAccountDataForUi(client);
    private static final CrmTbAccountObject account3 = generateCrmTbAccountDataForUi(client);
    private static final CrmTbAccountObject account4 = generateCrmTbAccountDataForUi(client);
    private static final CrmTbAccountObject account5 = generateCrmTbAccountDataForUi(client);
    private static final CrmTbAccountObject account6 = generateCrmTbAccountDataForUi(client);

    private static final String USER = "Autotest User";
    private static final String RESTRICTION_MANAGEMENT_UI = "Restriction management";
    private static final String RULE_ENGINE_SYSTEM = "Rule engine";

    @BeforeAll
    static void setup() {
        insertObjectToDb(CRM_USER_TABLE_NAME, generateUserByClient(client));
        account2.account = getRandomIntPositive();
        account3.account = getRandomIntPositive();
        account4.account = getRandomIntPositive();
        account5.account = getRandomIntPositive();
        account6.account = getRandomIntPositive();
        insertObjectsToDb(
                MT_ACCOUNT_TABLE_NAME,
                List.of(
                        generateMtAccountByCrmTbAccount(account), generateMtAccountByCrmTbAccount(account2),
                        generateMtAccountByCrmTbAccount(account3), generateMtAccountByCrmTbAccount(account4),
                        generateMtAccountByCrmTbAccount(account5), generateMtAccountByCrmTbAccount(account6)));
        insertCrmAccountsToDb(account, account2, account3, account4, account5, account6);
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
        String levelType = LOW.getLabel();
        BigInteger accountID = BigInteger.valueOf(account.account);
        NewTradingEnvRestrictionRequestBody putRestrictionLow = new NewTradingEnvRestrictionRequestBody();
        putRestrictionLow.setType(RestrictionType.TRADING_ENVIRONMENT);
        putRestrictionLow.setUcid(client.getUcid());
        putRestrictionLow.setCode(WORSE_TRADING.getCode());
        String comment = "Initial test comment";
        putRestrictionLow.setComment(comment);
        putRestrictionLow.setUpdatedBy(new UpdatedBy().system(RULE_ENGINE_SYSTEM));
        putRestrictionLow.setAccountId(BigInteger.valueOf(account.account));
        putRestrictionLow.setServerId(account.getServerIdSt());
        putRestrictionLow.setLevel(LOW);
        putRestrictionV3(putRestrictionLow);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        auditTrailPage.navigate(client.getUcid());
        auditTrailPage.waitForPageToLoad();
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
                is(String.format("%s%n%s (%s: %s)", comment, WORSE_TRADING.getName(), levelType, accountID)));
    }

    @Test
    @Order(2)
    @AllureId("1998")
    @DisplayName("Audit trail. Verify worse trading put change level event")
    void worseTradingAuditTest2() throws IOException {
        BigInteger accountID = BigInteger.valueOf(account.account);
        NewTradingEnvRestrictionRequestBody putRestrictionHigh = new NewTradingEnvRestrictionRequestBody();
        putRestrictionHigh.setType(RestrictionType.TRADING_ENVIRONMENT);
        putRestrictionHigh.setUcid(client.getUcid());
        putRestrictionHigh.setCode(WORSE_TRADING.getCode());
        String comment = "Change level test comment";
        putRestrictionHigh.setComment(comment);
        putRestrictionHigh.setUpdatedBy(new UpdatedBy().system(RULE_ENGINE_SYSTEM));
        putRestrictionHigh.setAccountId(accountID);
        putRestrictionHigh.setServerId(account.getServerIdSt());
        putRestrictionHigh.setLevel(HIGH);
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
        is(String.format(
                "%s%n%s (%s ➝ %s: %s)", comment, WORSE_TRADING.getName(), LOW.getLabel(), HIGH.getLabel(), accountID));
    }

    @Test
    @Order(3)
    @AllureId("1999")
    @DisplayName("Audit trail. Verify worse trading cancel event")
    void worseTradingAuditTest3() throws IOException {
        BigInteger accountID = BigInteger.valueOf(account.account);
        String comment = "Cancel test comment";
        DeleteTradingEnvRestrictionRequestBody deleteRestriction = DeleteTradingEnvRestrictionRequestBody.builder()
                .type(RestrictionType.TRADING_ENVIRONMENT)
                .ucid(client.getUcid())
                .code(WORSE_TRADING.getCode())
                .cancelReason(comment)
                .correlationType(RESTRICTION_MANAGEMENT)
                .correlationId(getRandomUuidString())
                .updatedBy(new UpdatedBy().system(VINDEX_BO_SYSTEM).user(USER))
                .accountId(accountID)
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
        is(String.format("%s%n%s (%s)", comment, WORSE_TRADING.getName(), accountID));
    }

    @Test
    @Order(4)
    @AllureId("2000")
    @DisplayName("Audit trail. Verify worse trading post event")
    void worseTradingAuditTest4() throws IOException {
        TradingEnvironmentLevel levelType = MEDIUM;
        BigInteger accountID = BigInteger.valueOf(account.account);
        String correlationID = getRandomUuidString();
        NewTradingEnvRestrictionRequestBody postRestriction = new NewTradingEnvRestrictionRequestBody();
        postRestriction.setType(RestrictionType.TRADING_ENVIRONMENT);
        postRestriction.setUcid(client.getUcid());
        postRestriction.setCode(WORSE_TRADING.getCode());
        String comment = "Post test comment";
        postRestriction.setComment(comment);
        postRestriction.setCorrelationType(RESTRICTION_MANAGEMENT);
        postRestriction.setCorrelationId(correlationID);
        postRestriction.setUpdatedBy(new UpdatedBy().system(VINDEX_BO_SYSTEM).user(USER));
        postRestriction.setAccountId(accountID);
        postRestriction.setServerId(account.getServerIdSt());
        postRestriction.setLevel(levelType);
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
                is(String.format(
                        "%s%n%s (%s: %s)", comment, WORSE_TRADING.getName(), levelType.getLabel(), accountID)));
    }

    @Test
    @AllureId("2401")
    @DisplayName("Audit trail. Verify worse trading apply and delete different levels")
    void worseTradingVerifyApplyAndDelete() throws Exception {

        String applyAndDeleteComment = "Apply and delete different levels";
        String addLevelComment = "Add different levels";

        // preconditions
        deleteTradingEnvironmentRestrictions(client.getUcid());
        String correlationId = getRandomUuidString();
        BigInteger accountID = BigInteger.valueOf(account.account);
        BigInteger accountID2 = BigInteger.valueOf(account2.account);
        BigInteger accountID3 = BigInteger.valueOf(account3.account);
        BigInteger accountID4 = BigInteger.valueOf(account4.account);
        BigInteger accountID5 = BigInteger.valueOf(account5.account);
        BigInteger accountID6 = BigInteger.valueOf(account6.account);
        putWorseTradingRestriction(
                client.getUcid(), accountID, correlationId, account.getServerIdSt(), MEDIUM, addLevelComment, USER);
        waitUntilWorseTradingRestrictionHasStatusAndLevel(accountID, MEDIUM, APPLIED);
        putWorseTradingRestriction(
                client.getUcid(), accountID2, correlationId, account2.getServerIdSt(), MEDIUM, addLevelComment, USER);
        waitUntilWorseTradingRestrictionHasStatusAndLevel(accountID2, MEDIUM, APPLIED);

        // test
        String correlationId2 = getRandomUuidString();
        deleteWorseTradingRestriction(
                client.getUcid(), accountID, correlationId2, account.getServerIdSt(), applyAndDeleteComment, USER);
        waitUntilWorseTradingRestrictionHasStatusAndLevel(accountID, MEDIUM, CANCELLED);
        deleteWorseTradingRestriction(
                client.getUcid(), accountID2, correlationId2, account2.getServerIdSt(), applyAndDeleteComment, USER);
        waitUntilWorseTradingRestrictionHasStatusAndLevel(accountID2, MEDIUM, CANCELLED);
        putWorseTradingRestriction(
                client.getUcid(),
                accountID3,
                correlationId2,
                account3.getServerIdSt(),
                MEDIUM,
                applyAndDeleteComment,
                USER);
        waitUntilWorseTradingRestrictionHasStatusAndLevel(accountID3, MEDIUM, APPLIED);
        putWorseTradingRestriction(
                client.getUcid(),
                accountID4,
                correlationId2,
                account4.getServerIdSt(),
                MEDIUM,
                applyAndDeleteComment,
                USER);
        waitUntilWorseTradingRestrictionHasStatusAndLevel(accountID4, MEDIUM, APPLIED);
        putWorseTradingRestriction(
                client.getUcid(),
                accountID5,
                correlationId2,
                account5.getServerIdSt(),
                HIGH,
                applyAndDeleteComment,
                USER);
        waitUntilWorseTradingRestrictionHasStatusAndLevel(accountID5, HIGH, APPLIED);
        putWorseTradingRestriction(
                client.getUcid(),
                accountID6,
                correlationId2,
                account6.getServerIdSt(),
                HIGH,
                applyAndDeleteComment,
                USER);
        waitUntilWorseTradingRestrictionHasStatusAndLevel(accountID6, HIGH, APPLIED);

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        auditTrailPage.navigate(client.getUcid());
        List<AuditTrailItemV2> auditTrailItems = auditTrailPage.getAuditTrailItemsV2();
        AuditTrailItemV2 item = auditTrailItems.getFirst();
        assertThat(
                "Verify audit trail item header",
                item.getHeader(),
                is(String.format("%s%n%s", RESTRICTION_MANAGEMENT_UI, USER)));
        assertThat(
                "Verify audit trail item details",
                item.getDetails(),
                is(String.format(
                        "%s%n%s (%s: %s, %s; %s: %s, %s)%n%s (%s, %s)",
                        applyAndDeleteComment,
                        WORSE_TRADING.getName(),
                        MEDIUM.getLabel(),
                        accountID3,
                        accountID4,
                        HIGH.getLabel(),
                        accountID5,
                        accountID6,
                        WORSE_TRADING.getName(),
                        accountID,
                        accountID2)));
    }

    @Test
    @AllureId("2402")
    @DisplayName("Audit trail. Verify worse trading change and delete different levels")
    void worseTradingVerifyChangeAndDelete() throws Exception {

        String changeAndDeleteComment = "Change and delete different levels";
        String addLevelComment = "Add different levels";

        // preconditions
        deleteTradingEnvironmentRestrictions(client.getUcid());
        String correlationId = getRandomUuidString();
        BigInteger accountID = BigInteger.valueOf(account.account);
        BigInteger accountID2 = BigInteger.valueOf(account2.account);
        BigInteger accountID3 = BigInteger.valueOf(account3.account);
        BigInteger accountID4 = BigInteger.valueOf(account4.account);
        BigInteger accountID5 = BigInteger.valueOf(account5.account);
        putWorseTradingRestriction(
                client.getUcid(), accountID, correlationId, account.getServerIdSt(), HIGH, addLevelComment, USER);
        waitUntilWorseTradingRestrictionHasStatusAndLevel(accountID, HIGH, APPLIED);
        putWorseTradingRestriction(
                client.getUcid(), accountID2, correlationId, account2.getServerIdSt(), HIGH, addLevelComment, USER);
        waitUntilWorseTradingRestrictionHasStatusAndLevel(accountID2, HIGH, APPLIED);
        putWorseTradingRestriction(
                client.getUcid(), accountID3, correlationId, account3.getServerIdSt(), LOW, addLevelComment, USER);
        waitUntilWorseTradingRestrictionHasStatusAndLevel(accountID3, LOW, APPLIED);
        putWorseTradingRestriction(
                client.getUcid(), accountID4, correlationId, account4.getServerIdSt(), HIGH, addLevelComment, USER);
        waitUntilWorseTradingRestrictionHasStatusAndLevel(accountID4, HIGH, APPLIED);
        putWorseTradingRestriction(
                client.getUcid(), accountID5, correlationId, account5.getServerIdSt(), HIGH, addLevelComment, USER);
        waitUntilWorseTradingRestrictionHasStatusAndLevel(accountID5, HIGH, APPLIED);

        // test
        String correlationId2 = getRandomUuidString();
        putWorseTradingRestriction(
                client.getUcid(),
                accountID,
                correlationId2,
                account.getServerIdSt(),
                MEDIUM,
                changeAndDeleteComment,
                USER);
        waitUntilWorseTradingRestrictionHasStatusAndLevel(accountID, MEDIUM, APPLIED);
        putWorseTradingRestriction(
                client.getUcid(),
                accountID2,
                correlationId2,
                account2.getServerIdSt(),
                MEDIUM,
                changeAndDeleteComment,
                USER);
        waitUntilWorseTradingRestrictionHasStatusAndLevel(accountID2, MEDIUM, APPLIED);
        putWorseTradingRestriction(
                client.getUcid(),
                accountID3,
                correlationId2,
                account3.getServerIdSt(),
                MEDIUM,
                changeAndDeleteComment,
                USER);
        waitUntilWorseTradingRestrictionHasStatusAndLevel(accountID3, MEDIUM, APPLIED);
        deleteWorseTradingRestriction(
                client.getUcid(), accountID4, correlationId2, account4.getServerIdSt(), changeAndDeleteComment, USER);
        waitUntilWorseTradingRestrictionHasStatusAndLevel(accountID4, HIGH, CANCELLED);
        deleteWorseTradingRestriction(
                client.getUcid(), accountID5, correlationId2, account5.getServerIdSt(), changeAndDeleteComment, USER);
        waitUntilWorseTradingRestrictionHasStatusAndLevel(accountID5, HIGH, CANCELLED);

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        auditTrailPage.navigate(client.getUcid());
        List<AuditTrailItemV2> auditTrailItems = auditTrailPage.getAuditTrailItemsV2();
        AuditTrailItemV2 item = auditTrailItems.getFirst();
        assertThat(
                "Verify audit trail item header",
                item.getHeader(),
                is(String.format("%s%n%s", RESTRICTION_MANAGEMENT_UI, USER)));
        assertThat(
                "Verify audit trail item details",
                item.getDetails(),
                is(String.format(
                        "%s%n%s (%s ➝ %s: %s, %s; %s ➝ %s: %s)%n%s (%s, %s)",
                        changeAndDeleteComment,
                        WORSE_TRADING.getName(),
                        HIGH.getLabel(),
                        MEDIUM.getLabel(),
                        accountID,
                        accountID2,
                        LOW.getLabel(),
                        MEDIUM.getLabel(),
                        accountID3,
                        WORSE_TRADING.getName(),
                        accountID4,
                        accountID5)));
    }

    @Test
    @AllureId("2403")
    @DisplayName("Audit trail. Verify worse trading apply, change and delete different levels")
    void worseTradingVerifyApplyChangeAndDelete() throws Exception {

        String changeAndDeleteComment = "Apply, change and delete";
        String addLevelComment = "Add different levels";

        // preconditions
        deleteTradingEnvironmentRestrictions(client.getUcid());
        String correlationId = getRandomUuidString();
        BigInteger accountID = BigInteger.valueOf(account.account);
        BigInteger accountID2 = BigInteger.valueOf(account2.account);
        BigInteger accountID3 = BigInteger.valueOf(account3.account);
        BigInteger accountID4 = BigInteger.valueOf(account4.account);
        BigInteger accountID5 = BigInteger.valueOf(account5.account);
        putWorseTradingRestriction(
                client.getUcid(), accountID, correlationId, account.getServerIdSt(), LOW, addLevelComment, USER);
        waitUntilWorseTradingRestrictionHasStatusAndLevel(accountID, LOW, APPLIED);
        putWorseTradingRestriction(
                client.getUcid(), accountID2, correlationId, account2.getServerIdSt(), MEDIUM, addLevelComment, USER);
        waitUntilWorseTradingRestrictionHasStatusAndLevel(accountID2, MEDIUM, APPLIED);
        putWorseTradingRestriction(
                client.getUcid(), accountID3, correlationId, account3.getServerIdSt(), HIGH, addLevelComment, USER);
        waitUntilWorseTradingRestrictionHasStatusAndLevel(accountID3, HIGH, APPLIED);
        putWorseTradingRestriction(
                client.getUcid(), accountID4, correlationId, account4.getServerIdSt(), HIGH, addLevelComment, USER);
        waitUntilWorseTradingRestrictionHasStatusAndLevel(accountID4, HIGH, APPLIED);

        // test
        String correlationId2 = getRandomUuidString();
        deleteWorseTradingRestriction(
                client.getUcid(), accountID, correlationId2, account.getServerIdSt(), changeAndDeleteComment, USER);
        waitUntilWorseTradingRestrictionHasStatusAndLevel(accountID, LOW, CANCELLED);
        deleteWorseTradingRestriction(
                client.getUcid(), accountID2, correlationId2, account2.getServerIdSt(), changeAndDeleteComment, USER);
        waitUntilWorseTradingRestrictionHasStatusAndLevel(accountID2, MEDIUM, CANCELLED);
        putWorseTradingRestriction(
                client.getUcid(),
                accountID3,
                correlationId2,
                account3.getServerIdSt(),
                MEDIUM,
                changeAndDeleteComment,
                USER);
        waitUntilWorseTradingRestrictionHasStatusAndLevel(accountID3, MEDIUM, APPLIED);
        putWorseTradingRestriction(
                client.getUcid(),
                accountID4,
                correlationId2,
                account4.getServerIdSt(),
                MEDIUM,
                changeAndDeleteComment,
                USER);
        waitUntilWorseTradingRestrictionHasStatusAndLevel(accountID4, MEDIUM, APPLIED);
        putWorseTradingRestriction(
                client.getUcid(),
                accountID5,
                correlationId2,
                account5.getServerIdSt(),
                MEDIUM,
                changeAndDeleteComment,
                USER);
        waitUntilWorseTradingRestrictionHasStatusAndLevel(accountID5, MEDIUM, APPLIED);

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        auditTrailPage.navigate(client.getUcid());
        List<AuditTrailItemV2> auditTrailItems = auditTrailPage.getAuditTrailItemsV2();
        AuditTrailItemV2 item = auditTrailItems.getFirst();
        assertThat(
                "Verify audit trail item header",
                item.getHeader(),
                is(String.format("%s%n%s", RESTRICTION_MANAGEMENT_UI, USER)));
        assertThat(
                "Verify audit trail item details",
                item.getDetails(),
                is(String.format(
                        "%s%n%s (%s: %s; %s ➝ %s: %s, %s)%n%s (%s, %s)",
                        changeAndDeleteComment,
                        WORSE_TRADING.getName(),
                        MEDIUM.getLabel(),
                        accountID5,
                        HIGH.getLabel(),
                        MEDIUM.getLabel(),
                        accountID3,
                        accountID4,
                        WORSE_TRADING.getName(),
                        accountID,
                        accountID2)));
    }
}
