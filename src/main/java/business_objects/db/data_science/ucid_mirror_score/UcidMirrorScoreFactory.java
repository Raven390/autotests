package business_objects.db.data_science.ucid_mirror_score;

import static utils.Utils.getTomorrowTimestampDbFormat;

import helpers.data.ClientHelper;
import utils.Utils;

public class UcidMirrorScoreFactory {

    public static UcidMirrorScore generateUcidMirrorScoreObject(ClientHelper client) {
        return new UcidMirrorScore(
                client.getUcid(),
                Utils.getRandomIntPositive().toString(),
                1,
                getTomorrowTimestampDbFormat(),
                1d,
                1,
                1,
                1d);
    }

    public static UcidMirrorScore generateUcidMirrorScoreObject(
            ClientHelper client, Double udicScore, Double modelScore) {
        return new UcidMirrorScore(
                client.getUcid(),
                Utils.getRandomIntPositive().toString(),
                1,
                getTomorrowTimestampDbFormat(),
                modelScore,
                1,
                1,
                udicScore);
    }
}
