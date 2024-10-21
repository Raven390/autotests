package helpers.kafka.crmEvents.toRemove;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

/**
 * Confluence link - https://vantagefx-hytechs.atlassian.net/wiki/spaces/AntiFraud/pages/1079902238/Withdrawal
 */
public class WithdrawalCrmEvent {

    @JsonProperty("uuid")
    public String uuid;

    @JsonProperty("create_time")
    public Date createTime;

    @JsonProperty("transfer_id")
    public Integer transferId;

    @JsonProperty("user_id")
    public Integer userId;

    @JsonProperty("mt_account")
    public Integer mtAccount;

    @JsonProperty("brand")
    public String brand;

    @JsonProperty("regulator")
    public String regulator;

    @JsonProperty("payment_method_code")
    public String paymentMethodCode;

    @JsonProperty("type")
    public String type;

    public static WithdrawalCrmEvent withdrawalCrmEvent(String traceId, Date createTime, Integer transferId, Integer userId, Integer mtAccount, String brand, String regulator, String paymentMethodCode, String type) {
        WithdrawalCrmEvent event = new WithdrawalCrmEvent();
        event.uuid = traceId;
        event.createTime = createTime;
        event.transferId = transferId;
        event.userId = userId;
        event.mtAccount = mtAccount;
        event.brand = brand;
        event.regulator = regulator;
        event.paymentMethodCode = paymentMethodCode;
        event.type = type;
        return event;
    }
}
