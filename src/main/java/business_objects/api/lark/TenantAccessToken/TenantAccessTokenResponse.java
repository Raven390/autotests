package business_objects.api.lark.TenantAccessToken;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class TenantAccessTokenResponse {
    @JsonProperty("code")
    Integer code;

    @JsonProperty("msg")
    String msg;

    @JsonProperty("tenant_access_token")
    String tenantAccessToken;

    @JsonProperty("expire")
    Integer expire;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTenantAccessToken() {
        return tenantAccessToken;
    }

    public void setTenantAccessToken(String tenantAccessToken) {
        this.tenantAccessToken = tenantAccessToken;
    }

    public Integer getExpire() {
        return expire;
    }

    public void setExpire(Integer expire) {
        this.expire = expire;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TenantAccessTokenResponse that = (TenantAccessTokenResponse) o;
        return Objects.equals(code, that.code) && Objects.equals(msg, that.msg) && Objects.equals(tenantAccessToken, that.tenantAccessToken) && Objects.equals(expire, that.expire);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, msg, tenantAccessToken, expire);
    }

    @Override
    public String toString() {
        return "TenantAccessTokenResponse{" + "code=" + code + ", msg='" + msg + '\'' + ", tenantAccessToken='" + tenantAccessToken + '\'' + ", expire=" + expire + '}';
    }
}
