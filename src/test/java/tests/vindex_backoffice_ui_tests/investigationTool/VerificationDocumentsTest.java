package tests.vindex_backoffice_ui_tests.investigationTool;

import static business_objects.db.clickhouse.crm_tb_files.CrmTbFilesFactory.generateCrmTbFilesByClient;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static utils.Constants.*;

import business_objects.db.clickhouse.crm_tb_files.CrmTbFilesEntry;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import helpers.data.ClientHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_WEB)
@Feature("BMS-2162 View verification documents")
class VerificationDocumentsTest extends TestBaseWeb {

    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmUser = generateUserByClient(client);
    private static final CrmTbFilesEntry crmFiles1 = generateCrmTbFilesByClient(client);
    private static final CrmTbFilesEntry crmFiles2 = generateCrmTbFilesByClient(client);

    @BeforeAll
    static void setup() {
        insertObjectToDb(CRM_USER_TABLE_NAME, crmUser);
        crmFiles2.setFileName(CRM_FILES_EXAMPLE_NAME_OTHER);
        insertObjectsToDb(CRM_FILES_TABLE_NAME, List.of(crmFiles1, crmFiles2));
    }

    @Test
    @AllureId("1893")
    @DisplayName("Check verification files info in general tab")
    void testVerificationFiles() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab(client.getUcid());
        assertThat("Verify other documents section title", generalTab.getVerificationRowTitle(), is("Other documents"));
        assertThat(
                "Verify other documents section file count", generalTab.getVerificationRowFilesCount(), is("2 files"));
        generalTab.clickVerificationFilesRow();
        generalTab.waitForFilesToLoad();
        assertThat(
                "Verify verification drawer title",
                generalTab.getVerificationDrawerTitle(),
                is("Other attached documents"));
        assertThat(
                "Verify verification drawer subheader",
                generalTab.getVerificationDrawerSubheader(),
                is(String.format(
                        "%s %s, %s, %s", crmUser.firstName, crmUser.lastName, crmUser.birthday, crmUser.nationality)));
        assertThat(
                "Verify verification drawer file dates",
                generalTab.getVerificationDrawerFileDates(),
                contains(crmFiles1.getUploadTimeUtc().format(DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm"))));
        assertThat(
                "Verify verification drawer file count",
                generalTab.getVerificationDrawerFileNumbers(),
                contains("2 files"));
        generalTab.checkImageDisplayed();
        generalTab.clickFilePreviewByGroupAndFileIndex(0, 1);
        generalTab.waitForFilesToLoad();
        generalTab.checkImageDisplayed();
    }

    @AfterAll
    static void teardown() {
        deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteEntryFromDb(CRM_FILES_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
    }
}
