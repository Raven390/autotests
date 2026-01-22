package helpers.data.enums;

import lombok.Getter;

@Getter
public enum VerificationStatus {
    NOT_VERIFIED("Not verified"),
    AWAITING_DOCUMENTS("Awaiting documents"),
    VERIFIED("Verified"),
    REJECTED("Rejected");

    private final String displayName;

    VerificationStatus(String displayName) {
        this.displayName = displayName;
    }
}
