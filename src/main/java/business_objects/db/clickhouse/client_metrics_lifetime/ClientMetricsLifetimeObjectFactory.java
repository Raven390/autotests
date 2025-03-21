package business_objects.db.clickhouse.client_metrics_lifetime;

import helpers.data.ClientHelper;
import io.qameta.allure.Step;

import static utils.Utils.getCurrentTimestampDbFormat;

public class ClientMetricsLifetimeObjectFactory {
    @Step("Generate client metrics lifetime object by client")
    public static ClientMetricsLifetimeObject generateClientMetricsLifetimeByClient(ClientHelper client,
            Double totalPnl, String lastActionDate) {
        return new ClientMetricsLifetimeObject(client.getUcid(), client.getBrand(), client.getRegulator(), client.getUserId().longValue(), totalPnl, 150.74, 96.75, 11L, lastActionDate, getCurrentTimestampDbFormat());
    }
}