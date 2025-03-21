package business_objects.api.connection_search_api.get_abuse_types;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class GetAbuseTypesResponse {

    @JsonProperty("abuseType")
    public String abuseType;

    @JsonProperty("maxScoreToInitial")
    public Double maxScoreToInitial;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GetAbuseTypesResponse that = (GetAbuseTypesResponse) o;
        return Objects.equals(abuseType, that.abuseType) && Objects.equals(maxScoreToInitial, that.maxScoreToInitial);
    }

    @Override
    public int hashCode() {
        return Objects.hash(abuseType, maxScoreToInitial);
    }

    @Override
    public String toString() {
        return "GetAbuseTypesResponse{" + "abuseType='" + abuseType + '\'' + ", maxScoreToInitial='" + maxScoreToInitial + '\'' + '}';
    }
}
