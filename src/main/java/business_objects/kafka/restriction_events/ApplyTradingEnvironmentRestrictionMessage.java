package business_objects.kafka.restriction_events;

import java.math.BigInteger;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplyTradingEnvironmentRestrictionMessage {
    private OffsetDateTime timestamp;
    private UUID messageId;
    private BigInteger clientId;
    private String brand;
    private BigInteger accountId;
    private Integer serverId;
    private Integer initialBanDurationInMinutes;
    private String modifier;
    private TradingEnvironmentRestriction restriction;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TradingEnvironmentRestriction {
        private Long restrictionId;
        private String restrictionCode;
        private String riskLevel;
    }
}
