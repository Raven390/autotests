package helpers.data.enums.payment_gate;

public enum Decision {
    FINAL_PENDING("final", 0, "Pending", "Pending"),
    FINAL_APPROVE("final", 1, "Approve", "Approve"),
    FINAL_REJECT("final", 2, "Reject", "Reject"),

    RISK_PENDING("risk", 0, "Risk Pending", "Risk assessment pending"),
    RISK_APPROVE("risk", 1, "Risk Approve", "Risk assessment approved"),
    RISK_REJECT("risk", 2, "Risk Reject", "Risk assessment rejected"),

    PAYMENT_PENDING("payment", 0, "Payment Pending", "Payment processing pending"),
    PAYMENT_APPROVE("payment", 1, "Payment Approve", "Payment processing approved"),
    PAYMENT_REJECT("payment", 2, "Payment Reject", "Payment processing rejected"),

    COMPLIANCE_PENDING("compliance", 0, "Compliance Pending", "Compliance check pending"),
    COMPLIANCE_APPROVE("compliance", 1, "Compliance Approve", "Compliance check approved"),
    COMPLIANCE_REJECT("compliance", 2, "Compliance Reject", "Compliance check rejected");

    private final String type;
    private final Integer code;
    private final String name;
    private final String description;

    Decision(String type, Integer code, String name, String description) {
        this.type = type;
        this.code = code;
        this.name = name;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
