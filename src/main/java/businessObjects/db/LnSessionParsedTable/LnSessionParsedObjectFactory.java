package businessObjects.db.lnSessionParsedTable;

import io.qameta.allure.Step;

import static utils.Utils.getCurrentTimestampDbFormat;

public class LnSessionParsedObjectFactory {

    @Step("Generate lexis nexis object for user")
    public static LnSessionParsedObject generateLexisNexisDataForUserId(String uid, Integer userId, Integer eventId) {
        return new LnSessionParsedObject(
                uid, 123, "vt", "sessionId", userId,
                "email@email.com", 60, "123456", "registration", true,
                "1970-01-01 00:00:00", 0,"12","device_id","12345",
                "1971-01-01 00:00:00", eventId,"127.0.0.1","proxyIpActivities",
                new String[]{"String_1", "String_2"},"proxyIpCity","proxyIpConnection",
                "1972-01-01", "proxyIpGeo","proxyIpHome","proxyIpIsp",
                30.30,40.40,"proxyIpOrganization", "proxyIpOrganizationType",
                "proxyIpPostalCode","proxyIpRegion","proxyIpResult","proxyIpRoutingType",
                2, 2,"proxyIpV6","proxyName",3d,"proxyType",
                "192.168.0.1", "trueIpActivities",new String[]{"String_1", "String_2"}, "trueIpCity"
                ,4, "1973-01-01","trueIpGeo","trueIpIsp",
                "1973-01-01", "trueIpOrganization", "trueIpOrganizationType","trueIpPostalCode"
                ,"trueIpRegion", "trueIpResult","trueIpRoutingType",100,1, "1",1
        );
    }

    // Registration Rule

    public static LnSessionParsedObject registrationRuleExitEventEnd1LNDbObject() {
        LnSessionParsedObject dbObject = new LnSessionParsedObject();
        dbObject.userId = 8_880_001;
        dbObject.brand = "Vantage";
        dbObject.eventType = "account_creation";
        dbObject.createTime = getCurrentTimestampDbFormat();
        return dbObject;
    }
}
