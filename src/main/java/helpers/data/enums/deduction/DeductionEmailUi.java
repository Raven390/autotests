package helpers.data.enums.deduction;

import lombok.Getter;

@Getter
public enum DeductionEmailUi {
    NO_EMAIL_SENT("No email sent"),
    AWAITING_APPROVAL("Awaiting approval"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    EMAIL_SENT("Email sent");

    private final String displayName;

    DeductionEmailUi(String displayName) {
        this.displayName = displayName;
    }
}
