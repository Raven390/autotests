package tests.vindex_backoffice_ui_tests.investigationTool.paymentProfile;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateStaticCrmTbAccountActive;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateStaticUserByClient;
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

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_credit_card_table.CrmTbCreditCardObject;
import business_objects.db.clickhouse.crm_tb_credit_card_table.CrmTbCreditCardObjectFactory;
import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntity;
import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntityFactory;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalEntity;
import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalEntityFactory;
import business_objects.db.clickhouse.mt_account.MtAccountObject;
import helpers.data.ClientHelper;
import helpers.data.enums.VerificationStatus;
import helpers.database.DbName;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

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
    private static CrmTbDepositEntity deposit;
    private static CrmTbDepositEntity deposit2;
    private static CrmTbDepositEntity deposit3;
    private static CrmTbWithdrawalEntity withdrawal1;
    private static CrmTbWithdrawalEntity withdrawal2;
    private static CrmTbCreditCardObject cardObject;

    @BeforeAll
    static void setup() throws IOException {
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser2);
        insertCrmAccountsToDb(account1);
        insertCrmAccountsToDb(account2);
        insertObjectsToDb(MT_ACCOUNT_TABLE_NAME, List.of(mtAccount1, mtAccount2));
        deposit = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client);
        deposit2 = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client);
        deposit3 = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client2);
        withdrawal1 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client);
        deposit.setPaymentProfile("Cryptocurrency " + getRandomIntPositive());
        deposit.setPaymentProfileKey(deposit.getPaymentProfile());
        deposit2.setPaymentProfile("Cryptocurrency " + getRandomIntPositive());
        deposit2.setPaymentProfileKey(deposit2.getPaymentProfile());
        withdrawal1.setPaymentProfile("Card");
        withdrawal1.setPaymentProfileKey(withdrawal1.getPaymentProfile());
        deposit.setPaymentType("Cryptocurrency");
        deposit2.setPaymentType("Cryptocurrency");
        withdrawal1.setPaymentType("Card");
        withdrawal1.setPaymentChannel("USDT(BEP20)-CPS");
        deposit.setPaymentChannel("Offline");
        deposit2.setPaymentChannel("Offline");
        withdrawal1.setIsDel(0);
        deposit.setIsDel(0);
        deposit2.setIsDel(0);
        deposit.setPaymentFamily("Crypto");
        deposit2.setPaymentFamily("Crypto");
        withdrawal1.setPaymentFamily("Card");
        deposit.setAmount(BigDecimal.valueOf(100.0));
        deposit.setAmountUsd(BigDecimal.valueOf(101.12));
        deposit2.setAmount(BigDecimal.valueOf(100.0));
        deposit2.setAmountUsd(BigDecimal.valueOf(102.12));
        withdrawal1.setAmount(BigDecimal.valueOf(100.0));
        withdrawal1.setAmountUsd(BigDecimal.valueOf(101.01));
        withdrawal1.setReversedAmount(BigDecimal.ZERO);
        withdrawal1.setReversedAmountUsd(BigDecimal.ZERO);

        deposit3.setPaymentProfile(deposit.getPaymentProfile());
        deposit3.setPaymentProfileKey(deposit.getPaymentProfileKey());
        deposit3.setPaymentType(deposit.getPaymentType());
        deposit3.setPaymentChannel(deposit.getPaymentChannel());
        deposit3.setIsDel(0);
        deposit3.setPaymentFamily(deposit.getPaymentFamily());
        deposit3.setAmount(BigDecimal.valueOf(10.01));
        deposit3.setAmountUsd(BigDecimal.valueOf(11.01));

        withdrawal2 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client);
        withdrawal2.setPaymentProfile("LBT1");
        withdrawal2.setPaymentProfileKey(withdrawal2.getPaymentProfile());
        withdrawal2.setPaymentType("Card");
        withdrawal2.setPaymentFamily("LBT");
        withdrawal2.setPaymentChannel("USDT(BEP20)-CPS");
        withdrawal2.setIsDel(0);
        withdrawal2.setCreditCardId(withdrawal1.getCreditCardId());
        withdrawal2.setPaymentFamily("LBT");
        withdrawal2.setAmount(BigDecimal.valueOf(10.0));
        withdrawal2.setAmountUsd(BigDecimal.valueOf(11.01));
        withdrawal2.setReversedAmount(BigDecimal.ZERO);
        withdrawal2.setReversedAmountUsd(BigDecimal.ZERO);
        cardObject = CrmTbCreditCardObjectFactory.generateByClient(
                client, withdrawal1.getCreditCardId().intValue());
        insertObjectsToDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, List.of(withdrawal1, withdrawal2));
        insertObjectToDb(CRM_TB_CREDIT_CARD_TABLE_NAME, cardObject);
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, deposit);
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, deposit2);
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, deposit3);
        executeQueryToDb(
                DbName.CLICKHOUSE,
                String.format(
                        "INSERT INTO consolidated.client_payment_info (user_id, brand, regulator, ucid, country, bank_name, bank_address, account_number, beneficiary_name, holder_address, swift, sort_code, bsb_code, bank_account_name, bank_branch_name, bank_city, bank_province, ifsc_code, is_del, last_updated) VALUES(%s, 'Vantage', 'VFSC2', '%s', '', 'Revolut Bank UAB', 'Konstitucijos ave. 21B, 08130, Vilnius, Lithuania', 'LT133250055915934239', 'Lolita Reid', 'Lolita Reid', 'REVOLT21', 'test sort code', 'test bsb code', 'Lolita Reid', '', '', '', 'test ifsc code', 0, '2025-11-04 14:08:55.000');",
                        client.getUserId(), client.getUcid()));
        addFraudForClient(client2, MARKET_MANIPULATION, POTENTIAL, List.of());
        // add verification status to client2
        executeQueryToDb(
                DbName.POSTGRES,
                String.format(
                        "INSERT INTO ve.verification_history (ucid, payment_profile_key, status, \"comment\", changed_by_username, changed_by_system, changed_at) VALUES('%s', '%s', '%s', 'comment', 'username', 'system', '2025-11-18 15:28:56.461');",
                        client2.getUcid(), deposit3.getPaymentProfileKey(), VerificationStatus.VERIFIED));
    }

    @AfterAll
    static void teardown() throws Exception {
        cleanUserPaymentsDb(client.getUcid());
        cleanUserPaymentsDb(client2.getUcid());
        deleteObjectFromDb(
                DbName.CLICKHOUSE, CRM_TB_CREDIT_CARD_TABLE_NAME, "user_id='%s'".formatted(crmTbUser.userId));
        deleteObjectFromDb(
                DbName.CLICKHOUSE, CRM_TB_WITHDRAW_ACCOUNT_TABLE_NAME, "user_id='%s'".formatted(crmTbUser.userId));
        deleteObjectFromDb(
                DbName.CLICKHOUSE, CLIENT_PAYMENT_INFO_TABLE_NAME, "user_id='%s'".formatted(crmTbUser.userId));
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
        paymentsPage.openPaymentProfileDetails(deposit.getPaymentProfile());
        Map<String, String> paymentProfileDetailsRows = paymentsPage.getPaymentProfileDetailsRows();
        assertThat("Verify payment profile details rows", paymentProfileDetailsRows.size(), equalTo(5));
        assertThat(
                "Verify payment profile details rows",
                paymentProfileDetailsRows.get("Family"),
                equalTo(deposit.getPaymentFamily()));
        assertThat(
                "Verify payment profile details rows",
                paymentProfileDetailsRows.get("Type"),
                equalTo(deposit.getPaymentType()));
        assertThat(
                "Verify payment profile details rows",
                paymentProfileDetailsRows.get("System"),
                equalTo(deposit.getPaymentChannel()));
        assertThat(
                "Verify payment profile details rows",
                paymentProfileDetailsRows.get("Wallet"),
                equalTo(deposit.getPaymentDetails()));
        List<String> paymentProfileDetailsTotals = paymentsPage.getPaymentProfileDetailsTotals();
        var wdTotal = paymentProfileDetailsTotals.get(0);
        var depTotal = paymentProfileDetailsTotals.get(1);
        var netDepTotal = paymentProfileDetailsTotals.get(2);
        assertThat(
                "Verify withdrawal total",
                wdTotal,
                equalTo(String.format("%s USD1 deposit50%% of all client’s deposits", deposit.getAmountUsd())));
        assertThat("Verify deposit total", depTotal, equalTo("0 USD0 withdrawalsno withdrawals"));
        assertThat(
                "Verify net deposit total",
                netDepTotal,
                equalTo(String.format("%s USDnet deposit", deposit.getAmountUsd())));
        paymentsPage.openPaymentProfileDetailsConnectedClients();
        List<String> paymentProfileDetailsConnectedClients = paymentsPage.getPaymentProfileDetailsConnectedClients();
        assertThat("Verify connected clients size", paymentProfileDetailsConnectedClients.size(), equalTo(1));
        var user2 = paymentProfileDetailsConnectedClients.stream()
                .filter(x -> x.contains(String.valueOf(crmTbUser2.userId)))
                .findFirst()
                .get();
        String format = String.format(
                "%s %s%sPotential Market manipulation%s%s%s%s",
                crmTbUser2.firstName,
                crmTbUser2.lastName,
                crmTbUser2.userId,
                deposit3.getAmountUsd(),
                "0",
                VerificationStatus.VERIFIED.getDisplayName(),
                LocalDate.now());
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
        paymentsPage.openPaymentProfileDetails(withdrawal1.getPaymentProfile());
        Map<String, String> paymentProfileDetailsRows = paymentsPage.getPaymentProfileDetailsRows();
        assertThat("Verify payment profile details rows", paymentProfileDetailsRows.size(), equalTo(11));
        assertThat(
                "Verify payment profile details rows",
                paymentProfileDetailsRows.get("Family"),
                equalTo(withdrawal1.getPaymentFamily()));
        assertThat(
                "Verify payment profile details rows",
                paymentProfileDetailsRows.get("Type"),
                equalTo(withdrawal1.getPaymentType()));
        assertThat(
                "Verify payment profile details rows",
                paymentProfileDetailsRows.get("System"),
                equalTo(withdrawal1.getPaymentChannel()));
        assertThat(
                "Verify payment profile details rows",
                paymentProfileDetailsRows.get("Card Number"),
                equalTo(String.format(
                        "%s***%s", cardObject.getCardBeginSixDigits(), cardObject.getCardLastFourDigits())));
        assertThat(
                "Verify payment profile details rows",
                paymentProfileDetailsRows.get("BIN"),
                equalTo(cardObject.cardBeginSixDigits));
        assertThat(
                "Verify payment profile details rows",
                paymentProfileDetailsRows.get("Holder"),
                equalTo(cardObject.cardHolderName));
        assertThat(
                "Verify payment profile details rows",
                paymentProfileDetailsRows.get("Expiry"),
                equalTo(String.format("%s/%s", cardObject.expiryMonth, cardObject.expiryYear.substring(2))));
        assertThat("Verify payment profile details rows", paymentProfileDetailsRows.get("3DS"), equalTo("Yes"));
        assertThat(
                "Verify payment profile details rows",
                paymentProfileDetailsRows.get("Bank"),
                equalTo("Revolut Bank Uab"));
        assertThat(
                "Verify payment profile details rows",
                paymentProfileDetailsRows.get("Country").strip(),
                equalTo("ROMANIA"));
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
        paymentsPage.openPaymentProfileDetails(withdrawal2.getPaymentProfile());
        Map<String, String> paymentProfileDetailsRows = paymentsPage.getPaymentProfileDetailsRows();
        assertThat("Verify payment profile details rows", paymentProfileDetailsRows.size(), equalTo(11));
        assertThat(
                "Verify payment profile details rows",
                paymentProfileDetailsRows.get("Family"),
                equalTo(withdrawal2.getPaymentFamily()));
        assertThat(
                "Verify payment profile details rows",
                paymentProfileDetailsRows.get("Type"),
                equalTo(withdrawal2.getPaymentType()));
        assertThat(
                "Verify payment profile details rows",
                paymentProfileDetailsRows.get("System"),
                equalTo(withdrawal2.getPaymentChannel()));
        assertThat(
                "Verify payment profile details rows",
                paymentProfileDetailsRows.get("Account"),
                equalTo("LT133250055915934239"));
        assertThat(
                "Verify payment profile details rows",
                paymentProfileDetailsRows.get("Bank Name"),
                equalTo("Revolut Bank UAB"));
        assertThat("Verify payment profile details rows", paymentProfileDetailsRows.get("Swift"), equalTo("REVOLT21"));
    }
}
