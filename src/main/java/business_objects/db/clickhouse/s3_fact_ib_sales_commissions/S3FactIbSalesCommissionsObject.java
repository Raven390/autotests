package business_objects.db.clickhouse.s3_fact_ib_sales_commissions;

import java.util.Objects;

public class S3FactIbSalesCommissionsObject {
    private String date;
    private Integer brandUid;
    private String brand;
    private String regulator;
    private Integer userId;
    private Integer crmServerId;
    private String ucid;
    private Integer account;
    private Integer serverId;
    private String server;
    private String accountGroup;
    private String platform;
    private String currency;
    private Integer salesUserId;
    private Integer ibUserId;
    private Integer ibRebateAccount;
    private Double salesCommission;
    private Double ibCommission;
    private Double salesVolume;
    private Double idVolume;
    private String dlInsertTs;
    private String dlUpdateTs;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getBrandUid() {
        return brandUid;
    }

    public void setBrandUid(Integer brandUid) {
        this.brandUid = brandUid;
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCrmServerId() {
        return crmServerId;
    }

    public void setCrmServerId(Integer crmServerId) {
        this.crmServerId = crmServerId;
    }

    public String getUcid() {
        return ucid;
    }

    public void setUcid(String ucid) {
        this.ucid = ucid;
    }

    public Integer getAccount() {
        return account;
    }

    public void setAccount(Integer account) {
        this.account = account;
    }

    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getAccountGroup() {
        return accountGroup;
    }

    public void setAccountGroup(String accountGroup) {
        this.accountGroup = accountGroup;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getSalesUserId() {
        return salesUserId;
    }

    public void setSalesUserId(Integer salesUserId) {
        this.salesUserId = salesUserId;
    }

    public Integer getIbUserId() {
        return ibUserId;
    }

    public void setIbUserId(Integer ibUserId) {
        this.ibUserId = ibUserId;
    }

    public Integer getIbRebateAccount() {
        return ibRebateAccount;
    }

    public void setIbRebateAccount(Integer ibRebateAccount) {
        this.ibRebateAccount = ibRebateAccount;
    }

    public Double getSalesCommission() {
        return salesCommission;
    }

    public void setSalesCommission(Double salesCommission) {
        this.salesCommission = salesCommission;
    }

    public void setSalesCommission(String salesCommission) {
        this.salesCommission = Double.parseDouble(salesCommission);
    }

    public Double getIbCommission() {
        return ibCommission;
    }

    public void setIbCommission(Double ibCommission) {
        this.ibCommission = ibCommission;
    }

    public void setIbCommission(String ibCommission) {
        this.ibCommission = Double.parseDouble(ibCommission);
    }

    public Double getSalesVolume() {
        return salesVolume;
    }

    public void setSalesVolume(Double salesVolume) {
        this.salesVolume = salesVolume;
    }

    public Double getIdVolume() {
        return idVolume;
    }

    public void setIdVolume(Double idVolume) {
        this.idVolume = idVolume;
    }

    public String getDlInsertTs() {
        return dlInsertTs;
    }

    public void setDlInsertTs(String dlInsertTs) {
        this.dlInsertTs = dlInsertTs;
    }

    public String getDlUpdateTs() {
        return dlUpdateTs;
    }

    public void setDlUpdateTs(String dlUpdateTs) {
        this.dlUpdateTs = dlUpdateTs;
    }

    @Override
    public String toString() {
        return "s3FactIbSalesCommissionsObject{" + "date='" + date + '\'' + ", brandUid=" + brandUid + ", brand='" + brand + '\'' + ", regulator='" + regulator + '\'' + ", userId='" + userId + '\'' + ", crmServerId=" + crmServerId + ", ucid='" + ucid + '\'' + ", account=" + account + ", serverId=" + serverId + ", server='" + server + '\'' + ", accountGroup='" + accountGroup + '\'' + ", platform='" + platform + '\'' + ", currency='" + currency + '\'' + ", salesUserId=" + salesUserId + ", ibUserId=" + ibUserId + ", ibRebateAccount=" + ibRebateAccount + ", salesCommission=" + salesCommission + ", ibCommission=" + ibCommission + ", salesVolume=" + salesVolume + ", idVolume=" + idVolume + ", diInsertTs='" + dlInsertTs + '\'' + ", diUpdateTs='" + dlUpdateTs + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        S3FactIbSalesCommissionsObject that = (S3FactIbSalesCommissionsObject) o;
        return Objects.equals(date, that.date) && Objects.equals(brandUid, that.brandUid) && Objects.equals(brand, that.brand) && Objects.equals(regulator, that.regulator) && Objects.equals(userId, that.userId) && Objects.equals(crmServerId, that.crmServerId) && Objects.equals(ucid, that.ucid) && Objects.equals(account, that.account) && Objects.equals(serverId, that.serverId) && Objects.equals(server, that.server) && Objects.equals(accountGroup, that.accountGroup) && Objects.equals(platform, that.platform) && Objects.equals(currency, that.currency) && Objects.equals(salesUserId, that.salesUserId) && Objects.equals(ibUserId, that.ibUserId) && Objects.equals(ibRebateAccount, that.ibRebateAccount) && Objects.equals(salesCommission, that.salesCommission) && Objects.equals(ibCommission, that.ibCommission) && Objects.equals(salesVolume, that.salesVolume) && Objects.equals(idVolume, that.idVolume) && Objects.equals(dlInsertTs, that.dlInsertTs) && Objects.equals(dlUpdateTs, that.dlUpdateTs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, brandUid, brand, regulator, userId, crmServerId, ucid, account, serverId, server, accountGroup, platform, currency, salesUserId, ibUserId, ibRebateAccount, salesCommission, ibCommission, salesVolume, idVolume, dlInsertTs, dlUpdateTs);
    }
}
