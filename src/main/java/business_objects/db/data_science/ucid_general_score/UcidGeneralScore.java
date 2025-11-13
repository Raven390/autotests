package business_objects.db.data_science.ucid_general_score;

import java.util.Objects;

public class UcidGeneralScore {
    String ucid;
    String id;
    Integer action;
    String timeUtc;
    String insertTimeUtc;
    Double modelScore;
    Integer countAction;
    Double cumSumScore;
    Double ucidScore;

    public UcidGeneralScore() {
    }

    public UcidGeneralScore(
            String ucid, String id, Integer action, String timeUtc, String insertTimeUtc, Double modelScore,
            Integer countAction, Double cumSumScore, Double ucidScore) {
        this.ucid = ucid;
        this.id = id;
        this.action = action;
        this.timeUtc = timeUtc;
        this.insertTimeUtc = insertTimeUtc;
        this.modelScore = modelScore;
        this.countAction = countAction;
        this.cumSumScore = cumSumScore;
        this.ucidScore = ucidScore;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof UcidGeneralScore that)) return false;
        return Objects.equals(ucid, that.ucid) && Objects.equals(id, that.id) && Objects.equals(
                action, that.action) && Objects.equals(timeUtc, that.timeUtc) && Objects.equals(
                        insertTimeUtc, that.insertTimeUtc) && Objects.equals(modelScore, that.modelScore) && Objects.equals(
                                countAction, that.countAction) && Objects.equals(cumSumScore, that.cumSumScore) && Objects.equals(
                                        ucidScore, that.ucidScore);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ucid, id, action, timeUtc, insertTimeUtc, modelScore, countAction, cumSumScore, ucidScore);
    }

    @Override
    public String toString() {
        return "UcidGeneralScore{" + "ucid='" + ucid + '\'' + ", id='" + id + '\'' + ", action=" + action + ", timeUtc='" + timeUtc + '\'' + ", insertTimeUtc='" + insertTimeUtc + '\'' + ", modelScore=" + modelScore + ", countAction=" + countAction + ", cumSumScore=" + cumSumScore + ", ucidScore=" + ucidScore + '\'' + '}';
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

    public String getInsertTimeUtc() {
        return insertTimeUtc;
    }

    public void setInsertTimeUtc(String insertTimeUtc) {
        this.insertTimeUtc = insertTimeUtc;
    }

    public Double getModelScore() {
        return modelScore;
    }

    public void setModelScore(Double modelScore) {
        this.modelScore = modelScore;
    }

    public Integer getCountAction() {
        return countAction;
    }

    public void setCountAction(Integer countAction) {
        this.countAction = countAction;
    }

    public Double getCumSumScore() {
        return cumSumScore;
    }

    public void setCumSumScore(Double cumSumScore) {
        this.cumSumScore = cumSumScore;
    }

    public Double getUcidScore() {
        return ucidScore;
    }

    public void setUcidScore(Double ucidScore) {
        this.ucidScore = ucidScore;
    }
}
