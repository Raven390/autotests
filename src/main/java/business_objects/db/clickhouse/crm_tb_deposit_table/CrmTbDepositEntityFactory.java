package business_objects.db.clickhouse.crm_tb_deposit_table;

import static utils.Utils.*;

import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import io.qameta.allure.Step;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public class CrmTbDepositEntityFactory {
    @Step("Generate deposit object by user id")
    public static CrmTbDepositEntity generateCrmTbDepositEntityByClient(ClientHelper client) {
        var now = OffsetDateTime.now();
        var amount = getRandomRoundedDouble(1000, 500_000);
        return CrmTbDepositEntity.builder()
                .sourceIdSt(7)
                .brandUid(3)
                .brand(client.getBrand())
                .paymentDetails("paymentDetails")
                .regulator(client.getRegulator())
                .userId((long) client.getUserId())
                .ucid(client.getUcid())
                .account(BigInteger.valueOf(client.getTradingAccount()))
                .transferId(BigInteger.valueOf(getRandomLongPositive()))
                .createTime(now)
                .createTimeUtc(now)
                .updateTime(now)
                .updateTimeUtc(now)
                .amountSubmitted(BigDecimal.valueOf(amount))
                .amountSubmittedUsd(BigDecimal.valueOf(amount))
                .amount(BigDecimal.valueOf(amount))
                .amountUsd(BigDecimal.valueOf(amount))
                .currency("USD")
                .statusId(5)
                .status("Success")
                .statusGroup("Success")
                .paymentTypeId(1)
                .paymentType("paymentType")
                .paymentChannelId(1)
                .paymentChannel("paymentChannel")
                .paymentFamily("paymentFamily")
                .paymentSystemAccount("paymentSystemAccount")
                .paymentSystemCurrency("USD")
                .paymentExpirationDate("2030-11-10")
                .ticket(String.valueOf(getRandomLongPositive()))
                .vWalletAccount(UUID.randomUUID().toString())
                .fee(BigDecimal.valueOf(getRandomRoundedDouble(0.1, 1000)))
                .processedNotes("notes")
                .isDel(0)
                .isNonApp(0)
                .lastUpdated(now)
                .orderNumber(String.valueOf(getRandomLongPositive()))
                .isDeleted(0)
                .creditCardId(getRandomLongPositive())
                .paymentProfile("paymentProfile")
                .firstSixDigits(String.valueOf(getRandomIntPositiveWithBounds(100_000, 999_999)))
                .paymentProfileMasked("paymentProfileMasked")
                .paymentProfileKey("paymentProfileMaskedKey")
                .build();
    }

    public static DataHelper addDepositSumByCategory(DataHelper data, Double amount) {
        data.crmTbDepositObjects = List.of(generateCrmTbDepositEntityByClient(data.clientHelper));
        data.crmTbDepositObjects.getFirst().setSourceIdSt(1);
        data.crmTbDepositObjects.getFirst().setBrandUid(0);
        data.crmTbDepositObjects.getFirst().setAmount(BigDecimal.valueOf(amount));
        data.crmTbDepositObjects.getFirst().setAmountUsd(BigDecimal.valueOf(amount));
        data.crmTbDepositObjects.getFirst().setStatusId(5);
        data.crmTbDepositObjects.getFirst().setStatus("Success");
        data.crmTbDepositObjects.getFirst().setPaymentTypeId(9);
        data.crmTbDepositObjects.getFirst().setPaymentType("Local Bank Transfer");
        data.crmTbDepositObjects.getFirst().setPaymentChannelId(1);
        data.crmTbDepositObjects.getFirst().setPaymentChannel("web");
        data.crmTbDepositObjects.getFirst().setPaymentSystemAccount("Credit card");
        return data;
    }
}
