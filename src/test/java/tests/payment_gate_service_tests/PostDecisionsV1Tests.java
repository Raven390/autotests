package tests.payment_gate_service_tests;


import business_objects.api.payment_gate.payments_decisions.PostDecisionsRequestBody;
import business_objects.api.payment_gate.payments_decisions.PostDecisionsResponseBody;
import business_objects.db.payment_gate.payment_decisions.PaymentDecisionsObject;
import business_objects.db.payment_gate.payment_details.PaymentDetailsObject;
import business_objects.db.payment_gate.payment_events.PaymentEventsObject;
import business_objects.db.payment_gate.payment_rejection_attributes.payment_events.PaymentRejectionAttributesObject;
import helpers.data.ClientHelper;
import helpers.database.DbName;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;

import java.sql.Timestamp;
import java.util.List;

import static business_objects.api.payment_gate.payments_decisions.DecisionsRequests.postDecisions;
import static business_objects.api.payment_gate.payments_decisions.PaymentsRequestBodyFactory.createPostDecisionsRequestBody;
import static business_objects.db.payment_gate.payment_details.PaymentDetailsObjectFactory.generatePaymentDetailsObject;
import static business_objects.db.payment_gate.payment_events.PaymentEventsObjectFactory.generatePaymentEventsObject;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.CleanTableHelper.cleanPaymentGateData;
import static helpers.database.DbHelper.getObjectsFromDB;
import static helpers.database.DbHelper.insertObjectsToDb;
import static helpers.database.PaymentGateHelper.getPaymentDecisionsByPaymentId;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Constants.LAYER_API;
import static utils.Constants.SUITE_PAYMENT_GATE_TESTS;

@Feature(FEATURE_PAYMENT_GATE)
@Story(STORY_PAYMENT_GATE_POST_DECISIONS)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_PAYMENT_GATE_TESTS)
class PostDecisionsV1Tests extends TestBaseApi {

    private static ClientHelper client1;
    private static ClientHelper client2;
    private static ClientHelper client3;
    private static ClientHelper client4;
    private static ClientHelper client5;
    private static ClientHelper client6;
    private static ClientHelper client7;
    private static PostDecisionsRequestBody postPaymentDecisionBody1;
    private static PostDecisionsRequestBody postPaymentDecisionBody2;
    private static PostDecisionsRequestBody postPaymentDecisionBody3;
    private static PostDecisionsRequestBody postPaymentDecisionBody4;
    private static PostDecisionsRequestBody postPaymentDecisionBody5;
    private static PostDecisionsRequestBody postPaymentDecisionBody6;
    private static PostDecisionsRequestBody postPaymentDecisionBody7;
    private static PaymentEventsObject paymentEventsObject1;
    private static PaymentEventsObject paymentEventsObject2;
    private static PaymentEventsObject paymentEventsObject3;
    private static PaymentEventsObject paymentEventsObject4;
    private static PaymentEventsObject paymentEventsObject5;
    private static PaymentEventsObject paymentEventsObject6;
    private static PaymentEventsObject paymentEventsObject7;
    private static PaymentDetailsObject paymentDetailsObject1;
    private static PaymentDetailsObject paymentDetailsObject2;
    private static PaymentDetailsObject paymentDetailsObject3;
    private static PaymentDetailsObject paymentDetailsObject4;
    private static PaymentDetailsObject paymentDetailsObject5;
    private static PaymentDetailsObject paymentDetailsObject6;
    private static PaymentDetailsObject paymentDetailsObject7;


    @BeforeAll
    static void setupData() {

        client1 = getRandomVantageClientAllFields();
        paymentEventsObject1 = generatePaymentEventsObject(client1);
        paymentDetailsObject1 = generatePaymentDetailsObject(paymentEventsObject1, client1);
        postPaymentDecisionBody1 = createPostDecisionsRequestBody();

        client2 = getRandomVantageClientAllFields();
        paymentEventsObject2 = generatePaymentEventsObject(client2);
        paymentDetailsObject2 = generatePaymentDetailsObject(paymentEventsObject2, client2);
        postPaymentDecisionBody2 = createPostDecisionsRequestBody();
        postPaymentDecisionBody3 = createPostDecisionsRequestBody();

        client3 = getRandomVantageClientAllFields();
        paymentEventsObject3 = generatePaymentEventsObject(client3);
        paymentDetailsObject3 = generatePaymentDetailsObject(paymentEventsObject3, client3);
        postPaymentDecisionBody3 = createPostDecisionsRequestBody(true);

        client4 = getRandomVantageClientAllFields();
        paymentEventsObject4 = generatePaymentEventsObject(client4);
        paymentDetailsObject4 = generatePaymentDetailsObject(paymentEventsObject4, client4);
        postPaymentDecisionBody4 = createPostDecisionsRequestBody();

        client5 = getRandomVantageClientAllFields();
        paymentEventsObject5 = generatePaymentEventsObject(client5);
        paymentDetailsObject5 = generatePaymentDetailsObject(paymentEventsObject5, client5);
        postPaymentDecisionBody5 = createPostDecisionsRequestBody();

        client6 = getRandomVantageClientAllFields();
        paymentEventsObject6 = generatePaymentEventsObject(client6);
        paymentDetailsObject6 = generatePaymentDetailsObject(paymentEventsObject6, client6);
        postPaymentDecisionBody6 = createPostDecisionsRequestBody();

        client7 = getRandomVantageClientAllFields();
        paymentEventsObject7 = generatePaymentEventsObject(client7);
        paymentDetailsObject7 = generatePaymentDetailsObject(paymentEventsObject7, client7);
        postPaymentDecisionBody7 = createPostDecisionsRequestBody();

        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE, List.of(
                paymentEventsObject1, paymentEventsObject2, paymentEventsObject3, paymentEventsObject4, paymentEventsObject5, paymentEventsObject6, paymentEventsObject7));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE, List.of(
                paymentDetailsObject1, paymentDetailsObject2, paymentDetailsObject3, paymentDetailsObject4, paymentDetailsObject5, paymentDetailsObject6, paymentDetailsObject7));
    }

    @AfterAll
    static void deleteData() throws Exception {
        cleanPaymentGateData(client1.getUcid(), client1.getUserId(), paymentEventsObject1.getPaymentId().toString());
        cleanPaymentGateData(client2.getUcid(), client2.getUserId(), paymentEventsObject2.getPaymentId().toString());
        cleanPaymentGateData(client3.getUcid(), client3.getUserId(), paymentEventsObject3.getPaymentId().toString());
        cleanPaymentGateData(client4.getUcid(), client4.getUserId(), paymentEventsObject4.getPaymentId().toString());
        cleanPaymentGateData(client5.getUcid(), client5.getUserId(), paymentEventsObject5.getPaymentId().toString());
        cleanPaymentGateData(client6.getUcid(), client6.getUserId(), paymentEventsObject6.getPaymentId().toString());
        cleanPaymentGateData(client7.getUcid(), client7.getUserId(), paymentEventsObject7.getPaymentId().toString());
    }

    @Test
    @AllureId("1597")
    @DisplayName("Post Decisions V1. Success test. 201")
    void PostPaymentDecisionV1Test1() throws Exception {

        Allure.step("send post decisions request with valid data");
        Response response = postDecisions(paymentEventsObject1.getPaymentId().toString(), List.of(postPaymentDecisionBody1));
        assertThat(response.code(), is(201));

        Allure.step("Validate Data in response");
        PostDecisionsResponseBody[] mappedResponse = objectMapper.readValue(response.body().string(), PostDecisionsResponseBody[].class);
        assertThat("Check response", mappedResponse[0].getDecisionId(), is(instanceOf(Integer.class)));
        assertThat("Check response", mappedResponse[0].getPaymentId(), is(paymentEventsObject1.getPaymentId().toString()));
        assertThat("Check response", mappedResponse[0].getDecisionType(), is(postPaymentDecisionBody1.getDecisionType()));
        assertThat("Check response", mappedResponse[0].getDecisionCode(), is(postPaymentDecisionBody1.getDecisionCode()));
        assertThat("Check response", mappedResponse[0].getDecidedAt(), is(postPaymentDecisionBody1.getDecidedAt()));

        List<PaymentDecisionsObject> dbObject = getObjectsFromDB(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DECISIONS_TABLE, String.format("payment_id = '%s'", paymentEventsObject1.getPaymentId().toString()), PaymentDecisionsObject.class);
        assertThat("Check db object", dbObject.getFirst().getPaymentId(), is(paymentEventsObject1.getPaymentId()));
        assertThat("Check db object", dbObject.getFirst().getDecisionType(), is(mappedResponse[0].getDecisionType()));
        assertThat("Check db object", dbObject.getFirst().getDecisionCode(), is(mappedResponse[0].getDecisionCode()));
        assertThat("Check db object", dbObject.getFirst().getRejectionCode(), is(nullValue()));
        assertThat("Check db object", dbObject.getFirst().getActor(), is("Rule engine"));
        assertThat("Check db object", dbObject.getFirst().getDateCreated(), is(notNullValue()));
        assertThat("Check db object", dbObject.getFirst().getDateUpdated(), is(notNullValue()));
        assertThat("Check db object", dbObject.getFirst().getDateDecided(), is(notNullValue()));
    }

    @Test
    @AllureId("1596")
    @DisplayName("Post Decisions V1. Success batch test. 201")
    void PostPaymentDecisionV1Test2() throws Exception {

        Allure.step("send post decisions request with valid data");
        postPaymentDecisionBody3.setDecisionCode(0);
        postPaymentDecisionBody3.setDecisionType("risk");
        Response response = postDecisions(paymentEventsObject2.getPaymentId().toString(), List.of(postPaymentDecisionBody2, postPaymentDecisionBody3));
        assertThat(response.code(), is(201));

        Allure.step("Validate Data in response");
        PostDecisionsResponseBody[] mappedResponse = objectMapper.readValue(response.body().string(), PostDecisionsResponseBody[].class);
        assertThat("Check length", mappedResponse.length, is(2));

        List<PaymentDecisionsObject> dbObject = getPaymentDecisionsByPaymentId(paymentEventsObject2.getPaymentId());
        assertThat("Check db object size", dbObject.size(), is(2));
    }

    @Test
    @AllureId("1595")
    @DisplayName("Post Decisions V1. Bad Request. 400")
    void PostPaymentDecisionV1Test3() throws Exception {

        Allure.step("send post decisions request with valid data");
        postPaymentDecisionBody4.setDecisionType(null);
        Response response = postDecisions(paymentEventsObject4.getPaymentId().toString(), List.of(postPaymentDecisionBody4));
        assertThat(response.code(), is(400));

        Allure.step("Validate Data in response");
        PostDecisionsResponseBody mappedResponse = objectMapper.readValue(response.body().string(), PostDecisionsResponseBody.class);
        assertThat("Check response", mappedResponse.getError(), is("validation_error"));
        assertThat("Check response", mappedResponse.getMessage(), containsString("createPaymentDecisions.paymentDecisionRequest[0].decisionType: must not be null"));
    }

    @Test
    @AllureId("1594")
    @DisplayName("Post Decisions V1. Conflict. 409")
    void PostPaymentDecisionV1Test4() throws Exception {

        Allure.step("send post decisions request with valid data");
        Response response = postDecisions(paymentEventsObject4.getPaymentId().toString(), List.of(postPaymentDecisionBody5, postPaymentDecisionBody5));
        assertThat(response.code(), is(409));

        Allure.step("Validate Data in response");
        PostDecisionsResponseBody mappedResponse = objectMapper.readValue(response.body().string(), PostDecisionsResponseBody.class);
        assertThat("Check response", mappedResponse.getError(), is("conflict"));
        assertThat("Check response", mappedResponse.getMessage(), containsString("could not execute statement"));
    }

    @Test
    @AllureId("1700")
    @DisplayName("Post Decisions V1. Success test with reject. 201")
    void PostPaymentDecisionV1Test5() throws Exception {

        Allure.step("send post decisions request with valid data");
        Response response = postDecisions(paymentEventsObject3.getPaymentId().toString(), List.of(postPaymentDecisionBody3));
        assertThat(response.code(), is(201));

        Allure.step("Validate Data in response");
        PostDecisionsResponseBody[] mappedResponse = objectMapper.readValue(response.body().string(), PostDecisionsResponseBody[].class);
        assertThat("Check response", mappedResponse[0].getDecisionId(), is(instanceOf(Integer.class)));
        assertThat("Check response", mappedResponse[0].getPaymentId(), is(paymentEventsObject3.getPaymentId().toString()));
        assertThat("Check response", mappedResponse[0].getDecisionType(), is(postPaymentDecisionBody3.getDecisionType()));
        assertThat("Check response", mappedResponse[0].getDecisionCode(), is(postPaymentDecisionBody3.getDecisionCode()));
        assertThat("Check response", mappedResponse[0].getDecidedAt(), is(postPaymentDecisionBody3.getDecidedAt()));

        List<PaymentDecisionsObject> dbObject = getObjectsFromDB(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DECISIONS_TABLE, String.format("payment_id = '%s'", paymentEventsObject3.getPaymentId().toString()), PaymentDecisionsObject.class);
        assertThat("Check db object", dbObject.getFirst().getId(), is(instanceOf(Integer.class)));
        assertThat("Check db object", dbObject.getFirst().getPaymentId(), is(paymentEventsObject3.getPaymentId()));
        assertThat("Check db object", dbObject.getFirst().getDecisionType(), is(mappedResponse[0].getDecisionType()));
        assertThat("Check db object", dbObject.getFirst().getDecisionCode(), is(mappedResponse[0].getDecisionCode()));
        assertThat("Check db object", dbObject.getFirst().getRejectionCode(), is(0));
        assertThat("Check db object", dbObject.getFirst().getActor(), is("Rule engine"));
        assertThat("Check db object", dbObject.getFirst().getDateCreated(), is(notNullValue()));
        assertThat("Check db object", dbObject.getFirst().getDateUpdated(), is(notNullValue()));
        assertThat("Check db object", dbObject.getFirst().getDateDecided(), is(notNullValue()));
        assertThat("Check db object", dbObject.getFirst().getReasonString(), is(instanceOf(String.class)));

        List<PaymentRejectionAttributesObject> attributes = getObjectsFromDB(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_REJECTION_ATTRIBUTES_TABLE, String.format("payment_id = '%s'", paymentEventsObject3.getPaymentId().toString()), PaymentRejectionAttributesObject.class);
        assertThat("Check attribute object", attributes.getFirst().getPaymentId(), is(paymentEventsObject3.getPaymentId()));
        assertThat("Check attribute object", attributes.getFirst().getDecisionId(), is(dbObject.getFirst().getId()));
        assertThat("Check attribute object", attributes.getFirst().getId(), is(instanceOf(Integer.class)));
        assertThat("Check attribute object", attributes.getFirst().getAttributeId(), is(instanceOf(Integer.class)));
        assertThat("Check attribute object", attributes.getFirst().getAttributeValue(), is("Passport"));
        assertThat("Check attribute object", attributes.getFirst().getDateCreated(), is(instanceOf(Timestamp.class)));
        assertThat("Check attribute object", attributes.getFirst().getDateUpdated(), is(instanceOf(Timestamp.class)));
    }
}
