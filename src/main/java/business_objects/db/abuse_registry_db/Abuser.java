package business_objects.db.abuse_registry_db;

import java.time.OffsetDateTime;

public class Abuser {
    private String ucid;
    private String status;
    private String comment;
    private String modifiedByUser;
    private String modifiedBySystem;
    private OffsetDateTime updatedAt;
    private OffsetDateTime createdAt;
    private boolean pendingProcessing;

    public Abuser(String ucid, String status, String comment, String modifiedByUser, String modifiedBySystem,
            OffsetDateTime updatedAt, OffsetDateTime createdAt, boolean pendingProcessing) {
        this.ucid = ucid;
        this.status = status;
        this.comment = comment;
        this.modifiedByUser = modifiedByUser;
        this.modifiedBySystem = modifiedBySystem;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.pendingProcessing = pendingProcessing;
    }

    public String getUcid() {
        return ucid;
    }

    public void setUcid(String ucid) {
        this.ucid = ucid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isPendingProcessing() {
        return pendingProcessing;
    }

    public void setPendingProcessing(boolean pendingProcessing) {
        this.pendingProcessing = pendingProcessing;
    }
}
