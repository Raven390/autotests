package tests.vindex_backoffice_ui_tests.investigationTool.paymentsTab.summarySubTab;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateAdditionalStaticCrmTbAccountActive;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateStaticCrmTbAccountActive;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateStaticUserByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.db.clickhouse.mt_tb_credits.MtTbCreditsObjectFactory.generateCreditsByClientRandomized;
import static helpers.database.DbHelper.insertObjectToDb;
import static helpers.database.DbHelper.insertObjectsToDb;
import static helpers.database.OperationsHelper.cleanUserPaymentsDb;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.Constants.*;
import static utils.Utils.getCurrentDateMonthDay;
import static utils.Utils.insertCrmAccountsToDb;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntity;
import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntityFactory;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalEntity;
import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalEntityFactory;
import business_objects.db.clickhouse.mt_account.MtAccountObject;
import business_objects.db.clickhouse.mt_tb_credits.MtTbCreditsObject;
import helpers.data.ClientHelper;
import helpers.data.enums.Brand;
import helpers.data.enums.Regulator;
import io.qameta.allure.AllureId;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

public class TransactionsGraphTest extends TestBaseWeb {
    private static final ClientHelper client;

    static {
        client = ClientHelper.builder()
                .userId(63_350_541)
                .uid("e5880ca5-8578-4a1e-969d-7a64716ca411")
                .brand(Brand.VANTAGE)
                .regulator(Regulator.FCA)
                .tradingAccount(322_322_322)
                .tradingAccount2(322_322_321)
                .serverId(228)
                .build();
    }

    private static CrmTbUserObject crmTbUser = generateStaticUserByClient(client);
    private static CrmTbAccountObject account1 = generateStaticCrmTbAccountActive(client);
    private static CrmTbAccountObject account2 = generateAdditionalStaticCrmTbAccountActive(client);
    private static MtAccountObject mtAccount1 = generateMtAccountByCrmTbAccount(account1);
    private static MtAccountObject mtAccount2 = generateMtAccountByCrmTbAccount(account2);

    @BeforeAll
    static void setup() throws Exception {
        crmTbUser.firstName = "Operator";
        crmTbUser.lastName = "Trademan";
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertCrmAccountsToDb(account1, account2);
        insertObjectsToDb(MT_ACCOUNT_TABLE_NAME, List.of(mtAccount1, mtAccount2));
        cleanUserPaymentsDb(client.getUcid());
    }

    @AfterAll
    public static void teardown() throws Exception {
        cleanUserPaymentsDb(client.getUcid());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1798")
    @DisplayName("Payments tab. Transaction graph tooltip show data from DB")
    void transactionGraphWithoutFilters() throws Exception {
        cleanUserPaymentsDb(client.getUcid());
        CrmTbDepositEntity deposit1 = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client);
        deposit1.setStatusId(5);
        deposit1.setPaymentType("Crypto");
        deposit1.setPaymentChannel("CryptoCoino");
        deposit1.setAmount(BigDecimal.valueOf(5000d));
        deposit1.setAmountUsd(BigDecimal.valueOf(5000d));
        CrmTbDepositEntity deposit2 = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client);
        deposit2.setStatusId(5);
        deposit2.setPaymentType("Bank of Latverya");
        deposit2.setPaymentChannel("Doom Crones");
        deposit2.setAmount(BigDecimal.valueOf(1000d));
        deposit2.setAmountUsd(BigDecimal.valueOf(1000d));
        insertObjectsToDb(CRM_DEPOSIT_TABLE_NAME, List.of(deposit1, deposit2));
        CrmTbWithdrawalEntity withdrawal1 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client);
        withdrawal1.setPaymentType("local depositor");
        withdrawal1.setPaymentChannel("Bison Bucks");
        withdrawal1.setAmount(BigDecimal.valueOf(500d));
        withdrawal1.setAmountUsd(BigDecimal.valueOf(500d));
        CrmTbWithdrawalEntity withdrawal2 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client);
        withdrawal2.setPaymentType("Bank of Latverya");
        withdrawal2.setPaymentChannel("channel");
        withdrawal2.setAmount(BigDecimal.valueOf(100d));
        withdrawal2.setAmountUsd(BigDecimal.valueOf(100d));
        CrmTbWithdrawalEntity withdrawal3 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client);
        withdrawal3.setPaymentType("Cryptobro");
        withdrawal3.setPaymentChannel("brocoin net");
        withdrawal3.setAmount(BigDecimal.valueOf(300d));
        withdrawal3.setAmountUsd(BigDecimal.valueOf(300d));
        insertObjectsToDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, List.of(withdrawal1, withdrawal2, withdrawal3));

        MtTbCreditsObject credit1 = generateCreditsByClientRandomized(client);
        credit1.setAmount(50_000d);
        credit1.setAmountUsd(50_000d);
        MtTbCreditsObject credit2 = generateCreditsByClientRandomized(client);
        credit2.setAmount(10_000d);
        credit2.setAmountUsd(10_000d);
        MtTbCreditsObject credit3 = generateCreditsByClientRandomized(client);
        credit3.setAmount(30_000d);
        credit3.setAmountUsd(30_000d);
        MtTbCreditsObject credit4 = generateCreditsByClientRandomized(client);
        credit4.setAmount(40_000d);
        credit4.setAmountUsd(40_000d);
        insertObjectsToDb(MT_CREDITS_TABLE_NAME, List.of(credit1, credit2, credit3, credit4));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        paymentsPage.navigatePaymentsTab(client.getUcid());

        double totalDeposits =
                deposit1.getAmountUsd().add(deposit2.getAmountUsd()).doubleValue();
        double totalWithdrawals = withdrawal1
                .getAmountUsd()
                .add(withdrawal2.getAmountUsd())
                .add(withdrawal3.getAmountUsd())
                .doubleValue();
        double totalCredits = credit1.amountUsd + credit2.amountUsd + credit3.amountUsd + credit4.amountUsd;
        paymentsPage.hoverOverFinancialTransactionsGraphByDateSingleDay(getCurrentDateMonthDay());
        checkValue(
                "//div[@data-qa='payments__transactions_chart__features__0']//div[@class='g-text g-text_variant_header-1 g-color-text g-color-text_color_brand']",
                dfWholed.format(totalDeposits));
        checkValue(
                "//div[@data-qa='payments__transactions_chart__features__1']//div[@class='g-text g-text_variant_header-1 g-color-text g-color-text_color_danger']",
                dfWholed.format(totalWithdrawals));
        checkValue(
                "//div[@data-qa='payments__transactions_chart__features__2']//div[@class='g-text g-text_variant_header-1 g-color-text g-color-text_color_misc']",
                dfWholed.format(totalCredits));
    }

    private void checkValue(String selector, String expectedValue) {
        page.waitForSelector(selector);
        String actualValue = page.locator(selector).textContent();
        assertEquals(expectedValue, actualValue);
    }
}
