package business_objects.api.connection_search_api.get_connections_by_payout;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetConnectionsByPayoutResponse {

    @JsonProperty("ucid")
    private String ucid;
}
