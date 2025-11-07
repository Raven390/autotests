package business_objects.kafka.alerts;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RuleAlertV2 {

    @JsonProperty("reason")
    private String reason;

    @JsonProperty("timestamp")
    private String timestamp;

    @JsonProperty("alertId")
    private String alertId;

    @JsonProperty("rule")
    private Rule rule;

    @JsonProperty("triggerCreatedTime")
    private String triggerCreatedTime;

    @JsonProperty("fraudType")
    private String fraudType;

    @JsonProperty("trigger")
    private String trigger;

    @JsonProperty("ucid")
    private String ucid;

    @JsonProperty("type")
    private String type;

    @JsonProperty("attributes")
    private Attribute attributes;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getAlertId() {
        return alertId;
    }

    public void setAlertId(String alertId) {
        this.alertId = alertId;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public String getTriggerCreatedTime() {
        return triggerCreatedTime;
    }

    public void setTriggerCreatedTime(String triggerCreatedTime) {
        this.triggerCreatedTime = triggerCreatedTime;
    }

    public String getFraudType() {
        return fraudType;
    }

    public void setFraudType(String fraudType) {
        this.fraudType = fraudType;
    }

    public String getTrigger() {
        return trigger;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    public String getUcid() {
        return ucid;
    }

    public void setUcid(String ucid) {
        this.ucid = ucid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Attribute getAttributes() {
        return attributes;
    }

    public void setAttributes(Attribute attributes) {
        this.attributes = attributes;
    }

    public static class Rule {

        @JsonProperty("ver")
        private String ver;

        @JsonProperty("name")
        private String name;

        public String getVer() {
            return ver;
        }

        public void setVer(String ver) {
            this.ver = ver;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class Attribute {

        @JsonProperty("Details")
        private String details;

        @JsonProperty("Ucid")
        private String ucid;

        @JsonProperty("UcidScore")
        private Double ucidScore;

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        public String getUcid() {
            return ucid;
        }

        public void setUcid(String ucid) {
            this.ucid = ucid;
        }

        public Double getUcidScore() {
            return ucidScore;
        }

        public void setUcidScore(Double ucidScore) {
            this.ucidScore = ucidScore;
        }
    }
}
