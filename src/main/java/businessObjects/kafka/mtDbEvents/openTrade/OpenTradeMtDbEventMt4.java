package businessObjects.kafka.mtDbEvents.openTrade;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 * Confluence link - https://vantagefx-hytechs.atlassian.net/wiki/spaces/AntiFraud/pages/1104118020/Open+trade
 */
public class OpenTradeMtDbEventMt4 {
    @JsonProperty("data")
    public OpenTradeMtDbEventMt4Data data;

    @JsonProperty("metadata")
    public OpenTradeMtDbEventMetadata metadata;

    public OpenTradeMtDbEventMt4(OpenTradeMtDbEventMt4Data data, OpenTradeMtDbEventMetadata metadata) {
        this.data = data;
        this.metadata = metadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpenTradeMtDbEventMt4 that = (OpenTradeMtDbEventMt4) o;
        return Objects.equals(data, that.data) && Objects.equals(metadata, that.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, metadata);
    }

    @Override
    public String toString() {
        return "OpenTradeMtDbEvent{" + "data=" + data + ", metadata=" + metadata + '}';
    }
}
