package tests.vindex_backoffice_ui_tests.abuseRegistry.fraudsters;

import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateStaticUserByClient;
import static helpers.api.AbuseRegistryHelper.addFraudsForClient;
import static helpers.data.enums.FraudType.CPA_ABUSE;
import static helpers.data.enums.FraudType.HEDGING;
import static helpers.database.ArHelper.deleteUserFromAbuseRegistry;
import static helpers.database.AuHelper.cleanClientAudit;
import static helpers.database.BoHelper.deleteUserBO;
import static helpers.database.DbHelper.getObjectsFromDB;
import static helpers.database.DbHelper.insertObjectsToDb;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static page_objects.backoffice_pages.investigationTool.RestrictionPage.cleanUserRestriction;
import static utils.Constants.*;
import static utils.Utils.getCurrentTimestampSeconds;

import business_objects.db.abuse_registry_db.AbuserFraudType;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import helpers.data.ClientHelper;
import helpers.data.enums.*;
import helpers.database.DbName;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import java.util.List;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

@Feature("BMS-1475 Mass delete. Limited access")
class MassDeleteTest extends TestBaseWeb {

    private static final ClientHelper client1;
    private static final ClientHelper client2;
    private static final ClientHelper client3;

    static {
        client1 = ClientHelper.builder()
                .userId(313_101)
                .uid("063cde3b-ea9d-48b5-8e2c-99f3d5f67999")
                .brand(Brand.VANTAGE)
                .regulator(Regulator.VFSC2)
                .tradingAccount(313_101_001)
                .serverId(42)
                .build();

        client2 = ClientHelper.builder()
                .userId(313_102)
                .uid("063cde3b-ea9d-48b5-8e2c-99f3d5f67999")
                .brand(Brand.VANTAGE)
                .regulator(Regulator.VFSC2)
                .tradingAccount(313_102_001)
                .serverId(42)
                .build();

        client3 = ClientHelper.builder()
                .userId(313_103)
                .uid("063cde3b-ea9d-48b5-8e2c-99f3d5f67999")
                .brand(Brand.VANTAGE)
                .regulator(Regulator.VFSC2)
                .tradingAccount(313_103_001)
                .serverId(42)
                .build();
    }

    @BeforeAll
    public static void setup() throws Exception {
        CrmTbUserObject crmClient1 = generateStaticUserByClient(client1);
        CrmTbUserObject crmClient2 = generateStaticUserByClient(client2);
        CrmTbUserObject crmClient3 = generateStaticUserByClient(client3);

        insertObjectsToDb(CRM_USER_TABLE_NAME, List.of(crmClient1, crmClient2, crmClient3));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @Tag(ABUSE_REGISTRY)
    @AllureId("1328")
    @DisplayName("Abuse registry mass delete full flow simple test")
    void abuseRegistryMassDeleteSimpleFullFlowTest() throws Exception {

        cleanClientAudit(client1.getUcid(), client2.getUcid());
        deleteUserBO(client1.getUcid());
        deleteUserBO(client2.getUcid());
        deleteUserBO(client3.getUcid());
        cleanUserRestriction(client1.getUcid());
        cleanUserRestriction(client2.getUcid());
        cleanUserRestriction(client3.getUcid());
        deleteUserFromAbuseRegistry(client1.getUcid(), client2.getUcid(), client3.getUcid());

        addFraudsForClient(client1, List.of(HEDGING, CPA_ABUSE), FraudTypeStatus.POTENTIAL);
        addFraudsForClient(client2, List.of(HEDGING), FraudTypeStatus.POTENTIAL);
        addFraudsForClient(client3, List.of(HEDGING), FraudTypeStatus.POTENTIAL);

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        fraudstersPage.navigateAbuseRegistryFraudsters();
        fraudstersPage.openRemoveDrawer();
        fraudstersPage.selectBrandToUpload(Brand.VANTAGE.getDisplayName());
        fraudstersPage.typeClientsID(
                client1.getUserId().toString(),
                client2.getUserId().toString(),
                client3.getUserId().toString());
        fraudstersPage.clickAddFraudButton();
        FraudTypeOld fraudTypeOld = FraudTypeOld.HEDGING;
        fraudstersPage.addSelectedFraudDelete(fraudTypeOld.getDisplayName());
        String commentary = "test" + getCurrentTimestampSeconds();
        fraudstersPage.fillCommentary(commentary);
        fraudstersPage.clickDeleteUpload();
        fraudstersPage.verifySuccessMessageDelete();

        page.waitForTimeout(1000);

        List<AbuserFraudType> fraudsFirst = getObjectsFromDB(
                DbName.POSTGRES, "ar.abuser_fraud_type", "ucid='" + client1.getUcid() + "'", AbuserFraudType.class);
        Allure.step("Assert that there two records in ar.abuser_fraud_type for the first client");
        assertEquals(2, fraudsFirst.size());

        Allure.step("Find among frauds of firs user fraud with time CPA Abuse (that that we not deleted)");

        AbuserFraudType fraudFirst = fraudsFirst.stream()
                .filter(fraud -> fraud.getFraudTypeCode().equals(CPA_ABUSE.getCode()))
                .findFirst()
                .orElse(null);
        Assertions.assertNotNull(fraudFirst);

        Allure.step("Assert that record in ar.abuser_fraud_type have right status");
        assertEquals("POTENTIAL", fraudFirst.getStatus());

        Allure.step("Find among frauds of firs user fraud with time CPA Abuse (that that we not deleted)");

        AbuserFraudType fraudSecond = fraudsFirst.stream()
                .filter(fraud -> fraud.getFraudTypeCode().equals(HEDGING.getCode()))
                .findFirst()
                .orElse(null);
        Assertions.assertNotNull(fraudSecond);

        Allure.step("Assert that record in ar.abuser_fraud_type have right status");
        assertEquals("CLEANED", fraudSecond.getStatus());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @Tag(ABUSE_REGISTRY)
    @AllureId("1377")
    @DisplayName("Abuse registry mass delete can delete only potential")
    void abuseRegistryMassCanDeleteOnlyPotential() throws Exception {

        cleanClientAudit(client1.getUcid(), client2.getUcid());
        deleteUserBO(client1.getUcid());
        cleanUserRestriction(client1.getUcid());
        deleteUserFromAbuseRegistry(client1.getUcid());

        addFraudsForClient(client1, List.of(HEDGING), FraudTypeStatus.POTENTIAL);
        addFraudsForClient(client1, List.of(CPA_ABUSE), FraudTypeStatus.CONFIRMED);

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        fraudstersPage.navigateAbuseRegistryFraudsters();
        fraudstersPage.openRemoveDrawer();
        fraudstersPage.selectBrandToUpload(Brand.VANTAGE.getDisplayName());
        fraudstersPage.typeClientsID(client1.getUserId().toString());
        fraudstersPage.clickAddFraudButton();
        FraudTypeOld fraudTypeOld1 = FraudTypeOld.HEDGING;
        FraudTypeOld fraudTypeOld2 = FraudTypeOld.CPA_ABUSE;
        fraudstersPage.addSelectedFraudDelete(fraudTypeOld1.getDisplayName());
        fraudstersPage.clickAddFraudButton();
        fraudstersPage.addSelectedFraudDelete(fraudTypeOld2.getDisplayName());
        String commentary = "test" + getCurrentTimestampSeconds();
        fraudstersPage.fillCommentary(commentary);
        fraudstersPage.clickDeleteUpload();
        fraudstersPage.verifySuccessMessageDelete();

        page.waitForTimeout(1000);

        List<AbuserFraudType> fraudsFirst = getObjectsFromDB(
                DbName.POSTGRES, "ar.abuser_fraud_type", "ucid='" + client1.getUcid() + "'", AbuserFraudType.class);
        Allure.step("Assert that there two records in ar.abuser_fraud_type for the first client");
        assertEquals(2, fraudsFirst.size());

        Allure.step("Find among frauds of firs user fraud with time CPA Abuse (that that we not deleted)");

        AbuserFraudType fraudFirst = fraudsFirst.stream()
                .filter(fraud -> fraud.getFraudTypeCode().equals(CPA_ABUSE.getCode()))
                .findFirst()
                .orElse(null);
        Assertions.assertNotNull(fraudFirst);

        Allure.step("Assert that record in ar.abuser_fraud_type have right status");
        assertEquals("CONFIRMED", fraudFirst.getStatus());

        Allure.step("Find among frauds of firs user fraud with time CPA Abuse (that that we not deleted)");

        AbuserFraudType fraudSecond = fraudsFirst.stream()
                .filter(fraud -> fraud.getFraudTypeCode().equals(HEDGING.getCode()))
                .findFirst()
                .orElse(null);
        Assertions.assertNotNull(fraudSecond);

        Allure.step("Assert that record in ar.abuser_fraud_type have right status");
        assertEquals("CLEANED", fraudSecond.getStatus());
    }
}
