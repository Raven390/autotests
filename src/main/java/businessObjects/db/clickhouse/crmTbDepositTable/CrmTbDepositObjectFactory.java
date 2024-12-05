package businessObjects.db.clickhouse.crmTbDepositTable;

import helpers.data.ClientHelper;
import io.qameta.allure.Step;

import static utils.Utils.*;

public class CrmTbDepositObjectFactory {
    @Step("Generate deposit object by user id")
    public static CrmTbDepositObject generateDepositByClient(ClientHelper client) {
        return new CrmTbDepositObject(client.getTradingAccount(), 1.0, 2.0, client.getBrand(), getCurrentTimestampDbFormat(), "USD", 0.1, "CreditCard", "paymentDetails", getCurrentTimestampDbFormat(), "paymentRequisite", "paymentSys", "EUR", "paymentType", "VFSC", 1, getRandomIntPositive(), getRandomIntPositive(), client.getUcid(), getRandomUuidString(), getCurrentTimestampDbFormat(), client.getUserId());
    }
}
