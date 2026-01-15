package tests.click_house_api_service_tests;

import static business_objects.api.clickhouse_api_service.get_abuse_types.GetAbuseTypesRequest.getAbuseTypes;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static helpers.api.AbuseRegistryHelper.addFraudsForClient;
import static helpers.data.ClientFactory.getRandomVantageClient;
import static helpers.database.ArHelper.deleteUserFromAbuseRegistry;
import static helpers.database.CleanTableHelper.cleanFraudTypeTableByClient;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.getCurrentTimestampDbFormat;

import business_objects.api.clickhouse_api_service.ClickhouseApiErrorResponse;
import business_objects.api.clickhouse_api_service.get_abuse_types.GetAbuseTypesResponse;
import business_objects.db.clickhouse.client_fraud_types.ClientFraudTypes;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import helpers.data.ClientHelper;
import helpers.data.enums.FraudTypeOld;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import java.io.IOException;
import java.util.List;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;

@Feature(FEATURE_CLICKHOUSE_API_SERVICE)
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_ABUSE_TYPES)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
class GetAbuseTypesTests extends TestBaseApi {

    private static ClientFraudTypes fraud1;
    private static ClientFraudTypes fraud2;
    private static ClientFraudTypes fraud3;
    private static ClientHelper client = getRandomVantageClient();
    private static ClientHelper client2 = getRandomVantageClient();

    @BeforeAll
    static void setup() throws IOException {
        CrmTbUserObject user = generateUserByClient(client);
        CrmTbUserObject user2 = generateUserByClient(client2);

        insertObjectToDb(CRM_USER_TABLE_NAME, user);
        insertObjectToDb(CRM_USER_TABLE_NAME, user2);

        fraud1 = new ClientFraudTypes(
                client.getUcid(),
                FraudTypeOld.HEDGING.getKey(),
                FRAUD_TYPE_SOURCE_VINDEX,
                0,
                getCurrentTimestampDbFormat());
        fraud2 = new ClientFraudTypes(
                client.getUcid(),
                FraudTypeOld.CPA_ABUSE.getKey(),
                FRAUD_TYPE_SOURCE_VINDEX,
                0,
                getCurrentTimestampDbFormat());
        fraud3 = new ClientFraudTypes(
                client2.getUcid(),
                FraudTypeOld.LOSS_VOUCHER_ABUSE.getKey(),
                FRAUD_TYPE_SOURCE_VINDEX,
                0,
                getCurrentTimestampDbFormat());
        addFraudsForClient(fraud1, fraud2, fraud3);
    }

    @AfterAll
    static void delete() throws Exception {
        cleanFraudTypeTableByClient(fraud1.getUcid(), fraud2.getUcid(), fraud3.getUcid());
        deleteUserFromAbuseRegistry(client.getUcid());
        deleteObjectFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteUserFromAbuseRegistry(client2.getUcid());
        deleteObjectFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", client2.getUcid()));
    }

    @Test
    @DisplayName("Clickhouse Api. Get abuse types single client success (200)")
    @AllureId("429")
    void getAbuseTypesSingleClientTest() throws IOException {

        Response response = getAbuseTypes(List.of(fraud1.getUcid()));

        assertThat(response.body(), is(notNullValue()));
        GetAbuseTypesResponse[] mappedResponse =
                objectMapper.readValue(response.body().string(), GetAbuseTypesResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert array size", mappedResponse.length, is(1));
        assertThat("Assert clientId", mappedResponse[0].getClientId(), is(fraud1.getUcid()));
        assertThat("Assert fraudType length", mappedResponse[0].getFraudType().length, is(2));
        assertThat("Assert fraudType", mappedResponse[0].getFraudType(), hasItemInArray(fraud1.getFraudTypeCode()));
        assertThat("Assert fraudType", mappedResponse[0].getFraudType(), hasItemInArray(fraud2.getFraudTypeCode()));
    }

    @Test
    @DisplayName("Clickhouse Api. Get abuse types multiple clients success (200)")
    @AllureId("430")
    void getAbuseTypesMultipleClientsTest() throws IOException {

        GetAbuseTypesResponse abuseTypesResponse1 = new GetAbuseTypesResponse(
                fraud1.getUcid(), new String[] {fraud2.getFraudTypeCode(), fraud1.getFraudTypeCode()});
        GetAbuseTypesResponse abuseTypesResponse2 =
                new GetAbuseTypesResponse(fraud3.getUcid(), new String[] {fraud3.getFraudTypeCode()});

        Response response = getAbuseTypes(List.of(fraud1.getUcid(), fraud3.getUcid()));

        assertThat(response.body(), is(notNullValue()));
        GetAbuseTypesResponse[] mappedResponse =
                objectMapper.readValue(response.body().string(), GetAbuseTypesResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert array size", mappedResponse.length, is(2));
        assertThat("Assert client 1 abuse types", mappedResponse, hasItemInArray(abuseTypesResponse1));
        assertThat("Assert client 2 abuse types", mappedResponse, hasItemInArray(abuseTypesResponse2));
    }

    @Test
    @DisplayName("Clickhouse Api. Get abuse types incorrect clientIds (400)")
    @AllureId("431")
    void getAbuseTypesIncorrectClientIdsTest() throws IOException {

        Response response = getAbuseTypes(List.of("test"));

        assertThat(response.body(), is(notNullValue()));
        ClickhouseApiErrorResponse mappedResponse =
                objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat(
                "Assert error",
                mappedResponse.getError(),
                is(
                        "Invalid \"clientId\" property format. The property clientId must contain brand and userId divided by a dash e.g., vantage-2068746030"));
        assertThat("Assert status", mappedResponse.getStatus(), is(400));
    }
}
