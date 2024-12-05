package businessObjects.api.clickhouseApiService.getAbuseTypes;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.Objects;

public class GetAbuseTypesResponse {

    @JsonProperty("clientId")
    public String clientId;

    @JsonProperty("fraudType")
    public String[] fraudType;

    public GetAbuseTypesResponse() {
    }

    public GetAbuseTypesResponse(String clientId, String[] fraudType) {
        this.clientId = clientId;
        this.fraudType = fraudType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetAbuseTypesResponse that = (GetAbuseTypesResponse) o;
        return Objects.equals(clientId, that.clientId) && Objects.deepEquals(fraudType, that.fraudType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, Arrays.hashCode(fraudType));
    }

    @Override
    public String toString() {
        return "GetAbuseTypesResponse{" + "clientId='" + clientId + '\'' + ", fraudType=" + Arrays.toString(fraudType) + '}';
    }
}
