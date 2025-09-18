package business_objects.db.clickhouse.crm_tb_withdrawal;

import helpers.data.ClientHelper;
import io.qameta.allure.Step;

import static utils.Utils.*;

public class CrmTbWithdrawalObjectFactory {
    @Step("Generate withdrawal object by user id")
    public static CrmTbWithdrawalObject generateCrmTbWithdrawalObjectByClient(ClientHelper client) {
        return new CrmTbWithdrawalObject(
                client.getServerId(), 1, client.getBrand(), client.getRegulator(), client.getUserId(), client.getUcid(), client.getTradingAccount(), getRandomIntPositive(), getCurrentTimestampDbFormat(), getCurrentTimestampDbFormat(), getCurrentTimestampDbFormat(), getCurrentTimestampDbFormat(), getCurrentTimestampDbFormat(), getCurrentTimestampDbFormat(), getRandomRoundedDouble(1000, 500_000), getRandomRoundedDouble(1000, 500_000), getRandomRoundedDouble(0.1, 1000), getRandomRoundedDouble(0.1, 1000), "USD", 16, "Risk Audit", 1, "paymentType", 1, "paymentChannel", "paymentSystemAccount", "EUR", "paymentDetails", getCurrentTimestampDbFormat(), getRandomIntPositive(), getRandomRoundedDouble(0.1, 500_000), "processedNotes", 1, 1, 1, getCurrentTimestampDbFormat()
        );
    }
}
