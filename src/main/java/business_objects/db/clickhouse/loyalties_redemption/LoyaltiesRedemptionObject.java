package business_objects.db.clickhouse.loyalties_redemption;

import java.util.Objects;

public class LoyaltiesRedemptionObject {

    public String ucid;
    public Integer loyaltyRedemptionId;
    public String brand;
    public String regulator;
    public Integer user_id;
    public Double rewardId;
    public Double rewardType;
    public Double source;
    public Double point;
    public String currency;
    public Double amount;
    public Double amountUsd;
    public Integer status;
    public String extend;
    public String createTime;
    public String createTimeUtc;
    public String lastUpdated;

    public LoyaltiesRedemptionObject() {
    }

    public LoyaltiesRedemptionObject(
            String ucid, Integer loyaltiesRedemption, String brand, String regulator, Integer user_id, Double rewardId,
            Double rewardType, Double source, Double point, String currency, Double amount, Double amountUsd,
            Integer status, String extend, String createTime, String createTimeUtc, String lastUpdated) {
        this.ucid = ucid;
        this.loyaltyRedemptionId = loyaltiesRedemption;
        this.brand = brand;
        this.regulator = regulator;
        this.user_id = user_id;
        this.rewardId = rewardId;
        this.rewardType = rewardType;
        this.source = source;
        this.point = point;
        this.currency = currency;
        this.amount = amount;
        this.amountUsd = amountUsd;
        this.status = status;
        this.extend = extend;
        this.createTime = createTime;
        this.createTimeUtc = createTimeUtc;
        this.lastUpdated = lastUpdated;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LoyaltiesRedemptionObject that = (LoyaltiesRedemptionObject) o;
        return Objects.equals(ucid, that.ucid) && Objects.equals(loyaltyRedemptionId, that.loyaltyRedemptionId) && Objects.equals(
                brand, that.brand) && Objects.equals(regulator, that.regulator) && Objects.equals(
                        user_id, that.user_id) && Objects.equals(rewardId, that.rewardId) && Objects.equals(
                                rewardType, that.rewardType) && Objects.equals(source, that.source) && Objects.equals(
                                        point, that.point) && Objects.equals(currency, that.currency) && Objects.equals(amount, that.amount) && Objects.equals(
                                                amountUsd, that.amountUsd) && Objects.equals(status, that.status) && Objects.equals(
                                                        extend, that.extend) && Objects.equals(createTime, that.createTime) && Objects.equals(
                                                                createTimeUtc, that.createTimeUtc) && Objects.equals(lastUpdated, that.lastUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ucid, loyaltyRedemptionId, brand, regulator, user_id, rewardId, rewardType, source, point, currency, amount, amountUsd, status, extend, createTime, createTimeUtc, lastUpdated);
    }

    @Override
    public String toString() {
        return "LoyaltiesRedemptionObject{" + "ucid='" + ucid + '\'' + ", loyaltiesRedemption=" + loyaltyRedemptionId + ", brand='" + brand + '\'' + ", regulator='" + regulator + '\'' + ", user_id='" + user_id + '\'' + ", rewardId=" + rewardId + ", rewardType=" + rewardType + ", source=" + source + ", point=" + point + ", currency='" + currency + '\'' + ", amount=" + amount + ", amountUsd=" + amountUsd + ", status=" + status + ", extend='" + extend + '\'' + ", createTime='" + createTime + '\'' + ", createTimeUtc='" + createTimeUtc + '\'' + ", lastUpdated='" + lastUpdated + '\'' + '}';
    }
}
