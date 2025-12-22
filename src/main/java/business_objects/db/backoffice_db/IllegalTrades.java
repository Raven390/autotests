package business_objects.db.backoffice_db;

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
}
