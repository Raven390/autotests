package tests.ruleEngineServiceTests;

import businessObjects.api.mitigationService.GetRestrictionResponseBody;
import businessObjects.kafka.mtDbEvents.closeTrade.CloseTradeMtDbEventMt4;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.data.rules.mirrorTradingRule.MirrorTradingRuleData;
import helpers.kafka.KafkaHelper;
import helpers.kafka.MatchResultWithMessage;
import io.qameta.allure.*;
import okhttp3.Response;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static businessObjects.api.mitigationService.MitigationServiceRequest.getRestrictionsByUcid;
import static businessObjects.kafka.mtDbEvents.closeTrade.CloseTradeMtDbEventFactory.generateCloseTradeMtDbEventMt4;
import static businessObjects.kafka.mtDbEvents.closeTrade.CloseTradeMtDbEventFactory.generateCloseTradeMtDbEventMt4ByTradingAccount;
import static helpers.data.rules.mirrorTradingRule.MirrorTradingRuleDataFactory.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.Constants.*;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_MIRROR_TRADING_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_SERVICE)
@Tag(TAG_MANUAL)
@Disabled
@Muted
public class MirrorTradeRuleTest {

    KafkaHelper kafka = new KafkaHelper();
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Mirror trading rule exit Event_End_1_1")
    @AllureId("179")
    public void mirrorTradeRuleExitEventEnd1Test() {
        // TODO finish
        // Prepare Data
        getMirrorTradingRuleExitEventEnd1_1Data();
        // Trigger rule
        CloseTradeMtDbEventMt4 closeTradeMtDbEventMt4 = generateCloseTradeMtDbEventMt4();
    }

    @Test
    @DisplayName("Mirror trading rule exit Event_End_1_2")
    @AllureId("220")
    public void mirrorTradeRuleExitEventEnd1_2Test() {
        // TODO finish
        // Prepare Data
        getMirrorTradingRuleExitEventEnd1_2Data();
        // Trigger rule
        CloseTradeMtDbEventMt4 closeTradeMtDbEventMt4 = generateCloseTradeMtDbEventMt4();
    }

    @Test
    @DisplayName("Mirror trading rule exit Event_End_7_1")
    @AllureId("178")
    public void mirrorTradeRuleExitEventEnd7_1Test() {
        // TODO finish
        // Prepare Data
        getMirrorTradingRuleExitEventEnd7_1Data();
        // Trigger rule
        CloseTradeMtDbEventMt4 closeTradeMtDbEventMt4 = generateCloseTradeMtDbEventMt4();
    }

    @Test
    @DisplayName("Mirror trading rule exit Event_End_7_2")
    @AllureId("177")
    public void mirrorTradeRuleExitEventEnd7_2Test() {
        // TODO finish
        // Prepare Data
        getMirrorTradingRuleExitEventEnd7_2Data();
        // Trigger rule
        CloseTradeMtDbEventMt4 closeTradeMtDbEventMt4 = generateCloseTradeMtDbEventMt4();
    }

    @Test
    @DisplayName("Mirror trading rule exit Event_End_7_3")
    @AllureId("176")
    public void mirrorTradeRuleExitEventEnd7_3Test() {
        // TODO finish
        // Prepare Data
        getMirrorTradingRuleExitEventEnd7_3Data();
        // Trigger rule
        CloseTradeMtDbEventMt4 closeTradeMtDbEventMt4 = generateCloseTradeMtDbEventMt4();
    }

    @Test
    @DisplayName("Mirror trading rule exit Event_End_7_4")
    @AllureId("175")
    public void mirrorTradeRuleExitEventEnd7_4Test() {
        // TODO finish
        // Prepare Data
        getMirrorTradingRuleExitEventEnd7_4Data();
        // Trigger rule
        CloseTradeMtDbEventMt4 closeTradeMtDbEventMt4 = generateCloseTradeMtDbEventMt4();
    }

    @Test
    @DisplayName("Mirror trading rule exit Event_End_7_5")
    @AllureId("174")
    public void mirrorTradeRuleExitEventEnd7_5Test() {
        // TODO finish
        // Prepare Data
        getMirrorTradingRuleExitEventEnd7_5Data();
        // Trigger rule
        CloseTradeMtDbEventMt4 closeTradeMtDbEventMt4 = generateCloseTradeMtDbEventMt4();
    }

    @Test
    @DisplayName("Mirror trading rule exit Event_End_6_1")
    @AllureId("173")
    public void mirrorTradeRuleExitEventEnd6Test() {
        // TODO finish
        // Prepare Data
        getMirrorTradingRuleExitEventEnd6_1Data();
        // Trigger rule
        CloseTradeMtDbEventMt4 closeTradeMtDbEventMt4 = generateCloseTradeMtDbEventMt4();
    }

    @Test
    @DisplayName("Mirror trading rule exit Event_End_5_1")
    @AllureId("181")
    public void mirrorTradeRuleExitEventEnd5_1Test() {
        // TODO finish
        // Prepare Data
        getMirrorTradingRuleExitEventEnd5_1Data();
        // Trigger rule
        CloseTradeMtDbEventMt4 closeTradeMtDbEventMt4 = generateCloseTradeMtDbEventMt4();
    }

    @Test
    @DisplayName("Mirror trading rule exit Event_End_5_2")
    @AllureId("182")
    public void mirrorTradeRuleExitEventEnd5_2Test() {
        // TODO finish
        // Prepare Data
        getMirrorTradingRuleExitEventEnd5_2Data();
        // Trigger rule
        CloseTradeMtDbEventMt4 closeTradeMtDbEventMt4 = generateCloseTradeMtDbEventMt4();
    }

    @Test
    @DisplayName("Mirror trading rule exit Event_End_4_1")
    @AllureId("183")
    public void mirrorTradeRuleExitEventEnd4Test() {
        // TODO finish
        // Prepare Data
        getMirrorTradingRuleExitEventEnd4_1Data();
        // Trigger rule
        CloseTradeMtDbEventMt4 closeTradeMtDbEventMt4 = generateCloseTradeMtDbEventMt4();
    }

    @Test
    @DisplayName("Mirror trading rule exit Event_End_4_2")
    @AllureId("184")
    public void mirrorTradeRuleExitEventEnd4_2Test() {
        // TODO finish
        // Prepare Data
        getMirrorTradingRuleExitEventEnd4_2Data();
        // Trigger rule
        CloseTradeMtDbEventMt4 closeTradeMtDbEventMt4 = generateCloseTradeMtDbEventMt4();
    }

    @Test
    @DisplayName("Mirror trading rule exit Event_End_3_1")
    @AllureId("185")
    public void mirrorTradeRuleExitEventEnd3_1Test() {
        // TODO finish
        // Prepare Data
        getMirrorTradingRuleExitEventEnd3_1Data();
        // Trigger rule
        CloseTradeMtDbEventMt4 closeTradeMtDbEventMt4 = generateCloseTradeMtDbEventMt4();
    }

    @Test
    @DisplayName("Mirror trading rule exit Event_End_3_2")
    @AllureId("186")
    public void mirrorTradeRuleExitEventEnd3_2Test() throws IOException {
        // Prepare Data
        MirrorTradingRuleData ruleData = getMirrorTradingRuleExitEventEnd3_2Data();
        // Trigger rule and wait for restriction and alert
        CloseTradeMtDbEventMt4 closeTradeMtDbEventMt4 = generateCloseTradeMtDbEventMt4ByTradingAccount(ruleData.clientHelper.getTradingAccount());
        // Check alert
        MatchResultWithMessage areAllParamsPresentInMessages = kafka.areAllParamsPresentInMessages(
                KAFKA_TOPIC_CRM_EVENTS, ruleData.clientHelper.getUcid());
        // Check restrictions
        Response response = getRestrictionsByUcid(ruleData.clientHelper.getUcid());
        GetRestrictionResponseBody[] restrictionBody = objectMapper.readValue(response.body().string(), GetRestrictionResponseBody[].class);
        assertEquals("MIRRORTRADE",restrictionBody[0].code);
        assertEquals(ruleData.clientHelper.getTradingAccount(),restrictionBody[0].accountId);
    }

    @Test
    @DisplayName("Mirror trading rule exit Event_End_2_1")
    @AllureId("187")
    public void mirrorTradeRuleExitEventEnd2Test() throws IOException {
        // Prepare Data
        MirrorTradingRuleData ruleData = getMirrorTradingRuleExitEventEnd2_1Data();
        // Trigger rule and exit without alert
        generateCloseTradeMtDbEventMt4ByTradingAccount(ruleData.clientHelper.getTradingAccount());
    }
}
