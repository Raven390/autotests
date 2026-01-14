package tests.vindex_backoffice_ui_tests.investigationTool.paymentProfile;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateStaticCrmTbAccountActive;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateStaticUserByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.DbHelper.*;
import static helpers.database.OperationsHelper.cleanUserPaymentsDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static utils.Constants.*;
import static utils.Utils.*;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntity;
import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntityFactory;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalEntity;
import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalEntityFactory;
import business_objects.db.clickhouse.mt_account.MtAccountObject;
import helpers.data.ClientHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.*;
import page_objects.backoffice_pages.investigationTool.PaymentsPage;
import tests.TestBaseWeb;

@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_WEB)
@Feature("BMS-2062 - Display Payment profiles list")
class PaymentProfilesListTest extends TestBaseWeb {

    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static CrmTbUserObject crmTbUser = generateStaticUserByClient(client);
    private static CrmTbAccountObject account1 = generateStaticCrmTbAccountActive(client);
    private static MtAccountObject mtAccount1 = generateMtAccountByCrmTbAccount(account1);
    private static CrmTbDepositEntity deposit;
    private static CrmTbDepositEntity deposit2;
    private static CrmTbWithdrawalEntity withdrawal1;
    private static CrmTbWithdrawalEntity withdrawalPending;
    private static CrmTbDepositEntity depositPending;

    @BeforeAll
    static void setup() {
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertCrmAccountsToDb(account1);
        insertObjectsToDb(MT_ACCOUNT_TABLE_NAME, List.of(mtAccount1));
        deposit = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client);
        deposit2 = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client);
        withdrawal1 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client);
        deposit.setPaymentProfile("Cryptocurrency " + getRandomIntPositive());
        deposit.setPaymentProfileKey(deposit.getPaymentProfile());
        deposit2.setPaymentProfile("Cryptocurrency " + getRandomIntPositive());
        deposit2.setPaymentProfileKey(deposit2.getPaymentProfile());
        withdrawal1.setPaymentProfile("Card" + getRandomIntPositive());
        withdrawal1.setPaymentProfileKey("Card" + getRandomIntPositive());
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
        withdrawal1.setPaymentFamily("LBT");
        deposit.setAmount(BigDecimal.valueOf(100.0));
        deposit.setAmountUsd(BigDecimal.valueOf(101.12));
        deposit2.setAmount(BigDecimal.valueOf(100.0));
        deposit2.setAmountUsd(BigDecimal.valueOf(102.12));
        withdrawal1.setAmount(BigDecimal.valueOf(100.0));
        withdrawal1.setAmountUsd(BigDecimal.valueOf(101.01));
        withdrawal1.setReversedAmount(BigDecimal.ZERO);
        withdrawal1.setReversedAmountUsd(BigDecimal.ZERO);
        withdrawalPending = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client);
        withdrawalPending.setPaymentProfile("Card" + getRandomIntPositive());
        withdrawalPending.setPaymentProfileKey("Card" + getRandomIntPositive());
        withdrawalPending.setPaymentType("Card");
        withdrawalPending.setPaymentChannel("USDT(BEP20)-CPS");
        withdrawalPending.setPaymentFamily("LBT");
        withdrawalPending.setIsDel(0);
        withdrawalPending.setStatusGroup("Pending");
        withdrawalPending.setStatusId(9);
        depositPending = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client);
        depositPending.setPaymentProfile("V-Wallet " + getRandomIntPositive());
        depositPending.setPaymentProfileKey(depositPending.getPaymentProfile());
        depositPending.setPaymentType("V-Wallet");
        depositPending.setPaymentFamily("V-Wallet");
        depositPending.setIsDel(0);
        depositPending.setStatusGroup("Pending");
        depositPending.setStatusId(6);
        insertObjectsToDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, List.of(withdrawal1, withdrawalPending));
        insertObjectsToDb(CRM_DEPOSIT_TABLE_NAME, List.of(deposit, deposit2, depositPending));
    }

    @AfterAll
    static void teardown() throws Exception {
        cleanUserPaymentsDb(client.getUcid());
        deleteEntryFromDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, String.format("ucid = '%s'", client.getUcid()));
        deleteEntryFromDb(CRM_DEPOSIT_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1745")
    @DisplayName("Payment profiles list test")
    void paymentProfileTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        paymentsPage.navigatePaymentsTab(client.getUcid());
        paymentsPage.clickPaymentProfilesTabButton();
        List<PaymentsPage.PaymentFamilyBlock> paymentProfilesList = paymentsPage.getPaymentProfilesList();
        assertThat("Verify payment profiles is not empty", paymentProfilesList.size(), equalTo(3));
        PaymentsPage.PaymentFamilyBlock lbtPaymentFamily = paymentProfilesList.stream()
                .filter(x -> x.header().contains("LBT"))
                .findFirst()
                .get();
        assertThat(
                "Verify LBT payment family header ",
                lbtPaymentFamily.header(),
                equalTo(String.format(
                        "LBT%d profiles %d USD %.2f USD No connections", 2, 0, withdrawal1.getAmountUsd())));
        assertThat(
                "Verify LBT payment profiles",
                lbtPaymentFamily.rowDataList().getFirst(),
                equalTo(String.format(
                        "%sNot verified%d USDNo deposits%.2f USD1 withdrawal0 clientsConnected",
                        withdrawal1.getPaymentProfile(), 0, withdrawal1.getAmountUsd())));
        assertThat(
                "Verify LBT payment profiles with pending status",
                lbtPaymentFamily.rowDataList().getLast(),
                equalTo(String.format(
                        "%sNot verified0 USDNo deposits0 USDNo withdrawals0 clientsConnected",
                        withdrawalPending.getPaymentProfile())));

        PaymentsPage.PaymentFamilyBlock cryptoPaymentFamily = paymentProfilesList.stream()
                .filter(x -> x.header().contains("Crypto"))
                .findFirst()
                .get();
        assertThat(
                "Verify Crypto payment family header ",
                cryptoPaymentFamily.header(),
                equalTo(String.format(
                        "Crypto%d profiles %.2f USD 0 USD No connections",
                        2, deposit.getAmountUsd().add(deposit2.getAmountUsd()))));
        assertThat(
                "Verify Crypto payment profiles 1",
                cryptoPaymentFamily.rowDataList().stream()
                        .filter(x -> x.contains(deposit.getPaymentProfile()))
                        .findFirst()
                        .get(),
                equalTo(String.format(
                        "%sNot verified%.2f USD%d deposit0 USDNo withdrawals%d clientsConnected",
                        deposit.getPaymentProfile(), deposit.getAmountUsd(), 1, 0)));
        assertThat(
                "Verify Crypto payment profiles 2",
                cryptoPaymentFamily.rowDataList().stream()
                        .filter(x -> x.contains(deposit2.getPaymentProfile()))
                        .findFirst()
                        .get(),
                equalTo(String.format(
                        "%sNot verified%.2f USD%d deposit0 USDNo withdrawals%d clientsConnected",
                        deposit2.getPaymentProfile(), deposit2.getAmountUsd(), 1, 0)));
        PaymentsPage.PaymentFamilyBlock vWalletPaymentFamily = paymentProfilesList.stream()
                .filter(x -> x.header().contains("V-Wallet"))
                .findFirst()
                .get();
        assertThat(
                "Verify V-Wallet payment family header",
                vWalletPaymentFamily.header(),
                equalTo("V-Wallet1 profile 0 USD 0 USD No connections"));
        assertThat(
                "Verify V-Wallet payment profiles with pending status",
                vWalletPaymentFamily.rowDataList().getFirst(),
                equalTo(String.format(
                        "%sNot verified0 USDNo deposits0 USDNo withdrawals0 clientsConnected",
                        depositPending.getPaymentProfile())));
    }
}
