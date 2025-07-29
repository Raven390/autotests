package business_objects.kafka.mt_events;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class TradeEventMetadata {

    @JsonProperty("platform")
    public String platform;

    public TradeEventMetadata() {
    }

    public TradeEventMetadata(String platform) {
        this.platform = platform;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TradeEventMetadata that = (TradeEventMetadata) o;
        return Objects.equals(platform, that.platform);
    }

    @Override
    public String toString() {
        return "TradeEventMetadata{" + "platform='" + platform + '\'' + '}';
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(platform);
    }
}
