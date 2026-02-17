package business_objects.db.backoffice_db;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IllegalTrades {
    private String account;
    private Integer serverId;
    private Double illegalProfit;
    private Double illegalProfitUsd;
    private String tickets;
    private String symbols;
    private String createdAt;
    private String ucid;
    private Integer ticketCount;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        IllegalTrades that = (IllegalTrades) o;
        return Objects.equals(account, that.account)
                && Objects.equals(serverId, that.serverId)
                && Objects.equals(illegalProfit, that.illegalProfit)
                && Objects.equals(tickets, that.tickets)
                && Objects.equals(symbols, that.symbols)
                && Objects.equals(ucid, that.ucid)
                && Objects.equals(ticketCount, that.ticketCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(account, serverId, illegalProfit, tickets, symbols, ucid, ticketCount);
    }
}
