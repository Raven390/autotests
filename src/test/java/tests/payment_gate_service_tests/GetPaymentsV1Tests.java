package tests.payment_gate_service_tests;

import business_objects.api.payment_gate.payments.GetPaymentsResponseBody;
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


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static business_objects.api.payment_gate.payments.PaymentsRequests.getPayments;
import static business_objects.db.payment_gate.payment_decisions.PaymentDecisionsObjectFactory.generatePaymentDecisionObject;
import static business_objects.db.payment_gate.payment_details.PaymentDetailsObjectFactory.generatePaymentDetailsObject;
import static business_objects.db.payment_gate.payment_events.PaymentEventsObjectFactory.generatePaymentEventsObject;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.CleanTableHelper.cleanPaymentGateData;
import static helpers.database.DbHelper.insertObjectsToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Constants.LAYER_API;
import static utils.Constants.SUITE_PAYMENT_GATE_TESTS;

@Feature(FEATURE_PAYMENT_GATE)
@Story(STORY_PAYMENT_GATE_GET_PAYMENTS)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_PAYMENT_GATE_TESTS)
class GetPaymentsV1Tests extends TestBaseApi {

    private static ClientHelper client1;
    private static ClientHelper client2;
    private static ClientHelper client3;
    private static ClientHelper client4;
    private static PaymentEventsObject paymentEventsObject1;
    private static PaymentEventsObject paymentEventsObject2;
    private static PaymentEventsObject paymentEventsObject3;
    private static PaymentEventsObject paymentEventsObject4;

    @BeforeAll
    static void setupData() {

        client1 = getRandomVantageClientAllFields();
        paymentEventsObject1 = generatePaymentEventsObject(client1);
        PaymentDetailsObject paymentDetailsObject1 = generatePaymentDetailsObject(paymentEventsObject1, client1);
        paymentDetailsObject1.setPayload("{\"id\": \"123e4567-e89b-12d3-a456-426614174000\", \"ip\": \"121.233.122.82\", \"card\": {\"card3ds\": 0, \"expYear\": \"2029\", \"expMonth\": \"4\", \"fullName\": \"sheryar shah\", \"lastFour\": \"1225\", \"binNumber\": \"654321\"}, \"cost\": 0.56, \"type\": \"withdrawal\", \"brand\": \"vantage\", \"status\": \"Success\", \"clientId\": 112341, \"platform\": \"WEB\", \"statusId\": 1, \"checkName\": \"WR_Blacklist\", \"eventDate\": \"2025-05-20T14:30:00Z\", \"regulator\": \"CIMA\", \"statusKYC\": \"Confirmed\", \"mt4Account\": 3031915, \"accountType\": \"MT5\", \"withdrawalId\": 2373634, \"schemaVersion\": \"1.0\", \"merchantOrderId\": \"VTSG1115142220250202132259\", \"paymentTypeCode\": 2, \"paymentTypeName\": \"Credit card\", \"withdrawalAmount\": 1500.00, \"paymentMethodCode\": \"CREDIT_CARD\", \"paymentChannelCode\": 1, \"paymentChannelName\": \"Credit card\", \"withdrawalCurrency\": \"USD\", \"withdrawalAmountUSD\": 1500.00, \"withdrawalApplicationTime\": \"2025-07-15 07:38:05\"}");
        PaymentDecisionsObject paymentDecisionsObject1 = generatePaymentDecisionObject(paymentEventsObject1);

        client2 = getRandomVantageClientAllFields();
        paymentEventsObject2 = generatePaymentEventsObject(client2);
        PaymentDetailsObject paymentDetailsObject2 = generatePaymentDetailsObject(paymentEventsObject2, client2);
        PaymentDecisionsObject paymentDecisionsObject2 = generatePaymentDecisionObject(paymentEventsObject2);

        client3 = getRandomVantageClientAllFields();
        paymentEventsObject3 = generatePaymentEventsObject(client3);
        PaymentDetailsObject paymentDetailsObject3 = generatePaymentDetailsObject(paymentEventsObject3, client3);
        PaymentDecisionsObject paymentDecisionsObject3 = generatePaymentDecisionObject(paymentEventsObject3);


        client4 = getRandomVantageClientAllFields();
        paymentEventsObject4 = generatePaymentEventsObject(client4);
        PaymentDetailsObject paymentDetailsObject4 = generatePaymentDetailsObject(paymentEventsObject4, client4);
        PaymentDecisionsObject paymentDecisionsObject4 = generatePaymentDecisionObject(paymentEventsObject4);

        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE, List.of(paymentEventsObject1, paymentEventsObject2, paymentEventsObject3, paymentEventsObject4));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE, List.of(paymentDetailsObject1, paymentDetailsObject2, paymentDetailsObject3, paymentDetailsObject4));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DECISIONS_TABLE, List.of(paymentDecisionsObject1, paymentDecisionsObject2, paymentDecisionsObject3, paymentDecisionsObject4));
    }

    @AfterAll
    static void deleteData() throws Exception {
        cleanPaymentGateData(client1.getUcid(), client1.getUserId(), paymentEventsObject1.getPaymentId().toString());
        cleanPaymentGateData(client2.getUcid(), client2.getUserId(), paymentEventsObject2.getPaymentId().toString());
        cleanPaymentGateData(client3.getUcid(), client3.getUserId(), paymentEventsObject3.getPaymentId().toString());
        cleanPaymentGateData(client4.getUcid(), client4.getUserId(), paymentEventsObject4.getPaymentId().toString());
    }

    @Test
    @AllureId("1602")
    @DisplayName("Get payment V1. Success test. 200")
    void GetPaymentsV1Test1() throws Exception {

        Allure.step("send get payment request with valid data");
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("ucid", client1.getUcid());
        Response response = getPayments(paramsMap);
        assertThat(response.code(), is(200));

        Allure.step("Validate Data in response");
        GetPaymentsResponseBody mappedResponse = objectMapper.readValue(response.body().string(), GetPaymentsResponseBody.class);
        // top-level paging
        assertThat("Assert page is 1", mappedResponse.getPage(), is(1));
        assertThat("Assert pageSize is 50", mappedResponse.getPageSize(), is(50));
        assertThat("Assert total is 1", mappedResponse.getTotal(), is(1));
        assertThat("Assert hasNext is false", mappedResponse.getHasNext(), is(false));
        // high-level success shape
        assertThat("Assert success has no error", mappedResponse.getError(), is(nullValue()));
        assertThat("Assert success has no message", mappedResponse.getMessage(), is(nullValue()));
        // items
        assertThat("Assert items present", mappedResponse.getItems(), notNullValue());
        assertThat("Assert items count >= 1", mappedResponse.getItems().size(), greaterThanOrEqualTo(1));

        GetPaymentsResponseBody.Item item = mappedResponse.getItems().getFirst();
        assertThat("Assert item.paymentId equals inserted paymentId", item.getPaymentId(), is(paymentEventsObject1.getPaymentId()));
        assertThat("Assert item.ucid equals client ucid", item.getUcid(), is(client1.getUcid()));
        assertThat("Assert item.type is withdrawal", item.getType(), equalToIgnoringCase("withdrawal"));
        assertThat("Assert item.storedAt present", item.getStoredAt(), not(isEmptyOrNullString()));
        assertThat("Assert item.finalDecisionCode present", item.getFinalDecisionCode(), notNullValue());

        // event block
        assertThat("Assert event present", item.getEvent(), notNullValue());
        GetPaymentsResponseBody.Event event = item.getEvent();
        // match provided event sample
        assertThat("Assert event.id matches", event.getId(), is(java.util.UUID.fromString("123e4567-e89b-12d3-a456-426614174000")));
        assertThat("Assert event.ip matches", event.getIp(), is("121.233.122.82"));
        assertThat("Assert event.cost matches", event.getCost(), is(closeTo(0.56, 0.000_001)));
        assertThat("Assert event.type is withdrawal", event.getType(), is("withdrawal"));
        assertThat("Assert event.brand is vantage", event.getBrand(), is("vantage"));
        assertThat("Assert event.status is Success", event.getStatus(), is("Success"));
        assertThat("Assert event.clientId matches", event.getClientId(), is(112341L));
        assertThat("Assert event.platform is WEB", event.getPlatform(), is("WEB"));
        assertThat("Assert event.statusId is 1", event.getStatusId(), is(1));
        assertThat("Assert event.checkName is WR_Blacklist", event.getCheckName(), is("WR_Blacklist"));
        assertThat("Assert event.eventDate present", event.getEventDate(), is("2025-05-20T14:30:00Z"));
        assertThat("Assert event.regulator is CIMA", event.getRegulator(), is("CIMA"));
        assertThat("Assert event.statusKYC is Confirmed", event.getStatusKYC(), is("Confirmed"));
        assertThat("Assert event.mt4Account matches", event.getMt4Account(), is(3031915L));
        assertThat("Assert event.accountType is MT5", event.getAccountType(), is("MT5"));
        assertThat("Assert event.withdrawalId matches", event.getWithdrawalId(), is(2373634L));
        assertThat("Assert event.schemaVersion is 1.0", event.getSchemaVersion(), is("1.0"));
        assertThat("Assert event.merchantOrderId set", event.getMerchantOrderId(), is("VTSG1115142220250202132259"));
        assertThat("Assert event.paymentTypeCode is 2", event.getPaymentTypeCode(), is(2));
        assertThat("Assert event.paymentTypeName is Credit card", event.getPaymentTypeName(), is("Credit card"));
        assertThat("Assert event.withdrawalAmount is 1500.00", event.getWithdrawalAmount(), is(closeTo(1500.00, 0.000_001)));
        assertThat("Assert event.paymentMethodCode is CREDIT_CARD", event.getPaymentMethodCode(), is("CREDIT_CARD"));
        assertThat("Assert event.paymentChannelCode is 1", event.getPaymentChannelCode(), is(1));
        assertThat("Assert event.paymentChannelName is Credit card", event.getPaymentChannelName(), is("Credit card"));
        assertThat("Assert event.withdrawalCurrency is USD", event.getWithdrawalCurrency(), is("USD"));
        assertThat("Assert event.withdrawalAmountUSD is 1500.00", event.getWithdrawalAmountUSD(), is(closeTo(1500.00, 0.000_001)));
        assertThat("Assert event.withdrawalApplicationTime present", event.getWithdrawalApplicationTime(), is("2025-07-15 07:38:05"));
        // card assertions
        assertThat("Assert card present", event.getCard(), notNullValue());
        GetPaymentsResponseBody.Card card = event.getCard();
        assertThat("Assert card.card3ds is 0", card.getCard3ds(), is(0));
        assertThat("Assert card.expYear is 2029", card.getExpYear(), is("2029"));
        assertThat("Assert card.expMonth is 4", card.getExpMonth(), is("4"));
        assertThat("Assert card.fullName is sheryar shah", card.getFullName(), is("sheryar shah"));
        assertThat("Assert card.lastFour is 1225", card.getLastFour(), is("1225"));
        assertThat("Assert card.binNumber is 654321", card.getBinNumber(), is("654321"));

        // decisions array present (can be empty)
        assertThat("Assert decisions list present", item.getDecisions(), notNullValue());
    }

    @Test
    @AllureId("1603")
    @DisplayName("Get payment V1. Success test all params. 200")
    void GetPaymentsV1Test2() throws Exception {

        Allure.step("send get payment request with valid data");
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("ucid", client1.getUcid());
//        paramsMap.put("paymentId", "");
//        paramsMap.put("merchantOrderId", "");
//        paramsMap.put("withdrawalId", "");
//        paramsMap.put("type", "");
//        paramsMap.put("decisionType", "");
//        paramsMap.put("decisionCode", "");
//        paramsMap.put("finalDecisionCode", "");
//        paramsMap.put("createdDateFrom", "");
//        paramsMap.put("createdDateTo", "");
        Response response = getPayments(paramsMap);
        assertThat(response.code(), is(200));

        Allure.step("Validate Data in response");
        GetPaymentsResponseBody mappedResponse = objectMapper.readValue(response.body().string(), GetPaymentsResponseBody.class);
        assertThat("Assert paymentId present", mappedResponse.getPage(), is(1));
        assertThat("Assert paymentId present", mappedResponse.getPageSize(), is(50));
        assertThat("Assert paymentId present", mappedResponse.getTotal(), greaterThanOrEqualTo(0));
        assertThat("Assert paymentId present", mappedResponse.getHasNext(), is(false));
        assertThat("Assert paymentId present", mappedResponse.getItems(), not(empty()));
    }

    @Test
    @AllureId("1604")
    @DisplayName("Get payment V1. Bad request. 400")
    void GetPaymentsV1Test3() throws Exception {

        Allure.step("send get payment request with valid data");
        Map<String, Object> paramsMap = new HashMap<>();
        Response response = getPayments(paramsMap);
        assertThat(response.code(), is(400));

        Allure.step("Validate Data in response");
        GetPaymentsResponseBody mappedResponse = objectMapper.readValue(response.body().string(), GetPaymentsResponseBody.class);
        assertThat("Assert response", mappedResponse.getError(), is("bad_request"));
        assertThat("Assert response", mappedResponse.getMessage(), containsString("Provide at least one of ucid, paymentId, merchantOrderId or withdrawalId."));
    }

    @Test
    @AllureId("1605")
    @DisplayName("Get payment V1. Bad request. 400")
    void GetPaymentsV1Test4() throws Exception {

        Allure.step("send get payment request with valid data");
        Map<String, Object> paramsMap = new HashMap<>();
        Response response = getPayments(paramsMap);
        paramsMap.put("ucid", "vantage-123");
        assertThat(response.code(), is(400));
        Allure.step("Validate Data in response");
        GetPaymentsResponseBody mappedResponse = objectMapper.readValue(response.body().string(), GetPaymentsResponseBody.class);
        assertThat("Assert response", mappedResponse.getError(), is("bad_request"));
        assertThat("Assert response", mappedResponse.getMessage(), containsString("Provide at least one of ucid, paymentId, merchantOrderId or withdrawalId."));
    }

}
