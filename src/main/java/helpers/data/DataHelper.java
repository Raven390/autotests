package helpers.data;

import static business_objects.db.clickhouse.bo_alerts.BoAlertsFactory.generateAlert;
import static business_objects.db.clickhouse.client_cards.ClientCardObjectFactory.generateClientCardsObject;
import static business_objects.db.clickhouse.client_fraud_types.ClientFraudTypesFactory.createClientFraudTypeCh;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateAccountByClient;
import static business_objects.db.clickhouse.crm_tb_account_for_mt.crm_tb_account.CrmTbAccountForMtObjectFactory.generateAccountForMtByClient;
import static business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient;
import static business_objects.db.clickhouse.data_science_test.connection_table.ConnectionTableEntry.ConnectionInfo.connectionInfoToString;
import static business_objects.db.clickhouse.data_science_test.connection_table.ConnectionTableEntryFactory.getConnection;
import static business_objects.db.clickhouse.data_science_test.device_id_table.DeviceIdTableEntryFactory.deviceIdTableEntryForConnectionSearch;
import static business_objects.db.clickhouse.data_science_test.document_table.DocumentTableEntryFactory.documentTableEntryForConnectionSearch;
import static business_objects.db.clickhouse.data_science_test.email_table.EmailTableEntryFactory.emailTableEntryForConnectionSearch;
import static business_objects.db.clickhouse.data_science_test.payout.PayoutTableEntryFactory.payoutTableEntryForConnectionSearch;
import static business_objects.db.clickhouse.data_science_test.phone.PhoneTableEntryFactory.phoneTableEntryForConnectionSearch;
import static business_objects.db.clickhouse.ln_session_parsed.LnSessionParsedObjectFactory.generateLexisNexisDataByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByClient;
import static helpers.api.AbuseRegistryHelper.addFraudsForClient;
import static helpers.database.DbHelper.*;
import static utils.Constants.*;

import businessObjects.db.clickhouse.ozTrades.OzTradesTableEntry;
import business_objects.db.clickhouse.aggr_credit_equity_rate.AggrCreditEquityRateObject;
import business_objects.db.clickhouse.aggr_floating_trades_group_by.AggrFloatingTradesGroupBy;
import business_objects.db.clickhouse.aggr_mirror_accounts_by_trades.MirrorLoginObject;
import business_objects.db.clickhouse.app_tb_finindex_data.AppTbFinindexData;
import business_objects.db.clickhouse.bo_alerts.BoAlertsObject;
import business_objects.db.clickhouse.client_cards.ClientCardsObject;
import business_objects.db.clickhouse.client_fraud_types.ClientFraudTypes;
import business_objects.db.clickhouse.cost_payment_fee.CostPaymentFee;
import business_objects.db.clickhouse.crm_bp_callbacks.CrmBpCallbacksObject;
import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_account_for_mt.crm_tb_account.CrmTbAccountForMtObject;
import business_objects.db.clickhouse.crm_tb_bonus_table.CrmTbBonusObject;
import business_objects.db.clickhouse.crm_tb_deposit_channel.CrmTbDepositChannelObject;
import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntity;
import business_objects.db.clickhouse.crm_tb_deposit_type.CrmTbDepositTypeObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalEntity;
import business_objects.db.clickhouse.crm_tb_withdrawal_type.CrmTbWithdrawalTypeObject;
import business_objects.db.clickhouse.data_science_test.connection_table.ConnectionTableEntry;
import business_objects.db.clickhouse.data_science_test.device_id_table.DeviceIdTableEntry;
import business_objects.db.clickhouse.data_science_test.document_table.DocumentTableEntry;
import business_objects.db.clickhouse.data_science_test.email_table.EmailTableEntry;
import business_objects.db.clickhouse.data_science_test.ip_table.IpTableEntry;
import business_objects.db.clickhouse.data_science_test.payout.PayoutTableEntry;
import business_objects.db.clickhouse.data_science_test.phone.PhoneTableEntry;
import business_objects.db.clickhouse.data_science_test.session_id.SessionIdTableEntry;
import business_objects.db.clickhouse.dict_account_to_ucid.DictAccountToUcidObject;
import business_objects.db.clickhouse.dict_active_trading_days_by_ucid.dict_is_test.DictActiveTradingDaysByUcidObject;
import business_objects.db.clickhouse.dict_is_test.DictIsTestObject;
import business_objects.db.clickhouse.ln_session_parsed.LnSessionParsedObject;
import business_objects.db.clickhouse.loyalties_redemption.LoyaltiesRedemptionObject;
import business_objects.db.clickhouse.mirror_ucid_table.MirrorUcidObject;
import business_objects.db.clickhouse.mt___mt5_deals_coerced_dd.Mt5DealsCoercedDd;
import business_objects.db.clickhouse.mt___mt5_deals_coerced_dd.Mt5DealsCoercedDdObjectV2;
import business_objects.db.clickhouse.mt___symbol_session.MtSymbolSession;
import business_objects.db.clickhouse.mt_account.MtAccountObject;
import business_objects.db.clickhouse.mt_balance_orders_table.MtBalanceOrdersObject;
import business_objects.db.clickhouse.mt_mt4_trades.MtMt4TradesObject;
import business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedObject;
import business_objects.db.clickhouse.mt_mt5_positions.MtMt5PositionsObject;
import business_objects.db.clickhouse.mt_tb_credits.MtTbCreditsObject;
import business_objects.db.clickhouse.s3MtSpreads.S3MtSpreadsObject;
import business_objects.db.clickhouse.s3_fact_ib_sales_commissions.S3FactIbSalesCommissionsObject;
import business_objects.db.clickhouse.s3_fact_login_metrics.S3FactLoginMetricsObject;
import business_objects.db.clickhouse.segmentation_table.SegmentationTableObject;
import business_objects.db.data_science.ucid_general_score.UcidGeneralScore;
import business_objects.db.data_science.ucid_general_score_python_test.UcidGeneralScorePythonTest;
import business_objects.db.data_science.ucid_mirror_score_python.UcidMirrorScorePython;
import business_objects.db.payment_gate.payment_details.PaymentDetailsObject;
import business_objects.db.payment_gate.payment_events.PaymentEventsObject;
import business_objects.db.payment_gate.payment_rule_executions.PaymentRuleExecutionsObject;
import business_objects.db.ticks.rates_usd_current.RatesUsdCurrentObject;
import business_objects.kafka.CustomEvent;
import business_objects.kafka.InternalHedgeEvent;
import business_objects.kafka.MirrorScoreEvent;
import business_objects.kafka.alerts.RuleAlert;
import business_objects.kafka.crm_events.*;
import business_objects.kafka.crm_events.CallbackEvent.CallbackEvent;
import business_objects.kafka.crm_events.CrmWithdrawalEvent;
import business_objects.kafka.crm_events.LoginEvent;
import business_objects.kafka.crm_events.RegistrationEvent;
import business_objects.kafka.crm_events.TransferToWaEvent;
import business_objects.kafka.mt_events.CloseTradeMtEvent;
import business_objects.kafka.mt_events.TradeEvent;
import helpers.data.enums.FraudType;
import helpers.data.enums.FraudTypeOld;
import helpers.data.enums.FraudTypeStatus;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import utils.Utils;

@Getter
@Setter
public class DataHelper {

    public ClientHelper clientHelper;
    public CrmTbUserObject crmTbUserObject;
    public DictAccountToUcidObject dictAccountToUcidObject;
    public DictIsTestObject dictIsTestObject;
    public List<DictActiveTradingDaysByUcidObject> dictActiveTradingDaysByUcidObject;
    public LnSessionParsedObject lnSessionParsedObjectRegistration;
    public LnSessionParsedObject lnSessionParsedObjectLogin;
    public List<ConnectionTableEntry> connections;
    public List<CrmTbUserObject> connectedUsers;
    public List<CostPaymentFee> costPaymentFees;
    public CloseTradeMtEvent closeTradeEvent;
    public TradeEvent tradeEvent;
    public List<ClientFraudTypes> clientFraudTypes;
    public List<ClientCardsObject> clientCards;
    public List<SegmentationTableObject> segmentObjects;
    private List<MtSymbolSession> mtSymbolSessions;
    public List<CrmBpCallbacksObject> callbacksObjects;
    public CrmTbAccountObject crmTbAccountObject;
    public CrmTbAccountForMtObject crmTbAccountForMtObject;
    public List<CrmTbAccountObject> crmTbAccountObjectConnections;
    public List<MtTbCreditsObject> mtTbCreditsObjects;
    public List<CrmTbWithdrawalTypeObject> crmTbWithdrawalTypeObjects;
    public List<CrmTbDepositTypeObject> crmTbDepositTypeObjects;
    public List<CrmTbDepositChannelObject> crmTbDepositChannelObjects;
    public List<CrmTbWithdrawalEntity> crmTbWithdrawalObjects;
    public List<CrmTbDepositEntity> crmTbDepositObjects;
    public List<CrmTbBonusObject> crmTbBonusObjects;
    public List<Mt5DealsCoercedObject> mt5DealsCoercedObjects;
    public List<Mt5DealsCoercedDd> mt5DealsCoercedDdObjects;
    public List<Mt5DealsCoercedDdObjectV2> mt5DealsCoercedDdObjectsV2;
    public List<PaymentEventsObject> paymentEventsObjects;
    public List<PaymentDetailsObject> paymentDetailsObjects;
    public List<PaymentRuleExecutionsObject> paymentRuleExecutionsObjects;
    public List<RatesUsdCurrentObject> ratesUsdCurrentObjects;
    public AggrCreditEquityRateObject aggrCreditEquityRate;
    public MirrorLoginObject aggrMirrorAccountsByTrades;
    public List<MtBalanceOrdersObject> mtBalanceOrdersObjects;
    public List<MirrorLoginObject> mirrorLoginObjects;
    public List<AggrFloatingTradesGroupBy> floatingTrades;
    public List<ClientHelper> connectedClientHelpers;
    public List<MirrorUcidObject> mirrorUcidObjects;
    public MtAccountObject mtAccountObject;
    public List<LoyaltiesRedemptionObject> loyaltyObjects;
    public List<MtMt5PositionsObject> mtMt5PositionsObjects;
    public LnSessionParsedObject lnSessionParsedObject;
    public RegistrationEvent registrationEvent;
    public CallbackEvent callbackEvent;
    public LoginEvent loginEvent;
    public List<SessionIdTableEntry> sessionIdTableEntries;
    public List<EmailTableEntry> emailTableEntries;
    public List<PhoneTableEntry> phoneTableEntries;
    public List<DocumentTableEntry> documentTableEntries;
    public List<IpTableEntry> ipTableEntries;
    public List<DeviceIdTableEntry> deviceIdTableEntries;
    public List<PayoutTableEntry> payoutTableEntries;
    public CloseTradeMtEvent closeTradeMtEvent;
    public List<Mt5DealsCoercedObject> mt5DealsObjects;
    public List<S3FactIbSalesCommissionsObject> s3FactIbSalesCommissionsObject;
    public List<S3FactLoginMetricsObject> S3FactLoginMetricsObjects;
    public List<MtMt4TradesObject> MtMt4TradesObjects;
    public UcidMirrorScorePython ucidMirrorScore;
    public List<RuleAlert> ruleAlerts;
    public List<BoAlertsObject> boAlertsObjects;
    public List<OzTradesTableEntry> ozTradesTableObjects;
    public UcidGeneralScore ucidGeneralScore;
    public List<UcidGeneralScore> ucidGeneralScores;
    public List<AppTbFinindexData> AppTbFinindexData;
    public CrmWithdrawalEvent crmWithdrawalEvent;
    public CrmWithdrawalEventV2 crmWithdrawalEventV2;
    public CustomEvent customEvent;
    public MirrorScoreEvent mirrorScoreEvent;
    public InternalHedgeEvent internalHedgeEvent;
    public TransferToWaEvent transferToWaEvent;
    public UcidGeneralScorePythonTest ucidGeneralScorePythonTest;
    public List<S3MtSpreadsObject> s3MtSpreadsObject;

    public DataHelper() {
        this.clientFraudTypes = new ArrayList<>();
    }

    public DataHelper createClient(ClientHelper clientHelper) {
        this.clientHelper = clientHelper;
        this.crmTbUserObject = generateUserByClient(this.clientHelper);
        this.crmTbAccountObject = generateAccountByClient(this.clientHelper, false);
        this.crmTbAccountForMtObject = generateAccountForMtByClient(this.clientHelper, false);
        this.mtAccountObject = generateMtAccountByClient(this.clientHelper);
        this.lnSessionParsedObject = generateLexisNexisDataByClient(this.clientHelper);
        return this;
    }

    protected static void setupAttrConnectionPayoutIdAndNameBirthWithMaxScore(
            DataHelper data, ClientHelper connectedClient) {

        if (data.connections == null) {
            data.connections = new ArrayList<>();
        }
        if (data.deviceIdTableEntries == null) {
            data.deviceIdTableEntries = new ArrayList<>();
        }
        if (data.documentTableEntries == null) {
            data.documentTableEntries = new ArrayList<>();
        }

        connectedClient.setDeviceId(data.clientHelper.getDeviceId());
        // add connection with connected client
        ConnectionTableEntry connection = getConnection(data.clientHelper, connectedClient);
        ConnectionTableEntry.ConnectionInfo connectionInfo1 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo1.connectionAttributeName = "payout";
        connectionInfo1.connectionAttributeValue = data.clientHelper.getDeviceId();
        connectionInfo1.sourceAttributeValue = data.clientHelper.getDeviceId();
        connectionInfo1.relationType = "exact";

        ConnectionTableEntry.ConnectionInfo connectionInfo2 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo2.connectionAttributeName = "document";
        connectionInfo2.connectionAttributeValue = "1";
        connectionInfo2.sourceAttributeValue = "1";
        connectionInfo2.relationType = "exact";

        connection.connectionInfo = connectionInfoToString(List.of(connectionInfo1, connectionInfo2));
        connection.connectionScore = 1d;
        data.connections.add(connection);

        data.lnSessionParsedObject.setDeviceId(data.clientHelper.getDeviceId());
        data.documentTableEntries.add(documentTableEntryForConnectionSearch(data.clientHelper));
        data.documentTableEntries.add(documentTableEntryForConnectionSearch(connectedClient));
    }

    protected static void setupAttrConnectionEmailPhoneWithCustomScore(
            DataHelper data, ClientHelper connectedClient, Double score) {

        if (data.connections == null) {
            data.connections = new ArrayList<>();
        }
        if (data.phoneTableEntries == null) {
            data.phoneTableEntries = new ArrayList<>();
        }
        if (data.emailTableEntries == null) {
            data.emailTableEntries = new ArrayList<>();
        }
        connectedClient.setEmail(data.clientHelper.getEmail());
        connectedClient.setPhoneNumber(data.clientHelper.getPhoneNumber());

        // add connection with connected client
        ConnectionTableEntry connection = getConnection(data.clientHelper, connectedClient, score);
        ConnectionTableEntry.ConnectionInfo connectionInfo1 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo1.connectionAttributeName = "email";
        connectionInfo1.connectionAttributeValue = data.clientHelper.getEmail();
        connectionInfo1.sourceAttributeValue = data.clientHelper.getEmail();
        connectionInfo1.relationType = "exact";
        ConnectionTableEntry.ConnectionInfo connectionInfo2 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo2.connectionAttributeName = "phone";
        connectionInfo2.connectionAttributeValue = data.clientHelper.getPhoneNumber();
        connectionInfo2.sourceAttributeValue = data.clientHelper.getPhoneNumber();
        connectionInfo2.relationType = "exact";
        connection.connectionInfo = connectionInfoToString(List.of(connectionInfo1, connectionInfo2));
        connection.connectionScore = score;
        data.connections.add(connection);
        // add email to LN record
        data.lnSessionParsedObject.setEmail(data.clientHelper.getEmail());
        data.lnSessionParsedObject.setMobile(data.clientHelper.getPhoneNumber());

        // add to emails table records with same email for initial and connected clients

        data.emailTableEntries.add(emailTableEntryForConnectionSearch(data.clientHelper, data.clientHelper.getEmail()));
        data.emailTableEntries.add(emailTableEntryForConnectionSearch(connectedClient, data.clientHelper.getEmail()));

        // add to phone table records with same email for initial and connected clients
        data.phoneTableEntries.add(
                phoneTableEntryForConnectionSearch(data.clientHelper, data.clientHelper.getPhoneNumber()));
        data.phoneTableEntries.add(
                phoneTableEntryForConnectionSearch(connectedClient, data.clientHelper.getPhoneNumber()));
    }

    public static void setupAttrConnectionDevice(DataHelper data, ClientHelper connectedClient) {

        if (data.connections == null) {
            data.connections = new ArrayList<>();
        }
        if (data.deviceIdTableEntries == null) {
            data.deviceIdTableEntries = new ArrayList<>();
        }
        connectedClient.setDeviceId(data.clientHelper.getDeviceId());

        // add connection with connected client
        ConnectionTableEntry connection = getConnection(data.clientHelper, connectedClient);
        ConnectionTableEntry.ConnectionInfo connectionInfo1 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo1.connectionAttributeName = "device";
        connectionInfo1.connectionAttributeValue = data.clientHelper.getDeviceId();
        connectionInfo1.sourceAttributeValue = data.clientHelper.getDeviceId();
        connectionInfo1.relationType = "exact";
        connection.connectionScore = 0.7;
        data.connections.add(connection);
        // add email to LN record
        data.lnSessionParsedObject.setDeviceId(data.clientHelper.getDeviceId());

        // add to emails table records with same email for initial and connected clients

        data.deviceIdTableEntries.add(
                deviceIdTableEntryForConnectionSearch(data.clientHelper, data.clientHelper.getDeviceId()));
        data.deviceIdTableEntries.add(
                deviceIdTableEntryForConnectionSearch(connectedClient, data.clientHelper.getDeviceId()));
    }

    public static void addConnectionByEmailPhoneAttribute(DataHelper data, ClientHelper clientTo, Double score) {
        if (data.connectedUsers == null) {
            data.connectedUsers = new ArrayList<>();
        }
        if (data.connectedClientHelpers == null) {
            data.connectedClientHelpers = new ArrayList<>();
        }
        data.connectedUsers.add(generateUserByClient(clientTo));
        data.connectedClientHelpers.add(clientTo);
        setupAttrConnectionEmailPhoneWithCustomScore(data, clientTo, score);
    }

    public static void setupAttrConnectionPayoutId(DataHelper data, ClientHelper connectedClient) {

        if (data.connections == null) {
            data.connections = new ArrayList<>();
        }
        if (data.payoutTableEntries == null) {
            data.payoutTableEntries = new ArrayList<>();
        }

        connectedClient.setDeviceId(data.clientHelper.getDeviceId());
        // add connection with connected client
        ConnectionTableEntry connection = getConnection(data.clientHelper, connectedClient);
        ConnectionTableEntry.ConnectionInfo connectionInfo1 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo1.connectionAttributeName = "payout";
        connectionInfo1.connectionAttributeValue = data.clientHelper.getDeviceId();
        connectionInfo1.sourceAttributeValue = data.clientHelper.getDeviceId();
        connectionInfo1.relationType = "exact";

        connection.connectionInfo = connectionInfoToString(List.of(connectionInfo1));
        connection.connectionScore = 1d;
        data.connections.add(connection);

        String payoutId = UUID.randomUUID().toString();

        data.lnSessionParsedObject.setDeviceId(data.clientHelper.getDeviceId());
        data.payoutTableEntries.add(payoutTableEntryForConnectionSearch(data.clientHelper, payoutId));
        data.payoutTableEntries.add(payoutTableEntryForConnectionSearch(connectedClient, payoutId));
    }

    public static void setupAttrConnectionDocumentAttribute(DataHelper data, ClientHelper connectedClient) {

        if (data.connections == null) {
            data.connections = new ArrayList<>();
        }
        if (data.documentTableEntries == null) {
            data.documentTableEntries = new ArrayList<>();
        }

        String documentId = Utils.getRandomUuidString();
        // add connection with connected client
        ConnectionTableEntry connection = getConnection(data.clientHelper, connectedClient);
        ConnectionTableEntry.ConnectionInfo connectionInfo1 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo1.connectionAttributeName = "document";
        connectionInfo1.connectionAttributeValue = documentId;
        connectionInfo1.sourceAttributeValue = documentId;
        connectionInfo1.relationType = "exact";

        connection.connectionInfo = connectionInfoToString(List.of(connectionInfo1));
        connection.connectionScore = 1d;
        data.connections.add(connection);

        data.documentTableEntries.add(documentTableEntryForConnectionSearch((data.clientHelper)));
    }

    public static void addConnectionByDeviceAttribute(DataHelper data, ClientHelper clientTo) {
        if (data.connectedUsers == null) {
            data.connectedUsers = new ArrayList<>();
        }
        if (data.connectedClientHelpers == null) {
            data.connectedClientHelpers = new ArrayList<>();
        }
        data.connectedUsers.add(generateUserByClient(clientTo));
        data.connectedClientHelpers.add(clientTo);
        setupAttrConnectionDevice(data, clientTo);
    }

    public static void addConnectionByPayoutIdAttribute(DataHelper data, ClientHelper clientTo) {
        if (data.connectedUsers == null) {
            data.connectedUsers = new ArrayList<>();
        }
        if (data.connectedClientHelpers == null) {
            data.connectedClientHelpers = new ArrayList<>();
        }
        data.connectedUsers.add(generateUserByClient(clientTo));
        data.connectedClientHelpers.add(clientTo);
        setupAttrConnectionPayoutId(data, clientTo);
    }

    public static void addConnectionByPayoutAndNameBirthAttribute(DataHelper data, ClientHelper clientTo) {
        if (data.connectedUsers == null) {
            data.connectedUsers = new ArrayList<>();
        }
        if (data.connectedClientHelpers == null) {
            data.connectedClientHelpers = new ArrayList<>();
        }
        data.connectedUsers.add(generateUserByClient(clientTo));
        data.connectedClientHelpers.add(clientTo);
        setupAttrConnectionPayoutIdAndNameBirthWithMaxScore(data, clientTo);
    }

    public static DataHelper addFraudTypeToConnectedUser(DataHelper data, FraudTypeStatus status, FraudType fraudType)
            throws IOException, InterruptedException {
        data.clientFraudTypes = new ArrayList<>();
        data.clientFraudTypes.add(createClientFraudTypeCh(
                data.connectedClientHelpers.getFirst().getUcid(), FraudTypeOld.HEDGING.getKey()));

        insertObjectToDb(CRM_USER_TABLE_NAME, data.connectedUsers.getFirst());

        addFraudsForClient(data.connectedClientHelpers.getFirst(), List.of(fraudType), status);
        return data;
    }

    public static DataHelper addAlert(DataHelper data, String ruleName, String status) {
        data.boAlertsObjects = List.of(generateAlert(data.clientHelper));
        data.boAlertsObjects.getFirst().setRule(ruleName);
        data.boAlertsObjects.getFirst().setStatus(status);
        return data;
    }

    public void addAlert(String ruleName, String status) {
        var data = this;
        List<BoAlertsObject> alerts = List.of(generateAlert(data.clientHelper));
        alerts.getFirst().setRule(ruleName);
        alerts.getFirst().setStatus(status);
        this.boAlertsObjects = alerts;
    }

    public DataHelper addWithdrawalSumByCategory(Double amount, Integer paymentType) {
        this.crmTbWithdrawalObjects = List.of(generateCrmTbWithdrawalEntityByClient(this.clientHelper));
        this.crmTbWithdrawalObjects.getFirst().setSourceIdSt(1);
        this.crmTbWithdrawalObjects.getFirst().setPaymentTypeId(paymentType);
        this.crmTbWithdrawalObjects.getFirst().setAmount(BigDecimal.valueOf(amount));
        this.crmTbWithdrawalObjects.getFirst().setAmountUsd(BigDecimal.valueOf(amount));
        this.crmTbWithdrawalObjects.getFirst().setStatusId(7);
        this.crmTbWithdrawalObjects.getFirst().setStatus("Complete");
        this.crmTbWithdrawalObjects.getFirst().setPaymentType(String.valueOf(paymentType));
        this.crmTbWithdrawalObjects.getFirst().setPaymentChannel("web");
        return this;
    }

    public DataHelper addDepositSumByCategory(Double amount) {
        this.crmTbDepositObjects = List.of(generateCrmTbDepositEntityByClient(this.clientHelper));
        this.crmTbDepositObjects.getFirst().setSourceIdSt(1);
        this.crmTbDepositObjects.getFirst().setBrandUid(0);
        this.crmTbDepositObjects.getFirst().setAmount(BigDecimal.valueOf(amount));
        this.crmTbDepositObjects.getFirst().setAmountUsd(BigDecimal.valueOf(amount));
        this.crmTbDepositObjects.getFirst().setStatusId(5);
        this.crmTbDepositObjects.getFirst().setStatus("Success");
        this.crmTbDepositObjects.getFirst().setPaymentTypeId(1);
        this.crmTbDepositObjects.getFirst().setPaymentType("Credit Card");
        this.crmTbDepositObjects.getFirst().setPaymentChannelId(1);
        this.crmTbDepositObjects.getFirst().setPaymentChannel("web");
        this.crmTbDepositObjects.getFirst().setPaymentSystemAccount("Credit card");
        return this;
    }

    public DataHelper addMultipleWithdrawalSumByCategory(
            Double amount, Integer paymentType, Integer withdrawalsNumber) {
        this.crmTbWithdrawalObjects = new ArrayList<>();
        for (int i = 0; i < withdrawalsNumber; i++) {
            this.crmTbWithdrawalObjects.add(generateCrmTbWithdrawalEntityByClient(this.clientHelper));
            this.crmTbWithdrawalObjects.get(i).setSourceIdSt(1);
            this.crmTbWithdrawalObjects.get(i).setPaymentTypeId(paymentType);
            this.crmTbWithdrawalObjects.get(i).setAmount(BigDecimal.valueOf(amount));
            this.crmTbWithdrawalObjects.get(i).setAmountUsd(BigDecimal.valueOf(amount));
            this.crmTbWithdrawalObjects.get(i).setStatusId(7);
            this.crmTbWithdrawalObjects.get(i).setStatus("Complete");
            this.crmTbWithdrawalObjects.get(i).setPaymentType(String.valueOf(paymentType));
            this.crmTbWithdrawalObjects.get(i).setPaymentChannel("web");
        }
        return this;
    }

    public DataHelper createDeposit() {
        this.crmTbDepositObjects = List.of(generateCrmTbDepositEntityByClient(this.getClientHelper()));
        return this;
    }

    public DataHelper createWithdrawal() {
        this.crmTbWithdrawalObjects = List.of(generateCrmTbWithdrawalEntityByClient(this.getClientHelper()));
        return this;
    }

    public DataHelper addWithdrawal() {
        this.getCrmTbWithdrawalObjects().add(generateCrmTbWithdrawalEntityByClient(this.getClientHelper()));
        return this;
    }

    public DataHelper addCreditCard() {
        if (this.getClientCards() == null) {
            this.setClientCards(new ArrayList<>());
        }

        this.getClientCards().add(generateClientCardsObject(this.getClientHelper()));

        return this;
    }
}
