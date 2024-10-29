package helpers.kafka.crmDbEvents.registration;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class RegistrationDbEvent {

    @JsonProperty("data")
    public RegistrationDbEventData data;

    @JsonProperty("metadata")
    public RegistrationDbEventMetadata metadata;

    public RegistrationDbEvent() {
    }

    public RegistrationDbEvent(RegistrationDbEventData data, RegistrationDbEventMetadata metadata) {
        this.data = data;
        this.metadata = metadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegistrationDbEvent that = (RegistrationDbEvent) o;
        return Objects.equals(data, that.data) && Objects.equals(metadata, that.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, metadata);
    }

    @Override
    public String toString() {
        return "RegistrationDbEvent{" +
                "data=" + data +
                ", metadata=" + metadata +
                '}';
    }
}
