package tests.vindex_backoffice_ui_tests.investigationTool.paymentProfile;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateStaticCrmTbAccountActive;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateStaticUserByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.DbHelper.insertObjectToDb;
import static helpers.database.DbHelper.insertObjectsToDb;
import static helpers.database.OperationsHelper.cleanUserPaymentsDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static utils.Constants.*;
import static utils.Utils.getRandomIntPositive;
import static utils.Utils.insertCrmAccountsToDb;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntity;
import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntityFactory;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalEntity;
import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalEntityFactory;
import business_objects.db.clickhouse.mt_account.MtAccountObject;
import helpers.data.ClientHelper;
import helpers.data.enums.VerificationStatus;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.*;
import page_objects.backoffice_pages.investigationTool.PaymentsPage;
import tests.TestBaseWeb;

@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_WEB)
@Feature("BMS-2160 - Display and set verification mark (status)")
class PaymentProfilesVerificationTest extends TestBaseWeb {

    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static CrmTbUserObject crmTbUser = generateStaticUserByClient(client);
    private static CrmTbAccountObject account1 = generateStaticCrmTbAccountActive(client);
    private static MtAccountObject mtAccount1 = generateMtAccountByCrmTbAccount(account1);
    private static CrmTbDepositEntity deposit;
    private static CrmTbWithdrawalEntity withdrawal1;

    @BeforeAll
    static void setup() {
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertCrmAccountsToDb(account1);
        insertObjectsToDb(MT_ACCOUNT_TABLE_NAME, List.of(mtAccount1));
        deposit = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client);
        withdrawal1 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client);
        deposit.setPaymentProfile("Cryptocurrency " + getRandomIntPositive());
        withdrawal1.setPaymentProfile("Card");
        deposit.setPaymentType("Cryptocurrency");
        deposit.setPaymentProfileKey("Card" + getRandomIntPositive());
        deposit.setStatusId(5);
        withdrawal1.setPaymentType("Card");
        withdrawal1.setPaymentChannel("USDT(BEP20)-CPS");
        deposit.setPaymentChannel("Offline");
        withdrawal1.setIsDel(0);

        deposit.setPaymentFamily("Crypto");
        withdrawal1.setPaymentFamily("LBT");
        deposit.setAmount(BigDecimal.valueOf(100.0));
        deposit.setAmountUsd(BigDecimal.valueOf(101.12));

        withdrawal1.setAmount(BigDecimal.valueOf(100.0));
        withdrawal1.setAmountUsd(BigDecimal.valueOf(101.01));
        withdrawal1.setReversedAmount(BigDecimal.ZERO);
        withdrawal1.setReversedAmountUsd(BigDecimal.ZERO);
        insertObjectsToDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, List.of(withdrawal1));
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, deposit);
    }

    @AfterAll
    static void teardown() throws Exception {
        cleanUserPaymentsDb(client.getUcid());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1861")
    @DisplayName("Payment profile verification test")
    void paymentProfileVerificationTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        paymentsPage.navigatePaymentsTab(client.getUcid());
        paymentsPage.clickPaymentProfilesTabButton();
        paymentsPage.openPaymentProfileDetails(withdrawal1.getPaymentProfile());
        paymentsPage.openPaymentProfileVerificationDrawer();
        assertThat(
                "Verify payment profile name",
                paymentsPage.getPaymentProfileVerificationDrawerName(),
                equalTo(withdrawal1.getPaymentProfile()));
        paymentsPage.selectVerificationStatus(VerificationStatus.VERIFIED);
        paymentsPage.commentAndSendVerificationStatus("test");

        List<PaymentsPage.PaymentFamilyBlock> paymentProfilesList = paymentsPage.getPaymentProfilesList();
        PaymentsPage.PaymentFamilyBlock block = paymentProfilesList.stream()
                .filter(x -> x.header().contains("LBT"))
                .findFirst()
                .get();
        assertThat(
                "Verify status in payment profiles",
                block.rowDataList().getFirst(),
                containsString(VerificationStatus.VERIFIED.getDisplayName()));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1862")
    @DisplayName("Payment profile verification only comment test")
    void paymentProfileVerificationCommentTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        paymentsPage.navigatePaymentsTab(client.getUcid());
        paymentsPage.clickPaymentProfilesTabButton();
        paymentsPage.openPaymentProfileDetails(deposit.getPaymentProfile());
        paymentsPage.openPaymentProfileVerificationDrawer();
        assertThat(
                "Verify payment profile name",
                paymentsPage.getPaymentProfileVerificationDrawerName(),
                equalTo(deposit.getPaymentProfile()));
        paymentsPage.commentAndSendVerificationStatus("test");

        List<PaymentsPage.PaymentFamilyBlock> paymentProfilesList = paymentsPage.getPaymentProfilesList();
        PaymentsPage.PaymentFamilyBlock block = paymentProfilesList.stream()
                .filter(x -> x.header().contains("Crypto"))
                .findFirst()
                .get();
        assertThat(
                "Verify status in payment profiles",
                block.rowDataList().getFirst(),
                containsString(VerificationStatus.NOT_VERIFIED.getDisplayName()));
    }
}
