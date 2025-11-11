package business_objects.db.clickhouse.crm_tb_withdraw_account;

import helpers.data.ClientHelper;
import io.qameta.allure.Step;

import static utils.Utils.getCurrentTimestampDbFormat;

public class CrmTbWithdrawAccountObjectFactory {
    @Step("Generate credit card object by user id")
    public static CrmTbWithdrawAccountObject generateByClient(ClientHelper client) {
        CrmTbWithdrawAccountObject depositObject = new CrmTbWithdrawAccountObject();
        depositObject.sourceIdSt = client.getServerId();
        depositObject.userId = client.getUserId();
        depositObject.bankName = "Bank123";
        depositObject.bankCard = "BankCard123";
        depositObject.swiftCode = "123";
        depositObject.createTime = getCurrentTimestampDbFormat();
        depositObject.updateTime = getCurrentTimestampDbFormat();
        depositObject.isDel = 0;
        depositObject.lastUpdated = getCurrentTimestampDbFormat();
        return depositObject;
    }
}
