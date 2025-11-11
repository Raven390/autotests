package page_objects.backoffice_pages.investigationTool;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.ElementState;
import com.microsoft.playwright.options.WaitForSelectorState;
import helpers.data.enums.FraudSubtype;
import helpers.data.enums.FraudType;
import helpers.data.enums.FraudTypeStatus;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import page_objects.backoffice_pages.AbstractPage;

import java.util.ArrayList;
import java.util.List;


import static helpers.data.enums.FraudTypeStatus.CONFIRMED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

public class ResolvePage extends AbstractPage {

    private static final String investigationCompleted = "Investigation completed";
    private static final String FRAUD_MANAGEMENT_COMPLETED = "Fraud management completed";
    public static final String FRAUD_TYPE_SELECTOR_FORMATTER = "div[data-qa='client_payment_resolving_drawer__fraud_type_selector__item_%s__%s']";
    public static final String REJECTION_REASON_INPUT_SELECTOR = ".v-rejection-reason-plain-input__input";
    public static final String FRAUD_MENU_ITEM_SELECTOR = "div.g-popup__content div.v-sub-menu__anchor";
    public static final String DRAWER_HEADER_SELECTOR = "[data-qa='drawer_header']";
    public static final String RESOLUTION = "Resolution";

    private final Locator loaderAnimation;
    private final Locator loaderSpin;
    private final Locator resolveButton;
    private final Locator investigateButton;
    private final Locator commentInput;
    private final Locator withdrawalList;
    private final Locator approveAllwithdrawalsButton;
    private final Locator rejectAllwithdrawalsButton;
    private final Locator completeInvestigationButton;
    private final Locator successToast;
    private final Locator closeToastButton;
    private final Locator cleanFraudListButton;
    private final Locator fraudListButton;
    private final Locator fraudTypeDropdownOption;
    private final Locator fraudSelectApplyButton;
    private final Locator clientRestrictionItem;
    private final Locator reportFraudButton;
    private final Locator reportForm;
    private final Locator submitFraudButton;
    private final Locator fraudDeletionPopup;
    private final Locator confirmFraudDeletionButton;
    private final Locator resetFraudChangesButton;
    private final Locator restrictionListButton;
    private final Locator applyButton;
    private final Locator resetRestrictionsChangesButton;
    private final Locator suggestedDeductionHeader;
    private final Locator suggestedDeductionAmount;
    private final Locator noDeductionSwitch;
    private final Locator illegalProfitAccountsDropdown;
    private final Locator suggestedDeductionItems;
    private final Locator suggestedDeductionValues;
    private final Locator thunderIcon;
    private final Locator accountIllegalProfit;
    private final Locator useAsIllegalProfitButton;
    private final Locator suggestedDeductionSection;
    private final Locator previouslyReportedFraudItem;
    private final Locator previouslyReportedFraudTypes;
    private final Locator previouslyReportedFraudSymbols;
    private final Locator fraudSourceDropdown;
    private final Locator illegalProfitPartialAmount;
    private final Locator selectedFraud;
    private final Locator addDeductionButton;
    private final Locator symbolDropdown;
    private final Locator dropdownOptions;
    private final Locator addRestrictionButton;
    // Rejection reasons UI
    private final Locator rejectionReasonSelects;
    private final Locator rejectionReasonItems;
    private final Locator rejectionDynamicInputs;
    private final Locator rejectionReasonInput;

    private static final String SELECTED_FRAUD_LOCATOR = "//div[@data-qa='selected_fraud_type_item']";
    private static final String FRAUD_TYPE_POPUP_LOCATOR = "//*[contains(@class, 'v-fraud-type-v2__popup')]";
    private static final String REMOVE_BUTTON_LOCATOR = "//button[@data-qa='selected_fraud_type_item__remove_button']";
    private static final String RESTRICTION_LIST_LOCATOR_ANCESTOR = "//ancestor::*[@class='v-client-restrictions-list-item']";
    private static final String RESTRICTION_LIST_LOCATOR = "//*[@class='v-client-restrictions-list-item']";
    private static final String RESTRICTION_DELETION_POPUP_LOCATOR = "//*[contains(@class,'v-client-restrictions-list-item__popup') and contains(@class,'g-popup ')]";
    private static final String RESET_FRAUD_CHANGES_BUTTON_LOCATOR = "//button[contains(@data-qa,'raud_type_selector__reset')]";
    private static final String RESET_RESTRICTION_CHANGES_BUTTON_LOCATOR = "//button[contains(@data-qa,'restrictions_selector__reset')]";
    private static final String FRAUD_CONTAINER_BY_NAME_PATTERN = "//span[text()='%s']/ancestor::div[contains(@data-qa,'client_report_fraud_drawer__reported_fraud_types_list__item')]";
    private static final String FRAUD_TIME_BY_NAME_PATTERN = String.format("%s/descendant::div[contains(@class,'g-color-text_color_secondary')]", FRAUD_CONTAINER_BY_NAME_PATTERN);
    private static final String DELETE_FRAUD_BY_NAME_PATTERN = String.format("%s/descendant::button", FRAUD_CONTAINER_BY_NAME_PATTERN);
    private static final String FRAUD_DROPOUT_LIST_ELEMENT_LOCATOR_PATTERN = "//*[contains(@class,'v-dropdown-select-item-base')]/div/div[text()='%s']";
    private static final String FRAUD_DROPOUT_LIST_ELEMENT_LOCATOR = "//*[contains(@class,'v-dropdown-select-item-base')]/div/div";
    private static final String FRAUD_BY_TEXT_PATTERN = "div.g-popup__content div.v-sub-menu__anchor >> text=%s";
    private static final String FRAUD_STATUS_PATTERN = "//div[@class='g-popup__content' or contains(@class,'v-sub-menu__content')]/descendant::div[contains(@data-qa,'fraud_type_selector__item_%s__%s')]";
    private static final String DROPDOWN_ITEM_BY_ACCOUNT = "//div[text()='%s']/ancestor::div[@class='v-suggested-deduction-select__item']";
    private static final String CONFIRMED_FRAUD_BUTTON_BY_FRAUD_TYPE_PATTERN = "//div[contains(@data-qa,'fraud_type_selector__submenu_%s')]";
    private static final String FRAUD_SUBTYPE_BUTTON_BY_FRAUD_TYPE_PATTERN = "//div[contains(@data-qa,'fraud_type_selector__submenu_%s__item')]";
    private final Locator cleanRestrictionListButton;


    public ResolvePage(Page page) {
        super(page);
        this.loaderAnimation = page.locator(".v-loader");
        this.loaderSpin = page.locator(".g-spin");
        this.resolveButton = page.locator(".g-button__text").getByText("Resolve");
        this.reportFraudButton = page.locator("[data-qa=investigation_tools__report_fraud_button]");
        this.investigateButton = page.locator(".g-button__text").getByText("Investigate");
        this.completeInvestigationButton = page.locator("//button[contains(@data-qa,'complete_investigation_button') or contains(@data-qa,'apply_changes_button')]");
        this.reportForm = page.locator("[data-qa='drawer_body']").getByText("Fraud management");
        this.commentInput = page.locator(".v-drawer-section-layout textarea");
        this.withdrawalList = page.locator(".v-withdrawals-list-with-rejections__list");
        this.approveAllwithdrawalsButton = page.locator(".v-rejection-decision-input button").nth(0);
        this.rejectAllwithdrawalsButton = page.locator(".v-rejection-decision-input .g-button").nth(1);
        this.successToast = page.locator(".g-toast__container").first();
        this.closeToastButton = page.locator(".g-button.g-toast__btn-close").first();
        this.cleanFraudListButton = page.locator("//div[@class='v-fraud-type-v2']/descendant::button[@data-qa='selected_fraud_type_item__remove_button']").first();
        this.cleanRestrictionListButton = page.locator("button[data-qa=\"button_with_confirmation\"]");
        this.fraudListButton = page.locator("//button[contains(@data-qa,'fraud_type_selector__anchor')]");
        this.restrictionListButton = page.locator("//*[text()='Restrictions']/..//button");
        this.fraudTypeDropdownOption = page.locator("//div[@class='v-drop-down-menu-2__content']/descendant::div[contains(@class,'v-sub-menu__anchor')]");
        this.fraudSelectApplyButton = page.locator("[data-qa='fraud_type_select_apply_button']");
        this.applyButton = page.locator("//button/*[text()='Apply']");
        this.clientRestrictionItem = page.locator(".v-client-restrictions-list-item__item-body");
        this.submitFraudButton = page.locator("button[data-qa='report_fraud_drawer__submit_button']");
        this.fraudDeletionPopup = page.locator("//div[@class='v-fraud-type-v2__action-content']");
        this.confirmFraudDeletionButton = page.locator("//span[@class='g-button__text' and text()='Yes']");
        this.resetFraudChangesButton = page.locator(RESET_FRAUD_CHANGES_BUTTON_LOCATOR);
        this.resetRestrictionsChangesButton = page.locator(RESET_RESTRICTION_CHANGES_BUTTON_LOCATOR);
        this.suggestedDeductionHeader = page.locator("//div[@class='v-suggested-deduction__header']");
        this.suggestedDeductionAmount = suggestedDeductionHeader.locator("//span[contains(@class,'g-text')]");
        this.noDeductionSwitch = suggestedDeductionHeader.locator("//input");
        this.illegalProfitAccountsDropdown = suggestedDeductionHeader.locator("//span[@class='g-button__text']");
        this.suggestedDeductionItems = page.locator("//div[@class='v-suggested-deduction-item__info']");
        this.suggestedDeductionValues = page.locator("//div[@class='v-suggested-deduction-item__deduction']");
        this.thunderIcon = page.locator("//*[contains(@class,'g-icon v-suggested-deduction-select__thunder')]");
        this.accountIllegalProfit = page.locator("//span[contains(@class,'g-text')]");
        this.useAsIllegalProfitButton = page.locator("//div[@class='v-suggested-deduction-select__controls']/button[contains(@class,'g-button_view_action')]");
        this.suggestedDeductionSection = page.locator("//div[@class='v-suggested-deduction']");
        this.previouslyReportedFraudItem = page.locator("//div[contains(@data-qa,'client_report_fraud_drawer__reported_fraud_types_list__item')]");
        this.previouslyReportedFraudTypes = page.locator("//span[contains(@class,'g-color-text_color_primary')]");
        this.previouslyReportedFraudSymbols = page.locator("//div[@class='v-reported-fraud-type-list__symbols']");
        this.fraudSourceDropdown = page.locator("//button[@data-qa='source_select__select_control']");
        this.illegalProfitPartialAmount = suggestedDeductionHeader.locator("//div[@class='v-text-with-icon__text']");
        this.selectedFraud = page.locator("//div[contains(@data-qa,'detected_fraud_types_list__item')]/descendant::span[contains(@class,'g-color-text_color_primary')]");
        this.addDeductionButton = page.locator("button[data-qa='client_report_fraud_drawer__add_deduction']");
        this.symbolDropdown = page.locator("//button[@data-qa='fraud_type_symbol_select__select_control']");
        this.dropdownOptions = page.locator("//span[@class='g-select-list__option-default-label']");
        this.addRestrictionButton = page.locator("//div[contains(@data-qa,'restrictions_selector')]/descendant::button[not(@data-qa)]");
        // Rejection reasons UI
        this.rejectionReasonSelects = page.locator("[data-qa='select-popup']");
        this.rejectionReasonInput = page.locator(".v-rejection-reason-input");
        this.rejectionReasonItems = page.locator("[data-qa='select-popup'] [role='option']");

        this.rejectionDynamicInputs = page.locator(".v-rejection-reason__dynamic-values input, .v-dynamic-attributes input[type='text'], input[data-qa='rejection_dynamic_attribute_input']");
    }

    String bigLorem = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc facilisis, metus eu mattis suscipit, est felis venenatis nunc, eu rhoncus sapien tortor sed turpis. Integer vitae leo pharetra, pellentesque nisi quis, pharetra arcu. Curabitur nec arcu ac.";
    String smallLorem = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc facilisis, metus eu mattis suscipit, est felis venenatis nunc, eu rhoncus sapien tortor sed turpis. Integer vitae leo pharetra, pellentesque nisi quis, pharetra arcu. Curabitur nec arcu ac";

    @Step("Check if the page loaded")
    public void isPageLoaded() {
        int n = 0;
        page.waitForTimeout(2000);
        while (loaderAnimation.isVisible() || (loaderSpin.isVisible()) && n < 8) {
            page.waitForTimeout(2000);
            n += 1;
        }
    }

    @Step("Get Withdrawal list")
    public List<String> getWithdrawalList() {
        Locator resolution = page.locator(DRAWER_HEADER_SELECTOR).getByText(RESOLUTION);
        resolution.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        List<String> list = new ArrayList<>();
        for (int i = 0; i < withdrawalList.count(); i++) {
            list.add(withdrawalList.nth(i).textContent());
        }
        return list;
    }

    @Step("Get Withdrawal list")
    public boolean isWithdrawalListVisible() {
        Locator resolution = page.locator(DRAWER_HEADER_SELECTOR).getByText(RESOLUTION);
        resolution.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        return withdrawalList.isVisible();
    }

    @Step("Open resolve form in suspicious client")
    public void openResolveSuspicious() {
        isPageLoaded();
        page.waitForTimeout(200);
        if (resolveButton.isVisible()) {
            resolveButton.click();
        } else {
            investigateButton.click();
            resolveButton.click();
        }
        isPageLoaded();
        if (successToast.isVisible()) {
            closeToastButton.click();
        } else {
            page.waitForTimeout(1);
        }
    }

    @Step("approve all withdrawals")
    public void clickWithdrawalApprove() {
        approveAllwithdrawalsButton.click();
    }


    public void clickCleanRestrictionList() {
        cleanRestrictionListButton.click();
    }

    @Step("reject all withdrawals")
    public void clickWithdrawalReject(String reasonTitle, String... dynamicValues) {
        rejectAllwithdrawalsButton.click();
        // Select predefined rejection reason and fill dynamic values if required
        selectRejectionReasonAndFillDynamics(reasonTitle, dynamicValues);
    }

    @Step("Resolve and approve all withdrawals")
    public void resolveWithdrawalsAllApprove(String comment) {
        approveAllwithdrawalsButton.click();
        commentInput.fill(comment);
        completeInvestigationButton.click();
        successToast.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    @Step("Resolve and reject all withdrawals")
    public void resolveWithdrawalsAllReject(String reasonTitle, String... dynamicValues) {
        Locator resolution = page.locator(DRAWER_HEADER_SELECTOR).getByText(RESOLUTION);
        resolution.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        rejectAllwithdrawalsButton.click();
        selectRejectionReasonAndFillDynamics(reasonTitle, dynamicValues);
        commentInput.fill("autotest to withdrawals");
        completeInvestigationButton.click();
        successToast.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    @Step("Resolve without any actions")
    public void resolveNoActions(String comment) {
        fillCommentAndApply(comment);
        successToast.getByText(investigationCompleted).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    @Step("Apply fraud management")
    public void applyFraudManagement(String comment) {
        fillCommentAndApply(comment);
        successToast.getByText(FRAUD_MANAGEMENT_COMPLETED).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    @Step("Fill comment and apply")
    public void fillCommentAndApply(String comment) {
        commentInput.fill(comment);
        completeInvestigationButton.click();
    }


    public void addFraud() {
        fraudListButton.click();
        Locator menuItems = page.locator(FRAUD_MENU_ITEM_SELECTOR);
        Locator target = menuItems.first();
        String fraudCode = FraudType.valueOfName(target.textContent()).getCode();

        target.hover();
        page.locator(FRAUD_TYPE_SELECTOR_FORMATTER.formatted(fraudCode, CONFIRMED.getDisplayName().toLowerCase())).click();
    }

    public void addFraud(FraudType fraud, FraudTypeStatus status) {
        fraudListButton.click();
        page.locator(FRAUD_MENU_ITEM_SELECTOR).getByText(fraud.getName()).hover();
        page.locator(FRAUD_TYPE_SELECTOR_FORMATTER.formatted(fraud.getCode(), status.getDisplayName().toLowerCase())).click();
    }


    public void addFraud(FraudType fraud, FraudSubtype subtype) {
        fraudListButton.click();
        page.locator(String.format(FRAUD_BY_TEXT_PATTERN, fraud.getName())).hover();
        page.locator(String.format(FRAUD_BY_TEXT_PATTERN, fraud.getName())).hover();
        page.locator(String.format(CONFIRMED_FRAUD_BUTTON_BY_FRAUD_TYPE_PATTERN, fraud.getCode())).hover();
        page.locator(String.format(FRAUD_SUBTYPE_BUTTON_BY_FRAUD_TYPE_PATTERN, fraud.getCode())).getByText(subtype.getName()).click();
    }

    public void addFraud(FraudType fraud) {
        addFraud(fraud, CONFIRMED);
    }

    public void addRestriction(String... addedRestriction) {
        Allure.step("add fraud on resolve screen");
        restrictionListButton.click();
        for (String i : addedRestriction) {
            page.getByRole(AriaRole.OPTION).getByText(i).click();
        }
        applyButton.click();
    }

    public void previouslyReportedFraudAddDeduction() {
        Allure.step("Click add deduction button");
        addDeductionButton.click();
    }

    public void resetFraudsChanges() {
        Allure.step("click 'Reset fraud changes' button");
        resetFraudChangesButton.click();
    }

    public void resetRestrictionChanges() {
        Allure.step("click 'Reset restriction changes' button");
        resetRestrictionsChangesButton.click();
    }

    public List<String> getFraudTypesList() {
        fraudListButton.click();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < fraudTypeDropdownOption.count(); i++) {
            list.add(fraudTypeDropdownOption.nth(i).textContent());
        }
        return list;
    }

    public void openReportFraudForm() {
        isPageLoaded();
        reportFraudButton.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        reportFraudButton.click();
        reportForm.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        assertTrue(reportForm.isVisible());
    }

    public void cantOpenReportFraudForm() {
        isPageLoaded();
        reportFraudButton.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        assertTrue(reportFraudButton.isDisabled());
    }

    @Step("Add new fraud in report fraud drawer")
    public void reportAddFraud(String comment, FraudType addedFraud, FraudTypeStatus fraudStatus) {
        commentInput.fill(comment);
        addFraud(addedFraud, fraudStatus);
        submitFraudButton.click();
        successToast.getByText("Fraud management completed").waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        assertTrue(successToast.getByText("Fraud management completed").isVisible());
    }

    @Step("Resolve with adding fraud")
    public void checkPreviousConfirmedFraudDisplayed(String addedFraud) {
        String locator = "//div[@data-qa='selected_fraud_type_item']//*[text()='" + addedFraud + "']";
        page.waitForSelector(locator).waitForElementState(ElementState.VISIBLE);
    }

    @Step("Get time label for the provided fraud")
    public String getFraudTimeByName(String fraudName) {
        Allure.step("Get time label for the provided fraud");
        return page.locator(String.format(FRAUD_TIME_BY_NAME_PATTERN, fraudName)).textContent();
    }

    @Step("Click delete fraud and confirm the popup")
    public void deleteFraudByName(String fraudName) {
        Allure.step("Click delete fraud and confirm the popup");
        page.locator(String.format(DELETE_FRAUD_BY_NAME_PATTERN, fraudName)).click();
        com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat(fraudDeletionPopup).containsText("Are you sure that the client should not be identified with this fraud anymore?");
        confirmFraudDeletionButton.click();
        commentInput.fill(String.format("confirm %s", fraudName));
        submitFraudButton.click();
    }

    @Step("Click delete fraud and confirm the popup")
    public void deleteFraudByNameNoPopup(String fraudName) {
        page.locator(String.format(DELETE_FRAUD_BY_NAME_PATTERN, fraudName)).click();
    }

    public List<String> getSelectedRestrictionsList() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < clientRestrictionItem.count(); i++) {
            list.add(clientRestrictionItem.nth(i).textContent());
        }
        return list;
    }

    @Deprecated()//"version for old UI"
    public void addFraudOld(String... addedFraud) {
        Allure.step("add fraud on resolve screen");
        fraudListButton.click();
        for (String i : addedFraud) {
            fraudTypeDropdownOption.getByText(i).click();
        }
        fraudSelectApplyButton.click();
    }

    @Step("Get suggested deduction amount")
    public String getSuggestedDeductionAmount() {
        return suggestedDeductionAmount.textContent();
    }

    @Step("Get illegal profit amount")
    public String getIllegalProfitAmount() {
        return illegalProfitAccountsDropdown.textContent();
    }

    @Step("Get suggested deduction items")
    public List<String> getSuggestedDeductionItems() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < suggestedDeductionItems.count(); i++) {
            list.add(suggestedDeductionItems.nth(i).textContent());
        }
        return list;
    }

    @Step("Get suggested deduction values")
    public List<String> getSuggestedDeductionValues() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < suggestedDeductionValues.count(); i++) {
            list.add(suggestedDeductionValues.nth(i).textContent());
        }
        return list;
    }

    @Step("Click illegal profit accounts dropdown")
    public void clickIllegalProfitAccountsDropdown() {
        illegalProfitAccountsDropdown.click();
    }

    @Step("Is lightning icon visible for the account")
    public Boolean isAccountWithAlert(String account) {
        return page.locator(String.format(DROPDOWN_ITEM_BY_ACCOUNT, account)).locator(thunderIcon).isVisible();
    }

    @Step("Click account in illegal profit accounts dropdown")
    public void clickAccountInDropdown(String account) {
        page.locator(String.format(DROPDOWN_ITEM_BY_ACCOUNT, account)).locator(accountIllegalProfit).click();
    }

    @Step("Click Use as illegal profit button")
    public void clickUseAsIllegalProfit() {
        useAsIllegalProfitButton.click();
    }

    @Step("Click No deduction switch")
    public void clickNoDeductionSwitch() {
        noDeductionSwitch.click();
    }

    @Step("Is Suggested deduction section visible")
    public Boolean isSuggestedDeductionSectionVisible() {
        return suggestedDeductionSection.isVisible();
    }

    @Step("Get previously reported fraud items")
    public List<List<String>> getPreviouslyReportedFraudItems() {
        List<List<String>> list = new ArrayList<>();
        for (int i = 0; i < previouslyReportedFraudItem.count(); i++) {
            List<String> typesList = new ArrayList<>();
            typesList.add(previouslyReportedFraudItem.nth(i).locator(previouslyReportedFraudTypes).textContent());
            typesList.add(previouslyReportedFraudItem.nth(i).locator(previouslyReportedFraudSymbols).textContent());
            list.add(typesList);
        }
        return list;
    }

    @Step("Get previously reported fraud items")
    public List<String> getPreviouslyReportedFraudItems2() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < previouslyReportedFraudItem.count(); i++) {
            list.add(previouslyReportedFraudItem.nth(i).locator(previouslyReportedFraudTypes).textContent());
        }
        return list;
    }

    @Step("Select predefined rejection reason and fill dynamic values if required")
    protected void selectRejectionReasonAndFillDynamics(String reasonTitle, String... dynamics) {
        Locator reasonOption = rejectionReasonItems.getByText(reasonTitle);
        reasonOption.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        reasonOption.click();

        Locator inputs = page.locator(REJECTION_REASON_INPUT_SELECTOR);
        int count = inputs.count();

        if (count == 0) {
            return;
        }

        assertThat("Length of dynamic inputs is equals to received arguments", inputs.count(), is(dynamics.length));
        for (int i = 0; i < count; i++) {
            Locator input = inputs.nth(i);

            try {
                input.scrollIntoViewIfNeeded();
                input.click();
                input.fill(dynamics[i]);
            } catch (Throwable ignored) {
            }
        }
    }


    @Step("Select fraud source")
    public void selectFraudSource(String fraudSource) {
        fraudSourceDropdown.click();
        page.getByText(fraudSource).click();
    }

    @Step("Get illegal profit partial amount")
    public String getIllegalProfitPartialAmount() {
        return illegalProfitPartialAmount.textContent();
    }

    @Step("Get selected fraud")
    public String getSelectedFraud() {
        return selectedFraud.textContent();
    }

    @Step("Get fraud subtypes by type")
    public List<String> getFraudSubtypesList(FraudType fraud) {
        fraudTypeDropdownOption.getByText(fraud.getName()).hover();
        page.locator(String.format(CONFIRMED_FRAUD_BUTTON_BY_FRAUD_TYPE_PATTERN, fraud.getCode())).hover();
        Locator fraudSubtypes = page.locator(String.format(FRAUD_SUBTYPE_BUTTON_BY_FRAUD_TYPE_PATTERN, fraud.getCode()));
        List<String> list = new ArrayList<>();
        for (int i = 0; i < fraudSubtypes.count(); i++) {
            list.add(fraudSubtypes.nth(i).textContent());
        }
        return list;
    }

    @Step("Click symbol dropdown")
    public void clickSymbolDropdown() {
        symbolDropdown.click();
    }

    @Step("Get list of available symbols")
    public List<String> getSymbolsList() {
        clickSymbolDropdown();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < dropdownOptions.count(); i++) {
            list.add(dropdownOptions.nth(i).textContent());
        }
        clickSymbolDropdown();
        dropdownOptions.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
        return list;
    }

    @Step("Click add fraud button")
    public void clickFraudListButton() {
        fraudListButton.click();
    }

    @Step("Click add restriction button")
    public void clickAddRestrictionButton() {
        addRestrictionButton.click();
    }

    @Step("Get list of available restrictions")
    public List<String> getRestrictionsList() {
        resetRestrictionChanges();
        clickAddRestrictionButton();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < dropdownOptions.count(); i++) {
            list.add(dropdownOptions.nth(i).textContent());
        }
        return list;
    }
}
