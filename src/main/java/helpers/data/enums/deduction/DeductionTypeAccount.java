package helpers.data.enums.deduction;

import lombok.Getter;

@Getter
public enum DeductionTypeAccount {
    ILLEGAL_PROFIT("ILLEGAL_PROFIT"),
    NO_ILLEGAL_PROFIT("NO_ILLEGAL_PROFIT");

    private final String displayName;

    DeductionTypeAccount(String displayName) {
        this.displayName = displayName;
    }
}
