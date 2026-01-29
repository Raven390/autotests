package business_objects.api.mitigation_service;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigInteger;
import java.util.Objects;

/**
 * New trading restriction
 */
public class NewTradingRestriction extends NewRestriction {
    @JsonProperty("accountId")
    private BigInteger accountId = null;

    @JsonProperty("serverId")
    private Integer serverId = null;

    public NewTradingRestriction accountId(BigInteger accountId) {
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

    public NewTradingRestriction serverId(Integer serverId) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NewTradingRestriction newTradingRestriction = (NewTradingRestriction) o;
        return Objects.equals(this.accountId, newTradingRestriction.accountId)
                && Objects.equals(this.serverId, newTradingRestriction.serverId)
                && super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, serverId, super.hashCode());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class NewTradingRestriction {\n");
        sb.append("    ").append(toIndentedString(super.toString())).append("\n");
        sb.append("    accountId: ").append(toIndentedString(accountId)).append("\n");
        sb.append("    serverId: ").append(toIndentedString(serverId)).append("\n");
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
