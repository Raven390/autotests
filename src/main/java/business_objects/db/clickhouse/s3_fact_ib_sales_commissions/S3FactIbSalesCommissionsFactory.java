package business_objects.db.clickhouse.s3_fact_ib_sales_commissions;

import helpers.data.ClientHelper;
import io.qameta.allure.Step;

import static utils.Utils.getCurrentDate;
import static utils.Utils.getCurrentTimestampDbFormat;

public class S3FactIbSalesCommissionsFactory {
    @Step("Generate IB sale commission data")
    public static S3FactIbSalesCommissionsObject generateS3FactIbSalesCommissionsClient(ClientHelper client) {
        S3FactIbSalesCommissionsObject commission = new S3FactIbSalesCommissionsObject();
        commission.setDate(getCurrentDate());
        commission.setBrand(client.getBrand());
        commission.setRegulator(client.getRegulator());
        commission.setUserId(client.getUserId());
        commission.setUcid(client.getUcid());
        commission.setAccount(client.getTradingAccount());
        commission.setServerId(client.getServerId());
        commission.setDlInsertTs(getCurrentTimestampDbFormat());
        commission.setDlUpdateTs(getCurrentTimestampDbFormat());
        commission.setIbUserId(123);
        return commission;
    }

}
