package business_objects.api.verification_service;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PutProfileStatusRequestBody {

    @JsonProperty("status")
    private String status;

    @JsonProperty("paymentProfileKey")
    private String paymentProfileKey;

    @JsonProperty("paymentProfile")
    private String paymentProfileMasked;

    @JsonProperty("ucid")
    private String ucid;

    @JsonProperty("comment")
    private String comment;

    @JsonProperty("updatedByUsername")
    private String updatedByUsername;

    @JsonProperty("updatedBySystem")
    private String updatedBySystem;
}
