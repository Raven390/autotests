package helpers.data.enums.deduction;

import lombok.Getter;

@Getter
public enum DeductionStatusEmail {
    SENT("SENT"),
    NOT_SENT("NOT_SENT");

    private final String displayName;

    DeductionStatusEmail(String displayName) {
        this.displayName = displayName;
    }
}
