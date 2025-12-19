package helpers.data.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum InternalReason {

    ACCOUNT_CREATION(
            "ACCOUNT_CREATION", "Due to suspicious activity, the client is not permitted to create additional trading accounts.", false, "RESTRICTION"
    ),

    INTERNAL_TRANSFER(
            "INTERNAL_TRANSFER", "In order to prevent the client from transferring illicit funds from one trading account to another, we are blocking all internal transfers under his user.", false, "RESTRICTION"
    ),

    DEPOSITS(
            "DEPOSITS", "Due to suspicions of illicit activity, the client should not be allowed to deposit funds. This can be due to several reasons, such as money laundering, chargebacks, credit card fraud or any of the trading fraud pattern for which funds are required.", false, "RESTRICTION"
    ),

    WITHDRAWALS(
            "WITHDRAWALS", "Clients who make guaranteed, illegal profits should not be allowed to withdraw funds.", false, "RESTRICTION"
    ),

    LOGIN_CRM(
            "LOGIN_CRM", "Considering the severity of certain clients' actions, their accounts need to be blocked completely. This can be relevant for more serious cases of Market manipulation, thin liquidity scalping, gap trading and more.", false, "RESTRICTION"
    ),

    CLOSE_ONLY_MODE(
            "CLOSE_ONLY_MODE", "Client is abusing our trading environment, so we are not allowing them to open new positions. We still need to wait for them to close the open ones. This restriction can be relevant for any trading fraud case.", false, "RESTRICTION"
    ),

    MANUAL_WITHDRAWAL_REVIEW(
            "MANUAL_WITHDRAWAL_REVIEW", "Client is suspected of taking advantage of our company to gain illegal profits, so we do not wish to allow them to withdraw funds without manual verification and possible deduction of said profits prior to withdrawal.", false, "RESTRICTION"
    ),

    CREDIT_AND_BONUS(
            "CREDIT_AND_BONUS", "Client is suspected of trying to take advantage of our various credit/bonus offerings, such as deposit bonuses, no deposit bonuses, loss vouchers, etc., and thus should not be eligible for these.", false, "RESTRICTION"
    ),

    HEDGING(
            "HEDGING", "Client are engaging in Hedging (Mirror trading) fraud in order to abuse our deposit bonus scheme and gain guaranteed profit through their trades.", false, "FRAUD"
    ),

    LATENCY_ARBITRAGE(
            "LATENCY_ARBITRAGE", "Client is taking advantage of delays in our quotes from liquidity providers to perform high-frequency trades for guaranteed profit.", false, "FRAUD"
    ),

    MARKET_MANIPULATION(
            "MARKET_MANIPULATION", "Client is manipulating real market prices and takes advantage of the predicted price movements in our broker, thus making large guaranteed profits.", false, "FRAUD"
    ),

    PRICING_ERROR(
            "PRICING_ERROR", "Client is taking advantage of errors in our quotes/pricing in order to make guaranteed profits.", false, "FRAUD"
    ),

    GAP_TRADING(
            "GAP_TRADING", "Client is taking advantage of high leverage to open positions by the time of market close. Then close the positions immediately after the market open. High profit due to high leverage and limited loss due to negative balance protection.", false, "FRAUD"
    ),

    SWAP_ARBITRAGE(
            "SWAP_ARBITRAGE", "Client is only opening the swap-receiving direction positions and hedging the positions outside to abuse swap-free trading environment.", false, "FRAUD"
    ),

    CPA_ABUSE(
            "CPA_ABUSE", "Clients under CPA only fulfill the minimum requirements to be eligible for CPA rebates and then stop all trading activity and withdraw their funds. By doing this, they can generate high amounts of rebates with minimal risk.", false, "FRAUD"
    ),

    BONUS_ABUSE(
            "BONUS_ABUSE", "Client is suspected to have registered this account to abuse our bonuses, such as our No Deposit Bonus scheme, to get credits and gain guaranteed, risk-free profit.", false, "FRAUD"
    ),

    RAF_ABUSE(
            "RAF_ABUSE", "Clients abuse our Refer A Friend (RAF) promotion by registering a large number of fake accounts in order to get rebates after them", false, "FRAUD"
    ),

    REBATE_CHURNING(
            "REBATE_CHURNING", "Client is abusing any kind of loophole to repeatedly open and close the positions without cost. Now most cases should be covered by IVR.", false, "FRAUD"
    ),

    LOSS_VOUCHER_ABUSE(
            "LOSS_VOUCHER_ABUSE", "By placing hedged orders within 1 account or across 2 different ones, client can utilize loss vouchers to get guaranteed profit by offsetting their losses by funds that can be withdrawn.", false, "FRAUD"
    ),

    NBP_ABUSE(
            "NBP_ABUSE", "Client is abusing the negative balance protection feature to hedge and gain guaranteed profit.", false, "FRAUD"
    ),

    TLS_ABUSE(
            "TLS_ABUSE", "Clients is taking advantage of the forex mid price bias during the EOD time to do the high win rate trades.", false, "FRAUD"
    ),

    POTENTIAL_ABUSE(
            "POTENTIAL_ABUSE", "Client is displaying suspicious patterns of behaviour, however we cannot (yet) classify him as any of the other categories, this serves as a general 'risky user flag'.", true, "FRAUD"
    ),

    LOOPHOLE_ABUSE(
            "LOOPHOLE_ABUSE", "Client is abusing any kind of loophole to arbitrage.", false, "FRAUD"
    ),

    HFT_ABUSE(
            "HFT_ABUSE", "Client is constantly profiting in high-frequent trading with extremely high win rate, and we have no idea what the client is actually doing. This is similar to MM sometimes.", false, "FRAUD"
    ),

    ANOMALOUS_PROFIT(
            "ANOMALOUS_PROFIT", "Clients have a tendency to utilize sources/methods outside of our scope to generate large amounts of risk free profits.", true, "FRAUD"
    ),

    NEWS_TRADER_BEFORE(
            "NEWS_TRADER", "Client is taking advantage negative balance protection to open positions by the news release with high leverage. Then client can make high profit with limited cost.", true, "FRAUD"
    ),

    NEWS_TRADER_AFTER(
            "NEWS_TRADER", "Client is using some EAs to detect the market price that react not enough to perform high-frequent trades with high win-rate trades.", true, "FRAUD"
    ),

    CONFIRMED_CHARGEBACK(
            "CHARGEBACK", "Client funds the trading account, uses services (e.g. trading, bonuses), then initiates chargebacks with their payment provider to reclaim deposited funds.", true, "FRAUD"
    ),

    POTENTIAL_CHARGEBACK(
            "CHARGEBACK", "Verification documents needed (e.g., deposit transaction proof, card photo). Please instruct the client to check their email and wait for the relevant update. Please reach out to AFOP for further clarification.", true, "FRAUD"
    ),

    EXCHANGER(
            "EXCHANGER", "Accounts are restricted due to concerns of possible money laundering, upgrader-type activity, or non-trading usage. Please advise the client to check inactivity behaviour and trading activity in the account.", true, "FRAUD"
    ),

    UPGRADER(
            "UPGRADER", "Accounts are restricted due to concerns of possible money laundering, upgrader-type activity, or non-trading usage. Please advise the client to check inactivity behaviour and trading activity in the account.", true, "FRAUD"
    );

    private final String code;
    private final String text;
    private final boolean exceptional;
    private final String type;

    InternalReason(String code, String text, boolean exceptional, String type) {
        this.code = code;
        this.text = text;
        this.exceptional = exceptional;
        this.type = type;
    }

    public static InternalReason getByCode(String code) {
        return Arrays.stream(values()).filter(r -> r.code.equalsIgnoreCase(code)).findFirst().orElse(null);
    }

    public static List<InternalReason> getExceptionalReasons() {
        return Arrays.stream(values()).filter(InternalReason::isExceptional).collect(Collectors.toList());
    }

    public static List<InternalReason> getNonExceptionalReasons() {
        return Arrays.stream(values()).filter(r -> !r.isExceptional()).collect(Collectors.toList());
    }

    public static List<InternalReason> getNonExceptionalReasons(String type) {
        return Arrays.stream(values()).filter(r -> !r.isExceptional() && r.getType().equals(type)).collect(Collectors.toList());
    }

    public static List<InternalReason> getExceptionalReasons(String type) {
        return Arrays.stream(values()).filter(r -> r.isExceptional() && r.getType().equals(type)).collect(Collectors.toList());
    }


}
