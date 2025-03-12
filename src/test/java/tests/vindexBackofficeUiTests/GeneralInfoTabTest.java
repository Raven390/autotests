package tests.vindexBackofficeUiTests;

import businessObjects.db.clickhouse.accountIbRelation.AccountIbRelationObject;
import businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObject;
import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;
import businessObjects.db.clickhouse.mtAccount.MtAccountObject;
import businessObjects.db.clickhouse.s3FactCpaCommissions.s3FactCpaCommissionsObject;
import businessObjects.db.clickhouse.s3FactIbSalesCommissions.S3FactIbSalesCommissionsObject;
import businessObjects.kafka.alerts.RuleAlert;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.Page;
import helpers.data.ClientHelper;
import helpers.data.enums.Brand;
import helpers.data.enums.Regulator;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

import static businessObjects.db.clickhouse.accountIbRelation.AccountIbRelationFactory.generateAccountIbRelationObjectByClient;
import static businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObjectFactory.generateAdditionalCrmTbAccountData;
import static businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObjectFactory.generateStaticCrmTbAccountActive;
import static businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObjectFactory.generateStaticUserByClient;
import static businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObjectFactory.generateUserByClient;
import static businessObjects.db.clickhouse.mtAccount.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static businessObjects.db.clickhouse.s3FactCpaCommissions.s3FactCpaCommissionsFactory.generates3FactCpaCommissionsObject;
import static businessObjects.db.clickhouse.s3FactIbSalesCommissions.S3FactIbSalesCommissionsFactory.generateS3FactIbSalesCommissionsClient;
import static businessObjects.kafka.alerts.RuleAlertFactory.generateRuleAlertByUcid;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.DbHelper.*;
import static helpers.database.DbHelper.deleteObjectFromDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static utils.Constants.*;
import static utils.Utils.*;

public class GeneralInfoTabTest extends TestBaseWeb {

    private static final KafkaHelper kafka = new KafkaHelper();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(getRandomVantageClientAllFields());

    static ClientHelper client = new ClientHelper(232_301, "d555fa11-3e45-44d3-8070-e28eaff997c7", Brand.INFINOX, Regulator.VFSC2, 232_301_001, 42);
    private static final CrmTbUserObject crmTbClient = generateUserByClient(client);
    private static CrmTbAccountObject account1;
    private static MtAccountObject mtAccount1;
    private static AccountIbRelationObject relation;
    private static S3FactIbSalesCommissionsObject commission;

    @BeforeAll
    public static void setup() throws ReflectiveOperationException, SQLException, JsonProcessingException {

        crmTbClient.firstName = "General";
        crmTbClient.lastName = "Testman";
        int referrerId = 232_304;
        int cpaId = 232_305;
        client.setReferrerId(referrerId);
        crmTbClient.rafReferrerId = referrerId;
        client.setCpaId(cpaId);
        crmTbClient.cpaId = cpaId;
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        RuleAlert alert = generateRuleAlertByUcid(crmTbUser.ucid);
        kafka.produceMessage(alert.alertId, objectMapper.writeValueAsString(alert), KAFKA_TOPIC_ALERTS);
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbClient);
        account1 = generateStaticCrmTbAccountActive(client);
        insertObjectToDb(CRM_ACCOUNT_TABLE_NAME, account1);
        mtAccount1 = generateMtAccountByCrmTbAccount(account1);
        insertObjectToDb(MT_ACCOUNT_TABLE_NAME, mtAccount1);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("559")
    @DisplayName("Verify all data is present in general tab")
    public void verifyGeneralInfoTabTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        generalTab.clickGeneralTabButton();
        assertThat("Assert that full name is correct", generalTab.getFullName(), equalTo(String.format("%s %s", crmTbUser.firstName, crmTbUser.lastName)));
        String registrationDateAgoPattern = "^(?:(?:\\d+ year(?:s)? )?(?:\\d+ month(?:s)? )?)?\\d+ day(?:s)?$";
        assertThat("Assert that registration date ago is correct", generalTab.getRegistrationDateAgo(), matchesPattern(registrationDateAgoPattern));
        assertThat("Assert that client id is correct", generalTab.getClientId(), equalTo(String.valueOf(crmTbUser.userId)));
        assertThat("Assert that registration date is correct", generalTab.getRegistrationDate(), equalTo(crmTbUser.registrationDateUtc.split(" ")[0]));
        assertThat("Assert that regulator is correct", generalTab.getRegulator(), equalTo(crmTbUser.regulator));
        assertThat("Assert that gender is correct", generalTab.getGender(), equalTo(crmTbUser.gender));
        assertThat("Assert that date of birth is correct", generalTab.getDateOfBirth(), equalTo(crmTbUser.birthday));
        assertThat("Assert that country is correct", generalTab.getCountry(), equalTo(crmTbUser.country));
        assertThat("Assert that nationality is correct", generalTab.getNationality(), equalTo(crmTbUser.nationality));
        assertThat("Assert that encoded email is correct", generalTab.getEmailAddress(), equalTo("t***4@example.com"));
        assertThat("Assert that encoded phone number is correct", generalTab.getPhoneNumber(), equalTo("+1*********3"));
        assertThat("Assert that 2 factor auth is correct", generalTab.get2FactorAuth(), equalTo("Yes"));
        generalTab.verifyKycSectionIsVisible();
        assertThat("Assert that registration source raf is correct", generalTab.getRegistrationSourceRaf(), equalTo(crmTbUser.rafReferrerId.toString()));
        assertThat("Assert that registration source cpa is correct", generalTab.getRegistrationSourceCpa(), equalTo(crmTbUser.cpaId.toString()));
        generalTab.clickShowHiddenDataButton();
        assertThat("Assert that email is correct", generalTab.getEmailAddress(), equalTo("test14@example.com"));
        assertThat("Assert that phone number is correct", generalTab.getPhoneNumber(), equalTo("+1810347493"));
    }

    @Test
    @AllureId("1044")
    @Feature("BMS-827 Modify displaying CPA/IB/referrer in general")
    @DisplayName("General Tab. User can see IB account in clients general info info more than one account")
    public void IBtest() {
        ClientHelper referral = new ClientHelper(232_303, "d555fa11-3e45-44d3-8070-e28eaff997c7", Brand.INFINOX, Regulator.VFSC2, 232_303_001, 232_303_002, 42);
        CrmTbUserObject crmTbReferral = generateStaticUserByClient(referral);

        crmTbReferral.firstName = "Relation";
        crmTbReferral.lastName = "Clientson";
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbReferral);
        CrmTbAccountObject refAccount1 = generateStaticCrmTbAccountActive(referral);
        CrmTbAccountObject refAccount2 = generateAdditionalCrmTbAccountData(referral);
        insertObjectToDb(CRM_ACCOUNT_TABLE_NAME, refAccount1);
        insertObjectToDb(CRM_ACCOUNT_TABLE_NAME, refAccount2);
        MtAccountObject refMtAccount1 = generateMtAccountByCrmTbAccount(refAccount1);
        MtAccountObject refMtAccount2 = generateMtAccountByCrmTbAccount(refAccount2);
        insertObjectToDb(MT_ACCOUNT_TABLE_NAME, refMtAccount1);
        insertObjectToDb(MT_ACCOUNT_TABLE_NAME, refMtAccount2);


        deleteObjectFromDb(ACCOUNT_IB_RELATION_TABLE_NAME, "ucid ='" + client.getUcid() + "'");
        deleteObjectFromDb(S3_FACT_IB_SALES_COMMISSIONS, "ucid ='" + client.getUcid() + "'");
        relation = generateAccountIbRelationObjectByClient(client);
        relation.setDirectIbRebateAccount(referral.getTradingAccount());
        insertObjectToDb(ACCOUNT_IB_RELATION_TABLE_NAME, relation);
        commission = generateS3FactIbSalesCommissionsClient(client);
        commission.setIbRebateAccount(relation.getDirectIbRebateAccount());
        commission.setSalesCommission(getRandomRoundedDouble(0.00, 5_000_000.00));
        commission.setIbCommission(getRandomRoundedDouble(0.00, 5_000_000.00));
        insertObjectToDb(S3_FACT_IB_SALES_COMMISSIONS, commission);
        AccountIbRelationObject relation2 = generateAccountIbRelationObjectByClient(client);
        relation2.setDirectIbRebateAccount(referral.getTradingAccount2());
        insertObjectToDb(ACCOUNT_IB_RELATION_TABLE_NAME, relation2);
        S3FactIbSalesCommissionsObject commission2 = generateS3FactIbSalesCommissionsClient(client);
        commission2.setIbRebateAccount(relation2.getDirectIbRebateAccount());
        commission2.setSalesCommission(getRandomRoundedDouble(0.00, 5_000_000.00));
        commission2.setIbCommission(getRandomRoundedDouble(0.00, 5_000_000.00));
        insertObjectToDb(S3_FACT_IB_SALES_COMMISSIONS, commission2);

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigate(client.getUcid());
        generalTab.checkIbRebates(referral.getTradingAccount(), (commission.getSalesCommission() + commission.getIbCommission()));
        generalTab.checkIbRebates(referral.getTradingAccount2(), (commission2.getSalesCommission() + commission2.getIbCommission()));
    }

    @Test
    @AllureId("1045")
    @Feature("BMS-827 Modify displaying CPA/IB/referrer in general")
    @DisplayName("General Tab. System displays different IB connections between one pair of users as one")
    public void ibSeparateTest() {
        ClientHelper referral = new ClientHelper(232_303, "d555fa11-3e45-44d3-8070-e28eaff997c7", Brand.INFINOX, Regulator.VFSC2, 232_303_001, 232_303_002, 42);
        CrmTbUserObject crmTbReferral = generateStaticUserByClient(referral);

        crmTbReferral.firstName = "Relation";
        crmTbReferral.lastName = "Clientson";
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbReferral);
        CrmTbAccountObject refAccount1 = generateStaticCrmTbAccountActive(referral);
        insertObjectToDb(CRM_ACCOUNT_TABLE_NAME, refAccount1);
        MtAccountObject refMtAccount1 = generateMtAccountByCrmTbAccount(refAccount1);
        insertObjectToDb(MT_ACCOUNT_TABLE_NAME, refMtAccount1);


        deleteObjectFromDb(ACCOUNT_IB_RELATION_TABLE_NAME, "ucid ='" + client.getUcid() + "'");
        deleteObjectFromDb(S3_FACT_IB_SALES_COMMISSIONS, "ucid ='" + client.getUcid() + "'");
        relation = generateAccountIbRelationObjectByClient(client);
        relation.setDirectIbRebateAccount(referral.getTradingAccount());
        insertObjectToDb(ACCOUNT_IB_RELATION_TABLE_NAME, relation);
        commission = generateS3FactIbSalesCommissionsClient(client);
        commission.setIbRebateAccount(relation.getDirectIbRebateAccount());
        commission.setSalesCommission(getRandomRoundedDouble(0.00, 5_000_000.00));
        commission.setIbCommission(getRandomRoundedDouble(0.00, 5_000_000.00));
        S3FactIbSalesCommissionsObject commission2 = generateS3FactIbSalesCommissionsClient(client);
        commission2.setIbRebateAccount(relation.getDirectIbRebateAccount());
        commission2.setSalesCommission(getRandomRoundedDouble(0.00, 5_000_000.00));
        commission2.setIbCommission(getRandomRoundedDouble(0.00, 5_000_000.00));
        commission2.setDlInsertTs("2025-02-23 09:06:22");
        commission2.setDlUpdateTs("2025-02-23 09:06:21");
        insertObjectsToDb(S3_FACT_IB_SALES_COMMISSIONS, List.of(commission, commission2));
        AccountIbRelationObject relation2 = generateAccountIbRelationObjectByClient(client);
        relation2.setDirectIbRebateAccount(referral.getTradingAccount());
        relation2.setCreateTimeUtc("2025-02-21 09:06:22");
        relation2.setRecordEffectiveStartDate("2021-02-21");
        relation2.setRecordEffectiveEndDate("2022-02-21");
        insertObjectToDb(ACCOUNT_IB_RELATION_TABLE_NAME, relation2);

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigate(client.getUcid());
        generalTab.checkIbRebates(referral.getTradingAccount(), (commission.getSalesCommission() + commission.getIbCommission()) + (commission2.getSalesCommission() + commission2.getIbCommission()));
    }

    @Test
    @AllureId("1046")
    @Feature("BMS-827 Modify displaying CPA/IB/referrer in general")
    @DisplayName("General Tab. User can't see IB account in clients general info if there is no data in DB")
    public void IbNotDisplayedTest() {
        deleteObjectFromDb(ACCOUNT_IB_RELATION_TABLE_NAME, "ucid ='" + client.getUcid() + "'");
        deleteObjectFromDb(S3_FACT_IB_SALES_COMMISSIONS, "ucid ='" + client.getUcid() + "'");
        commission = generateS3FactIbSalesCommissionsClient(client);
        commission.setIbRebateAccount(242_424_241);
        commission.setSalesCommission(getRandomRoundedDouble(0.00, 5_000_000.00));
        commission.setIbCommission(getRandomRoundedDouble(0.00, 5_000_000.00));
        S3FactIbSalesCommissionsObject commission2 = generateS3FactIbSalesCommissionsClient(client);
        commission2.setIbRebateAccount(232_323_231);
        commission2.setSalesCommission(getRandomRoundedDouble(0.00, 5_000_000.00));
        commission2.setIbCommission(getRandomRoundedDouble(0.00, 5_000_000.00));
        commission2.setDlInsertTs("2025-02-23 09:06:22");
        commission2.setDlUpdateTs("2025-02-23 09:06:21");
        insertObjectsToDb(S3_FACT_IB_SALES_COMMISSIONS, List.of(commission, commission2));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigate(client.getUcid());
        generalTab.IbSectionNotDisplayed();
    }

    @Test
    @AllureId("1074")
    @Feature("BMS-827 Modify displaying CPA/IB/referrer in general")
    @DisplayName("General Tab. User can't see IB account in clients general info if there only rebate data exist without connection record")
    public void IbNotDisplayedOnlyRebateExistTest() {
        deleteObjectFromDb(ACCOUNT_IB_RELATION_TABLE_NAME, "ucid ='" + client.getUcid() + "'");
        deleteObjectFromDb(S3_FACT_IB_SALES_COMMISSIONS, "ucid ='" + client.getUcid() + "'");

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigate(client.getUcid());
        generalTab.IbSectionNotDisplayed();
    }

    @Test
    @AllureId("1047")
    @Feature("BMS-827 Modify displaying CPA/IB/referrer in general")
    @DisplayName("General Tab. User can see referral client in clients general info")
    public void ReferralDisplayedTest() {

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigate(client.getUcid());
        generalTab.checkReferralDisplayed(client.getReferrerId());
    }

    @Test
    @AllureId("1048")
    @Feature("BMS-827 Modify displaying CPA/IB/referrer in general")
    @DisplayName("General Tab. User can't see referral client in clients general info")
    public void ReferralNotDisplayedTest() {
        CrmTbUserObject noRef = generateUserByClient(client);
        noRef.rafReferrerId = null;
        insertObjectToDb(CRM_USER_TABLE_NAME, noRef);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigate(client.getUcid());
        generalTab.ReferralSectionNotDisplayed();
    }

    @Test
    @AllureId("1049")
    @Feature("BMS-827 Modify displaying CPA/IB/referrer in general")
    @DisplayName("General Tab. User can see referral client in clients general info")
    public void ReferralLinkTest() {

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigate(client.getUcid());
        String refferalUcid = client.getBrand().toLowerCase(Locale.ROOT) + "-" + client.getReferrerId();
        Page refPage = context.waitForPage(() -> {
            generalTab.clickReferrerLink();
        });
        Allure.step("check that url contains expected referral client url");
        assertTrue(refPage.url().contains(refferalUcid));
    }

    @Test
    @AllureId("1050")
    @Feature("BMS-827 Modify displaying CPA/IB/referrer in general")
    @DisplayName("General Tab. User can see CPA client in clients general info")
    public void CpaDisplayedTest() {
        CrmTbUserObject noCpa = generateUserByClient(client);
        noCpa.cpaId = null;
        insertObjectToDb(CRM_USER_TABLE_NAME, noCpa);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigate(client.getUcid());
        generalTab.checkCpaDisplayed(client.getCpaId());
    }

    @Test
    @AllureId("1051")
    @Feature("BMS-827 Modify displaying CPA/IB/referrer in general")
    @DisplayName("General Tab. User can see CPA commissions values in clients general info")
    public void CpaAmountTest() {
        deleteObjectFromDb(S3_FACT_CPA_COMMISSIONS, "ucid ='" + client.getUcid() + "'");
        s3FactCpaCommissionsObject commission = generates3FactCpaCommissionsObject(client);
        insertObjectToDb(S3_FACT_CPA_COMMISSIONS, commission);
        s3FactCpaCommissionsObject commission2 = generates3FactCpaCommissionsObject(client);
        commission2.setDate("2023-02-24");
        insertObjectToDb(S3_FACT_CPA_COMMISSIONS, commission2);

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigate(client.getUcid());
        generalTab.checkCpaRebates(client.getCpaId(), commission.getCommission() + commission2.getCommission());
        generalTab.checkCpaDate(client.getCpaId(), commission.getDate());
    }

    @Test
    @AllureId("1052")
    @Feature("BMS-827 Modify displaying CPA/IB/referrer in general")
    @DisplayName("General Tab. User can't see CPA client in clients general info if there is no CPA data in clients DB record")
    public void CpaNotDisplayedTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalTab.navigate(client.getUcid());
        generalTab.CpaSectionNotDisplayed();
    }

    @AfterAll
    public static void teardown() throws SQLException {
        deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", crmTbUser.ucid));
        closeAlert(crmTbUser.ucid);
    }
}
