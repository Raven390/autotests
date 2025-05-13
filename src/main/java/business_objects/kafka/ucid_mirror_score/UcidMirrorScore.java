package business_objects.kafka.ucid_mirror_score;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UcidMirrorScore {

    @JsonProperty("Id")
    public String alertId;

    @JsonProperty("ucid")
    public String ucid;

    @JsonProperty("time")
    public String timestamp;

    @JsonProperty("model score")
    public Double modelScore;

    @JsonProperty("ucid score")
    public Double ucidScore;


}
