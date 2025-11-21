package tests.vindex_backoffice_ui_tests.investigationTool;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntity;
import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntityFactory;
import business_objects.db.clickhouse.crm_tb_transfer.CrmTbTransferObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalEntity;
import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalEntityFactory;
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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateAdditionalStaticCrmTbAccountActive;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateStaticCrmTbAccountActive;
import static business_objects.db.clickhouse.crm_tb_transfer.CrmTbTransferFactory.generateCrmTbTransferRandomized;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateStaticUserByClient;
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
    private static final DateTimeFormatter clickhouseDateTimeStringFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
        insertCrmAccountsToDb(account1, account2);
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
        CrmTbWithdrawalEntity withdrawal = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client);
        withdrawal.setStatusId(7);
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
        CrmTbDepositEntity deposit = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client);
        deposit.setStatusId(5);
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
        CrmTbDepositEntity transaction = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client);
        transaction.setStatusId(5);
        transaction.setPaymentType("Crypto");
        transaction.setPaymentChannel("CryptoCoino");
        transaction.setPaymentFamily("CryptoFamily");
        Allure.step("add record about new deposit with another type");
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, transaction);
        paymentsPage.navigatePaymentsTab(client.getUcid());
        paymentsPage.hoverOverCashflowLineByTypeDeposit(transaction.getPaymentType());
        paymentsPage.checkTotalCountByPaymentSystem(transaction.getPaymentChannel(), dfWholed.format(Math.round(transaction.getAmountUsd().doubleValue())));
        CrmTbDepositEntity transaction2 = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client);
        transaction2.setPaymentType("P2P");
        transaction2.setPaymentChannel("chanel1");
        transaction2.setPaymentFamily("P2PFamily");
        Allure.step("add record about new deposit with another type");
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, transaction2);
        page.waitForTimeout(1000);
        page.reload();
        paymentsPage.hoverOverCashflowLineByTypeDeposit(transaction2.getPaymentType());
        paymentsPage.checkTotalCountByPaymentSystem(transaction2.getPaymentChannel(), transaction2.getAmountUsd().doubleValue());
        CrmTbDepositEntity transaction3 = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client);
        transaction3.setPaymentType("Bank Transfers");
        transaction3.setPaymentChannel("transferno");
        transaction3.setPaymentFamily("Transit");
        Allure.step("add record about new deposit with another type");
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, transaction3);
        page.waitForTimeout(1000);
        page.reload();
        paymentsPage.hoverOverCashflowLineByTypeDeposit(transaction3.getPaymentType());
        paymentsPage.checkTotalCountByPaymentSystem(transaction3.getPaymentChannel(), transaction3.getAmountUsd().doubleValue());
        CrmTbDepositEntity transaction4 = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client);
        transaction4.setPaymentType("Payment Services");
        transaction4.setPaymentChannel("quiwy");
        transaction4.setPaymentFamily("EWall");
        Allure.step("add record about new deposit with another type");
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, transaction4);
        page.waitForTimeout(1000);
        page.reload();
        paymentsPage.hoverOverCashflowLineByTypeDeposit(transaction4.getPaymentType());
        paymentsPage.checkTotalCountByPaymentSystem(transaction4.getPaymentChannel(), transaction4.getAmountUsd().doubleValue());
        CrmTbDepositEntity transaction5 = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client);
        transaction5.setPaymentType("local depositor");
        transaction5.setPaymentChannel("otherway");
        transaction5.setPaymentFamily("otherDepos");
        Allure.step("add record about new deposit with another type");
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, transaction5);
        page.waitForTimeout(1000);
        page.reload();
        paymentsPage.hoverOverCashflowLineByTypeDeposit(transaction5.getPaymentType());
        paymentsPage.checkTotalCountByPaymentSystem(transaction5.getPaymentChannel(), transaction5.getAmountUsd().doubleValue());
        CrmTbDepositEntity transaction6 = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client);
        transaction6.setPaymentType("offline payment");
        transaction6.setPaymentChannel("otherwayBig");
        transaction6.setPaymentFamily("otherwayBigFamily");
        transaction6.setAmountUsd(transaction5.getAmountUsd().add(BigDecimal.valueOf(1.1d)));
        Allure.step("add record about new deposit with existing type and another channel type with greater amount");
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, transaction6);
        page.waitForTimeout(1000);
        page.reload();
        paymentsPage.hoverOverCashflowLineByTypeDeposit(transaction6.getPaymentType());
        paymentsPage.checkTotalCountByPaymentSystem(transaction6.getPaymentChannel(), transaction6.getAmountUsd().doubleValue());
        CrmTbDepositEntity transaction7 = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client);
        transaction7.setPaymentType("offline payment");
        transaction7.setPaymentChannel("otherwayBig");
        transaction7.setPaymentFamily("otherwayBigFamily");
        Allure.step("add record about new deposit with existing type and channel type to check that too;tip show sum");
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, transaction7);
        page.waitForTimeout(1000);
        page.reload();
        paymentsPage.hoverOverCashflowLineByTypeDeposit(transaction6.getPaymentType());
        paymentsPage.checkTotalCountByPaymentSystem(transaction6.getPaymentChannel(), transaction6.getAmountUsd().add(transaction7.getAmountUsd()).doubleValue());
        CrmTbWithdrawalEntity transaction8 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client);
        transaction8.setPaymentType("Crypto");
        transaction8.setPaymentChannel("CryptoCoino");
        transaction8.setPaymentFamily("CryptoFamily");
        CrmTbWithdrawalEntity transaction9 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client);
        transaction9.setPaymentType("P2P");
        transaction9.setPaymentChannel("chanel1");
        transaction9.setPaymentFamily("P2PFamily");
        CrmTbWithdrawalEntity transaction10 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client);
        transaction10.setPaymentType("Bank Transfers");
        transaction10.setPaymentChannel("transferno");
        transaction10.setPaymentFamily("Transit");
        CrmTbWithdrawalEntity transaction11 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client);
        transaction11.setPaymentType("Payment Services");
        transaction11.setPaymentChannel("quiwy");
        transaction11.setPaymentFamily("EWall");
        CrmTbWithdrawalEntity transaction12 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client);
        transaction12.setPaymentType("local depositor");
        transaction12.setPaymentChannel("otherway");
        transaction12.setPaymentFamily("otherDepos");
        insertObjectsToDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, List.of(transaction8, transaction9, transaction10, transaction11, transaction12));
        Allure.step("add withdrawals for every expected payment type to check them displayed ");
        page.waitForTimeout(5000);
        page.reload();
        paymentsPage.hoverOverCashflowLineByTypeWithdrawal(transaction8.getPaymentType());
        paymentsPage.checkTotalCountByPaymentSystem(transaction8.getPaymentChannel(), transaction8.getAmountUsd().doubleValue());
        paymentsPage.hoverOverCashflowLineByTypeWithdrawal(transaction9.getPaymentType());
        paymentsPage.checkTotalCountByPaymentSystem(transaction9.getPaymentChannel(), transaction9.getAmountUsd().doubleValue());
        paymentsPage.hoverOverCashflowLineByTypeWithdrawal(transaction10.getPaymentType());
        paymentsPage.checkTotalCountByPaymentSystem(transaction10.getPaymentChannel(), transaction10.getAmountUsd().doubleValue());
        paymentsPage.hoverOverCashflowLineByTypeWithdrawal(transaction11.getPaymentType());
        paymentsPage.checkTotalCountByPaymentSystem(transaction11.getPaymentChannel(), transaction11.getAmountUsd().doubleValue());
        paymentsPage.hoverOverCashflowLineByTypeWithdrawal(transaction12.getPaymentType());
        paymentsPage.checkTotalCountByPaymentSystem(transaction12.getPaymentChannel(), transaction12.getAmountUsd().doubleValue());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("589")
    @DisplayName("Payments tab. Cashflow header show data from DB Families")
    void cashflowValueInHeaderFamiliesTest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        cleanUserPaymentsDb(client.getUcid());
        CrmTbDepositEntity transaction = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client);
        transaction.setStatusId(5);
        transaction.setPaymentType("Crypto");
        transaction.setPaymentFamily("CryptoFamily");
        transaction.setPaymentChannel("CryptoCoino");
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, transaction);
        Allure.step("add record about deposit");
        page.waitForTimeout(1000);
        paymentsPage.navigatePaymentsTab(client.getUcid());
        paymentsPage.clickPaymentFamilyButton();
        paymentsPage.checkCashflowTopPaymentSourceHeaderDeposit(transaction.getPaymentFamily(), transaction.getAmountUsd().doubleValue());

        CrmTbDepositEntity transaction2 = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client);
        transaction2.setStatusId(5);
        transaction2.setPaymentType("Bank of Latverya");
        transaction2.setPaymentChannel("Doom Crones");
        transaction2.setPaymentFamily("Latverya");
        transaction2.setAmountUsd(transaction.getAmountUsd().add(BigDecimal.valueOf(1.1)));
        Allure.step("add record about new deposit with another type and bigger amount");
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, transaction2);
        page.waitForTimeout(1000);
        page.reload();
        paymentsPage.clickPaymentFamilyButton();
        paymentsPage.checkCashflowTopPaymentSourceHeaderDeposit(transaction2.getPaymentFamily(), transaction2.getAmountUsd().doubleValue());

        CrmTbDepositEntity transaction3 = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client);
        transaction3.setStatusId(5);
        transaction3.setPaymentChannel("SomeBank LLC");
        transaction3.setPaymentFamily(transaction2.getPaymentFamily());
        Allure.step("add record about new deposit with existing in DB and another channel");
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, transaction3);
        page.waitForTimeout(1000);
        page.reload();
        paymentsPage.clickPaymentFamilyButton();
        paymentsPage.checkCashflowTopPaymentSourceHeaderDeposit(transaction2.getPaymentFamily(), transaction2.getAmountUsd().add(transaction3.getAmountUsd()).doubleValue());

        CrmTbWithdrawalEntity transaction4 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client);
        transaction4.setPaymentType("P2Pinocchio");
        transaction4.setPaymentChannel("Pinocchio");
        transaction4.setPaymentFamily("Online paymentino");
        insertObjectToDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, transaction4);
        Allure.step("add record about withdrawal with type that was not used in deposits");
        page.waitForTimeout(1000);
        page.reload();
        paymentsPage.clickPaymentFamilyButton();
        paymentsPage.checkCashflowTopPaymentSourceHeaderWithdrawal(transaction4.getPaymentFamily(), transaction4.getAmountUsd().doubleValue());

        CrmTbWithdrawalEntity transaction5 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client);
        transaction5.setPaymentType("offline payment");
        transaction5.setPaymentChannel("dullas");
        transaction5.setPaymentFamily("offline depository");
        transaction5.setAmountUsd(transaction4.getAmountUsd().add(BigDecimal.valueOf(1.1)));
        insertObjectToDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, transaction5);
        Allure.step("add record about withdrawal with type that was not used early with bigger amount that previous withdrawal");
        page.waitForTimeout(1000);
        page.reload();
        paymentsPage.clickPaymentFamilyButton();
        paymentsPage.checkCashflowTopPaymentSourceHeaderWithdrawal(transaction5.getPaymentFamily(), transaction5.getAmountUsd().doubleValue());

        CrmTbWithdrawalEntity transaction6 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client);
        transaction6.setPaymentType("local depositor");
        transaction6.setPaymentChannel("Bison Bucks");
        transaction6.setPaymentFamily("offline depository");
        insertObjectToDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, transaction6);
        Allure.step("add record about withdrawal with type that was used for withdrawals and check that them summed");
        page.waitForTimeout(1000);
        page.reload();
        paymentsPage.clickPaymentFamilyButton();
        paymentsPage.checkCashflowTopPaymentSourceHeaderWithdrawal(transaction6.getPaymentFamily(), transaction5.getAmountUsd().add(transaction6.getAmountUsd()).doubleValue());
    }

    @Test
    @AllureId("1824")
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Payments tab. Cashflow header show data from DB Profiles")
    void cashflowValueInHeaderProfilesTest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        cleanUserPaymentsDb(client.getUcid());
        CrmTbDepositEntity transaction = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client);
        transaction.setStatusId(5);
        transaction.setPaymentType("Crypto");
        transaction.setPaymentChannel("CryptoCoino");
        transaction.setPaymentProfileMasked("CryptoFamily");
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, transaction);
        Allure.step("add record about deposit");
        page.waitForTimeout(1000);
        paymentsPage.navigatePaymentsTab(client.getUcid());
        paymentsPage.clickPaymentProfileButton();
        paymentsPage.checkCashflowTopPaymentSystemTypesHeaderDeposit(transaction.getPaymentType(), transaction.getAmountUsd().doubleValue());

        CrmTbDepositEntity transaction2 = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client);
        transaction2.setStatusId(5);
        transaction2.setPaymentType("Bank of Latverya");
        transaction2.setPaymentChannel("Doom Crones");
        transaction2.setPaymentProfileMasked("Latverya");
        transaction2.setAmountUsd(transaction.getAmountUsd().add(BigDecimal.valueOf(1.1)));
        Allure.step("add record about new deposit with another type and bigger amount");
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, transaction2);
        page.waitForTimeout(1000);
        page.reload();
        paymentsPage.clickPaymentProfileButton();
        paymentsPage.checkCashflowTopPaymentSystemTypesHeaderDeposit(transaction2.getPaymentType(), transaction2.getAmountUsd().doubleValue());

        CrmTbDepositEntity transaction3 = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client);
        transaction3.setStatusId(5);
        transaction3.setPaymentChannel("SomeBank LLC");
        transaction3.setPaymentProfileMasked(transaction2.getPaymentProfileMasked());
        Allure.step("add record about new deposit with existing in DB and another channel");
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, transaction3);
        page.waitForTimeout(1000);
        page.reload();
        paymentsPage.clickPaymentProfileButton();
        paymentsPage.checkCashflowTopPaymentSystemTypesHeaderDeposit(transaction2.getPaymentType(), transaction2.getAmountUsd().add(transaction3.getAmountUsd()).doubleValue());

        CrmTbWithdrawalEntity transaction4 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client);
        transaction4.setPaymentType("P2Pinocchio");
        transaction4.setPaymentChannel("Pinocchio");
        transaction4.setPaymentProfileMasked("Online paymentino");
        insertObjectToDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, transaction4);
        Allure.step("add record about withdrawal with type that was not used in deposits");
        page.waitForTimeout(1000);
        page.reload();
        paymentsPage.clickPaymentProfileButton();
        paymentsPage.checkCashflowTopPaymentSystemTypesHeaderWithdrawal(transaction4.getPaymentType(), transaction4.getAmountUsd().doubleValue());

        CrmTbWithdrawalEntity transaction5 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client);
        transaction5.setPaymentType("offline payment");
        transaction5.setPaymentChannel("dullas");
        transaction5.setPaymentProfileMasked("offline depository");
        transaction5.setAmountUsd(transaction4.getAmountUsd().add(BigDecimal.valueOf(1.1)));
        insertObjectToDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, transaction5);
        Allure.step("add record about withdrawal with type that was not used early with bigger amount that previous withdrawal");
        page.waitForTimeout(1000);
        page.reload();
        paymentsPage.clickPaymentProfileButton();
        paymentsPage.checkCashflowTopPaymentSystemTypesHeaderWithdrawal(transaction5.getPaymentType(), transaction5.getAmountUsd().doubleValue());

        CrmTbWithdrawalEntity transaction6 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client);
        transaction6.setPaymentType("local depositor");
        transaction6.setPaymentChannel("Bison Bucks");
        transaction6.setPaymentProfileMasked("offline depository");
        insertObjectToDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, transaction6);
        Allure.step("add record about withdrawal with type that was used for withdrawals and check that them summed");
        page.waitForTimeout(1000);
        page.reload();
        paymentsPage.clickPaymentProfileButton();
        paymentsPage.checkCashflowTopPaymentSystemTypesHeaderWithdrawal(transaction6.getPaymentType(), transaction5.getAmountUsd().add(transaction6.getAmountUsd()).doubleValue());

    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("597")
    @DisplayName("Payments tab. financialTransaction tabs show data from DB")
    void financialTransactionTabsShowsDataFromDb() throws Exception {
        cleanUserPaymentsDb(client.getUcid());

        CrmTbDepositEntity deposit1 = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client);
        deposit1.setStatusId(5);
        deposit1.setPaymentType("Crypto");
        deposit1.setPaymentChannel("CryptoCoino");
        CrmTbDepositEntity deposit2 = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client);
        deposit2.setStatusId(5);
        deposit2.setPaymentType("Bank of Latverya");
        deposit2.setPaymentChannel("Doom Crones");
        insertObjectsToDb(CRM_DEPOSIT_TABLE_NAME, List.of(deposit1, deposit2));
        CrmTbWithdrawalEntity withdrawal1 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client);
        withdrawal1.setPaymentType("local depositor");
        withdrawal1.setPaymentChannel("Bison Bucks");
        CrmTbWithdrawalEntity withdrawal2 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client);
        withdrawal2.setPaymentType("Bank of Latverya");
        withdrawal2.setPaymentChannel("channel");
        CrmTbWithdrawalEntity withdrawal3 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client);
        withdrawal3.setPaymentType("Cryptobro");
        withdrawal3.setPaymentChannel("brocoin net");
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

        double totalDeposits = deposit1.getAmountUsd().add(deposit2.getAmountUsd()).doubleValue();
        double totalWithdrawals = withdrawal1.getAmountUsd().add(withdrawal2.getAmountUsd()).add(withdrawal3.getAmountUsd()).doubleValue();
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
        CrmTbDepositEntity deposit1 = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client);
        deposit1.setStatusId(5);
        deposit1.setPaymentType("Crypto");
        deposit1.setPaymentChannel("CryptoCoino");
        CrmTbDepositEntity deposit2 = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client);
        deposit2.setStatusId(5);
        deposit2.setPaymentType("Bank of Latverya");
        deposit2.setPaymentChannel("Doom Crones");
        insertObjectsToDb(CRM_DEPOSIT_TABLE_NAME, List.of(deposit1, deposit2));
        CrmTbWithdrawalEntity withdrawal1 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client);
        withdrawal1.setPaymentType("local depositor");
        withdrawal1.setPaymentChannel("Bison Bucks");
        CrmTbWithdrawalEntity withdrawal2 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client);
        withdrawal2.setPaymentType("Bank of Latverya");
        withdrawal2.setPaymentChannel("channel");
        CrmTbWithdrawalEntity withdrawal3 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client);
        withdrawal3.setPaymentType("Cryptobro");
        withdrawal3.setPaymentChannel("brocoin net");
        insertObjectsToDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, List.of(withdrawal1, withdrawal2, withdrawal3));

        MtTbCreditsObject credit1 = generateCreditsByClientRandomized(client);
        MtTbCreditsObject credit2 = generateCreditsByClientRandomized(client);
        MtTbCreditsObject credit3 = generateCreditsByClientRandomized(client);
        MtTbCreditsObject credit4 = generateCreditsByClientRandomized(client);
        insertObjectsToDb(MT_CREDITS_TABLE_NAME, List.of(credit1, credit2, credit3, credit4));

        CrmTbTransferObject transfer = generateCrmTbTransferRandomized(client);
        insertObjectToDb(CRM_TRANSFERS_TABLE_NAME, transfer);
        page.waitForTimeout(3000);


        double totalDeposits = deposit1.getAmountUsd().add(deposit2.getAmountUsd()).doubleValue();
        double totalWithdrawals = withdrawal1.getAmountUsd().add(withdrawal2.getAmountUsd()).add(withdrawal3.getAmountUsd()).doubleValue();
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
        CrmTbDepositEntity deposit1 = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client);
        deposit1.setStatusId(5);
        deposit1.setPaymentType("Crypto");
        deposit1.setPaymentChannel("CryptoCoino");
        CrmTbDepositEntity deposit2 = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client);
        deposit2.setStatusId(5);
        deposit2.setPaymentType("Bank of Latverya");
        deposit2.setPaymentChannel("Doom Crones");
        insertObjectsToDb(CRM_DEPOSIT_TABLE_NAME, List.of(deposit1, deposit2));
        CrmTbWithdrawalEntity withdrawal1 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client);
        withdrawal1.setPaymentType("local depositor");
        withdrawal1.setPaymentChannel("Bison Bucks");
        CrmTbWithdrawalEntity withdrawal2 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client);
        withdrawal2.setPaymentType("Bank of Latverya");
        withdrawal2.setPaymentChannel("channel");
        CrmTbWithdrawalEntity withdrawal3 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client);
        withdrawal3.setPaymentType("Cryptobro");
        withdrawal3.setPaymentChannel("brocoin net");
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
        CrmTbDepositEntity deposit11 = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client);
        deposit11.setStatusId(5);
        deposit11.setPaymentType("Crypto");
        deposit11.setPaymentChannel("CryptoCoino");
        deposit11.setAccount(BigInteger.valueOf(client.getTradingAccount2()));
        CrmTbDepositEntity deposit12 = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client);
        deposit12.setStatusId(5);
        deposit12.setPaymentType("Bank of Latverya");
        deposit12.setPaymentChannel("Doom Crones");
        deposit12.setAccount(BigInteger.valueOf(client.getTradingAccount2()));
        insertObjectsToDb(CRM_DEPOSIT_TABLE_NAME, List.of(deposit11, deposit12));
        CrmTbWithdrawalEntity withdrawal11 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client);
        withdrawal11.setPaymentType("local depositor");
        withdrawal11.setPaymentChannel("Bison Bucks");
        withdrawal11.setAccount(BigInteger.valueOf(client.getTradingAccount2()));
        CrmTbWithdrawalEntity withdrawal12 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client);
        withdrawal12.setPaymentType("Bank of Latverya");
        withdrawal12.setPaymentChannel("channel");
        withdrawal12.setAccount(BigInteger.valueOf(client.getTradingAccount2()));
        CrmTbWithdrawalEntity withdrawal13 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client);
        withdrawal13.setPaymentType("Cryptobro");
        withdrawal13.setPaymentChannel("brocoin net");
        withdrawal13.setAccount(BigInteger.valueOf(client.getTradingAccount2()));
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

        var testDate = "2024-12-11 14:27:51";
        var testOffsetDate = OffsetDateTime.parse(testDate, clickhouseDateTimeStringFormat);

        cleanUserPaymentsDb(client.getUcid());
        CrmTbDepositEntity deposit1 = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client);
        deposit1.setStatusId(5);
        deposit1.setPaymentType("Crypto");
        deposit1.setPaymentChannel("CryptoCoino");
        deposit1.setCreateTime(testOffsetDate);
        CrmTbDepositEntity deposit2 = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client);
        deposit2.setStatusId(5);
        deposit2.setPaymentType("Crypto");
        deposit2.setPaymentChannel("CryptoCoino");
        insertObjectsToDb(CRM_DEPOSIT_TABLE_NAME, List.of(deposit1, deposit2));
        CrmTbWithdrawalEntity withdrawal1 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client);
        withdrawal1.setPaymentType("local depositor");
        withdrawal1.setPaymentChannel("Bison Bucks");
        withdrawal1.setCreateTime(testOffsetDate);
        CrmTbWithdrawalEntity withdrawal2 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client);
        withdrawal2.setPaymentType("Bank of Latverya");
        withdrawal2.setPaymentChannel("channel");
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
        paymentsPage.checkFinancialTransactionsRowInTooltip("Deposit", dfWholed.format(deposit1.getAmountUsd().doubleValue()));
        paymentsPage.checkFinancialTransactionsRowInTooltip("Withdrawal", dfWholed.format(withdrawal1.getAmountUsd().doubleValue()));
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
        var insideTestOffsetDate = OffsetDateTime.parse(insideTestDate, clickhouseDateTimeStringFormat);
        var outsideTestOffsetDate = OffsetDateTime.parse(outsideTestDate, clickhouseDateTimeStringFormat);

        cleanUserPaymentsDb(client.getUcid());
        CrmTbDepositEntity deposit1 = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client);
        deposit1.setStatusId(5);
        deposit1.setPaymentType("Crypto");
        deposit1.setPaymentChannel("CryptoCoino");
        deposit1.setCreateTime(insideTestOffsetDate);
        CrmTbDepositEntity deposit2 = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client);
        deposit2.setStatusId(5);
        deposit2.setPaymentType("Bank");
        deposit2.setPaymentChannel("Bankinn");
        CrmTbDepositEntity deposit3 = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client);
        deposit3.setStatusId(5);
        deposit3.setPaymentType("Card");
        deposit3.setPaymentChannel("cardon");
        deposit3.setCreateTime(outsideTestOffsetDate);
        insertObjectsToDb(CRM_DEPOSIT_TABLE_NAME, List.of(deposit1, deposit2, deposit3));
        CrmTbWithdrawalEntity withdrawal1 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client);
        withdrawal1.setPaymentType("local depositor");
        withdrawal1.setPaymentChannel("Bison Bucks");
        withdrawal1.setCreateTime(insideTestOffsetDate);
        CrmTbWithdrawalEntity withdrawal2 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client);
        withdrawal2.setPaymentType("Bank of Latverya");
        withdrawal2.setPaymentChannel("channel");
        CrmTbWithdrawalEntity withdrawal3 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client);
        withdrawal3.setPaymentType("Card");
        withdrawal3.setPaymentChannel("Bison Card");
        withdrawal3.setCreateTime(outsideTestOffsetDate);
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
        paymentsPage.checkFinancialTransactionsRowInTooltip("Deposit", dfWholed.format(deposit1.getAmountUsd().doubleValue()));
        paymentsPage.checkFinancialTransactionsRowInTooltip("Withdrawal", dfWholed.format(withdrawal1.getAmountUsd().doubleValue()));
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
        var insideTestOffsetDate = OffsetDateTime.parse(insideTestDate, clickhouseDateTimeStringFormat);
        var outsideTestOffsetDate = OffsetDateTime.parse(outsideTestDate, clickhouseDateTimeStringFormat);

        cleanUserPaymentsDb(client.getUcid());
        CrmTbDepositEntity deposit1 = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client);
        deposit1.setStatusId(5);
        deposit1.setPaymentType("Crypto");
        deposit1.setPaymentChannel("CryptoCoino");
        deposit1.setCreateTime(insideTestOffsetDate);
        CrmTbDepositEntity deposit2 = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client);
        deposit2.setStatusId(5);
        deposit2.setPaymentType("Bank");
        deposit2.setPaymentChannel("Bankinn");
        CrmTbDepositEntity deposit3 = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client);
        deposit3.setStatusId(5);
        deposit3.setPaymentType("Card");
        deposit3.setPaymentChannel("cardon");
        deposit3.setCreateTime(outsideTestOffsetDate);
        insertObjectsToDb(CRM_DEPOSIT_TABLE_NAME, List.of(deposit1, deposit2, deposit3));
        CrmTbWithdrawalEntity withdrawal1 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client);
        withdrawal1.setPaymentType("local depositor");
        withdrawal1.setPaymentChannel("Bison Bucks");
        withdrawal1.setCreateTime(insideTestOffsetDate);
        CrmTbWithdrawalEntity withdrawal2 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client);
        withdrawal2.setPaymentType("Bank of Latverya");
        withdrawal2.setPaymentChannel("channel");
        CrmTbWithdrawalEntity withdrawal3 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client);
        withdrawal3.setPaymentType("Card");
        withdrawal3.setPaymentChannel("Bison Card");
        withdrawal3.setCreateTime(outsideTestOffsetDate);
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
        paymentsPage.checkFinancialTransactionsRowInTooltip("Deposit", dfWholed.format(deposit1.getAmountUsd().doubleValue()));
        paymentsPage.checkFinancialTransactionsRowInTooltip("Withdrawal", dfWholed.format(withdrawal1.getAmountUsd().doubleValue()));
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
        var insideTestOffsetDate = OffsetDateTime.parse(insideTestDate, clickhouseDateTimeStringFormat);
        var outsideTestOffsetDate = OffsetDateTime.parse(outsideTestDate, clickhouseDateTimeStringFormat);

        cleanUserPaymentsDb(client.getUcid());
        CrmTbDepositEntity deposit1 = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client);
        deposit1.setStatusId(5);
        deposit1.setPaymentType("Crypto");
        deposit1.setPaymentChannel("CryptoCoino");
        deposit1.setCreateTime(insideTestOffsetDate);
        CrmTbDepositEntity deposit2 = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client);
        deposit2.setStatusId(5);
        deposit2.setPaymentType("Bank");
        deposit2.setPaymentChannel("Bankinn");
        CrmTbDepositEntity deposit3 = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client);
        deposit3.setStatusId(5);
        deposit3.setPaymentType("Card");
        deposit3.setPaymentChannel("cardon");
        deposit3.setCreateTime(outsideTestOffsetDate);
        insertObjectsToDb(CRM_DEPOSIT_TABLE_NAME, List.of(deposit1, deposit2, deposit3));
        CrmTbWithdrawalEntity withdrawal1 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client);
        withdrawal1.setPaymentType("local depositor");
        withdrawal1.setPaymentChannel("Bison Bucks");
        withdrawal1.setCreateTime(insideTestOffsetDate);
        CrmTbWithdrawalEntity withdrawal2 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client);
        withdrawal2.setPaymentType("Bank of Latverya");
        withdrawal2.setPaymentChannel("channel");
        CrmTbWithdrawalEntity withdrawal3 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client);
        withdrawal3.setPaymentType("Card");
        withdrawal3.setPaymentChannel("Bison Card");
        withdrawal3.setCreateTime(outsideTestOffsetDate);
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
        paymentsPage.checkFinancialTransactionsRowInTooltip("Deposit", dfWholed.format(deposit1.getAmountUsd().doubleValue()));
        paymentsPage.checkFinancialTransactionsRowInTooltip("Withdrawal", dfWholed.format(withdrawal1.getAmountUsd().doubleValue()));
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
        var insideTestOffsetDate = OffsetDateTime.parse(insideTestDate, clickhouseDateTimeStringFormat);
        var outsideTestOffsetDate = OffsetDateTime.parse(outsideTestDate, clickhouseDateTimeStringFormat);

        cleanUserPaymentsDb(client.getUcid());
        CrmTbDepositEntity deposit1 = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client);
        deposit1.setStatusId(5);
        deposit1.setPaymentType("Crypto");
        deposit1.setPaymentChannel("CryptoCoino");
        deposit1.setCreateTime(insideTestOffsetDate);
        CrmTbDepositEntity deposit2 = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client);
        deposit2.setStatusId(5);
        deposit2.setPaymentType("Bank");
        deposit2.setPaymentChannel("Bankinn");
        CrmTbDepositEntity deposit3 = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client);
        deposit3.setStatusId(5);
        deposit3.setPaymentType("Card");
        deposit3.setPaymentChannel("cardon");
        deposit3.setCreateTime(outsideTestOffsetDate);
        insertObjectsToDb(CRM_DEPOSIT_TABLE_NAME, List.of(deposit1, deposit2, deposit3));
        CrmTbWithdrawalEntity withdrawal1 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client);
        withdrawal1.setPaymentType("local depositor");
        withdrawal1.setPaymentChannel("Bison Bucks");
        withdrawal1.setCreateTime(insideTestOffsetDate);
        CrmTbWithdrawalEntity withdrawal2 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client);
        withdrawal2.setPaymentType("Bank of Latverya");
        withdrawal2.setPaymentChannel("channel");
        CrmTbWithdrawalEntity withdrawal3 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client);
        withdrawal3.setPaymentType("Card");
        withdrawal3.setPaymentChannel("Bison Card");
        withdrawal3.setCreateTime(outsideTestOffsetDate);
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
        paymentsPage.checkFinancialTransactionsRowInTooltip("Deposit", dfWholed.format(deposit1.getAmountUsd().doubleValue()));
        paymentsPage.checkFinancialTransactionsRowInTooltip("Withdrawal", dfWholed.format(withdrawal1.getAmountUsd().doubleValue()));
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
        var insideTestOffsetDate = OffsetDateTime.parse(insideTestDate, clickhouseDateTimeStringFormat);
        var outsideTestOffsetDate = OffsetDateTime.parse(outsideTestDate, clickhouseDateTimeStringFormat);

        cleanUserPaymentsDb(client.getUcid());
        CrmTbDepositEntity deposit1 = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client);
        deposit1.setStatusId(5);
        deposit1.setPaymentType("Crypto");
        deposit1.setPaymentChannel("CryptoCoino");
        deposit1.setCreateTime(insideTestOffsetDate);
        CrmTbDepositEntity deposit2 = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client);
        deposit2.setStatusId(5);
        deposit2.setPaymentType("Bank");
        deposit2.setPaymentChannel("Bankinn");
        CrmTbDepositEntity deposit3 = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client);
        deposit3.setStatusId(5);
        deposit3.setPaymentType("Card");
        deposit3.setPaymentChannel("cardon");
        deposit3.setCreateTime(outsideTestOffsetDate);
        insertObjectsToDb(CRM_DEPOSIT_TABLE_NAME, List.of(deposit1, deposit2, deposit3));
        CrmTbWithdrawalEntity withdrawal1 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client);
        withdrawal1.setPaymentType("local depositor");
        withdrawal1.setPaymentChannel("Bison Bucks");
        withdrawal1.setCreateTime(insideTestOffsetDate);
        CrmTbWithdrawalEntity withdrawal2 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client);
        withdrawal2.setPaymentType("Bank of Latverya");
        withdrawal2.setPaymentChannel("channel");
        CrmTbWithdrawalEntity withdrawal3 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client);
        withdrawal3.setPaymentType("Card");
        withdrawal3.setPaymentChannel("Bison Card");
        withdrawal3.setCreateTime(outsideTestOffsetDate);
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
        paymentsPage.checkFinancialTransactionsRowInTooltip("Deposit", dfWholed.format(deposit1.getAmountUsd()));
        paymentsPage.checkFinancialTransactionsRowInTooltip("Withdrawal", dfWholed.format(withdrawal1.getAmountUsd().doubleValue()));
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
        insertObjectToDb("consolidated.payments_total", payments);
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
        insertObjectToDb("consolidated.payments_total", payments);
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
        insertObjectToDb("consolidated.payments_total", payments);
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
        insertObjectToDb("consolidated.payments_total", payments);
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
        insertObjectToDb("consolidated.payments_total", payments);
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
        insertObjectToDb("consolidated.payments_total", payments);
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
        CrmTbDepositEntity deposit = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client);
        deposit.setCreateTimeUtc(OffsetDateTime.now().minusDays(6));
        deposit.setCreateTime(OffsetDateTime.now().minusDays(6));
        deposit.setStatusId(5);
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
