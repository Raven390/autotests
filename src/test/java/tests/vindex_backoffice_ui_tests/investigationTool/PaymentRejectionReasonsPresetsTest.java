package tests.vindex_backoffice_ui_tests.investigationTool;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.kafka.alerts.RuleAlertFactory.*;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.VerificationDocument.*;
import static helpers.database.ArHelper.deleteUserFromAbuseRegistry;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.CleanTableHelper.cleanPaymentGateData;
import static helpers.database.DbHelper.*;
import static helpers.database.DbName.POSTGRES;
import static helpers.database.PaymentGateHelper.generatePaymentWithdrawalPaymentGateData;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.getRandomIntPositive;
import static utils.Utils.insertCrmAccountsToDb;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalEntity;
import business_objects.db.clickhouse.mt_account.MtAccountObject;
import business_objects.kafka.alerts.PaymentAlertMessageV2;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.data.ClientHelper;
import helpers.data.PaymentGateData;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import java.util.List;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

@Feature("BMS-3019 Provide selectable values for rejection reason dynamic fields")
@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_WEB)
class PaymentRejectionReasonsPresetsTest extends TestBaseWeb {
    private static final KafkaHelper kafka = new KafkaHelper();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(client);
    private static final CrmTbAccountObject crmTbAccount = generateCrmTbAccountDataForUi(client);
    private static final MtAccountObject mtAccount = generateMtAccountByCrmTbAccount(crmTbAccount);
    private static PaymentGateData pgsData;
    private static CrmTbWithdrawalEntity withdrawalCard = generateCrmTbWithdrawalEntityByClient(client);
    private static CrmTbWithdrawalEntity withdrawalLBT = generateCrmTbWithdrawalEntityByClient(client);
    private static CrmTbWithdrawalEntity withdrawalIBT = generateCrmTbWithdrawalEntityByClient(client);
    private static CrmTbWithdrawalEntity withdrawalVWallet = generateCrmTbWithdrawalEntityByClient(client);
    private static CrmTbWithdrawalEntity withdrawalCrypto = generateCrmTbWithdrawalEntityByClient(client);
    private static final String UCID_WHERE = String.format("ucid = '%s'", client.getUcid());

    @BeforeAll
    static void setup() throws JsonProcessingException {
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertCrmAccountsToDb(crmTbAccount);
        insertObjectToDb(MT_ACCOUNT_TABLE_NAME, mtAccount);

        pgsData = generatePaymentWithdrawalPaymentGateData(client);
        insertObjectToDb(POSTGRES, PAYMENT_EVENT_TABLE_NAME, pgsData.getPaymentEvent());
        insertObjectToDb(POSTGRES, PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE, pgsData.getPaymentDetails());
        insertObjectToDb(POSTGRES, PAYMENT_GATEWAY_PAYMENT_DECISIONS_TABLE, pgsData.getPaymentDecisions());

        // Insert withdrawals with different payment profiles
        String cardProfile = "Card " + getRandomIntPositive();
        withdrawalCard.setPaymentFamily("Card");
        withdrawalCard.setPaymentProfile(cardProfile);
        withdrawalCard.setPaymentProfileKey(cardProfile);
        withdrawalCard.setPaymentProfileMasked(cardProfile);
        String ibtProfile = "Mexico bank transfer " + getRandomIntPositive();
        withdrawalIBT.setPaymentFamily("IBT");
        withdrawalIBT.setPaymentProfile(ibtProfile);
        withdrawalIBT.setPaymentProfileKey(ibtProfile);
        withdrawalIBT.setPaymentProfileMasked(ibtProfile);
        String lbtProfile = "Nigeria bank transfer " + getRandomIntPositive();
        withdrawalLBT.setPaymentFamily("LBT");
        withdrawalLBT.setPaymentProfile(lbtProfile);
        withdrawalLBT.setPaymentProfileKey(lbtProfile);
        withdrawalLBT.setPaymentProfileMasked(lbtProfile);
        String vWalletProfile = "V-Wallet " + getRandomIntPositive();
        withdrawalVWallet.setPaymentFamily("V-Wallet");
        withdrawalVWallet.setPaymentProfile(vWalletProfile);
        withdrawalVWallet.setPaymentProfileKey(vWalletProfile);
        withdrawalVWallet.setPaymentProfileMasked(vWalletProfile);
        String cryptoProfile = "Crypto " + getRandomIntPositive();
        withdrawalCrypto.setPaymentFamily("Crypto");
        withdrawalCrypto.setPaymentProfile(cryptoProfile);
        withdrawalCrypto.setPaymentProfileKey(cryptoProfile);
        withdrawalCrypto.setPaymentProfileMasked(cryptoProfile);

        insertObjectsToDb(
                CLICKHOUSE_CRM_TB_WITHDRAWAL,
                List.of(withdrawalCard, withdrawalLBT, withdrawalIBT, withdrawalVWallet, withdrawalCrypto));

        objectMapper.findAndRegisterModules();
        PaymentAlertMessageV2 alert = generatePaymentAlertByUcidByTrigger(client.getUcid(), "withdrawal");
        alert.setPaymentEventId(pgsData.getPaymentEvent().getPaymentId().toString());
        kafka.produceMessage(alert.getId().toString(), objectMapper.writeValueAsString(alert), KAFKA_TOPIC_ALERTS);
    }

    @AfterAll
    static void teardown() throws Exception {
        deleteObjectFromDb(CRM_USER_TABLE_NAME, UCID_WHERE);
        deleteObjectFromDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, UCID_WHERE);
        cleanPaymentGateData(
                client.getUcid(),
                client.getUserId(),
                pgsData.getPaymentEvent().getPaymentId().toString());
        deleteUserFromAbuseRegistry(client.getUcid());
        closeAlert(crmTbUser.ucid);
    }

    @Test
    @AllureId("2116")
    @DisplayName("Verify rejection reason presets for payment investigation")
    void rejectionReasonPresetsTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToMain();
        investigationPage.waitForPageToLoad();
        investigationPage.clickSelectPaymentInvestigationType();
        investigationPage.waitForPageToLoad();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        resolvePage.openResolveSuspicious();
        resolvePage.clickWithdrawalReject("Ownership proof required (Crypto wallet)");
        resolvePage.clickRejectionReasonCryptoAddress();
        assertThat(
                "Verify list of crypto address",
                resolvePage.getPresetOptionsList(),
                containsInAnyOrder(
                        withdrawalVWallet.getPaymentProfileMasked(), withdrawalCrypto.getPaymentProfileMasked()));
        resolvePage.clickPresetOption(withdrawalCrypto.getPaymentProfileMasked());
        resolvePage.waitForPopupToClose();
        assertThat(
                "Verify text of filled in rejection reason",
                resolvePage.getRejectionReasonText(),
                is(String.format(
                        "Please provide us proof of ownership for your crypto wallet %s .",
                        withdrawalCrypto.getPaymentProfileMasked())));
        resolvePage.clickWithdrawalReject("Ownership proof required");
        resolvePage.clickRejectionReasonPaymentProfile();
        assertThat(
                "Verify list of payment profiles",
                resolvePage.getPresetOptionsList(),
                containsInAnyOrder(
                        withdrawalCard.getPaymentProfileMasked(),
                        withdrawalLBT.getPaymentProfileMasked(),
                        withdrawalIBT.getPaymentProfileMasked(),
                        withdrawalVWallet.getPaymentProfileMasked(),
                        withdrawalCrypto.getPaymentProfileMasked()));
        resolvePage.clickPresetOption(withdrawalIBT.getPaymentProfileMasked());
        resolvePage.waitForPopupToClose();
        resolvePage.clickRejectionReasonVerificationDocuments();
        assertThat(
                "Verify list of verification documents",
                resolvePage.getPresetOptionsList(),
                containsInAnyOrder(
                        CARD_STATEMENT.getDisplayName(),
                        CARD_PHOTO.getDisplayName(),
                        WALLET_SCREENSHOT.getDisplayName(),
                        BANK_STATEMENT.getDisplayName()));
        resolvePage.clickPresetOption(CARD_STATEMENT.getDisplayName());
        resolvePage.clickPresetOption(CARD_PHOTO.getDisplayName());
        resolvePage.clickPresetOption(WALLET_SCREENSHOT.getDisplayName());
        resolvePage.clickPresetOption(BANK_STATEMENT.getDisplayName());
        resolvePage.clickApplyButton();
        resolvePage.waitForPopupToClose();
        assertThat(
                "Verify text of filled in rejection reason",
                resolvePage.getRejectionReasonText(),
                is(String.format(
                        "Please provide proof of ownership for your receiving account %s by uploading the relevant document: 1) Card statement: The statement clearly shows cardholder's name and the last 4 digits of the card. 2) Photo of the card: The photo clearly shows cardholder's name, first 6 & last 4 digits, while the CVV code is fully covered. 3) Screenshot wallet: Clearly shows wallet number/ wallet address and account holder's name. 4) Bank statement: The bank account number and the account holder's name are clearly shown .",
                        withdrawalIBT.getPaymentProfileMasked().replaceAll(".*\\s", "Bank account "))));
        String recommendedMethod = "testRecommendedMethod";
        resolvePage.clickWithdrawalReject("Use recommended method", recommendedMethod);
        assertThat(
                "Verify text of filled in rejection reason",
                resolvePage.getRejectionReasonText(),
                is(String.format("Please use the %n%s%n for future withdrawals.", recommendedMethod)));
    }
}
