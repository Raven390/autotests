package business_objects.api.clickhouse_api_service.get_market_manipulator_flag;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetMarketManipulatorFlagResponse {

    @JsonProperty("consecutiveFlag")
    private Boolean consecutiveFlag;

    @JsonProperty("consecutiveDeals")
    private Double consecutiveDeals;

    @JsonProperty("leverageFlag")
    private Boolean leverageFlag;

    @JsonProperty("profitFlag")
    private Boolean profitFlag;

    @JsonProperty("realLeverage")
    private Double realLeverage;

    @JsonProperty("totalScore")
    private Double totalScore;

    @JsonProperty("toxicityFlag")
    private Boolean toxicityFlag;

    @JsonProperty("toxicityRatio")
    private Double toxicityRatio;

    @JsonProperty("toxicityUsd")
    private Double toxicityUsd;
}
