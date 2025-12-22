package helpers.asserts;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import business_objects.kafka.payment.acknowledgement.Acknowledge;
import helpers.data.DataHelper;
import io.qameta.allure.Step;
import java.util.UUID;

public class AcknowledgeAssertsHelper {
    @Step("Assert acknowledge")
    public static void assertAcknowledge(DataHelper data, UUID paymentId, Acknowledge acknowledge) {
        if (data.crmWithdrawalEventV2 == null) {
            assertThat("Subtype should be 'acknowledge'", acknowledge.getSubtype(), is("acknowledge"));
            assertThat(
                    "TransferId should match transferToWaEvent.transferId",
                    acknowledge.getTransferId(),
                    is(data.transferToWaEvent.getTransferId()));
            assertThat(
                    "MerchantOrderId should match transferToWaEvent.merchantOrderId",
                    acknowledge.getMerchantOrderId(),
                    is(data.transferToWaEvent.getMerchantOrderId()));
            // assertThat("Brand should match transferToWaEvent.brand", acknowledge.getBrand(),
            // is(data.transferToWaEvent.getBrand()));
            assertThat(
                    "Id should match transferToWaEvent.id",
                    acknowledge.getId(),
                    is(data.transferToWaEvent.getId().toString()));
            assertThat("Status should be 'RECEIVED'", acknowledge.getStatus(), is("RECEIVED"));
            assertThat(
                    "CorrelationId should equal transferToWaEvent.id",
                    acknowledge.getCorrelationId(),
                    is(data.transferToWaEvent.getId().toString()));
            assertThat(
                    "Type should match transferToWaEvent.type",
                    acknowledge.getType(),
                    is(data.transferToWaEvent.getType()));
            assertThat("SchemaVersion should be '1.0'", acknowledge.getSchemaVersion(), is("1.0"));
            assertThat(
                    "Regulator should match transferToWaEvent.regulator",
                    acknowledge.getRegulator(),
                    is(data.transferToWaEvent.getRegulator()));
            assertThat("Timestamp should not be null", acknowledge.getTimestamp(), is(notNullValue()));
            assertThat(
                    "ClientId should match transferToWaEvent.clientId",
                    acknowledge.getClientId(),
                    is(data.transferToWaEvent.getClientId()));
            assertThat("PaymentId should match provided paymentId", acknowledge.getPaymentId(), is(paymentId));
        } else {
            assertThat("Subtype should be 'acknowledge'", acknowledge.getSubtype(), is("acknowledge"));
            assertThat(
                    "TransferId should match crmWithdrawalEventV2.withdrawalId",
                    acknowledge.getTransferId(),
                    is(data.crmWithdrawalEventV2.getWithdrawalId()));
            assertThat(
                    "MerchantOrderId should match crmWithdrawalEventV2.merchantOrderId",
                    acknowledge.getMerchantOrderId(),
                    is(data.crmWithdrawalEventV2.getMerchantOrderId()));
            // assertThat("Brand should match crmWithdrawalEventV2.brand", acknowledge.getBrand(),
            // is(data.crmWithdrawalEventV2.getBrand()));
            assertThat(
                    "Id should match crmWithdrawalEventV2.id",
                    acknowledge.getId(),
                    is(data.crmWithdrawalEventV2.getId()));
            assertThat("Status should be 'RECEIVED'", acknowledge.getStatus(), is("RECEIVED"));
            assertThat(
                    "CorrelationId should equal crmWithdrawalEventV2.id",
                    acknowledge.getCorrelationId(),
                    is(data.crmWithdrawalEventV2.getId()));
            assertThat(
                    "Type should match crmWithdrawalEventV2.type",
                    acknowledge.getType(),
                    is(data.crmWithdrawalEventV2.getType()));
            assertThat("SchemaVersion should be '2.0'", acknowledge.getSchemaVersion(), is("2.0"));
            assertThat(
                    "Regulator should match crmWithdrawalEventV2.regulator",
                    acknowledge.getRegulator(),
                    is(data.crmWithdrawalEventV2.getRegulator()));
            assertThat("Timestamp should not be null", acknowledge.getTimestamp(), is(notNullValue()));
            assertThat(
                    "ClientId should match crmWithdrawalEventV2.clientId",
                    acknowledge.getClientId(),
                    is(data.crmWithdrawalEventV2.getClientId()));
            assertThat("PaymentId should match provided paymentId", acknowledge.getPaymentId(), is(paymentId));
        }
    }
}
