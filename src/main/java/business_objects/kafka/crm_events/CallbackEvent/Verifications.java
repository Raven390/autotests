package business_objects.kafka.crm_events.CallbackEvent;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Verifications {
    private String cavv;
    private Avs avs;
}
