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

}
