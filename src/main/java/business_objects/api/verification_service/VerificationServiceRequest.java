package business_objects.api.verification_service;

import helpers.http_helper.HttpHelper;
import okhttp3.Response;

import java.io.IOException;

import static utils.ConfigFactory.*;

public class VerificationServiceRequest {

    private VerificationServiceRequest() {
    }

    public static Response putProfilesStatus(PutProfileStatusRequestBody putProfileStatusRequestBody)
            throws IOException {
        return new HttpHelper().sendPutRequest(String.format("%s%s", VERIFICATION_SERVICE_BASE_PATH, VERIFICATION_SERVICE_PUT_PROFILES_STATUS), null, null, putProfileStatusRequestBody);
    }
}
