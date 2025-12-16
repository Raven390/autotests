package business_objects.kafka.crm_events.CallbackEvent;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Charge {

    @JsonProperty("operation_type")
    private String operationType;

    private String type;
    private String uuid;

    @JsonProperty("refund_id")
    private String refundId;

    @JsonProperty("deposit_source")
    private String depositSource;

    @JsonProperty("psp_order_id")
    private String pspOrderId;

    @JsonProperty("is_refundable")
    private boolean refundable;

    @JsonProperty("mid_type")
    private String midType;

    private Attributes attributes;

    private String id;

    @JsonProperty("is_recurring")
    private boolean recurring;
}
