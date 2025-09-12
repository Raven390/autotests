package business_objects.db.payment_gate.payment_details;

import java.util.Objects;


public class PaymentDetailsObject {

    private Integer id;
    private String paymentId;
    private String brand;
    private String regulator;
    private String type;
    private String clientId;
    private String merchantOrderId;
    private String eventDate;
    private String status;
    private String platform;
    private String payload;
    private String sourceSystem;
    private String sourceEnv;
    private String dateCreated;


    public PaymentDetailsObject() {
    }

    public PaymentDetailsObject(
            Integer id, String paymentId, String brand, String regulator, String type, String clientId,
            String merchantOrderId, String eventDate, String status, String platform, String payload,
            String sourceSystem,
            String sourceEnv, String dateCreated) {
        this.id = id;
        this.paymentId = paymentId;
        this.brand = brand;
        this.regulator = regulator;
        this.type = type;
        this.clientId = clientId;
        this.merchantOrderId = merchantOrderId;
        this.eventDate = eventDate;
        this.status = status;
        this.platform = platform;
        this.payload = payload;
        this.sourceSystem = sourceSystem;
        this.sourceEnv = sourceEnv;
        this.dateCreated = dateCreated;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
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

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getMerchantOrderId() {
        return merchantOrderId;
    }

    public void setMerchantOrderId(String merchantOrderId) {
        this.merchantOrderId = merchantOrderId;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getSourceSystem() {
        return sourceSystem;
    }

    public void setSourceSystem(String sourceSystem) {
        this.sourceSystem = sourceSystem;
    }

    public String getSourceEnv() {
        return sourceEnv;
    }

    public void setSourceEnv(String sourceEnv) {
        this.sourceEnv = sourceEnv;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PaymentDetailsObject that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(paymentId, that.paymentId) && Objects.equals(
                brand, that.brand) && Objects.equals(regulator, that.regulator) && Objects.equals(type, that.type) && Objects.equals(
                        clientId, that.clientId) && Objects.equals(merchantOrderId, that.merchantOrderId) && Objects.equals(
                                eventDate, that.eventDate) && Objects.equals(status, that.status) && Objects.equals(
                                        platform, that.platform) && Objects.equals(payload, that.payload) && Objects.equals(
                                                sourceSystem, that.sourceSystem) && Objects.equals(sourceEnv, that.sourceEnv) && Objects.equals(
                                                        dateCreated, that.dateCreated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, paymentId, brand, regulator, type, clientId, merchantOrderId, eventDate, status, platform, payload, sourceSystem, sourceEnv, dateCreated);
    }

    @Override
    public String toString() {
        return "PaymentDetailsObject{" + "id=" + id + ", paymentId='" + paymentId + '\'' + ", brand='" + brand + '\'' + ", regulator='" + regulator + '\'' + ", type='" + type + '\'' + ", clientId='" + clientId + '\'' + ", merchantOrderId='" + merchantOrderId + '\'' + ", eventDate='" + eventDate + '\'' + ", status='" + status + '\'' + ", platform='" + platform + '\'' + ", payload='" + payload + '\'' + ", sourceSystem='" + sourceSystem + '\'' + ", sourceEnv='" + sourceEnv + '\'' + ", dateCreated='" + dateCreated + '\'' + '}';
    }
}