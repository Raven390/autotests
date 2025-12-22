package business_objects.api.mitigation_service;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Client trading restriction
 */
public class ClientTradingRestriction extends ClientRestriction {
    @JsonProperty("accountId")
    private BigInteger accountId = null;

    @JsonProperty("serverId")
    private Integer serverId = null;

    @JsonProperty("status")
    private List<StatusBySite> status = null;

    public ClientTradingRestriction accountId(BigInteger accountId) {
        this.accountId = accountId;
        return this;
    }

    /**
     * (required for trading restrictions): Account ID (Login) to which the restriction applies.
     *
     * @return accountId
     **/
    public BigInteger getAccountId() {
        return accountId;
    }

    public void setAccountId(BigInteger accountId) {
        this.accountId = accountId;
    }

    public ClientTradingRestriction serverId(Integer serverId) {
        this.serverId = serverId;
        return this;
    }

    /**
     * (required for trading restrictions): Server of the account.
     *
     * @return serverId
     **/
    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    public ClientTradingRestriction status(List<StatusBySite> status) {
        this.status = status;
        return this;
    }

    public ClientTradingRestriction addStatusItem(StatusBySite statusItem) {
        if (this.status == null) {
            this.status = new ArrayList<>();
        }
        this.status.add(statusItem);
        return this;
    }

    /**
     * Status of the restriction
     *
     * @return status
     **/
    public List<StatusBySite> getStatus() {
        return status;
    }

    public void setStatus(List<StatusBySite> status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClientTradingRestriction clientTradingRestriction = (ClientTradingRestriction) o;
        return Objects.equals(this.accountId, clientTradingRestriction.accountId)
                && Objects.equals(this.serverId, clientTradingRestriction.serverId)
                && Objects.equals(this.status, clientTradingRestriction.status)
                && super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, serverId, status, super.hashCode());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ClientTradingRestriction {\n");
        sb.append("    ").append(toIndentedString(super.toString())).append("\n");
        sb.append("    accountId: ").append(toIndentedString(accountId)).append("\n");
        sb.append("    serverId: ").append(toIndentedString(serverId)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
