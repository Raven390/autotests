package businessObjects.db.crmTbUserTable;

import java.util.Objects;

public class CrmTbUserObject {
    // Declare variables
    public int userId;
    public String ucid;
    public String brand;
    public String regulator;
    public String registrationDate;
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
    public String ibId;
    public String cpaId;
    public String rafReferrerId;
    public String kycStatus;
    public String lastUpdated;

    // Constructor to initialize all fields
    public CrmTbUserObject(int userId, String ucid, String brand, String regulator, String registrationDate,
            String firstName, String lastName, String gender, String birthday, String country,
            String countryCode, String isoCountryCode, String language, String nationality,
            String email, String phoneNum, String phoneCountryCode, String isTwoFaUser,
            String authentication, String websiteUserType, String emailVerificationMark,
            String phoneVerificationMark, String ibId, String cpaId, String rafReferrerId,
            String kycStatus, String lastUpdated) {
        this.userId = userId;
        this.ucid = ucid;
        this.brand = brand;
        this.regulator = regulator;
        this.registrationDate = registrationDate;
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
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CrmTbUserObject that = (CrmTbUserObject) o;
        return userId == that.userId && Objects.equals(ucid, that.ucid) && Objects.equals(brand, that.brand) && Objects.equals(regulator, that.regulator) && Objects.equals(registrationDate, that.registrationDate) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(gender, that.gender) && Objects.equals(birthday, that.birthday) && Objects.equals(country, that.country) && Objects.equals(countryCode, that.countryCode) && Objects.equals(isoCountryCode, that.isoCountryCode) && Objects.equals(language, that.language) && Objects.equals(nationality, that.nationality) && Objects.equals(email, that.email) && Objects.equals(phoneNum, that.phoneNum) && Objects.equals(phoneCountryCode, that.phoneCountryCode) && Objects.equals(isTwoFaUser, that.isTwoFaUser) && Objects.equals(authentication, that.authentication) && Objects.equals(websiteUserType, that.websiteUserType) && Objects.equals(emailVerificationMark, that.emailVerificationMark) && Objects.equals(phoneVerificationMark, that.phoneVerificationMark) && Objects.equals(ibId, that.ibId) && Objects.equals(cpaId, that.cpaId) && Objects.equals(rafReferrerId, that.rafReferrerId) && Objects.equals(kycStatus, that.kycStatus) && Objects.equals(lastUpdated, that.lastUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, ucid, brand, regulator, registrationDate, firstName, lastName, gender, birthday, country, countryCode, isoCountryCode, language, nationality, email, phoneNum, phoneCountryCode, isTwoFaUser, authentication, websiteUserType, emailVerificationMark, phoneVerificationMark, ibId, cpaId, rafReferrerId, kycStatus, lastUpdated);
    }

    @Override
    public String toString() {
        return "CrmTbUserObject{" +
                "user_id=" + userId +
                ", ucid='" + ucid + '\'' +
                ", brand='" + brand + '\'' +
                ", regulator='" + regulator + '\'' +
                ", registration_date='" + registrationDate + '\'' +
                ", first_name='" + firstName + '\'' +
                ", last_name='" + lastName + '\'' +
                ", gender='" + gender + '\'' +
                ", birthday='" + birthday + '\'' +
                ", country='" + country + '\'' +
                ", country_code='" + countryCode + '\'' +
                ", iso_country_code='" + isoCountryCode + '\'' +
                ", language='" + language + '\'' +
                ", nationality='" + nationality + '\'' +
                ", email='" + email + '\'' +
                ", phone_num='" + phoneNum + '\'' +
                ", phone_country_code='" + phoneCountryCode + '\'' +
                ", is_two_fa_user='" + isTwoFaUser + '\'' +
                ", authentication='" + authentication + '\'' +
                ", website_user_type='" + websiteUserType + '\'' +
                ", email_verification_date='" + emailVerificationMark + '\'' +
                ", phone_verification_date='" + phoneVerificationMark + '\'' +
                ", ib_id='" + ibId + '\'' +
                ", cpa_id='" + cpaId + '\'' +
                ", raf_referrer_id='" + rafReferrerId + '\'' +
                ", kyc_status='" + kycStatus + '\'' +
                ", last_updated='" + lastUpdated + '\'' +
                '}';
    }
}