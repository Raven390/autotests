package helpers.kafka.crmEvents;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CrmDetailChangeEvent {
    @JsonProperty("schemaVersion")
    public String schemaVersion;

    @JsonProperty("brand")
    public String brand;

    @JsonProperty("regulator")
    public String regulator;

    @JsonProperty("type")
    public String type;

    @JsonProperty("id")
    public String id;

    @JsonProperty("clientId")
    public Integer clientId;

    @JsonProperty("metaTraderAccount")
    public Integer metaTraderAccount;

    @JsonProperty("proofOfAddressStatus")
    public Boolean proofOfAddressStatus;

    @JsonProperty("proofOfIdentityStatus")
    public Boolean proofOfIdentityStatus;

    @JsonProperty("firstName")
    public String firstName;

    @JsonProperty("lastName")
    public String lastName;

    @JsonProperty("email")
    public String email;

    @JsonProperty("phoneNumber")
    public String phoneNumber;

    @JsonProperty("nationalityCode")
    public String nationalityCode;

    @JsonProperty("residencyCode")
    public String residencyCode;

    @JsonProperty("idNumber")
    public String idNumber;

    @JsonProperty("eventTime")
    public String eventTime;

    @JsonProperty("birthday")
    public String birthday;

    @JsonProperty("currency")
    public String currency;

    @JsonProperty("metaTraderAccountType")
    public String metaTraderAccountType;

    public static CrmDetailChangeEvent getDetailChangeEvent(
            String schemaVersion,
            String brand,
            String regulator,
            String type,
            String id,
            Integer clientId,
            Integer metaTraderAccount,
            Boolean proofOfAddressStatus,
            Boolean proofOfIdentityStatus,
            String lastName,
            String firstName,
            String email,
            String phoneNumber,
            String nationalityCode,
            String residencyCode,
            String idNumber,
            String eventTime,
            String birthday,
            String currency,
            String metaTraderAccountType) {
        CrmDetailChangeEvent event = new CrmDetailChangeEvent();
        event.schemaVersion = schemaVersion;
        event.brand = brand;
        event.regulator = regulator;
        event.type = type;
        event.id = id;
        event.clientId = clientId;
        event.metaTraderAccount = metaTraderAccount;
        event.proofOfAddressStatus = proofOfAddressStatus;
        event.proofOfIdentityStatus = proofOfIdentityStatus;
        event.firstName = firstName;
        event.lastName = lastName;
        event.email = email;
        event.phoneNumber = phoneNumber;
        event.nationalityCode = nationalityCode;
        event.residencyCode = residencyCode;
        event.idNumber = idNumber;
        event.eventTime = eventTime;
        event.birthday = birthday;
        event.currency = currency;
        event.metaTraderAccountType = metaTraderAccountType;
        return event;
    }
}
