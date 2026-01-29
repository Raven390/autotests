package business_objects.db.backoffice_db.InvestigationHistory;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvestigationHistoryObject {
    Long id;
    Long investigationId;
    String action;
    OffsetDateTime happenedAt;
    String actorUserId;
    String attributes;
    String assignedToUserId;
    String reassignedFromUserId;
}
