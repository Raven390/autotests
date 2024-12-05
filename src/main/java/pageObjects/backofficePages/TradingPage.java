package pageObjects.backofficePages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import utils.Utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TradingPage {

    private final Page page;
    private final Locator tradingTab;
    private final Locator accountsTab;
    private final Locator operationsTab;
    private final Locator loaderAnimation;
    private final Locator loaderSpin;
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

    public TradingPage(Page page) {
        this.page = page;
        this.loaderAnimation = page.locator(".v-loader");
        this.loaderSpin = page.locator(".g-spin");
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


    }

    @Step("Navigate to users trading tab")
    public void navigate(String ucid) {
        Allure.step("Navigate to users trading tab");
        page.navigate("http://k8s-test-nginxrev-55e209d446-410128713.us-east-1.elb.amazonaws.com/investigation?client_ucid=" + ucid);
        isPageLoaded();
        tradingTab.click();
        isPageLoaded();
    }

    @Step("Navigate to users restriction tab/operations")
    public void navigateOperations(String ucid) {
        Allure.step("Navigate to users trading tab/operations");
        page.navigate("http://k8s-test-nginxrev-55e209d446-410128713.us-east-1.elb.amazonaws.com/investigation?client_ucid=" + ucid);
        isPageLoaded();
        tradingTab.click();
        operationsTab.click();
        isPageLoaded();
    }

    @Step("Open users restriction tab")
    public void openTradingTab() {
        Allure.step("Open users trading tab/operations");
        isPageLoaded();
        tradingTab.click();
        operationsTab.click();
        isPageLoaded();
    }

    @Step("Check if the page loaded")
    public void isPageLoaded() {
        Allure.step("Check if the page loaded");
        int n = 0;
        page.waitForTimeout(2000);
        while ((loaderAnimation.isVisible() || loaderSpin.isVisible()) && n < 8) {
            page.waitForTimeout(2000);
            n += 1;
        }
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


}
