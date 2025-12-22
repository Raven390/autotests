package business_objects.db.data_science.ucid_general_score;

import static utils.Utils.*;

import helpers.data.ClientHelper;

public class UcidGeneralScoreFactory {

    public static UcidGeneralScore generateUcidGeneralScoreObject(
            ClientHelper client, Double modelScore, Double ucidScore) {
        return new UcidGeneralScore(
                client.getUcid(),
                getRandomIntPositive().toString(),
                1,
                getCurrentTimestampDbFormat(),
                getCurrentTimestampDbFormat(),
                modelScore,
                1,
                1d,
                ucidScore);
    }
}
