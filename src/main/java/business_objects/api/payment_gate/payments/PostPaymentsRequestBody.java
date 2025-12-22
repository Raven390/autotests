package business_objects.api.payment_gate.payments;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.Objects;

public class PostPaymentsRequestBody {

    @JsonProperty("schemaVersion")
    private String schemaVersion;

    @JsonProperty("brand")
    private String brand;

    @JsonProperty("regulator")
    private String regulator;

    @JsonProperty("type")
    private String type;

    @JsonProperty("id")
    private String id;

    @JsonProperty("clientId")
    private Integer clientId;

    @JsonProperty("withdrawalId")
    private Integer withdrawalId;

    @JsonProperty("merchantOrderId")
    private String merchantOrderId;

    @JsonProperty("mt4Account")
    private Integer mt4Account;

    @JsonProperty("accountType")
    private String accountType;

    @JsonProperty("platform")
    private String platform;

    @JsonProperty("checkName")
    private String checkName;

    @JsonProperty("statusId")
    private Integer statusId;

    @JsonProperty("status")
    private String status;

    // Keep date/time as strings to avoid timezone/format pitfalls unless project enforces Java Time
    @JsonProperty("eventDate")
    private String eventDate;

    @JsonProperty("withdrawalApplicationTime")
    private String withdrawalApplicationTime;

    @JsonProperty("withdrawalCurrency")
    private String withdrawalCurrency;

    @JsonProperty("withdrawalAmount")
    private BigDecimal withdrawalAmount;

    @JsonProperty("withdrawalAmountUSD")
    private BigDecimal withdrawalAmountUSD;

    @JsonProperty("paymentMethodCode")
    private String paymentMethodCode;

    @JsonProperty("paymentChannelCode")
    private Integer paymentChannelCode;

    @JsonProperty("paymentChannelName")
    private String paymentChannelName;

    @JsonProperty("paymentTypeName")
    private String paymentTypeName;

    @JsonProperty("paymentTypeCode")
    private Integer paymentTypeCode;

    @JsonProperty("ip")
    private String ip;

    @JsonProperty("statusKYC")
    private String statusKYC;

    @JsonProperty("cost")
    private BigDecimal cost;

    @JsonProperty("card")
    private Card card;

    public PostPaymentsRequestBody() {}

    public PostPaymentsRequestBody(
            String schemaVersion,
            String brand,
            String regulator,
            String type,
            String id,
            Integer clientId,
            Integer withdrawalId,
            String merchantOrderId,
            Integer mt4Account,
            String accountType,
            String platform,
            String checkName,
            Integer statusId,
            String status,
            String eventDate,
            String withdrawalApplicationTime,
            String withdrawalCurrency,
            BigDecimal withdrawalAmount,
            BigDecimal withdrawalAmountUSD,
            String paymentMethodCode,
            Integer paymentChannelCode,
            String paymentChannelName,
            String paymentTypeName,
            Integer paymentTypeCode,
            String ip,
            String statusKYC,
            BigDecimal cost,
            Card card) {
        this.schemaVersion = schemaVersion;
        this.brand = brand;
        this.regulator = regulator;
        this.type = type;
        this.id = id;
        this.clientId = clientId;
        this.withdrawalId = withdrawalId;
        this.merchantOrderId = merchantOrderId;
        this.mt4Account = mt4Account;
        this.accountType = accountType;
        this.platform = platform;
        this.checkName = checkName;
        this.statusId = statusId;
        this.status = status;
        this.eventDate = eventDate;
        this.withdrawalApplicationTime = withdrawalApplicationTime;
        this.withdrawalCurrency = withdrawalCurrency;
        this.withdrawalAmount = withdrawalAmount;
        this.withdrawalAmountUSD = withdrawalAmountUSD;
        this.paymentMethodCode = paymentMethodCode;
        this.paymentChannelCode = paymentChannelCode;
        this.paymentChannelName = paymentChannelName;
        this.paymentTypeName = paymentTypeName;
        this.paymentTypeCode = paymentTypeCode;
        this.ip = ip;
        this.statusKYC = statusKYC;
        this.cost = cost;
        this.card = card;
    }

    public String getSchemaVersion() {
        return schemaVersion;
    }

    public void setSchemaVersion(String schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getRegulator() {
        return regulator;
    }

    public void setRegulator(String regulator) {
        this.regulator = regulator;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public Integer getWithdrawalId() {
        return withdrawalId;
    }

    public void setWithdrawalId(Integer withdrawalId) {
        this.withdrawalId = withdrawalId;
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

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getCheckName() {
        return checkName;
    }

    public void setCheckName(String checkName) {
        this.checkName = checkName;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
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

    public BigDecimal getWithdrawalAmount() {
        return withdrawalAmount;
    }

    public void setWithdrawalAmount(BigDecimal withdrawalAmount) {
        this.withdrawalAmount = withdrawalAmount;
    }

    public BigDecimal getWithdrawalAmountUSD() {
        return withdrawalAmountUSD;
    }

    public void setWithdrawalAmountUSD(BigDecimal withdrawalAmountUSD) {
        this.withdrawalAmountUSD = withdrawalAmountUSD;
    }

    public String getPaymentMethodCode() {
        return paymentMethodCode;
    }

    public void setPaymentMethodCode(String paymentMethodCode) {
        this.paymentMethodCode = paymentMethodCode;
    }

    public Integer getPaymentChannelCode() {
        return paymentChannelCode;
    }

    public void setPaymentChannelCode(Integer paymentChannelCode) {
        this.paymentChannelCode = paymentChannelCode;
    }

    public String getPaymentChannelName() {
        return paymentChannelName;
    }

    public void setPaymentChannelName(String paymentChannelName) {
        this.paymentChannelName = paymentChannelName;
    }

    public String getPaymentTypeName() {
        return paymentTypeName;
    }

    public void setPaymentTypeName(String paymentTypeName) {
        this.paymentTypeName = paymentTypeName;
    }

    public Integer getPaymentTypeCode() {
        return paymentTypeCode;
    }

    public void setPaymentTypeCode(Integer paymentTypeCode) {
        this.paymentTypeCode = paymentTypeCode;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getStatusKYC() {
        return statusKYC;
    }

    public void setStatusKYC(String statusKYC) {
        this.statusKYC = statusKYC;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostPaymentsRequestBody that)) return false;
        return Objects.equals(schemaVersion, that.schemaVersion)
                && Objects.equals(brand, that.brand)
                && Objects.equals(regulator, that.regulator)
                && Objects.equals(type, that.type)
                && Objects.equals(id, that.id)
                && Objects.equals(clientId, that.clientId)
                && Objects.equals(withdrawalId, that.withdrawalId)
                && Objects.equals(merchantOrderId, that.merchantOrderId)
                && Objects.equals(mt4Account, that.mt4Account)
                && Objects.equals(accountType, that.accountType)
                && Objects.equals(platform, that.platform)
                && Objects.equals(checkName, that.checkName)
                && Objects.equals(statusId, that.statusId)
                && Objects.equals(status, that.status)
                && Objects.equals(eventDate, that.eventDate)
                && Objects.equals(withdrawalApplicationTime, that.withdrawalApplicationTime)
                && Objects.equals(withdrawalCurrency, that.withdrawalCurrency)
                && bigDecimalEquals(withdrawalAmount, that.withdrawalAmount)
                && bigDecimalEquals(withdrawalAmountUSD, that.withdrawalAmountUSD)
                && Objects.equals(paymentMethodCode, that.paymentMethodCode)
                && Objects.equals(paymentChannelCode, that.paymentChannelCode)
                && Objects.equals(paymentChannelName, that.paymentChannelName)
                && Objects.equals(paymentTypeName, that.paymentTypeName)
                && Objects.equals(paymentTypeCode, that.paymentTypeCode)
                && Objects.equals(ip, that.ip)
                && Objects.equals(statusKYC, that.statusKYC)
                && bigDecimalEquals(cost, that.cost)
                && Objects.equals(card, that.card);
    }

    private static boolean bigDecimalEquals(BigDecimal a, BigDecimal b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return a.compareTo(b) == 0;
    }

    @Override
    public int hashCode() {
        // Use normalized BigDecimal to keep hashCode consistent with equals
        return Objects.hash(
                schemaVersion,
                brand,
                regulator,
                type,
                id,
                clientId,
                withdrawalId,
                merchantOrderId,
                mt4Account,
                accountType,
                platform,
                checkName,
                statusId,
                status,
                eventDate,
                withdrawalApplicationTime,
                withdrawalCurrency,
                normalizeBigDecimal(withdrawalAmount),
                normalizeBigDecimal(withdrawalAmountUSD),
                paymentMethodCode,
                paymentChannelCode,
                paymentChannelName,
                paymentTypeName,
                paymentTypeCode,
                ip,
                statusKYC,
                normalizeBigDecimal(cost),
                card);
    }

    private static BigDecimal normalizeBigDecimal(BigDecimal val) {
        if (val == null) return null;
        return val.stripTrailingZeros();
    }

    private static class Card {
        @JsonProperty("binNumber")
        private String binNumber;

        @JsonProperty("lastFour")
        private String lastFour;

        @JsonProperty("expMonth")
        private String expMonth;

        @JsonProperty("expYear")
        private String expYear;

        @JsonProperty("fullName")
        private String fullName;

        @JsonProperty("card3ds")
        private Integer card3ds;

        public Card() {}

        public Card(
                String binNumber, String lastFour, String expMonth, String expYear, String fullName, Integer card3ds) {
            this.binNumber = binNumber;
            this.lastFour = lastFour;
            this.expMonth = expMonth;
            this.expYear = expYear;
            this.fullName = fullName;
            this.card3ds = card3ds;
        }

        public String getBinNumber() {
            return binNumber;
        }

        public void setBinNumber(String binNumber) {
            this.binNumber = binNumber;
        }

        public String getLastFour() {
            return lastFour;
        }

        public void setLastFour(String lastFour) {
            this.lastFour = lastFour;
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

        public Integer getCard3ds() {
            return card3ds;
        }

        public void setCard3ds(Integer card3ds) {
            this.card3ds = card3ds;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Card card1)) return false;
            return Objects.equals(binNumber, card1.binNumber)
                    && Objects.equals(lastFour, card1.lastFour)
                    && Objects.equals(expMonth, card1.expMonth)
                    && Objects.equals(expYear, card1.expYear)
                    && Objects.equals(fullName, card1.fullName)
                    && Objects.equals(card3ds, card1.card3ds);
        }

        @Override
        public int hashCode() {
            return Objects.hash(binNumber, lastFour, expMonth, expYear, fullName, card3ds);
        }
    }
}
