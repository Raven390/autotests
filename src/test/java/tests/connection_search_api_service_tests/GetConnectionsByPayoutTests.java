package tests.connection_search_api_service_tests;

import static business_objects.api.connection_search_api.get_connections.GetConnectionsResponseFactory.*;
import static business_objects.api.connection_search_api.get_connections_by_payout.GetConnectionsByPayoutRequest.getConnectionsByPayout;
import static helpers.data.ClientFactory.getRandomVantageClient;
import static helpers.data.DataSetupHelper.setupData;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.getRandomIntPositive;
import static utils.Utils.getRandomLongPositive;

import business_objects.api.connection_search_api.get_connections_by_payout.GetConnectionsByPayoutResponse;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;

@Feature(FEATURE_CONNECTION_SEARCH_API_SERVICE)
@Story(STORY_CONNECTION_SEARCH_BY_PAYOUT)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CONNECTION_SEARCH_SERVICE)
class GetConnectionsByPayoutTests extends TestBaseApi {

    private static final ClientHelper testClient1 = getRandomVantageClient();
    private static final ClientHelper testClient2 = getRandomVantageClient();

    @BeforeAll
    static void setup() throws Exception {}

    @AfterAll
    static void teardown() throws Exception {}

    @Test
    @AllureId("2133")
    @DisplayName("Connection search by payout. Get connection by payout category CREDIT_CARD(200)")
    void getConnectionsByPayoutTest1() throws Exception {
        DataHelper data = new DataHelper();
        data.createClient(testClient1).createCreditCard();

        DataHelper data2 = new DataHelper();
        data2.createClient(testClient2).createCreditCard();

        data2.getClientCards().getFirst().setId(getRandomLongPositive());
        data2.getClientCards()
                .getFirst()
                .setCardBeginSixDigits(data.getClientCards().getFirst().getCardBeginSixDigits());
        data2.getClientCards()
                .getFirst()
                .setCardLastFourDigits(data.getClientCards().getFirst().getCardLastFourDigits());
        data2.getClientCards()
                .getFirst()
                .setExpiryMonth(data.getClientCards().getFirst().getExpiryMonth());
        data2.getClientCards()
                .getFirst()
                .setExpiryYear(data.getClientCards().getFirst().getExpiryYear());

        setupData(data);
        setupData(data2);

        String paymentProfileKey = data.getClientCards().getFirst().getCardBeginSixDigits()
                + "***"
                + data.getClientCards().getFirst().getCardLastFourDigits() + "_"
                + data.getClientCards().getFirst().getExpiryMonth() + "/"
                + data.getClientCards().getFirst().getExpiryYear();

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("ucid", ""); // Current client ucid. To exclude from the result.
        queryParams.put("category", "CREDIT_CARD"); // Payment account category. Possible values:
        // CREDIT_CARD/CRYPTO/E_WALLET/LOCAL_BANK_TRANSFER/INTERNATIONAL_WIRE_TRANSFER'
        queryParams.put("paymentProfileKey", paymentProfileKey); // Payment account for search

        Response response = getConnectionsByPayout(queryParams);
        GetConnectionsByPayoutResponse[] responseBody =
                objectMapper.readValue(response.body().string(), GetConnectionsByPayoutResponse[].class);

        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response body is not empty", responseBody.length, is(2));
        assertThat("Check ucid 1", Arrays.toString(responseBody), containsString(data.clientHelper.getUcid()));
        assertThat("Check ucid 2", Arrays.toString(responseBody), containsString(data2.clientHelper.getUcid()));
    }

    @Test
    @AllureId("2134")
    @DisplayName("Connection search by payout. Get connection by payout category CREDIT_CARD, exclude ucid(200)")
    void getConnectionsByPayoutTest2() throws Exception {
        DataHelper data = new DataHelper();
        data.createClient(testClient1).createCreditCard();

        DataHelper data2 = new DataHelper();
        data2.createClient(testClient2).createCreditCard();

        data2.getClientCards().getFirst().setId(getRandomLongPositive());
        data2.getClientCards()
                .getFirst()
                .setCardBeginSixDigits(data.getClientCards().getFirst().getCardBeginSixDigits());
        data2.getClientCards()
                .getFirst()
                .setCardLastFourDigits(data.getClientCards().getFirst().getCardLastFourDigits());
        data2.getClientCards()
                .getFirst()
                .setExpiryMonth(data.getClientCards().getFirst().getExpiryMonth());
        data2.getClientCards()
                .getFirst()
                .setExpiryYear(data.getClientCards().getFirst().getExpiryYear());

        setupData(data);
        setupData(data2);

        String paymentProfileKey = data.getClientCards().getFirst().getCardBeginSixDigits()
                + "***"
                + data.getClientCards().getFirst().getCardLastFourDigits() + "_"
                + data.getClientCards().getFirst().getExpiryMonth() + "/"
                + data.getClientCards().getFirst().getExpiryYear();

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("ucid", data.clientHelper.getUcid()); // Current client ucid. To exclude from the result.
        queryParams.put("category", "CREDIT_CARD"); // Payment account category. Possible values:
        // CREDIT_CARD/CRYPTO/E_WALLET/LOCAL_BANK_TRANSFER/INTERNATIONAL_WIRE_TRANSFER'
        queryParams.put("paymentProfileKey", paymentProfileKey); // Payment account for search

        Response response = getConnectionsByPayout(queryParams);
        GetConnectionsByPayoutResponse[] responseBody =
                objectMapper.readValue(response.body().string(), GetConnectionsByPayoutResponse[].class);

        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response body is not empty", responseBody.length, is(1));
        assertThat(
                "Check not contains ucid 1",
                Arrays.toString(responseBody),
                not(containsString(data.clientHelper.getUcid())));
        assertThat("Check ucid 2", Arrays.toString(responseBody), containsString(data2.clientHelper.getUcid()));
    }

    @Test
    @AllureId("2135")
    @DisplayName("Connection search by payout. Get connection by payout category CRYPTO(200)")
    void getConnectionsByPayoutTest3() throws Exception {
        String paymentProfileKey = getRandomLongPositive().toString();

        DataHelper data = new DataHelper();
        data.createClient(testClient1).createWithdrawal();
        data.getCrmTbWithdrawalObjects().getFirst().setCryptoWalletAddress(paymentProfileKey);
        data.getCrmTbWithdrawalObjects().getFirst().setStatus("7");

        DataHelper data2 = new DataHelper();
        data2.createClient(testClient2).createWithdrawal();
        data2.getCrmTbWithdrawalObjects().getFirst().setCryptoWalletAddress(paymentProfileKey);
        data2.getCrmTbWithdrawalObjects().getFirst().setStatus("17");

        setupData(data);
        setupData(data2);

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("category", "CRYPTO"); // Payment account category. Possible values:
        // CREDIT_CARD/CRYPTO/E_WALLET/LOCAL_BANK_TRANSFER/INTERNATIONAL_WIRE_TRANSFER'
        queryParams.put("paymentProfileKey", paymentProfileKey); // Payment account for search

        Response response = getConnectionsByPayout(queryParams);
        GetConnectionsByPayoutResponse[] responseBody =
                objectMapper.readValue(response.body().string(), GetConnectionsByPayoutResponse[].class);

        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response body is not empty", responseBody.length, is(2));
        assertThat(
                "Check not contains ucid 1",
                Arrays.toString(responseBody),
                containsString(data.clientHelper.getUcid()));
        assertThat("Check ucid 2", Arrays.toString(responseBody), containsString(data2.clientHelper.getUcid()));
    }

    @Test
    @AllureId("2136")
    @DisplayName("Connection search by payout. Get connection by payout category E_WALLET(200)")
    void getConnectionsByPayoutTest4() throws Exception {
        String paymentProfileKey = "E_WALLET" + getRandomIntPositive();

        DataHelper data = new DataHelper();
        data.createClient(testClient1).createCreditCard().addWithdrawalSumByCategory(100d, 3);
        data.getCrmTbWithdrawalObjects().getFirst().setPaymentDetails(paymentProfileKey);

        DataHelper data2 = new DataHelper();
        data2.createClient(testClient2).createCreditCard().addWithdrawalSumByCategory(100d, 3);
        data2.getCrmTbWithdrawalObjects().getFirst().setPaymentDetails(paymentProfileKey);

        setupData(data);
        setupData(data2);

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("category", "E_WALLET"); // Payment account category. Possible values:
        // CREDIT_CARD/CRYPTO/E_WALLET/LOCAL_BANK_TRANSFER/INTERNATIONAL_WIRE_TRANSFER'
        queryParams.put("paymentProfileKey", paymentProfileKey); // Payment account for search

        Response response = getConnectionsByPayout(queryParams);
        GetConnectionsByPayoutResponse[] responseBody =
                objectMapper.readValue(response.body().string(), GetConnectionsByPayoutResponse[].class);

        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response body is not empty", responseBody.length, is(2));
        assertThat(
                "Check not contains ucid 1",
                Arrays.toString(responseBody),
                containsString(data.clientHelper.getUcid()));
        assertThat("Check ucid 2", Arrays.toString(responseBody), containsString(data2.clientHelper.getUcid()));
    }

    @Test
    @AllureId("2137")
    @DisplayName("Connection search by payout. Get connection by payout category LOCAL_BANK_TRANSFER(200)")
    void getConnectionsByPayoutTest5() throws Exception {
        String paymentProfileKey = "LOCAL_BANK_TRANSFER" + getRandomIntPositive();

        DataHelper data = new DataHelper();
        data.createClient(testClient1).createCreditCard().addWithdrawalSumByCategory(100d, 3);
        data.getCrmTbWithdrawalObjects().getFirst().setPaymentDetails(paymentProfileKey);

        DataHelper data2 = new DataHelper();
        data2.createClient(testClient2).createCreditCard().addWithdrawalSumByCategory(100d, 3);
        data2.getCrmTbWithdrawalObjects().getFirst().setPaymentDetails(paymentProfileKey);

        setupData(data);
        setupData(data2);

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("category", "LOCAL_BANK_TRANSFER"); // Payment account category. Possible values:
        // CREDIT_CARD/CRYPTO/E_WALLET/LOCAL_BANK_TRANSFER/INTERNATIONAL_WIRE_TRANSFER'
        queryParams.put("paymentProfileKey", paymentProfileKey); // Payment account for search

        Response response = getConnectionsByPayout(queryParams);
        GetConnectionsByPayoutResponse[] responseBody =
                objectMapper.readValue(response.body().string(), GetConnectionsByPayoutResponse[].class);

        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response body is not empty", responseBody.length, is(2));
        assertThat(
                "Check not contains ucid 1",
                Arrays.toString(responseBody),
                containsString(data.clientHelper.getUcid()));
        assertThat("Check ucid 2", Arrays.toString(responseBody), containsString(data2.clientHelper.getUcid()));
    }

    @Test
    @AllureId("2138")
    @DisplayName("Connection search by payout. Get connection by payout category INTERNATIONAL_WIRE_TRANSFER(200)")
    void getConnectionsByPayoutTest6() throws Exception {
        String paymentProfileKey = "LOCAL_BANK_TRANSFER" + getRandomIntPositive();
        DataHelper data = new DataHelper();
        data.createClient(testClient1).createCreditCard().addWithdrawalSumByCategory(100d, 3);
        data.getCrmTbWithdrawalObjects().getFirst().setPaymentDetails(paymentProfileKey);

        DataHelper data2 = new DataHelper();
        data2.createClient(testClient2).createCreditCard().addWithdrawalSumByCategory(100d, 3);
        data2.getCrmTbWithdrawalObjects().getFirst().setPaymentDetails(paymentProfileKey);

        setupData(data);
        setupData(data2);

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("category", "INTERNATIONAL_WIRE_TRANSFER"); // Payment account category. Possible values://
        // CREDIT_CARD/CRYPTO/E_WALLET/LOCAL_BANK_TRANSFER/INTERNATIONAL_WIRE_TRANSFER'
        queryParams.put("paymentProfileKey", paymentProfileKey); // Payment account for search

        Response response = getConnectionsByPayout(queryParams);
        GetConnectionsByPayoutResponse[] responseBody =
                objectMapper.readValue(response.body().string(), GetConnectionsByPayoutResponse[].class);

        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response body is not empty", responseBody.length, is(2));
        assertThat(
                "Check not contains ucid 1",
                Arrays.toString(responseBody),
                containsString(data.clientHelper.getUcid()));
        assertThat("Check ucid 2", Arrays.toString(responseBody), containsString(data2.clientHelper.getUcid()));
    }
}
