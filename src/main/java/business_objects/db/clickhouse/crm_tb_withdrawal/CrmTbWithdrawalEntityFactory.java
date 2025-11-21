package business_objects.db.clickhouse.crm_tb_withdrawal;

import helpers.data.ClientHelper;
import io.qameta.allure.Step;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.OffsetDateTime;
import java.util.UUID;

import static utils.Utils.*;

public class CrmTbWithdrawalEntityFactory {
    @Step("Generate withdrawal object by user id")
    public static CrmTbWithdrawalEntity generateCrmTbWithdrawalEntityByClient(ClientHelper client) {
        var now = OffsetDateTime.now();
        var amount = getRandomRoundedDouble(1000, 500_000);
        return CrmTbWithdrawalEntity.builder().sourceIdSt(client.getServerId()).brandUid(1).brand(client.getBrand()).regulator(client.getRegulator()).userId((long) client.getUserId()).ucid(client.getUcid()).account(BigInteger.valueOf(client.getTradingAccount())).transferId(BigInteger.valueOf(getRandomLongPositive())).createTime(now).createTimeUtc(now).updateTime(now).updateTimeUtc(now).reversedTime(now).reversedTimeUtc(now).amountSubmitted(BigDecimal.valueOf(amount)).amountSubmittedUsd(BigDecimal.valueOf(amount)).amount(BigDecimal.valueOf(amount)).amountUsd(BigDecimal.valueOf(amount)).reversedAmount(BigDecimal.valueOf(amount)).reversedAmountUsd(BigDecimal.valueOf(amount)).currency("USD").statusId(16).status("Risk Audit").paymentTypeId(1).paymentType("paymentType").paymentChannelId(1).paymentChannel("paymentChannel").paymentFamily("paymentFamily").paymentSystemAccount("paymentSystemAccount").paymentSystemCurrency("USD").paymentExpirationDate("2030-11-10").ticket(String.valueOf(getRandomLongPositive())).vWalletAccount(UUID.randomUUID().toString()).fee(BigDecimal.valueOf(getRandomRoundedDouble(0.1, 1000))).processedNotes("notes").isDel(0).isNonApp(0).lastUpdated(now).cryptoWalletAddress(String.valueOf(getRandomLongPositive())).cardNumber(String.valueOf(getRandomLongPositive())).sourceTable("sourceTable").orderNumber(String.valueOf(getRandomLongPositive())).isDeleted(0).accountName("accountName").ipAddress("ipAddress").creditCardId(getRandomLongPositive()).paymentProfile("paymentProfile").paymentProfileMasked("paymentProfileMasked").paymentProfileKey("paymentProfileMaskedKey").userPaymentInfoId(BigInteger.valueOf(getRandomLongPositive())).withdrawAccountId(BigInteger.valueOf(client.getTradingAccount())).skrillEmail("skrill@email.com").perfectmoneyEmail("peperfectmoney@email.com").bitwalletEmail("bitwallet@email.com").ebuyEmail("ebuy@email.com").build();
    }
}
