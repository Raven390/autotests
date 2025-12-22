package business_objects.db.clickhouse.crm_tb_transfer;

import static utils.Utils.*;

import helpers.data.ClientHelper;

public class CrmTbTransferFactory {

    private CrmTbTransferFactory() {}

    public static CrmTbTransferObject generateCrmTbTransferRandomized(ClientHelper client) {
        CrmTbTransferObject transfer = new CrmTbTransferObject();
        transfer.setSourceIdSt(client.getServerId());
        transfer.brand = client.getBrand();
        transfer.regulator = client.getRegulator();
        transfer.setUserId(client.getUserId());
        transfer.ucid = client.getUcid();
        transfer.setTransferId(client.getUserId() + getRandomIntPositive());
        transfer.setAccountFrom(client.getTradingAccount());
        transfer.accountTo = 424242001L;
        transfer.createTime = getCurrentTimestampDbFormat();
        transfer.createTimeUtc = getCurrentTimestampDbFormat();
        transfer.updateTime = getCurrentTimestampDbFormat();
        transfer.updateTimeUtc = getCurrentTimestampDbFormat();
        transfer.amountUsd = getRandomRoundedDouble(0.01, 999_999.99);
        transfer.amountFrom = transfer.amountUsd;
        transfer.amount = transfer.amountUsd;
        transfer.currencyFrom = "USD";
        transfer.currencyTo = "EUR";
        transfer.statusId = 2L;
        transfer.status = "ok";
        transfer.isDel = 0;
        transfer.isDeleted = 0;
        transfer.internalComment = "autotest" + getCurrentTimestampSeconds();
        transfer.lastUpdated = getCurrentTimestampDbFormat();
        return transfer;
    }
}
