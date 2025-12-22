package tests.payment_gate_service_tests;

import static business_objects.api.payment_gate.payments.PaymentsRequestBodyFactory.createPutPaymentsV2RequestBody;
import static business_objects.api.payment_gate.payments.PaymentsRequests.putPaymentsV2;
import static business_objects.db.payment_gate.payment_details.PaymentDetailsObjectFactory.generatePaymentDetailsObject;
import static business_objects.db.payment_gate.payment_events.PaymentEventsObjectFactory.generatePaymentEventsObject;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.CleanTableHelper.cleanPaymentGateData;
import static helpers.database.DbHelper.insertObjectsToDb;
import static helpers.database.PaymentGateHelper.getPaymentEvent;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

import business_objects.api.payment_gate.payments.PutPaymentsResponseBody;
import business_objects.api.payment_gate.payments.PutPaymentsV2RequestBody;
import business_objects.db.payment_gate.payment_details.PaymentDetailsObject;
import business_objects.db.payment_gate.payment_events.PaymentEventsObject;
import helpers.data.ClientHelper;
import helpers.database.DbName;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;

@Feature(FEATURE_PAYMENT_GATE)
@Story(STORY_PAYMENT_GATE_PUT_PAYMENTS_V2)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_PAYMENT_GATE_TESTS)
class PutPaymentsV2Tests extends TestBaseApi {

    private static ClientHelper client1;
    private static ClientHelper client2;
    private static ClientHelper client3;
    private static ClientHelper client4;
    private static PaymentEventsObject paymentEventsObject1;
    private static PaymentEventsObject paymentEventsObject2;
    private static PaymentEventsObject paymentEventsObject3;
    private static PaymentEventsObject paymentEventsObject4;
    private static PaymentDetailsObject paymentDetailsObject1;
    private static PaymentDetailsObject paymentDetailsObject2;
    private static PaymentDetailsObject paymentDetailsObject3;
    private static PaymentDetailsObject paymentDetailsObject4;

    private static PutPaymentsV2RequestBody putPaymentsRequestBody1;
    private static PutPaymentsV2RequestBody putPaymentsRequestBody2;
    private static PutPaymentsV2RequestBody putPaymentsRequestBody3;
    private static PutPaymentsV2RequestBody putPaymentsRequestBody4;

    @BeforeAll
    static void setupData() throws IOException {

        client1 = getRandomVantageClientAllFields();
        paymentEventsObject1 = generatePaymentEventsObject(client1);
        paymentDetailsObject1 = generatePaymentDetailsObject(paymentEventsObject1, client1);
        paymentDetailsObject1.setPayload(
                "{\"id\": \"123e4567-e89b-12d3-a456-426614174000\", \"ip\": \"121.233.122.82\", \"card\": {\"card3ds\": 0, \"expYear\": \"2029\", \"expMonth\": \"4\", \"fullName\": \"sheryar shah\", \"lastFour\": \"1225\", \"binNumber\": \"654321\"}, \"cost\": 0.56, \"type\": \"withdrawal\", \"brand\": \"vantage\", \"status\": \"Success\", \"clientId\": 112341, \"platform\": \"WEB\", \"statusId\": 1, \"checkName\": \"WR_Blacklist\", \"eventDate\": \"2025-05-20T14:30:00Z\", \"regulator\": \"CIMA\", \"statusKYC\": \"Confirmed\", \"mt4Account\": 3031915, \"accountType\": \"MT5\", \"withdrawalId\": 2373634, \"schemaVersion\": \"1.0\", \"merchantOrderId\": \"VTSG1115142220250202132259\", \"paymentTypeCode\": 2, \"paymentTypeName\": \"Credit card\", \"withdrawalAmount\": 1500.00, \"paymentMethodCode\": \"CREDIT_CARD\", \"paymentChannelCode\": 1, \"paymentChannelName\": \"Credit card\", \"withdrawalCurrency\": \"USD\", \"withdrawalAmountUSD\": 1500.00, \"withdrawalApplicationTime\": \"2025-07-15 07:38:05\"}");

        client2 = getRandomVantageClientAllFields();
        paymentEventsObject2 = generatePaymentEventsObject(client2);
        paymentDetailsObject2 = generatePaymentDetailsObject(paymentEventsObject2, client2);

        client3 = getRandomVantageClientAllFields();
        paymentEventsObject3 = generatePaymentEventsObject(client3);
        paymentDetailsObject3 = generatePaymentDetailsObject(paymentEventsObject3, client3);

        client4 = getRandomVantageClientAllFields();
        paymentEventsObject4 = generatePaymentEventsObject(client4);
        paymentDetailsObject4 = generatePaymentDetailsObject(paymentEventsObject4, client4);

        insertObjectsToDb(
                DbName.POSTGRES,
                PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE,
                List.of(paymentEventsObject1, paymentEventsObject2, paymentEventsObject3, paymentEventsObject4));
        insertObjectsToDb(
                DbName.POSTGRES,
                PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE,
                List.of(paymentDetailsObject1, paymentDetailsObject2, paymentDetailsObject3, paymentDetailsObject4));

        putPaymentsRequestBody1 = createPutPaymentsV2RequestBody(paymentEventsObject1.getPaymentId(), 2);
        putPaymentsRequestBody2 = createPutPaymentsV2RequestBody(paymentEventsObject2.getPaymentId(), 2);
        putPaymentsRequestBody3 = null;
        putPaymentsRequestBody4 = createPutPaymentsV2RequestBody(paymentEventsObject4.getPaymentId(), 2);
    }

    // @AfterAll
    static void deleteData() throws Exception {
        cleanPaymentGateData(client1.getUcid(), client1.getUserId());
        cleanPaymentGateData(client2.getUcid(), client2.getUserId());
        cleanPaymentGateData(client3.getUcid(), client2.getUserId());
        cleanPaymentGateData(client4.getUcid(), client4.getUserId());
    }

    @Test
    @AllureId("1606")
    @DisplayName("Put payment v1. Success test. 201")
    void putPaymentTest1() throws Exception {

        Allure.step("send put payment request with valid data");
        Response response = putPaymentsV2(putPaymentsRequestBody1);

        assertThat(response.code(), is(200));

        Allure.step("Validate Data in response");
        PutPaymentsResponseBody mappedResponse =
                objectMapper.readValue(response.body().string(), PutPaymentsResponseBody.class);
        assertThat("Check response", mappedResponse.getPaymentId(), is(instanceOf(UUID.class)));
        assertThat("Check response", mappedResponse.getPaymentId(), is(paymentEventsObject1.getPaymentId()));
        assertThat("Check response", mappedResponse.getDecisionId(), is(2));
        assertThat("Check response", mappedResponse.getDecidedAt(), is(notNullValue()));
        assertThat(
                "Check response",
                mappedResponse.getLinks().getSelf(),
                is(String.format("/v2/payments/%s", paymentEventsObject1.getPaymentId())));
        assertThat(
                "Check id in db",
                getPaymentEvent(putPaymentsRequestBody1.getPaymentId()).getFinalDecisionId(),
                is(putPaymentsRequestBody1.getDecisionId()));
    }

    @Test
    @AllureId("1607")
    @DisplayName("Put payment v1. 400 validation failed")
    void putPaymentTest2() throws Exception {

        Allure.step("send put payment request with valid data");
        Response response = putPaymentsV2(putPaymentsRequestBody3);
        assertThat(response.code(), is(400));

        Allure.step("Validate Data in response");
        PutPaymentsResponseBody mappedResponse =
                objectMapper.readValue(response.body().string(), PutPaymentsResponseBody.class);
        assertThat("Check error", mappedResponse.getError(), is("bad_request"));
        assertThat("Check status", mappedResponse.getMessage(), is(nullValue()));
    }

    @Test
    @AllureId("1608")
    @DisplayName("Put payment v1. 400 validation failed")
    void putPaymentTest3() throws Exception {

        Allure.step("send put payment request with valid data");
        Response response = putPaymentsV2("{asdad}", 400);
        assertThat(response.code(), is(400));

        Allure.step("Validate Data in response");
        PutPaymentsResponseBody mappedResponse =
                objectMapper.readValue(response.body().string(), PutPaymentsResponseBody.class);
        assertThat("Check error", mappedResponse.getError(), is("bad_request"));
        assertThat("Check status", mappedResponse.getMessage(), containsString("must not be null"));
    }

    @Test
    @AllureId("1609")
    @DisplayName("Put payment v1. 404 payment not found")
    void putPaymentTest4() throws Exception {

        Allure.step("send put payment request with valid data");
        putPaymentsRequestBody4.setPaymentId(UUID.fromString("d6bbcd33-30d5-4459-8aa7-b408732c0c30"));
        Response response = putPaymentsV2(putPaymentsRequestBody4);
        assertThat(response.code(), is(404));

        Allure.step("Validate Data in response");
        PutPaymentsResponseBody mappedResponse =
                objectMapper.readValue(response.body().string(), PutPaymentsResponseBody.class);
        assertThat("Check error", mappedResponse.getError(), is("not_found"));
        assertThat(
                "Check status",
                mappedResponse.getMessage(),
                is(String.format("paymentId d6bbcd33-30d5-4459-8aa7-b408732c0c30 is not found")));
    }
}
