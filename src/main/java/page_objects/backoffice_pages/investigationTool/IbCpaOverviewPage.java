package page_objects.backoffice_pages.investigationTool;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import java.util.ArrayList;
import java.util.List;
import page_objects.backoffice_pages.AbstractPage;

public class IbCpaOverviewPage extends AbstractPage {

    private final Locator overviewTitle;
    private final Locator overviewSubheaderText;
    private final Locator overviewSubheaderIcon;
    private final Locator underThisTitle;
    private final Locator underThisItems;
    private final Locator clientsPerformanceTitle;
    private final Locator clientsPerformanceFeatures;
    private final Locator clientsTotalsTitle;
    private final Locator clientsTotalsItems;
    private final Locator chartYAxisLabel;
    private final Locator chartXAxisLabels;
    private final Locator lowerLevelIbTab;
    private final Locator tableHeader;
    private final Locator tableRow;
    private final Locator tableCell;
    private final Locator closeDrawerButton;

    private static final String OVERVIEW_SUBHEADER = "//div[contains(@class,'v-drawer-header__sub-header')]";
    private static final String UNDER_THIS_SECTION = "//div[contains(@class,'v-registration-source-drawer-referrals')]";
    private static final String CLIENTS_PERFORMANCE_SECTION =
            "//div[contains(@class,'v-registration-source-drawer-performance')]";
    private static final String CLIENTS_TOTALS_SECTION =
            "//div[@class='v-drawer-section-layout v-registration-source-drawer-totals']";
    private static final String OVERVIEW_CHART = "//div[@class='v-registration-source-chart']";

    public IbCpaOverviewPage(Page page) {
        super(page);
        overviewTitle =
                page.locator("//div[@class='v-drawer-header__title-container']").last();
        overviewSubheaderText =
                page.locator(String.format("%s/descendant::div[@class='v-text-with-icon__text']", OVERVIEW_SUBHEADER));
        overviewSubheaderIcon = page.locator(String.format(
                "%s/descendant::div[@class='v-text-with-icon__icon']/descendant::button", OVERVIEW_SUBHEADER));
        underThisTitle = page.locator(
                String.format("%s/descendant::div[contains(@class,'g-text_variant_subheader-2')]", UNDER_THIS_SECTION));
        underThisItems = page.locator(String.format(
                "%s/descendant::div[@class='v-registration-source-drawer-referrals__items']/div", UNDER_THIS_SECTION));
        clientsPerformanceTitle = page.locator(String.format("%s/div", CLIENTS_PERFORMANCE_SECTION))
                .first();
        clientsPerformanceFeatures = page.locator(String.format(
                "%s/descendant::div[@class='v-registration-source-drawer-performance__feature']",
                CLIENTS_PERFORMANCE_SECTION));
        clientsTotalsTitle =
                page.locator(String.format("%s/div", CLIENTS_TOTALS_SECTION)).first();
        clientsTotalsItems = page.locator(String.format(
                "%s/descendant::div[@class='v-registration-source-drawer-totals__items']/div", CLIENTS_TOTALS_SECTION));
        chartYAxisLabel = page.locator(
                String.format("%s/descendant::div[@class='v-line-chart__padded-value']/div", OVERVIEW_CHART));
        chartXAxisLabels = page.locator(
                String.format("%s/descendant::div[@class='v-line-chart__ticks-container']/div", OVERVIEW_CHART));
        lowerLevelIbTab = page.locator("//div[@title='Lower-level IB']");
        tableHeader = page.locator("//div[contains(@class,'header-cell') and not(contains(@class,'icon'))]");
        tableRow = page.locator("//div[contains(@class,'v-body-row')]");
        tableCell = tableRow.locator("//div[contains(@class,'g-text')]");
        closeDrawerButton = page.locator("//button[@data-qa='drawer_header__close_button']");
    }

    @Step("Get overview title")
    public String getOverviewTitle() {
        return overviewTitle.textContent();
    }

    @Step("Get overview subheader text")
    public String getOverviewSubheaderText() {
        return overviewSubheaderText.textContent();
    }

    @Step("Click overview subheader icon")
    public void clickOverviewSubheaderIcon() {
        page.waitForSelector(
                String.format(
                        "%s/descendant::div[@class='v-text-with-icon__icon']/descendant::button", OVERVIEW_SUBHEADER),
                new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
        overviewSubheaderIcon.click();
    }

    @Step("Get Under this title")
    public String getUnderThisTitle() {
        return underThisTitle.textContent();
    }

    @Step("Get Under this items")
    public List<String> getUnderThisItems() {
        List<String> items = new ArrayList<>();
        for (int i = 0; i < underThisItems.count(); i++) {
            items.add(underThisItems.nth(i).textContent());
        }
        return items;
    }

    @Step("Get Clients performance title")
    public String getClientsPerformanceTitle() {
        return clientsPerformanceTitle.textContent();
    }

    @Step("Get Clients performance items")
    public List<String> getClientsPerformanceItems() {
        List<String> items = new ArrayList<>();
        for (int i = 0; i < clientsPerformanceFeatures.count(); i++) {
            items.add(clientsPerformanceFeatures.nth(i).textContent());
        }
        return items;
    }

    @Step("Get Clients totals title")
    public String getClientsTotalsTitle() {
        return clientsTotalsTitle.textContent();
    }

    @Step("Get Clients totals items")
    public List<String> getClientsTotalsItems() {
        List<String> items = new ArrayList<>();
        for (int i = 0; i < clientsTotalsItems.count(); i++) {
            items.add(clientsTotalsItems.nth(i).textContent());
        }
        return items;
    }

    @Step("Get chart Y axis label")
    public String getChartYAxisLabel() {
        return chartYAxisLabel.textContent();
    }

    @Step("Get chart X axis labels")
    public List<String> getChartXAxisLabels() {
        List<String> labels = new ArrayList<>();
        for (int i = 0; i < chartXAxisLabels.count(); i++) {
            labels.add(chartXAxisLabels.nth(i).textContent());
        }
        return labels;
    }

    @Step("Click Lower-level IB tab")
    public void clickLowerLevelIbTab() {
        lowerLevelIbTab.click();
    }

    public List<String> getLowerLevelIbTableHeaders() {
        List<String> tableHeaders = new ArrayList<>();
        for (int i = 0; i < tableHeader.count(); i++) {
            tableHeaders.add(tableHeader.nth(i).textContent());
        }
        return tableHeaders;
    }

    public List<String> getLowerLevelIbAllRowsData() {
        List<String> tableCells = new ArrayList<>();
        for (int i = 0; i < tableCell.count(); i++) {
            tableCells.add(tableCell.nth(i).textContent());
        }
        return tableCells;
    }

    @Step("Click close drawer button")
    public void clickCloseDrawerButton() {
        closeDrawerButton.click();
    }
}
