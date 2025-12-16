package business_objects.kafka.crm_events.CallbackEvent;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CallbackEvent {
    private String id;
    private String schemaVersion;
    private String timestamp;
    private String eventDate;
    private String type;
    private String paymentProvider;
    private String messageId;
    private Long clientId;
    private String regulator;
    private String brand;
    private String businessOrderId;
    private String paymentMethodCode;
    private Callback callback;
}