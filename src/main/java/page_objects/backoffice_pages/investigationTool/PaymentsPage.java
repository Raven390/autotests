package page_objects.backoffice_pages.investigationTool;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;
import helpers.data.enums.DateTimeFormat;
import helpers.data.enums.VerificationStatus;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import page_objects.backoffice_pages.AbstractPage;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static utils.ConfigFactory.BASE_URL_E2E;
import static utils.Utils.writeLog;

public class PaymentsPage extends AbstractPage {

    private static final String NUMBER_OF_CONTAINERS = "Number of containers is: ";

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
    private final Locator withdrawalsTab;
    private final Locator summaryTab;
    private final Locator paymentProfilesTab;
    private final Locator withdrawalTabButtonContent;
    private final Locator filterOptions;
    private final Locator createTimeFilter;
    private final Locator typeFilter;
    private final Locator amountFilter;
    private final Locator tableRow;
    private final Locator tableCell;
    private final Locator tableHeader;
    private final Locator tooltip;
    private final Locator amountColumnHeader;
    private final Locator dateColumnHeader;
    private final Locator checkbox;
    private final Locator submitPanelCounter;
    private final Locator submitPanelInput;
    private final Locator submitPanelButtons;
    private final Locator approveButton;
    private final Locator approveAllButton;
    private final Locator rejectButton;
    private final Locator rejectAllButton;
    private final Locator rebatesReceivedWidget;
    private final Locator rebatesReceivedWidgetTitle;
    private final Locator rebatesReceivedWidgetValue;
    private final Locator rebatesReceivedWidgetCounter;
    private final Locator timelineAnnotation;
    private final Locator paymentProfilesFamilyBlock;
    private final Locator paymentProfileDetailsRow;
    private final Locator paymentProfileDetailsTotalItem;
    private final Locator paymentFamilyButton;
    private final Locator paymentProfileButton;
    private final Locator updateVerificationStatusButton;
    private final Locator commentInput;
    private final Locator successToast;


    private static final String CONNECTION_TABLE_BUTTON_SELECTOR = "input[value='TABLE']";
    private static final String FINANCIAL_TRANSACTIONS_SELECTOR = "//div[@class='v-payments-summary__chart']//div[text()='Financial transactions']";
    private static final String FINANCIAL_TRANSACTIONS_EMPTY_STATE_SELECTOR = "//div[text()='Financial transactions']//ancestor::div[@class='v-payments-summary__chart']//span[contains(text(), 'No operations to display')]";
    private static final String CASHFLOW_EMPTY_STATE_SELECTOR = "//div[text()='Cashflow']//ancestor::div[@class='v-payments-summary__chart']//span[contains(text(), 'No operations to display')]";
    private static final String CASHFLOW_DEPOSIT_EMPTY_STATE_SELECTOR = "//*[contains(@class,'v-cash-flow-chart-line_type_deposit')]/..//*[contains(@class,'v-cash-flow-chart-line__label_disabled')]/*[text()='No transactions']";
    private static final String CASHFLOW_WITHDRAWAL_EMPTY_STATE_SELECTOR = "//*[contains(@class,'v-cash-flow-chart-line_type_withdrawal')]/..//*[contains(@class,'v-cash-flow-chart-line__label_disabled')]/*[text()='No transactions']";
    private static final String TIMELINE_SECTIONS = "//*[@class = 'v-range-timeline__sections']";
    private static final String TIMELINE_SECTION = TIMELINE_SECTIONS + "/*[contains(@class, 'v-range-timeline-section')]";
    private static final String TIMELINE_BAR = TIMELINE_SECTION + "//*[@class = 'v-range-timeline-section__bar']";
    private static final String TIMELINE_VOLUME_BUTTON = "//*[@title='Volume']";
    private static final String TIMELINE_ACTIVITY_BUTTON = "//*[@title='Activity']";
    private static final String PAYMENT_PROFILE_ITEM = "//*[contains(@class,'v-payment-profiles-list__profile-name') and text()='%s']";
    private static final String ACTIVE_TIMELINE_SECTION_SELECTOR = "//*[contains(@class, 'v-range-timeline__section-container') and not(contains(@class, 'v-range-timeline__section-container_isTransparent'))]";
    private static final String VARIANT_BODY_1_SELECTOR = "//div[contains(@class, 'g-text_variant_body-1')]";
    private static final String ACCOUNT_SELECTION = "//div[contains(@class, '-filters__accounts')]//button";
    private static final String VARIANT_HEADER_2_SELECTOR = "//div[contains(@class, 'g-text_variant_header-2')]";
    private static final String CASHFLOW_SECTION_SELECTOR = "//div[@class = 'v-chart-wrapper__title']/div[text() = 'Cashflow']";
    private static final String CASHFLOW_HEADER_SELECTOR = "//*[@data-qa='payments__cashflow_chart__features']";
    private static final String FILTER_BY_PLACEHOLDER_PATTERN = "//span[text()='%s']/..";
    private static final String WIDGET_BY_TITLE_PATTERN = "//div[contains(@class,'v-payments-summary-card__title') and text()='%s']/ancestor::div[@class='v-payments-summary-card']";
    private static final String WIDGET_TITLE = "//div[contains(@class,'v-payments-summary-card__title-wrapper')]";
    private static final String WIDGET_VALUE = "//div[contains(@class,'v-payments-summary-card__total')]";
    private static final String WIDGET_COUNTER = "//div[contains(@class,'v-payments-summary-card__count')]";
    public static final String CONNECTED_CLIENTS_BUTTON = "//div[contains(@title,'Connected Clients')]";
    public static final String OPEN_VERIFICATION_DRAWER_BUTTON = "//*[@data-qa='payment_profile__view_drawer__change_status']";

    public PaymentsPage(Page page) {
        super(page);
        this.loaderAnimation = page.locator(".v-loader");
        this.paymentsTab = page.locator("//div[@class='g-tabs__item-title' and text()='Payments']/../..");
        this.financialTransactionsChart = page.locator(FINANCIAL_TRANSACTIONS_SELECTOR);
        this.cashflowDepositEmptyState = page.locator(CASHFLOW_DEPOSIT_EMPTY_STATE_SELECTOR);
        this.transactionTooltipTitleDate = page.locator(".v-payments-summary-transcations-tooltip__title");
        this.financialDateGraphContainer = page.locator("//div[text()='Financial transactions']/ancestor::div//div/div[@class='v-bar-chart__section']");
        this.financialTransactionGraphSection = page.locator(".v-bar-chart__section-container");
        this.financialDateGraphContainerTooltipTitle = page.locator("//div[contains(@class,'v-payments-summary-transcations-tooltip__title')]");
        this.financialDateGraphContainerTooltip = page.locator("//div[@class='v-payments-summary-transcations-tooltip']");
        this.clearSelectedAccountsButton = page.locator(".g-select-clear");
        this.accountSelectionWindow = page.locator(".v-payments-summary-filters__accounts button.g-select-control__button");
        this.timelineSection = page.locator(TIMELINE_SECTION);
        this.dateFilterSelectionButton = page.locator(".v-date-picker__select  button");
        this.calendarSelectionButton = page.locator("//div[@data-qa='select-popup']//div[@class='g-select-list__option']//span[text()='Custom dates']");
        this.timelineThumb = page.locator(".v-range-timeline-thumb");
        this.inactiveTimelineSection = page.locator(".v-range-timeline-section_isTransparent");
        this.activeTimelineSection = page.locator(ACTIVE_TIMELINE_SECTION_SELECTOR);
        this.withdrawalsTab = page.locator("//input[@value='WITHDRAWALS']");
        this.summaryTab = page.locator("//input[@value='SUMMARY']");
        this.paymentProfilesTab = page.locator("//input[@value='PAYMENT_PROFILES']");
        this.paymentProfilesFamilyBlock = page.locator("//div[@class='v-payments-profiles__family-block']");
        this.filterOptions = page.locator("//div[@role='option']");
        this.createTimeFilter = page.locator(String.format(FILTER_BY_PLACEHOLDER_PATTERN, "Lifetime"));
        this.typeFilter = page.locator(String.format(FILTER_BY_PLACEHOLDER_PATTERN, "All types"));
        this.amountFilter = page.locator(String.format(FILTER_BY_PLACEHOLDER_PATTERN, "Any amount"));
        this.withdrawalTabButtonContent = page.locator("//div[@class='v-payments__option-content']");
        this.tableRow = page.locator("//div[contains(@class,'v-body-row')]");
        this.tableCell = tableRow.locator("//div[contains(@class,'g-text')]");
        this.tableHeader = page.locator("//div[contains(@class,'header-cell') and not(contains(@class,'icon'))]");
        this.tooltip = page.locator("//div[@class='v-tooltip-content']");
        this.amountColumnHeader = tableHeader.getByText("AMOUNT");
        this.dateColumnHeader = tableHeader.getByText("DATE");
        this.checkbox = page.locator("//input[@type='checkbox']");
        this.submitPanelCounter = page.locator("//div[@class='v-multiselect-panel__counter']");
        this.submitPanelInput = page.locator("//div[@class='v-submit-panel__input']/descendant::input");
        this.submitPanelButtons = page.locator("//div[@class='v-submit-panel__buttons']");
        this.approveButton = submitPanelButtons.getByText("Approve");
        this.approveAllButton = submitPanelButtons.getByText("Approve all");
        this.rejectButton = submitPanelButtons.getByText("Reject");
        this.rejectAllButton = submitPanelButtons.getByText("Reject all");
        this.rebatesReceivedWidget = page.locator(String.format(WIDGET_BY_TITLE_PATTERN, "Rebates received"));
        this.rebatesReceivedWidgetTitle = rebatesReceivedWidget.locator(WIDGET_TITLE);
        this.rebatesReceivedWidgetValue = rebatesReceivedWidget.locator(WIDGET_VALUE);
        this.rebatesReceivedWidgetCounter = rebatesReceivedWidget.locator(WIDGET_COUNTER);
        this.timelineAnnotation = page.locator(".v-range-timeline-section__label");
        this.paymentProfileDetailsRow = page.locator(".v-payment-profile-overview__detail-row");
        this.paymentProfileDetailsTotalItem = page.locator(".v-payment-profile-totals__total-item");
        this.paymentFamilyButton = page.locator("//input[@value='PAYMENT_FAMILY']");
        this.paymentProfileButton = page.locator("//input[@value='PAYMENT_PROFILE']");
        this.updateVerificationStatusButton = page.locator("//button[@data-qa='payment_profile__edit_drawer__submit']");
        this.commentInput = page.locator("//*[@data-qa='payment_profile__edit_drawer__comment_input']//textarea");
        this.successToast = page.locator(".g-toast__container").first();
    }

    @Step("Open users operations tab")
    public void navigatePaymentsTab(String ucid) {
        navigate(ucid);
    }

    public void navigate(String ucid) {
        Allure.step("Navigate to payments tab");
        page.navigate(String.format("%sinvestigation/%s/%s", BASE_URL_E2E, ucid, "payments"));
        waitForPageToLoad();
    }

    @Step("Click payments tab")
    public void clickPaymentsTabButton() {
        paymentsTab.click();
        waitForPageToLoad();
    }

    @Step("Click payments tab")
    public void clickPaymentProfilesTabButton() {
        paymentProfilesTab.click();
        waitForPageToLoad();
    }

    public record PaymentFamilyBlock(String header, List<String> rowDataList) {
    }

    @Step("Get payment profile list")
    public List<PaymentFamilyBlock> getPaymentProfilesList() {
        List<PaymentFamilyBlock> list = new ArrayList<>();
        for (int i = 0; i < paymentProfilesFamilyBlock.count(); i++) {
            Locator row = paymentProfilesFamilyBlock.nth(i);
            Locator header = row.locator(".v-payments-profiles__family-summary");
            String headerText = header.textContent();
            Locator profileRow = row.locator(".v-payment-profiles-list__profile-row");
            List<String> rowDataList = new ArrayList<>();
            for (int k = 0; k < profileRow.count(); k++) {
                rowDataList.add(profileRow.nth(k).textContent());
            }
            list.add(new PaymentFamilyBlock(headerText, rowDataList));
        }
        return list;
    }

    @Step("Get payment profile details")
    public Map<String, String> getPaymentProfileDetailsRows() {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < paymentProfileDetailsRow.count(); i++) {
            Locator row = paymentProfileDetailsRow.nth(i);
            String name = row.locator("//*[contains(@class, 'g-text')]").nth(0).textContent();
            String value = row.locator("//*[contains(@class, 'g-text')]").nth(1).textContent();
            map.put(name, value);
        }
        return map;
    }

    @Step("Get payment profile details total")
    public List<String> getPaymentProfileDetailsTotals() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < paymentProfileDetailsTotalItem.count(); i++) {
            Locator row = paymentProfileDetailsTotalItem.nth(i);
            list.add(row.textContent());
        }
        return list;
    }

    @Step("Open payment profile details drawer")
    public void openPaymentProfileDetails(String profileName) {
        String str = String.format(PAYMENT_PROFILE_ITEM, profileName);
        page.waitForSelector(str).click();
        assertTrue(page.waitForSelector("//*[@data-qa='drawer_header']").isVisible());
    }

    @Step("Open payment profile drawer")
    public void openPaymentProfileDetailsConnectedClients() {
        page.waitForSelector(CONNECTED_CLIENTS_BUTTON).click();
    }

    @Step("Open payment profile verification drawer")
    public void openPaymentProfileVerificationDrawer() {
        page.waitForSelector(OPEN_VERIFICATION_DRAWER_BUTTON).click();
    }

    @Step("Get payment profile verification drawer")
    public String getPaymentProfileVerificationDrawerName() {
        return page.waitForSelector("//*[@data-qa='payment_profile__edit_drawer__profile_name']").textContent();
    }

    @Step("Select verification status")
    public void selectVerificationStatus(VerificationStatus verificationStatus) {
        page.waitForSelector(String.format("//*[@data-qa='payment_profile__edit_drawer__verification_status_selector__item__%s']", verificationStatus.toString())).click();
    }

    @Step("Send status update")
    public void commentAndSendVerificationStatus(String comment) {
        commentInput.fill(comment);
        updateVerificationStatusButton.click();
        successToast.getByText("Verification status updated").waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    @Step("Get payment profile details connected clients")
    public List<String> getPaymentProfileDetailsConnectedClients() {
        page.waitForSelector("//*[@data-qa= 'virtualized_table']");
        List<String> list = new ArrayList<>();
        Locator locator = page.locator("//*[contains(@data-qa, 'virtualized_table__rows__') and contains(@data-qa, '-') and string-length(translate(substring-after(@data-qa, '-'), '0123456789', '')) = 0]");
        for (int i = 0; i < locator.count(); i++) {
            list.add(locator.nth(i).textContent());
        }
        return list;
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
        checkCashflowEmptyStateDepositIsVisible();
        checkCashflowEmptyStateWithdrawalIsVisible();

    }

    @Step("Open users operations tab")
    public void checkCashflowEmptyStateIsNotVisible() {
        Allure.step("Check that Cashflow graph empty state is not visible");
        waitForPageToLoad();
        checkCashflowEmptyStateDepositIsNotVisible();
        checkCashflowEmptyStateWithdrawalIsNotVisible();

    }

    public void checkCashflowEmptyStateDepositIsVisible() {
        Allure.step("Check that Cashflow graph empty state on deposit side is visible");
        page.waitForSelector(CASHFLOW_DEPOSIT_EMPTY_STATE_SELECTOR);
        assertTrue(page.locator(CASHFLOW_DEPOSIT_EMPTY_STATE_SELECTOR).isVisible());
    }

    public void checkCashflowEmptyStateDepositIsNotVisible() {
        Allure.step("Check that Cashflow graph empty state on deposit side is not visible");
        assertFalse(page.locator(CASHFLOW_DEPOSIT_EMPTY_STATE_SELECTOR).isVisible());

    }

    public void clickOnAccountSelectionWindow() {
        page.locator(ACCOUNT_SELECTION).click();
    }

    public void checkCashflowEmptyStateWithdrawalIsVisible() {
        Allure.step("Check that Cashflow graph empty state on withdrawal side is visible");
        page.waitForSelector(CASHFLOW_WITHDRAWAL_EMPTY_STATE_SELECTOR);
        assertTrue(page.locator(CASHFLOW_WITHDRAWAL_EMPTY_STATE_SELECTOR).isVisible());

    }

    public void checkCashflowEmptyStateWithdrawalIsNotVisible() {
        Allure.step("Check that Cashflow graph empty state on withdrawal side is not visible");
        assertFalse(page.locator(CASHFLOW_WITHDRAWAL_EMPTY_STATE_SELECTOR).isVisible());

    }

    public void hoverOverCashflowLineByTypeDeposit(String typeSource) {
        String type = getPaymentType(typeSource);
        Allure.step("hover mouse over cashflow deposit line by type " + type);
        page.waitForSelector("//*[contains(@class, 'v-cash-flow-chart-line_type_deposit') ]/../*[contains(@class, 'v-cash-flow-chart-line') ]//span[contains(text(), '" + type + "')]");
        page.locator("//*[contains(@class, 'v-cash-flow-chart-line_type_deposit') ]/../*[contains(@class, 'v-cash-flow-chart-line') ]//span[contains(text(), '" + type + "')]").hover(new Locator.HoverOptions().setForce(true));

    }

    public void hoverOverCashflowLineByTypeWithdrawal(String typeSource) {
        String type = getPaymentType(typeSource);
        Allure.step("hover mouse over cashflow withdrawal line by type " + type);
        page.waitForSelector("//*[contains(@class, 'v-cash-flow-chart-line_type_withdrawal') ]/../*[contains(@class, 'v-cash-flow-chart-line') ]//span[contains(text(), '" + type + "')]");
        page.locator("//*[contains(@class, 'v-cash-flow-chart-line_type_withdrawal') ]/../*[contains(@class, 'v-cash-flow-chart-line') ]//span[contains(text(), '" + type + "')]").hover();

    }

    public void checkTotalCountByPaymentSystem(String paymentSystem, String totalCount) {
        Allure.step("Check total count by payment system name in appeared tip");
        page.waitForSelector("//span[text()='" + paymentSystem + "']/following-sibling::span[text()='" + totalCount + "']");
        assertTrue(page.waitForSelector("//span[text()='" + paymentSystem + "']/following-sibling::span[text()='" + totalCount + "']").isVisible());

    }

    public void checkTotalCountByPaymentSystem(String paymentSystem, Double totalCount) {
        checkTotalCountByPaymentSystem(paymentSystem, dfwholed.format(Math.round(totalCount)));

    }

    public void checkFinancialTransactionsTilesValues(String title, String expectedTotalValue,
            String expectedTotalOperations) {
        Allure.step("Check vales in financial operations tile " + title);
        String baseLocator = "//div[contains(text(),'" + title + "')]/../..//*[contains(@class, 'v-payments-summary-card__total')]";
        page.waitForSelector(baseLocator);
        String actualValue = page.locator(baseLocator + "/../div[1]").textContent();
        String actualOperations = page.locator(baseLocator + "/../div[2]").textContent();
        assertEquals(expectedTotalValue, actualValue);
        writeLog("actualOperations is " + actualOperations);
        assertTrue(actualOperations.contains(expectedTotalOperations));
    }

    public void checkFinancialTransactionsTilesValues(String title, Double expectedTotalValue,
            Integer expectedTotalOperations) {
        checkFinancialTransactionsTilesValues(title, decimalFormat.format(expectedTotalValue), expectedTotalOperations.toString());
    }

    public void checkCashflowTopPaymentSystemTypesHeaderDeposit(String expectedCategorySource, String expectedAmount) {
        String expectedCategory = getPaymentType(expectedCategorySource);
        Allure.step("Check top payment category and its total amount in usd Deposit");
        String topCatTileLocator = (CASHFLOW_SECTION_SELECTOR + "/../following-sibling::div/*[@class ='v-chart-wrapper__feature']/div[contains(text(), 'deposit')]");
        String topCatLocator = topCatTileLocator + "/following-sibling::div[2]";
        page.waitForSelector(topCatLocator);
        String topCategory = page.locator(topCatLocator).textContent();
        writeLog("Top category in Deposit: " + topCategory);
        assertTrue(topCategory.contains(expectedCategory));
        String topSumLocator = (topCatTileLocator + "/following-sibling::div[1]");
        page.waitForSelector(topSumLocator);
        String totalAmount = page.locator(topSumLocator).textContent();
        writeLog("totalAmount in Deposit: " + totalAmount);
        assertEquals(expectedAmount, totalAmount);
    }

    public void checkCashflowTopPaymentSourceHeaderDeposit(String expectedCategorySource, String expectedAmount) {
        Allure.step("Check top payment category and its total amount in usd Deposit");
        String topInfoLocator = (CASHFLOW_HEADER_SELECTOR + "//*[text()='Max deposit']/following-sibling::div");
        String topSourceLocator = topInfoLocator + "[2]";
        page.waitForSelector(topSourceLocator);
        String topCategory = page.locator(topSourceLocator).textContent();
        writeLog("Top category in Deposit: " + topCategory);
        assertTrue(topCategory.contains(expectedCategorySource));
        String topSumLocator = (topInfoLocator + "[1]");
        page.waitForSelector(topSumLocator);
        String totalAmount = page.locator(topSumLocator).textContent();
        writeLog("totalAmount in Deposit: " + totalAmount);
        assertEquals(expectedAmount, totalAmount);
    }

    public void checkCashflowTopPaymentSourceHeaderWithdrawal(String expectedCategorySource, String expectedAmount) {
        Allure.step("Check top payment category and its total amount in usd Withdrawal");
        String topInfoLocator = (CASHFLOW_HEADER_SELECTOR + "//*[text()='Max withdrawal']/following-sibling::div");
        String topSourceLocator = topInfoLocator + "[2]";
        page.waitForSelector(topSourceLocator);
        String topCategory = page.locator(topSourceLocator).textContent();
        writeLog("Top category in Deposit: " + topCategory);
        assertTrue(topCategory.contains(expectedCategorySource));
        String topSumLocator = (topInfoLocator + "[1]");
        page.waitForSelector(topSumLocator);
        String totalAmount = page.locator(topSumLocator).textContent();
        writeLog("totalAmount in Deposit: " + totalAmount);
        assertEquals(expectedAmount, totalAmount);
    }

    public void checkCashflowTopPaymentSystemTypesHeaderDeposit(String expectedCategory, Double expectedAmount) {
        checkCashflowTopPaymentSystemTypesHeaderDeposit(expectedCategory, dfwholed.format(expectedAmount));
    }

    public void checkCashflowTopPaymentSystemTypesHeaderWithdrawal(String expectedCategorySource,
            String expectedAmount) {
        Allure.step("Check top payment category and its total amount in usd Withdrawal");
        String topCatTileLocator = (CASHFLOW_SECTION_SELECTOR + "/../following-sibling::div/*[@class ='v-chart-wrapper__feature']/div[contains(text(), 'withdrawal')]");
        String topCatLocator = topCatTileLocator + "/following-sibling::div[2]";
        page.waitForSelector(topCatLocator);
        String expectedCategory = getPaymentType(expectedCategorySource);
        String topCategory = page.locator(topCatLocator).textContent();
        writeLog("Top category in Withdrawal: " + topCategory);
        assertEquals(expectedCategory, topCategory);
        String topSumLocator = (topCatTileLocator + "/following-sibling::div[1]");
        page.waitForSelector(topSumLocator);
        String totalAmount = page.locator(topSumLocator).textContent();
        writeLog("totalAmount in Withdrawal: " + totalAmount);
        assertEquals(expectedAmount, totalAmount);
    }

    public void checkCashflowTopPaymentSystemTypesHeaderWithdrawal(String expectedCategory, Double expectedAmount) {
        checkCashflowTopPaymentSystemTypesHeaderWithdrawal(expectedCategory, dfwholed.format(expectedAmount));
    }

    public void checkCashflowTopPaymentSourceHeaderDeposit(String expectedCategory, Double expectedAmount) {
        checkCashflowTopPaymentSourceHeaderDeposit(expectedCategory, dfwholed.format(expectedAmount));
    }

    public void checkCashflowTopPaymentSourceHeaderWithdrawal(String expectedCategory, Double expectedAmount) {
        checkCashflowTopPaymentSourceHeaderWithdrawal(expectedCategory, dfwholed.format(expectedAmount));
    }

    public void hoverOverFinancialTransactionsGraphByDateMMMdd(String dateString) throws ParseException {
        Allure.step("Hover over financial transactions graph by date");
        page.waitForTimeout(1000);
        int count = financialDateGraphContainer.count();
        writeLog(NUMBER_OF_CONTAINERS + count);
        boolean found = false;
        for (int i = 0; i < count && found == false; i++) {
            financialDateGraphContainer.nth(i).hover();
            page.waitForTimeout(200);
            if (financialDateGraphContainerTooltipTitle.isVisible()) {
                String interval = financialDateGraphContainerTooltipTitle.textContent();
                writeLog("interval is " + interval);
                SimpleDateFormat formatter = new SimpleDateFormat("MMM dd");
                Date date1 = formatter.parse(dateString);
                String[] dateIntervals = interval.split(" - ");
                writeLog("interval 1 is " + dateIntervals[0]);
                writeLog("interval 2 is " + dateIntervals[1]);
                Date date2 = formatter.parse(dateIntervals[0]);
                Date date3 = formatter.parse(dateIntervals[1]);

                if ((date1.after(date2) || date1.equals(date2)) && (date1.before(date3) || date1.equals(date3))) {
                    writeLog("SUCCESS date " + date1 + " is found");
                    found = true;
                }
            }
        }
    }

    public void hoverOverFinancialTransactionsGraphByDateMMMyyyy(String dateString) throws ParseException {
        Allure.step("Hover over financial transactions graph by date");
        page.waitForTimeout(1000);
        int count = financialDateGraphContainer.count();
        writeLog(NUMBER_OF_CONTAINERS + count);
        boolean found = false;
        for (int i = 0; i < count && found == false; i++) {
            financialDateGraphContainer.nth(i).hover();
            page.waitForTimeout(200);
            if (financialDateGraphContainerTooltipTitle.isVisible()) {
                String interval = financialDateGraphContainerTooltipTitle.textContent();
                writeLog("interval is " + interval);
                SimpleDateFormat formatter = new SimpleDateFormat("MMM yyyy");
                Date date1 = formatter.parse(dateString);
                String[] dateIntervals = interval.split(" - ");
                writeLog("interval 1 is " + dateIntervals[0]);
                writeLog("interval 2 is " + dateIntervals[1]);
                Date date2 = formatter.parse(dateIntervals[0]);
                Date date3 = formatter.parse(dateIntervals[1]);

                if ((date1.after(date2) || date1.equals(date2)) && (date1.before(date3) || date1.equals(date3))) {
                    writeLog("SUCCESS date " + date1 + " is found");
                    found = true;
                }
            }
        }
    }

    public void hoverOverFinancialTransactionsGraphByDateSingleDay(String dateString) throws ParseException {
        Allure.step("Hover over financial transactions graph by date");
        writeLog("searched date is " + dateString);
        page.waitForTimeout(1000);
        int count = financialDateGraphContainer.count();
        writeLog(NUMBER_OF_CONTAINERS + count);
        boolean found = false;
        for (int i = 0; i < count && found == false; i++) {
            financialDateGraphContainer.nth(i).hover();
            page.waitForTimeout(200);
            if (financialDateGraphContainerTooltipTitle.isVisible()) {
                String interval = financialDateGraphContainerTooltipTitle.textContent();
                if (interval.contains(dateString)) {
                    writeLog("SUCCESS date " + dateString + " is found");
                    found = true;
                }
            }
        }
    }

    public void hoverOverFirstFilledTransactionsGraphByDateSingleDay() throws ParseException {
        Allure.step("Hover over financial transactions graph by date");
        page.waitForTimeout(1000);
        int count = financialDateGraphContainer.count();
        writeLog(NUMBER_OF_CONTAINERS + count);
        boolean found = false;
        for (int i = 0; i < count && found == false; i++) {
            financialDateGraphContainer.nth(i).hover();
            page.waitForTimeout(200);
            if (financialDateGraphContainerTooltipTitle.isVisible()) {

                found = true;
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
            writeLog("There is no selected accounts");
        }
    }

    public void clickOnAccountSelection() {
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
        timelineSection.nth(sectionIndex).click(new Locator.ClickOptions().setForce(true));
    }

    public void clickOnLastTimelineSection() {
        Allure.step("click on last timeline section");
        waitForPageToLoad();
        int i = 0;
        while (timelineSection.count() == 0 && i < 50) {
            page.waitForTimeout(1000);
            i++;
        }
        int count = timelineSection.count();
        timelineSection.nth(count - 1).click(new Locator.ClickOptions().setForce(true));
    }

    public void clickOnPreLastTimelineSection() {
        Allure.step("click on last timeline section");
        waitForPageToLoad();
        int i = 0;
        while (timelineSection.count() == 0 && i < 50) {
            page.waitForTimeout(1000);
            i++;
        }
        int count = timelineSection.count();
        timelineSection.nth(count - 2).click(new Locator.ClickOptions().setForce(true));
    }

    public void checkTimelineSectionInactive(int sectionIndex) {
        waitForPageToLoad();
        int i = 0;
        while (timelineSection.count() == 0 && i < 50) {
            page.waitForTimeout(1000);
            i++;
        }
        Allure.step("check that timeline section number " + (sectionIndex + 1) + " is inactive");
        assertTrue(page.locator(TIMELINE_SECTION).nth(sectionIndex).and(inactiveTimelineSection).isVisible());
    }

    public void checkLastTimelineSectionInactive() {
        Allure.step("check that last timeline section number is inactive");
        waitForPageToLoad();
        int i = 0;
        while (timelineSection.count() == 0 && i < 50) {
            page.waitForTimeout(1000);
            i++;
        }
        int count = timelineSection.count();
        page.waitForTimeout(500);
        int filterCount = timelineSection.nth(count - 1).and(inactiveTimelineSection).count();
        writeLog("count of filters is" + filterCount);
        timelineSection.nth(count - 1).and(inactiveTimelineSection).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    public void shiftLeftTimelineThumbToTimelineSectionIndex(int sectionIndex) {
        waitForPageToLoad();
        int i = 0;
        while (timelineSection.count() == 0 && i < 50) {
            page.waitForTimeout(1000);
            i++;
        }
        Allure.step("check that last timeline section number is inactive");
        timelineThumb.nth(0).dragTo(timelineSection.nth(sectionIndex), new Locator.DragToOptions().setForce(true));
    }

    public void shiftRightTimelineThumbToTimelineSectionIndex(int sectionIndex) {
        waitForPageToLoad();
        int i = 0;
        while (timelineSection.count() == 0 && i < 50) {
            page.waitForTimeout(1000);
            i++;
        }
        Allure.step("check that last timeline section number is inactive");
        timelineThumb.nth(1).dragTo(timelineSection.nth(sectionIndex), new Locator.DragToOptions().setForce(true));
    }

    public void shiftRightTimelineThumbToPreLastTimelineSection() {
        waitForPageToLoad();
        int i = 0;
        while (timelineSection.count() == 0 && i < 50) {
            page.waitForTimeout(1000);
            i++;
        }
        Allure.step("check that last timeline section number is inactive");
        int count = timelineSection.count();
        timelineThumb.nth(1).dragTo(timelineSection.nth(count - 2));
    }

    public void checkTimelineSectionInactiveByDate(String date) {
        waitForPageToLoad();
        int i = 0;
        while (timelineSection.count() == 0 && i < 50) {
            page.waitForTimeout(1000);
            i++;
        }
        writeLog("the searched section is have date text " + date);
        Allure.step("check that timeline section, for exaple with date " + date + " inactive");
        page.waitForTimeout(500);
        assertTrue(inactiveTimelineSection.getByText(date).isVisible());
    }

    public void checkTimelineSectionVisibleByDate(String date) {
        writeLog("the searched section is have date text " + date);
        Allure.step("check that timeline section, for example with date " + date + " is visible");
        page.waitForTimeout(500);
        assertTrue(timelineSection.getByText(date).last().isVisible());
    }

    public void checkTimelineAnnotationInFormat(DateTimeFormat format) throws ParseException {
        Allure.step("Check that timeline annotations is in right format" + format.toString());
        SimpleDateFormat formatter = new SimpleDateFormat(format.getDisplayName(), Locale.ENGLISH);
        String annotation = timelineAnnotation.nth(1).textContent();
        assertDoesNotThrow(() -> {
            Date date = formatter.parse(annotation);
            writeLog("annotation is successfully parsed to: " + date.toString());
        });
    }

    public void checkFinancialTransactionSectionVisibleByDate(String date) {
        writeLog("the searched section is have date text " + date);
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

    public void clickWithdrawalsTabButton() {
        withdrawalsTab.click();
        page.waitForTimeout(500);
    }

    public String getWithdrawalsTabButtonText() {
        return withdrawalTabButtonContent.textContent();
    }

    private List<String> getFilterOptions(Locator filter) {
        filter.click();
        page.waitForTimeout(500);
        List<String> filterOptionsList = new ArrayList<>();
        for (int i = 0; i < filterOptions.count(); i++) {
            filterOptionsList.add(filterOptions.nth(i).textContent());
        }
        return filterOptionsList;
    }

    public List<String> getCreateTimeFilterOptions() {
        return getFilterOptions(createTimeFilter);
    }

    public List<String> getTypeFilterOptions() {
        return getFilterOptions(typeFilter);
    }

    public List<String> getAmountFilterOptions() {
        return getFilterOptions(amountFilter);
    }

    public int getRowsCount() {
        return tableRow.count();
    }

    public List<String> getAllRowsData() {
        List<String> tableCells = new ArrayList<>();
        for (int i = 0; i < tableCell.count(); i++) {
            tableCells.add(tableCell.nth(i).textContent());
        }
        return tableCells;
    }

    public List<String> getTableHeaders() {
        List<String> tableHeaders = new ArrayList<>();
        for (int i = 0; i < tableHeader.count(); i++) {
            tableHeaders.add(tableHeader.nth(i).textContent());
        }
        return tableHeaders;
    }

    public void clickAmountColumn() {
        amountColumnHeader.click();
    }

    public String getAmountColumnTooltip() {
        amountColumnHeader.hover();
        return tooltip.textContent();
    }

    public void clickDateColumn() {
        dateColumnHeader.click();
    }

    public String getDateColumnTooltip() {
        dateColumnHeader.hover();
        return tooltip.textContent();
    }

    public void selectCreateTimeFilterOption(String option) {
        createTimeFilter.click();
        filterOptions.getByText(option).click();
    }

    public void selectTypeFilterOption(String option) {
        typeFilter.click();
        filterOptions.getByText(option).click();
    }

    public void selectAmountFilterOption(String option) {
        amountFilter.click();
        filterOptions.getByText(option).click();
    }

    public void selectAllWithdrawals() {
        checkbox.first().click();
    }

    public String getSubmitPanelCounterText() {
        return submitPanelCounter.textContent();
    }

    public void fillSubmitPanelInput(String text) {
        submitPanelInput.fill(text);
    }

    public void clickRejectAllButton() {
        rejectAllButton.click();
    }

    public void clickApproveAllButton() {
        approveAllButton.click();
    }

    public void clickRejectButton() {
        rejectButton.click();
    }

    public void clickApproveButton() {
        approveButton.click();
    }

    @Step("Get Rebates received widget title")
    public String getRebatesReceivedWidgetTitle() {
        return rebatesReceivedWidgetTitle.textContent();
    }

    @Step("Get Rebates received widget value")
    public String getRebatesReceivedWidgetValue() {
        return rebatesReceivedWidgetValue.textContent();
    }

    @Step("Get Rebates received widget counter")
    public String getRebatesReceivedWidgetCounter() {
        return rebatesReceivedWidgetCounter.textContent();
    }

    public static String getPaymentType(String inputType) {
        String transformedType;
        if (inputType.toLowerCase().contains("bank")) {
            transformedType = "Bank Transfers";
        } else if (inputType.toLowerCase().contains("card")) {
            transformedType = "Cards";
        } else if (inputType.toLowerCase().contains("crypto") || inputType.toLowerCase().contains("pix")) {
            transformedType = "Crypto";
        } else
            if (inputType.toLowerCase().contains("union") || inputType.toLowerCase().contains("wise") || inputType.toLowerCase().contains("p2p")) {
                transformedType = "P2P";
            } else
                if (inputType.toLowerCase().contains("local depositor") || inputType.toLowerCase().contains("offline payment")) {
                    transformedType = "Other";
                } else {
                    transformedType = "Payment Services";
                }

        return transformedType;
    }

    public void isPaymentsTabHidden() {
        Allure.step("check is payment tab hidden");
        paymentsTab.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
    }

    public void isPaymentsTabVisible() {
        Allure.step("check is payment tab visible");
        waitForPageToLoad();
        paymentsTab.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    public void isWithdrawalsSubtabHidden() {
        Allure.step("check is withdrawals subtab hidden");
        waitForPageToLoad();
        withdrawalsTab.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
    }

    public void isWithdrawalsSubtabVisible() {
        Allure.step("check is withdrawals subtab visible");
        waitForPageToLoad();
        withdrawalsTab.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    public void isSummarySubtabVisible() {
        Allure.step("check is summary subtab visible");
        waitForPageToLoad();
        summaryTab.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    public void clickPaymentFamilyButton() {
        paymentFamilyButton.click();
    }

    public void clickPaymentProfileButton() {
        paymentProfileButton.click();
    }
}
