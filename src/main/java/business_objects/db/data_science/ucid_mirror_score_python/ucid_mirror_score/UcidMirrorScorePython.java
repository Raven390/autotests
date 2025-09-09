package business_objects.db.data_science.ucid_mirror_score_python.ucid_mirror_score;


public class UcidMirrorScorePython {
    String ucid;
    String id;
    Integer action;
    String timeUtc;
    String insertTimeUtc;
    Double modelScore;
    Integer countAction;
    Integer cumsumScore;
    Double ucidScore;

    public UcidMirrorScorePython() {
    }

    public UcidMirrorScorePython(
            String ucid, String id, Integer action, String timeUtc, String insertTimeUtc, Double modelScore,
            Integer countAction, Integer cumsumScore, Double ucidScore) {
        this.ucid = ucid;
        this.id = id;
        this.action = action;
        this.timeUtc = timeUtc;
        this.insertTimeUtc = insertTimeUtc;
        this.modelScore = modelScore;
        this.countAction = countAction;
        this.cumsumScore = cumsumScore;
        this.ucidScore = ucidScore;
    }

}
