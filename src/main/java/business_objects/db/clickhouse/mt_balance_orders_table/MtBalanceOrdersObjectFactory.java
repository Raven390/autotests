package business_objects.db.clickhouse.mt_balance_orders_table;

import static utils.Utils.getCurrentTimestampDbFormat;
import static utils.Utils.getRandomIntPositive;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import helpers.data.ClientHelper;
import io.qameta.allure.Step;

public class MtBalanceOrdersObjectFactory {
    @Step("Generate mt balance orders for given account")
    public static MtBalanceOrdersObject generateMtBalanceOrder(
            ClientHelper client, Double profit, Double profitUsd, String date) {
        return new MtBalanceOrdersObject(
                getRandomIntPositive(),
                client.getServerId(),
                "Test",
                client.getUcid(),
                client.getBrand(),
                "vfsc",
                client.getUserId(),
                client.getTradingAccount(),
                date,
                profit,
                profitUsd,
                "EURUSD",
                "Administration Fee Automation test");
    }

    @Step("Generate mt balance orders by account")
    public static MtBalanceOrdersObject generateMtBalanceOrderByAccount(CrmTbAccountObject account) {
        MtBalanceOrdersObject mtBalanceOrder = new MtBalanceOrdersObject();
        mtBalanceOrder.setTicket(getRandomIntPositive());
        mtBalanceOrder.setServerId(account.serverIdSt);
        mtBalanceOrder.setServerName(account.serverName);
        mtBalanceOrder.setUcid(account.ucid);
        mtBalanceOrder.setBrand(account.brand);
        mtBalanceOrder.setRegulator(account.regulator);
        mtBalanceOrder.setUserId(account.userId);
        mtBalanceOrder.setPlatform(account.platform);
        mtBalanceOrder.setAccount(account.account);
        mtBalanceOrder.setCurrency(account.currency);
        mtBalanceOrder.setCreateTime(getCurrentTimestampDbFormat());
        mtBalanceOrder.setCreateTimeUtc(getCurrentTimestampDbFormat());
        mtBalanceOrder.setAmount(123.45);
        mtBalanceOrder.setRateToUsd(1d);
        mtBalanceOrder.setAmountUsd(123.45);
        mtBalanceOrder.setOrderType("unknown");
        mtBalanceOrder.setComment("comment");
        mtBalanceOrder.setIsDeleted(0);
        mtBalanceOrder.setLastUpdated(getCurrentTimestampDbFormat());
        mtBalanceOrder.setInternalComment("mv");
        return mtBalanceOrder;
    }
}
