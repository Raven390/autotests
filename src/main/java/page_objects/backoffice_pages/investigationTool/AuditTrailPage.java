package page_objects.backoffice_pages.investigationTool;

import static com.microsoft.playwright.options.WaitForSelectorState.HIDDEN;
import static com.microsoft.playwright.options.WaitForSelectorState.VISIBLE;
import static org.junit.jupiter.api.Assertions.*;
import static utils.ConfigFactory.BASE_URL_E2E;

import business_objects.ui.audit_trail.AuditTrailItem;
import business_objects.ui.audit_trail.AuditTrailItemV2;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import java.util.ArrayList;
import java.util.List;
import page_objects.backoffice_pages.AbstractPage;

public class AuditTrailPage extends AbstractPage {

    private final Locator auditTrailTab;
    private final Locator auditTrailItem;
    private final Locator auditTrailItemV2;
    private final Locator auditTrailItemHeader;
    private final Locator auditTrailItemComment;
    private final Locator auditTrailItemHeaderV2;
    private final Locator auditTrailItemDetails;
    private final Locator auditTrailItemDetailsV2;
    private final Locator auditTrailItemTime;
    private final Locator auditTrailFilterActiveButton;
    private final Locator auditTrailFilterCommentsButton;
    private final Locator auditTrailClearFilterButton;
    private final Locator auditTrailFilterTeamSelect;
    private final Locator auditTrailDetailsTitle;
    private final Locator auditTrailDetailsLabels;
    private final Locator auditTrailDetailsAttributes;
    private final Locator auditTrailDetailsAttributesNames;
    private final Locator auditTrailDetailsAttributesValues;
    private final Locator auditTrailAlertCounter;
    private final Locator auditTrailPaymentProfile;

    private static final String AUDIT_TRAIL_TAB_LOADING_ELEMENT =
            "//div[@class='v-investigation-tools-trail__skeleton-container']";
    private static final String AUDIT_TRAIL_FILTER_ITEM_PATTERN = "//div[@role='option']/descendant::*[text()='%s']";
    private static final String AUDIT_TRAIL_FILTER = "//div[@data-qa='audit_trail__filters__action']";

    public AuditTrailPage(Page page) {
        super(page);
        this.auditTrailTab = page.locator("[role=\"tab\"][title=\"Audit trail\"]");
        this.auditTrailAlertCounter = page.locator("[role='tab'][title='Audit trail'] .g-tabs__item-counter");
        this.auditTrailItem = page.locator(
                "//div[@class='v-investigation-tools-trail__item']/div/div[contains(@class,'v-timeline-item')]");
        this.auditTrailItemV2 = page.locator("//div[@class='v-audit-trail-v2-item__card']");
        this.auditTrailItemHeader = page.locator("//div[@class='v-investigation-tools-trail-card__header']");
        this.auditTrailItemComment = page.locator("//div[@class='v-investigation-tools-trail-card__comment']");
        this.auditTrailItemHeaderV2 = page.locator("//*[@class='v-audit-trail-v2-content__header']");
        this.auditTrailItemDetails = page.locator("//div[@class='v-investigation-tools-trail-card__details']/span");
        this.auditTrailItemDetailsV2 = page.locator("//div[@class='v-audit-trail-v2-content__attributes']");
        this.auditTrailItemTime = page.locator("//div[@class='v-timeline-item__time']");
        this.auditTrailClearFilterButton =
                page.locator(AUDIT_TRAIL_FILTER + "/descendant::button[@data-qa='select-clear']");
        this.auditTrailFilterActiveButton = page.locator(AUDIT_TRAIL_FILTER + "//div[text()=\"Active alerts\"]");
        this.auditTrailFilterCommentsButton = page.locator(AUDIT_TRAIL_FILTER + "//div[text()=\"Comments\"]");
        this.auditTrailFilterTeamSelect = page.locator("//div[@class='g-select v-audit-trail-v2-filters__select']");
        this.auditTrailDetailsTitle = page.locator("//*[contains(@class,'v-audit-trail-v2-details__title')]");
        this.auditTrailDetailsLabels = page.locator("//*[@class='v-audit-trail-v2-details__labels']/div");
        this.auditTrailDetailsAttributes = page.locator("//*[@class='v-audit-trail-v2-details__attribute']");
        this.auditTrailDetailsAttributesNames =
                auditTrailDetailsAttributes.locator("//*[contains(@class,'g-color-text_color_secondary')]");
        this.auditTrailDetailsAttributesValues =
                page.locator("//*[contains(@class,'v-audit-trail-v2-details__attribute-value')]");
        this.auditTrailPaymentProfile = page.locator("//div[@class='v-audit-trail-v2-content__payment-profile']");
    }

    public void navigate(String ucid) {
        Allure.step("Navigate to audit tab");
        page.navigate(String.format("%sinvestigation/%s/%s", BASE_URL_E2E, ucid, "audit"));
        waitForPageToLoad();
    }

    @Step("Open general tab")
    public void openAuditTrailTab() {
        auditTrailTab.click();
        page.waitForTimeout(1000);
        waitForPageToLoad();
    }

    @Step("Find record in audit trail")
    public void findRecord(String actionComment) {
        String comment = auditTrailItem.first().locator(auditTrailItemComment).textContent();
        assertEquals(actionComment, comment);
    }

    @Step("Wait for page to load")
    public void waitForPageToLoad() {
        page.waitForSelector(
                AUDIT_TRAIL_TAB_LOADING_ELEMENT,
                new Page.WaitForSelectorOptions().setState(WaitForSelectorState.HIDDEN));
    }

    @Step("Get list of all audit trail items")
    public List<AuditTrailItem> getAuditTrailItems() {
        waitForPageToLoad();
        List<AuditTrailItem> auditTrailItems = new ArrayList<>();
        for (int i = 0; i < auditTrailItem.count(); i++) {
            AuditTrailItem item = new AuditTrailItem();
            item.setHeader(auditTrailItem.nth(i).locator(auditTrailItemHeader).textContent());
            item.setTime(auditTrailItem.nth(i).locator(auditTrailItemTime).textContent());
            if (auditTrailItem.nth(i).locator(auditTrailItemComment).count() > 0) {
                item.setComment(
                        auditTrailItem.nth(i).locator(auditTrailItemComment).textContent());
            }
            if (auditTrailItem.nth(i).locator(auditTrailItemDetails).count() > 0) {
                item.setDetails(
                        auditTrailItem.nth(i).locator(auditTrailItemDetails).innerText());
            }
            auditTrailItems.add(item);
        }
        return auditTrailItems;
    }

    @Step("Get list of all audit trail items v2")
    public List<AuditTrailItemV2> getAuditTrailItemsV2() {
        waitForPageToLoad();
        page.waitForCondition(() -> auditTrailItemV2.count() > 0, new Page.WaitForConditionOptions().setTimeout(5000));
        List<AuditTrailItemV2> auditTrailItems = new ArrayList<>();
        for (int i = 0; i < auditTrailItemV2.count(); i++) {
            AuditTrailItemV2 item = new AuditTrailItemV2();
            item.setHeader(
                    auditTrailItemV2.nth(i).locator(auditTrailItemHeaderV2).innerText());
            if (auditTrailItemV2.nth(i).locator(auditTrailItemDetailsV2).count() > 0) {
                item.setDetails(
                        auditTrailItemV2.nth(i).locator(auditTrailItemDetailsV2).innerText());
            }
            auditTrailItems.add(item);
        }
        return auditTrailItems;
    }

    @Step("Click active alerts audit filter button")
    public void clickAuditTrailActiveFilter() {
        auditTrailFilterActiveButton.click();
    }

    @Step("Click comments audit filter button")
    public void clickAuditTrailCommentsFilter() {
        auditTrailFilterCommentsButton.click();
    }

    @Step("Click team audit filter select")
    public void clickAuditTrailTeamFilter() {
        auditTrailFilterTeamSelect.click();
    }

    @Step("Select option in audit trail team filter")
    public void selectAuditTrailTeamFilter(String selection) {
        page.locator(String.format(AUDIT_TRAIL_FILTER_ITEM_PATTERN, selection)).click();
        waitForPageToLoad();
    }

    @Step("Clear audit trail filter")
    public void clearAuditTrailFilter() {
        auditTrailClearFilterButton.click();
    }

    public void isAuditTabHidden() {
        Allure.step("check is audit tab hidden");
        auditTrailTab.waitFor(new Locator.WaitForOptions().setState(HIDDEN));
    }

    public void isAuditTabVisible() {
        Allure.step("check is audit tab hidden");
        auditTrailTab.waitFor(new Locator.WaitForOptions().setState(VISIBLE));
    }

    @Step("Click first audit trail card")
    public void clickFirstAuditCard() {
        auditTrailItemV2.first().click();
    }

    @Step("Get audit trail details drawer title")
    public String getAuditTrailDetailsTitle() {
        return auditTrailDetailsTitle.textContent();
    }

    @Step("Get audit trail details drawer labels")
    public List<String> getAuditTrailDetailsLabels() {
        List<String> labels = new ArrayList<>();
        for (int i = 0; i < auditTrailDetailsLabels.count(); i++) {
            labels.add(auditTrailDetailsLabels.nth(i).textContent());
        }
        return labels;
    }

    @Step("Get audit trail details drawer attribute names")
    public List<String> getAuditTrailDetailsAttributeNames() {
        List<String> names = new ArrayList<>();
        for (int i = 0; i < auditTrailDetailsAttributesNames.count(); i++) {
            names.add(auditTrailDetailsAttributesNames.nth(i).textContent());
        }
        return names;
    }

    @Step("Get audit trail details drawer attribute values")
    public List<String> getAuditTrailDetailsAttributeValues() {
        List<String> values = new ArrayList<>();
        for (int i = 0; i < auditTrailDetailsAttributesValues.count(); i++) {
            values.add(auditTrailDetailsAttributesValues.nth(i).textContent());
        }
        return values;
    }

    @Step("Check audit trail alert counter")
    public void checkAuditTrailAlertCounter(int expectedAlertsCount) {
        waitForPageToLoad();
        auditTrailAlertCounter.waitFor(new Locator.WaitForOptions().setState(VISIBLE));
        int n = 0;
        while (expectedAlertsCount != Integer.parseInt(auditTrailAlertCounter.textContent()) || n < 50) {
            page.waitForTimeout(100);
            n++;
        }
        assertEquals(expectedAlertsCount, Integer.valueOf(auditTrailAlertCounter.textContent()));
    }

    @Step("Click payment profile by name")
    public void clickPaymentProfileByName(String paymentProfile) {
        auditTrailPaymentProfile.getByText(paymentProfile).first().click();
    }
}
