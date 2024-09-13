package tests.ruleEngineTests;

import static utils.Constants.*;

import helpers.data.PaymentHelper;
import helpers.data.UserHelper;
import helpers.kafka.KafkaMessageConsumerHelper;
import io.qameta.allure.*;
import java.sql.SQLException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class MirrorTradeRule {

    @Disabled
    @Test
    @DisplayName("""
            Hedge block flow IF Abuse registry check is YES
            """)
    @Feature(FEATURE_RULE_ENGINE)
    @Owner(OWNER_NIKOLAI_KORIAGIN)
    @Tag(TEAM_CORE)
    @Tag(LAYER_API)
    @AllureId("")
    public void mirrorTradeTest1() throws SQLException {
        UserHelper.createUser(true, "");

        // Triggering the rule with withdrawal attempt
        PaymentHelper.createPayment(PAYMENT_TYPE_WITHDRAWAL, "", 100);

        // Checking message in kafka - HEDGE BLOCK FLOW
        KafkaMessageConsumerHelper.consumeMessages("rule producer topic");
    }

    @Disabled
    @Test
    @DisplayName(
            """
            Hedge block flow
            IF Abuse registry check is NO AND Connection search:
            Linked new accounts with bonus found - exact match YES
            """)
    @Feature(FEATURE_RULE_ENGINE)
    @Owner(OWNER_NIKOLAI_KORIAGIN)
    @Tag(TEAM_CORE)
    @Tag(LAYER_API)
    @AllureId("")
    public void mirrorTradeTest2() throws SQLException {
        UserHelper.createUser(false, "");

        // Create linked user with bonus payment
        UserHelper.createLinkedUser();
        PaymentHelper.createPayment(PAYMENT_TYPE_BONUS, "", 100);

        // Triggering the rule with withdrawal attempt
        PaymentHelper.createPayment(PAYMENT_TYPE_WITHDRAWAL, "", 100);

        // Checking message in kafka - HEDGE BLOCK FLOW
        KafkaMessageConsumerHelper.consumeMessages("rule producer topic");
    }

    @Disabled
    @Test
    @DisplayName(
            """
            Normal withdrawal flow
            IF user NOT in abuse registry
            AND not linked account AND Less than 4 simple abuse point
            """)
    @Feature(FEATURE_RULE_ENGINE)
    @Owner(OWNER_NIKOLAI_KORIAGIN)
    @Tag(TEAM_CORE)
    @Tag(LAYER_API)
    @AllureId("")
    public void mirrorTradeTest3() throws SQLException {
        UserHelper.createUser(false, "");

        // Triggering the rule with withdrawal attempt
        // AND using risky payment method (adding 1 simple abuse points)
        PaymentHelper.createPayment(PAYMENT_TYPE_WITHDRAWAL, PAYMENT_PROVIDER_FASAPAY, 100);

        // Checking message in kafka - normal withdrawal flow
        KafkaMessageConsumerHelper.consumeMessages("rule producer topic");
    }

    @Disabled
    @Test
    @DisplayName(
            """
            Manual investigation
            IF user NOT in abuse registry
            AND not linked account
            AND More than 4 simple abuse point
            AND NOT trading on news
            AND NOT large spikes in real exposure
            AND NOT number of stop outs
            """)
    @Feature(FEATURE_RULE_ENGINE)
    @Owner(OWNER_NIKOLAI_KORIAGIN)
    @Tag(TEAM_CORE)
    @Tag(LAYER_API)
    @AllureId("")
    public void mirrorTradeTest4() throws SQLException {
        UserHelper.createUser(false, "");
        // create data for all simple abuse points
        Allure.step("Risky payment method");
        // next block in rule
        Allure.step("FTD bonus = 50%");
        Allure.step("Dummy trades");
        Allure.step("LN mid/high risk score");
        Allure.step("New account? Registered/FTD-d in the last week");
        Allure.step("Dormant account login");
        // next block in rule - Detailed trade check
        Allure.step("NOT Trading on news");
        Allure.step("NOT LARGE SPIKES IN REAL EXPOSURE");
        Allure.step("NOT NUMBER OF STOP OUTS");

        // Triggering the rule with withdrawal attempt
        PaymentHelper.createPayment(PAYMENT_TYPE_WITHDRAWAL, "", 100);

        // Checking message in kafka - MANUAL INVESTIGATION
        KafkaMessageConsumerHelper.consumeMessages("rule producer topic");
    }

    @Disabled
    @Test
    @DisplayName(
            """
            Hedge block flow
            IF user NOT in abuse registry
            AND not linked account
            AND More than 4 simple abuse point
            AND trading on news
            AND mirror trade check
            IS true
            """)
    @Feature(FEATURE_RULE_ENGINE)
    @Owner(OWNER_NIKOLAI_KORIAGIN)
    @Tag(TEAM_CORE)
    @Tag(LAYER_API)
    @AllureId("")
    public void mirrorTradeTest5() throws SQLException {
        UserHelper.createUser(false, "");
        // create data for all simple abuse points
        Allure.step("Risky payment method");
        // next block in rule
        Allure.step("FTD bonus = 50%");
        Allure.step("Dummy trades");
        Allure.step("LN mid/high risk score");
        Allure.step("New account? Registered/FTD-d in the last week");
        Allure.step("Dormant account login");
        // Next block in rule - Detailed trade check
        Allure.step("IS Trading on news");
        // Next block in rule - Mirror trade check
        Allure.step("Mirror trade check == true");

        // Triggering the rule with withdrawal attempt
        PaymentHelper.createPayment(PAYMENT_TYPE_WITHDRAWAL, "", 100);

        // Checking message in kafka - HEDGE BLOCK FLOW
        KafkaMessageConsumerHelper.consumeMessages("rule producer topic");
    }

    @Disabled
    @Test
    @DisplayName(
            """
            Hedge block flow
            IF user NOT in abuse registry
            AND not linked account
            AND More than 4 simple abuse point
            AND large spikes in real exposure
            AND mirror trade check
            IS true
            """)
    @Feature(FEATURE_RULE_ENGINE)
    @Owner(OWNER_NIKOLAI_KORIAGIN)
    @Tag(TEAM_CORE)
    @Tag(LAYER_API)
    @AllureId("")
    public void mirrorTradeTest6() throws SQLException {
        UserHelper.createUser(false, "");
        // create data for all simple abuse points
        Allure.step("Risky payment method");
        // next block in rule
        Allure.step("FTD bonus = 50%");
        Allure.step("Dummy trades");
        Allure.step("LN mid/high risk score");
        Allure.step("New account? Registered/FTD-d in the last week");
        Allure.step("Dormant account login");
        // Next block in rule - Detailed trade check
        Allure.step("IS Large spikes in real exposure");
        // Next block in rule - Mirror trade check
        Allure.step("Mirror trade check == true");

        // Triggering the rule with withdrawal attempt
        PaymentHelper.createPayment(PAYMENT_TYPE_WITHDRAWAL, "", 100);

        // Checking message in kafka - HEDGE BLOCK FLOW
        KafkaMessageConsumerHelper.consumeMessages("rule producer topic");
    }

    @Disabled
    @Test
    @DisplayName(
            """
            Hedge block flow
            IF user NOT in abuse registry
            AND not linked account
            AND More than 4 simple abuse point
            AND number of stop outs
            AND mirror trade check
            IS true
            """)
    @Feature(FEATURE_RULE_ENGINE)
    @Owner(OWNER_NIKOLAI_KORIAGIN)
    @Tag(TEAM_CORE)
    @Tag(LAYER_API)
    @AllureId("")
    public void mirrorTradeTest7() throws SQLException {
        UserHelper.createUser(false, "");
        // create data for all simple abuse points
        Allure.step("Risky payment method");
        // next block in rule
        Allure.step("FTD bonus = 50%");
        Allure.step("Dummy trades");
        Allure.step("LN mid/high risk score");
        Allure.step("New account? Registered/FTD-d in the last week");
        Allure.step("Dormant account login");
        // Next block in rule - Detailed trade check
        Allure.step("IS Number of stop outs");
        // Next block in rule - Mirror trade check
        Allure.step("Mirror trade check == true");

        // Triggering the rule with withdrawal attempt
        PaymentHelper.createPayment(PAYMENT_TYPE_WITHDRAWAL, "", 100);

        // Checking message in kafka - HEDGE BLOCK FLOW
        KafkaMessageConsumerHelper.consumeMessages("rule producer topic");
    }

    @Disabled
    @Test
    @DisplayName(
            """
            Block withdrawal + manual investigation~
            IF user NOT in abuse registry
            AND not linked account
            AND More than 4 simple abuse point
            AND trading on news
            AND mirror trade check
            IS false
            """)
    @Feature(FEATURE_RULE_ENGINE)
    @Owner(OWNER_NIKOLAI_KORIAGIN)
    @Tag(TEAM_CORE)
    @Tag(LAYER_API)
    @AllureId("")
    public void mirrorTradeTest8() throws SQLException {
        UserHelper.createUser(false, "");
        // create data for all simple abuse points
        Allure.step("Risky payment method");
        // next block in rule
        Allure.step("FTD bonus = 50%");
        Allure.step("Dummy trades");
        Allure.step("LN mid/high risk score");
        Allure.step("New account? Registered/FTD-d in the last week");
        Allure.step("Dormant account login");
        // Next block in rule - Detailed trade check
        Allure.step("IS Trading on news");
        // Next block in rule - Mirror trade check
        Allure.step("Mirror trade check == false");

        // Triggering the rule with withdrawal attempt
        PaymentHelper.createPayment(PAYMENT_TYPE_WITHDRAWAL, "", 100);

        // Checking message in kafka - Block withdrawal + manual investigation
        KafkaMessageConsumerHelper.consumeMessages("rule producer topic");
    }

    @Disabled
    @Test
    @DisplayName(
            """
            Block withdrawal + manual investigation
            IF user NOT in abuse registry
            AND not linked account
            AND More than 4 simple abuse point
            AND Large spikes in real exposure
            AND mirror trade check
            IS false
            """)
    @Feature(FEATURE_RULE_ENGINE)
    @Owner(OWNER_NIKOLAI_KORIAGIN)
    @Tag(TEAM_CORE)
    @Tag(LAYER_API)
    @AllureId("")
    public void mirrorTradeTest9() throws SQLException {
        UserHelper.createUser(false, "");
        // create data for all simple abuse points
        Allure.step("Risky payment method");
        // next block in rule
        Allure.step("FTD bonus = 50%");
        Allure.step("Dummy trades");
        Allure.step("LN mid/high risk score");
        Allure.step("New account? Registered/FTD-d in the last week");
        Allure.step("Dormant account login");
        // Next block in rule - Detailed trade check
        Allure.step("IS Large spikes in real exposure");
        // Next block in rule - Mirror trade check
        Allure.step("Mirror trade check == false");

        // Triggering the rule with withdrawal attempt
        PaymentHelper.createPayment(PAYMENT_TYPE_WITHDRAWAL, "", 100);

        // Checking message in kafka - Block withdrawal + manual investigation
        KafkaMessageConsumerHelper.consumeMessages("rule producer topic");
    }

    @Disabled
    @Test
    @DisplayName(
            """
            Block withdrawal + manual investigation
            IF user NOT in abuse registry
            AND not linked account
            AND More than 4 simple abuse point
            AND number of stop outs
            AND mirror trade check
            IS false
            """)
    @Feature(FEATURE_RULE_ENGINE)
    @Owner(OWNER_NIKOLAI_KORIAGIN)
    @Tag(TEAM_CORE)
    @Tag(LAYER_API)
    @AllureId("")
    public void mirrorTradeTest10() throws SQLException {
        UserHelper.createUser(false, "");
        // create data for all simple abuse points
        Allure.step("Risky payment method");
        // next block in rule
        Allure.step("FTD bonus = 50%");
        Allure.step("Dummy trades");
        Allure.step("LN mid/high risk score");
        Allure.step("New account? Registered/FTD-d in the last week");
        Allure.step("Dormant account login");
        // Next block in rule - Detailed trade check
        Allure.step("IS Number of stop outs");
        // Next block in rule - Mirror trade check
        Allure.step("Mirror trade check == false");

        // Triggering the rule with withdrawal attempt
        PaymentHelper.createPayment(PAYMENT_TYPE_WITHDRAWAL, "", 100);

        // Checking message in kafka - Block withdrawal + manual investigation
        KafkaMessageConsumerHelper.consumeMessages("rule producer topic");
    }
}
