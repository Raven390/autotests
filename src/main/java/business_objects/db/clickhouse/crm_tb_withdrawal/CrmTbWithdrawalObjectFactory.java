package business_objects.db.clickhouse.crm_tb_withdrawal;

import helpers.data.ClientHelper;
import io.qameta.allure.Step;

import static utils.Utils.*;

public class CrmTbWithdrawalObjectFactory {
    @Step("Generate withdrawal object by user id")
    public static CrmTbWithdrawalObject generateWithdrawalByClient(ClientHelper client) {
        return new CrmTbWithdrawalObject(
                client.getServerId(), 1, client.getBrand(), client.getRegulator(), client.getUserId(), client.getUcid(), client.getTradingAccount(), getRandomIntPositive(), getCurrentTimestampDbFormat(), getCurrentTimestampDbFormat(), getCurrentTimestampDbFormat(), getCurrentTimestampDbFormat(), getCurrentTimestampDbFormat(), getCurrentTimestampDbFormat(), getRandomRoundedDouble(0.1, 500_000), getRandomRoundedDouble(0.1, 500_000), getRandomRoundedDouble(0.1, 500_000), getRandomRoundedDouble(0.1, 500_000), "USD", 16, "Risk Audit", 1, "paymentType", 1, "paymentChannel", "paymentSystemAccount", "EUR", "paymentDetails", getCurrentTimestampDbFormat(), getRandomIntPositive(), getRandomRoundedDouble(0.1, 500_000), "processedNotes", 1, 1, 1, getCurrentTimestampDbFormat()
        );
    }

    public static CrmTbWithdrawalObject generateStaticWithdrawalByClient(ClientHelper client, String paymentType,
            int counter) {
        return new CrmTbWithdrawalObject(
                client.getServerId(), 1, client.getBrand(), client.getRegulator(), client.getUserId(), client.getUcid(), client.getTradingAccount(), (client.getUserId() * 100) + counter, "2024-10-13 12:03:12.084000000", "2024-10-13 12:03:12.084000000", "2024-10-13 12:03:12.084000000", "2024-10-13 12:03:12.084000000", "2024-10-13 12:03:12.084000000", "2024-10-13 12:03:12.084000000", 1.0, 2.0, 3.0, 4.0, "USD", 1, "Risk Audit", 1, paymentType, 1, "paymentChannel", "paymentSystemAccount", "EUR", "paymentDetails", "2024-10-13 12:03:12.084000000", (client.getUserId() * 100) + counter, 0.1, "processedNotes", 1, 1, 1, getCurrentTimestampDbFormat()
        );
    }
}
