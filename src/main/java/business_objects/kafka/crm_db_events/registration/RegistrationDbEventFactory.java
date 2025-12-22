package business_objects.kafka.crm_db_events.registration;

import static utils.Utils.*;

import io.qameta.allure.Step;

public class RegistrationDbEventFactory {

    @Step("Generate registration db event Metadata")
    private static RegistrationDbEventMetadata generateRegistrationDbEventMetadata() {
        return new RegistrationDbEventMetadata(
                formatTimeToUtc(getCurrentTimestampDbFormat()),
                formatTimeToUtc(getCurrentTimestampDbFormat()),
                "test_record_type",
                "update",
                "test_partition_key_type",
                "test_schema_name",
                "tb_account_mt4");
    }

    @Step("Generate registration db event Data")
    private static RegistrationDbEventData generateRegistrationDbEventData() {
        return new RegistrationDbEventData(
                formatTimeToUtc(getCurrentTimestampDbFormat()),
                formatTimeToUtc(getCurrentTimestampDbFormat()),
                getRandomIntPositive(),
                "test_brand",
                "test_regulator",
                1);
    }

    @Step("Generate registration db event Data")
    public static RegistrationDbEvent generateRegistrationDbEvent() {
        return new RegistrationDbEvent(generateRegistrationDbEventData(), generateRegistrationDbEventMetadata());
    }
}
