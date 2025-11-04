package tests.vindex_backoffice_ui_tests.investigationTool.paymentProfile;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalObject;
import business_objects.db.clickhouse.mt_account.MtAccountObject;
import helpers.data.ClientHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import page_objects.backoffice_pages.investigationTool.PaymentsPage;
import tests.TestBaseWeb;

import java.util.List;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateStaticCrmTbAccountActive;
import static business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositObjectFactory.generateDepositByClient;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateStaticUserByClient;
import static business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalObjectFactory.generateCrmTbWithdrawalObjectByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.DbHelper.insertObjectToDb;
import static helpers.database.DbHelper.insertObjectsToDb;
import static helpers.database.OperationsHelper.cleanUserPaymentsDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static utils.Constants.*;
import static utils.Utils.*;

@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_WEB)
@Feature("BMS-2062 - Display Payment profiles list")
class PaymentProfilesListTest extends TestBaseWeb {

    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static CrmTbUserObject crmTbUser = generateStaticUserByClient(client);
    private static CrmTbAccountObject account1 = generateStaticCrmTbAccountActive(client);
    private static MtAccountObject mtAccount1 = generateMtAccountByCrmTbAccount(account1);
    private static CrmTbDepositObject deposit;
    private static CrmTbDepositObject deposit2;
    private static CrmTbWithdrawalObject withdrawal1;

    @BeforeAll
    static void setup() {
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertCrmAccountsToDb(account1);
        insertObjectsToDb(MT_ACCOUNT_TABLE_NAME, List.of(mtAccount1));
        deposit = generateDepositByClient(client);
        deposit2 = generateDepositByClient(client);
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
        withdrawal1.paymentFamily = "LBT";
        deposit.amount = 100.0;
        deposit.amountUsd = 101.12;
        deposit2.amount = 100.0;
        deposit2.amountUsd = 102.12;
        withdrawal1.amount = 100.0;
        withdrawal1.amountUsd = 101.01;
        withdrawal1.reversedAmount = 0.0;
        withdrawal1.reversedAmountUsd = 0.0;
        insertObjectsToDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, List.of(withdrawal1));
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, deposit);
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, deposit2);
    }

    @AfterAll
    static void teardown() throws Exception {
        cleanUserPaymentsDb(client.getUcid());
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
        assertThat("Verify payment profiles is not empty", paymentProfilesList.size(), equalTo(2));
        PaymentsPage.PaymentFamilyBlock lbtPaymentFamily = paymentProfilesList.stream().filter(x -> x.header().contains("LBT")).findFirst().get();
        assertThat("Verify LBT payment family header ", lbtPaymentFamily.header(), equalTo(String.format("LBT%d profile %d USD %.2f USD %d connections", 1, 0, withdrawal1.amountUsd, 0)));
        assertThat("Verify LBT payment profiles", lbtPaymentFamily.rowDataList().getFirst(), equalTo(String.format("%s%d USDNo deposits%.2f USD1 withdrawal%d clientsConnected", withdrawal1.paymentProfile, 0, withdrawal1.amountUsd, 0)));

        PaymentsPage.PaymentFamilyBlock cryptoPaymentFamily = paymentProfilesList.stream().filter(x -> x.header().contains("Crypto")).findFirst().get();
        assertThat("Verify Crypto payment family header ", cryptoPaymentFamily.header(), equalTo(String.format("Crypto%d profiles %.2f USD 0 USD %d connections", 2, deposit.amountUsd + deposit2.amountUsd, 2)));
        assertThat("Verify Crypto payment profiles 1", cryptoPaymentFamily.rowDataList().stream().filter(x -> x.contains(deposit.paymentProfile)).findFirst().get(), equalTo(String.format("%s%.2f USD%d deposit0 USDNo withdrawals%d clientConnected", deposit.paymentProfile, deposit.amountUsd, 1, 1)));
        assertThat("Verify Crypto payment profiles 2", cryptoPaymentFamily.rowDataList().stream().filter(x -> x.contains(deposit2.paymentProfile)).findFirst().get(), equalTo(String.format("%s%.2f USD%d deposit0 USDNo withdrawals%d clientConnected", deposit2.paymentProfile, deposit2.amountUsd, 1, 1)));
    }


}
