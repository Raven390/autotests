package business_objects.api.connection_search_api.get_abuse_types_v2;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class GetAbuseTypesResponseV2 {

    @JsonProperty("abuseType")
    public String abuseType;

    @JsonProperty("abuseTypeStatus")
    public String abuseTypeStatus;

    @JsonProperty("maxScoreToInitial")
    public Double maxScoreToInitial;

    @JsonProperty("maxScoreClientId")
    public String maxScoreClientId;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof GetAbuseTypesResponseV2 that)) return false;
        return Objects.equals(abuseType, that.abuseType) && Objects.equals(abuseTypeStatus, that.abuseTypeStatus) && Objects.equals(
                maxScoreToInitial, that.maxScoreToInitial) && Objects.equals(maxScoreClientId, that.maxScoreClientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(abuseType, abuseTypeStatus, maxScoreToInitial, maxScoreClientId);
    }

    @Override
    public String toString() {
        return "GetAbuseTypesResponseV2{" + "abuseType='" + abuseType + '\'' + ", abuseTypeStatus='" + abuseTypeStatus + '\'' + ", maxScoreToInitial=" + maxScoreToInitial + ", maxScoreClientId='" + maxScoreClientId + '\'' + '}';
    }
}
