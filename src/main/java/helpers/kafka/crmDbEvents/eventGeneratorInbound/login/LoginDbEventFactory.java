package helpers.kafka.crmDbEvents.eventGeneratorInbound.login;


import java.time.Instant;

import static utils.Utils.getRandomIntPositive;

public class LoginDbEventFactory {

    private static LoginDbEventMetadata generateLoginDbEventMetadata() {
        return new LoginDbEventMetadata(
                Instant.now().toString(),
                "test_record_type",
                "login",
                "test_partition_key_type",
                "test_schema_name",
                "tb_user_login_info"
                );
    }


    private static LoginDbEventData generateLoginDbEventData() {
        return new LoginDbEventData(
                Instant.now().toString(),
                getRandomIntPositive(),
                "test_brand",
                "test_ip_address",
                "test_cid",
                "test_cookie"
                );
    }

    public static LoginDbEvent generateLoginDbEvent() {
        return new LoginDbEvent(
                generateLoginDbEventData(), generateLoginDbEventMetadata());
    }
}
