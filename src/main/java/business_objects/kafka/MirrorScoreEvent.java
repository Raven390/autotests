package business_objects.kafka;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MirrorScoreEvent {

    // JSON example for reference:
    //    {
    //        "type": "mirrorScore",
    //            "id": "9bc43e25-19b7-4b5f-a907-44877b677823",
    //            "schemaVersion": "3.2",
    //            "timestamp": 1768994134,
    //            "actionTimeUtc": "2026-01-21T11:07:47Z",
    //            "ucid": "vantage-3470414",
    //            "account": 7167125, //optional
    //            "countAction": 57,
    //            "actionId": "1316775961",
    //            "eventType": 8,
    //            "ucidScore": 0.87
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

    @JsonProperty("account")
    private Long account;

    @JsonProperty("server_id")
    private Long serverId;

    @JsonProperty("eventType")
    private Long eventType;

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
