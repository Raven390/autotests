package helpers.kafka.crmDbEvents.registration;

import java.time.Instant;

import static utils.Utils.getRandomIntPositive;

public class RegistrationDbEventFactory {

    private static RegistrationDbEventMetadata generateRegistrationDbEventMetadata() {
        return new RegistrationDbEventMetadata(
                Instant.now().toString(),
                "test_record_type",
                "update",
                "test_partition_key_type",
                "test_schema_name",
                "tb_account_mt4"
                );
    }


    private static RegistrationDbEventData generateRegistrationDbEventData() {
        return new RegistrationDbEventData(
                Instant.now().toString(),
                getRandomIntPositive(),
                "test_brand",
                "test_regulator",
                1
                );
    }

    public static RegistrationDbEvent generateRegistrationDbEvent() {
        return new RegistrationDbEvent(
                generateRegistrationDbEventData(), generateRegistrationDbEventMetadata());
    }
}
