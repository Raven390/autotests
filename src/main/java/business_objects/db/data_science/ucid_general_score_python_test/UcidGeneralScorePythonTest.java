package business_objects.db.data_science.ucid_general_score_python_test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UcidGeneralScorePythonTest {
    String ucid;
    String id;
    Integer action;
    String timeUtc;
    String insertTimeUtc;
    Double modelScore;
    Integer countAction;
    Double cumSumScore;
    Double ucidScore;
    Double ucidScoreFiveLast;
}
