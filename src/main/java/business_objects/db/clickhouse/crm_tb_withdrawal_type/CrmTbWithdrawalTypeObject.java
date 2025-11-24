package business_objects.db.clickhouse.crm_tb_withdrawal_type;

import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class CrmTbWithdrawalTypeObject {
    private Integer sourceIdSt;
    private Integer id;
    private Integer category;        // UInt8
    private String enName;         // String
    private String lastUpdated; // DateTime64(3)
}
