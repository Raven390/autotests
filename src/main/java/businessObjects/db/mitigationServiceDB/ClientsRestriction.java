package businessObjects.db.mitigationServiceDB;

import java.util.Objects;

public class ClientsRestriction {
    Long id;
    String ucid;
    String regulator;
    Long accountId;
    Long serverId;
    Long restrictionId;
    String applicationReason;
    String status;
    String cancellationReason;
    String failReason;
    String createdAt;
    String updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientsRestriction that = (ClientsRestriction) o;
        return Objects.equals(id, that.id) && Objects.equals(ucid, that.ucid) && Objects.equals(regulator, that.regulator) && Objects.equals(accountId, that.accountId) && Objects.equals(serverId, that.serverId) && Objects.equals(restrictionId, that.restrictionId) && Objects.equals(applicationReason, that.applicationReason) && Objects.equals(status, that.status) && Objects.equals(cancellationReason, that.cancellationReason) && Objects.equals(failReason, that.failReason) && Objects.equals(createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ucid, regulator, accountId, serverId, restrictionId, applicationReason, status, cancellationReason, failReason, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "ClientsRestriction{" +
                "id=" + id +
                ", ucid='" + ucid + '\'' +
                ", regulator='" + regulator + '\'' +
                ", accountId=" + accountId +
                ", serverId=" + serverId +
                ", restrictionId=" + restrictionId +
                ", applicationReason='" + applicationReason + '\'' +
                ", status='" + status + '\'' +
                ", cancellationReason='" + cancellationReason + '\'' +
                ", failReason='" + failReason + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public String getUcid() {
        return ucid;
    }

    public String getRegulator() {
        return regulator;
    }

    public Long getAccountId() {
        return accountId;
    }

    public Long getServerId() {
        return serverId;
    }

    public Long getRestrictionId() {
        return restrictionId;
    }

    public String getApplicationReason() {
        return applicationReason;
    }

    public String getStatus() {
        return status;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public String getFailReason() {
        return failReason;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}
