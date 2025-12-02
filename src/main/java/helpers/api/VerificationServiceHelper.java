package helpers.api;

import business_objects.api.verification_service.PutProfileStatusRequestBody;
import helpers.data.ClientHelper;
import helpers.data.enums.VerificationStatus;

import java.io.IOException;

import static business_objects.api.abuse_registry.AbuseRegistryRequest.*;
import static business_objects.api.verification_service.VerificationServiceRequest.putProfilesStatus;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static utils.Constants.VINDEX_BO_SYSTEM;

public class VerificationServiceHelper {

    private static final String ACTOR = "Auto Test";
    private static final String COMMENT = "Set by autotest";

    private VerificationServiceHelper() {
    }

    public static void putProfileStatus(VerificationStatus verificationStatus, String paymentProfileKey,
            ClientHelper client) throws IOException {
        PutProfileStatusRequestBody requestBody = PutProfileStatusRequestBody.builder().status(verificationStatus.toString()).paymentProfileKey(paymentProfileKey).ucid(client.getUcid()).comment(COMMENT).updatedByUsername(ACTOR).updatedBySystem(VINDEX_BO_SYSTEM).build();
        assertThat("Check that request was successful", putProfilesStatus(requestBody).code(), is(200));
    }
}
