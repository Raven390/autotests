package utils;

public class Constants {
    // TEAMS
    public static final String TEAM_CORE = "team_core";
    public static final String TEAM_BACKOFFICE = "team_backoffice";
    // SUITE
    public static final String SUITE_REGRESSION = "suite_regression";
    public static final String SUITE_SMOKE = "suite_smoke";
    public static final String SUITE_CLICKHOUSE_API_SERVICE = "suite_clickhouse_api_service";
    public static final String SUITE_CONNECTION_SEARCH_SERVICE = "suite_connection_search_service";
    public static final String SUITE_EVENT_GENERATOR_SERVICE = "suite_event_generator_service";
    public static final String SUITE_MITIGATION_SERVICE = "suite_mitigation_service";
    public static final String SUITE_RULE_ENGINE_SERVICE = "suite_rule_engine_service";
    // TEST CASE STATUSES
    public static final String STATUS_MANUAL = "status_manual";
    public static final String STATUS_AUTOMATED = "status_automated";
    // TEST LAYERS
    public static final String LAYER_API = "layer_api";
    public static final String LAYER_WEB = "layer_web";
    public static final String LAYER_MOBILE = "layer_mobile";
    // FEATURES
    public static final String FEATURE_LEXIS_NEXIS = "Lexis Nexis integration";
    public static final String FEATURE_RULE_ENGINE_SERVICE = "Rule engine service";
    public static final String FEATURE_CLICKHOUSE_API_SERVICE = "Clickhouse api service";
    public static final String FEATURE_EVENT_GENERATOR_SERVICE = "Event generator service";
    public static final String FEATURE_CONNECTION_SEARCH_API_SERVICE = "Connection search api service";
    // STORIES
    public static final String STORY_EVENT_GENERATOR_SERVICE_LOGIN = "Login event in Event Generator";
    public static final String STORY_EVENT_GENERATOR_SERVICE_REGISTRATION = "Registration event in Event Generator";
    public static final String STORY_EVENT_GENERATOR_SERVICE_WITHDRAWAL = "Withdrawal event in Event Generator";
    public static final String STORY_EVENT_GENERATOR_SERVICE_OPEN_TRADE = "Open trade event in Event Generator";
    public static final String STORY_EVENT_GENERATOR_SERVICE_CLOSE_TRADE = "Close trade event in Event Generator";
    public static final String STORY_EVENT_GENERATOR_SERVICE_RAF_BALANCE = "Raf balance trade event in Event Generator";
    public static final String STORY_CLICKHOUSE_API_SERVICE_GET_CLIENT = "Clickhouse api. Get client request";
    public static final String STORY_CLICKHOUSE_API_SERVICE_GET_CLIENTS = "Clickhouse api. Get client by trading account & server ID";
    public static final String STORY_CLICKHOUSE_API_SERVICE_GET_LEXIS_NEXIS = "Clickhouse api. Get lexis nexis data by client request";
    public static final String STORY_CONNECTION_SEARCH_BY_CLIENT_ID = "Get connections by client id";
    public static final String STORY_CONNECTION_SEARCH_BY_ATTRIBUTES = "Get connections by attribute";
    public static final String STORY_RULE_ENGINE_MIRROR_TRADING_RULE = "Mirror trading rule in Rule Engine service";
    public static final String STORY_RULE_ENGINE_REGISTRATION_RULE = "Registration rule in Rule Engine service";
    // PAYMENT TYPES
    public static final String PAYMENT_TYPE_WITHDRAWAL = "withdrawal";
    public static final String PAYMENT_TYPE_DEPOSIT = "deposit";
    public static final String PAYMENT_TYPE_BONUS = "bonus";
    // PAYMENT PROVIDERS
    public static final String PAYMENT_PROVIDER_FASAPAY = "fasapay";
    // OTHER
    public static final String TAG_BUILD_CHECK = "build_check";
    public static final String TAG_AUTOMATION = "Automation tests";
    public static final String TAG_MANUAL = "manual";
    // KAFKA
    public static final String KAFKA_TOPIC_CRM_EVENTS = "crm-events";
    public static final String KAFKA_TOPIC_CRM_DB_EVENTS = "crm-db-events";
    public static final String KAFKA_TOPIC_MT_EVENTS = "mt-events";
    public static final String KAFKA_TOPIC_MT_DB_EVENTS = "mt-db-events";
    public static final String KAFKA_TOPIC_ALERTS = "alerts";
    public static final String KAFKA_NO_MESSAGE_FOUND_ERROR = "Max attempts reached without finding a matching message";
    public static final String KAFKA_ALL_PARAMETERS_FOUND = "All the parameters were found in messages.";
    public static final String KAFKA_SOME_PARAMETERS_FOUND = "Some of the parameters were not found in messages.";
    public static final String KAFKA_NO_PARAMETERS_PROVIDED = "No search parameters provided.";
    // PARAMS
    public static final String REGISTRATION_HELPER_FIRST_NAME = "Test";
    public static final String REGISTRATION_HELPER_SECOND_NAME = "User";
    public static final String REGISTRATION_HELPER_REGULATOR_VFSC2 = "VFSC2";
    public static final String REGISTRATION_HELPER_WID_VANTAGE = "AU";
    public static final String REGISTRATION_HELPER_INTERFACE = "/api/registrationV2/register";
    // TABLES
    public static final String CRM_USER_TABLE_NAME = "vindex_test.crm__tb_user";
    public static final String MT_USER_TABLE_NAME = "vindex_test.mt__tb_user";
    public static final String CONNECTIONS_TABLE_NAME = "vindex_test.cs__tb_connection_table_v2";
    public static final String DOCUMENT_TABLE_NAME = "vindex_test.cs__tb_doc_table";
    public static final String EMAIL_TABLE_NAME = "vindex_test.cs__tb_email_table";
    public static final String IP_TABLE_NAME = "vindex_test.cs__tb_ip_table";
    public static final String PHONE_TABLE_NAME = "vindex_test.cs__tb_phone_table";
    public static final String PAYOUT_TABLE_NAME = "vindex_test.cs__tb_payout_table";
}
