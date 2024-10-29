package businessObjects.kafka.crmDbEvents.withdrawal;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class WithdrawalDbEvent {
    @JsonProperty(value = "data")
    public WithdrawalDbEventData data;

    @JsonProperty(value = "metadata")
    public WithdrawalDbEventMetadata metadata;

    public WithdrawalDbEvent() {
    }

    public WithdrawalDbEvent(WithdrawalDbEventData data, WithdrawalDbEventMetadata metadata) {
        this.data = data;
        this.metadata = metadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WithdrawalDbEvent that = (WithdrawalDbEvent) o;
        return Objects.equals(data, that.data) && Objects.equals(metadata, that.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, metadata);
    }

    @Override
    public String toString() {
        return "WithdrawalDbEvent{" +
                "data=" + data +
                ", metadata=" + metadata +
                '}';
    }
}
