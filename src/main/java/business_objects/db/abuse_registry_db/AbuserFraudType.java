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
    String fraudSubtypeCode;
    String symbols;
    String fraudSource;

    public AbuserFraudType() {
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AbuserFraudType that = (AbuserFraudType) o;
        return Objects.equals(ucid, that.ucid) && Objects.equals(fraudTypeCode, that.fraudTypeCode) && Objects.equals(status, that.status) && Objects.equals(comment, that.comment) && Objects.equals(modifiedByUser, that.modifiedByUser) && Objects.equals(modifiedBySystem, that.modifiedBySystem) && Objects.equals(fraudSubtypeCode, that.fraudSubtypeCode) && Objects.equals(symbols, that.symbols) && Objects.equals(fraudSource, that.fraudSource);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ucid, fraudTypeCode, status, comment, modifiedByUser, modifiedBySystem, fraudSubtypeCode, symbols, fraudSource);
    }

    @Override
    public String toString() {
        return "AbuserFraudType{" + "ucid='" + ucid + '\'' + ", fraudTypeCode='" + fraudTypeCode + '\'' + ", status='" + status + '\'' + ", comment='" + comment + '\'' + ", modifiedByUser='" + modifiedByUser + '\'' + ", modifiedBySystem='" + modifiedBySystem + '\'' + ", updatedAt='" + updatedAt + '\'' + ", createdAt='" + createdAt + '\'' + ", fraudSubtypeCode='" + fraudSubtypeCode + '\'' + ", symbols='" + symbols + '\'' + ", fraudSource='" + fraudSource + '\'' + '}';
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

    public String getFraudSubtypeCode() {
        return fraudSubtypeCode;
    }

    public void setFraudSubtypeCode(String fraudSubtypeCode) {
        this.fraudSubtypeCode = fraudSubtypeCode;
    }

    public String getSymbols() {
        return symbols;
    }

    public void setSymbols(String symbols) {
        this.symbols = symbols;
    }

    public String getFraudSource() {
        return fraudSource;
    }

    public void setFraudSource(String fraudSource) {
        this.fraudSource = fraudSource;
    }
}
