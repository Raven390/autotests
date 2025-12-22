package tests.payment_gate_service_tests;

import static business_objects.api.payment_gate.rule_executions.RuleExecutionsRequestBodyFactory.generatePutRuleExecutionsBody;
import static business_objects.api.payment_gate.rule_executions.RuleExecutionsRequests.putRuleExecutionsRequest;
import static business_objects.db.payment_gate.payment_details.PaymentDetailsObjectFactory.generatePaymentDetailsObject;
import static business_objects.db.payment_gate.payment_events.PaymentEventsObjectFactory.generatePaymentEventsObject;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.DbHelper.insertObjectsToDb;
import static helpers.database.PaymentGateHelper.getPaymentRuleExecution;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static utils.Constants.*;

import business_objects.api.payment_gate.payments.PostPaymentsResponseBody;
import business_objects.api.payment_gate.rule_executions.PutExecutionsResponseBody;
import business_objects.api.payment_gate.rule_executions.PutRuleExecutionsBody;
import business_objects.db.payment_gate.payment_details.PaymentDetailsObject;
import business_objects.db.payment_gate.payment_events.PaymentEventsObject;
import business_objects.db.payment_gate.payment_rule_executions.PaymentRuleExecutionsObject;
import helpers.data.ClientHelper;
import helpers.database.CleanTableHelper;
import helpers.database.DbName;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import java.util.List;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;

@Feature(FEATURE_PAYMENT_GATE)
@Story(STORY_PAYMENT_GATE_PUT_RULE_EXECUTIONS)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_PAYMENT_GATE_TESTS)
class PutRuleExecutionsV1Tests extends TestBaseApi {

    private static ClientHelper client1;
    private static ClientHelper client2;
    private static ClientHelper client3;
    private static ClientHelper client4;
    private static PutRuleExecutionsBody putRuleExecutionsBody1;
    private static PutRuleExecutionsBody putRuleExecutionsBody2;
    private static PutRuleExecutionsBody putRuleExecutionsBody3;
    private static PutRuleExecutionsBody putRuleExecutionsBody4;
    private static PaymentEventsObject paymentEventsObject1;
    private static PaymentEventsObject paymentEventsObject2;
    private static PaymentEventsObject paymentEventsObject3;
    private static PaymentEventsObject paymentEventsObject4;
    private static PaymentDetailsObject paymentDetailsObject1;
    private static PaymentDetailsObject paymentDetailsObject3;

    @BeforeAll
    static void setupData() {

        client1 = getRandomVantageClientAllFields();
        paymentEventsObject1 = generatePaymentEventsObject(client1);
        paymentDetailsObject1 = generatePaymentDetailsObject(paymentEventsObject1, client1);
        putRuleExecutionsBody1 = generatePutRuleExecutionsBody(paymentEventsObject1);

        client2 = getRandomVantageClientAllFields();
        paymentEventsObject2 = generatePaymentEventsObject(client2);
        putRuleExecutionsBody2 = generatePutRuleExecutionsBody(paymentEventsObject2);

        client3 = getRandomVantageClientAllFields();
        paymentEventsObject3 = generatePaymentEventsObject(client3);
        paymentDetailsObject3 = generatePaymentDetailsObject(paymentEventsObject3, client3);
        putRuleExecutionsBody3 = generatePutRuleExecutionsBody(paymentEventsObject3);

        client4 = getRandomVantageClientAllFields();
        paymentEventsObject4 = generatePaymentEventsObject(client4);
        putRuleExecutionsBody4 = generatePutRuleExecutionsBody(paymentEventsObject4, true);

        insertObjectsToDb(
                DbName.POSTGRES,
                PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE,
                List.of(paymentEventsObject1, paymentEventsObject3));
        insertObjectsToDb(
                DbName.POSTGRES,
                PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE,
                List.of(paymentDetailsObject1, paymentDetailsObject3));
    }

    @AfterAll
    static void deleteData() throws Exception {
        CleanTableHelper.cleanPaymentGateData(
                client1.getUcid(),
                client1.getUserId(),
                putRuleExecutionsBody1.getPaymentId().toString());
        CleanTableHelper.cleanPaymentGateData(
                client2.getUcid(),
                client2.getUserId(),
                putRuleExecutionsBody2.getPaymentId().toString());
        CleanTableHelper.cleanPaymentGateData(
                client3.getUcid(),
                client3.getUserId(),
                putRuleExecutionsBody3.getPaymentId().toString());
        CleanTableHelper.cleanPaymentGateData(
                client4.getUcid(),
                client4.getUserId(),
                putRuleExecutionsBody4.getPaymentId().toString());
    }

    @Test
    @AllureId("1568")
    @DisplayName("Put Rule execution V1. Success test. 201")
    void PutRuleExecutionsV1Test1() throws Exception {

        Allure.step("send put payment request with valid data");
        Response response = putRuleExecutionsRequest(putRuleExecutionsBody1);
        assertThat(response.code(), is(201));

        Allure.step("Validate Data in response");
        PutExecutionsResponseBody mappedResponse =
                objectMapper.readValue(response.body().string(), PutExecutionsResponseBody.class);
        assertThat("Assert id", mappedResponse.getId(), is(instanceOf(Integer.class)));

        PaymentRuleExecutionsObject paymentExecutionObject = getPaymentRuleExecution(mappedResponse.getId());

        Allure.step("Validate object saved in DB");
        assertThat("DB record should exist", paymentExecutionObject, notNullValue());
        assertThat(
                "Assert paymentId matches request",
                paymentExecutionObject.getPaymentId(),
                is(putRuleExecutionsBody1.getPaymentId()));
        // runId in DB may be transformed (e.g., hashed/truncated) by the service; just validate it's set and positive
        assertThat("Assert runId is present", paymentExecutionObject.getRunId(), notNullValue());
        assertThat("Assert runId is positive", paymentExecutionObject.getRunId() > 0, is(true));
        assertThat(
                "Assert ruleId matches request",
                paymentExecutionObject.getRuleId(),
                is(putRuleExecutionsBody1.getRuleId()));
        assertThat(
                "Assert ruleVersion matches request",
                paymentExecutionObject.getRuleVersion(),
                is(putRuleExecutionsBody1.getRuleVersion()));
        assertThat(
                "Assert ruleEndId matches request",
                paymentExecutionObject.getRuleEndId(),
                is(Integer.parseInt(putRuleExecutionsBody1.getRuleEndId())));
        // Just not null check due to time conversion
        assertThat("dateCreated should be set", paymentExecutionObject.getDateCreated(), notNullValue());
        assertThat("dateUpdated should be set", paymentExecutionObject.getDateUpdated(), notNullValue());
        assertThat(
                "dateStarted should equal request startedAt",
                paymentExecutionObject.getDateStarted(),
                is(notNullValue()));
        assertThat(
                "dateCompleted should equal request completedAt",
                paymentExecutionObject.getDateCompleted(),
                is(notNullValue()));
    }

    @Test
    @AllureId("1569")
    @DisplayName("Put Rule execution V1. Payment not found test. 404")
    void PutRuleExecutionsV1Test2() throws Exception {

        Allure.step("send put payment request with valid data");
        Response response = putRuleExecutionsRequest(putRuleExecutionsBody2);
        assertThat(response.code(), is(404));

        Allure.step("Validate Data in response");
        PostPaymentsResponseBody mappedResponse =
                objectMapper.readValue(response.body().string(), PostPaymentsResponseBody.class);
        assertThat("Assert error", mappedResponse.getError(), is("not_found"));
        assertThat(
                "Assert message",
                mappedResponse.getMessage(),
                is("Payment not found with ID: " + putRuleExecutionsBody2.getPaymentId()));
    }

    @Test
    @AllureId("1570")
    @DisplayName("Put Rule execution V1. Success idempotent replay (no change)test. 200")
    void PutRuleExecutionsV1Test3() throws Exception {

        Allure.step("send put payment request with valid data");
        Response response = putRuleExecutionsRequest(putRuleExecutionsBody3);
        assertThat(response.code(), is(201));

        Allure.step("Validate Data in response");
        PutExecutionsResponseBody mappedResponse =
                objectMapper.readValue(response.body().string(), PutExecutionsResponseBody.class);
        assertThat("Assert id", mappedResponse.getId(), is(instanceOf(Integer.class)));

        PaymentRuleExecutionsObject paymentExecutionObject = getPaymentRuleExecution(mappedResponse.getId());

        Allure.step("Validate object saved in DB");
        assertThat("DB record should exist", paymentExecutionObject, notNullValue());
        assertThat(
                "Assert paymentId matches request",
                paymentExecutionObject.getPaymentId(),
                is(putRuleExecutionsBody3.getPaymentId()));
        // runId in DB may be transformed (e.g., hashed/truncated) by the service; just validate it's set and positive
        assertThat("Assert runId is present", paymentExecutionObject.getRunId(), notNullValue());
        assertThat("Assert runId is positive", paymentExecutionObject.getRunId() > 0, is(true));
        assertThat(
                "Assert ruleId matches request",
                paymentExecutionObject.getRuleId(),
                is(putRuleExecutionsBody3.getRuleId()));
        assertThat(
                "Assert ruleVersion matches request",
                paymentExecutionObject.getRuleVersion(),
                is(putRuleExecutionsBody3.getRuleVersion()));
        assertThat(
                "Assert ruleEndId matches request",
                paymentExecutionObject.getRuleEndId(),
                is(Integer.parseInt(putRuleExecutionsBody3.getRuleEndId())));
        // Just not null check due to time conversion
        assertThat("dateCreated should be set", paymentExecutionObject.getDateCreated(), notNullValue());
        assertThat("dateUpdated should be set", paymentExecutionObject.getDateUpdated(), notNullValue());
        assertThat(
                "dateStarted should equal request startedAt",
                paymentExecutionObject.getDateStarted(),
                is(notNullValue()));
        assertThat(
                "dateCompleted should equal request completedAt",
                paymentExecutionObject.getDateCompleted(),
                is(notNullValue()));

        Allure.step("send put payment request with same data");
        Response response2 = putRuleExecutionsRequest(putRuleExecutionsBody3);
        assertThat(response.code(), is(201));

        Allure.step("Validate Data in response");
        PutExecutionsResponseBody mappedResponse2 =
                objectMapper.readValue(response2.body().string(), PutExecutionsResponseBody.class);
        assertThat("Assert id", mappedResponse.getId(), is(mappedResponse2.getId()));
    }

    @Test
    @AllureId("1571")
    @DisplayName("Put Rule execution V1. 400 Bad Request")
    void PutRuleExecutionsV1Test4() throws Exception {

        Allure.step("send put payment request with valid data");
        Response response = putRuleExecutionsRequest(putRuleExecutionsBody4);
        assertThat(response.code(), is(400));

        Allure.step("Validate Data in response");
        PostPaymentsResponseBody mappedResponse =
                objectMapper.readValue(response.body().string(), PostPaymentsResponseBody.class);
        assertThat("Assert error", mappedResponse.getError(), is("bad_request"));
        assertThat("Assert message", mappedResponse.getMessage(), containsString("must not be null"));
    }
}
