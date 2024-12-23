package pageObjects.backofficePages;

import businessObjects.kafka.restrictionEvents.WithdrawalApprovals;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ResolvePage extends AbstractPage {
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
    private final Locator closeToastButtom;
    private final Locator cleanFraudListButton;
    private final Locator fraudListButton;
    private final Locator fraudSelectItem;
    private final Locator fraudSelectApplyButton;
    private final Locator clientRestrictionItem;

    public ResolvePage(Page page) {
        super(page);
        this.loaderAnimation = page.locator(".v-loader");
        this.loaderSpin = page.locator(".g-spin");
        this.resolveButton = page.locator(".g-button__text").getByText("Resolve");
        this.investigateButton = page.locator(".g-button__text").getByText("Investigate");
        this.completeInvestigationButton = page.locator(".g-button__text").getByText("Complete investigation");
        this.resolutionForm = page.locator("[data-qa='drawer_body']").getByText("Resolution");
        this.commentInput = page.locator(".v-investigation-tools-client-resolving-drawer__textarea-container textarea");
        this.withdrawalList = page.locator(".v-withdrawals-list");
        this.approveAllwithdrawalsButton = page.locator(".v-withdrawals-list__reject-resolve button").nth(0);
        this.rejectAllwithdrawalsButton = page.locator(".v-withdrawals-list__reject-resolve button").nth(1);
        this.approveFirstButton = page.locator(".v-withdrawals-list__reject-resolve button").nth(2);
        this.successToast = page.locator(".g-toast__title").getByText("Investigation completed");
        this.closeToastButtom = page.locator(".g-button.g-toast__btn-close");
        this.cleanFraudListButton = page.locator("button[data-qa='fraud_type_selector_clear_button']");
        this.fraudListButton = page.locator("button[data-qa='fraud_type_select_anchor_button']");
        this.fraudSelectItem = page.locator("[data-qa='fraud_type_select_item']");
        this.fraudSelectApplyButton = page.locator("[data-qa='fraud_type_select_apply_button']");
        this.clientRestrictionItem = page.locator(".v-client-restrictions-list-item__item-body");
    }

    String bigLorem = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc facilisis, metus eu mattis suscipit, est felis venenatis nunc, eu rhoncus sapien tortor sed turpis. Integer vitae leo pharetra, pellentesque nisi quis, pharetra arcu. Curabitur nec arcu ac.";
    String smallLorem = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc facilisis, metus eu mattis suscipit, est felis venenatis nunc, eu rhoncus sapien tortor sed turpis. Integer vitae leo pharetra, pellentesque nisi quis, pharetra arcu. Curabitur nec arcu ac";

    @Step("Check if the page loaded")
    public void isPageLoaded() {
        int n = 0;
        page.waitForTimeout(2000);
        while ((loaderAnimation.isVisible() || loaderSpin.isVisible()) && n < 8) {
            page.waitForTimeout(2000);
            n += 1;
        }
    }

    @Step("Open resolve form in suspicious client")
    public void openResolveSuspicious() {
        isPageLoaded();
        if (resolveButton.isVisible()) {
            resolveButton.click();
        } else {
            investigateButton.click();
            resolveButton.click();
        }
        isPageLoaded();

    }

    @Step("Resolve and approve all withdrawals")
    public void resolveWithdrawalsAllApprove() {
//        page.waitForSelector(resolutionForm.toString());
        assertTrue(resolutionForm.isVisible());
        approveAllwithdrawalsButton.click();
        commentInput.fill("autotest to withdrawals");
        completeInvestigationButton.click();
        successToast.isVisible();
    }

    @Step("Resolve and reject all withdrawals")
    public void resolveWithdrawalsAllReject() {
        assertTrue(resolutionForm.isVisible());
        rejectAllwithdrawalsButton.click();
        commentInput.fill("autotest to withdrawals");
        completeInvestigationButton.click();
        successToast.isVisible();
    }

    @Step("Resolve and approve one withdrawal")
    public void resolveWithdrawalsApproveFirst() {
        assertTrue(resolutionForm.isVisible());
        rejectAllwithdrawalsButton.click();
        approveFirstButton.click();
        commentInput.fill("autotest to withdrawals");
        completeInvestigationButton.click();
        successToast.isVisible();
    }

    @Step("Resolve without any actions")
    public void resolveSimple(String comment) {
        commentInput.fill(comment);
        completeInvestigationButton.click();
        successToast.isVisible();
    }

    @Step("Resolve with adding fraud")
    public void resolveAddFraud(String comment, String addedFraud) {
        commentInput.fill(comment);
        fraudListButton.click();
        fraudSelectItem.getByText(addedFraud).click();
        fraudSelectApplyButton.click();
        completeInvestigationButton.click();
        successToast.isVisible();
    }


    public void checkFraudsList() {
        fraudListButton.click();
        List<String> frauds = Arrays.asList("CPA", "Hedging", "Market manipulation", "Pricing errors", "Gap trading", "Swap arbitrage", "RAF abuse", "Rebate churning", "Loss voucher abuse", "NBP abuse", "TLS abuse", "Potential abuse");
        for (String item : frauds) {
            assertTrue(fraudSelectItem.getByText(item).isVisible());
        }

    }


    public void checkRestrictionIsDisplayed(String restriction) {
        Allure.step("Check if the restriction " + restriction + " is displayed on resolve screen");
        assertTrue(clientRestrictionItem.getByText(restriction).isVisible());
    }

    @Step("Resolve test 250 symbols in comment")
    public void test250Symbols() {
        commentInput.fill(bigLorem);
        assertEquals(commentInput.inputValue(), smallLorem);
    }

    @Step("Resolve cleaning fraud list")
    public void resolveNoFrauds(String comment) {
        commentInput.fill(comment);
        cleanFraudListButton.click();
        completeInvestigationButton.click();
        successToast.isVisible();
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
        assertEquals(expectedStatus, (apply.status));
    }

}
