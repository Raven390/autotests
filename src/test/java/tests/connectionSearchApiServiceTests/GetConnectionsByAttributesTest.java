package tests.connectionSearchApiServiceTests;

import businessObjects.api.connectionSearchApi.GetConnectionsResponse;
import businessObjects.api.connectionSearchApi.GetConnectionsResponseError;
import businessObjects.db.clickhouse.csTbConnectionTableV3.ConnectionTableEntryV3;
import businessObjects.db.clickhouse.csTbDocTable.DocumentTableEntry;
import businessObjects.db.clickhouse.csTbEmailTable.EmailTableEntry;
import businessObjects.db.clickhouse.csTbIpTable.IpTableEntry;
import businessObjects.db.clickhouse.csTbPayoutTable.PayoutTableEntry;
import businessObjects.db.clickhouse.csTbPhoneTable.PhoneTableEntry;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static businessObjects.api.connectionSearchApi.GetConnectionsRequest.getConnectionsByAttributes;
import static businessObjects.api.connectionSearchApi.GetConnectionsResponseFactory.*;
import static businessObjects.db.clickhouse.csTbConnectionTableV3.ConnectionTableEntryV3Factory.*;
import static businessObjects.db.clickhouse.csTbDocTable.DocumentTableEntryFactory.documentTableEntryForConnectionSearch;
import static businessObjects.db.clickhouse.csTbEmailTable.EmailTableEntryFactory.emailTableEntryForConnectionSearch;
import static businessObjects.db.clickhouse.csTbEmailTable.EmailTableEntryFactory.emailTableEntryForConnectionSearchFiltration;
import static businessObjects.db.clickhouse.csTbIpTable.IpTableEntryFactory.ipTableEntryForConnectionSearch;
import static businessObjects.db.clickhouse.csTbPayoutTable.PayoutTableEntryFactory.payoutTableEntryForConnectionSearch;
import static businessObjects.db.clickhouse.csTbPhoneTable.PhoneTableEntryFactory.phoneTableEntryForConnectionSearch;
import static helpers.database.DbHelper.deleteEntryFromDb;
import static helpers.database.DbHelper.insertObjectToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

@Feature(FEATURE_CONNECTION_SEARCH_API_SERVICE)
@Story(STORY_CONNECTION_SEARCH_BY_ATTRIBUTES)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CONNECTION_SEARCH_SERVICE)
public class GetConnectionsByAttributesTest extends TestBaseApi {

    // Expected responses
    public static GetConnectionsResponse getConnectionsByAttributesDocumentResponseSuccess = getConnectionsByAttributesDocumentResponseSuccess();
    public final GetConnectionsResponse getConnectionsByAttributesEmailResponseSuccess = getConnectionsByAttributesEmailResponseSuccess();
    public final GetConnectionsResponse getConnectionsByAttributesIpResponseSuccess = getConnectionsByAttributesIpResponseSuccess();
    public final GetConnectionsResponse getConnectionsByAttributesPhoneResponseSuccess = getConnectionsByAttributesPhoneResponseSuccess();
    public final GetConnectionsResponse getConnectionsByAttributesPayoutResponseSuccess = getConnectionsByAttributesPayoutResponseSuccess();
    public final GetConnectionsResponse getConnectionsByAttributesConnDepthResponseSuccess = getConnectionsByAttributesForDepth();
    public final GetConnectionsResponse[] getConnectionsByAttributesFiltrationResponseSuccess = getConnectionsByAttributesForFiltrationByParams();
    public final GetConnectionsResponseError getConnectionsResponseError = getConnectionsByAttributesResponseErrorBadRequest();
    // Objects to insert to connections table
    public static final ConnectionTableEntryV3 connectionTableEntryByDocumentV3 = getConnectionTableEntryByDocumentV3();
    public static final ConnectionTableEntryV3 connectionTableEntryByEmailV3 = getConnectionTableEntryByEmailV3();
    public static final ConnectionTableEntryV3 connectionTableEntryByIpV3 = getConnectionTableEntryByIpV3();
    public static final ConnectionTableEntryV3 connectionTableEntryByPhoneV3 = getConnectionTableEntryByPhoneV3();
    public static final ConnectionTableEntryV3 connectionTableEntryByPayoutV3 = getConnectionTableEntryByPayoutV3();
    public static final ConnectionTableEntryV3 connectionTableEntryForDepthV3 = getConnectionTableEntryForDepthV3();
    public static final ConnectionTableEntryV3 connectionTableEntryFiltration1 = getConnectionTableByAttributesEntryForFiltration1V3();
    public static final ConnectionTableEntryV3 connectionTableEntryFiltration2 = getConnectionTableByAttributesEntryForFiltration2V3();
    // Objects to insert to attributes tables
    public static final DocumentTableEntry documentTableEntry = documentTableEntryForConnectionSearch();
    public static final EmailTableEntry emailTableEntry = emailTableEntryForConnectionSearch();
    public static final IpTableEntry ipTableEntry = ipTableEntryForConnectionSearch();
    public static final PhoneTableEntry phoneTableEntry = phoneTableEntryForConnectionSearch();
    public static final PayoutTableEntry payoutTableEntry = payoutTableEntryForConnectionSearch();
    public static final EmailTableEntry emailTableEntryFiltration = emailTableEntryForConnectionSearchFiltration();

    @BeforeAll
    public static void setupConnectionTableEntry() throws ReflectiveOperationException, SQLException {
        // Insert data to connections table
        insertObjectToDb(CONNECTIONS_V3_TABLE_NAME, connectionTableEntryByDocumentV3);
        insertObjectToDb(CONNECTIONS_V3_TABLE_NAME, connectionTableEntryByEmailV3);
        insertObjectToDb(CONNECTIONS_V3_TABLE_NAME, connectionTableEntryByIpV3);
        insertObjectToDb(CONNECTIONS_V3_TABLE_NAME, connectionTableEntryByPhoneV3);
        insertObjectToDb(CONNECTIONS_V3_TABLE_NAME, connectionTableEntryByPayoutV3);
        insertObjectToDb(CONNECTIONS_V3_TABLE_NAME, connectionTableEntryForDepthV3);
        insertObjectToDb(CONNECTIONS_V3_TABLE_NAME, connectionTableEntryFiltration1);
        insertObjectToDb(CONNECTIONS_V3_TABLE_NAME, connectionTableEntryFiltration2);
        // Insert data to attributes tables
        insertObjectToDb(DOCUMENT_TABLE_NAME, documentTableEntry);
        insertObjectToDb(EMAIL_TABLE_NAME, emailTableEntry);
        insertObjectToDb(IP_TABLE_NAME, ipTableEntry);
        insertObjectToDb(PHONE_TABLE_NAME, phoneTableEntry);
        insertObjectToDb(PAYOUT_TABLE_NAME, payoutTableEntry);
        insertObjectToDb(EMAIL_TABLE_NAME, emailTableEntryFiltration);
    }

    @Test
    @DisplayName("Connection search by attributes Api. Get connection by document success(200)")
    @AllureId("188")
    public void getConnectionsByAttributesDocumentSuccessTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("documentType", documentTableEntry.accIdType);
        queryParams.put("documentNumber", documentTableEntry.accIdNum);
        queryParams.put("documentCountryId", documentTableEntry.nationalityId);

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                response.body().string(),
                GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length > 0, equalTo(true));

        assertThat("Check the response body", Arrays.stream(responseBody).toList(), containsInAnyOrder(getConnectionsByAttributesDocumentResponseSuccess, getConnectionsByAttributesConnDepthResponseSuccess));
    }

    @Test
    @DisplayName("Connection search by attributes Api. Get connection by document with connection depth success(200)")
    @AllureId("189")
    public void getConnectionsByAttributesConnDepthSuccessTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("documentType", documentTableEntry.accIdType);
        queryParams.put("documentNumber", documentTableEntry.accIdNum);
        queryParams.put("documentCountryId", documentTableEntry.nationalityId);
        queryParams.put("connectionDepth", 1);

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                response.body().string(),
                GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length > 0, equalTo(true));
        GetConnectionsResponse firstResponse = responseBody[0];

        assertThat("Check the response body", firstResponse, equalTo(getConnectionsByAttributesDocumentResponseSuccess));
    }

    @Test
    @DisplayName("Connection search by attributes Api. Get connection by email success(200)")
    @AllureId("190")
    public void getConnectionsByAttributesEmailSuccessTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("emailAddress", emailTableEntry.email);

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                response.body().string(),
                GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length > 0, equalTo(true));
        GetConnectionsResponse firstResponse = responseBody[0];

        assertThat("Check the response body", firstResponse, equalTo(getConnectionsByAttributesEmailResponseSuccess));
    }

    @Test
    @DisplayName("Connection search by attributes Api. Get connection by ip success(200)")
    @AllureId("191")
    public void getConnectionsByAttributesIpSuccessTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("ipAddress", ipTableEntry.ip);

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                response.body().string(),
                GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length > 0, equalTo(true));
        GetConnectionsResponse firstResponse = responseBody[0];

        assertThat("Check the response body", firstResponse, equalTo(getConnectionsByAttributesIpResponseSuccess));
    }

    @Test
    @DisplayName("Connection search by attributes Api. Get connection by phone success(200)")
    @AllureId("192")
    public void getConnectionsByAttributesPhoneSuccessTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("phoneNumber", phoneTableEntry.phoneNum);

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                response.body().string(),
                GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length > 0, equalTo(true));
        GetConnectionsResponse firstResponse = responseBody[0];

        assertThat("Check the response body", firstResponse, equalTo(getConnectionsByAttributesPhoneResponseSuccess));
    }

    @Test
    @DisplayName("Connection search by attributes Api. Get connection by payout success(200)")
    @AllureId("193")
    public void getConnectionsByAttributesPayoutSuccessTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("payoutId", payoutTableEntry.payoutId);

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                response.body().string(),
                GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length > 0, equalTo(true));
        GetConnectionsResponse firstResponse = responseBody[0];

        assertThat("Check the response body", firstResponse, equalTo(getConnectionsByAttributesPayoutResponseSuccess));
    }

    @Test
    @DisplayName("Connection search by attributes Api. Get connection by all params success(200)")
    @AllureId("194")
    public void getConnectionsByAttributesAllSuccessTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("documentType", documentTableEntry.accIdType);
        queryParams.put("documentNumber", documentTableEntry.accIdNum);
        queryParams.put("documentCountryId", documentTableEntry.nationalityId);
        queryParams.put("emailAddress", emailTableEntry.email);
        queryParams.put("ipAddress", ipTableEntry.ip);
        queryParams.put("phoneNumber", phoneTableEntry.phoneNum);
        queryParams.put("payoutId", payoutTableEntry.payoutId);

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                response.body().string(),
                GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length > 0, equalTo(true));

        assertThat("Check that response body has object found by document", responseBody, hasItemInArray(getConnectionsByAttributesDocumentResponseSuccess));
        assertThat("Check that response body has object found by emailAddress", responseBody, hasItemInArray(getConnectionsByAttributesEmailResponseSuccess));
        assertThat("Check that response body has object found by ipAddress", responseBody, hasItemInArray(getConnectionsByAttributesIpResponseSuccess));
        assertThat("Check that response body has object found by phoneNumber", responseBody, hasItemInArray(getConnectionsByAttributesPhoneResponseSuccess));
        assertThat("Check that response body has object found by payoutId", responseBody, hasItemInArray(getConnectionsByAttributesPayoutResponseSuccess));
        assertThat("Check that response body has object found by depth", responseBody, hasItemInArray(getConnectionsByAttributesConnDepthResponseSuccess));
    }

    @Test
    @DisplayName("Connection search by attributes Api. Get connection with connectionScoreFrom success(200)")
    @AllureId("")
    public void getConnectionsByAttributesConnectionScoreFromSuccessTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("emailAddress", emailTableEntryFiltration.email);
        queryParams.put("connectionScoreFrom", 1);

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                response.body().string(),
                GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length, equalTo(1));

        assertThat("Check the response body", responseBody[0], equalTo(getConnectionsByAttributesFiltrationResponseSuccess[0]));
    }

    @Test
    @DisplayName("Connection search by attributes Api. Get connection with connectionScoreTo success(200)")
    @AllureId("")
    public void getConnectionsByAttributesConnectionScoreToSuccessTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("emailAddress", emailTableEntryFiltration.email);
        queryParams.put("connectionScoreTo", 0.9);

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                response.body().string(),
                GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length, equalTo(1));

        assertThat("Check the response body", responseBody[0], equalTo(getConnectionsByAttributesFiltrationResponseSuccess[1]));
    }

    @Test
    @DisplayName("Connection search by attributes Api. Get connection with connectionType success(200)")
    @AllureId("")
    public void getConnectionsByAttributesConnectionTypeSuccessTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("emailAddress", emailTableEntryFiltration.email);
        queryParams.put("connectionType", List.of("Same Network"));

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                response.body().string(),
                GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length, equalTo(1));

        assertThat("Check the response body", responseBody[0], equalTo(getConnectionsByAttributesFiltrationResponseSuccess[1]));
    }

    @Test
    @DisplayName("Connection search by attributes Api. documentCountryId not int bad request (400)")
    @AllureId("195")
    public void getConnectionsByAttributesDocumentCountryIdNotIntBadRequestTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("documentType", documentTableEntry.accIdType);
        queryParams.put("documentNumber", documentTableEntry.accIdNum);
        queryParams.put("documentCountryId", "test");

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponseError responseBody = objectMapper.readValue(
                response.body().string(),
                GetConnectionsResponseError.class
        );

        assertThat("Check the response code is 400", response.code(), is(400));

        assertThat("Check the response body", responseBody, equalTo(getConnectionsResponseErrorDocumentCountryIdNotInt()));
    }

    @Test
    @DisplayName("Connection search by attributes Api. documentType missing bad request (400)")
    @AllureId("196")
    public void getConnectionsByAttributesDocumentTypeMissingBadRequestTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("documentNumber", documentTableEntry.accIdNum);
        queryParams.put("documentCountryId", documentTableEntry.nationalityId);

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponseError responseBody = objectMapper.readValue(
                response.body().string(),
                GetConnectionsResponseError.class
        );

        assertThat("Check the response code is 400", response.code(), is(400));

        assertThat("Check the response body", responseBody, equalTo(getConnectionsResponseErrorDocumentTypeBadRequest()));
    }

    @Test
    @DisplayName("Connection search by attributes Api. documentNumber missing bad request (400)")
    @AllureId("197")
    public void getConnectionsByAttributesDocumentNumberMissingBadRequestTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("documentType", documentTableEntry.accIdType);
        queryParams.put("documentCountryId", documentTableEntry.nationalityId);

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponseError responseBody = objectMapper.readValue(
                response.body().string(),
                GetConnectionsResponseError.class
        );

        assertThat("Check the response code is 400", response.code(), is(400));

        assertThat("Check the response body", responseBody, equalTo(getConnectionsResponseErrorDocumentNumberBadRequest()));
    }

    @Test
    @DisplayName("Connection search by attributes Api. documentCountryId missing bad request (400)")
    @AllureId("198")
    public void getConnectionsByAttributesDocumentCountryIdMissingBadRequestTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("documentType", documentTableEntry.accIdType);
        queryParams.put("documentNumber", documentTableEntry.accIdNum);

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponseError responseBody = objectMapper.readValue(
                response.body().string(),
                GetConnectionsResponseError.class
        );

        assertThat("Check the response code is 400", response.code(), is(400));

        assertThat("Check the response body", responseBody, equalTo(getConnectionsResponseErrorDocumentCountryIdBadRequest()));
    }

    @Test
    @DisplayName("Connection search by attributes Api. No params bad request (400)")
    @AllureId("199")
    public void getConnectionsByAttributesNoParamsBadRequestTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponseError responseBody = objectMapper.readValue(
                response.body().string(),
                GetConnectionsResponseError.class
        );

        assertThat("Check the response code is 400", response.code(), is(400));

        assertThat("Check the response body", responseBody, equalTo(getConnectionsResponseErrorNoSearchParameters()));
    }

    @Test
    @DisplayName("Connection search by attributes Api. Get connection with connectionScoreFrom not int Bad Request(400)")
    @AllureId("")
    public void getConnectionsConnectionScoreFromNotIntBadRequestTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("emailAddress", emailTableEntryFiltration.email);
        queryParams.put("connectionScoreFrom", "test");

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponseError responseBody = objectMapper.readValue(
                response.body().string(),
                GetConnectionsResponseError.class
        );

        assertThat("Check the response code is 400", response.code(), is(400));

        assertThat("Check the timestamp field", responseBody.timestamp, notNullValue());

        assertThat("Check the response body", responseBody, equalTo(getConnectionsResponseError));
    }

    @Test
    @DisplayName("Connection search by attributes Api. Get connection with connectionScoreTo not int Bad Request(400)")
    @AllureId("")
    public void getConnectionsConnectionScoreToNotIntBadRequestTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("emailAddress", emailTableEntryFiltration.email);
        queryParams.put("connectionScoreTo", "test");

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponseError responseBody = objectMapper.readValue(
                response.body().string(),
                GetConnectionsResponseError.class
        );

        assertThat("Check the response code is 400", response.code(), is(400));

        assertThat("Check the timestamp field", responseBody.timestamp, notNullValue());

        assertThat("Check the response body", responseBody, equalTo(getConnectionsResponseError));
    }

    @AfterAll
    public static void deleteConnectionTableEntry() throws SQLException {
        // Delete data from connections table
        deleteEntryFromDb(CONNECTIONS_V3_TABLE_NAME, String.format("user_from = '%s'", connectionTableEntryByDocumentV3.userFrom));
        deleteEntryFromDb(CONNECTIONS_V3_TABLE_NAME, String.format("user_from = '%s'", connectionTableEntryByEmailV3.userFrom));
        deleteEntryFromDb(CONNECTIONS_V3_TABLE_NAME, String.format("user_from = '%s'", connectionTableEntryByIpV3.userFrom));
        deleteEntryFromDb(CONNECTIONS_V3_TABLE_NAME, String.format("user_from = '%s'", connectionTableEntryByPhoneV3.userFrom));
        deleteEntryFromDb(CONNECTIONS_V3_TABLE_NAME, String.format("user_from = '%s'", connectionTableEntryByPayoutV3.userFrom));
        deleteEntryFromDb(CONNECTIONS_V3_TABLE_NAME, String.format("user_from = '%s'", connectionTableEntryForDepthV3.userFrom));
        deleteEntryFromDb(CONNECTIONS_V3_TABLE_NAME, String.format("user_from = '%s'", connectionTableEntryFiltration1.userFrom));
        deleteEntryFromDb(CONNECTIONS_V3_TABLE_NAME, String.format("user_from = '%s'", connectionTableEntryFiltration2.userFrom));
        // Delete data from attributes tables
        deleteEntryFromDb(DOCUMENT_TABLE_NAME, String.format("acc_id_num = '%s'", documentTableEntry.accIdNum));
        deleteEntryFromDb(EMAIL_TABLE_NAME, String.format("email = '%s'", emailTableEntry.email));
        deleteEntryFromDb(IP_TABLE_NAME, String.format("ip = '%s'", ipTableEntry.ip));
        deleteEntryFromDb(PHONE_TABLE_NAME, String.format("phone_num = '%s'", phoneTableEntry.phoneNum));
        deleteEntryFromDb(PAYOUT_TABLE_NAME, String.format("payout_id = '%s'", payoutTableEntry.payoutId));
        deleteEntryFromDb(EMAIL_TABLE_NAME, String.format("email = '%s'", emailTableEntryFiltration.email));
    }
}
