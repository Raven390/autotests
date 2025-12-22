package business_objects.db.audit_service_db;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;

@Data
public class AuditEvent {

    private Long id;
    private UUID messageId;
    private String ucid;
    private String type;
    private String name;
    private String actorType;
    private String actorName;
    private String sourceService;
    private String correlationType;
    private String correlationId;
    private LocalDateTime happenedAt;
    private String notes;
    private String payload;
    private String alertId;
    private String alertReason;
    private String trigger;
    private String investigationType;
    private String alertPriority;
    private String account;
    private String serverId;
    private String symbol;
    private String amount;
    private String amountUsd;
    private String currency;
    private String paymentMethod;
    private String investigator;
    private String transferId;
    private String paymentDecision;
    private String paymentDecisionReason;
    private String comment;
    private String restrictionId;
    private String restriction;
    private String fraudTypeName;
    private String fraudTypeStatus;
    private String clientStatus;
    private String illegalProfit;
    private String suggestedDeduction;
    private String serverName;
    private Boolean oldFormat;
    private String illegalProfitCurrency;
    private String suggestedDeductionCurrency;
    private Boolean suggestedDeductionHold;
}
