package tests.vindex_backoffice_ui_tests.investigationTool;

import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import helpers.data.ClientHelper;
import helpers.data.enums.FraudType;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.io.IOException;
import java.util.List;

import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static helpers.api.AbuseRegistryHelper.addFraudForClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.FraudType.*;
import static helpers.data.enums.FraudTypeStatus.CONFIRMED;
import static helpers.data.enums.FraudTypeStatus.POTENTIAL;
import static helpers.database.ArHelper.deleteUserFromAbuseRegistry;
import static helpers.database.BoHelper.deleteUserBO;
import static helpers.database.DbHelper.*;
import static helpers.database.DbHelper.deleteEntryFromDb;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static page_objects.backoffice_pages.investigationTool.RestrictionPage.cleanUserRestriction;
import static utils.Constants.*;

@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_WEB)
class FraudManagingTest extends TestBaseWeb {

    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(client);


    @BeforeAll
    static void setup() throws IOException {
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
    }

    @AfterAll
    static void teardown() throws Exception {
        deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteUserFromAbuseRegistry(client.getUcid());
        deleteUserBO(client.getUcid());
        cleanUserRestriction(client.getUcid());
    }

    @AllureId("1504")
    @DisplayName("Fraud types that was separately added as potential and confirmed must be shown only as confirmed on FE")
    @Test
    void overridedPotentialNotShown() throws IOException {

        FraudType fraudType = getRandomFraudType();

        Allure.step("report one fraud type to the client two times, first as potential, second as confirmed");
        addFraudForClient(client, fraudType, POTENTIAL, List.of());
        page.waitForTimeout(500);
        addFraudForClient(client, fraudType, CONFIRMED, List.of());
        page.waitForTimeout(500);

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        resolvePage.openReportFraudForm();
        List<List<String>> frauds = resolvePage.getPreviouslyReportedFraudItems();
        Allure.step("check that there only one fraud type displayed");
        assertEquals(1, frauds.size());
        Allure.step("check that name of the fraud is that that we reported on client");
        assertEquals(fraudType.getName(), frauds.getFirst().getFirst());
    }
}
