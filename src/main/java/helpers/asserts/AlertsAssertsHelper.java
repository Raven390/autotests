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
        assertThat("Verify alert", alerts.getFirst().timestamp, matchesPattern("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(?:\\.\\d+)?(?:Z|[+-]\\d{2}:\\d{2})$"));
        assertThat("Verify alert", alerts.getFirst().alertId, is(withdrawalEvent.id));
        assertThat("Verify alert", alerts.getFirst().type, is("TRADING"));
        assertThat("Verify alert", alerts.getFirst().ucid, is(client.getUcid()));
        assertThat("Verify alert", alerts.getFirst().triggerCreatedTime, is(withdrawalEvent.eventDate));

        assertThat("Verify alert", alerts.getFirst().rule.name, is("Withdrawal Review"));
        assertThat("Verify alert", alerts.getFirst().rule.fraudType, is("POTENTIAL_ABUSE"));
        assertThat("Verify alert", alerts.getFirst().rule.trigger, is("Withdrawal"));
        assertThat("Verify alert", alerts.getFirst().rule.ver, notNullValue());

        assertThat("Verify alert", alerts.getFirst().rule.attributes.platform, is(withdrawalEvent.platform));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.currency, is(withdrawalEvent.withdrawalCurrency));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.createTime, is(withdrawalEvent.withdrawalApplicationTime));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.paymentType, is(withdrawalEvent.paymentMethodCode));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.check, is(withdrawalEvent.checkName));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.account, is(String.valueOf(withdrawalEvent.mt4Account)));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.paymentChannel, is(withdrawalEvent.paymentChannelName));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.amount, is("1"));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.date, is(withdrawalEvent.eventDate));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.withdrawalId, is(String.valueOf(withdrawalEvent.withdrawalId)));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.orderId, is(withdrawalEvent.merchantOrderId));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.regulator, is(withdrawalEvent.regulator));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.brand, is(withdrawalEvent.brand));
        return true;
    }
}
