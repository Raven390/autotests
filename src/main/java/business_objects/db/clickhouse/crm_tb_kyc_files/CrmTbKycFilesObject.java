package business_objects.db.clickhouse.crm_tb_kyc_files;

import lombok.*;

import java.math.BigInteger;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CrmTbKycFilesObject {

    private Integer sourceIdSt;
    private Integer brandUid;
    private String brand;
    private String regulator;
    private Long userId;
    private String ucid;
    private BigInteger id;
    private BigInteger proofId;
    private BigInteger docFileId;
    private String fileName;
    private String filePath;
    private Integer fileTypeId;
    private String fileType;
    private String uploadTime;
    private String uploadTimeUtc;
    private String updateTime;
    private String updateTimeUtc;
    private Integer isDel;
    private String lastUpdated;
}
