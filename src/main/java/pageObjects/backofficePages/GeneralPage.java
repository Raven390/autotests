package pageObjects.backofficePages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

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


    public GeneralPage(Page page) {
        this.page = page;
        this.loaderAnimation = page.locator(".v-loader");
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
        this.notAppliedPlaceholder = page.locator(".v-text-with-icon__text").getByText("Not applied");
        this.poaNotAppliedPlaceholder = page.locator("[data-qa=\"investigation_tools_kyc__status\"]").getByText("Proof of address not applied");
        this.poiNotAppliedPlaceholder = page.locator("[data-qa=\"investigation_tools_kyc__status\"]").getByText("Proof of identity not applied");
        this.attemptSection = page.locator(".v-investigation-tools-kyc-row__cell_type_attempts");
        this.kycAddressRow = page.locator("[data-qa=\"investigation_tools_kyc__address_row\"]");
        this.kycAddressRowDetails = page.locator("[data-qa=\"investigation_tools_kyc__address_row\"] [data-qa=\"investigation_tools_kyc_row__params\"]");
        this.kycIdRow = page.locator("[data-qa=\"investigation_tools_kyc__identity_row\"]");
        this.kycIdRowDetails = page.locator("[data-qa=\"investigation_tools_kyc__identity_row\"] [data-qa=\"investigation_tools_kyc_row__params\"]");
        this.historyDrawer = page.locator("[data-qa=\"drawer_body\"]");


    }

    @Step("open users general tab")
    public void navigateGeneralTab(String ucid) {
        page.navigate("http://k8s-test-nginxrev-55e209d446-410128713.us-east-1.elb.amazonaws.com/investigation?client_ucid=" + ucid);
        isPageLoaded();
        generalTab.click();
        isPageLoaded();
    }

    @Step("check that general tab rendered properly")
    public void checkUI() {
        generalInfoSection.isVisible();
        generalInfoBody.isVisible();
        generalInfoHeader.isVisible();
        kyclInfoSection.isVisible();
        kyclInfoBody.isVisible();
        kyclInfoHeader.isVisible();
    }

    @Step("checkKycStatusGeneral")
    public void checkKycStatusGeneral(String title, String status) {
        assertTrue(page.locator("//span[text()='" + title + "']/ancestor::tr/descendant::div[@class='g-label__content']").getByText(status).isVisible());
    }

    @Step("checkKycAttemptsGeneral")
    public void checkKycAttemptsGeneral(String title, String expectedAttempts) {
        assertTrue(page.locator("//span[text()='" + title + "']/ancestor::tr/td[contains(@class,'v-investigation-tools-kyc-row__cell_type_attempts')]").getByText(expectedAttempts).isVisible());
    }

    @Step("checkKycAttemptsGeneral")
    public void checkIdInfoGeneral(String title, String expectedInfo) {
        assertTrue(page.locator("//span[text()='" + title + "']/ancestor::tr/td[contains(@class,'v-investigation-tools-kyc-row__cell_type_params')]").getByText(expectedInfo).isVisible());
    }

    @Step("KYC details is displayed")
    public void kycDetailsDisplayed(String detailText) {
        assertTrue(kycLineDetails.getByText(detailText).isVisible());
    }

    @Step("KYC history details is displayed")
    public void kycHistoryDrawerDisplayed() {
        assertTrue(historyDrawer.isVisible());
    }

    @Step("open KYC info")
    public void kycDetailsOpen(String title) {
        kycLine.getByText(title).click();
    }

    @Step("check fv zoom functions")
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

    @Step("check fv rotate functions")
    public void FVRotateFunctions() {
        displayedFile.isVisible();
        assertTrue(displayedFile.getAttribute("style").contains("rotate(0deg)"));
        rotateButton.click();
        assertTrue(displayedFile.getAttribute("style").contains("rotate(90deg)"));
        rotateButton.click();
        assertTrue(displayedFile.getAttribute("style").contains("rotate(180deg)"));
        rotateButton.click();
        assertTrue(displayedFile.getAttribute("style").contains("rotate(270deg)"));
        rotateButton.click();
        assertTrue(displayedFile.getAttribute("style").contains("rotate(0deg)"));
    }

    @Step("check fv mirror functions")
    public void FVMirrorFunctions() {
        displayedFile.isVisible();
        assertTrue(displayedFile.getAttribute("style").contains("scaleX(1)"));
        mirrorButton.click();
        assertTrue(displayedFile.getAttribute("style").contains("scaleX(-1)"));
        mirrorButton.click();
        assertTrue(displayedFile.getAttribute("style").contains("scaleX(1)"));
    }

    @Step("check fv slide functions")
    public void FVSlideFunctions() {
        displayedFile.isVisible();
        assertTrue(imageFile.getAttribute("src").contains("V+03hf4+zfGB8vjYP1bbAAAAABJRU5ErkJggg=="));
        sliderForwardButton.click();
        assertTrue(displayedFile.getAttribute("style").contains("w8wTQORKhjH5gAAAABJRU5ErkJggg=="));
        sliderBackwardButton.click();
        assertTrue(imageFile.getAttribute("src").contains("V+03hf4+zfGB8vjYP1bbAAAAABJRU5ErkJggg=="));
    }

    @Step("check POA details general")
    public void poaDetailsGeneral(String lineOne, String lineTwo) {
        kycAddressRowDetails.getByText(lineOne);
        kycAddressRowDetails.getByText(lineTwo);
    }

    @Step("check No applied placeholder is visible")
    public void noAppliedIsVisible() {
        assertTrue(notAppliedPlaceholder.isVisible());
    }

    @Step("poaPlaceholderIsVisible")
    public void poaPlaceholderIsVisible() {
        assertTrue(poaNotAppliedPlaceholder.isVisible());
    }

    @Step("poiPlaceholderIsVisible")
    public void poiPlaceholderIsVisible() {
        assertTrue(poiNotAppliedPlaceholder.isVisible());
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


}

