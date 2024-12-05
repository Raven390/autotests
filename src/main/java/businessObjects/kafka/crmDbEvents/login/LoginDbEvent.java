package businessObjects.kafka.crmDbEvents.login;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class LoginDbEvent {

    @JsonProperty("data")
    public LoginDbEventData data;

    @JsonProperty("metadata")
    public LoginDbEventMetadata metadata;

    public LoginDbEvent() {
    }

    public LoginDbEvent(LoginDbEventData data, LoginDbEventMetadata metadata) {
        this.data = data;
        this.metadata = metadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginDbEvent that = (LoginDbEvent) o;
        return Objects.equals(data, that.data) && Objects.equals(metadata, that.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, metadata);
    }

    @Override
    public String toString() {
        return "LoginDbEvent{" + "data=" + data + ", metadata=" + metadata + '}';
    }
}
