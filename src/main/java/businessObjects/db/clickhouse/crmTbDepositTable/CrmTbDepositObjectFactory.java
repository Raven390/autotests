package businessObjects.db.clickhouse.crmTbDepositTable;

import helpers.data.ClientHelper;
import io.qameta.allure.Step;

import static utils.Utils.*;

public class CrmTbDepositObjectFactory {
    @Step("Generate deposit object by user id")
    public static CrmTbDepositObject generateDepositByClient(ClientHelper client) {
        CrmTbDepositObject depositObject = new CrmTbDepositObject();
        depositObject.account = client.getTradingAccount();
        depositObject.amount = 1.12;
        depositObject.amountUsd = 1.14;
        depositObject.brand = client.getBrand();
        depositObject.createTime = getCurrentTimestampDbFormat();
        depositObject.createTimeUtc = getCurrentTimestampDbFormat();
        depositObject.currency = "EUR";
        depositObject.fee = 0.10;
        depositObject.regulator = client.getRegulator();
        depositObject.statusId = 5;
        depositObject.transferId = getRandomIntPositive();
        depositObject.ucid = client.getUcid();
        depositObject.updateTime = getCurrentTimestampDbFormat();
        depositObject.updateTimeUtc = getCurrentTimestampDbFormat();
        depositObject.userId = client.getUserId();
        return depositObject;
    }
}
