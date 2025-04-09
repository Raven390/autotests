package business_objects.kafka.crm_db_events.registration;

import io.qameta.allure.Step;
import utils.Utils;


import static utils.Utils.getRandomIntPositive;

public class RegistrationDbEventFactory {

    @Step("Generate registration db event Metadata")
    private static RegistrationDbEventMetadata generateRegistrationDbEventMetadata() {
        return new RegistrationDbEventMetadata(
                Utils.getCurrentTimestampDbFormat(), "test_record_type", "update", "test_partition_key_type", "test_schema_name", "tb_account_mt4"
        );
    }


    @Step("Generate registration db event Data")
    private static RegistrationDbEventData generateRegistrationDbEventData() {
        return new RegistrationDbEventData(
                Utils.getCurrentTimestampDbFormat(), getRandomIntPositive(), "test_brand", "test_regulator", 1
        );
    }

    @Step("Generate registration db event Data")
    public static RegistrationDbEvent generateRegistrationDbEvent() {
        return new RegistrationDbEvent(
                generateRegistrationDbEventData(), generateRegistrationDbEventMetadata());
    }
}
