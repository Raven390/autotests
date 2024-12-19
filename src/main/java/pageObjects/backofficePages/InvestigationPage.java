package pageObjects.backofficePages;

import static helpers.database.DbHelper.getObjectsFromDB;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static utils.ConfigFactory.BASE_URL_E2E;
import static utils.Constants.VANTAGE_BRAND_IMAGE_SRC;
import static utils.TestUtils.comparePageScreenshotWithBaseline;

import businessObjects.db.auditServiceDb.Event;
import businessObjects.ui.user.User;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;
import helpers.database.DbName;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private final Locator clientContainer;
    private final Locator brandImage;
    private final Locator countryCodeElement;
    private final Locator clientIdElement;
    private final Locator investigationStatusElement;
    private final Locator clientAssignmentElement;
    private final Locator clientCardTimerElement;
    private final Locator clientCardAlertsCountElement;
    private final Locator currentTabCardsCountElement;
    private final Locator suspiciousClientsFilterIcon;
    private final Locator applyFilterButton;
    private final Locator showMoreRulesButton;
    private final Locator ruleSearchInput;
    private final Locator resetBrandsButton;
    private final Locator resetRulesButton;
    private final Locator resetCountriesButton;
    private final Locator resetAssigneeButton;
    private final Locator brandButtons;
    private final Locator ruleCheckboxes;
    private final Locator countryCheckboxes;
    private final Locator assigneeCheckboxes;
    private final Locator assignButton;

    private final String CLIENT_LIST_LOADING = "//div[@class='v-suspicious-client-list-skeleton']";
    private final String FILTER_BUTTON_BY_TEXT_PATTERN = "//span[text()='%s']/parent::button";
    private final String CHECKBOX_BY_VALUE_PATTERN = "//input[@value='%s' and @type='checkbox']";
    private final String CLIENT_CARD_BY_CLIENT_ID_PATTERN = "//div[text()='%s']/ancestor::div[contains(@data-qa,'investigation_page__suspicious_client_card')]";
    private final String FILTER_LOADING = "//div[@class='v-investigation-tools-side-panel__filters']/button[contains(@class,'g-button_loading')]";

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
        this.darkThemeButton = page.locator(".g-radio-button__option-control[value='dark']");
        this.lightThemeButton = page.locator(".g-radio-button__option-control[value='light']");
        this.darkBody = page.locator(".g-root.g-root_theme_dark");
        this.lightBody = page.locator(".g-root.g-root_theme_light");
        this.susClientSection = page.locator("[data-qa='investigation_page__suspicious_clients_container']");
        this.susClientSectionFolded = page.locator(".v-investigation-tools-side-panel_collapsed[data-qa='investigation_page__suspicious_clients_container']");
        this.susClientList = page.locator("[data-qa='investigation_page__suspicious_clients_list']");
        this.assignToMeFilter = page.locator("[data-qa='investigation_page__suspicious_clients_buttons'] [value='MY']");
        this.unassignedFilter = page.locator("[data-qa='investigation_page__suspicious_clients_buttons'] [value='UNASSIGNED']");
        this.allSusClientsFilter = page.locator("[data-qa='investigation_page__suspicious_clients_buttons'] [value='ALL']");
        this.susClientSectionFoldButton = page.locator("[data-qa='investigation_page__side_panel_toggler']");
        this.susClientSectionFoldButtonFolded = page.locator(".v-investigation-tools-side-panel__toggler_collapsed [data-qa='investigation_page__side_panel_toggler']");
        this.addCommentButton = page.locator("[data-qa='investigation_tools__add_comment_button']");
        this.addCommentSubmitButton = page.locator("[data-qa='investigation_tools__add_comment_submit_button']");
        this.addCommentPopup = page.locator("[data-qa='investigation_tools__add_comment_popup']");
        this.addCommentInput = page.locator("[data-qa='investigation_tools__add_comment_textarea_container'] textarea");
        this.addCommentDangerToast = page.locator(".g-toast_theme_danger");
        this.successToast = page.locator(".g-toast_theme_success");
        this.infoToast = page.locator(".g-toast_theme_info");
        this.investigateButtonList = page.locator("[data-qa='investigation_tools__client_card_assign_button']");
        this.suspiciousClientsList = page.locator("[data-qa='investigation_page__suspicious_clients_list']");
        this.investigateButton = page.locator(".g-button__text").getByText("Investigate");
        this.clientContainer = page.locator("//*[@data-qa='data_item_wrapper_container']");
        this.brandImage = page.locator("//img[@class='g-avatar__image']");
        this.countryCodeElement = page.locator("//span[contains(@class,'g-text')]");
        this.clientIdElement = page.locator("//div[contains(@class,'g-text_variant_subheader-1')]");
        this.investigationStatusElement = page.locator("//div[contains(@class,'v-suspicious-client-card__status')]");
        this.clientAssignmentElement = page.locator("//div[contains(@class,'v-suspicious-client-card__assigned-user')]");
        this.clientCardTimerElement = page.locator("//div[contains(@class,'v-suspicious-client-card__timer')]");
        this.clientCardAlertsCountElement = page.locator("//div[contains(@class,'v-suspicious-client-card__alerts-count')]");
        this.currentTabCardsCountElement = page.locator("//label[contains(@class,'g-radio-button__option_checked')]/descendant::span[contains(@class,'g-color-text_color_hint')]");
        this.suspiciousClientsFilterIcon = page.locator("//div[@class='v-investigation-tools-side-panel__filters']");
        this.applyFilterButton = page.locator("//button[contains(@class,'g-button_width_max')]");
        this.showMoreRulesButton = page.locator("//span[text()='Show more']/..");
        this.ruleSearchInput = page.locator("//input[@placeholder='Search by rule']");
        this.resetBrandsButton = page.locator("//div[@data-qa='suspicious_client_filters__brands']/descendant::span[text()='Reset']");
        this.resetRulesButton = page.locator("//div[@data-qa='suspicious_client_filters__rules']/descendant::span[text()='Reset']");
        this.resetCountriesButton = page.locator("//div[@data-qa='suspicious_client_filters__countries']/descendant::span[text()='Reset']");
        this.resetAssigneeButton = page.locator("//div[@data-qa='suspicious_client_filters__assignees']/descendant::span[text()='Reset']");
        this.brandButtons = page.locator("//div[@data-qa='suspicious_client_filters__brands']/descendant::button[contains(@class,'g-button_size_m')]");
        this.ruleCheckboxes = page.locator("//div[@data-qa='suspicious_client_filters__rules']/descendant::label[contains(@class,'g-checkbox')]");
        this.countryCheckboxes = page.locator("//div[@data-qa='suspicious_client_filters__countries']/descendant::label[contains(@class,'g-checkbox')]");
        this.assigneeCheckboxes = page.locator("//div[@data-qa='suspicious_client_filters__assignees']/descendant::label[contains(@class,'g-checkbox')]");
        this.assignButton = page.locator("//button[@data-qa='investigation_tools__client_card_assign_button']");
    }

    @Step("Open the BackOffice main page")
    public void navigate() {
        page.navigate(BASE_URL_E2E);
        super.waitForPageToLoad();
    }

    @Step("Open the MOCKED BackOffice main page")
    public void navigateMock() {
        page.route("**/api/alerts", route -> {
            String alert = "{\n" + "        'id': 1518,\n" + "        'uuid': 'c6b6af2e-43a2-425d-bf87-ee2b6141e267',\n" + "        'date': '2024-09-12T07:57:46.713048Z',\n" + "        'amount': {\n" + "            'value': -235331367481903743,\n" + "            'currency': 'Monica'\n" + "        },\n" + "        'rule': [\n" + "            'ProctorMan',\n" + "            'Marquez',\n" + "            'Ramirez',\n" + "            'Simpson',\n" + "            'McFadden',\n" + "            'Farley'\n" + "        ],\n" + "        'client': {\n" + "            'id': null,\n" + "            'regulator': null,\n" + "            'brand': null\n" + "        },\n" + "        'status': 'NEW',\n" + "        'tag': []\n" + "    }";
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
    public void sideMenuFoldButtonTest() {
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
        assignToMeFilter.locator("[aria-checked='true']").isVisible();
    }

    @Step("Filter unassigned")
    public void filterUnassigned() {
        unassignedFilter.click();
        unassignedFilter.locator("[aria-checked='true']").isVisible();
    }

    @Step("Filter all")
    public void filterAll() {
        allSusClientsFilter.click();
        allSusClientsFilter.locator("[aria-checked='true']").isVisible();
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
        while ((!page.locator("//*[@data-qa='investigation_page__suspicious_client_card']/descendant::div[text()='" + userId + "']").isVisible()) && attempts < 50) {
            suspiciousClientsList.hover();//.evaluate("e => e.scrollTop += 100");
            page.mouse().wheel(0, 100);
//            page.waitForTimeout(500);
            attempts++;
        }
        page.locator("//*[@data-qa='investigation_page__suspicious_client_card']/descendant::div[text()='" + userId + "']").hover();
        page.locator("//div[text()='" + userId + "']/ancestor::div[@data-qa='investigation_page__suspicious_client_card']/descendant::button[@data-qa='investigation_tools__client_card_assign_button']").click();
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

    @Step("Verify each client card has a brand image")
    public void verifyEachClientHasBrandImg() {
        assertThat(clientContainer.count(), greaterThan(0));
        for (int i = 0; i < clientContainer.count(); i++) {
            Locator child = clientContainer.nth(i).locator(brandImage);
            assertThat(child.count(), not(equalTo(0)));
        }
    }

    @Step("Verify each client card has a country code")
    public void verifyEachClientHasCountryCode() {
        assertThat(clientContainer.count(), greaterThan(0));
        List<String> clientsCountryList = new ArrayList<>();
        for (int i = 0; i < clientContainer.count(); i++) {
            Locator child = clientContainer.nth(i).locator(countryCodeElement);
            assertThat(child.count(), not(equalTo(0)));
            clientsCountryList.add(child.textContent());
        }
        String iso2Pattern = "^[A-Z]{2}$";
        clientsCountryList.forEach(country -> assertThat(String.format("Assert that country '%s' matches iso2 format", country), country.matches(iso2Pattern))
        );
    }

    @Step("Verify each client card has a client id")
    public void verifyEachClientHasClientId() {
        assertThat(clientContainer.count(), greaterThan(0));
        List<String> clientIdList = new ArrayList<>();
        for (int i = 0; i < clientContainer.count(); i++) {
            Locator child = clientContainer.nth(i).locator(clientIdElement);
            assertThat(child.count(), not(equalTo(0)));
            clientIdList.add(child.textContent());
        }
        String positiveIntsPattern = "^[1-9]\\d*$";
        clientIdList.forEach(clientId -> assertThat(String.format("Assert that clientId '%s' is a positive int", clientId), clientId.matches(positiveIntsPattern))
        );
    }

    @Step("Verify each client card has any investigation status")
    public void verifyEachClientHasAnyInvestigationStatus() {
        assertThat(clientContainer.count(), greaterThan(0));
        List<String> investigationStatusList = new ArrayList<>();
        for (int i = 0; i < clientContainer.count(); i++) {
            Locator child = clientContainer.nth(i).locator(investigationStatusElement);
            assertThat(child.count(), not(equalTo(0)));
            investigationStatusList.add(child.textContent());
        }
        investigationStatusList.forEach(status -> assertThat(String.format("Assert that investigation status '%s' has value in ['Investigating', 'Suspicious']", status), status, anyOf(is("Investigating"), is("Suspicious"))
        )
        );
    }

    @Step("Verify each client card has investigation status 'Investigating'")
    public void verifyEachClientHasInvestigationStatusInvestigating() {
        assertThat(clientContainer.count(), greaterThan(0));
        List<String> investigationStatusList = new ArrayList<>();
        for (int i = 0; i < clientContainer.count(); i++) {
            Locator child = clientContainer.nth(i).locator(investigationStatusElement);
            assertThat(child.count(), not(equalTo(0)));
            investigationStatusList.add(child.textContent());
        }
        investigationStatusList.forEach(status -> assertThat(String.format("Assert that investigation status '%s' has value 'Investigating'", status), status, (is("Investigating"))
        )
        );
    }

    @Step("Verify each client card has investigation status 'Investigating'")
    public void verifyEachClientAssignedToUser(User user) {
        assertThat(clientContainer.count(), greaterThan(0));
        List<String> assignmentList = new ArrayList<>();
        for (int i = 0; i < clientContainer.count(); i++) {
            Locator child = clientContainer.nth(i).locator(clientAssignmentElement);
            assertThat(child.count(), not(equalTo(0)));
            assignmentList.add(child.textContent());
        }
        assignmentList.forEach(assignee -> assertThat("Assert that assignment element is present", assignee, (is(String.format("%s %s", user.getFirstName(), user.getLastName())))
        )
        );
    }

    @Step("Verify each client card has a card timer")
    public void verifyEachClientHasCardTimer() {
        assertThat(clientContainer.count(), greaterThan(0));
        List<String> cardTimerList = new ArrayList<>();
        for (int i = 0; i < clientContainer.count(); i++) {
            Locator child = clientContainer.nth(i).locator(clientCardTimerElement);
            assertThat(child.count(), not(equalTo(0)));
            cardTimerList.add(child.textContent());
        }
        String timerPattern = "^(\\d{1,2}:\\d{2}(:\\d{2})?|(\\d{1,2}d \\d{1,2}h))$";
        cardTimerList.forEach(timer -> assertThat(String.format("Assert that card timer '%s' is a positive int", timer), timer.matches(timerPattern))
        );
    }

    @Step("Verify each client card has an alert count")
    public void verifyEachClientHasAlertCount() {
        assertThat(clientContainer.count(), greaterThan(0));
        List<String> alertCountList = new ArrayList<>();
        for (int i = 0; i < clientContainer.count(); i++) {
            Locator child = clientContainer.nth(i).locator(clientCardAlertsCountElement);
            assertThat(child.count(), not(equalTo(0)));
            alertCountList.add(child.textContent());
        }
        String positiveIntsPattern = "^[1-9]\\d*$";
        alertCountList.forEach(alertCount -> assertThat(String.format("Assert that alert count '%s' is a positive int", alertCount), alertCount.matches(positiveIntsPattern))
        );
    }

    public void scrollClientCardsToBottom() throws InterruptedException {
        clientContainer.first().hover();
        for (int i = 0; i < 10; i++) {
            Thread.sleep(200);
            page.mouse().wheel(0, 500);
        }
    }

    @Step("Verify client cards count is equal to actual number of client cards in the list")
    public void verifyClientCardsCount() throws InterruptedException {
        scrollClientCardsToBottom();
        String style = clientContainer.last().getAttribute("style");
        String regex = "top:\\s*(\\d+)px";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(style);
        if (matcher.find()) {
            int actualCardsCount = (Integer.parseInt(matcher.group(1)) / 110) + 1;
            int expectedCardsCount = Integer.parseInt(currentTabCardsCountElement.textContent());
            assertThat(String.format("Assert that card count in tab (%s) is equal to card count by counting rows (%s)", expectedCardsCount, actualCardsCount), actualCardsCount, equalTo(expectedCardsCount)
            );
        } else {
            assertThat("Was not able to find 'top' value in style attribute", false);
        }
    }

    public void waitForPageToLoad() {
        try {
            page.locator(CLIENT_LIST_LOADING).waitFor(new Locator.WaitForOptions().setTimeout(5000));
        } catch (PlaywrightException ignored) {
        }
        page.waitForSelector(CLIENT_LIST_LOADING, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.HIDDEN));
        page.waitForSelector(FILTER_LOADING, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.HIDDEN));
    }

    public void clickSuspiciousClientsFiltration() {
        suspiciousClientsFilterIcon.click();
    }

    public void selectBrandFilterByText(String text) {
        page.locator(String.format(FILTER_BUTTON_BY_TEXT_PATTERN, text)).click();
    }

    public void clickApplyFiltrationButton() {
        applyFilterButton.click();
        waitForPageToLoad();
    }

    @Step("Verify all client cards have vantage image")
    public void verifyBrandImagesAreVantageOnly() {
        assertThat(clientContainer.count(), greaterThan(0));
        for (int i = 0; i < clientContainer.count(); i++) {
            Locator child = clientContainer.nth(i).locator(brandImage);
            assertThat("Assert that brand image is one for Vantage", child.getAttribute("src"), equalTo(VANTAGE_BRAND_IMAGE_SRC));
        }
    }

    public void selectRuleWithNameWithSearch(String name) {
        page.locator(String.format(CHECKBOX_BY_VALUE_PATTERN, name)).click();
    }

    public void selectRuleWithName(String name) {
        page.locator(String.format(CHECKBOX_BY_VALUE_PATTERN, name)).click();
    }

    @Step("Verify all client cards are filtered by rule name {name}")
    public void verifyAllCardsFilteredByRuleName(String name) {
        for (int i = 0; i < clientContainer.count(); i++) {
            clientContainer.nth(i).click();
            String actualRuleName = new AlertsPage(page).getFirstAlertRuleName();
            assertThat("Assert that each client card is filtered by rule name", actualRuleName, equalTo(name));
        }
    }

    public void selectCountryFilter(String country) {
        page.locator(String.format(CHECKBOX_BY_VALUE_PATTERN, country)).click();
    }

    @Step("Verify all client cards are filtered by country {country}")
    public void verifyAllCardsFilteredByCountry(String country) {
        assertThat(clientContainer.count(), greaterThan(0));
        for (int i = 0; i < clientContainer.count(); i++) {
            Locator child = clientContainer.nth(i).locator(countryCodeElement);
            assertThat("Assert that country in client card is according to filtration", child.textContent(), equalTo(country));
        }
    }

    public void selectAssigneeFilter(User assignee) {
        page.locator(String.format(CHECKBOX_BY_VALUE_PATTERN, assignee.getId())).click();
    }

    @Step("Verify all client cards are filtered by assignee")
    public void verifyAllCardsFilteredByAssignee(User assignee) {
        assertThat(clientContainer.count(), greaterThan(0));
        for (int i = 0; i < clientContainer.count(); i++) {
            Locator child = clientContainer.nth(i).locator(clientAssignmentElement);
            assertThat("Assert that assignee in client card is according to filtration", child.textContent(), equalTo(String.format("%s %s", assignee.getFirstName(), assignee.getLastName())));
        }
    }

    public void verifyNoBrandIsSelected() {
        for (int i = 0; i < brandButtons.count(); i++) {
            Locator button = brandButtons.nth(i);
            assertThat("Assert that each brand button is not selected", button.getAttribute("class"), not(containsString("g-button_view_action")));
        }
    }

    public void verifyNoRuleIsSelected() {
        for (int i = 0; i < ruleCheckboxes.count(); i++) {
            Locator checkbox = ruleCheckboxes.nth(i);
            assertThat("Assert that each rule checkbox is not selected", checkbox.getAttribute("class"), not(containsString("g-checkbox_checked")));
        }
    }

    public void verifyNoCountryIsSelected() {
        for (int i = 0; i < countryCheckboxes.count(); i++) {
            Locator checkbox = countryCheckboxes.nth(i);
            assertThat("Assert that each country checkbox is not selected", checkbox.getAttribute("class"), not(containsString("g-checkbox_checked")));
        }
    }

    public void verifyNoAssigneeIsSelected() {
        for (int i = 0; i < assigneeCheckboxes.count(); i++) {
            Locator checkbox = assigneeCheckboxes.nth(i);
            assertThat("Assert that each assignee checkbox is not selected", checkbox.getAttribute("class"), not(containsString("g-checkbox_checked")));
        }
    }

    @Step("Press reset button for brands and verify that none are selected")
    public void resetBrandFilterAndVerify() {
        resetBrandsButton.click();
        verifyNoBrandIsSelected();
    }

    @Step("Press reset button for rules and verify that none are selected")
    public void resetRulesFilterAndVerify() {
        resetRulesButton.click();
        verifyNoRuleIsSelected();
    }

    @Step("Press reset button for countries and verify that none are selected")
    public void resetCountriesFilterAndVerify() {
        resetCountriesButton.click();
        verifyNoCountryIsSelected();
    }

    @Step("Press reset button for assignee and verify that none are selected")
    public void resetAssigneeFilterAndVerify() {
        resetAssigneeButton.click();
        verifyNoAssigneeIsSelected();
    }

    @Step("Press reset all button and verify that none of the filters are selected")
    public void resetAllFiltersAndVerify() {
        page.locator(String.format(FILTER_BUTTON_BY_TEXT_PATTERN, "Reset all")).click();
        verifyNoBrandIsSelected();
        verifyNoRuleIsSelected();
        verifyNoCountryIsSelected();
        verifyNoAssigneeIsSelected();
    }

    @Step("Assign client with client id {clientId} to the current user")
    public void assignClientByClientId(String clientId) {
        Locator clientCard = page.locator(String.format(CLIENT_CARD_BY_CLIENT_ID_PATTERN, clientId));
        clientCard.hover();
        clientCard.locator(assignButton).click();
    }

    @Step("Verify client card with client id {clientId} is visible")
    public void verifyClientCardWithClientIdVisible(String clientId) {
        assertThat("Assert client card with client id is visible", page.locator(String.format(CLIENT_CARD_BY_CLIENT_ID_PATTERN, clientId)).isVisible(), equalTo(true));
    }

    @Step("Click client card with client id {clientId}")
    public void clickClientCardByClientId(String clientId) {
        Locator clientCard = page.locator(String.format(CLIENT_CARD_BY_CLIENT_ID_PATTERN, clientId));
        clientCard.hover();
        clientCard.click();
    }
}
