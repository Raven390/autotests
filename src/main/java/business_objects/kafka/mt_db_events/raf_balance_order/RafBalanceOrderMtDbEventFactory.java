package business_objects.kafka.mt_db_events.raf_balance_order;

import helpers.data.enums.DateTimeFormat;
import io.qameta.allure.Step;
import utils.Utils;

import static utils.Utils.getRandomInt;
import static utils.Utils.getRandomIntNotInRange;


public class RafBalanceOrderMtDbEventFactory {

    @Step("Generate raf balance db event metadata")
    private static RafBalanceOrderMtDbEventMetadata generateRafBalanceOrderMtDbEventMetadata() {
        return new RafBalanceOrderMtDbEventMetadata(
                Utils.getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 0, 0, 1), "test_record_type", "insert", "test_partition_key_type", "test_schema_name", null);
    }

    @Step("Generate raf balance db event metadata mt 4")
    private static RafBalanceOrderMtDbEventMetadata generateRafBalanceOrderMtDbEventMetadataMt4() {
        RafBalanceOrderMtDbEventMetadata metadataMt4 = generateRafBalanceOrderMtDbEventMetadata();
        metadataMt4.tableName = "mt4_trades";
        return metadataMt4;
    }

    @Step("Generate raf balance db event metadata mt 5")
    private static RafBalanceOrderMtDbEventMetadata generateRafBalanceOrderMtDbEventMetadataMt5() {
        RafBalanceOrderMtDbEventMetadata metadataMt5 = generateRafBalanceOrderMtDbEventMetadata();
        metadataMt5.tableName = "mt5_deals";
        return metadataMt5;
    }

    @Step("Generate raf balance db event data mt 4")
    private static RafBalanceOrderMtDbEventMt4Data generateRafBalanceOrderMtDbEventDataMt4() {
        return new RafBalanceOrderMtDbEventMt4Data(
                Utils.getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 0, 0, 2), getRandomInt(), getRandomIntNotInRange(741_000, 749_999), "RAF", 6, 9);
    }

    @Step("Generate raf balance db event data mt 5")
    private static RafBalanceOrderMtDbEventMt5Data generateRafBalanceOrderMtDbEventDataMt5() {
        return new RafBalanceOrderMtDbEventMt5Data(
                Utils.getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 0, 0, 3), getRandomInt(), getRandomIntNotInRange(741_000, 749_999), "RAF", 2, 9);
    }

    @Step("Generate raf balance db event mt 4")
    public static RafBalanceOrderMtDbEventMt4 generateRafBalanceOrderMtDbEventMt4() {
        return new RafBalanceOrderMtDbEventMt4(
                generateRafBalanceOrderMtDbEventDataMt4(), generateRafBalanceOrderMtDbEventMetadataMt4());
    }

    @Step("Generate raf balance db event mt 5")
    public static RafBalanceOrderMtDbEventMt5 generateRafBalanceOrderMtDbEventMt5() {
        return new RafBalanceOrderMtDbEventMt5(
                generateRafBalanceOrderMtDbEventDataMt5(), generateRafBalanceOrderMtDbEventMetadataMt5());
    }
}
