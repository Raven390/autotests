package business_objects.db.clickhouse.s3MtSpreads;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class S3MtSpreadsObject {

    private int serverId; // UInt16
    private long account; // UInt64
    private OffsetDateTime date; // Date32
    private BigDecimal spreadRevenueUsd; // Decimal(18,2)
    private OffsetDateTime chInsertTs; // DateTime64(3)
}
