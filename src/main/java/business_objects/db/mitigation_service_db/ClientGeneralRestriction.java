package business_objects.db.mitigation_service_db;

import java.util.Objects;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    private String correlationType;
    private String correlationId;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ClientGeneralRestriction that)) return false;
        return Objects.equals(id, that.id)
                && Objects.equals(ucid, that.ucid)
                && Objects.equals(regulator, that.regulator)
                && Objects.equals(restrictionId, that.restrictionId)
                && Objects.equals(comment, that.comment)
                && Objects.equals(status, that.status)
                && Objects.equals(cancellationReason, that.cancellationReason)
                && Objects.equals(failReason, that.failReason)
                && Objects.equals(createdAt, that.createdAt)
                && Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id,
                ucid,
                regulator,
                restrictionId,
                comment,
                status,
                cancellationReason,
                failReason,
                createdAt,
                updatedAt);
    }

    @Override
    public String toString() {
        return "ClientGeneralRestriction{" + "id=" + id + ", ucid='" + ucid + '\'' + ", regulator='" + regulator + '\''
                + ", restrictionId=" + restrictionId + ", comment='" + comment + '\'' + ", status='" + status + '\''
                + ", cancellationReason='" + cancellationReason + '\'' + ", failReason='" + failReason + '\''
                + ", createdAt='" + createdAt + '\'' + ", updatedAt='" + updatedAt + '\'' + '}';
    }
}
