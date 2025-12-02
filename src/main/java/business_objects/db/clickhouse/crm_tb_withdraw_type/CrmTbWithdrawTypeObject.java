package business_objects.db.clickhouse.crm_tb_withdraw_type;

import java.util.Objects;

public class CrmTbWithdrawTypeObject {
    private Integer sourceIdSt;
    private Integer id;
    private Integer category;        // UInt8
    private String enName;         // String
    private String lastUpdated; // DateTime64(3)

    @Override
    public String toString() {
        return "CrmTbWithdrawTypeObject{" + "sourceIdSt=" + sourceIdSt + ", id=" + id + ", category=" + category + ", enName='" + enName + '\'' + ", lastUpdated='" + lastUpdated + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CrmTbWithdrawTypeObject that = (CrmTbWithdrawTypeObject) o;
        return sourceIdSt == that.sourceIdSt && id == that.id && category == that.category && Objects.equals(enName, that.enName) && Objects.equals(lastUpdated, that.lastUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceIdSt, id, category, enName, lastUpdated);
    }

    public Integer getSourceIdSt() {
        return sourceIdSt;
    }

    public void setSourceIdSt(Integer sourceIdSt) {
        this.sourceIdSt = sourceIdSt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
