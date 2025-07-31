package business_objects.db.data_science.ucid_mirror_score;

import helpers.data.ClientHelper;
import utils.Utils;

import static utils.Utils.getTomorrowTimestampDbFormat;

public class UcidMirrorScoreFactory {

    public static UcidMirrorScore generateUcidMirrorScoreObject(ClientHelper client) {
        return new UcidMirrorScore(client.getUcid(), Utils.getRandomIntPositive().toString(), 1, getTomorrowTimestampDbFormat(), 1d, 1, 1, 1d);
    }

}
