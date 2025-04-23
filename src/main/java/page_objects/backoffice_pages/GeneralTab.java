package page_objects.backoffice_pages;

import business_objects.db.clickhouse.s3_fact_login_metrics.S3FactLoginMetricsObject;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.ElementState;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;


import java.sql.SQLException;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static helpers.database.DbHelper.deleteEntryFromDb;
import static org.junit.jupiter.api.Assertions.*;
import static utils.ConfigFactory.BASE_URL_E2E;
import static utils.Constants.ID_PROOF_TABLE_NAME;
import static utils.Constants.KYC_FILES_TABLE_NAME;
import static utils.Utils.roundDouble;

public class GeneralTab extends AbstractPage {

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
    private final Locator registrationSourceReferral;
    private final Locator registrationSourceCpa;
    private final Locator fileViewerImage;
    private final Locator fileViewerImagePresentation;
    private final Locator attemptItem;
    private final Locator ibOverviewButton;
    private final Locator cpaOverviewButton;

    private static final String LOADING_SPINNER_SELECTOR = ".v-loader";
    private static final String PLACEHOLDER_SELECTOR = ".v-text-with-icon__text";
    private static final String KYC_STATUS_SELECTOR = "[data-qa='investigation_tools_kyc__status']";
    private static final String GENERAL_TAB_LOADING_ELEMENT = "//div[contains(@class,'v-investigation-tools-general-skeleton__skeleton')]";
    private static final String GENERAL_INFO_HEADER = "//div[@class='v-investigation-tools-general-info__header']";
    private static final String ELEMENT_BY_LABEL_PATTERN = "//span[text()='%s']/ancestor::div[@class='v-investigation-tools-general-info__item']/descendant::div[@class='v-text-with-icon__text']";
    private static final String BUTTON_LOADING = "//button[contains(@class,'g-button_loading')]";
    private static final String POF_ROW_SELECTOR = "//tr[@data-qa='investigation_tools_kyc__face_row']";
    private static final String KYC_ROW_TITLE = "//td[@data-qa='investigation_tools_kyc_row__title']";
    private static final String KYC_ROW_STATUS = "//td[@data-qa='investigation_tools_kyc_row__status']";
    private static final String KYC_ROW_DATE = "//td[@data-qa='investigation_tools_kyc_row__date']";
    private static final String KYC_ROW_PARAMS = "//td[@data-qa='investigation_tools_kyc_row__params']";
    private static final String KYC_ROW_ATTEMPT = "//td[@data-qa='investigation_tools_kyc_row__attempts']";
    private static final String SECONDARY_TEXT_SELECTOR = "//*[contains(@class,'g-color-text_color_secondary')]";
    private static final String NOT_SECONDARY_TEXT_SELECTOR = "//*[not (contains(@class,'g-color-text_color_secondary'))]";
    private static final String SUMMARY_PANEL = "//*[@data-qa='investigation_page__investigation_tools'";
    private static final String SUMMARY_PANEL_VALUE = "//div[contains(@class, 'v-client-summary-panel__value')]/div[@class= 'v-text-with-icon__text']";
    private static final String SUMMARY_PANEL_ITEM = "//div[contains(@class, 'v-client-summary-panel__item')]";
    private static final String IB_ROW = "//*[text()='IB program']//ancestor::tr";
    private static final String REFERRAL_ROW = "//*[text()='Referral client']//ancestor::tr";
    private static final String CPA_ROW = "//*[text()='CPA affiliate']//ancestor::tr";
    private static final String REFERRAL_LOGIN = "//*[contains(@class, 'v-registration-source-row__cell v-registration-source-row__cell_type_login')]";
    private static final String REFERRAL_DATE = "//*[contains(@class, 'v-registration-source-row__date')]";
    private static final String REFERRAL_REBATES = "//*[contains(@class, 'v-registration-source-row__cell v-registration-source-row__cell_type_rebates')]";
    private static final String TEXT_ELEMENT = "//*[contains(@class, 'v-text-with-icon__text')]";

    public GeneralTab(Page page) {
        super(page);
        this.loaderAnimation = page.locator(LOADING_SPINNER_SELECTOR);
        this.generalTab = page.locator("[role='tab'][title='General']");
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
        this.kycAddressRow = page.locator("[data-qa='investigation_tools_kyc__address_row']");
        this.kycAddressRowDetails = page.locator("[data-qa='investigation_tools_kyc__address_row'] [data-qa='investigation_tools_kyc_row__params']");
        this.kycIdRow = page.locator("[data-qa='investigation_tools_kyc__identity_row']");
        this.kycIdRowDetails = page.locator("[data-qa='investigation_tools_kyc__identity_row'] [data-qa='investigation_tools_kyc_row__params']");
        this.historyDrawer = page.locator("[data-qa='drawer_body']");
        this.fullNameElement = page.locator(String.format("%s/descendant::div[@class='v-text-with-icon__text'][1]", GENERAL_INFO_HEADER));
        this.registrationDateAgoElement = page.locator(String.format("%s/descendant::div[@class='v-text-with-icon__text'][2]", GENERAL_INFO_HEADER));
        this.showHiddenDataButton = page.locator("//button[@data-qa='investigation_page__general_info_unmask_btn']");
        this.registrationSourceReferral = page.locator("//span[text()='Referral client']/ancestor::tr/descendant::div[@class='v-text-with-icon__text'][1]");
        this.registrationSourceCpa = page.locator("//span[text()='CPA affiliate']/ancestor::tr/descendant::div[@class='v-text-with-icon__text'][1]");
        this.fileViewerImage = page.locator("[data-qa='gallery__image']");
        this.fileViewerImagePresentation = page.locator("[data-qa='gallery__slide']");
        this.attemptItem = page.locator(".v-investigation-tools-kyc-attempts__item");
        this.ibOverviewButton = page.locator("//div[text()='IB overview']");
        this.cpaOverviewButton = page.locator("//div[text()='CPA overview']");
    }

    @Step("Open users general tab")
    public void navigateGeneralTab(String ucid) {
        page.navigate(BASE_URL_E2E + "investigation/" + ucid + "/general");
        waitForPageToLoad();

    }

    @Step("Open users general tab")
    public void navigate(String ucid) {
        navigateGeneralTab(ucid);

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
        fileViewerImage.isVisible();
        assertTrue(fileViewerImagePresentation.getAttribute("style").contains("scale(1)"));
        plusZoomButton.click();
        assertTrue(fileViewerImagePresentation.getAttribute("style").contains("scale(1.1)"));
        plusZoomButton.click();
        assertTrue(fileViewerImagePresentation.getAttribute("style").contains("scale(1.2)"));
        originalSizeButton.click();
        assertTrue(fileViewerImagePresentation.getAttribute("style").contains("scale(1)"));
        plusZoomButton.click();
        plusZoomButton.click();
        assertTrue(fileViewerImagePresentation.getAttribute("style").contains("scale(1.2)"));
        minusZoomButton.click();
        assertTrue(fileViewerImagePresentation.getAttribute("style").contains("scale(1.1)"));
    }

    @Step("Check fv rotate functions")
    public void FVRotateFunctions() {
        fileViewerImage.isVisible();
        assertTrue(fileViewerImagePresentation.getAttribute("style").contains("rotate(0deg)"));
        rotateButton.click();
        assertTrue(fileViewerImagePresentation.getAttribute("style").contains("rotate(-90deg)"));
        rotateButton.click();
        assertTrue(fileViewerImagePresentation.getAttribute("style").contains("rotate(-180deg)"));
        rotateButton.click();
        assertTrue(fileViewerImagePresentation.getAttribute("style").contains("rotate(-270deg)"));
        rotateButton.click();
        assertTrue(fileViewerImagePresentation.getAttribute("style").contains("rotate(0deg)"));
    }

    @Step("Check fv mirror functions")
    public void FVMirrorFunctions() {
        fileViewerImage.isVisible();
        assertTrue(fileViewerImagePresentation.getAttribute("style").contains("scaleX(1)"));
        mirrorButton.click();
        assertTrue(fileViewerImagePresentation.getAttribute("style").contains("scaleX(-1)"));
        mirrorButton.click();
        assertTrue(fileViewerImagePresentation.getAttribute("style").contains("scaleX(1)"));
    }

    @Step("Check fv slide functions")
    public void FVSlideFunctions() {
        fileViewerImage.isVisible();
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

    @Step("POI placeholder is visible")
    public void pofPlaceholderIsNotVisible() {
        Allure.step("POF placeholder is not visible");
        page.waitForSelector(KYC_STATUS_SELECTOR);
        assertFalse(page.getByText("Proof of face").isVisible());
    }

    @Step("Wait for page to load")
    public void waitForPageToLoad() {
        page.waitForTimeout(500);
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

    @Step("Get registration source Referral")
    public String getRegistrationSourceRaf() {
        return registrationSourceReferral.textContent();
    }

    @Step("Get registration source CPA")
    public String getRegistrationSourceCpa() {
        return registrationSourceCpa.textContent();
    }

    @Step("Verify kyc section is visible")
    public void verifyKycSectionIsVisible() {
        assertThat(kyclInfoSection).isVisible();
    }

    public void deleteClientsPoiAttempts(String ucid) throws SQLException {
        deleteEntryFromDb(ID_PROOF_TABLE_NAME, "ucid ='" + ucid + "' and file_type_id = 12");
    }

    public void deleteClientsPofAttempts(String ucid) throws SQLException {
        deleteEntryFromDb(ID_PROOF_TABLE_NAME, "ucid ='" + ucid + "' and file_type_id = 27");
    }

    public void deleteClientsPofFileRecord(String ucid) throws SQLException {
        deleteEntryFromDb(KYC_FILES_TABLE_NAME, "ucid ='" + ucid + "' and file_type_id = 27");
    }

    public void checkRightImage(String sourceLinkLastPart) {
        String source = fileViewerImage.getAttribute("src");
        String[] splitedSource1 = source.split("/");
        assertTrue(splitedSource1[splitedSource1.length - 1].contains(sourceLinkLastPart));
    }

    public void checkImageDisplayed() {
        String source = fileViewerImage.getAttribute("src");
        assertNotNull(source);
        assertFalse(source.isEmpty());
    }

    public void checkNumberOfAttemptsInViewer(int expectedNumberOfAttempts) {
        assertEquals(expectedNumberOfAttempts, attemptItem.count());
    }

    public void checkValueKycPofTitle(String expectedValue) {
        assertEquals(expectedValue, page.locator(POF_ROW_SELECTOR + KYC_ROW_TITLE).textContent());
    }

    public void checkValueKycPofStatus(String expectedValue) {
        assertEquals(expectedValue.toLowerCase(), page.locator(POF_ROW_SELECTOR + KYC_ROW_STATUS).textContent().toLowerCase());
    }

    public void checkValueKycPofDate(String expectedValue) {
        String[] values = expectedValue.split(":");
        String testValue = values[0] + ":" + values[1];
        String locator = POF_ROW_SELECTOR + KYC_ROW_DATE + "//" + VARIANT_BODY_1_SELECTOR;
        page.waitForSelector(locator).waitForElementState(ElementState.VISIBLE);
        assertEquals(testValue.toLowerCase(), page.locator(locator).nth(0).textContent().toLowerCase());
    }

    public void checkValueKycPofParameters(String expectedValue) {
        assertEquals(expectedValue, page.locator(POF_ROW_SELECTOR + KYC_ROW_PARAMS).textContent());
    }

    public void checkValueKycPofAttempts(String expectedValue) {
        assertEquals(expectedValue, page.locator(POF_ROW_SELECTOR + KYC_ROW_ATTEMPT).first().textContent());
    }

    public void checkSummaryPanelValue(String sectionName, String expectedValue) {
        Allure.step("check value in section " + sectionName + " of the summary panel");
        String locator = SUMMARY_PANEL_ITEM + "//*[text()='" + sectionName + "']/.." + SUMMARY_PANEL_VALUE;
        page.waitForSelector(locator).waitForElementState(ElementState.VISIBLE);
        assertEquals(expectedValue, page.locator(locator).textContent());
    }

    public void checkSummaryPanelFraudValue(String sectionName, String expectedValue) {
        Allure.step("check value in section fraud of the summary panel");
        String locator = SUMMARY_PANEL_ITEM + "//*[text()='" + sectionName + "']/.." + SUMMARY_PANEL_VALUE;
        page.waitForSelector(locator).waitForElementState(ElementState.VISIBLE);
        assertTrue(page.locator(locator).textContent().contains(expectedValue));
    }

    public void checkSummaryPanelValue(String sectionName, double expectedValue) {
        double rounded = roundDouble(expectedValue, 2);
        String string = decimalFormat.format(rounded);
        checkSummaryPanelValue(sectionName, string);
    }

    public void checkIbRebates(int login, double expectedValue) {
        Allure.step("check rebates in IB line");
        String locator = IB_ROW + "//*[text()='" + login + "']//ancestor::tr" + REFERRAL_REBATES + TEXT_ELEMENT;
        page.waitForSelector(locator).waitForElementState(ElementState.VISIBLE);
        String testedValue = decimalFormat.format(expectedValue);
        assertEquals("IB rebates " + testedValue + "  USD", page.locator(locator).textContent());

    }

    public void IbSectionNotDisplayed() {
        Allure.step("check that IB section is not displayed");
        waitForPageToLoad();
        assertFalse(page.locator(IB_ROW).isVisible());
    }

    public void clickReferrerLink() {
        Allure.step("Click referral client link");
        String locator = REFERRAL_ROW + REFERRAL_LOGIN + TEXT_ELEMENT;
        page.locator(locator).click();
    }

    public void checkReferralDisplayed(int userId) {
        Allure.step("Check that referral client line is displayed with the data from clients record in DB");
        String locator = REFERRAL_ROW + REFERRAL_LOGIN + TEXT_ELEMENT;
        page.waitForSelector(locator).waitForElementState(ElementState.VISIBLE);
        assertEquals(String.valueOf(userId), page.locator(locator).textContent());
    }

    public void checkCpaDisplayed(int userId) {
        Allure.step("Check that CPA client line is displayed with the data from clients record in DB");
        String locator = CPA_ROW + REFERRAL_LOGIN + TEXT_ELEMENT;
        page.waitForSelector(locator).waitForElementState(ElementState.VISIBLE);
        assertEquals(String.valueOf(userId), page.locator(locator).textContent());
    }

    public void ReferralSectionNotDisplayed() {
        Allure.step("check that IB section is not displayed");
        waitForPageToLoad();
        assertFalse(page.locator(REFERRAL_ROW).isVisible());
    }

    public void CpaSectionNotDisplayed() {
        Allure.step("check that CPA section is not displayed");
        waitForPageToLoad();
        assertFalse(page.locator(CPA_ROW).isVisible());
    }

    public void checkCpaRebates(int login, double expectedValue) {
        Allure.step("check rebates in CPA line");
        String locator = CPA_ROW + "//*[text()='" + login + "']//ancestor::tr" + REFERRAL_REBATES + TEXT_ELEMENT;
        page.waitForSelector(locator).waitForElementState(ElementState.VISIBLE);
        String testedValue = decimalFormat.format(expectedValue);
        assertEquals("CPA rebates " + testedValue + "  USD", page.locator(locator).textContent());

    }

    public void checkCpaDate(int login, String expectedValue) {
        Allure.step("check rebates in CPA line");
        String locator = CPA_ROW + "//*[text()='" + login + "']//ancestor::tr" + REFERRAL_DATE + TEXT_ELEMENT;
        page.waitForSelector(locator).waitForElementState(ElementState.VISIBLE);
        assertEquals(expectedValue, page.locator(locator).textContent());
    }

    public void clickIbOverviewButton() {
        ibOverviewButton.first().click();
    }

    public Double calculateRevenue(S3FactLoginMetricsObject revenue) {
        return revenue.getDailyCoreSpreadRevenuePe() + revenue.getDailyTakerSpreadRevenuePe() + revenue.getDailyLpSpreadRevenuePe() + revenue.getDailyVbSpreadRevenuePe() + revenue.getDailyAppliedMinSpreadRevenuePe() + revenue.getDailyAppliedMaxSpreadRevenuePe() + revenue.getDailyCoreSpreadRevenueOz() + revenue.getDailyTakerSpreadRevenueOz() + revenue.getDailyVbSpreadRevenueOz() + revenue.getDailyAppliedMinSpreadRevenueOz() + revenue.getDailyCommissionRevenue() + revenue.getDailySwapsRevenue();
    }

    public void clickCpaOverviewButton() {
        cpaOverviewButton.first().click();
    }
}

