package tests.vindex_backoffice_ui_tests.investigationTool.paymentsTab;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.DbHelper.insertObjectToDb;
import static utils.Constants.*;
import static utils.Utils.insertCrmAccountsToDb;

import business_objects.db.clickhouse.crm_bp_callbacks.CrmBpCallbacksObject;
import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntity;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import helpers.data.ClientHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBaseWeb;

@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_WEB)
class PaymentsTabTransactionsTest extends TestBaseWeb {

    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(client);
    private static final CrmTbAccountObject account = generateCrmTbAccountDataForUi(client);
    private static CrmTbDepositEntity deposit;
    private static CrmBpCallbacksObject callback;

    @BeforeAll
    static void setup() {
        deposit = generateCrmTbDepositEntityByClient(client);
        deposit.setStatusGroup("Fail");
        deposit.setPaymentFamily("Card");
        callback = CrmBpCallbacksObject.builder()
                .ucid(deposit.getUcid())
                .orderNumber(deposit.getOrderNumber())
                .type("callback_deposit")
                .cardBrand(faker.finalFantasyXIV().job())
                .cardMaskedNumber("12128***1218")
                .declineReason(faker.chuckNorris().fact())
                .isFraudDeclined((short) 1)
                .cardHolderName(faker.name().fullName())
                .bin("123456")
                .cardExpirationDate("09/29")
                .is3d((short) 1)
                .cardIssuerCountryIso2("US")
                .build();
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertCrmAccountsToDb(account);
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, deposit);
        insertObjectToDb(CALLBACKS_TABLE_NAME, callback);
    }

    @Feature("BMS-3074 Add failed Card transactions callback info to transaction details")
    @DisplayName("Failed deposit have data from callback in table")
    @Test
    @AllureId("2141")
    void failedDepositInRowHavaCallbackData() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(client.getUcid());
        paymentsPage.navigateTransactions(client.getUcid());
        paymentsPage.checkMethodFamilyValue(deposit.getPaymentFamily());
        paymentsPage.checkMethodProfileValue(callback.getCardBrand() + " " + callback.getCardMaskedNumber());
    }

    @Feature("BMS-3074 Add failed Card transactions callback info to transaction details")
    @DisplayName("Failed deposit have data from callback in drawer")
    @Test
    @AllureId("2142")
    void failedDepositInDrawerHavaCallbackData() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(client.getUcid());
        paymentsPage.navigateTransactions(client.getUcid());
        paymentsPage.openTransactionDetainsDrawer(0);
        paymentsPage.checkTransactionAttributeValues(callback.getDeclineReason(), "Fail Reason");
        paymentsPage.checkTransactionAttributeValues("Yes", "Due To Fraud");
        paymentsPage.checkTransactionAttributeValues(callback.getCardHolderName(), "Entered Holder Name ");
        paymentsPage.checkTransactionAttributeValues(deposit.getPaymentFamily(), "Family");
        paymentsPage.checkTransactionAttributeValues(deposit.getPaymentType(), "Payment Type");
        paymentsPage.checkTransactionAttributeValues(deposit.getPaymentChannel(), "System");
        paymentsPage.checkTransactionAttributeValues(callback.getCardMaskedNumber(), "Card Number");
        paymentsPage.checkTransactionAttributeValues(callback.getBin(), "BIN");
        paymentsPage.checkTransactionAttributeValues(callback.getCardExpirationDate(), "Expiry");
        paymentsPage.checkTransactionAttributeValues("Yes", "3DS");
        paymentsPage.checkTransactionAttributeValues(" United States", "Country");
    }
}
