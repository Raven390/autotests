package businessObjects.api.connectionSearchApi;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.Objects;

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

    public GetConnectionsResponse() {
    }

    public GetConnectionsResponse(String clientIdFrom, String clientIdTo, Double connectionStrength, ConnectionDetail[] connectionDetail, String connectionType, Integer connectionDepth, String abuseType, Double connectionStrengthToInitial) {
        this.clientIdFrom = clientIdFrom;
        this.clientIdTo = clientIdTo;
        this.connectionStrength = connectionStrength;
        this.connectionDetail = connectionDetail;
        this.connectionType = connectionType;
        this.connectionDepth = connectionDepth;
        this.abuseType = abuseType;
        this.connectionStrengthToInitial = connectionStrengthToInitial;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetConnectionsResponse that = (GetConnectionsResponse) o;
        return Objects.equals(clientIdFrom, that.clientIdFrom) && Objects.equals(clientIdTo, that.clientIdTo) && Objects.equals(connectionStrength, that.connectionStrength) && Objects.deepEquals(connectionDetail, that.connectionDetail) && Objects.equals(connectionType, that.connectionType) && Objects.equals(connectionDepth, that.connectionDepth) && Objects.equals(abuseType, that.abuseType) && Objects.equals(connectionStrengthToInitial, that.connectionStrengthToInitial);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientIdFrom, clientIdTo, connectionStrength, Arrays.hashCode(connectionDetail), connectionType, connectionDepth, abuseType, connectionStrengthToInitial);
    }

    public static class ConnectionDetail {

        @JsonProperty("connectionAttributeName")
        public String connectionAttributeName;

        @JsonProperty("connectionAttributeValue")
        public String connectionAttributeValue;

        public ConnectionDetail() {
        }

        public ConnectionDetail(String connectionAttributeName, String connectionAttributeValue) {
            this.connectionAttributeName = connectionAttributeName;
            this.connectionAttributeValue = connectionAttributeValue;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ConnectionDetail that = (ConnectionDetail) o;
            return Objects.equals(connectionAttributeName, that.connectionAttributeName) && Objects.equals(connectionAttributeValue, that.connectionAttributeValue);
        }

        @Override
        public int hashCode() {
            return Objects.hash(connectionAttributeName, connectionAttributeValue);
        }

        @Override
        public String toString() {
            return "ConnectionDetail{" +
                    "connectionAttributeName='" + connectionAttributeName + '\'' +
                    ", connectionAttributeValue='" + connectionAttributeValue + '\'' +
                    '}';
        }
    }
}
