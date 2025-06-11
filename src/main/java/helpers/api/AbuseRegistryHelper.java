package helpers.api;

import business_objects.api.abuse_registry.PostFraudTypesRequestBody;
import helpers.data.ClientHelper;
import helpers.data.enums.FraudType;
import helpers.data.enums.FraudTypeStatus;

import java.io.IOException;
import java.util.List;

import static business_objects.api.abuse_registry.AbuseRegistryRequest.postFraudTypes;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

public class AbuseRegistryHelper {

    private AbuseRegistryHelper() {
    }

    public static void addFraudsForClient(ClientHelper client, List<FraudType> fraudTypes, FraudTypeStatus status)
            throws IOException {
        assertThat("Check that fraudTypes list is not empty", fraudTypes.size(), greaterThan(0));
        PostFraudTypesRequestBody requestBody = new PostFraudTypesRequestBody(
                "Auto Test", "BO", "Set by autotest", fraudTypes.stream().map(fraudType -> new PostFraudTypesRequestBody.FraudTypeWithStatus(status.getStatus(), fraudType.getCode())).toList());
        assertThat("Check that request was successful", postFraudTypes(client, requestBody).code(), is(200));
    }
}
