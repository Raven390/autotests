package businessObjects.db.clickhouse.crmTbBonusTable;


import java.util.Objects;

public class CrmTbBonusObject {

    public Integer transferId;
    public String uid;
    public String ucid;
    public String brand;
    public String regulator;
    public Integer userId;
    public Integer account;
    public String createTime;
    public String updateTime;
    public Double amount;
    public Double amountUsd;
    public String currency;
    public Integer status;
    public Integer type;
    public String typeRemark;
    public String comment;

    public CrmTbBonusObject() {
    }

    public CrmTbBonusObject(Integer transferId, String uid, String ucid, String brand, String regulator, Integer userId,
            Integer account, String createTime, String updateTime, Double amount, Double amountUsd,
            String currency, Integer status, Integer type, String typeRemark, String comment) {
        this.transferId = transferId;
        this.uid = uid;
        this.ucid = ucid;
        this.brand = brand;
        this.regulator = regulator;
        this.userId = userId;
        this.account = account;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.amount = amount;
        this.amountUsd = amountUsd;
        this.currency = currency;
        this.status = status;
        this.type = type;
        this.typeRemark = typeRemark;
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CrmTbBonusObject that = (CrmTbBonusObject) o;
        return Objects.equals(transferId, that.transferId) && Objects.equals(uid, that.uid) && Objects.equals(ucid, that.ucid) && Objects.equals(brand, that.brand) && Objects.equals(regulator, that.regulator) && Objects.equals(userId, that.userId) && Objects.equals(account, that.account) && Objects.equals(createTime, that.createTime) && Objects.equals(updateTime, that.updateTime) && Objects.equals(amount, that.amount) && Objects.equals(amountUsd, that.amountUsd) && Objects.equals(currency, that.currency) && Objects.equals(status, that.status) && Objects.equals(type, that.type) && Objects.equals(typeRemark, that.typeRemark) && Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transferId, uid, ucid, brand, regulator, userId, account, createTime, updateTime, amount, amountUsd, currency, status, type, typeRemark, comment);
    }

    @Override
    public String toString() {
        return "CrmTbBonusObject{" + "transferId=" + transferId + ", uid='" + uid + '\'' + ", ucid='" + ucid + '\'' + ", brand='" + brand + '\'' + ", regulator='" + regulator + '\'' + ", userId=" + userId + ", account=" + account + ", createTime='" + createTime + '\'' + ", updateTime='" + updateTime + '\'' + ", amount=" + amount + ", amountUsd=" + amountUsd + ", currency='" + currency + '\'' + ", status=" + status + ", type=" + type + ", typeRemark='" + typeRemark + '\'' + ", comment='" + comment + '\'' + '}';
    }
}