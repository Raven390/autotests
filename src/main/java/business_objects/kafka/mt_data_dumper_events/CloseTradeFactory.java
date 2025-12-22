package business_objects.kafka.mt_data_dumper_events;

import static utils.Utils.getCurrentTimestampMillis;

import io.qameta.allure.Step;
import java.util.List;
import utils.Utils;

public class CloseTradeFactory {

    // MT4 event

    @Step("Generate close trade data dumper mt4")
    public static TradeEventMt4 generateCloseTradeDataDumperMt4() {
        return new TradeEventMt4(generateMt4CloseTradeMtDbEventHeader(), generateMt4CloseTradeMtDbEventPayload());
    }

    @Step("Generate MT4 close trade data dumper event header")
    static TradeEventMt4.Header generateMt4CloseTradeMtDbEventHeader() {
        return new TradeEventMt4.Header(
                Utils.getRandomUuidString(), "trade_record", 1, 18, getCurrentTimestampMillis());
    }

    @Step("Generate MT4 close trade data dumper event payload")
    static TradeEventMt4.Payload generateMt4CloseTradeMtDbEventPayload() {
        return new TradeEventMt4.Payload(
                1d,
                List.of(1, 1),
                100d,
                1d,
                getCurrentTimestampMillis(),
                getCurrentTimestampMillis(),
                1,
                "Automation tests",
                1d,
                1d,
                List.of(1d, 1d),
                List.of(1, 1),
                1d,
                1d,
                2,
                1d,
                1L,
                1L,
                1d,
                1d,
                1d,
                1L,
                1d,
                1d,
                Utils.getRandomLongPositive(),
                1L,
                1d,
                1d,
                2,
                1d,
                1L,
                1L,
                1L,
                1d,
                1,
                1d,
                1,
                1d,
                "EURUSD",
                1d,
                1L,
                1L,
                1d,
                1d);
    }

    // MT5 event

    @Step("Generate close trade data dumper mt5")
    public static TradeEventMt5 generateCloseTradeDataDumperMt5() {
        return new TradeEventMt5(
                generateMt5CloseTradeMtDbEventHeader(),
                generateMt5CloseTradeMtDbEventPayload(generateMt5CloseTradeMtDbEventRawApiData()));
    }

    @Step("Generate MT5 close trade data dumper event header")
    private static TradeEventMt5.Header generateMt5CloseTradeMtDbEventHeader() {
        return new TradeEventMt5.Header(Utils.getRandomUuidString(), "DealPerform", 0, 33, getCurrentTimestampMillis());
    }

    @Step("Generate MT5 close trade data dumper event payload")
    private static TradeEventMt5.Payload generateMt5CloseTradeMtDbEventPayload(TradeEventMt5.RawApiData rawApiData) {
        return new TradeEventMt5.Payload(
                1.0,
                1,
                100d,
                "Automation tests",
                1.0,
                1.0,
                Utils.getRandomIntPositive().longValue(),
                0L,
                1,
                2,
                1,
                100d,
                0L,
                "",
                1.0,
                0,
                100d,
                "1",
                1,
                Utils.getRandomLongPositive(),
                1d,
                1d,
                1d,
                1d,
                1,
                1L,
                1L,
                1d,
                1d,
                1d,
                1d,
                1d,
                1d,
                1d,
                1d,
                1d,
                List.of(rawApiData),
                0,
                1d,
                "EURUSD",
                1d,
                1d,
                getCurrentTimestampMillis(),
                getCurrentTimestampMillis(),
                1d,
                1L,
                0L,
                0L,
                0L);
    }

    @Step("Generate MT5 close trade data dumper event rawApiData")
    private static TradeEventMt5.RawApiData generateMt5CloseTradeMtDbEventRawApiData() {
        return new TradeEventMt5.RawApiData(1, 2, 3);
    }
}
