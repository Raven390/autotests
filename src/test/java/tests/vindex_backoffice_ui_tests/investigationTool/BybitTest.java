//package tests.vindex_backoffice_ui_tests.investigationTool;
//
//import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
//import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
//import business_objects.db.clickhouse.mt_account.MtAccountObject;
//import business_objects.kafka.alerts.RuleAlert;
//import business_objects.ui.audit_trail.AuditTrailItem;
//import business_objects.ui.user.User;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import helpers.data.ClientHelper;
//import helpers.kafka.KafkaHelper;
//import io.qameta.allure.AllureId;
//import org.junit.jupiter.api.*;
//import tests.TestBaseWeb;
//
//import java.io.IOException;
//import java.sql.SQLException;
//import java.text.DecimalFormat;
//import java.util.List;
//
//import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataBybit;
//import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
//import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateBybitUserByClient;
//import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
//import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
//import static business_objects.kafka.alerts.RuleAlertFactory.generateRuleAlertByUcid;
//import static business_objects.kafka.alerts.RuleAlertFactory.generateWithdrawalNotificationAlert;
//import static business_objects.ui.user.UserFactory.autotestUserOne;
//import static helpers.data.ClientFactory.getRandomBybitClient;
//import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
//import static helpers.data.enums.Restriction.LOGIN_CRM;
//import static helpers.database.BoHelper.closeAlert;
//import static helpers.database.DbHelper.deleteEntryFromDb;
//import static helpers.database.DbHelper.insertObjectToDb;
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.*;
//import static utils.Constants.*;
//import static utils.Utils.closeAllAlertsBo;
//
//public class BybitTest extends TestBaseWeb {
//
//    private static final KafkaHelper kafka = new KafkaHelper();
//    private static final ObjectMapper objectMapper = new ObjectMapper();
//
//    private static RuleAlert alert;
//    private static final ClientHelper client = getRandomBybitClient();
//    private static final CrmTbUserObject crmTbUser = generateBybitUserByClient(client);
//    private static final CrmTbAccountObject account = generateCrmTbAccountDataBybit(client);
//    private static final MtAccountObject mtAccount = generateMtAccountByCrmTbAccount(account);
//    private static final String TIME_PATTERN = "^([01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$";
//    private static final User user = autotestUserOne();
//
//    @BeforeEach
//    public void setup() throws ReflectiveOperationException, SQLException, JsonProcessingException {
//        closeAllAlertsBo();
//        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
//        insertObjectToDb(CRM_ACCOUNT_TABLE_NAME, account);
//        insertObjectToDb(MT_ACCOUNT_TABLE_NAME, mtAccount);
//        alert = generateRuleAlertByUcid(crmTbUser.ucid);
//        kafka.produceMessage(alert.alertId, objectMapper.writeValueAsString(alert), KAFKA_TOPIC_ALERTS);
//    }
//
//    @AfterEach
//    public void teardown() throws SQLException {
//        deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
//        closeAlert(client.getUcid());
//    }
//
//    @Test
//    @Tag(TEAM_BACKOFFICE)
//    @Tag(LAYER_WEB)
//    @AllureId("")
//    @DisplayName("Verify Bybit alert card does not contain country code")
//    public void verifyAlertReceivedTest() {
//        investigationPage.navigateEnterPage();
//        keycloackPage.loginAsAutotestUser();
//        investigationPage.navigateToMain();
//        investigationPage.waitForPageToLoad();
//        investigationPage.filterUnassigned();
//        investigationPage.waitForPageToLoad();
//    }
//}
