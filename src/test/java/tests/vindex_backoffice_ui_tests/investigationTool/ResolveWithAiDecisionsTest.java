package tests.vindex_backoffice_ui_tests.investigationTool;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.kafka.ai_decisions.AiDecisionsFactory.generateAiDecisionsByClientAlertIdClassifications;
import static business_objects.kafka.alerts.RuleAlertFactory.generateTradingAlertWithDecision;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.FraudType.*;
import static helpers.data.enums.FraudTypeStatus.*;
import static helpers.database.ArHelper.deleteUserFromAbuseRegistry;
import static helpers.database.BoHelper.*;
import static helpers.database.CleanTableHelper.*;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.insertCrmAccountsToDb;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.kafka.ai_decisions.AiDecisions;
import business_objects.kafka.alerts.PaymentAlertMessageV2;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.data.ClientHelper;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import java.util.List;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_WEB)
@Feature("BMS-3251 Highlight different decisions on resolution")
class ResolveWithAiDecisionsTest extends TestBaseWeb {

    private static final KafkaHelper kafka = new KafkaHelper();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmUser = generateUserByClient(client);
    private static final CrmTbAccountObject crmAccount = generateCrmTbAccountDataForUi(client);
    private static final String AI_DECISION_MISMATCH_TEXT = "AI decision mismatch";
    private static final String AI_DECISION_MATCH_TEXT = "AI decision match";
    private static final String RESOLVE_COMMENT = "Autotest resolve comment";
    private static final String AI_DECISION_TOOLTIP_TEXT = "%s%% confidence of %s and %s";
    private static final String AI_DECISION_MISMATCH_TOAST_TEXT =
            "AI confirms %s and %s with %s%% confidence. Are you sure in your resolution? You can proceed with your decision or reassign the case for additional review";

    @BeforeAll
    static void setup() {
        objectMapper.findAndRegisterModules();
        insertObjectToDb(CRM_USER_TABLE_NAME, crmUser);
        insertCrmAccountsToDb(crmAccount);
        insertObjectToDb(MT_ACCOUNT_TABLE_NAME, generateMtAccountByCrmTbAccount(crmAccount));
    }

    @AfterAll
    static void teardown() throws Exception {
        cleanCrmUserTableByClient(client.getUcid());
        deleteUserBO(client.getUcid());
        cleanUserAudit(client.getUcid());
        deleteUserFromAbuseRegistry(client.getUcid());
        cleanUserRestrictionGeneral(client.getUcid());
    }

    @AfterEach
    void teardownEach() {
        closeAlert(client.getUcid());
        deleteAiDecisionsByUcid(client.getUcid());
    }

    @Test
    @AllureId("2404")
    @DisplayName("Verify fraud mismatch decision on resolution")
    void resolveAiDecisionsTest1() throws Exception {
        // Alert with rule that is trusted
        sendAlertWithAiDecisions();

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsTradingOpsUser();
        investigationPage.navigateToClient(client.getUcid());
        resolvePage.openResolveSuspicious();
        resolvePage.addFraud(MARKET_MANIPULATION, POTENTIAL);
        assertThat("Verify AI decision mismatch text", resolvePage.getAiDecisionText(), is(AI_DECISION_MISMATCH_TEXT));
        assertThat(
                "Verify AI decision mismatch tooltip text",
                resolvePage.getAiDecisionTooltipText(),
                is(String.format(AI_DECISION_TOOLTIP_TEXT, 90, CPA_ABUSE.getName(), BONUS_ABUSE.getName())));
        resolvePage.fillCommentAndApply(RESOLVE_COMMENT);
        assertThat(
                "Verify AI decision mismatch confirmation text",
                resolvePage.getFraudAiDecisionMismatchConfirmationText(),
                is(String.format(AI_DECISION_MISMATCH_TOAST_TEXT, CPA_ABUSE.getName(), BONUS_ABUSE.getName(), 90)));
        resolvePage.clickFraudAiDecisionMismatchConfirmButton();
        resolvePage.waitForSuccessToastVisibility();
    }

    @Test
    @AllureId("2405")
    @DisplayName("Verify fraud match decision on resolution")
    void resolveAiDecisionsTest2() throws Exception {
        // Alert with rule that is trusted
        sendAlertWithAiDecisions();

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsTradingOpsUser();
        investigationPage.navigateToClient(client.getUcid());
        resolvePage.openResolveSuspicious();
        resolvePage.addFraud(CPA_ABUSE, POTENTIAL);
        assertThat("Verify AI decision match text", resolvePage.getAiDecisionText(), is(AI_DECISION_MATCH_TEXT));
        assertThat(
                "Verify AI decision match tooltip text",
                resolvePage.getAiDecisionTooltipText(),
                is(String.format(AI_DECISION_TOOLTIP_TEXT, 90, CPA_ABUSE.getName(), BONUS_ABUSE.getName())));
        resolvePage.fillCommentAndApply(RESOLVE_COMMENT);
        resolvePage.waitForSuccessToastVisibility();
    }

    @Test
    @AllureId("2406")
    @DisplayName("Verify mismatch decision no fraud to assign drawer on resolution")
    void resolveAiDecisionsTest3() throws Exception {
        // Alert with rule that is trusted
        sendAlertWithAiDecisions();

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsTradingOpsUser();
        investigationPage.navigateToClient(client.getUcid());
        resolvePage.openResolveSuspicious();
        resolvePage.fillCommentAndApply(RESOLVE_COMMENT);
        assertThat(
                "Verify AI decision mismatch confirmation text",
                resolvePage.getFraudAiDecisionMismatchConfirmationText(),
                is(String.format(AI_DECISION_MISMATCH_TOAST_TEXT, CPA_ABUSE.getName(), BONUS_ABUSE.getName(), 90)));
        resolvePage.clickFraudAiDecisionMismatchReassignButton();
        assignDrawer.waitForAssignDrawerToLoad();
    }

    @Test
    @AllureId("2407")
    @DisplayName("Verify mismatch decision no fraud confirm on resolution")
    void resolveAiDecisionsTest4() throws Exception {
        // Alert with rule that is trusted
        sendAlertWithAiDecisions();

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsTradingOpsUser();
        investigationPage.navigateToClient(client.getUcid());
        resolvePage.openResolveSuspicious();
        resolvePage.fillCommentAndApply(RESOLVE_COMMENT);
        assertThat(
                "Verify AI decision mismatch confirmation text",
                resolvePage.getFraudAiDecisionMismatchConfirmationText(),
                is(String.format(AI_DECISION_MISMATCH_TOAST_TEXT, CPA_ABUSE.getName(), BONUS_ABUSE.getName(), 90)));
        resolvePage.clickFraudAiDecisionMismatchConfirmButton();
        resolvePage.waitForSuccessToastVisibility();
    }

    @Test
    @AllureId("2408")
    @DisplayName("Verify no mismatch for fraud uncertain on resolution")
    void resolveAiDecisionsTest5() throws Exception {
        // Alert with rule that is trusted
        PaymentAlertMessageV2 alert = generateTradingAlertWithDecision(client.getUcid(), "withdrawal", null);
        alert.rule.name = "Withdrawal review";
        alert.account = client.getTradingAccount().toString();
        alert.serverId = client.getServerId().toString();
        kafka.produceMessages(alert.id.toString(), KAFKA_TOPIC_ALERTS, objectMapper.writeValueAsString(alert));

        AiDecisions aiDecisions = generateAiDecisionsByClientAlertIdClassifications(
                client,
                alert.id,
                List.of(
                        AiDecisions.Classification.builder()
                                .category(CONFIRMED.getDisplayName())
                                .fraud(CPA_ABUSE.getName())
                                .confidenceScore(50)
                                .build(),
                        AiDecisions.Classification.builder()
                                .category(POTENTIAL.getDisplayName())
                                .fraud("Uncertain")
                                .confidenceScore(60)
                                .build()));
        kafka.produceMessages(
                aiDecisions.getMessageId().toString(),
                KAFKA_TOPIC_AI_DECISIONS,
                objectMapper.writeValueAsString(aiDecisions));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsTradingOpsUser();
        investigationPage.navigateToClient(client.getUcid());
        resolvePage.openResolveSuspicious();
        resolvePage.addFraud(CPA_ABUSE, POTENTIAL);
        resolvePage.fillCommentAndApply(RESOLVE_COMMENT);
        resolvePage.waitForSuccessToastVisibility();
    }

    @Test
    @AllureId("2409")
    @DisplayName("Verify no mismatch for decision normal on resolution")
    void resolveAiDecisionsTest6() throws Exception {
        // Alert with rule that is trusted
        PaymentAlertMessageV2 alert = generateTradingAlertWithDecision(client.getUcid(), "withdrawal", null);
        alert.rule.name = "Withdrawal review";
        alert.account = client.getTradingAccount().toString();
        alert.serverId = client.getServerId().toString();
        kafka.produceMessages(alert.id.toString(), KAFKA_TOPIC_ALERTS, objectMapper.writeValueAsString(alert));

        AiDecisions aiDecisions = generateAiDecisionsByClientAlertIdClassifications(
                client,
                alert.id,
                List.of(
                        AiDecisions.Classification.builder()
                                .category(CONFIRMED.getDisplayName())
                                .fraud(CPA_ABUSE.getName())
                                .confidenceScore(50)
                                .build(),
                        AiDecisions.Classification.builder()
                                .category("Normal")
                                .confidenceScore(60)
                                .build()));
        kafka.produceMessages(
                aiDecisions.getMessageId().toString(),
                KAFKA_TOPIC_AI_DECISIONS,
                objectMapper.writeValueAsString(aiDecisions));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsTradingOpsUser();
        investigationPage.navigateToClient(client.getUcid());
        resolvePage.openResolveSuspicious();
        resolvePage.fillCommentAndApply(RESOLVE_COMMENT);
        resolvePage.waitForSuccessToastVisibility();
    }

    @Test
    @AllureId("2410")
    @DisplayName("Verify no fraud mismatch for untrusted rule on resolution")
    void resolveAiDecisionsTest7() throws Exception {
        // Alert with rule that is not trusted
        PaymentAlertMessageV2 alert = generateTradingAlertWithDecision(client.getUcid(), "withdrawal", null);
        alert.rule.name = "Untrusted withdrawal review";
        alert.account = client.getTradingAccount().toString();
        alert.serverId = client.getServerId().toString();
        kafka.produceMessages(alert.id.toString(), KAFKA_TOPIC_ALERTS, objectMapper.writeValueAsString(alert));

        AiDecisions aiDecisions =
                generateAiDecisionsByClientAlertIdClassifications(client, alert.id, getGenericClassificationsList());
        kafka.produceMessages(
                aiDecisions.getMessageId().toString(),
                KAFKA_TOPIC_AI_DECISIONS,
                objectMapper.writeValueAsString(aiDecisions));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsTradingOpsUser();
        investigationPage.navigateToClient(client.getUcid());
        resolvePage.openResolveSuspicious();
        resolvePage.addFraud(MARKET_MANIPULATION, POTENTIAL);
        resolvePage.fillCommentAndApply(RESOLVE_COMMENT);
        resolvePage.waitForSuccessToastVisibility();
    }

    private static void sendAlertWithAiDecisions() throws JsonProcessingException {
        PaymentAlertMessageV2 alert = generateTradingAlertWithDecision(client.getUcid(), "withdrawal", null);
        alert.rule.name = "Withdrawal review";
        alert.account = client.getTradingAccount().toString();
        alert.serverId = client.getServerId().toString();
        kafka.produceMessages(alert.id.toString(), KAFKA_TOPIC_ALERTS, objectMapper.writeValueAsString(alert));

        AiDecisions aiDecisions =
                generateAiDecisionsByClientAlertIdClassifications(client, alert.id, getGenericClassificationsList());
        kafka.produceMessages(
                aiDecisions.getMessageId().toString(),
                KAFKA_TOPIC_AI_DECISIONS,
                objectMapper.writeValueAsString(aiDecisions));
    }

    private static List<AiDecisions.Classification> getGenericClassificationsList() {
        return List.of(
                AiDecisions.Classification.builder()
                        .category(CONFIRMED.getDisplayName())
                        .fraud(CPA_ABUSE.getName())
                        .confidenceScore(90)
                        .build(),
                AiDecisions.Classification.builder()
                        .category(POTENTIAL.getDisplayName())
                        .fraud(BONUS_ABUSE.getName())
                        .confidenceScore(90)
                        .build(),
                AiDecisions.Classification.builder()
                        .category(CONFIRMED.getDisplayName())
                        .fraud(MARKET_MANIPULATION.getName())
                        .confidenceScore(80)
                        .build());
    }
}
