package business_objects.db.clickhouse.mt_mt4_trades_coerced;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MtMt4TradesCoercedObject {

    private String brand;
    private String regulator;
    private Long userId;
    private String ucid;
    private Long account;
    private String platform;
    private Long serverId;
    private String serverName;
    private String accountType;
    private String accountGroup;
    private String accountCurrency;
    private Long ticket;
    private Long cmd;
    private String ticketType;
    private Long reason;
    private String reasonName;
    private Long contractSize;
    private String openTime;
    private String openTimeUtc;
    private Double openPrice;
    private Double stopLoss;
    private Double takeProfit;
    private String symbol;
    private String symbolUnderlying;
    private String baseCurrency;
    private String quoteCurrency;
    private Double openRateUsdBase;
    private Double openRateUsdQuote;
    private Double openRateUsdAcc;
    private Double closeRateUsdBase;
    private Double closeRateUsdQuote;
    private Double closeRateUsdAcc;
    private Long volume;
    private Double volumeLots;
    private Double notionalValueUsd;
    private Double openNotionalValueUsd;
    private Double closeNotionalValueUsd;
    private Double profit;
    private Double storage;
    private Double commission;
    private Double profitUsd;
    private Double storageUsd;
    private Double commissionUsd;
    private String closeTime;
    private String closeTimeUtc;
    private Double closePrice;
    private Long positionId;
    private String comment;
    private Double spreadRevenueUsd;
    private Double taxesUsd;
    private Double feeUsd;
    private Long isDeleted;
    private String lastUpdated;
    private String internalComment;
}
