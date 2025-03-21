package business_objects.api.mitigation_service;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.Objects;

public class ApplyConfirmedKafkaMessage {

    @JsonProperty("processedAt")
    public String processedAt;
    @JsonProperty("restrictions")
    public Restriction[] restrictions;

    public ApplyConfirmedKafkaMessage() {
    }

    public ApplyConfirmedKafkaMessage(String processedAt, Restriction[] restrictions) {
        this.processedAt = processedAt;
        this.restrictions = restrictions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApplyConfirmedKafkaMessage that = (ApplyConfirmedKafkaMessage) o;
        return Objects.equals(processedAt, that.processedAt) && Objects.deepEquals(restrictions, that.restrictions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(processedAt, Arrays.hashCode(restrictions));
    }

    @Override
    public String toString() {
        return "ApplyConfirmedKafkaMessage{" + "processedAt='" + processedAt + '\'' + ", restrictions=" + Arrays.toString(restrictions) + '}';
    }

    public static class Restriction {

        @JsonProperty("restrictionId")
        public Integer restrictionId;
        @JsonProperty("restrictionStatus")
        public String restrictionStatus;
        @JsonProperty("failReason")
        public String failReason;

        public Restriction() {
        }

        public Restriction(Integer restrictionId, String restrictionStatus, String failReason) {
            this.restrictionId = restrictionId;
            this.restrictionStatus = restrictionStatus;
            this.failReason = failReason;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Restriction that = (Restriction) o;
            return Objects.equals(restrictionId, that.restrictionId) && Objects.equals(restrictionStatus, that.restrictionStatus) && Objects.equals(failReason, that.failReason);
        }

        @Override
        public int hashCode() {
            return Objects.hash(restrictionId, restrictionStatus, failReason);
        }

        @Override
        public String toString() {
            return "Restriction{" + "restrictionId=" + restrictionId + ", restrictionStatus='" + restrictionStatus + '\'' + ", failReason='" + failReason + '\'' + '}';
        }
    }
}
