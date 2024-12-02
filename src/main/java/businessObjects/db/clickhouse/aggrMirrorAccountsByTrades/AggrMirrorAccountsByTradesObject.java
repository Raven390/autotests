package businessObjects.db.clickhouse.aggrMirrorAccountsByTrades;


import java.util.Objects;

public class AggrMirrorAccountsByTradesObject {
    public String symbol;
    public String requestTradingAccount;
    public String requestServerId;
    public String requestVolumeInLots;
    public String mirrorAccounts;
    public String mirrorServerId;
    public String mirrorVolumeInLots;

    public AggrMirrorAccountsByTradesObject() {
    }

    public AggrMirrorAccountsByTradesObject(String symbol, String requestTradingAccount, String requestServerId,
                                            String requestVolumeInLots, String mirrorAccounts,String mirrorServerId,
                                            String mirrorVolumeInLots) {
        this.symbol = symbol;
        this.requestTradingAccount = requestTradingAccount;
        this.requestServerId = requestServerId;
        this.requestVolumeInLots = requestVolumeInLots;
        this.mirrorAccounts = mirrorAccounts;
        this.mirrorServerId = mirrorServerId;
        this.mirrorVolumeInLots = mirrorVolumeInLots;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AggrMirrorAccountsByTradesObject that = (AggrMirrorAccountsByTradesObject) o;
        return Objects.equals(symbol, that.symbol) && Objects.equals(requestTradingAccount, that.requestTradingAccount)
                && Objects.equals(requestServerId, that.requestServerId) && Objects.equals(requestVolumeInLots, that.requestVolumeInLots)
                && Objects.equals(mirrorAccounts, that.mirrorAccounts) && Objects.equals(mirrorServerId, that.mirrorServerId)
                && Objects.equals(mirrorVolumeInLots, that.mirrorVolumeInLots);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, requestTradingAccount, requestServerId,requestVolumeInLots,mirrorAccounts,mirrorServerId,mirrorVolumeInLots);
    }

    @Override
    public String toString() {
        return "aggrMirrorAccountsByTradesObject{" +
                "symbol='" + symbol + '\'' +
                ", requestTradingAccount=" + requestTradingAccount +
                ", requestServerId='" + requestServerId + '\'' +
                ", requestVolumeInLots='" + requestVolumeInLots + '\'' +
                ", mirrorAccounts='" + mirrorAccounts + '\'' +
                ", mirrorServerId='" + mirrorServerId + '\'' +
                ", mirrorVolumeInLots='" + mirrorVolumeInLots + '\'' +
                '}';
    }
}
