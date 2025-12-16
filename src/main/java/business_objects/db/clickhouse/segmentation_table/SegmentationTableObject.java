package business_objects.db.clickhouse.segmentation_table;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class SegmentationTableObject {
    private String ucid;
    private Double revenue;
    private Double netDeposit;
    private String segment;
    private String date;

}

