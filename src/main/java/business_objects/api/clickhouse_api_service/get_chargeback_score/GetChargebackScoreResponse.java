package business_objects.api.clickhouse_api_service.get_chargeback_score;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetChargebackScoreResponse {

    @JsonProperty("ratioAbuse")
    private Double ratioAbuse;
}
