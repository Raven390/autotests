package business_objects.kafka.alerts;

import static utils.Utils.*;

import helpers.data.ClientHelper;
import helpers.data.enums.FraudType;
import io.qameta.allure.Step;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

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

    @Step("Generate rule alert for client with ucid '{ucid}' and trigger")
    public static RuleAlert generateRuleAlertByUcid(String ucid, String trigger) {
        RuleAlert alert = new RuleAlert();
        alert.alertId = getRandomUuidString();
        alert.timestamp = Instant.now().toString();
        alert.ucid = ucid;
        alert.rule = new RuleAlert.Rule();
        alert.rule.code = 11;
        alert.rule.ver = "01";
        alert.rule.name = "Registration";
        alert.rule.trigger = trigger;
        alert.rule.fraudType = "MARKET_MANIPULATION";
        alert.rule.attributes = new RuleAlert.Rule.Attribute();
        alert.rule.attributes.stepName = "Linked market manipulator abuser";
        return alert;
    }

    @Step("Generate rule alert for client with ucid '{ucid}'")
    public static RuleAlert generateRuleAlertByUcid(ClientHelper client) {
        RuleAlert alert = new RuleAlert();
        alert.alertId = getRandomUuidString();
        alert.timestamp = Instant.now().toString();
        alert.ucid = client.getUcid();
        alert.rule = new RuleAlert.Rule();
        alert.rule.code = 11;
        alert.rule.ver = "01";
        alert.rule.name = "Mirror Trading";
        alert.rule.trigger = "openTrade";
        alert.rule.fraudType = FraudType.HEDGING.getCode();
        alert.rule.attributes = new RuleAlert.Rule.Attribute();
        alert.rule.attributes.stepName = "Linked hedging abuser";
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

    @Step("Generate withdrawal notification alert with paymentId")
    public static RuleAlert generateWithdrawalNotificationAlertWithPaymentId(ClientHelper client, UUID paymentId) {
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
        alert.rule.attributes.paymentId = paymentId.toString();
        return alert;
    }

    @Step("Generate withdrawal notification alert")
    public static RuleAlert generatePgsWithdrawalNotificationAlert(ClientHelper client, UUID paymentId) {
        RuleAlert alert = new RuleAlert();
        alert.alertId = getRandomUuidString();
        alert.timestamp = Instant.now().toString();
        alert.ucid = client.getUcid();
        alert.type = "TRADING";
        alert.triggerCreatedTime = Instant.now().toString();
        alert.rule = new RuleAlert.Rule();
        alert.rule.ver = "0.1.8";
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
        alert.rule.attributes.paymentId = paymentId.toString();
        return alert;
    }

    @Step("Generate payment alert for client with ucid '{ucid}'")
    public static PaymentAlertMessage generatePaymentAlertByUcid(String ucid) {
        return new PaymentAlertMessage(
                UUID.randomUUID(),
                AlertMessageType.PAYMENT,
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                ucid,
                new PaymentAlertMessage.Rule(
                        "Payment Fraud Detection",
                        "MARKET_MANIPULATION",
                        "1.0",
                        "Payment Initiated",
                        Collections.emptyMap()),
                "123456",
                "server1",
                "CRYPTO",
                "1000.00",
                "USD",
                UUID.randomUUID().toString());
    }

    @Step("Generate payment alert for client with ucid '{ucid}' and trigger '{trigger}'")
    public static PaymentAlertMessageV2 generatePaymentAlertByUcidByTrigger(String ucid, String trigger) {
        BaseAlertMessageV2.Rule paymentRule = new BaseAlertMessageV2.Rule();
        paymentRule.name = "fraud_detection";
        paymentRule.ver = "1.0.0";
        return new PaymentAlertMessageV2(
                UUID.randomUUID(),
                AlertMessageType.PAYMENT,
                OffsetDateTime.now(),
                OffsetDateTime.now().minusMinutes(2),
                ucid,
                "POTENTIAL_ABUSE",
                trigger,
                "Suspicious payment",
                paymentRule,
                new HashMap<>(),
                "12345",
                "srv-45",
                "CRYPTO",
                "500.00",
                "500.00",
                "USD",
                "D987654321",
                getRandomUuidString());
    }
}
