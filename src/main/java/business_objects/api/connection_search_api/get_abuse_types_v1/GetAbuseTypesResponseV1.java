package business_objects.api.connection_search_api.get_abuse_types_v1;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class GetAbuseTypesResponseV1 {

    @JsonProperty("abuseType")
    public String abuseType;

    @JsonProperty("maxScoreToInitial")
    public Double maxScoreToInitial;

    @JsonProperty("maxScoreClientId")
    public String maxScoreClientId;

    @JsonProperty("fraudTypeStatus")
    public String fraudTypeStatus;

    @Override
    public String toString() {
        return "GetAbuseTypesResponse{" + "abuseType='" + abuseType + '\'' + ", maxScoreToInitial=" + maxScoreToInitial + ", maxScoreClientId=" + maxScoreClientId + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GetAbuseTypesResponseV1 that = (GetAbuseTypesResponseV1) o;
        return Objects.equals(abuseType, that.abuseType) && Objects.equals(maxScoreToInitial, that.maxScoreToInitial) && Objects.equals(maxScoreClientId, that.maxScoreClientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(abuseType, maxScoreToInitial, maxScoreClientId);
    }
}
