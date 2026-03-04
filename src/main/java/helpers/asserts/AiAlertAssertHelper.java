package helpers.asserts;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Utils.writeLog;

import business_objects.kafka.ai_alerts.AiAlert;
import business_objects.kafka.ai_alerts.AiTradingAlert;
import business_objects.kafka.ai_alerts.AiWithdrawalAlert;
import helpers.data.DataHelper;
import io.qameta.allure.Step;
import java.util.List;

public class AiAlertAssertHelper {

    @Step("Verify alert")
    public static void assertAiAlert(
            DataHelper data, List<AiAlert> alerts, String rule, String fraudType, String reason) {
        assertThat("Alerts list should contain exactly 1 item", alerts.size(), is(1));
        AiAlert alert = alerts.getFirst();

        try {
            writeLog("[DEBUG_LOG] Alert Serialized: "
                    + new com.fasterxml.jackson.databind.ObjectMapper()
                            .writerWithDefaultPrettyPrinter()
                            .writeValueAsString(alert));
        } catch (Exception e) {
            e.printStackTrace();
        }

        writeLog(alert.toString());

        assertThat("Verify alert", alert.getId(), is(notNullValue()));
        assertThat("Verify alert", alert.getEventId(), is(notNullValue()));
        assertThat("Verify alert", alert.getProducedAtUtc(), is(notNullValue()));
        assertThat("Verify alert", alert.getAlertDate(), is(notNullValue()));
        assertThat("Verify alert", alert.getUcid(), is(data.getClientHelper().getUcid()));
        assertThat(
                "Verify alert",
                alert.getTradingAccount(),
                is(data.getClientHelper().getTradingAccount()));
        assertThat(
                "Verify alert", alert.getServerId(), is(data.getClientHelper().getServerId()));
        assertThat("Verify alert", alert.getRule(), is(rule));
        assertThat("Verify alert", alert.getFraudType(), is(fraudType));
        assertThat("Verify alert", alert.getReason(), is(reason));

        if (alert instanceof AiWithdrawalAlert withdrawalAlert) {
            assertThat("Verify alert", alert.getAlertType(), is("WITHDRAW_ALERT"));
            AiWithdrawalAlert.AlertText text = withdrawalAlert.getAlertText();
            assertThat("Verify alert", text.getAccount(), is(nullValue()));
            assertThat("Verify alert", text.getAmount(), is(nullValue()));
            assertThat("Verify alert", text.getAmountUsd(), is(nullValue()));
            assertThat("Verify alert", text.getBrand(), is(nullValue()));
            assertThat("Verify alert", text.getCheck(), is(nullValue()));
            assertThat("Verify alert", text.getCreateTime(), is(nullValue()));
            assertThat("Verify alert", text.getCurrency(), is(nullValue()));
            assertThat("Verify alert", text.getDate(), is(nullValue()));
            assertThat("Verify alert", text.getOrderId(), is(nullValue()));
            assertThat("Verify alert", text.getPaymentId(), is(nullValue()));
            assertThat("Verify alert", text.getPaymentType(), is(nullValue()));
            assertThat("Verify alert", text.getRateUsd(), is(nullValue()));
            assertThat("Verify alert", text.getRegulator(), is(nullValue()));

            assertThat(
                    "Verify alert",
                    text.getPlatform(),
                    is(data.getCrmWithdrawalEventV2().getPlatform()));
            assertThat(
                    "Verify alert",
                    text.getPaymentChannel(),
                    is(data.getCrmWithdrawalEventV2().getPaymentChannelName()));
            assertThat(
                    "Verify alert",
                    text.getWithdrawalId(),
                    is(data.getCrmWithdrawalEventV2().getWithdrawalId()));
        } else if (alert instanceof AiTradingAlert tradingAlert) {
            assertThat("Verify alert", alert.getAlertType(), is("TRADING_ALERT"));
            AiTradingAlert.AlertText text = tradingAlert.getAlertText();
            assertThat("Verify alert", text, is(notNullValue()));
            // TODO enable after implementing it for trading rules
            //            assertThat("Verify alert", text.getAccount(), is(notNullValue()));
            //            assertThat("Verify alert", text.getReason(), is(notNullValue()));
            //            assertThat("Verify alert", text.getSymbolTraded(), is(notNullValue()));
            //            assertThat("Verify alert", text.getTicketId(), is(notNullValue()));
            //            assertThat("Verify alert", text.getServerId(), is(notNullValue()));
        }
    }
}
