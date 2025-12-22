package business_objects.api.lark.TenantAccessToken;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class TenantAccessTokenRequest {
    @JsonProperty("app_id")
    public String appId;

    @JsonProperty("app_secret")
    public String appSecret;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TenantAccessTokenRequest that = (TenantAccessTokenRequest) o;
        return Objects.equals(appId, that.appId) && Objects.equals(appSecret, that.appSecret);
    }

    @Override
    public int hashCode() {
        return Objects.hash(appId, appSecret);
    }

    @Override
    public String toString() {
        return "TenantAccessTokenRequest{" + "appId='" + appId + '\'' + ", appSecret='" + appSecret + '\'' + '}';
    }

    public TenantAccessTokenRequest() {}

    public TenantAccessTokenRequest(String appId, String appSecret) {
        this.appId = appId;
        this.appSecret = appSecret;
    }
}
