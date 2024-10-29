package businessObjects.db.crmTbUserTable;

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
    public String email_verification_date;
    public String phone_verification_date;
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
            String authentication, String website_user_type, String email_verification_date,
            String phone_verification_date, String ib_id, String cpa_id, String raf_referrer_id,
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
        this.email_verification_date = email_verification_date;
        this.phone_verification_date = phone_verification_date;
        this.ib_id = ib_id;
        this.cpa_id = cpa_id;
        this.raf_referrer_id = raf_referrer_id;
        this.kyc_status = kyc_status;
        this.last_updated = last_updated;
    }
}