package utils;

public class Constants {
    // TEAMS
    public static final String TEAM_BACKOFFICE = "team_backoffice";
    public static final String TEAM_CORE = "team_core";
    // SUITE
    public static final String DEBUG_RUNNER = "debug_runner";
    public static final String SUITE_CLICKHOUSE_API_SERVICE = "suite_clickhouse_api_service";
    public static final String SUITE_UTILITIES_API_SERVICE = "suite_utilities_api_service";
    public static final String SUITE_CONNECTION_SEARCH_SERVICE = "suite_connection_search_service";
    public static final String SUITE_EVENT_GENERATOR_SERVICE = "suite_event_generator_service";
    public static final String SUITE_MIRROR_TRADING_SCORE_API_TESTS = "mirror_trading_score_api_tests";
    public static final String SUITE_MITIGATION_SERVICE = "suite_mitigation_service";
    public static final String SUITE_REGRESSION = "suite_regression";
    public static final String SUITE_RULE_ENGINE_API_TESTS = "suite_rule_engine_api_tests";
    public static final String SUITE_RULE_ENGINE_RULES_TESTS = "suite_rule_engine_rules_tests";
    public static final String SUITE_PAYMENT_GATE_TESTS = "suite_payment_gate_tests";
    public static final String SUITE_SMOKE = "suite_smoke";
    public static final String SUITE_SMOKE_PROD = "suite_smoke_production";
    // TEST LAYERS
    public static final String LAYER_API = "layer_api";
    public static final String LAYER_KAFKA = "layer_kafka";
    public static final String LAYER_MOBILE = "layer_mobile";
    public static final String LAYER_WEB = "layer_web";
    // VINDEX SERVICES
    public static final String ABUSE_REGISTRY = "abuse_registry";
    // FEATURES
    public static final String FEATURE_CLICKHOUSE_API_SERVICE = "Clickhouse api service";
    public static final String FEATURE_CONNECTION_SEARCH_API_SERVICE = "Connection search api service";
    public static final String FEATURE_EVENT_GENERATOR_SERVICE = "Event generator service";
    public static final String FEATURE_LEXIS_NEXIS = "Lexis Nexis integration";
    public static final String FEATURE_MIRROR_TRADING_SCORE_API_SERVICE = "Mirror trading score api service";
    public static final String FEATURE_RULE_ENGINE_SERVICE = "Rule engine service";
    public static final String FEATURE_PRODUCTION_TESTS_CLICKHOUSE_API = "Clickhouse Api. Production tests";
    public static final String FEATURE_PRODUCTION_TESTS_CONNECTION_SEARCH = "Connection search. Production tests";
    public static final String FEATURE_PAYMENT_GATE = "Payment gate";
    public static final String FEATURE_UTILITIES_API_SERVICE = "Payment gate";
    // STORIES
    public static final String STORY_CHECK_CONNECTED_IB = "Check connected IB";
    public static final String STORY_CLICKHOUSE_API_SERVICE_GET_ABUSE_TYPES = "Clickhouse api. Get abuse types";
    public static final String STORY_CLICKHOUSE_API_SERVICE_GET_MARKET_CLOSE = "Clickhouse api. Get market close";
    public static final String STORY_CLICKHOUSE_API_SERVICE_GET_BALANCE_ORDERS = "Clickhouse api. Get balance orders";
    public static final String STORY_CLICKHOUSE_API_SERVICE_GET_BONUSES = "Clickhouse api. Get bonuses";
    public static final String STORY_CLICKHOUSE_API_SERVICE_GET_CLIENT = "Clickhouse api. Get client request";
    public static final String STORY_CLICKHOUSE_API_SERVICE_GET_CLIENT_TRADING_ACCOUNTS = "Clickhouse api. Get client trading accounts request";
    public static final String STORY_CLICKHOUSE_API_SERVICE_GET_CLIENTS = "Clickhouse api. Get client by trading account & server ID";
    public static final String STORY_CLICKHOUSE_API_SERVICE_GET_CREDIT_EQUITY = "Clickhouse api. Get credit equity request";
    public static final String STORY_CLICKHOUSE_API_SERVICE_GET_CREDIT_RISK_FREE_REVENUE_RATIO = "Clickhouse api. Get credit risk free revenue ratio";
    public static final String STORY_CLICKHOUSE_API_SERVICE_GET_CREDITS = "Clickhouse api. Get credits";
    public static final String STORY_CLICKHOUSE_API_SERVICE_GET_DEPOSITS = "Clickhouse api. Get deposits";
    public static final String STORY_CLICKHOUSE_API_SERVICE_GET_FAST_TRADES = "Clickhouse api. Get fast trades";
    public static final String STORY_CLICKHOUSE_API_SERVICE_GET_FLOATING_TRADES_GROUP_BY = "Clickhouse api. Get floating trades group by";
    public static final String STORY_CLICKHOUSE_API_SERVICE_GET_LEXIS_NEXIS = "Clickhouse api. Get lexis nexis data by client request";
    public static final String STORY_CLICKHOUSE_API_SERVICE_GET_LEXIS_NEXIS_DATA = "Clickhouse api. Get lexis nexis custom column data request";
    public static final String STORY_CLICKHOUSE_API_SERVICE_GET_MIRROR_ACCOUNTS_BY_TRADES = "Clickhouse api. Get mirror accounts by trades";
    public static final String STORY_CLICKHOUSE_API_SERVICE_GET_MIRROR_TRADE_ON_LAST_WITHDRAWAL = "Clickhouse api. Get mirror trade on last withdrawal";
    public static final String STORY_CLICKHOUSE_API_SERVICE_GET_SWAP_FREE_FEES = "Clickhouse api. Get swap free fees";
    public static final String STORY_CLICKHOUSE_API_SERVICE_GET_SWAP_FREE_VOLUME = "Clickhouse api. Get swap free volume";
    public static final String STORY_CLICKHOUSE_API_SERVICE_GET_SYMBOL_GROUPS = "Clickhouse api. Get symbol groups";
    public static final String STORY_CLICKHOUSE_API_SERVICE_GET_UNCLOSED_TRADES_BY_SYMBOL = "Clickhouse api. Get unclosed trades by symbol";
    public static final String STORY_CLICKHOUSE_API_SERVICE_GET_TRADES = "Clickhouse api. Get trades";
    public static final String STORY_CLICKHOUSE_API_SERVICE_GET_TRADES_GROUP_BY_SYMBOL = "Clickhouse api. Get trades grouped by symbol";
    public static final String STORY_CLICKHOUSE_API_SERVICE_GET_UNCLOSED_TRADES = "Clickhouse api. Get unclosed trades";
    public static final String STORY_CLICKHOUSE_API_SERVICE_GET_WITHDRAWALS = "Clickhouse api. Get withdrawals";
    public static final String STORY_CONNECTION_SEARCH_BY_ATTRIBUTES = "Get connections by attribute";
    public static final String STORY_CONNECTION_SEARCH_BY_CLIENT_ID = "Get connections by client id";
    public static final String STORY_CONNECTION_SEARCH_GET_ABUSE_TYPES_BY_CLIENT_ID_V1 = "Get abuse types by client id V1";
    public static final String STORY_CONNECTION_SEARCH_GET_ABUSE_TYPES_BY_CLIENT_ID_V2 = "Get abuse types by client id V2";
    public static final String STORY_CONNECTION_SEARCH_GET_ABUSE_TYPES_BY_ATTRIBUTES = "Get abuse types by attribute";
    public static final String STORY_DATA_DUMPER_CLOSE_TRADE_EVENT = "Data dumper. Close trade event";
    public static final String STORY_DATA_DUMPER_LOSS_COMPENSATION_EVENT = "Data dumper. Trade loss compensation event";
    public static final String STORY_DATA_DUMPER_OPEN_TRADE_EVENT = "Data dumper. Open trade event";
    public static final String STORY_DATA_DUMPER_STOP_OUT_EVENT = "Data dumper. Stopout trade event";
    public static final String STORY_EVENT_GENERATOR_SERVICE_CLOSE_TRADE = "Close trade event in Event Generator";
    public static final String STORY_EVENT_GENERATOR_SERVICE_LOGIN = "Login event in Event Generator";
    public static final String STORY_EVENT_GENERATOR_SERVICE_OPEN_TRADE = "Open trade event in Event Generator";
    public static final String STORY_EVENT_GENERATOR_SERVICE_RAF_BALANCE = "Raf balance trade event in Event Generator";
    public static final String STORY_EVENT_GENERATOR_SERVICE_REGISTRATION = "Registration event in Event Generator";
    public static final String STORY_EVENT_GENERATOR_SERVICE_WITHDRAWAL = "Withdrawal event in Event Generator";
    public static final String STORY_GET_MIRROR_TRADING_SCORE = "Get mirror trading score";
    public static final String STORY_PRODUCTION_TESTS = "Production smoke test cases";
    public static final String STORY_RULE_ENGINE_ABNORMAL_PROFIT_RULE = "Abnormal profit rule ";
    public static final String STORY_RULE_ENGINE_CPA_ABUSE_RULE = "CPA abuse rule ";
    public static final String STORY_RULE_ENGINE_LOSS_VOUCHER_RULE = "Loss voucher rule ";
    public static final String STORY_RULE_ENGINE_MARKET_MANIPULATION_RULE = "Market manipulation rule ";
    public static final String STORY_RULE_ENGINE_MIRROR_TRADING_CLOSE_TRADE_RULE = "Mirror trading rule with close trade. ";
    public static final String STORY_RULE_ENGINE_ML_MIRROR_TRADE_RULE = "Ml mirror trading rule with close trade. ";
    public static final String STORY_RULE_ENGINE_MIRROR_TRADING_CLOSE_TRADE_BYBIT_RULE = "Mirror trading rule with close trade, only for bybit clients. ";
    public static final String STORY_RULE_ENGINE_NDB_ABUSE_RULE = "No deposit bonus abuse rule ";
    public static final String STORY_RULE_ENGINE_NPB_LOSING_LEG_RULE = "NBP losing leg rule ";
    public static final String STORY_RULE_ENGINE_NPB_WINNING_LEG_RULE = "NBP winning leg rule ";
    public static final String STORY_RULE_ENGINE_REGISTRATION_RULE = "Registration rule ";
    public static final String STORY_RULE_ENGINE_WITHDRAWAL_NOTIFICATION_IN_ROUTER_RULE = "Router rule. Withdrawal notification branch.";
    public static final String STORY_RULE_ENGINE_MIRROR_TRADE_IN_ROUTER_RULE = "Router rule. Mirror trade branch";
    public static final String STORY_RULE_ENGINE_CONNECTION_SEARCH_IN_ROUTER_RULE = "Router rule. Connection search branch";
    public static final String STORY_RULE_ENGINE_WITHDRAWAL_ROUTER_RULE_TRANSFER_TO_WA = "Router rule. Transfer to wa";
    public static final String STORY_RULE_ENGINE_WITHDRAWAL_ROUTER_RULE_CRM_PAYMENT = "Router rule. crm payment topic";
    public static final String STORY_RULE_ENGINE_WITHDRAWAL_ROUTER_RULE_CRM_EVENTS = "Router rule. crm events topic";
    public static final String STORY_RULE_ENGINE_WITHDRAWAL_INTEGRITY_CHECK_ROUTER_RULE = "Router rule. Withdrawal integrity check ";
    public static final String STORY_RULE_ENGINE_LATENCY_ARBITRAGE_RULE = "Latency arbitrage rule ";
    public static final String STORY_RULE_ENGINE_MIRROR_TRADE_OPEN_TRADE_EVENT_RULE = "Mirror trade with open trade event rule ";
    public static final String STORY_RULE_ENGINE_NEWS_TRADER_OPEN_TRADE_EVENT_RULE = "News Trader with close trade event rule ";
    public static final String STORY_RULE_ENGINE_NO_SLIPPAGE_RULE = "No slippage rule with close trade event ";
    public static final String STORY_RULE_ENGINE_LOGIN_RULE = "Login rule with login event ";
    public static final String STORY_RULE_ENGINE_CUSTOM_RULE = "Custom rule ";
    public static final String STORY_PAYMENT_GATE_GET_DECISIONS = "Get decisions request";
    public static final String STORY_PAYMENT_GATE_POST_DECISIONS = "Post decisions request";
    public static final String STORY_PAYMENT_GATE_POST_TEMP_DECISIONS = "Post temp decisions request";
    public static final String STORY_PAYMENT_GATE_POST_PAYMENTS = "Post payments request";
    public static final String STORY_PAYMENT_GATE_PUT_PAYMENTS_V1 = "PUT payments V1 request";
    public static final String STORY_PAYMENT_GATE_PUT_PAYMENTS_V2 = "PUT payments V2 request";
    public static final String STORY_PAYMENT_GATE_POST_RULE_EXECUTIONS = "Post rule executions request";
    public static final String STORY_PAYMENT_GATE_GET_RULE_EXECUTIONS = "Get rule executions request";
    public static final String STORY_PAYMENT_GATE_PUT_RULE_EXECUTIONS = "Put rule executions request";
    public static final String STORY_PAYMENT_GATE_RECONCILIATION = "Payment gate reconciliation job";
    public static final String STORY_PAYMENT_GATE_DECISION_MONITORING = "Payment gate temp decision monitoring job";
    public static final String STORY_PAYMENT_GATE_PUT_DECISIONS = "Put decisions request";
    public static final String STORY_PAYMENT_GATE_GET_PAYMENTS_AGGR_BY_CLIENT = "Get payments aggr by client request";
    public static final String STORY_PAYMENT_GATE_GET_PAYMENTS = "Get payments request";
    public static final String STORY_PAYMENT_GATE_GET_REJECTION_REASONS = "Get rejection reasons request";
    public static final String STORY_UTILITIES_API_GET_DECRYPT = "Get decrypt user data";
    // PAYMENT TYPES
    public static final String PAYMENT_TYPE_BONUS = "bonus";
    public static final String PAYMENT_TYPE_DEPOSIT = "deposit";
    public static final String PAYMENT_TYPE_WITHDRAWAL = "withdrawal";
    // PAYMENT PROVIDERS
    public static final String PAYMENT_PROVIDER_FASAPAY = "fasapay";
    // OTHER
    public static final String ROLE_UNKNOWN = "unknown";
    public static final String TAG_AUTOMATED = "automated";
    public static final String TAG_MANUAL = "manual";
    // KAFKA
    public static final String KAFKA_ALL_PARAMETERS_FOUND = "All the parameters were found in messages.";
    public static final String KAFKA_NO_MESSAGE_FOUND_ERROR = "Max attempts reached without finding a matching message";
    public static final String KAFKA_NO_PARAMETERS_PROVIDED = "No search parameters provided.";
    public static final String KAFKA_SOME_PARAMETERS_FOUND = "Some of the parameters were not found in messages.";
    public static final String KAFKA_TOPIC_ACCOUNT_RESTRICTIONS_APPLY = "account.restrictions.apply";
    public static final String KAFKA_TOPIC_ACCOUNT_RESTRICTIONS_CANCEL = "account.restrictions.cancel";
    public static final String KAFKA_TOPIC_ALERTS = "alerts";
    public static final String KAFKA_TOPIC_CLIENT_RESTRICTIONS_APPLY = "client.restrictions.apply";
    public static final String KAFKA_TOPIC_CLIENT_RESTRICTIONS_CANCEL = "client.restrictions.cancel";
    public static final String KAFKA_TOPIC_CRM_DB_EVENTS = "crm-db-events";
    public static final String KAFKA_TOPIC_CRM_EVENTS = "crm-events";
    public static final String KAFKA_TOPIC_CUSTOM_EVENTS = "custom-events";
    public static final String KAFKA_TOPIC_UCID_MIRROR_SCORE = "ucid_mirror_score";
    public static final String KAFKA_TOPIC_ML_MIRROR_TRADE_EVENTS = "ml-mirror-trade-events";
    public static final String KAFKA_TOPIC_CRM_ACKNOWLEDGE = "crm-acknowledge";
    public static final String KAFKA_TOPIC_CRM_PAYMENTS = "crm-payment";
    public static final String KAFKA_TOPIC_MT_4_TRADE_RECORD = "mt4_trade_record";
    public static final String KAFKA_TOPIC_MT_5_DEAL_PERFORM = "mt5_DealPerform";
    public static final String KAFKA_TOPIC_MT_DB_EVENTS = "mt-db-events";
    public static final String KAFKA_TOPIC_MT_EVENTS = "mt-events";
    public static final String KAFKA_TOPIC_WITHDRAWAL_APPROVALS = "withdrawal.approvals";
    public static final String KAFKA_TOPIC_ACCOUNT_DEDUCTION_REQUEST = "account.deduction.request";
    public static final String KAFKA_TOPIC_ACCOUNT_DEDUCTION_REQUEST_RESPONSE = "account.deduction.requestResponse";
    public static final String KAFKA_TOPIC_PAYMENT_ACKNOWLEDGE = "payment.acknowledge";
    // TABLES
    public static final String ACCOUNT_IB_RELATION_SNAPSHOT_TABLE_NAME = "consolidated.account_ib_relation_snapshot";
    public static final String ACCOUNT_IB_RELATION_TABLE_NAME = "consolidated.account_ib_relation";
    public static final String AGGR_CREDIT_EQUITY_RATE = "consolidated_api.aggr__credit_equity_rate";
    public static final String AGGR_CREDIT_RISK_FREE_REVENUE_RATIO = "consolidated_api.aggr__credit_risk_free_revenue_ratio";
    public static final String AR_ABUSER_DEDUCTION_TABLE_NAME = "postgres.ar.abuser_deduction";
    public static final String AR_ABUSER_FRAUD_TYPE_TABLE_NAME = "postgres.ar.abuser_fraud_type";
    public static final String AR_ABUSER_HISTORY_TABLE_NAME = "postgres.ar.abuser_history";
    public static final String AR_ABUSER_TABLE_NAME = "postgres.ar.abuser";
    public static final String AR_DEDUCTION_KAFKA_REQUEST_TABLE_NAME = "postgres.ar.deduction_kafka_request";
    public static final String AR_DEDUCTION_KAFKA_RESPONSE_TABLE_NAME = "postgres.ar.deduction_kafka_response";
    public static final String AR_PENDING_PROCESSING_TABLE_NAME = "postgres.ar.pending_processing";
    public static final String AR_FRAUD_TYPE_CATEGORY_TABLE_NAME = "postgres.ar.fraud_type_category";
    public static final String AUDIT_EVENT_OLD = "postgres.au.event";
    public static final String AUDIT_EVENT = "postgres.au.audit_event";
    public static final String BO_ALERT_TABLE_NAME = "postgres.bo.alert";
    public static final String PAYMENT_EVENT_TABLE_NAME = "postgres.paymentgate.payment_events";
    public static final String BO_INVESTIGATION_TABLE_NAME = "postgres.bo.investigation";
    public static final String BO_INVESTIGATION_HISTORY_TABLE_NAME = "postgres.bo.investigation_history";
    public static final String CLICKHOUSE_BO_ALERTS_TABLE_NAME = "consolidated.bo___alerts";
    public static final String CLICKHOUSE_BO_ALERT_TABLE_NAME = "consolidated.bo___alert";
    public static final String CLICKHOUSE_OZ_TRADES_TABLE_NAME = "consolidated.oz___trades";
    public static final String BO_BACKOFFICE_USER_TABLE_NAME = "postgres.bo.backoffice_user";
    public static final String BO_CLIENT_FRAUD_TYPES_TABLE_NAME = "consolidated.client_fraud_types";
    public static final String CLIENT_CARDS_TABLE_NAME = "consolidated.client_cards";
    public static final String BO_CLIENT_TABLE_NAME = "postgres.bo.client";
    public static final String BO_CLIENTS_FRAUD_TYPES_TABLE_NAME = "postgres.bo.clients_fraud_types";
    public static final String BO_USER_ACTION_AUDIT_TABLE_NAME = "postgres.bo.user_action_audit";
    public static final String BO_USER_SESSION_TABLE_NAME = "postgres.bo.user_session";
    public static final String BO_WD_REQUEST_TABLE_NAME = "postgres.bo.wd_request";
    public static final String BO_ILLEGAL_TRADES_TABLE_NAME = "postgres.bo.illegal_trades";
    public static final String CLIENT_FRAUD_TYPES_TABLE_NAME = "consolidated.client_fraud_types";
    public static final String CONNECTIONS_TABLE_NAME = "data_science.connection_table";
    public static final String CRM_BONUS_TABLE_NAME = "consolidated.crm___tb_bonus";
    public static final String CRM_TB_CREDIT_CARD_TABLE_NAME = "consolidated.crm___tb_credit_card";
    public static final String CRM_DEPOSIT_TABLE_NAME = "consolidated.crm___tb_deposit";
    public static final String CRM_DEPOSIT_TYPE_TABLE_NAME = "consolidated.crm___tb_deposit_type";
    public static final String CRM_DEPOSIT_CHANNEL_TABLE_NAME = "consolidated.crm___tb_deposit_channel";
    public static final String CRM_TB_ACCOUNT_FOR_MT_TABLE_NAME = "consolidated.crm___tb_account_for_mt";
    public static final String CRM_TB_ACCOUNT_TABLE_NAME = "consolidated.crm___tb_account";
    public static final String CRM_TB_LOYALTY_REDEMPTION = "consolidated.crm___tb_loyalty_redemption";
    public static final String CRM_TB_USER_EXTENDS_TABLE_NAME = "consolidated.crm___tb_user_extends";
    public static final String CRM_TB_WITHDRAW_ACCOUNT_TABLE_NAME = "consolidated.crm___tb_withdraw_account";
    public static final String CLIENT_PAYMENT_INFO_TABLE_NAME = "consolidated.client_payment_info";
    public static final String CRM_TRANSFERS_TABLE_NAME = "consolidated.crm___tb_transfer";
    public static final String CRM_USER_TABLE_NAME = "consolidated.crm___tb_user";
    public static final String CRM_FILES_TABLE_NAME = "consolidated.crm___tb_files";
    public static final String CLICKHOUSE_CRM_TB_WITHDRAWAL = "consolidated.crm___tb_withdrawal";
    public static final String CLICKHOUSE_CRM_TB_WITHDRAWAL_TYPE = "consolidated.crm___tb_withdraw_type";
    public static final String DATA_SCIENCE_UCID_MIRROR_SCORE_BYBIT = "data_science.ucid_mirror_score_bybit";
    public static final String DATA_SCIENCE_FEATURE_STORE_SERVICE_TABLE_NAME = "data_science.feature_store_service";
    public static final String DATA_SCIENCE_UCID_MIRROR_SCORE_PYTHON = "data_science.ucid_mirror_score_python";
    public static final String DATA_SCIENCE_UCID_MIRROR_SCORE_TABLE_NAME = "data_science.ucid_mirror_score";
    public static final String DATA_SCIENCE_UCID_GENERAL_SCORE_TABLE_NAME = "data_science.ucid_general_score";
    public static final String DEVICE_ID_TABLE_NAME = "data_science.device_id";
    public static final String DICT_ACCOUNT_TO_UCID = "consolidated.dict_account_to_ucid";
    public static final String DICT_ACTIVE_TRADE_DAYS_BY_UCID = "consolidated.dict_active_trade_days_by_ucid";
    public static final String DICT_IS_TEST = "consolidated.dict_is_test_account";
    public static final String DIGITAL_ID_TABLE_NAME = "data_science.digital_id";
    public static final String DOCUMENT_TABLE_NAME = "data_science.document";
    public static final String EMAIL_TABLE_NAME = "data_science.email";
    public static final String ID_PROOF_TABLE_NAME = "consolidated.crm___tb_id_proof";
    public static final String IP_TABLE_NAME = "data_science.ip";
    public static final String KYC_FILES_TABLE_NAME = "consolidated.crm___tb_kyc_files";
    public static final String LEXIS_NEXIS_TABLE_NAME = "lexisnexis.ln_session_parsed";
    public static final String MIRROR_LOGIN_TABLE_NAME = "data_science.mirror_login";
    public static final String MIRROR_UCID_TABLE_NAME = "consolidated.mirror_ucid";
    public static final String MITIGATION_CLIENT_GENERAL_RESTRICTION = "postgres.mi.client_general_restriction";
    public static final String MITIGATION_CLIENT_GENERAL_RESTRICTION_ACTION = "postgres.mi.client_general_restriction_action";
    public static final String MITIGATION_CLIENT_TRADING_RESTRICTION = "postgres.mi.client_trading_restriction";
    public static final String MITIGATION_CLIENT_TRADING_RESTRICTION_ACTION = "postgres.mi.client_trading_restriction_action";
    public static final String MITIGATION_CLIENT_TRADING_RESTRICTION_STATUS_BY_SITE = "postgres.mi.client_trading_restriction_status_by_site";
    public static final String MITIGATION_KAFKA_REQUEST_GENERAL = "postgres.mi.client_general_restriction_kafka_request";
    public static final String MITIGATION_KAFKA_REQUEST_TRADING = "postgres.mi.client_trading_restriction_kafka_request";
    public static final String MITIGATION_KAFKA_RESPONSE_GENERAL = "postgres.mi.client_general_restriction_kafka_response";
    public static final String MITIGATION_KAFKA_RESPONSE_TRADING = "postgres.mi.client_trading_restriction_kafka_response";
    public static final String MITIGATION_CLIENT_BYBIT_RESTRICTION = "postgres.mi.client_bybit_restriction";
    public static final String MT4_TRADES_COERCED_TABLE_NAME = "consolidated.mt___mt4_trades_coerced";
    public static final String MT4_TRADES_TABLE_NAME = "consolidated.mt___mt4_trades";
    public static final String MT5_DEALS_COERCED_TABLE_NAME = "consolidated.mt___mt5_deals_coerced";
    public static final String MT5_DEALS_COERCED_DD_TABLE_NAME = "consolidated.mt___mt5_deals_coerced_dd";
    public static final String MT5_DEALS_TABLE_NAME = "consolidated.mt___mt5_deals";
    public static final String MT5_DEALS_COERCED_TOXICITY_TABLE_NAME = "consolidated.mt___mt5_deals_coerced_toxicity";
    public static final String MT5_POSITIONS_TABLE_NAME = "consolidated.mt___mt5_positions";
    public static final String MT_ACCOUNT_TABLE_NAME = "consolidated.mt___account";
    public static final String MT_BALANCE_ORDERS_TABLE_NAME = "consolidated.mt___balance_orders";
    public static final String MT_CREDITS_TABLE_NAME = "consolidated.mt___credit_orders";
    public static final String NAME_BIRTH_TABLE_NAME = "data_science.name_birth";
    public static final String PAYOUT_TABLE_NAME = "data_science.payout";
    public static final String PHONE_TABLE_NAME = "data_science.phone";
    public static final String RULE_ENGINE_RULE_DEPLOYMENT_TABLE = "ruleengine.rule_deployment";
    public static final String RULE_ENGINE_RULE_TABLE = "ruleengine.rule";
    public static final String S3_DIM_CLIENT = "consolidated.s3___dim_client";
    public static final String S3_FACT_CPA_COMMISSIONS = "consolidated.s3___fact_cpa_commissions";
    public static final String S3_FACT_IB_SALES_COMMISSIONS = "consolidated.s3___fact_ib_sales_commissions";
    public static final String CALLBACKS_TABLE_NAME = "consolidated.crm___bp_callbacks";
    public static final String S3_FACT_LOGIN_METRICS_TABLE_NAME = "consolidated.s3___fact_login_metrics";
    public static final String SEGMENTATION_TABLE_NAME = "data_science.segmentation_table";
    public static final String SESSION_ID_TABLE_NAME = "data_science.session_id";
    public static final String WEB_SESSION_TABLE_NAME = "data_science.web_session_id";
    public static final String MT_CID_TABLE_NAME = "data_science.mt_cid";
    public static final String RATES_USD_CURRENT = "ticks.rates_usd_current";
    public static final String APP_TB_FININDEX_DATA = "consolidated.app___tb_finindex_data";
    public static final String REPORTING_DB_ZEEBE_RULE_ELEMENTS = "reporting.zeebe_rules_elements";
    public static final String REPORTING_DB_ZEEBE_RULES_STARTED = "reporting.zeebe_rules_started";
    public static final String PAYMENT_GATEWAY_PAYMENT_DECISIONS_TABLE = "postgres.paymentgate.payment_decisions";
    public static final String PAYMENT_GATEWAY_PAYMENT_DECISIONS_SENT_TABLE = "postgres.paymentgate.payment_decisions_sent";
    public static final String PAYMENT_GATEWAY_PAYMENT_REJECTION_ATTRIBUTES_TABLE = "postgres.paymentgate.payment_rejection_attributes";
    public static final String PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE = "postgres.paymentgate.payment_details";
    public static final String PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE = "postgres.paymentgate.payment_events";
    public static final String PAYMENT_GATEWAY_PAYMENT_RULE_EXECUTIONS_TABLE = "postgres.paymentgate.payment_rule_executions";
    public static final String PAYMENT_GATEWAY_TMP_RULE_DECISIONS_TABLE = "postgres.paymentgate.tmp_rule_decisions";
    public static final String PAYMENT_GATEWAY_TMP_RULE_DECISIONS_SENT_TABLE = "postgres.paymentgate.payment_decisions_sent";
    public static final String VE_VERIFICATION_HISTORY = "postgres.ve.verification_history";
    public static final String EQUITY_HISTORY_TABLE = "consolidated.mt___equity_history";
    public static final String AUDIT_EVENT_TABLE = "postgres.au.audit_event";
    public static final String CRM_WALLET_TRADE_ORDER_TABLE = "consolidated.crm___tb_wallet_trade_order";
    // UI
    public static final String VANTAGE_BRAND_IMAGE_SRC = "data:image/svg+xml,%3csvg%20width='33'%20height='32'%20viewBox='0%200%2033%2032'%20fill='none'%20xmlns='http://www.w3.org/2000/svg'%3e%3crect%20width='32'%20height='32'%20transform='translate(0.5)'%20fill='%23034854'/%3e%3cpath%20d='M12.617%208.98231H8.34277L16.9316%2024.9503L19.0687%2020.9986L12.617%208.98231Z'%20fill='white'/%3e%3cpath%20d='M14.1896%208.9823H25.5608L19.9155%2019.426V12.5711L14.1896%208.9823Z'%20fill='%23E35728'/%3e%3c/svg%3e";
    // KYC files
    public static final String FILE_KYC_NAME = "/other/5b14c35f65cb4eebb7f4e1f375049c85.jpeg";

    // Connection search values
    public static final String CONNECTION_ATTRIBUTE_NAME_DEVICE = "device";
    public static final String CONNECTION_ATTRIBUTE_NAME_DIGITAL = "digital";
    public static final String CONNECTION_ATTRIBUTE_NAME_DOCUMENT = "document";
    public static final String CONNECTION_ATTRIBUTE_NAME_DOCUMENT_NUMBER = "documentNumber";
    public static final String CONNECTION_ATTRIBUTE_NAME_DOCUMENT_TYPE = "documentType";
    public static final String CONNECTION_ATTRIBUTE_NAME_EMAIL = "email";
    public static final String CONNECTION_ATTRIBUTE_NAME_EMAIL_ADDRESS = "emailAddress";
    public static final String CONNECTION_ATTRIBUTE_NAME_IP = "ip";
    public static final String CONNECTION_ATTRIBUTE_NAME_IP_ADDRESS = "ipAddress";
    public static final String CONNECTION_ATTRIBUTE_NAME_NAME_AND_BIRTH = "name+dateofbirth";
    public static final String CONNECTION_ATTRIBUTE_NAME_NAME_BIRTH = "nameBirth";
    public static final String CONNECTION_ATTRIBUTE_NAME_PAYOUT = "payout";
    public static final String CONNECTION_ATTRIBUTE_NAME_PAYOUT_ID = "payoutId";
    public static final String CONNECTION_ATTRIBUTE_NAME_PHONE = "phone";
    public static final String CONNECTION_ATTRIBUTE_NAME_PHONE_NUMBER = "phoneNumber";
    public static final String CONNECTION_ATTRIBUTE_NAME_SESSION = "session";
    public static final String CONNECTION_ATTRIBUTE_NAME_WEB_SESSION = "webSession";
    public static final String CONNECTION_ATTRIBUTE_NAME_MT_CID = "mtCid";
    public static final String CONNECTION_SEARCH_DATA_CARD_NUMBER = "535456**** **0344";
    public static final String CONNECTION_SEARCH_DATA_DEVICE = "4a25971ab724427eb8fc24a257c5b2df";
    public static final String CONNECTION_SEARCH_DATA_DEVICE2 = "5a25971ab724427eb8fc24a257c5b2df";
    public static final String CONNECTION_SEARCH_DATA_DIGITAL = "5784170d787442f8945926a2c9b24c40";
    public static final String CONNECTION_SEARCH_DATA_DOCUMENT = "3110200460092";
    public static final String CONNECTION_SEARCH_DATA_EMAIL1 = "matisse@gmx.net";
    public static final String CONNECTION_SEARCH_DATA_EMAIL2 = "matisse@gmx.net";
    public static final String CONNECTION_SEARCH_DATA_IP1 = "92.14.100.34";
    public static final String CONNECTION_SEARCH_DATA_IP2 = "92.14.100.35";
    public static final String CONNECTION_SEARCH_DATA_IP3 = "111.111.111.111";
    public static final String CONNECTION_SEARCH_DATA_IP4 = "124.12.12.42";
    public static final String CONNECTION_SEARCH_DATA_IP5 = "124.12.12.46";
    public static final String CONNECTION_SEARCH_DATA_NAME_BIRTH = "kanjana sirajindapirom 1978-02-17";
    public static final String CONNECTION_SEARCH_DATA_SESSION = "63767f3e2a9340efafd32c354660b128";
    public static final String CONNECTION_TYPE_INDIRECT = "Indirect";
    public static final String CONNECTION_TYPE_RELATION_TYPE_EXACT = "exact";
    public static final String CONNECTION_TYPE_RELATION_TYPE_SIMILAR = "similar";
    public static final String CONNECTION_TYPE_SAME_IDENTITY = "Same Identity";
    public static final String CONNECTION_TYPE_SAME_NETWORK = "Same Network";
    public static final String CONNECTION_TYPE_SAME_PERSON = "Same Person";
    //
    public static final String ACCOUNT_GROUP_S_VFX_EUR = "S_VFX_EUR";
    public static final String ACCOUNT_STATUS_ACTIVE = "Active";
    public static final String ACCOUNT_STATUS_INACTIVE = "Inactive";
    public static final String ACCOUNT_TYPE_STANDARD = "Standard";
    public static final String ACCOUNT_TYPE_SWAP_FREE = "Swap free";
    public static final String COMMENT_AUTOMATION_TESTS = "Automation tests";
    public static final String EURGBP = "EURGBP";
    public static final String EURUSD = "EURUSD";
    public static final String PLATFORM_MT_4 = "MT4";
    public static final String PLATFORM_MT_5 = "MT5";
    public static final String TIME_2024_12_31_00_00_00 = "2024-12-31 00:00:00";
    public static final String TIME_2024_2024_12_29_14_59_30_084000000 = "2024-12-29 14:59:30.084000000";
    //
    public static final String EMPTY_RULE_XML = """
            <bpmn:definitions xmlns:bpmn="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI" xmlns:dc="http://www.omg.org/spec/DD/20100524/DC" xmlns:zeebe="http://camunda.org/schema/zeebe/1.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:modeler="http://camunda.org/schema/modeler/1.0" id="Definitions_1ctfxpu" targetNamespace="http://bpmn.io/schema/bpmn" exporter="Camunda Modeler" exporterVersion="5.29.0" modeler:executionPlatform="Camunda Cloud" modeler:executionPlatformVersion="8.5.0">
              <bpmn:process id="process_id" isExecutable="true">
                <bpmn:startEvent id="test_event" name="test_name">
                  <bpmn:extensionElements>
                    <zeebe:ioMapping>
                      <zeebe:output source="=&#34;0.1.1&#34;" target="version" />
                      <zeebe:output source="=&#34;Withdrawal&#34;" target="eventType" />
                    </zeebe:ioMapping>
                  </bpmn:extensionElements>
                </bpmn:startEvent>
              </bpmn:process>
              <bpmndi:BPMNDiagram id="BPMNDiagram_1">
                <bpmndi:BPMNPlane id="BPMNPlane_1" bpmnElement="Process_0qr2f1y">
                  <bpmndi:BPMNShape id="BPMNShape_0gbdl80" bpmnElement="StartEventCPA">
                    <dc:Bounds x="162" y="82" width="36" height="36" />
                    <bpmndi:BPMNLabel>
                      <dc:Bounds x="154" y="128" width="52" height="14" />
                    </bpmndi:BPMNLabel>
                  </bpmndi:BPMNShape>
                </bpmndi:BPMNPlane>
              </bpmndi:BPMNDiagram>
            </bpmn:definitions>
            """;
    // Event generator event names
    public static final String CRM_DEPOSIT_EVENT = "deposit";
    public static final String CRM_LOGIN_EVENT = "login";
    public static final String CRM_REGISTRATION_EVENT = "registration";
    public static final String CRM_WITHDRAWAL_EVENT = "withdrawal";
    public static final String EG_LOGIN_EVENT = "egLoginToWeb";
    public static final String EG_RAF_BALANCE_EVENT = "egRaf";
    public static final String REGISTRATION_EVENT = "registration";
    public static final String EG_WITHDRAWAL_EVENT = "egWithdrawal";
    public static final String KAFKA_MESSAGE_KEY = "QA";
    public static final String MT_CLOSE_TRADE_EVENT = "closeTrade";
    public static final String MT_OPEN_TRADE_EVENT = "openTrade";
    public static final String MT_RAF_BALANCE_EVENT = "raf";
    //Other
    public static final String APPLIED_STATUS = "APPLIED";
    public static final String COMMENT_ADDED_TYPE = "COMMENT_ADDED";
    public static final String CONNECTION_SEARCH_DATA_DOCUMENT_HIDDEN = "3***********2";
    public static final String CONNECTION_SEARCH_DATA_EMAIL_HIDDEN = "m***e@gmx.net";
    public static final String CONNECTION_SEARCH_DATA_PHONE_HIDDEN = "+1*********3";
    public static final String ENCODED_EMAIL = "paRP/scRJ89KbGWkZOrJVF/FbLaXR1jx";
    public static final String ENCODED_PHONE = "OlIoGyRiWyMgmlKyQZkW6w==";
    public static final String FRAUD_TYPE_MORE_UNKNOWN = "MOREUNKNOWN";
    public static final String FRAUD_TYPE_SOURCE_INSIGHT = "INSIGHT";
    public static final String FRAUD_TYPE_SOURCE_VINDEX = "VINDEX";
    public static final String FRAUD_TYPE_UNKNOWN = "UNKNOWN";
    public static final String POTENTIAL_ABUSE = "Potential abuse";
    public static final String RESTRICTION_APPLIED_STATUS = "RESTRICTION_APPLIED";
    public static final String RESTRICTION_REQUESTED_STATUS = "RESTRICTION_REQUESTED";
    public static final String RESTRICTION_TYPE_GENERAL = "GENERAL";
    public static final String RESTRICTION_TYPE_TRADING = "TRADING";
    public static final String STATUS_NEW = "new";
    public static final String STATUS_NORMAL = "Normal";
    public static final String STATUS_SUSPICIOUS = "Suspicious";
    public static final String VINDEX_BO_SYSTEM = "Vindex BO";
    public static final String CRM_FILES_EXAMPLE_NAME = "/other/5b14c35f65cb4eebb7f4e1f375049c85.jpeg";
    public static final String CRM_FILES_EXAMPLE_NAME_OTHER = "/other/d26364637ea848f0b4d2e363ea761461.png";
}
