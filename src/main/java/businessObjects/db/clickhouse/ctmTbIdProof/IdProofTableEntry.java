package businessObjects.db.clickhouse.ctmTbIdProof;

import java.util.Objects;

public class IdProofTableEntry {

    public Integer id;
    public String uid;
    public String ucid;
    public Integer userId;
    public String brand;
    public String regulator;
    public String createTime;
    public String updateTime;
    public String firstName;
    public String middleName;
    public String lastName;
    public String dateOfBirth;
    public Integer statusId;
    public String status;
    public String auditor;
    public String notes;
    public String reason;
    public String documentType;
    public String documentNumber;
    public Integer nationalityId;
    public String nationality;
    public Integer auditId;
    public String auditType;
    public Integer fileTypeId;
    public String fileType;
    public String lastUpdated;


    public IdProofTableEntry() {
    }

    public IdProofTableEntry(Integer id, String uid, String ucid, Integer userId, String brand, String regulator,
            String createTime, String updateTime, String firstName, String middleName, String lastName,
            String dateOfBirth, Integer statusId, String status, String auditor, String notes, String reason,
            String documentType, String documentNumber, Integer nationalityId, String nationality, Integer auditId,
            String auditType, Integer fileTypeId, String fileType, String lastUpdated) {
        this.id = id;
        this.uid = uid;
        this.ucid = ucid;
        this.userId = userId;
        this.brand = brand;
        this.regulator = regulator;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.statusId = statusId;
        this.status = status;
        this.auditor = auditor;
        this.notes = notes;
        this.reason = reason;
        this.documentType = documentType;
        this.documentNumber = documentNumber;
        this.nationalityId = nationalityId;
        this.nationality = nationality;
        this.auditId = auditId;
        this.auditType = auditType;
        this.fileTypeId = fileTypeId;
        this.fileType = fileType;
        this.lastUpdated = lastUpdated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IdProofTableEntry that = (IdProofTableEntry) o;
        return Objects.equals(id, that.id) && Objects.equals(uid, that.uid) && Objects.equals(ucid, that.ucid) && Objects.equals(userId, that.userId) && Objects.equals(brand, that.brand) && Objects.equals(regulator, that.regulator) && Objects.equals(createTime, that.createTime) && Objects.equals(updateTime, that.updateTime) && Objects.equals(firstName, that.firstName) && Objects.equals(middleName, that.middleName) && Objects.equals(lastName, that.lastName) && Objects.equals(dateOfBirth, that.dateOfBirth) && Objects.equals(statusId, that.statusId) && Objects.equals(status, that.status) && Objects.equals(auditor, that.auditor) && Objects.equals(notes, that.notes) && Objects.equals(reason, that.reason) && Objects.equals(documentType, that.documentType) && Objects.equals(documentNumber, that.documentNumber) && Objects.equals(nationalityId, that.nationalityId) && Objects.equals(nationality, that.nationality) && Objects.equals(auditId, that.auditId) && Objects.equals(auditType, that.auditType) && Objects.equals(fileTypeId, that.fileTypeId) && Objects.equals(fileType, that.fileType) && Objects.equals(lastUpdated, that.lastUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uid, ucid, userId, brand, regulator, createTime, updateTime, firstName, middleName, lastName, dateOfBirth, statusId, status, auditor, notes, reason, documentType, documentNumber, nationalityId, nationality, auditId, auditType, fileTypeId, fileType, lastUpdated);
    }

    @Override
    public String toString() {
        return "IdProofTableEntry{" + "id=" + id + ", uid='" + uid + '\'' + ", ucid='" + ucid + '\'' + ", userId=" + userId + ", brand='" + brand + '\'' + ", regulator='" + regulator + '\'' + ", createTime='" + createTime + '\'' + ", updateTime='" + updateTime + '\'' + ", firstName='" + firstName + '\'' + ", middleName='" + middleName + '\'' + ", lastName='" + lastName + '\'' + ", dateOfBirth='" + dateOfBirth + '\'' + ", statusId=" + statusId + ", status='" + status + '\'' + ", auditor='" + auditor + '\'' + ", notes='" + notes + '\'' + ", reason='" + reason + '\'' + ", documentType='" + documentType + '\'' + ", documentNumber='" + documentNumber + '\'' + ", nationalityId=" + nationalityId + ", nationality='" + nationality + '\'' + ", auditId=" + auditId + ", auditType='" + auditType + '\'' + ", fileTypeId=" + fileTypeId + ", fileType='" + fileType + '\'' + ", lastUpdated='" + lastUpdated + '\'' + '}';
    }
}
