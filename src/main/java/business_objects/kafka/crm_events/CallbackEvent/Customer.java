package business_objects.kafka.crm_events.CallbackEvent;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Customer {
    private String country;
    private String address;
    private String city;
    private String phone;

    @JsonProperty("extra_data")
    private Object extraData;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("zip_code")
    private String zipCode;
}
