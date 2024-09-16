package helpers.kafka.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

public class WithdrawalEvent {

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
}
