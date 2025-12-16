package business_objects.kafka.crm_events.CallbackEvent;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CallbackData {

    @JsonProperty("psp_name")
    private String pspName;

    private Charge charge;

    @JsonProperty("order_id")
    private String orderId;
}
