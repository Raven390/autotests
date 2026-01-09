package tests.rule_engine_service_tests.rules.payment.router_rule_crm_payment.subrules;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static helpers.api.RestrictionHelper.setRestrictionAPIGeneral;
import static helpers.data.DataDeleteHelper.deleteData;
import static helpers.data.DataSetupHelper.setupData;
import static helpers.data.enums.Restriction.MANUAL_WITHDRAWAL_REVIEW;
import static helpers.data.rules.payments.router_rule_crm_payment.WithdrawalNotificationDataFactory.setupWithdrawalNotificationRuleData;
import static helpers.database.DbHelper.*;
import static utils.Constants.*;

import helpers.data.DataHelper;
import io.qameta.allure.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.*;
import tests.TestBaseRule;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_WITHDRAWAL_NOTIFICATION_IN_ROUTER_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class WithdrawalNotificationRuleTests extends TestBaseRule {

    private static Map<String, DataHelper> dataMap = new HashMap<>();

    @BeforeAll
    static void setup() throws IOException {
        startSshTunnel();
        enableCRMEmulator();
        dataMap = setupWithdrawalNotificationRuleData();
    }

    @AfterAll
    static void teardown() throws Exception {
        deleteData(dataMap);
    }

    @Test
    @AllureId("1769")
    @DisplayName(
            "Withdrawal notification check in Router rule. Exit without alert if check name is empty and no restriction. ElementId: Event_end_2")
    void withdrawalNotificationRule1Test() throws Exception {
        DataHelper data = dataMap.get("1");
        setupData(data);

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId("Event_end_2", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr_payment");
        checkElementId("Activity_1kf15qu", data.crmWithdrawalEvent.getId(), "router_rule_crm_payment");
    }

    @Test
    @AllureId("1790")
    @DisplayName(
            "Withdrawal notification check in Router rule. Exit without alert if check name is null and no restriction. ElementId: Event_end_2")
    void withdrawalNotificationRule2Test() throws Exception {
        DataHelper data = dataMap.get("2");
        setupData(data);

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId("Event_end_2", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr_payment");
        checkElementId("Activity_1kf15qu", data.crmWithdrawalEvent.getId(), "router_rule_crm_payment");
    }

    @Test
    @AllureId("1770")
    @DisplayName(
            "Withdrawal notification rule. Exit with alert if check name is not empty and WR restriction exists. ElementId: Event_1gdl5i3")
    void withdrawalNotificationRule3Test() throws Exception {
        DataHelper data = dataMap.get("3");
        setupData(data);

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId("Event_1gdl5i3", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr_payment");
        checkElementId("Activity_1kf15qu", data.crmWithdrawalEvent.getId(), "router_rule_crm_payment");
    }

    @Test
    @AllureId("1771")
    @DisplayName(
            "Withdrawal notification rule. Exit with alert if check name is not empty and WR restriction not exists. ElementId: Event_1gdl5i3")
    void withdrawalNotificationRule4Test() throws Exception {
        DataHelper data = dataMap.get("4");
        setupData(data);

        setRestrictionAPIGeneral(data.clientHelper.getUcid(), MANUAL_WITHDRAWAL_REVIEW.getCode());

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId("Event_1gdl5i3", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr_payment");
        checkElementId("Activity_1kf15qu", data.crmWithdrawalEvent.getId(), "router_rule_crm_payment");
    }
}
