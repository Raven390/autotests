package businessObjects.api.clickhouseApiService.getLexisNexisData;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

public class GetLexisNexisDataResponse {

    public List<Items> items;

    @JsonProperty("totalCount")
    public Integer totalCount;

    public static class Items {
        @JsonProperty("uid")
        public String uid;

        @JsonProperty("mobile_code")
        public Integer mobile_code;

        @JsonProperty("eventId")
        public Integer eventId;

        public Items() {
        }

        public Items(String uid) {
            this.uid = uid;
        }

        public Items(String uid, Integer mobile_code, Integer eventId) {
            this.uid = uid;
            this.mobile_code = mobile_code;
            this.eventId = eventId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Items items = (Items) o;
            return Objects.equals(uid, items.uid) && Objects.equals(mobile_code, items.mobile_code) && Objects.equals(
                    eventId, items.eventId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(uid, mobile_code, eventId);
        }
    }

    public static Items getItem(String uid) {
        return new Items(uid);
    }

    public static Items getItem(String uid, Integer mobile_code, Integer eventId) {
        return new Items(uid, mobile_code, eventId);
    }
}
