package helpers.data.enums;

import static helpers.data.enums.FraudCategory.*;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;

@Getter
public enum FraudType {
    HEDGING(
            "HEDGING",
            "Hedging",
            "Client are engaging in Hedging (Mirror trading) fraud in order to abuse our deposit bonus scheme and gain guaranteed profit through their trades.",
            TRADING),
    LATENCY_ARBITRAGE(
            "LATENCY_ARBITRAGE",
            "Latency arbitrage",
            "Client is taking advantage of delays in our quotes from liquidity providers to perform high-frequency trades for guaranteed profit.",
            TRADING),
    CPA_ABUSE(
            "CPA_ABUSE",
            "CPA abuse",
            "Clients under CPA only fulfill the minimum requirements to be eligible for CPA rebates and then stop all trading activity and withdraw their funds. By doing this, they can generate high amounts of rebates with minimal risk.",
            TRADING),
    BONUS_ABUSE(
            "BONUS_ABUSE",
            "Bonus abuse",
            "Client is suspected to have registered this account to abuse our bonuses, such as our No Deposit Bonus scheme, to get credits and gain guaranteed, risk-free profit.",
            TRADING),
    LOSS_VOUCHER_ABUSE(
            "LOSS_VOUCHER_ABUSE",
            "Loss voucher abuse",
            "By placing hedged orders within 1 account or across 2 different ones, client can utilize loss vouchers to get guaranteed profit by offsetting their losses by funds that can be withdrawn.",
            TRADING),
    NBP_ABUSE(
            "NBP_ABUSE",
            "NBP abuse",
            "Client is abusing the negative balance protection feature to hedge and gain guaranteed profit.",
            TRADING),
    GAP_TRADING(
            "GAP_TRADING",
            "Gap trading",
            "Client is taking advantage of high leverage to open positions by the time of market close. Then close the positions immediately after the market open. High profit due to high leverage and limited loss due to negative balance protection.",
            TRADING),
    REBATE_CHURNING(
            "REBATE_CHURNING",
            "Rebate churning",
            "Client is abusing any kind of loophole to repeatedly  open and close the positions without cost. Now most cases should be covered by IVR.",
            TRADING),
    TLS_ABUSE(
            "TLS_ABUSE",
            "TLS abuse",
            "Clients is taking advantage of the forex mid price bias during the EOD time to do the high win rate trades.",
            TRADING),
    LOOPHOLE_ABUSE("LOOPHOLE_ABUSE", "Loophole abuse", "Client is abusing any kind of loophole to arbitrage.", TRADING),
    HFT_ABUSE(
            "HFT_ABUSE",
            "HFT abuse",
            "Client is constantly profiting in high-frequent trading with extremely high win rate, and we have no idea what the client is actually doing. This is similar to MM sometimes.",
            false,
            TRADING),
    NEWS_TRADER(
            "NEWS_TRADER",
            "News trader",
            "Client is using some EAs to detect the market price that react not enough to perform high-frequent trades with high win-rate trades.",
            TRADING),
    MARKET_MANIPULATION(
            "MARKET_MANIPULATION",
            "Market manipulation",
            "Client is manipulating real market prices and takes advantage of the predicted price movements in our broker, thus making large guaranteed profits.",
            TRADING),
    SWAP_ARBITRAGE(
            "SWAP_ARBITRAGE",
            "Swap arbitrage",
            "Client is only opening the swap-receiving direction positions and hedging the positions outside to abuse swap-free trading environment.",
            TRADING),
    PRICING_ERROR(
            "PRICING_ERROR",
            "Pricing errors",
            "Client is taking advantage of errors in our quotes/pricing in order to make guaranteed profits.",
            TRADING),
    SLIPPAGE_FREE_ABUSE(
            "SLIPPAGE_FREE_ABUSE",
            "Slippage-free abuser",
            "For those clients are abusing our slippage-free trading environment. The client will frequently use stop orders (buy stop、sell stop、stop loss) and modify the corresponding price to make a higher win rate in the volatile market. These clients often combines with price latency or system configuration loopholes to increase arbitrage opportunities.",
            TRADING),
    EXCHANGER(
            "EXCHANGER",
            "Exchanger",
            "Client uses their trading account primarily as a money transfer / exchange channel rather than for genuine trading. Typically involves frequent deposits and withdrawals with little or no trading activity.",
            PAYMENT),
    UPGRADER(
            "UPGRADER",
            "Upgrader",
            "Client attempts to upgrade limits, verification level, or account privileges using suspicious or fraudulent payment methods. Deposits are made with the sole purpose of unlocking higher withdrawal or trading limits, not genuine use.",
            PAYMENT),
    CLAIMER(
            "CLAIMER",
            "Claimer",
            "Client claims non-receipt or disputes legitimate payments to trigger refunds, bonuses, or compensations, often while successfully using the platform.",
            PAYMENT),
    CHARGEBACK(
            "CHARGEBACK",
            "Chargeback",
            "Client funds the trading account, uses services (e.g. trading, bonuses), then initiates chargebacks with their payment provider to reclaim deposited funds.",
            BOTH),
    MONEY_LAUNDRY(
            "MONEY_LAUNDRY",
            "Money launderer",
            "Client engages in money laundering via the trading account, using deposits and withdrawals to disguise the origin of funds.",
            PAYMENT),
    ATO(
            "ATO",
            "Account takeover",
            "Fraudster gains unauthorized access to a legitimate client account and uses it to withdraw or move funds.",
            PAYMENT),
    RAF_ABUSE("RAF_ABUSE", "RAF abuse", "", false, PAYMENT);

    private final String code;
    private final String name;
    private final String description;
    private final boolean isVisible;
    private final FraudCategory category;

    public static FraudType valueOfName(String name) {
        return Arrays.stream(values())
                .filter(f -> f.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No enum constant with name: " + name));
    }

    public static FraudType valueOfCode(String code) {
        return Arrays.stream(values())
                .filter(f -> f.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No enum constant with name: " + code));
    }

    FraudType(String code, String name, String description, FraudCategory category) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.category = category;
        this.isVisible = true;
    }

    FraudType(String code, String name, String description, boolean isVisible, FraudCategory category) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.isVisible = isVisible;
        this.category = category;
    }

    @Override
    public String toString() {
        return "FraudType{" + "code='" + code + '\'' + ", name='" + name + '\'' + ", description='" + description + '\''
                + ", isVisible=" + isVisible + ", category='" + category + '\'' + '}';
    }

    public static FraudType getRandomFraudType() {
        FraudType[] frauds = values();
        SecureRandom random = new SecureRandom();
        return frauds[random.nextInt(frauds.length)];
    }

    public static List<String> getVisibleFraudTypeNamesList() {
        return Arrays.stream(values())
                .filter(FraudType::isVisible)
                .map(FraudType::getName)
                .toList();
    }

    public static List<String> getPaymentFraudTypeNamesList() {
        return Arrays.stream(values())
                .filter(f -> (f.getCategory() == PAYMENT || f.getCategory() == BOTH) && f.isVisible())
                .map(FraudType::getName)
                .toList();
    }

    public static List<String> getTradingFraudTypeNamesList() {
        return Arrays.stream(values())
                .filter(f -> (f.getCategory() == TRADING || f.getCategory() == BOTH) && f.isVisible())
                .map(FraudType::getName)
                .toList();
    }
}
