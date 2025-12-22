package business_objects.db.clickhouse.crm_tb_credit_card_table;

import java.util.Objects;

public class CrmTbCreditCardObject {

    public Integer sourceIdSt;
    public Integer id;
    public Integer userId;
    public String createTime;
    public String updateTime;
    public Integer isDel;
    public String cardBeginSixDigits;
    public String cardLastFourDigits;
    public String cardHolderName;
    public String expiryMonth;
    public String expiryYear;
    public Integer threeDomainSecure;
    public Integer paymentType;
    public Integer status;
    public String lastUpdated;

    public CrmTbCreditCardObject() {}

    public CrmTbCreditCardObject(
            Integer sourceIdSt,
            Integer id,
            Integer userId,
            String createTime,
            String updateTime,
            Integer isDel,
            String cardBeginSixDigits,
            String cardLastFourDigits,
            String cardHolderName,
            String expiryMonth,
            String expiryYear,
            Integer threeDomainSecure,
            Integer paymentType,
            Integer status,
            String lastUpdated) {
        this.sourceIdSt = sourceIdSt;
        this.id = id;
        this.userId = userId;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.isDel = isDel;
        this.cardBeginSixDigits = cardBeginSixDigits;
        this.cardLastFourDigits = cardLastFourDigits;
        this.cardHolderName = cardHolderName;
        this.expiryMonth = expiryMonth;
        this.expiryYear = expiryYear;
        this.threeDomainSecure = threeDomainSecure;
        this.paymentType = paymentType;
        this.status = status;
        this.lastUpdated = lastUpdated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CrmTbCreditCardObject that = (CrmTbCreditCardObject) o;
        return Objects.equals(sourceIdSt, that.sourceIdSt)
                && Objects.equals(userId, that.userId)
                && Objects.equals(isDel, that.isDel)
                && Objects.equals(cardBeginSixDigits, that.cardBeginSixDigits)
                && Objects.equals(cardLastFourDigits, that.cardLastFourDigits)
                && Objects.equals(cardHolderName, that.cardHolderName)
                && Objects.equals(expiryMonth, that.expiryMonth)
                && Objects.equals(expiryYear, that.expiryYear)
                && Objects.equals(threeDomainSecure, that.threeDomainSecure)
                && Objects.equals(paymentType, that.paymentType)
                && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                sourceIdSt,
                id,
                userId,
                createTime,
                updateTime,
                isDel,
                cardBeginSixDigits,
                cardLastFourDigits,
                cardHolderName,
                expiryMonth,
                expiryYear,
                threeDomainSecure,
                paymentType,
                status,
                lastUpdated);
    }

    @Override
    public String toString() {
        return "CrmTbCreditCardObject{" + "sourceIdSt=" + sourceIdSt + ", id=" + id + ", userId=" + userId
                + ", createTime='" + createTime + '\'' + ", updateTime='" + updateTime + '\'' + ", isDel=" + isDel
                + ", cardBeginSixDigits='" + cardBeginSixDigits + '\'' + ", cardLastFourDigits='" + cardLastFourDigits
                + '\'' + ", cardHolderName='" + cardHolderName + '\'' + ", expiryMonth='" + expiryMonth + '\''
                + ", expiryYear='" + expiryYear + '\'' + ", threeDomainSecure='" + threeDomainSecure + '\''
                + ", paymentType='" + paymentType + '\'' + ", status='" + status + '\'' + ", lastUpdated='"
                + lastUpdated + '\'' + '}';
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }

    public String getCardBeginSixDigits() {
        return cardBeginSixDigits;
    }

    public void setCardBeginSixDigits(String cardBeginSixDigits) {
        this.cardBeginSixDigits = cardBeginSixDigits;
    }

    public String getCardLastFourDigits() {
        return cardLastFourDigits;
    }

    public void setCardLastFourDigits(String cardLastFourDigits) {
        this.cardLastFourDigits = cardLastFourDigits;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getExpiryMonth() {
        return expiryMonth;
    }

    public void setExpiryMonth(String expiryMonth) {
        this.expiryMonth = expiryMonth;
    }

    public String getExpiryYear() {
        return expiryYear;
    }

    public void setExpiryYear(String expiryYear) {
        this.expiryYear = expiryYear;
    }

    public Integer getThreeDomainSecure() {
        return threeDomainSecure;
    }

    public void setThreeDomainSecure(Integer threeDomainSecure) {
        this.threeDomainSecure = threeDomainSecure;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
