package helpers.asserts;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;

import business_objects.kafka.alerts.RuleAlertV2;
import helpers.data.DataHelper;
import io.qameta.allure.Step;
import java.util.List;

public class AlertsAssertsHelper {

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
        assertThat("Type should be 'TRADING'", alerts.getFirst().getType(), is("TRADING"));
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
        assertThat("Type should be 'TRADING'", alerts.getFirst().getType(), is("TRADING"));
    }
}
