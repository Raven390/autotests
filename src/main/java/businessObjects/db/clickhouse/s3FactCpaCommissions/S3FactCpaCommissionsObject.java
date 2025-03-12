package businessObjects.db.clickhouse.s3FactCpaCommissions;

public class S3FactCpaCommissionsObject {
    protected Long id;
    protected String date;
    protected Integer brandUid;
    protected String brand;
    protected String regulator;
    protected Integer userId;
    protected String ucid;
    protected Integer cpaId;
    protected String traderId;
    protected String afp;
    protected String tradingCode;
    protected String commissionType;
    protected Double commission;
    protected String partitionBrand;
    protected String dlInsertTs;
    protected String dlUpdateTs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setId(Integer id) {
        this.id = ((long) id);
    }

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

    public String getUcid() {
        return ucid;
    }

    public void setUcid(String ucid) {
        this.ucid = ucid;
    }

    public Integer getCpaId() {
        return cpaId;
    }

    public void setCpaId(Integer cpaId) {
        this.cpaId = cpaId;
    }

    public String getTraderId() {
        return traderId;
    }

    public void setTraderId(String traderId) {
        this.traderId = traderId;
    }

    public String getAfp() {
        return afp;
    }

    public void setAfp(String afp) {
        this.afp = afp;
    }

    public String getTradingCode() {
        return tradingCode;
    }

    public void setTradingCode(String tradingCode) {
        this.tradingCode = tradingCode;
    }

    public String getCommissionType() {
        return commissionType;
    }

    public void setCommissionType(String commissionType) {
        this.commissionType = commissionType;
    }

    public Double getCommission() {
        return commission;
    }

    public void setCommission(Double commission) {
        this.commission = commission;
    }

    public String getPartitionBrand() {
        return partitionBrand;
    }

    public void setPartitionBrand(String partitionBrand) {
        this.partitionBrand = partitionBrand;
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
}
