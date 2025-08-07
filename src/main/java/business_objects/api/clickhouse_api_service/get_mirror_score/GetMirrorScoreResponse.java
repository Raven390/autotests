package business_objects.api.clickhouse_api_service.get_mirror_score;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class GetMirrorScoreResponse {

    @JsonProperty("modelScore")
    private String modelScore;

    @JsonProperty("ucidScore")
    private String ucidScore;

    public GetMirrorScoreResponse() {
    }

    public GetMirrorScoreResponse(String modelScore, String ucidScore) {
        this.modelScore = modelScore;
        this.ucidScore = ucidScore;
    }

    public String getModelScore() {
        return modelScore;
    }

    public void setModelScore(String modelScore) {
        this.modelScore = modelScore;
    }

    public String getUcidScore() {
        return ucidScore;
    }

    public void setUcidScore(String ucidScore) {
        this.ucidScore = ucidScore;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof GetMirrorScoreResponse that)) return false;
        return Objects.equals(modelScore, that.modelScore) && Objects.equals(ucidScore, that.ucidScore);
    }

    @Override
    public int hashCode() {
        return Objects.hash(modelScore, ucidScore);
    }

    @Override
    public String toString() {
        return "GetMirrorScoreResponse{" + "modelScore='" + modelScore + '\'' + ", ucidScore='" + ucidScore + '\'' + '}';
    }
}
