package business_objects.db.clickhouse.mt___symbol_session;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MtSymbolSession {
    private String symbol;
    private Integer sourceIdSt;
    private Integer day;
    private String trade;
}
