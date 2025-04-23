package tests.vindex_backoffice_ui_tests;

import business_objects.db.backoffice_db.clients_fraud_types.ClientsFraudTypes;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import helpers.data.ClientHelper;
import helpers.data.enums.Brand;
import helpers.data.enums.FraudType;
import helpers.data.enums.Regulator;
import helpers.database.DbName;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBaseWeb;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateStaticUserByClient;
import static helpers.database.CleanTableHelper.*;
import static helpers.database.BoHelper.*;
import static helpers.database.DbHelper.getObjectsFromDB;
import static helpers.database.DbHelper.insertObjectToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static utils.Constants.*;

public class AddFraudToNonSuspiciousTest extends TestBaseWeb {


    static ClientHelper innocentClient = new ClientHelper(191_901, Brand.INFINOX, Regulator.FCA);

    @BeforeAll
    public static void setup() throws ReflectiveOperationException, SQLException {
        CrmTbUserObject clientDb = generateStaticUserByClient(innocentClient);
        clientDb.firstName = "Zero";
        insertObjectToDb(CRM_USER_TABLE_NAME, clientDb);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Report fraud on non violator - Hedging")
    @AllureId("875")
    public void reportFraudTestHedging() throws Exception {
        deleteUserBO(innocentClient.getUcid());
        cleanUserAudit(innocentClient.getUcid());
        resolvePage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        FraudType fraudType = FraudType.HEDGING;
        resolvePage.reportAddFraud("test" + timestamp, fraudType.getDisplayName());
        checkUserFraudBo(innocentClient.getUcid(), fraudType.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Report fraud on non violator - Latency arbitrage")
    @AllureId("876")
    public void reportFraudTestLatencyArbitrage() throws Exception {
        deleteUserBO(innocentClient.getUcid());
        cleanUserAudit(innocentClient.getUcid());
        resolvePage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        FraudType fraudType = FraudType.LATENCY_ARBITRAGE;
        resolvePage.reportAddFraud("test" + timestamp, fraudType.getDisplayName());
        checkUserFraudBo(innocentClient.getUcid(), fraudType.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Report fraud on non violator - Market manipulation")
    @AllureId("877")
    public void reportFraudTestMarketManipulation() throws Exception {
        deleteUserBO(innocentClient.getUcid());
        cleanUserAudit(innocentClient.getUcid());
        resolvePage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        FraudType fraudType = FraudType.MARKET_MANIPULATION;
        resolvePage.reportAddFraud("test" + timestamp, fraudType.getDisplayName());
        checkUserFraudBo(innocentClient.getUcid(), fraudType.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Report fraud on non violator - Pricing errors")
    @AllureId("878")
    public void reportFraudTestPricingErrors() throws Exception {
        deleteUserBO(innocentClient.getUcid());
        cleanUserAudit(innocentClient.getUcid());
        resolvePage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        FraudType fraudType = FraudType.PRICING_ERROR;
        resolvePage.reportAddFraud("test" + timestamp, fraudType.getDisplayName());
        checkUserFraudBo(innocentClient.getUcid(), fraudType.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Report fraud on non violator - Gap trading")
    @AllureId("879")
    public void reportFraudTestGapTrading() throws Exception {
        deleteUserBO(innocentClient.getUcid());
        cleanUserAudit(innocentClient.getUcid());
        resolvePage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        FraudType fraudType = FraudType.GAP_TRADING;
        resolvePage.reportAddFraud("test" + timestamp, fraudType.getDisplayName());
        checkUserFraudBo(innocentClient.getUcid(), fraudType.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Report fraud on non violator - Swap arbitrage")
    @AllureId("880")
    public void reportFraudTestSwapArbitrage() throws Exception {
        deleteUserBO(innocentClient.getUcid());
        cleanUserAudit(innocentClient.getUcid());
        resolvePage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        FraudType fraudType = FraudType.SWAP_ARBITRAGE;
        resolvePage.reportAddFraud("test" + timestamp, fraudType.getDisplayName());
        checkUserFraudBo(innocentClient.getUcid(), fraudType.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Report fraud on non violator - RAF abuse")
    @AllureId("881")
    public void reportFraudTestRafAbuse() throws Exception {
        deleteUserBO(innocentClient.getUcid());
        cleanUserAudit(innocentClient.getUcid());
        resolvePage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        FraudType fraudType = FraudType.RAF_ABUSE;
        resolvePage.reportAddFraud("test" + timestamp, fraudType.getDisplayName());
        checkUserFraudBo(innocentClient.getUcid(), fraudType.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Report fraud on non violator - Rebate churning")
    @AllureId("882")
    public void reportFraudTestRebateChurning() throws Exception {
        deleteUserBO(innocentClient.getUcid());
        cleanUserAudit(innocentClient.getUcid());
        resolvePage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        FraudType fraudType = FraudType.REBATE_CHURNING;
        resolvePage.reportAddFraud("test" + timestamp, fraudType.getDisplayName());
        checkUserFraudBo(innocentClient.getUcid(), fraudType.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Report fraud on non violator - Loss voucher abuse")
    @AllureId("883")
    public void reportFraudTestLossVoucherAbuse() throws Exception {
        deleteUserBO(innocentClient.getUcid());
        cleanUserAudit(innocentClient.getUcid());
        resolvePage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        FraudType fraudType = FraudType.LOSS_VOUCHER_ABUSE;
        resolvePage.reportAddFraud("test" + timestamp, fraudType.getDisplayName());
        checkUserFraudBo(innocentClient.getUcid(), fraudType.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Report fraud on non violator - NBP abuse")
    @AllureId("884")
    public void reportFraudTestNbpAbuse() throws Exception {
        deleteUserBO(innocentClient.getUcid());
        cleanUserAudit(innocentClient.getUcid());
        resolvePage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        FraudType fraudType = FraudType.NBP_ABUSE;
        resolvePage.reportAddFraud("test" + timestamp, fraudType.getDisplayName());
        checkUserFraudBo(innocentClient.getUcid(), fraudType.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Report fraud on non violator - TLS abuse")
    @AllureId("885")
    public void reportFraudTestTlsAbuse() throws Exception {
        deleteUserBO(innocentClient.getUcid());
        cleanUserAudit(innocentClient.getUcid());
        resolvePage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        FraudType fraudType = FraudType.TLS_ABUSE;
        resolvePage.reportAddFraud("test" + timestamp, fraudType.getDisplayName());
        checkUserFraudBo(innocentClient.getUcid(), fraudType.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Report fraud on non violator - Potential abuse")
    @AllureId("886")
    public void reportFraudTestPotentialAbuse() throws Exception {
        deleteUserBO(innocentClient.getUcid());
        cleanUserAudit(innocentClient.getUcid());
        resolvePage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        FraudType fraudType = FraudType.POTENTIAL_ABUSE;
        resolvePage.reportAddFraud("test" + timestamp, fraudType.getDisplayName());
        checkUserFraudBo(innocentClient.getUcid(), fraudType.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Report fraud on non violator - Bonus abuse")
    @AllureId("887")
    public void reportFraudTestBonusAbuse() throws Exception {
        deleteUserBO(innocentClient.getUcid());
        cleanUserAudit(innocentClient.getUcid());
        resolvePage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        FraudType fraudType = FraudType.BONUS_ABUSE;
        resolvePage.reportAddFraud("test" + timestamp, fraudType.getDisplayName());
        checkUserFraudBo(innocentClient.getUcid(), fraudType.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Report fraud on non violator - Loophole abuse")
    @AllureId("888")
    public void reportFraudTestLoopholeAbuse() throws Exception {
        deleteUserBO(innocentClient.getUcid());
        cleanUserAudit(innocentClient.getUcid());
        resolvePage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        FraudType fraudType = FraudType.LOOPHOLE_ABUSE;
        resolvePage.reportAddFraud("test" + timestamp, fraudType.getDisplayName());
        checkUserFraudBo(innocentClient.getUcid(), fraudType.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Report fraud on non violator - HFT abuse")
    @AllureId("889")
    public void reportFraudTestHftAbuse() throws Exception {
        deleteUserBO(innocentClient.getUcid());
        cleanUserAudit(innocentClient.getUcid());
        resolvePage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        FraudType fraudType = FraudType.HFT_ABUSE;
        resolvePage.reportAddFraud("test" + timestamp, fraudType.getDisplayName());
        checkUserFraudBo(innocentClient.getUcid(), fraudType.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Report fraud on non violator - News trader")
    @AllureId("890")
    public void reportFraudTestNewsTrader() throws Exception {
        deleteUserBO(innocentClient.getUcid());
        cleanUserAudit(innocentClient.getUcid());
        resolvePage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        FraudType fraudType = FraudType.NEWS_TRADER;
        resolvePage.reportAddFraud("test" + timestamp, fraudType.getDisplayName());
        checkUserFraudBo(innocentClient.getUcid(), fraudType.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Report fraud on non violator - Anomalous profit")
    @AllureId("891")
    public void reportFraudTestAnomalousProfit() throws Exception {
        deleteUserBO(innocentClient.getUcid());
        cleanUserAudit(innocentClient.getUcid());
        resolvePage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        FraudType fraudType = FraudType.ANOMALOUS_PROFIT;
        resolvePage.reportAddFraud("test" + timestamp, fraudType.getDisplayName());
        checkUserFraudBo(innocentClient.getUcid(), fraudType.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Report fraud on non violator - CPA abuse")
    @AllureId("892")
    public void reportFraudTestCpaAbuse() throws Exception {
        deleteUserBO(innocentClient.getUcid());
        cleanUserAudit(innocentClient.getUcid());
        resolvePage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        FraudType fraudType = FraudType.CPA_ABUSE;
        resolvePage.reportAddFraud("test" + timestamp, fraudType.getDisplayName());
        checkUserFraudBo(innocentClient.getUcid(), fraudType.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Report fraud on user without alert but with a previously confirmed fraud")
    @AllureId("893")
    public void reportFraudNoAlertPreviousFraudTest() throws Exception {
        FraudType fraud1 = FraudType.getRandomFraudType();
        FraudType fraud2;
        do {
            fraud2 = FraudType.getRandomFraudType();
        } while (fraud1.equals(fraud2));
        deleteUserBO(innocentClient.getUcid());
        cleanUserAudit(innocentClient.getUcid());
        resolvePage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        resolvePage.reportAddFraud("test1" + timestamp, fraud1.getDisplayName());
        resolvePage.openReportFraudForm();
        resolvePage.checkPreviousConfirmedFraudDisplayed(fraud1.getDisplayName());
        resolvePage.reportAddFraud("test2" + timestamp, fraud2.getDisplayName());
        checkUserFraudBo(innocentClient.getUcid(), fraud2.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Verify date label for fraud type set today")
    @AllureId("1062")
    public void previousFraudTestToday() throws Exception {
        deleteUserBO(innocentClient.getUcid());
        cleanUserAudit(innocentClient.getUcid());
        FraudType fraudType = FraudType.HEDGING;
        insertObjectToDb(DbName.BO, BO_CLIENTS_FRAUD_TYPES_TABLE_NAME, new ClientsFraudTypes(innocentClient.getUcid(), (long) fraudType.getFraudTypeId(), false, Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC).minusHours(4))));
        resolvePage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        assertThat("Verify time label for the fraud type", resolvePage.getFraudTimeByName(fraudType.getDisplayName()), is("Today"));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Verify date label for fraud type set yesterday")
    @AllureId("1063")
    public void previousFraudTestYesterday() throws Exception {
        deleteUserBO(innocentClient.getUcid());
        cleanUserAudit(innocentClient.getUcid());
        FraudType fraudType = FraudType.HEDGING;
        insertObjectToDb(DbName.BO, BO_CLIENTS_FRAUD_TYPES_TABLE_NAME, new ClientsFraudTypes(innocentClient.getUcid(), (long) fraudType.getFraudTypeId(), false, Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC).minusDays(1).minusHours(2))));
        resolvePage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        assertThat("Verify time label for the fraud type", resolvePage.getFraudTimeByName(fraudType.getDisplayName()), is("Yesterday"));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Verify date label for fraud type set by days")
    @AllureId("1064")
    public void previousFraudTestByDays() throws Exception {
        deleteUserBO(innocentClient.getUcid());
        cleanUserAudit(innocentClient.getUcid());
        FraudType fraudType = FraudType.HEDGING;
        insertObjectToDb(DbName.BO, BO_CLIENTS_FRAUD_TYPES_TABLE_NAME, new ClientsFraudTypes(innocentClient.getUcid(), (long) fraudType.getFraudTypeId(), false, Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC).minusDays(2))));
        resolvePage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        assertThat("Verify time label for the fraud type", resolvePage.getFraudTimeByName(fraudType.getDisplayName()), is("2 days ago"));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Verify date label for fraud type set by months")
    @AllureId("1065")
    public void previousFraudTestByMonths() throws Exception {
        deleteUserBO(innocentClient.getUcid());
        cleanUserAudit(innocentClient.getUcid());
        FraudType fraudType = FraudType.HEDGING;
        insertObjectToDb(DbName.BO, BO_CLIENTS_FRAUD_TYPES_TABLE_NAME, new ClientsFraudTypes(innocentClient.getUcid(), (long) fraudType.getFraudTypeId(), false, Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC).minusMonths(1).minusHours(4))));
        resolvePage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        assertThat("Verify time label for the fraud type", resolvePage.getFraudTimeByName(fraudType.getDisplayName()), is("1 month ago"));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Verify date label for fraud type set by years")
    @AllureId("1066")
    public void previousFraudTestByYears() throws Exception {
        deleteUserBO(innocentClient.getUcid());
        cleanUserAudit(innocentClient.getUcid());
        FraudType fraudType = FraudType.HEDGING;
        insertObjectToDb(DbName.BO, BO_CLIENTS_FRAUD_TYPES_TABLE_NAME, new ClientsFraudTypes(innocentClient.getUcid(), (long) fraudType.getFraudTypeId(), false, Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC).minusMonths(12).minusHours(4))));
        resolvePage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        assertThat("Verify time label for the fraud type", resolvePage.getFraudTimeByName(fraudType.getDisplayName()), is("1 year ago"));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Delete previously confirmed fraud")
    @AllureId("1067")
    public void previousFraudTestDeleteFraud() throws Exception {
        deleteUserBO(innocentClient.getUcid());
        cleanUserAudit(innocentClient.getUcid());
        FraudType fraudType = FraudType.HEDGING;
        insertObjectToDb(DbName.BO, BO_CLIENTS_FRAUD_TYPES_TABLE_NAME, new ClientsFraudTypes(innocentClient.getUcid(), (long) fraudType.getFraudTypeId(), false, Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC).minusHours(4))));
        resolvePage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        resolvePage.deleteFraudByName(fraudType.getDisplayName());
        List<ClientsFraudTypes> clientsFraudTypes = getObjectsFromDB(DbName.BO, BO_CLIENTS_FRAUD_TYPES_TABLE_NAME, String.format("client_ucid = '%s'", innocentClient.getUcid()), ClientsFraudTypes.class);
        assertThat("Verify there is only 1 fraud type", clientsFraudTypes.size(), is(1));
        assertThat("Verify the fraud type is deleted", clientsFraudTypes.getFirst().isDeleted, is(true));
    }

}
