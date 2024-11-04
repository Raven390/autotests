package businessObjects.db.mtTbUser;

public class MtTbUserObject {

    private String id;
    private String ucid;
    private String crmTbUserId;
    private int account;
    private String serverName;
    private String platform;
    private String type;
    private int serverId;
    private String createdDate;
    private String status;
    private double balance;
    private String currency;
    private double balanceUsd;
    private double equity;
    private double credit;
    private int leverage;
    private String accountGroup;
    private double marginFree;
    private double pnl;
    private String lastActionDate;
    private String lastUpdated;

    // Constructor to initialize all fields
    public MtTbUserObject(String id, String ucid, String crmTbUserId, int account, String serverName, String platform, String type,
            int serverId, String createdDate, String status, double balance, String currency,
            double balanceUsd, double equity, double credit, int leverage, String accountGroup,
            double marginFree, double pnl, String lastActionDate, String lastUpdated) {
        this.id = id;
        this.ucid = ucid;
        this.crmTbUserId = crmTbUserId;
        this.account = account;
        this.serverName = serverName;
        this.platform = platform;
        this.type = type;
        this.serverId = serverId;
        this.createdDate = createdDate;
        this.status = status;
        this.balance = balance;
        this.currency = currency;
        this.balanceUsd = balanceUsd;
        this.equity = equity;
        this.credit = credit;
        this.leverage = leverage;
        this.accountGroup = accountGroup;
        this.marginFree = marginFree;
        this.pnl = pnl;
        this.lastActionDate = lastActionDate;
        this.lastUpdated = lastUpdated;
    }
}