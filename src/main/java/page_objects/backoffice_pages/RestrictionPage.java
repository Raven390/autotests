package page_objects.backoffice_pages;

import business_objects.api.mitigation_service.PostRestrictionRequestBody;
import business_objects.db.audit_service_db.Event;
import business_objects.db.mitigation_service_db.ClientsRestriction;
import business_objects.kafka.restriction_events.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import helpers.data.enums.Restriction;
import helpers.database.DbName;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;

import static business_objects.api.mitigation_service.MitigationServiceRequest.postRestriction;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static com.microsoft.playwright.options.WaitForSelectorState.HIDDEN;
import static com.microsoft.playwright.options.WaitForSelectorState.VISIBLE;
import static helpers.database.DbHelper.deleteEntryFromDb;
import static helpers.database.DbHelper.getObjectsFromDB;
import static org.junit.jupiter.api.Assertions.*;
import static utils.ConfigFactory.BASE_URL_E2E;
import static utils.Constants.*;

public class RestrictionPage extends AbstractPage {

    private final Locator restrictionTab;
    private final Locator accountSwitch;
    private final Locator transferSwitch;
    private final Locator depositsSwitch;
    private final Locator withdrawalsSwitch;
    private final Locator loginSwitch;
    private final Locator manualSwitch;
    private final Locator creditAndBonusSwitch;
    private final Locator closeSwitch;
    private final Locator offQuotesSwitch;
    private final Locator abBookSwitch;
    private final Locator header1;
    private final Locator header2;
    private final Locator dialog;
    private final Locator reasonInput;
    private final Locator restrictionSetCancel;
    private final Locator restrictionSetSet;
    private final Locator setToast;
    private final Locator restrictionCancelCancel;
    private final Locator restrictionCancelSet;
    private final Locator restrictionCancelSetTrade;
    private final Locator cancelToast;
    private final Locator checkedAccount;
    private final Locator checkedTransfer;
    private final Locator checkedDeposits;
    private final Locator checkedWithdrawals;
    private final Locator checkedLogin;
    private final Locator checkedManual;
    private final Locator checkedCreditAndBonus;
    private final Locator loaderAnimation;
    private final Locator loaderSpin;
    private final Locator selectAllAccCheckbox;
    private final Locator checkedCloseOnlyMode;
    private final Locator checkedOffQuotesMode;
    private final Locator checkedAbBook;
    private final Locator withdrawalList;
    private final Locator approveAllwithdrawalsButton;
    private final Locator rejectAllwithdrawalsButton;
    private final Locator approveFirstButton;
    private final Locator accountLabel;
    private final Locator activitySection;
    private final Locator tooltip;

    private static final String RESTRICTION_ITEM_BY_NAME_PATTERN = "//div[contains(@class,'v-restrictions-tab-item__name') and text()='%s']";

    public RestrictionPage(Page page) {
        super(page);
        this.loaderAnimation = page.locator(".v-loader");
        this.loaderSpin = page.locator(".g-spin").first();
        this.restrictionTab = page.locator("[role=\"tab\"][title=\"Restrictions\"]");
        this.accountSwitch = page.locator(".v-restrictions-tab-item__name").getByText(Restriction.ACCOUNT_CREATION_REVIEW.getName());
        this.transferSwitch = page.locator(".v-restrictions-tab-item__name").getByText(Restriction.INTERNAL_TRANSFER.getName());
        this.depositsSwitch = page.locator(".v-restrictions-tab-item__name").getByText(Restriction.DEPOSITS.getName());
        this.withdrawalsSwitch = page.locator(".v-restrictions-tab-item__name").getByText(Restriction.WITHDRAWALS.getName());
        this.loginSwitch = page.locator(".v-restrictions-tab-item__name").getByText(Restriction.LOGIN_CRM.getName());
        this.manualSwitch = page.locator(".v-restrictions-tab-item__name").getByText(Restriction.MANUAL_WITHDRAWAL_REVIEW.getName());
        this.creditAndBonusSwitch = page.locator(".v-restrictions-tab-item__name").getByText(Restriction.CREDIT_AND_BONUS.getName());
        this.closeSwitch = page.locator(".v-restrictions-tab-item__name").getByText(Restriction.CLOSE_ONLY_MODE.getName());
        this.offQuotesSwitch = page.locator(".v-restrictions-tab-item__name").getByText("Off quotes");
        this.abBookSwitch = page.locator(".v-restrictions-tab-item__name").getByText("B-Book -> A-Book");
        this.header1 = page.locator(".g-text_variant_header-1");
        this.header2 = page.locator(".g-text_variant_header-2");
        this.dialog = page.locator(".v-restriction-tab-general-modal");
        this.reasonInput = page.locator("textarea[placeholder=\"Reason (discovered fraud type, etc.)\"]");
        this.restrictionSetCancel = page.locator("v-common-modal__buttons").getByText("Cancel");
        this.restrictionSetSet = page.locator(".v-common-modal__buttons").getByText("Set");
        this.selectAllAccCheckbox = page.locator(".v-checkbox-list-with-select-all__select-all");
        this.setToast = page.locator(".g-toast__title").getByText("Restriction was set");
        this.restrictionCancelCancel = page.locator("v-common-modal__buttons").getByText("Cancel");
        this.restrictionCancelSet = page.locator(".v-common-modal__buttons").getByText("Remove");
        this.restrictionCancelSetTrade = page.locator(".v-common-modal__buttons").getByText("Apply changes");
        this.cancelToast = page.locator(".g-toast__title").getByText("Restriction was removed");
        this.checkedAccount = page.locator(".v-restrictions-tab-item_checked .v-restrictions-tab-item__header").getByText(Restriction.ACCOUNT_CREATION_REVIEW.getName());
        this.checkedTransfer = page.locator(".v-restrictions-tab-item_checked .v-restrictions-tab-item__header").getByText(Restriction.INTERNAL_TRANSFER.getName());
        this.checkedDeposits = page.locator(".v-restrictions-tab-item_checked .v-restrictions-tab-item__header").getByText(Restriction.DEPOSITS.getName());
        this.checkedWithdrawals = page.locator(".v-restrictions-tab-item_checked .v-restrictions-tab-item__header").getByText(Restriction.WITHDRAWALS.getName());
        this.checkedLogin = page.locator(".v-restrictions-tab-item_checked .v-restrictions-tab-item__header").getByText(Restriction.LOGIN_CRM.getName());
        this.checkedManual = page.locator(".v-restrictions-tab-item_checked .v-restrictions-tab-item__header").getByText(Restriction.MANUAL_WITHDRAWAL_REVIEW.getName());
        this.checkedCreditAndBonus = page.locator(".v-restrictions-tab-item_checked .v-restrictions-tab-item__header").getByText(Restriction.CREDIT_AND_BONUS.getName());
        this.checkedCloseOnlyMode = page.locator(".v-restrictions-tab-item_checked .v-restrictions-tab-item__header").getByText(Restriction.CLOSE_ONLY_MODE.getName());
        this.checkedOffQuotesMode = page.locator(".v-restrictions-tab-item_checked .v-restrictions-tab-item__header").getByText("Off quotes");
        this.checkedAbBook = page.locator(".v-restrictions-tab-item_checked .v-restrictions-tab-item__header").getByText("B-Book -> A-Book");
        this.withdrawalList = page.locator(".v-withdrawals-list");
        this.approveAllwithdrawalsButton = page.locator(".v-withdrawals-list__reject-resolve button").nth(0);
        this.rejectAllwithdrawalsButton = page.locator(".v-withdrawals-list__reject-resolve button").nth(1);
        this.approveFirstButton = page.locator(".v-withdrawals-list__reject-resolve button").nth(2);
        this.accountLabel = page.locator(".v-restriction-tab-trading-modal__labels");
        this.activitySection = page.locator(".v-restriction-tab-trading-modal__activity");
        this.tooltip = page.locator(".g-tooltip__content");
    }

    @Step("Open users restriction tab")
    public void navigate(String ucid) {
        page.navigate(String.format("%sinvestigation/%s", BASE_URL_E2E, ucid));
        isPageLoaded();
        restrictionTab.click();
        isPageLoaded();
    }


    public void openRestrictionsTab() {
        Allure.step("Open restrictions tab bi click tab button in ui");
        restrictionTab.click();
        waitForPageToLoad();
    }


    public void checkUI() {
        Allure.step("Check that restriction tab rendered properly");
        accountSwitch.isVisible();
        transferSwitch.isVisible();
        depositsSwitch.isVisible();
        withdrawalsSwitch.isVisible();
        loginSwitch.isVisible();
        manualSwitch.isVisible();
        closeSwitch.isVisible();
        offQuotesSwitch.isVisible();
        abBookSwitch.isVisible();
        header1.getByText("General").isVisible();
        header1.getByText("Trading").isVisible();
        header2.getByText("Restrictions on CRM and client’s operations").isVisible();
        header2.getByText("Restrictions on selected client’s trading accounts").isVisible();
    }


    public void clickAccountSwitch() {
        Allure.step("Set account restriction");
        accountSwitch.click();
    }


    public void clickTransferSwitch() {
        Allure.step("Set transfer restriction");
        transferSwitch.click();
    }


    public void clickDepositsSwitch() {
        Allure.step("Set deposit restriction");
        depositsSwitch.click();
    }


    public void clickWithdrawalsSwitch() {
        Allure.step("Set Withdrawals restriction");
        withdrawalsSwitch.click();
    }


    public void clickLoginSwitch() {
        Allure.step("Set login restriction");
        loginSwitch.click();
    }


    public void clickManualWithdrawalSwitch() {
        Allure.step("Set 'Manual Withdrawal' restriction");
        manualSwitch.click();
    }

    public void clickCreditAndBonusSwitch() {
        Allure.step("Set Credit and Bonus restriction");
        creditAndBonusSwitch.click();
    }

    public void clickCloseOnlyModeSwitch() {
        Allure.step("Set Close only mode restriction");
        closeSwitch.click();
    }

    public void clickOffQuotesModeSwitch() {
        Allure.step("Set OffQuotes restriction");
        offQuotesSwitch.click();
    }

    public void clickAbBookSwitch() {
        Allure.step("Set AB Book restriction");
        abBookSwitch.click();
    }


    public void checkThatAccountIsChecked() {
        Allure.step("Check that account restriction tumbler is checked");
        checkedAccount.isVisible();
    }

    public void checkThatAOffQuotesIsChecked() {
        Allure.step("Check that OffQuotes tumbler is checked");
        checkedOffQuotesMode.isVisible();
    }

    public void checkThatAbBookIsChecked() {
        Allure.step("Check that AbBook tumbler is checked");
        checkedAbBook.isVisible();
    }

    public void checkThatTransferIsChecked() {
        Allure.step("Check that Transfer restriction tumbler is checked");
        checkedTransfer.isVisible();
    }

    public void checkThatDepositsIsChecked() {
        Allure.step("Check that Deposits restriction tumbler is checked");
        checkedDeposits.isVisible();
    }

    public void checkThatWithdrawalsIsChecked() {
        Allure.step("Check that Withdrawals restriction tumbler is checked");
        checkedWithdrawals.isVisible();
    }

    public void checkThatLoginIsChecked() {
        Allure.step("Check that Login restriction tumbler is checked");
        checkedLogin.isVisible();
    }

    public void checkThatCloseOnlyIsChecked() {
        Allure.step("Check that ManualWithdrawal restriction tumbler is checked");
        checkedCloseOnlyMode.isVisible();
    }

    @Step("Check that ManualWithdrawal restriction tumbler is checked")
    public void checkThatManualWithdrawalIsChecked() {
        checkedManual.isVisible();
    }

    @Step("Check that Credit and Bonus restriction tumbler is checked")
    public void checkThatCreditAndBonusIsChecked() {
        checkedCreditAndBonus.isVisible();
    }

    @Step("Click checked account tumbler")
    public void clickCheckedAccount() {
        checkedAccount.click();
    }

    @Step("Click checked transfer tumbler")
    public void clickCheckedTransfer() {
        checkedTransfer.click();
    }

    @Step("Click checked Deposits tumbler")
    public void clickCheckedDeposits() {
        checkedDeposits.click();
    }

    @Step("Click checked Withdrawals tumbler")
    public void clickCheckedWithdrawals() {
        checkedWithdrawals.click();
    }

    @Step("Click checked Login CRM tumbler")
    public void clickCheckedLogin() {
        checkedLogin.click();
    }

    @Step("Click checked Manual Withdrawal Review tumbler")
    public void clickCheckedManual() {
        checkedManual.click();
    }

    @Step("Click checked Credit and Bonus tumbler")
    public void clickCheckedCreditAndBonus() {
        checkedCreditAndBonus.click();
    }

    @Step("Click checked Close Only Mode tumbler")
    public void clickCheckedCloseOnly() {
        checkedCloseOnlyMode.click();
    }

    @Step("Check if restriction with name {restrictionName} is visible")
    public boolean isRestrictionPresent(String restrictionName) {
        return page.locator(String.format(RESTRICTION_ITEM_BY_NAME_PATTERN, restrictionName)).isVisible();
    }

    @Step("Click checked OffQuotes tumbler")
    public void clickCheckedOffQuotes() {
        checkedOffQuotesMode.click();
    }

    @Step("Click checked AbBook tumbler")
    public void clickCheckedAbBook() {
        checkedAbBook.click();
    }


    public void fillApplyReason(String reason) {
        Allure.step("Fill apply reason");
        dialog.isVisible();
        reasonInput.fill(reason);
        restrictionSetSet.click();
        setToast.isVisible();
        page.waitForTimeout(1000);
    }

    @Step("Fill apply reason trading Green all accounts")
    public void fillApplyReasonTradingAllAccs(String reason) {
        dialog.isVisible();
        selectAllAccCheckbox.click();
        reasonInput.fill(reason);
        restrictionSetSet.click();
        setToast.isVisible();
        page.waitForTimeout(1000);
    }

    @Step("Fill cancel reason")
    public void fillCancelReason(String reason) {
        dialog.isVisible();
        reasonInput.fill(reason);
        restrictionCancelSet.click();
        cancelToast.isVisible();
        page.waitForTimeout(1000);
    }

    @Step("Fill cancel reason Manual withdrawal with withdrawals all green")
    public void fillCancelReasonManualWithdrawalAllGreen(String reason) {
        page.waitForTimeout(1000);
        isPageLoaded();
        assertTrue(dialog.isVisible());
        assertTrue(withdrawalList.isVisible());
        approveAllwithdrawalsButton.click();
        reasonInput.fill(reason);
        restrictionCancelSet.click();
        cancelToast.isVisible();
        page.waitForTimeout(1000);
    }

    @Step("Fill cancel reason Manual withdrawal with withdrawals all refuse")
    public void fillCancelReasonManualWithdrawalAllrefuse(String reason) {
        page.waitForTimeout(1000);
        isPageLoaded();
        assertTrue(dialog.isVisible());
        assertTrue(withdrawalList.isVisible());
        rejectAllwithdrawalsButton.click();
        reasonInput.fill(reason);
        restrictionCancelSet.click();
        cancelToast.isVisible();
        page.waitForTimeout(1000);
    }

    @Step("Fill cancel reason Manual withdrawal with withdrawals approve one")
    public void fillCancelReasonManualWithdrawalApproveOne(String reason) {
        page.waitForTimeout(1000);
        isPageLoaded();
        assertTrue(dialog.isVisible());
        assertTrue(withdrawalList.isVisible());
        rejectAllwithdrawalsButton.click();
        approveFirstButton.click();
        reasonInput.fill(reason);
        restrictionCancelSet.click();
        cancelToast.isVisible();
        page.waitForTimeout(1000);
    }


    public void fillCancelReasonManualWithdrawalApproveOneByPaymentType(String reason, String paymentType) {
        Allure.step("Approve one withdrawal while reject all others");
        page.waitForTimeout(1000);
        isPageLoaded();
        assertTrue(dialog.isVisible());
        assertTrue(withdrawalList.isVisible());
        rejectAllwithdrawalsButton.click();
        clickApproveWithdrawalByPaymentType(paymentType);
        reasonInput.fill(reason);
        restrictionCancelSet.click();
        cancelToast.isVisible();
        page.waitForTimeout(1000);
    }

    public void fillCancelReasonTrade(String reason) {
        Allure.step("Fill cancel reason");
        page.waitForTimeout(1000);
        isPageLoaded();
        dialog.isVisible();
        selectAllAccCheckbox.click();
        reasonInput.fill(reason);
        restrictionCancelSetTrade.click();
        cancelToast.isVisible();
        page.waitForTimeout(1000);
    }

    public void clickApproveWithdrawalByPaymentType(String paymentType) {
        Allure.step("click approve withdrawal on selected transaction");
        String locator = "//div[text() = '" + paymentType + "']/ancestor::div[@class = 'v-withdrawals-list__list-item']//button[1]";
        page.waitForSelector(locator);
        page.locator(locator).click();
    }

    public void clickRefuseWithdrawalByPaymentType(String paymentType) {
        Allure.step("click refuse withdrawal on selected transaction");
        page.waitForSelector("//div[text() = '" + paymentType + "']/ancestor::div[@class = 'v-withdrawals-list__list-item']//button[2]");
        page.locator("//div[text() = '" + paymentType + "']/ancestor::div[@class = 'v-withdrawals-list__list-item']//button[2]").click();
    }

    public static void checkKafkaRequestApplyUserId(int userIdInt) throws JsonProcessingException,
            InterruptedException {
        Allure.step("Check request message for apply cancellation for client in kafka");
        String userId = String.valueOf(userIdInt);
        Thread.sleep(7000);
        System.out.println("we search user " + userId);
        KafkaHelper helper = new KafkaHelper();
        List<String> kafkaResponses = helper.consumeMessages("client.restrictions.apply", userId);
        for (String response : kafkaResponses) {
            System.out.println(response);
        }
        String kafkaResponse = kafkaResponses.getLast();
        System.out.println("tested message is " + kafkaResponse);
        ObjectMapper objectMapper = new ObjectMapper();
        ClientRestrictionApply apply = objectMapper.readValue(kafkaResponse, ClientRestrictionApply.class);
        apply.clientId.toString().equals(userId);
        assertNotNull((apply.clientId));
        assertNotNull((apply.timestamp));
        assertNotNull((apply.messageId));
        assertNotNull((apply.regulator));
        assertNotNull((apply.restrictions));
    }

    public void checkKafkaRequestCancelUcid(int userIdInt) throws JsonProcessingException, InterruptedException {
        Allure.step("Check request message for restriction cancellation for client in kafka");
        String userId = String.valueOf(userIdInt);
        Thread.sleep(7000);
        KafkaHelper helper = new KafkaHelper();
        List<String> kafkaResponses = helper.consumeMessages(KAFKA_TOPIC_CLIENT_RESTRICTIONS_CANCEL, userId);
        for (String response : kafkaResponses) {
            System.out.println(response);
        }
        String kafkaResponse = kafkaResponses.getLast();
        ObjectMapper objectMapper = new ObjectMapper();
        ClientRestrictionCancel cancel = objectMapper.readValue(kafkaResponse, ClientRestrictionCancel.class);
        assertNotNull((cancel.clientId));
        assertNotNull((cancel.timestamp));
        assertNotNull((cancel.messageId));
        assertNotNull((cancel.brand));
        assertNotNull((cancel.regulator));
        assertNotNull((cancel.restrictions));
    }

    public void checkKafkaRequestApplyAccount(int accountIdInt) throws JsonProcessingException, InterruptedException {
        checkKafkaRequestApplyAccount(accountIdInt, 525_600);
    }

    public void checkKafkaRequestApplyAccount(int accountIdInt, int banDurationMin) throws JsonProcessingException,
            InterruptedException {
        Allure.step("Check request message for restriction apply for account in kafka");
        String accoundId = String.valueOf(accountIdInt);
        Thread.sleep(7000);
        KafkaHelper helper = new KafkaHelper();
        List<String> kafkaResponses = helper.consumeMessages(KAFKA_TOPIC_ACCOUNT_RESTRICTIONS_APPLY, accoundId);
        for (String response : kafkaResponses) {
            System.out.println(response);
        }
        String kafkaResponse = kafkaResponses.getLast();
        System.out.println("tested message is " + kafkaResponse);
        ObjectMapper objectMapper = new ObjectMapper();
        AccountRestrictionApply apply = objectMapper.readValue(kafkaResponse, AccountRestrictionApply.class);
        assertEquals(accountIdInt, apply.accountId);
        assertNotNull((apply.timestamp));
        assertNotNull((apply.messageId));
        assertNotNull((apply.serverId));
        assertNotNull((apply.brand));
        assertEquals(banDurationMin, (apply.initialBanDurationInMinutes));
        assertNotNull((apply.modifier));
        assertNotNull((apply.restrictions));
    }

    public static void checkKafkaRequestApplyAccount(int accountIdInt, int serverId, int banDurationMin,
            int restrictionId, String reason, String restrictionCode) throws JsonProcessingException,
            InterruptedException {
        Allure.step("Check request message for restriction apply for account in kafka");
        String accountId = String.valueOf(accountIdInt);
        Thread.sleep(7000);
        KafkaHelper helper = new KafkaHelper();
        List<String> kafkaResponses = helper.consumeMessages(KAFKA_TOPIC_ACCOUNT_RESTRICTIONS_APPLY, accountId);
        System.out.println("first message is " + kafkaResponses.getFirst());
        System.out.println("last message is " + kafkaResponses.getLast());
        for (String response : kafkaResponses) {
            System.out.println(response);
        }
        String kafkaResponse = kafkaResponses.getLast();
        System.out.println("tested message is " + kafkaResponse);
        ObjectMapper objectMapper = new ObjectMapper();
        AccountRestrictionApply apply = objectMapper.readValue(kafkaResponse, AccountRestrictionApply.class);
        assertEquals(accountIdInt, apply.accountId);
        assertNotNull((apply.timestamp));
        assertNotNull((apply.messageId));
        assertEquals(serverId, (apply.serverId));
        assertEquals(banDurationMin, (apply.initialBanDurationInMinutes));
        assertNotNull((apply.modifier));
        AccountRestrictionApply.Restriction[] restriction = apply.restrictions;
        AccountRestrictionApply.Restriction testRestriction = restriction[0];
        assertEquals(restrictionId, testRestriction.restrictionId);
//        assertEquals(reason, testRestriction.internalReason);
        assertEquals(restrictionCode, testRestriction.restrictionCode);
    }


    public void checkKafkaRequestCancelAccount(int accoundIdInt) throws JsonProcessingException, InterruptedException {
        Allure.step("Check request message for restriction cancellation for account in kafka");
        String accoundId = String.valueOf(accoundIdInt);
        Thread.sleep(7000);
        KafkaHelper helper = new KafkaHelper();
        List<String> kafkaResponses = helper.consumeMessages(KAFKA_TOPIC_ACCOUNT_RESTRICTIONS_CANCEL, accoundId);
        for (String response : kafkaResponses) {
            System.out.println(response);
        }
        String kafkaResponse = kafkaResponses.getLast();
        System.out.println("tested message is " + kafkaResponse);
        ObjectMapper objectMapper = new ObjectMapper();
        AccountRestrictionCancel cancel = objectMapper.readValue(kafkaResponse, AccountRestrictionCancel.class);
        assertNotNull((cancel.accountId));
        assertNotNull((cancel.timestamp));
        assertNotNull((cancel.messageId));
        assertNotNull((cancel.serverId));
        assertNotNull((cancel.modifier));
        assertNotNull((cancel.restrictions));
    }


    public void checkKafkaRequestWithdrawal(String transactionID, String expectedStatus) throws InterruptedException,
            JsonProcessingException {
        Allure.step("Check withdrawal approval message");
        Thread.sleep(7000);
        System.out.println("we search transaction " + transactionID);
        KafkaHelper helper = new KafkaHelper();
        List<String> kafkaResponses = helper.consumeMessages("withdrawal.approvals", transactionID);
        for (String response : kafkaResponses) {
            System.out.println(response);
        }
        String kafkaResponse = kafkaResponses.getLast();
        System.out.println("tested message is " + kafkaResponse);
        ObjectMapper objectMapper = new ObjectMapper();
        WithdrawalApprovals apply = objectMapper.readValue(kafkaResponse, WithdrawalApprovals.class);
        assertEquals(apply.transferId.toString(), (transactionID));
        assertNotNull((apply.regulator));
        assertNotNull((apply.brand));
        assertNotNull((apply.timestamp));
        assertNotNull((apply.status));
        assertEquals(expectedStatus, (apply.status));
    }

    @Step
    public void readMessagesFromClientApply(String userId) throws InterruptedException {
        Thread.sleep(4000);
        KafkaHelper helper = new KafkaHelper();
        helper.consumeMessage("client.restrictions.apply", userId);
    }

    @Step
    public void readMessagesFromWithdrawalApprovals(String userId) throws InterruptedException,
            JsonProcessingException {
        Thread.sleep(4000);
        KafkaHelper helper = new KafkaHelper();
        helper.consumeMessage("withdrawal.approvals", userId);
    }

    @Step("Clean users restriction history")
    public static void cleanUserRestriction(String ucid) throws Exception {
        Allure.step("Clean user restriction history of client " + ucid);
        List<ClientsRestriction> restrictionList = getObjectsFromDB(DbName.MITIGATION_POSTGRES, "clients_restriction", "ucid = '" + ucid + "'", ClientsRestriction.class);
        for (ClientsRestriction i : restrictionList) {
            String Id = i.id.toString();
            deleteEntryFromDb(DbName.MITIGATION_POSTGRES, "action", "clients_restriction_id = " + Id);
            Thread.sleep(200);
            deleteEntryFromDb(DbName.MITIGATION_POSTGRES, "kafka_request", "clients_restriction_id = " + Id);
            Thread.sleep(200);
            deleteEntryFromDb(DbName.MITIGATION_POSTGRES, "kafka_response", "clients_restriction_id = " + Id);
            Thread.sleep(200);
            deleteEntryFromDb(DbName.MITIGATION_POSTGRES, "clients_restriction", "id = " + Id);
            Thread.sleep(200);
        }
    }


    public static void checkUserHaveRestriction(String ucid, int restrictionId, String applicationReason,
            String expectedStatus) throws Exception {
        Thread.sleep(7000);
        Allure.step("check user have restriction in Mitigation DataBase");
        List<ClientsRestriction> restrictionList = getObjectsFromDB(DbName.MITIGATION_POSTGRES, "clients_restriction", "ucid = '" + ucid + "' and id = " + restrictionId, ClientsRestriction.class);
        ClientsRestriction restriction = restrictionList.getLast();
        assertEquals(ucid, restriction.ucid);
        assertEquals(expectedStatus, restriction.status);
    }

    @Step("Clean users audit history")
    public void cleanUserAudit(String ucid) throws Exception {
        deleteEntryFromDb(DbName.AUDIT, "event", "ucid = '" + ucid + "'");
        Thread.sleep(200);
    }

    public static void setRestrictionAPIGeneral(String ucid, String code, String applyReason, String updatedBySystem,
            String updatedByUser) throws IOException {
        Allure.step("Set restriction though API GENERAL");
        PostRestrictionRequestBody postRestrictionRequestBody = new PostRestrictionRequestBody(
                ucid, code, "GENERAL", null, null, applyReason, new PostRestrictionRequestBody.UpdatedBy(updatedBySystem, updatedByUser)
        );
        Response response = postRestriction(postRestrictionRequestBody);
        assertNotNull(response);
        assertEquals(response.code(), 200);
    }

    public static String setRestrictionAPIGeneralResponse(String ucid, String code, String applyReason,
            String updatedBySystem,
            String updatedByUser) throws IOException {
        Allure.step("Set restriction though API GENERAL");
        PostRestrictionRequestBody postRestrictionRequestBody = new PostRestrictionRequestBody(
                ucid, code, "GENERAL", null, null, applyReason, new PostRestrictionRequestBody.UpdatedBy(updatedBySystem, updatedByUser)
        );
        Response response = postRestriction(postRestrictionRequestBody);
        assertNotNull(response);
        assertEquals(response.code(), 200);
        assert response.body() != null;
        String responseVal = response.body().string();
        return responseVal;
    }

    @Step("Set restriction though API")
    public static void setRestrictionAPIGeneral(String ucid, String code) throws IOException {
        Allure.step("Set restriction though API GENERAL");
        PostRestrictionRequestBody postRestrictionRequestBody = new PostRestrictionRequestBody(
                ucid, code, "GENERAL", null, null, "Integration test", new PostRestrictionRequestBody.UpdatedBy("test", "automation")
        );
        Response response = postRestriction(postRestrictionRequestBody);
        assertNotNull(response);
    }

    @Step
    public void setRestrictionAPITrade(String ucid, int accId, int serverId, String code) throws IOException {
        Allure.step("Set restriction though API TRADE");
        PostRestrictionRequestBody postRestrictionRequestBody = new PostRestrictionRequestBody(
                ucid, code, "TRADING", accId, serverId, "Integration test", new PostRestrictionRequestBody.UpdatedBy("string", "string")
        );
        Response response = postRestriction(postRestrictionRequestBody);
        assertNotNull(response);
    }

    @Step
    public static String setRestrictionAPITradeResponse(String ucid, String code, int accId, int serverId,
            String applyReason,
            String updatedBySystem, String updatedByUser) throws IOException {
        Allure.step("Set restriction though API TRADE");
        PostRestrictionRequestBody postRestrictionRequestBody = new PostRestrictionRequestBody(
                ucid, code, "TRADING", accId, serverId, applyReason, new PostRestrictionRequestBody.UpdatedBy(updatedBySystem, updatedByUser)
        );
        Response response = postRestriction(postRestrictionRequestBody);
        assertNotNull(response);
        assert response.body() != null;
        String responseVal = response.body().string();
        return responseVal;
    }

    public void checkRestrictionCancellationAuditBO(String ucid, String detail) throws Exception {
        Thread.sleep(7000);
        List<Event> event = getObjectsFromDB(DbName.AUDIT, "event", "ucid = '" + ucid + "'", Event.class);
        String type1 = event.get(2).getType();
        assertEquals("CANCELLATION_REQUESTED", type1);
        String details = event.get(2).getDetails();
        assertEquals(details, detail);
        String type2 = event.get(3).getType();
        assertEquals("RESTRICTION_CANCELLED", type2);
        String system = event.get(2).getInitiatedBySystem();
        assertEquals("Vindex BO", system);
    }

    public void checkRestrictionCancellationAuditBO(String ucid, String type, String expectedDetails) throws Exception {
        Thread.sleep(7000);
        List<Event> event = getObjectsFromDB(DbName.AUDIT, " event", "ucid = '" + ucid + "' and type = '" + type + "' AND details = '" + expectedDetails + "'", Event.class);
        assertNotNull(event);
        assertNotNull(event.getLast().getKafkaMessageId());
        assertNotNull(event.getLast().getId());
        assertNotNull(event.getLast().getUcid());
        assertNotNull(event.getLast().getType());
        assertNotNull(event.getLast().getCreatedAt());
        assertNotNull(event.getLast().getInitiatedBySystem());
        assertNotNull(event.getLast().getInitiatedByUser());
        assertNotNull(event.getLast().getComment());
    }

    public static void checkRestrictionApplymentAuditGeneral(String ucid, String detail) throws Exception {
        Allure.step("check that record about restriction apply appeared in the audit trail");
        Thread.sleep(7000);
        List<Event> event = getObjectsFromDB(DbName.AUDIT, "event", "ucid = '" + ucid + "'", Event.class);
        String type1 = event.get(event.size() - 2).getType();
        assertEquals("RESTRICTION_REQUESTED", type1);
        String details = event.get(event.size() - 2).getDetails();
        assertEquals(details, detail);
        String type2 = event.getLast().getType();
        assertEquals("RESTRICTION_APPLIED", type2);
        String system = event.getLast().getInitiatedBySystem();
        assertEquals("Vindex BO", system);
    }

    public static void checkRestrictionApplymentAuditGeneral(String ucid, String expectedSystem, String expectedUser,
            String expectedComment, String detail) throws Exception {
        Allure.step("check that record about restriction apply appeared in the audit trail");
        Thread.sleep(7000);
        List<Event> events = getObjectsFromDB(DbName.AUDIT, "event", "ucid = '" + ucid + "'", Event.class);
        Event event1 = events.get(events.size() - 2);
        System.out.println("event1 = " + event1);
        Event event2 = events.getLast();
        System.out.println("event2 = " + event2);
        assertEquals("RESTRICTION_REQUESTED", event1.getType());
        assertEquals(expectedSystem, event1.getInitiatedBySystem());
        assertEquals(expectedUser, event1.getInitiatedByUser());
        assertEquals(expectedComment, event1.getComment());
        assertEquals(detail, event1.getDetails());

        assertEquals("RESTRICTION_APPLIED", event2.getType());
        assertEquals(expectedSystem, event2.getInitiatedBySystem());
        assertEquals(expectedUser, event2.getInitiatedByUser());
        assertNull(event2.getComment());
        assertEquals(detail, event2.getDetails());
    }

    public static void checkRestrictionApplymentAuditTrading(String ucid, String expectedSystem, String expectedUser,
            String expectedComment, String detail, int accountId) throws Exception {
        Allure.step("check that record about restriction apply appeared in the audit trail");
        Thread.sleep(15_000);
        List<Event> events = getObjectsFromDB(DbName.AUDIT, "event", "ucid = '" + ucid + "'", Event.class);
        System.out.println("first event = " + events.getFirst());
        System.out.println("last event = " + events.getLast());
        Event event1 = events.get(events.size() - 2);
        System.out.println("test event1 (Request) = " + event1);
        Event event2 = events.getLast();
        System.out.println("test event2 (Applyment)= " + event2);
        assertEquals("RESTRICTION_REQUESTED", event1.getType());
        assertEquals(expectedSystem, event1.getInitiatedBySystem());
        assertEquals(expectedUser, event1.getInitiatedByUser());
        assertEquals(expectedComment, event1.getComment());
        assertEquals(detail, event1.getDetails().split("; ")[0]);
        assertEquals(String.valueOf(accountId), event1.getDetails().split("; account: ")[1]);

        assertEquals("RESTRICTION_APPLIED", event2.getType());
        assertEquals(expectedSystem, event2.getInitiatedBySystem());
        assertEquals(expectedUser, event2.getInitiatedByUser());
        assertNull(event2.getComment());
        assertEquals(detail, event2.getDetails().split("; ")[0]);
        assertEquals(String.valueOf(accountId), event2.getDetails().split("; account: ")[1]);
    }


    public void isPageLoaded() {
        int n = 0;
        page.waitForTimeout(2000);
        while ((loaderAnimation.isVisible() || loaderSpin.isVisible()) && n < 8) {
            page.waitForTimeout(2000);
            n += 1;
        }
    }

    public void cleanKafka(String ucid) throws Exception {
        readMessagesFromClientApply(ucid);
        readMessagesFromWithdrawalApprovals(ucid);

    }

    public void isInactiveAccountLabelPresented() {
        Allure.step("check that account label is presented and have text 'inactive'");
        String label = accountLabel.nth(0).textContent();
        assertEquals("Inactive", label);

    }

    public void hoverOverActivitySection() {
        Allure.step("hover mouse over account activity section (with the date or day)");
        activitySection.nth(0).hover();
    }

    public void checkTooltipText(String text) {
        Allure.step("check that appeared tooltip have text '" + text + "'");
        String label = tooltip.textContent();
        assertEquals(text, label);
    }

    public void checkDisplayedRestrictionsDetails() {
        Allure.step("check that text displayed near restrictions is correct'");
        for (Restriction restriction : Restriction.values()) {
            if (restriction.isBoVisibility()) {
                assertThat(page.locator("//div[contains(@class, 'v-restrictions-tab-item__name') and text()='" + restriction.getName() + "']/../following-sibling::div")).hasText(restriction.getDescription());
                System.out.println("restriction '" + restriction.getName() + "' have correct description :'" + restriction.getDescription() + "'");
            }
        }
    }

    public void checkDisplayedRestrictions() {
        Allure.step("check that only restrictions with bo_visibility == true in database is displayed");
        for (Restriction restriction : Restriction.values()) {
            if (restriction.isBoVisibility()) {
                page.locator("//div[contains(@class, 'v-restrictions-tab-item__name') and text()='" + restriction.getName() + "']/../following-sibling::div").waitFor(new Locator.WaitForOptions().setState(VISIBLE));
                System.out.println("restriction '" + restriction.getName() + " is visible");
            } else {

                page.locator("//div[contains(@class, 'v-restrictions-tab-item__name') and text()='" + restriction.getName() + "']/../following-sibling::div").waitFor(new Locator.WaitForOptions().setState(HIDDEN));
                System.out.println("restriction '" + restriction.getName() + " is not visible");
            }
        }
    }

    public void waitForPageToLoad() {
        page.waitForSelector("//div[@class='v-restrictions-tab-list']", new Page.WaitForSelectorOptions().setState(VISIBLE));
    }

}

