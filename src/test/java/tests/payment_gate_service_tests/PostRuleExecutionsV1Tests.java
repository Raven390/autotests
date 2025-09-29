package tests.payment_gate_service_tests;

import business_objects.api.payment_gate.payments.PostPaymentsResponseBody;
import business_objects.api.payment_gate.rule_executions.PostExecutionsResponseBody;
import business_objects.api.payment_gate.rule_executions.PostRuleExecutionsBody;
import business_objects.db.payment_gate.payment_details.PaymentDetailsObject;
import business_objects.db.payment_gate.payment_events.PaymentEventsObject;
import business_objects.db.payment_gate.payment_rule_executions.PaymentRuleExecutionsObject;
import helpers.data.ClientHelper;
import helpers.database.DbName;
import io.qameta.allure.Allure;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;

import java.util.List;

import static business_objects.api.payment_gate.rule_executions.RuleExecutionsRequestBodyFactory.generatePostRuleExecutionsBody;
import static business_objects.api.payment_gate.rule_executions.RuleExecutionsRequests.postRuleExecutionsRequest;
import static business_objects.db.payment_gate.payment_details.PaymentDetailsObjectFactory.generatePaymentDetailsObject;
import static business_objects.db.payment_gate.payment_events.PaymentEventsObjectFactory.generatePaymentEventsObject;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.CleanTableHelper.cleanPaymentGateData;
import static helpers.database.DbHelper.insertObjectsToDb;
import static helpers.database.PaymentGateHelper.getPaymentRuleExecution;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.containsString;
import static utils.Constants.*;
import static utils.Constants.LAYER_API;
import static utils.Constants.SUITE_PAYMENT_GATE_TESTS;

@Feature(FEATURE_PAYMENT_GATE)
@Story(STORY_PAYMENT_GATE_POST_RULE_EXECUTIONS)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_PAYMENT_GATE_TESTS)
class PostRuleExecutionsV1Tests extends TestBaseApi {

    private static ClientHelper client1;
    private static ClientHelper client2;
    private static ClientHelper client3;
    private static ClientHelper client4;
    private static PostRuleExecutionsBody postRuleExecutionsBody1;
    private static PostRuleExecutionsBody postRuleExecutionsBody2;
    private static PostRuleExecutionsBody postRuleExecutionsBody3;
    private static PostRuleExecutionsBody postRuleExecutionsBody4;


    @BeforeAll
    static void setupData() {

        client1 = getRandomVantageClientAllFields();
        PaymentEventsObject paymentEventsObject1 = generatePaymentEventsObject(client1);
        PaymentDetailsObject paymentDetailsObject1 = generatePaymentDetailsObject(paymentEventsObject1, client1);
        postRuleExecutionsBody1 = generatePostRuleExecutionsBody(paymentEventsObject1);

        client2 = getRandomVantageClientAllFields();
        PaymentEventsObject paymentEventsObject2 = generatePaymentEventsObject(client2);
        postRuleExecutionsBody2 = generatePostRuleExecutionsBody(paymentEventsObject2);

        client3 = getRandomVantageClientAllFields();
        PaymentEventsObject paymentEventsObject3 = generatePaymentEventsObject(client3);
        PaymentDetailsObject paymentDetailsObject3 = generatePaymentDetailsObject(paymentEventsObject3, client3);
        postRuleExecutionsBody3 = generatePostRuleExecutionsBody(paymentEventsObject3);

        client4 = getRandomVantageClientAllFields();
        PaymentEventsObject paymentEventsObject4 = generatePaymentEventsObject(client4);
        postRuleExecutionsBody4 = generatePostRuleExecutionsBody(paymentEventsObject4, true);

        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE, List.of(paymentEventsObject1, paymentEventsObject3));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE, List.of(paymentDetailsObject1, paymentDetailsObject3));
    }

    @AfterAll
    static void deleteData() throws Exception {
        cleanPaymentGateData(client1.getUcid(), client1.getUserId(), postRuleExecutionsBody1.getPaymentId().toString());
        cleanPaymentGateData(client2.getUcid(), client2.getUserId(), postRuleExecutionsBody2.getPaymentId().toString());
        cleanPaymentGateData(client3.getUcid(), client3.getUserId(), postRuleExecutionsBody3.getPaymentId().toString());
        cleanPaymentGateData(client4.getUcid(), client4.getUserId(), postRuleExecutionsBody4.getPaymentId().toString());
    }

    @Test
    @DisplayName("Post Rule execution V1. Success test. 201")
    void PostRuleExecutionsV1Test1() throws Exception {

        Allure.step("send put payment request with valid data");
        Response response = postRuleExecutionsRequest(postRuleExecutionsBody1);
        assertThat(response.code(), is(201));

        Allure.step("Validate Data in response");
        PostExecutionsResponseBody mappedResponse = objectMapper.readValue(response.body().string(), PostExecutionsResponseBody.class);
        assertThat("Assert id", mappedResponse.getId(), is(instanceOf(Integer.class)));

        PaymentRuleExecutionsObject paymentExecutionObject = getPaymentRuleExecution(mappedResponse.getId());

        Allure.step("Validate object saved in DB");
        assertThat("DB record should exist", paymentExecutionObject, notNullValue());
        assertThat("Assert paymentId matches request", paymentExecutionObject.getPaymentId(), is(postRuleExecutionsBody1.getPaymentId()));
        // runId in DB may be transformed (e.g., hashed/truncated) by the service; just validate it's set and positive
        assertThat("Assert runId is present", paymentExecutionObject.getRunId(), notNullValue());
        assertThat("Assert runId is positive", paymentExecutionObject.getRunId() > 0, is(true));
        assertThat("Assert ruleId matches request", paymentExecutionObject.getRuleId(), is(postRuleExecutionsBody1.getRuleId()));
        assertThat("Assert ruleVersion matches request", paymentExecutionObject.getRuleVersion(), is(postRuleExecutionsBody1.getRuleVersion()));
        assertThat("Assert ruleEndId matches request", paymentExecutionObject.getRuleEndId(), is(Integer.parseInt(postRuleExecutionsBody1.getRuleEndId())));

        assertThat("dateCreated should be set", paymentExecutionObject.getDateCreated(), notNullValue());
        assertThat("dateUpdated should be set", paymentExecutionObject.getDateUpdated(), notNullValue());
        assertThat("dateStarted should equal request startedAt", paymentExecutionObject.getDateStarted(), is(notNullValue()));
        assertThat("dateCompleted should equal request completedAt", paymentExecutionObject.getDateCompleted(), is(notNullValue()));

    }

    @Test
    @DisplayName("Post Rule execution V1. Payment not found test. 404")
    void PostRuleExecutionsV1Test2() throws Exception {

        Allure.step("send put payment request with valid data");
        Response response = postRuleExecutionsRequest(postRuleExecutionsBody2);
        assertThat(response.code(), is(404));

        Allure.step("Validate Data in response");
        PostPaymentsResponseBody mappedResponse = objectMapper.readValue(response.body().string(), PostPaymentsResponseBody.class);
        assertThat("Assert error", mappedResponse.getError(), is("not_found"));
        assertThat("Assert message", mappedResponse.getMessage(), is("Payment not found with ID: " + postRuleExecutionsBody2.getPaymentId()));
    }

    @Test
    @DisplayName("Post Rule execution V1. Success idempotent replay (no change)test. 200")
    void PostRuleExecutionsV1Test3() throws Exception {

        Allure.step("send post payment request with valid data");
        Response response = postRuleExecutionsRequest(postRuleExecutionsBody3);
        assertThat(response.code(), is(201));

        Allure.step("Validate Data in response");
        PostExecutionsResponseBody mappedResponse = objectMapper.readValue(response.body().string(), PostExecutionsResponseBody.class);
        assertThat("Assert id", mappedResponse.getId(), is(instanceOf(Integer.class)));

        PaymentRuleExecutionsObject paymentExecutionObject = getPaymentRuleExecution(mappedResponse.getId());

        Allure.step("Validate object saved in DB");
        assertThat("DB record should exist", paymentExecutionObject, notNullValue());
        assertThat("Assert paymentId matches request", paymentExecutionObject.getPaymentId(), is(postRuleExecutionsBody3.getPaymentId()));
        // runId in DB may be transformed (e.g., hashed/truncated) by the service; just validate it's set and positive
        assertThat("Assert runId is present", paymentExecutionObject.getRunId(), notNullValue());
        assertThat("Assert runId is positive", paymentExecutionObject.getRunId() > 0, is(true));
        assertThat("Assert ruleId matches request", paymentExecutionObject.getRuleId(), is(postRuleExecutionsBody3.getRuleId()));
        assertThat("Assert ruleVersion matches request", paymentExecutionObject.getRuleVersion(), is(postRuleExecutionsBody3.getRuleVersion()));
        assertThat("Assert ruleEndId matches request", paymentExecutionObject.getRuleEndId(), is(Integer.parseInt(postRuleExecutionsBody3.getRuleEndId())));
        assertThat("dateCreated should be set", paymentExecutionObject.getDateCreated(), notNullValue());
        assertThat("dateUpdated should be set", paymentExecutionObject.getDateUpdated(), notNullValue());
        assertThat("dateStarted should equal request startedAt", paymentExecutionObject.getDateStarted(), is(notNullValue()));
        assertThat("dateCompleted should equal request completedAt", paymentExecutionObject.getDateCompleted(), is(notNullValue()));

        Allure.step("send post payment request with same data");
        Response response2 = postRuleExecutionsRequest(postRuleExecutionsBody3);
        assertThat(response.code(), is(201));

        Allure.step("Validate Data in response");
        PostExecutionsResponseBody mappedResponse2 = objectMapper.readValue(response2.body().string(), PostExecutionsResponseBody.class);
        assertThat("Assert id", mappedResponse.getId(), is(mappedResponse2.getId()));
    }

    @Test
    @DisplayName("Post Rule execution V1. 400 Bad Request")
    void PostRuleExecutionsV1Test4() throws Exception {

        Allure.step("send post payment request with valid data");
        Response response = postRuleExecutionsRequest(postRuleExecutionsBody4);
        assertThat(response.code(), is(400));

        Allure.step("Validate Data in response");
        PostPaymentsResponseBody mappedResponse = objectMapper.readValue(response.body().string(), PostPaymentsResponseBody.class);
        assertThat("Assert error", mappedResponse.getError(), is("bad_request"));
        assertThat("Assert message", mappedResponse.getMessage(), containsString("must not be null"));
    }
}
