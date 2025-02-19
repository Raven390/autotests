package businessObjects.db.clickhouse.s3FactLoginMetrics;

import helpers.data.ClientHelper;
import io.qameta.allure.Step;

import static utils.Utils.getCurrentDate;
import static utils.Utils.getCurrentTimestampDbFormat;

public class s3FactLoginMetricsFactory {
    @Step("Generate data for given account")
    public static s3FactLoginMetricsObject generates3FactLoginMetricsClient(ClientHelper client) {
        s3FactLoginMetricsObject metric = new s3FactLoginMetricsObject();
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
    public static s3FactLoginMetricsObject generates3FactLoginMetricsClientAdditionalAccount(ClientHelper client) {
        s3FactLoginMetricsObject metric = new s3FactLoginMetricsObject();
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
