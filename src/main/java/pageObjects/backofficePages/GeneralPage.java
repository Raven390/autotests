package pageObjects.backofficePages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;


import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class GeneralPage {

    private final Page page;
    private final Locator generalTab;
    private final Locator loaderAnimation;
    private final Locator generalInfoSection;
    private final Locator generalInfoHeader;
    private final Locator generalInfoBody;
    private final Locator kyclInfoSection;
    private final Locator kyclInfoHeader;
    private final Locator kyclInfoBody;
    private final Locator kycStatusLabel;
    private final Locator kycLineTitle;
    private final Locator kycLineDetails;
    private final Locator kycLine;
    private final Locator displayedFile;
    private final Locator plusZoomButton;
    private final Locator minusZoomButton;
    private final Locator mirrorButton;
    private final Locator rotateButton;
    private final Locator originalSizeButton;
    private final Locator imageFile;
    private final Locator sliderForwardButton;
    private final Locator sliderBackwardButton;
    private final Locator sliderGalleryCounter;
    private final Locator notAppliedPlaceholder;
    private final Locator attemptSection;
    private final Locator kycAddressRow;
    private final Locator kycAddressRowDetails;
    private final Locator kycIdRow;
    private final Locator kycIdRowDetails;
    private final Locator poaNotAppliedPlaceholder;
    private final Locator poiNotAppliedPlaceholder;
    private final Locator historyDrawer;
    private final Locator fullNameElement;
    private final Locator registrationDateAgoElement;
    private final Locator showHiddenDataButton;
    private final Locator registrationSourceIb;
    private final Locator registrationSourceCpa;

    private final String LOADING_SPINNER_SELECTOR = ".v-loader";
    private final String PLACEHOLDER_SELECTOR = ".v-text-with-icon__text";
    private final String KYC_STATUS_SELECTOR = "[data-qa=\"investigation_tools_kyc__status\"]";
    private final String GENERAL_TAB_LOADING_ELEMENT = "//div[contains(@class,'v-investigation-tools-general-skeleton__skeleton')]";
    private final String GENERAL_INFO_HEADER = "//div[@class='v-investigation-tools-general-info__header']";
    private final String ELEMENT_BY_LABEL_PATTERN = "//span[text()='%s']/ancestor::div[@class='v-investigation-tools-general-info__item']/descendant::div[@class='v-text-with-icon__text']";
    private final String BUTTON_LOADING = "//button[contains(@class,'g-button_loading')]";

    public GeneralPage(Page page) {
        this.page = page;
        this.loaderAnimation = page.locator(LOADING_SPINNER_SELECTOR);
        this.generalTab = page.locator("[role=\"tab\"][title=\"General\"]");
        this.generalInfoSection = page.locator(".v-investigation-tools-general-info");
        this.generalInfoHeader = page.locator(".v-investigation-tools-kyc__header");
        this.generalInfoBody = page.locator(".v-investigation-tools-general-info__content");
        this.kyclInfoSection = page.locator(".v-investigation-tools-kyc");
        this.kyclInfoHeader = page.locator(".v-investigation-tools-kyc__header");
        this.kyclInfoBody = page.locator(".v-investigation-tools-kyc__content");
        this.kycStatusLabel = page.locator(".v-kyc-attempt-status-label");
        this.kycLineTitle = page.locator(".v-investigation-tools-kyc-row__cell_type_title");
        this.kycLineDetails = page.locator(".v-investigation-tools-kyc-row__params");
        this.kycLine = page.locator(".v-investigation-tools-kyc-row");
        this.displayedFile = page.locator(".v-gallery__draggable");
        this.minusZoomButton = page.locator(".v-gallery__controls button").nth(0);
        this.plusZoomButton = page.locator(".v-gallery__controls button").nth(2);
        this.originalSizeButton = page.locator(".v-gallery__controls button").nth(1);
        this.rotateButton = page.locator(".v-gallery__controls button").nth(3);
        this.mirrorButton = page.locator(".v-gallery__controls button").nth(4);
        this.imageFile = page.locator(".v-investigation-tools-kyc-drawer__gallery .v-gallery__image");
        this.sliderForwardButton = page.locator(".v-gallery__slides button").nth(0);
        this.sliderBackwardButton = page.locator(".v-gallery__slides button").nth(1);
        this.sliderGalleryCounter = page.locator(".v-gallery__slides .v-gallery__counter");
        this.notAppliedPlaceholder = page.locator(PLACEHOLDER_SELECTOR).getByText("Not applied");
        this.poaNotAppliedPlaceholder = page.locator(KYC_STATUS_SELECTOR).getByText("Proof of address not applied");
        this.poiNotAppliedPlaceholder = page.locator(KYC_STATUS_SELECTOR).getByText("Proof of identity not applied");
        this.attemptSection = page.locator(".v-investigation-tools-kyc-row__cell_type_attempts");
        this.kycAddressRow = page.locator("[data-qa=\"investigation_tools_kyc__address_row\"]");
        this.kycAddressRowDetails = page.locator("[data-qa=\"investigation_tools_kyc__address_row\"] [data-qa=\"investigation_tools_kyc_row__params\"]");
        this.kycIdRow = page.locator("[data-qa=\"investigation_tools_kyc__identity_row\"]");
        this.kycIdRowDetails = page.locator("[data-qa=\"investigation_tools_kyc__identity_row\"] [data-qa=\"investigation_tools_kyc_row__params\"]");
        this.historyDrawer = page.locator("[data-qa=\"drawer_body\"]");
        this.fullNameElement = page.locator(String.format("%s/descendant::div[@class='v-text-with-icon__text'][1]", GENERAL_INFO_HEADER));
        this.registrationDateAgoElement = page.locator(String.format("%s/descendant::div[@class='v-text-with-icon__text'][2]", GENERAL_INFO_HEADER));
        this.showHiddenDataButton = page.locator("//button[@data-qa='investigation_page__general_info_unmask_btn']");
        this.registrationSourceIb = page.locator("//span[text()='IB']/following-sibling::span");
        this.registrationSourceCpa = page.locator("//span[text()='CPA']/following-sibling::span");
    }

    @Step("Open users general tab")
    public void navigateGeneralTab(String ucid) {
        page.navigate("http://k8s-test-nginxrev-55e209d446-410128713.us-east-1.elb.amazonaws.com/investigation?client_ucid=" + ucid);
        waitForPageToLoad();
        generalTab.click();
        waitForPageToLoad();
    }

    @Step("Click general tab")
    public void clickGeneralTabButton() {
        generalTab.click();
        waitForPageToLoad();
    }

    @Step("Check that general tab rendered properly")
    public void checkUI() {
        generalInfoSection.isVisible();
        generalInfoBody.isVisible();
        generalInfoHeader.isVisible();
        kyclInfoSection.isVisible();
        kyclInfoBody.isVisible();
        kyclInfoHeader.isVisible();
    }

    @Step("Check KYC status general")
    public void checkKycStatusGeneral(String title, String status) {
        page.waitForSelector(String.format("//span[text()='%s']/ancestor::tr/descendant::div[@class='g-label__content']", title));
        assertTrue(page.locator(String.format("//span[text()='%s']/ancestor::tr/descendant::div[@class='g-label__content']", title)).getByText(status).isVisible());
    }

    @Step("Check KYC attempts general")
    public void checkKycAttemptsGeneral(String title, String expectedAttempts) {
        page.waitForSelector("//span[text()='" + title + "']/ancestor::tr/td[contains(@class,'v-investigation-tools-kyc-row__cell_type_attempts')]");
        assertTrue(page.locator(String.format("//span[text()='%s']/ancestor::tr/td[contains(@class,'v-investigation-tools-kyc-row__cell_type_attempts')]", title)).getByText(expectedAttempts).isVisible());
    }

    @Step("Check KYC attempts general")
    public void checkIdInfoGeneral(String title, String expectedInfo) {
        page.waitForSelector("//span[text()='" + title + "']/ancestor::tr/td[contains(@class,'v-investigation-tools-kyc-row__cell_type_attempts')]");
        assertTrue(page.locator(String.format("//span[text()='%s']/ancestor::tr/td[contains(@class,'v-investigation-tools-kyc-row__cell_type_params')]", title)).getByText(expectedInfo).isVisible());
    }

    @Step("KYC details is displayed")
    public void kycDetailsDisplayed(String detailText) {
        assertTrue(kycLineDetails.getByText(detailText).isVisible());
    }

    @Step("KYC history details is displayed")
    public void kycHistoryDrawerDisplayed() {
        assertTrue(historyDrawer.isVisible());
    }

    @Step("Open KYC info")
    public void kycDetailsOpen(String title) {
        kycLine.getByText(title).click();
    }

    @Step("Check fv zoom functions")
    public void FVZoomFunctions() {
        displayedFile.isVisible();
        assertTrue(displayedFile.getAttribute("style").contains("scale(1)"));
        plusZoomButton.click();
        assertTrue(displayedFile.getAttribute("style").contains("scale(1.1)"));
        plusZoomButton.click();
        assertTrue(displayedFile.getAttribute("style").contains("scale(1.2)"));
        originalSizeButton.click();
        assertTrue(displayedFile.getAttribute("style").contains("scale(1)"));
        plusZoomButton.click();
        plusZoomButton.click();
        assertTrue(displayedFile.getAttribute("style").contains("scale(1.2)"));
        minusZoomButton.click();
        assertTrue(displayedFile.getAttribute("style").contains("scale(1.1)"));
    }

    @Step("Check fv rotate functions")
    public void FVRotateFunctions() {
        displayedFile.isVisible();
        assertTrue(displayedFile.getAttribute("style").contains("rotate(0deg)"));
        rotateButton.click();
        assertTrue(displayedFile.getAttribute("style").contains("rotate(-90deg)"));
        rotateButton.click();
        assertTrue(displayedFile.getAttribute("style").contains("rotate(-180deg)"));
        rotateButton.click();
        assertTrue(displayedFile.getAttribute("style").contains("rotate(-270deg)"));
        rotateButton.click();
        assertTrue(displayedFile.getAttribute("style").contains("rotate(0deg)"));
    }

    @Step("Check fv mirror functions")
    public void FVMirrorFunctions() {
        displayedFile.isVisible();
        assertTrue(displayedFile.getAttribute("style").contains("scaleX(1)"));
        mirrorButton.click();
        assertTrue(displayedFile.getAttribute("style").contains("scaleX(-1)"));
        mirrorButton.click();
        assertTrue(displayedFile.getAttribute("style").contains("scaleX(1)"));
    }

    @Step("Check fv slide functions")
    public void FVSlideFunctions() {
        displayedFile.isVisible();
        String image1 = imageFile.getAttribute("src");
        String[] splitedSource1 = image1.split("/");
        sliderForwardButton.click();
        String image2 = imageFile.getAttribute("src");
        String[] splitedSource2 = image2.split("/");
        assertNotEquals(splitedSource1[splitedSource1.length - 1], splitedSource2[splitedSource2.length - 1]);
        sliderBackwardButton.click();
        String image3 = imageFile.getAttribute("src");
        String[] splitedSource3 = image3.split("/");
        assertEquals(splitedSource1[splitedSource1.length - 1], splitedSource3[splitedSource3.length - 1]);
    }

    @Step("Check POA details general")
    public void poaDetailsGeneral(String lineOne, String lineTwo) {
        kycAddressRowDetails.getByText(lineOne);
        kycAddressRowDetails.getByText(lineTwo);
    }

    @Step("Check no applied placeholder is visible")
    public void noAppliedIsVisible() {
        page.waitForSelector(PLACEHOLDER_SELECTOR);
        assertTrue(notAppliedPlaceholder.isVisible());
    }

    @Step("POA placeholder is visible")
    public void poaPlaceholderIsVisible() {
        page.waitForSelector(KYC_STATUS_SELECTOR);
        assertTrue(poaNotAppliedPlaceholder.isVisible());
    }

    @Step("POI placeholder is visible")
    public void poiPlaceholderIsVisible() {
        page.waitForSelector(KYC_STATUS_SELECTOR);
        assertTrue(poiNotAppliedPlaceholder.isVisible());
    }

    @Step("Wait for page to load")
    public void waitForPageToLoad() {
        page.waitForSelector(GENERAL_TAB_LOADING_ELEMENT, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.HIDDEN));
    }

    @Step("Get full name")
    public String getFullName() {
        return fullNameElement.textContent();
    }

    @Step("Get registration date ago")
    public String getRegistrationDateAgo() {
        return registrationDateAgoElement.textContent();
    }

    @Step("Click show hidden data button")
    public void clickShowHiddenDataButton() {
        showHiddenDataButton.click();
        page.waitForSelector(BUTTON_LOADING, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.DETACHED));
    }

    public String getElementTextByLabel(String label) {
        return page.locator(String.format(ELEMENT_BY_LABEL_PATTERN, label)).textContent();
    }

    @Step("Get client id")
    public String getClientId() {
        return getElementTextByLabel("Client ID");
    }

    @Step("Get registration date")
    public String getRegistrationDate() {
        return getElementTextByLabel("Registered");
    }

    @Step("Get brand")
    public String getBrand() {
        return getElementTextByLabel("Brand");
    }

    @Step("Get regulator")
    public String getRegulator() {
        return getElementTextByLabel("Regulator");
    }

    @Step("Get gender")
    public String getGender() {
        return getElementTextByLabel("Gender");
    }

    @Step("Get date of birth")
    public String getDateOfBirth() {
        return getElementTextByLabel("Date of birth");
    }

    @Step("Get country")
    public String getCountry() {
        return getElementTextByLabel("Country");
    }

    @Step("Get nationality")
    public String getNationality() {
        return getElementTextByLabel("Nationality");
    }

    @Step("Get email address")
    public String getEmailAddress() {
        return getElementTextByLabel("Email address");
    }

    @Step("Get phone number")
    public String getPhoneNumber() {
        return getElementTextByLabel("Phone number");
    }

    @Step("Get 2 factor auth")
    public String get2FactorAuth() {
        return getElementTextByLabel("2-Factor Auth");
    }

    @Step("Get registration source IB")
    public String getRegistrationSourceIb() {
        return registrationSourceIb.textContent();
    }

    @Step("Get registration source CPA")
    public String getRegistrationSourceCpa() {
        return registrationSourceCpa.textContent();
    }

    @Step("Verify kyc section is visible")
    public void verifyKycSectionIsVisible() {
        assertThat(kyclInfoSection).isVisible();
    }
}

