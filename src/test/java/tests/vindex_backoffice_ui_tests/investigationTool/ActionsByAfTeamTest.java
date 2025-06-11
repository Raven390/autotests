package tests.vindex_backoffice_ui_tests.investigationTool;

import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import helpers.data.enums.FraudTypeOld;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBaseWeb;


import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateStaticUserByClient;
import static helpers.database.BoHelper.deleteUserBO;
import static helpers.database.CleanTableHelper.cleanUserAudit;
import static helpers.database.DbHelper.insertObjectToDb;
import static helpers.kafka.alerts.CreateSimpleAlert.createSimpleAlert;
import static tests.vindex_backoffice_ui_tests.investigationTool.ResolveTest.resolveClient;
import static utils.Constants.*;

@Feature("BMS-1453 Actions by AF team")
public class ActionsByAfTeamTest extends TestBaseWeb {

    @BeforeAll
    static void setup() {
        CrmTbUserObject resolveClientDB = generateStaticUserByClient(resolveClient);
        insertObjectToDb(CRM_USER_TABLE_NAME, resolveClientDB);
    }


    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1324")
    @Feature("BMS-1453 Actions by AF team")
    @DisplayName("BO user with AF role can't assign suspicious client with the active alert to himself to perform investigation from the client card")
    public void assignClientCardTest() throws Exception {

        deleteUserBO(resolveClient.getUcid());
        cleanUserAudit(resolveClient.getUcid());
        createSimpleAlert(resolveClient.getUcid(), FraudTypeOld.CPA_ABUSE.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAFUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.cantInvestigateClientCard();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1325")
    @Feature("BMS-1453 Actions by AF team")
    @DisplayName("AF user can't comment client")
    public void commentOutsideResolveTest() throws Exception {
        deleteUserBO(resolveClient.getUcid());
        cleanUserAudit(resolveClient.getUcid());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAFUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        investigationPage.cantOpenCommentForm();

    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("AF user can't report fraud")
    @AllureId("1325")
    @Feature("BMS-1453 Actions by AF team")
    public void reportFraudTestHedging() throws Exception {
        resolvePage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(resolveClient.getUcid());
        resolvePage.cantOpenReportFraudForm();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1327")
    @DisplayName("Restriction tab AF user can't set  restriction UI")
    void setAccountRestrictionUITest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAFUser();
        restrictionPage.navigate(resolveClient.getUcid());
        restrictionPage.cantAddNewRestriction();
    }

}
