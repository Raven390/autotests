package business_objects.api.clickhouse_api_service.get_lexis_nexis_data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

public class GetLexisNexisDataResponse {

    public List<Items> items;

    @JsonProperty("totalCount")
    public Integer totalCount;

    public static class Items {
        @JsonProperty("id")
        public String id;

        @JsonProperty("mobile_code")
        public Integer mobile_code;

        @JsonProperty("eventId")
        public Integer eventId;

        public Items() {
        }

        public Items(String id) {
            this.id = id;
        }

        public Items(String ucid, Integer mobile_code, Integer eventId) {
            this.id = ucid;
            this.mobile_code = mobile_code;
            this.eventId = eventId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Items items = (Items) o;
            return Objects.equals(id, items.id) && Objects.equals(mobile_code, items.mobile_code) && Objects.equals(eventId, items.eventId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, mobile_code, eventId);
        }

        @Override
        public String toString() {
            return "Items{" + "id='" + id + '\'' + ", mobile_code=" + mobile_code + ", eventId=" + eventId + '}';
        }

        public static Items getItem(String id) {
            return new Items(id);
        }

        public static Items getItem(String id, Integer mobile_code, Integer eventId) {
            return new Items(id, mobile_code, eventId);
        }
    }
}
