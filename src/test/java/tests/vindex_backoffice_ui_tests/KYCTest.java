package tests.vindex_backoffice_ui_tests;

import business_objects.db.clickhouse.crm_id_proof.CrmTbIdProofObject;
import business_objects.db.clickhouse.crm_tb_kyc_files.KycFilesTableEntry;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import helpers.data.ClientHelper;
import io.qameta.allure.AllureId;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBaseWeb;

import java.sql.SQLException;

import static business_objects.db.clickhouse.crm_id_proof.CrmTbIdProofFactory.generateIdProofObject;
import static business_objects.db.clickhouse.crm_tb_kyc_files.KycFilesTableEntryFactory.getKycFile;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateStaticUserByClient;
import static helpers.data.enums.Brand.INFINOX;
import static helpers.data.enums.Regulator.VFSC2;
import static helpers.database.DbHelper.deleteEntryFromDb;
import static helpers.database.DbHelper.insertObjectToDb;
import static utils.Constants.*;
import static utils.Utils.*;

public class KYCTest extends TestBaseWeb {

    static Faker faker = new Faker();

    static ClientHelper pofClient = new ClientHelper(525_210, INFINOX, VFSC2);

    @BeforeAll
    public static void setup() throws ReflectiveOperationException, SQLException {
        CrmTbUserObject pofClientDb = generateStaticUserByClient(pofClient);
        pofClientDb.firstName = "Face";
        pofClientDb.lastName = "Mc Shooty";
        insertObjectToDb(CRM_USER_TABLE_NAME, pofClientDb);
    }


    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("351")
    @DisplayName("Check correct status display Submitted")
    public void checkCorrectStatusDisplaySubmittedTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab("infinox-525206");
        generalTab.checkKycStatusGeneral("Proof of identity", "Submitted");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("354")
    @DisplayName("Check correct status display Rejected")
    public void checkCorrectStatusDisplayRejectedTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab("infinox-525208");
        generalTab.checkKycStatusGeneral("Proof of identity", "Rejected");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("352")
    @DisplayName("Check correct status display Pending")
    public void checkCorrectStatusDisplayPendingTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab("infinox-525207");
        generalTab.checkKycStatusGeneral("Proof of identity", "Pending");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("353")
    @DisplayName("Check correct status display Completed")
    public void checkCorrectStatusDisplayCompletedTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab("infinox-525205");
        generalTab.checkKycStatusGeneral("Proof of identity", "Approved");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("357")
    @DisplayName("KYC File viewer BO user can zoom displayed file using buttons in UI")
    public void userCanZoomTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab("infinox-525204");
        generalTab.kycDetailsOpen("Proof of identity");
        generalTab.FVZoomFunctions();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("358")
    @DisplayName("KYC File viewer BO user can rotate displayed file using buttons in UI")
    public void userCanRotateTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab("infinox-525204");
        generalTab.kycDetailsOpen("Proof of identity");
        generalTab.FVRotateFunctions();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("359")
    @DisplayName("KYC File viewer BO user can mirror displayed file using buttons in UI")
    public void userCanMirrorTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab("infinox-525204");
        generalTab.kycDetailsOpen("Proof of identity");
        generalTab.FVMirrorFunctions();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("356")
    @DisplayName("KYC File viewer BO user can slide displayed file using buttons in UI")
    public void userCanSlideTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab("infinox-525204");
        generalTab.kycDetailsOpen("Proof of identity");
        generalTab.FVSlideFunctions();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("326")
    @DisplayName("User has history drawer")
    public void userHasHistoryDrawerTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab("infinox-525204");
        generalTab.kycDetailsOpen("Proof of identity");
        generalTab.kycHistoryDrawerDisplayed();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("318")
    @DisplayName("Client without KYC applyment must have placeholder")
    public void userHavePlaceholderNoKYCTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab("infinox-525201");
        generalTab.noAppliedIsVisible();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("321")
    @DisplayName("Client without multiple KYC attempts must have displayed number of attempts")
    public void userHaveNumberOfAttemptTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab("infinox-525204");
        generalTab.checkKycAttemptsGeneral("Proof of identity", "2");
        generalTab.checkKycAttemptsGeneral("Proof of address", "2");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("320")
    @DisplayName("Client applied ID must have address info on general tab")
    public void clientHaveAddressInfoGeneralTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab("infinox-525204");
        generalTab.poaDetailsGeneral("USA, DC, Washington", "321 Main St, 654321");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("319")
    @DisplayName("Client applied only POI must have placeholder about POA")
    public void clientHavePlaceholderPOATest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab("infinox-525202");
        generalTab.poaPlaceholderIsVisible();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("320")
    @DisplayName("Client have placeholder if not applied POI")
    public void clientHavePlaceholderPOI() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab("infinox-525203");
        generalTab.poiPlaceholderIsVisible();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("899")
    @DisplayName("KYC - Client not have placeholder if not applied POF")
    public void clientNotHavePlaceholderPofTest() throws SQLException {
        generalTab.deleteClientsPofAttempts(pofClient.getUcid());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab(pofClient.getUcid());
        generalTab.pofPlaceholderIsNotVisible();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("902")
    @DisplayName("POF - Check correct status display Submitted")
    public void checkCorrectStatusDisplaySubmittedPofTest() throws SQLException, ReflectiveOperationException {

        generalTab.deleteClientsPofAttempts(pofClient.getUcid());
        generalTab.deleteClientsPofFileRecord(pofClient.getUcid());

        CrmTbIdProofObject idProofObject = generateIdProofObject(pofClient);
        idProofObject.setFileTypeId(27);
        idProofObject.setStatus("SUBMITTED");

        KycFilesTableEntry file = getKycFile(pofClient);
        file.proofId = idProofObject.getId();
        file.fileName = FILE_KYC_POF_1_NAME;
        file.fileTypeId = 27;
        insertObjectToDb(KYC_FILES_TABLE_NAME, file);
        insertObjectToDb(ID_PROOF_TABLE_NAME, idProofObject);

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab(pofClient.getUcid());
        generalTab.checkKycStatusGeneral("Proof of face", "Submitted");

        deleteEntryFromDb(KYC_FILES_TABLE_NAME, "id = " + file.id);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("903")
    @DisplayName("POF - Check correct status display Rejected")
    public void checkCorrectStatusDisplayRejectedPofTest() throws SQLException, ReflectiveOperationException {

        generalTab.deleteClientsPofAttempts(pofClient.getUcid());
        generalTab.deleteClientsPofFileRecord(pofClient.getUcid());

        CrmTbIdProofObject idProofObject = generateIdProofObject(pofClient);
        idProofObject.setFileTypeId(27);
        idProofObject.setStatus("REJECTED");

        KycFilesTableEntry file = getKycFile(pofClient);
        file.proofId = idProofObject.getId();
        file.fileName = FILE_KYC_POF_1_NAME;
        file.fileTypeId = 27;
        insertObjectToDb(KYC_FILES_TABLE_NAME, file);
        insertObjectToDb(ID_PROOF_TABLE_NAME, idProofObject);

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab(pofClient.getUcid());
        generalTab.checkKycStatusGeneral("Proof of face", "Rejected");

        deleteEntryFromDb(KYC_FILES_TABLE_NAME, "id = " + file.id);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("904")
    @DisplayName("POF - Check correct status display Approved")
    public void checkCorrectStatusDisplayApprovedPofTest() throws SQLException, ReflectiveOperationException {

        generalTab.deleteClientsPofAttempts(pofClient.getUcid());
        generalTab.deleteClientsPofFileRecord(pofClient.getUcid());

        CrmTbIdProofObject idProofObject = generateIdProofObject(pofClient);
        idProofObject.setFileTypeId(27);
        idProofObject.setStatus("COMPLETED");

        KycFilesTableEntry file = getKycFile(pofClient);
        file.proofId = idProofObject.getId();
        file.fileName = FILE_KYC_POF_1_NAME;
        file.fileTypeId = 27;
        insertObjectToDb(KYC_FILES_TABLE_NAME, file);
        insertObjectToDb(ID_PROOF_TABLE_NAME, idProofObject);

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab(pofClient.getUcid());
        generalTab.checkKycStatusGeneral("Proof of face", "Approved");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("905")
    @DisplayName("POF - Check correct status display Pending")
    public void checkCorrectStatusPendingApprovedPofTest() throws SQLException, ReflectiveOperationException {

        generalTab.deleteClientsPofAttempts(pofClient.getUcid());
        generalTab.deleteClientsPofFileRecord(pofClient.getUcid());

        CrmTbIdProofObject idProofObject = generateIdProofObject(pofClient);
        idProofObject.setFileTypeId(27);
        idProofObject.setStatus("PENDING");

        KycFilesTableEntry file = getKycFile(pofClient);
        file.proofId = idProofObject.getId();
        file.fileName = FILE_KYC_POF_1_NAME;
        file.fileTypeId = 27;
        insertObjectToDb(KYC_FILES_TABLE_NAME, file);
        insertObjectToDb(ID_PROOF_TABLE_NAME, idProofObject);

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab(pofClient.getUcid());
        generalTab.checkKycStatusGeneral("Proof of face", "Pending");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("906")
    @DisplayName("POF - Check correct image displayed in Viewer")
    public void checkThatImageDrawerShowsRightImagePofTest() throws SQLException, ReflectiveOperationException {

        generalTab.deleteClientsPofAttempts(pofClient.getUcid());
        generalTab.deleteClientsPofFileRecord(pofClient.getUcid());

        CrmTbIdProofObject idProofObject = generateIdProofObject(pofClient);
        idProofObject.setFileTypeId(27);
        idProofObject.setStatus("PENDING");

        KycFilesTableEntry file = getKycFile(pofClient);
        file.proofId = idProofObject.getId();
        file.fileName = FILE_KYC_POF_1_NAME;
        file.fileTypeId = 27;
        insertObjectToDb(KYC_FILES_TABLE_NAME, file);
        insertObjectToDb(ID_PROOF_TABLE_NAME, idProofObject);

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab(pofClient.getUcid());
        generalTab.kycDetailsOpen("Proof of face");
        generalTab.checkRightImage("H23GMk+EzskgAAAAAElFTkSuQmCC");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("912")
    @DisplayName("POF - viewer show attempt history")
    public void checkThatImageDrawerShowHistoryTest() throws SQLException, ReflectiveOperationException {

        generalTab.deleteClientsPofAttempts(pofClient.getUcid());
        generalTab.deleteClientsPofFileRecord(pofClient.getUcid());

        CrmTbIdProofObject idProofObject1 = generateIdProofObject(pofClient);
        idProofObject1.setFileTypeId(27);
        idProofObject1.setStatus("REJECTED");

        CrmTbIdProofObject idProofObject2 = generateIdProofObject(pofClient);
        idProofObject2.setFileTypeId(27);
        idProofObject2.setStatus("PENDING");
        idProofObject2.setCreateTime("2024-12-21 11:17:50.030000000");

        KycFilesTableEntry file1 = getKycFile(pofClient);
        file1.proofId = idProofObject1.getId();
        file1.fileName = FILE_KYC_POF_1_NAME;
        file1.fileTypeId = 27;
        insertObjectToDb(KYC_FILES_TABLE_NAME, file1);

        KycFilesTableEntry file2 = getKycFile(pofClient);
        file2.proofId = idProofObject2.getId();
        file2.fileName = FILE_KYC_POF_2_NAME;
        file2.fileTypeId = 27;

        insertObjectToDb(KYC_FILES_TABLE_NAME, file1);
        insertObjectToDb(KYC_FILES_TABLE_NAME, file2);
        insertObjectToDb(ID_PROOF_TABLE_NAME, idProofObject1);
        insertObjectToDb(ID_PROOF_TABLE_NAME, idProofObject2);

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab(pofClient.getUcid());
        generalTab.kycDetailsOpen("Proof of face");
        generalTab.checkNumberOfAttemptsInViewer(2);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("913")
    @DisplayName("POF - row shows actual data")
    public void checkThatPofDataInGeneralTest() throws SQLException, ReflectiveOperationException {

        generalTab.deleteClientsPofAttempts(pofClient.getUcid());
        generalTab.deleteClientsPofFileRecord(pofClient.getUcid());

        CrmTbIdProofObject idProofObject1 = generateIdProofObject(pofClient);
        idProofObject1.setFileTypeId(27);
        idProofObject1.setStatus("REJECTED");
        idProofObject1.setUpdateTime("2024-11-29 11:17:50.030000000");
        idProofObject1.setCreateTime("2024-11-29 11:17:50.030000000");
        idProofObject1.setDateOfBirth(null);

        CrmTbIdProofObject idProofObject2 = generateIdProofObject(pofClient);
        idProofObject2.setFileTypeId(27);
        idProofObject2.setStatus("PENDING");
        idProofObject2.setUpdateTime("2024-12-29 11:17:50.030000000");
        idProofObject2.setCreateTime("2024-12-29 11:17:50.030000000");
        idProofObject2.setDateOfBirth(null);
        idProofObject2.setDocumentType(faker.animal().name());
        idProofObject2.setDocumentNumber(String.valueOf(getCurrentTimestamp()));

        KycFilesTableEntry file1 = getKycFile(pofClient);
        file1.proofId = idProofObject1.getId();
        file1.fileName = FILE_KYC_POF_1_NAME;
        file1.fileTypeId = 27;
        insertObjectToDb(KYC_FILES_TABLE_NAME, file1);

        KycFilesTableEntry file2 = getKycFile(pofClient);
        file2.proofId = idProofObject2.getId();
        file2.fileName = FILE_KYC_POF_2_NAME;
        file2.fileTypeId = 27;

        insertObjectToDb(KYC_FILES_TABLE_NAME, file1);
        insertObjectToDb(KYC_FILES_TABLE_NAME, file2);
        insertObjectToDb(ID_PROOF_TABLE_NAME, idProofObject1);
        page.waitForTimeout(100);
        insertObjectToDb(ID_PROOF_TABLE_NAME, idProofObject2);

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigateGeneralTab(pofClient.getUcid());
        generalTab.checkValueKycPofTitle("Proof of face");
        generalTab.checkValueKycPofStatus(idProofObject2.getStatus());
        generalTab.checkValueKycPofDate(idProofObject2.getUpdateTime().split(" ")[0]);
        generalTab.checkValueKycPofParameters(idProofObject2.getDocumentType() + " " + idProofObject2.getDocumentNumber());
        generalTab.checkValueKycPofAttempts("2 attempts");
    }
}
