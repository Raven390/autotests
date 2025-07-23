package helpers.data.enums.deduction;

public enum DeductionStatusApproval {
    AWAITING_APPROVAL("AWAITING_APPROVAL"), NOT_REQUIRED("NOT_REQUIRED"), APPROVED("APPROVED"), REJECTED("REJECTED");

    private final String displayName;

    DeductionStatusApproval(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}