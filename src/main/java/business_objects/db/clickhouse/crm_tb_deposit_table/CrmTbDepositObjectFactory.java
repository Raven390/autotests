package business_objects.db.clickhouse.crm_tb_deposit_table;

import helpers.data.ClientHelper;
import io.qameta.allure.Step;

import static utils.Utils.*;

public class CrmTbDepositObjectFactory {
    @Step("Generate deposit object by user id")
    public static CrmTbDepositObject generateDepositByClient(ClientHelper client) {
        CrmTbDepositObject depositObject = new CrmTbDepositObject();
        depositObject.sourceIdSt = client.getServerId();
        depositObject.brandUid = 1;
        depositObject.brand = client.getBrand();
        depositObject.regulator = client.getRegulator();
        depositObject.userId = client.getUserId();
        depositObject.ucid = client.getUcid();
        depositObject.account = client.getTradingAccount();
        depositObject.transferId = getRandomIntPositive();
        depositObject.createTime = getCurrentTimestampDbFormat();
        depositObject.createTimeUtc = getCurrentTimestampDbFormat();
        depositObject.updateTime = getCurrentTimestampDbFormat();
        depositObject.updateTimeUtc = getCurrentTimestampDbFormat();
        depositObject.amount = getRandomRoundedDouble(0.1, 500_000);
        depositObject.amountUsd = getRandomRoundedDouble(0.1, 500_000);
        depositObject.currency = "EUR";
        depositObject.statusId = 5;
        depositObject.status = "Success";
        depositObject.paymentTypeId = 1;
        depositObject.paymentType = "Other";
        depositObject.paymentChannel = "Other";
        depositObject.paymentSystemAccount = "Other";
        depositObject.paymentSystemCurrency = "EUR";
        depositObject.paymentDetails = "details";
        depositObject.paymentExpirationDate = getCurrentTimestampDbFormat();
        depositObject.ticket = "121212";
        depositObject.fee = getRandomRoundedDouble(0.1, 500_000);
        depositObject.processedNotes = "notes";
        depositObject.isDel = 0;
        depositObject.isNonApp = 1;
        depositObject.lastUpdated = getCurrentTimestampDbFormat();
        return depositObject;
    }
}
