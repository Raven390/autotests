package business_objects.db.data_science.ucid_mirror_score;

import java.util.Objects;

public class UcidMirrorScore {
    String id;
    String ucid;
    String time;
    Double modelScore;
    Double ucidScore;

    public UcidMirrorScore() {
    }

    public UcidMirrorScore(String id, String ucid, String time, Double modelScore, Double ucidScore) {
        this.id = id;
        this.ucid = ucid;
        this.time = time;
        this.modelScore = modelScore;
        this.ucidScore = ucidScore;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof UcidMirrorScore that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(ucid, that.ucid) && Objects.equals(
                time, that.time) && Objects.equals(modelScore, that.modelScore) && Objects.equals(
                        ucidScore, that.ucidScore);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ucid, time, modelScore, ucidScore);
    }

    @Override
    public String toString() {
        return "UcidMirrorScoreFactory{" + "id='" + id + '\'' + ", ucid='" + ucid + '\'' + ", time='" + time + '\'' + ", modelScore=" + modelScore + ", ucidScore=" + ucidScore + '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUcid() {
        return ucid;
    }

    public void setUcid(String ucid) {
        this.ucid = ucid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Double getModelScore() {
        return modelScore;
    }

    public void setModelScore(Double modelScore) {
        this.modelScore = modelScore;
    }

    public Double getUcidScore() {
        return ucidScore;
    }

    public void setUcidScore(Double ucidScore) {
        this.ucidScore = ucidScore;
    }
}
