package tests.vindexBackofficeUiTests;

import businessObjects.db.clickhouse.connectionTable.ConnectionTableEntry;
import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;
import businessObjects.kafka.alerts.RuleAlert;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.data.ClientHelper;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.sql.SQLException;
import java.util.List;

import static businessObjects.db.clickhouse.connectionTable.ConnectionTableEntryFactory.getConnectionTableEntryForUi;
import static businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObjectFactory.generateUserByClient;
import static businessObjects.kafka.alerts.RuleAlertFactory.generateRuleAlertByUcid;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static utils.Constants.*;

public class ConnectionSearchHideSensitiveDataTest extends TestBaseWeb {

    private static final KafkaHelper kafka = new KafkaHelper();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static ClientHelper client;
    private static ClientHelper connectedClient;

    @BeforeAll
    public static void setup() throws Exception {
        client = getRandomVantageClientAllFields();
        connectedClient = getRandomVantageClientAllFields();
        CrmTbUserObject crmTbUser = generateUserByClient(client);
        CrmTbUserObject connectedCrmTbUser = generateUserByClient(connectedClient);
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertObjectToDb(CRM_USER_TABLE_NAME, connectedCrmTbUser);
        ConnectionTableEntry connectionTableEntry = getConnectionTableEntryForUi(client, connectedClient);
        insertObjectToDb(CONNECTIONS_TABLE_NAME, connectionTableEntry);
        RuleAlert alert = generateRuleAlertByUcid(crmTbUser.ucid);
        kafka.produceMessage(alert.alertId, objectMapper.writeValueAsString(alert), KAFKA_TOPIC_ALERTS);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("687")
    @DisplayName("Verify sensitive data hiding in connection search card view")
    public void verifyConnectionSearchHideSensitiveDataCardTest() {
        investigationPage.navigate();
        keycloackPage.loginAsCoreUser();
        investigationPage.waitForPageToLoad();
        investigationPage.clickSuspiciousClientsFiltration();
        investigationPage.selectBrandFilterByText(client.getBrand());
        investigationPage.clickApplyFiltrationButton();
        investigationPage.filterUnassigned();
        investigationPage.waitForPageToLoad();
        investigationPage.clickClientCardByClientId(String.valueOf(client.getUserId()));
        alertsPage.waitForPageToLoad();
        connectionPage.clickConnectionTabButton();
        connectionPage.openConnectionCard(client.getUcid());
        connectionPage.ccCheckGeneralInfoRows("Email", "t****4@example.com");
        connectionPage.ccCheckDirectConnectionRows(connectedClient.getUcid(), "phoneNumber", "F**********************=");
        connectionPage.ccCheckDirectConnectionRows(connectedClient.getUcid(), "emailAddress", "m*****e@gmx.net");
        connectionPage.ccCheckDirectConnectionRows(connectedClient.getUcid(), "documentNumber", "3***********2");
        connectionPage.clickUnmaskConnectionCardDataButton();
        connectionPage.ccCheckGeneralInfoRows("Email", "test14@example.com");
        connectionPage.ccCheckDirectConnectionRows(connectedClient.getUcid(), "phoneNumber", "F2jTWljlC4HSI0uMpPz5Yw==");
        connectionPage.ccCheckDirectConnectionRows(connectedClient.getUcid(), "emailAddress", "matisse@gmx.net");
        connectionPage.ccCheckDirectConnectionRows(connectedClient.getUcid(), "documentNumber", "3110200460092");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("688")
    @DisplayName("Verify sensitive data hiding in connection search table view")
    public void verifyConnectionSearchHideSensitiveDataTableTest() {
        investigationPage.navigate();
        keycloackPage.loginAsCoreUser();
        investigationPage.waitForPageToLoad();
        investigationPage.clickSuspiciousClientsFiltration();
        investigationPage.selectBrandFilterByText(client.getBrand());
        investigationPage.clickApplyFiltrationButton();
        investigationPage.filterUnassigned();
        investigationPage.waitForPageToLoad();
        investigationPage.clickClientCardByClientId(String.valueOf(client.getUserId()));
        alertsPage.waitForPageToLoad();
        connectionPage.clickConnectionTabButton();
        connectionPage.openConnectionTable();
        List<String> attributesList = connectionPage.getConnectionTableAttributesList(connectedClient);
        assertThat(attributesList, hasItems("phoneNumberF**********************=", "emailAddressm*****e@gmx.net", "documentNumber3***********2"));
        connectionPage.clickUnmaskConnectionTableDataButton();
        attributesList = connectionPage.getConnectionTableAttributesList(connectedClient);
        assertThat(attributesList, hasItems("phoneNumberF2jTWljlC4HSI0uMpPz5Yw==", "emailAddressmatisse@gmx.net", "documentNumber3110200460092"));
    }

    @AfterAll
    public static void teardown() throws SQLException {
        deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", connectedClient.getUcid()));
        deleteEntryFromDb(CONNECTIONS_TABLE_NAME, String.format("user_from = '%s'", client.getUcid()));
        closeAlert(client.getUcid());
    }
}
