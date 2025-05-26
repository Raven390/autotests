package business_objects.kafka.crm_events;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class RegistrationEvent {

    @JsonProperty("schemaVersion")
    public String schemaVersion;

    @JsonProperty("id")
    public String id;

    @JsonProperty("createTime")
    public String createTime;

    @JsonProperty("clientId")
    public Integer clientId;

    @JsonProperty("brand")
    public String brand;

    @JsonProperty("regulator")
    public String regulator;

    @JsonProperty("metaTraderAccount")
    public Integer metaTraderAccount;

    @JsonProperty("type")
    public String type;

    @JsonProperty("eventDate")
    public String eventDate;

    @JsonProperty("metadata")
    public CrmEventMetadata metadata;

    @JsonProperty("initialEventTime")
    public String initialEventTime;

    @JsonProperty("referrerId")
    public Integer referrerId;

    @JsonProperty("firstName")
    public String firstName;

    @JsonProperty("lastName")
    public String lastName;

    @JsonProperty("email")
    public String email;

    @JsonProperty("phoneNumber")
    public String phoneNumber;

    @JsonProperty("birthday")
    public String birthday;

    @JsonProperty("firstLanguage")
    public String firstLanguage;

    @JsonProperty("nationalityCode")
    public String nationalityCode;

    @JsonProperty("residencyCode")
    public String residencyCode;

    @JsonProperty("websiteUserType")
    public String websiteUserType;

    @JsonProperty("lexisNexis")
    public LexisNexis lexisNexis;

    @JsonProperty("ibId")
    public int ibId;

    @JsonProperty("cpaId")
    public int cpaId;

    public static class LexisNexis {

        @JsonProperty("trueIp")
        public String trueIp;

        @JsonProperty("device")
        public String device;

        @JsonProperty("digitalId")
        public String digitalId;

        @JsonProperty("sessionId")
        public String sessionId;

        @JsonProperty("webSessionId")
        public String webSessionId;

        @JsonProperty("policyScore")
        public Integer policyScore;

        @JsonProperty("rawResponse")
        public String rawResponse;

        @JsonProperty("riskRating")
        public String riskRating;

        @Override
        public String toString() {
            return "LexisNexis{" + "trueIp='" + trueIp + '\'' + ", device='" + device + '\'' + ", digitalId='" + digitalId + '\'' + ", sessionId='" + sessionId + '\'' + ", webSessionId='" + webSessionId + '\'' + ", policyScore=" + policyScore + ", rawResponse='" + rawResponse + '\'' + ", riskRating='" + riskRating + '\'' + '}';
        }
    }

    public RegistrationEvent(
            String createTime, Integer clientId, String brand, String regulator, Integer metaTraderAccount,
            String initialEventTime, String type, String eventDate) {
        this.createTime = createTime;
        this.clientId = clientId;
        this.brand = brand;
        this.regulator = regulator;
        this.metaTraderAccount = metaTraderAccount;
        this.initialEventTime = initialEventTime;
        this.type = type;
        this.eventDate = eventDate;
    }

    public RegistrationEvent() {
    }

    @Override
    public String toString() {
        return "RegistrationEvent{" + "schemaVersion='" + schemaVersion + '\'' + ", id='" + id + '\'' + ", createTime='" + createTime + '\'' + ", clientId=" + clientId + ", brand='" + brand + '\'' + ", regulator='" + regulator + '\'' + ", metaTraderAccount=" + metaTraderAccount + ", type='" + type + '\'' + ", eventDate='" + eventDate + '\'' + ", metadata=" + metadata + ", initialEventTime='" + initialEventTime + '\'' + ", referrerId=" + referrerId + ", firstName='" + firstName + '\'' + ", lastName='" + lastName + '\'' + ", email='" + email + '\'' + ", phoneNumber='" + phoneNumber + '\'' + ", birthday='" + birthday + '\'' + ", firstLanguage='" + firstLanguage + '\'' + ", nationalityCode='" + nationalityCode + '\'' + ", residencyCode='" + residencyCode + '\'' + ", websiteUserType='" + websiteUserType + '\'' + ", lexisNexis=" + lexisNexis.toString() + ", ibId=" + ibId + ", cpaId=" + cpaId + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegistrationEvent that = (RegistrationEvent) o;
        return ibId == that.ibId && cpaId == that.cpaId && Objects.equals(schemaVersion, that.schemaVersion) && Objects.equals(id, that.id) && Objects.equals(createTime, that.createTime) && Objects.equals(clientId, that.clientId) && Objects.equals(brand, that.brand) && Objects.equals(regulator, that.regulator) && Objects.equals(metaTraderAccount, that.metaTraderAccount) && Objects.equals(type, that.type) && Objects.equals(eventDate, that.eventDate) && Objects.equals(metadata, that.metadata) && Objects.equals(initialEventTime, that.initialEventTime) && Objects.equals(referrerId, that.referrerId) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(email, that.email) && Objects.equals(phoneNumber, that.phoneNumber) && Objects.equals(birthday, that.birthday) && Objects.equals(firstLanguage, that.firstLanguage) && Objects.equals(nationalityCode, that.nationalityCode) && Objects.equals(residencyCode, that.residencyCode) && Objects.equals(websiteUserType, that.websiteUserType) && Objects.equals(lexisNexis, that.lexisNexis);
    }

    @Override
    public int hashCode() {
        return Objects.hash(schemaVersion, id, createTime, clientId, brand, regulator, metaTraderAccount, type, eventDate, metadata, initialEventTime, referrerId, firstName, lastName, email, phoneNumber, birthday, firstLanguage, nationalityCode, residencyCode, websiteUserType, lexisNexis, ibId, cpaId);
    }
}
