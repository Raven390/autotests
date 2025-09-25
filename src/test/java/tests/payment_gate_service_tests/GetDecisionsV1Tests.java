package tests.payment_gate_service_tests;


import business_objects.api.payment_gate.decisions.GetDecisionsResponseBody;
import business_objects.db.payment_gate.payment_decisions.PaymentDecisionsObject;
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

import static business_objects.api.payment_gate.decisions.DecisionsRequests.getDecisions;
import static business_objects.db.payment_gate.payment_decisions.PaymentDecisionsObjectFactory.generatePaymentDecisionObject;
import static business_objects.db.payment_gate.payment_details.PaymentDetailsObjectFactory.generatePaymentDetailsObject;
import static business_objects.db.payment_gate.payment_events.PaymentEventsObjectFactory.generatePaymentEventsObject;
import static business_objects.db.payment_gate.payment_rule_executions.PaymentRuleExecutionsObjectFactory.generatePaymentRuleExecutionsObject;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.CleanTableHelper.cleanPaymentGateData;
import static helpers.database.DbHelper.insertObjectsToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Constants.LAYER_API;
import static utils.Utils.getRandomUuidString;

@Feature(FEATURE_PAYMENT_GATE)
@Story(STORY_PAYMENT_GATE_GET_DECISIONS)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_PAYMENT_GATE_TESTS)
class GetDecisionsV1Tests extends TestBaseApi {
    private static ClientHelper client1;
    private static PaymentEventsObject paymentEventsObject1;
    private static PaymentDetailsObject paymentDetailsObject1;
    private static PaymentDecisionsObject paymentDecisionsObject1;
    private static PaymentRuleExecutionsObject paymentRuleExecutionsObject1;

    @BeforeAll
    static void setupData() {

        client1 = getRandomVantageClientAllFields();
        paymentEventsObject1 = generatePaymentEventsObject(client1);
        paymentDetailsObject1 = generatePaymentDetailsObject(paymentEventsObject1, client1);
        paymentRuleExecutionsObject1 = generatePaymentRuleExecutionsObject(paymentEventsObject1);
        paymentDecisionsObject1 = generatePaymentDecisionObject(paymentEventsObject1);

        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE, List.of(paymentEventsObject1));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE, List.of(paymentDetailsObject1));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_RULE_EXECUTIONS_TABLE, List.of(paymentRuleExecutionsObject1));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DECISIONS_TABLE, List.of(paymentDecisionsObject1));
    }

    @AfterAll
    static void deleteData() throws Exception {
        cleanPaymentGateData(client1.getUcid(), client1.getUserId(), paymentEventsObject1.getPaymentId().toString());
    }

    @Test
    @AllureId("1590")
    @DisplayName("Get payment decision V1. Success test no parameters. 200")
    void GetDecisionsV1Test1() throws Exception {

        Map<String, Object> paramsMap = new HashMap<>();
        Response response = getDecisions(paymentEventsObject1.getPaymentId().toString(), paramsMap);
        assertThat("Assert status", response.code(), is(200));
        Allure.step("Validate Data in response");
        GetDecisionsResponseBody[] mappedResponse = objectMapper.readValue(response.body().string(), GetDecisionsResponseBody[].class);
        assertThat("Assert one decision returned", mappedResponse.length, is(1));
        GetDecisionsResponseBody item = mappedResponse[0];
        // Basic field presence and values
        assertThat("Assert paymentType equals event type", item.getPaymentType(), is(paymentEventsObject1.getType()));
        assertThat("Assert decisionType equals DB decisionType", item.getDecisionType(), is(paymentDecisionsObject1.getDecisionType()));
        assertThat("Assert decisionCode equals DB decisionCode", item.getDecisionCode(), is(String.valueOf(paymentDecisionsObject1.getDecisionCode())));
        assertThat("Assert decision", item.getDecision(), is("Approve"));
        assertThat("Assert actor", item.getActor(), is(paymentDecisionsObject1.getActor()));
        assertThat("Assert time", item.getDecidedAt(), is(notNullValue()));
        assertThat("Assert rejection code", item.getRejectionCode(), is(paymentDecisionsObject1.getRejectionCode().toString()));
        assertThat("Assert reason", item.getRejectionReason(), is("Test"));
        assertThat("Assert actor", item.getActor(), is("QA"));
    }

    @Test
    @AllureId("1591")
    @DisplayName("Get payment decision V1. Success test empty parameters. 200")
    void GetDecisionsV1Test2() throws Exception {

        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("decisionId", "");
        paramsMap.put("decisionType", "");
        paramsMap.put("actor", "");
        Response response = getDecisions(paymentEventsObject1.getPaymentId().toString(), paramsMap);
        assertThat("Assert status", response.code(), is(200));
        Allure.step("Validate Data in response");
        GetDecisionsResponseBody[] mappedResponse = objectMapper.readValue(response.body().string(), GetDecisionsResponseBody[].class);
        assertThat("Assert one decision returned", mappedResponse.length, is(1));
        GetDecisionsResponseBody item = mappedResponse[0];
        // Basic field presence and values
        assertThat("Assert paymentType equals event type", item.getPaymentType(), is(paymentEventsObject1.getType()));
        assertThat("Assert decisionType equals DB decisionType", item.getDecisionType(), is(paymentDecisionsObject1.getDecisionType()));
        assertThat("Assert decisionCode equals DB decisionCode", item.getDecisionCode(), is(String.valueOf(paymentDecisionsObject1.getDecisionCode())));
        assertThat("Assert decision", item.getDecision(), is("Approve"));
        assertThat("Assert actor", item.getActor(), is(paymentDecisionsObject1.getActor()));
        assertThat("Assert time", item.getDecidedAt(), is(notNullValue()));
        assertThat("Assert rejection code", item.getRejectionCode(), is(paymentDecisionsObject1.getRejectionCode().toString()));
        assertThat("Assert reason", item.getRejectionReason(), is("Test"));
        assertThat("Assert actor", item.getActor(), is("QA"));
    }

    @Test
    @AllureId("1592")
    @DisplayName("Get payment decision V1. Not found. 404")
    void GetDecisionsV1Test3() throws Exception {

        Map<String, Object> paramsMap = new HashMap<>();
        Response response = getDecisions(getRandomUuidString(), paramsMap);
        assertThat("Assert status", response.code(), is(404));
        Allure.step("Validate Data in response");
        GetDecisionsResponseBody mappedResponse = objectMapper.readValue(response.body().string(), GetDecisionsResponseBody.class);
        // Basic field presence and values
        assertThat("Assert status", mappedResponse.getError(), is("not_found"));
        assertThat("Assert status", mappedResponse.getMessage(), containsString("Payment ID not found"));
    }

    @Test
    @AllureId("1593")
    @DisplayName("Get payment decision V1. Bad request. 400")
    void GetDecisionsV1Test4() throws Exception {

        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("decisionId", "asdf");
        Response response = getDecisions(getRandomUuidString(), paramsMap);
        assertThat("Assert status", response.code(), is(400));
        Allure.step("Validate Data in response");
        GetDecisionsResponseBody mappedResponse = objectMapper.readValue(response.body().string(), GetDecisionsResponseBody.class);
        // Basic field presence and values
        assertThat("Assert error", mappedResponse.getTitle(), is("Bad Request"));
        assertThat("Assert error", mappedResponse.getStatus(), is("400"));
        assertThat("Assert error", mappedResponse.getDetail(), containsString("Failed to convert 'decisionId' with value:"));
    }
}
