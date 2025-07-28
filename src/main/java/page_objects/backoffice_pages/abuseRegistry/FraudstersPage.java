package page_objects.backoffice_pages.abuseRegistry;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Allure;
import page_objects.backoffice_pages.AbstractPage;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static utils.ConfigFactory.BASE_URL_E2E;

public class FraudstersPage extends AbstractPage {

    private final Locator uploadListButton;
    private final Locator uploadDrawer;
    private final String uploadDrawerLocator = "//*[@data-qa='drawer_body']";
    private final String brandSelectButtonLocatorPattern = uploadDrawerLocator + "//*[@class='v-label-list__list']/button/*[text()='%s']";
    private final String fraudTypeSelectionSection = uploadDrawerLocator + "//*[@class='v-fraud-type-selector']";
    private final String restrictionSelectionSection = uploadDrawerLocator + "//*[@class='v-client-restrictions-selector']";
    private final Locator clientIdInput;
    private final Locator addFraudButton;
    private final Locator fraudTypeInput;
    private final String fraudDropoutListElementLocatorPattern = "//*[contains(@class,'v-dropdown-select-item-base')]/div/div[text()='%s']";
    private final String restrictionPopupListElementLocatorPattern = "//*[@class='g-select-list__option-default-label'][text()='%s']";
    private final Locator fraudDropoutListElement;
    private final Locator addRestrictionButton;
    private final Locator selectPopup;
    private final Locator selectPopupApplyButton;
    private final Locator commentaryField;
    private final Locator applyUploadButton;
    private final Locator successToast;
    private final Locator restrictionListButton;
    private final Locator restrictionApplyButton;
    private final Locator deleteUploadButton;
    private final Locator removeListButton;
    private final Locator removeDrawer;


    public FraudstersPage(Page page) {
        super(page);
        this.uploadListButton = page.locator("//button/*[text()='Add']");
        this.removeListButton = page.locator("//button/*[text()='Remove']");
        this.uploadDrawer = page.locator(uploadDrawerLocator + "//*[text()='Add clients to abuse registry']");
        this.removeDrawer = page.locator(uploadDrawerLocator + "//*[text()='Remove fraud types or restrictions']");
        this.clientIdInput = page.locator(uploadDrawerLocator + "//textarea[@placeholder='Enter client IDs separated with spaces, commas, semicolons, or new lines']");
        this.addFraudButton = page.locator(uploadDrawerLocator + "//*[@data-qa='fraud_type_select_anchor_button']");
        this.addRestrictionButton = page.locator(restrictionSelectionSection + "//button");
        this.restrictionApplyButton = page.locator(restrictionSelectionSection + "//button/*[text()='Apply']");
        this.fraudTypeInput = page.locator("//input[@placeholder='Type fraud name']");
        this.fraudDropoutListElement = page.locator("//*[contains(@class,'v-dropdown-select-item-base')]/div/div");
        this.selectPopup = page.locator("[data-qa=\"select-popup\"]");
        this.selectPopupApplyButton = page.locator("//*[@data-qa=\"select-popup\"]//button/*[text()='Apply']");
        this.commentaryField = page.locator("//textarea[@placeholder='Describe your decision']");
        this.applyUploadButton = page.locator(uploadDrawerLocator + "//button/*[text()='Apply']");
        this.deleteUploadButton = page.locator(uploadDrawerLocator + "//button/*[text()='Remove']");
        this.successToast = page.locator("//*[contains(@class, 'g-toast_theme_success')]");
        this.restrictionListButton = page.locator("//*[text()='Active restrictions']/..//button");

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

    public void openRemoveDrawer() {
        Allure.step("open upload remove by click to remove list button");
        removeListButton.click();
        removeDrawer.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    public void openUploadDrawer() {
        Allure.step("open upload drawer by click to upload list button");
        uploadListButton.click();
        uploadDrawer.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    public void selectBrandToUpload(String brandName) {
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

    public void typeClientsID(String... clientId) {
        String input = null;
        if (clientId.length > 1) {
            input = String.join(",", clientId);
        } else {
            input = clientId[0];
        }
        typeClientID(input);
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
        String subelement = element + "/../../..//div[@class='v-dropdown-select-item__sub-menu-content']//div[text()='" + status + "']";
        page.locator(subelement).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        page.locator(subelement).click();
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

    public void verifySuccessMessageUpload() {
        successToast.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        assertTrue(successToast.textContent().contains("Selected actions are now being processed"));
    }

    public void verifySuccessMessageDelete() {
        successToast.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        assertTrue(successToast.textContent().contains("Selected actions are now being processed"));
    }

    public void addRestriction(String... addedRestriction) {
        Allure.step("add fraud on resolve screen");
        restrictionListButton.click();
        for (String i : addedRestriction) {
            page.getByRole(AriaRole.OPTION).getByText(i).click();
        }
        restrictionApplyButton.click();
    }
}

