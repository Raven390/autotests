package business_objects.db.abuse_registry_db;

public class FraudTypeCategory {
    private String fraudTypeCode;
    private Integer categoryId;

    public FraudTypeCategory() {
    }

    public FraudTypeCategory(String fraudTypeCode, Integer categoryId) {
        this.fraudTypeCode = fraudTypeCode;
        this.categoryId = categoryId;
    }

    public String getFraudTypeCode() {
        return fraudTypeCode;
    }

    public void setFraudTypeCode(String fraudTypeCode) {
        this.fraudTypeCode = fraudTypeCode;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
}
