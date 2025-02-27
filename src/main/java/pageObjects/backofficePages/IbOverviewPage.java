package pageObjects.backofficePages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

import java.util.ArrayList;
import java.util.List;

public class IbOverviewPage extends AbstractPage {

    private final Locator ibOverviewTitle;
    private final Locator ibOverviewSubheaderText;
    private final Locator ibOverviewSubheaderIcon;
    private final Locator underThisIbTitle;
    private final Locator underThisIbItems;
    private final Locator clientsPerformanceTitle;
    private final Locator clientsPerformanceFeatures;
    private final Locator clientsTotalsTitle;
    private final Locator clientsTotalsItems;
    private final Locator chartYAxisLabel;
    private final Locator chartXAxisLabels;

    private static final String IB_OVERVIEW_SUBHEADER = "//div[contains(@class,'v-drawer-header__sub-header')]";
    private static final String UNDER_THIS_IB_SECTION = "//div[contains(@class,'v-registration-source-drawer-referrals')]";
    private static final String CLIENTS_PERFORMANCE_SECTION = "//div[contains(@class,'v-registration-source-drawer-performance')]";
    private static final String CLIENTS_TOTALS_SECTION = "//div[@class='v-drawer-section-layout v-registration-source-drawer-totals']";
    private static final String IB_OVERVIEW_CHART = "//div[@class='v-registration-source-chart']";

    public IbOverviewPage(Page page) {
        super(page);
        ibOverviewTitle = page.locator("//div[@class='v-drawer-header__title-container']").last();
        ibOverviewSubheaderText = page.locator(String.format("%s/descendant::div[@class='v-text-with-icon__text']", IB_OVERVIEW_SUBHEADER));
        ibOverviewSubheaderIcon = page.locator(String.format("%s/descendant::div[@class='v-text-with-icon__icon']", IB_OVERVIEW_SUBHEADER));
        underThisIbTitle = page.locator(String.format("%s/descendant::div[contains(@class,'g-text_variant_subheader-2')]", UNDER_THIS_IB_SECTION));
        underThisIbItems = page.locator(String.format("%s/descendant::div[@class='v-registration-source-drawer-referrals__items']/div", UNDER_THIS_IB_SECTION));
        clientsPerformanceTitle = page.locator(String.format("%s/div", CLIENTS_PERFORMANCE_SECTION)).first();
        clientsPerformanceFeatures = page.locator(String.format("%s/descendant::div[@class='v-registration-source-drawer-performance__feature']", CLIENTS_PERFORMANCE_SECTION));
        clientsTotalsTitle = page.locator(String.format("%s/div", CLIENTS_TOTALS_SECTION)).first();
        clientsTotalsItems = page.locator(String.format("%s/descendant::div[@class='v-registration-source-drawer-totals__items']/div", CLIENTS_TOTALS_SECTION));
        chartYAxisLabel = page.locator(String.format("%s/descendant::div[@class='v-line-chart__padded-value']/div", IB_OVERVIEW_CHART));
        chartXAxisLabels = page.locator(String.format("%s/descendant::div[@class='v-line-chart__ticks-container']/div", IB_OVERVIEW_CHART));
    }

    @Step("Get IB overview title")
    public String getIbOverviewTitle() {
        return ibOverviewTitle.textContent();
    }

    @Step("Get IB overview subheader text")
    public String getIbOverviewSubheaderText() {
        return ibOverviewSubheaderText.textContent();
    }

    @Step("Click IB overview subheader icon")
    public void clickIbOverviewSubheaderIcon() {
        ibOverviewSubheaderIcon.click();
    }

    @Step("Get Under this IB title")
    public String getUnderThisIbTitle() {
        return underThisIbTitle.textContent();
    }

    @Step("Get Under this IB items")
    public List<String> getUnderThisIbItems() {
        List<String> items = new ArrayList<>();
        for (int i = 0; i < underThisIbItems.count(); i++) {
            items.add(underThisIbItems.nth(i).textContent());
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
}
