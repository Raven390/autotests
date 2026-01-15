package tests.vindex_backoffice_ui_tests.abuseRegistry;

import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.FraudType.HEDGING;
import static helpers.data.enums.FraudTypeStatus.CONFIRMED;
import static helpers.database.ArHelper.deleteUserFromAbuseRegistry;
import static helpers.database.DbHelper.*;
import static helpers.database.DbHelper.deleteObjectFromDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

import business_objects.db.abuse_registry_db.Abuser;
import business_objects.db.abuse_registry_db.AbuserFraudType;
import business_objects.db.abuse_registry_db.AbuserHistory;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import helpers.data.ClientHelper;
import helpers.database.DbName;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import java.sql.Timestamp;
import java.time.Instant;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

class FraudsterPendingProcessingTest extends TestBaseWeb {

    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(client);

    @BeforeAll
    static void setup() {
        var nowTimestamp = Timestamp.from(Instant.now());

        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertObjectToDb(
                DbName.POSTGRES,
                AR_ABUSER_TABLE_NAME,
                new Abuser(
                        client.getUcid(),
                        CONFIRMED.getStatus(),
                        "auto-test comment",
                        "AUTOTEST USER",
                        "Vindex BO",
                        nowTimestamp,
                        nowTimestamp,
                        true));
        insertObjectToDb(
                DbName.POSTGRES,
                AR_ABUSER_HISTORY_TABLE_NAME,
                new AbuserHistory(
                        null,
                        client.getUcid(),
                        "CLIENT_STATUS",
                        CONFIRMED.getStatus(),
                        "CLIENT_STATUS",
                        "auto-test comment",
                        "AUTOTEST USER",
                        "Vindex BO",
                        nowTimestamp,
                        null,
                        null));
        insertObjectToDb(
                DbName.POSTGRES,
                AR_ABUSER_FRAUD_TYPE_TABLE_NAME,
                new AbuserFraudType(
                        client.getUcid(),
                        HEDGING.getCode(),
                        CONFIRMED.getStatus(),
                        "auto-test comment",
                        "AUTOTEST USER",
                        "Vindex BO",
                        nowTimestamp,
                        nowTimestamp,
                        "INTERNAL",
                        null,
                        "Vindex"));
        insertObjectToDb(
                DbName.POSTGRES,
                AR_ABUSER_HISTORY_TABLE_NAME,
                new AbuserHistory(
                        null,
                        client.getUcid(),
                        HEDGING.getCode(),
                        CONFIRMED.getStatus(),
                        "FRAUD_TYPE_STATUS",
                        "auto-test comment",
                        "AUTOTEST USER",
                        "Vindex BO",
                        nowTimestamp,
                        "INTERNAL",
                        null));
    }

    @AfterAll
    static void teardown() throws Exception {
        deleteObjectFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteUserFromAbuseRegistry(client.getUcid());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @Tag(ABUSE_REGISTRY)
    @Feature("BMS-1832 Bulk deduction: Manual processing")
    @AllureId("1545")
    @DisplayName("Verify Abuse registry pending processing filter")
    void pendingProcessingFilterTest() {
        var content = "Deduction calculation required";
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        fraudstersPage.navigateAbuseRegistryFraudsters();
        fraudstersPage.clickPendingProcessingToggle();
        assertThat(
                "Generated client in pending processing state is not present!",
                fraudstersPage.getClientInPendingProcessingContent(client.getUcid()),
                is(content));
        fraudstersPage.getPendingProcessingCellsContent();
        assertThat(
                "Clients NOT in pending processing state are present!",
                fraudstersPage.getPendingProcessingCellsContent(),
                everyItem(is(content)));
    }
}
