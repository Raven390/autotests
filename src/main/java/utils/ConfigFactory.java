package utils;

import java.util.Objects;

import org.aeonbits.owner.Config;

public class ConfigFactory {
    private static final UserConfig CONFIG = org.aeonbits.owner.ConfigFactory.create(UserConfig.class, System.getProperties());

    // URLs
    public static final String BASE_URL_E2E = CONFIG.baseURLE2E();
    public static final String ENTER_PAGE_E2E = CONFIG.enterPageE2E();
    public static final String BASE_URL_VANTAGE_ACCOUNT = CONFIG.baseUrlVantageAccount();
    public static final String BASE_URL_CRM_EMULATOR = CONFIG.crmEmulatorBaseUrl();
    // Kafka
    public static final String KAFKA_PUBLIC = CONFIG.testClusterKafkaPublic();
    public static final String KAFKA_PRIVATE = CONFIG.testClusterKafkaPrivate();
    public static final String KAFKA_PASSWORD = CONFIG.testClusterKafkaPassword();
    // Settings
    public static final String PATH_TRACE_VIDEO = CONFIG.pathTraceVideo();
    public static final String PATH_TRACE = CONFIG.pathTrace();
    public static final String PATH_BASELINE_SCREENSHOT = CONFIG.pathBaselineScreenshot();
    // Databases
    public static final String MYSQL_STAGING_CRM_HOST = CONFIG.mysqlStagingCrmHost();
    public static final String MYSQL_STAGING_CRM_LOGIN = CONFIG.mysqlStagingCrmLogin();
    public static final String MYSQL_STAGING_CRM_PASSWORD = CONFIG.mysqlStagingCrmPassword();
    // Registration helper
    public static final String REGISTRATION_HELPER_LOGIN = CONFIG.registrationHelperLogin();
    public static final String REGISTRATION_HELPER_PASSWORD = CONFIG.registrationHelperPassword();
    // Clickhouse api
    public static final String CLICKHOUSE_API_BASE_TEST = CONFIG.clickhouseBasePathTest();
    public static final String CLICKHOUSE_API_BASE_PROD = CONFIG.clickhouseBasePathProd();
    public static final String CLICKHOUSE_API_GET_CLIENT = CONFIG.clickhouseGetClientPath();
    public static final String CLICKHOUSE_API_GET_CLIENTS = CONFIG.clickhouseGetClientsPath();
    public static final String CLICKHOUSE_API_GET_CLIENTS_V2 = CONFIG.clickhouseGetClientsV2Path();
    public static final String CLICKHOUSE_API_GET_CLIENT_TRADING_ACCOUNTS = CONFIG.clickhouseGetClientTradingAccountsPath();
    public static final String CLICKHOUSE_API_GET_LEXIS_NEXIS = CONFIG.clickhouseGetLexisNexisPath();
    public static final String CLICKHOUSE_API_GET_LEXIS_NEXIS_DATA = CONFIG.clickhouseGetLexisNexisDataPath();
    public static final String CLICKHOUSE_API_GET_TRADES_GROUP_BY = CONFIG.clickhouseGetTradesGroupByPath();
    public static final String CLICKHOUSE_API_GET_TRADES = CONFIG.clickhouseGetTradesPath();
    public static final String CLICKHOUSE_API_GET_UNCLOSED_TRADES = CONFIG.clickhouseGetUnclosedTradesPath();
    public static final String CLICKHOUSE_API_GET_DEPOSITS = CONFIG.clickhouseGetDepositsPath();
    public static final String CLICKHOUSE_API_GET_WITHDRAWALS = CONFIG.clickhouseGetWithdrawalsPath();
    public static final String CLICKHOUSE_API_GET_CREDITS = CONFIG.clickhouseGetCreditsPath();
    public static final String CLICKHOUSE_API_GET_BONUSES = CONFIG.clickhouseGetBonusesPath();
    public static final String CLICKHOUSE_API_GET_CREDIT_EQUITY = CONFIG.clickhouseGetCreditEquityRatio();
    public static final String CLICKHOUSE_API_GET_FLOATING_TRADES_GROUP_BY = CONFIG.clickhouseGetFloatingTradesGroupBy();
    public static final String CLICKHOUSE_API_GET_MIRROR_ACCOUNTS_BY_TRADES = CONFIG.clickhouseGetMirrorAccountsByTrades();
    public static final String CLICKHOUSE_API_GET_ABUSE_TYPES = CONFIG.clickhouseGetAbuseTypes();
    public static final String CLICKHOUSE_API_GET_CREDIT_RISK_FREE_REVENUE_RATIO = CONFIG.clickhouseGetCreditRiskFreeRevenueRatio();
    public static final String CLICKHOUSE_API_GET_SWAP_FREE_FEES = CONFIG.clickhouseGetSwapFreeFees();
    public static final String CLICKHOUSE_API_GET_SWAP_FREE_VOLUMES = CONFIG.clickhouseGetSwapFreeVolumes();
    public static final String CLICKHOUSE_API_GET_BALANCE_ORDERS = CONFIG.clickhouseGetBalanceOrders();
    public static final String CLICKHOUSE_API_GET_FAST_TRADES = CONFIG.clickhouseGetFastTrades();
    public static final String CLICKHOUSE_API_GET_LEXIS_NEXIS_V2 = CONFIG.clickhouseGetLexisNexisV2();
    public static final String CLICKHOUSE_API_GET_TRADES_BY_TRADE_ID = CONFIG.clickhouseGetTradesByTradeId();
    public static final String CLICKHOUSE_API_GET_TOTAL_LOYALTIES = CONFIG.clickhouseGetTotalLoyalties();
    public static final String CLICKHOUSE_API_GET_MIRROR_CLIENTS_BY_TRADES = CONFIG.clickhouseGetMirrorClientsByTrades();
    public static final String CLICKHOUSE_API_GET_FINANCIAL_CALENDAR = CONFIG.clickhouseGetFinancialCalendar();
    public static final String CLICKHOUSE_API_GET_DUMMY_TRADE_DATA = CONFIG.clickhouseGetDummyTradeData();
    public static final String CLICKHOUSE_API_GET_ACCOUNT_BALANCE = CONFIG.clickhouseGetAccountBalance();
    public static final String CLICKHOUSE_API_GET_ABNORMAL_PROFIT = CONFIG.clickhouseGetAbnormalProfit();
    public static final String CLICKHOUSE_API_GET_WINNING_DEALS_COUNT = CONFIG.clickhouseGetWinningDealsCount();
    public static final String CLICKHOUSE_API_GET_VERIFY_TRADING_ACCOUNT = CONFIG.clickhouseGetVerifyTradingAccount();
    public static final String CLICKHOUSE_API_GET_STOPOUT_TRADES_RATIO = CONFIG.clickhouseGetStopoutTradesRatio();
    public static final String CLICKHOUSE_API_GET_SLIPPAGE_AMOUNT = CONFIG.clickhouseGetSlippageAmount();
    public static final String CLICKHOUSE_API_GET_SHORT_TOXICITY = CONFIG.clickhouseGetShortToxicity();
    public static final String CLICKHOUSE_API_GET_REBATE_AMOUNT = CONFIG.clickhouseGetRebateAmount();
    public static final String CLICKHOUSE_API_GET_PROFIT_TO_CAPITAL_RATIO = CONFIG.clickhouseGetProfitToCapital();
    public static final String CLICKHOUSE_API_GET_NOTIONAL_VALUE_AMOUNT = CONFIG.clickhouseGetNotionalValueAmount();
    public static final String CLICKHOUSE_API_GET_NET_PROFIT = CONFIG.clickhouseGetNetProfit();
    public static final String CLICKHOUSE_API_GET_NAME_BIRTH = CONFIG.clickhouseGetNameBirth();
    public static final String CLICKHOUSE_API_GET_MIRROR_TRADE_WAVES = CONFIG.clickhouseGetMirrorTradeWaves();
    public static final String CLICKHOUSE_API_GET_MIRROR_SCORE = CONFIG.clickhouseGetMirrorScore();
    public static final String CLICKHOUSE_API_GET_MAX_DAILY_SLIPPAGE_AMOUNT = CONFIG.clickhouseGetMaxDailySlippageAmount();
    public static final String CLICKHOUSE_API_GET_MARKET_MANIPULATOR_FLAG = CONFIG.clickhouseGetMarketManipulatorFlag();
    public static final String CLICKHOUSE_API_GET_FAST_TRADES_AND_TOTAL_COUNT = CONFIG.clickhouseGetFastTradesAndTotalCount();
    public static final String CLICKHOUSE_API_GET_CUMULATIVE_DEPOSITS = CONFIG.clickhouseGetCumulativeDeposits();
    public static final String CLICKHOUSE_API_GET_COUNT_TRADING_DAYS = CONFIG.clickhouseGetCountTradingDays();
    public static final String CLICKHOUSE_API_GET_COUNT_CPA = CONFIG.clickhouseGetCountCpa();
    public static final String CLICKHOUSE_API_GET_CHARGEBACK_SCORE = CONFIG.clickhouseGetChargebackScore();
    public static final String CLICKHOUSE_API_GET_ALERTS = CONFIG.clickhouseGetAlerts();
    // Rule engine
    public static final String RULE_ENGINE_PATH_TEST_ENV = CONFIG.ruleEngineBasePathTest();
    public static final String RULE_ENGINE_GET_BRANDS = CONFIG.ruleEngineGetBrands();
    public static final String RULE_ENGINE_GET_EVENTS = CONFIG.ruleEngineGetEvents();
    public static final String RULE_ENGINE_RULES = CONFIG.ruleEngineRules();
    public static final String RULE_ENGINE_RULE_DEPLOYMENTS = CONFIG.ruleEngineRuleDeployments();
    public static final String RULE_ENGINE_RULE_DEPLOYMENT_BY_UUID = CONFIG.ruleEngineRuleDeploymentsByUuid();
    // Mirror trading score service
    public static final String MIRROR_TRADING_SCORE_SERVICE_GET_SCORE = CONFIG.mirrorTradingScoreGetScore();
    // Rule engine database
    public static final String RULE_ENGINE_DB_NAME = CONFIG.ruleEngineDbName();
    public static final String RULE_ENGINE_DB_USER = CONFIG.ruleEngineDbUser();
    public static final String RULE_ENGINE_DB_PASSWORD = CONFIG.ruleEngineDbPassword();
    // Connection search
    public static final String CONNECTION_SEARCH_BASE_PATH_TEST = CONFIG.connectionSearchBasePathTest();
    public static final String CONNECTION_SEARCH_BASE_PATH_PROD = CONFIG.connectionSearchBasePathTest();
    public static final String CONNECTION_SEARCH_GET_CONNECTIONS_BY_CLIENT = CONFIG.connectionSearchGetConnectionsByClient();
    public static final String CONNECTION_SEARCH_GET_CONNECTIONS_BY_ATTRIBUTES = CONFIG.connectionSearchGetConnectionsByAttributes();
    public static final String CONNECTION_SEARCH_GET_ABUSE_TYPES_BY_CLIENT = CONFIG.connectionSearchGetAbuseTypesByClient();
    public static final String CONNECTION_SEARCH_GET_ABUSE_TYPES_BY_ATTRIBUTES = CONFIG.connectionSearchGetAbuseTypesByAttributes();
    public static final String CONNECTION_SEARCH_GET_CHECK_CONNECTED_IB = CONFIG.connectionSearchGetCheckConnectedIb();
    // Mitigation service
    public static final String MITIGATION_SERVICE_BASE_PATH = CONFIG.mitigationServiceBasePath();
    public static final String MITIGATION_SERVICE_INSIGHT_BASE_PATH = CONFIG.mitigationServiceInsightBasePath();
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
    // Postrgres db
    public static final String POSTGRES_DB_SSH_HOST = CONFIG.postgresDbSshHost();
    public static final String POSTGRES_DB_SSH_PORT = String.valueOf(CONFIG.postgresDbSshPort());
    public static final String POSTGRES_DB_SSH_USER = CONFIG.postgresDbSshUser();
    public static final String POSTGRES_DB_SSH_PRIVATE_KEY = CONFIG.postgresDbSshPrivateKey();
    public static final String POSTGRES_DB_HOST = CONFIG.postgresDbHost(); // As seen from the SSH server
    public static final String POSTGRES_DB_PORT = String.valueOf(CONFIG.postgresDbPort());
    public static final String POSTGRES_DB_USER = CONFIG.postgresDbUser();
    public static final String POSTGRES_DB_PASSWORD = CONFIG.postgresDbPassword();
    public static final String POSTGRES_DB_NAME = CONFIG.postgresDbName();
    // Data science db
    public static final String DATA_SCIENCE_MIRROR_LOGIN = CONFIG.mitigationDbName();
    // Audit service db
    public static final String AUDIT_DB_USER = CONFIG.auditDbUser();
    public static final String AUDIT_DB_PASSWORD = CONFIG.auditDbPassword();
    public static final String AUDIT_DB_NAME = CONFIG.auditDbName();
    // Backoffice service db
    public static final String BACKOFFICE_BO_DB_USER = CONFIG.backofficeDbUser();
    public static final String BACKOFFICE_DB_PASSWORD = CONFIG.backofficeDbPassword();
    public static final String BACKOFFICE_DB_NAME = CONFIG.backofficeDbName();
    // Clickhouse database
    public static final String CLICKHOUSE_HOST = CONFIG.clickhouseHost();
    public static final String CLICKHOUSE_USER = CONFIG.clickhouseUser();
    public static final String CLICKHOUSE_PASSWORD = CONFIG.clickhousePassword();
    // Backoffice test user
    public static final String CRM_INTEGRATION_USER_UCID = CONFIG.crmIntegrationUserUcid();
    // Other
    public static final String COUNTRY_MALAYSIA = CONFIG.countryMalaysia();
    public static final Double TIMEOUT = CONFIG.waitTimeout();
    // Backoffice UI users
    public static final String ID_FIRST_LOGIN = CONFIG.idFirstLogin();
    public static final String USERNAME_FIRST_LOGIN = CONFIG.usernameFirstLogin();
    public static final String PASSWORD_FIRST_LOGIN = CONFIG.passwordFirstLogin();
    public static final String FIRST_NAME_FIRST_LOGIN = CONFIG.firstNameFirstLogin();
    public static final String LAST_NAME_FIRST_LOGIN = CONFIG.lastNameFirstLogin();
    public static final String EMAIL_FIRST_LOGIN = CONFIG.emailFirstLogin();
    public static final String ID_CORE = CONFIG.idCore();
    public static final String USERNAME_CORE = CONFIG.usernameCore();
    public static final String PASSWORD_CORE = CONFIG.passwordCore();
    public static final String FIRST_NAME_CORE = CONFIG.firstNameCore();
    public static final String LAST_NAME_CORE = CONFIG.lastNameCore();
    public static final String EMAIL_CORE = CONFIG.emailCore();
    public static final String ID_DEV = CONFIG.idDev();
    public static final String USERNAME_DEV = CONFIG.usernameDev();
    public static final String PASSWORD_DEV = CONFIG.passwordDev();
    public static final String FIRST_NAME_DEV = CONFIG.firstNameDev();
    public static final String LAST_NAME_DEV = CONFIG.lastNameDev();
    public static final String EMAIL_DEV = CONFIG.emailDev();
    public static final String ID_AUTOTEST_ONE = CONFIG.idAutotestOne();
    public static final String USERNAME_AUTOTEST_ONE = CONFIG.usernameAutotestOne();
    public static final String PASSWORD_AUTOTEST_ONE = CONFIG.passwordAutotestOne();
    public static final String USERNAME_AUTOTEST_AF = CONFIG.usernameAutotestAF();
    public static final String PASSWORD_AUTOTEST_AF = CONFIG.passwordAutotestAF();
    public static final String FIRST_NAME_AUTOTEST_ONE = CONFIG.firstNameAutotestOne();
    public static final String LAST_NAME_AUTOTEST_ONE = CONFIG.lastNameAutotestOne();
    public static final String EMAIL_AUTOTEST_ONE = CONFIG.emailAutotestOne();
    // Abuse registry
    public static final String ABUSE_REGISTRY_BASE_PATH = CONFIG.abuseRegistryBasePath();
    public static final String ABUSE_REGISTRY_V2_BASE_PATH = CONFIG.abuseRegistryV2BasePath();
    public static final String ABUSE_REGISTRY_POST_FRAUD_TYPES = CONFIG.abuseRegistryPostFraudTypes();
    public static final String ABUSE_REGISTRY_POST_ABUSER_STATUS = CONFIG.abuseRegistryPostAbuserStatus();

    @Config.Sources({"classpath:config/config.properties", "system:properties"})
    public interface UserConfig extends Config {

        // URLs

        @Key("baseUrlE2e")
        String baseURLE2E();

        @Key("enterPageE2E")
        String enterPageE2E();

        @Key("baseUrlVantageAccount")
        String baseUrlVantageAccount();

        @Key("crmEmulatorBaseUrl")
        String crmEmulatorBaseUrl();

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

        @Key("clickhouseBasePathTest")
        String clickhouseBasePathTest();

        @Key("clickhouseGetClientPath")
        String clickhouseGetClientPath();

        @Key("clickhouseBasePathProd")
        String clickhouseBasePathProd();

        @Key("clickhouseGetClientsPath")
        String clickhouseGetClientsPath();

        @Key("clickhouseGetClientsV2Path")
        String clickhouseGetClientsV2Path();

        @Key("clickhouseGetClientTradingAccountsPath")
        String clickhouseGetClientTradingAccountsPath();

        @Key("clickhouseGetTradesGroupByPath")
        String clickhouseGetTradesGroupByPath();

        @Key("clickhouseGetTradesPath")
        String clickhouseGetTradesPath();

        @Key("clickhouseGetUnclosedTradesPath")
        String clickhouseGetUnclosedTradesPath();

        @Key("clickhouseGetDepositsPath")
        String clickhouseGetDepositsPath();

        @Key("clickhouseGetWithdrawalsPath")
        String clickhouseGetWithdrawalsPath();

        @Key("clickhouseGetCreditsPath")
        String clickhouseGetCreditsPath();

        @Key("clickhouseGetBonusesPath")
        String clickhouseGetBonusesPath();

        @Key("clickhouseGetLexisNexisDataPath")
        String clickhouseGetLexisNexisDataPath();

        @Key("clickhouseGetLexisNexisPath")
        String clickhouseGetLexisNexisPath();

        @Key("clickhouseGetCreditEquityRatio")
        String clickhouseGetCreditEquityRatio();

        @Key("clickhouseGetFloatingTradesGroupBy")
        String clickhouseGetFloatingTradesGroupBy();

        @Key("clickhouseGetMirrorAccountsByTrades")
        String clickhouseGetMirrorAccountsByTrades();

        @Key("clickhouseGetAbuseTypes")
        String clickhouseGetAbuseTypes();

        @Key("clickhouseGetCreditRiskFreeRevenueRatio")
        String clickhouseGetCreditRiskFreeRevenueRatio();

        @Key("clickhouseGetSwapFreeFees")
        String clickhouseGetSwapFreeFees();

        @Key("clickhouseGetSwapFreeVolumes")
        String clickhouseGetSwapFreeVolumes();

        @Key("clickhouseGetBalanceOrders")
        String clickhouseGetBalanceOrders();

        @Key("clickhouseGetFastTrades")
        String clickhouseGetFastTrades();

        @Key("clickhouseGetLexisNexisV2")
        String clickhouseGetLexisNexisV2();

        @Key("clickhouseGetTradesByTradeId")
        String clickhouseGetTradesByTradeId();

        @Key("clickhouseGetTotalLoyalties")
        String clickhouseGetTotalLoyalties();

        @Key("clickhouseGetMirrorClientsByTrades")
        String clickhouseGetMirrorClientsByTrades();

        @Key("clickhouseGetFinancialCalendar")
        String clickhouseGetFinancialCalendar();

        @Key("clickhouseGetDummyTradeData")
        String clickhouseGetDummyTradeData();

        @Key("clickhouseGetAccountBalance")
        String clickhouseGetAccountBalance();

        @Key("clickhouseGetAbnormalProfit")
        String clickhouseGetAbnormalProfit();

        @Key("clickhouseGetWinningDealsCount")
        String clickhouseGetWinningDealsCount();

        @Key("clickhouseGetVerifyTradingAccount")
        String clickhouseGetVerifyTradingAccount();

        @Key("clickhouseGetStopoutTradesRatio")
        String clickhouseGetStopoutTradesRatio();

        @Key("clickhouseGetSlippageAmount")
        String clickhouseGetSlippageAmount();

        @Key("clickhouseGetShortToxicity")
        String clickhouseGetShortToxicity();

        @Key("clickhouseGetRebateAmount")
        String clickhouseGetRebateAmount();

        @Key("clickhouseGetProfitToCapital")
        String clickhouseGetProfitToCapital();

        @Key("clickhouseGetNotionalValueAmount")
        String clickhouseGetNotionalValueAmount();

        @Key("clickhouseGetNetProfit")
        String clickhouseGetNetProfit();

        @Key("clickhouseGetNameBirth")
        String clickhouseGetNameBirth();

        @Key("clickhouseGetMirrorTradeWaves")
        String clickhouseGetMirrorTradeWaves();

        @Key("clickhouseGetMirrorScore")
        String clickhouseGetMirrorScore();

        @Key("clickhouseGetMaxDailySlippageAmount")
        String clickhouseGetMaxDailySlippageAmount();

        @Key("clickhouseGetMarketManipulatorFlag")
        String clickhouseGetMarketManipulatorFlag();

        @Key("clickhouseGetFastTradesAndTotalCount")
        String clickhouseGetFastTradesAndTotalCount();

        @Key("clickhouseGetCumulativeDeposits")
        String clickhouseGetCumulativeDeposits();

        @Key("clickhouseGetCountTradingDays")
        String clickhouseGetCountTradingDays();

        @Key("clickhouseGetCountCpa")
        String clickhouseGetCountCpa();

        @Key("clickhouseGetChargebackScore")
        String clickhouseGetChargebackScore();

        @Key("clickhouseGetAlerts")
        String clickhouseGetAlerts();

        // Rule engine

        @Key("ruleEngineBasePathTest")
        String ruleEngineBasePathTest();

        @Key("ruleEngineGetBrands")
        String ruleEngineGetBrands();

        @Key("ruleEngineGetEvents")
        String ruleEngineGetEvents();

        @Key("ruleEngineRules")
        String ruleEngineRules();

        @Key("ruleEngineRuleDeployment")
        String ruleEngineRuleDeployments();

        @Key("ruleEngineRuleDeploymentByUuid")
        String ruleEngineRuleDeploymentsByUuid();

        // Mirror trading score service

        @Key("mirrorTradingScoreGetScore")
        String mirrorTradingScoreGetScore();

        // Rule engine database

        @Key("ruleEngineDbName")
        String ruleEngineDbName();

        @Key("ruleEngineDbUser")
        String ruleEngineDbUser();

        @Key("ruleEngineDbPassword")
        String ruleEngineDbPassword();

        // Connection search

        @Key("connectionSearchBasePathTest")
        String connectionSearchBasePathTest();

        @Key("connectionSearchBasePathProd")
        String connectionSearchBasePathProd();

        @Key("connectionSearchGetConnectionsByClient")
        String connectionSearchGetConnectionsByClient();

        @Key("connectionSearchGetConnectionsByAttributes")
        String connectionSearchGetConnectionsByAttributes();

        @Key("connectionSearchGetAbuseTypesByClient")
        String connectionSearchGetAbuseTypesByClient();

        @Key("connectionSearchGetAbuseTypesByAttributes")
        String connectionSearchGetAbuseTypesByAttributes();

        @Key("connectionSearchGetCheckConnectedIb")
        String connectionSearchGetCheckConnectedIb();

        // Mitigation service

        @Key("mitigationServiceBasePath")
        String mitigationServiceBasePath();

        @Key("mitigationServiceInsightBasePath")
        String mitigationServiceInsightBasePath();

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

        // Postgres db

        @Key("postgresDbSshHost")
        String postgresDbSshHost();

        @Key("postgresDbSshPort")
        int postgresDbSshPort();

        @Key("postgresDbSshUser")
        String postgresDbSshUser();

        @Key("postgresDbSshPrivateKey")
        String postgresDbSshPrivateKey();

        @Key("postgresDbHost")
        String postgresDbHost();

        @Key("postgresDbPort")
        int postgresDbPort();

        @Key("postgresDbUser")
        String postgresDbUser();

        @Key("postgresDbPassword")
        String postgresDbPassword();

        @Key("postgresDbName")
        String postgresDbName();

        // Audit service db

        @Key("auditDbUser")
        String auditDbUser();

        @Key("auditDbPassword")
        String auditDbPassword();

        @Key("auditDbName")
        String auditDbName();

        // Backoffice service db

        @Key("backofficeDbUser")
        String backofficeDbUser();

        @Key("backofficeDbPassword")
        String backofficeDbPassword();

        @Key("backofficeDbName")
        String backofficeDbName();

        // Backoffice DB general?

        @Key("postgresDBHost")
        String postgresDBHost();

        // Other

        @Key("countryMalaysia")
        String countryMalaysia();

        // Test users

        @Key("crmIntegrationUserUcid")
        String crmIntegrationUserUcid();

        @Key("crmIntegrationUserId")
        String crmIntegrationUserId();

        // Backoffice UI users

        @Key("idFirstLogin")
        String idFirstLogin();

        @Key("usernameFirstLogin")
        String usernameFirstLogin();

        @Key("passwordFirstLogin")
        String passwordFirstLogin();

        @Key("firstNameFirstLogin")
        String firstNameFirstLogin();

        @Key("lastNameFirstLogin")
        String lastNameFirstLogin();

        @Key("emailFirstLogin")
        String emailFirstLogin();

        @Key("idCore")
        String idCore();

        @Key("usernameCore")
        String usernameCore();

        @Key("passwordCore")
        String passwordCore();

        @Key("emailCore")
        String emailCore();

        @Key("firstNameCore")
        String firstNameCore();

        @Key("lastNameCore")
        String lastNameCore();

        @Key("idDev")
        String idDev();

        @Key("usernameDev")
        String usernameDev();

        @Key("passwordDev")
        String passwordDev();

        @Key("emailDev")
        String emailDev();

        @Key("firstNameDev")
        String firstNameDev();

        @Key("lastNameDev")
        String lastNameDev();

        @Key("idAutotestOne")
        String idAutotestOne();

        @Key("usernameAutotestOne")
        String usernameAutotestOne();

        @Key("passwordAutotestOne")
        String passwordAutotestOne();

        @Key("usernameAutotestAF")
        String usernameAutotestAF();

        @Key("passwordAutotestAF")
        String passwordAutotestAF();

        @Key("emailAutotestOne")
        String emailAutotestOne();

        @Key("firstNameAutotestOne")
        String firstNameAutotestOne();

        @Key("lastNameAutotestOne")
        String lastNameAutotestOne();

        @Key("abuseRegistryBasePath")
        String abuseRegistryBasePath();

        @Key("abuseRegistryV2BasePath")
        String abuseRegistryV2BasePath();

        @Key("abuseRegistryPostFraudTypes")
        String abuseRegistryPostFraudTypes();

        @Key("abuseRegistryPostAbuserStatus")
        String abuseRegistryPostAbuserStatus();
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
