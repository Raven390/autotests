package pageObjects.backofficePages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

import static org.junit.jupiter.api.Assertions.*;

public class AuditTrailPage {

    private final Page page;
    private final Locator auditTrailTab;
    private final Locator loaderAnimation;
    private final Locator auditTrailItem;
    private final Locator auditTrailItemComment;

    public AuditTrailPage(Page page) {
        this.page = page;
        this.loaderAnimation = page.locator(".v-loader");
        this.auditTrailTab = page.locator("[role=\"tab\"][title=\"Audit trail\"]");
        this.auditTrailItem = page.locator(".v-timeline-item");
        this.auditTrailItemComment = page.locator(".v-timeline-item .v-investigation-tools-trail-card__comment");

    }

    @Step("Open users general tab")
    public void navigateAuditTrailTab(String ucid) {
        page.navigate("http://k8s-test-nginxrev-55e209d446-410128713.us-east-1.elb.amazonaws.com/investigation/" + ucid);
        isPageLoaded();
        auditTrailTab.click();
        isPageLoaded();
    }

    @Step("Open general tab")
    public void openAuditTrailTab() {
        isPageLoaded();
        auditTrailTab.click();
        isPageLoaded();
    }

    @Step("Check if the page loaded")
    public void isPageLoaded() {
        int n = 0;
        page.waitForTimeout(2000);
        while (loaderAnimation.isVisible() && n < 8) {
            page.waitForTimeout(2000);
            n += 1;
        }
    }

    @Step("Find record in audit trail")
    public void findRecord(String actionComment) {
        String comment = auditTrailItemComment.first().textContent();

        assertEquals(actionComment, comment);


    }


}

