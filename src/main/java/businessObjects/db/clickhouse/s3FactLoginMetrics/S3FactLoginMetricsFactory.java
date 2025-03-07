package businessObjects.db.clickhouse.s3FactLoginMetrics;

import helpers.data.ClientHelper;
import io.qameta.allure.Step;

import static utils.Utils.getCurrentDate;
import static utils.Utils.getCurrentTimestampDbFormat;

public class S3FactLoginMetricsFactory {
    @Step("Generate data for given account")
    public static S3FactLoginMetricsObject generateS3FactLoginMetricsClient(ClientHelper client) {
        S3FactLoginMetricsObject metric = new S3FactLoginMetricsObject();
        metric.setDate(getCurrentDate());
        metric.setBrand(client.getBrand());
        metric.setRegulator(client.getRegulator());
        metric.setUserId(client.getUserId());
        metric.setUcid(client.getUcid());
        metric.setAccount(client.getTradingAccount());
        metric.setServerId(client.getServerId());
        metric.setDlInsertTs(getCurrentTimestampDbFormat());
        metric.setDlUpdateTs(getCurrentTimestampDbFormat());
        return metric;
    }

    @Step("Generate data for given account")
    public static S3FactLoginMetricsObject generateS3FactLoginMetricsClientAdditionalAccount(ClientHelper client) {
        S3FactLoginMetricsObject metric = new S3FactLoginMetricsObject();
        metric.setDate(getCurrentDate());
        metric.setBrand(client.getBrand());
        metric.setRegulator(client.getRegulator());
        metric.setUserId(client.getUserId());
        metric.setUcid(client.getUcid());
        metric.setAccount(client.getTradingAccount2());
        metric.setServerId(client.getServerId());
        metric.setDlInsertTs(getCurrentTimestampDbFormat());
        metric.setDlUpdateTs(getCurrentTimestampDbFormat());
        return metric;
    }
}
