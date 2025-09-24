package tests.payment_gate_service_tests;

import business_objects.api.payment_gate.rule_executions.GetExecutionsResponseBody;
import business_objects.api.payment_gate.rule_executions.PostRuleExecutionsBody;
import business_objects.db.payment_gate.payment_details.PaymentDetailsObject;
import business_objects.db.payment_gate.payment_events.PaymentEventsObject;
import business_objects.db.payment_gate.payment_rule_executions.PaymentRuleExecutionsObject;
import helpers.data.ClientHelper;
import helpers.database.DbName;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static business_objects.api.payment_gate.rule_executions.RuleExecutionsRequestBodyFactory.generatePostRuleExecutionsBody;
import static business_objects.api.payment_gate.rule_executions.RuleExecutionsRequests.getRuleExecutionsRequest;
import static business_objects.db.payment_gate.payment_details.PaymentDetailsObjectFactory.generatePaymentDetailsObject;
import static business_objects.db.payment_gate.payment_events.PaymentEventsObjectFactory.generatePaymentEventsObject;
import static business_objects.db.payment_gate.payment_rule_executions.PaymentRuleExecutionsObjectFactory.generatePaymentRuleExecutionsObject;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.CleanTableHelper.cleanPaymentGateData;
import static helpers.database.DbHelper.insertObjectsToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

@Feature(FEATURE_PAYMENT_GATE)
@Story(STORY_PAYMENT_GATE_GET_RULE_EXECUTIONS)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_PAYMENT_GATE_TESTS)
class GetRuleExecutionsV1Tests extends TestBaseApi {
    private static ClientHelper client1;
    private static ClientHelper client2;
    private static ClientHelper client3;
    private static ClientHelper client4;
    private static PostRuleExecutionsBody postRuleExecutionsBody1;
    private static PostRuleExecutionsBody postRuleExecutionsBody2;
    private static PostRuleExecutionsBody postRuleExecutionsBody3;
    private static PostRuleExecutionsBody postRuleExecutionsBody4;
    private static PaymentEventsObject paymentEventsObject1;
    private static PaymentEventsObject paymentEventsObject2;
    private static PaymentEventsObject paymentEventsObject3;
    private static PaymentEventsObject paymentEventsObject4;
    private static PaymentDetailsObject paymentDetailsObject1;
    private static PaymentDetailsObject paymentDetailsObject2;
    private static PaymentDetailsObject paymentDetailsObject3;
    private static PaymentDetailsObject paymentDetailsObject4;
    private static PaymentRuleExecutionsObject paymentRuleExecutionsObject1;

    @BeforeAll
    static void setupData() {

        client1 = getRandomVantageClientAllFields();
        paymentEventsObject1 = generatePaymentEventsObject(client1);
        paymentDetailsObject1 = generatePaymentDetailsObject(paymentEventsObject1, client1);
        postRuleExecutionsBody1 = generatePostRuleExecutionsBody(paymentEventsObject1);
        paymentRuleExecutionsObject1 = generatePaymentRuleExecutionsObject(paymentEventsObject1);

        client2 = getRandomVantageClientAllFields();
        paymentEventsObject2 = generatePaymentEventsObject(client2);
        paymentDetailsObject2 = generatePaymentDetailsObject(paymentEventsObject2, client2);
        postRuleExecutionsBody2 = generatePostRuleExecutionsBody(paymentEventsObject2);

        client3 = getRandomVantageClientAllFields();
        paymentEventsObject3 = generatePaymentEventsObject(client3);
        paymentDetailsObject3 = generatePaymentDetailsObject(paymentEventsObject3, client3);
        postRuleExecutionsBody3 = generatePostRuleExecutionsBody(paymentEventsObject3);

        client4 = getRandomVantageClientAllFields();
        paymentEventsObject4 = generatePaymentEventsObject(client4);
        paymentDetailsObject4 = generatePaymentDetailsObject(paymentEventsObject4, client4);
        postRuleExecutionsBody4 = generatePostRuleExecutionsBody(paymentEventsObject4, true);

        insertObjectsToDb(DbName.PAYMENT_GATE, PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE, List.of(paymentEventsObject1, paymentEventsObject2, paymentEventsObject3, paymentEventsObject4));
        insertObjectsToDb(DbName.PAYMENT_GATE, PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE, List.of(paymentDetailsObject1, paymentDetailsObject2, paymentDetailsObject3, paymentDetailsObject4));
        insertObjectsToDb(DbName.PAYMENT_GATE, PAYMENT_GATEWAY_PAYMENT_RULE_EXECUTIONS_TABLE, List.of(paymentRuleExecutionsObject1));
    }

    @AfterAll
    static void deleteData() throws Exception {
        cleanPaymentGateData(client1.getUcid(), client1.getUserId(), postRuleExecutionsBody1.getPaymentId().toString());
        cleanPaymentGateData(client2.getUcid(), client2.getUserId(), postRuleExecutionsBody2.getPaymentId().toString());
        cleanPaymentGateData(client3.getUcid(), client3.getUserId(), postRuleExecutionsBody3.getPaymentId().toString());
        cleanPaymentGateData(client4.getUcid(), client4.getUserId(), postRuleExecutionsBody4.getPaymentId().toString());
    }

    @Test
    @AllureId("1585")
    @DisplayName("Get rule execution V1. Success test. 200")
    void GetRuleExecutionsV1Test1() throws Exception {

        Allure.step("send get payment request with valid data");
        Map<String, Object> paramsMap = new HashMap<>();
        Response response = getRuleExecutionsRequest(postRuleExecutionsBody1.getPaymentId().toString(), paramsMap);
        assertThat(response.code(), is(200));

        Allure.step("Validate Data in response");
        GetExecutionsResponseBody mappedResponse = objectMapper.readValue(response.body().string(), GetExecutionsResponseBody.class);
        assertThat("Assert paymentId present", mappedResponse.getPaymentId(), is(paymentRuleExecutionsObject1.getPaymentId()));
        // Additional asserts based on the sample response structure
        assertThat("Assert items list present", mappedResponse.getItems(), is(notNullValue()));
        GetExecutionsResponseBody.Item item = mappedResponse.getItems().getFirst();
        assertThat("Assert item.id present", item.getId(), is(paymentRuleExecutionsObject1.getId()));
        assertThat("Assert item.runId present", item.getRunId(), not(paymentRuleExecutionsObject1.getRunId()));
        assertThat("Assert item.paymentId equals response paymentId", item.getPaymentId(), is(paymentRuleExecutionsObject1.getPaymentId().toString()));
        assertThat("Assert item.ruleId present", item.getRuleId(), is(paymentRuleExecutionsObject1.getRuleId()));
        assertThat("Assert item.ruleVersion present", item.getRuleVersion(), is(paymentRuleExecutionsObject1.getRuleVersion()));
        assertThat("Assert item.ruleEndId present", item.getRuleEndId(), is(paymentRuleExecutionsObject1.getRuleEndId()));
        assertThat("Assert item.startedAt present", item.getStartedAt(), not(paymentRuleExecutionsObject1.getDateStarted()));
        assertThat("Assert item.completedAt present", item.getCompletedAt(), not(paymentRuleExecutionsObject1.getDateCompleted()));
        assertThat("Assert item.createdAt present", item.getCreatedAt(), not(paymentRuleExecutionsObject1.getDateCreated()));
        assertThat("Assert item.updatedAt present", item.getUpdatedAt(), not(paymentRuleExecutionsObject1.getDateUpdated()));
    }

    @Test
    @AllureId("1584")
    @DisplayName("Get rule execution V1. Success test. 400")
    void GetRuleExecutionsV1Test2() throws Exception {

        Allure.step("send get payment request with valid data");
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("ruleId", "ааа");
        Response response = getRuleExecutionsRequest(postRuleExecutionsBody2.getPaymentId().toString(), paramsMap);
        assertThat(response.code(), is(400));

        Allure.step("Validate Data in response");
        GetExecutionsResponseBody mappedResponse = objectMapper.readValue(response.body().string(), GetExecutionsResponseBody.class);
        assertThat("Assert status present", mappedResponse.getStatus(), is(400));
        assertThat("Assert title is Bad Request", mappedResponse.getTitle(), is("Bad Request"));
        assertThat("Assert detail explains ruleId conversion failure", mappedResponse.getDetail(), containsString("Failed to convert 'ruleId'"));
    }

    @Test
    @AllureId("1583")
    @DisplayName("Get rule execution V1. Success test. 400")
    void GetRuleExecutionsV1Test3() throws Exception {

        Allure.step("send get payment request with valid data");
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("ruleEndId", "fff");
        Response response = getRuleExecutionsRequest(postRuleExecutionsBody2.getPaymentId().toString(), paramsMap);
        assertThat(response.code(), is(400));

        Allure.step("Validate Data in response");
        GetExecutionsResponseBody mappedResponse = objectMapper.readValue(response.body().string(), GetExecutionsResponseBody.class);
        assertThat("Assert status present", mappedResponse.getStatus(), is(400));
        assertThat("Assert title is Bad Request", mappedResponse.getTitle(), is("Bad Request"));
        assertThat("Assert detail explains ruleId conversion failure", mappedResponse.getDetail(), containsString("Failed to convert 'ruleEndId'"));
    }

    @Test
    @AllureId("1582")
    @DisplayName("Get rule execution V1. Success test. 404")
    void GetRuleExecutionsV1Test4() throws Exception {

        Allure.step("send get payment request with valid data");
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("ruleId", "");
        paramsMap.put("ruleEndId", "");
        String uuid = UUID.randomUUID().toString();
        Response response = getRuleExecutionsRequest(uuid, paramsMap);
        assertThat(response.code(), is(404));

        Allure.step("Validate Data in response");
        GetExecutionsResponseBody mappedResponse = objectMapper.readValue(response.body().string(), GetExecutionsResponseBody.class);
        assertThat("Assert status present", mappedResponse.getError(), is("not_found"));
        assertThat("Assert status present", mappedResponse.getMessage(), is(String.format("paymentId %s is not found", uuid)));
    }

}
