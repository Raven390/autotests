package businessObjects.db.backofficeDb.client;

import java.util.Objects;

public class Client {

    public Integer id;
    public String ucid;
    public Integer crmId;
    public String clientBrand;
    public String clientRegulator;
    public String country;
    public String countryCode;
    public String updatedAt;
    public String assignedUserId;

    public Client() {
    }

    public Client(Integer id, String ucid, Integer crmId, String clientBrand, String clientRegulator, String country, String countryCode, String updatedAt, String assignedUserId) {
        this.id = id;
        this.ucid = ucid;
        this.crmId = crmId;
        this.clientBrand = clientBrand;
        this.clientRegulator = clientRegulator;
        this.country = country;
        this.countryCode = countryCode;
        this.updatedAt = updatedAt;
        this.assignedUserId = assignedUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(id, client.id) && Objects.equals(ucid, client.ucid) && Objects.equals(crmId, client.crmId) && Objects.equals(clientBrand, client.clientBrand) && Objects.equals(clientRegulator, client.clientRegulator) && Objects.equals(country, client.country) && Objects.equals(countryCode, client.countryCode) && Objects.equals(updatedAt, client.updatedAt) && Objects.equals(assignedUserId, client.assignedUserId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ucid, crmId, clientBrand, clientRegulator, country, countryCode, updatedAt, assignedUserId);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", ucid='" + ucid + '\'' +
                ", crmId=" + crmId +
                ", clientBrand='" + clientBrand + '\'' +
                ", clientRegulator='" + clientRegulator + '\'' +
                ", country='" + country + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", assignedUserId='" + assignedUserId + '\'' +
                '}';
    }
}
