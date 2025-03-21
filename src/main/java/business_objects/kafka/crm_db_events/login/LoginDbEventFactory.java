package business_objects.kafka.crm_db_events.login;


import io.qameta.allure.Step;

import java.time.Instant;

import static utils.Utils.getRandomIntPositive;


public class LoginDbEventFactory {

    @Step("Generate login db event Metadata")
    private static LoginDbEventMetadata generateLoginDbEventMetadata() {
        return new LoginDbEventMetadata(
                Instant.now().toString(), "test_record_type", "login", "test_partition_key_type", "test_schema_name", "tb_user_login_info"
        );
    }

    @Step("Generate login db event Data")
    private static LoginDbEventData generateLoginDbEventData() {
        return new LoginDbEventData(
                Instant.now().toString(), getRandomIntPositive(), "test_brand", "test_ip_address", "test_cid", "test_cookie"
        );
    }

    @Step("Generate login db event")
    public static LoginDbEvent generateLoginDbEvent() {
        return new LoginDbEvent(
                generateLoginDbEventData(), generateLoginDbEventMetadata());
    }
}
