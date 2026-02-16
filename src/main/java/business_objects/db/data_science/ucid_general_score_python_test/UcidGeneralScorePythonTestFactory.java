package business_objects.db.data_science.ucid_general_score_python_test;

import static utils.Utils.getCurrentTimestampDbFormat;
import static utils.Utils.getRandomIntPositive;

import helpers.data.DataHelper;

public class UcidGeneralScorePythonTestFactory {

    public static UcidGeneralScorePythonTest generateUcidGeneralScorePythonTestObject(
            DataHelper data, Double modelScore, Double ucidScore, Double cumSumScore, Double ucidScoreFiveLast) {
        return new UcidGeneralScorePythonTest(
                data.clientHelper.getUcid(),
                getRandomIntPositive().toString(),
                1,
                getCurrentTimestampDbFormat(),
                getCurrentTimestampDbFormat(),
                modelScore,
                1,
                cumSumScore,
                ucidScore,
                ucidScoreFiveLast);
    }
}
