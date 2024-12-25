package businessObjects.db.clickhouse.documentTable;

import java.util.Objects;

public class DocumentTableEntry {

    public Integer userId;
    public String brand;
    public String accIdType;
    public String accIdNum;
    public Integer nationalityId;

    public DocumentTableEntry() {
    }

    public DocumentTableEntry(Integer userId, String brand, String accIdType, String accIdNum, Integer nationalityId) {
        this.userId = userId;
        this.brand = brand;
        this.accIdType = accIdType;
        this.accIdNum = accIdNum;
        this.nationalityId = nationalityId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DocumentTableEntry that = (DocumentTableEntry) o;
        return Objects.equals(userId, that.userId) && Objects.equals(brand, that.brand) && Objects.equals(accIdType, that.accIdType) && Objects.equals(accIdNum, that.accIdNum) && Objects.equals(nationalityId, that.nationalityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, brand, accIdType, accIdNum, nationalityId);
    }

    @Override
    public String toString() {
        return "DocumentTableEntry{" + "userId=" + userId + ", brand='" + brand + '\'' + ", accIdType='" + accIdType + '\'' + ", accIdNum='" + accIdNum + '\'' + ", nationalityId=" + nationalityId + '}';
    }
}
