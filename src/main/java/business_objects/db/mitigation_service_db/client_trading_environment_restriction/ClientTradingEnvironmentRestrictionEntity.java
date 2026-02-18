package business_objects.db.mitigation_service_db.client_trading_environment_restriction;

import business_objects.api.mitigation_service.RestrictionStatus;
import helpers.data.enums.TradingEnvironmentLevel;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class ClientTradingEnvironmentRestrictionEntity {

    private Integer id;
    private String ucid;
    private String regulator;
    private Long accountId;
    private Integer serverId;
    private Integer restrictionId;
    private TradingEnvironmentLevel level;
    private RestrictionStatus status;
    private String comment;
    private String applicationReason;
    private String cancellationReason;
    private String correlationType;
    private String correlationId;
    private String failReason;
    private String createdAt;
    private String updatedAt;
}
