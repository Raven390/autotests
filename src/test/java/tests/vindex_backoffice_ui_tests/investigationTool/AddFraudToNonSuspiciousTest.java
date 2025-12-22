package tests.vindex_backoffice_ui_tests.investigationTool;

import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateStaticUserByClient;
import static helpers.api.AbuseRegistryHelper.addFraudsForClient;
import static helpers.api.MitigationHelper.getClientRestrictionListFromDb;
import static helpers.data.enums.FraudType.*;
import static helpers.data.enums.FraudTypeStatus.CLEANED;
import static helpers.data.enums.FraudTypeStatus.CONFIRMED;
import static helpers.data.enums.Restriction.*;
import static helpers.database.BoHelper.*;
import static helpers.database.CleanTableHelper.*;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static utils.Constants.*;

import business_objects.db.abuse_registry_db.AbuserFraudType;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import helpers.data.ClientHelper;
import helpers.data.enums.Brand;
import helpers.data.enums.FraudType;
import helpers.data.enums.Regulator;
import helpers.database.ArHelper;
import helpers.database.DbName;
import io.qameta.allure.AllureId;
import io.qameta.allure.Muted;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

@Disabled
@Muted
@Tag(TAG_MANUAL)
public class AddFraudToNonSuspiciousTest extends TestBaseWeb {

    static ClientHelper innocentClient;

    static {
        innocentClient = ClientHelper.builder()
                .userId(191_901)
                .brand(Brand.VANTAGE)
                .regulator(Regulator.FCA)
                .build();
    }

    private static final String WHERE_STATEMENT = "ucid = '%s'";
    private static final String UPDATE_FRAUD_TIME_QUERY =
            "UPDATE %s SET updated_at = '%s', created_at = '%s' WHERE ucid = '%s' AND fraud_type_code = '%s'";

    @BeforeAll
    static void setup() {
        CrmTbUserObject clientDb = generateStaticUserByClient(innocentClient);
        clientDb.firstName = "Zero";
        insertObjectToDb(CRM_USER_TABLE_NAME, clientDb);
    }

    @BeforeEach
    void cleanUser() throws Exception {
        deleteUserBO(innocentClient.getUcid());
        cleanUserAudit(innocentClient.getUcid());
        cleanUserRestrictionGeneral(innocentClient.getUcid());
        cleanUserRestrictionTrading(innocentClient.getUcid());
        ArHelper.deleteUserFromAbuseRegistry(innocentClient.getUcid());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Report fraud on non violator - Hedging")
    @AllureId("875")
    void reportFraudTestHedging() throws Exception {
        resolvePage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        FraudType fraudType = HEDGING;
        resolvePage.reportAddFraud("test" + timestamp, fraudType, CONFIRMED);
        List<AbuserFraudType> frauds = getObjectsFromDB(
                DbName.POSTGRES,
                AR_ABUSER_FRAUD_TYPE_TABLE_NAME,
                String.format(WHERE_STATEMENT, innocentClient.getUcid()),
                AbuserFraudType.class);
        assertThat("Verify there is only 1 fraud", frauds.size(), is(1));
        assertThat("Verify fraud type", frauds.getFirst().getFraudTypeCode(), is(fraudType.getCode()));
        assertThat("Verify status", frauds.getFirst().getStatus(), is(CONFIRMED.getStatus()));
        assertThat(
                "Verify preset restrictions",
                getClientRestrictionListFromDb(innocentClient),
                containsInAnyOrder(ACCOUNT_CREATION, CREDIT_AND_BONUS, DEPOSITS, INTERNAL_TRANSFER, WITHDRAWALS));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Report fraud on non violator - Latency arbitrage")
    @AllureId("876")
    void reportFraudTestLatencyArbitrage() throws Exception {
        resolvePage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        FraudType fraudType = LATENCY_ARBITRAGE;
        resolvePage.reportAddFraud("test" + timestamp, fraudType, CONFIRMED);
        List<AbuserFraudType> frauds = getObjectsFromDB(
                DbName.POSTGRES,
                AR_ABUSER_FRAUD_TYPE_TABLE_NAME,
                String.format(WHERE_STATEMENT, innocentClient.getUcid()),
                AbuserFraudType.class);
        assertThat("Verify there is only 1 fraud", frauds.size(), is(1));
        assertThat("Verify fraud type", frauds.getFirst().getFraudTypeCode(), is(fraudType.getCode()));
        assertThat("Verify status", frauds.getFirst().getStatus(), is(CONFIRMED.getStatus()));
        assertThat(
                "Verify preset restrictions",
                getClientRestrictionListFromDb(innocentClient),
                containsInAnyOrder(ACCOUNT_CREATION, CREDIT_AND_BONUS, DEPOSITS, INTERNAL_TRANSFER, WITHDRAWALS));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Report fraud on non violator - CPA abuse")
    @AllureId("892")
    void reportFraudTestCpaAbuse() throws Exception {
        resolvePage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        FraudType fraudType = CPA_ABUSE;
        resolvePage.reportAddFraud("test" + timestamp, fraudType, CONFIRMED);
        List<AbuserFraudType> frauds = getObjectsFromDB(
                DbName.POSTGRES,
                AR_ABUSER_FRAUD_TYPE_TABLE_NAME,
                String.format(WHERE_STATEMENT, innocentClient.getUcid()),
                AbuserFraudType.class);
        assertThat("Verify there is only 1 fraud", frauds.size(), is(1));
        assertThat("Verify fraud type", frauds.getFirst().getFraudTypeCode(), is(fraudType.getCode()));
        assertThat("Verify status", frauds.getFirst().getStatus(), is(CONFIRMED.getStatus()));
        assertThat(
                "Verify preset restrictions",
                getClientRestrictionListFromDb(innocentClient),
                containsInAnyOrder(ACCOUNT_CREATION, CREDIT_AND_BONUS, DEPOSITS));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Report fraud on non violator - Bonus abuse")
    @AllureId("887")
    void reportFraudTestBonusAbuse() throws Exception {
        resolvePage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        FraudType fraudType = BONUS_ABUSE;
        resolvePage.reportAddFraud("test" + timestamp, fraudType, CONFIRMED);
        List<AbuserFraudType> frauds = getObjectsFromDB(
                DbName.POSTGRES,
                AR_ABUSER_FRAUD_TYPE_TABLE_NAME,
                String.format(WHERE_STATEMENT, innocentClient.getUcid()),
                AbuserFraudType.class);
        assertThat("Verify there is only 1 fraud", frauds.size(), is(1));
        assertThat("Verify fraud type", frauds.getFirst().getFraudTypeCode(), is(fraudType.getCode()));
        assertThat("Verify status", frauds.getFirst().getStatus(), is(CONFIRMED.getStatus()));
        assertThat(
                "Verify preset restrictions",
                getClientRestrictionListFromDb(innocentClient),
                containsInAnyOrder(ACCOUNT_CREATION, CREDIT_AND_BONUS, DEPOSITS, INTERNAL_TRANSFER, WITHDRAWALS));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Report fraud on non violator - Loss voucher abuse")
    @AllureId("883")
    void reportFraudTestLossVoucherAbuse() throws Exception {
        resolvePage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        FraudType fraudType = LOSS_VOUCHER_ABUSE;
        resolvePage.reportAddFraud("test" + timestamp, fraudType, CONFIRMED);
        List<AbuserFraudType> frauds = getObjectsFromDB(
                DbName.POSTGRES,
                AR_ABUSER_FRAUD_TYPE_TABLE_NAME,
                String.format(WHERE_STATEMENT, innocentClient.getUcid()),
                AbuserFraudType.class);
        assertThat("Verify there is only 1 fraud", frauds.size(), is(1));
        assertThat("Verify fraud type", frauds.getFirst().getFraudTypeCode(), is(fraudType.getCode()));
        assertThat("Verify status", frauds.getFirst().getStatus(), is(CONFIRMED.getStatus()));
        assertThat(
                "Verify preset restrictions",
                getClientRestrictionListFromDb(innocentClient),
                containsInAnyOrder(ACCOUNT_CREATION, CREDIT_AND_BONUS, DEPOSITS, INTERNAL_TRANSFER, WITHDRAWALS));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Report fraud on non violator - NBP abuse")
    @AllureId("884")
    void reportFraudTestNbpAbuse() throws Exception {
        resolvePage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        FraudType fraudType = NBP_ABUSE;
        resolvePage.reportAddFraud("test" + timestamp, fraudType, CONFIRMED);
        List<AbuserFraudType> frauds = getObjectsFromDB(
                DbName.POSTGRES,
                AR_ABUSER_FRAUD_TYPE_TABLE_NAME,
                String.format(WHERE_STATEMENT, innocentClient.getUcid()),
                AbuserFraudType.class);
        assertThat("Verify there is only 1 fraud", frauds.size(), is(1));
        assertThat("Verify fraud type", frauds.getFirst().getFraudTypeCode(), is(fraudType.getCode()));
        assertThat("Verify status", frauds.getFirst().getStatus(), is(CONFIRMED.getStatus()));
        assertThat(
                "Verify preset restrictions",
                getClientRestrictionListFromDb(innocentClient),
                containsInAnyOrder(CREDIT_AND_BONUS, MANUAL_WITHDRAWAL_REVIEW));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Report fraud on non violator - Gap trading")
    @AllureId("879")
    void reportFraudTestGapTrading() throws Exception {
        resolvePage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        FraudType fraudType = GAP_TRADING;
        resolvePage.reportAddFraud("test" + timestamp, fraudType, CONFIRMED);
        List<AbuserFraudType> frauds = getObjectsFromDB(
                DbName.POSTGRES,
                AR_ABUSER_FRAUD_TYPE_TABLE_NAME,
                String.format(WHERE_STATEMENT, innocentClient.getUcid()),
                AbuserFraudType.class);
        assertThat("Verify there is only 1 fraud", frauds.size(), is(1));
        assertThat("Verify fraud type", frauds.getFirst().getFraudTypeCode(), is(fraudType.getCode()));
        assertThat("Verify status", frauds.getFirst().getStatus(), is(CONFIRMED.getStatus()));
        assertThat(
                "Verify preset restrictions",
                getClientRestrictionListFromDb(innocentClient),
                containsInAnyOrder(ACCOUNT_CREATION, CREDIT_AND_BONUS, DEPOSITS, INTERNAL_TRANSFER));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Report fraud on non violator - Rebate churning")
    @AllureId("882")
    void reportFraudTestRebateChurning() throws Exception {
        resolvePage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        FraudType fraudType = REBATE_CHURNING;
        resolvePage.reportAddFraud("test" + timestamp, fraudType, CONFIRMED);
        List<AbuserFraudType> frauds = getObjectsFromDB(
                DbName.POSTGRES,
                AR_ABUSER_FRAUD_TYPE_TABLE_NAME,
                String.format(WHERE_STATEMENT, innocentClient.getUcid()),
                AbuserFraudType.class);
        assertThat("Verify there is only 1 fraud", frauds.size(), is(1));
        assertThat("Verify fraud type", frauds.getFirst().getFraudTypeCode(), is(fraudType.getCode()));
        assertThat("Verify status", frauds.getFirst().getStatus(), is(CONFIRMED.getStatus()));
        assertThat(
                "Verify preset restrictions",
                getClientRestrictionListFromDb(innocentClient),
                containsInAnyOrder(ACCOUNT_CREATION, CREDIT_AND_BONUS, DEPOSITS, INTERNAL_TRANSFER, WITHDRAWALS));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Report fraud on non violator - TLS abuse")
    @AllureId("885")
    void reportFraudTestTlsAbuse() throws Exception {
        resolvePage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        FraudType fraudType = TLS_ABUSE;
        resolvePage.reportAddFraud("test" + timestamp, fraudType, CONFIRMED);
        List<AbuserFraudType> frauds = getObjectsFromDB(
                DbName.POSTGRES,
                AR_ABUSER_FRAUD_TYPE_TABLE_NAME,
                String.format(WHERE_STATEMENT, innocentClient.getUcid()),
                AbuserFraudType.class);
        assertThat("Verify there is only 1 fraud", frauds.size(), is(1));
        assertThat("Verify fraud type", frauds.getFirst().getFraudTypeCode(), is(fraudType.getCode()));
        assertThat("Verify status", frauds.getFirst().getStatus(), is(CONFIRMED.getStatus()));
        assertThat(
                "Verify preset restrictions",
                getClientRestrictionListFromDb(innocentClient),
                containsInAnyOrder(ACCOUNT_CREATION, CREDIT_AND_BONUS, DEPOSITS, INTERNAL_TRANSFER, WITHDRAWALS));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Report fraud on non violator - Loophole abuse")
    @AllureId("888")
    void reportFraudTestLoopholeAbuse() throws Exception {
        resolvePage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        FraudType fraudType = LOOPHOLE_ABUSE;
        resolvePage.reportAddFraud("test" + timestamp, fraudType, CONFIRMED);
        List<AbuserFraudType> frauds = getObjectsFromDB(
                DbName.POSTGRES,
                AR_ABUSER_FRAUD_TYPE_TABLE_NAME,
                String.format(WHERE_STATEMENT, innocentClient.getUcid()),
                AbuserFraudType.class);
        assertThat("Verify there is only 1 fraud", frauds.size(), is(1));
        assertThat("Verify fraud type", frauds.getFirst().getFraudTypeCode(), is(fraudType.getCode()));
        assertThat("Verify status", frauds.getFirst().getStatus(), is(CONFIRMED.getStatus()));
        assertThat(
                "Verify preset restrictions",
                getClientRestrictionListFromDb(innocentClient),
                containsInAnyOrder(ACCOUNT_CREATION, CREDIT_AND_BONUS, DEPOSITS, INTERNAL_TRANSFER, WITHDRAWALS));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Report fraud on non violator - News trader")
    @AllureId("890")
    void reportFraudTestNewsTrader() throws Exception {
        resolvePage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        FraudType fraudType = NEWS_TRADER;
        resolvePage.reportAddFraud("test" + timestamp, fraudType, CONFIRMED);
        List<AbuserFraudType> frauds = getObjectsFromDB(
                DbName.POSTGRES,
                AR_ABUSER_FRAUD_TYPE_TABLE_NAME,
                String.format(WHERE_STATEMENT, innocentClient.getUcid()),
                AbuserFraudType.class);
        assertThat("Verify there is only 1 fraud", frauds.size(), is(1));
        assertThat("Verify fraud type", frauds.getFirst().getFraudTypeCode(), is(fraudType.getCode()));
        assertThat("Verify status", frauds.getFirst().getStatus(), is(CONFIRMED.getStatus()));
        assertThat(
                "Verify preset restrictions",
                getClientRestrictionListFromDb(innocentClient),
                containsInAnyOrder(CREDIT_AND_BONUS, MANUAL_WITHDRAWAL_REVIEW));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Report fraud on non violator - Chargeback")
    @AllureId("890")
    void reportFraudTestChargeback() throws Exception {
        resolvePage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        FraudType fraudType = CHARGEBACK;
        resolvePage.reportAddFraud("test" + timestamp, fraudType, CONFIRMED);
        List<AbuserFraudType> frauds = getObjectsFromDB(
                DbName.POSTGRES,
                AR_ABUSER_FRAUD_TYPE_TABLE_NAME,
                String.format(WHERE_STATEMENT, innocentClient.getUcid()),
                AbuserFraudType.class);
        assertThat("Verify there is only 1 fraud", frauds.size(), is(1));
        assertThat("Verify fraud type", frauds.getFirst().getFraudTypeCode(), is(fraudType.getCode()));
        assertThat("Verify status", frauds.getFirst().getStatus(), is(CONFIRMED.getStatus()));
        assertThat(
                "Verify preset restrictions",
                getClientRestrictionListFromDb(innocentClient),
                containsInAnyOrder(ACCOUNT_CREATION, CREDIT_AND_BONUS, DEPOSITS));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Report fraud on non violator - Market manipulation")
    @AllureId("877")
    void reportFraudTestMarketManipulation() throws Exception {
        resolvePage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        FraudType fraudType = MARKET_MANIPULATION;
        resolvePage.reportAddFraud("test" + timestamp, fraudType, CONFIRMED);
        List<AbuserFraudType> frauds = getObjectsFromDB(
                DbName.POSTGRES,
                AR_ABUSER_FRAUD_TYPE_TABLE_NAME,
                String.format(WHERE_STATEMENT, innocentClient.getUcid()),
                AbuserFraudType.class);
        assertThat("Verify there is only 1 fraud", frauds.size(), is(1));
        assertThat("Verify fraud type", frauds.getFirst().getFraudTypeCode(), is(fraudType.getCode()));
        assertThat("Verify status", frauds.getFirst().getStatus(), is(CONFIRMED.getStatus()));
        assertThat(
                "Verify preset restrictions",
                getClientRestrictionListFromDb(innocentClient),
                containsInAnyOrder(ACCOUNT_CREATION, CREDIT_AND_BONUS, DEPOSITS, INTERNAL_TRANSFER, WITHDRAWALS));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Report fraud on non violator - Swap arbitrage")
    @AllureId("880")
    void reportFraudTestSwapArbitrage() throws Exception {
        resolvePage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        FraudType fraudType = SWAP_ARBITRAGE;
        resolvePage.reportAddFraud("test" + timestamp, fraudType, CONFIRMED);
        List<AbuserFraudType> frauds = getObjectsFromDB(
                DbName.POSTGRES,
                AR_ABUSER_FRAUD_TYPE_TABLE_NAME,
                String.format(WHERE_STATEMENT, innocentClient.getUcid()),
                AbuserFraudType.class);
        assertThat("Verify there is only 1 fraud", frauds.size(), is(1));
        assertThat("Verify fraud type", frauds.getFirst().getFraudTypeCode(), is(fraudType.getCode()));
        assertThat("Verify status", frauds.getFirst().getStatus(), is(CONFIRMED.getStatus()));
        assertThat(
                "Verify preset restrictions",
                getClientRestrictionListFromDb(innocentClient),
                containsInAnyOrder(ACCOUNT_CREATION, MANUAL_WITHDRAWAL_REVIEW));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Report fraud on non violator - Pricing errors")
    @AllureId("878")
    void reportFraudTestPricingErrors() throws Exception {
        resolvePage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        FraudType fraudType = PRICING_ERROR;
        resolvePage.reportAddFraud("test" + timestamp, fraudType, CONFIRMED);
        List<AbuserFraudType> frauds = getObjectsFromDB(
                DbName.POSTGRES,
                AR_ABUSER_FRAUD_TYPE_TABLE_NAME,
                String.format(WHERE_STATEMENT, innocentClient.getUcid()),
                AbuserFraudType.class);
        assertThat("Verify there is only 1 fraud", frauds.size(), is(1));
        assertThat("Verify fraud type", frauds.getFirst().getFraudTypeCode(), is(fraudType.getCode()));
        assertThat("Verify status", frauds.getFirst().getStatus(), is(CONFIRMED.getStatus()));
        assertThat(
                "Verify preset restrictions",
                getClientRestrictionListFromDb(innocentClient),
                containsInAnyOrder(ACCOUNT_CREATION, CREDIT_AND_BONUS, DEPOSITS, INTERNAL_TRANSFER, WITHDRAWALS));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Report fraud on non violator - Slippage-free abuser")
    @AllureId("891")
    void reportFraudTestSlippageFreeAbuser() throws Exception {
        resolvePage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        FraudType fraudType = SLIPPAGE_FREE_ABUSE;
        resolvePage.reportAddFraud("test" + timestamp, fraudType, CONFIRMED);
        List<AbuserFraudType> frauds = getObjectsFromDB(
                DbName.POSTGRES,
                AR_ABUSER_FRAUD_TYPE_TABLE_NAME,
                String.format(WHERE_STATEMENT, innocentClient.getUcid()),
                AbuserFraudType.class);
        assertThat("Verify there is only 1 fraud", frauds.size(), is(1));
        assertThat("Verify fraud type", frauds.getFirst().getFraudTypeCode(), is(fraudType.getCode()));
        assertThat("Verify status", frauds.getFirst().getStatus(), is(CONFIRMED.getStatus()));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Report fraud on user without alert but with a previously confirmed fraud")
    @AllureId("893")
    void reportFraudNoAlertPreviousFraudTest() {
        FraudType fraud1 = FraudType.getRandomFraudType();
        FraudType fraud2;
        do {
            fraud2 = FraudType.getRandomFraudType();
        } while (fraud1.equals(fraud2));
        resolvePage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        resolvePage.reportAddFraud("test1" + timestamp, fraud1, CONFIRMED);
        resolvePage.openReportFraudForm();
        resolvePage.checkPreviousConfirmedFraudDisplayed(fraud1.getName());
        resolvePage.reportAddFraud("test2" + timestamp, fraud2, CONFIRMED);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Verify date label for fraud type set today")
    @AllureId("1062")
    void previousFraudTestToday() throws Exception {
        FraudType fraudType = FraudType.HEDGING;
        addFraudsForClient(innocentClient, List.of(fraudType), CONFIRMED);
        resolvePage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        assertThat(
                "Verify time label for the fraud type",
                resolvePage.getFraudTimeByName(fraudType.getName()),
                is("Today"));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Verify date label for fraud type set yesterday")
    @AllureId("1063")
    void previousFraudTestYesterday() throws Exception {
        FraudType fraudType = FraudType.HEDGING;
        addFraudsForClient(innocentClient, List.of(fraudType), CONFIRMED);
        executeQueryToDb(
                DbName.POSTGRES,
                String.format(
                        UPDATE_FRAUD_TIME_QUERY,
                        AR_ABUSER_FRAUD_TYPE_TABLE_NAME,
                        Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC).minusDays(1)),
                        Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC).minusDays(1)),
                        innocentClient.getUcid(),
                        fraudType.getCode()));
        resolvePage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        assertThat(
                "Verify time label for the fraud type",
                resolvePage.getFraudTimeByName(fraudType.getName()),
                is("Yesterday"));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Verify date label for fraud type set by days")
    @AllureId("1064")
    void previousFraudTestByDays() throws Exception {
        FraudType fraudType = FraudType.HEDGING;
        addFraudsForClient(innocentClient, List.of(fraudType), CONFIRMED);
        executeQueryToDb(
                DbName.POSTGRES,
                String.format(
                        UPDATE_FRAUD_TIME_QUERY,
                        AR_ABUSER_FRAUD_TYPE_TABLE_NAME,
                        Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC).minusDays(2)),
                        Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC).minusDays(1)),
                        innocentClient.getUcid(),
                        fraudType.getCode()));
        resolvePage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        assertThat(
                "Verify time label for the fraud type",
                resolvePage.getFraudTimeByName(fraudType.getName()),
                is("2 days ago"));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Verify date label for fraud type set by months")
    @AllureId("1065")
    void previousFraudTestByMonths() throws Exception {
        FraudType fraudType = FraudType.HEDGING;
        addFraudsForClient(innocentClient, List.of(fraudType), CONFIRMED);
        executeQueryToDb(
                DbName.POSTGRES,
                String.format(
                        UPDATE_FRAUD_TIME_QUERY,
                        AR_ABUSER_FRAUD_TYPE_TABLE_NAME,
                        Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC).minusDays(31)),
                        Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC).minusDays(31)),
                        innocentClient.getUcid(),
                        fraudType.getCode()));
        resolvePage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        assertThat(
                "Verify time label for the fraud type",
                resolvePage.getFraudTimeByName(fraudType.getName()),
                is("1 month ago"));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Verify date label for fraud type set by years")
    @AllureId("1066")
    void previousFraudTestByYears() throws Exception {
        FraudType fraudType = FraudType.HEDGING;
        addFraudsForClient(innocentClient, List.of(fraudType), CONFIRMED);
        executeQueryToDb(
                DbName.POSTGRES,
                String.format(
                        UPDATE_FRAUD_TIME_QUERY,
                        AR_ABUSER_FRAUD_TYPE_TABLE_NAME,
                        Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC).minusDays(366)),
                        Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC).minusDays(366)),
                        innocentClient.getUcid(),
                        fraudType.getCode()));
        resolvePage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        assertThat(
                "Verify time label for the fraud type",
                resolvePage.getFraudTimeByName(fraudType.getName()),
                is("1 year ago"));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Delete previously confirmed fraud")
    @AllureId("1067")
    void previousFraudTestDeleteFraud() throws Exception {
        deleteUserBO(innocentClient.getUcid());
        cleanUserAudit(innocentClient.getUcid());
        FraudType fraudType = FraudType.HEDGING;
        addFraudsForClient(innocentClient, List.of(fraudType), CONFIRMED);
        resolvePage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        resolvePage.deleteFraudByName(fraudType.getName());
        List<AbuserFraudType> frauds = getObjectsFromDB(
                DbName.POSTGRES,
                AR_ABUSER_FRAUD_TYPE_TABLE_NAME,
                String.format(WHERE_STATEMENT, innocentClient.getUcid()),
                AbuserFraudType.class);
        assertThat("Verify there is only 1 fraud type", frauds.size(), is(1));
        assertThat("Verify the fraud type is deleted", frauds.getFirst().getStatus(), is(CLEANED.getStatus()));
    }
}
