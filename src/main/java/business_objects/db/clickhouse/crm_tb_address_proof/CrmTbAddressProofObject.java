package business_objects.db.clickhouse.crm_tb_address_proof;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CrmTbAddressProofObject {

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
    private String postcode;
    private Integer countryId;
    private String country;
    private String state;
    private String city;
    private String address;
    private Integer statusId;
    private String status;
    private String auditor;
    private String notes;
    private Integer pendingReason;
    private Integer rejectReason;
    private String reason;
    private String translateAddress;
    private Integer auditId;
    private String auditType;
    private Integer fileTypeId;
    private String fileType;
    private Integer isDel;
    private String lastUpdated;
}
