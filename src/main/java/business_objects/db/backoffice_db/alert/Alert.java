package business_objects.db.backoffice_db.alert;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class Alert {

    private Long id;
    private String String;
    private String happenedAt;
    private String receivedAt;
    private String closedAt;
    private String status;
    private String rule;
    private String trigger;
    private String ruleVersion;
    private String fraudType;
    private String ruleAttributes;
    private String alertResolution;
    private boolean highPriority;
    private String investigatorId;
    private Long resolutionQualityId;
    private String type;
    private Long investigationId;
    private String clientUcid;
    private String triggerHappenedAt;
    private String paymentEventId;
    private String account;
    private String paymentMethod;
    private BigDecimal amount;
    private String serverId;
    private String currency;
    private BigDecimal amountUsd;
    private String reason;
    private String merchantOrderId;
    private String symbol;
}
