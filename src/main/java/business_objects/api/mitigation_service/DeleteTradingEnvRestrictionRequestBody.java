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
    private RestrictionType type = null;

    @JsonProperty("ucid")
    private String ucid = null;

    @JsonProperty("code")
    private String code = null;

    @JsonProperty("cancelReason")
    private String cancelReason = null;

    @JsonProperty("correlationType")
    private CorrelationType correlationType = null;

    @JsonProperty("correlationId")
    private String correlationId = null;

    @JsonProperty("updatedBy")
    private UpdatedBy updatedBy = null;

    @JsonProperty("accountId")
    private BigInteger accountId = null;

    @JsonProperty("serverId")
    private Integer serverId = null;
}
