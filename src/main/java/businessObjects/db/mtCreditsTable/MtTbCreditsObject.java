package businessObjects.db.mtCreditsTable;


public class MtTbCreditsObject {
    private Integer account;
    private Double amount;
    private Double amountUsd;
    private String brand;
    private String comment;
    private String createTime;
    private String currency;
    private String regulator;
    private Integer serverId;
    private String serverName;
    private Integer ticket;
    private String ucid;
    private String uid;
    private Integer userId;

    // Constructor with all fields
    public MtTbCreditsObject(Integer account, Double amount, Double amountUsd, String brand, String comment,
                            String createTime, String currency, String regulator, Integer serverId, String serverName,
                             Integer ticket, String ucid, String uid, Integer userId) {
        this.account = account;
        this.amount = amount;
        this.amountUsd = amountUsd;
        this.brand = brand;
        this.comment = comment;
        this.createTime = createTime;
        this.currency = currency;
        this.regulator = regulator;
        this.serverId = serverId;
        this.serverName = serverName;
        this.ticket = ticket;
        this.ucid = ucid;
        this.uid = uid;
        this.userId = userId;
    }
}