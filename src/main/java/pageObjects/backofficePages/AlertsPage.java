package pageObjects.backofficePages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.List;

import static com.microsoft.playwright.options.WaitForSelectorState.DETACHED;

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

    public AlertsPage(Page page) {
        super(page);
        this.firstAlertRuleName = page.locator("//div[@class='v-investigation-tools-alert-card__header']/div[contains(@class,'g-color-text_color_primary')]");
        this.alertCard = page.locator("//div[@class='v-timeline']/div[contains(@class,'v-timeline-item')]");
        this.alertRuleName = page.locator("//div[contains(@class,'g-color-text_color_primary')]");
        this.alertRuleTrigger = page.locator("//div[contains(@class,'v-investigation-tools-alert-card__trigger')]/div[@class='v-text-with-icon__text']");
        this.alertStatus = page.locator("//div[contains(@class,'g-label_interactive')]");
        this.alertAttributes = page.locator("//div[@class='v-investigation-tools-alert-card__attributes']");
        this.alertPopup = page.locator("//div[contains(@class,'g-popup_open')]");
        this.alertDate = page.locator("//div[contains(@class,'v-timeline__date')]");
        this.alertTime = page.locator("//div[@class='v-timeline-item__time']/span");
        this.alertCount = page.locator("//div[@class='v-investigation-tools-client-alerts-tab__refresher']/div");
        this.refreshButton = page.locator("//button[@data-qa='investigation_tools_client_alerts_refresher']");
        this.alertStatusDropdown = page.locator("//button[@role='combobox']");
        this.alertStatusDropdownActive = page.locator("//*[text()='Active alerts']");
        this.alertStatusDropdownClosed = page.locator("//*[text()='Closed alerts']");
        this.alertStatusDropdownAll = page.locator("//*[text()='All alerts']");
        this.refreshButtonDisabled = page.locator("//button[@data-qa='investigation_tools_client_alerts_refresher' and contains(@class,'g-button_disabled')]");
        this.alertsLoading = page.locator("//div[contains(@class,'v-investigation-tools-alert-card-skeleton__item-body')]").first();
    }

    public String getFirstAlertRuleName() {
        return firstAlertRuleName.first().textContent();
    }

    public void clickRefreshButton() {
        refreshButton.click();
        refreshButtonDisabled.waitFor(new Locator.WaitForOptions().setState(DETACHED));
    }

    public void waitForPageToLoad() {
        alertsLoading.waitFor(new Locator.WaitForOptions().setState(DETACHED));
    }

    public String getAlertsCountText() {
        return alertCount.textContent();
    }

    public int getAlertsCount() {
        return alertCard.count();
    }

    public List<String> getAlertsDatesList() {
        return alertDate.allTextContents();
    }

    public List<String> getAlertsTimesList() {
        return alertCard.locator(alertTime).allTextContents();
    }

    public List<String> getAlertsRuleNamesList() {
        return alertCard.locator(alertRuleName).allTextContents();
    }

    public List<String> getAlertsRuleTriggersList() {
        return alertCard.locator(alertRuleTrigger).allTextContents();
    }

    public List<String> getAlertsAttributesList() {
        return alertCard.locator(alertAttributes).allTextContents();
    }

    public void filterActiveAlerts() {
        alertStatusDropdown.click();
        alertStatusDropdownActive.click();
        waitForPageToLoad();
    }

    public void filterClosedAlerts() {
        alertStatusDropdown.click();
        alertStatusDropdownClosed.click();
        waitForPageToLoad();
    }

    public void filterAllAlerts() {
        alertStatusDropdown.click();
        alertStatusDropdownAll.click();
        waitForPageToLoad();
    }

    public String getFirstAlertStatus() {
        alertStatus.first().hover();
        return alertPopup.textContent();
    }
}

