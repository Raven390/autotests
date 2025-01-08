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
    public static final String STORY_CLICKHOUSE_API_SERVICE_GET_CLIENT_TRADING_ACCOUNTS = "Clickhouse api. Get client trading accounts request";
    public static final String STORY_CLICKHOUSE_API_SERVICE_GET_CLIENTS = "Clickhouse api. Get client by trading account & server ID";
    public static final String STORY_CLICKHOUSE_API_SERVICE_GET_TRADES_GROUP_BY_SYMBOL = "Clickhouse api. Get trades grouped by symbol";
    public static final String STORY_CLICKHOUSE_API_SERVICE_GET_DEPOSITS = "Clickhouse api. Get deposits";
    public static final String STORY_CLICKHOUSE_API_SERVICE_GET_WITHDRAWALS = "Clickhouse api. Get withdrawals";
    public static final String STORY_CLICKHOUSE_API_SERVICE_GET_BONUSES = "Clickhouse api. Get bonuses";
    public static final String STORY_CLICKHOUSE_API_SERVICE_GET_BALANCE_ORDERS = "Clickhouse api. Get balance orders";
    public static final String STORY_CLICKHOUSE_API_SERVICE_GET_CREDITS = "Clickhouse api. Get credits";
    public static final String STORY_CLICKHOUSE_API_SERVICE_GET_TRADES = "Clickhouse api. Get trades";
    public static final String STORY_CLICKHOUSE_API_SERVICE_GET_UNCLOSED_TRADES = "Clickhouse api. Get unclosed trades";
    public static final String STORY_CLICKHOUSE_API_SERVICE_GET_LEXIS_NEXIS = "Clickhouse api. Get lexis nexis data by client request";
    public static final String STORY_CLICKHOUSE_API_SERVICE_GET_LEXIS_NEXIS_DATA = "Clickhouse api. Get lexis nexis custom column data request";
    public static final String STORY_CLICKHOUSE_API_SERVICE_GET_CREDIT_EQUITY = "Clickhouse api. Get credit equity request";
    public static final String STORY_CLICKHOUSE_API_SERVICE_GET_FLOATING_TRADES_GROUP_BY = "Clickhouse api. Get floating trades group by";
    public static final String STORY_CLICKHOUSE_API_SERVICE_GET_MIRROR_ACCOUNTS_BY_TRADES = "Clickhouse api. Get mirror accounts by trades";
    public static final String STORY_CLICKHOUSE_API_SERVICE_POST_ABUSE_TYPES = "Clickhouse api. Post abuse types";
    public static final String STORY_CLICKHOUSE_API_SERVICE_GET_CREDIT_RISK_FREE_REVENUE_RATIO = "Clickhouse api. Get credit risk free revenue ratio";
    public static final String STORY_CLICKHOUSE_API_SERVICE_GET_SWAP_FREE_FEES = "Clickhouse api. Get swap free fees";
    public static final String STORY_CLICKHOUSE_API_SERVICE_GET_SWAP_FREE_VOLUME = "Clickhouse api. Get swap free volume";
    public static final String STORY_CONNECTION_SEARCH_BY_CLIENT_ID = "Get connections by client id";
    public static final String STORY_CONNECTION_SEARCH_BY_ATTRIBUTES = "Get connections by attribute";
    public static final String STORY_RULE_ENGINE_MIRROR_TRADING_RULE = "Mirror trading rule in Rule Engine service";
    public static final String STORY_RULE_ENGINE_REGISTRATION_RULE = "Registration rule in Rule Engine service";
    public static final String STORY_RULE_ENGINE_CPA_ABUSE_RULE = "CPA abuse rule in Rule Engine service";
    // PAYMENT TYPES
    public static final String PAYMENT_TYPE_WITHDRAWAL = "withdrawal";
    public static final String PAYMENT_TYPE_DEPOSIT = "deposit";
    public static final String PAYMENT_TYPE_BONUS = "bonus";
    // PAYMENT PROVIDERS
    public static final String PAYMENT_PROVIDER_FASAPAY = "fasapay";
    // OTHER
    public static final String TAG_BUILD_CHECK = "build_check";
    public static final String TAG_AUTOMATED = "automated";
    public static final String TAG_MANUAL = "manual";
    public static final String ROLE_UNKNOWN = "unknown";
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
    public static final String CRM_ACCOUNT_TABLE_NAME = "vindex_test.crm___tb_account";
    public static final String CRM_USER_TABLE_NAME = "vindex_test.crm___tb_user";
    public static final String CRM_BONUS_TABLE_NAME = "vindex_test.crm__tb_bonus";
    public static final String CRM_DEPOSIT_TABLE_NAME = "vindex_test.crm__tb_deposit";
    public static final String CRM_WITHDRAWAL_TABLE_NAME = "vindex_test.crm__tb_withdrawal";
    //public static final String MT_USER_TABLE_NAME = "vindex_test.mt__tb_user";
    public static final String MT_CREDITS_TABLE_NAME = "vindex_test.mt___credit_orders";
    public static final String MT_BALANCE_ORDERS_TABLE_NAME = "vindex_test.mt___balance_orders";
    public static final String MT_TRADES_TABLE_NAME = "vindex_test.mt__tb_trade";
    public static final String CONNECTIONS_TABLE_NAME = "vindex_test.connection_table";
    public static final String DOCUMENT_TABLE_NAME = "vindex_test.document";
    public static final String DIGITAL_ID_TABLE_NAME = "vindex_test.digital_id";
    public static final String DEVICE_ID_TABLE_NAME = "vindex_test.device_id";
    public static final String SESSION_ID_TABLE_NAME = "vindex_test.session_id";
    public static final String NAME_BIRTH_TABLE_NAME = "vindex_test.name_birth";
    public static final String WEB_SESSION_TABLE_NAME = "vindex_test.web_session_id";
    public static final String EMAIL_TABLE_NAME = "vindex_test.email";
    public static final String IP_TABLE_NAME = "vindex_test.ip";
    public static final String PHONE_TABLE_NAME = "vindex_test.phone";
    public static final String PAYOUT_TABLE_NAME = "vindex_test.payout";
    public static final String LEXIS_NEXIS_TABLE_NAME = "vindex_test.ln_session_parsed";
    public static final String CLIENT_FRAUD_TYPES_TABLE_NAME = "vindex_test.bo___client_fraud_types";
    public static final String MITIGATION_CLIENTS_RESTRICTION = "postgres.mi.clients_restriction";
    public static final String MITIGATION_ACTION = "postgres.mi.action";
    public static final String MITIGATION_KAFKA_REQUEST = "postgres.mi.kafka_request";
    public static final String MITIGATION_KAFKA_RESPONSE = "postgres.mi.kafka_response";
    public static final String MT5_DEALS_COERCED_TABLE_NAME = "vindex_test.mt___mt5_deals_coerced";
    public static final String BO_ALERT_TABLE_NAME = "postgres.bo.alert";
    public static final String BO_CLIENT_TABLE_NAME = "postgres.bo.client";
    public static final String BO_BACKOFFICE_USER_TABLE_NAME = "postgres.bo.backoffice_user";
    public static final String AGGR_MIRROR_ACCOUNTS_BY_TRADES = "vindex_test_api.aggr__mirror_accounts_by_trades";
    public static final String AGGR_CREDIT_EQUITY_RATE = "vindex_test_api.aggr__credit_equity_rate";
    public static final String AGGR_CREDIT_RISK_FREE_REVENUE_RATIO = "vindex_test_api.aggr__credit_risk_free_revenue_ratio";
    public static final String BO_USER_ACTION_AUDIT_TABLE_NAME = "postgres.bo.user_action_audit";
    public static final String BO_USER_SESSION_TABLE_NAME = "postgres.bo.user_session";
    public static final String BO_CLIENTS_FRAUD_TYPES_TABLE_NAME = "postgres.bo.clients_fraud_types";
    public static final String KYC_FILES_TABLE_NAME = "vindex_test.crm___tb_kyc_files";
    public static final String ID_PROOF_TABLE_NAME = "vindex_test.crm___tb_id_proof";
    // UI
    public static final String VANTAGE_BRAND_IMAGE_SRC = "data:image/svg+xml,%3csvg%20width='33'%20height='32'%20viewBox='0%200%2033%2032'%20fill='none'%20xmlns='http://www.w3.org/2000/svg'%3e%3crect%20width='32'%20height='32'%20transform='translate(0.5)'%20fill='%23034854'/%3e%3cpath%20d='M12.617%208.98231H8.34277L16.9316%2024.9503L19.0687%2020.9986L12.617%208.98231Z'%20fill='white'/%3e%3cpath%20d='M14.1896%208.9823H25.5608L19.9155%2019.426V12.5711L14.1896%208.9823Z'%20fill='%23E35728'/%3e%3c/svg%3e";
}
