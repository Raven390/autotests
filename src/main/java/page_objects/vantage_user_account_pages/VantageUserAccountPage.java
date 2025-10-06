package page_objects.vantage_user_account_pages;

import static utils.ConfigFactory.BASE_URL_VANTAGE_ACCOUNT;
import static utils.Utils.writeLog;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

public class VantageUserAccountPage {
    private final Page page;
    private final Locator loader;
    private final Locator closeAlertButton;
    private final Locator closeBannerButton;
    private final Locator declineCookies;
    private final Locator personalDetailsWindowTitle;
    private final Locator genderMaleRadioButton;
    private final Locator dobDay;
    private final Locator dobMonth;
    private final Locator dobYear;
    private final Locator dayValueDropdown;
    private final Locator monthValueDropdown;
    private final Locator yearValueDropdown;
    private final Locator userPhoneNumber;
    private final Locator nextButton;

    private final Locator accountCurrencyBlock;
    private final Locator acceptTermsCheckbox;

    public VantageUserAccountPage(Page page) {
        this.page = page;
        this.loader = page.locator("xpath=//*[@class='client-portal-loading']");
        this.closeBannerButton = page.locator("xpath=//*[@class='bannerContainer']//*[contains(@data-testid,'closeImg') and contains(@class,'close-icon')]");
        this.closeAlertButton = page.locator("xpath=//*[@data-testid='notificationDialog']//*[contains(@role,'dialog')]//*[@type='button' and @aria-label='Close']");
        this.declineCookies = page.locator("xpath=//*[@id='adroll_consent_reject']//*[@class='adroll_button_text']");
        this.personalDetailsWindowTitle = page.locator("xpath=//span[normalize-space()='Personal Details']");
        this.genderMaleRadioButton = page.locator("xpath=//span[normalize-space()='Male']");
        this.dobDay = page.locator("xpath=//input[@id='dob']");
        this.dobMonth = page.locator("xpath=//input[@placeholder='Month']");
        this.dobYear = page.locator("xpath=//input[@placeholder='Year']");
        this.dayValueDropdown = page.locator("xpath=//body/div[@class='el-select-dropdown el-popper']//*/ul[@class='el-scrollbar__view el-select-dropdown__list']/li[1]");
        this.monthValueDropdown = page.locator("xpath=//*[contains(@class,'el-select-dropdown__item') and @data-testid='01']");
        this.yearValueDropdown = page.locator("xpath=//*[@data-testid='1990']");
        this.userPhoneNumber = page.locator("xpath=//input[@id='mobile']");
        this.nextButton = page.locator("xpath=//button[@data-testid='next']");

        this.accountCurrencyBlock = page.locator("xpath=//li[@data-testid='USD']");
        this.acceptTermsCheckbox = page.locator("xpath=//span[@class='el-checkbox__inner']");
    }

    @Step("Check if the page loaded")
    public void isPageLoaded() {
        int n = 0;
        page.waitForTimeout(2000);
        while (loader.isVisible() && n < 10) {
            page.waitForTimeout(2000);
            n += 1;
        }
        page.waitForTimeout(2000);
    }

    @Step("Check if element loaded")
    public void isElementLoaded(Locator locator) {
        int n = 0;
        page.waitForTimeout(2000);
        while (locator.isVisible() && n < 10) {
            page.waitForTimeout(2000);
            n += 1;
        }
    }

    @Step("Check that page present")
    public void checkPagePresent() {
        String url = BASE_URL_VANTAGE_ACCOUNT + "/register";
        writeLog("URL to check: " + url);
        page.waitForURL(url);
        writeLog("URL: " + url + " PRESENTED");
    }

    @Step("Check cookie banner present")
    public Boolean checkCookieBanner() {
        return declineCookies.isVisible();
    }

    @Step("Check cookie banner present")
    public void closeCookieBanner() {
        if (checkCookieBanner()) {
            declineCookies.click();
        } else {
            writeLog("Cookies banner wasn't presented");
        }
    }

    @Step("Close alert window")
    public void closeAlertWindow() {
        isPageLoaded();
        if (closeAlertButton.isVisible()) {
            closeAlertButton.waitFor();
            closeAlertButton.click();
        }
        isElementLoaded(closeBannerButton);
        if (closeBannerButton.isVisible()) {
            closeBannerButton.waitFor();
            closeBannerButton.click();
        }
    }

    @Step("Wait for personal details window presented")
    public void waitPersonalDetailsWindowPresented() {
        personalDetailsWindowTitle.waitFor();
    }

    @Step("Choose gender")
    public void chooseGender() {
        genderMaleRadioButton.click();
    }

    @Step("Select date of birth")
    public void selectDateOfBirth(String day, String month, String year) {
        dobDay.click();
        dayValueDropdown.click();
        dobMonth.click();
        monthValueDropdown.click();
        dobYear.click();
        dobYear.fill(year);
        yearValueDropdown.click();
    }

    @Step("Fill phone number")
    public void fillPhoneNumber(String phone) {
        userPhoneNumber.fill(phone);
    }

    @Step("Click Next button")
    public void clickNextButton() {
        nextButton.click();
    }

    @Step("Choose currency")
    public void chooseCurrency() {
        accountCurrencyBlock.click();
    }

    @Step("Choose currency")
    public void acceptTerms() {
        acceptTermsCheckbox.check();
    }
}
