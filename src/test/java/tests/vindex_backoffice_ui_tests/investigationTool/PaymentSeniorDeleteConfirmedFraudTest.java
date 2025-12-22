package tests.vindex_backoffice_ui_tests.investigationTool;

import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static helpers.api.AbuseRegistryHelper.addFraudsForClient;
import static helpers.data.ClientFactory.getRandomVantageClient;
import static helpers.data.enums.FraudType.*;
import static helpers.data.enums.FraudTypeStatus.*;
import static helpers.database.ArHelper.deleteUserFromAbuseRegistry;
import static helpers.database.CleanTableHelper.cleanCrmUserTableByClient;
import static helpers.database.DbHelper.getObjectsFromDB;
import static helpers.database.DbHelper.insertObjectToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

import business_objects.db.abuse_registry_db.AbuserFraudType;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import helpers.data.ClientHelper;
import helpers.database.DbName;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_WEB)
@Feature("BMS-2819 Add ability to remove payment fraud types for Payment Senior role")
class PaymentSeniorDeleteConfirmedFraudTest extends TestBaseWeb {
    private static final ClientHelper client = getRandomVantageClient();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(client);

    @BeforeAll
    static void setup() throws IOException {
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        addFraudsForClient(client.getUcid(), List.of(CHARGEBACK, ATO), POTENTIAL);
        addFraudsForClient(client.getUcid(), List.of(EXCHANGER, UPGRADER), CONFIRMED);
    }

    @AfterAll
    static void teardown() throws Exception {
        cleanCrmUserTableByClient(client.getUcid());
        deleteUserFromAbuseRegistry(client.getUcid());
    }

    @Test
    @AllureId("1929")
    @DisplayName("Payment senior user can delete both confirmed and potential payment fraud types")
    void paymentSeniorDeleteFraudsTest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsPaymentSeniorUser();
        investigationPage.navigateToClient(client.getUcid());
        resolvePage.openReportFraudForm();
        resolvePage.deleteFraudByNameNoPopup(String.format("%s %s", POTENTIAL.getDisplayName(), CHARGEBACK.getName()));
        resolvePage.deleteFraudByNameNoPopup(EXCHANGER.getName());
        resolvePage.applyFraudManagement("test comment");
        List<AbuserFraudType> abuserFraudTypes = getObjectsFromDB(
                DbName.POSTGRES,
                AR_ABUSER_FRAUD_TYPE_TABLE_NAME,
                String.format(
                        "ucid = '%s' and fraud_type_code IN ('%s', '%s')",
                        client.getUcid(), CHARGEBACK.getCode(), EXCHANGER.getCode()),
                AbuserFraudType.class);
        assertThat(
                "Verify all fraud types have status CLEANED",
                abuserFraudTypes,
                everyItem(hasProperty("status", equalTo(CLEANED.getStatus()))));
    }

    @Test
    @AllureId("1930")
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Payment team user can delete only potential payment fraud types")
    void paymentSeniorDeleteOnlyPotentialFraudsTest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsPaymentTeamUser();
        investigationPage.navigateToClient(client.getUcid());
        resolvePage.openReportFraudForm();
        resolvePage.deleteFraudByNameNoPopup(String.format("%s %s", POTENTIAL.getDisplayName(), ATO.getName()));
        assertThat(
                "Verify confirmed fraud has info icon that it can't be deleted",
                resolvePage.isInfoIconVisibleForFraudType(UPGRADER.getName()),
                is(true));
        resolvePage.applyFraudManagement("test comment");
        AbuserFraudType abuserFraudType = getObjectsFromDB(
                        DbName.POSTGRES,
                        AR_ABUSER_FRAUD_TYPE_TABLE_NAME,
                        String.format("ucid = '%s' and fraud_type_code = '%s'", client.getUcid(), ATO.getCode()),
                        AbuserFraudType.class)
                .getFirst();
        assertThat(
                "Verify deleted fraud type has status CLEANED", abuserFraudType.getStatus(), is(CLEANED.getStatus()));
    }
}
