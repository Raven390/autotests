package business_objects.kafka.mt_db_events.close_trade;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 * Confluence link - https://vantagefx-hytechs.atlassian.net/wiki/spaces/AntiFraud/pages/1096351889/Close+trade
 */
public class CloseTradeMtDbEventMt4 {
    @JsonProperty("data")
    public CloseTradeMtDbEventMt4Data data;

    @JsonProperty("metadata")
    public CloseTradeMtDbEventMetadata metadata;

    public CloseTradeMtDbEventMt4(CloseTradeMtDbEventMt4Data data, CloseTradeMtDbEventMetadata metadata) {
        this.data = data;
        this.metadata = metadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CloseTradeMtDbEventMt4 that = (CloseTradeMtDbEventMt4) o;
        return Objects.equals(data, that.data) && Objects.equals(metadata, that.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, metadata);
    }

    @Override
    public String toString() {
        return "CloseTradeMtDbEvent{" + "data=" + data + ", metadata=" + metadata + '}';
    }
}
