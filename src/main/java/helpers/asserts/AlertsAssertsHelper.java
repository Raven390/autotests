package helpers.asserts;

import business_objects.db.backoffice_db.alert.Alert;
import business_objects.kafka.alerts.RuleAlert;
import business_objects.kafka.crm_events.CrmWithdrawalEvent;
import helpers.data.ClientHelper;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;

public class AlertsAssertsHelper {

    public static boolean assertAlertsWithdrawalNotificationRule(List<RuleAlert> alerts, List<Alert> dbAlerts,
            CrmWithdrawalEvent withdrawalEvent, ClientHelper client) {
        assertThat("Alert timestamp uses ISO-8601 format", alerts.getFirst().timestamp, matchesPattern("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(?:\\.\\d+)?(?:Z|[+-]\\d{2}:\\d{2})$"));
        assertThat("Alert ID equals event ID", alerts.getFirst().alertId, is(withdrawalEvent.getId()));
        assertThat("Alert type is TRADING", alerts.getFirst().type, is("TRADING"));
        assertThat("Alert UCID equals client UCID", alerts.getFirst().ucid, is(client.getUcid()));
        assertThat("Alert triggerCreatedTime equals event date", alerts.getFirst().triggerCreatedTime, is(withdrawalEvent.getEventDate()));

        assertThat("Rule name is 'Withdrawal Review'", alerts.getFirst().rule.name, is("Withdrawal Review"));
        assertThat("Rule fraudType is 'POTENTIAL_ABUSE'", alerts.getFirst().rule.fraudType, is("POTENTIAL_ABUSE"));
        assertThat("Rule trigger is 'Withdrawal'", alerts.getFirst().rule.trigger, is("Withdrawal"));
        assertThat("Rule version is present", alerts.getFirst().rule.ver, notNullValue());

        assertThat("Attribute platform equals event platform", alerts.getFirst().rule.attributes.platform, is(withdrawalEvent.getAccountType()));
        assertThat("Attribute currency equals withdrawal currency", alerts.getFirst().rule.attributes.currency, is(withdrawalEvent.getWithdrawalCurrency()));
        assertThat("Attribute createTime equals withdrawal application time", alerts.getFirst().rule.attributes.createTime, containsString(withdrawalEvent.getWithdrawalApplicationTime()));
        assertThat("Attribute paymentType equals payment method code", alerts.getFirst().rule.attributes.paymentType, is(withdrawalEvent.getPaymentMethodCode()));
        assertThat("Attribute check equals check name", alerts.getFirst().rule.attributes.check, is(withdrawalEvent.getCheckName()));
        assertThat("Attribute account equals MT4 account", alerts.getFirst().rule.attributes.account, is(String.valueOf(withdrawalEvent.getMt4Account())));
        assertThat("Attribute paymentChannel equals payment channel name", alerts.getFirst().rule.attributes.paymentChannel, is(withdrawalEvent.getPaymentChannelName()));
        assertThat("Attribute amount equals '1'", alerts.getFirst().rule.attributes.amount, is("1"));
        assertThat("Attribute date equals event date", alerts.getFirst().rule.attributes.date, is(withdrawalEvent.getEventDate()));
        assertThat("Attribute withdrawalId equals withdrawal ID", alerts.getFirst().rule.attributes.withdrawalId, is(String.valueOf(withdrawalEvent.getWithdrawalId())));
        assertThat("Attribute orderId equals merchant order ID", alerts.getFirst().rule.attributes.orderId, is(withdrawalEvent.getMerchantOrderId()));
        assertThat("Attribute regulator equals event regulator", alerts.getFirst().rule.attributes.regulator, is(withdrawalEvent.getRegulator()));
        assertThat("Attribute brand equals event brand", alerts.getFirst().rule.attributes.brand, is(withdrawalEvent.getBrand()));
        return true;
    }
}
