package business_objects.db.verification_service_db;

import helpers.data.enums.VerificationStatus;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationHistory {
    private Long id;
    private String ucid;
    private String paymentProfileKey;
    private VerificationStatus status;
    private String comment;
    private String changedByUsername;
    private String changedBySystem;
    private OffsetDateTime changedAt;
}
