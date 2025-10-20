package tests.payment_gate_service_tests;

import business_objects.api.payment_gate.payments_decisions.PutDecisionsRequestBody;
import business_objects.api.payment_gate.payments_decisions.PutDecisionsResponseBody;
import business_objects.db.payment_gate.payment_decisions.PaymentDecisionsObject;
import business_objects.db.payment_gate.payment_details.PaymentDetailsObject;
import business_objects.db.payment_gate.payment_events.PaymentEventsObject;
import helpers.data.ClientHelper;
import helpers.database.DbName;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;

import java.util.List;

import static business_objects.api.payment_gate.payments_decisions.DecisionsRequests.putDecisions;
import static business_objects.api.payment_gate.payments_decisions.PaymentsRequestBodyFactory.createPutDecisionsRequestBody;
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
@Story(STORY_PAYMENT_GATE_PUT_DECISIONS)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_PAYMENT_GATE_TESTS)
class PutDecisionsV1Tests extends TestBaseApi {
    private static ClientHelper client1;
    private static ClientHelper client2;
    private static ClientHelper client3;
    private static ClientHelper client4;
    private static ClientHelper client5;
    private static PutDecisionsRequestBody putPaymentDecisionBody1;
    private static PutDecisionsRequestBody putPaymentDecisionBody2;
    private static PutDecisionsRequestBody putPaymentDecisionBody3;
    private static PutDecisionsRequestBody putPaymentDecisionBody4;
    private static PutDecisionsRequestBody putPaymentDecisionBody5;
    private static PaymentEventsObject paymentEventsObject1;
    private static PaymentEventsObject paymentEventsObject2;
    private static PaymentEventsObject paymentEventsObject3;
    private static PaymentEventsObject paymentEventsObject4;
    private static PaymentEventsObject paymentEventsObject5;
    private static PaymentDetailsObject paymentDetailsObject1;
    private static PaymentDetailsObject paymentDetailsObject2;
    private static PaymentDetailsObject paymentDetailsObject3;
    private static PaymentDetailsObject paymentDetailsObject4;
    private static PaymentDetailsObject paymentDetailsObject5;


    @BeforeAll
    static void setupData() {

        client1 = getRandomVantageClientAllFields();
        paymentEventsObject1 = generatePaymentEventsObject(client1);
        paymentDetailsObject1 = generatePaymentDetailsObject(paymentEventsObject1, client1);
        putPaymentDecisionBody1 = createPutDecisionsRequestBody();

        client2 = getRandomVantageClientAllFields();
        paymentEventsObject2 = generatePaymentEventsObject(client2);
        paymentDetailsObject2 = generatePaymentDetailsObject(paymentEventsObject2, client2);
        putPaymentDecisionBody2 = createPutDecisionsRequestBody();
        putPaymentDecisionBody3 = createPutDecisionsRequestBody();

        client3 = getRandomVantageClientAllFields();
        paymentEventsObject3 = generatePaymentEventsObject(client3);
        paymentDetailsObject3 = generatePaymentDetailsObject(paymentEventsObject3, client3);
        putPaymentDecisionBody3 = createPutDecisionsRequestBody();

        client4 = getRandomVantageClientAllFields();
        paymentEventsObject4 = generatePaymentEventsObject(client4);
        paymentDetailsObject4 = generatePaymentDetailsObject(paymentEventsObject4, client4);
        putPaymentDecisionBody4 = createPutDecisionsRequestBody();

        client5 = getRandomVantageClientAllFields();
        paymentEventsObject5 = generatePaymentEventsObject(client5);
        paymentDetailsObject5 = generatePaymentDetailsObject(paymentEventsObject5, client5);
        putPaymentDecisionBody5 = createPutDecisionsRequestBody();

        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE, List.of(paymentEventsObject1, paymentEventsObject2, paymentEventsObject3, paymentEventsObject4, paymentEventsObject5));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE, List.of(paymentDetailsObject1, paymentDetailsObject2, paymentDetailsObject3, paymentDetailsObject4, paymentDetailsObject5));
    }

    @AfterAll
    static void deleteData() throws Exception {
        cleanPaymentGateData(client1.getUcid(), client1.getUserId(), paymentEventsObject1.getPaymentId().toString());
        cleanPaymentGateData(client2.getUcid(), client2.getUserId(), paymentEventsObject2.getPaymentId().toString());
        cleanPaymentGateData(client3.getUcid(), client3.getUserId(), paymentEventsObject3.getPaymentId().toString());
        cleanPaymentGateData(client4.getUcid(), client4.getUserId(), paymentEventsObject4.getPaymentId().toString());
        cleanPaymentGateData(client5.getUcid(), client5.getUserId(), paymentEventsObject5.getPaymentId().toString());
    }

    @Test
    @AllureId("1598")
    @DisplayName("Put Decisions V1. Success test. 201")
    void PutPaymentDecisionV1Test1() throws Exception {

        Allure.step("send post decisions request with valid data");
        Response response = putDecisions(paymentEventsObject1.getPaymentId().toString(), List.of(putPaymentDecisionBody1));
        assertThat(response.code(), is(201));

        Allure.step("Validate Data in response");
        PutDecisionsResponseBody[] mappedResponse = objectMapper.readValue(response.body().string(), PutDecisionsResponseBody[].class);
        assertThat("Check response", mappedResponse[0].getDecisionId(), is(instanceOf(Integer.class)));
        assertThat("Check response", mappedResponse[0].getPaymentId(), is(paymentEventsObject1.getPaymentId().toString()));
        assertThat("Check response", mappedResponse[0].getDecisionType(), is(putPaymentDecisionBody1.getDecisionType()));
        assertThat("Check response", mappedResponse[0].getDecisionCode(), is(putPaymentDecisionBody1.getDecisionCode()));
        assertThat("Check response", mappedResponse[0].getDecidedAt(), is(putPaymentDecisionBody1.getDecidedAt()));

        List<PaymentDecisionsObject> dbObject = getObjectsFromDB(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DECISIONS_TABLE, String.format("payment_id = '%s'", paymentEventsObject1.getPaymentId().toString()), PaymentDecisionsObject.class);
        PaymentDecisionsObject decisionsObject = dbObject.getFirst();
        assertThat("Check db object", decisionsObject.getPaymentId(), is(paymentEventsObject1.getPaymentId()));
        assertThat("Check db object", decisionsObject.getDecisionType(), is(mappedResponse[0].getDecisionType()));
        assertThat("Check db object", decisionsObject.getDecisionCode(), is(mappedResponse[0].getDecisionCode()));
        assertThat("Check db object", decisionsObject.getRejectionCode(), is(nullValue()));
        assertThat("Check db object", decisionsObject.getDateCreated(), is(notNullValue()));
        assertThat("Check db object", decisionsObject.getDateUpdated(), is(notNullValue()));
        assertThat("Check db object", decisionsObject.getDateDecided(), is(notNullValue()));
        assertThat("Check db object", decisionsObject.getActor(), is("Vindex BO"));
    }

    @Test
    @AllureId("1599")
    @DisplayName("Put Decisions V1. Success batch test. 201")
    void PutPaymentDecisionV1Test2() throws Exception {

        Allure.step("send post decisions request with valid data");
        putPaymentDecisionBody3.setDecisionCode(0);
        putPaymentDecisionBody3.setDecisionType("risk");
        Response response = putDecisions(paymentEventsObject2.getPaymentId().toString(), List.of(putPaymentDecisionBody2, putPaymentDecisionBody3));
        assertThat(response.code(), is(201));

        Allure.step("Validate Data in response");
        PutDecisionsResponseBody[] mappedResponse = objectMapper.readValue(response.body().string(), PutDecisionsResponseBody[].class);
        assertThat("Check length", mappedResponse.length, is(2));

        List<PaymentDecisionsObject> dbObject = getPaymentDecisionsByPaymentId(paymentEventsObject2.getPaymentId());
        assertThat("Check db object size", dbObject.size(), is(2));
    }

    @Test
    @AllureId("1600")
    @DisplayName("Put Decisions V1. Bad Request. 400")
    void PutPaymentDecisionV1Test3() throws Exception {

        Allure.step("send post decisions request with valid data");
        putPaymentDecisionBody4.setDecisionType(null);
        Response response = putDecisions(paymentEventsObject4.getPaymentId().toString(), List.of(putPaymentDecisionBody4));
        assertThat(response.code(), is(400));

        Allure.step("Validate Data in response");
        PutDecisionsResponseBody mappedResponse = objectMapper.readValue(response.body().string(), PutDecisionsResponseBody.class);
        assertThat("Check response", mappedResponse.getError(), is("validation_error"));
        assertThat("Check response", mappedResponse.getMessage(), containsString("must not be null"));
    }

    @Test
    @AllureId("1601")
    @DisplayName("Put Decisions V1. Conflict. 409")
    void PutPaymentDecisionV1Test4() throws Exception {

        Allure.step("send post decisions request with valid data");
        Response response = putDecisions(paymentEventsObject4.getPaymentId().toString(), List.of(putPaymentDecisionBody5, putPaymentDecisionBody5));
        assertThat(response.code(), is(409));

        Allure.step("Validate Data in response");
        PutDecisionsResponseBody mappedResponse = objectMapper.readValue(response.body().string(), PutDecisionsResponseBody.class);
        assertThat("Check response", mappedResponse.getError(), is("conflict"));
        assertThat("Check response", mappedResponse.getMessage(), containsString("could not execute statement"));
    }
}
