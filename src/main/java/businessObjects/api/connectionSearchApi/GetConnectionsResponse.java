package businessObjects.api.connectionSearchApi;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.Objects;

import static utils.Utils.roundDouble;

public class GetConnectionsResponse {

    @JsonProperty("clientIdFrom")
    public String clientIdFrom;

    @JsonProperty("clientIdTo")
    public String clientIdTo;

    @JsonProperty("connectionStrength")
    public Double connectionStrength;

    @JsonProperty("connectionDetail")
    public ConnectionDetail[] connectionDetail;

    @JsonProperty("connectionType")
    public String connectionType;

    @JsonProperty("connectionDepth")
    public Integer connectionDepth;

    @JsonProperty("abuseType")
    public String abuseType;

    @JsonProperty("connectionStrengthToInitial")
    public Double connectionStrengthToInitial;

    @JsonProperty("connectionScore")
    public Double connectionScore;

    @JsonProperty("connectionScoreToInitial")
    public Double connectionScoreToInitial;

    public GetConnectionsResponse() {
    }

    public GetConnectionsResponse(String clientIdFrom, String clientIdTo, Double connectionStrength,
            ConnectionDetail[] connectionDetail, String connectionType, Integer connectionDepth, String abuseType,
            Double connectionStrengthToInitial, Double connectionScore, Double connectionScoreToInitial) {
        this.clientIdFrom = clientIdFrom;
        this.clientIdTo = clientIdTo;
        this.connectionStrength = connectionStrength;
        this.connectionDetail = connectionDetail;
        this.connectionType = connectionType;
        this.connectionDepth = connectionDepth;
        this.abuseType = abuseType;
        this.connectionStrengthToInitial = connectionStrengthToInitial;
        this.connectionScore = connectionScore;
        this.connectionScoreToInitial = connectionScoreToInitial;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetConnectionsResponse that = (GetConnectionsResponse) o;
        return Objects.equals(clientIdFrom, that.clientIdFrom) && Objects.equals(clientIdTo, that.clientIdTo) && Objects.deepEquals(connectionDetail, that.connectionDetail) && Objects.equals(connectionType, that.connectionType) && Objects.equals(connectionDepth, that.connectionDepth) && Objects.equals(abuseType, that.abuseType) && Objects.equals(roundDouble(connectionScore, 4), roundDouble(that.connectionScore, 4)) && Objects.equals(roundDouble(connectionScoreToInitial, 4), roundDouble(that.connectionScoreToInitial, 4));
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientIdFrom, clientIdTo, Arrays.hashCode(connectionDetail), connectionType, connectionDepth, abuseType, roundDouble(connectionScore, 4), roundDouble(connectionScoreToInitial, 4));
    }

    public static class ConnectionDetail {

        @JsonProperty("connectionAttributeName")
        public String connectionAttributeName;

        @JsonProperty("connectionAttributeValue")
        public String connectionAttributeValue;

        @JsonProperty("sourceAttributeValue")
        public String sourceAttributeValue;

        @JsonProperty("relationType")
        public String relationType;

        public ConnectionDetail() {
        }

        public ConnectionDetail(String connectionAttributeName, String connectionAttributeValue,
                String sourceAttributeValue, String relationType) {
            this.connectionAttributeName = connectionAttributeName;
            this.connectionAttributeValue = connectionAttributeValue;
            this.sourceAttributeValue = sourceAttributeValue;
            this.relationType = relationType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ConnectionDetail that = (ConnectionDetail) o;
            return Objects.equals(connectionAttributeName, that.connectionAttributeName) && Objects.equals(connectionAttributeValue, that.connectionAttributeValue) && Objects.equals(sourceAttributeValue, that.sourceAttributeValue) && Objects.equals(relationType, that.relationType);
        }

        @Override
        public int hashCode() {
            return Objects.hash(connectionAttributeName, connectionAttributeValue, sourceAttributeValue, relationType);
        }

        @Override
        public String toString() {
            return "ConnectionDetail{" + "connectionAttributeName='" + connectionAttributeName + '\'' + ", connectionAttributeValue='" + connectionAttributeValue + '\'' + ", sourceAttributeValue='" + sourceAttributeValue + '\'' + ", relationType='" + relationType + '\'' + '}';
        }
    }

    @Override
    public String toString() {
        return "GetConnectionsResponse{" + "clientIdFrom='" + clientIdFrom + '\'' + ", clientIdTo='" + clientIdTo + '\'' + ", connectionStrength=" + connectionStrength + ", connectionDetail=" + Arrays.toString(connectionDetail) + ", connectionType='" + connectionType + '\'' + ", connectionDepth=" + connectionDepth + ", abuseType='" + abuseType + '\'' + ", connectionStrengthToInitial=" + connectionStrengthToInitial + ", connectionScore=" + connectionScore + ", connectionScoreToInitial=" + connectionScoreToInitial + '}';
    }
}
