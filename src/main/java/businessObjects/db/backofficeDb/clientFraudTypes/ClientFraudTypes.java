package businessObjects.db.backofficeDb.clientFraudTypes;

import java.util.Objects;

public class ClientFraudTypes {
    public String clientUcid;
    public Long fraudTypeId;

    public ClientFraudTypes() {
    }

    public ClientFraudTypes(Long fraudTypeId, String clientUcid) {
        this.fraudTypeId = fraudTypeId;
        this.clientUcid = clientUcid;
    }

    public String getClientUcid() {
        return clientUcid;
    }

    public Long getFraudTypeId() {
        return fraudTypeId;
    }

    public void setClientUcid(String clientUcid) {
        this.clientUcid = clientUcid;
    }

    public void setFraudTypeId(Long fraudTypeId) {
        this.fraudTypeId = fraudTypeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientFraudTypes that = (ClientFraudTypes) o;
        return Objects.equals(clientUcid, that.clientUcid) && Objects.equals(fraudTypeId, that.fraudTypeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientUcid, fraudTypeId);
    }

    @Override
    public String toString() {
        return "ClientFraudtypes{" + "clientId=" + clientUcid + ", fraudType=" + fraudTypeId + '}';
    }
}
