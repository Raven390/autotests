package page_objects.backoffice_pages.investigationTool;

import business_objects.api.mitigation_service.PostRestrictionRequestBody;
import business_objects.db.audit_service_db.Event;
import business_objects.db.mitigation_service_db.ClientGeneralRestriction;
import business_objects.db.mitigation_service_db.ClientTradingRestriction;
import business_objects.kafka.restriction_events.*;
import business_objects.ui.user.User;
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
import page_objects.backoffice_pages.AbstractPage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static business_objects.api.mitigation_service.MitigationServiceRequest.postRestriction;
import static com.microsoft.playwright.options.WaitForSelectorState.*;
import static helpers.database.DbHelper.deleteEntryFromDb;
import static helpers.database.DbHelper.getObjectsFromDB;
import static org.hamcrest.Matchers.*;

import static org.hamcrest.MatcherAssert.assertThat;

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
    private final Locator openRestrictionsDrawerButton;
    private final Locator addRestrictionButton;
    private final Locator applyRestrictionButton;
    private final Locator commentInput;
    private final Locator applyChangesButton;
    private final Locator restrictionAppliedIcon;
    private final Locator restrictionAppliedBy;
    private final Locator restrictionAppliedComment;
    private final Locator restrictionAppliedDate;
    private final Locator restrictionTabLoaded;
    private final Locator inactiveAccountLabel;
    private final Locator restrictionOption;
    private final Locator restrictionOptionsContainer;

    private static final String RESTRICTION_ITEM_BY_NAME_PATTERN = "//div[contains(@class,'v-restrictions-tab-item__name') and text()='%s']";
    private static final String RESTRICTIONS_TAB_ITEM_NAME = ".v-restrictions-tab-item__name";
    private static final String RESTRICTIONS_TAB_ITEM_CHECKED = ".v-restrictions-tab-item_checked";
    private static final String RESTRICTIONS_TAB_ITEM_HEADER = ".v-restrictions-tab-item__header";
    private static final String CHECKED_RESTRICTION = String.format("%s %s", RESTRICTIONS_TAB_ITEM_CHECKED, RESTRICTIONS_TAB_ITEM_HEADER);
    private static final String RESTRICTION_OPTION_PATTERN = "//span[@class='g-select-list__option-default-label' and text()='%s']";
    private static final String RESTRICTION_TAB_ITEM_BY_NAME = "//div[contains(@class,'v-restrictions-tab-item__name') and text()='%s']/ancestor::div[@class='v-restrictions-tab-item']";
    private static final String ACTIVE_RESTRICTION_BY_NAME = "//div[text()='%s']/ancestor::div[@class='v-client-restrictions-list-item']";
    private static final String CLEAR_RESTRICTION_BUTTON_BY_NAME = ACTIVE_RESTRICTION_BY_NAME + "/descendant::button[@data-qa='selected_fraud_type_item__remove_button']";
    private static final String RESTRICTION_ACCOUNT_SELECTION_BUTTON_BY_NAME = ACTIVE_RESTRICTION_BY_NAME + "/descendant::span[contains(text(),'account')]/ancestor::button";

    public RestrictionPage(Page page) {
        super(page);
        this.loaderAnimation = page.locator(".v-loader");
        this.loaderSpin = page.locator(".g-spin").first();
        this.restrictionTab = page.locator("[role=\"tab\"][title=\"Restrictions\"]");
        this.accountSwitch = page.locator(RESTRICTIONS_TAB_ITEM_NAME).getByText(Restriction.ACCOUNT_CREATION.getName());
        this.transferSwitch = page.locator(RESTRICTIONS_TAB_ITEM_NAME).getByText(Restriction.INTERNAL_TRANSFER.getName());
        this.depositsSwitch = page.locator(RESTRICTIONS_TAB_ITEM_NAME).getByText(Restriction.DEPOSITS.getName());
        this.withdrawalsSwitch = page.locator(RESTRICTIONS_TAB_ITEM_NAME).getByText(Restriction.WITHDRAWALS.getName(), new Locator.GetByTextOptions().setExact(true));
        this.loginSwitch = page.locator(RESTRICTIONS_TAB_ITEM_NAME).getByText(Restriction.LOGIN_CRM.getName());
        this.manualSwitch = page.locator(RESTRICTIONS_TAB_ITEM_NAME).getByText(Restriction.MANUAL_WITHDRAWAL_REVIEW.getName());
        this.creditAndBonusSwitch = page.locator(RESTRICTIONS_TAB_ITEM_NAME).getByText(Restriction.CREDIT_AND_BONUS.getName());
        this.closeSwitch = page.locator(RESTRICTIONS_TAB_ITEM_NAME).getByText(Restriction.CLOSE_ONLY_MODE.getName());
        this.offQuotesSwitch = page.locator(RESTRICTIONS_TAB_ITEM_NAME).getByText("Off quotes");
        this.abBookSwitch = page.locator(RESTRICTIONS_TAB_ITEM_NAME).getByText("B-Book -> A-Book");
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
        this.checkedAccount = page.locator(CHECKED_RESTRICTION).getByText(Restriction.ACCOUNT_CREATION.getName());
        this.checkedTransfer = page.locator(CHECKED_RESTRICTION).getByText(Restriction.INTERNAL_TRANSFER.getName());
        this.checkedDeposits = page.locator(CHECKED_RESTRICTION).getByText(Restriction.DEPOSITS.getName());
        this.checkedWithdrawals = page.locator(CHECKED_RESTRICTION).getByText(Restriction.WITHDRAWALS.getName());
        this.checkedLogin = page.locator(CHECKED_RESTRICTION).getByText(Restriction.LOGIN_CRM.getName());
        this.checkedManual = page.locator(CHECKED_RESTRICTION).getByText(Restriction.MANUAL_WITHDRAWAL_REVIEW.getName());
        this.checkedCreditAndBonus = page.locator(CHECKED_RESTRICTION).getByText(Restriction.CREDIT_AND_BONUS.getName());
        this.checkedCloseOnlyMode = page.locator(CHECKED_RESTRICTION).getByText(Restriction.CLOSE_ONLY_MODE.getName());
        this.checkedOffQuotesMode = page.locator(CHECKED_RESTRICTION).getByText(Restriction.OFF_QUOTES.getName());
        this.checkedAbBook = page.locator(CHECKED_RESTRICTION).getByText(Restriction.B_BOOK_TO_A_BOOK.getName());
        this.withdrawalList = page.locator(".v-withdrawals-list");
        this.approveAllwithdrawalsButton = page.locator(".v-withdrawals-list__reject-resolve button").nth(0);
        this.rejectAllwithdrawalsButton = page.locator(".v-withdrawals-list__reject-resolve button").nth(1);
        this.approveFirstButton = page.locator(".v-withdrawals-list__reject-resolve button").nth(2);
        this.accountLabel = page.locator(".v-accounts-list-item__labels");
        this.activitySection = page.locator(".v-accounts-list-item__activity");
        this.tooltip = page.locator(".g-tooltip__content");
        this.openRestrictionsDrawerButton = page.locator("//span[text()='Apply restrictions' or text()=' Manage']");
        this.addRestrictionButton = page.locator("//div[@class='v-list-select']/descendant::button");
        this.applyRestrictionButton = page.locator("//span[text()='Apply']/parent::button[not(@disabled)]");
        this.commentInput = page.locator("//textarea");
        this.applyChangesButton = page.locator("//div[@class='v-restrictions-tab-drawer__footer']/descendant::span[text()='Apply changes' or text()='Apply']/parent::button");
        this.restrictionAppliedIcon = page.locator("//div[@class='v-restrictions-tab-item__status']/*[not(@class)]");
        this.restrictionAppliedBy = page.locator("//span[contains(@class,'v-restrictions-tab-item__actor')]");
        this.restrictionAppliedComment = page.locator("//span[contains(@class,'v-restrictions-tab-item__comment')]");
        this.restrictionAppliedDate = page.locator("//span[contains(@class,'v-restrictions-tab-item__date')]");
        this.restrictionTabLoaded = page.locator("//div[@class='v-investigation-tools-tabs__content']");
        this.inactiveAccountLabel = page.locator("//div[@class='v-accounts-list-item__labels']/descendant::div[text()='Inactive']").first();
        this.restrictionOption = page.locator("//span[@class='g-select-list__option-default-label']");
        this.restrictionOptionsContainer = page.locator("//div[@class='v-list-select__list-container']");
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
        assertNotNull((apply.restriction));
    }

    public static void checkKafkaRequestApplyAccount(int accountIdInt, int serverId, int banDurationMin,
            int restrictionId, String reason, String restrictionCode) throws JsonProcessingException,
            InterruptedException {
        Allure.step("Check request message for restriction apply for account in kafka");
        String accountId = String.valueOf(accountIdInt);
        KafkaHelper helper = new KafkaHelper();
        List<String> kafkaResponses = helper.consumeMessages(KAFKA_TOPIC_ACCOUNT_RESTRICTIONS_APPLY, String.valueOf(restrictionId));
        String kafkaResponse = kafkaResponses.getLast();
        System.out.println("Tested message is " + kafkaResponse);
        ObjectMapper objectMapper = new ObjectMapper();
        AccountRestrictionApply apply = objectMapper.readValue(kafkaResponse, AccountRestrictionApply.class);
        assertEquals(accountIdInt, apply.accountId);
        assertNotNull((apply.timestamp));
        assertNotNull((apply.messageId));
        assertEquals(serverId, (apply.serverId));
        assertEquals(banDurationMin, (apply.initialBanDurationInMinutes));
        assertNotNull((apply.modifier));
        AccountRestrictionApply.Restriction testRestriction = apply.restriction;
        assertEquals(restrictionId, testRestriction.restrictionId);
        assertEquals(restrictionCode, testRestriction.restrictionCode);
        assertNotNull(testRestriction.sites);
    }


    public void checkKafkaRequestCancelAccount(int accoundIdInt) throws JsonProcessingException, InterruptedException {
        Allure.step("Check request message for restriction cancellation for account in kafka");
        String accoundId = String.valueOf(accoundIdInt);
        KafkaHelper helper = new KafkaHelper();
        List<String> kafkaResponses = helper.consumeMessages(KAFKA_TOPIC_ACCOUNT_RESTRICTIONS_CANCEL, accoundId);
        String kafkaResponse = kafkaResponses.getLast();
        System.out.println("Tested message is " + kafkaResponse);
        ObjectMapper objectMapper = new ObjectMapper();
        AccountRestrictionCancel cancel = objectMapper.readValue(kafkaResponse, AccountRestrictionCancel.class);
        assertNotNull((cancel.getAccountId()));
        assertNotNull((cancel.getTimestamp()));
        assertNotNull((cancel.getMessageId()));
        assertNotNull((cancel.getServerId()));
        assertNotNull((cancel.getModifier()));
        assertNotNull((cancel.getRestrictions()));
    }


    public void checkKafkaRequestWithdrawal(String transactionID, String expectedStatus) throws InterruptedException,
            JsonProcessingException {
        Allure.step("Check withdrawal approval message");
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

    @Step("Clean users restriction history")
    public static void cleanUserRestriction(String ucid) throws Exception {
        Allure.step("Clean user restriction history of client " + ucid);
        List<ClientGeneralRestriction> restrictionList = getObjectsFromDB(DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_GENERAL_RESTRICTION, "ucid = '" + ucid + "'", ClientGeneralRestriction.class);
        for (ClientGeneralRestriction i : restrictionList) {
            String idString = i.getId().toString();
            deleteEntryFromDb(DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_GENERAL_RESTRICTION_ACTION, "client_restriction_id = " + idString);
            Thread.sleep(200);
            deleteEntryFromDb(DbName.MITIGATION_POSTGRES, MITIGATION_KAFKA_REQUEST_GENERAL, "client_restriction_id = " + idString);
            Thread.sleep(200);
            deleteEntryFromDb(DbName.MITIGATION_POSTGRES, MITIGATION_KAFKA_RESPONSE_GENERAL, "client_restriction_id = " + idString);
            Thread.sleep(200);
            deleteEntryFromDb(DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_GENERAL_RESTRICTION, "id = " + idString);
            Thread.sleep(200);
        }
    }

    public static void checkUserHaveRestrictionGeneral(String ucid, int restrictionId, String expectedStatus)
            throws Exception {
        Allure.step("check user have general restriction in Mitigation DataBase");
        List<ClientGeneralRestriction> restrictionList = getObjectsFromDB(DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_GENERAL_RESTRICTION, "ucid = '" + ucid + "' and id = " + restrictionId, ClientGeneralRestriction.class);
        ClientGeneralRestriction restriction = restrictionList.getLast();
        assertEquals(ucid, restriction.getUcid());
        assertEquals(expectedStatus, restriction.getStatus());
    }

    public static void checkUserHaveRestrictionTrading(String ucid, int restrictionId)
            throws Exception {
        Allure.step("check user have trading restriction in Mitigation DataBase");
        List<ClientTradingRestriction> restrictionList = getObjectsFromDB(DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_TRADING_RESTRICTION, "ucid = '" + ucid + "' and id = " + restrictionId, ClientTradingRestriction.class);
        ClientTradingRestriction restriction = restrictionList.getLast();
        assertEquals(ucid, restriction.getUcid());
    }

    @Step("Clean users audit history")
    public void cleanUserAudit(String ucid) throws Exception {
        deleteEntryFromDb(DbName.AUDIT, AUDIT_EVENT, "ucid = '" + ucid + "'");
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
        assertEquals(200, response.code());
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
        return response.body().string();
    }

    @Step("Set restriction though API")
    public static void setRestrictionAPIGeneral(String ucid, String code) throws IOException {
        Allure.step("Set restriction though API GENERAL");
        PostRestrictionRequestBody postRestrictionRequestBody = new PostRestrictionRequestBody(
                ucid, code, "GENERAL", null, null, "Integration test", new PostRestrictionRequestBody.UpdatedBy("test", "automation")
        );
        Response response = postRestriction(postRestrictionRequestBody);
        assertEquals(200, response.code());
    }

    @Step
    public void setRestrictionAPITrade(String ucid, int accId, int serverId, String code) throws IOException {
        Allure.step("Set restriction though API TRADE");
        PostRestrictionRequestBody postRestrictionRequestBody = new PostRestrictionRequestBody(
                ucid, code, "TRADING", accId, serverId, "Integration test", new PostRestrictionRequestBody.UpdatedBy("string", "string")
        );
        Response response = postRestriction(postRestrictionRequestBody);
        assertEquals(200, response.code());
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
        assertEquals(200, response.code());
        assert response.body() != null;
        return response.body().string();
    }

    public void checkRestrictionCancellationAuditBO(String ucid, String detail) throws Exception {
        List<Event> event = getObjectsFromDB(DbName.AUDIT, AUDIT_EVENT, "ucid = '" + ucid + "'", Event.class);
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
        List<Event> event = getObjectsFromDB(DbName.AUDIT, AUDIT_EVENT, "ucid = '" + ucid + "' and type = '" + type + "' AND details = '" + expectedDetails + "'", Event.class);
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
        List<Event> event = getObjectsFromDB(DbName.AUDIT, AUDIT_EVENT, "ucid = '" + ucid + "'", Event.class);
        String type1 = event.get(event.size() - 2).getType();
        assertEquals(RESTRICTION_REQUESTED_STATUS, type1);
        String details = event.get(event.size() - 2).getDetails();
        assertEquals(details, detail);
        String type2 = event.getLast().getType();
        assertEquals(RESTRICTION_APPLIED_STATUS, type2);
        String system = event.getLast().getInitiatedBySystem();
        assertEquals("Vindex BO", system);
    }

    public static void checkRestrictionApplymentAuditGeneral(String ucid, String expectedSystem, String expectedUser,
            String expectedComment, String detail) throws Exception {
        Allure.step("check that record about restriction apply appeared in the audit trail");
        List<Event> events = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            events = getObjectsFromDB(DbName.AUDIT, AUDIT_EVENT, "ucid = '" + ucid + "' ORDER BY created_at ASC", Event.class);
            if (events.size() >= 2) {
                break;
            } else if (i == 9) {
                assertThat("Assert that there are 2 events in audit", events.size(), greaterThanOrEqualTo(2));
            }
            Thread.sleep(1000);
        }
        Event event1 = events.getFirst();
        Event event2 = events.getLast();
        assertThat(event1.getType(), is(oneOf(RESTRICTION_REQUESTED_STATUS, RESTRICTION_APPLIED_STATUS)));
        assertThat(event2.getType(), is(oneOf(RESTRICTION_REQUESTED_STATUS, RESTRICTION_APPLIED_STATUS)));
        if (Objects.equals(event1.getType(), RESTRICTION_REQUESTED_STATUS)) {
            assertEquals(expectedSystem, event1.getInitiatedBySystem());
            assertEquals(expectedUser, event1.getInitiatedByUser());
            assertEquals(expectedComment, event1.getComment());
            assertEquals(detail, event1.getDetails());
            assertEquals(RESTRICTION_APPLIED_STATUS, event2.getType());
            assertEquals(expectedSystem, event2.getInitiatedBySystem());
            assertEquals(expectedUser, event2.getInitiatedByUser());
            assertNull(event2.getComment());
            assertEquals(detail, event2.getDetails());
        } else {
            assertEquals(RESTRICTION_APPLIED_STATUS, event1.getType());
            assertEquals(expectedSystem, event1.getInitiatedBySystem());
            assertEquals(expectedUser, event1.getInitiatedByUser());
            assertNull(event1.getComment());
            assertEquals(detail, event1.getDetails());
            assertEquals(RESTRICTION_REQUESTED_STATUS, event2.getType());
            assertEquals(expectedSystem, event2.getInitiatedBySystem());
            assertEquals(expectedUser, event2.getInitiatedByUser());
            assertEquals(expectedComment, event2.getComment());
            assertEquals(detail, event2.getDetails());
        }
    }

    public static void checkRestrictionApplymentAuditTrading(String ucid, String expectedSystem, String expectedUser,
            String expectedComment, String detail, int accountId) throws Exception {
        String eventDetailsRegex = "; account: ";
        Allure.step("check that record about restriction apply appeared in the audit trail");
        List<Event> events = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            events = getObjectsFromDB(DbName.AUDIT, AUDIT_EVENT, "ucid = '" + ucid + "' ORDER BY created_at ASC", Event.class);
            if (events.size() >= 2) {
                break;
            } else if (i == 9) {
                assertThat("Assert that there are 2 events in audit", events.size(), greaterThanOrEqualTo(2));
            }
            Thread.sleep(1000);
        }
        Event event1 = events.getFirst();
        Event event2 = events.getLast();
        assertThat(event1.getType(), is(oneOf(RESTRICTION_REQUESTED_STATUS, RESTRICTION_APPLIED_STATUS)));
        assertThat(event2.getType(), is(oneOf(RESTRICTION_REQUESTED_STATUS, RESTRICTION_APPLIED_STATUS)));
        if (Objects.equals(event1.getType(), RESTRICTION_REQUESTED_STATUS)) {
            assertEquals(expectedSystem, event1.getInitiatedBySystem());
            assertEquals(expectedUser, event1.getInitiatedByUser());
            assertEquals(expectedComment, event1.getComment());
            assertEquals(detail, event1.getDetails().split("; ")[0]);
            assertEquals(String.valueOf(accountId), event1.getDetails().split(eventDetailsRegex)[1]);
            assertEquals(RESTRICTION_APPLIED_STATUS, event2.getType());
            assertEquals(expectedSystem, event2.getInitiatedBySystem());
            assertEquals(expectedUser, event2.getInitiatedByUser());
            assertNull(event2.getComment());
            assertEquals(detail, event2.getDetails().split("; ")[0]);
            assertEquals(String.valueOf(accountId), event2.getDetails().split(eventDetailsRegex)[1]);
        } else {
            assertEquals(RESTRICTION_APPLIED_STATUS, event1.getType());
            assertEquals(expectedSystem, event1.getInitiatedBySystem());
            assertEquals(expectedUser, event1.getInitiatedByUser());
            assertNull(event1.getComment());
            assertEquals(detail, event1.getDetails().split("; ")[0]);
            assertEquals(String.valueOf(accountId), event1.getDetails().split(eventDetailsRegex)[1]);
            assertEquals(RESTRICTION_REQUESTED_STATUS, event2.getType());
            assertEquals(expectedSystem, event2.getInitiatedBySystem());
            assertEquals(expectedUser, event2.getInitiatedByUser());
            assertEquals(expectedComment, event2.getComment());
            assertEquals(detail, event2.getDetails().split("; ")[0]);
            assertEquals(String.valueOf(accountId), event2.getDetails().split(eventDetailsRegex)[1]);
        }
    }

    public void isPageLoaded() {
        int n = 0;
        page.waitForTimeout(2000);
        while ((loaderAnimation.isVisible() || loaderSpin.isVisible()) && n < 8) {
            page.waitForTimeout(2000);
            n += 1;
        }
    }

    public void waitForPageToLoad() {
        restrictionTabLoaded.waitFor(new Locator.WaitForOptions().setState(VISIBLE));
    }

    @Step("Set the provided restriction in UI")
    public void addNewRestriction(Restriction restriction, String comment) {
        openRestrictionsDrawerButton.click();
        addRestrictionButton.click();
        page.locator(String.format(RESTRICTION_OPTION_PATTERN, restriction.getName())).click();
        applyRestrictionButton.click();
        commentInput.fill(comment);
        applyChangesButton.click();
    }

    @Step("check that the manage restriction button is disabled")
    public void cantAddNewRestriction() {
        Allure.step("check that the manage restriction button is disabled");
        waitForPageToLoad();
        assertTrue(openRestrictionsDrawerButton.isDisabled());
    }

    @Step("Verify restriction is applied in UI")
    public void verifyRestrictionAppliedInUi(Restriction restriction, User user, String comment) {
        Locator restrictionItem = page.locator(String.format(RESTRICTION_TAB_ITEM_BY_NAME, restriction.getName()));
        restrictionItem.waitFor(new Locator.WaitForOptions().setState(VISIBLE));
        assertThat("Verify lock icon is visible", restrictionItem.locator(restrictionAppliedIcon).isVisible(), is(true));
        assertThat("Verify applied by text", restrictionItem.locator(restrictionAppliedBy).textContent(), is(String.format("Set by %s %s", user.getFirstName(), user.getLastName())));
        assertThat("Verify comment", restrictionItem.locator(restrictionAppliedComment).textContent(), is(String.format(" %s", comment)));
        assertThat("Verify application date", restrictionItem.locator(restrictionAppliedDate).textContent(), matchesPattern("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}"));
    }

    @Step("Remove the provided restriction")
    public void removeRestriction(Restriction restriction, String comment) {
        openRestrictionsDrawerButton.click();
        page.locator(String.format(CLEAR_RESTRICTION_BUTTON_BY_NAME, restriction.getName())).click();
        commentInput.fill(comment);
        applyChangesButton.click();
    }

    @Step("Verify restriction is not applied in UI")
    public void verifyRestrictionNotAppliedInUi(Restriction restriction) {
        page.locator(String.format(RESTRICTION_TAB_ITEM_BY_NAME, restriction.getName())).waitFor(new Locator.WaitForOptions().setState(DETACHED));
    }

    @Step("Verify inactive label in account selection is visible")
    public void verifyInactiveLabelIsVisible(Restriction restriction) {
        openRestrictionsDrawerButton.click();
        addRestrictionButton.click();
        page.locator(String.format(RESTRICTION_OPTION_PATTERN, restriction.getName())).click();
        applyRestrictionButton.click();
        page.locator(String.format(RESTRICTION_ACCOUNT_SELECTION_BUTTON_BY_NAME, restriction.getName())).click();
        inactiveAccountLabel.waitFor(new Locator.WaitForOptions().setState(VISIBLE));
    }

    @Step("Get last activity tooltip text")
    public String getLastActivityTooltip(Restriction restriction) {
        openRestrictionsDrawerButton.click();
        addRestrictionButton.click();
        page.locator(String.format(RESTRICTION_OPTION_PATTERN, restriction.getName())).click();
        applyRestrictionButton.click();
        page.locator(String.format(RESTRICTION_ACCOUNT_SELECTION_BUTTON_BY_NAME, restriction.getName())).click();
        activitySection.hover();
        return tooltip.textContent();
    }

    @Step("Get list of all displayed restrictions")
    public List<String> getDisplayedRestrictionsList() {
        openRestrictionsDrawerButton.click();
        addRestrictionButton.click();
        restrictionOptionsContainer.waitFor(new Locator.WaitForOptions().setState(VISIBLE));
        List<String> list = new ArrayList<>();
        for (int i = 0; i < restrictionOption.count(); i++) {
            list.add(restrictionOption.nth(i).textContent());
        }
        return list;
    }

}

