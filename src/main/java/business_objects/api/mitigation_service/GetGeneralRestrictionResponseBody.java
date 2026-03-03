package business_objects.api.mitigation_service;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GetGeneralRestrictionResponseBody extends GetRestrictionResponseBody {

    @JsonProperty("status")
    public String status;
}
