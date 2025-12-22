package business_objects.api.mitigation_service;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 * Client bybit restriction
 */
public class ClientBybitRestriction extends ClientRestriction {
    @JsonProperty("status")
    private RestrictionStatus status = null;

    @JsonProperty("accountId")
    private String accountId = null;

    public ClientBybitRestriction status(RestrictionStatus status) {
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

    public ClientBybitRestriction accountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    /**
     * (required for bybit restrictions): Account ID (Login) to which the restriction applies.
     *
     * @return accountId
     **/
    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClientBybitRestriction clientBybitRestriction = (ClientBybitRestriction) o;
        return Objects.equals(this.status, clientBybitRestriction.status)
                && Objects.equals(this.accountId, clientBybitRestriction.accountId)
                && super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, accountId, super.hashCode());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ClientBybitRestriction {\n");
        sb.append("    ").append(toIndentedString(super.toString())).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    accountId: ").append(toIndentedString(accountId)).append("\n");
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
