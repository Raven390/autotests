package tests.vindexBackofficeUiTests;

import businessObjects.db.clickhouse.dpAndWdByChannel.dpAndWdByChannelObject;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBaseWeb;

import static helpers.database.DbHelper.insertObjectToDb;
import static helpers.database.OperationsHelper.cleanUserCashflowDb;
import static utils.Constants.LAYER_WEB;
import static utils.Constants.TEAM_BACKOFFICE;
import static utils.Utils.getCurrentDate;
import static utils.Utils.getCurrentTimestampDbFormat;

public class OperationsTabTest extends TestBaseWeb {

    String testUserUcid = "infinox-171701";

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("562")
    @DisplayName("Operations tab. Cashflow chart show empty state when it not have data DB")
    public void CashflowEmptyStateTest() throws Exception {
        investigationPage.navigate();
        connectionPage.navigateMain();
        keycloackPage.loginWeb("dev", "123");
        cleanUserCashflowDb(testUserUcid);
        operationsPage.navigateOperationsTab(testUserUcid);
        operationsPage.checkCashflowEmptyStateIsVisible();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("573")
    @DisplayName("Operations tab. Cashflow chart show empty one side on deposit when it not have data DB")
    public void CashflowOnlyOneWithdrawalFilledTest() throws Exception {
        investigationPage.navigate();
        connectionPage.navigateMain();
        keycloackPage.loginWeb("dev", "123");
        cleanUserCashflowDb(testUserUcid);
        dpAndWdByChannelObject withtdrawal = new dpAndWdByChannelObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), "Withdrawal", "TestPaymentService", "Payment Services", 12.0, 22, getCurrentTimestampDbFormat());
        Allure.step("add record about withdrawal");
        insertObjectToDb("vindex_test.dp_and_wd_by_channel", withtdrawal);
        operationsPage.navigateOperationsTab(testUserUcid);
        operationsPage.checkCashflowEmptyStateDepositIsVisible();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("574")
    @DisplayName("Operations tab. Cashflow chart show empty one side on deposit when it not have data DB")
    public void CashflowOnlyDepositSideFilledTest() throws Exception {
        investigationPage.navigate();
        connectionPage.navigateMain();
        keycloackPage.loginWeb("dev", "123");
        cleanUserCashflowDb(testUserUcid);
        dpAndWdByChannelObject withtdrawal = new dpAndWdByChannelObject(testUserUcid, "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), "Deposit", "TestPaymentService", "Payment Services", 12.0, 22, getCurrentTimestampDbFormat());
        Allure.step("add record about deposit");
        insertObjectToDb("vindex_test.dp_and_wd_by_channel", withtdrawal);
        operationsPage.navigateOperationsTab(testUserUcid);
        operationsPage.checkCashflowEmptyStateWithdrawalIsVisible();
    }
}
