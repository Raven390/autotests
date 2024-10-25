package utils;

public class Constants {
    // TEAMS
    public static final String TEAM_CORE = "team_core";
    public static final String TEAM_BACKOFFICE = "team_backoffice";
    // SUITE
    public static final String SUITE_REGRESSION = "suite_regression";
    public static final String SUITE_SMOKE = "suite_smoke";
    // OWNERS
    public static final String OWNER_NIKOLAI_KORIAGIN = "NikolaiKoriagin";
    public static final String OWNER_DMITRI_KALACHEV = "DmitriKalachev";
    public static final String OWNER_FEDOR_NESTEROVICH = "FedorNesterovich";
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
    public static final String FEATURE_EVENT_GENERATOR_SERVICE_LOGIN = "Login event in Event Generator";
    // PAYMENT TYPES
    public static final String PAYMENT_TYPE_WITHDRAWAL = "withdrawal";
    public static final String PAYMENT_TYPE_DEPOSIT = "deposit";
    public static final String PAYMENT_TYPE_BONUS = "bonus";
    // PAYMENT PROVIDERS
    public static final String PAYMENT_PROVIDER_FASAPAY = "fasapay";
    // OTHER
    public static final String TAG_BUILD_CHECK = "build_check";
    public static final String TAG_AUTOMATION = "Automation tests";
    // KAFKA
    public static final String KAFKA_TOPIC_CRM_EVENTS = "crm-events";
    public static final String KAFKA_TOPIC_CRM_DB_EVENTS = "crm-db-events";
    public static final String KAFKA_TOPIC_MT_EVENTS = "mt-events";
    public static final String KAFKA_TOPIC_MT_DB_EVENTS = "mt-db-events";
    public static final String KAFKA_TOPIC_ALERTS = "alerts";
    public static final String KAFKA_NO_MESSAGE_FOUND_ERROR = "Max attempts reached without finding a matching message";
    // PARAMS
    public static final String REGISTRATION_HELPER_FIRST_NAME = "Test";
    public static final String REGISTRATION_HELPER_SECOND_NAME = "User";
    public static final String REGISTRATION_HELPER_REGULATOR_VFSC2 = "VFSC2";
    public static final String REGISTRATION_HELPER_WID_VANTAGE = "AU";
    public static final String REGISTRATION_HELPER_INTERFACE = "/api/registrationV2/register";
}
