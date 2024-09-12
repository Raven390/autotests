package pageObjects.backofficePages;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.ConfigFactory.BASE_URL_E2E;

import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Route;
import io.qameta.allure.Step;
import java.util.*;

public class AlertPage {
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

    public AlertPage(Page page) {
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
        this.dateRowCell = page.locator(".g-table__body .v-alert-list__column_type_date");
        this.amountRowCell = page.locator(".g-table__head .v-alert-list__column_type_amount");
        this.ruleRowCell = page.locator(".g-table__body .v-alert-list__column_type_rule");
        this.clientRowCell = page.locator(".g-table__body .v-alert-list__column_type_client");
        this.statusRowCell = page.locator(".g-table__body .v-alert-list__column_type_status");
        this.assigneeRowCell = page.locator(".g-table__body .v-alert-list__column_type_assignee");
        this.tagRowCell = page.locator(".g-table__body .v-alert-list__column_type_tag");
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
    }

    @Step("Open the BackOffice alert page")
    public void navigate() {
        page.navigate(BASE_URL_E2E);
    }

    @Step("Open the MOCKED BackOffice alert page")
    public void navigateMock() {
        page.route("**/api/alerts", route -> {
            String alert = "{\n" + "        \"id\": 1518,\n"
                    + "        \"uuid\": \"c6b6af2e-43a2-425d-bf87-ee2b6141e267\",\n"
                    + "        \"date\": \"2024-09-12T07:57:46.713048Z\",\n"
                    + "        \"amount\": {\n"
                    + "            \"value\": -235331367481903743,\n"
                    + "            \"currency\": \"Monica\"\n"
                    + "        },\n"
                    + "        \"rule\": [\n"
                    + "            \"ProctorMan\",\n"
                    + "            \"Marquez\",\n"
                    + "            \"Ramirez\",\n"
                    + "            \"Simpson\",\n"
                    + "            \"McFadden\",\n"
                    + "            \"Farley\"\n"
                    + "        ],\n"
                    + "        \"client\": {\n"
                    + "            \"id\": null,\n"
                    + "            \"regulator\": null,\n"
                    + "            \"brand\": null\n"
                    + "        },\n"
                    + "        \"status\": \"NEW\",\n"
                    + "        \"tag\": []\n"
                    + "    }";
            APIResponse response = route.fetch();
            String body = response.text();
            Map<String, String> headers = response.headers();
            route.fulfill(new Route.FulfillOptions()
                    .setResponse(response)
                    .setBody(alert)
                    .setHeaders(headers));
        });
        page.navigate(BASE_URL_E2E);
    }

    @Step("Check that user is logged in")
    public void isLoggedIn() {
        pageLogo.isVisible();
        userAvatar.isVisible();
    }

    @Step("Check that user is logged in")
    public void isNotLoggedIn() {
        assertEquals(pageLogo.count(), 0);
    }

    @Step("Check is  page basic elements visible")
    public void isAlertPageBasicElementsVisible() {
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

    @Step("check color scheme switcher")
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

    @Step("Find Proctor")
    public void findProctor() {
        page.getByText("Proctor");
    }
}
