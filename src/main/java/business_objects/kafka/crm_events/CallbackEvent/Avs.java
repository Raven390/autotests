package business_objects.kafka.crm_events.CallbackEvent;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Avs {
    private String result;
}
