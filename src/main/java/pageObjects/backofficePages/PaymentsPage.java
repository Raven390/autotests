package pageObjects.backofficePages;

import com.microsoft.playwright.*;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static utils.ConfigFactory.BASE_URL_E2E;

public class PaymentsPage extends AbstractPage {

    private final Locator loaderAnimation;
    private final Locator paymentsTab;
    private final Locator financialTransactionsChart;
    private final Locator cashflowDepositEmptyState;
    private final Locator transactionTooltipTitleDate;
    private final Locator financialDateGraphContainer;
    private final Locator financialDateGraphContainerTooltipTitle;
    private final Locator financialDateGraphContainerTooltip;
    private final Locator clearSelectedAccountsButton;
    private final Locator accountSelectionWindow;
    private final Locator timelineSection;
    private final Locator dateFilterSelectionButton;
    private final Locator calendarSelectionButton;
    private final Locator timelineThumb;
    private final Locator inactiveTimelineSection;
    private final Locator activeTimelineSection;
    private final Locator financialTransactionGraphSection;

    private final String CONNECTION_TABLE_BUTTON_SELECTOR = "input[value='TABLE']";
    private final String FINANCIAL_TRANSACTIONS_SELECTOR = "//div[@class='v-payments-summary__chart']//div[text()='Financial transactions']";
    private final String FINANCIAL_TRANSACTIONS_EMPTY_STATE_SELECTOR = "//div[text()='Financial transactions']//ancestor::div[@class='v-payments-summary__chart']//span[contains(text(), 'No operations to display')]";
    private final String CASHFLOW_EMPTY_STATE_SELECTOR = "//div[text()='Cashflow']//ancestor::div[@class='v-payments-summary__chart']//span[contains(text(), 'No operations to display')]";
    private final String CASHFLOW_DEPOSIT_EMPTY_STATE_SELECTOR = "//*[contains(@class, 'v-cash-flow-chart-line_type_deposit') and contains(@class, 'v-cash-flow-chart-line_disabled')]/../..//span[text()='No transactions']";
    private final String CASHFLOW_WITHDRAWAL_EMPTY_STATE_SELECTOR = "//*[contains(@class, 'v-cash-flow-chart-line_type_withdrawal') and contains(@class, 'v-cash-flow-chart-line_disabled')]/../..//span[text()='No transactions']";
    private final String TIMELINE_BAR_CONTAINER = "//*[contains(@class, 'v-range-timeline__section-container')]";
    private final String TIMELINE_BAR = "//*[contains(@class, 'v-range-timeline__bar')]";
    private final String TIMELINE_VOLUME_BUTTON = "//*[@title='Volume']";
    private final String TIMELINE_ACTIVITY_BUTTON = "//*[@title='Activity']";
    private final String ACTIVE_TIMELINE_SECTION_SELECTOR = "//*[contains(@class, 'v-range-timeline__section-container') and not(contains(@class, 'v-range-timeline__section-container_isTransparent'))]";
    private final String VARIANT_BODY_1_SELECTOR = "//div[contains(@class, 'g-text_variant_body-1')]";
    private final String ACCOUNT_SELECTION = "//div[contains(@class, '-filters__accounts')]";
    private final String VARIANT_HEADER_2_SELECTOR = "//div[contains(@class, 'g-text_variant_header-2')]";
    private final String CASHFLOW_SECTION_SELECTOR = "//div[@class = 'v-chart-wrapper__title']/div[text() = 'Cashflow']";


    public PaymentsPage(Page page) {
        super(page);
        this.loaderAnimation = page.locator(".v-loader");
        this.paymentsTab = page.locator("[role=\"tab\"][title=\"Payments\"]");
        this.financialTransactionsChart = page.locator(FINANCIAL_TRANSACTIONS_SELECTOR);
        this.cashflowDepositEmptyState = page.locator(CASHFLOW_DEPOSIT_EMPTY_STATE_SELECTOR);
        this.transactionTooltipTitleDate = page.locator(".v-payments-summary-transcations-tooltip__title");
        this.financialDateGraphContainer = page.locator("//div[text()='Financial transactions']/ancestor::div//div/div[@class='v-bar-chart__section']");
        this.financialTransactionGraphSection = page.locator(".v-bar-chart__section-container");
        this.financialDateGraphContainerTooltipTitle = page.locator("//div[contains(@class,'v-payments-summary-transcations-tooltip__title')]");
        this.financialDateGraphContainerTooltip = page.locator("//div[@class='v-payments-summary-transcations-tooltip']");
        this.clearSelectedAccountsButton = page.locator(".g-select-clear");
        this.accountSelectionWindow = page.locator(".v-payments-summary-filters__accounts button.g-select-control__button");
        this.timelineSection = page.locator(TIMELINE_BAR_CONTAINER);
        this.dateFilterSelectionButton = page.locator(".v-date-picker__select  button");
        this.calendarSelectionButton = page.locator("//div[@data-qa='select-popup']//div[@class='g-select-list__option']//span[text()='Custom dates']");
        this.timelineThumb = page.locator(".v-range-timeline__thumb");
        this.inactiveTimelineSection = page.locator(".v-range-timeline__section-container_isTransparent");
        this.activeTimelineSection = page.locator(ACTIVE_TIMELINE_SECTION_SELECTOR);
    }

    @Step("Open users operations tab")
    public void navigateOperationsTab(String ucid) {
        Allure.step("Navigate to user's operations tab");
        page.navigate(BASE_URL_E2E + "investigation/" + ucid);
        waitForPageToLoad();
        paymentsTab.click();
    }

    @Step("Click payments tab")
    public void clickPaymentsTabButton() {
        paymentsTab.click();
        waitForPageToLoad();
    }

    @Step("Open users operations tab")
    public void checkFinancialTransactionEmptyStateIsVisible() {
        Allure.step("Check that financial transaction graph empty state is visible");
        page.waitForSelector(FINANCIAL_TRANSACTIONS_EMPTY_STATE_SELECTOR);
        assertTrue(page.locator(FINANCIAL_TRANSACTIONS_EMPTY_STATE_SELECTOR).isVisible());

    }

    @Step("Open users operations tab")
    public void checkFinancialTransactionEmptyStateIsNotVisible() {
        Allure.step("Check that financial transaction graph empty state is NOT visible");
        page.waitForTimeout(100);
        assertTrue(page.locator(FINANCIAL_TRANSACTIONS_EMPTY_STATE_SELECTOR).isHidden());

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

    public void hoverOverCashflowLineByTypeDeposit(String type) {
        Allure.step("hover mouse over cashflow deposit line by type " + type);
        page.waitForSelector("//*[contains(@class, 'v-cash-flow-chart-line_type_deposit') ]/../*[contains(@class, 'v-cash-flow-chart-line') ]//span[contains(text(), '" + type + "')]");
        page.locator("//*[contains(@class, 'v-cash-flow-chart-line_type_deposit') ]/../*[contains(@class, 'v-cash-flow-chart-line') ]//span[contains(text(), '" + type + "')]").hover();

    }

    public void hoverOverCashflowLineByTypeWithdrawal(String type) {
        Allure.step("hover mouse over cashflow withdrawal line by type " + type);
        page.waitForSelector("//*[contains(@class, 'v-cash-flow-chart-line_type_withdrawal') ]/../*[contains(@class, 'v-cash-flow-chart-line') ]//span[contains(text(), '" + type + "')]");
        page.locator("//*[contains(@class, 'v-cash-flow-chart-line_type_withdrawal') ]/../*[contains(@class, 'v-cash-flow-chart-line') ]//span[contains(text(), '" + type + "')]").hover();

    }

    public void checkTotalCountByPaymentSystem(String paymentSystem, String totalCount) {
        Allure.step("Check total count by payment system name in appeared tip");
        page.waitForSelector("//span[text()='" + paymentSystem + "']/following-sibling::span[text()='" + totalCount + "']");
        assertTrue(page.waitForSelector("//span[text()='" + paymentSystem + "']/following-sibling::span[text()='" + totalCount + "']").isVisible());

    }

    public void checkFinancialTransactionsTilesValues(String title, String expectedTotalValue,
            String expectedTotalOperations) {
        Allure.step("Check vales in financial operations tile " + title);
        page.waitForSelector("//div[contains(text(),'" + title + "')]/following-sibling::div[contains(@class, 'v-payments-summary-cards__total')]");
        String actualValue = page.locator("//div[contains(text(),'" + title + "')]/following-sibling::div[contains(@class, 'v-payments-summary-cards__total')]").textContent();
        String actualOperations = page.locator("//div[contains(text(),'" + title + "')]/following-sibling::div[contains(@class, 'v-payments-summary-cards__total')]/../div[text() = '" + expectedTotalOperations + "']").textContent();
        assertEquals(expectedTotalValue, actualValue);
        assertTrue(actualOperations.contains(expectedTotalOperations));

    }

    public void checkCashflowTopPaymentSystemTypesHeaderDeposit(String expectedCategory, String expectedAmount) {
        Allure.step("Check top payment category and its total amount in usd Deposit");
        String topCatLocator = (CASHFLOW_SECTION_SELECTOR + "/../following-sibling::div" + VARIANT_BODY_1_SELECTOR + "[contains(text(), 'Deposit')]");
        page.waitForSelector(topCatLocator);
        String topCategory = page.locator(topCatLocator).textContent();
        System.out.println("Top category in Deposit: " + topCategory);
        assertTrue(topCategory.contains(expectedCategory));
        String topSumLocator = (CASHFLOW_SECTION_SELECTOR + "/../following-sibling::div" + VARIANT_BODY_1_SELECTOR + "[contains(text(), 'Deposit')]/preceding-sibling::div");
        page.waitForSelector(topSumLocator);
        String totalAmount = page.locator(topSumLocator).textContent();
        System.out.println("totalAmount in Deposit: " + totalAmount);
        assertEquals(expectedAmount, totalAmount);
    }

    public void checkCashflowTopPaymentSystemTypesHeaderWithdrawal(String expectedCategory, String expectedAmount) {
        Allure.step("Check top payment category and its total amount in usd Withdrawal");
        String topCatLocator = (CASHFLOW_SECTION_SELECTOR + "/../following-sibling::div" + VARIANT_BODY_1_SELECTOR + "[contains(text(), 'Withdrawal')]");
        page.waitForSelector(topCatLocator);
        String topCategory = page.locator(topCatLocator).textContent();
        System.out.println("Top category in Withdrawal: " + topCategory);
        assertTrue(topCategory.contains(expectedCategory));
        String topSumLocator = (CASHFLOW_SECTION_SELECTOR + "/../following-sibling::div" + VARIANT_BODY_1_SELECTOR + "[contains(text(), 'Withdrawal')]/preceding-sibling::div");
        page.waitForSelector(topSumLocator);
        String totalAmount = page.locator(topSumLocator).textContent();
        System.out.println("totalAmount in Withdrawal: " + totalAmount);
        assertEquals(expectedAmount, totalAmount);
    }

//    public void checkCashflowTopPaymentSystemTypesHeaderWithdrawal(String expectedCategory, String expectedAmount) {
//        Allure.step("Check top payment category and its total amount in usd Withdrawal");
//        page.waitForSelector("//div[contains(@class, 'g-text_variant_subheader-2') and contains(text(), 'Cashflow')]/../div[2]/div[2]/div[1]");
//        String totalAmount = page.locator("//div[contains(@class, 'g-text_variant_subheader-2') and contains(text(), 'Cashflow')]/../div[2]/div[2]/div[1]").textContent();
//        System.out.println("Total amount in usd Withdrawal: " + totalAmount);
//        assertEquals(expectedAmount, totalAmount);
//        page.waitForSelector("//div[contains(@class, 'g-text_variant_subheader-2') and contains(text(), 'Cashflow')]/../div[2]/div[2]/div[2]");
//        String topCategory = page.locator("//div[contains(@class, 'g-text_variant_subheader-2') and contains(text(), 'Cashflow')]/../div[2]/div[2]/div[2]").textContent();
//        System.out.println("current top category: " + topCategory);
//        assertTrue(topCategory.contains(expectedCategory));
//    }

    public void hoverOverFinancialTransactionsGraphByDateMMMdd(String dateString) throws ParseException {
        Allure.step("Hover over financial transactions graph by date");
        page.waitForTimeout(1000);
        int count = financialDateGraphContainer.count();
        System.out.println("number of containers is " + count);
        boolean found = false;
        for (int i = 0; i < count && found == false; i++) {
            financialDateGraphContainer.nth(i).hover();
            page.waitForTimeout(200);
            if (financialDateGraphContainerTooltipTitle.isVisible()) {
                String interval = financialDateGraphContainerTooltipTitle.textContent();
                System.out.println("interval is " + interval);
                SimpleDateFormat formatter = new SimpleDateFormat("MMM dd");
                Date date1 = formatter.parse(dateString);
                String[] dateIntervals = interval.split(" - ");
                System.out.println("interval 1 is " + dateIntervals[0]);
                System.out.println("interval 2 is " + dateIntervals[1]);
                Date date2 = formatter.parse(dateIntervals[0]);
                Date date3 = formatter.parse(dateIntervals[1]);

                if ((date1.after(date2) || date1.equals(date2)) && (date1.before(date3) || date1.equals(date3))) {
                    System.out.println("SUCCESS date " + date1 + " is found");
                    found = true;
                }
            }
        }
    }

    public void hoverOverFinancialTransactionsGraphByDateMMMyyyy(String dateString) throws ParseException {
        Allure.step("Hover over financial transactions graph by date");
        page.waitForTimeout(1000);
        int count = financialDateGraphContainer.count();
        System.out.println("number of containers is " + count);
        boolean found = false;
        for (int i = 0; i < count && found == false; i++) {
            financialDateGraphContainer.nth(i).hover();
            page.waitForTimeout(200);
            if (financialDateGraphContainerTooltipTitle.isVisible()) {
                String interval = financialDateGraphContainerTooltipTitle.textContent();
                System.out.println("interval is " + interval);
                SimpleDateFormat formatter = new SimpleDateFormat("MMM yyyy");
                Date date1 = formatter.parse(dateString);
                String[] dateIntervals = interval.split(" - ");
                System.out.println("interval 1 is " + dateIntervals[0]);
                System.out.println("interval 2 is " + dateIntervals[1]);
                Date date2 = formatter.parse(dateIntervals[0]);
                Date date3 = formatter.parse(dateIntervals[1]);

                if ((date1.after(date2) || date1.equals(date2)) && (date1.before(date3) || date1.equals(date3))) {
                    System.out.println("SUCCESS date " + date1 + " is found");
                    found = true;
                }
            }
        }
    }

    public void hoverOverFinancialTransactionsGraphByDateSingleDay(String dateString) throws ParseException {
        Allure.step("Hover over financial transactions graph by date");
        System.out.println("searched date is " + dateString);
        page.waitForTimeout(1000);
        int count = financialDateGraphContainer.count();
        System.out.println("number of containers is " + count);
        boolean found = false;
        for (int i = 0; i < count && found == false; i++) {
            financialDateGraphContainer.nth(i).hover();
            page.waitForTimeout(200);
            if (financialDateGraphContainerTooltipTitle.isVisible()) {
                String interval = financialDateGraphContainerTooltipTitle.textContent();
                if (interval.contains(dateString)) {
                    System.out.println("SUCCESS date " + dateString + " is found");
                    found = true;
                }
            }
        }
    }

    public void checkFinancialTransactionsRowInTooltip(String rowTitle, String expectedValue) {
        Allure.step("Check value in line " + rowTitle + " in appeared tooltip");
        page.waitForSelector("//div[@class='v-payments-summary-transcations-tooltip__type']//span[text()='" + rowTitle + "']/following-sibling::span");
        String actualValue = page.locator("//div[@class='v-payments-summary-transcations-tooltip__type']//span[text()='" + rowTitle + "']/following-sibling::span").textContent();
        assertEquals(expectedValue, actualValue);
    }

    public void selectTradingAccount(String accountId) {
        Allure.step("Click on select account button in dropdown list");
        page.waitForSelector("[data-qa='select-popup']");
        page.locator("//div[@data-qa='select-list']//*[text()='" + accountId + "']").click();
    }

    public void selectTradingAccount(int accountId) {
        selectTradingAccount(String.valueOf(accountId));
    }

    public void clearSelectedTradingAccount() {
        Allure.step("Click on clear selected accounts button in account selection window");
        if (clearSelectedAccountsButton.isVisible()) {
            clearSelectedAccountsButton.click();
        } else {
            System.out.println("There is no selected accounts");
        }
    }

    public void clickOnAccountSelectionWindow() {
        Allure.step("Click on account selection window");
        waitForPageToLoad();
        page.locator(ACCOUNT_SELECTION).click();
    }

    public void selectDatesInCalendar(String fromDate, String toDate) {
        Allure.step("select and Apply dates in the calendar");
        dateFilterSelectionButton.click();
        selectDateRangeInElement(calendarSelectionButton, fromDate, toDate);
    }

    public void selectDateFilter(String filterName) {
        Allure.step("select type of filtration by date " + filterName);
        dateFilterSelectionButton.click();
        page.waitForSelector("//div[@data-qa='select-popup']//div[@class='g-select-list__option']//span[text()='" + filterName + "']");
        page.locator("//div[@data-qa='select-popup']//div[@class='g-select-list__option']//span[text()='" + filterName + "']").click();
    }

    public void clickOnTimelineSectionByIndex(int sectionIndex) {
        Allure.step("click on timeline section number " + (sectionIndex + 1));
        timelineSection.nth(sectionIndex).click();
    }

    public void clickOnLastTimelineSection() {
        Allure.step("click on last timeline section");
        int count = timelineSection.count();
        timelineSection.nth(count - 1).click();
    }

    public void clickOnPreLastTimelineSection() {
        Allure.step("click on last timeline section");
        int count = timelineSection.count();
        timelineSection.nth(count - 2).click();
    }

    public void checkTimelineSectionInactive(int sectionIndex) {
        Allure.step("check that timeline section number " + (sectionIndex + 1) + " is inactive");
        assertTrue(timelineSection.nth(sectionIndex).and(inactiveTimelineSection).isVisible());
    }

    public void checkLastTimelineSectionInactive() {
        Allure.step("check that last timeline section number is inactive");
        int count = timelineSection.count();
        page.waitForTimeout(500);
        int filterCount = timelineSection.nth(count - 1).and(inactiveTimelineSection).count();
        System.out.println("count of filters is" + filterCount);
        assertTrue(timelineSection.nth(count - 1).and(inactiveTimelineSection).isVisible());
    }

    public void shiftLeftTimelineThumbToTimelineSectionIndex(int sectionIndex) {
        Allure.step("check that last timeline section number is inactive");
        timelineThumb.nth(0).dragTo(timelineSection.nth(sectionIndex));
    }

    public void shiftRightTimelineThumbToTimelineSectionIndex(int sectionIndex) {
        Allure.step("check that last timeline section number is inactive");
        timelineThumb.nth(1).dragTo(timelineSection.nth(sectionIndex));
    }

    public void shiftRightTimelineThumbToPreLastTimelineSection() {
        Allure.step("check that last timeline section number is inactive");
        int count = timelineSection.count();
        timelineThumb.nth(1).dragTo(timelineSection.nth(count - 2));
    }

    public void checkTimelineSectionInactiveByDate(String date) {
        System.out.println("the searched section is have date text " + date);
        Allure.step("check that timeline section, for exaple with date " + date + " inactive");
        page.waitForTimeout(500);
        assertTrue(inactiveTimelineSection.getByText(date).isVisible());
    }

    public void checkTimelineSectionVisibleByDate(String date) {
        System.out.println("the searched section is have date text " + date);
        Allure.step("check that timeline section, for example with date " + date + " is visible");
        page.waitForTimeout(500);
        assertTrue(timelineSection.getByText(date).last().isVisible());
    }

    public void checkFinancialTransactionSectionVisibleByDate(String date) {
        System.out.println("the searched section is have date text " + date);
        Allure.step("check that financial transaction graph section, for example with date " + date + " is visible");
        page.waitForTimeout(500);
        assertTrue(financialTransactionGraphSection.getByText(date).isVisible());
    }

    public void clickActivityButtonTimeline() {
        Allure.step("click activity button on timeline");
        waitForPageToLoad();
        page.locator(TIMELINE_ACTIVITY_BUTTON).click();
        waitForPageToLoad();
    }

    public void clickVolumeButtonTimeline() {
        Allure.step("click volume button on timeline");
        waitForPageToLoad();
        page.locator(TIMELINE_VOLUME_BUTTON).click();
        waitForPageToLoad();
    }

    public void checkStileValueOfTimelineBar(int barIndex, String expectedStyle) {
        Allure.step("click volume button on timeline");
        waitForPageToLoad();
        String actualStyle = page.locator(TIMELINE_BAR).nth(barIndex).getAttribute("style");
        assertEquals(expectedStyle, actualStyle);
    }

}