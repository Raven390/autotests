package business_objects.api.payment_gate.payments;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

/**
 * Response model for GET /payments endpoint.
 * Contains pagination fields and a list of payment items.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetPaymentsResponseBody {

    private Integer page;
    private Integer pageSize;
    private Integer total;
    private Boolean hasNext;
    private List<Item> items;
    private String error;
    private String message;

    public GetPaymentsResponseBody() {
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Boolean getHasNext() {
        return hasNext;
    }

    public void setHasNext(Boolean hasNext) {
        this.hasNext = hasNext;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Item {
        private UUID paymentId;
        private String ucid;
        private String type;
        private String storedAt;
        private Integer finalDecisionCode;
        private Event event;
        private List<Decision> decisions;

        public Item() {
        }

        public UUID getPaymentId() {
            return paymentId;
        }

        public void setPaymentId(UUID paymentId) {
            this.paymentId = paymentId;
        }

        public String getUcid() {
            return ucid;
        }

        public void setUcid(String ucid) {
            this.ucid = ucid;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getStoredAt() {
            return storedAt;
        }

        public void setStoredAt(String storedAt) {
            this.storedAt = storedAt;
        }

        public Integer getFinalDecisionCode() {
            return finalDecisionCode;
        }

        public void setFinalDecisionCode(Integer finalDecisionCode) {
            this.finalDecisionCode = finalDecisionCode;
        }

        public Event getEvent() {
            return event;
        }

        public void setEvent(Event event) {
            this.event = event;
        }

        public List<Decision> getDecisions() {
            return decisions;
        }

        public void setDecisions(List<Decision> decisions) {
            this.decisions = decisions;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Event {
        private UUID id;
        private String ip;
        private Card card;
        private Double cost;
        private String type;
        private String brand;
        private String status;
        private Long clientId;
        private String platform;
        private Integer statusId;
        private String checkName;
        private String eventDate;
        private String regulator;
        private String statusKYC;
        private Long mt4Account;
        private String accountType;
        private Long withdrawalId;
        private String schemaVersion;
        private String merchantOrderId;
        private Integer paymentTypeCode;
        private String paymentTypeName;
        private Double withdrawalAmount;
        private String paymentMethodCode;
        private Integer paymentChannelCode;
        private String paymentChannelName;
        private String withdrawalCurrency;
        private Double withdrawalAmountUSD;
        private String withdrawalApplicationTime;

        public Event() {
        }

        public UUID getId() {
            return id;
        }

        public void setId(UUID id) {
            this.id = id;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public Card getCard() {
            return card;
        }

        public void setCard(Card card) {
            this.card = card;
        }

        public Double getCost() {
            return cost;
        }

        public void setCost(Double cost) {
            this.cost = cost;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Long getClientId() {
            return clientId;
        }

        public void setClientId(Long clientId) {
            this.clientId = clientId;
        }

        public String getPlatform() {
            return platform;
        }

        public void setPlatform(String platform) {
            this.platform = platform;
        }

        public Integer getStatusId() {
            return statusId;
        }

        public void setStatusId(Integer statusId) {
            this.statusId = statusId;
        }

        public String getCheckName() {
            return checkName;
        }

        public void setCheckName(String checkName) {
            this.checkName = checkName;
        }

        public String getEventDate() {
            return eventDate;
        }

        public void setEventDate(String eventDate) {
            this.eventDate = eventDate;
        }

        public String getRegulator() {
            return regulator;
        }

        public void setRegulator(String regulator) {
            this.regulator = regulator;
        }

        public String getStatusKYC() {
            return statusKYC;
        }

        public void setStatusKYC(String statusKYC) {
            this.statusKYC = statusKYC;
        }

        public Long getMt4Account() {
            return mt4Account;
        }

        public void setMt4Account(Long mt4Account) {
            this.mt4Account = mt4Account;
        }

        public String getAccountType() {
            return accountType;
        }

        public void setAccountType(String accountType) {
            this.accountType = accountType;
        }

        public Long getWithdrawalId() {
            return withdrawalId;
        }

        public void setWithdrawalId(Long withdrawalId) {
            this.withdrawalId = withdrawalId;
        }

        public String getSchemaVersion() {
            return schemaVersion;
        }

        public void setSchemaVersion(String schemaVersion) {
            this.schemaVersion = schemaVersion;
        }

        public String getMerchantOrderId() {
            return merchantOrderId;
        }

        public void setMerchantOrderId(String merchantOrderId) {
            this.merchantOrderId = merchantOrderId;
        }

        public Integer getPaymentTypeCode() {
            return paymentTypeCode;
        }

        public void setPaymentTypeCode(Integer paymentTypeCode) {
            this.paymentTypeCode = paymentTypeCode;
        }

        public String getPaymentTypeName() {
            return paymentTypeName;
        }

        public void setPaymentTypeName(String paymentTypeName) {
            this.paymentTypeName = paymentTypeName;
        }

        public Double getWithdrawalAmount() {
            return withdrawalAmount;
        }

        public void setWithdrawalAmount(Double withdrawalAmount) {
            this.withdrawalAmount = withdrawalAmount;
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

        public String getWithdrawalCurrency() {
            return withdrawalCurrency;
        }

        public void setWithdrawalCurrency(String withdrawalCurrency) {
            this.withdrawalCurrency = withdrawalCurrency;
        }

        public Double getWithdrawalAmountUSD() {
            return withdrawalAmountUSD;
        }

        public void setWithdrawalAmountUSD(Double withdrawalAmountUSD) {
            this.withdrawalAmountUSD = withdrawalAmountUSD;
        }

        public String getWithdrawalApplicationTime() {
            return withdrawalApplicationTime;
        }

        public void setWithdrawalApplicationTime(String withdrawalApplicationTime) {
            this.withdrawalApplicationTime = withdrawalApplicationTime;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Card {
        private Integer card3ds;
        private String expYear;
        private String expMonth;
        private String fullName;
        private String lastFour;
        private String binNumber;

        public Card() {
        }

        public Integer getCard3ds() {
            return card3ds;
        }

        public void setCard3ds(Integer card3ds) {
            this.card3ds = card3ds;
        }

        public String getExpYear() {
            return expYear;
        }

        public void setExpYear(String expYear) {
            this.expYear = expYear;
        }

        public String getExpMonth() {
            return expMonth;
        }

        public void setExpMonth(String expMonth) {
            this.expMonth = expMonth;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getLastFour() {
            return lastFour;
        }

        public void setLastFour(String lastFour) {
            this.lastFour = lastFour;
        }

        public String getBinNumber() {
            return binNumber;
        }

        public void setBinNumber(String binNumber) {
            this.binNumber = binNumber;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Decision {
        private String decisionType;
        private Integer decisionCode;
        private String decision;
        private Integer rejectionCode;
        private String reasonString;
        private List<Attribute> rejectionAttributes;
        private Timestamp decidedAt;
        private String actor;

        public Decision(
                String decisionType, Integer decisionCode, String decisionName, Integer rejectionCode,
                String reasonString,
                List<Attribute> rejectionAttributes, Timestamp decidedAt, String actor) {
            this.decisionType = decisionType;
            this.decisionCode = decisionCode;
            this.rejectionCode = rejectionCode;
            this.reasonString = reasonString;
            this.rejectionAttributes = rejectionAttributes;
            this.decidedAt = decidedAt;
            this.actor = actor;
        }

        public Decision() {
        }

        public String getDecision() {
            return decision;
        }

        public void setDecision(String decision) {
            this.decision = decision;
        }

        public String getDecisionType() {
            return decisionType;
        }

        public void setDecisionType(String decisionType) {
            this.decisionType = decisionType;
        }

        public Integer getDecisionCode() {
            return decisionCode;
        }

        public void setDecisionCode(Integer decisionCode) {
            this.decisionCode = decisionCode;
        }

        public Integer getRejectionCode() {
            return rejectionCode;
        }

        public void setRejectionCode(Integer rejectionCode) {
            this.rejectionCode = rejectionCode;
        }

        public String getReasonString() {
            return reasonString;
        }

        public void setReasonString(String reasonString) {
            this.reasonString = reasonString;
        }

        public List<Attribute> getRejectionAttributes() {
            return rejectionAttributes;
        }

        public void setRejectionAttributes(
                List<Attribute> rejectionAttributes) {
            this.rejectionAttributes = rejectionAttributes;
        }

        public Timestamp getDecidedAt() {
            return decidedAt;
        }

        public void setDecidedAt(Timestamp decidedAt) {
            this.decidedAt = decidedAt;
        }

        public String getActor() {
            return actor;
        }

        public void setActor(String actor) {
            this.actor = actor;
        }

        public List<Attribute> getAttributes() {
            return rejectionAttributes;
        }

        public void setAttributes(List<Attribute> attributes) {
            this.rejectionAttributes = attributes;
        }

        public static class Attribute {
            private String code;
            private String value;

            public Attribute() {
            }

            public Attribute(String code, String value) {
                this.code = code;
                this.value = value;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }

    }
}
