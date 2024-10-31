package businessObjects.kafka.mtDbEvents.openTrade;

import io.qameta.allure.Step;

import static utils.Utils.getRandomInt;
import static utils.Utils.getRandomIntNotInRange;

import java.time.Instant;

public class OpenTradeMtDbEventFactory {

    @Step("Generate open trade db event metadata")
    private static OpenTradeMtDbEventMetadata generateOpenTradeMtDbEventMetadata() {
        return new OpenTradeMtDbEventMetadata(
                Instant.now().toString(), "test_record_type", "insert", "test_partition_key_type", "test_schema_name", null);
    }

    @Step("Generate open trade db event metadata mt 4")
    private static OpenTradeMtDbEventMetadata generateOpenTradeMtDbEventMetadataMt4() {
        OpenTradeMtDbEventMetadata metadataMt4 = generateOpenTradeMtDbEventMetadata();
        metadataMt4.tableName = "mt4_trades";
        return metadataMt4;
    }

    @Step("Generate open trade db event metadata mt 5")
    private static OpenTradeMtDbEventMetadata generateOpenTradeMtDbEventMetadataMt5() {
        OpenTradeMtDbEventMetadata metadataMt5 = generateOpenTradeMtDbEventMetadata();
        metadataMt5.tableName = "mt5_deals";
        return metadataMt5;
    }

    @Step("Generate open trade db event data mt 4")
    private static OpenTradeMtDbEventMt4Data generateOpenTradeMtDbEventDataMT4() {
        return new OpenTradeMtDbEventMt4Data(
                Instant.now().toString(), getRandomInt(), getRandomIntNotInRange(741_000, 749_999), 3.45d, "test_symbol", 0, null, 9);
    }

    @Step("Generate open trade db event data mt 5")
    private static OpenTradeMtDbEventMt5Data generateOpenTradeMtDbEventDataMT5() {
        return new OpenTradeMtDbEventMt5Data(
                Instant.now().toString(), getRandomInt(), getRandomIntNotInRange(741_000, 749_999), 3.45d, "test_symbol", 0, 0, 9);
    }

    @Step("Generate open trade db event mt 4")
    public static OpenTradeMtDbEventMt4 generateOpenTradeMtDbEventMt4() {
        return new OpenTradeMtDbEventMt4(generateOpenTradeMtDbEventDataMT4(), generateOpenTradeMtDbEventMetadataMt4());
    }

    @Step("Generate open trade db event data mt 5")
    public static OpenTradeMtDbEventMt5 generateOpenTradeMtDbEventMt5() {
        return new OpenTradeMtDbEventMt5(generateOpenTradeMtDbEventDataMT5(), generateOpenTradeMtDbEventMetadataMt5());
    }
}
