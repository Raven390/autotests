package utils;

import java.util.Objects;
import org.aeonbits.owner.Config;

public class ConfigFactory {
    private static final UserConfig CONFIG =
            org.aeonbits.owner.ConfigFactory.create(UserConfig.class, System.getProperties());

    public static final String
            // URLs
            BASE_URL_E2E = CONFIG.baseURLE2E(),
            BASE_URL_VANTAGE_ACCOUNT = CONFIG.baseUrlVantageAccount(),
            // KAFKA
            KAFKA_HOST = CONFIG.kafkaHost(),
            KAFKA_PORT = CONFIG.kafkaPort(),
            KAFKA_CORE_INCOMING_TOPIC = CONFIG.kafkaCoreIncomingTopic(),
            KAFKA_CORE_OUTCOMING_TOPIC = CONFIG.kafkaCoreOutcomingTopic(),
            BACK_OFFICE_KAFKA_PUBLIC = CONFIG.backOfficeKafkaPublic(),
            BACK_OFFICE_KAFKA_PRIVATE = CONFIG.backOfficeKafkaPrivate(),
            BACK_OFFICE_KAFKA_PASSWORD = CONFIG.backOfficeKafkaPassword(),

            // Databases
            POSTGRE_HOST = CONFIG.postgreLogin(),
            POSTGRE_PASSWORD = CONFIG.postgrePassword(),
            POSTGRE_LOGIN = CONFIG.postgreHost(),
            MYSQL_STAGING_CRM_HOST = CONFIG.mysqlStagingCrmHost(),
            MYSQL_STAGING_CRM_LOGIN = CONFIG.mysqlStagingCrmLogin(),
            MYSQL_STAGING_CRM_PASSWORD = CONFIG.mysqlStagingCrmPassword(),
            // Registration helper
            REGISTRATION_HELPER_LOGIN = CONFIG.registrationHelperLogin(),
            REGISTRATION_HELPER_PASSWORD = CONFIG.registrationHelperPassword(),
            REGISTRATION_HELPER_FIRST_NAME = CONFIG.registrationHelperFirstName(),
            REGISTRATION_HELPER_SECOND_NAME = CONFIG.registrationHelperSecondName(),
            REGISTRATION_HELPER_REGULATOR = CONFIG.registrationHelperRegulator(),
            REGISTRATION_HELPER_WID = CONFIG.registrationHelperWid(),
            REGISTRATION_HELPER_REG_INTERFACE = CONFIG.registrationHelperRegistrationInterface(),
            // Other
            COUNTRY_MALAYSIA = CONFIG.countryMalaysia();

    public static final Double TIMEOUT = CONFIG.waitTimeout();

    @Config.Sources({"classpath:config/config.properties", "system:properties"})
    public interface UserConfig extends Config {

        // URLs

        @Key("baseUrlE2e")
        String baseURLE2E();

        @Key("baseUrlVantageAccount")
        String baseUrlVantageAccount();

        // Settings

        @Key("headlessMode")
        Boolean headlessMode();

        @Key("timeout")
        Double waitTimeout();
        // Kafka

        @Key("kafkaHost")
        String kafkaHost();

        @Key("kafkaPort")
        String kafkaPort();

        @Key("kafkaCoreIncomingTopic")
        String kafkaCoreIncomingTopic();

        @Key("kafkaCoreOutcomingTopic")
        String kafkaCoreOutcomingTopic();

        @Key("backOfficeKafkaPublic")
        String backOfficeKafkaPublic();

        @Key("backOfficeKafkaPrivate")
        String backOfficeKafkaPrivate();

        @Key("backOfficeKafkaPassword")
        String backOfficeKafkaPassword();

        // Databases

        @Key("postgreHost")
        String postgreHost();

        @Key("postgreLogin")
        String postgreLogin();

        @Key("postgrePassword")
        String postgrePassword();

        @Key("mysqlStagingCrmHost")
        String mysqlStagingCrmHost();

        @Key("mysqlStagingCrmLogin")
        String mysqlStagingCrmLogin();

        @Key("mysqlStagingCrmPassword")
        String mysqlStagingCrmPassword();

        // Registration helper

        @Key("registrationHelperLogin")
        String registrationHelperLogin();

        @Key("registrationHelperPassword")
        String registrationHelperPassword();

        @Key("registrationHelperFirstName")
        String registrationHelperFirstName();

        @Key("registrationHelperSecondName")
        String registrationHelperSecondName();

        @Key("registrationHelperRegulator")
        String registrationHelperRegulator();

        @Key("registrationHelperWid")
        String registrationHelperWid();

        @Key("registrationHelperRegistrationInterface")
        String registrationHelperRegistrationInterface();

        // Other

        @Key("countryMalaysia")
        String countryMalaysia();
    }

    public static boolean isGitlab() {
        return Objects.equals(System.getenv("IS_GITLAB_CI"), "true");
    }

    public static Boolean getHeadless() {
        if (isGitlab()) {
            System.out.println("Set Headless mode to 'true'");
            return true;
        } else {
            System.out.println("Set Headless mode to local value in config.properties");
            return CONFIG.headlessMode();
        }
    }
}
