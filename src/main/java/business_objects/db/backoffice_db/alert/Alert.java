package business_objects.db.backoffice_db.alert;

import java.util.Objects;

public class Alert {

    private Long id;
    private String uuid;
    private Long clientId;
    private String happenedAt;
    private String receivedAt;
    private String closedAt;
    private String status;
    private String rule;
    private String trigger;
    private String ruleVersion;
    private String fraudType;
    private String ruleAttributes;
    private String alertResolution;
    private Boolean isHighPriority;
    private String investigatorId;
    private String investigationId;
    private String amountUsd;
    private String paymentMethod;

    public String getInvestigationId() {
        return investigationId;
    }

    public void setInvestigationId(String investigationId) {
        this.investigationId = investigationId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getHappenedAt() {
        return happenedAt;
    }

    public void setHappenedAt(String happenedAt) {
        this.happenedAt = happenedAt;
    }

    public String getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(String receivedAt) {
        this.receivedAt = receivedAt;
    }

    public String getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(String closedAt) {
        this.closedAt = closedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getTrigger() {
        return trigger;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    public String getRuleVersion() {
        return ruleVersion;
    }

    public void setRuleVersion(String ruleVersion) {
        this.ruleVersion = ruleVersion;
    }

    public String getFraudType() {
        return fraudType;
    }

    public void setFraudType(String fraudType) {
        this.fraudType = fraudType;
    }

    public String getRuleAttributes() {
        return ruleAttributes;
    }

    public void setRuleAttributes(String ruleAttributes) {
        this.ruleAttributes = ruleAttributes;
    }

    public String getAlertResolution() {
        return alertResolution;
    }

    public void setAlertResolution(String alertResolution) {
        this.alertResolution = alertResolution;
    }

    public Boolean getHighPriority() {
        return isHighPriority;
    }

    public void setHighPriority(Boolean highPriority) {
        isHighPriority = highPriority;
    }

    public String getInvestigatorId() {
        return investigatorId;
    }

    public void setInvestigatorId(String investigatorId) {
        this.investigatorId = investigatorId;
    }

    public String getAmountUsd() {
        return amountUsd;
    }

    public void setAmountUsd(String amountUsd) {
        this.amountUsd = amountUsd;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Alert() {
    }

    public Alert(Long id, String uuid, Long clientId, String happenedAt, String receivedAt, String closedAt,
            String status, String rule, String trigger, String ruleVersion, String fraudType, String ruleAttributes,
            String alertResolution, Boolean isHighPriority, String investigatorId, String investigationId,
            String amountUsd, String paymentMethod) {
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
        this.fraudType = fraudType;
        this.ruleAttributes = ruleAttributes;
        this.alertResolution = alertResolution;
        this.isHighPriority = isHighPriority;
        this.investigatorId = investigatorId;
        this.investigationId = investigationId;
        this.amountUsd = amountUsd;
        this.paymentMethod = paymentMethod;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Alert alert = (Alert) o;
        return Objects.equals(id, alert.id) && Objects.equals(uuid, alert.uuid) && Objects.equals(clientId, alert.clientId) && Objects.equals(happenedAt, alert.happenedAt) && Objects.equals(receivedAt, alert.receivedAt) && Objects.equals(closedAt, alert.closedAt) && Objects.equals(status, alert.status) && Objects.equals(rule, alert.rule) && Objects.equals(trigger, alert.trigger) && Objects.equals(ruleVersion, alert.ruleVersion) && Objects.equals(fraudType, alert.fraudType) && Objects.equals(ruleAttributes, alert.ruleAttributes) && Objects.equals(alertResolution, alert.alertResolution) && Objects.equals(isHighPriority, alert.isHighPriority) && Objects.equals(investigatorId, alert.investigatorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid, clientId, happenedAt, receivedAt, closedAt, status, rule, trigger, ruleVersion, fraudType, ruleAttributes, alertResolution, isHighPriority, investigatorId);
    }

    @Override
    public String toString() {
        return "Alert{" + "id=" + id + ", uuid='" + uuid + '\'' + ", clientId=" + clientId + ", happenedAt='" + happenedAt + '\'' + ", receivedAt='" + receivedAt + '\'' + ", closedAt='" + closedAt + '\'' + ", status='" + status + '\'' + ", rule='" + rule + '\'' + ", trigger='" + trigger + '\'' + ", ruleVersion='" + ruleVersion + '\'' + ", fraudType='" + fraudType + '\'' + ", ruleAttributes='" + ruleAttributes + '\'' + ", alertResolution='" + alertResolution + '\'' + ", isHighPriority=" + isHighPriority + ", investigatorId='" + investigatorId + '\'' + '}';
    }
}
