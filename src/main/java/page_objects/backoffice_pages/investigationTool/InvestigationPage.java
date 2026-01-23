package page_objects.backoffice_pages.investigationTool;

import static com.microsoft.playwright.options.WaitUntilState.DOMCONTENTLOADED;
import static helpers.database.DbHelper.getObjectsFromDB;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static utils.ConfigFactory.BASE_URL_E2E;
import static utils.ConfigFactory.ENTER_PAGE_E2E;
import static utils.Constants.VANTAGE_BRAND_IMAGE_SRC;
import static utils.TestUtils.comparePageScreenshotWithBaseline;

import business_objects.db.audit_service_db.EventOld;
import business_objects.ui.user.User;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;
import helpers.database.DbName;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import page_objects.backoffice_pages.AbstractPage;

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
    private final Locator assignDrawerButton;
    private final Locator assignDrawer;
    private final Locator commentButton;
    private final Locator closeToastButtom;
    private final Locator selectInvestigationTypeDropDown;
    private final Locator filterAmountFrom;
    private final Locator filterAmountTo;
    private final Locator amountFilterPresets;
    private final Locator resetAmountFilterButton;

    private static final String CLIENT_LIST_LOADING = "//div[@class='v-suspicious-client-list-skeleton']";
    private static final String FILTER_BUTTON_BY_TEXT_PATTERN = "//span[text()='%s']/parent::button";
    private static final String CHECKBOX_BY_VALUE_PATTERN = "//input[@value='%s' and @type='checkbox']";
    private static final String CLIENT_CARD_BY_CLIENT_ID_PATTERN =
            "//div[text()='%s']/ancestor::div[contains(@data-qa,'suspicious_clients__card')]";
    private static final String FILTER_LOADING =
            "//div[@class='v-investigation-tools-side-panel__filters']/button[contains(@class,'g-button_loading')]";
    private static final String INVESTIGATION_TYPE_LOCATOR_TEMPLATE =
            "//span[@class='g-select-list__option-default-label' and text()='%s']";
    public static final String HIGH_PRIORITY_LOCATOR =
            "//div[contains(@class,'v-suspicious-client-card__alerts-count') and contains(@class,'v-suspicious-client-card__alerts-count_isHighPriority')]";
    private static final String ASSIGN_USER_HEADER =
            "//div[@data-qa='drawer_header']/descendant::div[text()='Assign client investigation']";
    private static final String ASSIGN_USER_INPUT = "//descendant::input[@placeholder='Unassigned']";
    private static final String ASSIGN_USER_INPUT_LIST =
            "//div[@class='g-popup__content v-assign-user-selector__popup']";
    private static final String ASSIGN_USER_INPUT_LIST_ELEMENT = "//descendant::div[@class='v-assign-user-item']";
    private static final String ASSIGN_USER_INPUT_COMMENT = "//descendant::textarea[@class='g-text-area__control']";
    private static final String ASSIGN_USER_TO_ME_BUTTON = "//descendant::span[text()='Assign to me']";
    private final Locator unassignedSuspiciousClientsCounter;
    private final Locator mySuspiciousClientsCounter;
    private final Locator allSuspiciousClientsCounter;
    private final Locator tabTitle;

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
        this.dateRowCell =
                page.locator(".g-table__body .v-alert-list__column_type_date").first();
        this.amountRowCell =
                page.locator(".g-table__head .v-alert-list__column_type_amount").first();
        this.ruleRowCell =
                page.locator(".g-table__body .v-alert-list__column_type_rule").first();
        this.clientRowCell =
                page.locator(".g-table__body .v-alert-list__column_type_client").first();
        this.statusRowCell =
                page.locator(".g-table__body .v-alert-list__column_type_status").first();
        this.assigneeRowCell = page.locator(".g-table__body .v-alert-list__column_type_assignee")
                .first();
        this.tagRowCell =
                page.locator(".g-table__body .v-alert-list__column_type_tag").first();
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
        this.susClientSectionFolded = page.locator(
                ".v-investigation-tools-side-panel_collapsed[data-qa='investigation_page__suspicious_clients_container']");
        this.susClientList = page.locator("[data-qa='investigation_page__suspicious_clients_list']");
        this.assignToMeFilter = page.locator("[data-qa='suspicious_clients__tabs'] [value='MY']");
        this.unassignedFilter = page.locator("[data-qa='suspicious_clients__tabs'] [value='UNASSIGNED']");
        this.allSusClientsFilter = page.locator("[data-qa='suspicious_clients__tabs'] [value='ALL']");
        this.allSuspiciousClientsCounter = page.locator(
                "//*[@data-qa='suspicious_clients__tabs']//*[@value='ALL']/ancestor::label/descendant::*[contains(@class,'g-color-text_color_hint')]");
        this.mySuspiciousClientsCounter = page.locator(
                "//*[@data-qa='suspicious_clients__tabs']//*[@value='MY']/ancestor::label/descendant::*[contains(@class,'g-color-text_color_hint')]");
        this.unassignedSuspiciousClientsCounter = page.locator(
                "//*[@data-qa='suspicious_clients__tabs']//*[@value='UNASSIGNED']/ancestor::label/descendant::*[contains(@class,'g-color-text_color_hint')]");
        this.susClientSectionFoldButton = page.locator("[data-qa='suspicious_clients__list_toggle']");
        this.susClientSectionFoldButtonFolded = page.locator(
                ".v-investigation-tools-side-panel__toggler_collapsed [data-qa='suspicious_clients__list_toggle']");
        this.addCommentButton = page.locator("[data-qa='investigation_tools__add_comment_button']");
        this.addCommentSubmitButton = page.locator("[data-qa='investigation_tools__add_comment_submit_button']");
        this.addCommentPopup = page.locator("[data-qa='investigation_tools__add_comment_popup']");
        this.addCommentInput = page.locator("[data-qa='investigation_tools__add_comment_textarea_container'] textarea");
        this.addCommentDangerToast = page.locator(".g-toast_theme_danger");
        this.successToast = page.locator(".g-toast_theme_success");
        this.infoToast = page.locator(".g-toast_theme_info");
        this.investigateButtonList = page.locator("[data-qa='investigation_tools__client_card_assign_button']");
        this.suspiciousClientsList = page.locator("[data-qa='suspicious_clients__list']");
        this.investigateButton = page.locator(".g-button__text").getByText("Investigate");
        this.clientContainer =
                page.locator("//*[@data-qa='suspicious_clients__list']//*[@data-qa='data_item_wrapper_container']");
        this.brandImage = page.locator("//img[@class='g-avatar__image']");
        this.countryCodeElement = page.locator("//span[contains(@class,'g-text')]");
        this.clientIdElement = page.locator("//div[contains(@class,'g-text_variant_subheader-1')]");
        this.investigationStatusElement = page.locator("//div[contains(@class,'v-suspicious-client-card__status')]");
        this.clientAssignmentElement =
                page.locator("//div[contains(@class,'v-suspicious-client-card__assigned-user')]");
        this.clientCardTimerElement = page.locator("//div[contains(@class,'v-suspicious-client-card__timer')]");
        this.clientCardAlertsCountElement =
                page.locator("//div[contains(@class,'v-suspicious-client-card__alerts-count')]");
        this.currentTabCardsCountElement = page.locator(
                "//label[contains(@class,'g-radio-button__option_checked')]/descendant::span[contains(@class,'g-color-text_color_hint')]");
        this.suspiciousClientsFilterIcon = page.locator("//button[@data-qa='suspicious_clients__filters_toggle']");
        this.applyFilterButton = page.locator("//button[@data-qa='suspicious_clients__filters__apply']");
        this.showMoreRulesButton = page.locator("//span[text()='Show more']/..");
        this.ruleSearchInput = page.locator("//input[@placeholder='Search by rule']");
        this.resetBrandsButton = page.locator(
                "//button[@data-qa='suspicious_clients__filters__brands__title__reset']/descendant::span[text()='Reset']");
        this.resetRulesButton = page.locator(
                "//button[@data-qa='suspicious_clients__filters__rules__title__reset']/descendant::span[text()='Reset']");
        this.resetCountriesButton = page.locator(
                "//button[@data-qa='suspicious_clients__filters__countries__title__reset']/descendant::span[text()='Reset']");
        this.resetAssigneeButton = page.locator(
                "//button[@data-qa='suspicious_clients__filters__assignees__title__reset']/descendant::span[text()='Reset']");
        this.brandButtons = page.locator("//button[contains(@data-qa,'suspicious_clients__filters__brands__item')]");
        this.ruleCheckboxes = page.locator("//label[contains(@data-qa,'suspicious_clients__filters__rules__item')]");
        this.countryCheckboxes =
                page.locator("//label[contains(@data-qa,'suspicious_clients__filters__countries__item')]");
        this.assigneeCheckboxes =
                page.locator("//label[contains(@data-qa,'suspicious_clients__filters__assignees__item')]");
        this.assignButton = page.locator("//button[@data-qa='investigation_tools__client_card_assign_button']");
        this.assignDrawerButton = page.locator(".g-button__text").getByText("Assign");
        this.assignDrawer = page.locator("//div[@data-qa='drawer_body']");
        this.commentButton = page.locator("[data-qa='investigation_tools__add_comment_button']");
        this.closeToastButtom = page.locator(".g-button.g-toast__btn-close");
        this.selectInvestigationTypeDropDown = page.locator("//button[@data-qa='suspicious_clients__select_type']");
        this.filterAmountFrom = page.locator(
                "//*[@data-qa='suspicious_clients__filters__amount__input__input__from']/descendant::input");
        this.filterAmountTo =
                page.locator("//*[@data-qa='suspicious_clients__filters__amount__input__input__to']/descendant::input");
        this.tabTitle = page.locator(".v-investigation-tools-tabs__marker .g-tabs__item-title");
        this.amountFilterPresets = page.locator(
                "//div[@data-qa='suspicious_clients__filters__amount__presets']/descendant::*[@class='g-button__text']");
        this.resetAmountFilterButton =
                page.locator("//button[@data-qa='suspicious_clients__filters__amount__input__title__reset']");
    }

    @Step("Open the autotest login page main page")
    public void navigateEnterPage() {
        page.navigate(ENTER_PAGE_E2E, new Page.NavigateOptions().setWaitUntil(DOMCONTENTLOADED));
        page.waitForTimeout(200);
    }

    @Step("Open the BackOffice main page")
    public void navigateBase() {
        page.navigate(BASE_URL_E2E);
        super.waitForPageToLoad();
    }

    @Step("Open the BackOffice main page")
    public void navigateInvestigationTool() {
        page.navigate(BASE_URL_E2E + "investigation");
        super.waitForPageToLoad();
    }

    @Step("Open the MOCKED BackOffice main page")
    public void navigateMock() {
        page.route("**/api/alerts", route -> {
            String alert = "{\n" + "        'id': 1518,\n" + "        'uuid': 'c6b6af2e-43a2-425d-bf87-ee2b6141e267',\n"
                    + "        'date': '2024-09-12T07:57:46.713048Z',\n" + "        'amount': {\n"
                    + "            'value': -235331367481903743,\n" + "            'currency': 'Monica'\n"
                    + "        },\n" + "        'rule': [\n" + "            'ProctorMan',\n"
                    + "            'Marquez',\n" + "            'Ramirez',\n" + "            'Simpson',\n"
                    + "            'McFadden',\n" + "            'Farley'\n" + "        ],\n" + "        'client': {\n"
                    + "            'id': null,\n" + "            'regulator': null,\n" + "            'brand': null\n"
                    + "        },\n" + "        'status': 'NEW',\n" + "        'tag': []\n" + "    }";
            APIResponse response = route.fetch();
            String body = response.text();
            Map<String, String> headers = response.headers();
            route.fulfill(new Route.FulfillOptions()
                    .setResponse(response)
                    .setBody(alert)
                    .setHeaders(headers));
        });
        page.navigate(BASE_URL_E2E);
        waitForPageToLoad();
        page.evaluate(
                "document.querySelector('.v-alert-list__cell_date .g-text_variant_body-1').innerText = 'YESTERDAY'");
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
        waitForPageToLoad();
        unassignedFilter.click();
        unassignedFilter.locator("[aria-checked='true']").isVisible();
    }

    @Step("Filter all")
    public void filterAll() {
        allSusClientsFilter.click();
        allSusClientsFilter.locator("[aria-checked='true']").isVisible();
    }

    @Deprecated // need to update logic of mock
    @Step("Compare alert page with baseline screenshots")
    public void compareAlertPageWithBaseline(Page page, String baselinePath) {
        waitForPageToLoad();
        comparePageScreenshotWithBaseline(page, baselinePath);
    }

    @Step("Navigate to client")
    public void navigateToClient(String ucid) {
        page.navigate(String.format("%sinvestigation/%s", BASE_URL_E2E, ucid));
        waitForPageToLoad();
    }

    @Step("Mock comment api to return error")
    public void mockCommentError(String ucid) {
        page.route("**/clients/" + ucid + "/comments", route -> {
            APIResponse response = route.fetch();
            Map<String, String> headers = response.headers();
            route.fulfill(new Route.FulfillOptions()
                    .setResponse(response)
                    .setBody("500")
                    .setHeaders(headers)
                    .setStatus(500));
        });
    }

    @Step("Open add comment form")
    public void openCommentFormResolve() {
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

    @Step("Fill amount filter")
    public void fillAmountFilter(String amountFrom, String amountTo) {
        Allure.step("Fill amount filter");
        filterAmountFrom.fill(amountFrom);
        filterAmountTo.fill(amountTo);
    }

    @Step
    public void fillPaymentMethodFilter(String paymentMethod) {
        Allure.step("Fill payment method filter");
        page.locator(String.format(CHECKBOX_BY_VALUE_PATTERN, paymentMethod)).click();
    }

    @Step
    public void fillPriorityFilter(String priority) {
        Allure.step("Fill priority filter");
        page.locator(String.format(CHECKBOX_BY_VALUE_PATTERN, priority)).click();
    }

    @Step("Verify all client cards are filtered by High priority")
    public void verifyAllCardsFilteredByHighPriority() {
        for (int i = 0; i < clientContainer.count(); i++) {
            Locator child = clientContainer.nth(i).locator(HIGH_PRIORITY_LOCATOR);
            assertThat(
                    "Assert that client card is according to high priority filtration",
                    child.isVisible(),
                    equalTo(true));
        }
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
        page.waitForTimeout(500);
        waitForPageToLoad();
        int attempts = 0;
        while ((!page.locator(String.format(
                                "//*[@class='v-suspicious-client-list__item']/descendant::div[text()='%s']", userId))
                        .isVisible())
                && attempts < 5000) {
            suspiciousClientsList.hover(); // .evaluate("e => e.scrollTop += 100");
            page.mouse().wheel(0, 100);
            page.waitForTimeout(500);
            attempts++;
        }
        page.locator(String.format("//*[@class='v-suspicious-client-list__item']/descendant::div[text()='%s']", userId))
                .hover();
        page.locator(String.format(
                        "//div[text()='%s']/ancestor::div[@class='v-suspicious-client-list__item']/descendant::button",
                        userId))
                .nth(0)
                .click();
        String message = infoToast.textContent();
        assertEquals("Client investigation started", message);
    }

    @Step("user can't take client to investigation from the alert list")
    public void investigateUserAlertListDisabled(String userId) {
        Allure.step("user can't take client to investigation from the alert list");
        page.waitForTimeout(500);
        waitForPageToLoad();
        int attempts = 0;
        while ((!page.locator(String.format(
                                "//*[@class='v-suspicious-client-list__item']/descendant::div[text()='%s']", userId))
                        .isVisible())
                && attempts < 5000) {
            suspiciousClientsList.hover(); // .evaluate("e => e.scrollTop += 100");
            page.mouse().wheel(0, 100);
            page.waitForTimeout(500);
            attempts++;
        }
        page.locator(String.format("//*[@class='v-suspicious-client-list__item']/descendant::div[text()='%s']", userId))
                .hover();
        Locator investigateButton1 = page.locator(String.format(
                        "//div[text()='%s']/ancestor::div[@class='v-suspicious-client-list__item']/descendant::button",
                        userId))
                .nth(0);
        assertTrue(investigateButton1.isDisabled());
    }

    public void investigateUserAlertList(Integer userId) {
        investigateUserAlertList(String.valueOf(userId));
    }

    public void investigateUserAlertListDisabled(Integer userId) {
        investigateUserAlertListDisabled(String.valueOf(userId));
    }

    @Step("take client to investigation from the client card")
    public void investigateClientCard() {
        page.waitForTimeout(500);
        waitForPageToLoad();
        Allure.step("take client to investigation from the from the client card");
        investigateButton.click();
        String message = infoToast.textContent();
        assertEquals("Client investigation started", message);
        if (successToast.isVisible()) {
            closeToastButtom.click();
        } else {
            page.waitForTimeout(1);
        }
    }

    public void investigateClientCardDisabled() {
        Allure.step("check that investigation button in the client card is disabled");
        page.waitForTimeout(500);
        waitForPageToLoad();
        Allure.step("take client to investigation from the from the client card");
        assertTrue(investigateButton.isDisabled());
    }

    @Step("take client to investigation from the client card")
    public void cantInvestigateClientCard() {
        waitForPageToLoad();
        Allure.step("check that client cant be taken to investigation from the client card");
        assertTrue(investigateButton.isDisabled());
    }

    public void checkInvestigationAssigmentAudit(String ucid) throws Exception {
        Allure.step("check assigment event in Audit DB");
        page.waitForTimeout(5000);
        List<EventOld> event = getObjectsFromDB(DbName.POSTGRES, "event", "ucid = '" + ucid + "'", EventOld.class);
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
        clientsCountryList.forEach(country -> assertThat(
                String.format("Assert that country '%s' matches iso2 format", country), country.matches(iso2Pattern)));
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
        clientIdList.forEach(clientId -> assertThat(
                String.format("Assert that clientId '%s' is a positive int", clientId),
                clientId.matches(positiveIntsPattern)));
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
        investigationStatusList.forEach(status -> assertThat(
                String.format(
                        "Assert that investigation status '%s' has value in ['Investigating', 'Suspicious']", status),
                status,
                anyOf(is("Investigating"), is("Suspicious"))));
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
        investigationStatusList.forEach(status -> assertThat(
                String.format("Assert that investigation status '%s' has value 'Investigating'", status),
                status,
                (is("Investigating"))));
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
        assignmentList.forEach(assignee -> assertThat(
                "Assert that assignment element is present",
                assignee,
                (is(String.format("%s %s", user.getFirstName(), user.getLastName())))));
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
        cardTimerList.forEach(timer -> assertThat(
                String.format("Assert that card timer '%s' is a positive int", timer), timer.matches(timerPattern)));
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
        alertCountList.forEach(alertCount -> assertThat(
                String.format("Assert that alert count '%s' is a positive int", alertCount),
                alertCount.matches(positiveIntsPattern)));
    }

    @Step("Verify client cards count is equal to actual number of client cards in the list")
    public void verifyClientCardsCount() throws InterruptedException {
        clientContainer.first().hover();
        for (int i = 0; i < 100; i++) {
            Thread.sleep(100);
            page.mouse().wheel(0, 500);
        }
        String style = clientContainer.last().getAttribute("style");
        String regex = "top:\\s*(\\d+)px";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(style);
        if (matcher.find()) {
            int actualCardsCount = (Integer.parseInt(matcher.group(1)) / 110) + 1;
            int expectedCardsCount = Integer.parseInt(currentTabCardsCountElement.textContent());
            assertThat(
                    String.format(
                            "Assert that card count in tab (%s) is equal to card count by counting rows (%s)",
                            expectedCardsCount, actualCardsCount),
                    actualCardsCount,
                    equalTo(expectedCardsCount));
        } else {
            assertThat("Was not able to find 'top' value in style attribute", false);
        }
    }

    public void waitForPageToLoad() {
        try {
            page.locator(CLIENT_LIST_LOADING).waitFor(new Locator.WaitForOptions().setTimeout(5000));
        } catch (PlaywrightException ignored) {
        }
        page.waitForSelector(
                CLIENT_LIST_LOADING, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.HIDDEN));
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
            assertThat(
                    "Assert that brand image is one for Vantage",
                    child.getAttribute("src"),
                    equalTo(VANTAGE_BRAND_IMAGE_SRC));
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
            AlertsPage alertsPage = new AlertsPage(page);
            alertsPage.openAlertsTab();
            String actualRuleName = alertsPage.getFirstAlertRuleName();
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
            assertThat(
                    "Assert that country in client card is according to filtration",
                    child.textContent(),
                    equalTo(country));
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
            assertThat(
                    "Assert that assignee in client card is according to filtration",
                    child.textContent(),
                    equalTo(String.format("%s %s", assignee.getFirstName(), assignee.getLastName())));
        }
    }

    public void verifyNoBrandIsSelected() {
        for (int i = 0; i < brandButtons.count(); i++) {
            Locator button = brandButtons.nth(i);
            assertThat(
                    "Assert that each brand button is not selected",
                    button.getAttribute("class"),
                    not(containsString("g-button_view_action")));
        }
    }

    public void verifyNoRuleIsSelected() {
        for (int i = 0; i < ruleCheckboxes.count(); i++) {
            Locator checkbox = ruleCheckboxes.nth(i);
            assertThat(
                    "Assert that each rule checkbox is not selected",
                    checkbox.getAttribute("class"),
                    not(containsString("g-checkbox_checked")));
        }
    }

    public void verifyNoCountryIsSelected() {
        for (int i = 0; i < countryCheckboxes.count(); i++) {
            Locator checkbox = countryCheckboxes.nth(i);
            assertThat(
                    "Assert that each country checkbox is not selected",
                    checkbox.getAttribute("class"),
                    not(containsString("g-checkbox_checked")));
        }
    }

    public void verifyNoAssigneeIsSelected() {
        for (int i = 0; i < assigneeCheckboxes.count(); i++) {
            Locator checkbox = assigneeCheckboxes.nth(i);
            assertThat(
                    "Assert that each assignee checkbox is not selected",
                    checkbox.getAttribute("class"),
                    not(containsString("g-checkbox_checked")));
        }
    }

    @Step("Get client IDs from visible client cards")
    public List<String> getClientIdsFromClientCards() {
        Allure.step("Get client IDs from visible client cards");
        List<String> clientIds = new ArrayList<>();
        if (clientContainer.count() > 0) {
            for (int i = 0; i < clientContainer.count(); i++) {
                clientIds.add(clientContainer.nth(i).locator(clientIdElement).textContent());
            }
        }
        return clientIds;
    }

    @Step("Get client IDs from visible client cards")
    public List<Boolean> getPriorityFromClientCards() {
        Allure.step("Get client IDs from visible client cards");
        List<Boolean> clientIds = new ArrayList<>();
        if (clientContainer.count() > 0) {
            for (int i = 0; i < clientContainer.count(); i++) {
                Locator count = clientContainer.nth(i).locator(HIGH_PRIORITY_LOCATOR);
                clientIds.add(count.isVisible());
            }
        }
        return clientIds;
    }

    @Step("Apply filter button is disabled")
    public void verifyApplyFilterButtonIsDisabled() {
        assertTrue(applyFilterButton.isDisabled());
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

    @Step("Click assign drawer button")
    public void openAssignDrawer() {
        assignDrawerButton.click();
        assignDrawer.locator(ASSIGN_USER_HEADER).waitFor();
    }

    @Step("Assign drawer check buttons")
    public void assignDrawerCheckButtons(
            String meFullName, String seniorUserFullName, String seniorOtherTeamUserFullName) {
        var input = assignDrawer.locator(ASSIGN_USER_INPUT);

        input.click();
        var userList = page.locator(ASSIGN_USER_INPUT_LIST);

        var meElem = userList.locator(ASSIGN_USER_INPUT_LIST_ELEMENT + "[2]//div//div[text()='(Me)']");
        meElem.waitFor();
        meElem.click();
        assertEquals(meFullName, input.getAttribute("value"));
        input.click();

        var seniorElem = userList.locator(
                ASSIGN_USER_INPUT_LIST_ELEMENT + "//descendant::div[text()='" + seniorUserFullName + "']");
        seniorElem.waitFor();
        seniorElem.click();
        assertEquals(seniorUserFullName, input.getAttribute("value"));
        input.click();

        assertThrows(TimeoutError.class, () -> userList.locator(ASSIGN_USER_INPUT_LIST_ELEMENT
                        + "//descendant::div[text()='" + seniorOtherTeamUserFullName + "']")
                .waitFor(new Locator.WaitForOptions().setTimeout(1000)));

        var unassignElem = userList.locator(ASSIGN_USER_INPUT_LIST_ELEMENT + "[1]//div//div");
        unassignElem.waitFor();
        unassignElem.click();
        assertEquals("", input.getAttribute("value"));

        var assignToMeButton = assignDrawer.locator(ASSIGN_USER_TO_ME_BUTTON);
        assignToMeButton.click();
        assertEquals(meFullName, input.getAttribute("value"));
        input.click();
        unassignElem.waitFor();
        unassignElem.click();
        assertEquals("", input.getAttribute("value"));

        var commentTextArea = assignDrawer.locator(ASSIGN_USER_INPUT_COMMENT);
        commentTextArea.fill("test text");
        assertEquals("test text", commentTextArea.textContent());
    }

    @Step("Verify client card with client id {clientId} is visible")
    public void verifyClientCardWithClientIdVisible(String clientId) {
        assertThat(
                "Assert client card with client id is visible",
                page.locator(String.format(CLIENT_CARD_BY_CLIENT_ID_PATTERN, clientId))
                        .isVisible(),
                equalTo(true));
    }

    public void openCommentForm() {
        commentButton.click();
        addCommentInput.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    public void isCommentButtonDisabled() {
        Allure.step("check if comment button is disabled");
        assertTrue(commentButton.isDisabled());
    }

    public void cantOpenCommentForm() {
        waitForPageToLoad();
        assertTrue(commentButton.isDisabled());
    }

    @Step("Click client card with client id {clientId}")
    public void clickClientCardByClientId(String clientId) throws InterruptedException {
        Locator clientCard = page.locator(String.format(CLIENT_CARD_BY_CLIENT_ID_PATTERN, clientId));
        if (clientCard.isVisible()) {
            clientCard.hover();
            clientCard.click();
        } else {
            clientContainer.first().hover();
            for (int i = 0; i < 100; i++) {
                Thread.sleep(100);
                page.mouse().wheel(0, 500);
                if (clientCard.isVisible()) {
                    clientCard.hover();
                    clientCard.click();
                    return;
                }
            }
            assert false : "The client card has not appeared after the scroll";
        }
    }

    public void clickSelectInvestigationType(String investigationType) {
        waitForPageToLoad();
        selectInvestigationTypeDropDown.click();
        page.locator(String.format(INVESTIGATION_TYPE_LOCATOR_TEMPLATE, investigationType))
                .click();
    }

    public void selectInvestigationTypeSwitchIsHidden() {
        selectInvestigationTypeDropDown.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
        assertThat(
                "Assert that select investigation type drop down is hidden",
                selectInvestigationTypeDropDown.isVisible(),
                equalTo(false));
    }

    public void investigationTypeSwitchIsNotPresented() {
        waitForPageToLoad();
        selectInvestigationTypeDropDown.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
        assertThat(
                "Assert that select investigation type drop down is hidden",
                selectInvestigationTypeDropDown.isVisible(),
                equalTo(false));
    }

    public void investigationTypeSwitchIsPresented() {
        waitForPageToLoad();
        selectInvestigationTypeDropDown.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        assertThat(
                "Assert that select investigation type drop down is present",
                selectInvestigationTypeDropDown.isVisible(),
                equalTo(true));
    }

    @Step("Select 'Trading' investigation type from the dropdown")
    public void clickSelectTradingInvestigationType() {
        this.clickSelectInvestigationType("Trading");
    }

    @Step("Select 'Payment' investigation type from the dropdown")
    public void clickSelectPaymentInvestigationType() {
        this.clickSelectInvestigationType("Payments");
    }

    public void checkSuspiciousCounterValueALL(Long expectedCount) {
        Allure.step("Check suspicious counter value ALL");
        int i = 0;
        while ((!allSuspiciousClientsCounter.isVisible()) && i < 50) {
            page.waitForTimeout(500);
            i++;
        }
        String actualValue;
        if (allSuspiciousClientsCounter.isVisible()) {
            actualValue = allSuspiciousClientsCounter.textContent();
        } else {
            actualValue = "0";
        }
        if (actualValue == null || actualValue.isEmpty()) {
            actualValue = "0";
        }
        assertEquals(expectedCount, Long.parseLong(actualValue));
    }

    public void checkSuspiciousCounterValueMY(Long expectedCount) {
        Allure.step("Check suspicious counter value MY");
        int i = 0;
        while ((!mySuspiciousClientsCounter.isVisible()) && i < 50) {
            page.waitForTimeout(500);
            i++;
        }
        String actualValue;
        if (mySuspiciousClientsCounter.isVisible()) {
            actualValue = mySuspiciousClientsCounter.textContent();
        } else {
            actualValue = "0";
        }
        if (actualValue == null || actualValue.isEmpty()) {
            actualValue = "0";
        }
        assertEquals(expectedCount, Long.parseLong(actualValue));
    }

    public void checkSuspiciousCounterValueUNASSIGNED(Long expectedCount) {
        Allure.step("Check suspicious counter value UNASSIGNED");
        int i = 0;
        while ((!unassignedSuspiciousClientsCounter.isVisible()) && i < 50) {
            page.waitForTimeout(500);
            i++;
        }
        String actualValue;
        if (unassignedSuspiciousClientsCounter.isVisible()) {
            actualValue = unassignedSuspiciousClientsCounter.textContent();
        } else {
            actualValue = "0";
        }
        if (actualValue == null || actualValue.isEmpty()) {
            actualValue = "0";
        }
        assertEquals(expectedCount, Long.parseLong(actualValue));
    }

    public void checkTabOrder() {
        Allure.step("Check tab order");
        assertEquals("Audit trail", tabTitle.nth(0).textContent());
        assertEquals("General", tabTitle.nth(1).textContent());
        assertEquals("Sessions", tabTitle.nth(2).textContent());
        assertEquals("Payments", tabTitle.nth(3).textContent());
        assertEquals("Trading", tabTitle.nth(4).textContent());
        assertEquals("Connections", tabTitle.nth(5).textContent());
        assertEquals("Restrictions", tabTitle.nth(6).textContent());
        assertEquals("Alerts", tabTitle.nth(7).textContent());
    }

    public void checkOpenedTab(String tabName) {
        waitForPageToLoad();
        Allure.step("Check opened tab");
        assertTrue(page.url().contains(tabName.toLowerCase()));
    }

    @Step("Get amount filter 'from' and 'to' values")
    public Map<String, String> getAmountFromToValues() {
        Map<String, String> result = new HashMap<>();
        String from = filterAmountFrom.getAttribute("value");
        String to = filterAmountTo.getAttribute("value");
        result.put("from", from == null ? "" : from);
        result.put("to", to == null ? "" : to);
        return result;
    }

    @Step("Click amount preset by visible name: {presetName}")
    public void clickAmountPresetByName(String presetName) {
        amountFilterPresets.getByText(presetName).click();
    }

    @Step("Reset amount filter")
    public void resetAmountFilter() {
        resetAmountFilterButton.click();
    }
}
