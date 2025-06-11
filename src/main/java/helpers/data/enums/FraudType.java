package helpers.data.enums;

import java.util.Random;

public enum FraudType {
    HEDGING("HEDGING", "Hedging", "Client are engaging in Hedging (Mirror trading) fraud in order to abuse our deposit bonus scheme and gain guaranteed profit through their trades."), LATENCY_ARBITRAGE("LATENCY_ARBITRAGE", "Latency arbitrage", "Client is taking advantage of delays in our quotes from liquidity providers to perform high-frequency trades for guaranteed profit."), CPA_ABUSE("CPA_ABUSE", "CPA abuse", "Clients under CPA only fulfill the minimum requirements to be eligible for CPA rebates and then stop all trading activity and withdraw their funds. By doing this, they can generate high amounts of rebates with minimal risk."), BONUS_ABUSE("BONUS_ABUSE", "Bonus abuse", "Client is suspected to have registered this account to abuse our bonuses, such as our No Deposit Bonus scheme, to get credits and gain guaranteed, risk-free profit."), LOSS_VOUCHER_ABUSE("LOSS_VOUCHER_ABUSE", "Loss voucher abuse", "By placing hedged orders within 1 account or across 2 different ones, client can utilize loss vouchers to get guaranteed profit by offsetting their losses by funds that can be withdrawn."), NBP_ABUSE("NBP_ABUSE", "NBP abuse", "Client is abusing the negative balance protection feature to hedge and gain guaranteed profit."), GAP_TRADING("GAP_TRADING", "Gap trading", "Client is taking advantage of high leverage to open positions by the time of market close. Then close the positions immediately after the market open. High profit due to high leverage and limited loss due to negative balance protection."), REBATE_CHURNING("REBATE_CHURNING", "Rebate churning", "Client is abusing any kind of loophole to repeatedly  open and close the positions without cost. Now most cases should be covered by IVR."), TLS_ABUSE("TLS_ABUSE", "TLS abuse", "Clients is taking advantage of the forex mid price bias during the EOD time to do the high win rate trades."), LOOPHOLE_ABUSE("LOOPHOLE_ABUSE", "Loophole abuse", "Client is abusing any kind of loophole to arbitrage."), NEWS_TRADER("NEWS_TRADER", "News trader", "Client is using some EAs to detect the market price that react not enough to perform high-frequent trades with high win-rate trades."), CHARGEBACK("CHARGEBACK", "Chargeback", "A type of fraud where a client disputes a previously completed payment (e.g. account funding) in an attempt to reclaim the funds after they have already been used for trading"), MARKET_MANIPULATION("MARKET_MANIPULATION", "Market manipulation", "Client is manipulating real market prices and takes advantage of the predicted price movements in our broker, thus making large guaranteed profits."), SWAP_ARBITRAGE("SWAP_ARBITRAGE", "Swap arbitrage", "Client is only opening the swap-receiving direction positions and hedging the positions outside to abuse swap-free trading environment."), PRICING_ERROR("PRICING_ERROR", "Pricing errors", "Client is taking advantage of errors in our quotes/pricing in order to make guaranteed profits."), SLIPPAGE_FREE_ABUSE("SLIPPAGE_FREE_ABUSE", "Slippage-free abuser", "For those clients are abusing our slippage-free trading environment. The client will frequently use stop orders (buy stop、sell stop、stop loss) and modify the corresponding price to make a higher win rate in the volatile market. These clients often combines with price latency or system configuration loopholes to increase arbitrage opportunities.");

    private final String code;
    private final String name;
    private final String description;

    FraudType(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "FraudType{" + "code='" + code + '\'' + ", name='" + name + '\'' + ", description='" + description + '\'' + '}';
    }

    public static FraudType getRandomFraudType() {
        FraudType[] frauds = values();
        Random random = new Random();
        return frauds[random.nextInt(frauds.length)];
    }
}

