package business_objects.db.backoffice_db.ai_results;

import java.sql.Timestamp;
import java.util.UUID;
import lombok.Data;

@Data
public class AiAlertResult {
    private Long id;
    private UUID messageId;
    private Timestamp messageTimestamp;
    private Timestamp producedAt;
    private UUID alertId;
    private String alertAiType;
    private String ucid;
    private String account;
    private String serverId;
    private String ruleName;
    private String fraudType;
    private Timestamp createdAt;
    private Boolean trust;
    private String sendAuditStatus;
}
