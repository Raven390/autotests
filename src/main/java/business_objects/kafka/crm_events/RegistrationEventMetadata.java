package business_objects.kafka.crm_events;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class RegistrationEventMetadata {
    @JsonProperty("created")
    public String created;

    public RegistrationEventMetadata() {
    }

    public RegistrationEventMetadata(String created) {
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RegistrationEventMetadata that = (RegistrationEventMetadata) o;
        return Objects.equals(created, that.created);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(created);
    }

    @Override
    public String toString() {
        return "RegistrationEventMetadata{" + "created='" + created + '\'' + '}';
    }
}
