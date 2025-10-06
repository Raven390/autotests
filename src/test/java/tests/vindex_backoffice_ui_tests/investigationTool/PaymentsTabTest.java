package tests.vindex_backoffice_ui_tests.investigationTool;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositObject;
import business_objects.db.clickhouse.crm_tb_transfer.CrmTbTransferObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalObject;
import business_objects.db.clickhouse.mt_account.MtAccountObject;
import business_objects.db.clickhouse.mt_tb_credits.MtTbCreditsObject;
import business_objects.db.clickhouse.payments_total.PaymentsTotalObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import helpers.data.ClientHelper;
import helpers.data.enums.Brand;
import helpers.data.enums.DateTimeFormat;
import helpers.data.enums.Regulator;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;
import utils.Utils;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateAdditionalStaticCrmTbAccountActive;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateStaticCrmTbAccountActive;
import static business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositObjectFactory.generateDepositByClient;
import static business_objects.db.clickhouse.crm_tb_transfer.CrmTbTransferFactory.generateCrmTbTransferRandomized;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateStaticUserByClient;
import static business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalObjectFactory.generateCrmTbWithdrawalObjectByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.db.clickhouse.mt_tb_credits.MtTbCreditsObjectFactory.generateCreditsByClientRandomized;
import static helpers.data.enums.DateTimeFormat.*;
import static helpers.data.enums.DateTimeFormat.MONTH_TEXT_AND_YEAR;
import static helpers.database.DbHelper.insertObjectToDb;
import static helpers.database.DbHelper.insertObjectsToDb;
import static helpers.database.OperationsHelper.cleanUserPaymentsDb;
import static helpers.database.OperationsHelper.cleanUserFinancialTransactionDbUcid;
import static utils.Constants.*;
import static utils.Utils.*;

class PaymentsTabTest extends TestBaseWeb {

    private static ClientHelper client = new ClientHelper(313_102, "e5880ca5-8578-4a1e-969d-7a64716ca41f", Brand.INFINOX, Regulator.FCA, 313_102_001, 313_102_002, 42);
    private static CrmTbUserObject crmTbUser = generateStaticUserByClient(client);
    private static CrmTbAccountObject account1 = generateStaticCrmTbAccountActive(client);
    private static CrmTbAccountObject account2 = generateAdditionalStaticCrmTbAccountActive(client);
    private static MtAccountObject mtAccount1 = generateMtAccountByCrmTbAccount(account1);
    private static MtAccountObject mtAccount2 = generateMtAccountByCrmTbAccount(account2);

    @BeforeAll
    static void setup() throws ReflectiveOperationException, SQLException, JsonProcessingException {
        crmTbUser.firstName = "Operator";
        crmTbUser.lastName = "Trademan";
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertObjectsToDb(CRM_TB_ACCOUNT_TABLE_NAME, List.of(account1, account2));
        insertObjectsToDb(MT_ACCOUNT_TABLE_NAME, List.of(mtAccount1, mtAccount2));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("562")
    @DisplayName("Payments tab. Cashflow chart show empty state when it not have data DB")
    void cashflowEmptyStateTest() throws Exception {
        cleanUserPaymentsDb(client.getUcid());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        paymentsPage.navigatePaymentsTab(client.getUcid());
        paymentsPage.checkCashflowEmptyStateIsVisible();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("573")
    @DisplayName("Payments tab. Cashflow chart show empty one side on deposit when it not have data DB")
    void cashflowOnlyOneWithdrawalFilledTest() throws Exception {

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        cleanUserPaymentsDb(client.getUcid());
        CrmTbWithdrawalObject withdrawal = generateCrmTbWithdrawalObjectByClient(client);
        withdrawal.statusId = 7;
        Allure.step("add record about withdrawal");
        insertObjectToDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, withdrawal);
        paymentsPage.navigatePaymentsTab(client.getUcid());
        paymentsPage.checkCashflowEmptyStateDepositIsVisible();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("574")
    @DisplayName("Payments tab. Cashflow chart show empty one side on deposit when it not have data DB")
    void CashflowOnlyDepositSideFilledTest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        cleanUserPaymentsDb(client.getUcid());
        CrmTbDepositObject deposit = generateDepositByClient(client);
        deposit.statusId = 5;
        Allure.step("add record about deposit");
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, deposit);
        paymentsPage.navigatePaymentsTab(client.getUcid());
        paymentsPage.checkCashflowEmptyStateWithdrawalIsVisible();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("588")
    @DisplayName("Payments tab. Cashflow chart show data from DB")
    void cashflowTotalValueInTipTest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        cleanUserPaymentsDb(client.getUcid());
        CrmTbDepositObject transaction = generateDepositByClient(client);
        transaction.statusId = 5;
        transaction.paymentType = "Crypto";
        transaction.paymentChannel = "CryptoCoino";
        Allure.step("add record about new deposit with another type");
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, transaction);
        paymentsPage.navigatePaymentsTab(client.getUcid());
        paymentsPage.hoverOverCashflowLineByTypeDeposit(transaction.paymentType);
        paymentsPage.checkTotalCountByPaymentSystem(transaction.paymentChannel, dfWholed.format(Math.round(transaction.amountUsd)));
        CrmTbDepositObject transaction2 = generateDepositByClient(client);
        transaction2.paymentType = "P2P";
        transaction2.paymentChannel = "chanel1";
        Allure.step("add record about new deposit with another type");
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, transaction2);
        page.waitForTimeout(1000);
        page.reload();
        paymentsPage.hoverOverCashflowLineByTypeDeposit(transaction2.paymentType);
        paymentsPage.checkTotalCountByPaymentSystem(transaction2.paymentChannel, transaction2.amountUsd);
        CrmTbDepositObject transaction3 = generateDepositByClient(client);
        transaction3.paymentType = "Bank Transfers";
        transaction3.paymentChannel = "transferno";
        Allure.step("add record about new deposit with another type");
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, transaction3);
        page.waitForTimeout(1000);
        page.reload();
        paymentsPage.hoverOverCashflowLineByTypeDeposit(transaction3.paymentType);
        paymentsPage.checkTotalCountByPaymentSystem(transaction3.paymentChannel, transaction3.amountUsd);
        CrmTbDepositObject transaction4 = generateDepositByClient(client);
        transaction4.paymentType = "Payment Services";
        transaction4.paymentChannel = "quiwy";
        Allure.step("add record about new deposit with another type");
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, transaction4);
        page.waitForTimeout(1000);
        page.reload();
        paymentsPage.hoverOverCashflowLineByTypeDeposit(transaction4.paymentType);
        paymentsPage.checkTotalCountByPaymentSystem(transaction4.paymentChannel, transaction4.amountUsd);
        CrmTbDepositObject transaction5 = generateDepositByClient(client);
        transaction5.paymentType = "local depositor";
        transaction5.paymentChannel = "otherway";
        Allure.step("add record about new deposit with another type");
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, transaction5);
        page.waitForTimeout(1000);
        page.reload();
        paymentsPage.hoverOverCashflowLineByTypeDeposit(transaction5.paymentType);
        paymentsPage.checkTotalCountByPaymentSystem(transaction5.paymentChannel, transaction5.amountUsd);
        CrmTbDepositObject transaction6 = generateDepositByClient(client);
        transaction6.paymentType = "offline payment";
        transaction6.paymentChannel = "otherwayBig";
        transaction6.amountUsd = transaction5.amountUsd + 1.1;
        Allure.step("add record about new deposit with existing type and another channel type with greater amount");
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, transaction6);
        page.waitForTimeout(1000);
        page.reload();
        paymentsPage.hoverOverCashflowLineByTypeDeposit(transaction6.paymentType);
        paymentsPage.checkTotalCountByPaymentSystem(transaction6.paymentChannel, transaction6.amountUsd);
        CrmTbDepositObject transaction7 = generateDepositByClient(client);
        transaction7.paymentType = "offline payment";
        transaction7.paymentChannel = "otherwayBig";
        Allure.step("add record about new deposit with existing type and channel type to check that too;tip show sum");
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, transaction7);
        page.waitForTimeout(1000);
        page.reload();
        paymentsPage.hoverOverCashflowLineByTypeDeposit(transaction6.paymentType);
        paymentsPage.checkTotalCountByPaymentSystem(transaction6.paymentChannel, transaction6.amountUsd + transaction7.amountUsd);
        CrmTbWithdrawalObject transaction8 = generateCrmTbWithdrawalObjectByClient(client);
        transaction8.paymentType = "Crypto";
        transaction8.paymentChannel = "CryptoCoino";
        CrmTbWithdrawalObject transaction9 = generateCrmTbWithdrawalObjectByClient(client);
        transaction9.paymentType = "P2P";
        transaction9.paymentChannel = "chanel1";
        CrmTbWithdrawalObject transaction10 = generateCrmTbWithdrawalObjectByClient(client);
        transaction10.paymentType = "Bank Transfers";
        transaction10.paymentChannel = "transferno";
        CrmTbWithdrawalObject transaction11 = generateCrmTbWithdrawalObjectByClient(client);
        transaction11.paymentType = "Payment Services";
        transaction11.paymentChannel = "quiwy";
        CrmTbWithdrawalObject transaction12 = generateCrmTbWithdrawalObjectByClient(client);
        transaction12.paymentType = "local depositor";
        transaction12.paymentChannel = "otherway";
        insertObjectsToDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, List.of(transaction8, transaction9, transaction10, transaction11, transaction12));
        Allure.step("add withdrawals for every expected payment type to check them displayed ");
        page.waitForTimeout(5000);
        page.reload();
        paymentsPage.hoverOverCashflowLineByTypeWithdrawal(transaction8.paymentType);
        paymentsPage.checkTotalCountByPaymentSystem(transaction8.paymentChannel, transaction8.amountUsd);
        paymentsPage.hoverOverCashflowLineByTypeWithdrawal(transaction9.paymentType);
        paymentsPage.checkTotalCountByPaymentSystem(transaction9.paymentChannel, transaction9.amountUsd);
        paymentsPage.hoverOverCashflowLineByTypeWithdrawal(transaction10.paymentType);
        paymentsPage.checkTotalCountByPaymentSystem(transaction10.paymentChannel, transaction10.amountUsd);
        paymentsPage.hoverOverCashflowLineByTypeWithdrawal(transaction11.paymentType);
        paymentsPage.checkTotalCountByPaymentSystem(transaction11.paymentChannel, transaction11.amountUsd);
        paymentsPage.hoverOverCashflowLineByTypeWithdrawal(transaction12.paymentType);
        paymentsPage.checkTotalCountByPaymentSystem(transaction12.paymentChannel, transaction12.amountUsd);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("589")
    @DisplayName("Payments tab. Cashflow header show data from DB")
    void cashflowValueInHeaderTest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        cleanUserPaymentsDb(client.getUcid());
        CrmTbDepositObject transaction = generateDepositByClient(client);
        transaction.statusId = 5;
        transaction.paymentType = "Crypto";
        transaction.paymentChannel = "CryptoCoino";
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, transaction);
        Allure.step("add record about deposit");
        page.waitForTimeout(1000);
        paymentsPage.navigatePaymentsTab(client.getUcid());
        paymentsPage.checkCashflowTopPaymentSystemTypesHeaderDeposit(transaction.paymentType, transaction.amountUsd);

        CrmTbDepositObject transaction2 = generateDepositByClient(client);
        transaction2.statusId = 5;
        transaction2.paymentType = "Bank of Latverya";
        transaction2.paymentChannel = "Doom Crones";
        transaction2.amountUsd = transaction.amountUsd + 1.1;
        Allure.step("add record about new deposit with another type and bigger amount");
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, transaction2);
        page.waitForTimeout(1000);
        page.reload();
        paymentsPage.checkCashflowTopPaymentSystemTypesHeaderDeposit(transaction2.paymentType, transaction2.amountUsd);

        CrmTbDepositObject transaction3 = generateDepositByClient(client);
        transaction3.statusId = 5;
        transaction3.paymentType = transaction2.paymentType;
        transaction3.paymentChannel = "SomeBank LLC";
        Allure.step("add record about new deposit with existing in DB and another channel");
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, transaction3);
        page.waitForTimeout(1000);
        page.reload();
        paymentsPage.checkCashflowTopPaymentSystemTypesHeaderDeposit(transaction2.paymentType, transaction2.amountUsd + transaction3.amountUsd);

        CrmTbWithdrawalObject transaction4 = generateCrmTbWithdrawalObjectByClient(client);
        transaction4.paymentType = "P2Pinocchio";
        transaction4.paymentChannel = "Pinocchio";
        insertObjectToDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, transaction4);
        Allure.step("add record about withdrawal with type that was not used in deposits");
        page.waitForTimeout(1000);
        page.reload();
        paymentsPage.checkCashflowTopPaymentSystemTypesHeaderWithdrawal(transaction4.paymentType, transaction4.amountUsd);

        CrmTbWithdrawalObject transaction5 = generateCrmTbWithdrawalObjectByClient(client);
        transaction5.paymentType = "offline payment";
        transaction5.paymentChannel = "dullas";
        transaction5.amountUsd = transaction4.amountUsd + 1.1;
        insertObjectToDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, transaction5);
        Allure.step("add record about withdrawal with type that was not used early with bigger amount that previous withdrawal");
        page.waitForTimeout(1000);
        page.reload();
        paymentsPage.checkCashflowTopPaymentSystemTypesHeaderWithdrawal(transaction5.paymentType, transaction5.amountUsd);

        CrmTbWithdrawalObject transaction6 = generateCrmTbWithdrawalObjectByClient(client);
        transaction6.paymentType = "local depositor";
        transaction6.paymentChannel = "Bison Bucks";
        insertObjectToDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, transaction6);
        Allure.step("add record about withdrawal with type that was used for withdrawals and check that them summed");
        page.waitForTimeout(1000);
        page.reload();
        paymentsPage.checkCashflowTopPaymentSystemTypesHeaderWithdrawal(transaction6.paymentType, transaction5.amountUsd + transaction6.amountUsd);

    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("597")
    @DisplayName("Payments tab. financialTransaction tabs show data from DB")
    void financialTransactionTabsShowsDataFromDb() throws Exception {
        cleanUserPaymentsDb(client.getUcid());

        CrmTbDepositObject deposit1 = generateDepositByClient(client);
        deposit1.statusId = 5;
        deposit1.paymentType = "Crypto";
        deposit1.paymentChannel = "CryptoCoino";
        CrmTbDepositObject deposit2 = generateDepositByClient(client);
        deposit2.statusId = 5;
        deposit2.paymentType = "Bank of Latverya";
        deposit2.paymentChannel = "Doom Crones";
        insertObjectsToDb(CRM_DEPOSIT_TABLE_NAME, List.of(deposit1, deposit2));
        CrmTbWithdrawalObject withdrawal1 = generateCrmTbWithdrawalObjectByClient(client);
        withdrawal1.paymentType = "local depositor";
        withdrawal1.paymentChannel = "Bison Bucks";
        CrmTbWithdrawalObject withdrawal2 = generateCrmTbWithdrawalObjectByClient(client);
        withdrawal2.paymentType = "Bank of Latverya";
        withdrawal2.paymentChannel = "channel";
        CrmTbWithdrawalObject withdrawal3 = generateCrmTbWithdrawalObjectByClient(client);
        withdrawal3.paymentType = "Cryptobro";
        withdrawal3.paymentChannel = "brocoin net";
        insertObjectsToDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, List.of(withdrawal1, withdrawal2, withdrawal3));

        MtTbCreditsObject credit1 = generateCreditsByClientRandomized(client);
        MtTbCreditsObject credit2 = generateCreditsByClientRandomized(client);
        MtTbCreditsObject credit3 = generateCreditsByClientRandomized(client);
        MtTbCreditsObject credit4 = generateCreditsByClientRandomized(client);
        insertObjectsToDb(MT_CREDITS_TABLE_NAME, List.of(credit1, credit2, credit3, credit4));

        CrmTbTransferObject transfer = generateCrmTbTransferRandomized(client);
        insertObjectToDb(CRM_TRANSFERS_TABLE_NAME, transfer);
        page.waitForTimeout(3000);

        Allure.step("add to DB transactions with all presented types");

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();

        paymentsPage.navigatePaymentsTab(client.getUcid());

        double totalDeposits = deposit1.amountUsd + deposit2.amountUsd;
        double totalWithdrawals = withdrawal1.amountUsd + withdrawal2.amountUsd + withdrawal3.amountUsd;
        double totalCredits = credit1.amountUsd + credit2.amountUsd + credit3.amountUsd + credit4.amountUsd;

        paymentsPage.checkFinancialTransactionsTilesValues("Net deposits", totalDeposits - totalWithdrawals, 5);
        paymentsPage.checkFinancialTransactionsTilesValues("Total deposits", totalDeposits, 2);
        paymentsPage.checkFinancialTransactionsTilesValues("Total withdrawals", totalWithdrawals, 3);
        paymentsPage.checkFinancialTransactionsTilesValues("Total Internal transfers", transfer.getAmountUsd(), 1);
        paymentsPage.checkFinancialTransactionsTilesValues("Total credit", totalCredits, 4);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("597")
    @DisplayName("Payments tab. financialTransaction graph tooltip show data from DB")
    void financialTransactionGraphTooltipShowsDataFromDb() throws Exception {
        cleanUserPaymentsDb(client.getUcid());
        CrmTbDepositObject deposit1 = generateDepositByClient(client);
        deposit1.statusId = 5;
        deposit1.paymentType = "Crypto";
        deposit1.paymentChannel = "CryptoCoino";
        CrmTbDepositObject deposit2 = generateDepositByClient(client);
        deposit2.statusId = 5;
        deposit2.paymentType = "Bank of Latverya";
        deposit2.paymentChannel = "Doom Crones";
        insertObjectsToDb(CRM_DEPOSIT_TABLE_NAME, List.of(deposit1, deposit2));
        CrmTbWithdrawalObject withdrawal1 = generateCrmTbWithdrawalObjectByClient(client);
        withdrawal1.paymentType = "local depositor";
        withdrawal1.paymentChannel = "Bison Bucks";
        CrmTbWithdrawalObject withdrawal2 = generateCrmTbWithdrawalObjectByClient(client);
        withdrawal2.paymentType = "Bank of Latverya";
        withdrawal2.paymentChannel = "channel";
        CrmTbWithdrawalObject withdrawal3 = generateCrmTbWithdrawalObjectByClient(client);
        withdrawal3.paymentType = "Cryptobro";
        withdrawal3.paymentChannel = "brocoin net";
        insertObjectsToDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, List.of(withdrawal1, withdrawal2, withdrawal3));

        MtTbCreditsObject credit1 = generateCreditsByClientRandomized(client);
        MtTbCreditsObject credit2 = generateCreditsByClientRandomized(client);
        MtTbCreditsObject credit3 = generateCreditsByClientRandomized(client);
        MtTbCreditsObject credit4 = generateCreditsByClientRandomized(client);
        insertObjectsToDb(MT_CREDITS_TABLE_NAME, List.of(credit1, credit2, credit3, credit4));

        CrmTbTransferObject transfer = generateCrmTbTransferRandomized(client);
        insertObjectToDb(CRM_TRANSFERS_TABLE_NAME, transfer);
        page.waitForTimeout(3000);


        double totalDeposits = deposit1.amountUsd + deposit2.amountUsd;
        double totalWithdrawals = withdrawal1.amountUsd + withdrawal2.amountUsd + withdrawal3.amountUsd;
        double totalCredits = credit1.amountUsd + credit2.amountUsd + credit3.amountUsd + credit4.amountUsd;

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        paymentsPage.navigatePaymentsTab(client.getUcid());
        Allure.step("filter test date");
        paymentsPage.selectDateFilter("Last 7 days");
        paymentsPage.hoverOverFinancialTransactionsGraphByDateSingleDay(getCurrentDateMonthDay());
        paymentsPage.checkFinancialTransactionsRowInTooltip("Deposit", dfWholed.format(totalDeposits));
        paymentsPage.checkFinancialTransactionsRowInTooltip("Withdrawal", dfWholed.format(totalWithdrawals));
        paymentsPage.checkFinancialTransactionsRowInTooltip("Credit", dfWholed.format(totalCredits));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("617")
    @DisplayName("Payments tab. user can filter data by account")
    void operationsTabCanBeFilteredByAccount() throws Exception {
        cleanUserPaymentsDb(client.getUcid());
        CrmTbDepositObject deposit1 = generateDepositByClient(client);
        deposit1.statusId = 5;
        deposit1.paymentType = "Crypto";
        deposit1.paymentChannel = "CryptoCoino";
        CrmTbDepositObject deposit2 = generateDepositByClient(client);
        deposit2.statusId = 5;
        deposit2.paymentType = "Bank of Latverya";
        deposit2.paymentChannel = "Doom Crones";
        insertObjectsToDb(CRM_DEPOSIT_TABLE_NAME, List.of(deposit1, deposit2));
        CrmTbWithdrawalObject withdrawal1 = generateCrmTbWithdrawalObjectByClient(client);
        withdrawal1.paymentType = "local depositor";
        withdrawal1.paymentChannel = "Bison Bucks";
        CrmTbWithdrawalObject withdrawal2 = generateCrmTbWithdrawalObjectByClient(client);
        withdrawal2.paymentType = "Bank of Latverya";
        withdrawal2.paymentChannel = "channel";
        CrmTbWithdrawalObject withdrawal3 = generateCrmTbWithdrawalObjectByClient(client);
        withdrawal3.paymentType = "Cryptobro";
        withdrawal3.paymentChannel = "brocoin net";
        insertObjectsToDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, List.of(withdrawal1, withdrawal2, withdrawal3));
        Allure.step("filter test date for the first account");

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        paymentsPage.navigatePaymentsTab(client.getUcid());
        paymentsPage.checkCashflowEmptyStateIsNotVisible();
        paymentsPage.clickOnAccountSelectionWindow();
        paymentsPage.selectTradingAccount(client.getTradingAccount2());
        paymentsPage.checkCashflowEmptyStateIsVisible();
        paymentsPage.clearSelectedTradingAccount();
        paymentsPage.selectTradingAccount(client.getTradingAccount());
        paymentsPage.checkFinancialTransactionEmptyStateIsNotVisible();

        cleanUserPaymentsDb(client.getUcid());
        CrmTbDepositObject deposit11 = generateDepositByClient(client);
        deposit11.statusId = 5;
        deposit11.paymentType = "Crypto";
        deposit11.paymentChannel = "CryptoCoino";
        deposit11.account = client.getTradingAccount2();
        CrmTbDepositObject deposit12 = generateDepositByClient(client);
        deposit12.statusId = 5;
        deposit12.paymentType = "Bank of Latverya";
        deposit12.paymentChannel = "Doom Crones";
        deposit12.account = client.getTradingAccount2();
        insertObjectsToDb(CRM_DEPOSIT_TABLE_NAME, List.of(deposit11, deposit12));
        CrmTbWithdrawalObject withdrawal11 = generateCrmTbWithdrawalObjectByClient(client);
        withdrawal11.paymentType = "local depositor";
        withdrawal11.paymentChannel = "Bison Bucks";
        withdrawal11.account = client.getTradingAccount2();
        CrmTbWithdrawalObject withdrawal12 = generateCrmTbWithdrawalObjectByClient(client);
        withdrawal12.paymentType = "Bank of Latverya";
        withdrawal12.paymentChannel = "channel";
        withdrawal12.account = client.getTradingAccount2();
        CrmTbWithdrawalObject withdrawal13 = generateCrmTbWithdrawalObjectByClient(client);
        withdrawal13.paymentType = "Cryptobro";
        withdrawal13.paymentChannel = "brocoin net";
        withdrawal13.account = client.getTradingAccount2();
        insertObjectsToDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, List.of(withdrawal11, withdrawal12, withdrawal13));
        page.reload();
        paymentsPage.clearSelectedTradingAccount();
        paymentsPage.clickOnAccountSelectionWindow();
        paymentsPage.selectTradingAccount(client.getTradingAccount());
        paymentsPage.checkCashflowEmptyStateIsVisible();
        paymentsPage.clearSelectedTradingAccount();
        paymentsPage.selectTradingAccount(client.getTradingAccount2());
        paymentsPage.checkCashflowEmptyStateIsNotVisible();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("610")
    @DisplayName("Payments tab. User can filter operations by Dates Custom - one day")
    void filterCustomOneDayTest() throws Exception {

        Allure.step("generate data inside and outside of tested period");

        String testDate = "2024-12-11 14:27:51";

        cleanUserPaymentsDb(client.getUcid());
        CrmTbDepositObject deposit1 = generateDepositByClient(client);
        deposit1.statusId = 5;
        deposit1.paymentType = "Crypto";
        deposit1.paymentChannel = "CryptoCoino";
        deposit1.createTime = testDate;
        CrmTbDepositObject deposit2 = generateDepositByClient(client);
        deposit2.statusId = 5;
        deposit2.paymentType = "Crypto";
        deposit2.paymentChannel = "CryptoCoino";
        insertObjectsToDb(CRM_DEPOSIT_TABLE_NAME, List.of(deposit1, deposit2));
        CrmTbWithdrawalObject withdrawal1 = generateCrmTbWithdrawalObjectByClient(client);
        withdrawal1.paymentType = "local depositor";
        withdrawal1.paymentChannel = "Bison Bucks";
        withdrawal1.createTime = testDate;
        CrmTbWithdrawalObject withdrawal2 = generateCrmTbWithdrawalObjectByClient(client);
        withdrawal2.paymentType = "Bank of Latverya";
        withdrawal2.paymentChannel = "channel";
        insertObjectsToDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, List.of(withdrawal1, withdrawal2));
        MtTbCreditsObject credit1 = generateCreditsByClientRandomized(client);
        credit1.createTime = testDate;
        MtTbCreditsObject credit2 = generateCreditsByClientRandomized(client);
        insertObjectsToDb(MT_CREDITS_TABLE_NAME, List.of(credit1, credit2));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        paymentsPage.navigatePaymentsTab(client.getUcid());
        Allure.step("filter test date");
        paymentsPage.selectDatesInCalendar("2024-12-11", "2024-12-11");
        Allure.step("check that only data for the test date is displayed");
        paymentsPage.hoverOverFinancialTransactionsGraphByDateSingleDay("Dec 11");
        paymentsPage.checkFinancialTransactionsRowInTooltip("Deposit", dfWholed.format(deposit1.amountUsd));
        paymentsPage.checkFinancialTransactionsRowInTooltip("Withdrawal", dfWholed.format(withdrawal1.amountUsd));
        paymentsPage.checkFinancialTransactionsRowInTooltip("Credit", dfWholed.format(credit1.amountUsd));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("615")
    @DisplayName("Payments tab. User can filter operations by Dates Last 1 year")
    void filterLastYearTest() throws Exception {


        Allure.step("generate data inside and outside of tested period");

        String insideTestDate = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 11, 0);
        String outsideTestDate = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 367);

        cleanUserPaymentsDb(client.getUcid());
        CrmTbDepositObject deposit1 = generateDepositByClient(client);
        deposit1.statusId = 5;
        deposit1.paymentType = "Crypto";
        deposit1.paymentChannel = "CryptoCoino";
        deposit1.createTime = insideTestDate;
        CrmTbDepositObject deposit2 = generateDepositByClient(client);
        deposit2.statusId = 5;
        deposit2.paymentType = "Bank";
        deposit2.paymentChannel = "Bankinn";
        CrmTbDepositObject deposit3 = generateDepositByClient(client);
        deposit3.statusId = 5;
        deposit3.paymentType = "Card";
        deposit3.paymentChannel = "cardon";
        deposit3.createTime = outsideTestDate;
        insertObjectsToDb(CRM_DEPOSIT_TABLE_NAME, List.of(deposit1, deposit2, deposit3));
        CrmTbWithdrawalObject withdrawal1 = generateCrmTbWithdrawalObjectByClient(client);
        withdrawal1.paymentType = "local depositor";
        withdrawal1.paymentChannel = "Bison Bucks";
        withdrawal1.createTime = insideTestDate;
        CrmTbWithdrawalObject withdrawal2 = generateCrmTbWithdrawalObjectByClient(client);
        withdrawal2.paymentType = "Bank of Latverya";
        withdrawal2.paymentChannel = "channel";
        CrmTbWithdrawalObject withdrawal3 = generateCrmTbWithdrawalObjectByClient(client);
        withdrawal3.paymentType = "Card";
        withdrawal3.paymentChannel = "Bison Card";
        withdrawal3.createTime = outsideTestDate;
        insertObjectsToDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, List.of(withdrawal1, withdrawal2, withdrawal3));
        MtTbCreditsObject credit1 = generateCreditsByClientRandomized(client);
        credit1.createTime = insideTestDate;
        MtTbCreditsObject credit2 = generateCreditsByClientRandomized(client);
        MtTbCreditsObject credit3 = generateCreditsByClientRandomized(client);
        credit3.createTime = outsideTestDate;
        insertObjectsToDb(MT_CREDITS_TABLE_NAME, List.of(credit1, credit2, credit3));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        paymentsPage.navigatePaymentsTab(client.getUcid());
        Allure.step("filter test date");
        paymentsPage.selectDateFilter("Last 1 year");
        Allure.step("check that only data for the test date is displayed");
        paymentsPage.hoverOverFirstFilledTransactionsGraphByDateSingleDay();
        paymentsPage.checkFinancialTransactionsRowInTooltip("Deposit", dfWholed.format(deposit1.amountUsd));
        paymentsPage.checkFinancialTransactionsRowInTooltip("Withdrawal", dfWholed.format(withdrawal1.amountUsd));
        paymentsPage.checkFinancialTransactionsRowInTooltip("Credit", dfWholed.format(credit1.amountUsd));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("611")
    @DisplayName("Payments tab. User can filter operations by Dates Last 30 days")
    void filterLast30DaysTest() throws Exception {

        Allure.step("generate data inside and outside of tested period");

        String insideTestDate = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 29);
        String outsideTestDate = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 30);

        cleanUserPaymentsDb(client.getUcid());
        CrmTbDepositObject deposit1 = generateDepositByClient(client);
        deposit1.statusId = 5;
        deposit1.paymentType = "Crypto";
        deposit1.paymentChannel = "CryptoCoino";
        deposit1.createTime = insideTestDate;
        CrmTbDepositObject deposit2 = generateDepositByClient(client);
        deposit2.statusId = 5;
        deposit2.paymentType = "Bank";
        deposit2.paymentChannel = "Bankinn";
        CrmTbDepositObject deposit3 = generateDepositByClient(client);
        deposit3.statusId = 5;
        deposit3.paymentType = "Card";
        deposit3.paymentChannel = "cardon";
        deposit3.createTime = outsideTestDate;
        insertObjectsToDb(CRM_DEPOSIT_TABLE_NAME, List.of(deposit1, deposit2, deposit3));
        CrmTbWithdrawalObject withdrawal1 = generateCrmTbWithdrawalObjectByClient(client);
        withdrawal1.paymentType = "local depositor";
        withdrawal1.paymentChannel = "Bison Bucks";
        withdrawal1.createTime = insideTestDate;
        CrmTbWithdrawalObject withdrawal2 = generateCrmTbWithdrawalObjectByClient(client);
        withdrawal2.paymentType = "Bank of Latverya";
        withdrawal2.paymentChannel = "channel";
        CrmTbWithdrawalObject withdrawal3 = generateCrmTbWithdrawalObjectByClient(client);
        withdrawal3.paymentType = "Card";
        withdrawal3.paymentChannel = "Bison Card";
        withdrawal3.createTime = outsideTestDate;
        insertObjectsToDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, List.of(withdrawal1, withdrawal2, withdrawal3));
        MtTbCreditsObject credit1 = generateCreditsByClientRandomized(client);
        credit1.createTime = insideTestDate;
        MtTbCreditsObject credit2 = generateCreditsByClientRandomized(client);
        MtTbCreditsObject credit3 = generateCreditsByClientRandomized(client);
        credit3.createTime = outsideTestDate;
        insertObjectsToDb(MT_CREDITS_TABLE_NAME, List.of(credit1, credit2, credit3));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        paymentsPage.navigatePaymentsTab(client.getUcid());
        Allure.step("filter test date");
        paymentsPage.selectDateFilter("Last 30 days");
        Allure.step("check that only data for the test date is displayed");
        paymentsPage.hoverOverFirstFilledTransactionsGraphByDateSingleDay();
        paymentsPage.checkFinancialTransactionsRowInTooltip("Deposit", dfWholed.format(deposit1.amountUsd));
        paymentsPage.checkFinancialTransactionsRowInTooltip("Withdrawal", dfWholed.format(withdrawal1.amountUsd));
        paymentsPage.checkFinancialTransactionsRowInTooltip("Credit", dfWholed.format(credit1.amountUsd));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("897")
    @DisplayName("Payments tab. User can filter operations by Dates Last 6 months")
    void filterLast6MonthsTest() throws Exception {

        Allure.step("generate data inside and outside of tested period");

        String insideTestDate = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 5, 0);
        String outsideTestDate = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 5, 4);

        cleanUserPaymentsDb(client.getUcid());
        CrmTbDepositObject deposit1 = generateDepositByClient(client);
        deposit1.statusId = 5;
        deposit1.paymentType = "Crypto";
        deposit1.paymentChannel = "CryptoCoino";
        deposit1.createTime = insideTestDate;
        CrmTbDepositObject deposit2 = generateDepositByClient(client);
        deposit2.statusId = 5;
        deposit2.paymentType = "Bank";
        deposit2.paymentChannel = "Bankinn";
        CrmTbDepositObject deposit3 = generateDepositByClient(client);
        deposit3.statusId = 5;
        deposit3.paymentType = "Card";
        deposit3.paymentChannel = "cardon";
        deposit3.createTime = outsideTestDate;
        insertObjectsToDb(CRM_DEPOSIT_TABLE_NAME, List.of(deposit1, deposit2, deposit3));
        CrmTbWithdrawalObject withdrawal1 = generateCrmTbWithdrawalObjectByClient(client);
        withdrawal1.paymentType = "local depositor";
        withdrawal1.paymentChannel = "Bison Bucks";
        withdrawal1.createTime = insideTestDate;
        CrmTbWithdrawalObject withdrawal2 = generateCrmTbWithdrawalObjectByClient(client);
        withdrawal2.paymentType = "Bank of Latverya";
        withdrawal2.paymentChannel = "channel";
        CrmTbWithdrawalObject withdrawal3 = generateCrmTbWithdrawalObjectByClient(client);
        withdrawal3.paymentType = "Card";
        withdrawal3.paymentChannel = "Bison Card";
        withdrawal3.createTime = outsideTestDate;
        insertObjectsToDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, List.of(withdrawal1, withdrawal2, withdrawal3));
        MtTbCreditsObject credit1 = generateCreditsByClientRandomized(client);
        credit1.createTime = insideTestDate;
        MtTbCreditsObject credit2 = generateCreditsByClientRandomized(client);
        MtTbCreditsObject credit3 = generateCreditsByClientRandomized(client);
        credit3.createTime = outsideTestDate;
        insertObjectsToDb(MT_CREDITS_TABLE_NAME, List.of(credit1, credit2, credit3));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        paymentsPage.navigatePaymentsTab(client.getUcid());
        Allure.step("filter test date");
        paymentsPage.selectDateFilter("Last 6 months");
        Allure.step("check that only data for the test date is displayed");
        paymentsPage.hoverOverFirstFilledTransactionsGraphByDateSingleDay();
        paymentsPage.checkFinancialTransactionsRowInTooltip("Deposit", dfWholed.format(deposit1.amountUsd));
        paymentsPage.checkFinancialTransactionsRowInTooltip("Withdrawal", dfWholed.format(withdrawal1.amountUsd));
        paymentsPage.checkFinancialTransactionsRowInTooltip("Credit", dfWholed.format(credit1.amountUsd));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("609")
    @DisplayName("Payments tab. User can filter operations by Dates Last 7 days")
    void filterLast7DaysTest() throws Exception {


        Allure.step("generate data inside and outside of tested period");

        String insideTestDate = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 6);
        String outsideTestDate = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 7);

        cleanUserPaymentsDb(client.getUcid());
        CrmTbDepositObject deposit1 = generateDepositByClient(client);
        deposit1.statusId = 5;
        deposit1.paymentType = "Crypto";
        deposit1.paymentChannel = "CryptoCoino";
        deposit1.createTime = insideTestDate;
        CrmTbDepositObject deposit2 = generateDepositByClient(client);
        deposit2.statusId = 5;
        deposit2.paymentType = "Bank";
        deposit2.paymentChannel = "Bankinn";
        CrmTbDepositObject deposit3 = generateDepositByClient(client);
        deposit3.statusId = 5;
        deposit3.paymentType = "Card";
        deposit3.paymentChannel = "cardon";
        deposit3.createTime = outsideTestDate;
        insertObjectsToDb(CRM_DEPOSIT_TABLE_NAME, List.of(deposit1, deposit2, deposit3));
        CrmTbWithdrawalObject withdrawal1 = generateCrmTbWithdrawalObjectByClient(client);
        withdrawal1.paymentType = "local depositor";
        withdrawal1.paymentChannel = "Bison Bucks";
        withdrawal1.createTime = insideTestDate;
        CrmTbWithdrawalObject withdrawal2 = generateCrmTbWithdrawalObjectByClient(client);
        withdrawal2.paymentType = "Bank of Latverya";
        withdrawal2.paymentChannel = "channel";
        CrmTbWithdrawalObject withdrawal3 = generateCrmTbWithdrawalObjectByClient(client);
        withdrawal3.paymentType = "Card";
        withdrawal3.paymentChannel = "Bison Card";
        withdrawal3.createTime = outsideTestDate;
        insertObjectsToDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, List.of(withdrawal1, withdrawal2, withdrawal3));
        MtTbCreditsObject credit1 = generateCreditsByClientRandomized(client);
        credit1.createTime = insideTestDate;
        MtTbCreditsObject credit2 = generateCreditsByClientRandomized(client);
        MtTbCreditsObject credit3 = generateCreditsByClientRandomized(client);
        credit3.createTime = outsideTestDate;
        insertObjectsToDb(MT_CREDITS_TABLE_NAME, List.of(credit1, credit2, credit3));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        paymentsPage.navigatePaymentsTab(client.getUcid());
        Allure.step("filter test date");
        paymentsPage.selectDateFilter("Last 7 days");
        Allure.step("check that only data for the test date is displayed");
        paymentsPage.hoverOverFirstFilledTransactionsGraphByDateSingleDay();
        paymentsPage.checkFinancialTransactionsRowInTooltip("Deposit", dfWholed.format(deposit1.amountUsd));
        paymentsPage.checkFinancialTransactionsRowInTooltip("Withdrawal", dfWholed.format(withdrawal1.amountUsd));
        paymentsPage.checkFinancialTransactionsRowInTooltip("Credit", dfWholed.format(credit1.amountUsd));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("609")
    @DisplayName("Payments tab. User can filter operations by Dates Last 90 days")
    void filterLast90DaysTest() throws Exception {

        Allure.step("generate data inside and outside of tested period");

        String insideTestDate = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 89);
        String outsideTestDate = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 90);

        cleanUserPaymentsDb(client.getUcid());
        CrmTbDepositObject deposit1 = generateDepositByClient(client);
        deposit1.statusId = 5;
        deposit1.paymentType = "Crypto";
        deposit1.paymentChannel = "CryptoCoino";
        deposit1.createTime = insideTestDate;
        CrmTbDepositObject deposit2 = generateDepositByClient(client);
        deposit2.statusId = 5;
        deposit2.paymentType = "Bank";
        deposit2.paymentChannel = "Bankinn";
        CrmTbDepositObject deposit3 = generateDepositByClient(client);
        deposit3.statusId = 5;
        deposit3.paymentType = "Card";
        deposit3.paymentChannel = "cardon";
        deposit3.createTime = outsideTestDate;
        insertObjectsToDb(CRM_DEPOSIT_TABLE_NAME, List.of(deposit1, deposit2, deposit3));
        CrmTbWithdrawalObject withdrawal1 = generateCrmTbWithdrawalObjectByClient(client);
        withdrawal1.paymentType = "local depositor";
        withdrawal1.paymentChannel = "Bison Bucks";
        withdrawal1.createTime = insideTestDate;
        CrmTbWithdrawalObject withdrawal2 = generateCrmTbWithdrawalObjectByClient(client);
        withdrawal2.paymentType = "Bank of Latverya";
        withdrawal2.paymentChannel = "channel";
        CrmTbWithdrawalObject withdrawal3 = generateCrmTbWithdrawalObjectByClient(client);
        withdrawal3.paymentType = "Card";
        withdrawal3.paymentChannel = "Bison Card";
        withdrawal3.createTime = outsideTestDate;
        insertObjectsToDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, List.of(withdrawal1, withdrawal2, withdrawal3));
        MtTbCreditsObject credit1 = generateCreditsByClientRandomized(client);
        credit1.createTime = insideTestDate;
        MtTbCreditsObject credit2 = generateCreditsByClientRandomized(client);
        MtTbCreditsObject credit3 = generateCreditsByClientRandomized(client);
        credit3.createTime = outsideTestDate;
        insertObjectsToDb(MT_CREDITS_TABLE_NAME, List.of(credit1, credit2, credit3));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        paymentsPage.navigatePaymentsTab(client.getUcid());
        Allure.step("filter test date");
        paymentsPage.selectDateFilter("Last 90 days");
        Allure.step("check that only data for the test date is displayed");
        paymentsPage.hoverOverFirstFilledTransactionsGraphByDateSingleDay();
        paymentsPage.checkFinancialTransactionsRowInTooltip("Deposit", dfWholed.format(deposit1.amountUsd));
        paymentsPage.checkFinancialTransactionsRowInTooltip("Withdrawal", dfWholed.format(withdrawal1.amountUsd));
        paymentsPage.checkFinancialTransactionsRowInTooltip("Credit", dfWholed.format(credit1.amountUsd));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("642")
    @DisplayName("Payments tab. User can manipulate timeline by click to a half of timeline")
    void manipulateTimelineByClickTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        paymentsPage.navigatePaymentsTab(client.getUcid());
        paymentsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(5));
        paymentsPage.clickOnPreLastTimelineSection();
        paymentsPage.checkLastTimelineSectionInactive();
        paymentsPage.clickOnTimelineSectionByIndex(1);
        paymentsPage.checkTimelineSectionInactive(0);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("641")
    @DisplayName("Payments tab. User can manipulate timeline by drag")
    void manipulateTimelineByDragTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        paymentsPage.navigatePaymentsTab(client.getUcid());
        paymentsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(5));
        paymentsPage.shiftRightTimelineThumbToPreLastTimelineSection();
        paymentsPage.checkLastTimelineSectionInactive();
        paymentsPage.shiftLeftTimelineThumbToTimelineSectionIndex(2);
        paymentsPage.checkTimelineSectionInactive(0);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("635")
    @DisplayName("Payments tab. When user uses timeline , when user filters 6 days must have 1 inactive day on the right.")
    void timelineInactiveDaysFilter6DaysTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        paymentsPage.navigatePaymentsTab(client.getUcid());
        Allure.step("filter 6 days");
        paymentsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(5));
        paymentsPage.checkTimelineSectionInactive(6);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("634")
    @DisplayName("Payments tab. When user uses timeline , when user filters 5 days must have 1 inactive day on both sides")
    void timelineInactiveDaysFilter5DaysTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        paymentsPage.navigatePaymentsTab(client.getUcid());
        Allure.step("filter 5 days");
        paymentsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(4));
        paymentsPage.checkTimelineSectionInactive(6);
        paymentsPage.checkTimelineSectionInactive(0);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("633")
    @DisplayName("Payments tab. When user uses timeline , when user filters 4 days must have 1 inactive day on the left and 2 on the right.")
    void timelineInactiveDaysFilter4DaysTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        paymentsPage.navigatePaymentsTab(client.getUcid());
        Allure.step("filter 4 days");
        paymentsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(3));
        paymentsPage.checkTimelineSectionInactive(6);
        paymentsPage.checkTimelineSectionInactive(5);
        paymentsPage.checkTimelineSectionInactive(0);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("632")
    @DisplayName("Payments tab. When user uses timeline , when user filters three days must have 2 inactive days on both sides")
    void timelineInactiveDaysFilter3DaysTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        paymentsPage.navigatePaymentsTab(client.getUcid());
        Allure.step("filter 3 days");
        paymentsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(2));
        paymentsPage.checkTimelineSectionInactive(6);
        paymentsPage.checkTimelineSectionInactive(5);
        paymentsPage.checkTimelineSectionInactive(0);
        paymentsPage.checkTimelineSectionInactive(1);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("631")
    @DisplayName("Payments tab. When user uses timeline , when user filters two days must have 2 inactive days on the left and 3 on the right")
    void timelineInactiveDaysFilter2DaysTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        paymentsPage.navigatePaymentsTab(client.getUcid());
        Allure.step("filter 2 days");
        paymentsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(1));
        paymentsPage.checkTimelineSectionInactive(6);
        paymentsPage.checkTimelineSectionInactive(5);
        paymentsPage.checkTimelineSectionInactive(4);
        paymentsPage.checkTimelineSectionInactive(0);
        paymentsPage.checkTimelineSectionInactive(1);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("630")
    @DisplayName("Payments tab. When user uses timeline , when user filters one day must have 3 inactive days on both sides")
    void timelineInactiveDaysFilter1DayTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        paymentsPage.navigatePaymentsTab(client.getUcid());
        Allure.step("filter 1 day");
        paymentsPage.selectDatesInCalendar(getCurrentDate(), getCurrentDate());
        paymentsPage.checkTimelineSectionInactive(6);
        paymentsPage.checkTimelineSectionInactive(5);
        paymentsPage.checkTimelineSectionInactive(4);
        paymentsPage.checkTimelineSectionInactive(0);
        paymentsPage.checkTimelineSectionInactive(1);
        paymentsPage.checkTimelineSectionInactive(2);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("658")
    @DisplayName("Payments tab. Financial transaction graph, when filtered 99 days - 9 months Division = months Timeline = every month")
    void filterLegendFinancialTransaction99DaysAnd10monthsTest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        Allure.step("add to DB transaction in a testing interval so financial transaction graph will be visible");
        cleanUserFinancialTransactionDbUcid(client.getUcid());
        PaymentsTotalObject payments = new PaymentsTotalObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, getYesterdayDate(), 16.3, 17, 18.3, 19, 20.3, 21, 22.3, 23, 24.3, 25, getCurrentTimestampDbFormat());
        insertObjectToDb("vindex_test.payments_total", payments);
        paymentsPage.navigatePaymentsTab(client.getUcid());
        Allure.step("filter 99 days");
        paymentsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(98));
        paymentsPage.checkFinancialTransactionSectionVisibleByDate(Utils.getCurrentDateMonthYear());
        paymentsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDateMonthYearIntMonth(3));
        page.reload();
        Allure.step("filter 98 days");
        paymentsPage.selectDatesInCalendar(getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE, 0, 8, 1, 0, 0), getCurrentDate());
        paymentsPage.checkFinancialTransactionSectionVisibleByDate(Utils.getCurrentDateMonthYear());
        paymentsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDateMonthYearIntMonth(3));
        paymentsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDateMonthYearIntMonth(8));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("701")
    @DisplayName("Payments tab. Financial transaction graph, when filtered 10 months - 20 months Division = months Timeline = every 4 month")
    void filterLegendFinancialTransaction10monthsAnd20monthsTest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        Allure.step("add to DB transaction in a testing interval so financial transaction graph will be visible");
        cleanUserFinancialTransactionDbUcid(client.getUcid());
        PaymentsTotalObject payments = new PaymentsTotalObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, getYesterdayDate(), 16.3, 17, 18.3, 19, 20.3, 21, 22.3, 23, 24.3, 25, getCurrentTimestampDbFormat());
        insertObjectToDb("vindex_test.payments_total", payments);
        paymentsPage.navigatePaymentsTab(client.getUcid());
        Allure.step("filter 10 months");
        paymentsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDateYearMonthDayByIntMonthMinus1Day(10));
        paymentsPage.checkFinancialTransactionSectionVisibleByDate(Utils.getPreviousDateMonthYearIntMonth(1));
        paymentsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDateMonthYearIntMonth(4));
        paymentsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDateMonthYearIntMonth(7));
        paymentsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDateMonthYearIntMonth(10));
        page.reload();
        Allure.step("filter 20 months");
        paymentsPage.selectDatesInCalendar(getPreviousDateYearMonthDayByIntMonthMinus1Day(19), getCurrentDate());
        paymentsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDateMonthYearIntMonth(1));
        paymentsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDateMonthYearIntMonth(4));
        paymentsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDateMonthYearIntMonth(7));
        paymentsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDateMonthYearIntMonth(10));
        paymentsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDateMonthYearIntMonth(19));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("655")
    @DisplayName("Payments tab. Financial transaction graph, when filtered 1-14 days Division = 1 day Timeline = every day")
    void filterLegendFinancialTransaction1DayAnd14DaysTest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        Allure.step("add to DB transaction in a testing interval so financial transaction graph will be visible");
        cleanUserFinancialTransactionDbUcid(client.getUcid());
        PaymentsTotalObject payments = new PaymentsTotalObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), 16.3, 17, 18.3, 19, 20.3, 21, 22.3, 23, 24.3, 25, getCurrentTimestampDbFormat());
        insertObjectToDb("vindex_test.payments_total", payments);
        paymentsPage.navigatePaymentsTab(client.getUcid());
        Allure.step("filter 1 day");
        paymentsPage.selectDatesInCalendar(getCurrentDate(), getCurrentDate());
        paymentsPage.checkFinancialTransactionSectionVisibleByDate(Utils.getCurrentDateMonthDay());
        page.reload();
        Allure.step("filter 14 days");
        paymentsPage.selectDatesInCalendar(getPreviousDayByIntDaysYearMonthDay(13), getCurrentDate());
        paymentsPage.checkFinancialTransactionSectionVisibleByDate(Utils.getCurrentDateMonthDay());
        paymentsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDayMonthDayByIntDay(13));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("656")
    @DisplayName("Payments tab. Financial transaction graph, when filtered 15-20 days Division = 1 day Timeline = every 4 day")
    void filterLegendFinancialTransaction15DaysAnd20DaysTest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        Allure.step("add to DB transaction in a testing interval so financial transaction graph will be visible");
        cleanUserFinancialTransactionDbUcid(client.getUcid());
        PaymentsTotalObject payments = new PaymentsTotalObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), 16.3, 17, 18.3, 19, 20.3, 21, 22.3, 23, 24.3, 25, getCurrentTimestampDbFormat());
        insertObjectToDb("vindex_test.payments_total", payments);
        paymentsPage.navigatePaymentsTab(client.getUcid());
        Allure.step("filter 15 days");
        paymentsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(14));
        paymentsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDayMonthDayByIntDay(2));
        paymentsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDayMonthDayByIntDay(14));
        page.reload();
        Allure.step("filter 20 days");
        paymentsPage.selectDatesInCalendar(getPreviousDayByIntDaysYearMonthDay(19), getCurrentDate());
        paymentsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDayMonthDayByIntDay(3));
        paymentsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDayMonthDayByIntDay(19));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("702")
    @DisplayName("Payments tab. Financial transaction graph, when filtered 20+ months Division = years Timeline = every year")
    void filterLegendFinancialTransactionMoreThan20monthsDaysTest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        Allure.step("add to DB transaction in a testing interval so financial transaction graph will be visible");
        cleanUserFinancialTransactionDbUcid(client.getUcid());
        PaymentsTotalObject payments = new PaymentsTotalObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, getCurrentDate(), 16.3, 17, 18.3, 19, 20.3, 21, 22.3, 23, 24.3, 25, getCurrentTimestampDbFormat());
        insertObjectToDb("vindex_test.payments_total", payments);
        paymentsPage.navigatePaymentsTab(client.getUcid());
        Allure.step("filter 20 months ");
        paymentsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDateYearMonthDayByIntMonthMinus1Day(21));
        paymentsPage.checkFinancialTransactionSectionVisibleByDate(getCurrentYear());
        paymentsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousYearByInt(1));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("657")
    @DisplayName("Payments tab. Financial transaction graph, when filtered 21-98 days Division = week Timeline = every week")
    void filterLegendFinancialTransaction21DaysAnd98DaysTest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        Allure.step("add to DB transaction in a testing interval so financial transaction graph will be visible");
        cleanUserFinancialTransactionDbUcid(client.getUcid());
        PaymentsTotalObject payments = new PaymentsTotalObject(client.getUcid(), "Infinox", "FCA", 171_701, 17_170_101, getYesterdayDate(), 16.3, 17, 18.3, 19, 20.3, 21, 22.3, 23, 24.3, 25, getCurrentTimestampDbFormat());
        insertObjectToDb("vindex_test.payments_total", payments);
        paymentsPage.navigatePaymentsTab(client.getUcid());
        Allure.step("filter 21 days");
        paymentsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(20));
        paymentsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDayMonthDayByIntDay(6));
        paymentsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDayMonthDayByIntDay(20));
        page.reload();
        Allure.step("filter 98 days");
        paymentsPage.selectDatesInCalendar(getPreviousDayByIntDaysYearMonthDay(97), getCurrentDate());
        paymentsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDayMonthDayByIntDay(6));
        paymentsPage.checkFinancialTransactionSectionVisibleByDate(getPreviousDayMonthDayByIntDay(97));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("636")
    @DisplayName("Payments tab. When user filters 1-7 days one division on timeline is 1 day with date under each section")
    void filterLegend1And7DaysTest() throws Exception {
        cleanUserPaymentsDb(client.getUcid());
        CrmTbDepositObject deposit = generateDepositByClient(client);
        deposit.createTimeUtc = (getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 6, 0, 0));
        deposit.createTime = (getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 6, 0, 0));
        deposit.statusId = 5;
        Allure.step("add record about deposit");
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, deposit);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        paymentsPage.navigatePaymentsTab(client.getUcid());
        Allure.step("filter one day");
        paymentsPage.selectDatesInCalendar(getCurrentDate(), getCurrentDate());
        paymentsPage.checkTimelineSectionVisibleByDate(getCurrentDateMonthDay());
        page.reload();
        Allure.step("filter seven days");
        paymentsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(6));
        paymentsPage.checkTimelineSectionVisibleByDate(getCurrentDateMonthDay());
        paymentsPage.checkTimelineSectionVisibleByDate(getPreviousDayMonthDayByIntDay(6));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("637")
    @DisplayName("Payments tab. When user filters 8-98 days one division on timeline is 1 Division = 1 day annotation = Days MON DD")
    void filterLegend8And31DaysTest() throws ParseException {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        paymentsPage.navigatePaymentsTab(client.getUcid());
        Allure.step("filter 8 day");
        Allure.step("filter 8 day");
        paymentsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(7));
        paymentsPage.checkTimelineAnnotationInFormat(MONTH_TEXT_AND_DAY);
        page.reload();
        Allure.step("filter 98 days");
        paymentsPage.selectDatesInCalendar(getPreviousDayByIntDaysYearMonthDay(97), getCurrentDate());
        paymentsPage.checkTimelineAnnotationInFormat(DateTimeFormat.MONTH_TEXT_AND_DAY);
    }


    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("638")
    @DisplayName("Payments tab. When user filters 99-365 days one division on timeline is 1 Division = 1 day annotation = Days MON DD")
    void filterLegend31And98DaysTest() throws ParseException {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        paymentsPage.navigatePaymentsTab(client.getUcid());
        Allure.step("filter 99 day");
        paymentsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(98));
        paymentsPage.checkTimelineAnnotationInFormat(MONTH_TEXT_AND_DAY);
        page.reload();
        Allure.step("filter 365 days");
        paymentsPage.selectDatesInCalendar(getPreviousDayByIntDaysYearMonthDay(363), getCurrentDate());
        paymentsPage.checkTimelineAnnotationInFormat(DAY_SHORT_MONTH_YEAR);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("639")
    @DisplayName("Payments tab. When user filters 1-6 years one Division = 1 week annotation = DD MON YYYY")
    void filterLegend98DaysAnd3YearTest() throws ParseException {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        paymentsPage.navigatePaymentsTab(client.getUcid());
        Allure.step("filter 366 days");
        paymentsPage.selectDatesInCalendar(getCurrentDate(), getPreviousDayByIntDaysYearMonthDay(366));
        paymentsPage.checkTimelineAnnotationInFormat(MONTH_TEXT_AND_YEAR);
        page.reload();
        Allure.step("filter 6 years");
        paymentsPage.selectDatesInCalendar(getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE, 0, 0, 364 * 6, 0, 0), getCurrentDate());
        paymentsPage.checkTimelineAnnotationInFormat(MONTH_TEXT_AND_YEAR);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("640")
    @DisplayName("Payments tab. When user filters 6+ years division on timeline is 1 year with eek annotation = YYYY")
    void filterLegend3YearsTest() throws ParseException {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        paymentsPage.navigatePaymentsTab(client.getUcid());
        Allure.step("filter 7 years");
        paymentsPage.selectDatesInCalendar(getCurrentDate(), getPreviousYearByIntYearMonthDay(7));
        paymentsPage.checkTimelineAnnotationInFormat(YEAR);
    }

    @Disabled("deprecated due to lack of separate empty screen ")
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("574")
    @DisplayName("Payments tab. financialTransaction chart show empty state when it not have data DB")
    void financialTransactionEmptyStateTest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        cleanUserFinancialTransactionDbUcid(client.getUcid());
        paymentsPage.navigatePaymentsTab(client.getUcid());
        paymentsPage.checkFinancialTransactionEmptyStateIsVisible();
    }

}
