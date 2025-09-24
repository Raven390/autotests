package tests.payment_gate_service_tests;


import business_objects.api.payment_gate.payments.PostPaymentsRequestBody;
import business_objects.api.payment_gate.payments.PostPaymentsResponseBody;
import business_objects.db.payment_gate.payment_details.PaymentDetailsObject;
import business_objects.db.payment_gate.payment_events.PaymentEventsObject;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;


import java.util.UUID;

import static business_objects.api.payment_gate.payments.PaymentsRequests.postPayments;
import static business_objects.api.payment_gate.payments.PaymentsRequestBodyFactory.createPostPaymentsRequestBody;
import static helpers.database.CleanTableHelper.cleanPaymentGateData;
import static helpers.database.PaymentGateHelper.getPaymentDetailsByClientId;
import static helpers.database.PaymentGateHelper.getPaymentEventByUcid;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Constants.LAYER_API;
import static utils.Constants.SUITE_PAYMENT_GATE_TESTS;
import static utils.Utils.buildUcid;

@Feature(FEATURE_PAYMENT_GATE)
@Story(STORY_PAYMENT_GATE_POST_PAYMENTS)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_PAYMENT_GATE_TESTS)
class PostPaymentsV1Tests extends TestBaseApi {

    private static PostPaymentsRequestBody postPaymentsRequestBody1;
    private static PostPaymentsRequestBody postPaymentsRequestBody2;
    private static PostPaymentsRequestBody postPaymentsRequestBody3;
    private static PostPaymentsRequestBody postPaymentsRequestBody4;
    private static String ucid1;
    private static String ucid2;
    private static String ucid4;


    @BeforeAll
    static void setupData() {
        postPaymentsRequestBody1 = createPostPaymentsRequestBody();
        ucid1 = buildUcid(postPaymentsRequestBody1.getBrand(), postPaymentsRequestBody1.getClientId());
        postPaymentsRequestBody2 = createPostPaymentsRequestBody();
        ucid2 = buildUcid(postPaymentsRequestBody2.getBrand(), postPaymentsRequestBody2.getClientId());
        postPaymentsRequestBody3 = null;
        postPaymentsRequestBody4 = createPostPaymentsRequestBody();
        ucid4 = buildUcid(postPaymentsRequestBody4.getBrand(), postPaymentsRequestBody4.getClientId());
    }

    @AfterAll
    static void deleteData() throws Exception {
        cleanPaymentGateData(ucid1, postPaymentsRequestBody1.getClientId());
        cleanPaymentGateData(ucid2, postPaymentsRequestBody2.getClientId());
        cleanPaymentGateData(ucid4, postPaymentsRequestBody4.getClientId());
    }

    @Test
    @AllureId("1543")
    @DisplayName("Post payment v1. Success test. 201")
    void postPaymentTest1() throws Exception {

        Allure.step("send post payment request with valid data");
        Response response = postPayments(postPaymentsRequestBody1);
        assertThat(response.code(), is(201));

        Allure.step("Validate Data in response");
        PostPaymentsResponseBody mappedResponse = objectMapper.readValue(response.body().string(), PostPaymentsResponseBody.class);
        assertThat("Check payment id", mappedResponse.getPaymentId(), is(instanceOf(UUID.class)));
        assertThat("Check type", mappedResponse.getType(), is(postPaymentsRequestBody1.getType()));
        assertThat("Check storedAt", mappedResponse.getStoredAt(), matchesPattern("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(?:\\.\\d+)?(?:Z|[+-]\\d{2}:\\d{2})?$"));
        assertThat("Check idempotency key", mappedResponse.getIdempotencyKey(), is(postPaymentsRequestBody1.getWithdrawalId()));
        assertThat("Check links", mappedResponse.getLinks().getSelf(), is("/v1/payments/" + mappedResponse.getPaymentId()));

        Thread.sleep(2500);

        Allure.step("Validate Data in paymentEvent table");
        String expectedUcid = buildUcid(postPaymentsRequestBody1.getBrand(), postPaymentsRequestBody1.getClientId());
        PaymentEventsObject paymentEventsObject = getPaymentEventByUcid(expectedUcid);
        assertThat("Assert record should be found for UCID", paymentEventsObject, is(notNullValue()));
        assertThat("Assert paymentId", paymentEventsObject.getPaymentId(), is(mappedResponse.getPaymentId()));
        assertThat("Assert crmId", paymentEventsObject.getCrmId(), is(postPaymentsRequestBody1.getWithdrawalId().toString()));
        assertThat("Assert type", paymentEventsObject.getType(), is(postPaymentsRequestBody1.getType()));
        assertThat("Assert decisionId", paymentEventsObject.getFinalDecisionId(), is(nullValue()));
        assertThat("Assert ucid", paymentEventsObject.getUcid(), is(expectedUcid));
        assertThat("Assert deliveryStatus", paymentEventsObject.getDeliveryStatus(), is("PENDING"));
        //assertThat("Assert dateCreated", paymentEventsObject.getDateCreated(), matchesPattern("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(?:\\.\\d+)?(?:Z|[+-]\\d{2}:\\d{2})?$"));
        //assertThat("Assert dateUpdated", paymentEventsObject.getDateUpdated(), matchesPattern("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(?:\\.\\d+)?(?:Z|[+-]\\d{2}:\\d{2})?$"));
        //TODO enable after fix (dateDecided should be null or 0
        //assertThat("Assert dateDecided",paymentEventsObject.getDateDecided(), is(mappedResponse.getPaymentId()));

        Allure.step("Validate Data in paymentDetails table");
        PaymentDetailsObject paymentDetailsObject = getPaymentDetailsByClientId(postPaymentsRequestBody1.getClientId());
        assertThat("Assert brand", paymentDetailsObject.getBrand(), is(postPaymentsRequestBody1.getBrand()));
        assertThat("Assert regulator", paymentDetailsObject.getRegulator(), is(postPaymentsRequestBody1.getRegulator()));
        assertThat("Assert type", paymentDetailsObject.getType(), is(postPaymentsRequestBody1.getType()));
        assertThat("Assert client id", paymentDetailsObject.getClientId(), is(String.valueOf(postPaymentsRequestBody1.getClientId())));
        assertThat("Assert merchant id", paymentDetailsObject.getMerchantOrderId(), is(postPaymentsRequestBody1.getMerchantOrderId()));
        //assertThat("Assert eventDate", paymentDetailsObject.getEventDate(), is(postPaymentsRequestBody1.getEventDate()));
        assertThat("Assert status", paymentDetailsObject.getStatus(), is(postPaymentsRequestBody1.getStatus()));
        PostPaymentsRequestBody dbObject = objectMapper.readValue(paymentDetailsObject.getPayload(), PostPaymentsRequestBody.class);
        assertThat("Assert payload", postPaymentsRequestBody1.equals(dbObject), is(true));
        assertThat("Assert system", paymentDetailsObject.getSourceSystem(), is("CRM"));
        assertThat("Assert env", paymentDetailsObject.getSourceEnv(), is("test"));
    }

    @Test
    @AllureId("1546")
    @DisplayName("Post payment v1. Success test. 200 idempotent")
    void postPaymentTest2() throws Exception {

        Allure.step("send post payment request with valid data");
        Response response = postPayments(postPaymentsRequestBody2);
        Response response1 = postPayments(postPaymentsRequestBody2);
        assertThat(response.code(), is(201));
        assertThat(response1.code(), is(200));

        Allure.step("Validate Data in response");
        PostPaymentsResponseBody mappedResponse = objectMapper.readValue(response.body().string(), PostPaymentsResponseBody.class);
        assertThat("Check payment id", mappedResponse.getPaymentId(), is(instanceOf(UUID.class)));
        assertThat("Check type", mappedResponse.getType(), is(postPaymentsRequestBody2.getType()));
        assertThat("Check storedAt", mappedResponse.getStoredAt(), matchesPattern("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(?:\\.\\d+)?(?:Z|[+-]\\d{2}:\\d{2})?$"));
        assertThat("Check idempotency key", mappedResponse.getIdempotencyKey(), is(postPaymentsRequestBody2.getWithdrawalId()));
        assertThat("Check links", mappedResponse.getLinks().getSelf(), is("/v1/payments/" + mappedResponse.getPaymentId()));

        Thread.sleep(2500);

        Allure.step("Validate Data in paymentEvent table");
        String expectedUcid = buildUcid(postPaymentsRequestBody2.getBrand(), postPaymentsRequestBody2.getClientId());
        PaymentEventsObject paymentEventsObject = getPaymentEventByUcid(expectedUcid);
        assertThat("Assert record should be found for UCID", paymentEventsObject, is(notNullValue()));
        assertThat("Assert paymentId", paymentEventsObject.getPaymentId(), is(mappedResponse.getPaymentId()));
        assertThat("Assert crmId", paymentEventsObject.getCrmId(), is(postPaymentsRequestBody2.getWithdrawalId().toString()));
        assertThat("Assert type", paymentEventsObject.getType(), is(postPaymentsRequestBody2.getType()));
        assertThat("Assert decisionId", paymentEventsObject.getFinalDecisionId(), is(nullValue()));
        assertThat("Assert ucid", paymentEventsObject.getUcid(), is(expectedUcid));
        //assertThat("Assert dateCreated", paymentEventsObject.getDateCreated(), matchesPattern("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(?:\\.\\d+)?(?:Z|[+-]\\d{2}:\\d{2})?$"));
        //assertThat("Assert dateUpdated", paymentEventsObject.getDateUpdated(), matchesPattern("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(?:\\.\\d+)?(?:Z|[+-]\\d{2}:\\d{2})?$"));
        //TODO enable after fix (dateDecided should be null or 0
        //assertThat("Assert dateDecided",paymentEventsObject.getDateDecided(), is(mappedResponse.getPaymentId()));

        Allure.step("Validate Data in paymentDetails table");
        PaymentDetailsObject paymentDetailsObject = getPaymentDetailsByClientId(postPaymentsRequestBody2.getClientId());
        assertThat("Assert brand", paymentDetailsObject.getBrand(), is(postPaymentsRequestBody2.getBrand()));
        assertThat("Assert regulator", paymentDetailsObject.getRegulator(), is(postPaymentsRequestBody2.getRegulator()));
        assertThat("Assert type", paymentDetailsObject.getType(), is(postPaymentsRequestBody2.getType()));
        assertThat("Assert client id", paymentDetailsObject.getClientId(), is(String.valueOf(postPaymentsRequestBody2.getClientId())));
        assertThat("Assert merchant id", paymentDetailsObject.getMerchantOrderId(), is(postPaymentsRequestBody2.getMerchantOrderId()));
        // Just not null due to time conversion issues
        assertThat("Assert eventDate", paymentDetailsObject.getEventDate(), is(notNullValue()));
        assertThat("Assert status", paymentDetailsObject.getStatus(), is(postPaymentsRequestBody2.getStatus()));
        PostPaymentsRequestBody dbObject = objectMapper.readValue(paymentDetailsObject.getPayload(), PostPaymentsRequestBody.class);
        assertThat("Assert payload", postPaymentsRequestBody2.equals(dbObject), is(true));
        assertThat("Assert system", paymentDetailsObject.getSourceSystem(), is("CRM"));
        assertThat("Assert env", paymentDetailsObject.getSourceEnv(), is("test"));

        Allure.step("Validate Data in response1");
        PostPaymentsResponseBody mappedResponse1 = objectMapper.readValue(response1.body().string(), PostPaymentsResponseBody.class);
        assertThat("Assert payment1", mappedResponse1.getPaymentId(), is(mappedResponse.getPaymentId()));
        assertThat("Assert type", mappedResponse1.getType(), is(postPaymentsRequestBody2.getType()));
        // Just not null due to time conversion issues
        assertThat("Assert storedAt", mappedResponse1.getStoredAt(), is(notNullValue()));
        assertThat("Assert idempotent", mappedResponse1.getIdempotent(), is(true));
    }

    @Test
    @AllureId("1547")
    @DisplayName("Post payment v1. 400 validation failed")
    void postPaymentTest3() throws Exception {

        Allure.step("send post payment request with valid data");
        Response response = postPayments(postPaymentsRequestBody3);
        assertThat(response.code(), is(400));

        Allure.step("Validate Data in response");
        PostPaymentsResponseBody mappedResponse = objectMapper.readValue(response.body().string(), PostPaymentsResponseBody.class);
        assertThat("Check error", mappedResponse.getError(), is("bad_request"));
        assertThat("Check status", mappedResponse.getMessage(), is("json can not be null"));
    }

    @Test
    @AllureId("1548")
    @DisplayName("Post payment v1. 400 validation failed")
    void postPaymentTest4() throws Exception {

        Allure.step("send post payment request with valid data");
        Response response = postPayments("{asdad}", 400);
        assertThat(response.code(), is(400));

        Allure.step("Validate Data in response");
        PostPaymentsResponseBody mappedResponse = objectMapper.readValue(response.body().string(), PostPaymentsResponseBody.class);
        assertThat("Check error", mappedResponse.getError(), is("bad_request"));
        assertThat("Check status", mappedResponse.getMessage(), is("Invalid JSON format"));
    }
}