package business_objects.api.mitigation_service;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class DeleteTradingEnvRestrictionRequestBody {
    @JsonProperty("type")
    private RestrictionType type;

    @JsonProperty("ucid")
    private String ucid;

    @JsonProperty("code")
    private String code;

    @JsonProperty("cancelReason")
    private String cancelReason;

    @JsonProperty("correlationType")
    private CorrelationType correlationType;

    @JsonProperty("correlationId")
    private String correlationId;

    @JsonProperty("updatedBy")
    private UpdatedBy updatedBy;

    @JsonProperty("accountId")
    private BigInteger accountId;

    @JsonProperty("serverId")
    private Integer serverId;
}
