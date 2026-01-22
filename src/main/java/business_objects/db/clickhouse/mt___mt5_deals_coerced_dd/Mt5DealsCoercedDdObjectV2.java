package business_objects.db.clickhouse.mt___mt5_deals_coerced_dd;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Mt5DealsCoercedDdObjectV2 {

    private String brand;
    private String regulator;
    private Integer userId;
    private String ucid;
    private Long account;
    private String platform;
    private Integer serverId;
    private String serverName;
    private String accountType;
    private String accountGroup;
    private String accountCurrency;
    private Long deal;
    private Long order;
    private Integer action;
    private Integer entry;
    private Integer reason;
    private Long contractSize;
    private String time;
    private String timeUtc;
    private String symbol;
    private String symbolUnderlying;
    private String baseCurrency;
    private String quoteCurrency;
    private Double rateUsdBase;
    private Double rateUsdQuote;
    private Double rateUsdAcc;
    private Double price;
    private Long volume;
    private Double volumeLots;
    private Double notionalValueUsd;
    private Double profit;
    private Double storage;
    private Double commission;
    private Double profitUsd;
    private Double storageUsd;
    private Double commissionUsd;
    private Long expertId;
    private Long positionId;
    private String comment;
    private Double sl;
    private Double tp;
    private Double priceGateway;
    private Double marketBid;
    private Double marketAsk;
    private Double rateProfit;
    private Integer isDeleted;
    private String lastUpdated;
    private String internalComment;
    private Integer isAbnormalTime;
    private Integer leverage;
    private Double balance;
    private Double equity;
    private Double margin;
    private Double freeMargin;
    private Double balanceUsd;
    private Double equityUsd;
    private Double marginUsd;
    private Double freeMarginUsd;
    private Double openPositionsNvUsd;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mt5DealsCoercedDdObjectV2 that = (Mt5DealsCoercedDdObjectV2) o;
        return Objects.equals(brand, that.brand)
                && Objects.equals(regulator, that.regulator)
                && Objects.equals(userId, that.userId)
                && Objects.equals(ucid, that.ucid)
                && Objects.equals(account, that.account)
                && Objects.equals(platform, that.platform)
                && Objects.equals(serverId, that.serverId)
                && Objects.equals(serverName, that.serverName)
                && Objects.equals(accountType, that.accountType)
                && Objects.equals(accountGroup, that.accountGroup)
                && Objects.equals(accountCurrency, that.accountCurrency)
                && Objects.equals(deal, that.deal)
                && Objects.equals(order, that.order)
                && Objects.equals(action, that.action)
                && Objects.equals(entry, that.entry)
                && Objects.equals(reason, that.reason)
                && Objects.equals(contractSize, that.contractSize)
                && Objects.equals(time, that.time)
                && Objects.equals(timeUtc, that.timeUtc)
                && Objects.equals(symbol, that.symbol)
                && Objects.equals(symbolUnderlying, that.symbolUnderlying)
                && Objects.equals(baseCurrency, that.baseCurrency)
                && Objects.equals(quoteCurrency, that.quoteCurrency)
                && Objects.equals(rateUsdBase, that.rateUsdBase)
                && Objects.equals(rateUsdQuote, that.rateUsdQuote)
                && Objects.equals(rateUsdAcc, that.rateUsdAcc)
                && Objects.equals(price, that.price)
                && Objects.equals(volume, that.volume)
                && Objects.equals(volumeLots, that.volumeLots)
                && Objects.equals(notionalValueUsd, that.notionalValueUsd)
                && Objects.equals(profit, that.profit)
                && Objects.equals(storage, that.storage)
                && Objects.equals(commission, that.commission)
                && Objects.equals(profitUsd, that.profitUsd)
                && Objects.equals(storageUsd, that.storageUsd)
                && Objects.equals(commissionUsd, that.commissionUsd)
                && Objects.equals(expertId, that.expertId)
                && Objects.equals(positionId, that.positionId)
                && Objects.equals(comment, that.comment)
                && Objects.equals(sl, that.sl)
                && Objects.equals(tp, that.tp)
                && Objects.equals(priceGateway, that.priceGateway)
                && Objects.equals(marketBid, that.marketBid)
                && Objects.equals(marketAsk, that.marketAsk)
                && Objects.equals(rateProfit, that.rateProfit)
                && Objects.equals(isDeleted, that.isDeleted)
                && Objects.equals(lastUpdated, that.lastUpdated)
                && Objects.equals(internalComment, that.internalComment)
                && Objects.equals(isAbnormalTime, that.isAbnormalTime)
                && Objects.equals(leverage, that.leverage)
                && Objects.equals(balance, that.balance)
                && Objects.equals(equity, that.equity)
                && Objects.equals(margin, that.margin)
                && Objects.equals(freeMargin, that.freeMargin)
                && Objects.equals(balanceUsd, that.balanceUsd)
                && Objects.equals(equityUsd, that.equityUsd)
                && Objects.equals(marginUsd, that.marginUsd)
                && Objects.equals(freeMarginUsd, that.freeMarginUsd)
                && Objects.equals(openPositionsNvUsd, that.openPositionsNvUsd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                brand,
                regulator,
                userId,
                ucid,
                account,
                platform,
                serverId,
                serverName,
                accountType,
                accountGroup,
                accountCurrency,
                deal,
                order,
                action,
                entry,
                reason,
                contractSize,
                time,
                timeUtc,
                symbol,
                symbolUnderlying,
                baseCurrency,
                quoteCurrency,
                rateUsdBase,
                rateUsdQuote,
                rateUsdAcc,
                price,
                volume,
                volumeLots,
                notionalValueUsd,
                profit,
                storage,
                commission,
                profitUsd,
                storageUsd,
                commissionUsd,
                expertId,
                positionId,
                comment,
                sl,
                tp,
                priceGateway,
                marketBid,
                marketAsk,
                rateProfit,
                isDeleted,
                lastUpdated,
                internalComment,
                isAbnormalTime,
                leverage,
                balance,
                equity,
                margin,
                freeMargin,
                balanceUsd,
                equityUsd,
                marginUsd,
                freeMarginUsd,
                openPositionsNvUsd);
    }
}
