package business_objects.kafka.mt_db_events.raf_balance_order;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 * Confluence link - https://vantagefx-hytechs.atlassian.net/wiki/spaces/AntiFraud/pages/1112440834/RAF+balance+order
 */
public class RafBalanceOrderMtDbEventMt4 {
    @JsonProperty("data")
    RafBalanceOrderMtDbEventMt4Data data;

    @JsonProperty("metadata")
    RafBalanceOrderMtDbEventMetadata metadata;

    public RafBalanceOrderMtDbEventMt4(
            RafBalanceOrderMtDbEventMt4Data data, RafBalanceOrderMtDbEventMetadata metadata) {
        this.data = data;
        this.metadata = metadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RafBalanceOrderMtDbEventMt4 that = (RafBalanceOrderMtDbEventMt4) o;
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

    public RafBalanceOrderMtDbEventMt4Data getData() {
        return data;
    }

    public void setData(RafBalanceOrderMtDbEventMt4Data data) {
        this.data = data;
    }

    public RafBalanceOrderMtDbEventMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(RafBalanceOrderMtDbEventMetadata metadata) {
        this.metadata = metadata;
    }
}
