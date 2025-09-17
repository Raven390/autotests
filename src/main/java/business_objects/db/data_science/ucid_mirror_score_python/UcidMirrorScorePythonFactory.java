package business_objects.db.data_science.ucid_mirror_score_python;

import helpers.data.ClientHelper;
import utils.Utils;

import static utils.Utils.getTomorrowTimestampDbFormat;

public class UcidMirrorScorePythonFactory {

    public static UcidMirrorScorePython generateUcidMirrorScorePythonObject(ClientHelper client, Double modelScore,
            Double ucidScore) {
        return new UcidMirrorScorePython(client.getUcid(), Utils.getRandomIntPositive().toString(), 1, getTomorrowTimestampDbFormat(), getTomorrowTimestampDbFormat(), modelScore, 1, 1, ucidScore);
    }

}
