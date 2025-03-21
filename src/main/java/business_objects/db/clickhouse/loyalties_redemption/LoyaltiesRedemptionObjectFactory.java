package business_objects.db.clickhouse.loyalties_redemption;

import helpers.data.ClientHelper;

import static utils.Utils.getCurrentTimestampDbFormat;

public class LoyaltiesRedemptionObjectFactory {

    public static LoyaltiesRedemptionObject generateLoyaltiesRedemptionObjectByClient(ClientHelper client) {
        return new LoyaltiesRedemptionObject(client.getUcid(), 1, client.getBrand(), client.getRegulator(), client.getUserId(), 1d, 1d, 1d, 1d, "", 1d, 1d, 1, "", getCurrentTimestampDbFormat(), getCurrentTimestampDbFormat(), getCurrentTimestampDbFormat());
    }
}
