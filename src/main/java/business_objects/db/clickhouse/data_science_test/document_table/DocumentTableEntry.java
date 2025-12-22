package business_objects.db.clickhouse.data_science_test.document_table;

import java.util.Objects;

public class DocumentTableEntry {

    public String ucid;
    public String accIdType;
    public String accIdNum;
    public Integer nationalityId;

    public DocumentTableEntry() {}

    public DocumentTableEntry(String ucid, String accIdType, String accIdNum, Integer nationalityId) {
        this.ucid = ucid;
        this.accIdType = accIdType;
        this.accIdNum = accIdNum;
        this.nationalityId = nationalityId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DocumentTableEntry that = (DocumentTableEntry) o;
        return Objects.equals(ucid, that.ucid)
                && Objects.equals(accIdType, that.accIdType)
                && Objects.equals(accIdNum, that.accIdNum)
                && Objects.equals(nationalityId, that.nationalityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ucid, accIdType, accIdNum, nationalityId);
    }

    @Override
    public String toString() {
        return "DocumentTableEntry{" + "ucid='" + ucid + '\'' + ", accIdType='" + accIdType + '\'' + ", accIdNum='"
                + accIdNum + '\'' + ", nationalityId=" + nationalityId + '}';
    }
}
