package businessObjects.kafka.crmDbEvents.withdrawal;

import io.qameta.allure.Step;

import java.time.Instant;

import static utils.Utils.*;

public class WithdrawalDbEventFactory {

    @Step("Generate withdrawal db event Metadata without table name")
    private static WithdrawalDbEventMetadata generateWithdrawalDbEventNoTableNameMetadata() {
        return new WithdrawalDbEventMetadata(
                Instant.now().toString(), "test_record_type", "update", "test_partition_key_type", "test_schema_name", getRandomInt().toString(), getRandomInt().toString(), getRandomInt().toString(), getRandomInt().toString(), Instant.now().toString(), "test_stream_position"
        );
    }

    @Step("Generate withdrawal db event Metadata")
    private static WithdrawalDbEventMetadata generateWithdrawalDbEventMetadata() {
        WithdrawalDbEventMetadata metadata = generateWithdrawalDbEventNoTableNameMetadata();
        metadata.tableName = "tb_payment_withdraw";
        return metadata;
    }

    @Step("Generate withdrawal db event Metadata")
    private static WithdrawalDbEventMetadata generateWithdrawalDbEventCpsMetadata() {
        WithdrawalDbEventMetadata metadata = generateWithdrawalDbEventNoTableNameMetadata();
        metadata.tableName = "tb_payment_withdraw_cps";
        return metadata;
    }

    @Step("Generate withdrawal db event Data")
    private static WithdrawalDbEventData generateWithdrawalDbEventData() {
        return new WithdrawalDbEventData(
                Instant.now().toString(), getRandomIntPositive(), getRandomIntPositive(), getRandomIntPositive(), "test_brand", "test_regulator", "test_payment_method_type", 1, 1.2d, 1.2d, 1.2d, 1.2d, "test_card_number", 1, "test_update_time", "test_cps_attach_variable", "test_order_number", "test,cps,mandatory,field", 1, "test_upi_account_name", 1.2d, 1, 1, "test_order_currency", 1, 1, 1.2d, 1, 1.2d
        );
    }

    @Step("Generate withdrawal db event cps Data")
    private static WithdrawalDbEventCpsData generateWithdrawalDbEventCpsData() {
        return new WithdrawalDbEventCpsData(
                Instant.now().toString(), getRandomIntPositive(), 1, 1, "test_brand", "test_regulator", 1, 1.2d, 1.2d, 1.2d, 1.2d, "test_card_number", 1, "test_update_time", "test_order_number", 1.2d, 1, 1, "test_order_currency", 1, 1.2d, 1.2d
        );
    }

    @Step("Generate withdrawal db event")
    public static WithdrawalDbEvent generateWithdrawalDbEvent() {
        return new WithdrawalDbEvent(
                generateWithdrawalDbEventData(), generateWithdrawalDbEventMetadata());
    }

    @Step("Generate withdrawal db event cps")
    public static WithdrawalDbEventCps generateWithdrawalDbEventCps() {
        return new WithdrawalDbEventCps(
                generateWithdrawalDbEventCpsData(), generateWithdrawalDbEventCpsMetadata());
    }
}
