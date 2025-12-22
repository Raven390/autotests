package business_objects.db.clickhouse.crm_tb_bonus_table;

import java.util.Objects;

public class CrmTbBonusObject {

    public Integer sourceIdSt;
    public Integer brandUid;
    public String brand;
    public String regulator;
    public Integer userId;
    public String ucid;
    public Integer account;
    public Integer transferId;
    public String createTime;
    public String createTimeUtc;
    public String updateTime;
    public String updateTimeUtc;
    public Double amount;
    public Double amountUsd;
    public String currency;
    public Integer statusId;
    public String status;
    public String type;
    public String typeRemark;
    public String comment;
    public String ticket;
    public String lastUpdated;

    public CrmTbBonusObject() {}

    public CrmTbBonusObject(
            Integer sourceIdSt,
            Integer brandUid,
            String brand,
            String regulator,
            Integer userId,
            String ucid,
            Integer account,
            Integer transferId,
            String createTime,
            String createTimeUtc,
            String updateTime,
            String updateTimeUtc,
            Double amount,
            Double amountUsd,
            String currency,
            Integer statusId,
            String status,
            String type,
            String typeRemark,
            String comment,
            String ticket,
            String lastUpdated) {
        this.sourceIdSt = sourceIdSt;
        this.brandUid = brandUid;
        this.brand = brand;
        this.regulator = regulator;
        this.userId = userId;
        this.ucid = ucid;
        this.account = account;
        this.transferId = transferId;
        this.createTime = createTime;
        this.createTimeUtc = createTimeUtc;
        this.updateTime = updateTime;
        this.updateTimeUtc = updateTimeUtc;
        this.amount = amount;
        this.amountUsd = amountUsd;
        this.currency = currency;
        this.statusId = statusId;
        this.status = status;
        this.type = type;
        this.typeRemark = typeRemark;
        this.comment = comment;
        this.ticket = ticket;
        this.lastUpdated = lastUpdated;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CrmTbBonusObject that = (CrmTbBonusObject) o;
        return Objects.equals(sourceIdSt, that.sourceIdSt)
                && Objects.equals(brandUid, that.brandUid)
                && Objects.equals(brand, that.brand)
                && Objects.equals(regulator, that.regulator)
                && Objects.equals(userId, that.userId)
                && Objects.equals(ucid, that.ucid)
                && Objects.equals(account, that.account)
                && Objects.equals(transferId, that.transferId)
                && Objects.equals(createTime, that.createTime)
                && Objects.equals(createTimeUtc, that.createTimeUtc)
                && Objects.equals(updateTime, that.updateTime)
                && Objects.equals(updateTimeUtc, that.updateTimeUtc)
                && Objects.equals(amount, that.amount)
                && Objects.equals(amountUsd, that.amountUsd)
                && Objects.equals(currency, that.currency)
                && Objects.equals(statusId, that.statusId)
                && Objects.equals(status, that.status)
                && Objects.equals(type, that.type)
                && Objects.equals(typeRemark, that.typeRemark)
                && Objects.equals(comment, that.comment)
                && Objects.equals(ticket, that.ticket)
                && Objects.equals(lastUpdated, that.lastUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                sourceIdSt,
                brandUid,
                brand,
                regulator,
                userId,
                ucid,
                account,
                transferId,
                createTime,
                createTimeUtc,
                updateTime,
                updateTimeUtc,
                amount,
                amountUsd,
                currency,
                statusId,
                status,
                type,
                typeRemark,
                comment,
                ticket,
                lastUpdated);
    }

    @Override
    public String toString() {
        return "CrmTbBonusObject{" + "sourceIdSt=" + sourceIdSt + ", brandUid=" + brandUid + ", brand='" + brand + '\''
                + ", regulator='" + regulator + '\'' + ", userId=" + userId + ", ucid='" + ucid + '\'' + ", account="
                + account + ", transferId=" + transferId + ", createTime='" + createTime + '\'' + ", createTimeUtc='"
                + createTimeUtc + '\'' + ", updateTime='" + updateTime + '\'' + ", updateTimeUtc='" + updateTimeUtc
                + '\'' + ", amount=" + amount + ", amountUsd=" + amountUsd + ", currency='" + currency + '\''
                + ", statusId=" + statusId + ", status='" + status + '\'' + ", type='" + type + '\'' + ", typeRemark='"
                + typeRemark + '\'' + ", comment='" + comment + '\'' + ", ticket='" + ticket + '\'' + ", lastUpdated='"
                + lastUpdated + '\'' + '}';
    }
}
