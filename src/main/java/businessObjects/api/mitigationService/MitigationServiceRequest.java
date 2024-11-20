package businessObjects.api.mitigationService;

import helpers.httpHelper.HttpHelper;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;

import static utils.ConfigFactory.*;

public class MitigationServiceRequest {

    public static Response getRestrictionCatalog() throws IOException {
        return new HttpHelper().sendGetRequest(MITIGATION_SERVICE_BASE_PATH + MITIGATION_SERVICE_GET_RESTRICTION_CATALOG, null, null);
    }

    public static Response postRestriction(PostRestrictionRequestBody postRestrictionRequestBody) throws IOException {
        return new HttpHelper().sendPostRequest(MITIGATION_SERVICE_BASE_PATH + MITIGATION_SERVICE_RESTRICTIONS, null, null, postRestrictionRequestBody);
    }

    public static Response getRestrictionsByUcid(String ucid) throws IOException {
        return new HttpHelper().sendGetRequest(MITIGATION_SERVICE_BASE_PATH + MITIGATION_SERVICE_RESTRICTIONS, null, Map.of("ucid", ucid));
    }

    public static Response cancelRestrictionById(Integer id, CancelRestrictionRequestBody cancelRestrictionRequestBody) throws IOException {
        return new HttpHelper().sendPostRequest(String.format(MITIGATION_SERVICE_BASE_PATH + MITIGATION_SERVICE_CANCEL_RESTRICTION, id), null, null, cancelRestrictionRequestBody);
    }


    public static Response enableCRMEmulator() throws IOException {
        return new HttpHelper().sendPostRequest("http://k8s-test-emulator-6a1119759b-161076615.us-east-1.elb.amazonaws.com/mitigation/enabled", null, null, "true");
    }
}
