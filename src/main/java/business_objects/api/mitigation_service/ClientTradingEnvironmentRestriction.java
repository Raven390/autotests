package business_objects.api.mitigation_service;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;
import java.util.Objects;

/**
 * Client trading environment restriction
 */
public class ClientTradingEnvironmentRestriction extends ClientRestriction {
    @JsonProperty("status")
    private RestrictionStatus status = null;

    @JsonProperty("accountId")
    private BigInteger accountId = null;

    @JsonProperty("serverId")
    private Integer serverId = null;

    @JsonProperty("level")
    private String level = null;

    public ClientTradingEnvironmentRestriction status(RestrictionStatus status) {
        this.status = status;
        return this;
    }

    /**
     * Get status
     * 
     * @return status
     **/
    public RestrictionStatus getStatus() {
        return status;
    }

    public void setStatus(RestrictionStatus status) {
        this.status = status;
    }

    public ClientTradingEnvironmentRestriction accountId(BigInteger accountId) {
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

    public ClientTradingEnvironmentRestriction serverId(Integer serverId) {
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

    public ClientTradingEnvironmentRestriction level(String level) {
        this.level = level;
        return this;
    }

    /**
     * Get level
     * 
     * @return level
     **/
    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClientTradingEnvironmentRestriction clientTradingEnvironmentRestriction = (ClientTradingEnvironmentRestriction) o;
        return Objects.equals(this.status, clientTradingEnvironmentRestriction.status) && Objects.equals(this.accountId, clientTradingEnvironmentRestriction.accountId) && Objects.equals(this.serverId, clientTradingEnvironmentRestriction.serverId) && Objects.equals(this.level, clientTradingEnvironmentRestriction.level) && super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, accountId, serverId, level, super.hashCode());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ClientTradingEnvironmentRestriction {\n");
        sb.append("    ").append(toIndentedString(super.toString())).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    accountId: ").append(toIndentedString(accountId)).append("\n");
        sb.append("    serverId: ").append(toIndentedString(serverId)).append("\n");
        sb.append("    level: ").append(toIndentedString(level)).append("\n");
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
