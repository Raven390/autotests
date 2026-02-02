package helpers.data.enums;

import lombok.Getter;

@Getter
public enum VerificationDocument {
    CARD_STATEMENT("Card statement"),
    CARD_PHOTO("Photo of the card"),
    WALLET_SCREENSHOT("Screenshot wallet"),
    BANK_STATEMENT("Bank statement");

    private final String displayName;

    VerificationDocument(String displayName) {
        this.displayName = displayName;
    }
}
