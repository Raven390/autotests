package helpers.kafka.mtDbEvents.closeTrade;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 * Confluence link - https://vantagefx-hytechs.atlassian.net/wiki/spaces/AntiFraud/pages/1096351889/Close+trade
 */
public class CloseTradeMtDbEventMt5 {
    @JsonProperty("data")
    public CloseTradeMtDbEventMt5Data data;

    @JsonProperty("metadata")
    public CloseTradeMtDbEventMetadata metadata;

    public CloseTradeMtDbEventMt5(CloseTradeMtDbEventMt5Data data, CloseTradeMtDbEventMetadata metadata) {
        this.data = data;
        this.metadata = metadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CloseTradeMtDbEventMt5 that = (CloseTradeMtDbEventMt5) o;
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
