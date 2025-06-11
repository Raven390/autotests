package page_objects.backoffice_pages.abuseRegistry;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Allure;
import page_objects.backoffice_pages.AbstractPage;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static utils.ConfigFactory.BASE_URL_E2E;

public class AbuseRegistryPage extends AbstractPage {

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


    public AbuseRegistryPage(Page page) {
        super(page);
        this.uploadListButton = page.locator("//button/*[text() = 'Upload list']");
        this.uploadDrawer = page.locator(uploadDrawerLocator + "//*[text()='Add clients to abuse registry']");
        this.clientIdInput = page.locator(uploadDrawerLocator + "//*[@class='v-limited-text-area']//textarea[@placeholder='Enter client IDs']");
        this.addFraudButton = page.locator(fraudTypeSelectionSection + "//*[@data-qa='fraud_type_select_anchor_button']");
        this.addRestrictionButton = page.locator(restrictionSelectionSection + "//button");
        this.fraudTypeInput = page.locator("//input[@placeholder='Type fraud name']");
        this.fraudDropoutListElement = page.locator("//*[contains(@class,'v-dropdown-select-item-base')]/div/div");
        this.selectPopup = page.locator("[data-qa=\"select-popup\"]");
        this.selectPopupApplyButton = page.locator("//*[@data-qa=\"select-popup\"]//button/*[text()='Apply']");
        this.commentaryField = page.locator("//textarea[@placeholder='Describe your decision']");
        this.applyUploadButton = page.locator(uploadDrawerLocator + "//button/*[text()='Apply']");
        this.successToast = page.locator("//*[contains(@class, 'g-toast_theme_success')]");

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

    public void addSelectedFraudFraud(String fraud, String status) {
        String element = String.format(fraudDropoutListElementLocatorPattern, fraud);
        page.locator(element).hover();
        page.locator(element).hover();
        String subelement = element + "/../../..//div[@class='v-dropdown-select-item__sub-menu-content']//div[text()='" + status + "']";
        page.locator(subelement).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        page.locator(subelement).click();
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

    public void verifySuccessMessage() {
        successToast.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        assertTrue(successToast.textContent().contains("Request for adding clients to abuse registry is sent"));
    }
}

