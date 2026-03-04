package page_objects.backoffice_pages.investigationTool;

import static com.microsoft.playwright.options.WaitForSelectorState.*;
import static utils.ConfigFactory.BASE_URL_E2E;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import java.util.List;
import page_objects.backoffice_pages.AbstractPage;

public class AlertsPage extends AbstractPage {

    private final Locator firstAlertRuleName;
    private final Locator alertCard;
    private final Locator alertRuleName;
    private final Locator alertRuleTrigger;
    private final Locator alertStatus;
    private final Locator alertAttributes;
    private final Locator alertPopup;
    private final Locator alertDate;
    private final Locator alertTime;
    private final Locator alertCount;
    private final Locator refreshButton;
    private final Locator alertStatusDropdown;
    private final Locator alertStatusDropdownActive;
    private final Locator alertStatusDropdownClosed;
    private final Locator alertStatusDropdownAll;
    private final Locator refreshButtonDisabled;
    private final Locator alertsLoading;
    private final Locator alertsTab;
    private final Locator loadedAlertsCount;

    public AlertsPage(Page page) {
        super(page);
        this.firstAlertRuleName = page.locator(
                "//div[@class='v-investigation-tools-alert-card__header']/div[contains(@class,'g-color-text_color_primary')]");
        this.alertCard = page.locator("//div[@class='v-timeline']/div[contains(@class,'v-timeline-item')]");
        this.alertRuleName = page.locator("//div[contains(@class,'g-color-text_color_primary')]");
        this.alertRuleTrigger = page.locator(
                "//div[contains(@class,'v-investigation-tools-alert-card__trigger')]/div[@class='v-text-with-icon__text']");
        this.alertStatus = page.locator("//div[contains(@class,'g-label_interactive')]");
        this.alertAttributes = page.locator("//div[@class='v-investigation-tools-alert-card__attributes']");
        this.alertPopup = page.locator("//div[contains(@class,'g-popup_open')]");
        this.alertDate = page.locator("//div[contains(@class,'v-timeline__date')]");
        this.alertTime = page.locator("//div[@class='v-timeline-item__time']/span");
        this.alertCount = page.locator("//div[@class='v-investigation-tools-client-alerts-tab__refresher']/div");
        this.refreshButton = page.locator("//button[@data-qa='client_alerts__refresher']");
        this.alertStatusDropdown = page.locator("//button[@role='combobox']");
        this.alertStatusDropdownActive = page.locator("//*[text()='Active alerts']");
        this.alertStatusDropdownClosed = page.locator("//*[text()='Closed alerts']");
        this.alertStatusDropdownAll = page.locator("//*[text()='All alerts']");
        this.refreshButtonDisabled =
                page.locator("//button[@data-qa='client_alerts__refresher' and contains(@class,'g-button_disabled')]");
        this.alertsLoading = page.locator(
                        "//div[contains(@class,'v-investigation-tools-alert-card-skeleton__item-body')]")
                .first();
        this.loadedAlertsCount =
                page.locator("//div[@class='v-investigation-tools-client-alerts-tab__refresher']/div[text()!='']");
        this.alertsTab = page.locator("//div[@data-qa='investigation__tools__tab__ALERTS']");
    }

    @Step("Open Alerts tab")
    public void openAlertsTab() {
        alertsTab.click();
        waitForPageToLoad();
    }

    public void navigate(String ucid) {
        Allure.step("Navigate to Alerts tab");
        page.navigate(String.format("%sinvestigation/%s/%s", BASE_URL_E2E, ucid, "alerts"));
        waitForPageToLoad();
    }

    @Step("Get rule name of the first alert")
    public String getFirstAlertRuleName() {
        return firstAlertRuleName.first().textContent();
    }

    @Step("Click refresh alert list button")
    public void clickRefreshButton() {
        refreshButton.click();
        refreshButtonDisabled.waitFor(new Locator.WaitForOptions().setState(DETACHED));
    }

    public void waitForPageToLoad() {
        alertsLoading.waitFor(new Locator.WaitForOptions().setState(DETACHED));
    }

    @Step("Get text of the element with amount of the alerts")
    public String getAlertsCountText() {
        return alertCount.textContent();
    }

    @Step("Get amount of all the alert cards")
    public int getAlertsCount() {
        return alertCard.count();
    }

    @Step("Get list of dates for all the alert cards")
    public List<String> getAlertsDatesList() {
        return alertDate.allTextContents();
    }

    @Step("Get list of time for all the alert cards")
    public List<String> getAlertsTimesList() {
        return alertCard.locator(alertTime).allTextContents();
    }

    @Step("Get list of rule names for all the alert cards")
    public List<String> getAlertsRuleNamesList() {
        return alertCard.locator(alertRuleName).allTextContents();
    }

    @Step("Get list of rule triggers for all the alert cards")
    public List<String> getAlertsRuleTriggersList() {
        return alertCard.locator(alertRuleTrigger).allTextContents();
    }

    @Step("Get list of attributes for all the alert cards")
    public List<String> getAlertsAttributesList() {
        return alertCard.locator(alertAttributes).allTextContents();
    }

    @Step("Filter active alerts")
    public void filterActiveAlerts() {
        alertStatusDropdown.click();
        alertStatusDropdownActive.click();
        waitForPageToLoad();
    }

    @Step("Filter closed alerts")
    public void filterClosedAlerts() {
        alertStatusDropdown.click();
        alertStatusDropdownClosed.click();
        waitForPageToLoad();
        refreshButtonDisabled.waitFor(new Locator.WaitForOptions().setState(DETACHED));
    }

    @Step("Filter all alerts")
    public void filterAllAlerts() {
        alertStatusDropdown.click();
        alertStatusDropdownAll.click();
        waitForPageToLoad();
    }

    @Step("Get popup text after hovering over status of the first alert")
    public String getFirstAlertStatus() {
        alertStatus.first().hover();
        return alertPopup.textContent();
    }

    public void waitForAlertsCountToLoad() {
        loadedAlertsCount.waitFor(new Locator.WaitForOptions().setState(VISIBLE));
    }

    public void isAlertTabVisible() {
        Allure.step("check is alert tab visible");
        alertsTab.waitFor(new Locator.WaitForOptions().setState(VISIBLE));
    }

    public void isAlertTabHidden() {
        Allure.step("check is alert tab hidden");
        waitForPageToLoad();
        alertsTab.waitFor(new Locator.WaitForOptions().setState(HIDDEN));
    }
}
