package business_objects.db.clickhouse.cost_payment_fee;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CostPaymentFee {

    private Integer id;
    private String category;
    private String country;

    private BigDecimal chargebackFeePrc;
    private BigDecimal withdrawalFeePrc;
    private BigDecimal depositFeePrc;
    private BigDecimal settleFXDepositPrc;
    private BigDecimal settleDepositPrc;

    private OffsetDateTime lastUpdated;
}
