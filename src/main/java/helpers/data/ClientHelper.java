package helpers.data;

import static utils.Utils.getUcidByUserIdAndBrand;

import com.fasterxml.jackson.annotation.JsonIgnore;
import helpers.data.enums.Brand;
import helpers.data.enums.Regulator;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ClientHelper {

    private Integer userId;
    private String uid;
    private Brand brand;
    private Regulator regulator;
    private Integer tradingAccount;
    private Integer tradingAccount2;
    private Integer serverId;
    private String email;
    private String phoneNumber;
    private String ipAddress;
    private String countryCode;
    private Integer cpaId;
    private Integer ibId;
    private Integer referrerId;
    private String deviceId;
    private String webSessionId;
    private String sessionId;
    private String digitalId;
    private String dateOfBirth;
    private String firstName;
    private String lastName;
    private String country;
    private String mtCid;

    @JsonIgnore
    public String getUcid() {
        return getUcidByUserIdAndBrand(userId, brand);
    }

    public String getNameDateOfBirth() {
        return firstName + " " + lastName + " " + dateOfBirth;
    }

    public String getBrand() {
        return brand.getDisplayName();
    }

    public String getRegulator() {
        return regulator.getDisplayName();
    }
}
