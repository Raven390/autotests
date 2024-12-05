package businessObjects.kafka.crmDbEvents.registration;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class RegistrationDbEventData {

    @JsonProperty("create_time")
    public String createTime;

    @JsonProperty("user_id")
    public Integer userId;

    @JsonProperty("brand")
    public String brand;

    @JsonProperty("regulator")
    public String regulator;

    @JsonProperty("mt4_account")
    public Integer mtAccount;

    public RegistrationDbEventData() {
    }

    public RegistrationDbEventData(String createTime, Integer userId, String brand, String regulator,
            Integer mtAccount) {
        this.createTime = createTime;
        this.userId = userId;
        this.brand = brand;
        this.regulator = regulator;
        this.mtAccount = mtAccount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegistrationDbEventData that = (RegistrationDbEventData) o;
        return Objects.equals(createTime, that.createTime) && Objects.equals(userId, that.userId) && Objects.equals(brand, that.brand) && Objects.equals(regulator, that.regulator) && Objects.equals(mtAccount, that.mtAccount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(createTime, userId, brand, regulator, mtAccount);
    }

    @Override
    public String toString() {
        return "RegistrationDbEventData{" + "createTime='" + createTime + '\'' + ", userId=" + userId + ", brand='" + brand + '\'' + ", regulator='" + regulator + '\'' + ", mtAccount=" + mtAccount + '}';
    }
}
