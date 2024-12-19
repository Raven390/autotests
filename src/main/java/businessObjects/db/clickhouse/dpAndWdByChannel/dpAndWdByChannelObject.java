package businessObjects.db.clickhouse.dpAndWdByChannel;

import java.util.Objects;

public class dpAndWdByChannelObject {
    public String ucid;
    public String brand;
    public String regulator;
    public long userId;
    public long account;
    public String date;
    public String transferType;
    public String paymentSystem;
    public String psCategory;
    public double totalAmountUsd;
    public long totalCount;
    public String lastUpdated;

    public String getUcid() {
        return ucid;
    }

    public void setUcid(String ucid) {
        this.ucid = ucid;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getRegulator() {
        return regulator;
    }

    public void setRegulator(String regulator) {
        this.regulator = regulator;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getAccount() {
        return account;
    }

    public void setAccount(long account) {
        this.account = account;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    public String getPaymentSystem() {
        return paymentSystem;
    }

    public void setPaymentSystem(String paymentSystem) {
        this.paymentSystem = paymentSystem;
    }

    public String getPsCategory() {
        return psCategory;
    }

    public void setPsCategory(String psCategory) {
        this.psCategory = psCategory;
    }

    public double getTotalAmountUsd() {
        return totalAmountUsd;
    }

    public void setTotalAmountUsd(double totalAmountUsd) {
        this.totalAmountUsd = totalAmountUsd;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String toString() {
        return "dpAndWdByChannelObject{" + "ucid='" + ucid + '\'' + ", brand='" + brand + '\'' + ", regulator='" + regulator + '\'' + ", userId=" + userId + ", account=" + account + ", date='" + date + '\'' + ", transferType='" + transferType + '\'' + ", paymentSystem='" + paymentSystem + '\'' + ", psCategory='" + psCategory + '\'' + ", totalAmountUsd=" + totalAmountUsd + ", totalCount=" + totalCount + ", lastUpdated='" + lastUpdated + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        dpAndWdByChannelObject that = (dpAndWdByChannelObject) o;
        return userId == that.userId && account == that.account && totalCount == that.totalCount && Objects.equals(ucid, that.ucid) && Objects.equals(brand, that.brand) && Objects.equals(regulator, that.regulator) && Objects.equals(date, that.date) && Objects.equals(transferType, that.transferType) && Objects.equals(paymentSystem, that.paymentSystem) && Objects.equals(psCategory, that.psCategory) && Objects.equals(totalAmountUsd, that.totalAmountUsd) && Objects.equals(lastUpdated, that.lastUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ucid, brand, regulator, userId, account, date, transferType, paymentSystem, psCategory, totalAmountUsd, totalCount, lastUpdated);
    }

    public dpAndWdByChannelObject(String ucid, String brand, String regulator, long userId, long account, String date,
            String transferType, String paymentSystem, String psCategory, double totalAmountUsd, long totalCount,
            String lastUpdated) {
        this.ucid = ucid;
        this.brand = brand;
        this.regulator = regulator;
        this.userId = userId;
        this.account = account;
        this.date = date;
        this.transferType = transferType;
        this.paymentSystem = paymentSystem;
        this.psCategory = psCategory;
        this.totalAmountUsd = totalAmountUsd;
        this.totalCount = totalCount;
        this.lastUpdated = lastUpdated;
    }
}
