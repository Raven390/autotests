package helpers.data;

import utils.Utils;

import java.util.Random;

public class ClientHelper {

    private Integer userId;
    private String uuid;
    private Integer tradingAccount;
    private Integer serverId;
    private String ucid;

    // Constructor
    public ClientHelper() {
        this.userId = Utils.getRandomIntPositive();
        this.uuid = Utils.getRandomUuidString();
        this.tradingAccount = Utils.getRandomIntPositive();
        this.serverId = new Random().nextInt(1, 50);
        this.ucid = "vantage-" + this.userId;
    }

    // Getters
    public Integer getUserId() {
        return userId;
    }

    public String getUuid() {
        return uuid;
    }

    public Integer getTradingAccount() {
        return tradingAccount;
    }

    public Integer getServerId() {
        return serverId;
    }

    public String getUcid() {
        return ucid;
    }
}
