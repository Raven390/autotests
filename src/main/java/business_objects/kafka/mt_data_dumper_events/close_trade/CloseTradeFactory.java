package business_objects.kafka.mt_data_dumper_events.close_trade;

import io.qameta.allure.Step;
import utils.Utils;

import java.util.List;

public class CloseTradeFactory {

    // MT4 event

    @Step("Generate close trade data dumper mt4")
    public static CloseTradeMt4 generateCloseTradeDataDumperMt4() {
        return new CloseTradeMt4(generateMt4CloseTradeMtDbEventHeader(), generateMt4CloseTradeMtDbEventPayload());
    }

    @Step("Generate MT4 close trade data dumper event header")
    static CloseTradeMt4.Header generateMt4CloseTradeMtDbEventHeader() {
        return new CloseTradeMt4.Header(
                Utils.getRandomUuidString(), "trade_record", 1, 18, Utils.getCurrentTimestamp());
    }

    @Step("Generate MT4 close trade data dumper event payload")
    static CloseTradeMt4.Payload generateMt4CloseTradeMtDbEventPayload() {
        return new CloseTradeMt4.Payload(1d, List.of(1, 1), 100, 1d, Utils.getCurrentTimestamp(), Utils.getCurrentTimestamp(), 1, "Automation tests", 1d, 1d, List.of(1d, 1d), List.of(1, 1), 1d, 1d, 2, 1d, 1L, 1L, 1d, 1d, 1d, 1L, 1d, 1d, Utils.getRandomLongPositive(), 1L, 1, 1d, 2, 1d, 1L, 1L, 1L, 1d, 1, 1d, 1, 1d, "EURUSD", 1d, 1L, 1L, 1d, 1d);
    }

    // MT5 event

    @Step("Generate close trade data dumper mt5")
    public static CloseTradeMt5 generateCloseTradeDataDumperMt5() {
        return new CloseTradeMt5(
                generateMt5CloseTradeMtDbEventHeader(), generateMt5CloseTradeMtDbEventPayload(generateMt5CloseTradeMtDbEventRawApiData()));
    }

    @Step("Generate MT5 close trade data dumper event header")
    private static CloseTradeMt5.Header generateMt5CloseTradeMtDbEventHeader() {
        return new CloseTradeMt5.Header(Utils.getRandomUuidString(), "Deal", 0, 33, Utils.getCurrentTimestamp());
    }

    @Step("Generate MT5 close trade data dumper event payload")
    private static CloseTradeMt5.Payload generateMt5CloseTradeMtDbEventPayload(CloseTradeMt5.RawApiData rawApiData) {
        return new CloseTradeMt5.Payload(1.0, 1, 100, "Automation tests", 1.0, 1.0, Utils.getRandomIntPositive(), 0, 1, 2, 1, 100, 0, "", 1.0, 0, 100, "1", 1, Utils.getRandomLongPositive(), 1d, 1d, 1d, 1d, 1, 1L, 1L, 1d, 1d, 1d, 1d, 1d, 1d, 1d, 1d, 1d, List.of(rawApiData), 0, 1d, "EURUSD", 1d, 1d, Utils.getCurrentTimestamp(), Utils.getCurrentTimestamp(), 1d, 1, 0, 0, 0);
    }

    @Step("Generate MT5 close trade data dumper event rawApiData")
    private static CloseTradeMt5.RawApiData generateMt5CloseTradeMtDbEventRawApiData() {
        return new CloseTradeMt5.RawApiData(1, 2, 3);
    }
}
