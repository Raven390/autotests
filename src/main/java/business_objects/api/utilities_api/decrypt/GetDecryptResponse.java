package business_objects.api.utilities_api.decrypt;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class GetDecryptResponse {

    @JsonProperty("brand")
    private String brand;

    @JsonProperty("emailDecrypt")
    private String emailDecrypt;

    @JsonProperty("phoneNumDecrypt")
    private String phoneNumDecrypt;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getEmailDecrypt() {
        return emailDecrypt;
    }

    public void setEmailDecrypt(String emailDecrypt) {
        this.emailDecrypt = emailDecrypt;
    }

    public String getPhoneNumDecrypt() {
        return phoneNumDecrypt;
    }

    public void setPhoneNumDecrypt(String phoneNumDecrypt) {
        this.phoneNumDecrypt = phoneNumDecrypt;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof GetDecryptResponse that)) return false;
        return Objects.equals(brand, that.brand) && Objects.equals(emailDecrypt, that.emailDecrypt) && Objects.equals(
                phoneNumDecrypt, that.phoneNumDecrypt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(brand, emailDecrypt, phoneNumDecrypt);
    }

    @Override
    public String toString() {
        return "GetDecryptResponse{" + "brand='" + brand + '\'' + ", emailDecrypt='" + emailDecrypt + '\'' + ", phoneNumDecrypt='" + phoneNumDecrypt + '\'' + '}';
    }
}
