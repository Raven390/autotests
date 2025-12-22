package business_objects.db.clickhouse.payments_total;

import static utils.Utils.*;

import helpers.data.ClientHelper;

public class PaymentsTotalFactory {
    public static PaymentsTotalObject generatePaymentsTotalObjectClient(ClientHelper client) {
        PaymentsTotalObject payment = new PaymentsTotalObject();
        payment.ucid = client.getUcid();
        payment.brand = client.getBrand();
        payment.regulator = client.getRegulator();
        payment.userId = client.getUserId();
        payment.account = client.getTradingAccount();
        payment.date = getCurrentDate();
        payment.totalDepositUsd = getRandomRoundedDouble(0, 500_000);
        payment.totalDepositCount = getRandomIntPositive();
        payment.totalWithdrawalUsd = getRandomRoundedDouble(0, 500_000);
        payment.totalWithdrawalCount = getRandomIntPositive();
        payment.netDepositsUsd = getRandomRoundedDouble(0, 500_000);
        payment.totalBalanceopsCount = getRandomIntPositive();
        payment.totalCreditsUsd = getRandomRoundedDouble(0, 500_000);
        payment.totalCreditsCount = getRandomIntPositive();
        payment.totalTransfersUsd = getRandomRoundedDouble(0, 500_000);
        payment.totalTransfersCount = getRandomIntPositive();
        payment.lastUpdated = getCurrentTimestampDbFormat();
        return payment;
    }
}
