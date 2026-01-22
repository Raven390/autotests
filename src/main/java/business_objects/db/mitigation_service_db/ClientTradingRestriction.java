package business_objects.db.mitigation_service_db;

import java.util.Objects;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ClientTradingRestriction that = (ClientTradingRestriction) o;
        return Objects.equals(getId(), that.getId())
                && Objects.equals(getUcid(), that.getUcid())
                && Objects.equals(getRegulator(), that.getRegulator())
                && Objects.equals(getAccountId(), that.getAccountId())
                && Objects.equals(getServerId(), that.getServerId())
                && Objects.equals(getRestrictionId(), that.getRestrictionId())
                && Objects.equals(getComment(), that.getComment())
                && Objects.equals(getCancellationReason(), that.getCancellationReason())
                && Objects.equals(getCreatedAt(), that.getCreatedAt())
                && Objects.equals(getUpdatedAt(), that.getUpdatedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getId(),
                getUcid(),
                getRegulator(),
                getAccountId(),
                getServerId(),
                getRestrictionId(),
                getComment(),
                getCancellationReason(),
                getCreatedAt(),
                getUpdatedAt());
    }

    @Override
    public String toString() {
        return "ClientsRestrictionTrading{" + "id=" + getId() + ", ucid='" + getUcid() + '\'' + ", regulator='"
                + getRegulator() + '\'' + ", accountId=" + getAccountId() + ", serverId=" + getServerId()
                + ", restrictionId=" + getRestrictionId() + ", comment='" + getComment() + '\''
                + ", cancellationReason='" + getCancellationReason() + '\'' + ", createdAt='" + getCreatedAt() + '\''
                + ", updatedAt='" + getUpdatedAt() + '\'' + '}';
    }
}
