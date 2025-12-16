package business_objects.kafka.crm_events.CallbackEvent;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Attributes {

    @JsonProperty("card_holder_name")
    private String cardHolderName;

    @JsonProperty("is_declined_due_to_funds")
    private boolean declinedDueToFunds;

    private String description;

    @JsonProperty("created_at")
    private long createdAt;

    @JsonProperty("mid_alias")
    private String midAlias;

    @JsonProperty("is_network_token_used")
    private boolean networkTokenUsed;

    private Source source;

    @JsonProperty("card_masked_number")
    private String cardMaskedNumber;

    private Verifications verifications;

    @JsonProperty("is3_d")
    private boolean is3d;

    @JsonProperty("updated_at")
    private long updatedAt;

    @JsonProperty("issuer_scheme_id")
    private String issuerSchemeId;

    @JsonProperty("is_hard_decline")
    private boolean hardDecline;

    @JsonProperty("card_brand")
    private String cardBrand;

    private String currency;

    @JsonProperty("payment_method")
    private String paymentMethod;

    @JsonProperty("credit_card_token")
    private String creditCardToken;

    private double amount;

    @JsonProperty("card_number")
    private String cardNumber;

    @JsonProperty("card_expiration")
    private String cardExpiration;

    @JsonProperty("live_mode")
    private boolean liveMode;

    @JsonProperty("installment_details")
    private Object installmentDetails;

    @JsonProperty("psp_response_data")
    private Object pspResponseData;

    @JsonProperty("custom_data")
    private CustomData customData;

    @JsonProperty("is_cancelled")
    private boolean cancelled;

    private String status;

    private Customer customer;
}
