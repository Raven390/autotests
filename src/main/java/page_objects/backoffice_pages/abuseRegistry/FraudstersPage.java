package page_objects.backoffice_pages.abuseRegistry;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static utils.ConfigFactory.BASE_URL_E2E;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import helpers.data.enums.FraudSubtype;
import helpers.data.enums.FraudType;
import helpers.data.enums.FraudTypeStatus;
import io.qameta.allure.Allure;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import page_objects.backoffice_pages.AbstractPage;

public class FraudstersPage extends AbstractPage {

    private final Locator uploadListButton;
    private final Locator uploadDrawer;
    private final String uploadDrawerLocator = "//*[@data-qa='drawer_body']";
    private final String brandSelectButtonLocatorPattern =
            uploadDrawerLocator + "//*[@class='v-label-list__list']/button/*[text()='%s']";
    private final String fraudTypeSelectionSection = uploadDrawerLocator + "//*[@class='v-fraud-type-selector']";
    private final String restrictionSelectionSection =
            uploadDrawerLocator + "//*[@class='v-client-restrictions-selector']";
    private final Locator clientIdInput;
    private final Locator addFraudButton;
    private final Locator fraudTypeInput;
    private final String fraudDropoutListElementLocatorPattern =
            "//*[contains(@class,'v-drop-down-menu-2__content')]/div/div[text()='%s']";
    private final String sourceSelectButtonLocatorPattern = "[data-qa='buttons_list__item__%s']";
    private final String restrictionPopupListElementLocatorPattern =
            "//*[@class='g-select-list__option-default-label'][text()='%s']";
    private final Locator validationList;
    private final Locator addRestrictionButton;
    private final Locator selectPopup;
    private final Locator selectPopupApplyButton;
    private final Locator commentaryField;
    private final Locator applyUploadButton;
    private final Locator successToast;
    private final Locator warningToast;
    private final Locator restrictionListButton;
    private final Locator restrictionApplyButton;
    private final Locator deleteUploadButton;
    private final Locator removeListButton;
    private final Locator clientIdsButton;
    private final Locator removeDrawer;
    private final Locator pendingProcessingToggleLocator;
    private final Locator pendingProcessingCells;
    private final Locator validationListItem;
    private final Locator serverAccInput;
    private final Locator uploadByIdButton;
    private final Locator uploadByAccountButton;

    private static final String FRAUD_SUBTYPE_FORMAT =
            "//*[(@class='v-menuitem') and contains(@data-qa, '_fraud_type_selector__submenu_')]/*[text()='%s']";
    private static final String FRAUD_TYPE_SELECTOR_FORMAT = "//*[@class ='v-sub-menu']//*[text()='%s']";
    private static final String FRAUD_TYPE_STATUS_FOR_SUBTYPE_FORMAT = "//*[@class='v-sub-menu']/*[text()='%s']";
    private static final String FRAUD_BY_TEXT_PATTERN =
            "//div[@class='g-popup__content' or contains(@class,'v-sub-menu__content') or contains(@data-qa,'fraud_type_selector__dropdown')]/descendant::div[text()='%s']";
    private static final String FRAUD_STATUS_PATTERN =
            "//div[@class='g-popup__content' or contains(@class,'v-sub-menu__content')  or contains(@data-qa,'fraud_type_selector__dropdown')]/descendant::div[contains(@data-qa,'fraud_type_selector__dropdown__item__submenu__%s:%s')]";
    private static final String FRAUD_SOURCE_PATTERN = "//button[@data-qa='buttons_list__item__%s']";

    public static final String FRAUD_TYPE_SELECTOR_STATUS =
            "//*[@data-qa='abuse_registry_manage_fraud_drawer__fraud_type_selector__item_%s__%s']";

    public FraudstersPage(Page page) {
        super(page);
        this.uploadListButton = page.locator("//button/*[text()='Add']");
        this.removeListButton = page.locator("//button/*[text()='Remove']");
        this.uploadDrawer = page.locator(uploadDrawerLocator + "//*[text()='Add clients to abuse registry']");
        this.removeDrawer = page.locator(uploadDrawerLocator + "//*[text()='Remove fraud types or restrictions']");
        this.clientIdInput = page.locator(uploadDrawerLocator
                + "//textarea[@placeholder='Enter client IDs separated with spaces, commas, semicolons or new lines']");
        this.serverAccInput = page.locator(
                uploadDrawerLocator
                        + "//textarea[@placeholder='Enter a list of accounts with servers (e.g.: MT5-PUG2 123456789), separated with spaces, commas, semicolons or new lines']");
        this.addFraudButton = page.locator(
                uploadDrawerLocator
                        + "//*[@data-qa='fraud_type_selector__add_button' or @data-qa='abuse_registry_manage_fraud_drawer__fraud_type_selector__anchor']");
        this.validationList = page.locator(".v-abuse-registry-batch-delete-errors__list");
        this.validationListItem = page.locator(".v-abuse-registry-batch-delete-errors-item__item");
        this.addRestrictionButton = page.locator(restrictionSelectionSection + "//button");
        this.restrictionApplyButton = page.locator(restrictionSelectionSection + "//button/*[text()='Apply']");
        this.fraudTypeInput = page.locator("//input[@placeholder='Type fraud name']");
        this.selectPopup = page.locator("[data-qa=\"select-popup\"]");
        this.selectPopupApplyButton = page.locator("[data-qa='client_restrictions_selector__apply']");
        this.commentaryField = page.locator("//textarea[@placeholder='Describe your decision']");
        this.applyUploadButton = page.locator(uploadDrawerLocator + "//button/*[text()='Apply']");
        this.clientIdsButton = page.locator(uploadDrawerLocator + "//div/*[@title='Client IDs']");
        this.deleteUploadButton = page.locator(uploadDrawerLocator + "//button/*[text()='Remove']");
        this.successToast = page.locator("//*[contains(@class, 'g-toast_theme_success')]");
        this.warningToast = page.locator("//*[contains(@class, 'g-toast_theme_warning')]");
        this.restrictionListButton = page.locator("//*[text()='Active restrictions']/..//button");
        this.pendingProcessingToggleLocator =
                page.locator("//*[@data-qa=\"abuse_registry__controls__pending_processing_switch\"]");
        this.pendingProcessingCells = page.locator(
                "//div[contains(@class,'v-body-row')]/descendant::div[contains(@data-qa,'pending_processing')]");
        this.uploadByIdButton = page.locator("[title='Client IDs']");
        this.uploadByAccountButton = page.locator("[title='Accounts']");
    }

    public void navigateAbuseRegistry() {
        Allure.step("navigate abuse registry page");
        page.navigate(BASE_URL_E2E + "abuse-registry/");
        waitForPageToLoad();
    }

    public void navigateAbuseRegistryFraudsters() {
        Allure.step("navigate abuse to registry page / fraudsters");
        page.navigate(BASE_URL_E2E + "abuse-registry/fraudsters");
        waitForPageToLoad();
    }

    public void navigateAbuseRegistryDeductions() {
        Allure.step("navigate abuse to registry page / deductions");
        page.navigate(BASE_URL_E2E + "abuse-registry/deductions");
        waitForPageToLoad();
    }

    public void openRemoveDrawer() {
        Allure.step("open upload remove by click to remove list button");
        removeListButton.click();
        removeDrawer.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    public void openRemoveDrawerButtonIsHidden() {
        waitForPageToLoad();
        removeListButton.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
        assertTrue(removeListButton.isHidden());
    }

    public void openRemoveDrawerButtonIsDisabled() {
        waitForPageToLoad();
        assertTrue(uploadListButton.isDisabled());
    }

    public void openUploadDrawer() {
        Allure.step("open upload drawer by click to upload list button");
        uploadListButton.click();
        uploadDrawer.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    public void selectBrandToUpload(String brandName) {
        page.click(String.format(brandSelectButtonLocatorPattern, brandName));
    }

    public void selectFraudSource(String sourceName) {
        page.click(String.format(sourceSelectButtonLocatorPattern, sourceName));
    }

    public void clickVindexFraudSource() {
        selectFraudSource("Vindex");
    }

    public void selectClientIdsAndBrandToUpload(String brandName) {
        clientIdsButton.hover();
        clientIdsButton.click();
        page.click(String.format(brandSelectButtonLocatorPattern, brandName));
    }

    public void typeClientID(String clientID) {
        clientIdInput.fill(clientID);
        boolean assertion = clientIdInput.textContent().contains(clientID);
        int iterator = 0;
        while (!assertion && iterator < 50) {
            page.waitForTimeout(100);
            assertion = clientIdInput.textContent().contains(clientID);
            iterator++;
        }
    }

    public void typeServerAcc(String serverAcc) {
        serverAccInput.fill(serverAcc);
        boolean assertion = serverAccInput.textContent().contains(serverAcc);
        int iterator = 0;
        while (!assertion && iterator < 50) {
            page.waitForTimeout(100);
            assertion = serverAccInput.textContent().contains(serverAcc);
            iterator++;
        }
    }

    public void typeClientsID(String... clientId) {
        String input = null;
        if (clientId.length > 1) {
            input = String.join(",", clientId);
        } else {
            input = clientId[0];
        }
        typeClientID(input);
    }

    public void typeServerNameAcc(String... serverNameAcc) {
        StringBuilder inputBuilder = new StringBuilder();

        for (int i = 0; i < serverNameAcc.length; i++) {
            if (i > 0) {
                if (i % 2 == 0) {
                    inputBuilder.append(",");
                } else {
                    inputBuilder.append(" ");
                }
            }
            inputBuilder.append(serverNameAcc[i]);
        }

        String input = inputBuilder.toString();
        typeServerAcc(input);
    }

    public void clickAddFraudButton() {
        Allure.step("click to plus button in 'Detected fraud' section");
        addFraudButton.click();
        fraudTypeInput.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    public void addSelectedFraudAdd(String fraud, String status) {
        String element = String.format(fraudDropoutListElementLocatorPattern, fraud);
        page.locator(element).hover();
        page.locator(element).hover();
        String subelement = "//*[contains(@class, 'v-sub-menu__content')]//div[text()='" + status + "']";
        page.locator(subelement).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        page.locator(subelement).click();
    }

    public void addSelectedFraud(FraudType fraud, FraudTypeStatus status) {
        String element = String.format(fraudDropoutListElementLocatorPattern, fraud.getName());
        page.locator(element).hover();
        page.locator(element).hover();
        String subelement = String.format(
                FRAUD_TYPE_SELECTOR_STATUS,
                fraud.getCode(),
                status.getDisplayName().toLowerCase(Locale.ROOT));
        page.locator(subelement).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        page.locator(subelement).click();
    }

    public boolean addFraudForDeleteWithStatus(FraudType fraud, FraudTypeStatus status) {
        clickAddFraudButton();
        page.locator(String.format(FRAUD_BY_TEXT_PATTERN, fraud.getName())).hover();
        page.locator(String.format(FRAUD_BY_TEXT_PATTERN, fraud.getName())).hover();

        Locator locator = page.locator(String.format(FRAUD_STATUS_PATTERN, fraud.getCode(), status.getStatus()));
        if (locator.isVisible()) {
            locator.click();
            return true;
        }
        return false;
    }

    public List<String> getValidationList() {
        validationList.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        List<String> list = new ArrayList<>();
        for (int i = 0; i < validationListItem.count(); i++) {
            list.add(validationListItem.nth(i).textContent());
        }
        return list;
    }

    public void addSelectedFraudAddWithSource(String fraud, String status, String source) {
        String element = String.format(fraudDropoutListElementLocatorPattern, fraud);
        page.locator(element).hover();
        page.locator(element).hover();
        String subelement = "//*[contains(@class, 'v-sub-menu__content')]//div[text()='" + status + "']";
        page.locator(subelement).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        page.locator(subelement).click();
        page.locator(String.format(FRAUD_SOURCE_PATTERN, source)).click();
    }

    public void addSelectedFraudDelete(String fraud) {
        String element = String.format(fraudDropoutListElementLocatorPattern, fraud);
        page.locator(element).hover();
        page.locator(element).hover();
        page.locator(element).click();
        page.waitForTimeout(500);
    }

    public void clickAddRestrictionButton() {
        Allure.step("click to add restriction");
        addRestrictionButton.click();
        selectPopup.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    public void clickPendingProcessingToggle() {
        Allure.step("click to toggle pending processing");
        pendingProcessingToggleLocator.click();
    }

    public void selectRestriction(String restriction) {
        String locator = String.format(restrictionPopupListElementLocatorPattern, restriction);
        page.locator(locator).click();
    }

    public void clickApplyselectedRestrictions() {
        Allure.step("click to apply restrictions");
        selectPopupApplyButton.click();
    }

    public void fillCommentary(String commentary) {
        commentaryField.fill(commentary);
    }

    public void clickApplyUpload() {
        applyUploadButton.click();
        uploadDrawer.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
    }

    public void clickDeleteUpload() {
        deleteUploadButton.click();
        uploadDrawer.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
    }

    public void clickUploadByClientId() {
        uploadByIdButton.click();
    }

    public void clickUploadByAccount() {
        uploadByAccountButton.click();
    }

    public void verifySuccessMessageUpload() {
        successToast.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        assertThat(successToast.textContent(), containsString("Request received"));
    }

    public void verifySuccessMessageUpload(int deductionsCount) {
        successToast.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        String s = successToast.textContent();
        assertThat(s, containsString("Request received"));
        if (deductionsCount > 1) {
            assertThat(s, containsString(String.format("%d deductions were created automatically", deductionsCount)));
        } else if (deductionsCount == 1) {
            assertThat(s, containsString(String.format("%d deduction was created automatically", deductionsCount)));
        }
    }

    public void verifyWarningMessageUpload(int pendingProcessingCount) {
        warningToast.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        String s = warningToast.textContent();
        assertThat(s, containsString("illegal profit and suggested deduction need to be processed manually"));
        if (pendingProcessingCount > 1) {
            assertThat(s, containsString(String.format("%d deductions require calculation", pendingProcessingCount)));
        } else if (pendingProcessingCount == 1) {
            assertThat(s, containsString(String.format("%d deduction requires calculation", pendingProcessingCount)));
        }
    }

    public void verifySuccessMessageDelete() {
        successToast.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        assertTrue(successToast.textContent().contains("Selected actions are now being processed"));
    }

    public String getClientInPendingProcessingContent(String ucid) {
        return page.locator(String.format("//*[@data-qa='virtualized_table__rows__%s__pending_processing']", ucid))
                .textContent();
    }

    public List<String> getPendingProcessingCellsContent() {
        var list = new ArrayList<String>();
        for (int i = 0; i < pendingProcessingCells.count(); i++) {
            list.add(pendingProcessingCells.nth(i).textContent());
        }
        return list;
    }

    public void addRestriction(String... addedRestriction) {
        Allure.step("add fraud on resolve screen");
        restrictionListButton.click();
        for (String i : addedRestriction) {
            page.getByRole(AriaRole.OPTION).getByText(i).click();
        }
        restrictionApplyButton.click();
    }

    public void addFraud(FraudType fraud, FraudTypeStatus status, FraudSubtype subtype) {
        addFraudButton.click();
        page.locator(FRAUD_TYPE_SELECTOR_FORMAT.formatted(fraud.getName())).hover();
        page.locator(FRAUD_TYPE_SELECTOR_FORMAT.formatted(fraud.getName())).hover();
        page.locator(FRAUD_TYPE_STATUS_FOR_SUBTYPE_FORMAT.formatted(status.getDisplayName()))
                .hover();
        page.locator(FRAUD_TYPE_STATUS_FOR_SUBTYPE_FORMAT.formatted(status.getDisplayName()))
                .hover();
        page.locator(FRAUD_SUBTYPE_FORMAT.formatted(subtype.getName())).click();
    }
}
