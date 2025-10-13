package page_objects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

public class StageRegistrationHelperPage {
    private final Page page;
    private final Locator actionInput;
    private final Locator updateActionButton;
    private final Locator userNameInput;
    private final Locator userSecondNameInput;
    private final Locator userCountryDropdown;
    private final Locator userEmailInput;
    private final Locator userEmailCodeInput;
    private final Locator userPasswordInput;
    private final Locator regulatorDropdown;
    private final Locator widInput;
    private final Locator registerInterfaceDropdown;
    private final Locator sendButton;

    public StageRegistrationHelperPage(Page page) {
        this.page = page;
        this.actionInput = page.locator("xpath=//input[@id='email_url']");
        this.updateActionButton = page.locator("xpath=//button[@id='change_button']");
        this.userNameInput = page.locator("xpath=//input[@id='firstName']");
        this.userSecondNameInput = page.locator("xpath= //input[@id='lastName']");
        this.userCountryDropdown = page.locator("xpath=//input[@id='country']");
        this.userEmailInput = page.locator("xpath=//input[@id='email']");
        this.userEmailCodeInput = page.locator("xpath=//input[@id='captcha']");
        this.userPasswordInput = page.locator("xpath=//input[@id='password']");
        this.regulatorDropdown = page.locator("xpath=//select[@id='regulator']");
        this.widInput = page.locator("xpath=//input[@id='wid']");
        this.registerInterfaceDropdown = page.locator("xpath=//select[@id='register-interface']");
        this.sendButton = page.locator("xpath=//button[@id='sub-open']");
    }

    @Step("Open the stage registration helper page")
    public void navigate(String login, String password) {
        page.navigate("https://" + login + ":" + password + "@testreg.marketsdata1.com");
    }

    @Step("Update action")
    public void updateAction(String value) {
        actionInput.fill(value);
        updateActionButton.click();
    }

    @Step("Fill name")
    public void fillName(String name) {
        userNameInput.fill(name);
    }

    @Step("Fill second name")
    public void fillSecondName(String secondName) {
        userSecondNameInput.fill(secondName);
    }

    @Step("Choose country")
    public void chooseCountry(String country) {
        userCountryDropdown.fill(country);
    }

    @Step("Fill email")
    public void fillEmail(String email) {
        userEmailInput.fill(email);
    }

    @Step("Fill email verification code")
    public void fillEmailVerificationCode(String code) {
        userEmailCodeInput.fill(code);
    }

    @Step("Fill password")
    public void fillPassword(String password) {
        userPasswordInput.fill(password);
    }

    @Step("Choose regulator")
    public void chooseRegulator(String regulator) {
        regulatorDropdown.selectOption(regulator);
    }

    @Step("Fill wid")
    public void fillWid(String wid) {
        widInput.fill(wid);
    }

    @Step("Choose register interface")
    public void chooseRegisterInterface(String registerInterface) {
        registerInterfaceDropdown.selectOption(registerInterface);
    }

    @Step("Click send form button")
    public void clickSendFormButton() {
        sendButton.click();
    }
}
