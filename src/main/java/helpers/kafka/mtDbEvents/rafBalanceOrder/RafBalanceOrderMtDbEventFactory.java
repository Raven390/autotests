package helpers.kafka.mtDbEvents.rafBalanceOrder;

import static utils.Utils.getRandomInt;
import static utils.Utils.getRandomIntNotInRange;

import java.time.Instant;

public class RafBalanceOrderMtDbEventFactory {

    private static RafBalanceOrderMtDbEventMetadata generateRafBalanceOrderMtDbEventMetadata() {
        return new RafBalanceOrderMtDbEventMetadata(
                Instant.now().toString(), "test_record_type", "insert", "test_partition_key_type", "test_schema_name", null);
    }

    private static RafBalanceOrderMtDbEventMetadata generateRafBalanceOrderMtDbEventMetadataMt4() {
        RafBalanceOrderMtDbEventMetadata metadataMt4 = generateRafBalanceOrderMtDbEventMetadata();
        metadataMt4.tableName = "mt4_trades";
        return metadataMt4;
    }

    private static RafBalanceOrderMtDbEventMetadata generateRafBalanceOrderMtDbEventMetadataMt5() {
        RafBalanceOrderMtDbEventMetadata metadataMt5 = generateRafBalanceOrderMtDbEventMetadata();
        metadataMt5.tableName = "mt5_deals";
        return metadataMt5;
    }

    private static RafBalanceOrderMtDbEventMt4Data generateRafBalanceOrderMtDbEventDataMt4() {
        return new RafBalanceOrderMtDbEventMt4Data(
                Instant.now().toString(), getRandomInt(), getRandomIntNotInRange(741_000, 749_999), "RAF", 6, 9);
    }

    private static RafBalanceOrderMtDbEventMt5Data generateRafBalanceOrderMtDbEventDataMt5() {
        return new RafBalanceOrderMtDbEventMt5Data(
                Instant.now().toString(), getRandomInt(), getRandomIntNotInRange(741_000, 749_999), "RAF", 2, 9);
    }

    public static RafBalanceOrderMtDbEventMt4 generateRafBalanceOrderMtDbEventMt4() {
        return new RafBalanceOrderMtDbEventMt4(
                generateRafBalanceOrderMtDbEventDataMt4(), generateRafBalanceOrderMtDbEventMetadataMt4());
    }

    public static RafBalanceOrderMtDbEventMt5 generateRafBalanceOrderMtDbEventMt5() {
        return new RafBalanceOrderMtDbEventMt5(
                generateRafBalanceOrderMtDbEventDataMt5(), generateRafBalanceOrderMtDbEventMetadataMt5());
    }
}
