package tests.vindex_backoffice_ui_tests.abuseRegistry.fraudsters;

import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static helpers.api.AbuseRegistryHelper.addFraudForClient;
import static helpers.api.AbuseRegistryHelper.setClientStatus;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.FraudSubtype.INTERNAL;
import static helpers.data.enums.FraudType.HEDGING;
import static helpers.data.enums.FraudTypeStatus.*;
import static helpers.database.ArHelper.deleteUserFromAbuseRegistry;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static utils.Constants.*;

import business_objects.db.abuse_registry_db.Abuser;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.api.MitigationHelper;
import helpers.data.ClientHelper;
import helpers.data.enums.FraudTypeStatus;
import helpers.database.DbName;
import io.qameta.allure.Feature;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

@Feature("BMS-1549 Auto changing holding")
@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_WEB)
@Tag(ABUSE_REGISTRY)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AutoCleanPotentialStatusTest {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ClientHelper clientWithFraud = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmTbUserWithFraud = generateUserByClient(clientWithFraud);

    private static final ClientHelper clientWithoutFraud = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmTbUserWithoutFraud = generateUserByClient(clientWithoutFraud);

    private static final ClientHelper clientWithCleanedFraud = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmTbUserWithCleanedFraud = generateUserByClient(clientWithCleanedFraud);

    @BeforeAll
    static void setup() throws Exception {
        insertObjectsToDb(
                CRM_USER_TABLE_NAME, List.of(crmTbUserWithFraud, crmTbUserWithoutFraud, crmTbUserWithCleanedFraud));
        addFraudForClient(clientWithFraud, HEDGING, INTERNAL, CONFIRMED, List.of("EURUSD", "GBPUSD"));
        addFraudForClient(clientWithCleanedFraud, HEDGING, INTERNAL, CLEANED, List.of("EURUSD", "GBPUSD"));

        setClientStatus(clientWithoutFraud, FraudTypeStatus.POTENTIAL);
        setClientStatus(clientWithFraud, FraudTypeStatus.POTENTIAL);
        setClientStatus(clientWithCleanedFraud, FraudTypeStatus.POTENTIAL);
        MitigationHelper.setGeneralRestrictionApi(clientWithFraud.getUcid(), "02");
    }

    @AfterAll
    static void teardown() throws Exception {
        deleteUserFromAbuseRegistry(clientWithFraud.getUcid());
        deleteUserFromAbuseRegistry(clientWithCleanedFraud.getUcid());
        deleteUserFromAbuseRegistry(clientWithoutFraud.getUcid());
    }

    @Test
    void shouldCleanUser_whenClientStatusIsPotentialAndNoFraudOrRestriction() throws Exception {
        Thread.sleep(Duration.ofSeconds(40).toMillis());
        String whereUcid = String.format(
                "ucid IN ('%s', '%s', '%s')",
                crmTbUserWithFraud.ucid, crmTbUserWithoutFraud.ucid, crmTbUserWithCleanedFraud.ucid);
        List<Abuser> abusers = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_TABLE_NAME, whereUcid, Abuser.class);

        assertThat(abusers, Matchers.not(Matchers.empty()));
        assertThat(abusers, Matchers.hasSize(3));

        Optional<Abuser> cleanedAbuserOptional = abusers.stream()
                .filter(abuser -> abuser.getStatus().equals(CLEANED.getStatus()))
                .findFirst();

        assertThat(cleanedAbuserOptional.isPresent(), Matchers.is(true));

        Abuser cleanedAbuser = cleanedAbuserOptional.get();
        assertThat(cleanedAbuser.getStatus(), Matchers.is(CLEANED.getStatus()));

        List<Abuser> potentialList = abusers.stream()
                .filter(abuser -> !abuser.getStatus().equals(CLEANED.getStatus()))
                .toList();

        assertThat(potentialList, Matchers.hasSize(2));
    }
}
