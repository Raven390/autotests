package businessObjects.kafka.mtDbEvents.rafBalanceOrder;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 * Confluence link - https://vantagefx-hytechs.atlassian.net/wiki/spaces/AntiFraud/pages/1112440834/RAF+balance+order
 */
public class RafBalanceOrderMtDbEventMt5 {
    @JsonProperty("data")
    public RafBalanceOrderMtDbEventMt5Data data;

    @JsonProperty("metadata")
    public RafBalanceOrderMtDbEventMetadata metadata;

    public RafBalanceOrderMtDbEventMt5(
                                       RafBalanceOrderMtDbEventMt5Data data, RafBalanceOrderMtDbEventMetadata metadata) {
        this.data = data;
        this.metadata = metadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RafBalanceOrderMtDbEventMt5 that = (RafBalanceOrderMtDbEventMt5) o;
        return Objects.equals(data, that.data) && Objects.equals(metadata, that.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, metadata);
    }

    @Override
    public String toString() {
        return "RafBalanceOrderMtDbEvent{" + "data=" + data + ", metadata=" + metadata + '}';
    }
}
