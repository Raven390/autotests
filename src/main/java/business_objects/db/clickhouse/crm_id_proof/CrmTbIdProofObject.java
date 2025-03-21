package business_objects.db.clickhouse.crm_id_proof;

import java.util.Objects;

public class CrmTbIdProofObject {

    protected Integer id;
    protected String uid;
    protected String ucid;
    protected Integer userId;
    protected String brand;
    protected String regulator;
    protected String createTime;
    protected String createTimeUtc;
    protected String updateTime;
    protected String updateTimeUtc;
    protected String firstName;
    protected String middleName;
    protected String lastName;
    protected String dateOfBirth;
    protected Integer statusId;
    protected String status;
    protected String auditor;
    protected String notes;
    protected String reason;
    protected String documentType;
    protected String documentNumber;
    protected Integer nationalityId;
    protected String nationality;
    protected Integer auditId;
    protected String auditType;
    protected Integer fileTypeId;
    protected String fileType;
    protected String lastUpdated;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CrmTbIdProofObject that = (CrmTbIdProofObject) o;
        return Objects.equals(id, that.id) && Objects.equals(uid, that.uid) && Objects.equals(ucid, that.ucid) && Objects.equals(userId, that.userId) && Objects.equals(brand, that.brand) && Objects.equals(regulator, that.regulator) && Objects.equals(createTime, that.createTime) && Objects.equals(createTimeUtc, that.createTimeUtc) && Objects.equals(updateTime, that.updateTime) && Objects.equals(updateTimeUtc, that.updateTimeUtc) && Objects.equals(firstName, that.firstName) && Objects.equals(middleName, that.middleName) && Objects.equals(lastName, that.lastName) && Objects.equals(dateOfBirth, that.dateOfBirth) && Objects.equals(statusId, that.statusId) && Objects.equals(status, that.status) && Objects.equals(auditor, that.auditor) && Objects.equals(notes, that.notes) && Objects.equals(reason, that.reason) && Objects.equals(documentType, that.documentType) && Objects.equals(documentNumber, that.documentNumber) && Objects.equals(nationalityId, that.nationalityId) && Objects.equals(nationality, that.nationality) && Objects.equals(auditId, that.auditId) && Objects.equals(auditType, that.auditType) && Objects.equals(fileTypeId, that.fileTypeId) && Objects.equals(fileType, that.fileType) && Objects.equals(lastUpdated, that.lastUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uid, ucid, userId, brand, regulator, createTime, createTimeUtc, updateTime, updateTimeUtc, firstName, middleName, lastName, dateOfBirth, statusId, status, auditor, notes, reason, documentType, documentNumber, nationalityId, nationality, auditId, auditType, fileTypeId, fileType, lastUpdated);
    }

    @Override
    public String toString() {
        return "CrmTbIdProofObject{" + "id=" + id + ", uid='" + uid + '\'' + ", ucid='" + ucid + '\'' + ", userId=" + userId + ", brand='" + brand + '\'' + ", regulator='" + regulator + '\'' + ", createTime='" + createTime + '\'' + ", createTimeUtc='" + createTimeUtc + '\'' + ", updateTime='" + updateTime + '\'' + ", updateTimeUtc='" + updateTimeUtc + '\'' + ", firstName='" + firstName + '\'' + ", middleName='" + middleName + '\'' + ", lastName='" + lastName + '\'' + ", dateOfBirth='" + dateOfBirth + '\'' + ", statusId=" + statusId + ", status='" + status + '\'' + ", auditor='" + auditor + '\'' + ", notes='" + notes + '\'' + ", reason='" + reason + '\'' + ", documentType='" + documentType + '\'' + ", documentNumber='" + documentNumber + '\'' + ", nationalityId=" + nationalityId + ", nationality='" + nationality + '\'' + ", auditId=" + auditId + ", auditType='" + auditType + '\'' + ", fileTypeId=" + fileTypeId + ", fileType='" + fileType + '\'' + ", lastUpdated='" + lastUpdated + '\'' + '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUcid() {
        return ucid;
    }

    public void setUcid(String ucid) {
        this.ucid = ucid;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getRegulator() {
        return regulator;
    }

    public void setRegulator(String regulator) {
        this.regulator = regulator;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateTimeUtc() {
        return createTimeUtc;
    }

    public void setCreateTimeUtc(String createTimeUtc) {
        this.createTimeUtc = createTimeUtc;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateTimeUtc() {
        return updateTimeUtc;
    }

    public void setUpdateTimeUtc(String updateTimeUtc) {
        this.updateTimeUtc = updateTimeUtc;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public Integer getNationalityId() {
        return nationalityId;
    }

    public void setNationalityId(Integer nationalityId) {
        this.nationalityId = nationalityId;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Integer getAuditId() {
        return auditId;
    }

    public void setAuditId(Integer auditId) {
        this.auditId = auditId;
    }

    public String getAuditType() {
        return auditType;
    }

    public void setAuditType(String auditType) {
        this.auditType = auditType;
    }

    public Integer getFileTypeId() {
        return fileTypeId;
    }

    public void setFileTypeId(Integer fileTypeId) {
        this.fileTypeId = fileTypeId;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
