package business_objects.db.mitigation_service_db.client_trading_environment_restriction;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientTradingEnvironmentRestrictionEntity {

    private Integer id;
    private String ucid;
    private String regulator;
    private Long accountId;
    private Integer serverId;
    private Integer restrictionId;
    private String level;
    private String status;
    private String comment;
    private String applicationReason;
    private String cancellationReason;
    private String correlationType;
    private String correlationId;
    private String failReason;
    private String createdAt;
    private String updatedAt;
}
