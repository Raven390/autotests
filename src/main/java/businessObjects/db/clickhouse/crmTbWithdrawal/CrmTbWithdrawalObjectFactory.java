package businessObjects.db.clickhouse.crmTbWithdrawal;

import helpers.data.ClientHelper;
import io.qameta.allure.Step;

import static utils.Utils.*;

public class CrmTbWithdrawalObjectFactory {
    @Step("Generate user object by user id")
    public static CrmTbWithdrawalObject generateWithdrawalByClient(ClientHelper client) {
        return new CrmTbWithdrawalObject(
                1, 1, client.getBrand(), client.getRegulator(), client.getUserId(), client.getUcid(), client.getTradingAccount(), getRandomIntPositive(), getCurrentTimestampDbFormat(), getCurrentTimestampDbFormat(), getCurrentTimestampDbFormat(), getCurrentTimestampDbFormat(), getCurrentTimestampDbFormat(), getCurrentTimestampDbFormat(), 1.0, 2.0, 3.0, 4.0, "USD", 1, "Audit", 1, "paymentType", 1, "paymentChannel", "paymentSystemAccount", "EUR", "paymentDetails", getCurrentTimestampDbFormat(), getRandomIntPositive(), 0.1, "processedNotes", 1, 1, 1, getCurrentTimestampDbFormat()
        );
    }
}
