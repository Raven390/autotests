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
import com.microsoft.playwright.options.WaitForSelectorState;
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

public class RestrictionPage extends AbstractPage {

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
    private final Locator selectAllAccCheckbox;
    private final Locator checkedCloseOnlyMode;
    private final Locator checkedOffQuotesMode;
    private final Locator checkedAbBook;
    private final Locator withdrawalList;
    private final Locator approveAllwithdrawalsButton;
    private final Locator rejectAllwithdrawalsButton;
    private final Locator approveFirstButton;


    public RestrictionPage(Page page) {
        super(page);
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
        page.navigate("http://k8s-test-nginxrev-55e209d446-410128713.us-east-1.elb.amazonaws.com/investigation?client_ucid=" + ucid);
        waitForPageToLoad();
        restrictionTab.click();
        waitForPageToLoad();
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
    public void clickAccountSwitch() throws InterruptedException, JsonProcessingException {
        accountSwitch.click();
    }

    @Step("Set transfer restriction")
    public void clickTransferSwitch() throws InterruptedException, JsonProcessingException {
        transferSwitch.click();
    }

    @Step("Set deposit restriction")
    public void clickDepositsSwitch() throws InterruptedException, JsonProcessingException {
        depositsSwitch.click();
    }

    @Step("Set Withdrawals restriction")
    public void clickWithdrawalsSwitch() throws InterruptedException, JsonProcessingException {
        withdrawalsSwitch.click();
    }

    @Step("Set login restriction")
    public void clickLoginSwitch() throws InterruptedException, JsonProcessingException {
        loginSwitch.click();
    }

    @Step("Set ManualWithdrawal restriction")
    public void clickManualWithdrawalSwitch() throws InterruptedException, JsonProcessingException {
        manualSwitch.click();
    }

    @Step("Set Close only mode restriction")
    public void clickCloseOnlyModeSwitch() throws InterruptedException, JsonProcessingException {
        closeSwitch.click();
    }

    @Step("Set OffQuotes restriction")
    public void clickOffQuotesModeSwitch() throws InterruptedException, JsonProcessingException {
        offQuotesSwitch.click();
    }

    @Step("Set AbBook restriction")
    public void clickAbBookSwitch() throws InterruptedException, JsonProcessingException {
        abBookSwitch.click();
    }

    @Step("Check that account restriction tumbler is checked")
    public void checkThatAccountIsChecked() throws InterruptedException, JsonProcessingException {
        checkedAccount.isVisible();
    }

    @Step("Check that OffQuotes tumbler is checked")
    public void checkThatAOffQuotesIsChecked() throws InterruptedException, JsonProcessingException {
        checkedOffQuotesMode.isVisible();
    }

    @Step("Check that AbBook tumbler is checked")
    public void checkThatAbBookIsChecked() throws InterruptedException, JsonProcessingException {
        checkedAbBook.isVisible();
    }

    @Step("Check that Transfer restriction tumbler is checked")
    public void checkThatTransferIsChecked() throws InterruptedException, JsonProcessingException {
        checkedTransfer.isVisible();
    }

    @Step("Check that Deposits restriction tumbler is checked")
    public void checkThatDepositsIsChecked() throws InterruptedException, JsonProcessingException {
        checkedDeposits.isVisible();
    }

    @Step("Check that Withdrawals restriction tumbler is checked")
    public void checkThatWithdrawalsIsChecked() throws InterruptedException, JsonProcessingException {
        checkedWithdrawals.isVisible();
    }

    @Step("Check that Login restriction tumbler is checked")
    public void checkThatLoginIsChecked() throws InterruptedException, JsonProcessingException {
        checkedLogin.isVisible();
    }

    @Step("Check that ManualWithdrawal restriction tumbler is checked")
    public void checkThatCloseOnlyIsChecked() throws InterruptedException, JsonProcessingException {
        checkedCloseOnlyMode.isVisible();
    }

    @Step("Check that ManualWithdrawal restriction tumbler is checked")
    public void checkThatManualWithdrawalIsChecked() throws InterruptedException, JsonProcessingException {
        checkedManual.isVisible();
    }

    @Step("Click checked account tumbler")
    public void clickCheckedAccount() throws InterruptedException, JsonProcessingException {
        checkedAccount.click();
    }

    @Step("Click checked transfer tumbler")
    public void clickCheckedTransfer() throws InterruptedException, JsonProcessingException {
        checkedTransfer.click();
    }

    @Step("Click checked Deposits tumbler")
    public void clickCheckedDeposits() throws InterruptedException, JsonProcessingException {
        checkedDeposits.click();
    }

    @Step("Click checked Withdrawals tumbler")
    public void clickCheckedWithdrawals() throws InterruptedException, JsonProcessingException {
        checkedWithdrawals.click();
    }

    @Step("Click checked Login CRM tumbler")
    public void clickCheckedLogin() throws InterruptedException, JsonProcessingException {
        checkedLogin.click();
    }

    @Step("Click checked Manual Withdrawal Review tumbler")
    public void clickCheckedManual() throws InterruptedException, JsonProcessingException {
        checkedManual.click();
    }

    @Step("Click checked Close Only Mode tumbler")
    public void clickCheckedCloseOnly() throws InterruptedException, JsonProcessingException {
        checkedCloseOnlyMode.click();
    }

    @Step("Click checked OffQuotes tumbler")
    public void clickCheckedOffQuotes() throws InterruptedException, JsonProcessingException {
        checkedOffQuotesMode.click();
    }

    @Step("Click checked AbBook tumbler")
    public void clickCheckedAbBook() throws InterruptedException, JsonProcessingException {
        checkedAbBook.click();
    }

    @Step("Fill apply reason")
    public void fillApplyReason(String reason) throws InterruptedException, JsonProcessingException {
        dialog.isVisible();
        reasonInput.fill(reason);
        restrictionSetSet.click();
        setToast.isVisible();
        page.waitForTimeout(1000);
    }

    @Step("Fill apply reason trading Green all accounts")
    public void fillApplyReasonTradingAllAccs(String reason) throws InterruptedException, JsonProcessingException {
        dialog.isVisible();
        selectAllAccCheckbox.click();
        reasonInput.fill(reason);
        restrictionSetSet.click();
        setToast.isVisible();
        page.waitForTimeout(1000);
    }

    @Step("Fill cancel reason")
    public void fillCancelReason(String reason) throws InterruptedException, JsonProcessingException {
        dialog.isVisible();
        reasonInput.fill(reason);
        restrictionCancelSet.click();
        cancelToast.isVisible();
        page.waitForTimeout(1000);
    }

    @Step("Fill cancel reason Manual withdrawal with withdrawals all green")
    public void fillCancelReasonManualWithdrawalAllGreen(String reason) throws InterruptedException,
            JsonProcessingException {
        page.waitForTimeout(1000);
        waitForPageToLoad();
        assertTrue(dialog.isVisible());
        assertTrue(withdrawalList.isVisible());
        approveAllwithdrawalsButton.click();
        reasonInput.fill(reason);
        restrictionCancelSet.click();
        cancelToast.isVisible();
        page.waitForTimeout(1000);
    }

    @Step("Fill cancel reason Manual withdrawal with withdrawals all refuse")
    public void fillCancelReasonManualWithdrawalAllrefuse(String reason) throws InterruptedException,
            JsonProcessingException {
        page.waitForTimeout(1000);
        waitForPageToLoad();
        assertTrue(dialog.isVisible());
        assertTrue(withdrawalList.isVisible());
        rejectAllwithdrawalsButton.click();
        reasonInput.fill(reason);
        restrictionCancelSet.click();
        cancelToast.isVisible();
        page.waitForTimeout(1000);
    }

    @Step("Fill cancel reason Manual withdrawal with withdrawals approve one")
    public void fillCancelReasonManualWithdrawalApproveOne(String reason) throws InterruptedException,
            JsonProcessingException {
        page.waitForTimeout(1000);
        waitForPageToLoad();
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
    public void fillCancelReasonTrade(String reason) throws InterruptedException, JsonProcessingException {
        page.waitForTimeout(1000);
        waitForPageToLoad();
        dialog.isVisible();
        selectAllAccCheckbox.click();
        reasonInput.fill(reason);
        restrictionCancelSetTrade.click();
        cancelToast.isVisible();
        page.waitForTimeout(1000);
    }

    @Step("Check request to apply message")
    public void checkKafkaRequestApplyUCID(String userId) throws InterruptedException, JsonProcessingException {
        KafkaHelper helper = new KafkaHelper();
        List<String> kafkaResponses = helper.consumeMessages("client.restrictions.apply", userId);
        String kafkaResponse = kafkaResponses.getLast();
        ObjectMapper objectMapper = new ObjectMapper();
        ClientRestrictionApply apply = objectMapper.readValue(kafkaResponse, ClientRestrictionApply.class);
        apply.clientId.equals(userId);
        assertNotNull((apply.clientId));
        assertNotNull((apply.timestamp));
        assertNotNull((apply.messageId));
        assertNotNull((apply.regulator));
        assertNotNull((apply.restrictions));
    }

    @Step("Check withdrawal approval message")
    public void checkKafkaRequestWithdrawal(String transactionID, String expectedStatus) throws InterruptedException,
            JsonProcessingException {
        KafkaHelper helper = new KafkaHelper();
        List<String> kafkaResponses = helper.consumeMessages("withdrawal.approvals", transactionID);
        String kafkaResponse = kafkaResponses.getLast();
        ObjectMapper objectMapper = new ObjectMapper();
        WithdrawalApprovals apply = objectMapper.readValue(kafkaResponse, WithdrawalApprovals.class);
        apply.transferId.equals(transactionID);
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
    public void checkKafkaRequestApplyUCIDID(String userId, String Id) throws InterruptedException,
            JsonProcessingException {
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
                ucid, code, "GENERAL", null, null, "Integration test", new PostRestrictionRequestBody.UpdatedBy("string", "string")


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

    @Step("Wait for page to load")
    public void waitForPageToLoad() {
        page.waitForSelector(LOADING_ANIMATION_SELECTOR, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.HIDDEN));
        page.waitForSelector(LOADER_SPIN_LOCATOR, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.HIDDEN));
    }
}

