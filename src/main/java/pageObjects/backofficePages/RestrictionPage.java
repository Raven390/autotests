package pageObjects.backofficePages;

import businessObjects.api.mitigationService.PostRestrictionRequestBody;
import businessObjects.db.mitigationServiceDB.ClientsRestriction;
import businessObjects.kafka.restrictionEvents.ClientRestrictionApply;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import helpers.database.DbName;
import helpers.kafka.KafkaHelper;
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
    private final Locator cancelToast;
    private final Locator checkedAccount;
    private final Locator checkedTransfer;


    public RestrictionPage(Page page) {
        this.page = page;
        this.restrictionTab = page.locator("[role=\"tab\"][title=\"Restrictions\"]");
        this.accountSwitch = page.locator(".v-restrictions-tab-item__name").getByText("Open new account");
        this.transferSwitch = page.locator(".v-restrictions-tab-item__switch").nth(1);
        this.depositsSwitch = page.locator(".v-restrictions-tab-item__switch").nth(2);
        this.withdrawalsSwitch = page.locator(".v-restrictions-tab-item__switch").nth(3);
        this.loginSwitch = page.locator(".v-restrictions-tab-item__switch").nth(4);
        this.manualSwitch = page.locator(".v-restrictions-tab-item__switch").nth(5);
        this.closeSwitch = page.locator(".v-restrictions-tab-item__switch").nth(6);
        this.offQuotesSwitch = page.locator(".v-restrictions-tab-item__switch").nth(7);
        this.abBookSwitch = page.locator(".v-restrictions-tab-item__switch").nth(8);
        this.header1 = page.locator(".g-text_variant_header-1");
        this.header2 = page.locator(".g-text_variant_header-2");
        this.dialog = page.locator(".v-restriction-tab-general-modal");
        this.reasonInput = page.locator("textarea[placeholder=\"Reason (discovered fraud type, etc.)\"]");
        this.restrictionSetCancel = page.locator("v-common-modal__buttons").getByText("Cancel");
        this.restrictionSetSet = page.locator(".v-common-modal__buttons").getByText("Set");
        this.setToast = page.locator(".g-toast__title").getByText("Restriction was set");
        this.restrictionCancelCancel = page.locator("v-common-modal__buttons").getByText("Cancel");
        this.restrictionCancelSet = page.locator(".v-common-modal__buttons").getByText("Remove");
        this.cancelToast = page.locator(".g-toast__title").getByText("Restriction was removed");
        this.checkedAccount = page.locator(".v-restrictions-tab-item_checked").getByText("Open new account");
        this.checkedTransfer = page.locator(".v-restrictions-tab-item_checked").getByText("Internal transfer");
    }

    @Step("open users restriction tab")
    public void navigate() {
        page.navigate("http://k8s-test-nginxrev-55e209d446-410128713.us-east-1.elb.amazonaws.com/investigation?client_ucid=vantage-10081449&alerts_status=OPEN");
        restrictionTab.click();
    }

    @Step("check that restriction tab rendered properly")
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

    @Step("set account restriction")
    public void clickAccountSwitch() throws InterruptedException, JsonProcessingException {
        accountSwitch.click();
    }

    @Step("set account restriction")
    public void clickTransferSwitch() throws InterruptedException, JsonProcessingException {
        transferSwitch.click();
    }

    @Step("check that account restruction tumbler is checked")
    public void checkThatAccountIsChecked() throws InterruptedException, JsonProcessingException {
        checkedAccount.isVisible();
    }

    @Step("check that Transfer restruction tumbler is checked")
    public void checkThatTransferIsChecked() throws InterruptedException, JsonProcessingException {
        checkedTransfer.isVisible();
    }

    @Step("click checked account tumbler")
    public void clickCheckedAccount() throws InterruptedException, JsonProcessingException {
        checkedAccount.click();
    }

    @Step("click checked transfer tumbler")
    public void clickCheckedTransfer() throws InterruptedException, JsonProcessingException {
        checkedTransfer.click();
    }

    @Step("fill apply reason")
    public void fillApplyReason(String reason) throws InterruptedException, JsonProcessingException {
        dialog.isVisible();
        reasonInput.fill(reason);
        restrictionSetSet.click();
        setToast.isVisible();
    }

    @Step("fill cancel reason")
    public void fillCancelReason(String reason) throws InterruptedException, JsonProcessingException {
        dialog.isVisible();
        reasonInput.fill(reason);
        restrictionCancelSet.click();
        cancelToast.isVisible();
    }

    @Step("check request to apply message")
    public void checkKafkaRequestApplyUCID(String userId) throws InterruptedException, JsonProcessingException {
        KafkaHelper helper = new KafkaHelper();
        String kafkaResponse = helper.consumeMessage("client.restrictions.apply", "10081449");
        ObjectMapper objectMapper = new ObjectMapper();
        ClientRestrictionApply apply = objectMapper.readValue(kafkaResponse, ClientRestrictionApply.class);
        apply.clientId.equals(userId);
        assertNotNull((apply.clientId));
        assertNotNull((apply.timestamp));
        assertNotNull((apply.messageId));
        assertNotNull((apply.regulator));
        assertNotNull((apply.restrictions));
    }

    @Step
    public void checkKafkaRequestApplyUCIDID(String userId, String Id) throws InterruptedException, JsonProcessingException {
        KafkaHelper helper = new KafkaHelper();
        String kafkaResponse = helper.consumeMessage("client.restrictions.apply", "10081449");
        ObjectMapper objectMapper = new ObjectMapper();
        ClientRestrictionApply apply = objectMapper.readValue(kafkaResponse, ClientRestrictionApply.class);
        apply.clientId.equals(userId);
        assertNotNull((apply.clientId));
        assertNotNull((apply.timestamp));
        assertNotNull((apply.messageId));
        assertNotNull((apply.regulator));
        assertNotNull((apply.restrictions));
    }

    @Step("clean users restriction history")
    public void cleanUserRestriction(String ucid) throws Exception {
        List<ClientsRestriction> restrictionList = getObjectsFromDB(DbName.MITIGATION_POSTGRES, "clients_restriction", "ucid = '"+ ucid+"'", ClientsRestriction.class);
        if(restrictionList == null){
            System.out.println("restrictionList is null");
        }else{
            for (ClientsRestriction i : restrictionList) {
                String Id = i.getId().toString();
                deleteEntryFromDb(DbName.MITIGATION_POSTGRES, "mi.mi.action","clients_restriction_id = " +Id);
                Thread.sleep(100);
                deleteEntryFromDb(DbName.MITIGATION_POSTGRES, "mi.mi.kafka_request","clients_restriction_id = "+Id);
                Thread.sleep(100);
                deleteEntryFromDb(DbName.MITIGATION_POSTGRES, "mi.mi.kafka_response","clients_restriction_id = "+Id);
                Thread.sleep(100);
                deleteEntryFromDb(DbName.MITIGATION_POSTGRES, "mi.mi.clients_restriction","id = "+Id);
                Thread.sleep(100);
            }
        }
    }

    @Step("clean users audit history")
    public void cleanUserAudit(String ucid) throws Exception {
        deleteEntryFromDb(DbName.AUDIT, "au.au.event","ucid = '"+ ucid+"'");
    }

    @Step
    public void setRestrictionAPIGeneral (String ucid, String code) throws IOException {
        PostRestrictionRequestBody postRestrictionRequestBody = new PostRestrictionRequestBody(
                ucid,
                code,
                "GENERAL",
                null,
                null,
                "Integration test",
                new PostRestrictionRequestBody.UpdatedBy("string", "string")
        );
        Response response = postRestriction(postRestrictionRequestBody);
        assertNotNull(response);
    }


}

