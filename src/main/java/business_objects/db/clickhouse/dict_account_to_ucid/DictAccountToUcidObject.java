package business_objects.db.clickhouse.dict_account_to_ucid;

import java.util.Objects;

public class DictAccountToUcidObject {
    // Declare variables
    public int source_id_st;
    public String brand;
    public String regulator;
    public Integer userId;
    public String ucid;
    public Integer account;
    public Integer serverIdSt;
    public String serverName;
    public Integer isDel;
    public String lastUpdated;

    public DictAccountToUcidObject() {}

    public DictAccountToUcidObject(
            int source_id_st,
            String brand,
            String regulator,
            Integer userId,
            String ucid,
            Integer account,
            Integer serverIdSt,
            String serverName,
            Integer isDel,
            String lastUpdated) {
        this.source_id_st = source_id_st;
        this.brand = brand;
        this.regulator = regulator;
        this.userId = userId;
        this.ucid = ucid;
        this.account = account;
        this.serverIdSt = serverIdSt;
        this.serverName = serverName;
        this.isDel = isDel;
        this.lastUpdated = lastUpdated;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DictAccountToUcidObject that = (DictAccountToUcidObject) o;
        return source_id_st == that.source_id_st
                && Objects.equals(brand, that.brand)
                && Objects.equals(regulator, that.regulator)
                && Objects.equals(userId, that.userId)
                && Objects.equals(ucid, that.ucid)
                && Objects.equals(account, that.account)
                && Objects.equals(serverIdSt, that.serverIdSt)
                && Objects.equals(serverName, that.serverName)
                && Objects.equals(isDel, that.isDel)
                && Objects.equals(lastUpdated, that.lastUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                source_id_st, brand, regulator, userId, ucid, account, serverIdSt, serverName, isDel, lastUpdated);
    }

    @Override
    public String toString() {
        return "DictAccountToUcidObject{" + "source_id_st=" + source_id_st + ", brand='" + brand + '\''
                + ", regulator='" + regulator + '\'' + ", userId=" + userId + ", ucid='" + ucid + '\'' + ", account="
                + account + ", serverIdSt=" + serverIdSt + ", serverName='" + serverName + '\'' + ", isDel=" + isDel
                + ", lastUpdated='" + lastUpdated + '\'' + '}';
    }
}
