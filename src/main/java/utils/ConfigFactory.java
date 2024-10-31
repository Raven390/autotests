package utils;

import java.util.Objects;
import org.aeonbits.owner.Config;

public class ConfigFactory {
    private static final UserConfig CONFIG = org.aeonbits.owner.ConfigFactory.create(UserConfig.class, System.getProperties());

    public static final String
    // URLs
    BASE_URL_E2E = CONFIG.baseURLE2E(), BASE_URL_VANTAGE_ACCOUNT = CONFIG.baseUrlVantageAccount(),
            // Kafka
            KAFKA_PUBLIC = CONFIG.testClusterKafkaPublic(), KAFKA_PRIVATE = CONFIG.testClusterKafkaPrivate(),
            KAFKA_PASSWORD = CONFIG.testClusterKafkaPassword(),
            // Settings
            PATH_TRACE_VIDEO = CONFIG.pathTraceVideo(), PATH_TRACE = CONFIG.pathTrace(),
            PATH_BASELINE_SCREENSHOT = CONFIG.pathBaselineScreenshot(),
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
            // Clickhouse api
            CLICKHOUSE_API_BASE_PATH = CONFIG.clickhouseBasePath(),
            CLICKHOUSE_API_GET_CLIENT_PATH = CONFIG.clickhouseGetClientPath(),
            CLICKHOUSE_API_GET_CLIENTS_PATH = CONFIG.clickhouseGetClientsPath(),
            CLICKHOUSE_API_GET_LEXIS_NEXIS_PATH = CONFIG.clickhouseGetLexisNexisPath(),
            // Connection search
            CONNECTION_SEARCH_BASE_PATH = CONFIG.connectionSearchBasePath(),
            CONNECTION_SEARCH_GET_CONNECTIONS_BY_CLIENT = CONFIG.connectionSearchGetConnectionsByClient(),
            CONNECTION_SEARCH_GET_CONNECTIONS_BY_ATTRIBUTES = CONFIG.connectionSearchGetConnectionsByAttributes(),
            // Clickhouse database
            CLICKHOUSE_HOST = CONFIG.clickhouseHost(), CLICKHOUSE_USER = CONFIG.clickhouseUser(),
            CLICKHOUSE_PASSWORD = CONFIG.clickhousePassword(),
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

        @Key("pathTraceVideo")
        String pathTraceVideo();

        @Key("pathTrace")
        String pathTrace();

        @Key("pathBaselineScreenshot")
        String pathBaselineScreenshot();

        // Kafka

        @Key("testClusterKafkaPublic")
        String testClusterKafkaPublic();

        @Key("testClusterKafkaPrivate")
        String testClusterKafkaPrivate();

        @Key("testClusterKafkaPassword")
        String testClusterKafkaPassword();

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

        // Clickhouse database

        @Key("clickhouseHost")
        String clickhouseHost();

        @Key("clickhouseUser")
        String clickhouseUser();

        @Key("clickhousePassword")
        String clickhousePassword();

        // Clickhouse api helper

        @Key("clickhouseBasePath")
        String clickhouseBasePath();

        @Key("clickhouseGetClientPath")
        String clickhouseGetClientPath();

        @Key("clickhouseGetClientsPath")
        String clickhouseGetClientsPath();

        @Key("clickhouseGetLexisNexisPath")
        String clickhouseGetLexisNexisPath();

        // Connection search

        @Key("connectionSearchBasePath")
        String connectionSearchBasePath();

        @Key("connectionSearchGetConnectionsByClient")
        String connectionSearchGetConnectionsByClient();

        @Key("connectionSearchGetConnectionsByAttributes")
        String connectionSearchGetConnectionsByAttributes();

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
