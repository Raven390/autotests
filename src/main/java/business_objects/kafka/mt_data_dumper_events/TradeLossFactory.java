package business_objects.kafka.mt_data_dumper_events;

import static utils.Utils.getCurrentTimestampMillis;

import io.qameta.allure.Step;
import java.util.List;
import utils.Utils;

public class TradeLossFactory {

    // MT4 event

    @Step("Generate Trade Loss data dumper mt4")
    public static TradeEventMt4 generateTradeLossTradeDataDumperMt4() {
        return new TradeEventMt4(
                generateMt4TradeLossTradeMtDbEventHeader(), generateMt4TradeLossTradeMtDbEventPayload());
    }

    @Step("Generate MT4 Trade Loss data dumper event header")
    static TradeEventMt4.Header generateMt4TradeLossTradeMtDbEventHeader() {
        return new TradeEventMt4.Header(
                Utils.getRandomUuidString(), "trade_record", 1, 18, Utils.getCurrentTimestampMillis());
    }

    @Step("Generate MT4 Trade Loss data dumper event payload")
    static TradeEventMt4.Payload generateMt4TradeLossTradeMtDbEventPayload() {
        return new TradeEventMt4.Payload(
                1d,
                List.of(1, 1),
                100d,
                1d,
                Utils.getCurrentTimestampMillis(),
                Utils.getCurrentTimestampMillis(),
                6,
                "Automation tests for data dumper Trade Loss / TLV event",
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
                Utils.getRandomIntPositive().longValue(),
                1L,
                1d,
                1d,
                2,
                1d,
                getCurrentTimestampMillis(),
                getCurrentTimestampMillis(),
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

    @Step("Generate Trade Loss data dumper mt5")
    public static TradeEventMt5 generateTradeLossTradeDataDumperMt5() {
        return new TradeEventMt5(
                generateMt5TradeLossTradeMtDbEventHeader(),
                generateMt5TradeLossTradeMtDbEventPayload(generateMt5TradeLossTradeMtDbEventRawApiData()));
    }

    @Step("Generate MT5 Trade Loss data dumper event header")
    private static TradeEventMt5.Header generateMt5TradeLossTradeMtDbEventHeader() {
        return new TradeEventMt5.Header(
                Utils.getRandomUuidString(), "DealPerform", 0, 33, Utils.getCurrentTimestampMillis());
    }

    @Step("Generate MT5 Trade Loss data dumper event payload")
    private static TradeEventMt5.Payload generateMt5TradeLossTradeMtDbEventPayload(
            TradeEventMt5.RawApiData rawApiData) {
        return new TradeEventMt5.Payload(
                1.0,
                2,
                100d,
                "Automation tests for data dumper Trade Loss / TLV event",
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
                Utils.getRandomIntPositive().longValue(),
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
                Utils.getCurrentTimestampMillis(),
                Utils.getCurrentTimestampMillis(),
                1d,
                1L,
                0L,
                0L,
                0L);
    }

    @Step("Generate MT5 Trade Loss data dumper event rawApiData")
    private static TradeEventMt5.RawApiData generateMt5TradeLossTradeMtDbEventRawApiData() {
        return new TradeEventMt5.RawApiData(1, 2, 3);
    }
}
