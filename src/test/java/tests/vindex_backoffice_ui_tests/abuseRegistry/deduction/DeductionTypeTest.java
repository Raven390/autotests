package tests.vindex_backoffice_ui_tests.abuseRegistry.deduction;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.kafka.alerts.RuleAlertFactory.generateRuleAlertByUcid;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.FraudType.*;
import static helpers.data.enums.FraudTypeStatus.CONFIRMED;
import static helpers.database.ArHelper.deleteUserFromAbuseRegistry;
import static helpers.database.AuHelper.cleanClientAudit;
import static helpers.database.BoHelper.deleteUserBO;
import static helpers.database.DbHelper.*;
import static helpers.database.DbName.CLICKHOUSE;
import static helpers.database.DbName.POSTGRES;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static utils.Constants.*;
import static utils.Utils.getRandomIntPositive;

import business_objects.db.abuse_registry_db.AbuserDeduction;
import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.mt_account.MtAccountObject;
import business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObject;
import business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObjectFactory;
import business_objects.kafka.alerts.RuleAlert;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.data.ClientHelper;
import helpers.data.enums.FraudSubtype;
import helpers.database.DbName;
import io.qameta.allure.AllureId;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

public class DeductionTypeTest extends TestBaseWeb {
    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final String ucid = client.getUcid();
    private static final CrmTbUserObject clientUser = generateUserByClient(client);
    private static final CrmTbAccountObject account1 = generateCrmTbAccountDataForUi(client);
    private static final CrmTbAccountObject account2 = generateCrmTbAccountDataForUi(client);
    private static final CrmTbAccountObject account3 = generateCrmTbAccountDataForUi(client);
    private static final RuleAlert alert = generateRuleAlertByUcid(client);
    private static final RuleAlert alert2 = generateRuleAlertByUcid(client);
    private static final RuleAlert alert3 = generateRuleAlertByUcid(client);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static MtAccountObject mtAccount1;
    private static MtAccountObject mtAccount2;
    private static MtAccountObject mtAccount3;

    private static MtMt4TradesCoercedObject mtMt4TradesCoercedObject;
    private static MtMt4TradesCoercedObject mtMt4TradesCoercedObject2;
    private static MtMt4TradesCoercedObject mtMt4TradesCoercedObject3;

    @BeforeAll
    static void setup() {
        account2.setAccount(client.getTradingAccount2());
        account3.setAccount(getRandomIntPositive());
        mtAccount1 = generateMtAccountByCrmTbAccount(account1);
        mtAccount2 = generateMtAccountByCrmTbAccount(account2);
        mtAccount3 = generateMtAccountByCrmTbAccount(account3);

        mtMt4TradesCoercedObject = MtMt4TradesCoercedObjectFactory.generateMt4TradesCoercedRandomized(client);
        mtMt4TradesCoercedObject.setTicketType("Buy");
        mtMt4TradesCoercedObject.setReasonName("Client");
        mtMt4TradesCoercedObject.setProfit(1200.50);
        mtMt4TradesCoercedObject.setStorage(1400.50);
        mtMt4TradesCoercedObject.setCommission(1600.00);
        mtMt4TradesCoercedObject.setProfitUsd(1200.50);
        mtMt4TradesCoercedObject.setStorageUsd(1400.50);
        mtMt4TradesCoercedObject.setCommissionUsd(1600.00);
        mtMt4TradesCoercedObject2 = MtMt4TradesCoercedObjectFactory.generateMt4TradesCoercedRandomized(client);
        mtMt4TradesCoercedObject2.setTicketType("Buy");
        mtMt4TradesCoercedObject2.setReasonName("Client");
        mtMt4TradesCoercedObject2.setProfit(2200.50);
        mtMt4TradesCoercedObject2.setStorage(2400.50);
        mtMt4TradesCoercedObject2.setCommission(2600.00);
        mtMt4TradesCoercedObject2.setProfitUsd(2200.50);
        mtMt4TradesCoercedObject2.setStorageUsd(2400.50);
        mtMt4TradesCoercedObject2.setCommissionUsd(2600.00);
        mtMt4TradesCoercedObject2.setAccount(Long.valueOf(client.getTradingAccount2()));
        mtMt4TradesCoercedObject3 = MtMt4TradesCoercedObjectFactory.generateMt4TradesCoercedRandomized(client);
        mtMt4TradesCoercedObject3.setTicketType("Buy");
        mtMt4TradesCoercedObject3.setReasonName("Client");
        mtMt4TradesCoercedObject3.setProfit(3200.50);
        mtMt4TradesCoercedObject3.setStorage(3400.50);
        mtMt4TradesCoercedObject3.setCommission(3600.00);
        mtMt4TradesCoercedObject3.setProfitUsd(3200.50);
        mtMt4TradesCoercedObject3.setStorageUsd(3400.50);
        mtMt4TradesCoercedObject3.setCommissionUsd(3600.00);
        mtMt4TradesCoercedObject3.setAccount(Long.valueOf(account3.getAccount()));

        insertObjectsToDb(DbName.CLICKHOUSE, CRM_USER_TABLE_NAME, List.of(clientUser));
        insertObjectsToDb(DbName.CLICKHOUSE, CRM_TB_ACCOUNT_FOR_MT_TABLE_NAME, List.of(account1, account2, account3));
        insertObjectsToDb(DbName.CLICKHOUSE, CRM_TB_ACCOUNT_TABLE_NAME, List.of(account1, account2, account3));
        insertObjectsToDb(DbName.CLICKHOUSE, MT_ACCOUNT_TABLE_NAME, List.of(mtAccount1, mtAccount2, mtAccount3));
        insertObjectsToDb(
                DbName.CLICKHOUSE,
                MT4_TRADES_COERCED_TABLE_NAME,
                List.of(mtMt4TradesCoercedObject, mtMt4TradesCoercedObject2, mtMt4TradesCoercedObject3));
    }

    @AfterAll
    static void teardown() throws Exception {
        deleteObjectsFromDb(CLICKHOUSE, MT4_TRADES_COERCED_TABLE_NAME, "ucid", List.of(ucid));
        deleteObjectsFromDb(CLICKHOUSE, CRM_USER_TABLE_NAME, "ucid", List.of(ucid));
    }

    @AfterEach
    void cleanAbuseRegistryAndBoAndAu() throws Exception {
        deleteUserFromAbuseRegistry(ucid);
        deleteUserBO(ucid);
        cleanClientAudit(ucid);
    }

    @Test
    @AllureId("2061")
    @DisplayName("Manage Fraud: FULL_DEDUCTION deductions for selected illegal profit accounts")
    void fullDeductionManageFraudTest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(ucid);
        alertsPage.waitForPageToLoad();
        resolvePage.openReportFraudForm();
        resolvePage.addFraud(HEDGING, CONFIRMED, FraudSubtype.EXTERNAL);
        resolvePage.selectFraudSource("Vindex");
        resolvePage.clickIllegalProfitAccountsDropdown();
        resolvePage.clickAccountInDropdown(account1.account.toString());
        resolvePage.clickAccountInDropdown(account2.account.toString());
        resolvePage.clickAccountInDropdown(account3.account.toString());
        resolvePage.clickUseAsIllegalProfit();
        resolvePage.fillCommentAndApply("test comment");
        var deductionList = getObjectsFromDB(
                POSTGRES, AR_ABUSER_DEDUCTION_TABLE_NAME, String.format("ucid = '%s'", ucid), AbuserDeduction.class);

        assertEquals(3, deductionList.size(), "Expected exactly 3 deductions (one per account)");

        var expectedAccounts = Set.of(
                String.valueOf(account1.account), String.valueOf(account2.account), String.valueOf(account3.account));

        var actualAccounts = deductionList.stream()
                .map(AbuserDeduction::getAccount)
                .map(String::valueOf)
                .collect(java.util.stream.Collectors.toSet());

        assertEquals(expectedAccounts, actualAccounts, "Expected exactly one deduction for each selected account");

        assertTrue(
                deductionList.stream().allMatch(d -> "FULL_DEDUCTION".equals(d.getDeductionType())),
                "Expected deductionType = FULL_DEDUCTION for all created deductions");
    }

    @Test
    @AllureId("2062")
    @DisplayName("Manage Fraud: PARTIAL_DEDUCTION deductions for saved illegal profit trades")
    void partialDeductionManageFraudTest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigateOperations(ucid);
        tradingPage.waitForPageToLoad();
        tradingPage.clickIllegalProfitButton();

        tradingPage.selectIllegalTradeByTicket(mtMt4TradesCoercedObject.getTicket());
        tradingPage.selectIllegalTradeByTicket(mtMt4TradesCoercedObject2.getTicket());
        tradingPage.selectIllegalTradeByTicket(mtMt4TradesCoercedObject3.getTicket());

        tradingPage.clickSaveAsIllegalProfit();

        investigationPage.navigateToClient(ucid);
        alertsPage.waitForPageToLoad();
        resolvePage.openReportFraudForm();
        resolvePage.addFraud(HEDGING, CONFIRMED, FraudSubtype.INTERNAL);
        resolvePage.selectFraudSource("Vindex");
        resolvePage.fillCommentAndApply("test comment");
        var deductionList = getObjectsFromDB(
                POSTGRES, AR_ABUSER_DEDUCTION_TABLE_NAME, String.format("ucid = '%s'", ucid), AbuserDeduction.class);

        assertEquals(3, deductionList.size(), "Expected exactly 3 deductions (one per account)");

        var expectedAccounts = Set.of(
                String.valueOf(account1.account), String.valueOf(account2.account), String.valueOf(account3.account));

        var actualAccounts = deductionList.stream()
                .map(AbuserDeduction::getAccount)
                .map(String::valueOf)
                .collect(java.util.stream.Collectors.toSet());

        assertEquals(expectedAccounts, actualAccounts, "Expected exactly one deduction for each selected account");

        assertTrue(
                deductionList.stream().allMatch(d -> "PARTIAL_DEDUCTION".equals(d.getDeductionType())),
                "Expected deductionType = PARTIAL_DEDUCTION for all created deductions");
    }

    @Test
    @AllureId("2063")
    @DisplayName("Manage Fraud: NO_DEDUCTION deductions for selected accounts")
    void noDeductionManageFraudTest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(ucid);
        alertsPage.waitForPageToLoad();
        resolvePage.openReportFraudForm();
        resolvePage.addFraud(REBATE_CHURNING, CONFIRMED);
        resolvePage.selectFraudSource("Vindex");
        resolvePage.clickNoDeductionFraudAccountSelect();
        resolvePage.clickNoDeductionFraudAccountCheckbox(account1.account.toString());
        resolvePage.clickNoDeductionFraudAccountCheckbox(account2.account.toString());
        resolvePage.clickNoDeductionFraudAccountCheckbox(account3.account.toString());
        resolvePage.clickSaveAsFraud();
        resolvePage.fillCommentAndApply("test comment");
        var deductionList = getObjectsFromDB(
                POSTGRES, AR_ABUSER_DEDUCTION_TABLE_NAME, String.format("ucid = '%s'", ucid), AbuserDeduction.class);

        assertEquals(3, deductionList.size(), "Expected exactly 3 deductions (one per account)");

        var expectedAccounts = Set.of(
                String.valueOf(account1.account), String.valueOf(account2.account), String.valueOf(account3.account));

        var actualAccounts = deductionList.stream()
                .map(AbuserDeduction::getAccount)
                .map(String::valueOf)
                .collect(java.util.stream.Collectors.toSet());

        assertEquals(expectedAccounts, actualAccounts, "Expected exactly one deduction for each selected account");

        assertTrue(
                deductionList.stream().allMatch(d -> "NO_DEDUCTION".equals(d.getDeductionType())),
                "Expected deductionType = NO_DEDUCTION for all created deductions");
    }

    @Test
    @AllureId("2064")
    @DisplayName("Alert Resolve: FULL_DEDUCTION deductions for selected illegal profit accounts")
    void fullDeductionResolveTest() throws Exception {
        kafka.produceMessages(alert.alertId, KAFKA_TOPIC_ALERTS, objectMapper.writeValueAsString(alert));
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(client.getUcid());
        investigationPage.waitForPageToLoad();
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.addFraud(HEDGING, CONFIRMED, FraudSubtype.EXTERNAL);
        resolvePage.clickIllegalProfitAccountsDropdown();
        resolvePage.clickAccountInDropdown(account1.account.toString());
        resolvePage.clickAccountInDropdown(account2.account.toString());
        resolvePage.clickAccountInDropdown(account3.account.toString());
        resolvePage.clickUseAsIllegalProfit();
        resolvePage.resolveNoActions("test comment");
        resolvePage.waitForPageToLoad();
        var deductionList = getObjectsFromDB(
                POSTGRES, AR_ABUSER_DEDUCTION_TABLE_NAME, String.format("ucid = '%s'", ucid), AbuserDeduction.class);

        assertEquals(3, deductionList.size(), "Expected exactly 3 deductions (one per account)");

        var expectedAccounts = Set.of(
                String.valueOf(account1.account), String.valueOf(account2.account), String.valueOf(account3.account));

        var actualAccounts = deductionList.stream()
                .map(AbuserDeduction::getAccount)
                .map(String::valueOf)
                .collect(java.util.stream.Collectors.toSet());

        assertEquals(expectedAccounts, actualAccounts, "Expected exactly one deduction for each selected account");

        assertTrue(
                deductionList.stream().allMatch(d -> "FULL_DEDUCTION".equals(d.getDeductionType())),
                "Expected deductionType = FULL_DEDUCTION for all created deductions");
    }

    @Test
    @AllureId("2065")
    @DisplayName("Alert Resolve: PARTIAL_DEDUCTION deductions for saved illegal profit trades")
    void partialDeductionResolveTest() throws Exception {
        kafka.produceMessages(alert2.alertId, KAFKA_TOPIC_ALERTS, objectMapper.writeValueAsString(alert2));
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigateOperations(ucid);
        tradingPage.waitForPageToLoad();
        tradingPage.clickIllegalProfitButton();

        tradingPage.selectIllegalTradeByTicket(mtMt4TradesCoercedObject.getTicket());
        tradingPage.selectIllegalTradeByTicket(mtMt4TradesCoercedObject2.getTicket());
        tradingPage.selectIllegalTradeByTicket(mtMt4TradesCoercedObject3.getTicket());

        tradingPage.clickSaveAsIllegalProfit();

        investigationPage.navigateToClient(client.getUcid());
        investigationPage.waitForPageToLoad();
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.addFraud(HEDGING, CONFIRMED, FraudSubtype.INTERNAL);
        resolvePage.resolveNoActions("test comment");
        resolvePage.waitForPageToLoad();
        var deductionList = getObjectsFromDB(
                POSTGRES, AR_ABUSER_DEDUCTION_TABLE_NAME, String.format("ucid = '%s'", ucid), AbuserDeduction.class);

        assertEquals(3, deductionList.size(), "Expected exactly 3 deductions (one per account)");

        var expectedAccounts = Set.of(
                String.valueOf(account1.account), String.valueOf(account2.account), String.valueOf(account3.account));

        var actualAccounts = deductionList.stream()
                .map(AbuserDeduction::getAccount)
                .map(String::valueOf)
                .collect(java.util.stream.Collectors.toSet());

        assertEquals(expectedAccounts, actualAccounts, "Expected exactly one deduction for each selected account");

        assertTrue(
                deductionList.stream().allMatch(d -> "PARTIAL_DEDUCTION".equals(d.getDeductionType())),
                "Expected deductionType = PARTIAL_DEDUCTION for all created deductions");
    }

    @Test
    @AllureId("2066")
    @DisplayName("Alert Resolve: NO_DEDUCTION deductions for selected accounts")
    void noDeductionResolveTest() throws Exception {
        kafka.produceMessages(alert3.alertId, KAFKA_TOPIC_ALERTS, objectMapper.writeValueAsString(alert3));
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(client.getUcid());
        investigationPage.waitForPageToLoad();
        investigationPage.investigateClientCard();
        resolvePage.openResolveSuspicious();
        resolvePage.addFraud(REBATE_CHURNING, CONFIRMED);
        resolvePage.clickNoDeductionFraudAccountSelect();
        resolvePage.clickNoDeductionFraudAccountCheckbox(account1.account.toString());
        resolvePage.clickNoDeductionFraudAccountCheckbox(account2.account.toString());
        resolvePage.clickNoDeductionFraudAccountCheckbox(account3.account.toString());
        resolvePage.clickSaveAsFraud();
        resolvePage.resolveNoActions("test comment");
        resolvePage.waitForPageToLoad();
        var deductionList = getObjectsFromDB(
                POSTGRES, AR_ABUSER_DEDUCTION_TABLE_NAME, String.format("ucid = '%s'", ucid), AbuserDeduction.class);

        assertEquals(3, deductionList.size(), "Expected exactly 3 deductions (one per account)");

        var expectedAccounts = Set.of(
                String.valueOf(account1.account), String.valueOf(account2.account), String.valueOf(account3.account));

        var actualAccounts = deductionList.stream()
                .map(AbuserDeduction::getAccount)
                .map(String::valueOf)
                .collect(java.util.stream.Collectors.toSet());

        assertEquals(expectedAccounts, actualAccounts, "Expected exactly one deduction for each selected account");

        assertTrue(
                deductionList.stream().allMatch(d -> "NO_DEDUCTION".equals(d.getDeductionType())),
                "Expected deductionType = NO_DEDUCTION for all created deductions");
    }
}
