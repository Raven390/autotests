package pageObjects.backofficePages;

import static helpers.database.DbHelper.getObjectsFromDB;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static utils.ConfigFactory.BASE_URL_E2E;
import static utils.TestUtils.comparePageScreenshotWithBaseline;

import businessObjects.db.auditServiceDb.Event;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Route;
import helpers.database.DbName;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import java.util.*;

public class InvestigationPage extends AbstractPage {
    private final Locator pageLogo;
    private final Locator userAvatar;
    private final Locator alertList;
    private final Locator sideBarButton;
    private final Locator sideBarMenuUnfolded;
    private final Locator sideBarMenuFolded;
    private final Locator dateRowHeader;
    private final Locator amountRowHeader;
    private final Locator ruleRowHeader;
    private final Locator clientRowHeader;
    private final Locator statusRowHeader;
    private final Locator assigneeRowHeader;
    private final Locator tagRowHeader;
    private final Locator dateRowCell;
    private final Locator amountRowCell;
    private final Locator ruleRowCell;
    private final Locator clientRowCell;
    private final Locator statusRowCell;
    private final Locator assigneeRowCell;
    private final Locator tagRowCell;
    private final Locator soundButton;
    private final Locator refreshButton;
    private final Locator profileButton;
    private final Locator settingButton;
    private final Locator supportButton;
    private final Locator bugReportButton;
    private final Locator breadcrumbs;
    private final Locator darkThemeButton;
    private final Locator lightThemeButton;
    private final Locator darkBody;
    private final Locator lightBody;
    private final Locator susClientSection;
    private final Locator susClientList;
    private final Locator assignToMeFilter;
    private final Locator unassignedFilter;
    private final Locator allSusClientsFilter;
    private final Locator susClientSectionFoldButton;
    private final Locator susClientSectionFoldButtonFolded;
    private final Locator susClientSectionFolded;
    private final Locator addCommentButton;
    private final Locator addCommentSubmitButton;
    private final Locator addCommentPopup;
    private final Locator addCommentInput;
    private final Locator addCommentDangerToast;
    private final Locator successToast;
    private final Locator investigateButtonList;
    private final Locator infoToast;
    private final Locator suspiciousClientsList;
    private final Locator investigateButton;

    public InvestigationPage(Page page) {
        super(page);
        this.pageLogo = page.locator(".gn-aside-header__header .gn-logo");
        this.userAvatar = page.locator(".v-aside-header-footer .g-avatar__icon");
        this.alertList = page.locator(".gn-aside-header__content .v-app-layout__content");
        this.sideBarButton = page.locator(".v-aside-header-menu-item__icon-place .g-button");
        this.sideBarMenuFolded = page.locator(".gn-aside-header.gn-aside-header_compact");
        this.sideBarMenuUnfolded = page.locator("gn-aside-header.v-aside-header");
        this.dateRowHeader = page.locator(".g-table__head .v-alert-list__column_type_date");
        this.amountRowHeader = page.locator(".g-table__head .v-alert-list__column_type_amount");
        this.ruleRowHeader = page.locator(".g-table__head .v-alert-list__column_type_rule");
        this.clientRowHeader = page.locator(".g-table__head .v-alert-list__column_type_client");
        this.statusRowHeader = page.locator(".g-table__head .v-alert-list__column_type_status");
        this.assigneeRowHeader = page.locator(".g-table__head .v-alert-list__column_type_assignee");
        this.tagRowHeader = page.locator(".g-table__body .v-alert-list__column_type_tag");
        this.dateRowCell = page.locator(".g-table__body .v-alert-list__column_type_date").first();
        this.amountRowCell = page.locator(".g-table__head .v-alert-list__column_type_amount").first();
        this.ruleRowCell = page.locator(".g-table__body .v-alert-list__column_type_rule").first();
        this.clientRowCell = page.locator(".g-table__body .v-alert-list__column_type_client").first();
        this.statusRowCell = page.locator(".g-table__body .v-alert-list__column_type_status").first();
        this.assigneeRowCell = page.locator(".g-table__body .v-alert-list__column_type_assignee").first();
        this.tagRowCell = page.locator(".g-table__body .v-alert-list__column_type_tag").first();
        this.soundButton = page.locator(".soundButton"); // not implemented
        this.refreshButton = page.locator(".refreshButton"); // not implemented
        this.profileButton = page.locator(".profileButton"); // not implemented-dummy
        this.settingButton = page.locator(".settingButton"); // not implemented
        this.supportButton = page.locator(".supportButton"); // not implemented
        this.bugReportButton = page.locator(".BugReportButton"); // not implemented
        this.breadcrumbs = page.locator(".breadcrumbs"); // not implemented
        this.darkThemeButton = page.locator(".g-radio-button__option-control[value=\"dark\"]");
        this.lightThemeButton = page.locator(".g-radio-button__option-control[value=\"light\"]");
        this.darkBody = page.locator(".g-root.g-root_theme_dark");
        this.lightBody = page.locator(".g-root.g-root_theme_light");
        this.susClientSection = page.locator("[data-qa=\"investigation_page__suspicious_clients_container\"]");
        this.susClientSectionFolded = page.locator(".v-investigation-tools-side-panel_collapsed[data-qa=\"investigation_page__suspicious_clients_container\"]");
        this.susClientList = page.locator("[data-qa=\"investigation_page__suspicious_clients_list\"]");
        this.assignToMeFilter = page.locator("[data-qa=\"investigation_page__suspicious_clients_buttons\"] [value=\"MY\"]");
        this.unassignedFilter = page.locator("[data-qa=\"investigation_page__suspicious_clients_buttons\"] [value=\"UNASSIGNED\"]");
        this.allSusClientsFilter = page.locator("[data-qa=\"investigation_page__suspicious_clients_buttons\"] [value=\"ALL\"]");
        this.susClientSectionFoldButton = page.locator("[data-qa=\"investigation_page__side_panel_toggler\"]");
        this.susClientSectionFoldButtonFolded = page.locator(".v-investigation-tools-side-panel__toggler_collapsed [data-qa=\"investigation_page__side_panel_toggler\"]");
        this.addCommentButton = page.locator("[data-qa=\"investigation_tools__add_comment_button\"]");
        this.addCommentSubmitButton = page.locator("[data-qa=\"investigation_tools__add_comment_submit_button\"]");
        this.addCommentPopup = page.locator("[data-qa=\"investigation_tools__add_comment_popup\"]");
        this.addCommentInput = page.locator("[data-qa=\"investigation_tools__add_comment_textarea_container\"] textarea");
        this.addCommentDangerToast = page.locator(".g-toast_theme_danger");
        this.successToast = page.locator(".g-toast_theme_success");
        this.infoToast = page.locator(".g-toast_theme_info");
        this.investigateButtonList = page.locator("[data-qa=\"investigation_tools__client_card_assign_button\"]");
        this.suspiciousClientsList = page.locator("[data-qa=\"investigation_page__suspicious_clients_list\"]");
        this.investigateButton = page.locator(".g-button__text").getByText("Investigate");

    }

    @Step("Open the BackOffice main page")
    public void navigate() {
        page.navigate(BASE_URL_E2E);
        waitForPageToLoad();
    }

    @Step("Open the MOCKED BackOffice main page")
    public void navigateMock() {
        page.route("**/api/alerts", route -> {
            String alert = "{\n" + "        \"id\": 1518,\n" + "        \"uuid\": \"c6b6af2e-43a2-425d-bf87-ee2b6141e267\",\n" + "        \"date\": \"2024-09-12T07:57:46.713048Z\",\n" + "        \"amount\": {\n" + "            \"value\": -235331367481903743,\n" + "            \"currency\": \"Monica\"\n" + "        },\n" + "        \"rule\": [\n" + "            \"ProctorMan\",\n" + "            \"Marquez\",\n" + "            \"Ramirez\",\n" + "            \"Simpson\",\n" + "            \"McFadden\",\n" + "            \"Farley\"\n" + "        ],\n" + "        \"client\": {\n" + "            \"id\": null,\n" + "            \"regulator\": null,\n" + "            \"brand\": null\n" + "        },\n" + "        \"status\": \"NEW\",\n" + "        \"tag\": []\n" + "    }";
            APIResponse response = route.fetch();
            String body = response.text();
            Map<String, String> headers = response.headers();
            route.fulfill(new Route.FulfillOptions().setResponse(response).setBody(alert).setHeaders(headers));
        });
        page.navigate(BASE_URL_E2E);
        waitForPageToLoad();
        page.evaluate("document.querySelector('.v-alert-list__cell_date .g-text_variant_body-1').innerText = 'YESTERDAY'");
    }

    @Step("Check that user is logged in")
    public void isLoggedIn() {
        waitForPageToLoad();
        pageLogo.isVisible();
        userAvatar.isVisible();
    }

    @Step("Check that user is logged in")
    public void isNotLoggedIn() {
        waitForPageToLoad();
        assertEquals(pageLogo.count(), 0);
    }

    @Step("Check is  page basic elements visible")
    public void isAlertPageBasicElementsVisible() {
        waitForPageToLoad();
        alertList.isVisible();
        dateRowHeader.isVisible();
        amountRowHeader.isVisible();
        ruleRowHeader.isVisible();
        clientRowHeader.isVisible();
        statusRowHeader.isVisible();
        assigneeRowHeader.isVisible();
        tagRowHeader.isVisible();
        amountRowCell.isVisible();
        ruleRowCell.isVisible();
        clientRowCell.isVisible();
        statusRowCell.isVisible();
        assigneeRowCell.isVisible();
        tagRowCell.isVisible();
        userAvatar.isVisible();
        sideBarButton.isVisible();
        pageLogo.isVisible();
    }

    @Step("Check is  side menu folds")
    public void sideMenuFoldButtonTest() throws InterruptedException {
        sideBarButton.isVisible();
        pageLogo.isVisible();
        alertList.isVisible();
        if (sideBarMenuUnfolded.isVisible()) {
            sideBarButton.click();
            sideBarMenuFolded.isVisible();

        } else {
            sideBarMenuFolded.isVisible();
            sideBarButton.click();
            sideBarMenuUnfolded.isVisible();
        }
    }

    @Step("unfold if folded suspicious clients side menu folded")
    public void unfoldSusClientSectionIfFolded() {
        susClientSectionFoldButton.isVisible();
        pageLogo.isVisible();
        susClientSection.isVisible();
        if (susClientSectionFolded.isVisible()) {
            susClientSectionFoldButtonFolded.click();
        } else {
            page.waitForTimeout(1);
        }
    }

    @Step("Unfold suspicious clients side menu folds")
    public void unfoldSusClientFoldSection() {
        susClientSectionFoldButtonFolded.isVisible();
        susClientSectionFolded.isVisible();
        susClientSectionFoldButtonFolded.click();
    }

    @Step("Fold suspicious clients side menu folds")
    public void foldSusClientFoldSection() {
        susClientSectionFoldButton.isVisible();
        susClientSection.isVisible();
        susClientSectionFoldButton.click();
        susClientSectionFoldButtonFolded.isVisible();
        susClientSectionFolded.isVisible();
    }

    @Step("Unfold sidebar")
    public void unfoldSidebar() {
        sideBarButton.isVisible();
        pageLogo.isVisible();
        if (sideBarMenuFolded.isVisible()) {
            sideBarButton.click();
            sideBarMenuUnfolded.isVisible();
        }
    }

    @Step("Fold sidebar")
    public void foldSidebar() {
        sideBarButton.isVisible();
        pageLogo.isVisible();
        if (sideBarMenuUnfolded.isVisible()) {
            sideBarButton.click();
            sideBarMenuFolded.isVisible();
        }
    }

    @Step("Check color scheme switcher")
    public void colorThemeSwitch() {
        lightThemeButton.click();
        lightBody.isVisible();
        darkThemeButton.click();
        darkBody.isVisible();
    }

    @Step("Check is profile button on the  page is visible")
    public void isProfileButtonVisible() {
        profileButton.isVisible();
    }

    @Step("Click profile button")
    public void clickProfileButton() {
        profileButton.click();
    }

    @Step("Switch to the Light mode")
    public void turnLightMode() {
        lightThemeButton.click();
        lightBody.isVisible();
    }

    @Step("Switch to the Light mode")
    public void turnDarkMode() {
        darkThemeButton.click();
        darkBody.isVisible();
    }

    @Step("Filter assigned to me")
    public void filterAssignedMe() {
        assignToMeFilter.click();
        assignToMeFilter.locator("[aria-checked=\"true\"]").isVisible();
    }

    @Step("Filter unassigned")
    public void filterUnassigned() {
        unassignedFilter.click();
        unassignedFilter.locator("[aria-checked=\"true\"]").isVisible();
    }

    @Step("Filter all")
    public void filterAll() {
        allSusClientsFilter.click();
        allSusClientsFilter.locator("[aria-checked=\"true\"]").isVisible();
    }

    @Deprecated//need to update logic of mock

    @Step("Compare alert page with baseline screenshots")
    public void compareAlertPageWithBaseline(Page page, String baselinePath) {
        waitForPageToLoad();
        comparePageScreenshotWithBaseline(page, baselinePath);
    }

    @Step("Navigate to client")
    public void navigateToClient(String ucid) {
        page.navigate("http://k8s-test-nginxrev-55e209d446-410128713.us-east-1.elb.amazonaws.com/investigation?client_ucid=" + ucid);
        waitForPageToLoad();
    }

    @Step("Mock comment api to return error")
    public void mockCommentError(String ucid) {
        page.route("**/api/clients/" + ucid + "/comments", route -> {
            APIResponse response = route.fetch();
            Map<String, String> headers = response.headers();
            route.fulfill(new Route.FulfillOptions().setResponse(response).setBody("500").setHeaders(headers).setStatus(500));
        });
    }

    @Step("Open add comment form")
    public void openCommentForm() {
        Allure.step("Open add comment form");
        addCommentButton.click();
        assertTrue(addCommentPopup.isVisible());
    }

    @Step("Fill add comment form")
    public void fillCommentForm(String comment) {
        Allure.step("Fill add comment form");
        addCommentInput.fill(comment);
        addCommentInput.textContent().contains(comment);
    }

    @Step("Submit comment form with error")
    public void submitCommentFormError() {
        Allure.step("Submit comment form with error");
        addCommentSubmitButton.click();
        String message = addCommentDangerToast.textContent();
        assertEquals("Oops! Something went wrong while adding your comment. Please try again.", message);
    }

    @Step("Submit comment form")
    public void submitCommentForm() {
        Allure.step("Submit comment form");
        addCommentSubmitButton.click();
        String message = successToast.textContent();
        assertEquals("Comment added to Audit trail", message);
    }

    @Step("take client to investigation from the alert list")
    public void investigateUserAlertList(String userId) {
        Allure.step("take client to investigation from the alert list");
        int attempts = 0;
        while ((!page.locator("//*[@data-qa=\"investigation_page__suspicious_client_card\"]/descendant::div[text()='" + userId + "']").isVisible()) && attempts < 50) {
            suspiciousClientsList.hover();//.evaluate("e => e.scrollTop += 100");
            page.mouse().wheel(0, 10);
            attempts++;
        }
        page.locator("//*[@data-qa=\"investigation_page__suspicious_client_card\"]/descendant::div[text()='" + userId + "']").hover();
        page.locator("//div[text()='" + userId + "']/ancestor::div[@data-qa=\"investigation_page__suspicious_client_card\"]/descendant::button[@data-qa=\"investigation_tools__client_card_assign_button\"]").click();
        String message = infoToast.textContent();
        assertEquals("Client investigation started", message);
    }

    @Step("take client to investigation from the client card")
    public void investigateClientCard() {
        Allure.step("take client to investigation from the from the client card");
        investigateButton.click();
        String message = infoToast.textContent();
        assertEquals("Client investigation started", message);
    }

    public void checkInvestigationAssigmentAudit(String ucid) throws Exception {
        Allure.step("check assigment event in Audit DB");
        List<Event> event = getObjectsFromDB(DbName.AUDIT, "au.au.event", "ucid = '" + ucid + "'", Event.class);
        String type = event.get(1).getType();
        assertEquals("CLIENT_ASSIGNED", type);
        String system = event.get(1).getInitiatedBySystem();
        assertEquals("Vindex BO", system);
    }

}
