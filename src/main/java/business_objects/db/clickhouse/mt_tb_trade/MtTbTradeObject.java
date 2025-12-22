package business_objects.db.clickhouse.mt_tb_trade;

import java.util.Objects;

public class MtTbTradeObject {

    public String platform;
    public Integer ticket;
    public Integer account;
    public String currency;
    public String symbol;
    public String type;
    public String openTime;
    public Double openPrice;
    public String closeTime;
    public Double closePrice;
    public Double stopLoss;
    public Double takeProfit;
    public Double commissionUsd;
    public Double swapUsd;
    public Double profitUsd;
    public String reason;
    public String comment;
    public Double spreadRevenueUsd;
    public Double volumeUsd;
    public Double volumeLots;
    public Double taxesUsd;
    public Double feeUsd;
    public Integer serverId;
    public String lastUpdated;

    public MtTbTradeObject() {}

    public MtTbTradeObject(
            String platform,
            Integer ticket,
            Integer account,
            String currency,
            String symbol,
            String type,
            String openTime,
            Double openPrice,
            String closeTime,
            Double closePrice,
            Double stopLoss,
            Double takeProfit,
            Double commissionUsd,
            Double swapUsd,
            Double profitUsd,
            String reason,
            String comment,
            Double spreadRevenueUsd,
            Double volumeUsd,
            Double volumeLots,
            Double taxesUsd,
            Double feeUsd,
            Integer serverId,
            String lastUpdated) {
        this.platform = platform;
        this.ticket = ticket;
        this.account = account;
        this.currency = currency;
        this.symbol = symbol;
        this.type = type;
        this.openTime = openTime;
        this.openPrice = openPrice;
        this.closeTime = closeTime;
        this.closePrice = closePrice;
        this.stopLoss = stopLoss;
        this.takeProfit = takeProfit;
        this.commissionUsd = commissionUsd;
        this.swapUsd = swapUsd;
        this.profitUsd = profitUsd;
        this.reason = reason;
        this.comment = comment;
        this.spreadRevenueUsd = spreadRevenueUsd;
        this.volumeUsd = volumeUsd;
        this.volumeLots = volumeLots;
        this.taxesUsd = taxesUsd;
        this.feeUsd = feeUsd;
        this.serverId = serverId;
        this.lastUpdated = lastUpdated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MtTbTradeObject that = (MtTbTradeObject) o;
        return Objects.equals(platform, that.platform)
                && Objects.equals(ticket, that.ticket)
                && Objects.equals(account, that.account)
                && Objects.equals(currency, that.currency)
                && Objects.equals(symbol, that.symbol)
                && Objects.equals(type, that.type)
                && Objects.equals(openTime, that.openTime)
                && Objects.equals(openPrice, that.openPrice)
                && Objects.equals(closeTime, that.closeTime)
                && Objects.equals(closePrice, that.closePrice)
                && Objects.equals(stopLoss, that.stopLoss)
                && Objects.equals(takeProfit, that.takeProfit)
                && Objects.equals(commissionUsd, that.commissionUsd)
                && Objects.equals(swapUsd, that.swapUsd)
                && Objects.equals(profitUsd, that.profitUsd)
                && Objects.equals(reason, that.reason)
                && Objects.equals(comment, that.comment)
                && Objects.equals(spreadRevenueUsd, that.spreadRevenueUsd)
                && Objects.equals(volumeUsd, that.volumeUsd)
                && Objects.equals(volumeLots, that.volumeLots)
                && Objects.equals(taxesUsd, that.taxesUsd)
                && Objects.equals(feeUsd, that.feeUsd)
                && Objects.equals(serverId, that.serverId)
                && Objects.equals(lastUpdated, that.lastUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                platform,
                ticket,
                account,
                currency,
                symbol,
                type,
                openTime,
                openPrice,
                closeTime,
                closePrice,
                stopLoss,
                takeProfit,
                commissionUsd,
                swapUsd,
                profitUsd,
                reason,
                comment,
                spreadRevenueUsd,
                volumeUsd,
                volumeLots,
                taxesUsd,
                feeUsd,
                serverId,
                lastUpdated);
    }

    @Override
    public String toString() {
        return "MtTbTradeObject{" + "platform='" + platform + '\'' + ", ticket=" + ticket + ", account=" + account
                + ", currency='" + currency + '\'' + ", symbol='" + symbol + '\'' + ", type='" + type + '\''
                + ", openTime='" + openTime + '\'' + ", openPrice=" + openPrice + ", closeTime='" + closeTime + '\''
                + ", closePrice=" + closePrice + ", stopLoss=" + stopLoss + ", takeProfit=" + takeProfit
                + ", commissionUsd=" + commissionUsd + ", swapUsd=" + swapUsd + ", profitUsd=" + profitUsd
                + ", reason='" + reason + '\'' + ", comment='" + comment + '\'' + ", spreadRevenueUsd="
                + spreadRevenueUsd + ", volumeUsd=" + volumeUsd + ", volumeLots=" + volumeLots + ", taxesUsd="
                + taxesUsd + ", feeUsd=" + feeUsd + ", serverId=" + serverId + ", lastUpdated='" + lastUpdated + '\''
                + '}';
    }
}
