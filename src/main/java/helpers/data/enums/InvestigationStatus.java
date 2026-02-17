package helpers.data.enums;

import lombok.Getter;

@Getter
public enum InvestigationStatus {
    INVESTIGATING("INVESTIGATING"),
    ASSIGNED("ASSIGNED"),
    COMPLETED("COMPLETED"),
    NEW("NEW");

    private final String displayName;

    InvestigationStatus(String displayName) {
        this.displayName = displayName;
    }
}
