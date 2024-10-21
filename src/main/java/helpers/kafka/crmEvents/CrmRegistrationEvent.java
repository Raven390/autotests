package helpers.kafka.crmEvents;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CrmRegistrationEvent {
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

    @JsonProperty("referrerId")
    public Integer referrerId;

    @JsonProperty("metaTraderAccount")
    public Integer metaTraderAccount;

    @JsonProperty("ipAddress")
    public String ipAddress;

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

    @JsonProperty("createTime")
    public String createTime;

    @JsonProperty("birthday")
    public String birthday;

    @JsonProperty("currency")
    public String currency;

    @JsonProperty("metaTraderAccountType")
    public String metaTraderAccountType;

    public static CrmRegistrationEvent getRegistrationEvent(String schemaVersion, String brand, String regulator, String type, String id, Integer clientId, Integer referrerId, Integer metaTraderAccount, String ipAddress, String firstName, String lastName, String email, String phoneNumber, String nationalityCode, String residencyCode, String createTime, String birthday, String currency, String metaTraderAccountType) {
        CrmRegistrationEvent event = new CrmRegistrationEvent();
        event.schemaVersion = schemaVersion;
        event.brand = brand;
        event.regulator = regulator;
        event.type = type;
        event.id = id;
        event.clientId = clientId;
        event.referrerId = referrerId;
        event.metaTraderAccount = metaTraderAccount;
        event.ipAddress = ipAddress;
        event.firstName = firstName;
        event.lastName = lastName;
        event.email = email;
        event.phoneNumber = phoneNumber;
        event.nationalityCode = nationalityCode;
        event.residencyCode = residencyCode;
        event.createTime = createTime;
        event.birthday = birthday;
        event.currency = currency;
        event.metaTraderAccountType = metaTraderAccountType;
        return event;
    }
}
