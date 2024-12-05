package businessObjects.api.mitigationService;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.Objects;

public class CancelConfirmedKafkaMessage {

    @JsonProperty("cancelAt")
    public String cancelAt;
    @JsonProperty("restrictions")
    public Restriction[] restrictions;

    public CancelConfirmedKafkaMessage() {
    }

    public CancelConfirmedKafkaMessage(String cancelAt, Restriction[] restrictions) {
        this.cancelAt = cancelAt;
        this.restrictions = restrictions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CancelConfirmedKafkaMessage that = (CancelConfirmedKafkaMessage) o;
        return Objects.equals(cancelAt, that.cancelAt) && Objects.deepEquals(restrictions, that.restrictions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cancelAt, Arrays.hashCode(restrictions));
    }

    @Override
    public String toString() {
        return "CancelConfirmedKafkaMessage{" + "cancelAt='" + cancelAt + '\'' + ", restrictions=" + Arrays.toString(restrictions) + '}';
    }

    public static class Restriction {

        @JsonProperty("restrictionId")
        public Integer restrictionId;
        @JsonProperty("restrictionStatus")
        public String restrictionStatus;

        public Restriction() {
        }

        public Restriction(Integer restrictionId, String restrictionStatus) {
            this.restrictionId = restrictionId;
            this.restrictionStatus = restrictionStatus;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Restriction that = (Restriction) o;
            return Objects.equals(restrictionId, that.restrictionId) && Objects.equals(restrictionStatus, that.restrictionStatus);
        }

        @Override
        public int hashCode() {
            return Objects.hash(restrictionId, restrictionStatus);
        }

        @Override
        public String toString() {
            return "Restriction{" + "restrictionId=" + restrictionId + ", restrictionStatus='" + restrictionStatus + '\'' + '}';
        }
    }
}
