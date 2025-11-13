package business_objects.db.data_science.ucid_general_score;

import helpers.data.ClientHelper;

import static utils.Utils.*;

public class UcidGeneralScoreFactory {

    public static UcidGeneralScore generateUcidGeneralScoreObject(ClientHelper client, Double modelScore,
            Double ucidScore) {
        return new UcidGeneralScore(client.getUcid(), getRandomIntPositive().toString(), 1, getCurrentTimestampDbFormat(), getCurrentTimestampDbFormat(), modelScore, 1, 1d, ucidScore);
    }

}
