package businessObjects.db.crmTbBonusTable;


public class CrmTbBonusObject {
    // Declare variables
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


}