package business_objects.kafka;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MirrorScoreEvent {

    // JSON example for reference:
    //    {
    //             "type": "mirrorScore",
    //            "id": "7c8a3c2b-2a5e-4cb5-9f84-67a21e4adf93",
    //            "schemaVersion": "2.3",
    //            "timestamp": 1734973200,
    //            "actionTimeUtc": "2025-10-22T09:45:32Z",
    //            "ucid": "vantage-6794350",
    //            "countAction": 23,
    //            "actionId": "MT5-CLOSE-1734973200-01",
    //            "ucidScore": 1.33
    //    }

    @JsonProperty("type")
    private String type;

    @JsonProperty("id")
    private String id;

    @JsonProperty("schemaVersion")
    private String schemaVersion;

    @JsonProperty("timestamp")
    private Integer timestamp;

    @JsonProperty("actionTimeUtc")
    private String actionTimeUtc;

    @JsonProperty("ucid")
    private String ucid;

    @JsonProperty("countAction")
    private Integer countAction;

    @JsonProperty("actionId")
    private String actionId;

    @JsonProperty("ucidScore")
    private Double ucidScore;

    public MirrorScoreEvent(
            String type,
            String id,
            String schemaVersion,
            Integer timestamp,
            String actionTimeUtc,
            String ucid,
            Integer countAction,
            String actionId,
            Double ucidScore) {
        this.type = type;
        this.id = id;
        this.schemaVersion = schemaVersion;
        this.timestamp = timestamp;
        this.actionTimeUtc = actionTimeUtc;
        this.ucid = ucid;
        this.countAction = countAction;
        this.actionId = actionId;
        this.ucidScore = ucidScore;
    }

    public MirrorScoreEvent() {}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSchemaVersion() {
        return schemaVersion;
    }

    public void setSchemaVersion(String schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    public String getActionTimeUtc() {
        return actionTimeUtc;
    }

    public void setActionTimeUtc(String actionTimeUtc) {
        this.actionTimeUtc = actionTimeUtc;
    }

    public String getUcid() {
        return ucid;
    }

    public void setUcid(String ucid) {
        this.ucid = ucid;
    }

    public Integer getCountAction() {
        return countAction;
    }

    public void setCountAction(Integer countAction) {
        this.countAction = countAction;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public Double getUcidScore() {
        return ucidScore;
    }

    public void setUcidScore(Double ucidScore) {
        this.ucidScore = ucidScore;
    }
}
