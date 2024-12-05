package businessObjects.kafka.crmEvents;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class RegistrationEvent {
    @JsonProperty("id")
    public String id;

    @JsonProperty("createTime")
    public String createTime;

    @JsonProperty("clientId")
    public Integer clientId;

    @JsonProperty("brand")
    public String brand;

    @JsonProperty("regulator")
    public String regulator;

    @JsonProperty("metaTraderAccount")
    public Integer metaTraderAccount;

    @JsonProperty("type")
    public String type;

    public RegistrationEvent() {
    }

    public RegistrationEvent(String createTime, Integer clientId, String brand, String regulator,
            Integer metaTraderAccount, String type) {
        this.createTime = createTime;
        this.clientId = clientId;
        this.brand = brand;
        this.regulator = regulator;
        this.metaTraderAccount = metaTraderAccount;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegistrationEvent that = (RegistrationEvent) o;
        return Objects.equals(createTime, that.createTime) && Objects.equals(clientId, that.clientId) && Objects.equals(brand, that.brand) && Objects.equals(regulator, that.regulator) && Objects.equals(metaTraderAccount, that.metaTraderAccount) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(createTime, clientId, brand, regulator, metaTraderAccount, type);
    }

    @Override
    public String toString() {
        return "RegistrationEvent{" + "id='" + id + '\'' + ", createTime='" + createTime + '\'' + ", clientId=" + clientId + ", brand='" + brand + '\'' + ", regulator='" + regulator + '\'' + ", metaTraderAccount=" + metaTraderAccount + ", type='" + type + '\'' + '}';
    }
}
