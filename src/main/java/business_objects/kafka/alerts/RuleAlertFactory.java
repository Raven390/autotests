package business_objects.kafka.alerts;

import helpers.data.ClientHelper;
import io.qameta.allure.Step;

import java.time.Instant;

import static utils.Utils.*;

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
    public static RuleAlert generateWithdrawalNotificationAlert(ClientHelper client) {
        RuleAlert alert = new RuleAlert();
        alert.alertId = getRandomUuidString();
        alert.timestamp = Instant.now().toString();
        alert.ucid = client.getUcid();
        alert.rule = new RuleAlert.Rule();
        alert.rule.ver = "0.1";
        alert.rule.name = "Withdrawal Review";
        alert.rule.trigger = "Withdrawal";
        alert.rule.fraudType = "POTENTIAL_ABUSE";
        alert.rule.attributes = new RuleAlert.Rule.Attribute();
        alert.rule.attributes.withdrawalId = getRandomIntPositive().toString();
        alert.rule.attributes.amount = "123.45";
        alert.rule.attributes.currency = "EUR";
        alert.rule.attributes.paymentType = "CRYPTO";
        alert.rule.attributes.check = "Big_Amount";
        alert.rule.attributes.orderId = "AU603771220250201005755";
        alert.rule.attributes.paymentChannel = "Cryptocurrency-ETH";
        alert.rule.attributes.brand = client.getBrand();
        alert.rule.attributes.account = client.getTradingAccount().toString();
        alert.rule.attributes.platform = "MT4";
        alert.rule.attributes.createTime = getCurrentTimestampDbFormat().replace(" ", "T") + "+03:00";
        alert.rule.attributes.regulator = client.getRegulator();
        alert.rule.attributes.date = getCurrentTimestampDbFormat().replace(" ", "T") + "+03:00";
        return alert;
    }
}
