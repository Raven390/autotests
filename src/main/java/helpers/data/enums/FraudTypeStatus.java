package helpers.data.enums;

import java.security.SecureRandom;
import lombok.Getter;

@Getter
public enum FraudTypeStatus {
    CONFIRMED("CONFIRMED", "Confirmed"),
    POTENTIAL("POTENTIAL", "Potential"),
    CLEANED("CLEANED", "Cleaned");

    private final String status;
    private final String displayName;

    FraudTypeStatus(String status, String displayName) {
        this.status = status;
        this.displayName = displayName;
    }

    public static FraudTypeStatus getRandomFraudStatusUi() {
        FraudTypeStatus fraud;
        do {
            FraudTypeStatus[] status = values();
            SecureRandom random = new SecureRandom();
            fraud = status[random.nextInt(status.length)];
        } while (fraud == CLEANED);
        return fraud;
    }
}
