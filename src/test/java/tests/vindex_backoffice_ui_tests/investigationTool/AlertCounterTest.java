package tests.vindex_backoffice_ui_tests.investigationTool;

import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateStaticUserByClient;
import static business_objects.ui.user.UserFactory.autotestUserOne;
import static business_objects.ui.user.UserFactory.autotestUserPT;
import static helpers.data.ClientFactory.getRandomVantageClient;
import static helpers.data.enums.InvestigationStatus.ACTIVE;
import static helpers.database.BoHelper.*;
import static helpers.database.CleanTableHelper.cleanUserAudit;
import static helpers.database.DbHelper.*;
import static helpers.kafka.alerts.CreateSimpleAlert.sendSimpleAlert;
import static helpers.kafka.alerts.CreateSimpleAlert.sendSimplePaymentAlert;
import static utils.Constants.CRM_USER_TABLE_NAME;

import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import helpers.data.ClientHelper;
import helpers.data.enums.AlertType;
import helpers.data.enums.InvestigationStatus;
import helpers.database.ArHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import java.sql.SQLException;
import java.util.List;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

class AlertCounterTest extends TestBaseWeb {
    static ClientHelper client1 = getRandomVantageClient();
    private static CrmTbUserObject crmTbUser1 = generateStaticUserByClient(client1);
    static ClientHelper client2 = getRandomVantageClient();
    private static CrmTbUserObject crmTbUser2 = generateStaticUserByClient(client2);
    static ClientHelper client3 = getRandomVantageClient();
    private static CrmTbUserObject crmTbUser3 = generateStaticUserByClient(client3);

    @BeforeAll
    static void setup() throws ReflectiveOperationException, SQLException, JsonProcessingException {
        insertObjectsToDb(CRM_USER_TABLE_NAME, List.of(crmTbUser1, crmTbUser2, crmTbUser3));
    }

    @AfterEach
    void teardown() throws Exception {
        closeAlert(crmTbUser1.ucid);
        deleteUserBO(client1.getUcid());
        closeAlert(crmTbUser2.ucid);
        deleteUserBO(client2.getUcid());
        closeAlert(crmTbUser3.ucid);
        deleteUserBO(client3.getUcid());
    }

    @AfterAll
    static void clean() throws Exception {
        deleteObjectFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", crmTbUser1.ucid));
        closeAlert(crmTbUser1.ucid);
        deleteUserBO(client1.getUcid());
        cleanUserAudit(client1.getUcid());
        ArHelper.deleteUserFromAbuseRegistry(client1.getUcid());
        deleteObjectFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", crmTbUser2.ucid));
        closeAlert(crmTbUser2.ucid);
        deleteUserBO(client2.getUcid());
        cleanUserAudit(client2.getUcid());
        ArHelper.deleteUserFromAbuseRegistry(client2.getUcid());
        deleteObjectFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", crmTbUser3.ucid));
        closeAlert(crmTbUser3.ucid);
        deleteUserBO(client3.getUcid());
        cleanUserAudit(client3.getUcid());
        ArHelper.deleteUserFromAbuseRegistry(client3.getUcid());
    }

    @Test
    @AllureId("1580")
    @Feature("BMS-2108 Display total amount of clients with active alerts")
    @DisplayName("BO user with general role can see counter for both alert types")
    void assignClientCardBothTypesAlertsGeneralRoleCounterTest() throws Exception {
        sendSimplePaymentAlert(client1.getUcid());
        sendSimplePaymentAlert(client1.getUcid());
        sendSimplePaymentAlert(client2.getUcid());
        sendSimplePaymentAlert(client3.getUcid());
        sendSimpleAlert(client1.getUcid(), "MARKET_MANIPULATION");
        sendSimpleAlert(client2.getUcid(), "HEDGING");
        sendSimpleAlert(client2.getUcid(), "MARKET_MANIPULATION");
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateInvestigationTool();
        investigationPage.clickSelectInvestigationType("Trading");
        long countUnassignT = countInvestigationsDb(AlertType.TRADING, InvestigationStatus.NEW);
        long countAllT = countInvestigationsDb(AlertType.TRADING, ACTIVE) + countUnassignT;
        long countMyT =
                countUsersInvestigationsDb(AlertType.TRADING, autotestUserOne().getId());
        investigationPage.checkSuspiciousCounterValueALL(countAllT);
        investigationPage.checkSuspiciousCounterValueUNASSIGNED(countUnassignT);
        investigationPage.checkSuspiciousCounterValueMY(countMyT);
        investigationPage.filterUnassigned();
        investigationPage.investigateUserAlertList(client1.getUserId());
        countUnassignT = countInvestigationsDb(AlertType.TRADING, InvestigationStatus.NEW);
        countAllT = countInvestigationsDb(AlertType.TRADING, ACTIVE) + countUnassignT;
        countMyT =
                countUsersInvestigationsDb(AlertType.TRADING, autotestUserOne().getId());
        investigationPage.checkSuspiciousCounterValueALL(countAllT);
        investigationPage.checkSuspiciousCounterValueUNASSIGNED(countUnassignT);
        investigationPage.checkSuspiciousCounterValueMY(countMyT);
        investigationPage.clickSelectInvestigationType("Payments");
        long countUnassignP = countInvestigationsDb(AlertType.PAYMENT, InvestigationStatus.NEW);
        long countAllP = countInvestigationsDb(AlertType.PAYMENT, ACTIVE) + countUnassignP;
        long countMyP =
                countUsersInvestigationsDb(AlertType.PAYMENT, autotestUserOne().getId());
        investigationPage.checkSuspiciousCounterValueALL(countAllP);
        investigationPage.checkSuspiciousCounterValueUNASSIGNED(countUnassignP);
        investigationPage.checkSuspiciousCounterValueMY(countMyP);
        investigationPage.filterUnassigned();
        investigationPage.investigateUserAlertList(client1.getUserId());
        countUnassignP = countInvestigationsDb(AlertType.PAYMENT, InvestigationStatus.NEW);
        countAllP = countInvestigationsDb(AlertType.PAYMENT, ACTIVE) + countUnassignP;
        countMyP =
                countUsersInvestigationsDb(AlertType.PAYMENT, autotestUserOne().getId());
        investigationPage.checkSuspiciousCounterValueALL(countAllP);
        investigationPage.checkSuspiciousCounterValueUNASSIGNED(countUnassignP);
        investigationPage.checkSuspiciousCounterValueMY(countMyP);
    }

    @Test
    @AllureId("1581")
    @Feature("BMS-2108 Display total amount of clients with active alerts")
    @DisplayName("BO user with Payment Team role can see counter for  alerts")
    void assignClientCardBothTypesAlertsPaymentRoleCounterTest() throws Exception {
        sendSimplePaymentAlert(client1.getUcid());
        sendSimplePaymentAlert(client1.getUcid());
        sendSimplePaymentAlert(client2.getUcid());
        sendSimplePaymentAlert(client3.getUcid());
        sendSimpleAlert(client1.getUcid(), "MARKET_MANIPULATION");
        sendSimpleAlert(client2.getUcid(), "HEDGING");
        sendSimpleAlert(client2.getUcid(), "MARKET_MANIPULATION");
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsPaymentTeamUser();
        investigationPage.navigateInvestigationTool();
        long countUnassignP = countInvestigationsDb(AlertType.PAYMENT, InvestigationStatus.NEW);
        long countAllP = countInvestigationsDb(AlertType.PAYMENT, ACTIVE) + countUnassignP;
        long countMyP =
                countUsersInvestigationsDb(AlertType.PAYMENT, autotestUserPT().getId());
        investigationPage.checkSuspiciousCounterValueALL(countAllP);
        investigationPage.checkSuspiciousCounterValueUNASSIGNED(countUnassignP);
        investigationPage.checkSuspiciousCounterValueMY(countMyP);
        investigationPage.filterUnassigned();
        investigationPage.investigateUserAlertList(client1.getUserId());
        countUnassignP = countInvestigationsDb(AlertType.PAYMENT, InvestigationStatus.NEW);
        countAllP = countInvestigationsDb(AlertType.PAYMENT, ACTIVE) + countUnassignP;
        countMyP =
                countUsersInvestigationsDb(AlertType.PAYMENT, autotestUserPT().getId());
        investigationPage.checkSuspiciousCounterValueALL(countAllP);
        investigationPage.checkSuspiciousCounterValueUNASSIGNED(countUnassignP);
        investigationPage.checkSuspiciousCounterValueMY(countMyP);
    }
}
