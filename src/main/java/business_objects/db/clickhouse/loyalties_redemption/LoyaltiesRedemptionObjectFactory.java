package business_objects.db.clickhouse.loyalties_redemption;

import static utils.Utils.getCurrentTimestampDbFormat;

import helpers.data.ClientHelper;

public class LoyaltiesRedemptionObjectFactory {

    public static LoyaltiesRedemptionObject generateLoyaltiesRedemptionObjectByClient(ClientHelper client) {
        return new LoyaltiesRedemptionObject(
                client.getUcid(),
                1,
                client.getBrand(),
                client.getRegulator(),
                client.getUserId(),
                1d,
                1d,
                1d,
                1d,
                "",
                1d,
                1d,
                1,
                "",
                getCurrentTimestampDbFormat(),
                getCurrentTimestampDbFormat(),
                getCurrentTimestampDbFormat());
    }
}
