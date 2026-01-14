package tests.vindex_backoffice_ui_tests.investigationTool;

import static business_objects.db.clickhouse.crm_id_proof.CrmTbIdProofFactory.generateIdProofObjectByClient;
import static business_objects.db.clickhouse.crm_tb_address_proof.CrmTbAddressProofFactory.generateAddressProofObjectByClient;
import static business_objects.db.clickhouse.crm_tb_kyc_files.CrmTbKycFilesFactory.generateKycFilesObjectByClient;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.DbHelper.*;
import static utils.Constants.*;
import static utils.Utils.getCurrentTimestampMinusOffsetFormatted;

import business_objects.db.clickhouse.crm_id_proof.CrmTbIdProofObject;
import business_objects.db.clickhouse.crm_tb_address_proof.CrmTbAddressProofObject;
import business_objects.db.clickhouse.crm_tb_kyc_files.CrmTbKycFilesObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import helpers.data.ClientHelper;
import helpers.data.enums.DateTimeFormat;
import io.qameta.allure.AllureId;
import java.sql.SQLException;
import java.util.List;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_WEB)
class KYCTest extends TestBaseWeb {

    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final String DELETE_WHERE = String.format("ucid = '%s'", client.getUcid());
    private static final String POI = "Proof of identity";
    private static final String POA = "Proof of address";
    private static final String POF = "Proof of face";
    private static final int POF_FILE_TYPE_ID = 27;

    @BeforeAll
    static void setup() throws ReflectiveOperationException, SQLException {
        CrmTbUserObject pofClientDb = generateUserByClient(client);
        insertObjectToDb(CRM_USER_TABLE_NAME, pofClientDb);
    }

    @BeforeEach
    void beforeEach() {
        deleteObjectFromDb(CRM_TB_ID_PROOF_TABLE_NAME, DELETE_WHERE);
        deleteObjectFromDb(CRM_TB_ADDRESS_PROOF_TABLE_NAME, DELETE_WHERE);
        deleteObjectFromDb(CRM_TB_KYC_FILES_TABLE_NAME, DELETE_WHERE);
    }

    @AfterAll
    static void teardown() {
        deleteObjectFromDb(CRM_USER_TABLE_NAME, DELETE_WHERE);
        deleteObjectFromDb(CRM_TB_ID_PROOF_TABLE_NAME, DELETE_WHERE);
        deleteObjectFromDb(CRM_TB_ADDRESS_PROOF_TABLE_NAME, DELETE_WHERE);
        deleteObjectFromDb(CRM_TB_KYC_FILES_TABLE_NAME, DELETE_WHERE);
    }

    @Test
    @AllureId("351")
    @DisplayName("Check correct status display Submitted")
    void checkCorrectStatusDisplaySubmittedTest() {
        CrmTbKycFilesObject kycFile = generateKycFilesObjectByClient(client);
        CrmTbIdProofObject idProofObject = generateIdProofObjectByClient(client, kycFile);
        insertObjectToDb(CRM_TB_KYC_FILES_TABLE_NAME, kycFile);
        insertObjectToDb(CRM_TB_ID_PROOF_TABLE_NAME, idProofObject);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab(client.getUcid());
        generalTab.checkKycStatusGeneral(POI, "Submitted");
    }

    @Test
    @AllureId("354")
    @DisplayName("Check correct status display Rejected")
    void checkCorrectStatusDisplayRejectedTest() {
        CrmTbKycFilesObject kycFile = generateKycFilesObjectByClient(client);
        CrmTbIdProofObject idProofObject = generateIdProofObjectByClient(client, kycFile);
        idProofObject.setStatus("REJECTED");
        insertObjectToDb(CRM_TB_KYC_FILES_TABLE_NAME, kycFile);
        insertObjectToDb(CRM_TB_ID_PROOF_TABLE_NAME, idProofObject);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab(client.getUcid());
        generalTab.checkKycStatusGeneral(POI, "Rejected");
    }

    @Test
    @AllureId("352")
    @DisplayName("Check correct status display Pending")
    void checkCorrectStatusDisplayPendingTest() {
        CrmTbKycFilesObject kycFile = generateKycFilesObjectByClient(client);
        CrmTbIdProofObject idProofObject = generateIdProofObjectByClient(client, kycFile);
        idProofObject.setStatus("PENDING");
        insertObjectToDb(CRM_TB_KYC_FILES_TABLE_NAME, kycFile);
        insertObjectToDb(CRM_TB_ID_PROOF_TABLE_NAME, idProofObject);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab(client.getUcid());
        generalTab.checkKycStatusGeneral(POI, "PENDING");
    }

    @Test
    @AllureId("353")
    @DisplayName("Check correct status display Completed")
    void checkCorrectStatusDisplayCompletedTest() {
        CrmTbKycFilesObject kycFile = generateKycFilesObjectByClient(client);
        CrmTbIdProofObject idProofObject = generateIdProofObjectByClient(client, kycFile);
        idProofObject.setStatus("COMPLETED");
        insertObjectToDb(CRM_TB_KYC_FILES_TABLE_NAME, kycFile);
        insertObjectToDb(CRM_TB_ID_PROOF_TABLE_NAME, idProofObject);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab(client.getUcid());
        generalTab.checkKycStatusGeneral(POI, "Approved");
    }

    @Test
    @AllureId("357")
    @DisplayName("KYC File viewer BO user can zoom displayed file using buttons in UI")
    void userCanZoomTest() {
        CrmTbKycFilesObject kycFile = generateKycFilesObjectByClient(client);
        CrmTbIdProofObject idProofObject = generateIdProofObjectByClient(client, kycFile);
        insertObjectToDb(CRM_TB_KYC_FILES_TABLE_NAME, kycFile);
        insertObjectToDb(CRM_TB_ID_PROOF_TABLE_NAME, idProofObject);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab(client.getUcid());
        generalTab.kycDetailsOpen(POI);
        generalTab.FVZoomFunctions();
    }

    @Test
    @AllureId("358")
    @DisplayName("KYC File viewer BO user can rotate displayed file using buttons in UI")
    void userCanRotateTest() {
        CrmTbKycFilesObject kycFile = generateKycFilesObjectByClient(client);
        CrmTbIdProofObject idProofObject = generateIdProofObjectByClient(client, kycFile);
        insertObjectToDb(CRM_TB_KYC_FILES_TABLE_NAME, kycFile);
        insertObjectToDb(CRM_TB_ID_PROOF_TABLE_NAME, idProofObject);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab(client.getUcid());
        generalTab.kycDetailsOpen(POI);
        generalTab.FVRotateFunctions();
    }

    @Test
    @AllureId("359")
    @DisplayName("KYC File viewer BO user can mirror displayed file using buttons in UI")
    void userCanMirrorTest() {
        CrmTbKycFilesObject kycFile = generateKycFilesObjectByClient(client);
        CrmTbIdProofObject idProofObject = generateIdProofObjectByClient(client, kycFile);
        insertObjectToDb(CRM_TB_KYC_FILES_TABLE_NAME, kycFile);
        insertObjectToDb(CRM_TB_ID_PROOF_TABLE_NAME, idProofObject);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab(client.getUcid());
        generalTab.kycDetailsOpen(POI);
        generalTab.FVMirrorFunctions();
    }

    @Test
    @AllureId("356")
    @DisplayName("KYC File viewer BO user can slide displayed file using buttons in UI")
    void userCanSlideTest() {
        CrmTbKycFilesObject kycFile = generateKycFilesObjectByClient(client);
        CrmTbIdProofObject idProofObject = generateIdProofObjectByClient(client, kycFile);
        CrmTbKycFilesObject kycFile2 = generateKycFilesObjectByClient(client);
        kycFile2.setFileName("/other/5b14c35f65cb4eebb7f4e1f375049c85.jpeg");
        kycFile2.setFilePath(kycFile2.getFileName());
        CrmTbIdProofObject idProofObject2 = generateIdProofObjectByClient(client, kycFile2);
        insertObjectsToDb(CRM_TB_KYC_FILES_TABLE_NAME, List.of(kycFile, kycFile2));
        insertObjectsToDb(CRM_TB_ID_PROOF_TABLE_NAME, List.of(idProofObject, idProofObject2));
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab(client.getUcid());
        generalTab.kycDetailsOpen(POI);
        generalTab.FVSlideFunctions();
    }

    @Test
    @AllureId("326")
    @DisplayName("User has history drawer")
    void userHasHistoryDrawerTest() {
        CrmTbKycFilesObject kycFile = generateKycFilesObjectByClient(client);
        CrmTbIdProofObject idProofObject = generateIdProofObjectByClient(client, kycFile);
        insertObjectToDb(CRM_TB_KYC_FILES_TABLE_NAME, kycFile);
        insertObjectToDb(CRM_TB_ID_PROOF_TABLE_NAME, idProofObject);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab(client.getUcid());
        generalTab.kycDetailsOpen(POI);
        generalTab.kycHistoryDrawerDisplayed();
    }

    @Test
    @AllureId("318")
    @DisplayName("Client without KYC applyment must have placeholder")
    void userHavePlaceholderNoKYCTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab(client.getUcid());
        generalTab.noAppliedIsVisible();
    }

    @Test
    @AllureId("321")
    @DisplayName("Client without multiple KYC attempts must have displayed number of attempts")
    void userHaveNumberOfAttemptTest() {
        CrmTbKycFilesObject kycFile = generateKycFilesObjectByClient(client);
        CrmTbIdProofObject idProofObject = generateIdProofObjectByClient(client, kycFile);
        CrmTbKycFilesObject kycFile2 = generateKycFilesObjectByClient(client);
        CrmTbIdProofObject idProofObject2 = generateIdProofObjectByClient(client, kycFile2);
        CrmTbKycFilesObject kycFile3 = generateKycFilesObjectByClient(client);
        CrmTbAddressProofObject addressProofObject = generateAddressProofObjectByClient(client, kycFile3);
        CrmTbKycFilesObject kycFile4 = generateKycFilesObjectByClient(client);
        CrmTbAddressProofObject addressProofObject2 = generateAddressProofObjectByClient(client, kycFile4);
        insertObjectsToDb(CRM_TB_KYC_FILES_TABLE_NAME, List.of(kycFile, kycFile2, kycFile3, kycFile4));
        insertObjectsToDb(CRM_TB_ID_PROOF_TABLE_NAME, List.of(idProofObject, idProofObject2));
        insertObjectsToDb(CRM_TB_ADDRESS_PROOF_TABLE_NAME, List.of(addressProofObject, addressProofObject2));
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab(client.getUcid());
        generalTab.checkKycAttemptsGeneral(POI, "2");
        generalTab.checkKycAttemptsGeneral(POA, "2");
    }

    @Test
    @AllureId("320")
    @DisplayName("Client applied ID must have address info on general tab")
    void clientHaveAddressInfoGeneralTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab("vantage-525204");
        generalTab.poaDetailsGeneral("USA, DC, Washington", "321 Main St, 654321");
    }

    @Test
    @AllureId("319")
    @DisplayName("Client applied only POI must have placeholder about POA")
    void clientHavePlaceholderPOATest() {
        CrmTbKycFilesObject kycFile = generateKycFilesObjectByClient(client);
        CrmTbIdProofObject idProofObject = generateIdProofObjectByClient(client, kycFile);
        insertObjectToDb(CRM_TB_KYC_FILES_TABLE_NAME, kycFile);
        insertObjectToDb(CRM_TB_ID_PROOF_TABLE_NAME, idProofObject);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab(client.getUcid());
        generalTab.poaPlaceholderIsVisible();
    }

    @Test
    @AllureId("320")
    @DisplayName("Client have placeholder if not applied POI")
    void clientHavePlaceholderPOI() {
        CrmTbKycFilesObject kycFile = generateKycFilesObjectByClient(client);
        CrmTbAddressProofObject addressProofObject = generateAddressProofObjectByClient(client, kycFile);
        insertObjectToDb(CRM_TB_KYC_FILES_TABLE_NAME, kycFile);
        insertObjectToDb(CRM_TB_ADDRESS_PROOF_TABLE_NAME, addressProofObject);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab(client.getUcid());
        generalTab.poiPlaceholderIsVisible();
    }

    @Test
    @AllureId("899")
    @DisplayName("KYC - Client not have placeholder if not applied POF")
    void clientNotHavePlaceholderPofTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab(client.getUcid());
        generalTab.pofPlaceholderIsNotVisible();
    }

    @Test
    @AllureId("902")
    @DisplayName("POF - Check correct status display Submitted")
    void checkCorrectStatusDisplaySubmittedPofTest() {
        CrmTbKycFilesObject kycFile = generateKycFilesObjectByClient(client);
        CrmTbIdProofObject idProofObject = generateIdProofObjectByClient(client, kycFile);
        idProofObject.setFileTypeId(POF_FILE_TYPE_ID);
        idProofObject.setStatus("SUBMITTED");

        kycFile.setProofId(idProofObject.getId());
        kycFile.setFileTypeId(POF_FILE_TYPE_ID);
        insertObjectToDb(CRM_TB_KYC_FILES_TABLE_NAME, kycFile);
        insertObjectToDb(CRM_TB_ID_PROOF_TABLE_NAME, idProofObject);

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab(client.getUcid());
        generalTab.checkKycStatusGeneral(POF, "Submitted");
    }

    @Test
    @AllureId("903")
    @DisplayName("POF - Check correct status display Rejected")
    void checkCorrectStatusDisplayRejectedPofTest() {
        CrmTbKycFilesObject kycFile = generateKycFilesObjectByClient(client);
        CrmTbIdProofObject idProofObject = generateIdProofObjectByClient(client, kycFile);
        idProofObject.setFileTypeId(POF_FILE_TYPE_ID);
        idProofObject.setStatus("REJECTED");

        kycFile.setProofId(idProofObject.getId());
        kycFile.setFileTypeId(POF_FILE_TYPE_ID);
        insertObjectToDb(CRM_TB_KYC_FILES_TABLE_NAME, kycFile);
        insertObjectToDb(CRM_TB_ID_PROOF_TABLE_NAME, idProofObject);

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab(client.getUcid());
        generalTab.checkKycStatusGeneral(POF, "Rejected");
    }

    @Test
    @AllureId("904")
    @DisplayName("POF - Check correct status display Approved")
    void checkCorrectStatusDisplayApprovedPofTest() {
        CrmTbKycFilesObject kycFile = generateKycFilesObjectByClient(client);
        CrmTbIdProofObject idProofObject = generateIdProofObjectByClient(client, kycFile);
        idProofObject.setFileTypeId(POF_FILE_TYPE_ID);
        idProofObject.setStatus("COMPLETED");

        kycFile.setFileTypeId(POF_FILE_TYPE_ID);
        insertObjectToDb(CRM_TB_KYC_FILES_TABLE_NAME, kycFile);
        insertObjectToDb(CRM_TB_ID_PROOF_TABLE_NAME, idProofObject);

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab(client.getUcid());
        generalTab.checkKycStatusGeneral(POF, "Approved");
    }

    @Test
    @AllureId("905")
    @DisplayName("POF - Check correct status display Pending")
    void checkCorrectStatusPendingApprovedPofTest() {
        CrmTbKycFilesObject kycFile = generateKycFilesObjectByClient(client);
        CrmTbIdProofObject idProofObject = generateIdProofObjectByClient(client, kycFile);
        idProofObject.setFileTypeId(POF_FILE_TYPE_ID);
        idProofObject.setStatus("PENDING");

        kycFile.setFileTypeId(POF_FILE_TYPE_ID);
        insertObjectToDb(CRM_TB_KYC_FILES_TABLE_NAME, kycFile);
        insertObjectToDb(CRM_TB_ID_PROOF_TABLE_NAME, idProofObject);

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab(client.getUcid());
        generalTab.checkKycStatusGeneral(POF, "Pending");
    }

    @Test
    @AllureId("906")
    @DisplayName("POF - Check correct image displayed in Viewer")
    void checkThatImageDrawerShowsRightImagePofTest() {
        CrmTbKycFilesObject kycFile = generateKycFilesObjectByClient(client);
        CrmTbIdProofObject idProofObject = generateIdProofObjectByClient(client, kycFile);
        idProofObject.setFileTypeId(POF_FILE_TYPE_ID);
        idProofObject.setStatus("PENDING");

        kycFile.setFileTypeId(POF_FILE_TYPE_ID);
        insertObjectToDb(CRM_TB_KYC_FILES_TABLE_NAME, kycFile);
        insertObjectToDb(CRM_TB_ID_PROOF_TABLE_NAME, idProofObject);

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab(client.getUcid());
        generalTab.kycDetailsOpen(POF);
        generalTab.checkImageDisplayed();
    }

    @Test
    @AllureId("912")
    @DisplayName("POF - viewer show attempt history")
    void checkThatImageDrawerShowHistoryTest() {
        CrmTbKycFilesObject kycFile1 = generateKycFilesObjectByClient(client);
        CrmTbIdProofObject idProofObject1 = generateIdProofObjectByClient(client, kycFile1);
        idProofObject1.setFileTypeId(POF_FILE_TYPE_ID);
        idProofObject1.setStatus("REJECTED");

        CrmTbKycFilesObject kycFile2 = generateKycFilesObjectByClient(client);
        CrmTbIdProofObject idProofObject2 = generateIdProofObjectByClient(client, kycFile2);
        idProofObject2.setFileTypeId(POF_FILE_TYPE_ID);
        idProofObject2.setStatus("PENDING");

        kycFile1.setFileTypeId(POF_FILE_TYPE_ID);
        kycFile2.setFileTypeId(POF_FILE_TYPE_ID);

        insertObjectToDb(CRM_TB_KYC_FILES_TABLE_NAME, kycFile1);
        insertObjectToDb(CRM_TB_KYC_FILES_TABLE_NAME, kycFile2);
        insertObjectToDb(CRM_TB_ID_PROOF_TABLE_NAME, idProofObject1);
        insertObjectToDb(CRM_TB_ID_PROOF_TABLE_NAME, idProofObject2);

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab(client.getUcid());
        generalTab.kycDetailsOpen(POF);
        generalTab.checkNumberOfAttemptsInViewer(2);
    }

    @Test
    @AllureId("913")
    @DisplayName("POF - row shows actual data")
    void checkThatPofDataInGeneralTest() {
        CrmTbKycFilesObject kycFile1 = generateKycFilesObjectByClient(client);
        CrmTbIdProofObject idProofObject1 = generateIdProofObjectByClient(client, kycFile1);
        idProofObject1.setFileTypeId(POF_FILE_TYPE_ID);
        idProofObject1.setStatus("REJECTED");
        idProofObject1.setCreateTime(
                getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 1, 0, 0, 0));
        idProofObject1.setUpdateTime(
                getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 1, 0, 0, 0));

        CrmTbKycFilesObject kycFile2 = generateKycFilesObjectByClient(client);
        CrmTbIdProofObject idProofObject2 = generateIdProofObjectByClient(client, kycFile2);
        idProofObject2.setFileTypeId(POF_FILE_TYPE_ID);
        idProofObject2.setStatus("PENDING");

        kycFile1.setFileTypeId(POF_FILE_TYPE_ID);
        kycFile2.setFileTypeId(POF_FILE_TYPE_ID);

        insertObjectToDb(CRM_TB_KYC_FILES_TABLE_NAME, kycFile1);
        insertObjectToDb(CRM_TB_KYC_FILES_TABLE_NAME, kycFile2);
        insertObjectToDb(CRM_TB_ID_PROOF_TABLE_NAME, idProofObject1);
        insertObjectToDb(CRM_TB_ID_PROOF_TABLE_NAME, idProofObject2);

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab(client.getUcid());
        generalTab.checkValueKycPofTitle(POF);
        generalTab.checkValueKycPofStatus(idProofObject2.getStatus());
        generalTab.checkValueKycPofDate(idProofObject2.getUpdateTime());
        generalTab.checkValueKycPofParameters(
                idProofObject2.getDocumentType() + " " + idProofObject2.getDocumentNumber());
        generalTab.checkValueKycPofAttempts("2 attempts");
    }
}
