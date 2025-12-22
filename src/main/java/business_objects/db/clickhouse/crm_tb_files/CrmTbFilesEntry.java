package business_objects.db.clickhouse.crm_tb_files;

import java.math.BigInteger;
import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CrmTbFilesEntry {

    private Integer sourceIdSt;
    private Integer brandUid;
    private String brand;
    private String regulator;
    private Long userId;
    private Integer category;
    private String ucid;
    private BigInteger id;
    private String fileName;
    private String filePath;
    private OffsetDateTime uploadTime;
    private OffsetDateTime uploadTimeUtc;
    private OffsetDateTime updateTime;
    private OffsetDateTime updateTimeUtc;
    private Integer isDel;
    private OffsetDateTime lastUpdated;
    private String updateUser;
    private String updateIp;
    private String uploadIp;
}
