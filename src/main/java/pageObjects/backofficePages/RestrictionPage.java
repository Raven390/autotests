package pageObjects.backofficePages;

import businessObjects.api.mitigationService.PostRestrictionRequestBody;
import businessObjects.db.auditServiceDb.Event;
import businessObjects.db.mitigationServiceDb.ClientsRestriction;
import businessObjects.kafka.restrictionEvents.ClientRestrictionApply;
import businessObjects.kafka.restrictionEvents.WithdrawalApprovals;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import helpers.database.DbName;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;

import static businessObjects.api.mitigationService.MitigationServiceRequest.postRestriction;
import static helpers.database.DbHelper.deleteEntryFromDb;
import static helpers.database.DbHelper.getObjectsFromDB;
import static org.junit.jupiter.api.Assertions.*;

public class RestrictionPage {

    private final Page page;
    private final Locator restrictionTab;
    private final Locator accountSwitch;
    private final Locator transferSwitch;
    private final Locator depositsSwitch;
    private final Locator withdrawalsSwitch;
    private final Locator loginSwitch;
    private final Locator manualSwitch;
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


    public RestrictionPage(Page page) {
        this.page = page;
        this.loaderAnimation = page.locator(".v-loader");
        this.loaderSpin = page.locator(".g-spin");
        this.restrictionTab = page.locator("[role=\"tab\"][title=\"Restrictions\"]");
        this.accountSwitch = page.locator(".v-restrictions-tab-item__name").getByText("Open new account");
        this.transferSwitch = page.locator(".v-restrictions-tab-item__name").getByText("Internal transfer");
        this.depositsSwitch = page.locator(".v-restrictions-tab-item__name").getByText("Deposits");
        this.withdrawalsSwitch = page.locator(".v-restrictions-tab-item__name").getByText("Withdrawals");
        this.loginSwitch = page.locator(".v-restrictions-tab-item__name").getByText("Login CRM");
        this.manualSwitch = page.locator(".v-restrictions-tab-item__name").getByText("Manual Withdrawal Review");
        this.closeSwitch = page.locator(".v-restrictions-tab-item__name").getByText("Close only mode");
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
        this.checkedAccount = page.locator(".v-restrictions-tab-item_checked").getByText("Open new account");
        this.checkedTransfer = page.locator(".v-restrictions-tab-item_checked").getByText("Internal transfer");
        this.checkedDeposits = page.locator(".v-restrictions-tab-item_checked").getByText("Deposits");
        this.checkedWithdrawals = page.locator(".v-restrictions-tab-item_checked").getByText("Withdrawals");
        this.checkedLogin = page.locator(".v-restrictions-tab-item_checked").getByText("Login CRM");
        this.checkedManual = page.locator(".v-restrictions-tab-item_checked").getByText("Manual Withdrawal Review");
        this.checkedCloseOnlyMode = page.locator(".v-restrictions-tab-item_checked").getByText("Close only mode");
        this.checkedOffQuotesMode = page.locator(".v-restrictions-tab-item_checked").getByText("Off quotes");
        this.checkedAbBook = page.locator(".v-restrictions-tab-item_checked").getByText("B-Book -> A-Book");
        this.withdrawalList = page.locator(".v-withdrawals-list");
        this.approveAllwithdrawalsButton = page.locator(".v-withdrawals-list__reject-resolve button").nth(0);
        this.rejectAllwithdrawalsButton = page.locator(".v-withdrawals-list__reject-resolve button").nth(1);
        this.approveFirstButton = page.locator(".v-withdrawals-list__reject-resolve button").nth(2);
    }

    @Step("Open users restriction tab")
    public void navigate(String ucid) {
        page.navigate("http://k8s-test-nginxrev-55e209d446-410128713.us-east-1.elb.amazonaws.com/investigation/" + ucid);
        isPageLoaded();
        restrictionTab.click();
        isPageLoaded();
    }

    @Step("Check that restriction tab rendered properly")
    public void checkUI() {
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

    @Step("Set account restriction")
    public void clickAccountSwitch() {
        accountSwitch.click();
    }

    @Step("Set transfer restriction")
    public void clickTransferSwitch() {
        transferSwitch.click();
    }

    @Step("Set deposit restriction")
    public void clickDepositsSwitch() {
        depositsSwitch.click();
    }

    @Step("Set Withdrawals restriction")
    public void clickWithdrawalsSwitch() {
        withdrawalsSwitch.click();
    }

    @Step("Set login restriction")
    public void clickLoginSwitch() {
        loginSwitch.click();
    }

    @Step("Set ManualWithdrawal restriction")
    public void clickManualWithdrawalSwitch() {
        manualSwitch.click();
    }

    @Step("Set Close only mode restriction")
    public void clickCloseOnlyModeSwitch() {
        closeSwitch.click();
    }

    @Step("Set OffQuotes restriction")
    public void clickOffQuotesModeSwitch() {
        offQuotesSwitch.click();
    }

    @Step("Set AbBook restriction")
    public void clickAbBookSwitch() {
        abBookSwitch.click();
    }

    @Step("Check that account restriction tumbler is checked")
    public void checkThatAccountIsChecked() {
        checkedAccount.isVisible();
    }

    @Step("Check that OffQuotes tumbler is checked")
    public void checkThatAOffQuotesIsChecked() {
        checkedOffQuotesMode.isVisible();
    }

    @Step("Check that AbBook tumbler is checked")
    public void checkThatAbBookIsChecked() {
        checkedAbBook.isVisible();
    }

    @Step("Check that Transfer restriction tumbler is checked")
    public void checkThatTransferIsChecked() {
        checkedTransfer.isVisible();
    }

    @Step("Check that Deposits restriction tumbler is checked")
    public void checkThatDepositsIsChecked() {
        checkedDeposits.isVisible();
    }

    @Step("Check that Withdrawals restriction tumbler is checked")
    public void checkThatWithdrawalsIsChecked() {
        checkedWithdrawals.isVisible();
    }

    @Step("Check that Login restriction tumbler is checked")
    public void checkThatLoginIsChecked() {
        checkedLogin.isVisible();
    }

    @Step("Check that ManualWithdrawal restriction tumbler is checked")
    public void checkThatCloseOnlyIsChecked() {
        checkedCloseOnlyMode.isVisible();
    }

    @Step("Check that ManualWithdrawal restriction tumbler is checked")
    public void checkThatManualWithdrawalIsChecked() {
        checkedManual.isVisible();
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

    @Step("Click checked Close Only Mode tumbler")
    public void clickCheckedCloseOnly() {
        checkedCloseOnlyMode.click();
    }

    @Step("Click checked OffQuotes tumbler")
    public void clickCheckedOffQuotes() {
        checkedOffQuotesMode.click();
    }

    @Step("Click checked AbBook tumbler")
    public void clickCheckedAbBook() {
        checkedAbBook.click();
    }

    @Step("Fill apply reason")
    public void fillApplyReason(String reason) {
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

    @Step("Fill cancel reason")
    public void fillCancelReasonTrade(String reason) {
        page.waitForTimeout(1000);
        isPageLoaded();
        dialog.isVisible();
        selectAllAccCheckbox.click();
        reasonInput.fill(reason);
        restrictionCancelSetTrade.click();
        cancelToast.isVisible();
        page.waitForTimeout(1000);
    }

    @Step("Check request to apply message")
    public void checkKafkaRequestApplyUCID(String userId) throws JsonProcessingException, InterruptedException {
        Thread.sleep(4000);
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

    @Step("Check withdrawal approval message")
    public void checkKafkaRequestWithdrawal(String transactionID, String expectedStatus) throws InterruptedException,
            JsonProcessingException {
        Thread.sleep(4000);
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
        apply.transferId.toString().equals(transactionID);
        assertNotNull((apply.regulator));
        assertNotNull((apply.brand));
        assertNotNull((apply.timestamp));
        assertNotNull((apply.status));
        assertEquals(expectedStatus, (apply.status));
    }


    @Step("Check request to apply message trade")
    public void checkKafkaRequestApplyTradeUCID(String userId) throws InterruptedException, JsonProcessingException {
        KafkaHelper helper = new KafkaHelper();
        String kafkaResponse = helper.consumeMessage("account.restrictions.apply", userId);
        ObjectMapper objectMapper = new ObjectMapper();
        ClientRestrictionApply apply = objectMapper.readValue(kafkaResponse, ClientRestrictionApply.class);
        apply.clientId.equals(userId);
        assertNotNull((apply.clientId));
        assertNotNull((apply.timestamp));
        assertNotNull((apply.messageId));
        assertNotNull((apply.regulator));
        assertNotNull((apply.restrictions));
        assertNotEquals((apply.clientId), "null");
        assertNotEquals((apply.timestamp), "null");
        assertNotEquals((apply.messageId), "null");
        assertNotEquals((apply.regulator), "null");
        assertNotEquals((apply.restrictions), "null");
    }

    @Step
    public void checkKafkaRequestApplyTradeUCIDID(String userId, String Id) throws InterruptedException,
            JsonProcessingException {
        Thread.sleep(4000);
        KafkaHelper helper = new KafkaHelper();
        String kafkaResponse = helper.consumeMessage("account.restrictions.apply", userId);
        System.out.println(kafkaResponse);
        ObjectMapper objectMapper = new ObjectMapper();
        ClientRestrictionApply apply = objectMapper.readValue(kafkaResponse, ClientRestrictionApply.class);
        apply.clientId.toString().equals(userId);
        assertNotNull((apply.clientId));
        assertNotNull((apply.timestamp));
        assertNotNull((apply.messageId));
        assertNotNull((apply.regulator));
        assertNotNull((apply.restrictions));
        assertNotEquals((apply.clientId), null);
        assertNotEquals((apply.timestamp), "null");
        assertNotEquals((apply.messageId), "null");
        assertNotEquals((apply.regulator), "null");
        assertNotEquals((apply.restrictions), null);
    }

    @Step
    public void checkKafkaRequestApplyUCIDID(String userId, String Id) throws InterruptedException,
            JsonProcessingException {
        Thread.sleep(2000);
        KafkaHelper helper = new KafkaHelper();
        String kafkaResponse = helper.consumeMessage("client.restrictions.apply", userId);
        ObjectMapper objectMapper = new ObjectMapper();
        ClientRestrictionApply apply = objectMapper.readValue(kafkaResponse, ClientRestrictionApply.class);
        apply.clientId.equals(userId);
        assertNotNull((apply.clientId));
        assertNotNull((apply.timestamp));
        assertNotNull((apply.messageId));
        assertNotNull((apply.regulator));
        assertNotNull((apply.restrictions));
        assertNotEquals((apply.clientId), "null");
        assertNotEquals((apply.timestamp), "null");
        assertNotEquals((apply.messageId), "null");
        assertNotEquals((apply.regulator), "null");
        assertNotEquals((apply.restrictions), "null");
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
    public void cleanUserRestriction(String ucid) throws Exception {
        List<ClientsRestriction> restrictionList = getObjectsFromDB(DbName.MITIGATION_POSTGRES, "clients_restriction", "ucid = '" + ucid + "'", ClientsRestriction.class);
        for (ClientsRestriction i : restrictionList) {
            String Id = i.id.toString();
            deleteEntryFromDb(DbName.MITIGATION_POSTGRES, "mi.mi.action", "clients_restriction_id = " + Id);
            Thread.sleep(200);
            deleteEntryFromDb(DbName.MITIGATION_POSTGRES, "mi.mi.kafka_request", "clients_restriction_id = " + Id);
            Thread.sleep(200);
            deleteEntryFromDb(DbName.MITIGATION_POSTGRES, "mi.mi.kafka_response", "clients_restriction_id = " + Id);
            Thread.sleep(200);
            deleteEntryFromDb(DbName.MITIGATION_POSTGRES, "mi.mi.clients_restriction", "id = " + Id);
            Thread.sleep(200);
        }
    }

    @Step("Clean users audit history")
    public void cleanUserAudit(String ucid) throws Exception {
        deleteEntryFromDb(DbName.AUDIT, "au.au.event", "ucid = '" + ucid + "'");
        Thread.sleep(200);
    }

    @Step("Set restriction though API")
    public void setRestrictionAPIGeneral(String ucid, String code) throws IOException {
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

    public void checkRestrictionCancellationAudit(String ucid, String detail) throws Exception {
        List<Event> event = getObjectsFromDB(DbName.AUDIT, "au.au.event", "ucid = '" + ucid + "'", Event.class);
        String type1 = event.get(2).getType();
        assertEquals("CANCELLATION_REQUESTED", type1);
        String details = event.get(2).getDetails();
        assertEquals(details, detail);
        String type2 = event.get(3).getType();
        assertEquals("RESTRICTION_CANCELLED", type2);
        String system = event.get(2).getInitiatedBySystem();
        assertEquals("Vindex BO", system);
    }

    public void checkRestrictionCancellationAudit(String ucid, String type, String expectedDetails) throws Exception {
        List<Event> event = getObjectsFromDB(DbName.AUDIT, "au.au.event", "ucid = '" + ucid + "' and type = '" + type + "' AND details = '" + expectedDetails + "'", Event.class);
        assertNotNull(event);
        assertNotNull(event.get(0).getKafkaMessageId());
        assertNotNull(event.get(0).getId());
        assertNotNull(event.get(0).getUcid());
        assertNotNull(event.get(0).getType());
        assertNotNull(event.get(0).getCreatedAt());
        assertNotNull(event.get(0).getInitiatedBySystem());
        assertNotNull(event.get(0).getInitiatedByUser());
        assertNotNull(event.get(0).getComment());
    }

    public void checkRestrictionApplymentAudit(String ucid, String detail) throws Exception {
        List<Event> event = getObjectsFromDB(DbName.AUDIT, "au.au.event", "ucid = '" + ucid + "'", Event.class);
        String type1 = event.get(0).getType();
        assertEquals("RESTRICTION_REQUESTED", type1);
        String details = event.get(0).getDetails();
        assertEquals(details, detail);
        String type2 = event.get(1).getType();
        assertEquals("RESTRICTION_APPLIED", type2);
        String system = event.get(0).getInitiatedBySystem();
        assertEquals("Vindex BO", system);
    }

    @Step("Check if the page loaded")
    public void isPageLoaded() {
        int n = 0;
        page.waitForTimeout(2000);
        while ((loaderAnimation.isVisible() || loaderSpin.isVisible()) && n < 8) {
            page.waitForTimeout(2000);
            n += 1;
        }
    }

    @Step("Check if the page loaded")
    public void cleanKafka(String userId) throws Exception {
        readMessagesFromClientApply(userId);
        readMessagesFromWithdrawalApprovals(userId);

    }


}

