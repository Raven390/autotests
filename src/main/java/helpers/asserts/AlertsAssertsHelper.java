package helpers.asserts;

import static helpers.data.enums.AlertType.TRADING;
import static helpers.database.DbHelper.getObjectsFromDB;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static utils.Constants.*;

import business_objects.db.backoffice_db.alert.Alert;
import business_objects.db.backoffice_db.alert_fallback.AlertFallbackObject;
import business_objects.kafka.alerts.RuleAlertV2;
import helpers.data.DataHelper;
import helpers.database.DbName;
import io.qameta.allure.Step;
import java.util.List;
import java.util.logging.Logger;

public class AlertsAssertsHelper {

    public static void assertThatAlertNotFailed(String ucid, String rulename) throws Exception {
        int failedAlerts = 0;
        int parsedAlerts = 0;
        int count = 0;

        do {
            List<AlertFallbackObject> dbAlertsFailed = getObjectsFromDB(
                    DbName.POSTGRES,
                    BO_ALERT_FALLBACK_TABLE_NAME,
                    "alert_raw LIKE '%" + ucid + "%' and alert_raw LIKE '%" + rulename + "%'",
                    AlertFallbackObject.class);
            failedAlerts = dbAlertsFailed.size();

            List<Alert> dbAlertsParsed = getObjectsFromDB(
                    DbName.POSTGRES, BO_ALERT_TABLE_NAME, String.format("client_ucid = '%s'", ucid), Alert.class);
            parsedAlerts = dbAlertsParsed.size();
            Thread.sleep(5000);
            Logger.getLogger("assertThatAlertNotFailed")
                    .info("failedAlerts: " + failedAlerts + "\nparsedAlerts: " + parsedAlerts + " count: " + count
                            + "");
        } while ((parsedAlerts == 0 && failedAlerts == 0) && count++ < 50);

        assertNotEquals(0, parsedAlerts);
        assertEquals(0, failedAlerts);
    }

    @Step("Assert risk withdrawal alert")
    public static void assertRiskWithdrawalAlert(DataHelper data, List<RuleAlertV2> alerts) {
        assertThat("Alerts list should contain exactly 1 item", alerts.size(), is(1));
        assertThat(
                "Rule version should not be null", alerts.getFirst().getRule().getVer(), is(notNullValue()));
        assertThat(
                "Rule name should be 'Withdrawal Review'",
                alerts.getFirst().getRule().getName(),
                is("Withdrawal Review"));
        assertThat(
                "Attributes.withdrawalId should match crmWithdrawalEventV2.withdrawalId",
                alerts.getFirst().getAttributes().getWithdrawalId(),
                is(data.crmWithdrawalEventV2.getWithdrawalId()));
        assertThat(
                "Attributes.paymentChannel should match crmWithdrawalEventV2.paymentChannelName",
                alerts.getFirst().getAttributes().getPaymentChannel(),
                is(data.crmWithdrawalEventV2.getPaymentChannelName()));
        assertThat(
                "Attributes.platform should match crmWithdrawalEventV2.accountType",
                alerts.getFirst().getAttributes().getPlatform(),
                is(data.crmWithdrawalEventV2.getAccountType()));
        assertThat("AmountUsd should be a Double", alerts.getFirst().getAmountUsd(), is(instanceOf(Double.class)));
        assertThat("Amount should be a Double", alerts.getFirst().getAmount(), is(instanceOf(Double.class)));
        assertThat("PaymentMethod should be 'CRYPTO'", alerts.getFirst().getPaymentMethod(), is("CRYPTO"));
        assertThat(
                "AlertId should match crmWithdrawalEventV2.id",
                alerts.getFirst().getAlertId(),
                is(data.crmWithdrawalEventV2.getId()));
        assertThat("MerchantOrderId should not be null", alerts.getFirst().getMerchantOrderId(), is(notNullValue()));
        assertThat(
                "Reason should be 'Potential fraud detected'",
                alerts.getFirst().getReason(),
                is("Potential fraud detected"));
        assertThat(
                "TriggerCreatedTime should not be null", alerts.getFirst().getTriggerCreatedTime(), is(notNullValue()));
        assertThat("FraudType should be 'POTENTIAL_ABUSE'", alerts.getFirst().getFraudType(), is("POTENTIAL_ABUSE"));
        assertThat("Currency should not be null", alerts.getFirst().getCurrency(), is(notNullValue()));
        assertThat("Account should not be null", alerts.getFirst().getAccount(), is(notNullValue()));
        assertThat("Trigger should be 'Withdrawal'", alerts.getFirst().getTrigger(), is("Withdrawal"));
        assertThat("Timestamp should not be null", alerts.getFirst().getTimestamp(), is(notNullValue()));
        assertThat("Ucid should match client's ucid", alerts.getFirst().getUcid(), is(data.clientHelper.getUcid()));
        assertThat("Type should be 'TRADING'", alerts.getFirst().getType(), is(TRADING.getDisplayName()));
    }

    @Step("Assert risk transfer alert")
    public static void assertRiskTransferToWaAlert(DataHelper data, List<RuleAlertV2> alerts) {
        assertThat("Alerts list should contain exactly 1 item", alerts.size(), is(1));
        assertThat(
                "Rule version should not be null", alerts.getFirst().getRule().getVer(), is(notNullValue()));
        assertThat(
                "Rule name should be 'Withdrawal Review'",
                alerts.getFirst().getRule().getName(),
                is("Withdrawal Review"));
        assertThat(
                "Attributes.withdrawalId should match transferToWaEvent.transferId",
                alerts.getFirst().getAttributes().getWithdrawalId(),
                is(data.transferToWaEvent.getTransferId()));
        assertThat(
                "Attributes.paymentChannel should be 'Wallet-Transfer'",
                alerts.getFirst().getAttributes().getPaymentChannel(),
                is("Wallet-Transfer"));
        assertThat(
                "Attributes.platform should match transferToWaEvent.accountType",
                alerts.getFirst().getAttributes().getPlatform(),
                is(data.transferToWaEvent.getAccountType()));
        assertThat("AmountUsd should be a Double", alerts.getFirst().getAmountUsd(), is(instanceOf(Double.class)));
        assertThat("Amount should be a Double", alerts.getFirst().getAmount(), is(instanceOf(Double.class)));
        assertThat("PaymentMethod should be 'CRYPTO'", alerts.getFirst().getPaymentMethod(), is("CRYPTO"));
        assertThat(
                "AlertId should match transferToWaEvent.id",
                alerts.getFirst().getAlertId(),
                is(data.transferToWaEvent.getId().toString()));
        assertThat("MerchantOrderId should not be null", alerts.getFirst().getMerchantOrderId(), is(notNullValue()));
        assertThat(
                "Reason should be 'Potential fraud detected'",
                alerts.getFirst().getReason(),
                is("Potential fraud detected"));
        assertThat(
                "TriggerCreatedTime should not be null", alerts.getFirst().getTriggerCreatedTime(), is(notNullValue()));
        assertThat("FraudType should be 'POTENTIAL_ABUSE'", alerts.getFirst().getFraudType(), is("POTENTIAL_ABUSE"));
        assertThat("Currency should not be null", alerts.getFirst().getCurrency(), is(notNullValue()));
        assertThat("Account should not be null", alerts.getFirst().getAccount(), is(notNullValue()));
        assertThat("Trigger should be 'transferToWA'", alerts.getFirst().getTrigger(), is("transferToWA"));
        assertThat("Timestamp should not be null", alerts.getFirst().getTimestamp(), is(notNullValue()));
        assertThat("Ucid should match client's ucid", alerts.getFirst().getUcid(), is(data.clientHelper.getUcid()));
        assertThat("Type should be 'TRADING'", alerts.getFirst().getType(), is(TRADING.getDisplayName()));
    }

    public static void checkTradingAlert(
            DataHelper data,
            List<RuleAlertV2> alerts,
            String expectedReasonPrefix,
            String expectedFraudType,
            String expectedTrigger,
            String alertRuleName)
            throws Exception {
        assertThat("Alerts list should contain exactly 1 item", alerts.size(), is(1));
        RuleAlertV2 alert = alerts.getFirst();
        assertThat("Verify alert ", alert.getUcid(), is(data.getClientHelper().getUcid()));
        assertThat(
                "Verify alert ", alert.getServerId(), is(data.getClientHelper().getServerId()));
        assertThat(
                "Verify alert ", alert.getAccount(), is(data.getClientHelper().getTradingAccount()));
        assertThat("Verify alert ", alert.getType(), is(TRADING.getDisplayName()));
        assertThat("Verify alert reason", alert.getReason(), startsWith(expectedReasonPrefix));
        assertThat("Verify alert triggerCreatedTime", alert.getTriggerCreatedTime(), is(notNullValue()));
        assertThat("Verify alert fraudType", alert.getFraudType(), is(expectedFraudType));
        assertThat("Verify alert trigger", alert.getTrigger(), is(expectedTrigger));

        assertThatAlertNotFailed(alert.getUcid(), alertRuleName);
    }

    @Step("Assert Unlimited leverage alert")
    public static void assertUnlimitedLeverageAlert(DataHelper data, List<RuleAlertV2> alerts) {
        assertThat("Alerts list should contain exactly 1 item", alerts.size(), is(1));
        RuleAlertV2 alert = alerts.getFirst();

        assertThat("assert alert", alert.getAlertId(), is(data.tradeEvent.id));
        assertThat("assert alert", alert.getTimestamp(), is(notNullValue()));
        assertThat("assert alert", alert.getType(), is(TRADING.getDisplayName()));
        assertThat("assert alert", alert.getTriggerCreatedTime(), is(data.tradeEvent.eventDate));
        assertThat("assert alert", alert.getUcid(), is(data.clientHelper.getUcid()));
        assertThat("assert alert", alert.getTrigger(), is("Close Trade"));
        assertThat("assert alert", alert.getReason(), is("News trading pattern with Unlimited Leverage"));
        assertThat("assert alert", alert.getAccount().longValue(), is(data.tradeEvent.tradingAccount));
        assertThat("assert alert", alert.getServerId(), is(data.tradeEvent.serverId));
        assertThat("assert alert", alert.getFraudType(), is("NEWS_TRADER"));
        assertThat("assert alert", alert.getRule().getVer(), is(notNullValue()));
        assertThat("assert alert", alert.getRule().getName(), is("News Trading"));
        assertThat("assert alert", alert.getAttributes().getTicketId(), is(String.valueOf(data.tradeEvent.tradeId)));
    }

    @Step("Assert Unlimited leverage alert")
    public static void assertCustomRuleAlert(DataHelper data, List<RuleAlertV2> alerts) {
        assertThat("Alerts list should contain exactly 1 item", alerts.size(), is(1));
        RuleAlertV2 alert = alerts.getFirst();

        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        assertThat(
                "Verify alert",
                alert.getReason(),
                is(
                        "Client repeatedly opens opposite-direction trades using known hedging EA comments ('vef', 'My Order')."));
        assertThat(
                "Verify alert",
                alert.getTimestamp(),
                matchesPattern("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(?:\\.\\d+)?(?:Z|[+-]\\d{2}:\\d{2})$"));
        assertThat("Verify alert", alert.getAlertId(), is(data.customEvent.getId()));
        assertThat(
                "Verify alert",
                alert.getTriggerCreatedTime(),
                matchesPattern("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(?:\\.\\d+)?(?:Z|[+-]\\d{2}:\\d{2})$"));
        assertThat("Verify alert", alert.getFraudType(), is(data.customEvent.getFraudType()));
        assertThat("Verify alert", alert.getTrigger(), is(data.customEvent.getType()));
        assertThat("Verify alert", alert.getUcid(), is(data.clientHelper.getUcid()));
        assertThat("Verify alert", alert.getType(), is(TRADING.getDisplayName()));
        assertThat("Verify alert", alert.getRule().getName(), is(data.customEvent.getSource()));
        assertThat("Verify alert", alert.getRule().getVer(), notNullValue());
        assertThat("Verify alert", alert.getAttributes().getDetails(), is(""));
    }
}
