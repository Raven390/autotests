package page_objects.backoffice_pages;

import business_objects.kafka.restriction_events.WithdrawalApprovals;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.ElementState;
import com.microsoft.playwright.options.WaitForSelectorState;
import helpers.data.enums.FraudType;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;

import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class ResolvePage extends AbstractPage {

    private static final String investigationCompleted = "Investigation completed";

    private final Locator loaderAnimation;
    private final Locator loaderSpin;
    private final Locator resolveButton;
    private final Locator investigateButton;
    private final Locator resolutionForm;
    private final Locator commentInput;
    private final Locator withdrawalList;
    private final Locator approveAllwithdrawalsButton;
    private final Locator rejectAllwithdrawalsButton;
    private final Locator approveFirstButton;
    private final Locator completeInvestigationButton;
    private final Locator successToast;
    private final Locator closeToastButton;
    private final Locator cleanFraudListButton;
    private final Locator fraudListButton;
    private final Locator fraudSelectItem;
    private final Locator fraudSelectApplyButton;
    private final Locator clientRestrictionItem;
    private final Locator approveSecondButton;
    private final Locator submitCommentButton;
    private final Locator reportFraudButton;
    private final Locator reportForm;
    private final Locator submitFraudButton;
    private final Locator selectedFraudLabel;
    private final Locator fraudDeletionPopup;
    private final Locator confirmFraudDeletionButton;
    private final Locator resetButton;
    private final Locator restrictionListButton;
    private final Locator applyButton;

    private final String SELECTED_FRAUD_LOCATOR = "//div[@data-qa='selected_fraud_type_item']";
    private final String FRAUD_TYPE_POPUP_LOCATOR = "//*[contains(@class, 'v-fraud-type__popup')]";
    private final String REMOVE_BUTTON_LOCATOR = "//button[@data-qa='selected_fraud_type_item__remove_button']";
    private final String RESTRICTION_LIST_LOCATOR_ANCESTOR = "//ancestor::*[@class='v-client-restrictions-list-item']";
    private final String RESTRICTION_LIST_LOCATOR = "//*[@class='v-client-restrictions-list-item']";
    private final String RESTRICTION_DELETION_POPUP_LOCATOR = "//*[contains(@class,'v-client-restrictions-list-item__popup') and contains(@class,'g-popup ')]";
    private final String RESET_CHANGES_BUTTON_LOCATOR = "//*[@data-qa='fraud_type_selector_clear_button']";
    private final String FRAUD_CONTAINER_BY_NAME_PATTERN = "//span[text()='%s']/ancestor::div[@class='v-fraud-type']";
    private final String FRAUD_TIME_BY_NAME_PATTERN = String.format("%s/descendant::div[contains(@class,'g-color-text_color_secondary')]", FRAUD_CONTAINER_BY_NAME_PATTERN);
    private final String DELETE_FRAUD_BY_NAME_PATTERN = String.format("%s/descendant::button[@data-qa='selected_fraud_type_item__remove_button']", FRAUD_CONTAINER_BY_NAME_PATTERN);


    public ResolvePage(Page page) {
        super(page);
        this.loaderAnimation = page.locator(".v-loader");
        this.loaderSpin = page.locator(".g-spin");
        this.resolveButton = page.locator(".g-button__text").getByText("Resolve");
        this.reportFraudButton = page.locator("[data-qa=investigation_tools__report_fraud_button]");
        this.investigateButton = page.locator(".g-button__text").getByText("Investigate");
        this.completeInvestigationButton = page.locator(".g-button__text").getByText("Complete investigation");
        this.resolutionForm = page.locator("[data-qa='drawer_body']").getByText("Resolution");
        this.reportForm = page.locator("[data-qa='drawer_body']").getByText("Report fraud");
        this.commentInput = page.locator(".v-drawer-section-layout textarea");
        this.withdrawalList = page.locator(".v-withdrawals-list");
        this.approveAllwithdrawalsButton = page.locator(".v-withdrawals-list__reject-resolve button").nth(0);
        this.rejectAllwithdrawalsButton = page.locator(".v-withdrawals-list__reject-resolve button").nth(1);
        this.approveFirstButton = page.locator(".v-withdrawals-list__reject-resolve button").nth(2);
        this.approveSecondButton = page.locator(".v-withdrawals-list__reject-resolve button").nth(4);
        this.successToast = page.locator(".g-toast__container").first();
        this.closeToastButton = page.locator(".g-button.g-toast__btn-close").first();
        this.cleanFraudListButton = page.locator("button[data-qa='selected_fraud_type_item__remove_button']");
        this.fraudListButton = page.locator("button[data-qa='fraud_type_select_anchor_button']");
        this.restrictionListButton = page.locator("//*[text()='Active restrictions']/..//button");
        this.fraudSelectItem = page.locator("[data-qa='fraud_type_select_item']");
        this.fraudSelectApplyButton = page.locator("[data-qa='fraud_type_select_apply_button']");
        this.applyButton = page.locator("//button/*[text()='Apply']");
        this.clientRestrictionItem = page.locator(".v-client-restrictions-list-item__item-body");
        this.submitCommentButton = page.locator("[data-qa='investigation_tools__add_comment_textarea_container']");
        this.submitFraudButton = page.locator("button[data-qa='report_fraud_drawer__submit_button']");
        this.selectedFraudLabel = page.locator(SELECTED_FRAUD_LOCATOR);
        this.fraudDeletionPopup = page.locator("//div[@class='v-fraud-type__action-content']");
        this.confirmFraudDeletionButton = page.locator("//span[@class='g-button__text' and text()='Yes']");
        this.resetButton = page.locator(RESET_CHANGES_BUTTON_LOCATOR);
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

    @Step("Resolve and approve all withdrawals")
    public void resolveWithdrawalsAllApprove() {
//        page.waitForSelector(resolutionForm.toString());
        resolutionForm.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        approveAllwithdrawalsButton.click();
        commentInput.fill("autotest to withdrawals");
        completeInvestigationButton.click();
        successToast.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    @Step("Resolve and approve all withdrawals")
    public void resolveWithdrawalsAllApprove(String comment) {
        resolutionForm.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        approveAllwithdrawalsButton.click();
        commentInput.fill(comment);
        completeInvestigationButton.click();
        successToast.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    @Step("Resolve and reject all withdrawals")
    public void resolveWithdrawalsAllReject() {
        resolutionForm.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        rejectAllwithdrawalsButton.click();
        commentInput.fill("autotest to withdrawals");
        completeInvestigationButton.click();
        successToast.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    @Step("Resolve and reject all withdrawals")
    public void resolveWithdrawalsAllReject(String comment) {
        Allure.step("Resolve and reject all withdrawals");
        resolutionForm.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        rejectAllwithdrawalsButton.click();
        commentInput.fill(comment);
        completeInvestigationButton.click();
        successToast.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    @Step("Resolve and approve one withdrawal")
    public void resolveWithdrawalsApproveFirst() {
        resolutionForm.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        rejectAllwithdrawalsButton.click();
        approveFirstButton.click();
        commentInput.fill("autotest to withdrawals");
        completeInvestigationButton.click();
        successToast.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    public void clickApproveWithdrawalByPaymentType(String paymentType) {
        Allure.step("click approve withdrawal on selected transaction");
        String locator = "//div[text() = '" + paymentType + "']/ancestor::div[@class = 'v-withdrawals-list__list-item']//button[1]";
        page.waitForSelector(locator);
        page.locator(locator).click();
    }

    @Step("Resolve and approve one withdrawal")
    public void resolveWithdrawalsApproveOneByType(String paymentType) {
        resolutionForm.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        rejectAllwithdrawalsButton.click();
        clickApproveWithdrawalByPaymentType(paymentType);
        commentInput.fill("autotest to withdrawals");
        completeInvestigationButton.click();
        successToast.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    @Step("Resolve without any actions")
    public void resolveSimple(String comment) {
        commentInput.fill(comment);
        completeInvestigationButton.click();
        successToast.getByText(investigationCompleted).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    @Step("Resolve with adding fraud")
    public void resolveAddFraud(String comment, String addedFraud) {
        commentInput.fill(comment);
        fraudListButton.click();
        fraudSelectItem.getByText(addedFraud).click();
        fraudSelectApplyButton.click();
        completeInvestigationButton.click();
        successToast.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    @Step("Resolve with adding fraud")
    public void resolveInvestigation() {
        completeInvestigationButton.click();
        successToast.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    public void addFraud(String... addedFraud) {
        Allure.step("add fraud on resolve screen");
        fraudListButton.click();
        for (String i : addedFraud) {
            fraudSelectItem.getByText(i).click();
        }
        fraudSelectApplyButton.click();
    }

    public void addRestriction(String... addedRestriction) {
        Allure.step("add fraud on resolve screen");
        restrictionListButton.click();
        for (String i : addedRestriction) {
            page.getByRole(AriaRole.OPTION).getByText(i).click();
        }
        applyButton.click();
    }

    public void resetFrauds() {
        Allure.step("click 'Reset changes' button");
        resetButton.click();
    }

    @Step("Resolve with adding a few frauds")
    public void resolveAddMultipleFraud(String comment, String... addedFraud) {
        commentInput.fill(comment);
        for (String i : addedFraud) {
            fraudListButton.click();
            fraudSelectItem.getByText(i).click();
            fraudSelectApplyButton.click();
        }

        completeInvestigationButton.click();
        successToast.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }


    public void checkFraudsList() {
        fraudListButton.click();
        FraudType[] fraudsTypes = FraudType.values();
        for (FraudType i : fraudsTypes) {
            String item = i.getDisplayName();
            assertTrue(fraudSelectItem.getByText(item).isVisible());
        }

    }


    public void checkRestrictionIsDisplayed(String restriction) {
        Allure.step("Check if the restriction " + restriction + " is displayed on resolve screen");
        clientRestrictionItem.getByText(restriction).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    @Step("Resolve test 250 symbols in comment")
    public void test250Symbols() {
        commentInput.fill(bigLorem);
        assertEquals(commentInput.inputValue(), smallLorem);
    }

    @Step("Resolve cleaning fraud list")
    public void resolveNoFrauds(String comment) {
        commentInput.fill(comment);
        if (cleanFraudListButton.isVisible()) {
            cleanFraudListButton.click();
        }
        completeInvestigationButton.click();
        successToast.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        String actual = successToast.textContent();
        assertEquals(investigationCompleted, actual);
    }

    @Step("Resolve cleaning fraud list")
    public void resolveClearFrauds(String comment) {

        commentInput.fill(comment);
        if (cleanFraudListButton.isVisible()) {
            cleanFraudListButton.click();
            page.locator(FRAUD_TYPE_POPUP_LOCATOR + "//button").getByText("Yes").click();

        }
        completeInvestigationButton.click();
        successToast.getByText(investigationCompleted).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        String actual = successToast.textContent();
        assertEquals(investigationCompleted, actual);
    }

    @Step("Resolve cleaning fraud list")
    public void resolveNoActionFrauds(String comment) {
        commentInput.fill(comment);
        completeInvestigationButton.click();
        successToast.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        String actual = successToast.textContent();
        assertEquals(investigationCompleted, actual);
    }

    public void fillCommentForm(String comment) {
        Allure.step("Fill comment form");
        commentInput.fill(comment);
        assertEquals(commentInput.inputValue(), comment);
    }

    public void submitCommentForm() {
        Allure.step("click on the add comment button");
        submitCommentButton.click();
        successToast.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        String actual = successToast.textContent();
        assertEquals("Comment added to Audit trail", actual);
    }

    @Step("Check withdrawal approval message in Kafka")
    public void checkKafkaRequestWithdrawal(String transactionID, String expectedStatus) throws InterruptedException,
            JsonProcessingException {
        KafkaHelper helper = new KafkaHelper();
        List<String> kafkaResponses = helper.consumeMessages("withdrawal.approvals", transactionID);
        for (String response : kafkaResponses) {
            System.out.println(response);
        }
        String kafkaResponse = kafkaResponses.getLast();
        ObjectMapper objectMapper = new ObjectMapper();
        WithdrawalApprovals apply = objectMapper.readValue(kafkaResponse, WithdrawalApprovals.class);
        assertTrue(apply.transferId.equals(String.valueOf(transactionID)));
        assertNotNull((apply.regulator));
        assertNotNull((apply.brand));
        assertNotNull((apply.timestamp));
        assertNotNull((apply.status));
        assertNotNull((apply.internalReason));
        assertEquals(expectedStatus, (apply.status));
    }

    public void openReportFraudForm() {
        isPageLoaded();
        reportFraudButton.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        reportFraudButton.click();
        reportForm.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        assertTrue(reportForm.isVisible());
    }

    @Step("Resolve with adding fraud")
    public void reportAddFraud(String comment, String addedFraud) {
        commentInput.fill(comment);
        fraudListButton.click();
        fraudSelectItem.getByText(addedFraud).click();
        fraudSelectApplyButton.click();
        submitFraudButton.click();
        successToast.getByText("Fraud reported. Good job!").waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        assertTrue(successToast.getByText("Fraud reported. Good job!").isVisible());
    }

    @Step("Resolve with adding fraud")
    public void checkPreviousConfirmedFraudDisplayed(String addedFraud) {
        String locator = "//div[@data-qa='selected_fraud_type_item']//*[text()='" + addedFraud + "']";
        page.waitForSelector(locator).waitForElementState(ElementState.VISIBLE);
    }

    public void checkFraudDisplayed(String... addedFraud) {
        Allure.step("check that fraud type displayed");
        for (String i : addedFraud) {
            page.locator(SELECTED_FRAUD_LOCATOR).getByText(i).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        }
    }

    public void checkFraudNotDisplayed(String... addedFraud) {
        Allure.step("check that fraud type displayed");
        for (String i : addedFraud) {
            page.locator(SELECTED_FRAUD_LOCATOR).getByText(i).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
        }
    }

    @Step("Get time label for the provided fraud")
    public String getFraudTimeByName(String fraudName) {
        return page.locator(String.format(FRAUD_TIME_BY_NAME_PATTERN, fraudName)).textContent();
    }

    @Step("Get time label for the provided fraud")
    public void resolveFillCommentary(String comment) {
        commentInput.fill(comment);
    }

    @Step("Click delete fraud and confirm the popup")
    public void deleteFraudByName(String fraudName) {
        page.locator(String.format(DELETE_FRAUD_BY_NAME_PATTERN, fraudName)).click();
        assertThat(fraudDeletionPopup).containsText("Are you sure that the client should not be identified with this fraud anymore?");
        confirmFraudDeletionButton.click();
        commentInput.fill(String.format("confirm %s", fraudName));
        submitFraudButton.click();
    }

    @Step("Click delete fraud ")
    public void clickDeleteRestrictionButtonByName(String restriction) {
        String locator = "//*[text()='" + restriction + "']" + RESTRICTION_LIST_LOCATOR_ANCESTOR + REMOVE_BUTTON_LOCATOR;
        page.locator(locator).click();
    }

    public void checkRestrictionDisplayed(String... addedFraud) {
        Allure.step("check that fraud type displayed");
        for (String i : addedFraud) {
            page.locator(RESTRICTION_LIST_LOCATOR).getByText(i).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        }
    }

    public void checkRestrictionNotDisplayed(String... addedFraud) {
        Allure.step("check that fraud type displayed");
        for (String i : addedFraud) {
            page.locator(RESTRICTION_LIST_LOCATOR).getByText(i).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
        }
    }

    public void confirmRestrictionDeletion(String buttonText) {
        Allure.step("Click button " + buttonText + " in popup");
        page.waitForSelector(RESTRICTION_DELETION_POPUP_LOCATOR).waitForElementState(ElementState.VISIBLE);
        page.locator(RESTRICTION_DELETION_POPUP_LOCATOR).getByText(buttonText).click();
    }


}
