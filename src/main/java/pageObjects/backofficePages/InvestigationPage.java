package pageObjects.backofficePages;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.ConfigFactory.BASE_URL_E2E;
import static utils.TestUtils.comparePageScreenshotWithBaseline;

import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Route;
import io.qameta.allure.Step;
import java.util.*;

public class InvestigationPage {
    private final Page page;
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
    private final Locator loaderAnimation;
    private final Locator susClientSection;
    private final Locator susClientList;
    private final Locator assignToMeFilter;
    private final Locator unassignedFilter;
    private final Locator allSusClientsFilter;
    private final Locator susClientSectionFoldButton;
    private final Locator susClientSectionFoldButtonFolded;
    private final Locator susClientSectionFolded;

    public InvestigationPage(Page page) {
        this.page = page;
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
        this.loaderAnimation = page.locator(".v-loader");
        this.susClientSection = page.locator("[data-qa=\"investigation_page__suspicious_clients_container\"]");
        this.susClientSectionFolded = page.locator(".v-investigation-tools-side-panel_collapsed[data-qa=\"investigation_page__suspicious_clients_container\"]");
        this.susClientList = page.locator("[data-qa=\"investigation_page__suspicious_clients_list\"]");
        this.assignToMeFilter = page.locator("[data-qa=\"investigation_page__suspicious_clients_buttons\"] [value=\"MY\"]");
        this.unassignedFilter = page.locator("[data-qa=\"investigation_page__suspicious_clients_buttons\"] [value=\"UNASSIGNED\"]");
        this.allSusClientsFilter = page.locator("[data-qa=\"investigation_page__suspicious_clients_buttons\"] [value=\"ALL\"]");
        this.susClientSectionFoldButton = page.locator("[data-qa=\"investigation_page__side_panel_toggler\"]");
        this.susClientSectionFoldButtonFolded = page.locator(".v-investigation-tools-side-panel__toggler_collapsed [data-qa=\"investigation_page__side_panel_toggler\"]");
    }

    @Step("Open the BackOffice main page")
    public void navigate() {
        page.navigate(BASE_URL_E2E);
        isPageLoaded();
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
        isPageLoaded();
        page.evaluate("document.querySelector('.v-alert-list__cell_date .g-text_variant_body-1').innerText = 'YESTERDAY'");
    }

    @Step("Check that user is logged in")
    public void isLoggedIn() {
        isPageLoaded();
        pageLogo.isVisible();
        userAvatar.isVisible();
    }

    @Step("Check that user is logged in")
    public void isNotLoggedIn() {
        isPageLoaded();
        assertEquals(pageLogo.count(), 0);
    }

    @Step("Check is  page basic elements visible")
    public void isAlertPageBasicElementsVisible() {
        isPageLoaded();
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

    @Step("unfold suspicious clients side menu folds")
    public void unfoldSusClientFoldSection() {
        susClientSectionFoldButtonFolded.isVisible();
        susClientSectionFolded.isVisible();
        susClientSectionFoldButtonFolded.click();
    }

    @Step("fold suspicious clients side menu folds")
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

    @Step("Check if the page loaded")
    public void isPageLoaded() {
        int n = 0;
        page.waitForTimeout(2000);
        while (loaderAnimation.isVisible() && n < 8) {
            page.waitForTimeout(2000);
            n += 1;
        }
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

    @Step("filter assigned to me")
    public void filterAssignedMe() {
        assignToMeFilter.click();
        assignToMeFilter.locator("[aria-checked=\"true\"]").isVisible();
    }

    @Step("filter unassigned")
    public void filterUnassigned() {
        unassignedFilter.click();
        unassignedFilter.locator("[aria-checked=\"true\"]").isVisible();
    }

    @Step("filter all")
    public void filterAll() {
        allSusClientsFilter.click();
        allSusClientsFilter.locator("[aria-checked=\"true\"]").isVisible();
    }

    @Deprecated//need to update logic of mock

    @Step("Compare alert page with baseline screenshots")
    public void compareAlertPageWithBaseline(Page page, String baselinePath) {
        isPageLoaded();
        comparePageScreenshotWithBaseline(page, baselinePath);
    }

    @Step("navigate to client")
    public void navigateToClient(String ucid) {
        page.navigate("http://k8s-test-nginxrev-55e209d446-410128713.us-east-1.elb.amazonaws.com/investigation?client_ucid=" + ucid);
        isPageLoaded();
    }

}
