package business_objects.db.ib_summary_by_date;

import static utils.Utils.getCurrentDate;

import helpers.data.ClientHelper;

public class IbSummaryByDateFactory {
    public static IbSummaryByDateObject generateIbSummaryByDateByClient(ClientHelper client) {
        return new IbSummaryByDateObject(
                1,
                client.getUserId(),
                client.getBrand(),
                client.getRegulator(),
                client.getUcid(),
                client.getTradingAccount().longValue(),
                getCurrentDate(),
                123.45,
                234.56,
                345.67,
                111.11,
                99.87,
                9.654,
                987.321,
                12_332.12);
    }
}
