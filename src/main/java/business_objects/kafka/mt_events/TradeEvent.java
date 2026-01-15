package business_objects.kafka.mt_events;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TradeEvent {

    @JsonProperty("tradeId")
    public long tradeId;

    @JsonProperty("symbol")
    public String symbol;

    @JsonProperty("id")
    public String id;

    @JsonProperty("serverId")
    public int serverId;

    @JsonProperty("tradingAccount")
    public long tradingAccount;

    @JsonProperty("volume")
    public double volume;

    @JsonProperty("openTime")
    public String openTime;

    @JsonProperty("openTimeUtc")
    public String openTimeUtc;

    @JsonProperty("closeTime")
    public String closeTime;

    @JsonProperty("closeTimeUtc")
    public String closeTimeUtc;

    @JsonProperty("equity")
    public double equity;

    @JsonProperty("balance")
    public double balance;

    @JsonProperty("leverage")
    public double leverage;

    @JsonProperty("margin")
    public double margin;

    @JsonProperty("freeMargin")
    public double freeMargin;

    @JsonProperty("type")
    public String type;

    @JsonProperty("eventDate")
    public String eventDate;

    @JsonProperty("initialEventTime")
    public String initialEventTime;

    @JsonProperty("metadata")
    public TradeEventMetadata metadata;
}
