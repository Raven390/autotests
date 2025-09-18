package business_objects.kafka.alerts;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.UUID;

public class PaymentAlertMessage extends BaseAlertMessage {
    @JsonProperty(value = "account", required = true)
    private String account;

    @JsonProperty(value = "serverId", required = true)
    private String serverId;

    @JsonProperty(value = "paymentMethod", required = true)
    private String paymentMethod;

    @JsonProperty(value = "amount", required = true)
    private String amount;

    @JsonProperty(value = "currency", required = true)
    private String currency;

    @JsonProperty(value = "paymentEventId", required = true)
    private String paymentEventId;

    public PaymentAlertMessage(
            UUID id,
            AlertMessageType type,
            OffsetDateTime dateTime,
            OffsetDateTime triggerCreatedTime,
            String ucid,
            Rule rule,
            String account,
            String serverId,
            String paymentMethod,
            String amount,
            String currency,
            String paymentEventId) {
        super(id, type, dateTime, triggerCreatedTime, ucid, rule);
        this.account = account;
        this.serverId = serverId;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.currency = currency;
        this.paymentEventId = paymentEventId;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setPaymentEventId(String paymentEventId) {
        this.paymentEventId = paymentEventId;
    }

    public String getAccount() {
        return account;
    }

    public String getServerId() {
        return serverId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getPaymentEventId() {
        return paymentEventId;
    }
}
