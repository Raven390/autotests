package helpers.data.enums;

import lombok.Getter;

@Getter
public enum ConnectedClientStatus {
    UNDER_INVESTIGATION("Under investigation"), SUSPICIOUS("Suspicious"), NORMAL("Normal");

    private final String displayName;

    ConnectedClientStatus(String displayName) {
        this.displayName = displayName;
    }
}
