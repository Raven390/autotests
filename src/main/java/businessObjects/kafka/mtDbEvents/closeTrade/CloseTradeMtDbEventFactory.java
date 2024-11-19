package businessObjects.kafka.mtDbEvents.closeTrade;

import io.qameta.allure.Step;

import static utils.Utils.getRandomInt;
import static utils.Utils.getRandomIntNotInRange;

import java.time.Instant;

public class CloseTradeMtDbEventFactory {

    @Step("Generate close trade db event metadata")
    private static CloseTradeMtDbEventMetadata generateCloseTradeMtDbEventMetadata() {
        return new CloseTradeMtDbEventMetadata(
                Instant.now().toString(), "test_record_type", "insert", "test_partition_key_type", "test_schema_name", null);
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
                Instant.now().toString(), getRandomInt(), getRandomIntNotInRange(741_000, 749_999), 3.45d, "test_symbol", 0, 9);
    }

    @Step("Generate close trade db event mt4 data by trading account")
    private static CloseTradeMtDbEventMt4Data generateCloseTradeMtDbEventMt4DataByTradingAccount(Integer tradingAccount) {
        return new CloseTradeMtDbEventMt4Data(
                Instant.now().toString(), getRandomInt(), tradingAccount, 3.45d, "test_symbol", 0, 9);
    }

    @Step("Generate close trade db event mt5 data")
    private static CloseTradeMtDbEventMt5Data generateCloseTradeMtDbEventMt5Data() {
        return new CloseTradeMtDbEventMt5Data(
                Instant.now().toString(), getRandomInt(), getRandomIntNotInRange(741_000, 749_999), 3.45d, "test_symbol", 1, 0, 9);
    }

    @Step("Generate close trade db event mt4")
    public static CloseTradeMtDbEventMt4 generateCloseTradeMtDbEventMt4() {
        return new CloseTradeMtDbEventMt4(
                generateCloseTradeMtDbEventMt4Data(), generateCloseTradeMtDbEventMetadataMt4());
    }

    @Step("Generate close trade db event mt4")
    public static CloseTradeMtDbEventMt4 generateCloseTradeMtDbEventMt4ByTradingAccount(Integer tradingAccount) {
        return new CloseTradeMtDbEventMt4(
                generateCloseTradeMtDbEventMt4DataByTradingAccount(tradingAccount), generateCloseTradeMtDbEventMetadataMt4());
    }

    @Step("Generate close trade db event mt5")
    public static CloseTradeMtDbEventMt5 generateCloseTradeMtDbEventMt5() {
        return new CloseTradeMtDbEventMt5(
                generateCloseTradeMtDbEventMt5Data(), generateCloseTradeMtDbEventMetadataMt5());
    }
}
