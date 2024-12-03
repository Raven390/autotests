package tests.vindexBackofficeUiTests;

import helpers.kafka.alerts.CreateSimpleAlert;
import io.qameta.allure.AllureId;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBaseWeb;

import static helpers.database.AuditHelper.cleanUserAudit;
import static utils.Constants.LAYER_WEB;
import static utils.Constants.TEAM_BACKOFFICE;

public class ResolveTest extends TestBaseWeb {

    @Test
    @Owner("DMITRI KALACHEV")
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("432")
    @DisplayName("resolve client with withdrawal transactions approve all")
    public void resolveWithWithdrawalsApproveAll() throws Exception {
        cleanUserAudit("infinox-141402");
        CreateSimpleAlert.createSimpleAlert("infinox-141402", "CPA");
        investigationPage.navigate();
        keycloackPage.loginWEB("dev", "123");
        investigationPage.navigateToClient("infinox-141402");
        resolveScreen.openResolveSuspicious();
        resolveScreen.resolveWithdrawalsAllApprove();
        String details = "Transaction ID 141402; 5.00 USD 2024-11-13 10:11 crypto; Accept";
        restrictionPage.checkRestrictionCancellationAudit("infinox-141402", "WD_REQUEST_DECISION", details);
        restrictionPage.checkKafkaRequestWithdrawal("141402", "5");

    }

    @Test
    @Owner("DMITRI KALACHEV")
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("432")
    @DisplayName("resolve client with withdrawal transactions reject all")
    public void resolveWithWithdrawalsRejectAll() throws Exception {
        cleanUserAudit("infinox-141402");
        CreateSimpleAlert.createSimpleAlert("infinox-141402", "CPA");
        investigationPage.navigate();
        keycloackPage.loginWEB("dev", "123");
        investigationPage.navigateToClient("infinox-141402");
        resolveScreen.openResolveSuspicious();
        resolveScreen.resolveWithdrawalsAllReject();
        String details = "Transaction ID 141404; 71.00 USDT 2024-11-13 10:11 bank trasfer; Refuse";
        restrictionPage.checkRestrictionCancellationAudit("infinox-141402", "WD_REQUEST_DECISION", details);
        restrictionPage.checkKafkaRequestWithdrawal("141401", "4");
    }

    @Test
    @Owner("DMITRI KALACHEV")
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("432")
    @DisplayName("resolve client with withdrawal transactions approve one")
    public void resolveWithWithdrawalsApproveOne() throws Exception {
        investigationPage.navigate();
        keycloackPage.loginWEB("dev", "123");
        //first run
        cleanUserAudit("infinox-141402");
        CreateSimpleAlert.createSimpleAlert("infinox-141402", "CPA");
        investigationPage.navigateToClient("infinox-141402");
        resolveScreen.openResolveSuspicious();
        resolveScreen.resolveWithdrawalsApproveFirst();
        String details1 = "Transaction ID 141404; 71.00 USDT 2024-11-13 10:11 bank trasfer; Refuse";
        restrictionPage.checkRestrictionCancellationAudit("infinox-141402", "WD_REQUEST_DECISION", details1);
        restrictionPage.checkKafkaRequestWithdrawal("141402", "4");
        //second run
        cleanUserAudit("infinox-141402");
        CreateSimpleAlert.createSimpleAlert("infinox-141402", "CPA");
        investigationPage.navigateToClient("infinox-141402");
        resolveScreen.openResolveSuspicious();
        resolveScreen.resolveWithdrawalsApproveFirst();
        String details2 = "Transaction ID 141401; 5.00 USD 2024-11-13 10:11 bank card; Accept";
        restrictionPage.checkRestrictionCancellationAudit("infinox-141402", "WD_REQUEST_DECISION", details2);
        restrictionPage.checkKafkaRequestWithdrawal("141401", "5");
    }
}
