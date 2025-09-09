package business_objects.db.mitigation_service_db;

import java.util.Objects;

public class ClientGeneralRestriction {

    private Long id;
    private String ucid;
    private String regulator;
    private Long restrictionId;
    private String comment;
    private String status;
    private String cancellationReason;
    private String failReason;
    private String createdAt;
    private String updatedAt;

    public ClientGeneralRestriction() {
    }

    public ClientGeneralRestriction(Long id, String ucid, String regulator, Long restrictionId, String comment,
            String status, String cancellationReason, String failReason, String createdAt, String updatedAt) {
        this.id = id;
        this.ucid = ucid;
        this.regulator = regulator;
        this.restrictionId = restrictionId;
        this.comment = comment;
        this.status = status;
        this.cancellationReason = cancellationReason;
        this.failReason = failReason;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public ClientGeneralRestriction(String ucid, String regulator, Long restrictionId, String comment,
            String status) {
        this.ucid = ucid;
        this.regulator = regulator;
        this.restrictionId = restrictionId;
        this.comment = comment;
        this.status = status;
    }

    public ClientGeneralRestriction(String ucid, String regulator, Long restrictionId,
            String status) {
        this.ucid = ucid;
        this.regulator = regulator;
        this.restrictionId = restrictionId;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUcid() {
        return ucid;
    }

    public void setUcid(String ucid) {
        this.ucid = ucid;
    }

    public String getRegulator() {
        return regulator;
    }

    public void setRegulator(String regulator) {
        this.regulator = regulator;
    }

    public Long getRestrictionId() {
        return restrictionId;
    }

    public void setRestrictionId(Long restrictionId) {
        this.restrictionId = restrictionId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ClientGeneralRestriction that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(ucid, that.ucid) && Objects.equals(
                regulator, that.regulator) && Objects.equals(restrictionId, that.restrictionId) && Objects.equals(
                        comment, that.comment) && Objects.equals(status, that.status) && Objects.equals(
                                cancellationReason, that.cancellationReason) && Objects.equals(failReason, that.failReason) && Objects.equals(
                                        createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ucid, regulator, restrictionId, comment, status, cancellationReason, failReason, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "ClientGeneralRestriction{" + "id=" + id + ", ucid='" + ucid + '\'' + ", regulator='" + regulator + '\'' + ", restrictionId=" + restrictionId + ", comment='" + comment + '\'' + ", status='" + status + '\'' + ", cancellationReason='" + cancellationReason + '\'' + ", failReason='" + failReason + '\'' + ", createdAt='" + createdAt + '\'' + ", updatedAt='" + updatedAt + '\'' + '}';
    }
}
