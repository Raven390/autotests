package utils;

public class Constants {
    // TEAMS
    public static final String TEAM_CORE = "team_core";
    public static final String TEAM_BACKOFFICE = "team_backoffice";
    // SUITE
    public static final String SUITE_REGRESSION = "suite_regression";
    public static final String SUITE_SMOKE = "suite_smoke";
    public static final String SUITE_SMOKE_PROD = "suite_smoke_production";
    public static final String SUITE_CLICKHOUSE_API_SERVICE = "suite_clickhouse_api_service";
    public static final String SUITE_CONNECTION_SEARCH_SERVICE = "suite_connection_search_service";
    public static final String SUITE_EVENT_GENERATOR_SERVICE = "suite_event_generator_service";
    public static final String SUITE_MITIGATION_SERVICE = "suite_mitigation_service";
    public static final String SUITE_RULE_ENGINE_RULES_TESTS = "suite_rule_engine_rules_tests";
    public static final String SUITE_RULE_ENGINE_API_TESTS = "suite_rule_engine_api_tests";
    public static final String SUITE_MIRROR_TRADING_SCORE_API_TESTS = "mirror_trading_score_api_tests";
    public static final String DEBUG_RUNNER = "debug_runner";
    // TEST LAYERS
    public static final String LAYER_API = "layer_api";
    public static final String LAYER_WEB = "layer_web";
    public static final String LAYER_MOBILE = "layer_mobile";
    public static final String LAYER_KAFKA = "layer_kafka";
    // FEATURES
    public static final String FEATURE_LEXIS_NEXIS = "Lexis Nexis integration";
    public static final String FEATURE_RULE_ENGINE_SERVICE = "Rule engine service";
    public static final String FEATURE_CLICKHOUSE_API_SERVICE = "Clickhouse api service";
    public static final String FEATURE_EVENT_GENERATOR_SERVICE = "Event generator service";
    public static final String FEATURE_CONNECTION_SEARCH_API_SERVICE = "Connection search api service";
    public static final String FEATURE_MIRROR_TRADING_SCORE_API_SERVICE = "Mirror trading score api service";
    // STORIES
    public static final String STORY_PRODUCTION_TESTS = "Production smoke test cases";
    public static final String STORY_EVENT_GENERATOR_SERVICE_LOGIN = "Login event in Event Generator";
    public static final String STORY_EVENT_GENERATOR_SERVICE_REGISTRATION = "Registration event in Event Generator";
    public static final String STORY_EVENT_GENERATOR_SERVICE_WITHDRAWAL = "Withdrawal event in Event Generator";
    public static final String STORY_EVENT_GENERATOR_SERVICE_OPEN_TRADE = "Open trade event in Event Generator";
    public static final String STORY_EVENT_GENERATOR_SERVICE_CLOSE_TRADE = "Close trade event in Event Generator";
    public static final String STORY_EVENT_GENERATOR_SERVICE_RAF_BALANCE = "Raf balance trade event in Event Generator";
    public static final String STORY_DATA_DUMPER_CLOSE_TRADE_EVENT = "Data dumper. Close trade event";
    public static final String STORY_DATA_DUMPER_OPEN_TRADE_EVENT = "Data dumper. Open trade event";
    public static final String STORY_DATA_DUMPER_STOP_OUT_EVENT = "Data dumper. Stopout trade event";
    public static final String STORY_DATA_DUMPER_LOSS_COMPENSATION_EVENT = "Data dumper. Trade loss compensation event";
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
    public static final String STORY_CLICKHOUSE_API_SERVICE_GET_ABUSE_TYPES = "Clickhouse api. Post abuse types";
    public static final String STORY_CLICKHOUSE_API_SERVICE_GET_CREDIT_RISK_FREE_REVENUE_RATIO = "Clickhouse api. Get credit risk free revenue ratio";
    public static final String STORY_CLICKHOUSE_API_SERVICE_GET_SWAP_FREE_FEES = "Clickhouse api. Get swap free fees";
    public static final String STORY_CLICKHOUSE_API_SERVICE_GET_SWAP_FREE_VOLUME = "Clickhouse api. Get swap free volume";
    public static final String STORY_CONNECTION_SEARCH_BY_CLIENT_ID = "Get connections by client id";
    public static final String STORY_CONNECTION_SEARCH_BY_ATTRIBUTES = "Get connections by attribute";
    public static final String STORY_CHECK_CONNECTED_IB = "Check connected IB";
    public static final String STORY_GET_MIRROR_TRADING_SCORE = "Get mirror trading score";
    public static final String STORY_RULE_ENGINE_MIRROR_TRADING_RULE = "Mirror trading rule in Rule Engine service";
    public static final String STORY_RULE_ENGINE_REGISTRATION_RULE = "Registration rule in Rule Engine service";
    public static final String STORY_RULE_ENGINE_CPA_ABUSE_RULE = "CPA abuse rule in Rule Engine service";
    public static final String STORY_RULE_ENGINE_ABNORMAL_PROFIT_RULE = "Abnormal profit rule in Rule Engine service";
    public static final String STORY_RULE_ENGINE_LOSS_VOUCHER_RULE = "Loss voucher rule in Rule Engine service";
    public static final String STORY_RULE_ENGINE_NDB_ABUSE_RULE = "No deposit bonus abuse rule in Rule Engine service";
    public static final String STORY_RULE_ENGINE_MARKET_MANIPULATION_RULE = "Market manipulation rule in Rule Engine service";
    public static final String STORY_RULE_ENGINE_WITHDRAWAL_NOTIFICATION_RULE = "Withdrawal notification rule in Rule Engine service";
    public static final String STORY_CLICKHOUSE_API_SERVICE_GET_FAST_TRADES = "Clickhouse api. Get fast trades";
    public static final String STORY_RULE_ENGINE_NPB_LOSING_LEG_RULE = "NBP losing leg rule in Rule Engine service";
    public static final String STORY_RULE_ENGINE_NPB_WINNING_LEG_RULE = "NBP winning leg rule in Rule Engine service";
    // PAYMENT TYPES
    public static final String PAYMENT_TYPE_WITHDRAWAL = "withdrawal";
    public static final String PAYMENT_TYPE_DEPOSIT = "deposit";
    public static final String PAYMENT_TYPE_BONUS = "bonus";
    // PAYMENT PROVIDERS
    public static final String PAYMENT_PROVIDER_FASAPAY = "fasapay";
    // OTHER
    public static final String TAG_AUTOMATED = "automated";
    public static final String TAG_MANUAL = "manual";
    public static final String ROLE_UNKNOWN = "unknown";
    // KAFKA
    public static final String KAFKA_TOPIC_CRM_EVENTS = "crm-events";
    public static final String KAFKA_TOPIC_CRM_DB_EVENTS = "crm-db-events";
    public static final String KAFKA_TOPIC_MT_EVENTS = "mt-events";
    public static final String KAFKA_TOPIC_MT_DB_EVENTS = "mt-db-events";
    public static final String KAFKA_TOPIC_MT_4_TRADE_RECORD = "mt4_trade_record";
    public static final String KAFKA_TOPIC_MT_5_DEAL = "mt5_Deal";
    public static final String KAFKA_TOPIC_ALERTS = "alerts";
    public static final String KAFKA_NO_MESSAGE_FOUND_ERROR = "Max attempts reached without finding a matching message";
    public static final String KAFKA_ALL_PARAMETERS_FOUND = "All the parameters were found in messages.";
    public static final String KAFKA_SOME_PARAMETERS_FOUND = "Some of the parameters were not found in messages.";
    public static final String KAFKA_NO_PARAMETERS_PROVIDED = "No search parameters provided.";
    public static final String KAFKA_TOPIC_ACCOUNT_RESTRICTIONS_APPLY = "account.restrictions.apply";
    public static final String KAFKA_TOPIC_ACCOUNT_RESTRICTIONS_CANCEL = "account.restrictions.cancel";
    public static final String KAFKA_TOPIC_CLIENT_RESTRICTIONS_CANCEL = "client.restrictions.cancel";
    public static final String KAFKA_TOPIC_CLIENT_RESTRICTIONS_APPLY = "client.restrictions.apply";
    public static final String KAFKA_TOPIC_WITHDRAWAL_APPROVALS = "withdrawal.approvals";
    // PARAMS
    public static final String REGISTRATION_HELPER_FIRST_NAME = "Test";
    public static final String REGISTRATION_HELPER_SECOND_NAME = "User";
    public static final String REGISTRATION_HELPER_REGULATOR_VFSC2 = "VFSC2";
    public static final String REGISTRATION_HELPER_WID_VANTAGE = "AU";
    public static final String REGISTRATION_HELPER_INTERFACE = "/api/registrationV2/register";
    // TABLES
    public static final String CRM_ACCOUNT_TABLE_NAME = "vindex_test.crm___tb_account";
    public static final String MT_ACCOUNT_TABLE_NAME = "vindex_test.mt___account";
    public static final String CRM_USER_TABLE_NAME = "vindex_test.crm___tb_user";
    public static final String CRM_BONUS_TABLE_NAME = "vindex_test.crm___tb_bonus";
    public static final String CRM_DEPOSIT_TABLE_NAME = "vindex_test.crm___tb_deposit";
    public static final String CRM_WITHDRAWAL_TABLE_NAME = "vindex_test.crm___tb_withdrawal";
    public static final String MT_CREDITS_TABLE_NAME = "vindex_test.mt___credit_orders";
    public static final String CRM_TRANSFERS_TABLE_NAME = "vindex_test.crm___tb_transfer";
    public static final String MT_BALANCE_ORDERS_TABLE_NAME = "vindex_test.mt___balance_orders";
    public static final String MT_TRADES_TABLE_NAME = "vindex_test.mt__tb_trade";
    public static final String MT4_TRADES_COERCED_TABLE_NAME = "vindex_test.mt___mt4_trades_coerced";
    public static final String MT4_TRADES_TABLE_NAME = "vindex_test.mt___mt4_trades";
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
    public static final String BO_CLIENT_FRAUD_TYPES_TABLE_NAME = "vindex_test.client_fraud_types";
    public static final String CLIENT_FRAUD_TYPES_TABLE_NAME = "vindex_test.client_fraud_types";
    public static final String MITIGATION_CLIENT_RESTRICTION_GENERAL = "postgres.mi.client_general_restriction";
    public static final String MITIGATION_CLIENT_GENERAL_RESTRICTION_ACTION = "postgres.mi.client_general_restriction_action";
    public static final String MITIGATION_CLIENT_TRADING_RESTRICTION_ACTION = "postgres.mi.client_trading_restriction_action";
    public static final String MITIGATION_CLIENT_RESTRICTION_TRADING = "postgres.mi.client_trading_restriction";
    public static final String MITIGATION_CLIENT_TRADING_RESTRICTION_STATUS_BY_SITE = "postgres.mi.client_trading_restriction_status_by_site";
    public static final String MITIGATION_CLIENTS_RESTRICTION_OLD = "postgres.mi.clients_restriction";
    public static final String MITIGATION_ACTION = "postgres.mi.action";
    public static final String MITIGATION_KAFKA_REQUEST_OLD = "postgres.mi.kafka_request";
    public static final String MITIGATION_KAFKA_REQUEST_TRADING = "postgres.mi.client_trading_restriction_kafka_request";
    public static final String MITIGATION_KAFKA_REQUEST_GENERAL = "postgres.mi.client_general_restriction_kafka_request";
    public static final String MITIGATION_KAFKA_RESPONSE_OLD = "postgres.mi.kafka_response";
    public static final String MITIGATION_KAFKA_RESPONSE_TRADING = "postgres.mi.client_trading_restriction_kafka_response";
    public static final String MITIGATION_KAFKA_RESPONSE_GENERAL = "postgres.mi.client_general_restriction_kafka_response";
    public static final String AUDIT_EVENT = "postgres.au.event";
    public static final String MT5_DEALS_COERCED_TABLE_NAME = "vindex_test.mt___mt5_deals_coerced";
    public static final String MT5_POSITIONS_TABLE_NAME = "vindex_test.mt___mt5_positions";
    public static final String BO_ALERT_TABLE_NAME = "postgres.bo.alert";
    public static final String BO_CLIENT_TABLE_NAME = "postgres.bo.client";
    public static final String BO_BACKOFFICE_USER_TABLE_NAME = "postgres.bo.backoffice_user";
    public static final String AGGR_CREDIT_EQUITY_RATE = "vindex_test_api.aggr__credit_equity_rate";
    public static final String AGGR_CREDIT_RISK_FREE_REVENUE_RATIO = "vindex_test_api.aggr__credit_risk_free_revenue_ratio";
    public static final String BO_USER_ACTION_AUDIT_TABLE_NAME = "postgres.bo.user_action_audit";
    public static final String BO_USER_SESSION_TABLE_NAME = "postgres.bo.user_session";
    public static final String BO_CLIENTS_FRAUD_TYPES_TABLE_NAME = "postgres.bo.clients_fraud_types";
    public static final String KYC_FILES_TABLE_NAME = "vindex_test.crm___tb_kyc_files";
    public static final String ID_PROOF_TABLE_NAME = "vindex_test.crm___tb_id_proof";
    public static final String CLIENT_METRICS_LIFETIME_TABLE_NAME = "vindex_test.client_metrics_lifetime";
    public static final String MIRROR_LOGIN_TABLE_NAME = "vindex_test.mirror_login";
    public static final String MIRROR_UCID_TABLE_NAME = "vindex_test.mirror_ucid";
    public static final String S3_DIM_CLIENT = "vindex_test.s3___dim_client";
    public static final String TS_BY_SYMBOL_DAILY_TABLE_NAME = "vindex_test.ts_by_symbol_daily";
    public static final String AGGR_FLOATING_TRADES_GROUP_BY = "vindex_test_api.aggr__floating_trades_group_by";
    public static final String ACCOUNT_IB_RELATION_TABLE_NAME = "vindex_test.account_ib_relation";
    public static final String S3_FACT_IB_SALES_COMMISSIONS = "vindex_test.s3___fact_ib_sales_commissions";
    public static final String S3_FACT_CPA_COMMISSIONS = "vindex_test.s3___fact_cpa_commissions";
    public static final String PAYMENTS_TOTAL_TABLE_NAME = "vindex_test.payments_total";
    public static final String SEGMENTATION_TABLE_NAME = "data_science_test.segmentation_table";
    public static final String MT5_DEALS_COERCED_TOXICITY_TABLE_NAME = "vindex_test.mt___mt5_deals_coerced_toxicity";
    public static final String DP_AND_WD_BY_CHANNEL_TABLE_NAME = "vindex_test.dp_and_wd_by_channel";
    public static final String CRM_TB_LOYALTY_REDEMPTION = "vindex_test.crm___tb_loyalty_redemption";
    public static final String S3_FACT_LOGIN_METRICS_TABLE_NAME = "vindex_test.s3___fact_login_metrics";
    public static final String DICT_ACCOUNT_TO_UCID = "vindex_test.dict_account_to_ucid";
    public static final String IB_SUMMARY_LIFETIME_TABLE_NAME = "datamarts_test.ib_summary_lifetime";
    public static final String IB_SUMMARY_BY_DATE_TABLE_NAME = "datamarts_test.ib_summary_by_date";
    public static final String CRM_TB_USER_EXTENDS_TABLE_NAME = "vindex_test.crm___tb_user_extends";
    public static final String RULE_ENGINE_RULE_TABLE = "ruleengine.rule";
    public static final String RULE_ENGINE_RULE_DEPLOYMENT_TABLE = "ruleengine.rule_deployment";
    public static final String ACCOUNT_IB_RELATION_SNAPSHOT_TABLE_NAME = "vindex_test.account_ib_relation_snapshot";
    public static final String BO_WD_REQUEST_TABLE_NAME = "postgres.bo.wd_request";
    public static final String DATA_SCIENCE_UCID_MIRROR_SCORE_TABLE_NAME = "data_science_test.ucid_mirror_score";
    public static final String DATA_SCIENCE_MIRROR_DATA_WITH_STAT_TABLE_NAME = "data_science_test.mirror_model_mirror_data_with_stat";
    // UI
    public static final String VANTAGE_BRAND_IMAGE_SRC = "data:image/svg+xml,%3csvg%20width='33'%20height='32'%20viewBox='0%200%2033%2032'%20fill='none'%20xmlns='http://www.w3.org/2000/svg'%3e%3crect%20width='32'%20height='32'%20transform='translate(0.5)'%20fill='%23034854'/%3e%3cpath%20d='M12.617%208.98231H8.34277L16.9316%2024.9503L19.0687%2020.9986L12.617%208.98231Z'%20fill='white'/%3e%3cpath%20d='M14.1896%208.9823H25.5608L19.9155%2019.426V12.5711L14.1896%208.9823Z'%20fill='%23E35728'/%3e%3c/svg%3e";
    // KYC files
    public static final String FILE_KYC_POF_1_NAME = "/testFaceFile1.jpg";
    public static final String FILE_KYC_POF_2_NAME = "/testFaceFile2.jpg";
    public static final String FILE_KYC_POF_3_NAME = "/testFaceFile3.jpg";
    // Connection search values
    public static final String CONNECTION_ATTRIBUTE_NAME_EMAIL = "email";
    public static final String CONNECTION_ATTRIBUTE_NAME_EMAIL_ADDRESS = "emailAddress";
    public static final String CONNECTION_ATTRIBUTE_NAME_PAYOUT = "payout";
    public static final String CONNECTION_ATTRIBUTE_NAME_PAYOUT_ID = "payoutId";
    public static final String CONNECTION_ATTRIBUTE_NAME_SESSION = "session";
    public static final String CONNECTION_ATTRIBUTE_NAME_WEB_SESSION = "webSession";
    public static final String CONNECTION_ATTRIBUTE_NAME_NAME_BIRTH = "nameBirth";
    public static final String CONNECTION_ATTRIBUTE_NAME_NAME_AND_BIRTH = "name+dateofbirth";
    public static final String CONNECTION_ATTRIBUTE_NAME_DIGITAL = "digital";
    public static final String CONNECTION_ATTRIBUTE_NAME_DEVICE = "device";
    public static final String CONNECTION_ATTRIBUTE_NAME_PHONE = "phone";
    public static final String CONNECTION_ATTRIBUTE_NAME_IP_ADDRESS = "ipAddress";
    public static final String CONNECTION_ATTRIBUTE_NAME_IP = "ip";
    public static final String CONNECTION_ATTRIBUTE_NAME_DOCUMENT_TYPE = "documentType";
    public static final String CONNECTION_ATTRIBUTE_NAME_DOCUMENT = "document";
    public static final String CONNECTION_TYPE_SAME_PERSON = "Same Person";
    public static final String CONNECTION_TYPE_SAME_NETWORK = "Same Network";
    public static final String CONNECTION_TYPE_SAME_IDENTITY = "Same Identity";
    public static final String CONNECTION_TYPE_RELATION_TYPE_EXACT = "exact";
    public static final String CONNECTION_TYPE_RELATION_TYPE_SIMILAR = "similar";
    public static final String CONNECTION_SEARCH_DATA_CARD_NUMBER = "535456**** **0344";
    public static final String CONNECTION_SEARCH_DATA_EMAIL1 = "matisse@gmx.net";
    public static final String CONNECTION_SEARCH_DATA_EMAIL2 = "matisse@gmx.net";
    public static final String CONNECTION_SEARCH_DATA_IP1 = "92.14.100.34";
    public static final String CONNECTION_SEARCH_DATA_IP2 = "92.14.100.35";
    public static final String CONNECTION_SEARCH_DATA_IP3 = "111.111.111.111";
    public static final String CONNECTION_SEARCH_DATA_IP4 = "124.12.12.42";
    public static final String CONNECTION_SEARCH_DATA_IP5 = "124.12.12.46";
    public static final String CONNECTION_SEARCH_DATA_DEVICE = "4a25971ab724427eb8fc24a257c5b2df";
    public static final String CONNECTION_SEARCH_DATA_DEVICE2 = "5a25971ab724427eb8fc24a257c5b2df";
    public static final String CONNECTION_SEARCH_DATA_DOCUMENT = "3110200460092";
    //
    public static final String COMMENT_AUTOMATION_TESTS = "Automation tests";
    public static final String TIME_2024_12_31_00_00_00 = "2024-12-31 00:00:00";
    public static final String TIME_2024_2024_12_29_14_59_30_084000000 = "2024-12-29 14:59:30.084000000";
    public static final String ACCOUNT_TYPE_STANDARD = "Standard";
    public static final String ACCOUNT_TYPE_SWAP_FREE = "Swap free";
    public static final String ACCOUNT_GROUP_S_VFX_EUR = "S_VFX_EUR";
    public static final String ACCOUNT_STATUS_ACTIVE = "Active";
    public static final String ACCOUNT_STATUS_INACTIVE = "Inactive";
    public static final String PLATFORM_MT_4 = "MT4";
    public static final String PLATFORM_MT_5 = "MT5";
    public static final String EURUSD = "EURUSD";
    public static final String EURGBP = "EURGBP";
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
    public static final String EG_LOGIN_EVENT = "egLoginToWeb";
    public static final String CRM_LOGIN_EVENT = "login";
    public static final String EG_REGISTRATION_EVENT = "egRegistration";
    public static final String CRM_REGISTRATION_EVENT = "registration";
    public static final String EG_WITHDRAWAL_EVENT = "egWithdrawal";
    public static final String CRM_WITHDRAWAL_EVENT = "withdrawal";
    public static final String EG_DEPOSIT_EVENT = "egDeposit";
    public static final String CRM_DEPOSIT_EVENT = "deposit";
    public static final String EG_CLOSE_TRADE_EVENT = "egCloseTrade";
    public static final String MT_CLOSE_TRADE_EVENT = "closeTrade";
    public static final String EG_OPEN_TRADE_EVENT = "egOpenTrade";
    public static final String MT_OPEN_TRADE_EVENT = "openTrade";
    public static final String EG_RAF_BALANCE_EVENT = "egRaf";
    public static final String MT_RAF_BALANCE_EVENT = "raf";
    public static final String KAFKA_MESSAGE_KEY = "QA";
    //Other
    public static final String FRAUD_TYPE_SOURCE_VINDEX = "VINDEX";
    public static final String FRAUD_TYPE_SOURCE_INSIGHT = "INSIGHT";
    public static final String FRAUD_TYPE_UNKNOWN = "UNKNOWN";
    public static final String FRAUD_TYPE_MOREUNKNOWN = "MOREUNKNOWN";
    public static final String RESTRICTION_TYPE_GENERAL = "GENERAL";
    public static final String RESTRICTION_TYPE_TRADING = "TRADING";
    public static final String RESTRICTION_REQUESTED_STATUS = "RESTRICTION_REQUESTED";
    public static final String RESTRICTION_APPLIED_STATUS = "RESTRICTION_APPLIED";
    public static final String COMMENT_ADDED_TYPE = "COMMENT_ADDED";
    public static final String VINDEX_BO_SYSTEM = "Vindex BO";
}
