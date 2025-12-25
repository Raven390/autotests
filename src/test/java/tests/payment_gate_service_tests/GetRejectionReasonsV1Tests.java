package tests.payment_gate_service_tests;

import static business_objects.api.payment_gate.rejection_reasons.RejectionReasonsRequests.getRejectionReasons;
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

import business_objects.api.payment_gate.rejection_reasons.GetRejectionReasonsResponseBody;
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
import java.util.Collections;
import java.util.List;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;

@Feature(FEATURE_PAYMENT_GATE)
@Story(STORY_PAYMENT_GATE_GET_REJECTION_REASONS)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_PAYMENT_GATE_TESTS)
class GetRejectionReasonsV1Tests extends TestBaseApi {
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
        insertObjectsToDb(
                DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_RULE_EXECUTIONS_TABLE, List.of(paymentRuleExecutionsObject1));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DECISIONS_TABLE, List.of(paymentDecisionsObject1));
    }

    @AfterAll
    static void deleteData() throws Exception {
        cleanPaymentGateData(
                client1.getUcid(),
                client1.getUserId(),
                paymentEventsObject1.getPaymentId().toString());
    }

    @Test
    @AllureId("1693")
    @DisplayName("Get rejection reasons V1. Success test request with code. 200")
    void GetRejectionReasonsV1Test1() throws Exception {
        Response response = getRejectionReasons("0", "");
        assertThat("Assert status", response.code(), is(200));
        Allure.step("Validate Data in response");
        String body = response.body().string();
        Allure.step("Response body: " + body);
        GetRejectionReasonsResponseBody[] mappedResponse =
                objectMapper.readValue(body, GetRejectionReasonsResponseBody[].class);
        assertThat("Assert at least one rejection reason returned", mappedResponse.length, is(1));
        GetRejectionReasonsResponseBody item = mappedResponse[0];
        // Validate dictionary fields presence (do not assert exact values, just presence)
        assertThat("Assert code present", item.getCode(), is("0"));
        assertThat("Assert name present", item.getName(), is("Account activity review"));
        assertThat("Assert description present", item.getDescription(), is("Account activity review."));
        assertThat("Assert attributes", item.getAttributes(), notNullValue());
        assertThat("Assert attributes", item.getAttributes().getFirst().getCode(), is("1"));
        assertThat("Assert attributes", item.getAttributes().getFirst().getName(), is("Test attr"));
        assertThat("Assert attributes", item.getAttributes().getFirst().getDescription(), is("Don't remove"));
    }

    @Test
    @AllureId("1697")
    @DisplayName("Get rejection reasons V1. Success test request with name. 200")
    void GetRejectionReasonsV1Test2() throws Exception {
        Response response = getRejectionReasons("1", "Default");
        assertThat("Assert status", response.code(), is(200));
        Allure.step("Validate Data in response");
        String body = response.body().string();
        Allure.step("Response body: " + body);
        GetRejectionReasonsResponseBody[] mappedResponse =
                objectMapper.readValue(body, GetRejectionReasonsResponseBody[].class);
        assertThat("Assert at least one rejection reason returned", mappedResponse.length, is(1));
        GetRejectionReasonsResponseBody item = mappedResponse[0];
        // Validate dictionary fields presence (do not assert exact values, just presence)
        assertThat("Assert code present", item.getCode(), is("1"));
        assertThat("Assert name present", item.getName(), is("Default"));
        assertThat("Assert description present", item.getDescription(), is(""));
        assertThat("Assert description present", item.getAttributes(), is(Collections.emptyList()));
    }

    @Test
    @AllureId("1698")
    @DisplayName("Get rejection reasons V1. Success test request without params. 200")
    void GetRejectionReasonsV1Test3() throws Exception {
        Response response = getRejectionReasons();
        assertThat("Assert status", response.code(), is(200));
        Allure.step("Validate Data in response");
        String body = response.body().string();
        Allure.step("Response body: " + body);
        GetRejectionReasonsResponseBody[] mappedResponse =
                objectMapper.readValue(body, GetRejectionReasonsResponseBody[].class);
        assertThat("Assert at least one rejection reason returned", mappedResponse.length, greaterThan(1));
    }
}
