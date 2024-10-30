package businessObjects.db.crmTbUserTable;

import java.util.Objects;

public class CrmTbUserObject {
    // Declare variables
    public String id;
    public int user_id;
    public String ucid;
    public String brand;
    public String regulator;
    public String registration_date;
    public String first_name;
    public String last_name;
    public String gender;
    public String birthday;
    public String country;
    public String country_code;
    public String iso_country_code;
    public String language;
    public String nationality;
    public String email;
    public String phone_num;
    public String phone_country_code;
    public String is_two_fa_user;
    public String authentication;
    public String website_user_type;
    public String email_verification_mark;
    public String phone_verification_mark;
    public String ib_id;
    public String cpa_id;
    public String raf_referrer_id;
    public String kyc_status;
    public String last_updated;

    // Constructor to initialize all fields
    public CrmTbUserObject(String id, int user_id, String ucid, String brand, String regulator, String registration_date,
            String first_name, String last_name, String gender, String birthday, String country,
            String country_code, String iso_country_code, String language, String nationality,
            String email, String phone_num, String phone_country_code, String is_two_fa_user,
            String authentication, String website_user_type, String email_verification_mark,
            String phone_verification_mark, String ib_id, String cpa_id, String raf_referrer_id,
            String kyc_status, String last_updated) {
        this.id = id;
        this.user_id = user_id;
        this.ucid = ucid;
        this.brand = brand;
        this.regulator = regulator;
        this.registration_date = registration_date;
        this.first_name = first_name;
        this.last_name = last_name;
        this.gender = gender;
        this.birthday = birthday;
        this.country = country;
        this.country_code = country_code;
        this.iso_country_code = iso_country_code;
        this.language = language;
        this.nationality = nationality;
        this.email = email;
        this.phone_num = phone_num;
        this.phone_country_code = phone_country_code;
        this.is_two_fa_user = is_two_fa_user;
        this.authentication = authentication;
        this.website_user_type = website_user_type;
        this.email_verification_mark = email_verification_mark;
        this.phone_verification_mark = phone_verification_mark;
        this.ib_id = ib_id;
        this.cpa_id = cpa_id;
        this.raf_referrer_id = raf_referrer_id;
        this.kyc_status = kyc_status;
        this.last_updated = last_updated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CrmTbUserObject that = (CrmTbUserObject) o;
        return user_id == that.user_id && Objects.equals(id, that.id) && Objects.equals(ucid, that.ucid) && Objects.equals(brand, that.brand) && Objects.equals(regulator, that.regulator) && Objects.equals(registration_date, that.registration_date) && Objects.equals(first_name, that.first_name) && Objects.equals(last_name, that.last_name) && Objects.equals(gender, that.gender) && Objects.equals(birthday, that.birthday) && Objects.equals(country, that.country) && Objects.equals(country_code, that.country_code) && Objects.equals(iso_country_code, that.iso_country_code) && Objects.equals(language, that.language) && Objects.equals(nationality, that.nationality) && Objects.equals(email, that.email) && Objects.equals(phone_num, that.phone_num) && Objects.equals(phone_country_code, that.phone_country_code) && Objects.equals(is_two_fa_user, that.is_two_fa_user) && Objects.equals(authentication, that.authentication) && Objects.equals(website_user_type, that.website_user_type) && Objects.equals(email_verification_mark, that.email_verification_mark) && Objects.equals(phone_verification_mark, that.phone_verification_mark) && Objects.equals(ib_id, that.ib_id) && Objects.equals(cpa_id, that.cpa_id) && Objects.equals(raf_referrer_id, that.raf_referrer_id) && Objects.equals(kyc_status, that.kyc_status) && Objects.equals(last_updated, that.last_updated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user_id, ucid, brand, regulator, registration_date, first_name, last_name, gender, birthday, country, country_code, iso_country_code, language, nationality, email, phone_num, phone_country_code, is_two_fa_user, authentication, website_user_type, email_verification_mark, phone_verification_mark, ib_id, cpa_id, raf_referrer_id, kyc_status, last_updated);
    }

    @Override
    public String toString() {
        return "CrmTbUserObject{" +
                "id='" + id + '\'' +
                ", user_id=" + user_id +
                ", ucid='" + ucid + '\'' +
                ", brand='" + brand + '\'' +
                ", regulator='" + regulator + '\'' +
                ", registration_date='" + registration_date + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", gender='" + gender + '\'' +
                ", birthday='" + birthday + '\'' +
                ", country='" + country + '\'' +
                ", country_code='" + country_code + '\'' +
                ", iso_country_code='" + iso_country_code + '\'' +
                ", language='" + language + '\'' +
                ", nationality='" + nationality + '\'' +
                ", email='" + email + '\'' +
                ", phone_num='" + phone_num + '\'' +
                ", phone_country_code='" + phone_country_code + '\'' +
                ", is_two_fa_user='" + is_two_fa_user + '\'' +
                ", authentication='" + authentication + '\'' +
                ", website_user_type='" + website_user_type + '\'' +
                ", email_verification_date='" + email_verification_mark + '\'' +
                ", phone_verification_date='" + phone_verification_mark + '\'' +
                ", ib_id='" + ib_id + '\'' +
                ", cpa_id='" + cpa_id + '\'' +
                ", raf_referrer_id='" + raf_referrer_id + '\'' +
                ", kyc_status='" + kyc_status + '\'' +
                ", last_updated='" + last_updated + '\'' +
                '}';
    }
}