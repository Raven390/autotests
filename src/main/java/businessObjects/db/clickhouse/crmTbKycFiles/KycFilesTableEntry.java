package businessObjects.db.clickhouse.crmTbKycFiles;

import java.util.Objects;

public class KycFilesTableEntry {

    public Integer id;
    public String uid;
    public Integer proofId;
    public Integer docFileId;
    public Integer userId;
    public String ucid;
    public String brand;
    public String regulator;
    public String fileName;
    public String filePath;
    public Integer fileTypeId;
    public String fileType;
    public String uploadTime;
    public String updateTime;
    public String lastUpdated;

    public KycFilesTableEntry() {
    }

    public KycFilesTableEntry(Integer id, String uid, Integer proofId, Integer docFileId, Integer userId, String ucid,
            String brand, String regulator, String fileName, String filePath, Integer fileTypeId, String fileType,
            String uploadTime, String updateTime, String lastUpdated) {
        this.id = id;
        this.uid = uid;
        this.proofId = proofId;
        this.docFileId = docFileId;
        this.userId = userId;
        this.ucid = ucid;
        this.brand = brand;
        this.regulator = regulator;
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileTypeId = fileTypeId;
        this.fileType = fileType;
        this.uploadTime = uploadTime;
        this.updateTime = updateTime;
        this.lastUpdated = lastUpdated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KycFilesTableEntry that = (KycFilesTableEntry) o;
        return Objects.equals(id, that.id) && Objects.equals(uid, that.uid) && Objects.equals(proofId, that.proofId) && Objects.equals(docFileId, that.docFileId) && Objects.equals(userId, that.userId) && Objects.equals(ucid, that.ucid) && Objects.equals(brand, that.brand) && Objects.equals(regulator, that.regulator) && Objects.equals(fileName, that.fileName) && Objects.equals(filePath, that.filePath) && Objects.equals(fileTypeId, that.fileTypeId) && Objects.equals(fileType, that.fileType) && Objects.equals(uploadTime, that.uploadTime) && Objects.equals(updateTime, that.updateTime) && Objects.equals(lastUpdated, that.lastUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uid, proofId, docFileId, userId, ucid, brand, regulator, fileName, filePath, fileTypeId, fileType, uploadTime, updateTime, lastUpdated);
    }

    @Override
    public String toString() {
        return "KycFilesTableEntry{" + "id=" + id + ", uid='" + uid + '\'' + ", proofId=" + proofId + ", docFileId=" + docFileId + ", userId=" + userId + ", ucid='" + ucid + '\'' + ", brand='" + brand + '\'' + ", regulator='" + regulator + '\'' + ", fileName='" + fileName + '\'' + ", filePath='" + filePath + '\'' + ", fileTypeId=" + fileTypeId + ", fileType='" + fileType + '\'' + ", uploadTime='" + uploadTime + '\'' + ", updateTime='" + updateTime + '\'' + ", lastUpdated='" + lastUpdated + '\'' + '}';
    }
}
