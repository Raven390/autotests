package businessObjects.db.clickhouse.aggrMirrorAccountsByTrades;


import java.util.Objects;

public class MirrorLoginObject {
    public String symbol;
    public String login_1;
    public String server_id_1;
    public Double lots_1;
    public String login_2;
    public String server_id_2;
    public Double lots_2;
    public String date;

    public MirrorLoginObject() {
    }

    public MirrorLoginObject(String symbol, String requestTradingAccount, String requestServerId,
            Double requestVolumeInLots, String mirrorAccounts, String mirrorServerId,
            Double mirrorVolumeInLots, String date) {
        this.symbol = symbol;
        this.login_1 = requestTradingAccount;
        this.server_id_1 = requestServerId;
        this.lots_1 = requestVolumeInLots;
        this.login_2 = mirrorAccounts;
        this.server_id_2 = mirrorServerId;
        this.lots_2 = mirrorVolumeInLots;
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MirrorLoginObject that = (MirrorLoginObject) o;
        return Objects.equals(symbol, that.symbol) && Objects.equals(login_1, that.login_1) && Objects.equals(
                server_id_1, that.server_id_1) && Objects.equals(lots_1, that.lots_1) && Objects.equals(
                        login_2, that.login_2) && Objects.equals(server_id_2, that.server_id_2) && Objects.equals(lots_2, that.lots_2) && Objects.equals(
                                date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, login_1, server_id_1, lots_1, login_2, server_id_2, lots_2, date);
    }

    @Override
    public String toString() {
        return "MirrorLoginObject{" + "symbol='" + symbol + '\'' + ", login_1='" + login_1 + '\'' + ", server_id_1='" + server_id_1 + '\'' + ", lots_1=" + lots_1 + ", login_2='" + login_2 + '\'' + ", server_id_2='" + server_id_2 + '\'' + ", lots_2=" + lots_2 + ", date='" + date + '\'' + '}';
    }
}