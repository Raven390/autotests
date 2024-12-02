package businessObjects.db.boDb.alert;

import java.util.Objects;

public class Alert {

    public Long id;
    public String uuid;
    public Long clientId;
    public String happenedAt;
    public String receivedAt;
    public String closedAt;
    public String status;
    public String rule;
    public String trigger;
    public String ruleVersion;
    public Long ruleCode;
    public String fraudType;
    public String ruleAttributes;
    public Boolean confirmed;

    public Alert() {
    }

    public Alert(Long id, String uuid, Long clientId, String happenedAt, String receivedAt, String closedAt, String status, String rule, String trigger, String ruleVersion, Long ruleCode, String fraudType, String ruleAttributes, Boolean confirmed) {
        this.id = id;
        this.uuid = uuid;
        this.clientId = clientId;
        this.happenedAt = happenedAt;
        this.receivedAt = receivedAt;
        this.closedAt = closedAt;
        this.status = status;
        this.rule = rule;
        this.trigger = trigger;
        this.ruleVersion = ruleVersion;
        this.ruleCode = ruleCode;
        this.fraudType = fraudType;
        this.ruleAttributes = ruleAttributes;
        this.confirmed = confirmed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Alert alert = (Alert) o;
        return Objects.equals(id, alert.id) && Objects.equals(uuid, alert.uuid) && Objects.equals(clientId, alert.clientId) && Objects.equals(happenedAt, alert.happenedAt) && Objects.equals(receivedAt, alert.receivedAt) && Objects.equals(closedAt, alert.closedAt) && Objects.equals(status, alert.status) && Objects.equals(rule, alert.rule) && Objects.equals(trigger, alert.trigger) && Objects.equals(ruleVersion, alert.ruleVersion) && Objects.equals(ruleCode, alert.ruleCode) && Objects.equals(fraudType, alert.fraudType) && Objects.equals(ruleAttributes, alert.ruleAttributes) && Objects.equals(confirmed, alert.confirmed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid, clientId, happenedAt, receivedAt, closedAt, status, rule, trigger, ruleVersion, ruleCode, fraudType, ruleAttributes, confirmed);
    }

    @Override
    public String toString() {
        return "Alert{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", clientId=" + clientId +
                ", happenedAt='" + happenedAt + '\'' +
                ", receivedAt='" + receivedAt + '\'' +
                ", closedAt='" + closedAt + '\'' +
                ", status='" + status + '\'' +
                ", rule='" + rule + '\'' +
                ", trigger='" + trigger + '\'' +
                ", ruleVersion='" + ruleVersion + '\'' +
                ", ruleCode='" + ruleCode + '\'' +
                ", fraudType='" + fraudType + '\'' +
                ", ruleAttributes='" + ruleAttributes + '\'' +
                ", confirmed=" + confirmed +
                '}';
    }
}
