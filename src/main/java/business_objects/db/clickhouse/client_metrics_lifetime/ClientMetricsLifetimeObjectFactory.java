package business_objects.db.clickhouse.client_metrics_lifetime;

import static utils.Utils.*;
import static utils.Utils.getCurrentTimestampMinusOffsetFormatted;

import helpers.data.ClientHelper;
import helpers.data.enums.DateTimeFormat;
import io.qameta.allure.Step;

public class ClientMetricsLifetimeObjectFactory {
    @Step("Generate client metrics lifetime object by client")
    public static ClientMetricsLifetimeObject generateClientMetricsLifetimeByClient(
            ClientHelper client, Double totalPnl, String lastActionDate) {
        return new ClientMetricsLifetimeObject(
                client.getUcid(),
                client.getBrand(),
                client.getRegulator(),
                client.getUserId().longValue(),
                totalPnl,
                150.74,
                96.75,
                11L,
                lastActionDate,
                getCurrentTimestampDbFormat());
    }

    @Step("Generate client metrics lifetime object by client")
    public static ClientMetricsLifetimeObject generateClientMetricsLifetimeByClient(ClientHelper client) {
        return new ClientMetricsLifetimeObject(
                client.getUcid(),
                client.getBrand(),
                client.getRegulator(),
                client.getUserId().longValue(),
                getRandomRoundedDouble(-200_000, 2_000_000),
                getRandomRoundedDouble(-200_000, 2_000_000),
                getRandomRoundedDouble(-200_000, 2_000_000),
                getRandomIntPositive().longValue(),
                getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 0, 1, 12, 11),
                getCurrentTimestampDbFormat());
    }
}
