package business_objects.api.abuse_registry;

import static utils.ConfigFactory.*;

import helpers.data.ClientHelper;
import helpers.http_helper.HttpHelper;
import java.io.IOException;
import okhttp3.Response;

public class AbuseRegistryRequest {

    private AbuseRegistryRequest() {}

    public static Response postFraudTypes(ClientHelper client, PostFraudTypesRequestBody postFraudTypesRequestBody)
            throws IOException {
        return new HttpHelper()
                .sendPostRequest(
                        String.format(ABUSE_REGISTRY_BASE_PATH + ABUSE_REGISTRY_POST_FRAUD_TYPES, client.getUcid()),
                        null,
                        null,
                        postFraudTypesRequestBody);
    }

    public static Response postFraudTypes(String ucid, PostFraudTypesRequestBody postFraudTypesRequestBody)
            throws IOException {
        return new HttpHelper()
                .sendPostRequest(
                        String.format(ABUSE_REGISTRY_BASE_PATH + ABUSE_REGISTRY_POST_FRAUD_TYPES, ucid),
                        null,
                        null,
                        postFraudTypesRequestBody);
    }

    public static Response postFraudTypesV2(
            ClientHelper client, PostFraudTypesV2RequestBody postFraudTypesV2RequestBody) throws IOException {
        return new HttpHelper()
                .sendPostRequest(
                        String.format(ABUSE_REGISTRY_V2_BASE_PATH + ABUSE_REGISTRY_POST_FRAUD_TYPES, client.getUcid()),
                        null,
                        null,
                        postFraudTypesV2RequestBody);
    }

    public static Response postAbuserStatus(
            ClientHelper client, PostAbuserStatusRequestBody postAbuserStatusRequestBody) throws IOException {
        return new HttpHelper()
                .sendPostRequest(
                        String.format(ABUSE_REGISTRY_BASE_PATH + ABUSE_REGISTRY_POST_ABUSER_STATUS, client.getUcid()),
                        null,
                        null,
                        postAbuserStatusRequestBody);
    }
}
