package tests.payment_gate_service_tests;

import static business_objects.api.payment_gate.rule_executions.RuleExecutionsRequestBodyFactory.generatePostRuleExecutionsBody;
import static business_objects.api.payment_gate.rule_executions.RuleExecutionsRequests.getRuleExecutionsByUcidRequest;
import static business_objects.db.payment_gate.payment_details.PaymentDetailsObjectFactory.generatePaymentDetailsObject;
import static business_objects.db.payment_gate.payment_events.PaymentEventsObjectFactory.generatePaymentEventsObject;
import static business_objects.db.payment_gate.payment_rule_executions.PaymentRuleExecutionsObjectFactory.generatePaymentRuleExecutionsObject;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.CleanTableHelper.cleanPaymentGateData;
import static helpers.database.DbHelper.insertObjectsToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

import business_objects.api.payment_gate.rule_executions.GetRuleExecutionsByUcidResponseBody;
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
import java.util.*;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;

@Feature(FEATURE_PAYMENT_GATE)
@Story(STORY_PAYMENT_GATE_GET_RULE_EXECUTIONS)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_PAYMENT_GATE_TESTS)
class GetRuleExecutionsByUCIDV1Tests extends TestBaseApi {
    private static ClientHelper client1;
    private static ClientHelper client2;
    private static ClientHelper client3;
    private static ClientHelper client4;
    private static ClientHelper client5;
    private static ClientHelper client6;
    private static PostRuleExecutionsBody postRuleExecutionsBody1;
    private static PostRuleExecutionsBody postRuleExecutionsBody2;
    private static PostRuleExecutionsBody postRuleExecutionsBody3;
    private static PostRuleExecutionsBody postRuleExecutionsBody4;
    private static PostRuleExecutionsBody postRuleExecutionsBody5;
    private static PostRuleExecutionsBody postRuleExecutionsBody6;
    private static PaymentEventsObject paymentEventsObject1;
    private static PaymentEventsObject paymentEventsObject2;
    private static PaymentEventsObject paymentEventsObject3;
    private static PaymentEventsObject paymentEventsObject4;
    private static PaymentEventsObject paymentEventsObject5;
    private static PaymentEventsObject paymentEventsObject6;
    private static PaymentDetailsObject paymentDetailsObject1;
    private static PaymentDetailsObject paymentDetailsObject2;
    private static PaymentDetailsObject paymentDetailsObject3;
    private static PaymentDetailsObject paymentDetailsObject4;
    private static PaymentDetailsObject paymentDetailsObject5;
    private static PaymentDetailsObject paymentDetailsObject6;
    private static PaymentRuleExecutionsObject paymentRuleExecutionsObject1;
    private static PaymentRuleExecutionsObject paymentRuleExecutionsObject2;
    private static PaymentRuleExecutionsObject paymentRuleExecutionsObject3;
    private static PaymentRuleExecutionsObject paymentRuleExecutionsObject4;
    private static PaymentRuleExecutionsObject paymentRuleExecutionsObject5;
    private static PaymentRuleExecutionsObject paymentRuleExecutionsObject6;

    @BeforeAll
    static void setupData() {

        client1 = getRandomVantageClientAllFields();
        paymentEventsObject1 = generatePaymentEventsObject(client1);
        paymentDetailsObject1 = generatePaymentDetailsObject(paymentEventsObject1, client1);
        postRuleExecutionsBody1 = generatePostRuleExecutionsBody(paymentEventsObject1);
        paymentRuleExecutionsObject1 = generatePaymentRuleExecutionsObject(paymentEventsObject1);
        paymentRuleExecutionsObject1.setRuleEndId(100);
        paymentRuleExecutionsObject1.setRuleId(1);

        client2 = getRandomVantageClientAllFields();
        paymentEventsObject2 = generatePaymentEventsObject(client2);
        paymentDetailsObject2 = generatePaymentDetailsObject(paymentEventsObject2, client2);
        postRuleExecutionsBody2 = generatePostRuleExecutionsBody(paymentEventsObject2);
        paymentRuleExecutionsObject2 = generatePaymentRuleExecutionsObject(paymentEventsObject2);
        paymentRuleExecutionsObject2.setRuleEndId(201);
        paymentRuleExecutionsObject2.setRuleId(1);

        client3 = getRandomVantageClientAllFields();
        paymentEventsObject3 = generatePaymentEventsObject(client3);
        paymentDetailsObject3 = generatePaymentDetailsObject(paymentEventsObject3, client3);
        postRuleExecutionsBody3 = generatePostRuleExecutionsBody(paymentEventsObject3);
        paymentRuleExecutionsObject3 = generatePaymentRuleExecutionsObject(paymentEventsObject3);
        paymentRuleExecutionsObject3.setRuleEndId(202);
        paymentRuleExecutionsObject3.setRuleId(1);

        client4 = getRandomVantageClientAllFields();
        paymentEventsObject4 = generatePaymentEventsObject(client4);
        paymentDetailsObject4 = generatePaymentDetailsObject(paymentEventsObject4, client4);
        postRuleExecutionsBody4 = generatePostRuleExecutionsBody(paymentEventsObject4, true);
        paymentRuleExecutionsObject4 = generatePaymentRuleExecutionsObject(paymentEventsObject4);
        paymentRuleExecutionsObject4.setRuleEndId(299);
        paymentRuleExecutionsObject4.setRuleId(1);

        client5 = getRandomVantageClientAllFields();
        paymentEventsObject5 = generatePaymentEventsObject(client5);
        paymentDetailsObject5 = generatePaymentDetailsObject(paymentEventsObject5, client5);
        postRuleExecutionsBody5 = generatePostRuleExecutionsBody(paymentEventsObject5, true);
        paymentRuleExecutionsObject5 = generatePaymentRuleExecutionsObject(paymentEventsObject5);
        paymentRuleExecutionsObject5.setRuleEndId(300);
        paymentRuleExecutionsObject5.setRuleId(1);

        client6 = getRandomVantageClientAllFields();
        paymentEventsObject6 = generatePaymentEventsObject(client6);
        paymentDetailsObject6 = generatePaymentDetailsObject(paymentEventsObject6, client6);
        postRuleExecutionsBody6 = generatePostRuleExecutionsBody(paymentEventsObject6, true);
        paymentRuleExecutionsObject6 = generatePaymentRuleExecutionsObject(paymentEventsObject6);
        paymentRuleExecutionsObject6.setRuleEndId(399);
        paymentRuleExecutionsObject6.setRuleId(1);

        insertObjectsToDb(
                DbName.POSTGRES,
                PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE,
                List.of(
                        paymentEventsObject1,
                        paymentEventsObject2,
                        paymentEventsObject3,
                        paymentEventsObject4,
                        paymentEventsObject5,
                        paymentEventsObject6));
        insertObjectsToDb(
                DbName.POSTGRES,
                PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE,
                List.of(
                        paymentDetailsObject1,
                        paymentDetailsObject2,
                        paymentDetailsObject3,
                        paymentDetailsObject4,
                        paymentDetailsObject5,
                        paymentDetailsObject6));
        insertObjectsToDb(
                DbName.POSTGRES,
                PAYMENT_GATEWAY_PAYMENT_RULE_EXECUTIONS_TABLE,
                List.of(
                        paymentRuleExecutionsObject1,
                        paymentRuleExecutionsObject2,
                        paymentRuleExecutionsObject3,
                        paymentRuleExecutionsObject4,
                        paymentRuleExecutionsObject5,
                        paymentRuleExecutionsObject6));
    }

    @AfterAll
    static void deleteData() throws Exception {
        cleanPaymentGateData(
                client1.getUcid(),
                client1.getUserId(),
                postRuleExecutionsBody1.getPaymentId().toString());
        cleanPaymentGateData(
                client2.getUcid(),
                client2.getUserId(),
                postRuleExecutionsBody2.getPaymentId().toString());
        cleanPaymentGateData(
                client3.getUcid(),
                client3.getUserId(),
                postRuleExecutionsBody3.getPaymentId().toString());
        cleanPaymentGateData(
                client4.getUcid(),
                client4.getUserId(),
                postRuleExecutionsBody4.getPaymentId().toString());
        cleanPaymentGateData(
                client5.getUcid(),
                client5.getUserId(),
                postRuleExecutionsBody5.getPaymentId().toString());
        cleanPaymentGateData(
                client5.getUcid(),
                client5.getUserId(),
                postRuleExecutionsBody5.getPaymentId().toString());
    }

    @Test
    @AllureId("1825")
    @DisplayName("Get rule execution V1. Success test for rule_end = 100. 200")
    void GetRuleExecutionsByUCIDV1Test1() throws Exception {

        Allure.step("send get payment request with valid data");
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("ruleId", paymentRuleExecutionsObject1.getRuleId());
        paramsMap.put("ruleEndId", paymentRuleExecutionsObject1.getRuleEndId());
        paramsMap.put("dateFrom", "2025-08-27T09:31:02Z");
        paramsMap.put("ruleOutcomeEndType", "OK");
        Response response = getRuleExecutionsByUcidRequest(client1.getUcid(), paramsMap);
        assertThat(response.code(), is(200));
        assertThat(response.body(), notNullValue());
        String responseBodyString = response.peekBody(Long.MAX_VALUE).string();
        assertThat("Response body should not be an empty array", responseBodyString, not("[]"));

        Allure.step("Validate Data in response");
        List<GetRuleExecutionsByUcidResponseBody> mappedResponse = Arrays.stream(
                        objectMapper.readValue(responseBodyString, GetRuleExecutionsByUcidResponseBody[].class))
                .toList();
        assertThat(
                "Assert paymentId present",
                mappedResponse.getFirst().getPaymentId(),
                is(paymentRuleExecutionsObject1.getPaymentId()));
        // Additional asserts based on the sample response structure
        assertThat("Assert items list present", mappedResponse.getFirst().getItems(), is(notNullValue()));
        GetRuleExecutionsByUcidResponseBody.Item item =
                mappedResponse.getFirst().getItems().getFirst();
        assertThat("Assert item.runId present", item.getRunId(), not(paymentRuleExecutionsObject1.getRunId()));
        assertThat(
                "Assert item.paymentId equals response paymentId",
                item.getPaymentId(),
                is(paymentRuleExecutionsObject1.getPaymentId().toString()));
        assertThat("Assert item.ruleId present", item.getRuleId(), is(paymentRuleExecutionsObject1.getRuleId()));
        assertThat(
                "Assert item.ruleVersion present",
                item.getRuleVersion(),
                is(paymentRuleExecutionsObject1.getRuleVersion()));
        assertThat(
                "Assert item.ruleEndId present", item.getRuleEndId(), is(paymentRuleExecutionsObject1.getRuleEndId()));
        assertThat(
                "Assert item.startedAt present",
                item.getStartedAt(),
                not(paymentRuleExecutionsObject1.getDateStarted()));
        assertThat(
                "Assert item.completedAt present",
                item.getCompletedAt(),
                not(paymentRuleExecutionsObject1.getDateCompleted()));
        assertThat(
                "Assert item.createdAt present",
                item.getCreatedAt(),
                not(paymentRuleExecutionsObject1.getDateCreated()));
        assertThat(
                "Assert item.updatedAt present",
                item.getUpdatedAt(),
                not(paymentRuleExecutionsObject1.getDateUpdated()));
    }

    @Test
    @AllureId("1988")
    @DisplayName("Get rule execution V1. Success test for rule_end = 201. 200")
    void GetRuleExecutionsByUCIDV1Test2() throws Exception {

        Allure.step("send get payment request with valid data");
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("ruleId", paymentRuleExecutionsObject2.getRuleId());
        paramsMap.put("ruleEndId", paymentRuleExecutionsObject2.getRuleEndId());
        paramsMap.put("dateFrom", "2025-08-27T09:31:02Z");
        paramsMap.put("ruleOutcomeEndType", "OK");
        Response response = getRuleExecutionsByUcidRequest(client2.getUcid(), paramsMap);
        assertThat(response.code(), is(200));
        assertThat(response.body(), notNullValue());
        String responseBodyString = response.peekBody(Long.MAX_VALUE).string();
        assertThat("Response body should not be an empty array", responseBodyString, not("[]"));

        Allure.step("Validate Data in response");
        List<GetRuleExecutionsByUcidResponseBody> mappedResponse = Arrays.stream(
                        objectMapper.readValue(responseBodyString, GetRuleExecutionsByUcidResponseBody[].class))
                .toList();
        assertThat(
                "Assert paymentId present",
                mappedResponse.getFirst().getPaymentId(),
                is(paymentRuleExecutionsObject2.getPaymentId()));
        // Additional asserts based on the sample response structure
        assertThat("Assert items list present", mappedResponse.getFirst().getItems(), is(notNullValue()));
        GetRuleExecutionsByUcidResponseBody.Item item =
                mappedResponse.getFirst().getItems().getFirst();
        assertThat("Assert item.runId present", item.getRunId(), not(paymentRuleExecutionsObject2.getRunId()));
        assertThat(
                "Assert item.paymentId equals response paymentId",
                item.getPaymentId(),
                is(paymentRuleExecutionsObject2.getPaymentId().toString()));
        assertThat("Assert item.ruleId present", item.getRuleId(), is(paymentRuleExecutionsObject2.getRuleId()));
        assertThat(
                "Assert item.ruleVersion present",
                item.getRuleVersion(),
                is(paymentRuleExecutionsObject2.getRuleVersion()));
        assertThat(
                "Assert item.ruleEndId present", item.getRuleEndId(), is(paymentRuleExecutionsObject2.getRuleEndId()));
        assertThat(
                "Assert item.startedAt present",
                item.getStartedAt(),
                not(paymentRuleExecutionsObject2.getDateStarted()));
        assertThat(
                "Assert item.completedAt present",
                item.getCompletedAt(),
                not(paymentRuleExecutionsObject2.getDateCompleted()));
        assertThat(
                "Assert item.createdAt present",
                item.getCreatedAt(),
                not(paymentRuleExecutionsObject2.getDateCreated()));
        assertThat(
                "Assert item.updatedAt present",
                item.getUpdatedAt(),
                not(paymentRuleExecutionsObject2.getDateUpdated()));
    }

    @Test
    @AllureId("1989")
    @DisplayName("Get rule execution V1. Success test for rule_end = 202. 200")
    void GetRuleExecutionsByUCIDV1Test3() throws Exception {

        Allure.step("send get payment request with valid data");
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("ruleId", paymentRuleExecutionsObject3.getRuleId());
        paramsMap.put("ruleEndId", paymentRuleExecutionsObject3.getRuleEndId());
        paramsMap.put("dateFrom", "2025-08-27T09:31:02Z");
        paramsMap.put("ruleOutcomeEndType", "ALERT");
        Response response = getRuleExecutionsByUcidRequest(client3.getUcid(), paramsMap);
        assertThat(response.code(), is(200));
        assertThat(response.body(), notNullValue());
        String responseBodyString = response.peekBody(Long.MAX_VALUE).string();
        assertThat("Response body should not be an empty array", responseBodyString, not("[]"));

        Allure.step("Validate Data in response");
        List<GetRuleExecutionsByUcidResponseBody> mappedResponse = Arrays.stream(
                        objectMapper.readValue(responseBodyString, GetRuleExecutionsByUcidResponseBody[].class))
                .toList();
        assertThat(
                "Assert paymentId present",
                mappedResponse.getFirst().getPaymentId(),
                is(paymentRuleExecutionsObject3.getPaymentId()));
        // Additional asserts based on the sample response structure
        assertThat("Assert items list present", mappedResponse.getFirst().getItems(), is(notNullValue()));
        GetRuleExecutionsByUcidResponseBody.Item item =
                mappedResponse.getFirst().getItems().getFirst();
        assertThat("Assert item.runId present", item.getRunId(), not(paymentRuleExecutionsObject3.getRunId()));
        assertThat(
                "Assert item.paymentId equals response paymentId",
                item.getPaymentId(),
                is(paymentRuleExecutionsObject3.getPaymentId().toString()));
        assertThat("Assert item.ruleId present", item.getRuleId(), is(paymentRuleExecutionsObject3.getRuleId()));
        assertThat(
                "Assert item.ruleVersion present",
                item.getRuleVersion(),
                is(paymentRuleExecutionsObject3.getRuleVersion()));
        assertThat(
                "Assert item.ruleEndId present", item.getRuleEndId(), is(paymentRuleExecutionsObject3.getRuleEndId()));
        assertThat(
                "Assert item.startedAt present",
                item.getStartedAt(),
                not(paymentRuleExecutionsObject3.getDateStarted()));
        assertThat(
                "Assert item.completedAt present",
                item.getCompletedAt(),
                not(paymentRuleExecutionsObject3.getDateCompleted()));
        assertThat(
                "Assert item.createdAt present",
                item.getCreatedAt(),
                not(paymentRuleExecutionsObject3.getDateCreated()));
        assertThat(
                "Assert item.updatedAt present",
                item.getUpdatedAt(),
                not(paymentRuleExecutionsObject3.getDateUpdated()));
    }

    @Test
    @AllureId("1990")
    @DisplayName("Get rule execution V1. Success test for rule_end = 299. 200")
    void GetRuleExecutionsByUCIDV1Test4() throws Exception {

        Allure.step("send get payment request with valid data");
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("ruleId", paymentRuleExecutionsObject4.getRuleId());
        paramsMap.put("ruleEndId", paymentRuleExecutionsObject4.getRuleEndId());
        paramsMap.put("dateFrom", "2025-08-27T09:31:02Z");
        paramsMap.put("ruleOutcomeEndType", "ALERT");
        Response response = getRuleExecutionsByUcidRequest(client4.getUcid(), paramsMap);
        assertThat(response.code(), is(200));
        assertThat(response.body(), notNullValue());
        String responseBodyString = response.peekBody(Long.MAX_VALUE).string();
        assertThat("Response body should not be an empty array", responseBodyString, not("[]"));

        Allure.step("Validate Data in response");
        List<GetRuleExecutionsByUcidResponseBody> mappedResponse = Arrays.stream(
                        objectMapper.readValue(responseBodyString, GetRuleExecutionsByUcidResponseBody[].class))
                .toList();
        assertThat(
                "Assert paymentId present",
                mappedResponse.getFirst().getPaymentId(),
                is(paymentRuleExecutionsObject4.getPaymentId()));
        // Additional asserts based on the sample response structure
        assertThat("Assert items list present", mappedResponse.getFirst().getItems(), is(notNullValue()));
        GetRuleExecutionsByUcidResponseBody.Item item =
                mappedResponse.getFirst().getItems().getFirst();
        assertThat("Assert item.runId present", item.getRunId(), not(paymentRuleExecutionsObject4.getRunId()));
        assertThat(
                "Assert item.paymentId equals response paymentId",
                item.getPaymentId(),
                is(paymentRuleExecutionsObject4.getPaymentId().toString()));
        assertThat("Assert item.ruleId present", item.getRuleId(), is(paymentRuleExecutionsObject4.getRuleId()));
        assertThat(
                "Assert item.ruleVersion present",
                item.getRuleVersion(),
                is(paymentRuleExecutionsObject4.getRuleVersion()));
        assertThat(
                "Assert item.ruleEndId present", item.getRuleEndId(), is(paymentRuleExecutionsObject4.getRuleEndId()));
        assertThat(
                "Assert item.startedAt present",
                item.getStartedAt(),
                not(paymentRuleExecutionsObject4.getDateStarted()));
        assertThat(
                "Assert item.completedAt present",
                item.getCompletedAt(),
                not(paymentRuleExecutionsObject4.getDateCompleted()));
        assertThat(
                "Assert item.createdAt present",
                item.getCreatedAt(),
                not(paymentRuleExecutionsObject4.getDateCreated()));
        assertThat(
                "Assert item.updatedAt present",
                item.getUpdatedAt(),
                not(paymentRuleExecutionsObject4.getDateUpdated()));
    }

    @Test
    @AllureId("1991")
    @DisplayName("Get rule execution V1. Success test for rule_end = 300. 200")
    void GetRuleExecutionsByUCIDV1Test5() throws Exception {

        Allure.step("send get payment request with valid data");
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("ruleId", paymentRuleExecutionsObject5.getRuleId());
        paramsMap.put("ruleEndId", paymentRuleExecutionsObject5.getRuleEndId());
        paramsMap.put("dateFrom", "2025-08-27T09:31:02Z");
        paramsMap.put("ruleOutcomeEndType", "REJECT");
        Response response = getRuleExecutionsByUcidRequest(client5.getUcid(), paramsMap);
        assertThat(response.code(), is(200));
        assertThat(response.body(), notNullValue());
        String responseBodyString = response.peekBody(Long.MAX_VALUE).string();
        assertThat("Response body should not be an empty array", responseBodyString, not("[]"));

        Allure.step("Validate Data in response");
        List<GetRuleExecutionsByUcidResponseBody> mappedResponse = Arrays.stream(
                        objectMapper.readValue(responseBodyString, GetRuleExecutionsByUcidResponseBody[].class))
                .toList();
        assertThat(
                "Assert paymentId present",
                mappedResponse.getFirst().getPaymentId(),
                is(paymentRuleExecutionsObject5.getPaymentId()));
        // Additional asserts based on the sample response structure
        assertThat("Assert items list present", mappedResponse.getFirst().getItems(), is(notNullValue()));
        GetRuleExecutionsByUcidResponseBody.Item item =
                mappedResponse.getFirst().getItems().getFirst();
        assertThat("Assert item.runId present", item.getRunId(), not(paymentRuleExecutionsObject5.getRunId()));
        assertThat(
                "Assert item.paymentId equals response paymentId",
                item.getPaymentId(),
                is(paymentRuleExecutionsObject5.getPaymentId().toString()));
        assertThat("Assert item.ruleId present", item.getRuleId(), is(paymentRuleExecutionsObject5.getRuleId()));
        assertThat(
                "Assert item.ruleVersion present",
                item.getRuleVersion(),
                is(paymentRuleExecutionsObject5.getRuleVersion()));
        assertThat(
                "Assert item.ruleEndId present", item.getRuleEndId(), is(paymentRuleExecutionsObject5.getRuleEndId()));
        assertThat(
                "Assert item.startedAt present",
                item.getStartedAt(),
                not(paymentRuleExecutionsObject5.getDateStarted()));
        assertThat(
                "Assert item.completedAt present",
                item.getCompletedAt(),
                not(paymentRuleExecutionsObject5.getDateCompleted()));
        assertThat(
                "Assert item.createdAt present",
                item.getCreatedAt(),
                not(paymentRuleExecutionsObject5.getDateCreated()));
        assertThat(
                "Assert item.updatedAt present",
                item.getUpdatedAt(),
                not(paymentRuleExecutionsObject5.getDateUpdated()));
    }

    @Test
    @AllureId("1992")
    @DisplayName("Get rule execution V1. Success test for rule_end = 399. 200")
    void GetRuleExecutionsByUCIDV1Test6() throws Exception {

        Allure.step("send get payment request with valid data");
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("ruleId", paymentRuleExecutionsObject6.getRuleId());
        paramsMap.put("ruleEndId", paymentRuleExecutionsObject6.getRuleEndId());
        paramsMap.put("dateFrom", "2025-08-27T09:31:02Z");
        paramsMap.put("ruleOutcomeEndType", "REJECT");
        Response response = getRuleExecutionsByUcidRequest(client6.getUcid(), paramsMap);
        assertThat(response.code(), is(200));
        assertThat(response.body(), notNullValue());
        String responseBodyString = response.peekBody(Long.MAX_VALUE).string();
        assertThat("Response body should not be an empty array", responseBodyString, not("[]"));

        Allure.step("Validate Data in response");
        List<GetRuleExecutionsByUcidResponseBody> mappedResponse = Arrays.stream(
                        objectMapper.readValue(responseBodyString, GetRuleExecutionsByUcidResponseBody[].class))
                .toList();
        assertThat(
                "Assert paymentId present",
                mappedResponse.getFirst().getPaymentId(),
                is(paymentRuleExecutionsObject6.getPaymentId()));
        // Additional asserts based on the sample response structure
        assertThat("Assert items list present", mappedResponse.getFirst().getItems(), is(notNullValue()));
        GetRuleExecutionsByUcidResponseBody.Item item =
                mappedResponse.getFirst().getItems().getFirst();
        assertThat("Assert item.runId present", item.getRunId(), not(paymentRuleExecutionsObject6.getRunId()));
        assertThat(
                "Assert item.paymentId equals response paymentId",
                item.getPaymentId(),
                is(paymentRuleExecutionsObject6.getPaymentId().toString()));
        assertThat("Assert item.ruleId present", item.getRuleId(), is(paymentRuleExecutionsObject6.getRuleId()));
        assertThat(
                "Assert item.ruleVersion present",
                item.getRuleVersion(),
                is(paymentRuleExecutionsObject6.getRuleVersion()));
        assertThat(
                "Assert item.ruleEndId present", item.getRuleEndId(), is(paymentRuleExecutionsObject6.getRuleEndId()));
        assertThat(
                "Assert item.startedAt present",
                item.getStartedAt(),
                not(paymentRuleExecutionsObject6.getDateStarted()));
        assertThat(
                "Assert item.completedAt present",
                item.getCompletedAt(),
                not(paymentRuleExecutionsObject6.getDateCompleted()));
        assertThat(
                "Assert item.createdAt present",
                item.getCreatedAt(),
                not(paymentRuleExecutionsObject6.getDateCreated()));
        assertThat(
                "Assert item.updatedAt present",
                item.getUpdatedAt(),
                not(paymentRuleExecutionsObject6.getDateUpdated()));
    }
}
