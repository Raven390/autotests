package helpers.data.enums;

import lombok.Getter;

@Getter
public enum Regulator {
    VFSC2("VFSC2"),
    VFSC("VFSC"),
    SVG("SVG"),
    FSC("FSC"),
    FCA("FCA"),
    FSA("FSA"),
    ASIC("ASIC"),
    CIMA("CIMA"),
    FMA("FMA");

    private final String displayName;

    Regulator(String displayName) {
        this.displayName = displayName;
    }
}
