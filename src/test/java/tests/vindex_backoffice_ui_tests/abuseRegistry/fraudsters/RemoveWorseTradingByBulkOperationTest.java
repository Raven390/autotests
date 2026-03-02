package tests.vindex_backoffice_ui_tests.abuseRegistry.fraudsters;

import static business_objects.api.mitigation_service.RestrictionStatus.APPLIED;
import static business_objects.api.mitigation_service.RestrictionStatus.CANCELLED;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static helpers.api.MitigationHelper.deleteTradingEnvironmentRestrictions;
import static helpers.api.RestrictionHelper.putWorseTradingRestriction;
import static helpers.api.RestrictionHelper.waitUntilWorseTradingRestrictionHasStatusAndLevel;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.Brand.VANTAGE;
import static helpers.data.enums.Restriction.WORSE_TRADING;
import static helpers.data.enums.TradingEnvironmentLevel.LOW;
import static helpers.data.enums.TradingEnvironmentLevel.MEDIUM;
import static helpers.database.AuHelper.cleanClientAudit;
import static helpers.database.BoHelper.deleteUserBO;
import static helpers.database.CleanTableHelper.cleanCrmUserTableByClient;
import static helpers.database.DbHelper.*;
import static helpers.database.DbName.POSTGRES;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static utils.Constants.*;
import static utils.Utils.*;
import static utils.Utils.getRandomIntPositive;

import business_objects.api.mitigation_service.RestrictionStatus;
import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.mitigation_service_db.client_trading_environment_restriction.ClientTradingEnvironmentRestrictionEntity;
import helpers.data.ClientHelper;
import io.qameta.allure.AllureId;
import java.math.BigInteger;
import java.util.List;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

class RemoveWorseTradingByBulkOperationTest extends TestBaseWeb {
    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final CrmTbAccountObject account = generateCrmTbAccountDataForUi(client);
    private static final CrmTbAccountObject account2 = generateCrmTbAccountDataForUi(client);

    private static final String USER = "Autotest User";

    @BeforeAll
    static void setup() {
        insertObjectToDb(CRM_USER_TABLE_NAME, generateUserByClient(client));
        account2.account = getRandomIntPositive();
        insertObjectsToDb(
                MT_ACCOUNT_TABLE_NAME,
                List.of(generateMtAccountByCrmTbAccount(account), generateMtAccountByCrmTbAccount(account2)));
        insertCrmAccountsToDb(account, account2);
    }

    @AfterAll
    static void teardown() throws Exception {
        deleteUserBO(client.getUcid());
        cleanClientAudit(client.getUcid());
        deleteTradingEnvironmentRestrictions(client.getUcid());
        cleanCrmUserTableByClient(client.getUcid());
    }

    @Test
    @AllureId("2447")
    @DisplayName("Bulk remove Worse Trading restriction test")
    void bulkRemoveWorseTradingTest() throws Exception {

        String correlationId = getRandomUuidString();
        BigInteger accountID = BigInteger.valueOf(account.account);
        BigInteger accountID2 = BigInteger.valueOf(account2.account);
        String addLevelComment = "Add worse trading restriction";
        putWorseTradingRestriction(
                client.getUcid(), accountID, correlationId, account.getServerIdSt(), LOW, addLevelComment, USER);
        waitUntilWorseTradingRestrictionHasStatusAndLevel(accountID, LOW, APPLIED);
        putWorseTradingRestriction(
                client.getUcid(), accountID2, correlationId, account2.getServerIdSt(), MEDIUM, addLevelComment, USER);
        waitUntilWorseTradingRestrictionHasStatusAndLevel(accountID2, MEDIUM, APPLIED);

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsTradingOpsSeniorUser();
        fraudstersPage.navigateAbuseRegistryFraudsters();
        fraudstersPage.openRemoveDrawer();
        fraudstersPage.selectBrandToUpload(VANTAGE.getDisplayName());
        fraudstersPage.typeClientID(client.getUserId().toString());
        fraudstersPage.clickAddRestrictionButton();
        fraudstersPage.selectRestriction(WORSE_TRADING.getDescription());
        fraudstersPage.clickApplyselectedRestrictions();
        String commentary = "Delete 'Worse trading' restriction " + getCurrentTimestampSeconds();
        fraudstersPage.fillCommentary(commentary);
        fraudstersPage.clickDeleteUpload();
        fraudstersPage.verifySuccessMessageDelete();

        waitUntilWorseTradingRestrictionHasStatusAndLevel(accountID, LOW, CANCELLED);

        List<ClientTradingEnvironmentRestrictionEntity> entities = getObjectsFromDB(
                POSTGRES,
                MITIGATION_CLIENT_TRADING_ENVIRONMENT_RESTRICTION,
                String.format("ucid='%s'", account.getUcid()),
                ClientTradingEnvironmentRestrictionEntity.class);

        assertNotNull(entities, "Restriction list should not be null");
        assertEquals(2, entities.size(), "Verify exactly 2 restrictions for UCID: " + account.getUcid());

        entities.forEach(entity -> assertEquals(
                RestrictionStatus.CANCELLED,
                entity.getStatus(),
                "Expected status CANCELLED, but got " + entity.getStatus() + " for record ID: " + entity.getId()));
    }
}
