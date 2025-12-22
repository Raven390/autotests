package business_objects.db.clickhouse.dp_and_wd_by_channel;

import static utils.Utils.*;

import helpers.data.ClientHelper;

public class dpAndWdByChannelFactory {
    public static dpAndWdByChannelObject generatedpAndWdByChannelObject(ClientHelper client) {
        dpAndWdByChannelObject transaction = new dpAndWdByChannelObject();
        transaction.ucid = client.getUid();
        transaction.userId = client.getUserId();
        transaction.brand = client.getBrand();
        transaction.regulator = client.getRegulator();
        transaction.account = client.getTradingAccount();
        transaction.date = getCurrentDate();
        transaction.transferType = "Withdrawal";
        transaction.paymentChannel = "TestPaymentService";
        transaction.channelCategory = "Test Service Cathegory";
        transaction.totalAmountUsd = getRandomRoundedDouble(0.01, 50_000_000);
        transaction.totalCount = getRandomIntPositive();
        return transaction;
    }
}
