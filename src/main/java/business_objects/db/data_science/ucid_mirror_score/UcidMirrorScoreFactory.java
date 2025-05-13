package business_objects.db.data_science.ucid_mirror_score;

import helpers.data.ClientHelper;
import utils.Utils;

public class UcidMirrorScoreFactory {

    public UcidMirrorScore getUcidMirrorScoreObject(ClientHelper client) {
        return new UcidMirrorScore(
                Utils.getRandomUuidString(), client.getUcid(), Utils.getCurrentTimestampDbFormat(), 1.25, 2.50);
    }

}
