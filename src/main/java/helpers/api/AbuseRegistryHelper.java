package helpers.api;

import business_objects.api.abuse_registry.PostAbuserStatusRequestBody;
import business_objects.api.abuse_registry.PostFraudTypesRequestBody;
import business_objects.api.abuse_registry.PostFraudTypesV2RequestBody;
import business_objects.db.clickhouse.client_fraud_types.ClientFraudTypes;
import helpers.data.ClientHelper;
import helpers.data.enums.FraudSubtype;
import helpers.data.enums.FraudType;
import helpers.data.enums.FraudTypeStatus;
import helpers.http_helper.HttpHelper;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import static business_objects.api.abuse_registry.AbuseRegistryRequest.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static utils.ConfigFactory.ABUSE_REGISTRY_BASE_PATH;

public class AbuseRegistryHelper {

    private static final String FRAUDS_ADDED_SUCCESSFULLY = "Frauds successfully sent for client %s";

    private AbuseRegistryHelper() {
    }

    static Logger ln = Logger.getLogger(AbuseRegistryHelper.class.getName());

    public static void addFraudsForClient(ClientHelper client, List<FraudType> fraudTypes, FraudTypeStatus status)
            throws IOException {
        assertThat("Check that fraudTypes list is not empty", fraudTypes.size(), greaterThan(0));
        PostFraudTypesRequestBody requestBody = new PostFraudTypesRequestBody(
                "Auto Test", "BO", "Set by autotest", fraudTypes.stream().map(fraudType -> new PostFraudTypesRequestBody.FraudTypeWithStatus(status.getStatus(), fraudType.getCode())).toList());
        assertThat("Check that request was successful", postFraudTypes(client, requestBody).code(), is(200));
        ln.info(String.format(FRAUDS_ADDED_SUCCESSFULLY, client.getUcid()));
    }

    public static void addFraudForClient(ClientHelper client, FraudType fraudType, FraudSubtype fraudSubtype,
            FraudTypeStatus status, List<String> symbols)
            throws IOException {
        PostFraudTypesV2RequestBody requestBody = new PostFraudTypesV2RequestBody(
                "Auto Test", "BO", "Set by autotest", List.of(new PostFraudTypesV2RequestBody.FraudType(fraudType.getCode(), status.getStatus(), "Set by autotest", fraudSubtype.getCode(), symbols)));
        assertThat("Check that request was successful", postFraudTypesV2(client, requestBody).code(), is(200));
        ln.info(String.format(FRAUDS_ADDED_SUCCESSFULLY, client.getUcid()));
    }

    public static void addFraudsForClient(String ucid, List<FraudType> fraudTypes, FraudTypeStatus status)
            throws IOException {
        assertThat("Check that fraudTypes list is not empty", fraudTypes.size(), greaterThan(0));
        PostFraudTypesRequestBody requestBody = new PostFraudTypesRequestBody(
                "Auto Test", "BO", "Set by autotest", fraudTypes.stream().map(fraudType -> new PostFraudTypesRequestBody.FraudTypeWithStatus(status.getStatus(), fraudType.getCode())).toList());
        assertThat("Check that request was successful", postFraudTypes(ucid, requestBody).code(), is(200));
        ln.info(String.format(FRAUDS_ADDED_SUCCESSFULLY, ucid));
    }

    public static void addFraudForClient(ClientFraudTypes fraud)
            throws IOException {
        PostFraudTypesRequestBody requestBody = new PostFraudTypesRequestBody(
                "Auto Test", "BO", "Set by autotest", List.of(new PostFraudTypesRequestBody.FraudTypeWithStatus("CONFIRMED", fraud.getFraudTypeCode())));
        assertThat("Check that request was successful", postFraudTypes(fraud.getUcid(), requestBody).code(), is(200));
        ln.info("fraud " + fraud.getFraudTypeCode() + " successfully sent for client " + fraud.getUcid());
    }

    public static void addFraudsForClient(ClientFraudTypes... frauds) throws IOException {
        for (ClientFraudTypes fraud : frauds) {
            addFraudForClient(fraud);
        }
    }

    public static void addFraudForClient(ClientFraudTypes fraud, String status)
            throws IOException {
        PostFraudTypesRequestBody requestBody = new PostFraudTypesRequestBody(
                "Auto Test", "BO", "Set by autotest", List.of(new PostFraudTypesRequestBody.FraudTypeWithStatus(status, fraud.getFraudTypeCode())));
        assertThat("Check that request was successful", postFraudTypes(fraud.getUcid(), requestBody).code(), is(200));
    }


    public static void setClientStatus(ClientHelper client, FraudTypeStatus status)
            throws IOException {
        PostAbuserStatusRequestBody requestBody = new PostAbuserStatusRequestBody(
                "Auto Test", "BO", "Set by autotest", status.getStatus());
        assertThat("Check that request was successful", postAbuserStatus(client, requestBody).code(), is(200));
    }

    public static Response getClientStatus(ClientHelper client) throws IOException {
        return new HttpHelper().sendGetRequest(ABUSE_REGISTRY_BASE_PATH + "/v1/abusers/" + client.getUcid() + "/status", null, null);
    }
}
