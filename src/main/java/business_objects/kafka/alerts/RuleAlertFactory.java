package business_objects.kafka.alerts;

import io.qameta.allure.Step;

import java.time.Instant;

import static utils.Utils.getRandomUuidString;

public class RuleAlertFactory {
    @Step("Generate rule alert for client with ucid '{ucid}'")
    public static RuleAlert generateRuleAlertByUcid(String ucid) {
        RuleAlert alert = new RuleAlert();
        alert.alertId = getRandomUuidString();
        alert.timestamp = Instant.now().toString();
        alert.ucid = ucid;
        alert.rule = new RuleAlert.Rule();
        alert.rule.code = 11;
        alert.rule.ver = "01";
        alert.rule.name = "Registration";
        alert.rule.trigger = "clientRegistration";
        alert.rule.fraudType = "MARKET_MANIPULATION";
        alert.rule.attributes = new RuleAlert.Rule.Attribute();
        alert.rule.attributes.stepName = "Linked market manipulator abuser";
        return alert;
    }
}
