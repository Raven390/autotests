package business_objects.db.clickhouse.s3_fact_login_metrics;

import helpers.data.ClientHelper;
import io.qameta.allure.Step;

import static utils.Utils.*;

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
        metric.setDailyCoreSpreadRevenueOz(getRandomRoundedDouble(-99_999.99, 99_999.99));
        metric.setDailyCoreSpreadRevenuePe(getRandomRoundedDouble(-99_999.99, 99_999.99));
        metric.setDailyTakerSpreadRevenueOz(getRandomRoundedDouble(-99_999.99, 99_999.99));
        metric.setDailyTakerSpreadRevenuePe(getRandomRoundedDouble(-99_999.99, 99_999.99));
        metric.setDailyLpSpreadRevenueOz(getRandomRoundedDouble(-99_999.99, 99_999.99));
        metric.setDailyLpSpreadRevenuePe(getRandomRoundedDouble(-99_999.99, 99_999.99));
        metric.setDailyVbSpreadRevenueOz(getRandomRoundedDouble(-99_999.99, 99_999.99));
        metric.setDailyVbSpreadRevenuePe(getRandomRoundedDouble(-99_999.99, 99_999.99));
        metric.setDailyAppliedMinSpreadRevenueOz(getRandomRoundedDouble(-99_999.99, 99_999.99));
        metric.setDailyAppliedMinSpreadRevenuePe(getRandomRoundedDouble(-99_999.99, 99_999.99));
        metric.setDailyAppliedMaxSpreadRevenueOz(getRandomRoundedDouble(-99_999.99, 99_999.99));
        metric.setDailyAppliedMaxSpreadRevenuePe(getRandomRoundedDouble(-99_999.99, 99_999.99));
        metric.setDailyMakerSpreadRevenueOz(getRandomRoundedDouble(-99_999.99, 99_999.99));
        metric.setDailyClientSlippageRevenueOz(getRandomRoundedDouble(-99_999.99, 99_999.99));
        metric.setDailyClientSlippageRevenuePe(getRandomRoundedDouble(-99_999.99, 99_999.99));
        metric.setDailyCommissionRevenue(getRandomRoundedDouble(-99_999.99, 99_999.99));
        metric.setDailySwapsRevenue(getRandomRoundedDouble(-99_999.99, 99_999.99));
        metric.setIbCommission(getRandomRoundedDouble(-99_999.99, 99_999.99));
        metric.setSalesCommission(getRandomRoundedDouble(-99_999.99, 99_999.99));
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
