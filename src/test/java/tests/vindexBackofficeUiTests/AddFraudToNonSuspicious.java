package tests.vindexBackofficeUiTests;

import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;
import helpers.data.ClientHelper;
import helpers.data.enums.Brand;
import helpers.data.enums.FraudType;
import helpers.data.enums.Regulator;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBaseWeb;

import java.sql.SQLException;

import static businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObjectFactory.generateStaticUserByClient;
import static helpers.database.AuditHelper.cleanUserAudit;
import static helpers.database.BoHelper.checkUserFraudDb;
import static helpers.database.BoHelper.deleteUserBO;
import static helpers.database.DbHelper.insertObjectToDb;
import static utils.Constants.*;

public class AddFraudToNonSuspicious extends TestBaseWeb {


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
        activityTab.navigateToMain();
        keycloackPage.loginAsDevUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        FraudType fraudType = FraudType.HEDGING;
        resolvePage.reportAddFraud("test" + timestamp, fraudType.getDisplayName());
        checkUserFraudDb(innocentClient.getUcid(), fraudType.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Report fraud on non violator - Latency arbitrage")
    @AllureId("876")
    public void reportFraudTestLatencyArbitrage() throws Exception {
        deleteUserBO(innocentClient.getUcid());
        cleanUserAudit(innocentClient.getUcid());
        activityTab.navigateToMain();
        keycloackPage.loginAsDevUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        FraudType fraudType = FraudType.LATENCY_ARBITRAGE;
        resolvePage.reportAddFraud("test" + timestamp, fraudType.getDisplayName());
        checkUserFraudDb(innocentClient.getUcid(), fraudType.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Report fraud on non violator - Market manipulation")
    @AllureId("877")
    public void reportFraudTestMarketManipulation() throws Exception {
        deleteUserBO(innocentClient.getUcid());
        cleanUserAudit(innocentClient.getUcid());
        activityTab.navigateToMain();
        keycloackPage.loginAsDevUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        FraudType fraudType = FraudType.MARKET_MANIPULATION;
        resolvePage.reportAddFraud("test" + timestamp, fraudType.getDisplayName());
        checkUserFraudDb(innocentClient.getUcid(), fraudType.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Report fraud on non violator - Pricing errors")
    @AllureId("878")
    public void reportFraudTestPricingErrors() throws Exception {
        deleteUserBO(innocentClient.getUcid());
        cleanUserAudit(innocentClient.getUcid());
        activityTab.navigateToMain();
        keycloackPage.loginAsDevUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        FraudType fraudType = FraudType.PRICING_ERROR;
        resolvePage.reportAddFraud("test" + timestamp, fraudType.getDisplayName());
        checkUserFraudDb(innocentClient.getUcid(), fraudType.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Report fraud on non violator - Gap trading")
    @AllureId("879")
    public void reportFraudTestGapTrading() throws Exception {
        deleteUserBO(innocentClient.getUcid());
        cleanUserAudit(innocentClient.getUcid());
        activityTab.navigateToMain();
        keycloackPage.loginAsDevUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        FraudType fraudType = FraudType.GAP_TRADING;
        resolvePage.reportAddFraud("test" + timestamp, fraudType.getDisplayName());
        checkUserFraudDb(innocentClient.getUcid(), fraudType.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Report fraud on non violator - Swap arbitrage")
    @AllureId("880")
    public void reportFraudTestSwapArbitrage() throws Exception {
        deleteUserBO(innocentClient.getUcid());
        cleanUserAudit(innocentClient.getUcid());
        activityTab.navigateToMain();
        keycloackPage.loginAsDevUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        FraudType fraudType = FraudType.SWAP_ARBITRAGE;
        resolvePage.reportAddFraud("test" + timestamp, fraudType.getDisplayName());
        checkUserFraudDb(innocentClient.getUcid(), fraudType.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Report fraud on non violator - RAF abuse")
    @AllureId("881")
    public void reportFraudTestRafAbuse() throws Exception {
        deleteUserBO(innocentClient.getUcid());
        cleanUserAudit(innocentClient.getUcid());
        activityTab.navigateToMain();
        keycloackPage.loginAsDevUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        FraudType fraudType = FraudType.RAF_ABUSE;
        resolvePage.reportAddFraud("test" + timestamp, fraudType.getDisplayName());
        checkUserFraudDb(innocentClient.getUcid(), fraudType.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Report fraud on non violator - Rebate churning")
    @AllureId("882")
    public void reportFraudTestRebateChurning() throws Exception {
        deleteUserBO(innocentClient.getUcid());
        cleanUserAudit(innocentClient.getUcid());
        activityTab.navigateToMain();
        keycloackPage.loginAsDevUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        FraudType fraudType = FraudType.REBATE_CHURNING;
        resolvePage.reportAddFraud("test" + timestamp, fraudType.getDisplayName());
        checkUserFraudDb(innocentClient.getUcid(), fraudType.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Report fraud on non violator - Loss voucher abuse")
    @AllureId("883")
    public void reportFraudTestLossVoucherAbuse() throws Exception {
        deleteUserBO(innocentClient.getUcid());
        cleanUserAudit(innocentClient.getUcid());
        activityTab.navigateToMain();
        keycloackPage.loginAsDevUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        FraudType fraudType = FraudType.LOSS_VOUCHER_ABUSE;
        resolvePage.reportAddFraud("test" + timestamp, fraudType.getDisplayName());
        checkUserFraudDb(innocentClient.getUcid(), fraudType.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Report fraud on non violator - NBP abuse")
    @AllureId("884")
    public void reportFraudTestNbpAbuse() throws Exception {
        deleteUserBO(innocentClient.getUcid());
        cleanUserAudit(innocentClient.getUcid());
        activityTab.navigateToMain();
        keycloackPage.loginAsDevUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        FraudType fraudType = FraudType.NBP_ABUSE;
        resolvePage.reportAddFraud("test" + timestamp, fraudType.getDisplayName());
        checkUserFraudDb(innocentClient.getUcid(), fraudType.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Report fraud on non violator - TLS abuse")
    @AllureId("885")
    public void reportFraudTestTlsAbuse() throws Exception {
        deleteUserBO(innocentClient.getUcid());
        cleanUserAudit(innocentClient.getUcid());
        activityTab.navigateToMain();
        keycloackPage.loginAsDevUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        FraudType fraudType = FraudType.TLS_ABUSE;
        resolvePage.reportAddFraud("test" + timestamp, fraudType.getDisplayName());
        checkUserFraudDb(innocentClient.getUcid(), fraudType.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Report fraud on non violator - Potential abuse")
    @AllureId("886")
    public void reportFraudTestPotentialAbuse() throws Exception {
        deleteUserBO(innocentClient.getUcid());
        cleanUserAudit(innocentClient.getUcid());
        activityTab.navigateToMain();
        keycloackPage.loginAsDevUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        FraudType fraudType = FraudType.POTENTIAL_ABUSE;
        resolvePage.reportAddFraud("test" + timestamp, fraudType.getDisplayName());
        checkUserFraudDb(innocentClient.getUcid(), fraudType.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Report fraud on non violator - Bonus abuse")
    @AllureId("887")
    public void reportFraudTestBonusAbuse() throws Exception {
        deleteUserBO(innocentClient.getUcid());
        cleanUserAudit(innocentClient.getUcid());
        activityTab.navigateToMain();
        keycloackPage.loginAsDevUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        FraudType fraudType = FraudType.BONUS_ABUSE;
        resolvePage.reportAddFraud("test" + timestamp, fraudType.getDisplayName());
        checkUserFraudDb(innocentClient.getUcid(), fraudType.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Report fraud on non violator - Loophole abuse")
    @AllureId("888")
    public void reportFraudTestLoopholeAbuse() throws Exception {
        deleteUserBO(innocentClient.getUcid());
        cleanUserAudit(innocentClient.getUcid());
        activityTab.navigateToMain();
        keycloackPage.loginAsDevUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        FraudType fraudType = FraudType.LOOPHOLE_ABUSE;
        resolvePage.reportAddFraud("test" + timestamp, fraudType.getDisplayName());
        checkUserFraudDb(innocentClient.getUcid(), fraudType.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Report fraud on non violator - HFT abuse")
    @AllureId("889")
    public void reportFraudTestHftAbuse() throws Exception {
        deleteUserBO(innocentClient.getUcid());
        cleanUserAudit(innocentClient.getUcid());
        activityTab.navigateToMain();
        keycloackPage.loginAsDevUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        FraudType fraudType = FraudType.HFT_ABUSE;
        resolvePage.reportAddFraud("test" + timestamp, fraudType.getDisplayName());
        checkUserFraudDb(innocentClient.getUcid(), fraudType.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Report fraud on non violator - News trader")
    @AllureId("890")
    public void reportFraudTestNewsTrader() throws Exception {
        deleteUserBO(innocentClient.getUcid());
        cleanUserAudit(innocentClient.getUcid());
        activityTab.navigateToMain();
        keycloackPage.loginAsDevUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        FraudType fraudType = FraudType.NEWS_TRADER;
        resolvePage.reportAddFraud("test" + timestamp, fraudType.getDisplayName());
        checkUserFraudDb(innocentClient.getUcid(), fraudType.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Report fraud on non violator - Anomalous profit")
    @AllureId("891")
    public void reportFraudTestAnomalousProfit() throws Exception {
        deleteUserBO(innocentClient.getUcid());
        cleanUserAudit(innocentClient.getUcid());
        activityTab.navigateToMain();
        keycloackPage.loginAsDevUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        FraudType fraudType = FraudType.ANOMALOUS_PROFIT;
        resolvePage.reportAddFraud("test" + timestamp, fraudType.getDisplayName());
        checkUserFraudDb(innocentClient.getUcid(), fraudType.getFraudTypeId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Report fraud on non violator - CPA abuse")
    @AllureId("892")
    public void reportFraudTestCpaAbuse() throws Exception {
        deleteUserBO(innocentClient.getUcid());
        cleanUserAudit(innocentClient.getUcid());
        activityTab.navigateToMain();
        keycloackPage.loginAsDevUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        FraudType fraudType = FraudType.CPA_ABUSE;
        resolvePage.reportAddFraud("test" + timestamp, fraudType.getDisplayName());
        checkUserFraudDb(innocentClient.getUcid(), fraudType.getFraudTypeId());
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
        activityTab.navigateToMain();
        keycloackPage.loginAsDevUser();
        investigationPage.navigateToClient(innocentClient.getUcid());
        resolvePage.openReportFraudForm();
        resolvePage.reportAddFraud("test1" + timestamp, fraud1.getDisplayName());
        resolvePage.openReportFraudForm();
        resolvePage.checkPreviousConfirmedFraudDisplayed(fraud1.getDisplayName());
        resolvePage.reportAddFraud("test2" + timestamp, fraud2.getDisplayName());
        checkUserFraudDb(innocentClient.getUcid(), fraud2.getFraudTypeId());
    }

}
