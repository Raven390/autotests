
package tests.vindex_backoffice_ui_tests.investigationTool;

import business_objects.db.abuse_registry_db.AbuserFraudType;
import business_objects.db.backoffice_db.client.Client;
import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.mt_account.MtAccountObject;
import business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObject;
import business_objects.db.clickhouse.mt_mt5_positions.MtMt5PositionsObject;
import helpers.data.ClientHelper;
import helpers.database.DbName;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.util.List;

import static business_objects.db.backoffice_db.client.ClientFactory.generateCrmTbAccountData;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObjectFactory.generateMt4TradesCoercedAccountProfitComment;
import static business_objects.db.clickhouse.mt_mt5_positions.MtMt5PositionsObjectFactory.generateMtMt5PositionsObject;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.Currency.USD;
import static helpers.data.enums.FraudType.CHARGEBACK;
import static helpers.data.enums.FraudTypeStatus.CONFIRMED;
import static helpers.database.ArHelper.deleteUserFromAbuseRegistry;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static utils.Constants.*;
import static utils.Utils.getRandomIntPositive;

@Feature("Payment Team manage fraud types without active payment alerts")
class PaymentTeamManageFraudWithoutAlertsTest extends TestBaseWeb {

    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(client);

    private static MtAccountObject mtAccount1;
    private static MtAccountObject mtAccount2;
    private static MtMt4TradesCoercedObject trade1;
    private static MtMt4TradesCoercedObject trade2;

    @BeforeAll
    static void setup() {
        CrmTbAccountObject account1 = generateCrmTbAccountDataForUi(client);
        account1.currency = USD.getCode();
        CrmTbAccountObject account2 = generateCrmTbAccountDataForUi(client);
        account2.account = getRandomIntPositive();
        account2.currency = USD.getCode();
        mtAccount1 = generateMtAccountByCrmTbAccount(account1);
        mtAccount2 = generateMtAccountByCrmTbAccount(account2);

        String comment = "comment";
        trade1 = generateMt4TradesCoercedAccountProfitComment(account1, 500.12, comment);
        trade2 = generateMt4TradesCoercedAccountProfitComment(account2, 1000.23, comment);

        Client boClient = generateCrmTbAccountData(client);

        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertObjectsToDb(CRM_TB_ACCOUNT_TABLE_NAME, List.of(account1, account2));
        insertObjectsToDb(MT_ACCOUNT_TABLE_NAME, List.of(mtAccount1, mtAccount2));
        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade1, trade2));
        MtMt5PositionsObject position = generateMtMt5PositionsObject(client);
        position.setAccount(mtAccount2.account);
        position.setServerId(mtAccount2.sourceIdSt);
        insertObjectToDb(MT5_POSITIONS_TABLE_NAME, position);
        insertObjectToDb(DbName.POSTGRES, BO_CLIENT_TABLE_NAME, boClient);
    }

    @AfterEach
    void teardownEach() throws Exception {
        deleteUserFromAbuseRegistry(client.getUcid());
        closeAlert(crmTbUser.ucid);
    }

    @AfterAll
    static void teardown() throws Exception {
        deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteEntryFromDb(MT4_TRADES_COERCED_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteEntryFromDb(MT5_POSITIONS_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteUserFromAbuseRegistry(client.getUcid());
        deleteEntryFromDb(DbName.POSTGRES, BO_CLIENT_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        closeAlert(crmTbUser.ucid);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1788")
    @DisplayName("Payment Team can manage PAYMENT fraud type without active payment alerts - verify UI blocks")
    void paymentTeamManagePaymentFraudWithoutAlerts() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsPaymentTeamUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        resolvePage.openReportFraudForm();

        // Add PAYMENT fraud type (CHARGEBACK)
        resolvePage.reportFraud(CHARGEBACK, CONFIRMED);

        // Verify that "Previously reported" section is visible
        List<String> previouslyReportedFrauds = resolvePage.getPreviouslyReportedFraudItems2();
        assertThat("Verify that previously reported section is displayed", previouslyReportedFrauds, hasSize(0));

        // Verify that "Detected fraud" section is visible (selected fraud should be displayed)
        String selectedFraud = resolvePage.getSelectedFraud();
        assertThat("Verify that detected fraud section shows selected fraud", selectedFraud.contains("Chargeback"), is(true));

        // Verify that "Restrictions" section is visible1
        List<String> restrictionsList = resolvePage.getRestrictionsList();
        assertThat("Verify that restrictions section is accessible", restrictionsList.isEmpty(), is(false));

        // Verify that "Suggested Deduction" block is NOT displayed
        boolean isSuggestedDeductionVisible = resolvePage.isSuggestedDeductionSectionVisible();
        assertThat("Verify that Suggested Deduction block is NOT displayed", isSuggestedDeductionVisible, is(false));

        // Verify that comment input is visible
        resolvePage.fillCommentAndApply("Payment fraud management test");
        List<AbuserFraudType> abuserFraudTypes = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_FRAUD_TYPE_TABLE_NAME, "ucid = '%s'".formatted(client.getUcid()), AbuserFraudType.class);
        assertThat(abuserFraudTypes.size(), is(1));
        AbuserFraudType abuserFraudType = abuserFraudTypes.getFirst();
        assertThat("Fraud type inserted into AR DB", abuserFraudType.getFraudTypeCode(), is(CHARGEBACK.getCode()));
    }
}