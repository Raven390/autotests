package helpers.asserts;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import business_objects.kafka.restriction_events.WithdrawalApprovals;
import business_objects.kafka.restriction_events.WithdrawalApprovalsV2;
import helpers.data.DataHelper;
import helpers.data.enums.Brand;
import io.qameta.allure.Step;
import java.util.UUID;

public class WithdrawalApprovalAssertsHelper {
    @Step("Assert withdrawal approval")
    public static void assertWithdrawalApprovalV2(
            DataHelper data, UUID paymentId, WithdrawalApprovalsV2 withdrawalApproval) {
        if (data.crmWithdrawalEventV2 == null) {
            assertThat(
                    "SchemaVersion should match transferToWaEvent.schemaVersion",
                    withdrawalApproval.getSchemaVersion(),
                    is(data.transferToWaEvent.getSchemaVersion()));
            assertThat("PaymentId should match provided paymentId", withdrawalApproval.getPaymentId(), is(paymentId));
            assertThat(
                    "Id should match transferToWaEvent.id",
                    withdrawalApproval.getId(),
                    is(data.transferToWaEvent.getId().toString()));
            assertThat("Timestamp should not be null", withdrawalApproval.getTimestamp(), is(notNullValue()));
            assertThat(
                    "TransferId should match transferToWaEvent.transferId",
                    withdrawalApproval.getTransferId(),
                    is(data.transferToWaEvent.getTransferId()));
            assertThat(
                    "Brand should be Vantage display name",
                    withdrawalApproval.getBrand(),
                    is(Brand.VANTAGE.getDisplayName()));
            assertThat(
                    "ClientId should match transferToWaEvent.clientId",
                    withdrawalApproval.getClientId().toString(),
                    is(data.transferToWaEvent.getClientId().toString()));
            assertThat(
                    "Type should match transferToWaEvent.type",
                    withdrawalApproval.getType(),
                    is(data.transferToWaEvent.getType()));
            assertThat(
                    "Regulator should match transferToWaEvent.regulator",
                    withdrawalApproval.getRegulator(),
                    is(data.transferToWaEvent.getRegulator()));
            assertThat("InternalReason should be empty", withdrawalApproval.getInternalReason(), is(""));
            assertThat("Status should be 'Approve'", withdrawalApproval.getStatus(), is("Approve"));
            assertThat(
                    "MerchantOrderId should match transferToWaEvent.merchantOrderId",
                    withdrawalApproval.getMerchantOrderId(),
                    is(data.transferToWaEvent.getMerchantOrderId()));
            assertThat(
                    "CheckName should match transferToWaEvent.checkName",
                    withdrawalApproval.getCheckName(),
                    is(data.transferToWaEvent.getCheckName()));
            assertThat("RuleName should be 'Router rule'", withdrawalApproval.getRuleName(), is("Router rule"));
            assertThat("RejectionReasonCode should be empty", withdrawalApproval.getRejectionReasonCode(), is(""));
            assertThat("RejectionReason should be empty", withdrawalApproval.getRejectionReason(), is(""));
            assertThat(
                    "RejectionReasonRecommend should be empty",
                    withdrawalApproval.getRejectionReasonRecommend(),
                    is(""));
            assertThat("UnderManualReview should be 1", withdrawalApproval.getUnderManualReview(), is(1));
        } else {
            assertThat(
                    "SchemaVersion should match crmWithdrawalEventV2.schemaVersion",
                    withdrawalApproval.getSchemaVersion(),
                    is(data.crmWithdrawalEventV2.getSchemaVersion()));
            assertThat("PaymentId should match provided paymentId", withdrawalApproval.getPaymentId(), is(paymentId));
            assertThat(
                    "Id should match crmWithdrawalEventV2.id",
                    withdrawalApproval.getId(),
                    is(data.crmWithdrawalEventV2.getId()));
            assertThat("Timestamp should not be null", withdrawalApproval.getTimestamp(), is(notNullValue()));
            assertThat(
                    "TransferId should match crmWithdrawalEventV2.withdrawalId",
                    withdrawalApproval.getTransferId(),
                    is(data.crmWithdrawalEventV2.getWithdrawalId()));
            assertThat(
                    "Brand should be Vantage display name",
                    withdrawalApproval.getBrand(),
                    is(Brand.VANTAGE.getDisplayName()));
            assertThat(
                    "ClientId should match crmWithdrawalEventV2.clientId",
                    withdrawalApproval.getClientId().toString(),
                    is(data.crmWithdrawalEventV2.getClientId().toString()));
            assertThat(
                    "Type should match crmWithdrawalEventV2.type",
                    withdrawalApproval.getType(),
                    is(data.crmWithdrawalEventV2.getType()));
            assertThat(
                    "Regulator should match crmWithdrawalEventV2.regulator",
                    withdrawalApproval.getRegulator(),
                    is(data.crmWithdrawalEventV2.getRegulator()));
            assertThat("InternalReason should be empty", withdrawalApproval.getInternalReason(), is(""));
            assertThat("Status should be 'Approve'", withdrawalApproval.getStatus(), is("Approve"));
            assertThat(
                    "MerchantOrderId should match crmWithdrawalEventV2.merchantOrderId",
                    withdrawalApproval.getMerchantOrderId(),
                    is(data.crmWithdrawalEventV2.getMerchantOrderId()));
            assertThat(
                    "CheckName should match crmWithdrawalEventV2.checkName",
                    withdrawalApproval.getCheckName(),
                    is(data.crmWithdrawalEventV2.getCheckName()));
            assertThat("RuleName should be 'Router rule'", withdrawalApproval.getRuleName(), is("Router rule"));
            assertThat("RejectionReasonCode should be empty", withdrawalApproval.getRejectionReasonCode(), is(""));
            assertThat("RejectionReason should be empty", withdrawalApproval.getRejectionReason(), is(""));
            assertThat(
                    "RejectionReasonRecommend should be empty",
                    withdrawalApproval.getRejectionReasonRecommend(),
                    is(""));
            assertThat("UnderManualReview should be 1", withdrawalApproval.getUnderManualReview(), is(1));
        }
    }

    @Step("Assert withdrawal approval")
    public static void assertWithdrawalApprovalV1(
            DataHelper data, UUID paymentId, WithdrawalApprovals withdrawalApproval) {
        if (data.crmWithdrawalEventV2 == null) {
            assertThat("Assert withdrawal.approval message", withdrawalApproval.getTimestamp(), is(notNullValue()));
            assertThat(
                    "Assert withdrawal.approval message",
                    withdrawalApproval.getMessageId(),
                    is(data.crmWithdrawalEvent.getId()));
            assertThat(
                    "Assert withdrawal.approval message",
                    withdrawalApproval.getTransferId(),
                    is(data.crmWithdrawalEvent.getWithdrawalId()));
            assertThat(
                    "Assert withdrawal.approval message",
                    withdrawalApproval.getBrand(),
                    is(data.crmWithdrawalEvent.getBrand().replace("v", "V")));
            assertThat(
                    "Assert withdrawal.approval message",
                    withdrawalApproval.getRegulator(),
                    is(data.crmWithdrawalEvent.getRegulator()));
            assertThat("Assert withdrawal.approval message", withdrawalApproval.getInternalReason(), is(""));
            assertThat("Assert withdrawal.approval message", withdrawalApproval.getStatus(), is("Approve"));
            assertThat(
                    "Assert withdrawal.approval message",
                    withdrawalApproval.getOrderNumber(),
                    is(data.crmWithdrawalEventV2.getMerchantOrderId()));
            assertThat("Assert withdrawal.approval message", withdrawalApproval.getCheckName(), is(""));
        }
    }
}
