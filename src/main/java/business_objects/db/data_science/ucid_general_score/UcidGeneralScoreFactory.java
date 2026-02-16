package business_objects.db.data_science.ucid_general_score;

import static java.time.OffsetDateTime.now;
import static utils.Utils.*;

import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import java.time.OffsetDateTime;

public class UcidGeneralScoreFactory {

    public static UcidGeneralScore generateUcidGeneralScoreObject(
            ClientHelper client, Double modelScore, Double ucidScore) {
        return new UcidGeneralScore(
                client.getUcid(), getRandomIntPositive().toString(), 1, now(), now(), modelScore, 1, 1d, ucidScore);
    }

    public static UcidGeneralScore generateUcidGeneralScoreObject(
            DataHelper data, OffsetDateTime date, Double modelScore, Double ucidScore) {
        return new UcidGeneralScore(
                data.getClientHelper().getUcid(),
                getRandomIntPositive().toString(),
                1,
                date,
                date,
                modelScore,
                1,
                1d,
                ucidScore);
    }
}
