package tests.ruleEngineServiceTests;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static utils.Constants.*;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_MIRROR_TRADING_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_SERVICE)
@Tag(TAG_MANUAL)
public class MirrorTradeRuleTest {

    @Test
    @DisplayName("Mirror trading rule exit Event_End_1")
    @AllureId("179")
    public void mirrorTradeRuleExitEventEnd1Test() {
        Allure.step("Client has no previous restrictions");
        Allure.step("Client has no connected account (or have, bu connected account has no bonuses)");
        Allure.step("Account has a credit");
        Allure.step("CreditEquityRatio > 0.7");
        Allure.step("Client use payment method from a gray list");
        Allure.step("Sum of abuse score > 4");
        Allure.step("RiskFreeRevenueRatio > 0.5");
        Allure.step("TradingOnNewsPeriods is False");
        Allure.step("Dummy trades is False");
        Allure.step("count(tradesWithStopouts)/count(trades) > 0.8 is False");
        Allure.step("count(balanceOrdersWithTypeWO) > 0 is True");
        Allure.step("SUM(mirrorAccountsByTradesClient.Volime)/SUM(mirrorAccountsByTradesDoppelganger) > 0.9 is True");
        Allure.step("Set restriction");
        Allure.step("Send alert");
    }

    @Test
    @DisplayName("Mirror trading rule exit Event_End_7_1")
    @AllureId("178")
    public void mirrorTradeRuleExitEventEnd7_1Test() {
        Allure.step("Client has no previous restrictions");
        Allure.step("Client has no connected account (or have, bu connected account has no bonuses)");
        Allure.step("Account has a credit");
        Allure.step("CreditEquityRatio > 0.7");
        Allure.step("Client use payment method from a gray list");
        Allure.step("Sum of abuse score > 4");
        Allure.step("RiskFreeRevenueRatio > 0.5");
        Allure.step("TradingOnNewsPeriods is False");
        Allure.step("Dummy trades is False");
        Allure.step("count(tradesWithStopouts)/count(trades) > 0.8 is False");
        Allure.step("count(balanceOrdersWithTypeWO) > 0 is True");
        Allure.step("SUM(mirrorAccountsByTradesClient.Volime)/SUM(mirrorAccountsByTradesDoppelganger) > 0.9 is False");
        Allure.step("Set restriction");
        Allure.step("Send alert");
    }

    @Test
    @DisplayName("Mirror trading rule exit Event_End_7_2")
    @AllureId("177")
    public void mirrorTradeRuleExitEventEnd7_2Test() {
        Allure.step("Client has no previous restrictions");
        Allure.step("Client has no connected account (or have, bu connected account has no bonuses)");
        Allure.step("Account has a credit");
        Allure.step("CreditEquityRatio > 0.7");
        Allure.step("Client use payment method from a gray list");
        Allure.step("Sum of abuse score > 4");
        Allure.step("RiskFreeRevenueRatio > 0.5");
        Allure.step("TradingOnNewsPeriods is True");
        Allure.step("SUM(mirrorAccountsByTradesClient.Volime)/SUM(mirrorAccountsByTradesDoppelganger) > 0.9 is False");
        Allure.step("Set restriction");
        Allure.step("Send alert");
    }

    @Test
    @DisplayName("Mirror trading rule exit Event_End_7_3")
    @AllureId("176")
    public void mirrorTradeRuleExitEventEnd7_3Test() {
        Allure.step("Client has no previous restrictions");
        Allure.step("Client has no connected account (or have, bu connected account has no bonuses)");
        Allure.step("Account has a credit");
        Allure.step("CreditEquityRatio > 0.7");
        Allure.step("Client use payment method from a gray list");
        Allure.step("Sum of abuse score > 4");
        Allure.step("RiskFreeRevenueRatio > 0.5");
        Allure.step("TradingOnNewsPeriods is False");
        Allure.step("Dummy trades is True");
        Allure.step("SUM(mirrorAccountsByTradesClient.Volime)/SUM(mirrorAccountsByTradesDoppelganger) > 0.9 is False");
        Allure.step("Set restriction");
        Allure.step("Send alert");
    }

    @Test
    @DisplayName("Mirror trading rule exit Event_End_7_4")
    @AllureId("175")
    public void mirrorTradeRuleExitEventEnd7_4Test() {
        Allure.step("Client has no previous restrictions");
        Allure.step("Client has no connected account (or have, bu connected account has no bonuses)");
        Allure.step("Account has a credit");
        Allure.step("CreditEquityRatio > 0.7");
        Allure.step("Client use payment method from a gray list");
        Allure.step("Sum of abuse score > 4");
        Allure.step("RiskFreeRevenueRatio > 0.5");
        Allure.step("TradingOnNewsPeriods is False");
        Allure.step("Dummy trades is False");
        Allure.step("count(tradesWithStopouts)/count(trades) > 0.8 is True");
        Allure.step("SUM(mirrorAccountsByTradesClient.Volime)/SUM(mirrorAccountsByTradesDoppelganger) > 0.9 is False");
        Allure.step("Set restriction");
        Allure.step("Send alert");
    }

    @Test
    @DisplayName("Mirror trading rule exit Event_End_7_5")
    @AllureId("174")
    public void mirrorTradeRuleExitEventEnd7_5Test() {
        Allure.step("Client has no previous restrictions");
        Allure.step("Client has no connected account (or have, bu connected account has no bonuses)");
        Allure.step("Account has a credit");
        Allure.step("CreditEquityRatio > 0.7");
        Allure.step("Client use payment method from a gray list");
        Allure.step("Sum of abuse score > 4");
        Allure.step("RiskFreeRevenueRatio > 0.5");
        Allure.step("TradingOnNewsPeriods is False");
        Allure.step("Dummy trades is False");
        Allure.step("count(tradesWithStopouts)/count(trades) > 0.8 is False");
        Allure.step("count(balanceOrdersWithTypeWO) > 0 is False");
        Allure.step("Set restriction");
        Allure.step("Send alert");
    }

    @Test
    @DisplayName("Mirror trading rule exit Event_End_6")
    @AllureId("173")
    public void mirrorTradeRuleExitEventEnd6Test() {
        Allure.step("Client has no previous restrictions");
        Allure.step("Client has no connected account (or have, bu connected account has no bonuses)");
        Allure.step("Account has a credit");
        Allure.step("CreditEquityRatio > 0.7");
        Allure.step("Client use payment method from a gray list");
        Allure.step("Sum of abuse score > 4");
        Allure.step("RiskFreeRevenueRatio > 0.5 is False");
        Allure.step("Exit without alert");
    }

    @Test
    @DisplayName("Mirror trading rule exit Event_End_5_1")
    @AllureId("181")
    public void mirrorTradeRuleExitEventEnd5_1Test() {
        Allure.step("Client has no previous restrictions");
        Allure.step("Client has no connected account (or have, bu connected account has no bonuses)");
        Allure.step("Account has a credit");
        Allure.step("CreditEquityRatio > 0.7");
        Allure.step("Client use payment method from a gray list");
        Allure.step("Payment method is on the gray list. + 1 abuse score");
        Allure.step("tdBonus.amount/ ftdDeposit.amount >= 0.3. + 1 abuse score");
        Allure.step("trades.groupBySymbol < 5. + 1 abuse score");
        Allure.step("Registration country != last login login country. + 1 abuse score");
        Allure.step("Sum of abuse score > 4 is False");
        Allure.step("Exit without alert");
    }

    @Test
    @DisplayName("Mirror trading rule exit Event_End_5_2")
    @AllureId("182")
    public void mirrorTradeRuleExitEventEnd5_2Test() {
        Allure.step("Client has no previous restrictions");
        Allure.step("Client has no connected account (or have, bu connected account has no bonuses)");
        Allure.step("Account has a credit");
        Allure.step("CreditEquityRatio > 0.7");
        Allure.step("Client use payment method from a gray list");
        Allure.step("lexisNexis.riskRating in ('high', 'medium'). + 2 abuse score");
        Allure.step("now() -client.dateRegistration < 168h. + 1 abuse score");
        Allure.step("Sum of abuse score > 4 is False");
        Allure.step("Exit without alert");
    }

    @Test
    @DisplayName("Mirror trading rule exit Event_End_4_1")
    @AllureId("183")
    public void mirrorTradeRuleExitEventEnd4Test() {
        Allure.step("Client has no previous restrictions");
        Allure.step("Client has no connected account (or have, bu connected account has no bonuses)");
        Allure.step("Account has a credit is False");
        Allure.step("Exit without alert");
    }

    @Test
    @DisplayName("Mirror trading rule exit Event_End_4_2")
    @AllureId("184")
    public void mirrorTradeRuleExitEventEnd4_2Test() {
        Allure.step("Client has no previous restrictions");
        Allure.step("Client has no connected account (or have, bu connected account has no bonuses)");
        Allure.step("Account has a credit");
        Allure.step("CreditEquityRatio > 0.7 is False");
        Allure.step("Exit without alert");
    }

    @Test
    @DisplayName("Mirror trading rule exit Event_End_3_1")
    @AllureId("185")
    public void mirrorTradeRuleExitEventEnd3_1Test() {
        Allure.step("Client has no previous restrictions");
        Allure.step("Client has mirror trading abuse connected account");
        Allure.step("Set restriction");
        Allure.step("Send alert");
    }

    @Test
    @DisplayName("Mirror trading rule exit Event_End_3_2")
    @AllureId("186")
    public void mirrorTradeRuleExitEventEnd3_2Test() {
        Allure.step("Client has no previous restrictions");
        Allure.step("Client doesn't has mirror trading abuse connected account");
        Allure.step("Client has connected account with bonuses");
        Allure.step("Set restriction");
        Allure.step("Send alert");
    }

    @Test
    @DisplayName("Mirror trading rule exit Event_End_2")
    @AllureId("187")
    public void mirrorTradeRuleExitEventEnd2Test() {
        Allure.step("Client has previous restrictions");
        Allure.step("Exit without alert");
    }
}
