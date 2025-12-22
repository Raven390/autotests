package business_objects.db.clickhouse.dp_and_wd_by_channel;

import java.util.Objects;

public class dpAndWdByChannelObject {
    public String ucid;
    public String brand;
    public String regulator;
    public long userId;
    public long account;
    public String date;
    public String transferType;
    public String paymentChannel;
    public String channelCategory;
    public double totalAmountUsd;
    public long totalCount;

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

    public String getPaymentChannel() {
        return paymentChannel;
    }

    public void setPaymentChannel(String paymentChannel) {
        this.paymentChannel = paymentChannel;
    }

    public String getChannelCategory() {
        return channelCategory;
    }

    public void setChannelCategory(String channelCategory) {
        this.channelCategory = channelCategory;
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

    @Override
    public String toString() {
        return "dpAndWdByChannelObject{" + "ucid='" + ucid + '\'' + ", brand='" + brand + '\'' + ", regulator='"
                + regulator + '\'' + ", userId=" + userId + ", account=" + account + ", date='" + date + '\''
                + ", transferType='" + transferType + '\'' + ", paymentSystem='" + paymentChannel + '\''
                + ", psCategory='" + channelCategory + '\'' + ", totalAmountUsd=" + totalAmountUsd + ", totalCount="
                + totalCount + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        dpAndWdByChannelObject that = (dpAndWdByChannelObject) o;
        return userId == that.userId
                && account == that.account
                && totalCount == that.totalCount
                && Objects.equals(ucid, that.ucid)
                && Objects.equals(brand, that.brand)
                && Objects.equals(regulator, that.regulator)
                && Objects.equals(date, that.date)
                && Objects.equals(transferType, that.transferType)
                && Objects.equals(paymentChannel, that.paymentChannel)
                && Objects.equals(channelCategory, that.channelCategory)
                && Objects.equals(totalAmountUsd, that.totalAmountUsd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                ucid,
                brand,
                regulator,
                userId,
                account,
                date,
                transferType,
                paymentChannel,
                channelCategory,
                totalAmountUsd,
                totalCount);
    }
}
