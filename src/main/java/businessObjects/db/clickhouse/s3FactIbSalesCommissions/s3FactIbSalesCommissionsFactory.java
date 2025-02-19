package businessObjects.db.clickhouse.s3FactIbSalesCommissions;

import helpers.data.ClientHelper;
import io.qameta.allure.Step;

import static utils.Utils.getCurrentDate;
import static utils.Utils.getCurrentTimestampDbFormat;

public class s3FactIbSalesCommissionsFactory {
    @Step("Generate data for given account")
    public static s3FactIbSalesCommissionsObject generateS3FactIbSalesCommissionsClient(ClientHelper client) {
        s3FactIbSalesCommissionsObject commission = new s3FactIbSalesCommissionsObject();
        commission.setDate(getCurrentDate());
        commission.setBrand(client.getBrand());
        commission.setRegulator(client.getRegulator());
        commission.setUserId(client.getUserId());
        commission.setUcid(client.getUcid());
        commission.setAccount(client.getTradingAccount());
        commission.setServerId(client.getServerId());
        commission.setDlInsertTs(getCurrentTimestampDbFormat());
        commission.setDlUpdateTs(getCurrentTimestampDbFormat());
        return commission;
    }

}
