package helpers.data.enums;

public enum ConnectionAttributes {
    EMAIL("email"), PAYOUT("payout"), SESSION("session"), WEB_SESSION("webSession"), NAME_BIRTH("nameBirth"), DIGITAL("digital"), DEVICE("device"), PHONE("phone"), IP("ip"), DOCUMENT("document");

    private final String value;

    ConnectionAttributes(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}