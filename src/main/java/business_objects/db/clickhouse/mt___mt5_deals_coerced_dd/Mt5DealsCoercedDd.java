package business_objects.db.clickhouse.mt___mt5_deals_coerced_dd;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mt5DealsCoercedDd {

    private Integer serverId;
    private Integer account;
    private Long deal;
    private Integer entry;
    private String timeUtc;
    private Double freeMarginUsd;
    private Double equityUsd;
    private Double leverage;
}
