package businessObjects.db.backofficeDb.clientFraudTypes;

import java.util.Objects;

public class ClientFraudTypes {
    public Long clientId;
    public Long fraudTypeId;

    public ClientFraudTypes() {
    }

    public ClientFraudTypes(Long fraudTypeId, Long clientId) {
        this.fraudTypeId = fraudTypeId;
        this.clientId = clientId;
    }

    public Long getClientId() {
        return clientId;
    }

    public Long getFraudTypeId() {
        return fraudTypeId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public void setFraudTypeId(Long fraudTypeId) {
        this.fraudTypeId = fraudTypeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientFraudTypes that = (ClientFraudTypes) o;
        return Objects.equals(clientId, that.clientId) && Objects.equals(fraudTypeId, that.fraudTypeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, fraudTypeId);
    }

    @Override
    public String toString() {
        return "ClientFraudtypes{" + "clientId=" + clientId + ", fraudType=" + fraudTypeId + '}';
    }
}
