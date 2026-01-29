package helpers.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HistoryAction {
    ASSIGNED("ASSIGNED"),
    STARTED("STARTED"),
    UNASSIGNED("UNASSIGNED"),
    COMPLETED("COMPLETED"),
    REASSIGNED("REASSIGNED");

    private final String displayName;
}
