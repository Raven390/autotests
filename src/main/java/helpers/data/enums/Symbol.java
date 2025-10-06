package helpers.data.enums;

import java.util.Arrays;
import java.util.Random;

import static utils.Utils.writeLog;

public enum Symbol {
    AAPL("AAPL", "Apple Inc."), MSFT("MSFT", "Microsoft Corporation"), GOOGL("GOOGL", "Alphabet Inc."), AMZN("AMZN", "Amazon.com, Inc."), TSLA("TSLA", "Tesla, Inc."), FB("FB", "Meta Platforms, Inc."), NFLX("NFLX", "Netflix, Inc."), NVDA("NVDA", "NVIDIA Corporation"), AMD("AMD", "Advanced Micro Devices, Inc."), BABA("BABA", "Alibaba Group Holding Limited"), JPM("JPM", "JPMorgan Chase & Co."), V("V", "Visa Inc."), MA("MA", "Mastercard Incorporated"), PYPL("PYPL", "PayPal Holdings, Inc."), INTC("INTC", "Intel Corporation"), CSCO("CSCO", "Cisco Systems, Inc."), ORCL("ORCL", "Oracle Corporation"), IBM("IBM", "International Business Machines Corporation"), T("T", "AT&T Inc."), VZ("VZ", "Verizon Communications Inc."), WMT("WMT", "Walmart Inc."), HD("HD", "The Home Depot, Inc."), COST("COST", "Costco Wholesale Corporation"), DIS("DIS", "The Walt Disney Company"), PEP("PEP", "PepsiCo, Inc."), KO("KO", "The Coca-Cola Company"), MCD("MCD", "McDonald's Corporation"), BA("BA", "The Boeing Company"), CAT("CAT", "Caterpillar Inc."), GE("GE", "General Electric Company"), GM("GM", "General Motors Company"), F("F", "Ford Motor Company"), XOM("XOM", "Exxon Mobil Corporation"), CVX("CVX", "Chevron Corporation"), PFE("PFE", "Pfizer Inc."), MRK("MRK", "Merck & Co., Inc."), ABBV("ABBV", "AbbVie Inc."), UNH("UNH", "UnitedHealth Group Incorporated"), JNJ("JNJ", "Johnson & Johnson"), PG("PG", "The Procter & Gamble Company"), NKE("NKE", "Nike, Inc."), SBUX("SBUX", "Starbucks Corporation"), BKNG("BKNG", "Booking Holdings Inc."), ADP("ADP", "Automatic Data Processing, Inc."), NOW("NOW", "ServiceNow, Inc."), CRM("CRM", "Salesforce, Inc."), SQ("SQ", "Block, Inc."), UBER("UBER", "Uber Technologies, Inc."), LYFT("LYFT", "Lyft, Inc."), SNAP("SNAP", "Snap Inc."), USD("USD", "United States Dollar"), EUR("EUR", "Euro"), GBP("GBP", "British Pound Sterling"), JPY("JPY", "Japanese Yen"), CHF("CHF", "Swiss Franc"), CAD("CAD", "Canadian Dollar"), AUD("AUD", "Australian Dollar"), NZD("NZD", "New Zealand Dollar"), CNY("CNY", "Chinese Yuan"), INR("INR", "Indian Rupee");

    private final String symbolCode;
    private final String symbolName;

    Symbol(String symbolCode, String symbolName) {
        this.symbolCode = symbolCode;
        this.symbolName = symbolName;
    }

    public String getSymbolCode() {
        return symbolCode;
    }

    public String getSymbolName() {
        return symbolName;
    }

    @Override
    public String toString() {
        return symbolCode + " (" + symbolName + ")";
    }


    public static Symbol getRandomSymbol() {
        Symbol[] symbols = values();
        Random random = new Random();
        return symbols[random.nextInt(symbols.length)];
    }

    public static Symbol getNextRandomSymbol(Symbol... initSymbol) {
        Symbol result;
        do {
            Symbol[] symbols = values();
            Random random = new Random();
            result = symbols[random.nextInt(symbols.length)];
        } while (Arrays.asList(initSymbol).contains(result));
        return result;
    }

    public static Symbol getSymbolByCode(String code) {
        for (Symbol symbol : values()) {
            if (symbol.getSymbolCode().equalsIgnoreCase(code)) {
                return symbol;
            }
        }
        writeLog("Symbol not found: " + code);
        return null;
    }

}
