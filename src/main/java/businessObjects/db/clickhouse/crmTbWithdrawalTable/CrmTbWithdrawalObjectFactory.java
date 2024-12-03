package businessObjects.db.clickhouse.crmTbWithdrawalTable;

import helpers.data.ClientHelper;
import io.qameta.allure.Step;

import static utils.Utils.*;

public class CrmTbWithdrawalObjectFactory {
    @Step("Generate user object by user id")
    public static CrmTbWithdrawalObject generateWithdrawalByClient(ClientHelper client) {
        return new CrmTbWithdrawalObject(client.getTradingAccount(), 1.0,2.0, client.getBrand(), getCurrentTimestampDbFormat(),
                "USD",0.1,"CreditCard", "paymentDetails", getCurrentTimestampDbFormat(),
                "paymentRequisite","paymentSystemAccount",
                "EUR","paymentType","VFSC",0.9,getCurrentTimestampDbFormat(),
                1, getRandomIntPositive(), getRandomIntPositive(), client.getUcid(), getRandomUuidString(), getCurrentTimestampDbFormat(), client.getUserId());
    }
}
