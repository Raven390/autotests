package business_objects.db.data_science.ucid_mirror_score;

import java.util.Objects;

public class UcidMirrorScore {
    String ucid;
    String id;
    Integer action;
    String timeUtc;
    Double modelScore;
    Integer countFraud;
    Integer countAction;
    Double ucidScore;

    public UcidMirrorScore() {
    }

    public UcidMirrorScore(
            String ucid, String id, Integer action, String timeUtc, Double modelScore, Integer countFraud,
            Integer countAction, Double ucidScore) {
        this.ucid = ucid;
        this.id = id;
        this.action = action;
        this.timeUtc = timeUtc;
        this.modelScore = modelScore;
        this.countFraud = countFraud;
        this.countAction = countAction;
        this.ucidScore = ucidScore;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof UcidMirrorScore that)) return false;
        return Objects.equals(ucid, that.ucid) && Objects.equals(id, that.id) && Objects.equals(
                action, that.action) && Objects.equals(timeUtc, that.timeUtc) && Objects.equals(
                        modelScore, that.modelScore) && Objects.equals(countFraud, that.countFraud) && Objects.equals(
                                countAction, that.countAction) && Objects.equals(ucidScore, that.ucidScore);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ucid, id, action, timeUtc, modelScore, countFraud, countAction, ucidScore);
    }

    @Override
    public String toString() {
        return "UcidMirrorScore{" + "ucid='" + ucid + '\'' + ", id='" + id + '\'' + ", action=" + action + ", timeUtc='" + timeUtc + '\'' + ", modelScore=" + modelScore + ", countFraud=" + countFraud + ", countAction=" + countAction + ", ucidScore=" + ucidScore + '}';
    }

    public String getUcid() {
        return ucid;
    }

    public void setUcid(String ucid) {
        this.ucid = ucid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public String getTimeUtc() {
        return timeUtc;
    }

    public void setTimeUtc(String timeUtc) {
        this.timeUtc = timeUtc;
    }

    public Double getModelScore() {
        return modelScore;
    }

    public void setModelScore(Double modelScore) {
        this.modelScore = modelScore;
    }

    public Integer getCountFraud() {
        return countFraud;
    }

    public void setCountFraud(Integer countFraud) {
        this.countFraud = countFraud;
    }

    public Integer getCountAction() {
        return countAction;
    }

    public void setCountAction(Integer countAction) {
        this.countAction = countAction;
    }

    public Double getUcidScore() {
        return ucidScore;
    }

    public void setUcidScore(Double ucidScore) {
        this.ucidScore = ucidScore;
    }
}
