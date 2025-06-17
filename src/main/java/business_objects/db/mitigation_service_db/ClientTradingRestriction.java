package business_objects.db.mitigation_service_db;

import java.util.Objects;

public class ClientTradingRestriction {

    private Long id;
    private String ucid;
    private String regulator;
    private Long accountId;
    private Long serverId;
    private Long restrictionId;
    private String comment;
    private String cancellationReason;
    private String createdAt;
    private String updatedAt;

    public ClientTradingRestriction() {
    }

    public ClientTradingRestriction(Long id, String ucid, String regulator, Long accountId, Long serverId,
            Long restrictionId, String comment, String cancellationReason, String createdAt, String updatedAt) {
        this.setId(id);
        this.setUcid(ucid);
        this.setRegulator(regulator);
        this.setAccountId(accountId);
        this.setServerId(serverId);
        this.setRestrictionId(restrictionId);
        this.setComment(comment);
        this.setCancellationReason(cancellationReason);
        this.setCreatedAt(createdAt);
        this.setUpdatedAt(updatedAt);
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

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getServerId() {
        return serverId;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
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

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
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
        if (o == null || getClass() != o.getClass()) return false;
        ClientTradingRestriction that = (ClientTradingRestriction) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getUcid(), that.getUcid()) && Objects.equals(getRegulator(), that.getRegulator()) && Objects.equals(getAccountId(), that.getAccountId()) && Objects.equals(getServerId(), that.getServerId()) && Objects.equals(getRestrictionId(), that.getRestrictionId()) && Objects.equals(getComment(), that.getComment()) && Objects.equals(getCancellationReason(), that.getCancellationReason()) && Objects.equals(getCreatedAt(), that.getCreatedAt()) && Objects.equals(getUpdatedAt(), that.getUpdatedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUcid(), getRegulator(), getAccountId(), getServerId(), getRestrictionId(), getComment(), getCancellationReason(), getCreatedAt(), getUpdatedAt());
    }

    @Override
    public String toString() {
        return "ClientsRestrictionTrading{" + "id=" + getId() + ", ucid='" + getUcid() + '\'' + ", regulator='" + getRegulator() + '\'' + ", accountId=" + getAccountId() + ", serverId=" + getServerId() + ", restrictionId=" + getRestrictionId() + ", comment='" + getComment() + '\'' + ", cancellationReason='" + getCancellationReason() + '\'' + ", createdAt='" + getCreatedAt() + '\'' + ", updatedAt='" + getUpdatedAt() + '\'' + '}';
    }
}
