package page_objects.backoffice_pages.abuseRegistry;

import static utils.ConfigFactory.BASE_URL_E2E;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import helpers.data.enums.FraudSource;
import helpers.data.enums.FraudSubtype;
import helpers.data.enums.FraudType;
import helpers.data.enums.FraudTypeStatus;
import io.qameta.allure.Step;
import java.util.ArrayList;
import java.util.List;
import page_objects.backoffice_pages.AbstractPage;

public class FraudstersPage extends AbstractPage {

    private final Locator uploadListButton;
    private final Locator removeListButton;
    private final Locator uploadDrawer;
    private final Locator removeDrawer;
    private final Locator clientIdInput;
    private final Locator serverAccInput;
    private final Locator addFraudButton;
    private final Locator fraudTypeInput;
    private final Locator validationList;
    private final Locator validationListItem;
    private final Locator addRestrictionButton;
    private final Locator selectPopup;
    private final Locator selectPopupApplyButton;
    private final Locator commentaryField;
    private final Locator applyUploadButton;
    private final Locator clientIdsButton;
    private final Locator deleteUploadButton;
    private final Locator successToast;
    private final Locator warningToast;
    private final Locator pendingProcessingToggleLocator;
    private final Locator pendingProcessingCells;
    private final Locator uploadByIdButton;
    private final Locator uploadByAccountButton;

    private static final String UPLOAD_DRAWER_LOCATOR = "//*[@data-qa='drawer_body']";
    private static final String RESTRICTION_SELECTION_SECTION =
            UPLOAD_DRAWER_LOCATOR + "//*[@class='v-client-restrictions-selector']";
    private static final String FRAUD_DROPOUT_LIST_ELEMENT_LOCATOR_PATTERN =
            "//div[contains(@class,'g-popup')]//div[text()='%s']";
    private static final String SOURCE_SELECT_BUTTON_LOCATOR_PATTERN = "[data-qa='buttons_list__item__%s']";
    private static final String RESTRICTION_POPUP_LIST_ELEMENT_LOCATOR_PATTERN =
            "//*[@class='g-select-list__option-default-label'][text()='%s']";
    private static final String RESTRICTION_WORSE_TRADING_POPUP_LIST_ELEMENT_LOCATOR_PATTERN =
            "//*[@class='v-menuitem']//*[text()='%s']";
    private static final String BRAND_SELECT_BUTTON_LOCATOR_PATTERN =
            UPLOAD_DRAWER_LOCATOR + "//*[@class='v-label-list__list']/button/*[text()='%s']";
    private static final String FRAUD_SUBTYPE_FORMAT =
            "//*[(@class='v-menuitem') and contains(@data-qa, '_fraud_type_selector__submenu_')]/*[text()='%s']";
    private static final String FRAUD_STATUS_PATTERN =
            "//div[@class='g-popup__content' or contains(@class,'v-sub-menu__content') or contains(@data-qa,'fraud_type_selector__dropdown')]/descendant::div[contains(@data-qa,'fraud_type_selector__dropdown__item__submenu__%s:%s')]";
    private static final String FRAUD_TYPE_SELECTOR_FORMAT = "//div[contains(@class,'g-popup')]//div[text()='%s']";
    private static final String FRAUD_TYPE_SELECTOR_STATUS =
            "//div[contains(@data-menu-role,'submenu-container')]//div[text()='%s']";
    private static final String FRAUD_SOURCE_PATTERN = "//button[@data-qa='buttons_list__item__%s']";

    public static final String MSG_UPLOAD_SUCCESS = "Request received";
    public static final String MSG_DELETE_SUCCESS = "Selected actions are now being processed";
    public static final String MSG_WARNING_MANUAL_PROCESS =
            "illegal profit and suggested deduction need to be processed manually";

    public FraudstersPage(Page page) {
        super(page);
        this.uploadListButton = page.locator("//button/*[text()='Add']");
        this.removeListButton = page.locator("//button/*[text()='Remove']");
        this.uploadDrawer = page.locator(UPLOAD_DRAWER_LOCATOR + "//*[text()='Add clients to abuse registry']");
        this.removeDrawer = page.locator(UPLOAD_DRAWER_LOCATOR + "//*[text()='Remove fraud types or restrictions']");
        this.clientIdInput = page.locator(UPLOAD_DRAWER_LOCATOR
                + "//textarea[@placeholder='Enter client IDs separated with spaces, commas, semicolons or new lines']");
        this.serverAccInput = page.locator(
                UPLOAD_DRAWER_LOCATOR
                        + "//textarea[@placeholder='Enter a list of accounts with servers (e.g.: MT5-PUG2 123456789), separated with spaces, commas, semicolons or new lines']");
        this.addFraudButton = page.locator(
                UPLOAD_DRAWER_LOCATOR
                        + "//*[@data-qa='fraud_type_selector__add_button' or @data-qa='abuse_registry_manage_fraud_drawer__fraud_type_selector__anchor']");
        this.validationList = page.locator(".v-abuse-registry-batch-delete-errors__list");
        this.validationListItem = page.locator(".v-abuse-registry-batch-delete-errors-item__item");
        this.addRestrictionButton = page.locator(RESTRICTION_SELECTION_SECTION + "//button");
        this.fraudTypeInput = page.locator("//input[@placeholder='Type fraud name']");
        this.selectPopup = page.locator("[data-qa=\"select-popup\"]");
        this.selectPopupApplyButton = page.locator("[data-qa='client_restrictions_selector__apply']");
        this.commentaryField = page.locator("//textarea[@placeholder='Describe your decision']");
        this.applyUploadButton = page.locator(UPLOAD_DRAWER_LOCATOR + "//button/*[text()='Apply']");
        this.clientIdsButton = page.locator(UPLOAD_DRAWER_LOCATOR + "//div/*[@title='Client IDs']");
        this.deleteUploadButton = page.locator(UPLOAD_DRAWER_LOCATOR + "//button/*[text()='Remove']");
        this.successToast = page.locator("//*[contains(@class, 'g-toast_theme_success')]");
        this.warningToast = page.locator("//*[contains(@class, 'g-toast_theme_warning')]");
        this.pendingProcessingToggleLocator =
                page.locator("//*[@data-qa=\"abuse_registry__controls__pending_processing_switch\"]");
        this.pendingProcessingCells = page.locator(
                "//div[contains(@class,'v-body-row')]/descendant::div[contains(@data-qa,'pending_processing')]");
        this.uploadByIdButton = page.locator("[title='Client IDs']");
        this.uploadByAccountButton = page.locator("[title='Accounts']");
    }

    @Step("Navigate to {path}")
    private void navigateTo(String path) {
        page.navigate(BASE_URL_E2E + "abuse-registry/" + path);
        waitForPageToLoad();
    }

    @Step("Navigate to abuse-registry/fraudsters")
    public void navigateAbuseRegistryFraudsters() {
        navigateTo("fraudsters");
    }

    @Step("Navigate to abuse-registry/deductions")
    public void navigateAbuseRegistryDeductions() {
        navigateTo("deductions");
    }

    @Step("Open drawer to {action}")
    private void openDrawer(Locator button, Locator drawer) {
        button.click();
        drawer.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    @Step("Open upload to drawer")
    public void openUploadDrawer() {
        openDrawer(uploadListButton, uploadDrawer);
    }

    @Step("Open remove to drawer")
    public void openRemoveDrawer() {
        openDrawer(removeListButton, removeDrawer);
    }

    @Step("Check if remove drawer button is hidden")
    public boolean isRemoveDrawerButtonHidden() {
        waitForPageToLoad();
        removeListButton.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
        return removeListButton.isHidden();
    }

    @Step("Check if remove drawer button is disabled")
    public boolean isRemoveDrawerButtonDisabled() {
        waitForPageToLoad();
        return removeListButton.isDisabled();
    }

    @Step("Click upload by client ID")
    public void clickUploadByClientId() {
        uploadByIdButton.click();
    }

    @Step("Click upload by account")
    public void clickUploadByAccount() {
        uploadByAccountButton.click();
    }

    @Step("Select brand '{brandName}' to upload")
    public void selectBrandToUpload(String brandName) {
        page.click(String.format(BRAND_SELECT_BUTTON_LOCATOR_PATTERN, brandName));
    }

    @Step("Select fraud source '{sourceName}'")
    public void selectFraudSource(FraudSource sourceName) {
        page.click(String.format(SOURCE_SELECT_BUTTON_LOCATOR_PATTERN, sourceName.getDisplayName()));
    }

    @Step("Select client IDs and brand '{brandName}' to upload")
    public void selectClientIdsAndBrandToUpload(String brandName) {
        clientIdsButton.hover();
        clientIdsButton.click();
        selectBrandToUpload(brandName);
    }

    @Step("Type single client ID: {clientId}")
    public void typeClientID(String clientId) {
        typeClientsID(clientId);
    }

    @Step("Type client IDs: {clientIds}")
    public void typeClientsID(String... clientIds) {
        String input = String.join(",", clientIds);
        clientIdInput.fill(input);
    }

    @Step("Type server accounts: {serverNameAcc}")
    public void typeServerNameAcc(String... serverNameAcc) {
        StringBuilder inputBuilder = new StringBuilder();
        for (int i = 0; i < serverNameAcc.length; i++) {
            if (i > 0) {
                inputBuilder.append(i % 2 == 0 ? "," : " ");
            }
            inputBuilder.append(serverNameAcc[i]);
        }
        serverAccInput.fill(inputBuilder.toString());
    }

    @Step("Fill commentary: {commentary}")
    public void fillCommentary(String commentary) {
        commentaryField.fill(commentary);
    }

    @Step("Click pending processing toggle")
    public void clickPendingProcessingToggle() {
        pendingProcessingToggleLocator.click();
    }

    @Step("Click to add fraud button")
    public void clickAddFraudButton() {
        addFraudButton.click();
        fraudTypeInput.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    @Step("Add fraud {fraud} for delete with status {status}")
    public boolean addFraudForDeleteWithStatus(FraudType fraud, FraudTypeStatus status) {
        addFraud(fraud);

        Locator statusLocator = page.locator(String.format(FRAUD_STATUS_PATTERN, fraud.getCode(), status.getStatus()));
        if (statusLocator.isVisible()) {
            statusLocator.hover();
            statusLocator.click();
            return true;
        }
        return false;
    }

    @Step("Click to add restriction")
    public void clickAddRestrictionButton() {
        addRestrictionButton.click();
        selectPopup.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    @Step("Select restriction: {restriction}")
    public void selectRestriction(String restriction) {
        page.locator(String.format(RESTRICTION_POPUP_LIST_ELEMENT_LOCATOR_PATTERN, restriction))
                .click();
    }

    @Step("Select restriction worse trading level: {level}")
    public void selectRestrictionWorseTradingLevel(String level) {
        page.locator(String.format(RESTRICTION_WORSE_TRADING_POPUP_LIST_ELEMENT_LOCATOR_PATTERN, level))
                .click();
    }

    @Step("Apply selected restrictions")
    public void clickApplySelectedRestrictions() {
        selectPopupApplyButton.click();
    }

    @Step("Click apply upload")
    public void clickApplyUpload() {
        applyUploadButton.click();
        uploadDrawer.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
    }

    @Step("Click delete upload")
    public void clickDeleteUpload() {
        deleteUploadButton.click();
        uploadDrawer.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
    }

    @Step("Get validation error messages from the list")
    public List<String> getValidationErrors() {
        validationList.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        return validationListItem.allTextContents().stream()
                .map(String::trim)
                .filter(text -> !text.isEmpty())
                .toList();
    }

    @Step("Get success toast message text")
    public String getSuccessToastMessage() {
        successToast.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        return successToast.textContent();
    }

    @Step("Get warning toast message text")
    public String getWarningToastMessage() {
        warningToast.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        return warningToast.textContent();
    }

    @Step("Get pending processing content for UCID: {ucid}")
    public String getClientInPendingProcessingContent(String ucid) {
        return page.locator(String.format("//*[@data-qa='virtualized_table__rows__%s__pending_processing']", ucid))
                .textContent();
    }

    @Step("Get all pending processing cells content")
    public List<String> getPendingProcessingCellsContent() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < pendingProcessingCells.count(); i++) {
            list.add(pendingProcessingCells.nth(i).textContent().trim());
        }
        return list;
    }

    @Step("Check if upload success message is displayed")
    public boolean isUploadSuccessMessageDisplayed() {
        String toastText = getSuccessToastMessage();
        return toastText.contains(MSG_UPLOAD_SUCCESS);
    }

    @Step("Check if delete success message is displayed")
    public boolean isDeleteSuccessMessageDisplayed() {
        String toastText = getSuccessToastMessage();
        return toastText.contains(MSG_DELETE_SUCCESS);
    }

    @Step("Verify success toast contains expected count: {count}")
    public boolean verifySuccessMessageUpload(int count) {
        String actualText = getSuccessToastMessage();
        String expectedSubstring;
        if (count > 1) {
            expectedSubstring = "deductions were created automatically";
        } else {
            expectedSubstring = "deduction was created automatically";
        }
        return actualText.contains(String.valueOf(count)) && actualText.contains(expectedSubstring);
    }

    @Step("Check if warning manual process message is displayed for {pendingProcessingCount} deductions")
    public boolean isWarningManualProcessMessageDisplayed(int pendingProcessingCount) {
        String toastText = getWarningToastMessage();
        boolean containsBaseText = toastText.contains(MSG_WARNING_MANUAL_PROCESS);

        if (pendingProcessingCount > 1) {
            return containsBaseText
                    && toastText.contains(String.format("%d deductions require calculation", pendingProcessingCount));
        } else if (pendingProcessingCount == 1) {
            return containsBaseText
                    && toastText.contains(String.format("%d deduction requires calculation", pendingProcessingCount));
        }
        return containsBaseText;
    }

    @Step("Add detected fraud: {fraudType}")
    public void addFraud(FraudType fraudType) {
        clickAddFraudButton();
        selectFraudType(fraudType);
    }

    @Step("Add detected fraud: {fraudType} with status: {status}")
    public void addFraud(FraudType fraudType, FraudTypeStatus status) {
        addFraud(fraudType);
        selectFraudStatus(status);
    }

    @Step("Add detected fraud: {fraudType} with status: {status} and subtype: {subtype}")
    public void addFraud(FraudType fraudType, FraudTypeStatus status, FraudSubtype subtype) {
        addFraud(fraudType, status);
        selectFraudSubtype(subtype);
    }

    @Step("Select fraud type: {type}")
    private void selectFraudType(FraudType type) {
        hoverAndClick(FRAUD_TYPE_SELECTOR_FORMAT, type.getName());
    }

    @Step("Select status: {status}")
    private void selectFraudStatus(FraudTypeStatus status) {
        hoverAndClick(FRAUD_TYPE_SELECTOR_STATUS, status.getDisplayName());
    }

    @Step("Select subtype: {subtype}")
    private void selectFraudSubtype(FraudSubtype subtype) {
        hoverAndClick(FRAUD_SUBTYPE_FORMAT, subtype.getName());
    }

    @Step("Select fraud: {fraud} with status: {status} and source: {source}")
    public void selectSource(FraudSource source) {
        page.locator(String.format(FRAUD_SOURCE_PATTERN, source.getDisplayName()))
                .click();
    }

    @Step("Hover and click on element: {values}")
    public void hoverAndClick(String locator, String pattern) {
        page.locator(String.format(locator, pattern)).hover();
        page.locator(String.format(locator, pattern)).click();
    }
}
