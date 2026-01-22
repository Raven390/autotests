package helpers.data.enums;

import lombok.Getter;

@Getter
public enum InvestigationStatus {
    ACTIVE("ACTIVE"),
    COMPLETED("COMPLETED"),
    NEW("NEW");

    private final String displayName;

    InvestigationStatus(String displayName) {
        this.displayName = displayName;
    }
}
