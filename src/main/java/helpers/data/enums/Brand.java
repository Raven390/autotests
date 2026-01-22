package helpers.data.enums;

import lombok.Getter;

@Getter
public enum Brand {
    PU_PRIME("PU Prime", "puprime"),
    MONETA("Moneta", "moneta"),
    INFINOX("Infinox", "infinox"),
    VANTAGE("Vantage", "vantage"),
    ULTIMA_MARKETS("Ultima Markets", "ultimamarkets"),
    VT("VT", "vt"),
    VJP("VJP", "vjp"),
    STAR_TRADER("StarTrader", "startrader"),
    BYBIT("ByBit", "bybit"),
    ALPHATICK("AlphaTick", "alphatick");

    private final String displayName;
    private final String ucidBrand;

    Brand(String displayName, String ucidBrand) {
        this.displayName = displayName;
        this.ucidBrand = ucidBrand;
    }
}
