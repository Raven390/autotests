package page_objects.backoffice_pages.investigationTool;

import business_objects.ui.audit_trail.AuditTrailItem;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import page_objects.backoffice_pages.AbstractPage;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static utils.ConfigFactory.BASE_URL_E2E;

public class AuditTrailPage extends AbstractPage {

    private final Locator auditTrailTab;
    private final Locator auditTrailItem;
    private final Locator auditTrailItemHeader;
    private final Locator auditTrailItemComment;
    private final Locator auditTrailItemDetails;
    private final Locator auditTrailItemTime;
    private final Locator auditTrailFilterButton;
    private final Locator auditTrailClearFilterButton;

    private static final String AUDIT_TRAIL_TAB_LOADING_ELEMENT = "//div[@class='v-investigation-tools-trail__skeleton-container']";
    private static final String AUDIT_TRAIL_FILTER_ITEM_PATTERN = "//div[@data-qa='select-popup']/descendant::*[text()='%s']";
    private static final String AUDIT_TRAIL_FILTER = "//div[contains(@class,'v-investigation-tools-trail-filters__select')]";

    public AuditTrailPage(Page page) {
        super(page);
        this.auditTrailTab = page.locator("[role=\"tab\"][title=\"Audit trail\"]");
        this.auditTrailItem = page.locator("//div[@class='v-investigation-tools-trail__item']/div/div[contains(@class,'v-timeline-item')]");
        this.auditTrailItemHeader = page.locator("//div[@class='v-investigation-tools-trail-card__header']");
        this.auditTrailItemComment = page.locator("//div[@class='v-investigation-tools-trail-card__comment']");
        this.auditTrailItemDetails = page.locator("//div[@class='v-investigation-tools-trail-card__details']/span");
        this.auditTrailItemTime = page.locator("//div[@class='v-timeline-item__time']");
        this.auditTrailFilterButton = page.locator(AUDIT_TRAIL_FILTER + "/descendant::button");
        this.auditTrailClearFilterButton = page.locator(AUDIT_TRAIL_FILTER + "/descendant::button[@data-qa='select-clear']");
    }

    @Step("Open users general tab")
    public void navigateAuditTrailTab(String ucid) {
        page.navigate(String.format("%sinvestigation/%s", BASE_URL_E2E, ucid));
        waitForPageToLoad();
        auditTrailTab.click();
        waitForPageToLoad();
    }

    @Step("Open general tab")
    public void openAuditTrailTab() {
        auditTrailTab.click();
        waitForPageToLoad();
    }

    @Step("Find record in audit trail")
    public void findRecord(String actionComment) {
        String comment = auditTrailItem.first().locator(auditTrailItemComment).textContent();
        assertEquals(actionComment, comment);
    }

    @Step("Wait for page to load")
    public void waitForPageToLoad() {
        page.waitForSelector(AUDIT_TRAIL_TAB_LOADING_ELEMENT, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.HIDDEN));
    }

    @Step("Get list of all audit trail items")
    public List<AuditTrailItem> getAuditTrailItems() {
        List<AuditTrailItem> auditTrailItems = new ArrayList<>();
        for (int i = 0; i < auditTrailItem.count(); i++) {
            AuditTrailItem item = new AuditTrailItem();
            item.setHeader(auditTrailItem.nth(i).locator(auditTrailItemHeader).textContent());
            item.setTime(auditTrailItem.nth(i).locator(auditTrailItemTime).textContent());
            if (auditTrailItem.nth(i).locator(auditTrailItemComment).count() > 0) {
                item.setComment(auditTrailItem.nth(i).locator(auditTrailItemComment).textContent());
            }
            if (auditTrailItem.nth(i).locator(auditTrailItemDetails).count() > 0) {
                item.setDetails(auditTrailItem.nth(i).locator(auditTrailItemDetails).innerText());
            }
            auditTrailItems.add(item);
        }
        return auditTrailItems;
    }

    @Step("Click audit trail filter button")
    public void clickAuditTrailFilter() {
        auditTrailFilterButton.click();
    }

    @Step("Select option in audit trail filter")
    public void selectAuditTrailFilter(String selection) {
        page.locator(String.format(AUDIT_TRAIL_FILTER_ITEM_PATTERN, selection)).click();
        waitForPageToLoad();
    }

    @Step("Clear audit trail filter")
    public void clearAuditTrailFilter() {
        auditTrailClearFilterButton.click();
    }
}

