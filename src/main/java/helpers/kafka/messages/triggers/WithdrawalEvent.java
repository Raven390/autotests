package helpers.kafka.messages.triggers;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

/**
 * Confluence link - https://vantagefx-hytechs.atlassian.net/wiki/spaces/AntiFraud/pages/1079902238/Withdrawal
 */
public class WithdrawalEvent {

    @JsonProperty("uuid")
    public String uuid;

    @JsonProperty("create_time")
    public Date createTime;

    @JsonProperty("transfer_id")
    public int transferId;

    @JsonProperty("user_id")
    public int userId;

    @JsonProperty("mt_account")
    public int mtAccount;

    @JsonProperty("brand")
    public String brand;

    @JsonProperty("regulator")
    public String regulator;

    @JsonProperty("payment_method_code")
    public String paymentMethodCode;

    @JsonProperty("type")
    public String type;

    public static WithdrawalEvent withdrawalEvent(
            String traceId,
            Date createTime,
            int transferId,
            int userId,
            int mtAccount,
            String brand,
            String regulator,
            String paymentMethodCode,
            String type) {
        WithdrawalEvent withdrawalEvent = new WithdrawalEvent();
        withdrawalEvent.uuid = traceId;
        withdrawalEvent.createTime = createTime;
        withdrawalEvent.transferId = transferId;
        withdrawalEvent.userId = userId;
        withdrawalEvent.mtAccount = mtAccount;
        withdrawalEvent.brand = brand;
        withdrawalEvent.regulator = regulator;
        withdrawalEvent.paymentMethodCode = paymentMethodCode;
        withdrawalEvent.type = type;
        return withdrawalEvent;
    }
}
