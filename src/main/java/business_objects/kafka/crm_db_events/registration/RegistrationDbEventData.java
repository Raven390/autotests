package business_objects.kafka.crm_db_events.registration;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class RegistrationDbEventData {

    @JsonProperty("create_time")
    public String createTime;

    @JsonProperty("update_time")
    public String updateTime;

    @JsonProperty("user_id")
    public Integer userId;

    @JsonProperty("brand")
    public String brand;

    @JsonProperty("mt4_account")
    public Integer mtAccount;

    @JsonProperty("regulator")
    public String regulator;

    public RegistrationDbEventData() {}

    public RegistrationDbEventData(
            String updateTime, String createTime, Integer userId, String brand, String regulator, Integer mtAccount) {
        this.updateTime = updateTime;
        this.createTime = createTime;
        this.userId = userId;
        this.brand = brand;
        this.mtAccount = mtAccount;
        this.regulator = regulator;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RegistrationDbEventData that = (RegistrationDbEventData) o;
        return Objects.equals(createTime, that.createTime)
                && Objects.equals(updateTime, that.updateTime)
                && Objects.equals(userId, that.userId)
                && Objects.equals(brand, that.brand)
                && Objects.equals(mtAccount, that.mtAccount)
                && Objects.equals(regulator, that.regulator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(createTime, updateTime, userId, brand, mtAccount, regulator);
    }

    @Override
    public String toString() {
        return "RegistrationDbEventData{" + "createTime='" + createTime + '\'' + ", updateTime='" + updateTime + '\''
                + ", userId=" + userId + ", brand='" + brand + '\'' + ", mtAccount=" + mtAccount + ", regulator='"
                + regulator + '\'' + '}';
    }
}
