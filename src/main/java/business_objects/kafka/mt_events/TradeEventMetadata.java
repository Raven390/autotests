package business_objects.kafka.mt_events;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class TradeEventMetadata {
    @JsonProperty("created")
    public String created;

    @JsonProperty("platform")
    public String platform;

    public TradeEventMetadata() {
    }

    public TradeEventMetadata(String created) {
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TradeEventMetadata that = (TradeEventMetadata) o;
        return Objects.equals(created, that.created);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(created);
    }

    @Override
    public String toString() {
        return "TradeEventMetadata{" + "created='" + created + '\'' + '}';
    }
}
