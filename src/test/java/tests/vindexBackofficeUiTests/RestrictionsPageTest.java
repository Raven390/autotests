package tests.vindexBackofficeUiTests;

import businessObjects.db.auditServiceDB.Event;
import helpers.database.DbName;
import io.qameta.allure.AllureId;
import io.qameta.allure.Owner;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.io.IOException;
import java.util.List;

import static businessObjects.api.mitigationService.MitigationServiceRequest.enableCRMEmulator;
import static helpers.database.DbHelper.getObjectsFromDB;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static utils.Constants.*;

public class RestrictionsPageTest  extends TestBaseWeb {

    @BeforeAll
    public static void CRMEmulation() throws IOException {
        Response response = enableCRMEmulator();
        assertNotNull(response);

    }

    @BeforeEach
    @Test
    public void before() throws Exception {
        restrictionPage.cleanUserRestriction("vantage-10081449");
        restrictionPage.cleanUserAudit("vantage-10081449");
    }

    @Test
    @Owner("DMITRI KALACHEV")
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("")
    @DisplayName("restriction tab set account restriction UI")
    void setAccountRestrictionUITest() throws Exception {
        investigationPage.navigate();
        keycloackPage.loginWEB("dev", "123");
        restrictionPage.navigate();
        restrictionPage.clickAccountSwitch();
        restrictionPage.fillApplyReason("test reason");
        restrictionPage.checkThatAccountIsChecked();
        restrictionPage.checkKafkaRequestApplyUCID("10081449");
        List<Event> event = getObjectsFromDB(DbName.AUDIT, "au.au.event", "ucid = 'vantage-10081449'", Event.class);
        String type1 = event.get(0).getType();
        assertEquals(type1, "RESTRICTION_REQUESTED");
        String details = event.get(0).getDetails();
        assertEquals(details, "Open new account");
        String type2 = event.get(1).getType();
        assertEquals(type2, "RESTRICTION_APPLIED");
        String system = event.get(0).getInitiatedBySystem();
        assertEquals(system, "vindex-backoffice");
    }

    @Test
    @Owner("DMITRI KALACHEV")
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("")
    @DisplayName("restriction tab remove account restriction UI")
    void cancelAccountRestrictionUITest() throws Exception {
        restrictionPage.setRestrictionAPIGeneral("vantage-10081449","01");
        investigationPage.navigate();
        keycloackPage.loginWEB("dev", "123");
        restrictionPage.navigate();
        restrictionPage.checkThatAccountIsChecked();
        restrictionPage.clickCheckedAccount();
        restrictionPage.fillCancelReason("test reason");

        restrictionPage.checkKafkaRequestApplyUCID("10081449");
        List<Event> event = getObjectsFromDB(DbName.AUDIT, "au.au.event", "ucid = 'vantage-10081449'", Event.class);
        String type1 = event.get(2).getType();
        assertEquals(type1, "CANCELLATION_REQUESTED");
        String details = event.get(2).getDetails();
        assertEquals(details, "Open new account");
        String type2 = event.get(3).getType();
        assertEquals(type2, "RESTRICTION_CANCELLED");
        String system = event.get(2).getInitiatedBySystem();
        assertEquals(system, "vindex-backoffice");
    }

    @Test
    @Owner("DMITRI KALACHEV")
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("")
    @DisplayName("restriction tab set transfer restriction UI")
    void setTransferRestrictionUITest() throws Exception {
        investigationPage.navigate();
        keycloackPage.loginWEB("dev", "123");
        restrictionPage.navigate();
        restrictionPage.clickTransferSwitch();
        restrictionPage.fillApplyReason("test reason");
        restrictionPage.checkThatTransferIsChecked();
        restrictionPage.checkKafkaRequestApplyUCID("10081449");
        List<Event> event = getObjectsFromDB(DbName.AUDIT, "au.au.event", "ucid = 'vantage-10081449'", Event.class);
        String type1 = event.get(0).getType();
        assertEquals(type1, "RESTRICTION_REQUESTED");
        String details = event.get(0).getDetails();
        assertEquals(details, "Internal transfer");
        String type2 = event.get(1).getType();
        assertEquals(type2, "RESTRICTION_APPLIED");
        String system = event.get(0).getInitiatedBySystem();
        assertEquals(system, "vindex-backoffice");
    }

    @Test
    @Owner("DMITRI KALACHEV")
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("")
    @DisplayName("restriction tab remove account restriction UI")
    void cancelTransferRestrictionUITest() throws Exception {
        restrictionPage.setRestrictionAPIGeneral("vantage-10081449","02");
        investigationPage.navigate();
        keycloackPage.loginWEB("dev", "123");
        restrictionPage.navigate();
        restrictionPage.checkThatTransferIsChecked();
        restrictionPage.clickCheckedTransfer();
        restrictionPage.fillCancelReason("test reason");

        restrictionPage.checkKafkaRequestApplyUCID("10081449");
        List<Event> event = getObjectsFromDB(DbName.AUDIT, "au.au.event", "ucid = 'vantage-10081449'", Event.class);
        String type1 = event.get(2).getType();
        assertEquals(type1, "CANCELLATION_REQUESTED");
        String details = event.get(2).getDetails();
        assertEquals(details, "Internal transfer");
        String type2 = event.get(3).getType();
        assertEquals(type2, "RESTRICTION_CANCELLED");
        String system = event.get(2).getInitiatedBySystem();
        assertEquals(system, "vindex-backoffice");
    }
}
