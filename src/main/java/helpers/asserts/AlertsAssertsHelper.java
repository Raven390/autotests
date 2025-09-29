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
        assertThat("Verify alert", alerts.getFirst().alertId, is(withdrawalEvent.getId()));
        assertThat("Verify alert", alerts.getFirst().type, is("TRADING"));
        assertThat("Verify alert", alerts.getFirst().ucid, is(client.getUcid()));
        assertThat("Verify alert", alerts.getFirst().triggerCreatedTime, is(withdrawalEvent.getEventDate()));

        assertThat("Verify alert", alerts.getFirst().rule.name, is("Withdrawal Review"));
        assertThat("Verify alert", alerts.getFirst().rule.fraudType, is("POTENTIAL_ABUSE"));
        assertThat("Verify alert", alerts.getFirst().rule.trigger, is("Withdrawal"));
        assertThat("Verify alert", alerts.getFirst().rule.ver, notNullValue());

        assertThat("Verify alert", alerts.getFirst().rule.attributes.platform, is(withdrawalEvent.getPlatform()));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.currency, is(withdrawalEvent.getWithdrawalCurrency()));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.createTime, is(withdrawalEvent.getWithdrawalApplicationTime()));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.paymentType, is(withdrawalEvent.getPaymentMethodCode()));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.check, is(withdrawalEvent.getCheckName()));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.account, is(String.valueOf(withdrawalEvent.getMt4Account())));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.paymentChannel, is(withdrawalEvent.getPaymentChannelName()));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.amount, is("1"));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.date, is(withdrawalEvent.getEventDate()));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.withdrawalId, is(String.valueOf(withdrawalEvent.getWithdrawalId())));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.orderId, is(withdrawalEvent.getMerchantOrderId()));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.regulator, is(withdrawalEvent.getRegulator()));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.brand, is(withdrawalEvent.getBrand()));
        return true;
    }
}
