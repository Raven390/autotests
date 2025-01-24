package businessObjects.api.connectionSearchApi.getAbuseTypes;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class GetAbuseTypesResponse {

    @JsonProperty("abuseType")
    public String abuseType;

    public GetAbuseTypesResponse() {
    }

    public GetAbuseTypesResponse(String abuseType) {
        this.abuseType = abuseType;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GetAbuseTypesResponse that = (GetAbuseTypesResponse) o;
        return Objects.equals(abuseType, that.abuseType);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(abuseType);
    }

    @Override
    public String toString() {
        return "GetAbuseTypesResponse{" + "abuseType='" + abuseType + '\'' + '}';
    }
}
