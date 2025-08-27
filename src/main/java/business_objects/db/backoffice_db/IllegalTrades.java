package business_objects.db.backoffice_db;


import java.util.Objects;

public class IllegalTrades {

    private String account;
    private Integer serverId;
    private Double illegalProfit;
    private Double illegalProfitUsd;
    private String tickets;
    private String symbols;
    private String createdAt;
    private String ucid;

    public IllegalTrades() {
    }

    public IllegalTrades(String account, Integer serverId, Double illegalProfit, Double illegalProfitUsd,
            String tickets, String symbols, String createdAt, String ucid) {
        this.account = account;
        this.serverId = serverId;
        this.illegalProfit = illegalProfit;
        this.illegalProfitUsd = illegalProfitUsd;
        this.tickets = tickets;
        this.symbols = symbols;
        this.createdAt = createdAt;
        this.ucid = ucid;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    public Double getIllegalProfit() {
        return illegalProfit;
    }

    public void setIllegalProfit(Double illegalProfit) {
        this.illegalProfit = illegalProfit;
    }

    public Double getIllegalProfitUsd() {
        return illegalProfitUsd;
    }

    public void setIllegalProfitUsd(Double illegalProfitUsd) {
        this.illegalProfitUsd = illegalProfitUsd;
    }

    public String getTickets() {
        return tickets;
    }

    public void setTickets(String tickets) {
        this.tickets = tickets;
    }

    public String getSymbols() {
        return symbols;
    }

    public void setSymbols(String symbols) {
        this.symbols = symbols;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUcid() {
        return ucid;
    }

    public void setUcid(String ucid) {
        this.ucid = ucid;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        IllegalTrades that = (IllegalTrades) o;
        return Objects.equals(account, that.account) && Objects.equals(serverId, that.serverId) && Objects.equals(illegalProfit, that.illegalProfit) && Objects.equals(tickets, that.tickets) && Objects.equals(symbols, that.symbols) && Objects.equals(createdAt, that.createdAt) && Objects.equals(ucid, that.ucid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(account, serverId, illegalProfit, tickets, symbols, createdAt, ucid);
    }

    @Override
    public String toString() {
        return "IllegalTrades{" + "account='" + account + '\'' + ", serverId=" + serverId + ", illegalProfit=" + illegalProfit + ", illegalProfitUsd=" + illegalProfitUsd + ", tickets='" + tickets + '\'' + ", symbols='" + symbols + '\'' + ", createdAt='" + createdAt + '\'' + ", ucid='" + ucid + '\'' + '}';
    }
}
