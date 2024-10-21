package helpers.kafka.crmEvents;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CrmDepositEvent {

    @JsonProperty("schemaVersion")
    public String schemaVersion;

    @JsonProperty("brand")
    public String brand;

    @JsonProperty("regulator")
    public String regulator;

    @JsonProperty("type")
    public String type;

    @JsonProperty("id")
    public String id;

    @JsonProperty("depositId")
    public String depositId;

    @JsonProperty("eventDate")
    public String eventDate;

    @JsonProperty("depositCurrency")
    public String depositCurrency;

    @JsonProperty("depositAmount")
    public Double depositAmount;

    @JsonProperty("clientId")
    public Integer clientId;

    @JsonProperty("paymentMethodCode")
    public String paymentMethodCode;

    @JsonProperty("paymentChannelCode")
    public String paymentChannelCode;

    @JsonProperty("paymentChannelName")
    public String paymentChannelName;

    @JsonProperty("cardHash")
    public String cardHash;

    @JsonProperty("binNumber")
    public String binNumber;

    @JsonProperty("expDate")
    public String expDate;

    @JsonProperty("fullName")
    public String fullName;

    @JsonProperty("iban")
    public String iban;

    @JsonProperty("bankAccountHolderName")
    public String bankAccountHolderName;

    @JsonProperty("bankName")
    public String bankName;

    @JsonProperty("walletAddress")
    public String walletAddress;

    public static CrmDepositEvent getDepositEvent(String schemaVersion, String brand, String regulator, String type, String id, String depositId, String eventDate, String depositCurrency, Double depositAmount, Integer clientId, String paymentMethodCode, String paymentChannelCode, String paymentChannelName, String cardHash, String binNumber, String expDate, String fullName, String iban, String bankAccountHolderName, String bankName, String walletAddress) {
        CrmDepositEvent event = new CrmDepositEvent();
        event.schemaVersion = schemaVersion;
        event.brand = brand;
        event.regulator = regulator;
        event.type = type;
        event.id = id;
        event.depositId = depositId;
        event.eventDate = eventDate;
        event.depositCurrency = depositCurrency;
        event.depositAmount = depositAmount;
        event.clientId = clientId;
        event.paymentMethodCode = paymentMethodCode;
        event.paymentChannelCode = paymentChannelCode;
        event.paymentChannelName = paymentChannelName;
        event.cardHash = cardHash;
        event.binNumber = binNumber;
        event.expDate = expDate;
        event.fullName = fullName;
        event.iban = iban;
        event.bankAccountHolderName = bankAccountHolderName;
        event.bankName = bankName;
        event.walletAddress = walletAddress;
        return event;
    }
}
