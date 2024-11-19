package businessObjects.db.crmDepositTable;


public class CrmTbDepositObject {
    private Integer account;
    private Double amount;
    private Double amountUsd;
    private String brand;
    private String createTime;
    private String currency;
    private Double fee;
    private String paymentChannel;
    private String paymentDetails;
    private String paymentExpirationDate;
    private String paymentRequisite;
    private String paymentSystemAccount;
    private String paymentSystemCurrency;
    private String paymentType;
    private String regulator;
    private Integer status;
    private Integer ticketId;
    private Integer transferId;
    private String ucid;
    private String uid;
    private String updateTime;
    private Integer userId;

    // Constructor with all fields
    public CrmTbDepositObject(
            Integer account, Double amount, Double amountUsd, String brand, String createTime,
            String currency, Double fee, String paymentChannel, String paymentDetails,
            String paymentExpirationDate, String paymentRequisite, String paymentSystemAccount,
            String paymentSystemCurrency, String paymentType, String regulator, Integer status,
            Integer ticketId, Integer transferId, String ucid, String uid, String updateTime, Integer userId) {
        this.account = account;
        this.amount = amount;
        this.amountUsd = amountUsd;
        this.brand = brand;
        this.createTime = createTime;
        this.currency = currency;
        this.fee = fee;
        this.paymentChannel = paymentChannel;
        this.paymentDetails = paymentDetails;
        this.paymentExpirationDate = paymentExpirationDate;
        this.paymentRequisite = paymentRequisite;
        this.paymentSystemAccount = paymentSystemAccount;
        this.paymentSystemCurrency = paymentSystemCurrency;
        this.paymentType = paymentType;
        this.regulator = regulator;
        this.status = status;
        this.ticketId = ticketId;
        this.transferId = transferId;
        this.ucid = ucid;
        this.uid = uid;
        this.updateTime = updateTime;
        this.userId = userId;
    }
}