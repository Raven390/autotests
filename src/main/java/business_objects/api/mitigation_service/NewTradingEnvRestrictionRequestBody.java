package business_objects.api.mitigation_service;

import com.fasterxml.jackson.annotation.JsonProperty;
import helpers.data.enums.TradingEnvironmentLevel;
import java.math.BigInteger;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class NewTradingEnvRestrictionRequestBody {
    @JsonProperty("type")
    private RestrictionType type;

    @JsonProperty("ucid")
    private String ucid;

    @JsonProperty("code")
    private String code;

    @JsonProperty("comment")
    private String comment;

    @JsonProperty("correlationType")
    private CorrelationType correlationType;

    @JsonProperty("correlationId")
    private String correlationId;

    @JsonProperty("updatedBy")
    private UpdatedBy updatedBy;

    @JsonProperty("additionalParams")
    private List<AdditionalParamEntry> additionalParams;

    @JsonProperty("accountId")
    private BigInteger accountId;

    @JsonProperty("serverId")
    private Integer serverId;

    @JsonProperty("level")
    private TradingEnvironmentLevel level;

    @JsonProperty("applicationReason")
    private String applicationReason;
}
