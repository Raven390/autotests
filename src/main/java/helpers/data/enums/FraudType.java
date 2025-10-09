package helpers.data.enums;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;

public enum FraudType {
    HEDGING("HEDGING", "Hedging", "Client are engaging in Hedging (Mirror trading) fraud in order to abuse our deposit bonus scheme and gain guaranteed profit through their trades."), LATENCY_ARBITRAGE("LATENCY_ARBITRAGE", "Latency arbitrage", "Client is taking advantage of delays in our quotes from liquidity providers to perform high-frequency trades for guaranteed profit."), CPA_ABUSE("CPA_ABUSE", "CPA abuse", "Clients under CPA only fulfill the minimum requirements to be eligible for CPA rebates and then stop all trading activity and withdraw their funds. By doing this, they can generate high amounts of rebates with minimal risk."), BONUS_ABUSE("BONUS_ABUSE", "Bonus abuse", "Client is suspected to have registered this account to abuse our bonuses, such as our No Deposit Bonus scheme, to get credits and gain guaranteed, risk-free profit."), LOSS_VOUCHER_ABUSE("LOSS_VOUCHER_ABUSE", "Loss voucher abuse", "By placing hedged orders within 1 account or across 2 different ones, client can utilize loss vouchers to get guaranteed profit by offsetting their losses by funds that can be withdrawn."), NBP_ABUSE("NBP_ABUSE", "NBP abuse", "Client is abusing the negative balance protection feature to hedge and gain guaranteed profit."), GAP_TRADING("GAP_TRADING", "Gap trading", "Client is taking advantage of high leverage to open positions by the time of market close. Then close the positions immediately after the market open. High profit due to high leverage and limited loss due to negative balance protection."), REBATE_CHURNING("REBATE_CHURNING", "Rebate churning", "Client is abusing any kind of loophole to repeatedly  open and close the positions without cost. Now most cases should be covered by IVR."), TLS_ABUSE("TLS_ABUSE", "TLS abuse", "Clients is taking advantage of the forex mid price bias during the EOD time to do the high win rate trades."), LOOPHOLE_ABUSE("LOOPHOLE_ABUSE", "Loophole abuse", "Client is abusing any kind of loophole to arbitrage."), HFT_ABUSE("HFT_ABUSE", "HFT abuse", "Client is constantly profiting in high-frequent trading with extremely high win rate, and we have no idea what the client is actually doing. This is similar to MM sometimes.", false), NEWS_TRADER("NEWS_TRADER", "News trader", "Client is using some EAs to detect the market price that react not enough to perform high-frequent trades with high win-rate trades."), MARKET_MANIPULATION("MARKET_MANIPULATION", "Market manipulation", "Client is manipulating real market prices and takes advantage of the predicted price movements in our broker, thus making large guaranteed profits."), SWAP_ARBITRAGE("SWAP_ARBITRAGE", "Swap arbitrage", "Client is only opening the swap-receiving direction positions and hedging the positions outside to abuse swap-free trading environment."), PRICING_ERROR("PRICING_ERROR", "Pricing errors", "Client is taking advantage of errors in our quotes/pricing in order to make guaranteed profits."), SLIPPAGE_FREE_ABUSE("SLIPPAGE_FREE_ABUSE", "Slippage-free abuser", "For those clients are abusing our slippage-free trading environment. The client will frequently use stop orders (buy stop、sell stop、stop loss) and modify the corresponding price to make a higher win rate in the volatile market. These clients often combines with price latency or system configuration loopholes to increase arbitrage opportunities."), EXCHANGER("EXCHANGER", "Exchanger", "Client uses their trading account primarily as a money transfer / exchange channel rather than for genuine trading. Typically involves frequent deposits and withdrawals with little or no trading activity."), UPGRADER("UPGRADER", "Upgrader", "Client attempts to upgrade limits, verification level, or account privileges using suspicious or fraudulent payment methods. Deposits are made with the sole purpose of unlocking higher withdrawal or trading limits, not genuine use."), CLAIMER("CLAIMER", "Claimer", "Client claims non-receipt or disputes legitimate payments to trigger refunds, bonuses, or compensations, often while successfully using the platform."), CHARGEBACK("CHARGEBACK", "Chargeback", "Client funds the trading account, uses services (e.g. trading, bonuses), then initiates chargebacks with their payment provider to reclaim deposited funds."), MONEY_LAUNDRY("MONEY_LAUNDRY", "Money launderer", "Client engages in money laundering via the trading account, using deposits and withdrawals to disguise the origin of funds."), ATO("ATO", "Account takeover", "Fraudster gains unauthorized access to a legitimate client account and uses it to withdraw or move funds."), RAF_ABUSE("RAF_ABUSE", "RAF abuse", "", false);

    private final String code;
    private final String name;
    private final String description;
    private final boolean isVisible;

    FraudType(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.isVisible = true;
    }

    FraudType(String code, String name, String description, boolean isVisible) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.isVisible = isVisible;
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

    public boolean isVisible() {
        return isVisible;
    }

    @Override
    public String toString() {
        return "FraudType{" + "code='" + code + '\'' + ", name='" + name + '\'' + ", description='" + description + '\'' + ", isVisible=" + isVisible + '}';
    }

    public static FraudType getRandomFraudType() {
        FraudType[] frauds = values();
        SecureRandom random = new SecureRandom();
        return frauds[random.nextInt(frauds.length)];
    }

    public static List<String> getVisibleFraudTypeNamesList() {
        return Arrays.stream(values()).filter(FraudType::isVisible).map(FraudType::getName).toList();
    }

}

