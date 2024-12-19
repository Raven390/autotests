package pageObjects.backofficePages;

import com.microsoft.playwright.*;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;


import static org.junit.jupiter.api.Assertions.*;
import static utils.ConfigFactory.BASE_URL_E2E;

public class OperationsPage extends AbstractPage {

    private final Locator loaderAnimation;
    private final Locator operationsTab;
    private final Locator financialTransactionsChart;
    private final Locator cashflowDepositEmptyState;

    private final String CONNECTION_TABLE_BUTTON_SELECTOR = "input[value='TABLE']";
    private final String FINANCIAL_TRANSACTIONS_SELECTOR = "//div[@class='v-payments-summary__chart']//div[text()='Financial transactions']";
    private final String FINANCIAL_TRANSACTIONS_EMPTY_STATE_SELECTOR = "//div[@class='v-payments-summary__chart']//div[text()='Financial transactions']/..//span[contains(text(), 'No relevant data is available ')]";
    private final String CASHFLOW_EMPTY_STATE_SELECTOR = "//div[@class='v-payments-summary__chart']//div[text()='Cashflow']/..//span[contains(text(), 'No relevant data is available ')]";
    private final String CASHFLOW_DEPOSIT_EMPTY_STATE_SELECTOR = "//*[contains(@class, 'v-cash-flow-chart-line_type_deposit') and contains(@class, 'v-cash-flow-chart-line_disabled')]/../..//span[text()='No transactions']";
    private final String CASHFLOW_WITHDRAWAL_EMPTY_STATE_SELECTOR = "//*[contains(@class, 'v-cash-flow-chart-line_type_withdrawal') and contains(@class, 'v-cash-flow-chart-line_disabled')]/../..//span[text()='No transactions']";


    public OperationsPage(Page page) {
        super(page);
        this.loaderAnimation = page.locator(".v-loader");
        this.operationsTab = page.locator("[role=\"tab\"][title=\"Operations\"]");
        this.financialTransactionsChart = page.locator(FINANCIAL_TRANSACTIONS_SELECTOR);
        this.cashflowDepositEmptyState = page.locator(CASHFLOW_DEPOSIT_EMPTY_STATE_SELECTOR);
    }

    @Step("Open users operations tab")
    public void navigateOperationsTab(String ucid) {
        Allure.step("Navigate to user with ucid " + ucid + " operations tab");
        page.navigate(BASE_URL_E2E + "investigation/" + ucid);
        waitForPageToLoad();
        operationsTab.click();
    }

    @Step("Open users operations tab")
    public void checkFinancialTransactionEmptyStateIsVisible() {
        Allure.step("Check that financial transaction graph empty state is visible");
        page.waitForSelector(FINANCIAL_TRANSACTIONS_EMPTY_STATE_SELECTOR);
        assertTrue(page.locator(FINANCIAL_TRANSACTIONS_EMPTY_STATE_SELECTOR).isVisible());

    }

    @Step("Open users operations tab")
    public void checkCashflowEmptyStateIsVisible() {
        Allure.step("Check that Cashflow graph empty state is visible");
        page.waitForSelector(CASHFLOW_EMPTY_STATE_SELECTOR);
        assertTrue(page.locator(CASHFLOW_EMPTY_STATE_SELECTOR).isVisible());

    }

    public void checkCashflowEmptyStateDepositIsVisible() {
        Allure.step("Check that Cashflow graph empty state on deposit side is visible");
        page.waitForSelector(CASHFLOW_DEPOSIT_EMPTY_STATE_SELECTOR);
        assertTrue(page.locator(CASHFLOW_DEPOSIT_EMPTY_STATE_SELECTOR).isVisible());

    }

    public void checkCashflowEmptyStateWithdrawalIsVisible() {
        Allure.step("Check that Cashflow graph empty state on withdrawal side is visible");
        page.waitForSelector(CASHFLOW_WITHDRAWAL_EMPTY_STATE_SELECTOR);
        assertTrue(page.locator(CASHFLOW_WITHDRAWAL_EMPTY_STATE_SELECTOR).isVisible());

    }

}
