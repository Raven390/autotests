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
    private static final String ACTOR = "Auto Test";
    private static final String SYSTEM = "BO";
    private static final String COMMENT = "Set by autotest";
    private static final String ASSERT_REASON = "Check that request was successful";

    private AbuseRegistryHelper() {
    }

    static Logger ln = Logger.getLogger(AbuseRegistryHelper.class.getName());

    public static void addFraudsForClient(ClientHelper client, List<FraudType> fraudTypes, FraudTypeStatus status)
            throws IOException, InterruptedException {
        assertThat("Check that fraudTypes list is not empty", fraudTypes.size(), greaterThan(0));
        PostFraudTypesRequestBody requestBody = new PostFraudTypesRequestBody(
                ACTOR, SYSTEM, COMMENT, fraudTypes.stream().map(fraudType -> new PostFraudTypesRequestBody.FraudTypeWithStatus(status.getStatus(), fraudType.getCode())).toList());
        int code = -1;

        for (int attempt = 1; attempt <= 5; attempt++) {
            code = postFraudTypes(client.getUcid(), requestBody).code();
            if (code == 200) {
                break; // Success, stop retrying
            }
            Thread.sleep(1000); // wait 1 second before next try
        }

        assertThat("Check that request was successful after retries", code, is(200));
        ln.info(String.format(FRAUDS_ADDED_SUCCESSFULLY, client.getUcid()));
    }

    public static void addFraudForClient(ClientHelper client, FraudType fraudType, FraudSubtype fraudSubtype,
            FraudTypeStatus status, List<String> symbols)
            throws IOException {
        PostFraudTypesV2RequestBody requestBody = new PostFraudTypesV2RequestBody(
                ACTOR, SYSTEM, COMMENT, List.of(new PostFraudTypesV2RequestBody.FraudType(fraudType.getCode(), status.getStatus(), "Set by autotest", fraudSubtype.getCode(), symbols)));
        int code = 0;
        for (int attempt = 1; attempt <= 5; attempt++) {
            code = postFraudTypesV2(client, requestBody).code();
            if (code == 200) {
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        assertThat(ASSERT_REASON, code, is(200));
        ln.info(String.format(FRAUDS_ADDED_SUCCESSFULLY, client.getUcid()));
    }

    public static void addFraudForClient(ClientHelper client, FraudType fraudType,
            FraudTypeStatus status, List<String> symbols)
            throws IOException {
        PostFraudTypesV2RequestBody requestBody = new PostFraudTypesV2RequestBody(
                ACTOR, SYSTEM, COMMENT, List.of(new PostFraudTypesV2RequestBody.FraudType(fraudType.getCode(), status.getStatus(), "Set by autotest", null, symbols)));
        int code = 0;
        for (int attempt = 1; attempt <= 5; attempt++) {
            code = postFraudTypesV2(client, requestBody).code();
            if (code == 200) {
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        assertThat(ASSERT_REASON, code, is(200));
        ln.info(String.format(FRAUDS_ADDED_SUCCESSFULLY, client.getUcid()));
    }

    public static void addFraudsForClient(String ucid, List<FraudType> fraudTypes, FraudTypeStatus status)
            throws IOException {
        assertThat("Check that fraudTypes list is not empty", fraudTypes.size(), greaterThan(0));
        PostFraudTypesRequestBody requestBody = new PostFraudTypesRequestBody(
                ACTOR, SYSTEM, COMMENT, fraudTypes.stream().map(fraudType -> new PostFraudTypesRequestBody.FraudTypeWithStatus(status.getStatus(), fraudType.getCode())).toList());
        assertThat(ASSERT_REASON, postFraudTypes(ucid, requestBody).code(), is(200));
        ln.info(String.format(FRAUDS_ADDED_SUCCESSFULLY, ucid));
    }

    public static void addFraudForClient(ClientFraudTypes fraud)
            throws IOException {
        PostFraudTypesRequestBody requestBody = new PostFraudTypesRequestBody(
                ACTOR, SYSTEM, COMMENT, List.of(new PostFraudTypesRequestBody.FraudTypeWithStatus("CONFIRMED", fraud.getFraudTypeCode())));
        assertThat(ASSERT_REASON, postFraudTypes(fraud.getUcid(), requestBody).code(), is(200));
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
                ACTOR, SYSTEM, COMMENT, List.of(new PostFraudTypesRequestBody.FraudTypeWithStatus(status, fraud.getFraudTypeCode())));
        assertThat(ASSERT_REASON, postFraudTypes(fraud.getUcid(), requestBody).code(), is(200));
    }


    public static void setClientStatus(ClientHelper client, FraudTypeStatus status)
            throws IOException {
        PostAbuserStatusRequestBody requestBody = new PostAbuserStatusRequestBody(
                ACTOR, SYSTEM, COMMENT, status.getStatus());
        int code = 0;
        for (int attempt = 1; attempt <= 5; attempt++) {
            code = postAbuserStatus(client, requestBody).code();
            if (code == 200) {
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        assertThat(ASSERT_REASON, code, is(200));
    }

    public static Response getClientStatus(ClientHelper client) throws IOException {
        return new HttpHelper().sendGetRequest(ABUSE_REGISTRY_BASE_PATH + "/abusers/" + client.getUcid(), null, null);
    }
}
