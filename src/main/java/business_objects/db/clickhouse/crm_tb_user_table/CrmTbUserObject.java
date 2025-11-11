package business_objects.db.clickhouse.crm_tb_user_table;

import java.util.Objects;

import static utils.Utils.getCurrentTimestampDbFormat;

public class CrmTbUserObject {
    // Declare variables
    public int userId;
    public String ucid;
    public String brand;
    public String regulator;
    public String brandGroup;
    public String registrationDate;
    public String registrationDateUtc;
    public String firstName;
    public String lastName;
    public String gender;
    public String birthday;
    public String country;
    public String countryCode;
    public String isoCountryCode;
    public String language;
    public String nationality;
    public String email;
    public String phoneNum;
    public String phoneCountryCode;
    public String isTwoFaUser;
    public String authentication;
    public String websiteUserType;
    public String emailVerificationMark;
    public String phoneVerificationMark;
    public Integer ibId;
    public Integer cpaId;
    public Integer rafReferrerId;
    public String kycStatus;
    public String lastUpdated;
    public String createTime;
    public String createTimeUtc;
    public String nationalityId;
    public String poiCompleteTs;

    public CrmTbUserObject() {
    }

    public CrmTbUserObject(int userId, String ucid, String brand, String regulator, String brandGroup,
            String registrationDate,
            String registrationDateUtc,
            String firstName, String lastName, String gender, String birthday, String country, String countryCode,
            String isoCountryCode, String language, String nationality, String email, String phoneNum,
            String phoneCountryCode, String isTwoFaUser, String authentication, String websiteUserType,
            String emailVerificationMark, String phoneVerificationMark, Integer ibId, Integer cpaId,
            Integer rafReferrerId, String kycStatus, String lastUpdated, String createTime, String createTimeUtc,
            String nationalityId, String poiCompletionTime) {
        this.userId = userId;
        this.ucid = ucid;
        this.brand = brand;
        this.regulator = regulator;
        this.brandGroup = brandGroup;
        this.registrationDate = registrationDate;
        this.registrationDateUtc = registrationDateUtc;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthday = birthday;
        this.country = country;
        this.countryCode = countryCode;
        this.isoCountryCode = isoCountryCode;
        this.language = language;
        this.nationality = nationality;
        this.email = email;
        this.phoneNum = phoneNum;
        this.phoneCountryCode = phoneCountryCode;
        this.isTwoFaUser = isTwoFaUser;
        this.authentication = authentication;
        this.websiteUserType = websiteUserType;
        this.emailVerificationMark = emailVerificationMark;
        this.phoneVerificationMark = phoneVerificationMark;
        this.ibId = ibId;
        this.cpaId = cpaId;
        this.rafReferrerId = rafReferrerId;
        this.kycStatus = kycStatus;
        this.lastUpdated = lastUpdated;
        this.createTime = createTime;
        this.createTimeUtc = createTimeUtc;
        this.nationalityId = nationalityId;
        this.poiCompleteTs = poiCompletionTime;
    }

    // Constructor to set name, ID, UCID, and Brand
    public CrmTbUserObject(int userId, String ucid, String brand, String regulator, String firstName, String lastName) {
        this.userId = userId;
        this.ucid = ucid;
        this.brand = brand;
        this.regulator = regulator;
        this.registrationDate = "2024-10-23 14:56:59";
        this.registrationDateUtc = "2024-10-23 14:56:59";
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = "male";
        this.birthday = "1975-05-11";
        this.country = "Cyprus";
        this.countryCode = "CY";
        this.isoCountryCode = "CY";
        this.language = "en";
        this.nationality = "RUS";
        this.email = "DUrksdLPlqZB6byC9vfKk6qm9BpUmsOS";
        this.phoneNum = "BjrbbdAHkwhBFLnPclfvbg==";
        this.phoneCountryCode = "996";
        this.isTwoFaUser = "1";
        this.authentication = "2FA";
        this.websiteUserType = "2";
        this.emailVerificationMark = "1";
        this.phoneVerificationMark = "1";
        this.ibId = 1;
        this.cpaId = 2;
        this.rafReferrerId = 3;
        this.kycStatus = "APPROVED";
        this.lastUpdated = getCurrentTimestampDbFormat();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CrmTbUserObject that = (CrmTbUserObject) o;
        return userId == that.userId && Objects.equals(ucid, that.ucid) && Objects.equals(brand, that.brand) && Objects.equals(regulator, that.regulator) && Objects.equals(registrationDate, that.registrationDate) && Objects.equals(registrationDateUtc, that.registrationDateUtc) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(gender, that.gender) && Objects.equals(birthday, that.birthday) && Objects.equals(country, that.country) && Objects.equals(countryCode, that.countryCode) && Objects.equals(isoCountryCode, that.isoCountryCode) && Objects.equals(language, that.language) && Objects.equals(nationality, that.nationality) && Objects.equals(email, that.email) && Objects.equals(phoneNum, that.phoneNum) && Objects.equals(phoneCountryCode, that.phoneCountryCode) && Objects.equals(isTwoFaUser, that.isTwoFaUser) && Objects.equals(authentication, that.authentication) && Objects.equals(websiteUserType, that.websiteUserType) && Objects.equals(emailVerificationMark, that.emailVerificationMark) && Objects.equals(phoneVerificationMark, that.phoneVerificationMark) && Objects.equals(ibId, that.ibId) && Objects.equals(cpaId, that.cpaId) && Objects.equals(rafReferrerId, that.rafReferrerId) && Objects.equals(kycStatus, that.kycStatus) && Objects.equals(lastUpdated, that.lastUpdated) && Objects.equals(createTime, that.createTime) && Objects.equals(createTimeUtc, that.createTimeUtc) && Objects.equals(nationalityId, that.nationalityId) && Objects.equals(poiCompleteTs, that.poiCompleteTs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, ucid, brand, regulator, registrationDate, registrationDateUtc, firstName, lastName, gender, birthday, country, countryCode, isoCountryCode, language, nationality, email, phoneNum, phoneCountryCode, isTwoFaUser, authentication, websiteUserType, emailVerificationMark, phoneVerificationMark, ibId, cpaId, rafReferrerId, kycStatus, lastUpdated, createTime, createTimeUtc, nationalityId, poiCompleteTs);
    }

    @Override
    public String toString() {
        return "CrmTbUserObject{" + "userId=" + userId + ", ucid='" + ucid + '\'' + ", brand='" + brand + '\'' + ", regulator='" + regulator + '\'' + ", registrationDate='" + registrationDate + '\'' + ", registrationDateUtc='" + registrationDateUtc + '\'' + ", firstName='" + firstName + '\'' + ", lastName='" + lastName + '\'' + ", gender='" + gender + '\'' + ", birthday='" + birthday + '\'' + ", country='" + country + '\'' + ", countryCode='" + countryCode + '\'' + ", isoCountryCode='" + isoCountryCode + '\'' + ", language='" + language + '\'' + ", nationality='" + nationality + '\'' + ", email='" + email + '\'' + ", phoneNum='" + phoneNum + '\'' + ", phoneCountryCode='" + phoneCountryCode + '\'' + ", isTwoFaUser='" + isTwoFaUser + '\'' + ", authentication='" + authentication + '\'' + ", websiteUserType='" + websiteUserType + '\'' + ", emailVerificationMark='" + emailVerificationMark + '\'' + ", phoneVerificationMark='" + phoneVerificationMark + '\'' + ", ibId=" + ibId + ", cpaId=" + cpaId + ", rafReferrerId=" + rafReferrerId + ", kycStatus='" + kycStatus + '\'' + ", lastUpdated='" + lastUpdated + '\'' + ", createTime='" + createTime + '\'' + ", createTimeUtc='" + createTimeUtc + '\'' + ", nationalityId='" + nationalityId + '\'' + ", poiCompleteTs='" + poiCompleteTs + '\'' + '}';
    }
}