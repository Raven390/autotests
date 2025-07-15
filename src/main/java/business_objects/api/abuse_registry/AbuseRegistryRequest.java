package business_objects.api.abuse_registry;

import helpers.data.ClientHelper;
import helpers.http_helper.HttpHelper;
import okhttp3.Response;

import java.io.IOException;

import static utils.ConfigFactory.*;

public class AbuseRegistryRequest {

    private AbuseRegistryRequest() {
    }

    public static Response postFraudTypes(ClientHelper client, PostFraudTypesRequestBody postFraudTypesRequestBody)
            throws IOException {
        return new HttpHelper().sendPostRequest(String.format(ABUSE_REGISTRY_BASE_PATH + ABUSE_REGISTRY_POST_FRAUD_TYPES, client.getUcid()), null, null, postFraudTypesRequestBody);
    }

    public static Response postFraudTypes(String ucid, PostFraudTypesRequestBody postFraudTypesRequestBody)
            throws IOException {
        return new HttpHelper().sendPostRequest(String.format(ABUSE_REGISTRY_BASE_PATH + ABUSE_REGISTRY_POST_FRAUD_TYPES, ucid), null, null, postFraudTypesRequestBody);
    }

    public static Response postAbuserStatus(ClientHelper client,
            PostAbuserStatusRequestBody postAbuserStatusRequestBody)
            throws IOException {
        return new HttpHelper().sendPostRequest(String.format(ABUSE_REGISTRY_BASE_PATH + ABUSE_REGISTRY_POST_ABUSER_STATUS, client.getUcid()), null, null, postAbuserStatusRequestBody);
    }

}
