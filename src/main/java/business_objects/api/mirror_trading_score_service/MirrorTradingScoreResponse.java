package business_objects.api.mirror_trading_score_service;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class MirrorTradingScoreResponse {

    @JsonProperty("modelScore")
    String modelScore;

    @JsonProperty("ucidScore")
    String ucidScore;

    public MirrorTradingScoreResponse() {
    }

    public MirrorTradingScoreResponse(String modelScore, String ucidScore) {
        this.modelScore = modelScore;
        this.ucidScore = ucidScore;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MirrorTradingScoreResponse that)) return false;
        return Objects.equals(modelScore, that.modelScore) && Objects.equals(ucidScore, that.ucidScore);
    }

    @Override
    public int hashCode() {
        return Objects.hash(modelScore, ucidScore);
    }

    @Override
    public String toString() {
        return "MirrorTradingScoreResponse{" + "modelScore='" + modelScore + '\'' + ", ucidScore='" + ucidScore + '\'' + '}';
    }

    public String getModelScore() {
        return modelScore;
    }

    public void setModelScore(String modelScore) {
        this.modelScore = modelScore;
    }
}
