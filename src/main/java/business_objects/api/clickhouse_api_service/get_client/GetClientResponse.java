package business_objects.api.clickhouse_api_service.get_client;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class GetClientResponse {

    @JsonProperty("websiteUserType")
    String websiteUserType;

    @JsonProperty("clientId")
    String clientId;

    @JsonProperty("userId")
    String userId;

    @JsonProperty("gender")
    String gender;

    @JsonProperty("regulator")
    String regulator;

    @JsonProperty("phoneNum")
    String phoneNum;

    @JsonProperty("phoneCountryCode")
    String phoneCountryCode;

    @JsonProperty("lastName")
    String lastName;

    @JsonProperty("twoFaUser")
    String twoFaUser;

    @JsonProperty("authentication")
    String authentication;

    @JsonProperty("firstName")
    String firstName;

    @JsonProperty("email")
    String email;

    @JsonProperty("createTime")
    String createTime;

    @JsonProperty("countryCode")
    String countryCode;

    @JsonProperty("language")
    String language;

    @JsonProperty("nationality")
    String nationality;

    @JsonProperty("isoCountryCode")
    String isoCountryCode;

    @JsonProperty("country")
    String country;

    @JsonProperty("brand")
    String brand;

    @JsonProperty("birthday")
    String birthday;

    @JsonProperty("registrationDate")
    String registrationDate;

    @JsonProperty("emailVerificationMark")
    String emailVerificationMark;

    @JsonProperty("phoneVerificationMark")
    String phoneVerificationMark;

    @JsonProperty("ibId")
    String ibId;

    @JsonProperty("cpaId")
    String cpaId;

    @JsonProperty("rafReferrerId")
    String rafReferrerId;

    @JsonProperty("lastUpdated")
    String lastUpdated;

    @JsonProperty("kycStatus")
    String kycStatus;

    @JsonProperty("poiCompletionTime")
    String poiCompletionTime;

    public GetClientResponse() {}

    public GetClientResponse(
            String websiteUserType,
            String clientId,
            String userId,
            String gender,
            String regulator,
            String phoneNum,
            String phoneCountryCode,
            String lastName,
            String twoFaUser,
            String authentication,
            String firstName,
            String email,
            String createTime,
            String countryCode,
            String language,
            String nationality,
            String isoCountryCode,
            String country,
            String brand,
            String birthday,
            String registrationDate,
            String emailVerificationMark,
            String phoneVerificationMark,
            String ibId,
            String cpaId,
            String rafReferrerId,
            String lastUpdated,
            String kycStatus,
            String poiCompletionTime) {
        this.websiteUserType = websiteUserType;
        this.clientId = clientId;
        this.userId = userId;
        this.gender = gender;
        this.regulator = regulator;
        this.phoneNum = phoneNum;
        this.phoneCountryCode = phoneCountryCode;
        this.lastName = lastName;
        this.twoFaUser = twoFaUser;
        this.authentication = authentication;
        this.firstName = firstName;
        this.email = email;
        this.createTime = createTime;
        this.countryCode = countryCode;
        this.language = language;
        this.nationality = nationality;
        this.isoCountryCode = isoCountryCode;
        this.country = country;
        this.brand = brand;
        this.birthday = birthday;
        this.registrationDate = registrationDate;
        this.emailVerificationMark = emailVerificationMark;
        this.phoneVerificationMark = phoneVerificationMark;
        this.ibId = ibId;
        this.cpaId = cpaId;
        this.rafReferrerId = rafReferrerId;
        this.lastUpdated = lastUpdated;
        this.kycStatus = kycStatus;
        this.poiCompletionTime = poiCompletionTime;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GetClientResponse that = (GetClientResponse) o;
        return Objects.equals(websiteUserType, that.websiteUserType)
                && Objects.equals(clientId, that.clientId)
                && Objects.equals(userId, that.userId)
                && Objects.equals(gender, that.gender)
                && Objects.equals(regulator, that.regulator)
                && Objects.equals(phoneNum, that.phoneNum)
                && Objects.equals(phoneCountryCode, that.phoneCountryCode)
                && Objects.equals(lastName, that.lastName)
                && Objects.equals(twoFaUser, that.twoFaUser)
                && Objects.equals(authentication, that.authentication)
                && Objects.equals(firstName, that.firstName)
                && Objects.equals(email, that.email)
                && Objects.equals(createTime, that.createTime)
                && Objects.equals(countryCode, that.countryCode)
                && Objects.equals(language, that.language)
                && Objects.equals(nationality, that.nationality)
                && Objects.equals(isoCountryCode, that.isoCountryCode)
                && Objects.equals(country, that.country)
                && Objects.equals(brand, that.brand)
                && Objects.equals(birthday, that.birthday)
                && Objects.equals(registrationDate, that.registrationDate)
                && Objects.equals(emailVerificationMark, that.emailVerificationMark)
                && Objects.equals(phoneVerificationMark, that.phoneVerificationMark)
                && Objects.equals(ibId, that.ibId)
                && Objects.equals(cpaId, that.cpaId)
                && Objects.equals(rafReferrerId, that.rafReferrerId)
                && Objects.equals(lastUpdated, that.lastUpdated)
                && Objects.equals(kycStatus, that.kycStatus)
                && Objects.equals(poiCompletionTime, that.poiCompletionTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                websiteUserType,
                clientId,
                userId,
                gender,
                regulator,
                phoneNum,
                phoneCountryCode,
                lastName,
                twoFaUser,
                authentication,
                firstName,
                email,
                createTime,
                countryCode,
                language,
                nationality,
                isoCountryCode,
                country,
                brand,
                birthday,
                registrationDate,
                emailVerificationMark,
                phoneVerificationMark,
                ibId,
                cpaId,
                rafReferrerId,
                lastUpdated,
                kycStatus,
                poiCompletionTime);
    }

    @Override
    public String toString() {
        return "GetClientResponse{" + "websiteUserType='" + websiteUserType + '\'' + ", clientId='" + clientId + '\''
                + ", userId='" + userId + '\'' + ", gender='" + gender + '\'' + ", regulator='" + regulator + '\''
                + ", phoneNum='" + phoneNum + '\'' + ", phoneCountryCode='" + phoneCountryCode + '\'' + ", lastName='"
                + lastName + '\'' + ", twoFaUser='" + twoFaUser + '\'' + ", authentication='" + authentication + '\''
                + ", firstName='" + firstName + '\'' + ", email='" + email + '\'' + ", createTime='" + createTime + '\''
                + ", countryCode='" + countryCode + '\'' + ", language='" + language + '\'' + ", nationality='"
                + nationality + '\'' + ", isoCountryCode='" + isoCountryCode + '\'' + ", country='" + country + '\''
                + ", brand='" + brand + '\'' + ", birthday='" + birthday + '\'' + ", registrationDate='"
                + registrationDate + '\'' + ", emailVerificationMark='" + emailVerificationMark + '\''
                + ", phoneVerificationMark='" + phoneVerificationMark + '\'' + ", ibId='" + ibId + '\'' + ", cpaId='"
                + cpaId + '\'' + ", rafReferrerId='" + rafReferrerId + '\'' + ", lastUpdated='" + lastUpdated + '\''
                + ", kycStatus='" + kycStatus + '\'' + ", poiCompletionTime='" + poiCompletionTime + '\'' + '}';
    }

    public String getWebsiteUserType() {
        return websiteUserType;
    }

    public void setWebsiteUserType(String websiteUserType) {
        this.websiteUserType = websiteUserType;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRegulator() {
        return regulator;
    }

    public void setRegulator(String regulator) {
        this.regulator = regulator;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getPhoneCountryCode() {
        return phoneCountryCode;
    }

    public void setPhoneCountryCode(String phoneCountryCode) {
        this.phoneCountryCode = phoneCountryCode;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTwoFaUser() {
        return twoFaUser;
    }

    public void setTwoFaUser(String twoFaUser) {
        this.twoFaUser = twoFaUser;
    }

    public String getAuthentication() {
        return authentication;
    }

    public void setAuthentication(String authentication) {
        this.authentication = authentication;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getIsoCountryCode() {
        return isoCountryCode;
    }

    public void setIsoCountryCode(String isoCountryCode) {
        this.isoCountryCode = isoCountryCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getEmailVerificationMark() {
        return emailVerificationMark;
    }

    public void setEmailVerificationMark(String emailVerificationMark) {
        this.emailVerificationMark = emailVerificationMark;
    }

    public String getPhoneVerificationMark() {
        return phoneVerificationMark;
    }

    public void setPhoneVerificationMark(String phoneVerificationMark) {
        this.phoneVerificationMark = phoneVerificationMark;
    }

    public String getIbId() {
        return ibId;
    }

    public void setIbId(String ibId) {
        this.ibId = ibId;
    }

    public String getCpaId() {
        return cpaId;
    }

    public void setCpaId(String cpaId) {
        this.cpaId = cpaId;
    }

    public String getRafReferrerId() {
        return rafReferrerId;
    }

    public void setRafReferrerId(String rafReferrerId) {
        this.rafReferrerId = rafReferrerId;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getKycStatus() {
        return kycStatus;
    }

    public void setKycStatus(String kycStatus) {
        this.kycStatus = kycStatus;
    }

    public String getPoiCompletionTime() {
        return poiCompletionTime;
    }

    public void setPoiCompletionTime(String poiCompletionTime) {
        this.poiCompletionTime = poiCompletionTime;
    }
}
