package business_objects.db.clickhouse.crm_tb_deposit_channel;

import java.util.Objects;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CrmTbDepositChannelObject {

    private Integer sourceIdSt; // UInt8
    private Integer id; // UInt16
    private Integer channelId; // UInt16
    private Integer typeId; // UInt16
    private Integer isMobileChannel; // UInt8
    private String name; // String
    private String lastUpdated; // DateTime64(3)

    @Override
    public String toString() {
        return "CrmTbDepositChannelObject{" + "sourceIdSt=" + sourceIdSt + ", id=" + id + ", channelId=" + channelId
                + ", typeId=" + typeId + ", isMobileChannel=" + isMobileChannel + ", name='" + name + '\''
                + ", lastUpdated='" + lastUpdated + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CrmTbDepositChannelObject that = (CrmTbDepositChannelObject) o;
        return Objects.equals(sourceIdSt, that.sourceIdSt)
                && Objects.equals(id, that.id)
                && Objects.equals(channelId, that.channelId)
                && Objects.equals(typeId, that.typeId)
                && Objects.equals(isMobileChannel, that.isMobileChannel)
                && Objects.equals(name, that.name)
                && Objects.equals(lastUpdated, that.lastUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceIdSt, id, channelId, typeId, isMobileChannel, name, lastUpdated);
    }
}
