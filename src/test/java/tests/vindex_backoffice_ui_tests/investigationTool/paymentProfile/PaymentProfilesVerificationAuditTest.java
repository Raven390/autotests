package tests.vindex_backoffice_ui_tests.investigationTool.paymentProfile;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntity;
import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntityFactory;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.mt_account.MtAccountObject;
import business_objects.ui.audit_trail.AuditTrailItemV2;
import helpers.data.ClientHelper;
import helpers.data.enums.VerificationStatus;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.io.IOException;
import java.math.BigDecimal;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateStaticCrmTbAccountActive;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateStaticUserByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static helpers.api.VerificationServiceHelper.putProfileStatus;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.VerificationStatus.*;
import static helpers.database.DbHelper.*;
import static helpers.database.OperationsHelper.cleanUserPaymentsDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static utils.Constants.*;
import static utils.Utils.insertCrmAccountsToDb;

@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_WEB)
@Feature("BMS-2161 - Verification history (Audit trail)")
class PaymentProfilesVerificationAuditTest extends TestBaseWeb {

    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static CrmTbUserObject crmTbUser = generateStaticUserByClient(client);
    private static CrmTbAccountObject account = generateStaticCrmTbAccountActive(client);
    private static MtAccountObject mtAccount = generateMtAccountByCrmTbAccount(account);
    private static CrmTbDepositEntity deposit;

    private static final String AUDIT_ITEM_HEADER = "Payment profile verification\nAuto Test";
    private static final String PAYMENT_PROFILE = "USDT TM4JDT9QV4W424ztdB4X9EXekFApJTsLG5";
    private static final String PAYMENT_PROFILE_MASKED = "USDT TM4J***sLG5";
    private static final String AUDIT_ITEM_DETAILS_TEMPLATE = "Set by autotest\n%s\n%s";

    @BeforeAll
    static void setup() {
        deposit = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client);
        deposit.setPaymentProfile(PAYMENT_PROFILE);
        deposit.setPaymentType("Other");
        deposit.setPaymentProfileKey(PAYMENT_PROFILE);
        deposit.setStatusId(5);
        deposit.setPaymentChannel("Cryptocurrency-USDT");
        deposit.setPaymentFamily("Crypto");
        deposit.setPaymentProfileMasked(PAYMENT_PROFILE_MASKED);
        deposit.setAmount(BigDecimal.valueOf(100.0));
        deposit.setAmountUsd(BigDecimal.valueOf(101.12));

        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertCrmAccountsToDb(account);
        insertObjectToDb(MT_ACCOUNT_TABLE_NAME, mtAccount);
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, deposit);
    }

    @AfterAll
    static void teardown() throws Exception {
        deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        cleanUserPaymentsDb(client.getUcid());
    }

    @Test
    @AllureId("1900")
    @DisplayName("Payment profile verification audit test")
    void paymentProfileVerificationAuditTest() throws IOException {
        putProfileStatus(NOT_VERIFIED, deposit.getPaymentProfileKey(), deposit.getPaymentProfileMasked(), client);
        putProfileStatus(VerificationStatus.AWAITING_DOCUMENTS, deposit.getPaymentProfileKey(), deposit.getPaymentProfileMasked(), client);
        putProfileStatus(VERIFIED, deposit.getPaymentProfileKey(), deposit.getPaymentProfileMasked(), client);
        putProfileStatus(VerificationStatus.REJECTED, deposit.getPaymentProfileKey(), deposit.getPaymentProfileMasked(), client);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(client.getUcid());
        auditTrailPage.openAuditTrailTab();
        AuditTrailItemV2 notVerifiedItem = new AuditTrailItemV2(
                AUDIT_ITEM_HEADER, String.format(AUDIT_ITEM_DETAILS_TEMPLATE, PAYMENT_PROFILE_MASKED, NOT_VERIFIED.getDisplayName()));
        AuditTrailItemV2 awaitingItem = new AuditTrailItemV2(
                AUDIT_ITEM_HEADER, String.format(AUDIT_ITEM_DETAILS_TEMPLATE, PAYMENT_PROFILE_MASKED, AWAITING_DOCUMENTS.getDisplayName()));
        AuditTrailItemV2 verifiedItem = new AuditTrailItemV2(
                AUDIT_ITEM_HEADER, String.format(AUDIT_ITEM_DETAILS_TEMPLATE, PAYMENT_PROFILE_MASKED, VERIFIED.getDisplayName()));
        AuditTrailItemV2 rejectedItem = new AuditTrailItemV2(
                AUDIT_ITEM_HEADER, String.format(AUDIT_ITEM_DETAILS_TEMPLATE, PAYMENT_PROFILE_MASKED, REJECTED.getDisplayName()));
        assertThat("Verify payment profile verification audit items", auditTrailPage.getAuditTrailItemsV2(), contains(rejectedItem, verifiedItem, awaitingItem, notVerifiedItem));
        auditTrailPage.clickPaymentProfileByName(PAYMENT_PROFILE_MASKED);
        assertThat("Verify payment profile drawer is opened", paymentsPage.getPaymentProfileDrawerSubheader(), is(String.format("%s%n%s", PAYMENT_PROFILE, REJECTED.getDisplayName())));
    }
}
