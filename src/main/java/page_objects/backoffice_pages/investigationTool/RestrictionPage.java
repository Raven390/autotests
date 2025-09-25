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
import static helpers.database.DbName.POSTGRES;
import static org.hamcrest.Matchers.*;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.junit.jupiter.api.Assertions.*;
import static utils.ConfigFactory.BASE_URL_E2E;
import static utils.Constants.*;

public class RestrictionPage extends AbstractPage {

    private final Locator restrictionTab;

    private final Locator loaderAnimation;
    private final Locator loaderSpin;
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
    private static final String CLEAR_RESTRICTION_BUTTON_BY_NAME = ACTIVE_RESTRICTION_BY_NAME + "/descendant::button[contains(@data-qa,'control__remove')]";
    private static final String RESTRICTION_ACCOUNT_SELECTION_BUTTON_BY_NAME = ACTIVE_RESTRICTION_BY_NAME + "/descendant::span[contains(text(),'account')]/ancestor::button";

    public RestrictionPage(Page page) {
        super(page);
        this.loaderAnimation = page.locator(".v-loader");
        this.loaderSpin = page.locator(".g-spin").first();
        this.restrictionTab = page.locator("[role=\"tab\"][title=\"Restrictions\"]");
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

    public void navigate(String ucid) {
        Allure.step("Navigate to restrictions tab");
        page.navigate(String.format("%sinvestigation/%s/%s", BASE_URL_E2E, ucid, "restrictions"));
        waitForPageToLoad();
    }


    public void openRestrictionsTab() {
        Allure.step("Open restrictions tab by click tab button in ui");
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
        assertEquals(apply.getTransferId().toString(), (transactionID));
        assertNotNull((apply.getRegulator()));
        assertNotNull((apply.getBrand()));
        assertNotNull((apply.getTimestamp()));
        assertNotNull((apply.getStatus()));
        assertEquals(expectedStatus, (apply.getStatus()));
    }

    @Step("Clean users restriction history")
    public static void cleanUserRestriction(String ucid) throws Exception {
        Allure.step("Clean user restriction history of client " + ucid);
        List<ClientGeneralRestriction> restrictionList = getObjectsFromDB(POSTGRES, MITIGATION_CLIENT_GENERAL_RESTRICTION, "ucid = '" + ucid + "'", ClientGeneralRestriction.class);
        for (ClientGeneralRestriction i : restrictionList) {
            String idString = i.getId().toString();
            deleteEntryFromDb(POSTGRES, MITIGATION_CLIENT_GENERAL_RESTRICTION_ACTION, "client_restriction_id = " + idString);
            Thread.sleep(200);
            deleteEntryFromDb(POSTGRES, MITIGATION_KAFKA_REQUEST_GENERAL, "client_restriction_id = " + idString);
            Thread.sleep(200);
            deleteEntryFromDb(POSTGRES, MITIGATION_KAFKA_RESPONSE_GENERAL, "client_restriction_id = " + idString);
            Thread.sleep(200);
            deleteEntryFromDb(POSTGRES, MITIGATION_CLIENT_GENERAL_RESTRICTION, "id = " + idString);
            Thread.sleep(200);
        }
    }

    public static void checkUserHaveRestrictionGeneral(String ucid, int restrictionId, String expectedStatus)
            throws Exception {
        Allure.step("check user have general restriction in Mitigation DataBase");
        List<ClientGeneralRestriction> restrictionList = getObjectsFromDB(POSTGRES, MITIGATION_CLIENT_GENERAL_RESTRICTION, "ucid = '" + ucid + "' and restriction_id = " + restrictionId, ClientGeneralRestriction.class);
        ClientGeneralRestriction restriction = restrictionList.getLast();
        assertEquals(ucid, restriction.getUcid());
        assertEquals(expectedStatus, restriction.getStatus());
    }

    public static void checkUserHaveRestrictionTrading(String ucid, int restrictionId)
            throws Exception {
        Allure.step("check user have trading restriction in Mitigation DataBase");
        List<ClientTradingRestriction> restrictionList = getObjectsFromDB(POSTGRES, MITIGATION_CLIENT_TRADING_RESTRICTION, "ucid = '" + ucid + "' and restriction_id = " + restrictionId, ClientTradingRestriction.class);
        ClientTradingRestriction restriction = restrictionList.getLast();
        assertEquals(ucid, restriction.getUcid());
    }

    @Deprecated
    @Step("Clean users audit history")
    public void cleanUserAudit(String ucid) throws Exception {
        deleteEntryFromDb(POSTGRES, AUDIT_EVENT, "ucid = '" + ucid + "'");
        Thread.sleep(200);
    }

    @Deprecated
    public static void setRestrictionAPIGeneral(String ucid, String code, String applyReason, String updatedBySystem,
            String updatedByUser) throws IOException {
        Allure.step("Set restriction through API GENERAL");
        PostRestrictionRequestBody postRestrictionRequestBody = new PostRestrictionRequestBody(
                ucid, code, "GENERAL", null, null, applyReason, new PostRestrictionRequestBody.UpdatedBy(updatedBySystem, updatedByUser)
        );
        Response response = postRestriction(postRestrictionRequestBody);
        assertNotNull(response);
        assertEquals(200, response.code());
    }

    @Deprecated
    public static String setRestrictionAPIGeneralResponse(String ucid, String code, String applyReason,
            String updatedBySystem,
            String updatedByUser) throws IOException {
        Allure.step("Set restriction through API GENERAL");
        PostRestrictionRequestBody postRestrictionRequestBody = new PostRestrictionRequestBody(
                ucid, code, "GENERAL", null, null, applyReason, new PostRestrictionRequestBody.UpdatedBy(updatedBySystem, updatedByUser)
        );
        Response response = postRestriction(postRestrictionRequestBody);
        assertNotNull(response);
        assertEquals(response.code(), 200);
        assert response.body() != null;
        return response.body().string();
    }

    @Step("Set restriction through API")
    public static void setRestrictionAPIGeneral(String ucid, String code) throws IOException {
        Allure.step("Set restriction through API GENERAL");
        PostRestrictionRequestBody postRestrictionRequestBody = new PostRestrictionRequestBody(
                ucid, code, "GENERAL", null, null, "Integration test", new PostRestrictionRequestBody.UpdatedBy("test", "automation")
        );
        Response response = postRestriction(postRestrictionRequestBody);
        assertEquals(200, response.code());
    }

    @Step
    public void setRestrictionAPITrade(String ucid, int accId, int serverId, String code) throws IOException {
        Allure.step("Set restriction through API TRADE");
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
        Allure.step("Set restriction through API TRADE");
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
        List<Event> event = getObjectsFromDB(POSTGRES, AUDIT_EVENT, "ucid = '" + ucid + "'", Event.class);
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
        List<Event> event = getObjectsFromDB(POSTGRES, AUDIT_EVENT, "ucid = '" + ucid + "' and type = '" + type + "' AND details = '" + expectedDetails + "'", Event.class);
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
        List<Event> event = getObjectsFromDB(POSTGRES, AUDIT_EVENT, "ucid = '" + ucid + "'", Event.class);
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
            events = getObjectsFromDB(POSTGRES, AUDIT_EVENT, "ucid = '" + ucid + "' ORDER BY created_at ASC", Event.class);
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
            events = getObjectsFromDB(POSTGRES, AUDIT_EVENT, "ucid = '" + ucid + "' ORDER BY created_at ASC", Event.class);
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

    public void isRestrictionTabHidden() {
        Allure.step("check is restriction tab hidden");
        restrictionTab.waitFor(new Locator.WaitForOptions().setState(HIDDEN));
    }

    public void isRestrictionTabVisible() {
        Allure.step("check is restriction tab visible");
        restrictionTab.waitFor(new Locator.WaitForOptions().setState(VISIBLE));
    }

}

