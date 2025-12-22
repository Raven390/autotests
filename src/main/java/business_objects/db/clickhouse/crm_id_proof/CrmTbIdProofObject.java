package business_objects.db.clickhouse.crm_id_proof;

import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CrmTbIdProofObject {

    private Integer sourceIdSt;
    private Integer brandUid;
    private String brand;
    private String regulator;
    private Long userId;
    private String ucid;
    private BigInteger id;
    private String createTime;
    private String createTimeUtc;
    private String updateTime;
    private String updateTimeUtc;
    private String firstName;
    private String middleName;
    private String lastName;
    private String dateOfBirth;
    private Integer statusId;
    private String status;
    private String auditor;
    private String notes;
    private Integer pendingReason;
    private Integer rejectReason;
    private String reason;
    private String documentType;
    private String documentNumber;
    private Integer nationalityId;
    private String nationality;
    private Integer auditId;
    private String auditType;
    private Integer fileTypeId;
    private String fileType;
    private Integer isDel;
    private String lastUpdated;
}
