package business_objects.db.clickhouse.bo_alerts;

import java.util.Objects;

public class BoAlertsObject {

    protected Integer alertId;
    protected Integer id;
    protected String ucid;
    protected String brand;
    protected String createdAt;
    protected String createdAtUtc;
    protected String resolvedAt;
    protected String resolvedAtUtc;
    protected String status;
    protected String rule;
    protected String ruleAttributes;
    protected String alertResolution;
    protected String lastUpdated;

    @Deprecated
    public BoAlertsObject(
            Integer alertId, String ucid, String brand, String createdAt, String createdAtUtc, String resolvedAt,
            String resolvedAtUtc, String status, String rule, String alertResolution, String lastUpdated) {
        this.alertId = alertId;
        this.ucid = ucid;
        this.brand = brand;
        this.createdAt = createdAt;
        this.createdAtUtc = createdAtUtc;
        this.resolvedAt = resolvedAt;
        this.resolvedAtUtc = resolvedAtUtc;
        this.status = status;
        this.rule = rule;
        this.alertResolution = alertResolution;
        this.lastUpdated = lastUpdated;
    }

    public BoAlertsObject(
            Integer id, String ucid, String brand, String createdAt, String createdAtUtc, String resolvedAt,
            String resolvedAtUtc, String status, String rule, String ruleAttributes, String alertResolution,
            String lastUpdated) {
        this.id = id;
        this.status = status;
        this.rule = rule;
        this.ruleAttributes = ruleAttributes;
        this.alertResolution = alertResolution;
        this.lastUpdated = lastUpdated;
    }

    public BoAlertsObject() {

    }

    public String getRuleAttributes() {
        return ruleAttributes;
    }

    public void setRuleAttributes(String ruleAttributes) {
        this.ruleAttributes = ruleAttributes;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAlertId() {
        return alertId;
    }

    public void setAlertId(Integer alertId) {
        this.alertId = alertId;
    }

    public String getUcid() {
        return ucid;
    }

    public void setUcid(String ucid) {
        this.ucid = ucid;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedAtUtc() {
        return createdAtUtc;
    }

    public void setCreatedAtUtc(String createdAtUtc) {
        this.createdAtUtc = createdAtUtc;
    }

    public String getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(String resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public String getResolvedAtUtc() {
        return resolvedAtUtc;
    }

    public void setResolvedAtUtc(String resolvedAtUtc) {
        this.resolvedAtUtc = resolvedAtUtc;
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

    public String getAlertResolution() {
        return alertResolution;
    }

    public void setAlertResolution(String alertResolution) {
        this.alertResolution = alertResolution;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BoAlertsObject that)) return false;
        return Objects.equals(alertId, that.alertId) && Objects.equals(ucid, that.ucid) && Objects.equals(
                brand, that.brand) && Objects.equals(createdAt, that.createdAt) && Objects.equals(
                        createdAtUtc, that.createdAtUtc) && Objects.equals(resolvedAt, that.resolvedAt) && Objects.equals(
                                resolvedAtUtc, that.resolvedAtUtc) && Objects.equals(status, that.status) && Objects.equals(
                                        rule, that.rule) && Objects.equals(alertResolution, that.alertResolution) && Objects.equals(lastUpdated, that.lastUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(alertId, ucid, brand, createdAt, createdAtUtc, resolvedAt, resolvedAtUtc, status, rule, alertResolution, lastUpdated);
    }

    @Override
    public String toString() {
        return "BoAlertsObject{" + "alertId=" + alertId + ", ucid='" + ucid + '\'' + ", brand='" + brand + '\'' + ", createdAt='" + createdAt + '\'' + ", createdAtUtc='" + createdAtUtc + '\'' + ", resolvedAt='" + resolvedAt + '\'' + ", resolvedAtUtc='" + resolvedAtUtc + '\'' + ", status='" + status + '\'' + ", rule='" + rule + '\'' + ", alertResolution='" + alertResolution + '\'' + ", lastUpdated='" + lastUpdated + '\'' + '}';
    }
}
