package business_objects.kafka.crm_events.CallbackEvent;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Callback {
    private Webhook webhook;
    private CallbackData data;
    private Meta meta;
}
