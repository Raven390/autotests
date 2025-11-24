package business_objects.db.clickhouse.crm_tb_credit_card_table;

import helpers.data.ClientHelper;
import io.qameta.allure.Step;

import static utils.Utils.*;

public class CrmTbCreditCardObjectFactory {
    @Step("Generate credit card object by user id")
    public static CrmTbCreditCardObject generateByClient(ClientHelper client, int creditCardId) {
        CrmTbCreditCardObject depositObject = new CrmTbCreditCardObject();
        depositObject.id = creditCardId;
        depositObject.sourceIdSt = client.getServerId();
        depositObject.userId = client.getUserId();
        depositObject.createTime = getCurrentTimestampDbFormat();
        depositObject.updateTime = getCurrentTimestampDbFormat();
        depositObject.cardBeginSixDigits = "454793";
        depositObject.cardLastFourDigits = "5815";
        depositObject.cardHolderName = "Mr Test";
        depositObject.expiryMonth = "12";
        depositObject.expiryYear = "2020";
        depositObject.threeDomainSecure = 1;
        depositObject.paymentType = 0;
        depositObject.status = 0;
        depositObject.isDel = 0;
        depositObject.lastUpdated = getCurrentTimestampDbFormat();
        return depositObject;
    }
}
