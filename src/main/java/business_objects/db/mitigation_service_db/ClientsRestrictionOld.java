package business_objects.db.mitigation_service_db;

import java.util.Objects;

public class ClientsRestrictionOld {

    public Long id;
    public String ucid;
    public String regulator;
    public Long accountId;
    public Long serverId;
    public Long restrictionId;
    public String applicationReason;
    public String status;
    public String cancellationReason;
    public String failReason;
    public String createdAt;
    public String updatedAt;

    public ClientsRestrictionOld() {
    }

    public ClientsRestrictionOld(Long id, String ucid, String regulator, Long accountId, Long serverId,
            Long restrictionId,
            String applicationReason, String status, String cancellationReason, String failReason, String createdAt,
            String updatedAt) {
        this.id = id;
        this.ucid = ucid;
        this.regulator = regulator;
        this.accountId = accountId;
        this.serverId = serverId;
        this.restrictionId = restrictionId;
        this.applicationReason = applicationReason;
        this.status = status;
        this.cancellationReason = cancellationReason;
        this.failReason = failReason;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public ClientsRestrictionOld(String ucid, String regulator, Long restrictionId, String applicationReason,
            String status) {
        this.ucid = ucid;
        this.regulator = regulator;
        this.restrictionId = restrictionId;
        this.applicationReason = applicationReason;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientsRestrictionOld that = (ClientsRestrictionOld) o;
        return Objects.equals(ucid, that.ucid) && Objects.equals(regulator, that.regulator) && Objects.equals(restrictionId, that.restrictionId) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ucid, regulator, restrictionId, status);
    }

    @Override
    public String toString() {
        return "ClientsRestriction{" + "id=" + id + ", ucid='" + ucid + '\'' + ", regulator='" + regulator + '\'' + ", accountId=" + accountId + ", serverId=" + serverId + ", restrictionId=" + restrictionId + ", applicationReason='" + applicationReason + '\'' + ", status='" + status + '\'' + ", cancellationReason='" + cancellationReason + '\'' + ", failReason='" + failReason + '\'' + ", createdAt='" + createdAt + '\'' + ", updatedAt='" + updatedAt + '\'' + '}';
    }
}
