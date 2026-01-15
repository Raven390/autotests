package page_objects.backoffice_pages.investigationTool;

import static com.microsoft.playwright.options.WaitForSelectorState.*;
import static helpers.database.DbHelper.deleteObjectFromDb;
import static helpers.database.DbHelper.getObjectsFromDB;
import static helpers.database.DbName.POSTGRES;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static utils.ConfigFactory.BASE_URL_E2E;
import static utils.Constants.*;
import static utils.Utils.writeLog;

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
import java.util.ArrayList;
import java.util.List;
import page_objects.backoffice_pages.AbstractPage;

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
    private final Locator worseTradingEmptyRestrictionsButton;
    private final Locator worseTradingAppliedRestrictionsButton;
    private final Locator worseTradingTabApplyButton;

    private static final String RESTRICTION_ITEM_BY_NAME_PATTERN =
            "//div[contains(@class,'v-restrictions-tab-item__name') and text()='%s']";
    private static final String RESTRICTIONS_TAB_ITEM_NAME = ".v-restrictions-tab-item__name";
    private static final String RESTRICTIONS_TAB_ITEM_CHECKED = ".v-restrictions-tab-item_checked";
    private static final String RESTRICTIONS_TAB_ITEM_HEADER = ".v-restrictions-tab-item__header";
    private static final String CHECKED_RESTRICTION =
            String.format("%s %s", RESTRICTIONS_TAB_ITEM_CHECKED, RESTRICTIONS_TAB_ITEM_HEADER);
    private static final String RESTRICTION_OPTION_PATTERN =
            "//span[@class='g-select-list__option-default-label' and text()='%s']";
    private static final String RESTRICTION_TAB_ITEM_BY_NAME =
            "//div[contains(@class,'v-restrictions-tab-item__name') and text()='%s']/ancestor::div[@class='v-restrictions-tab-item']";
    private static final String ACTIVE_RESTRICTION_BY_NAME =
            "//div[text()='%s']/ancestor::div[@class='v-client-restrictions-list-item']";
    private static final String CLEAR_RESTRICTION_BUTTON_BY_NAME =
            ACTIVE_RESTRICTION_BY_NAME + "/descendant::button[contains(@data-qa,'control__remove')]";
    private static final String RESTRICTION_ACCOUNT_SELECTION_BUTTON_BY_NAME =
            ACTIVE_RESTRICTION_BY_NAME + "/descendant::span[contains(text(),'account')]/ancestor::button";
    private static final String WT_ACCOUNT_ROW_BY_ACCOUNT_ID_PATTERN =
            "//*[@data-qa='restrictions__wt_drawer__account__%s']";
    private static final String WT_ACCOUNT_LEVEL_BUTTON_IN_ROW =
            "//*[@data-qa='restrictions__wt_drawer__account__level']";
    private static final String WT_LEVEL_OPTION_BY_LABEL_PATTERN =
            "//*[@data-qa='select-popup']//span[contains(@class,'g-select-list__option-default-label') and normalize-space(text())='%s']";
    private static final String WT_DRAWER_COMMENT_TEXTAREA =
            "//*[@data-qa='restrictions__wt_drawer__comment']//textarea";
    private static final String RESTRICTIONS_LIST_CONTAINER = "//*[@data-qa='restrictions__list']";
    private static final String WORSE_TRADING_RESTRICTION_IN_LIST = RESTRICTIONS_LIST_CONTAINER
            + "//*[contains(@class,'v-trading-env-restrictions-item__name') and normalize-space(text())='Worse trading']";
    private static final String WT_ACCOUNT_LEVEL_TEXT_IN_ROW =
            "button[data-qa='restrictions__wt_drawer__account__level'] .g-select-control__option-text";

    public RestrictionPage(Page page) {
        super(page);
        this.loaderAnimation = page.locator(".v-loader");
        this.loaderSpin = page.locator(".g-spin").first();
        this.restrictionTab = page.locator("[role=\"tab\"][title=\"Restrictions\"]");
        this.activitySection = page.locator(".v-accounts-list-item__activity");
        this.tooltip = page.locator(".g-tooltip__content");
        this.openRestrictionsDrawerButton =
                page.locator("//span[text()='Apply restrictions' or text()=' Manage restrictions']");
        this.addRestrictionButton = page.locator("//div[@class='v-list-select']/descendant::button");
        this.applyRestrictionButton = page.locator("//span[text()='Apply']/parent::button[not(@disabled)]");
        this.commentInput = page.locator("//textarea");
        this.applyChangesButton = page.locator(
                "//div[@class='v-restrictions-tab-drawer__footer']/descendant::span[text()='Apply changes' or text()='Apply']/parent::button");
        this.restrictionAppliedIcon = page.locator("//div[@class='v-restrictions-tab-item__status']/*[not(@class)]");
        this.restrictionAppliedBy = page.locator("//span[contains(@class,'v-restrictions-tab-item__actor')]");
        this.restrictionAppliedComment = page.locator("//span[contains(@class,'v-restrictions-tab-item__comment')]");
        this.restrictionAppliedDate = page.locator("//span[contains(@class,'v-restrictions-tab-item__date')]");
        this.restrictionTabLoaded = page.locator("//div[@class='v-investigation-tools-tabs__content']");
        this.inactiveAccountLabel = page.locator(
                        "//div[@class='v-accounts-list-item__labels']/descendant::div[text()='Inactive']")
                .first();
        this.restrictionOption = page.locator("//span[@class='g-select-list__option-default-label']");
        this.restrictionOptionsContainer = page.locator("//div[@class='v-list-select__list-container']");
        this.worseTradingAppliedRestrictionsButton =
                page.locator("//button[@data-qa='restrictions__manage_wt_button']");
        this.worseTradingEmptyRestrictionsButton =
                page.locator("//button[@data-qa='restrictions__empty_view__open_wt_drawer']");
        this.worseTradingTabApplyButton = page.locator("//button[@data-qa='restrictions__wt_drawer__submit']");
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

    public static void checkKafkaRequestApplyUserId(int userIdInt)
            throws JsonProcessingException, InterruptedException {
        Allure.step("Check request message for apply cancellation for client in kafka");
        String userId = String.valueOf(userIdInt);
        Thread.sleep(7000);
        writeLog("we search user " + userId);
        KafkaHelper helper = new KafkaHelper();
        List<String> kafkaResponses = helper.consumeMessages("client.restrictions.apply", userId);
        for (String response : kafkaResponses) {
            writeLog(response);
        }
        String kafkaResponse = kafkaResponses.getLast();
        writeLog("tested message is " + kafkaResponse);
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
            writeLog(response);
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

    public void checkKafkaRequestApplyAccount(int accountIdInt, int banDurationMin)
            throws JsonProcessingException, InterruptedException {
        Allure.step("Check request message for restriction apply for account in kafka");
        String accoundId = String.valueOf(accountIdInt);
        KafkaHelper helper = new KafkaHelper();
        List<String> kafkaResponses = helper.consumeMessages(KAFKA_TOPIC_ACCOUNT_RESTRICTIONS_APPLY, accoundId);
        for (String response : kafkaResponses) {
            writeLog(response);
        }
        String kafkaResponse = kafkaResponses.getLast();
        writeLog("tested message is " + kafkaResponse);
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

    public static void checkKafkaRequestApplyAccount(
            int accountIdInt,
            int serverId,
            int banDurationMin,
            int restrictionId,
            String reason,
            String restrictionCode)
            throws JsonProcessingException, InterruptedException {
        Allure.step("Check request message for restriction apply for account in kafka");
        String accountId = String.valueOf(accountIdInt);
        KafkaHelper helper = new KafkaHelper();
        List<String> kafkaResponses =
                helper.consumeMessages(KAFKA_TOPIC_ACCOUNT_RESTRICTIONS_APPLY, String.valueOf(restrictionId));
        String kafkaResponse = kafkaResponses.getLast();
        writeLog("Tested message is " + kafkaResponse);
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

    public static void checkKafkaRequestApplyTradingEnv(ApplyTradingEnvironmentRestrictionMessage expected)
            throws JsonProcessingException, InterruptedException {
        Allure.step("Check request message for trading env restriction apply for account in kafka");
        String accountId = String.valueOf(expected.getAccountId());
        KafkaHelper helper = new KafkaHelper();
        List<String> kafkaResponses =
                helper.consumeMessages(KAFKA_TOPIC_TRADING_ENV_RESTRICTIONS_APPLY, String.valueOf(accountId));
        String kafkaResponse = kafkaResponses.getLast();
        writeLog("Tested message is " + kafkaResponse);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        ApplyTradingEnvironmentRestrictionMessage actual =
                objectMapper.readValue(kafkaResponse, ApplyTradingEnvironmentRestrictionMessage.class);
        assertNotNull(actual);
        assertNotNull(actual.getRestriction());
        assertNotNull(actual.getTimestamp());
        assertNotNull(actual.getMessageId());
        assertNotNull(actual.getModifier());
        expected.getRestriction().setRestrictionId(actual.getRestriction().getRestrictionId());
        expected.setTimestamp(actual.getTimestamp());
        expected.setMessageId(actual.getMessageId());
        expected.setModifier(actual.getModifier());
        assertEquals(expected, actual);
    }

    public void checkKafkaRequestCancelAccount(int accoundIdInt) throws JsonProcessingException, InterruptedException {
        Allure.step("Check request message for restriction cancellation for account in kafka");
        String accoundId = String.valueOf(accoundIdInt);
        KafkaHelper helper = new KafkaHelper();
        List<String> kafkaResponses = helper.consumeMessages(KAFKA_TOPIC_ACCOUNT_RESTRICTIONS_CANCEL, accoundId);
        String kafkaResponse = kafkaResponses.getLast();
        writeLog("Tested message is " + kafkaResponse);
        ObjectMapper objectMapper = new ObjectMapper();
        AccountRestrictionCancel cancel = objectMapper.readValue(kafkaResponse, AccountRestrictionCancel.class);
        assertNotNull((cancel.getAccountId()));
        assertNotNull((cancel.getTimestamp()));
        assertNotNull((cancel.getMessageId()));
        assertNotNull((cancel.getServerId()));
        assertNotNull((cancel.getModifier()));
        assertNotNull((cancel.getRestrictions()));
    }

    public void checkKafkaRequestWithdrawal(String transactionID, String expectedStatus)
            throws InterruptedException, JsonProcessingException {
        Allure.step("Check withdrawal approval message");
        writeLog("we search transaction " + transactionID);
        KafkaHelper helper = new KafkaHelper();
        List<String> kafkaResponses = helper.consumeMessages("withdrawal.approvals", transactionID);
        for (String response : kafkaResponses) {
            writeLog(response);
        }
        String kafkaResponse = kafkaResponses.getLast();
        writeLog("tested message is " + kafkaResponse);
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
        List<ClientGeneralRestriction> restrictionList = getObjectsFromDB(
                POSTGRES,
                MITIGATION_CLIENT_GENERAL_RESTRICTION,
                "ucid = '" + ucid + "'",
                ClientGeneralRestriction.class);
        for (ClientGeneralRestriction i : restrictionList) {
            String idString = i.getId().toString();
            deleteObjectFromDb(
                    POSTGRES, MITIGATION_CLIENT_GENERAL_RESTRICTION_ACTION, "client_restriction_id = " + idString);
            deleteObjectFromDb(POSTGRES, MITIGATION_KAFKA_REQUEST_GENERAL, "client_restriction_id = " + idString);
            deleteObjectFromDb(POSTGRES, MITIGATION_KAFKA_RESPONSE_GENERAL, "client_restriction_id = " + idString);
            deleteObjectFromDb(POSTGRES, MITIGATION_CLIENT_GENERAL_RESTRICTION, "id = " + idString);
        }
    }

    public static void checkUserHaveRestrictionGeneral(String ucid, int restrictionId, String expectedStatus)
            throws Exception {
        Allure.step("check user have general restriction in Mitigation DataBase");
        List<ClientGeneralRestriction> restrictionList = getObjectsFromDB(
                POSTGRES,
                MITIGATION_CLIENT_GENERAL_RESTRICTION,
                "ucid = '" + ucid + "' and restriction_id = " + restrictionId,
                ClientGeneralRestriction.class);
        ClientGeneralRestriction restriction = restrictionList.getLast();
        assertEquals(ucid, restriction.getUcid());
        assertEquals(expectedStatus, restriction.getStatus());
    }

    public static void checkUserHaveRestrictionTrading(String ucid, int restrictionId) throws Exception {
        Allure.step("check user have trading restriction in Mitigation DataBase");
        List<ClientTradingRestriction> restrictionList = getObjectsFromDB(
                POSTGRES,
                MITIGATION_CLIENT_TRADING_RESTRICTION,
                "ucid = '" + ucid + "' and restriction_id = " + restrictionId,
                ClientTradingRestriction.class);
        ClientTradingRestriction restriction = restrictionList.getLast();
        assertEquals(ucid, restriction.getUcid());
    }

    @Deprecated
    @Step("Clean users audit history")
    public void cleanUserAudit(String ucid) throws Exception {
        deleteObjectFromDb(POSTGRES, AUDIT_EVENT_OLD, "ucid = '" + ucid + "'");
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
        page.locator(String.format(RESTRICTION_OPTION_PATTERN, restriction.getName()))
                .click();
        applyRestrictionButton.click();
        commentInput.fill(comment);
        applyChangesButton.click();
    }

    @Step("Open worse trading applied restrictions tab")
    public void openWorseTradingAppliedRestrictionsTab() {
        worseTradingAppliedRestrictionsButton.click();
    }

    @Step("Open worse trading empty restrictions tab")
    public void openWorseTradingEmptyRestrictionsTab() {
        worseTradingEmptyRestrictionsButton.click();
    }

    @Step("Set WT level '{levelLabel}' for account '{accountId}' in WT drawer")
    public void setWorseTradingLevelForAccount(String accountId, String levelLabel) {
        Locator row = page.locator(String.format(WT_ACCOUNT_ROW_BY_ACCOUNT_ID_PATTERN, accountId));
        row.waitFor(new Locator.WaitForOptions().setState(VISIBLE));

        Locator levelButton = row.locator(WT_ACCOUNT_LEVEL_BUTTON_IN_ROW);
        levelButton.waitFor(new Locator.WaitForOptions().setState(VISIBLE));
        levelButton.click();

        Locator popup = page.locator("[data-qa='select-popup']");
        popup.waitFor(new Locator.WaitForOptions().setState(VISIBLE));

        Locator option = page.locator(String.format(WT_LEVEL_OPTION_BY_LABEL_PATTERN, levelLabel));
        option.waitFor(new Locator.WaitForOptions().setState(VISIBLE));
        option.click();

        popup.waitFor(new Locator.WaitForOptions().setState(HIDDEN));
    }

    @Step("Get WT current level for account '{accountId}' in WT drawer")
    public String getWorseTradingLevelForAccount(String accountId) {
        Locator row = page.locator(String.format(WT_ACCOUNT_ROW_BY_ACCOUNT_ID_PATTERN, accountId));
        row.waitFor(new Locator.WaitForOptions().setState(VISIBLE));

        Locator levelText = row.locator(WT_ACCOUNT_LEVEL_TEXT_IN_ROW);
        levelText.waitFor(new Locator.WaitForOptions().setState(VISIBLE));

        return levelText.textContent().trim();
    }

    @Step("Apply worse trading with comment: {comment}")
    public void applyWorseTrading(String comment) {
        page.locator(WT_DRAWER_COMMENT_TEXTAREA).waitFor(new Locator.WaitForOptions().setState(VISIBLE));
        page.locator(WT_DRAWER_COMMENT_TEXTAREA).fill(comment);

        worseTradingTabApplyButton.click();
    }

    @Step("Apply worse trading")
    public void applyWorseTrading() {
        applyWorseTrading("Autotest comment");
    }

    @Step("Wait for 'Worse trading' restriction to appear in restrictions list (timeout {timeoutMs} ms)")
    public boolean waitForWorseTradingAppearRestrictionInList(int timeoutMs) {
        try {
            page.locator(WORSE_TRADING_RESTRICTION_IN_LIST)
                    .waitFor(new Locator.WaitForOptions().setState(VISIBLE).setTimeout(timeoutMs));
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    @Step("Wait for 'Worse trading' restriction to appear in restrictions list (timeout {timeoutMs} ms)")
    public boolean waitForWorseTradingDisappearRestrictionInList(int timeoutMs) {
        try {
            page.locator(WORSE_TRADING_RESTRICTION_IN_LIST)
                    .waitFor(new Locator.WaitForOptions().setState(HIDDEN).setTimeout(timeoutMs));
            return true;
        } catch (RuntimeException e) {
            return false;
        }
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
        assertThat(
                "Verify lock icon is visible",
                restrictionItem.locator(restrictionAppliedIcon).isVisible(),
                is(true));
        assertThat(
                "Verify applied by text",
                restrictionItem.locator(restrictionAppliedBy).textContent(),
                is(String.format("Set by %s %s", user.getFirstName(), user.getLastName())));
        assertThat(
                "Verify comment",
                restrictionItem.locator(restrictionAppliedComment).textContent(),
                is(String.format(" %s", comment)));
        assertThat(
                "Verify application date",
                restrictionItem.locator(restrictionAppliedDate).textContent(),
                matchesPattern("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}"));
    }

    @Step("Remove the provided restriction")
    public void removeRestriction(Restriction restriction, String comment) {
        openRestrictionsDrawerButton.click();
        page.locator(String.format(CLEAR_RESTRICTION_BUTTON_BY_NAME, restriction.getName()))
                .click();
        commentInput.fill(comment);
        applyChangesButton.click();
    }

    @Step("Verify restriction is not applied in UI")
    public void verifyRestrictionNotAppliedInUi(Restriction restriction) {
        page.locator(String.format(RESTRICTION_TAB_ITEM_BY_NAME, restriction.getName()))
                .waitFor(new Locator.WaitForOptions().setState(DETACHED));
    }

    @Step("Verify inactive label in account selection is visible")
    public void verifyInactiveLabelIsVisible(Restriction restriction) {
        openRestrictionsDrawerButton.click();
        addRestrictionButton.click();
        page.locator(String.format(RESTRICTION_OPTION_PATTERN, restriction.getName()))
                .click();
        applyRestrictionButton.click();
        page.locator(String.format(RESTRICTION_ACCOUNT_SELECTION_BUTTON_BY_NAME, restriction.getName()))
                .click();
        inactiveAccountLabel.waitFor(new Locator.WaitForOptions().setState(VISIBLE));
    }

    @Step("Get last activity tooltip text")
    public String getLastActivityTooltip(Restriction restriction) {
        openRestrictionsDrawerButton.click();
        addRestrictionButton.click();
        page.locator(String.format(RESTRICTION_OPTION_PATTERN, restriction.getName()))
                .click();
        applyRestrictionButton.click();
        page.locator(String.format(RESTRICTION_ACCOUNT_SELECTION_BUTTON_BY_NAME, restriction.getName()))
                .click();
        activitySection.last().hover();
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
