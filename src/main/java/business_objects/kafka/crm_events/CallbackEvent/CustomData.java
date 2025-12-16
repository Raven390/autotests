package business_objects.kafka.crm_events.CallbackEvent;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CustomData {

    @JsonProperty("three_d_authentication_result")
    private String threeDAuthResult;

    @JsonProperty("three_ds_transaction_id")
    private String threeDsTransactionId;

    @JsonProperty("initial_transaction_id")
    private String initialTransactionId;
}
