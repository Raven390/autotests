package business_objects.db.clickhouse.crm_tb_deposit_type;

import java.util.Objects;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CrmTbDepositTypeObject {

    private Integer sourceIdSt; // UInt8
    private Integer id; // UInt16
    private Integer category; // UInt8
    private String name; // String
    private String lastUpdated; // DateTime64(3)

    @Override
    public String toString() {
        return "CrmTbDepositTypeObject{" + "sourceIdSt=" + sourceIdSt + ", id=" + id + ", category=" + category
                + ", name='" + name + '\'' + ", lastUpdated='" + lastUpdated + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CrmTbDepositTypeObject that = (CrmTbDepositTypeObject) o;
        return Objects.equals(sourceIdSt, that.sourceIdSt)
                && Objects.equals(id, that.id)
                && Objects.equals(category, that.category)
                && Objects.equals(name, that.name)
                && Objects.equals(lastUpdated, that.lastUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceIdSt, id, category, name, lastUpdated);
    }
}
