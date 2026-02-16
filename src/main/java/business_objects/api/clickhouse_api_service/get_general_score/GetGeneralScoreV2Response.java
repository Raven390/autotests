package business_objects.api.clickhouse_api_service.get_general_score;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetGeneralScoreV2Response {

    @JsonProperty("modelScore")
    public Double modelScore;

    @JsonProperty("ucidScore")
    public Double ucidScore;

    @JsonProperty("avgFivePastUcidScore")
    public Double avgFivePastUcidScore;

    @JsonProperty("avgPastUcidScore")
    public Double avgPastUcidScore;
}
