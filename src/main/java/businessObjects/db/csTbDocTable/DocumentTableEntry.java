package businessObjects.db.csTbDocTable;

import java.util.Objects;

public class DocumentTableEntry {

    public String ucid;
    public Integer userId;
    public String brand;
    public String accIdType;
    public String accIdNum;
    public Integer nationalityId;
    public String updateTs;

    public DocumentTableEntry() {
    }

    public DocumentTableEntry(String ucid, Integer userId, String brand, String accIdType, String accIdNum, Integer nationalityId, String updateTs) {
        this.ucid = ucid;
        this.userId = userId;
        this.brand = brand;
        this.accIdType = accIdType;
        this.accIdNum = accIdNum;
        this.nationalityId = nationalityId;
        this.updateTs = updateTs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DocumentTableEntry that = (DocumentTableEntry) o;
        return Objects.equals(ucid, that.ucid) && Objects.equals(userId, that.userId) && Objects.equals(brand, that.brand) && Objects.equals(accIdType, that.accIdType) && Objects.equals(accIdNum, that.accIdNum) && Objects.equals(nationalityId, that.nationalityId) && Objects.equals(updateTs, that.updateTs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ucid, userId, brand, accIdType, accIdNum, nationalityId, updateTs);
    }

    @Override
    public String toString() {
        return "DocumentTableEntry{" +
                "ucid='" + ucid + '\'' +
                ", userId=" + userId +
                ", brand='" + brand + '\'' +
                ", accIdType='" + accIdType + '\'' +
                ", accIdNum='" + accIdNum + '\'' +
                ", nationalityId=" + nationalityId +
                ", updateTs='" + updateTs + '\'' +
                '}';
    }
}
