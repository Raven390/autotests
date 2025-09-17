package tests.vindex_backoffice_ui_tests.investigationTool;

import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import helpers.data.ClientHelper;
import helpers.data.enums.FraudTypeOld;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBaseWeb;


import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.BoHelper.deleteUserBO;
import static helpers.database.CleanTableHelper.cleanUserAudit;
import static helpers.database.DbHelper.insertObjectToDb;
import static helpers.kafka.alerts.CreateSimpleAlert.createSimpleAlert;
import static utils.Constants.*;

@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_WEB)
@Feature("BMS-1453 Actions by AF team")
public class ActionsByAfTeamTest extends TestBaseWeb {

    private static final ClientHelper client = getRandomVantageClientAllFields();

    @BeforeAll
    static void setup() {
        CrmTbUserObject resolveClientDB = generateUserByClient(client);
        insertObjectToDb(CRM_USER_TABLE_NAME, resolveClientDB);
    }


    @Test
    @AllureId("1324")
    @Feature("BMS-1453 Actions by AF team")
    @DisplayName("BO user with AF role can't assign suspicious client with the active alert to himself to perform investigation from the client card")
    public void assignClientCardTest() throws Exception {

        deleteUserBO(client.getUcid());
        cleanUserAudit(client.getUcid());
        createSimpleAlert(client.getUcid(), FraudTypeOld.CPA_ABUSE.getKey());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAFUser();
        investigationPage.navigateToClient(client.getUcid());
        investigationPage.cantInvestigateClientCard();
    }

    @Test
    @AllureId("1325")
    @Feature("BMS-1453 Actions by AF team")
    @DisplayName("AF user can't comment client")
    public void commentOutsideResolveTest() throws Exception {
        deleteUserBO(client.getUcid());
        cleanUserAudit(client.getUcid());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAFUser();
        investigationPage.navigateToClient(client.getUcid());
        investigationPage.cantOpenCommentForm();

    }

    @Test
    @DisplayName("AF user can't report fraud")
    @AllureId("1325")
    @Feature("BMS-1453 Actions by AF team")
    public void reportFraudTestHedging() {
        resolvePage.navigateEnterPage();
        keycloackPage.loginAsAFUser();
        investigationPage.navigateToClient(client.getUcid());
        resolvePage.cantOpenReportFraudForm();
    }

    @Test
    @AllureId("1327")
    @DisplayName("Restriction tab AF user can't set  restriction UI")
    void setAccountRestrictionUITest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAFUser();
        restrictionPage.navigate(client.getUcid());
        restrictionPage.cantAddNewRestriction();
    }

}
