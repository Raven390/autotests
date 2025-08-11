package business_objects.kafka.mt_data_dumper_events;

import io.qameta.allure.Step;
import utils.Utils;

import java.util.List;

import static utils.Utils.getRandomIntPositive;

public class StopoutFactory {

    // MT4 event

    @Step("Generate Stopout trade data dumper mt4")
    public static TradeEventMt4 generateStopoutTradeDataDumperMt4() {
        return new TradeEventMt4(generateMt4StopoutTradeMtDbEventHeader(), generateMt4StopoutTradeMtDbEventPayload());
    }

    @Step("Generate MT4 Stopout trade data dumper event header")
    static TradeEventMt4.Header generateMt4StopoutTradeMtDbEventHeader() {
        return new TradeEventMt4.Header(
                Utils.getRandomUuidString(), "trade_record", 1, 18, Utils.getCurrentTimestampMillis());
    }

    @Step("Generate MT4 Stopout trade data dumper event payload")
    static TradeEventMt4.Payload generateMt4StopoutTradeMtDbEventPayload() {
        return new TradeEventMt4.Payload(2d, List.of(1, 1), 100d, 3d, Utils.getCurrentTimestampMillis(), Utils.getCurrentTimestampMillis(), 1, "Automation tests data dumper stopout event (so)", 4d, 5d, List.of(1d, 1d), List.of(1, 1), 6d, 7d, 8, 9d, 10L, 11L, 12d, 13d, 14d, 15L, 16d, 17d, Utils.getRandomLongPositive(), 18L, 19d, 20d, 2, 21d, 22L, 23L, getRandomIntPositive().longValue(), 24d, 25, 26d, 27, 28d, "EURUSD", 29d, 30L, 31L, 32d, 33d);
    }

    // MT5 event

    @Step("Generate Stopout trade data dumper mt5")
    public static TradeEventMt5 generateStopoutTradeDataDumperMt5() {
        return new TradeEventMt5(
                generateMt5StopoutTradeMtDbEventHeader(), generateMt5StopoutTradeMtDbEventPayload(generateMt5StopoutTradeMtDbEventRawApiData()));
    }

    @Step("Generate MT5 Stopout trade data dumper event header")
    private static TradeEventMt5.Header generateMt5StopoutTradeMtDbEventHeader() {
        return new TradeEventMt5.Header(Utils.getRandomUuidString(), "DealPerform", 0, 33, Utils.getCurrentTimestampMillis());
    }

    @Step("Generate MT5 Stopout trade data dumper event payload")
    private static TradeEventMt5.Payload generateMt5StopoutTradeMtDbEventPayload(TradeEventMt5.RawApiData rawApiData) {
        return new TradeEventMt5.Payload(2.0, 0, 100d, "Automation tests data dumper stopout event (so)", 3.0, 4.0, Utils.getRandomIntPositive().longValue(), 5L, 6, 7, 1, 100d, 8L, "123", 9.0, 10, 11d, "12", 13, Utils.getRandomIntPositive().longValue(), 14d, 15d, 16d, 18d, 19, getRandomIntPositive().longValue(), 20L, 21d, 22d, 23d, 24d, 25d, 26d, 27d, 28d, 29d, List.of(rawApiData), 30, 31d, "EURUSD", 32d, 33d, Utils.getCurrentTimestampMillis(), Utils.getCurrentTimestampMillis(), 34d, 35L, 36L, 37L, 38L);
    }

    @Step("Generate MT5 Stopout trade data dumper event rawApiData")
    private static TradeEventMt5.RawApiData generateMt5StopoutTradeMtDbEventRawApiData() {
        return new TradeEventMt5.RawApiData(1, 2, 3);
    }
}
