package pageObjects.backofficePages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class AlertsPage extends AbstractPage {

    private final Locator firstAlertRuleName;

    public AlertsPage(Page page) {
        super(page);
        this.firstAlertRuleName = page.locator("//div[@class='v-investigation-tools-alert-card__header']/div[contains(@class,'g-color-text_color_primary')]");
    }

    public String getFirstAlertRuleName() {
        return firstAlertRuleName.first().textContent();
    }

}

