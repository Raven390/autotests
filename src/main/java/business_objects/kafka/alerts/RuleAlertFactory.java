package business_objects.kafka.alerts;

import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalObject;
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

    @Step("Generate withdrawal notification alert")
    public static RuleAlert generateWithdrawalNotificationAlert(CrmTbWithdrawalObject withdrawal) {
        RuleAlert alert = new RuleAlert();
        alert.alertId = getRandomUuidString();
        alert.timestamp = Instant.now().toString();
        alert.ucid = withdrawal.ucid;
        alert.rule = new RuleAlert.Rule();
        alert.rule.ver = "0.1";
        alert.rule.name = "Withdrawal Review";
        alert.rule.trigger = "Withdrawal";
        alert.rule.fraudType = "POTENTIAL_ABUSE";
        alert.rule.attributes = new RuleAlert.Rule.Attribute();
        alert.rule.attributes.withdrawalId = withdrawal.transferId.toString();
        alert.rule.attributes.amount = withdrawal.amount.toString();
        alert.rule.attributes.currency = withdrawal.currency;
        alert.rule.attributes.paymentType = withdrawal.paymentType;
        alert.rule.attributes.check = "Big_Amount";
        return alert;
    }
}
