package business_objects.ui.audit_trail;

import lombok.*;

@Setter
@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AuditTrailItemV2 {

    private String header;
    private String details;
}
