package business_objects.kafka.alerts;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

public class PaymentAlertMessageV2 extends BaseAlertMessageV2 {
    @JsonProperty(value = "account", required = true)
    public String account;

    @JsonProperty(value = "serverId", required = true)
    public String serverId;

    @JsonProperty(value = "paymentMethod", required = true)
    public String paymentMethod;

    @JsonProperty(value = "amount", required = true)
    public String amount;

    @JsonProperty(value = "amountUSD", required = true)
    public String amountUSD;

    @JsonProperty(value = "currency", required = true)
    public String currency;

    @JsonProperty(value = "merchantOrderId", required = true)
    public String merchantOrderId;

    @JsonProperty(value = "paymentEventId")
    public String paymentEventId;

    public PaymentAlertMessageV2(
            UUID id,
            AlertMessageType type,
            OffsetDateTime dateTime,
            OffsetDateTime triggerCreatedTime,
            String ucid,
            String fraudType,
            String trigger,
            String reason,
            Rule rule,
            Map<String, String> attributes,
            String account,
            String serverId,
            String paymentMethod,
            String amount,
            String amountUSD,
            String currency,
            String merchantOrderId,
            String paymentEventId) {
        super(id, type, dateTime, triggerCreatedTime, ucid, fraudType, trigger, reason, rule, attributes);
        this.account = account;
        this.serverId = serverId;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.amountUSD = amountUSD;
        this.currency = currency;
        this.merchantOrderId = merchantOrderId;
        this.paymentEventId = paymentEventId;
    }
}
