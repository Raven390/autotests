package business_objects.kafka.crm_events;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class RegistrationEvent {

    @JsonProperty("birthday")
    private String birthday;

    @JsonProperty("brand")
    private String brand;

    @JsonProperty("clientId")
    private Integer clientId;

    @JsonProperty("email")
    private String email;

    @JsonProperty("eventDate")
    private String eventDate;

    @JsonProperty("firstLanguage")
    private String firstLanguage;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("ibId")
    private Integer ibId;

    @JsonProperty("id")
    private String id;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("lexisNexis")
    private LexisNexis lexisNexis;

    @JsonProperty("nationalityCode")
    private String nationalityCode;

    @JsonProperty("phoneNumber")
    private String phoneNumber;

    @JsonProperty("referrerId")
    private String referrerId;

    @JsonProperty("regulator")
    private String regulator;

    @JsonProperty("residencyCode")
    private String residencyCode;

    @JsonProperty("schemaVersion")
    private String schemaVersion;

    @JsonProperty("type")
    private String type;

    @JsonProperty("websiteUserType")
    private String websiteUserType;

    public static class LexisNexis {

        @JsonProperty("device")
        private String device;

        @JsonProperty("digitalId")
        private String digitalId;

        @JsonProperty("policyScore")
        private Integer policyScore;

        @JsonProperty("rawResponse")
        private String rawResponse;

        @JsonProperty("riskRating")
        private String riskRating;

        @JsonProperty("sessionId")
        private Integer sessionId;

        @JsonProperty("trueIp")
        private String trueIp;

        @JsonProperty("webSessionId")
        private String webSessionId;

        public LexisNexis(
                String device, String digitalId, Integer policyScore, String rawResponse, String riskRating,
                Integer sessionId,
                String trueIp) {
            this.device = device;
            this.digitalId = digitalId;
            this.policyScore = policyScore;
            this.rawResponse = rawResponse;
            this.riskRating = riskRating;
            this.sessionId = sessionId;
            this.trueIp = trueIp;
        }

        public LexisNexis() {
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof LexisNexis that)) return false;
            return Objects.equals(device, that.device) && Objects.equals(digitalId, that.digitalId) && Objects.equals(
                    policyScore, that.policyScore) && Objects.equals(rawResponse, that.rawResponse) && Objects.equals(
                            riskRating, that.riskRating) && Objects.equals(sessionId, that.sessionId) && Objects.equals(trueIp, that.trueIp);
        }

        @Override
        public int hashCode() {
            return Objects.hash(device, digitalId, policyScore, rawResponse, riskRating, sessionId, trueIp, webSessionId);
        }

        @Override
        public String toString() {
            return "LexisNexis{" + "device='" + device + '\'' + ", digitalId='" + digitalId + '\'' + ", policyScore=" + policyScore + ", rawResponse='" + rawResponse + '\'' + ", riskRating='" + riskRating + '\'' + ", sessionId=" + sessionId + ", trueIp='" + trueIp + '\'' + ", webSessionId='" + webSessionId + '\'' + '}';
        }

        public String getDevice() {
            return device;
        }

        public void setDevice(String device) {
            this.device = device;
        }

        public String getDigitalId() {
            return digitalId;
        }

        public void setDigitalId(String digitalId) {
            this.digitalId = digitalId;
        }

        public Integer getPolicyScore() {
            return policyScore;
        }

        public void setPolicyScore(Integer policyScore) {
            this.policyScore = policyScore;
        }

        public String getRawResponse() {
            return rawResponse;
        }

        public void setRawResponse(String rawResponse) {
            this.rawResponse = rawResponse;
        }

        public String getRiskRating() {
            return riskRating;
        }

        public void setRiskRating(String riskRating) {
            this.riskRating = riskRating;
        }

        public Integer getSessionId() {
            return sessionId;
        }

        public void setSessionId(Integer sessionId) {
            this.sessionId = sessionId;
        }

        public String getTrueIp() {
            return trueIp;
        }

        public void setTrueIp(String trueIp) {
            this.trueIp = trueIp;
        }

        public String getWebSessionId() {
            return webSessionId;
        }

        public void setWebSessionId(String webSessionId) {
            this.webSessionId = webSessionId;
        }
    }

    public RegistrationEvent() {
    }

    public RegistrationEvent(
            String birthday, String brand, Integer clientId, String email, String eventDate, String firstLanguage,
            String firstName, Integer ibId, String id, String lastName, String nationalityCode, LexisNexis lexisNexis,
            String phoneNumber, String regulator, String residencyCode, String schemaVersion, String type,
            String websiteUserType) {
        this.birthday = birthday;
        this.brand = brand;
        this.clientId = clientId;
        this.email = email;
        this.eventDate = eventDate;
        this.firstLanguage = firstLanguage;
        this.firstName = firstName;
        this.ibId = ibId;
        this.id = id;
        this.lastName = lastName;
        this.nationalityCode = nationalityCode;
        this.lexisNexis = lexisNexis;
        this.phoneNumber = phoneNumber;
        this.regulator = regulator;
        this.residencyCode = residencyCode;
        this.schemaVersion = schemaVersion;
        this.type = type;
        this.websiteUserType = websiteUserType;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof RegistrationEvent that)) return false;
        return birthday == that.birthday && brand == that.brand && clientId == that.clientId && email == that.email && eventDate == that.eventDate && firstLanguage == that.firstLanguage && firstName == that.firstName && ibId == that.ibId && id == that.id && lastName == that.lastName && nationalityCode == that.nationalityCode && phoneNumber == that.phoneNumber && regulator == that.regulator && residencyCode == that.residencyCode && schemaVersion == that.schemaVersion && type == that.type && websiteUserType == that.websiteUserType && Objects.equals(
                lexisNexis, that.lexisNexis);
    }

    @Override
    public int hashCode() {
        return Objects.hash(birthday, brand, clientId, email, eventDate, firstLanguage, firstName, ibId, id, lastName, nationalityCode, lexisNexis, phoneNumber, regulator, residencyCode, schemaVersion, type, websiteUserType);
    }

    @Override
    public String toString() {
        return "RegistrationEvent{" + "birthday=" + birthday + ", brand=" + brand + ", clientId=" + clientId + ", email=" + email + ", eventDate=" + eventDate + ", firstLanguage=" + firstLanguage + ", firstName=" + firstName + ", ibId=" + ibId + ", id=" + id + ", lastName=" + lastName + ", nationalityCode=" + nationalityCode + ", lexisNexis=" + lexisNexis + ", phoneNumber=" + phoneNumber + ", regulator=" + regulator + ", residencyCode=" + residencyCode + ", schemaVersion=" + schemaVersion + ", type=" + type + ", websiteUserType=" + websiteUserType + '}';
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getFirstLanguage() {
        return firstLanguage;
    }

    public void setFirstLanguage(String firstLanguage) {
        this.firstLanguage = firstLanguage;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Integer getIbId() {
        return ibId;
    }

    public void setIbId(Integer ibId) {
        this.ibId = ibId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNationalityCode() {
        return nationalityCode;
    }

    public void setNationalityCode(String nationalityCode) {
        this.nationalityCode = nationalityCode;
    }

    public LexisNexis getLexisNexis() {
        return lexisNexis;
    }

    public void setLexisNexis(LexisNexis lexisNexis) {
        this.lexisNexis = lexisNexis;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRegulator() {
        return regulator;
    }

    public void setRegulator(String regulator) {
        this.regulator = regulator;
    }

    public String getResidencyCode() {
        return residencyCode;
    }

    public void setResidencyCode(String residencyCode) {
        this.residencyCode = residencyCode;
    }

    public String getSchemaVersion() {
        return schemaVersion;
    }

    public void setSchemaVersion(String schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWebsiteUserType() {
        return websiteUserType;
    }

    public void setWebsiteUserType(String websiteUserType) {
        this.websiteUserType = websiteUserType;
    }

    public String getReferrerId() {
        return referrerId;
    }

    public void setReferrerId(String referrerId) {
        this.referrerId = referrerId;
    }
}
