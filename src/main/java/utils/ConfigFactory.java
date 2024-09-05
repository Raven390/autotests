package utils;

import org.aeonbits.owner.Config;

public class ConfigFactory {
    private static final UserConfig CONFIG =
            org.aeonbits.owner.ConfigFactory.create(UserConfig.class, System.getProperties());

    public static final String
            // URLs
            BASE_URL_E2E = CONFIG.baseURLE2E(),
            BASE_URL_VANTAGE_ACCOUNT = CONFIG.baseUrlVantageAccount(),
            BASE_API_URL = CONFIG.apiBaseUrl(),
            PATH_GET_USER = CONFIG.apiPathGetBaseUrl(),
            // KAFKA
            KAFKA_HOST = CONFIG.kafkaHost(),
            KAFKA_PORT = CONFIG.kafkaPort(),
            KAFKA_CORE_INCOMING_TOPIC = CONFIG.kafkaCoreIncomingTopic(),
            KAFKA_CORE_OUTCOMING_TOPIC = CONFIG.kafkaCoreOutcomingTopic(),
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

    public static final Boolean HEADLESS = CONFIG.headlessMode();

    @Config.Sources({"classpath:config/config.properties", "system:properties"})
    public interface UserConfig extends Config {

        // URLs

        @Key("baseUrlE2e")
        String baseURLE2E();

        @Key("baseUrlVantageAccount")
        String baseUrlVantageAccount();

        @Key("apiBaseUrl")
        String apiBaseUrl();

        @Key("apiPathGetUser")
        String apiPathGetBaseUrl();

        // Settings

        @Key("headlessMode")
        Boolean headlessMode();

        // Kafka

        @Key("kafkaHost")
        String kafkaHost();

        @Key("kafkaPort")
        String kafkaPort();

        @Key("kafkaCoreIncomingTopic")
        String kafkaCoreIncomingTopic();

        @Key("kafkaCoreOutcomingTopic")
        String kafkaCoreOutcomingTopic();

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
}
