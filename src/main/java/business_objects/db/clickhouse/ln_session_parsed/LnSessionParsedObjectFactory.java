package business_objects.db.clickhouse.ln_session_parsed;

import helpers.data.ClientHelper;
import io.qameta.allure.Step;

import java.util.Arrays;

import static utils.Utils.*;

public class LnSessionParsedObjectFactory {

    static String summaryReasonCodeString = "{\"policy_detail_api\":[{\"customer\":{\"pvid\":\"1000180580\",\"review_status\":\"pass\",\"risk_rating\":\"low\",\"rules\":[{\"reason_code\":\"testRule\",\"rid\":\"1156819363\",\"score\":\"-10\"}]},\"id\":\"0\",\"type\":\"champion\"}]}";

    @Deprecated
    @Step("Generate lexis nexis object for user")
    public static LnSessionParsedObject generateLexisNexisDataForUserId(String uid, Integer userId, Integer eventId) {
        return new LnSessionParsedObject(
                "vt" + userId, 123, "vt", "sessionId", userId, "email@email.com", 60, "123456", "account_creation", true, "1970-01-01 00:00:00", 0, "12", "device_id", "12345", "1971-01-01 00:00:00", eventId, "127.0.0.1", "proxyIpActivities", new String[]{"String_1", "String_2"}, "proxyIpCity", "proxyIpConnection", "1972-01-01", "proxyIpGeo", "proxyIpHome", "proxyIpIsp", 30.30, 40.40, "proxyIpOrganization", "proxyIpOrganizationType", "proxyIpPostalCode", "proxyIpRegion", "proxyIpResult", "proxyIpRoutingType", 2, 2, "proxyIpV6", "proxyName", 3d, "proxyType", "192.168.0.1", "trueIpActivities", new String[]{"String_1", "String_2"}, "trueIpCity", 4, "1973-01-01", "trueIpGeo", "trueIpIsp", "1973-01-01", "trueIpOrganization", "trueIpOrganizationType", "trueIpPostalCode", "trueIpRegion", "trueIpResult", "trueIpRoutingType", 100, 1, "1", 1, summaryReasonCodeString, "Windows", Arrays.toString(new String[]{"\"Identity_Negative_History\"", "\"Identity_Spoofing\""})
        );
    }

    @Step("Generate lexis nexis object by Client")
    public static LnSessionParsedObject generateLexisNexisDataByClient(ClientHelper client) {
        return new LnSessionParsedObject(
                client.getUcid(), getRandomIntPositive(), client.getBrand(), getRandomUuidString(), client.getUserId(), "email@email.com", 60, "123456", "account_creation", true, "1970-01-01 00:00:00", 0, "12", client.getDeviceId(), "12345", "1971-01-01 00:00:00", 123, "127.0.0.1", "proxyIpActivities", new String[]{"String_1", "String_2"}, "proxyIpCity", "proxyIpConnection", "1972-01-01", "proxyIpGeo", "proxyIpHome", "proxyIpIsp", 30.30, 40.40, "proxyIpOrganization", "proxyIpOrganizationType", "proxyIpPostalCode", "proxyIpRegion", "proxyIpResult", "proxyIpRoutingType", 2, 2, "proxyIpV6", "proxyName", 3d, "proxyType", "192.168.0.1", "trueIpActivities", new String[]{"String_1", "String_2"}, "trueIpCity", 4, "1973-01-01", "trueIpGeo", "trueIpIsp", "1973-01-01", "trueIpOrganization", "trueIpOrganizationType", "trueIpPostalCode", "trueIpRegion", "trueIpResult", "trueIpRoutingType", 100, 1, "1", 1, summaryReasonCodeString, "Windows", Arrays.toString(new String[]{"\"Identity_Negative_History\"", "\"Identity_Spoofing\""})
        );
    }
}
