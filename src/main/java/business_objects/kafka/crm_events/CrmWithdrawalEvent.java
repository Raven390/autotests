package business_objects.kafka.crm_events;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class CrmWithdrawalEvent {
    @JsonProperty("accountType")
    private String accountType;

    @JsonProperty("binNumber")
    private String binNumber;

    @JsonProperty("brand")
    private String brand;

    @JsonProperty("checkName")
    private String checkName;

    @JsonProperty("clientId")
    private long clientId;

    @JsonProperty("eventDate")
    private String eventDate;

    @JsonProperty("expMonth")
    private String expMonth;

    @JsonProperty("expYear")
    private String expYear;

    @JsonProperty("fullName")
    private String fullName;

    @JsonProperty("id")
    private String id;

    @JsonProperty("merchantOrderId")
    private String merchantOrderId;

    @JsonProperty("mt4Account")
    private Integer mt4Account;

    @JsonProperty("paymentChannelCode")
    private String paymentChannelCode;

    @JsonProperty("paymentChannelName")
    private String paymentChannelName;

    @JsonProperty("paymentMethodCode")
    private String paymentMethodCode;

    @JsonProperty("platform")
    private String platform;

    @JsonProperty("regulator")
    private String regulator;

    @JsonProperty("schemaVersion")
    private String schemaVersion;

    @JsonProperty("type")
    private String type;

    @JsonProperty("withdrawalAmount")
    private Double withdrawalAmount;

    @JsonProperty("withdrawalApplicationTime")
    private String withdrawalApplicationTime;

    @JsonProperty("withdrawalCurrency")
    private String withdrawalCurrency;

    @JsonProperty("withdrawalId")
    private Long withdrawalId;

    public CrmWithdrawalEvent() {
    }

    public CrmWithdrawalEvent(
            String accountType, String binNumber, String brand, String checkName, long clientId, String eventDate,
            String expMonth, String expYear, String fullName, String id, String merchantOrderId, Integer mt4Account,
            String paymentChannelCode, String paymentChannelName, String paymentMethodCode, String platform,
            String regulator, String schemaVersion, String type, double withdrawalAmount,
            String withdrawalApplicationTime,
            String withdrawalCurrency, long withdrawalId) {
        this.accountType = accountType;
        this.binNumber = binNumber;
        this.brand = brand;
        this.checkName = checkName;
        this.clientId = clientId;
        this.eventDate = eventDate;
        this.expMonth = expMonth;
        this.expYear = expYear;
        this.fullName = fullName;
        this.id = id;
        this.merchantOrderId = merchantOrderId;
        this.mt4Account = mt4Account;
        this.paymentChannelCode = paymentChannelCode;
        this.paymentChannelName = paymentChannelName;
        this.paymentMethodCode = paymentMethodCode;
        this.platform = platform;
        this.regulator = regulator;
        this.schemaVersion = schemaVersion;
        this.type = type;
        this.withdrawalAmount = withdrawalAmount;
        this.withdrawalApplicationTime = withdrawalApplicationTime;
        this.withdrawalCurrency = withdrawalCurrency;
        this.withdrawalId = withdrawalId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getBinNumber() {
        return binNumber;
    }

    public void setBinNumber(String binNumber) {
        this.binNumber = binNumber;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCheckName() {
        return checkName;
    }

    public void setCheckName(String checkName) {
        this.checkName = checkName;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getExpMonth() {
        return expMonth;
    }

    public void setExpMonth(String expMonth) {
        this.expMonth = expMonth;
    }

    public String getExpYear() {
        return expYear;
    }

    public void setExpYear(String expYear) {
        this.expYear = expYear;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMerchantOrderId() {
        return merchantOrderId;
    }

    public void setMerchantOrderId(String merchantOrderId) {
        this.merchantOrderId = merchantOrderId;
    }

    public Integer getMt4Account() {
        return mt4Account;
    }

    public void setMt4Account(Integer mt4Account) {
        this.mt4Account = mt4Account;
    }

    public String getPaymentChannelCode() {
        return paymentChannelCode;
    }

    public void setPaymentChannelCode(String paymentChannelCode) {
        this.paymentChannelCode = paymentChannelCode;
    }

    public String getPaymentChannelName() {
        return paymentChannelName;
    }

    public void setPaymentChannelName(String paymentChannelName) {
        this.paymentChannelName = paymentChannelName;
    }

    public String getPaymentMethodCode() {
        return paymentMethodCode;
    }

    public void setPaymentMethodCode(String paymentMethodCode) {
        this.paymentMethodCode = paymentMethodCode;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getRegulator() {
        return regulator;
    }

    public void setRegulator(String regulator) {
        this.regulator = regulator;
    }

    public String getSchemaVersion() {
        return schemaVersion;
    }

    public void setSchemaVersion(String schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getWithdrawalAmount() {
        return withdrawalAmount;
    }

    public void setWithdrawalAmount(double withdrawalAmount) {
        this.withdrawalAmount = withdrawalAmount;
    }

    public String getWithdrawalApplicationTime() {
        return withdrawalApplicationTime;
    }

    public void setWithdrawalApplicationTime(String withdrawalApplicationTime) {
        this.withdrawalApplicationTime = withdrawalApplicationTime;
    }

    public String getWithdrawalCurrency() {
        return withdrawalCurrency;
    }

    public void setWithdrawalCurrency(String withdrawalCurrency) {
        this.withdrawalCurrency = withdrawalCurrency;
    }

    public long getWithdrawalId() {
        return withdrawalId;
    }

    public void setWithdrawalId(long withdrawalId) {
        this.withdrawalId = withdrawalId;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CrmWithdrawalEvent that)) return false;
        return clientId == that.clientId && mt4Account == that.mt4Account && Double.compare(withdrawalAmount, that.withdrawalAmount) == 0 && withdrawalId == that.withdrawalId && Objects.equals(
                accountType, that.accountType) && Objects.equals(binNumber, that.binNumber) && Objects.equals(
                        brand, that.brand) && Objects.equals(checkName, that.checkName) && Objects.equals(
                                eventDate, that.eventDate) && Objects.equals(expMonth, that.expMonth) && Objects.equals(
                                        expYear, that.expYear) && Objects.equals(fullName, that.fullName) && Objects.equals(id, that.id) && Objects.equals(
                                                merchantOrderId, that.merchantOrderId) && Objects.equals(paymentChannelCode, that.paymentChannelCode) && Objects.equals(
                                                        paymentChannelName, that.paymentChannelName) && Objects.equals(paymentMethodCode, that.paymentMethodCode) && Objects.equals(
                                                                platform, that.platform) && Objects.equals(regulator, that.regulator) && Objects.equals(
                                                                        schemaVersion, that.schemaVersion) && Objects.equals(type, that.type) && Objects.equals(
                                                                                withdrawalApplicationTime, that.withdrawalApplicationTime) && Objects.equals(withdrawalCurrency, that.withdrawalCurrency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountType, binNumber, brand, checkName, clientId, eventDate, expMonth, expYear, fullName, id, merchantOrderId, mt4Account, paymentChannelCode, paymentChannelName, paymentMethodCode, platform, regulator, schemaVersion, type, withdrawalAmount, withdrawalApplicationTime, withdrawalCurrency, withdrawalId);
    }

    @Override
    public String toString() {
        return "CrmWithdrawalEvent{" + "accountType='" + accountType + '\'' + ", binNumber='" + binNumber + '\'' + ", brand='" + brand + '\'' + ", checkName='" + checkName + '\'' + ", clientId=" + clientId + ", eventDate='" + eventDate + '\'' + ", expMonth='" + expMonth + '\'' + ", expYear='" + expYear + '\'' + ", fullName='" + fullName + '\'' + ", id='" + id + '\'' + ", merchantOrderId='" + merchantOrderId + '\'' + ", mt4Account=" + mt4Account + ", paymentChannelCode='" + paymentChannelCode + '\'' + ", paymentChannelName='" + paymentChannelName + '\'' + ", paymentMethodCode='" + paymentMethodCode + '\'' + ", platform='" + platform + '\'' + ", regulator='" + regulator + '\'' + ", schemaVersion='" + schemaVersion + '\'' + ", type='" + type + '\'' + ", withdrawalAmount=" + withdrawalAmount + ", withdrawalApplicationTime='" + withdrawalApplicationTime + '\'' + ", withdrawalCurrency='" + withdrawalCurrency + '\'' + ", withdrawalId=" + withdrawalId + '}';
    }
}
