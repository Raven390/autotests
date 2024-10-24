package helpers.kafka.crmDbEvents.eventGeneratorInbound.withdrawal;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class WithdrawalDbEventCps {

    @JsonProperty("data")
    public WithdrawalDbEventCpsData data;

    @JsonProperty("metadata")
    public WithdrawalDbEventMetadata metadata;

    public WithdrawalDbEventCps() {
    }

    public WithdrawalDbEventCps(WithdrawalDbEventCpsData data, WithdrawalDbEventMetadata metadata) {
        this.data = data;
        this.metadata = metadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WithdrawalDbEventCps that = (WithdrawalDbEventCps) o;
        return Objects.equals(data, that.data) && Objects.equals(metadata, that.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, metadata);
    }

    @Override
    public String toString() {
        return "WithdrawalDbEventCps{" +
                "data=" + data +
                ", metadata=" + metadata +
                '}';
    }
}
