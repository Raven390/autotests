package business_objects.db.abuse_registry_db;

import java.sql.Timestamp;
import java.util.Objects;

public class AbuserHistory {
    private Integer id;
    private String ucid;
    private String fraudTypeCode;
    private String status;
    private String source;
    private String comment;
    private String modifiedByUser;
    private String modifiedBySystem;
    private Timestamp timestamp;
    private String fraudSubtypeCode;
    private String symbols;
    private String correlationType;
    private String correlationId;

    public String getCorrelationType() {
        return correlationType;
    }

    public void setCorrelationType(String correlationType) {
        this.correlationType = correlationType;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public AbuserHistory() {}

    public AbuserHistory(
            Integer id,
            String ucid,
            String fraudTypeCode,
            String status,
            String source,
            String comment,
            String modifiedByUser,
            String modifiedBySystem,
            Timestamp timestamp,
            String fraudSubtypeCode,
            String symbols) {
        this.id = id;
        this.ucid = ucid;
        this.fraudTypeCode = fraudTypeCode;
        this.status = status;
        this.source = source;
        this.comment = comment;
        this.modifiedByUser = modifiedByUser;
        this.modifiedBySystem = modifiedBySystem;
        this.timestamp = timestamp;
        this.fraudSubtypeCode = fraudSubtypeCode;
        this.symbols = symbols;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUcid() {
        return ucid;
    }

    public void setUcid(String ucid) {
        this.ucid = ucid;
    }

    public String getFraudTypeCode() {
        return fraudTypeCode;
    }

    public void setFraudTypeCode(String fraudTypeCode) {
        this.fraudTypeCode = fraudTypeCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getModifiedByUser() {
        return modifiedByUser;
    }

    public void setModifiedByUser(String modifiedByUser) {
        this.modifiedByUser = modifiedByUser;
    }

    public String getModifiedBySystem() {
        return modifiedBySystem;
    }

    public void setModifiedBySystem(String modifiedBySystem) {
        this.modifiedBySystem = modifiedBySystem;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getFraudSubtypeCode() {
        return fraudSubtypeCode;
    }

    public void setFraudSubtypeCode(String fraudSubtypeCode) {
        this.fraudSubtypeCode = fraudSubtypeCode;
    }

    public String getSymbols() {
        return symbols;
    }

    public void setSymbols(String symbols) {
        this.symbols = symbols;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AbuserHistory that = (AbuserHistory) o;
        return Objects.equals(id, that.id)
                && Objects.equals(ucid, that.ucid)
                && Objects.equals(fraudTypeCode, that.fraudTypeCode)
                && Objects.equals(status, that.status)
                && Objects.equals(source, that.source)
                && Objects.equals(comment, that.comment)
                && Objects.equals(modifiedByUser, that.modifiedByUser)
                && Objects.equals(modifiedBySystem, that.modifiedBySystem)
                && Objects.equals(timestamp, that.timestamp)
                && Objects.equals(fraudSubtypeCode, that.fraudSubtypeCode)
                && Objects.equals(symbols, that.symbols);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id,
                ucid,
                fraudTypeCode,
                status,
                source,
                comment,
                modifiedByUser,
                modifiedBySystem,
                timestamp,
                fraudSubtypeCode,
                symbols);
    }

    @Override
    public String toString() {
        return "AbuserHistory{" + "id=" + id + ", ucid='" + ucid + '\'' + ", fraudTypeCode='" + fraudTypeCode + '\''
                + ", status='" + status + '\'' + ", source='" + source + '\'' + ", comment='" + comment + '\''
                + ", modifiedByUser='" + modifiedByUser + '\'' + ", modifiedBySystem='" + modifiedBySystem + '\''
                + ", timestamp=" + timestamp + ", fraudSubtypeCode='" + fraudSubtypeCode + '\'' + ", symbols='"
                + symbols + '\'' + '}';
    }
}
