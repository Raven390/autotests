package tests.vindex_backoffice_ui_tests.investigationTool.paymentsTab.summarySubTab;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateAdditionalStaticCrmTbAccountActive;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateStaticCrmTbAccountActive;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateStaticUserByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static helpers.database.DbHelper.insertObjectToDb;
import static helpers.database.DbHelper.insertObjectsToDb;
import static helpers.database.OperationsHelper.cleanUserPaymentsDb;
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
import helpers.data.enums.Brand;
import helpers.data.enums.Regulator;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

class PaymentsTabTest extends TestBaseWeb {

    private static final ClientHelper client;

    static {
        client = ClientHelper.builder()
                .userId(313_102)
                .uid("e5880ca5-8578-4a1e-969d-7a64716ca41f")
                .brand(Brand.INFINOX)
                .regulator(Regulator.FCA)
                .tradingAccount(313_102_001)
                .tradingAccount2(313_102_002)
                .serverId(42)
                .build();
    }

    private static CrmTbUserObject crmTbUser = generateStaticUserByClient(client);
    private static CrmTbAccountObject account1 = generateStaticCrmTbAccountActive(client);
    private static CrmTbAccountObject account2 = generateAdditionalStaticCrmTbAccountActive(client);
    private static MtAccountObject mtAccount1 = generateMtAccountByCrmTbAccount(account1);
    private static MtAccountObject mtAccount2 = generateMtAccountByCrmTbAccount(account2);

    @BeforeAll
    static void setup() {
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
        paymentsPage.checkCashflowTopPaymentSourceHeaderDeposit(
                transaction.getPaymentFamily(), transaction.getAmountUsd().doubleValue());

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
        paymentsPage.checkCashflowTopPaymentSourceHeaderDeposit(
                transaction2.getPaymentFamily(), transaction2.getAmountUsd().doubleValue());

        CrmTbDepositEntity transaction3 = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client);
        transaction3.setStatusId(5);
        transaction3.setPaymentChannel("SomeBank LLC");
        transaction3.setPaymentFamily(transaction2.getPaymentFamily());
        Allure.step("add record about new deposit with existing in DB and another channel");
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, transaction3);
        page.waitForTimeout(1000);
        page.reload();
        paymentsPage.clickPaymentFamilyButton();
        paymentsPage.checkCashflowTopPaymentSourceHeaderDeposit(
                transaction2.getPaymentFamily(),
                transaction2.getAmountUsd().add(transaction3.getAmountUsd()).doubleValue());

        CrmTbWithdrawalEntity transaction4 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client);
        transaction4.setPaymentType("P2Pinocchio");
        transaction4.setPaymentChannel("Pinocchio");
        transaction4.setPaymentFamily("Online paymentino");
        transaction4.setStatusId(7);
        insertObjectToDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, transaction4);
        Allure.step("add record about withdrawal with type that was not used in deposits");
        page.waitForTimeout(1000);
        page.reload();
        paymentsPage.clickPaymentFamilyButton();
        paymentsPage.checkCashflowTopPaymentSourceHeaderWithdrawal(
                transaction4.getPaymentFamily(), transaction4.getAmountUsd().doubleValue());

        CrmTbWithdrawalEntity transaction5 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client);
        transaction5.setPaymentType("offline payment");
        transaction5.setPaymentChannel("dullas");
        transaction5.setPaymentFamily("offline depository");
        transaction5.setStatusId(7);
        transaction5.setAmountUsd(transaction4.getAmountUsd().add(BigDecimal.valueOf(1.1)));
        insertObjectToDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, transaction5);
        Allure.step(
                "add record about withdrawal with type that was not used early with bigger amount that previous withdrawal");
        page.waitForTimeout(1000);
        page.reload();
        paymentsPage.clickPaymentFamilyButton();
        paymentsPage.checkCashflowTopPaymentSourceHeaderWithdrawal(
                transaction5.getPaymentFamily(), transaction5.getAmountUsd().doubleValue());

        CrmTbWithdrawalEntity transaction6 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client);
        transaction6.setPaymentType("local depositor");
        transaction6.setPaymentChannel("Bison Bucks");
        transaction6.setPaymentFamily("offline depository");
        transaction6.setStatusId(7);
        insertObjectToDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, transaction6);
        Allure.step("add record about withdrawal with type that was used for withdrawals and check that them summed");
        page.waitForTimeout(1000);
        page.reload();
        paymentsPage.clickPaymentFamilyButton();
        paymentsPage.checkCashflowTopPaymentSourceHeaderWithdrawal(
                transaction6.getPaymentFamily(),
                transaction5.getAmountUsd().add(transaction6.getAmountUsd()).doubleValue());
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
        paymentsPage.checkCashflowTopPaymentSystemTypesHeaderDeposit(
                transaction.getPaymentType(), transaction.getAmountUsd().doubleValue());

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
        paymentsPage.checkCashflowTopPaymentSystemTypesHeaderDeposit(
                transaction2.getPaymentType(), transaction2.getAmountUsd().doubleValue());

        CrmTbDepositEntity transaction3 = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client);
        transaction3.setStatusId(5);
        transaction3.setPaymentChannel("SomeBank LLC");
        transaction3.setPaymentProfileMasked(transaction2.getPaymentProfileMasked());
        Allure.step("add record about new deposit with existing in DB and another channel");
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, transaction3);
        page.waitForTimeout(1000);
        page.reload();
        paymentsPage.clickPaymentProfileButton();
        paymentsPage.checkCashflowTopPaymentSystemTypesHeaderDeposit(
                transaction2.getPaymentType(),
                transaction2.getAmountUsd().add(transaction3.getAmountUsd()).doubleValue());

        CrmTbWithdrawalEntity transaction4 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client);
        transaction4.setPaymentType("P2Pinocchio");
        transaction4.setPaymentChannel("Pinocchio");
        transaction4.setPaymentProfileMasked("Online paymentino");
        insertObjectToDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, transaction4);
        Allure.step("add record about withdrawal with type that was not used in deposits");
        page.waitForTimeout(1000);
        page.reload();
        paymentsPage.clickPaymentProfileButton();
        paymentsPage.checkCashflowTopPaymentSystemTypesHeaderWithdrawal(
                transaction4.getPaymentType(), transaction4.getAmountUsd().doubleValue());

        CrmTbWithdrawalEntity transaction5 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client);
        transaction5.setPaymentType("offline payment");
        transaction5.setPaymentChannel("dullas");
        transaction5.setPaymentProfileMasked("offline depository");
        transaction5.setAmountUsd(transaction4.getAmountUsd().add(BigDecimal.valueOf(1.1)));
        insertObjectToDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, transaction5);
        Allure.step(
                "add record about withdrawal with type that was not used early with bigger amount that previous withdrawal");
        page.waitForTimeout(1000);
        page.reload();
        paymentsPage.clickPaymentProfileButton();
        paymentsPage.checkCashflowTopPaymentSystemTypesHeaderWithdrawal(
                transaction5.getPaymentType(), transaction5.getAmountUsd().doubleValue());

        CrmTbWithdrawalEntity transaction6 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client);
        transaction6.setPaymentType("local depositor");
        transaction6.setPaymentChannel("Bison Bucks");
        transaction6.setPaymentProfileMasked("offline depository");
        insertObjectToDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, transaction6);
        Allure.step("add record about withdrawal with type that was used for withdrawals and check that them summed");
        page.waitForTimeout(1000);
        page.reload();
        paymentsPage.clickPaymentProfileButton();
        paymentsPage.checkCashflowTopPaymentSystemTypesHeaderWithdrawal(
                transaction6.getPaymentType(),
                transaction5.getAmountUsd().add(transaction6.getAmountUsd()).doubleValue());
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
    @DisplayName(
            "Payments tab. When user uses timeline , when user filters 6 days must have 1 inactive day on the right.")
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
    @DisplayName(
            "Payments tab. When user uses timeline , when user filters 5 days must have 1 inactive day on both sides")
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
    @DisplayName(
            "Payments tab. When user uses timeline , when user filters 4 days must have 1 inactive day on the left and 2 on the right.")
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
    @DisplayName(
            "Payments tab. When user uses timeline , when user filters three days must have 2 inactive days on both sides")
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
    @DisplayName(
            "Payments tab. When user uses timeline , when user filters two days must have 2 inactive days on the left and 3 on the right")
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
    @DisplayName(
            "Payments tab. When user uses timeline , when user filters one day must have 3 inactive days on both sides")
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
}
