package business_objects.kafka.crm_events;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class CrmEventMetadata {
    @JsonProperty("created")
    public String created;

    @JsonProperty("platform")
    public String platform;

    public CrmEventMetadata() {}

    public CrmEventMetadata(String created) {
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CrmEventMetadata that)) return false;
        return Objects.equals(created, that.created) && Objects.equals(platform, that.platform);
    }

    @Override
    public int hashCode() {
        return Objects.hash(created, platform);
    }

    @Override
    public String toString() {
        return "CrmEventMetadata{" + "created='" + created + '\'' + ", platform='" + platform + '\'' + '}';
    }
}
