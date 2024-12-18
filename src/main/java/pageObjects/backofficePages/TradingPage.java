package pageObjects.backofficePages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import utils.Utils;

import java.util.NoSuchElementException;
import java.util.Objects;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TradingPage extends AbstractPage {

    private final Locator tradingTab;
    private final Locator accountsTab;
    private final Locator operationsTab;
    private final Locator accountsTabContent;
    private final Locator dealsTabContent;
    private final Locator accountColumnHeader;
    private final Locator typeColumnHeader;
    private final Locator volumeColumnHeader;
    private final Locator profitColumnHeader;
    private final Locator openColumnHeader;
    private final Locator closeColumnHeader;
    private final Locator tpslColumnHeader;
    private final Locator swapColumnHeader;
    private final Locator srColumnHeader;
    private final Locator commissionColumnHeader;
    private final Locator methodColumnHeader;
    private final Locator commentColumnHeader;
    private final Locator accountColumnCell;
    private final Locator typeColumnCell;
    private final Locator profitColumnCell;
    private final Locator volumeColumnCell;
    private final Locator openColumnCell;
    private final Locator closeColumnCell;
    private final Locator tpslColumnCell;
    private final Locator swapColumnCell;
    private final Locator srColumnCell;
    private final Locator commissionColumnCell;
    private final Locator methodColumnCell;
    private final Locator commentColumnCell;
    private final Locator filterButton;
    private final Locator filterMenu;
    private final Locator checkboxItem;
    private final Locator typeShowMoreButton;
    private final Locator applyFiltersButton;
    private final Locator filterContainer;
    private final Locator volumeFromInput;
    private final Locator volumeToInput;
    private final Locator volumeColumnCellUSD;
    private final Locator openColumnCellDate;
    private final Locator closeColumnCellDate;
    private final Locator durationToInput;
    private final Locator durationFromInput;
    private final Locator profitToInput;
    private final Locator profitFromInput;
    private final Locator accountCard;
    private final Locator accountId;
    private final Locator balanceElement;
    private final Locator statusElement;
    private final Locator platformElement;
    private final Locator accountTypeElement;
    private final Locator createdTimeElement;
    private final Locator updatedTimeElement;
    private final Locator popupElement;
    private final Locator tableViewButton;
    private final Locator accountRow;
    private final Locator accountTableId;
    private final Locator accountTablePlatform;
    private final Locator accountTableType;
    private final Locator accountTableStatus;
    private final Locator accountTableCreated;
    private final Locator accountTableUpdated;
    private final Locator accountTableBalance;
    private final Locator accountTableTotalPnl;
    private final Locator accountTableEquity;
    private final Locator accountTableCredit;
    private final Locator accountTableLeverage;
    private final Locator accountTableMarginFree;
    private final Locator accountTableServer;
    private final Locator accountTableGroup;
    private final Locator accountTableHeaderAccount;
    private final Locator accountTableHeaderType;
    private final Locator accountTableHeaderStatus;
    private final Locator accountTableHeaderCreated;
    private final Locator accountTableHeaderUpdated;
    private final Locator accountTableHeaderBalance;
    private final Locator accountTableHeaderTotalPnl;
    private final Locator accountTableHeaderEquity;
    private final Locator accountTableHeaderCredit;
    private final Locator accountTableHeaderLeverage;
    private final Locator accountTableHeaderMarginFree;
    private final Locator accountTableHeaderServer;
    private final Locator accountTableHeaderGroup;

    private static final String ACCOUNT_CARD_VALUE_BY_TITLE_PATTERN = "//div[contains(@class,'v-trading-tab-accounts-card__column-title') and text()='%s']/following-sibling::div";
    private static final String POPUP_ELEMENT_XPATH = "//div[contains(@class,'g-popup_open')]";
    private static final String ACCOUNT_TABLE_COLUMN = "//td[contains(@class,'v-trading-tab-accounts-table__column_type_account')]%s";
    private static final String ACCOUNT_DATES_ELEMENT = "//div[@class='v-trading-tab-accounts-card__dates']%s";
    private static final String ACCOUNT_TABLE_HEADER_PATTERN = "//th[contains(@class,'v-trading-tab-accounts-table__column_type_%s')]";
    private static final String ACCOUNT_TABLE_CELL_PATTERN = "//td[contains(@class,'v-trading-tab-accounts-table__column_type_%s')]/div";

    public TradingPage(Page page) {
        super(page);
        this.tradingTab = page.locator("[role=\"tab\"][title=\"Trading\"]");
        this.accountsTab = page.locator(".g-radio-button__option-control[value=\"Accounts\"]");
        this.operationsTab = page.locator(".g-radio-button__option-control[value=\"Deals\"]");
        this.accountsTabContent = page.locator(".v-trading-tab-accounts");
        this.dealsTabContent = page.locator(".v-trading-tab-deals");
        this.accountColumnHeader = page.locator(".g-table__head .v-trading-tab-deals__column_type_account");
        this.typeColumnHeader = page.locator(".g-table__head .v-trading-tab-deals__column_type_type");
        this.volumeColumnHeader = page.locator(".g-table__head .v-trading-tab-deals__column_type_volume");
        this.profitColumnHeader = page.locator(".g-table__head .v-trading-tab-deals__column_type_profit");
        this.openColumnHeader = page.locator(".g-table__head .v-trading-tab-deals__column_type_open");
        this.closeColumnHeader = page.locator(".g-table__head .v-trading-tab-deals__column_type_close");
        this.tpslColumnHeader = page.locator(".g-table__head .v-trading-tab-deals__column_type_tp");
        this.swapColumnHeader = page.locator(".g-table__head .v-trading-tab-deals__column_type_swap");
        this.srColumnHeader = page.locator(".g-table__head .v-trading-tab-deals__column_type_sr");
        this.commissionColumnHeader = page.locator(".g-table__head .v-trading-tab-deals__column_type_commission");
        this.methodColumnHeader = page.locator(".g-table__head .v-trading-tab-deals__column_type_method");
        this.commentColumnHeader = page.locator(".g-table__head .v-trading-tab-deals__column_type_comment");
        this.accountColumnCell = page.locator(".g-table__body .v-trading-tab-deals__column_type_account");
        this.typeColumnCell = page.locator(".g-table__body .v-trading-tab-deals__column_type_type");
        this.profitColumnCell = page.locator(".g-table__body .v-trading-tab-deals__column_type_profit");
        this.volumeColumnCell = page.locator(".g-table__body .v-trading-tab-deals__column_type_volume");
        this.volumeColumnCellUSD = page.locator(".g-table__body .v-trading-tab-deals__column_type_volume .g-color-text_color_secondary");
        this.openColumnCellDate = page.locator(".g-table__body .v-trading-tab-deals__column_type_open .g-color-text_color_secondary");
        this.closeColumnCellDate = page.locator(".g-table__body .v-trading-tab-deals__column_type_close .g-color-text_color_secondary");
        this.openColumnCell = page.locator(".g-table__body .v-trading-tab-deals__column_type_open");
        this.closeColumnCell = page.locator(".g-table__body .v-trading-tab-deals__column_type_close");
        this.tpslColumnCell = page.locator(".g-table__body .v-trading-tab-deals__column_type_tp");
        this.swapColumnCell = page.locator(".g-table__body .v-trading-tab-deals__column_type_swap");
        this.srColumnCell = page.locator(".g-table__body .v-trading-tab-deals__column_type_sr");
        this.commissionColumnCell = page.locator(".g-table__body .v-trading-tab-deals__column_type_commission");
        this.methodColumnCell = page.locator(".g-table__body .v-trading-tab-deals__column_type_method");
        this.commentColumnCell = page.locator(".g-table__body .v-trading-tab-deals__column_type_comment");
        this.filterButton = page.locator(".v-trading-tab-deals__filters button");
        this.filterMenu = page.locator("[data-qa=\"drawer_body\"] .v-trading-tab-deals-filter__content");
        this.checkboxItem = page.locator(".v-trading-tab-deals-filter__filter-container  .g-checkbox");
        this.typeShowMoreButton = page.locator(".v-trading-tab-deals-filter__filter-container button").getByText("Show more");
        this.applyFiltersButton = page.locator("button").getByText("Apply");
        this.filterContainer = page.locator("v-trading-tab-deals-filter__filter-container");
        this.volumeFromInput = page.locator("//div[text()=\"Volume\"]/ancestor::div[contains(@class,'v-numeric-range-input')]/descendant::span[text()=\"From\"]/ancestor::span/input");
        this.volumeToInput = page.locator("//div[text()=\"Volume\"]/ancestor::div[contains(@class,'v-numeric-range-input')]/descendant::span[text()=\"To\"]/ancestor::span/input");
        this.durationFromInput = page.locator("//div[text()=\"Duration\"]/ancestor::div[contains(@class,'v-numeric-range-input')]/descendant::span[text()=\"From\"]/ancestor::span/input");
        this.durationToInput = page.locator("//div[text()=\"Duration\"]/ancestor::div[contains(@class,'v-numeric-range-input')]/descendant::span[text()=\"To\"]/ancestor::span/input");
        this.profitFromInput = page.locator("//div[text()=\"Profit\"]/ancestor::div[contains(@class,'v-numeric-range-input')]/descendant::span[text()=\"From\"]/ancestor::span/input");
        this.profitToInput = page.locator("//div[text()=\"Profit\"]/ancestor::div[contains(@class,'v-numeric-range-input')]/descendant::span[text()=\"To\"]/ancestor::span/input");
        this.accountCard = page.locator("//div[@class='v-trading-tab-accounts-card']");
        this.accountId = page.locator("//span[contains(@class,'g-text_variant_subheader-2')]");
        this.balanceElement = page.locator("//div[@class='v-trading-tab-accounts-card__balance']");
        this.statusElement = page.locator("//div[contains(@class,'v-trading-account-status-label')]/div[@class='v-text-with-icon__text']");
        this.platformElement = page.locator("//div[@class='v-trading-tab-accounts-card__left-col-footer']/div[@class='v-trading-tab-accounts-card__tooltip-wrap'][2]/descendant::div[@class='v-text-with-icon__text']");
        this.accountTypeElement = page.locator("//div[contains(@class,'v-trading-tab-accounts-card__account-type')]/descendant::div[@class='v-text-with-icon__text']");
        this.createdTimeElement = page.locator(String.format(ACCOUNT_DATES_ELEMENT, "/div[1]/descendant::div[@class='v-text-with-icon__text']"));
        this.updatedTimeElement = page.locator(String.format(ACCOUNT_DATES_ELEMENT, "/div[2]/descendant::div[@class='v-text-with-icon__text']"));
        this.popupElement = page.locator(POPUP_ELEMENT_XPATH);
        this.tableViewButton = page.locator("//input[@value='TABLE']");
        this.accountRow = page.locator("//tr[contains(@class,'g-table__row_vertical-align_top')]");
        this.accountTableId = page.locator(String.format(ACCOUNT_TABLE_COLUMN, "/descendant::div[contains(@class,'g-color-text_color_primary')]"));
        this.accountTablePlatform = page.locator(String.format(ACCOUNT_TABLE_COLUMN, "/descendant::div[contains(@class,'g-color-text_color_secondary')]"));
        this.accountTableType = page.locator(String.format(ACCOUNT_TABLE_CELL_PATTERN, "type"));
        this.accountTableStatus = page.locator(String.format(ACCOUNT_TABLE_CELL_PATTERN, "status"));
        this.accountTableCreated = page.locator(String.format(ACCOUNT_TABLE_CELL_PATTERN, "created"));
        this.accountTableUpdated = page.locator(String.format(ACCOUNT_TABLE_CELL_PATTERN, "updated"));
        this.accountTableBalance = page.locator(String.format(ACCOUNT_TABLE_CELL_PATTERN, "balance"));
        this.accountTableTotalPnl = page.locator(String.format(ACCOUNT_TABLE_CELL_PATTERN, "pnl"));
        this.accountTableEquity = page.locator(String.format(ACCOUNT_TABLE_CELL_PATTERN, "equity"));
        this.accountTableCredit = page.locator(String.format(ACCOUNT_TABLE_CELL_PATTERN, "credit"));
        this.accountTableLeverage = page.locator(String.format(ACCOUNT_TABLE_CELL_PATTERN, "leverage"));
        this.accountTableMarginFree = page.locator(String.format(ACCOUNT_TABLE_CELL_PATTERN, "margin-free"));
        this.accountTableServer = page.locator(String.format(ACCOUNT_TABLE_CELL_PATTERN, "server"));
        this.accountTableGroup = page.locator(String.format(ACCOUNT_TABLE_CELL_PATTERN, "group"));
        this.accountTableHeaderAccount = page.locator(String.format(ACCOUNT_TABLE_HEADER_PATTERN, "account"));
        this.accountTableHeaderType = page.locator(String.format(ACCOUNT_TABLE_HEADER_PATTERN, "type"));
        this.accountTableHeaderStatus = page.locator(String.format(ACCOUNT_TABLE_HEADER_PATTERN, "status"));
        this.accountTableHeaderCreated = page.locator(String.format(ACCOUNT_TABLE_HEADER_PATTERN, "created"));
        this.accountTableHeaderUpdated = page.locator(String.format(ACCOUNT_TABLE_HEADER_PATTERN, "updated"));
        this.accountTableHeaderBalance = page.locator(String.format(ACCOUNT_TABLE_HEADER_PATTERN, "balance"));
        this.accountTableHeaderTotalPnl = page.locator(String.format(ACCOUNT_TABLE_HEADER_PATTERN, "pnl"));
        this.accountTableHeaderEquity = page.locator(String.format(ACCOUNT_TABLE_HEADER_PATTERN, "equity"));
        this.accountTableHeaderCredit = page.locator(String.format(ACCOUNT_TABLE_HEADER_PATTERN, "credit"));
        this.accountTableHeaderLeverage = page.locator(String.format(ACCOUNT_TABLE_HEADER_PATTERN, "leverage"));
        this.accountTableHeaderMarginFree = page.locator(String.format(ACCOUNT_TABLE_HEADER_PATTERN, "margin-free"));
        this.accountTableHeaderServer = page.locator(String.format(ACCOUNT_TABLE_HEADER_PATTERN, "server"));
        this.accountTableHeaderGroup = page.locator(String.format(ACCOUNT_TABLE_HEADER_PATTERN, "group"));
    }

    @Step("Navigate to users trading tab")
    public void navigate(String ucid) {
        Allure.step("Navigate to users trading tab");
        page.navigate("http://k8s-test-nginxrev-55e209d446-410128713.us-east-1.elb.amazonaws.com/investigation?client_ucid=" + ucid);
        waitForPageToLoad();
        tradingTab.click();
        waitForPageToLoad();
    }

    @Step("Navigate to users restriction tab/operations")
    public void navigateOperations(String ucid) {
        Allure.step("Navigate to users trading tab/operations");
        page.navigate("http://k8s-test-nginxrev-55e209d446-410128713.us-east-1.elb.amazonaws.com/investigation?client_ucid=" + ucid);
        waitForPageToLoad();
        tradingTab.click();
        operationsTab.click();
        waitForPageToLoad();
    }

    @Step("Open users trading tab")
    public void openTradingTab() {
        waitForPageToLoad();
        tradingTab.click();
        waitForPageToLoad();
    }

    @Step("Open users trading-operations tab")
    public void openOperationsTab() {
        waitForPageToLoad();
        operationsTab.click();
        waitForPageToLoad();
    }


    @Step("Check if the trading/operations tab renders all basic elements")
    public void operationsRendersTest() {
        Allure.step("Check if the trading/operations tab renders all basic elements");
        assertTrue(operationsTab.isVisible());
        assertTrue(accountColumnHeader.isVisible());
        assertTrue(accountColumnCell.first().isVisible());
        assertTrue(typeColumnHeader.isVisible());
        assertTrue(typeColumnCell.first().isVisible());
        assertTrue(volumeColumnHeader.isVisible());
        assertTrue(volumeColumnHeader.isVisible());
        assertTrue(profitColumnHeader.isVisible());
        assertTrue(profitColumnCell.first().isVisible());
        assertTrue(openColumnHeader.isVisible());
        assertTrue(openColumnCell.first().isVisible());
        assertTrue(closeColumnHeader.isVisible());
        assertTrue(closeColumnCell.first().isVisible());
        assertTrue(tpslColumnHeader.isVisible());
        assertTrue(tpslColumnCell.first().isVisible());
        assertTrue(tpslColumnCell.first().isVisible());
        assertTrue(swapColumnHeader.isVisible());
        assertTrue(swapColumnCell.first().isVisible());
        assertTrue(srColumnHeader.isVisible());
        assertTrue(srColumnCell.first().isVisible());
        assertTrue(commissionColumnHeader.isVisible());
        assertTrue(commissionColumnCell.first().isVisible());
        assertTrue(methodColumnHeader.isVisible());
        assertTrue(methodColumnCell.first().isVisible());
        assertTrue(commentColumnHeader.isVisible());
        assertTrue(commissionColumnCell.first().isVisible());
        assertTrue(filterButton.isVisible());
        assertTrue(accountColumnHeader.getByText("ACCOUNT").isVisible());
        assertTrue(typeColumnHeader.getByText("TYPE").isVisible());
        assertTrue(volumeColumnHeader.getByText("VOLUME").isVisible());
        assertTrue(profitColumnHeader.getByText("PROFIT").isVisible());
        assertTrue(openColumnHeader.getByText("OPEN").isVisible());
        assertTrue(closeColumnHeader.getByText("CLOSE").isVisible());
        assertTrue(tpslColumnHeader.getByText("TP/SL").isVisible());
        assertTrue(swapColumnHeader.getByText("SWAP").isVisible());
        assertTrue(srColumnHeader.getByText("SR").isVisible());
        assertTrue(commissionColumnHeader.getByText("COMMISS.").isVisible());
        assertTrue(methodColumnHeader.getByText("METHOD").isVisible());
        assertTrue(commentColumnHeader.getByText("COMMENT").isVisible());
    }

    @Step("Check list of types for necessary types")
    public void checkTypeFilterList() {
        Allure.step("Check list of types for necessary types");
        assertEquals("Buy", checkboxItem.nth(0).textContent());
        assertEquals("Sell", checkboxItem.nth(1).textContent());
        assertEquals("Balance", checkboxItem.nth(2).textContent());
        assertEquals("Credit", checkboxItem.nth(3).textContent());
        assertEquals("Buy Limit", checkboxItem.nth(4).textContent());
        assertEquals("Sell Limit", checkboxItem.nth(5).textContent());
        assertEquals("Buy Stop", checkboxItem.nth(6).textContent());
        assertEquals("Sell Stop", checkboxItem.nth(7).textContent());
    }

    @Step("Open filter")
    public void openFilter() {
        Allure.step("Open filter");
        filterButton.click();
    }

    @Step("Click filter")
    public void clickFilterCheckbox(String typeName) {
        Allure.step("Click filter type " + typeName);
        checkboxItem.getByText(typeName, new Locator.GetByTextOptions().setExact(true)).click();
    }

    @Step("Check text content of first and last method cells on page")
    public void checkMethodCellsContent(String methodName) {
        Allure.step("Check text content of first and last method cells on page " + methodName);
        assertTrue(methodColumnCell.first().textContent().matches("(.)*" + methodName + "*"));
        assertTrue(methodColumnCell.last().textContent().matches("(.)*" + methodName + "*"));
    }

    @Step("Check text content of first and last method cells on page")
    public void checkTypeCellsContent(String typeName) {
        Allure.step("Check text content of first and last type cells on page " + typeName);
        assertTrue(typeColumnCell.first().textContent().matches("(.)*" + typeName + "*"));
        assertTrue(typeColumnCell.last().textContent().matches("(.)*" + typeName + "*"));
    }

    @Step("Click apply button")
    public void clickApplyButton() {
        Allure.step("Click apply button");
        applyFiltersButton.click();
    }

    @Step("Fill volume values")
    public void fillVolumeValues(String from, String to) {
        Allure.step("Fill volume values");
        volumeFromInput.fill(from);
        volumeToInput.fill(to);
    }

    @Step("Fill profit values")
    public void fillProfitValues(String from, String to) {
        Allure.step("Fill profit values");
        profitFromInput.fill(from);
        profitToInput.fill(to);
    }

    @Step("Fill volume values")
    public void fillDurationValues(String from, String to) {
        Allure.step("Fill volume values");
        durationFromInput.fill(from);
        durationToInput.fill(to);
    }

    @Step("Check text content of first and last Volume cells on page is in interval")
    public void checkVolumeCellsContentUSD(int from, int to) {
        Allure.step("Check text content of first and last Volume cells on page is in interval");
        String firstCell = volumeColumnCellUSD.first().textContent();
        String lastCell = volumeColumnCellUSD.last().textContent();
        System.out.println(Integer.parseInt(firstCell));
        System.out.println(Integer.parseInt(lastCell));
        assertTrue(from <= Integer.parseInt(firstCell) && Integer.parseInt(firstCell) <= to);
        assertTrue(from <= Integer.parseInt(lastCell) && Integer.parseInt(lastCell) <= to);
    }

    @Step("Check text content of first and last profit cells on page is in interval")
    public void checkProfitCellsContent(int from, int to) {
        Allure.step("Check text content of first and last profit cells on page is in interval");
        String firstCell = profitColumnCell.first().textContent();
        String lastCell = profitColumnCell.last().textContent();
        System.out.println(Double.parseDouble(firstCell));
        System.out.println(Double.parseDouble(lastCell));

        double profit1 = Double.parseDouble(firstCell);
        double profit2 = Double.parseDouble(lastCell);

        if (profit1 < 0) {
            profit1 = profit1 * -1;
        }

        if (profit2 < 0) {
            profit2 = profit2 * -1;
        }

        assertTrue(from <= profit1 && profit1 <= to);
        assertTrue(from <= profit2 && profit2 <= to);
    }

    @Step("Check text content of first profit cell")
    public void checkProfitCellsContentFirst(double expected) {
        Allure.step("Check text content of first profit cell");
        String firstCell = profitColumnCell.first().textContent();

        System.out.println(Double.parseDouble(firstCell));

        double profit1 = Double.parseDouble(firstCell);

        assertEquals(expected, profit1);
    }

    @Step("Check text content of first and last Dates in cells on page is in interval")
    public void checkDatesMinutes(int from, int to) {
        Allure.step("Check text content of first and last Dates in cells on page is in interval");
        String openDate = openColumnCellDate.first().textContent();
        String closeDate = closeColumnCellDate.first().textContent();
        System.out.println(openDate);
        System.out.println(closeDate);
        long difference = Utils.getDifferenceTimeMinutes(openDate, closeDate);
        assertTrue(from <= difference && difference <= to);
    }

    @Step("Wait for page to load")
    public void waitForPageToLoad() {
        page.waitForSelector(LOADING_ANIMATION_SELECTOR, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.HIDDEN));
        page.waitForSelector(LOADER_SPIN_LOCATOR, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.HIDDEN));
    }

    private int getAccountIndex(int accId) {
        for (int i = 0; i < accountCard.count(); i++) {
            Locator child = accountCard.nth(i).locator(accountId).last();
            if (Objects.equals(child.textContent(), String.valueOf(accId))) {
                return i;
            }
        }
        throw new NoSuchElementException(String.format("Account with accountId '%s' not found", accId));
    }

    @Step("Get account balance in card view")
    public String getAccountBalance(int accountId) {
        return accountCard.nth(getAccountIndex(accountId)).locator(balanceElement).locator("//span[contains(@class,'g-text_variant_header-2')]").textContent();
    }

    @Step("Get account balance usd in card view")
    public String getAccountBalanceUsd(int accountId) {
        return accountCard.nth(getAccountIndex(accountId)).locator(balanceElement).locator("//span[contains(@class,'g-text_variant_subheader-2')]").textContent();
    }

    @Step("Get account status in card view")
    public String getAccountStatus(int accountId) {
        return accountCard.nth(getAccountIndex(accountId)).locator(statusElement).textContent();
    }

    @Step("Get account platform in card view")
    public String getAccountPlatform(int accountId) {
        return accountCard.nth(getAccountIndex(accountId)).locator(platformElement).textContent();
    }

    @Step("Get account type in card view")
    public String getAccountType(int accountId) {
        return accountCard.nth(getAccountIndex(accountId)).locator(accountTypeElement).textContent();
    }

    @Step("Get account total pnl in card view")
    public String getAccountTotalPnl(int accountId) {
        return accountCard.nth(getAccountIndex(accountId)).locator(String.format(ACCOUNT_CARD_VALUE_BY_TITLE_PATTERN, "Total PNL")).textContent();
    }

    @Step("Get account equity in card view")
    public String getAccountEquity(int accountId) {
        return accountCard.nth(getAccountIndex(accountId)).locator(String.format(ACCOUNT_CARD_VALUE_BY_TITLE_PATTERN, "Equity")).textContent();
    }

    @Step("Get account credit in card view")
    public String getAccountCredit(int accountId) {
        return accountCard.nth(getAccountIndex(accountId)).locator(String.format(ACCOUNT_CARD_VALUE_BY_TITLE_PATTERN, "Credit")).textContent();
    }

    @Step("Get account leverage in card view")
    public String getAccountLeverage(int accountId) {
        return accountCard.nth(getAccountIndex(accountId)).locator(String.format(ACCOUNT_CARD_VALUE_BY_TITLE_PATTERN, "Leverage")).textContent();
    }

    @Step("Get account margin free in card view")
    public String getAccountMarginFree(int accountId) {
        return accountCard.nth(getAccountIndex(accountId)).locator(String.format(ACCOUNT_CARD_VALUE_BY_TITLE_PATTERN, "Margin free")).textContent();
    }

    @Step("Get account server in card view")
    public String getAccountServer(int accountId) {
        return accountCard.nth(getAccountIndex(accountId)).locator(String.format(ACCOUNT_CARD_VALUE_BY_TITLE_PATTERN, "Server")).textContent();
    }

    @Step("Get account group in card view")
    public String getAccountGroup(int accountId) {
        return accountCard.nth(getAccountIndex(accountId)).locator(String.format(ACCOUNT_CARD_VALUE_BY_TITLE_PATTERN, "Group")).textContent();
    }

    @Step("Get account created time in card view")
    public String getAccountCreatedTime(int accountId) {
        return accountCard.nth(getAccountIndex(accountId)).locator(createdTimeElement).textContent();
    }

    @Step("Get account updated time in card view")
    public String getAccountUpdatedTime(int accountId) {
        return accountCard.nth(getAccountIndex(accountId)).locator(updatedTimeElement).textContent();
    }

    @Step("Verify account id popup in card view is as expected")
    public void verifyAccountIdPopup() {
        accountCard.first().locator(accountId).last().hover();
        assertThat(popupElement).containsText("Login");
    }

    @Step("Verify account balance popup in card view is as expected")
    public void verifyBalancePopup(int accountId) {
        accountCard.nth(getAccountIndex(accountId)).locator(balanceElement).locator("//span[contains(@class,'g-text_variant_header-2')]").hover();
        assertThat(popupElement).containsText("Balance");
    }

    @Step("Verify account balance usd popup in card view is as expected")
    public void verifyBalanceUsdPopup(int accountId) {
        accountCard.nth(getAccountIndex(accountId)).locator(balanceElement).locator("//span[contains(@class,'g-text_variant_subheader-2')]").hover();
        assertThat(popupElement).containsText("Balance in USD");
    }

    @Step("Verify account status popup in card view is as expected")
    public void verifyStatusPopup() {
        accountCard.first().locator(statusElement).hover();
        assertThat(popupElement).containsText("Status");
    }

    @Step("Verify account platform popup in card view is as expected")
    public void verifyPlatformPopup() {
        accountCard.first().locator(platformElement).hover();
        assertThat(popupElement).containsText("Platform");
    }

    @Step("Verify account type popup in card view is as expected")
    public void verifyAccountTypePopup() {
        accountCard.first().locator(accountTypeElement).hover();
        assertThat(popupElement).containsText("Account type");
    }

    @Step("Verify account created time popup in card view is as expected")
    public void verifyCreatedTimePopup() {
        accountCard.first().locator(createdTimeElement).hover();
        assertThat(popupElement).containsText("Created time");
    }

    @Step("Verify account updated time popup in card view is as expected")
    public void verifyUpdatedTimePopup() {
        accountCard.first().locator(updatedTimeElement).hover();
        assertThat(popupElement).containsText("Updated time");
    }

    @Step("Click table view button")
    public void clickTableViewButton() {
        tableViewButton.click();
    }

    private int getAccountIndexTableView(int accId) {
        for (int i = 0; i < accountRow.count(); i++) {
            Locator child = accountRow.nth(i).locator(accountTableId);
            if (Objects.equals(child.textContent(), String.valueOf(accId))) {
                return i;
            }
        }
        throw new NoSuchElementException(String.format("Account with accountId '%s' not found", accId));
    }

    @Step("Get account platform in table view")
    public String getAccountTablePlatform(int accountId) {
        return accountRow.nth(getAccountIndexTableView(accountId)).locator(accountTablePlatform).textContent();
    }

    @Step("Get account type in table view")
    public String getAccountTableType(int accountId) {
        return accountRow.nth(getAccountIndexTableView(accountId)).locator(accountTableType).textContent();
    }

    @Step("Get account status in table view")
    public String getAccountTableStatus(int accountId) {
        return accountRow.nth(getAccountIndexTableView(accountId)).locator(accountTableStatus).textContent();
    }

    @Step("Get account created time in table view")
    public String getAccountTableCreated(int accountId) {
        return accountRow.nth(getAccountIndexTableView(accountId)).locator(accountTableCreated).textContent();
    }

    @Step("Get account updated time in table view")
    public String getAccountTableUpdated(int accountId) {
        return accountRow.nth(getAccountIndexTableView(accountId)).locator(accountTableUpdated).textContent();
    }

    @Step("Get account balance in table view")
    public String getAccountTableBalance(int accountId) {
        return accountRow.nth(getAccountIndexTableView(accountId)).locator(accountTableBalance).textContent();
    }

    @Step("Get account total pnl in table view")
    public String getAccountTableTotalPnl(int accountId) {
        return accountRow.nth(getAccountIndexTableView(accountId)).locator(accountTableTotalPnl).textContent();
    }

    @Step("Get account equity in table view")
    public String getAccountTableEquity(int accountId) {
        return accountRow.nth(getAccountIndexTableView(accountId)).locator(accountTableEquity).textContent();
    }

    @Step("Get account credit in table view")
    public String getAccountTableCredit(int accountId) {
        return accountRow.nth(getAccountIndexTableView(accountId)).locator(accountTableCredit).textContent();
    }

    @Step("Get account leverage in table view")
    public String getAccountTableLeverage(int accountId) {
        return accountRow.nth(getAccountIndexTableView(accountId)).locator(accountTableLeverage).textContent();
    }

    @Step("Get account margin free in table view")
    public String getAccountTableMarginFree(int accountId) {
        return accountRow.nth(getAccountIndexTableView(accountId)).locator(accountTableMarginFree).textContent();
    }

    @Step("Get account server in table view")
    public String getAccountTableServer(int accountId) {
        return accountRow.nth(getAccountIndexTableView(accountId)).locator(accountTableServer).textContent();
    }

    @Step("Get account group in table view")
    public String getAccountTableGroup(int accountId) {
        return accountRow.nth(getAccountIndexTableView(accountId)).locator(accountTableGroup).textContent();
    }

    @Step("Verify account table headers visibility and text")
    public void verifyAccountTableHeaders() {
        assertThat(accountTableHeaderAccount).isVisible();
        assertThat(accountTableHeaderAccount).containsText("ACCOUNT");
        assertThat(accountTableHeaderType).isVisible();
        assertThat(accountTableHeaderType).containsText("TYPE");
        assertThat(accountTableHeaderStatus).isVisible();
        assertThat(accountTableHeaderStatus).containsText("STATUS");
        assertThat(accountTableHeaderCreated).isVisible();
        assertThat(accountTableHeaderCreated).containsText("CREATED");
        assertThat(accountTableHeaderUpdated).isVisible();
        assertThat(accountTableHeaderUpdated).containsText("UPDATED");
        assertThat(accountTableHeaderBalance).isVisible();
        assertThat(accountTableHeaderBalance).containsText("BALANCE");
        assertThat(accountTableHeaderTotalPnl).isVisible();
        assertThat(accountTableHeaderTotalPnl).containsText("TOTAL PNL");
        assertThat(accountTableHeaderEquity).isVisible();
        assertThat(accountTableHeaderEquity).containsText("EQUITY");
        assertThat(accountTableHeaderCredit).isVisible();
        assertThat(accountTableHeaderCredit).containsText("CREDIT");
        assertThat(accountTableHeaderLeverage).isVisible();
        assertThat(accountTableHeaderLeverage).containsText("LEVERAGE");
        assertThat(accountTableHeaderMarginFree).isVisible();
        assertThat(accountTableHeaderMarginFree).containsText("MARGIN FREE");
        assertThat(accountTableHeaderServer).isVisible();
        assertThat(accountTableHeaderServer).containsText("SERVER");
        assertThat(accountTableHeaderGroup).isVisible();
        assertThat(accountTableHeaderGroup).containsText("GROUP");
    }

    @Step("Get account cell value for operation by index")
    public String getOperationAccountByIndex(int index) {
        return accountColumnCell.nth(index).textContent();
    }

    @Step("Get type cell value for operation by index")
    public String getOperationTypeByIndex(int index) {
        return typeColumnCell.nth(index).textContent();
    }

    @Step("Get volume cell value for operation by index")
    public String getOperationVolumeByIndex(int index) {
        return volumeColumnCell.nth(index).textContent();
    }

    @Step("Get profit cell value for operation by index")
    public String getOperationProfitByIndex(int index) {
        return profitColumnCell.nth(index).textContent();
    }

    @Step("Get open cell value for operation by index")
    public String getOperationOpenByIndex(int index) {
        return openColumnCell.nth(index).textContent();
    }

    @Step("Get close cell value for operation by index")
    public String getOperationCloseByIndex(int index) {
        return closeColumnCell.nth(index).textContent();
    }

    @Step("Get tp/sl cell value for operation by index")
    public String getOperationTpSlByIndex(int index) {
        return tpslColumnCell.nth(index).textContent();
    }

    @Step("Get swap cell value for operation by index")
    public String getOperationSwapByIndex(int index) {
        return swapColumnCell.nth(index).textContent();
    }

    @Step("Get sr cell value for operation by index")
    public String getOperationSrByIndex(int index) {
        return srColumnCell.nth(index).textContent();
    }

    @Step("Get commission cell value for operation by index")
    public String getOperationCommissionByIndex(int index) {
        return commissionColumnCell.nth(index).textContent();
    }

    @Step("Get method cell value for operation by index")
    public String getOperationMethodByIndex(int index) {
        return methodColumnCell.nth(index).textContent();
    }

    @Step("Get comment cell value for operation by index")
    public String getOperationCommentByIndex(int index) {
        return commentColumnCell.nth(index).textContent();
    }
}
