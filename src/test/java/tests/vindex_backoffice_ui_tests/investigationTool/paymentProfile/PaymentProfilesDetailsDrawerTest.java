package tests.vindex_backoffice_ui_tests.investigationTool.paymentProfile;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_credit_card_table.CrmTbCreditCardObject;
import business_objects.db.clickhouse.crm_tb_credit_card_table.CrmTbCreditCardObjectFactory;
import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.crm_tb_withdraw_account.CrmTbWithdrawAccountObject;
import business_objects.db.clickhouse.crm_tb_withdraw_account.CrmTbWithdrawAccountObjectFactory;
import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalObject;
import business_objects.db.clickhouse.mt_account.MtAccountObject;
import helpers.data.ClientHelper;
import helpers.database.DbName;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateStaticCrmTbAccountActive;
import static business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositObjectFactory.generateDepositByClient;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateStaticUserByClient;
import static business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalObjectFactory.generateCrmTbWithdrawalObjectByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static helpers.api.AbuseRegistryHelper.addFraudForClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.FraudType.MARKET_MANIPULATION;
import static helpers.data.enums.FraudTypeStatus.POTENTIAL;
import static helpers.database.DbHelper.*;
import static helpers.database.OperationsHelper.cleanUserPaymentsDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static utils.Constants.*;
import static utils.Utils.getRandomIntPositive;
import static utils.Utils.insertCrmAccountsToDb;

@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_WEB)
@Feature("BMS-2063 - Payment profiles details drawer")
class PaymentProfilesDetailsDrawerTest extends TestBaseWeb {

    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static CrmTbUserObject crmTbUser = generateStaticUserByClient(client);
    private static final ClientHelper client2 = getRandomVantageClientAllFields();
    private static CrmTbUserObject crmTbUser2 = generateStaticUserByClient(client2);
    private static CrmTbAccountObject account2 = generateStaticCrmTbAccountActive(client2);
    private static CrmTbAccountObject account1 = generateStaticCrmTbAccountActive(client);
    private static MtAccountObject mtAccount1 = generateMtAccountByCrmTbAccount(account1);
    private static MtAccountObject mtAccount2 = generateMtAccountByCrmTbAccount(account2);
    private static CrmTbDepositObject deposit;
    private static CrmTbDepositObject deposit2;
    private static CrmTbDepositObject deposit3;
    private static CrmTbWithdrawalObject withdrawal1;
    private static CrmTbWithdrawalObject withdrawal2;
    private static CrmTbCreditCardObject cardObject;
    private static CrmTbWithdrawAccountObject crmTbWithdrawAccountObject;

    @BeforeAll
    static void setup() throws IOException {
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser2);
        insertCrmAccountsToDb(account1);
        insertCrmAccountsToDb(account2);
        insertObjectsToDb(MT_ACCOUNT_TABLE_NAME, List.of(mtAccount1, mtAccount2));
        deposit = generateDepositByClient(client);
        deposit2 = generateDepositByClient(client);
        deposit3 = generateDepositByClient(client2);
        withdrawal1 = generateCrmTbWithdrawalObjectByClient(client);
        deposit.paymentProfile = "Cryptocurrency " + getRandomIntPositive();
        deposit2.paymentProfile = "Cryptocurrency " + getRandomIntPositive();
        withdrawal1.paymentProfile = "Card";
        deposit.paymentType = "Cryptocurrency";
        deposit2.paymentType = "Cryptocurrency";
        withdrawal1.paymentType = "Card";
        withdrawal1.paymentChannel = "USDT(BEP20)-CPS";
        deposit.paymentChannel = "Offline";
        deposit2.paymentChannel = "Offline";
        withdrawal1.isDel = 0;
        deposit.isDel = 0;
        deposit2.isDel = 0;
        deposit.paymentFamily = "Crypto";
        deposit2.paymentFamily = "Crypto";
        withdrawal1.paymentFamily = "Card";
        deposit.amount = 100.0;
        deposit.amountUsd = 101.12;
        deposit2.amount = 100.0;
        deposit2.amountUsd = 102.12;
        withdrawal1.amount = 100.0;
        withdrawal1.amountUsd = 101.01;
        withdrawal1.reversedAmount = 0.0;
        withdrawal1.reversedAmountUsd = 0.0;

        deposit3.paymentProfile = deposit.paymentProfile;
        deposit3.paymentType = deposit.paymentType;
        deposit3.paymentChannel = deposit.paymentChannel;
        deposit3.isDel = 0;
        deposit3.paymentFamily = deposit.paymentFamily;
        deposit3.amount = 10.01;
        deposit3.amountUsd = 11.01;

        withdrawal2 = generateCrmTbWithdrawalObjectByClient(client);
        withdrawal2.paymentProfile = "LBT1";
        withdrawal2.paymentType = "Card";
        withdrawal2.paymentFamily = "LBT";
        withdrawal2.paymentChannel = "USDT(BEP20)-CPS";
        withdrawal2.isDel = 0;
        withdrawal2.paymentFamily = "LBT";
        withdrawal2.amount = 10.0;
        withdrawal2.amountUsd = 11.01;
        withdrawal2.reversedAmount = 0.0;
        withdrawal2.reversedAmountUsd = 0.0;
        crmTbWithdrawAccountObject = CrmTbWithdrawAccountObjectFactory.generateByClient(client);
        cardObject = CrmTbCreditCardObjectFactory.generateDepositByClient(client);
        insertObjectsToDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, List.of(withdrawal1, withdrawal2));
        insertObjectsToDb(CRM_TB_WITHDRAW_ACCOUNT_TABLE_NAME, List.of(crmTbWithdrawAccountObject));
        insertObjectToDb(CRM_TB_CREDIT_CARD_TABLE_NAME, cardObject);
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, deposit);
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, deposit2);
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, deposit3);
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, deposit3);
        addFraudForClient(client2, MARKET_MANIPULATION, POTENTIAL, List.of());
    }

    @AfterAll
    static void teardown() throws Exception {
        cleanUserPaymentsDb(client.getUcid());
        cleanUserPaymentsDb(client2.getUcid());
        deleteEntryFromDb(DbName.CLICKHOUSE, CRM_TB_CREDIT_CARD_TABLE_NAME, "user_id='%s'".formatted(crmTbUser.userId));
        deleteEntryFromDb(DbName.CLICKHOUSE, CRM_TB_WITHDRAW_ACCOUNT_TABLE_NAME, "user_id='%s'".formatted(crmTbUser.userId));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1760")
    @DisplayName("Payment profiles details test")
    void paymentProfileDetailsTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        paymentsPage.navigatePaymentsTab(client.getUcid());
        paymentsPage.clickPaymentProfilesTabButton();
        paymentsPage.openPaymentProfileDetails(deposit.paymentProfile);
        Map<String, String> paymentProfileDetailsRows = paymentsPage.getPaymentProfileDetailsRows();
        assertThat("Verify payment profile details rows", paymentProfileDetailsRows.size(), equalTo(4));
        assertThat("Verify payment profile details rows", paymentProfileDetailsRows.get("Family"), equalTo(deposit.paymentFamily));
        assertThat("Verify payment profile details rows", paymentProfileDetailsRows.get("Type"), equalTo(deposit.paymentType));
        assertThat("Verify payment profile details rows", paymentProfileDetailsRows.get("System"), equalTo(deposit.paymentChannel));
        assertThat("Verify payment profile details rows", paymentProfileDetailsRows.get("Wallet"), equalTo(deposit.paymentDetails));
        List<String> paymentProfileDetailsTotals = paymentsPage.getPaymentProfileDetailsTotals();
        String wdTotal = paymentProfileDetailsTotals.get(0);
        String depTotal = paymentProfileDetailsTotals.get(1);
        String netDepTotal = paymentProfileDetailsTotals.get(2);
        assertThat("Verify withdrawal total", wdTotal, equalTo(String.format("%s USD1 deposit50%% of all client’s deposits", deposit.amountUsd)));
        assertThat("Verify deposit total", depTotal, equalTo("0 USD0 withdrawalsno withdrawals"));
        assertThat("Verify net deposit total", netDepTotal, equalTo(String.format("%s USDnet deposit", deposit.amountUsd)));
        paymentsPage.openPaymentProfileDetailsConnectedClients();
        List<String> paymentProfileDetailsConnectedClients = paymentsPage.getPaymentProfileDetailsConnectedClients();
        assertThat("Verify connected clients size", paymentProfileDetailsConnectedClients.size(), equalTo(2));
        String user2 = paymentProfileDetailsConnectedClients.stream().filter(x -> x.contains(String.valueOf(crmTbUser2.userId))).findFirst().get();
        String format = String.format("%s %s%sMarket manipulation%s%s%s", crmTbUser2.firstName, crmTbUser2.lastName, crmTbUser2.userId, deposit3.amountUsd,               // deposit
                "0",                    // wd
                LocalDate.now()         // date
        );
        assertThat("Verify connected client", user2, containsString(format));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1761")
    @DisplayName("Payment profiles details card test")
    void paymentProfileDetails2Test() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        paymentsPage.navigatePaymentsTab(client.getUcid());
        paymentsPage.clickPaymentProfilesTabButton();
        paymentsPage.openPaymentProfileDetails(withdrawal1.paymentProfile);
        Map<String, String> paymentProfileDetailsRows = paymentsPage.getPaymentProfileDetailsRows();
        assertThat("Verify payment profile details rows", paymentProfileDetailsRows.size(), equalTo(10));
        assertThat("Verify payment profile details rows", paymentProfileDetailsRows.get("Family"), equalTo(withdrawal1.paymentFamily));
        assertThat("Verify payment profile details rows", paymentProfileDetailsRows.get("Type"), equalTo(withdrawal1.paymentType));
        assertThat("Verify payment profile details rows", paymentProfileDetailsRows.get("System"), equalTo(withdrawal1.paymentChannel));
        assertThat("Verify payment profile details rows", paymentProfileDetailsRows.get("Card Number"), equalTo(String.format("%s***%s", cardObject.getCardBeginSixDigits(), cardObject.getCardLastFourDigits())));
        assertThat("Verify payment profile details rows", paymentProfileDetailsRows.get("BIN"), equalTo(cardObject.cardBeginSixDigits));
        assertThat("Verify payment profile details rows", paymentProfileDetailsRows.get("Holder"), equalTo(cardObject.cardHolderName));
        assertThat("Verify payment profile details rows", paymentProfileDetailsRows.get("Expiry"), equalTo(String.format("%s/%s", cardObject.expiryMonth, cardObject.expiryYear.substring(2))));
        assertThat("Verify payment profile details rows", paymentProfileDetailsRows.get("3DS"), equalTo("Yes"));
        assertThat("Verify payment profile details rows", paymentProfileDetailsRows.get("Bank"), equalTo("Revolut Bank Uab"));
        assertThat("Verify payment profile details rows", paymentProfileDetailsRows.get("Country").strip(), equalTo("LITHUANIA"));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1762")
    @DisplayName("Payment profiles details LBT test")
    void paymentProfileDetails3Test() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        paymentsPage.navigatePaymentsTab(client.getUcid());
        paymentsPage.clickPaymentProfilesTabButton();
        paymentsPage.openPaymentProfileDetails(withdrawal2.paymentProfile);
        Map<String, String> paymentProfileDetailsRows = paymentsPage.getPaymentProfileDetailsRows();
        assertThat("Verify payment profile details rows", paymentProfileDetailsRows.size(), equalTo(6));
        assertThat("Verify payment profile details rows", paymentProfileDetailsRows.get("Family"), equalTo(withdrawal2.paymentFamily));
        assertThat("Verify payment profile details rows", paymentProfileDetailsRows.get("Type"), equalTo(withdrawal2.paymentType));
        assertThat("Verify payment profile details rows", paymentProfileDetailsRows.get("System"), equalTo(withdrawal2.paymentChannel));
        assertThat("Verify payment profile details rows", paymentProfileDetailsRows.get("Account"), equalTo(crmTbWithdrawAccountObject.bankCard));
        assertThat("Verify payment profile details rows", paymentProfileDetailsRows.get("Bank Name"), equalTo(crmTbWithdrawAccountObject.bankName));
        assertThat("Verify payment profile details rows", paymentProfileDetailsRows.get("Swift"), equalTo(crmTbWithdrawAccountObject.swiftCode));

    }


}
