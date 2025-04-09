package business_objects.kafka.mt_db_events.close_trade;

import io.qameta.allure.Step;
import utils.Utils;

import java.time.Instant;

import static utils.Utils.*;

public class CloseTradeMtDbEventFactory {

    @Step("Generate close trade db event metadata")
    private static CloseTradeMtDbEventMetadata generateCloseTradeMtDbEventMetadata() {
        return new CloseTradeMtDbEventMetadata(
                Utils.getCurrentTimestampDbFormat(), "event", "INSERT", "HASH", "events", "mt4_trades");
    }

    @Step("Generate close trade db event mt4 metadata")
    private static CloseTradeMtDbEventMetadata generateCloseTradeMtDbEventMetadataMt4() {
        CloseTradeMtDbEventMetadata metadataMt4 = generateCloseTradeMtDbEventMetadata();
        metadataMt4.tableName = "mt4_trades";
        return metadataMt4;
    }

    @Step("Generate close trade db event mt5 metadata")
    private static CloseTradeMtDbEventMetadata generateCloseTradeMtDbEventMetadataMt5() {
        CloseTradeMtDbEventMetadata metadataMt5 = generateCloseTradeMtDbEventMetadata();
        metadataMt5.tableName = "mt5_deals";
        return metadataMt5;
    }

    @Step("Generate close trade db event mt4 data")
    private static CloseTradeMtDbEventMt4Data generateCloseTradeMtDbEventMt4Data() {
        return new CloseTradeMtDbEventMt4Data(
                Utils.getCurrentTimestampDbFormat(), Instant.now().plusMillis(100_000).toString(), getRandomIntPositive(), getRandomIntNotInRange(741_000, 749_999), 3.45d, "EURUSD", 1, 9);
    }

    @Step("Generate close trade db event mt5 data")
    private static CloseTradeMtDbEventMt5Data generateCloseTradeMtDbEventMt5Data() {
        return new CloseTradeMtDbEventMt5Data(
                Utils.getCurrentTimestampDbFormat(), getRandomInt(), getRandomIntNotInRange(741_000, 749_999), 3.45d, "EURUSD", 1, 0, 9);
    }

    @Step("Generate close trade db event mt4")
    public static CloseTradeMtDbEventMt4 generateCloseTradeMtDbEventMt4() {
        return new CloseTradeMtDbEventMt4(
                generateCloseTradeMtDbEventMt4Data(), generateCloseTradeMtDbEventMetadataMt4());
    }

    @Step("Generate close trade db event mt5")
    public static CloseTradeMtDbEventMt5 generateCloseTradeMtDbEventMt5() {
        return new CloseTradeMtDbEventMt5(
                generateCloseTradeMtDbEventMt5Data(), generateCloseTradeMtDbEventMetadataMt5());
    }
}
