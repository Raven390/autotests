package business_objects.db.abuse_registry_db;

import java.util.Objects;

public class AbuserFraudType {
    String ucid;
    String fraudTypeCode;
    String status;
    String comment;
    String modifiedByUser;
    String modifiedBySystem;
    String updatedAt;
    String createdAt;

    public AbuserFraudType() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbuserFraudType that = (AbuserFraudType) o;
        return Objects.equals(ucid, that.ucid) && Objects.equals(fraudTypeCode, that.fraudTypeCode) && Objects.equals(status, that.status) && Objects.equals(comment, that.comment) && Objects.equals(modifiedByUser, that.modifiedByUser) && Objects.equals(modifiedBySystem, that.modifiedBySystem) && Objects.equals(updatedAt, that.updatedAt) && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ucid, fraudTypeCode, status, comment, modifiedByUser, modifiedBySystem, updatedAt, createdAt);
    }

    public String getUcid() {
        return ucid;
    }

    public void setUcid(String ucid) {
        this.ucid = ucid;
    }

    public String getFraudTypeCode() {
        return fraudTypeCode;
    }

    public void setFraudTypeCode(String fraudTypeCode) {
        this.fraudTypeCode = fraudTypeCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getModifiedByUser() {
        return modifiedByUser;
    }

    public void setModifiedByUser(String modifiedByUser) {
        this.modifiedByUser = modifiedByUser;
    }

    public String getModifiedBySystem() {
        return modifiedBySystem;
    }

    public void setModifiedBySystem(String modifiedBySystem) {
        this.modifiedBySystem = modifiedBySystem;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
