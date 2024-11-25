package utils;

import java.util.Objects;

import org.aeonbits.owner.Config;

public class ConfigFactory {
    private static final UserConfig CONFIG = org.aeonbits.owner.ConfigFactory.create(UserConfig.class, System.getProperties());

    // URLs
    public static final String BASE_URL_E2E = CONFIG.baseURLE2E();
    public static final String BASE_URL_VANTAGE_ACCOUNT = CONFIG.baseUrlVantageAccount();
    // Kafka
    public static final String  KAFKA_PUBLIC = CONFIG.testClusterKafkaPublic();
    public static final String KAFKA_PRIVATE = CONFIG.testClusterKafkaPrivate();
    public static final String KAFKA_PASSWORD = CONFIG.testClusterKafkaPassword();
    // Settings
    public static final String PATH_TRACE_VIDEO = CONFIG.pathTraceVideo();
    public static final String PATH_TRACE = CONFIG.pathTrace();
    public static final String PATH_BASELINE_SCREENSHOT = CONFIG.pathBaselineScreenshot();
    // Databases
    public static final String POSTGRE_HOST = CONFIG.postgreHost();
    public static final String POSTGRE_PASSWORD = CONFIG.postgrePassword();
    public static final String POSTGRE_LOGIN = CONFIG.postgreLogin();
    public static final String MYSQL_STAGING_CRM_HOST = CONFIG.mysqlStagingCrmHost();
    public static final String MYSQL_STAGING_CRM_LOGIN = CONFIG.mysqlStagingCrmLogin();
    public static final String MYSQL_STAGING_CRM_PASSWORD = CONFIG.mysqlStagingCrmPassword();
    // Registration helper
    public static final String REGISTRATION_HELPER_LOGIN = CONFIG.registrationHelperLogin();
    public static final String REGISTRATION_HELPER_PASSWORD = CONFIG.registrationHelperPassword();
    // Clickhouse api
    public static final String CLICKHOUSE_API_BASE_PATH = CONFIG.clickhouseBasePath();
    public static final String CLICKHOUSE_API_GET_CLIENT_PATH = CONFIG.clickhouseGetClientPath();
    public static final String CLICKHOUSE_API_GET_CLIENTS_PATH = CONFIG.clickhouseGetClientsPath();
    public static final String CLICKHOUSE_API_GET_LEXIS_NEXIS_PATH = CONFIG.clickhouseGetLexisNexisPath();
    public static final String CLICKHOUSE_API_GET_TRADES_GROUP_BY = CONFIG.clickhouseGetTradesGroupByPath();
    public static final String CLICKHOUSE_API_GET_TRADES = CONFIG.clickhouseGetTradesPath();
    public static final String CLICKHOUSE_API_GET_DEPOSITS = CONFIG.clickhouseGetDepositsPath();
    public static final String CLICKHOUSE_API_GET_WITHDRAWALS = CONFIG.clickhouseGetWithdrawalsPath();
    public static final String CLICKHOUSE_API_GET_CREDITS = CONFIG.clickhouseGetCreditsPath();
    public static final String CLICKHOUSE_API_GET_BONUSES = CONFIG.clickhouseGetBonusesPath();
    public static final String CLICKHOUSE_API_GET_CREDIT_EQUITY_PATH = CONFIG.clickhouseGetCreditEquityRatio();
    public static final String CLICKHOUSE_API_GET_FLOATING_TRADES_GROUP_BY_PATH = CONFIG.clickhouseGetFloatingTradesGroupBy();
    public static final String CLICKHOUSE_API_GET_MIRROR_ACCOUNTS_BY_TRADES_PATH = CONFIG.clickhouseGetMirrorAccountsByTrades();
    // Connection search
    public static final String CONNECTION_SEARCH_BASE_PATH = CONFIG.connectionSearchBasePath();
    public static final String CONNECTION_SEARCH_GET_CONNECTIONS_BY_CLIENT = CONFIG.connectionSearchGetConnectionsByClient();
    public static final String CONNECTION_SEARCH_GET_CONNECTIONS_BY_ATTRIBUTES = CONFIG.connectionSearchGetConnectionsByAttributes();
    // Mitigation service
    public static final String MITIGATION_SERVICE_BASE_PATH = CONFIG.mitigationServiceBasePath();
    public static final String MITIGATION_SERVICE_GET_RESTRICTION_CATALOG = CONFIG.mitigationServiceGetRestrictionCatalog();
    public static final String MITIGATION_SERVICE_RESTRICTIONS = CONFIG.mitigationServiceRestrictions();
    public static final String MITIGATION_SERVICE_CANCEL_RESTRICTION = CONFIG.mitigationServiceCancelRestriction();
    // Mitigation service db
    public static final String MITIGATION_DB_SSH_HOST = CONFIG.mitigationDbSshHost();
    public static final String MITIGATION_DB_SSH_PORT = String.valueOf(CONFIG.mitigationDbSshPort());
    public static final String MITIGATION_DB_SSH_USER = CONFIG.mitigationDbSshUser();
    public static final String MITIGATION_DB_SSH_PRIVATE_KEY = CONFIG.mitigationDbSshPrivateKey();
    public static final String MITIGATION_DB_HOST = CONFIG.mitigationDbHost(); // As seen from the SSH server
    public static final String MITIGATION_DB_PORT = String.valueOf(CONFIG.mitigationDbPort());
    public static final String MITIGATION_DB_USER = CONFIG.mitigationDbUser();
    public static final String MITIGATION_DB_PASSWORD = CONFIG.mitigationDbPassword();
    public static final String MITIGATION_DB_NAME = CONFIG.mitigationDbName();
    // Audit service db
    public static final String AUDIT_DB_USER = CONFIG.auditDbUser();
    public static final String AUDIT_DB_PASSWORD = CONFIG.auditDbPassword();
    public static final String AUDIT_DB_NAME = CONFIG.auditDbName();
    // BO service db
    public static final String BO_DB_USER = CONFIG.boDbUser();
    public static final String BO_DB_PASSWORD = CONFIG.boDbPassword();
    public static final String BO_DB_NAME = CONFIG.boDbName();
    // BO DB general
    public static final String POSTGRE_DB_HOST = CONFIG.postgreDBHost();
    // Clickhouse database
    public static final String CLICKHOUSE_HOST = CONFIG.clickhouseHost();
    public static final String CLICKHOUSE_USER = CONFIG.clickhouseUser();
    public static final String CLICKHOUSE_PASSWORD = CONFIG.clickhousePassword();
    // Other
    public static final String COUNTRY_MALAYSIA = CONFIG.countryMalaysia();

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

        @Key("clickhouseGetTradesGroupByPath")
        String clickhouseGetTradesGroupByPath();

        @Key("clickhouseGetTradesPath")
        String clickhouseGetTradesPath();

        @Key("clickhouseGetDepositsPath")
        String clickhouseGetDepositsPath();

        @Key("clickhouseGetWithdrawalsPath")
        String clickhouseGetWithdrawalsPath();

        @Key("clickhouseGetCreditsPath")
        String clickhouseGetCreditsPath();

        @Key("clickhouseGetBonusesPath")
        String clickhouseGetBonusesPath();

        @Key("clickhouseGetLexisNexisPath")
        String clickhouseGetLexisNexisPath();

        @Key("clickhouseGetCreditEquityRatio")
        String clickhouseGetCreditEquityRatio();

        @Key("clickhouseGetFloatingTradesGroupBy")
        String clickhouseGetFloatingTradesGroupBy();

        @Key("clickhouseGetMirrorAccountsByTrades")
        String clickhouseGetMirrorAccountsByTrades();

        // Connection search

        @Key("connectionSearchBasePath")
        String connectionSearchBasePath();

        @Key("connectionSearchGetConnectionsByClient")
        String connectionSearchGetConnectionsByClient();

        @Key("connectionSearchGetConnectionsByAttributes")
        String connectionSearchGetConnectionsByAttributes();

        // Mitigation service

        @Key("mitigationServiceBasePath")
        String mitigationServiceBasePath();

        @Key("mitigationServiceGetRestrictionCatalog")
        String mitigationServiceGetRestrictionCatalog();

        @Key("mitigationServiceRestrictions")
        String mitigationServiceRestrictions();

        @Key("mitigationServiceCancelRestriction")
        String mitigationServiceCancelRestriction();

        // Mitigation service db
        @Key("mitigationDbSshHost")
        String mitigationDbSshHost();

        @Key("mitigationDbSshPort")
        int mitigationDbSshPort();

        @Key("mitigationDbSshUser")
        String mitigationDbSshUser();

        @Key("mitigationDbSshPrivateKey")
        String mitigationDbSshPrivateKey();

        @Key("mitigationDbHost")
        String mitigationDbHost();

        @Key("mitigationDbPort")
        int mitigationDbPort();

        @Key("mitigationDbUser")
        String mitigationDbUser();

        @Key("mitigationDbPassword")
        String mitigationDbPassword();

        @Key("mitigationDbName")
        String mitigationDbName();

        // Audit service db

        @Key("auditDbUser")
        String auditDbUser();

        @Key("auditDbPassword")
        String auditDbPassword();

        @Key("auditDbName")
        String auditDbName();

        // BO service db

        @Key("boDbUser")
        String boDbUser();

        @Key("boDbPassword")
        String boDbPassword();

        @Key("boDbName")
        String boDbName();

        // DO DB general

        @Key("postgreDBHost")
        String postgreDBHost();


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
