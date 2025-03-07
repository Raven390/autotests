package businessObjects.db.backofficeDb.clientsFraudTypes;

import java.sql.Timestamp;
import java.util.Objects;

public class ClientsFraudTypes {
    public String clientUcid;
    public Long fraudTypeId;
    public Boolean isDeleted;
    public Timestamp updatedAt;

    public ClientsFraudTypes() {
    }

    public ClientsFraudTypes(Long fraudTypeId, String clientUcid) {
        this.fraudTypeId = fraudTypeId;
        this.clientUcid = clientUcid;
    }

    public ClientsFraudTypes(String clientUcid, Long fraudTypeId, Boolean isDeleted, Timestamp updatedAt) {
        this.clientUcid = clientUcid;
        this.fraudTypeId = fraudTypeId;
        this.isDeleted = isDeleted;
        this.updatedAt = updatedAt;
    }

    public String getClientUcid() {
        return clientUcid;
    }

    public void setClientUcid(String clientUcid) {
        this.clientUcid = clientUcid;
    }

    public Long getFraudTypeId() {
        return fraudTypeId;
    }

    public void setFraudTypeId(Long fraudTypeId) {
        this.fraudTypeId = fraudTypeId;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ClientsFraudTypes that = (ClientsFraudTypes) o;
        return Objects.equals(clientUcid, that.clientUcid) && Objects.equals(fraudTypeId, that.fraudTypeId) && Objects.equals(isDeleted, that.isDeleted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientUcid, fraudTypeId, isDeleted);
    }

    @Override
    public String toString() {
        return "ClientFraudTypes{" + "clientUcid='" + clientUcid + '\'' + ", fraudTypeId=" + fraudTypeId + ", isDeleted=" + isDeleted + ", updatedAt='" + updatedAt + '\'' + '}';
    }
}
