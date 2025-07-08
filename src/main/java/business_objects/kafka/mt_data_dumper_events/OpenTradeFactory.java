package business_objects.kafka.mt_data_dumper_events;

import io.qameta.allure.Step;
import utils.Utils;

import java.util.List;

public class OpenTradeFactory {

    // MT4 event

    @Step("Generate open trade data dumper mt4")
    public static TradeEventMt4 generateOpenTradeDataDumperMt4() {
        return new TradeEventMt4(generateMt4OpenTradeMtDbEventHeader(), generateMt4OpenTradeMtDbEventPayload());
    }

    @Step("Generate MT4 Open trade data dumper event header")
    static TradeEventMt4.Header generateMt4OpenTradeMtDbEventHeader() {
        return new TradeEventMt4.Header(
                Utils.getRandomUuidString(), "trade_record", 0, 18, Utils.getCurrentTimestamp());
    }

    @Step("Generate MT4 Open trade data dumper event payload")
    static TradeEventMt4.Payload generateMt4OpenTradeMtDbEventPayload() {
        return new TradeEventMt4.Payload(1d, List.of(1, 1), 100d, 1d, 0L, 0L, 0, "Automation tests", 1d, 1d, List.of(1d, 1d), List.of(1, 1), 1d, 1d, 2, 1d, 1L, 1L, 1d, 1d, 1d, 1L, 1d, 1d, Utils.getRandomIntPositive().longValue(), 1L, 1d, 1d, 0, 1d, 1L, 1L, 1L, 1d, 1, 1d, 0, 1d, "EURUSD", 1d, Utils.getCurrentTimestamp(), Utils.getCurrentTimestamp(), 1d, 1d);
    }

    // MT5 event

    @Step("Generate Open trade data dumper mt5")
    public static TradeEventMt5 generateOpenTradeDataDumperMt5() {
        return new TradeEventMt5(
                generateMt5OpenTradeMtDbEventHeader(), generateMt5OpenTradeMtDbEventPayload(generateMt5OpenTradeMtDbEventRawApiData()));
    }

    @Step("Generate MT5 Open trade data dumper event header")
    private static TradeEventMt5.Header generateMt5OpenTradeMtDbEventHeader() {
        return new TradeEventMt5.Header(Utils.getRandomUuidString(), "Deal", 0, 33, Utils.getCurrentTimestamp());
    }

    @Step("Generate MT5 Open trade data dumper event payload")
    private static TradeEventMt5.Payload generateMt5OpenTradeMtDbEventPayload(TradeEventMt5.RawApiData rawApiData) {
        return new TradeEventMt5.Payload(1.0, 0, 100d, "Automation tests", 1.0, 1.0, Utils.getRandomIntPositive().longValue(), 0L, 1, 2, 0, 100d, 0L, "", 1.0, 0, 100d, "1", 1, Utils.getRandomIntPositive().longValue(), 1d, 1d, 1d, 1d, 1, 1L, 1L, 1d, 1d, 1d, 1d, 1d, 1d, 1d, 1d, 1d, List.of(rawApiData), 0, 1d, "EURUSD", 1d, 1d, 0L, 0L, 1d, 1L, 0L, 0L, 0L);
    }

    @Step("Generate MT5 Open trade data dumper event rawApiData")
    private static TradeEventMt5.RawApiData generateMt5OpenTradeMtDbEventRawApiData() {
        return new TradeEventMt5.RawApiData(1, 2, 3);
    }
}
