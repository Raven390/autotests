package tests.vindex_backoffice_ui_tests.investigationTool.trading;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_credit_card_table.CrmTbCreditCardObject;
import business_objects.db.clickhouse.crm_tb_credit_card_table.CrmTbCreditCardObjectFactory;
import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntity;
import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntityFactory;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.crm_tb_withdraw_account.CrmTbWithdrawAccountObject;
import business_objects.db.clickhouse.crm_tb_withdraw_account.CrmTbWithdrawAccountObjectFactory;
import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalEntity;
import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalEntityFactory;
import business_objects.db.clickhouse.mt_account.MtAccountObject;
import business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObject;
import business_objects.db.clickhouse.mt_mt5_positions.MtMt5PositionsObject;
import business_objects.db.payment_gate.payment_decisions.PaymentDecisionsObject;
import business_objects.db.payment_gate.payment_details.PaymentDetailsObject;
import business_objects.db.payment_gate.payment_events.PaymentEventsObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import helpers.data.ClientHelper;
import helpers.database.DbName;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.AllureId;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.time.OffsetDateTime;
import java.util.List;

import static business_objects.db.payment_gate.payment_decisions.PaymentDecisionsObjectFactory.generatePaymentDecisionObject;
import static business_objects.db.payment_gate.payment_details.PaymentDetailsObjectFactory.generatePaymentDetailsObject;
import static business_objects.db.payment_gate.payment_events.PaymentEventsObjectFactory.generatePaymentEventsObject;
import static business_objects.kafka.alerts.RuleAlertFactory.*;
import static helpers.database.BoHelper.closeAlert;
import static org.hamcrest.MatcherAssert.assertThat;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_account_for_mt.crm_tb_account.CrmTbAccountForMtObjectFactory.generateAccountForMtByAccount;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObjectFactory.generateMt4TradesCoercedAccountProfitComment;
import static business_objects.db.clickhouse.mt_mt5_positions.MtMt5PositionsObjectFactory.generateMtMt5PositionsObject;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.Currency.USD;
import static helpers.database.DbHelper.*;
import static utils.Constants.*;
import static utils.Utils.getRandomIntPositive;

public class TransactionHistoryTest extends TestBaseWeb {
    private static final KafkaHelper kafka = new KafkaHelper();
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private static final ClientHelper client1 = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(client1);
    private static final CrmTbAccountObject account1 = generateCrmTbAccountDataForUi(client1);
    private static final CrmTbAccountObject account2 = generateCrmTbAccountDataForUi(client1);
    private static final DecimalFormat formatter = new DecimalFormat("#,##0.##");
    private static MtAccountObject mtAccount1;
    private static MtMt4TradesCoercedObject trade1;
    private static MtMt4TradesCoercedObject tradeWithdrawal;

    private static CrmTbDepositEntity deposit1;
    private static CrmTbDepositEntity deposit2;
    private static CrmTbDepositEntity deposit3;
    private static CrmTbWithdrawalEntity withdrawal1;
    private static CrmTbWithdrawalEntity withdrawal2;
    private static CrmTbWithdrawalEntity withdrawal3;
    private static CrmTbCreditCardObject cardObject;
    private static CrmTbWithdrawAccountObject crmTbWithdrawAccountObject;

    //PGS
    private static PaymentEventsObject paymentEventsObject1;
    private static PaymentDetailsObject paymentDetailsObject1;
    private static PaymentDecisionsObject paymentDecisionsObject1;
    private static PaymentEventsObject paymentEventsObject2;
    private static PaymentDetailsObject paymentDetailsObject2;
    private static PaymentDecisionsObject paymentDecisionsObject2;
    private static PaymentEventsObject paymentEventsObject3;
    private static PaymentDetailsObject paymentDetailsObject3;
    private static PaymentDecisionsObject paymentDecisionsObject3;

    @BeforeAll
    static void setup() throws Exception {
        account1.currency = USD.getCode();
        account2.account = client1.getTradingAccount2();
        mtAccount1 = generateMtAccountByCrmTbAccount(account1);
        var mtAccount2 = generateMtAccountByCrmTbAccount(account2);

        var comment = "comment";
        trade1 = generateMt4TradesCoercedAccountProfitComment(account1, 500.12 + 10_000d, comment);
        tradeWithdrawal = generateMt4TradesCoercedAccountProfitComment(account1, -10_000d, "withdraw");

        MtMt4TradesCoercedObject trade2 = generateMt4TradesCoercedAccountProfitComment(account2, 500.12 + 10_000d, comment);
        var tradeWithdrawal2 = generateMt4TradesCoercedAccountProfitComment(account2, -10_000d, "withdraw");

        insertObjectsToDb(CRM_USER_TABLE_NAME, List.of(crmTbUser));
        insertObjectsToDb(CRM_TB_ACCOUNT_TABLE_NAME, List.of(account1, account2));
        insertObjectsToDb(CRM_TB_ACCOUNT_FOR_MT_TABLE_NAME, List.of(generateAccountForMtByAccount(account1), generateAccountForMtByAccount(account2)));
        insertObjectsToDb(MT_ACCOUNT_TABLE_NAME, List.of(mtAccount1, mtAccount2));
        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade1, tradeWithdrawal, trade2, tradeWithdrawal2));
        MtMt5PositionsObject position1 = generateMtMt5PositionsObject(client1);
        insertObjectsToDb(MT5_POSITIONS_TABLE_NAME, List.of(position1));
        executeQueryToDb(DbName.CLICKHOUSE, String.format("UPDATE %s SET is_deleted = 1 WHERE account = %s", MT5_POSITIONS_TABLE_NAME, mtAccount2.account));

        crmTbWithdrawAccountObject = CrmTbWithdrawAccountObjectFactory.generateByClient(client1);
        cardObject = CrmTbCreditCardObjectFactory.generateByClient(client1, getRandomIntPositive());
        cardObject.cardBeginSixDigits = "454793";
        insertObjectsToDb(CRM_TB_WITHDRAW_ACCOUNT_TABLE_NAME, List.of(crmTbWithdrawAccountObject));
        insertObjectToDb(CRM_TB_CREDIT_CARD_TABLE_NAME, cardObject);

        var prevYear = OffsetDateTime.now().minusYears(1);
        //tb
        deposit1 = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client1);
        deposit1.setCreateTime(prevYear);
        deposit1.setCreateTimeUtc(prevYear);
        deposit1.setCreditCardId((long) cardObject.id);
        deposit1.setFirstSixDigits(cardObject.cardBeginSixDigits);
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, deposit1);

        deposit2 = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client1);
        deposit2.setCreateTime(prevYear);
        deposit2.setCreateTimeUtc(prevYear);
        deposit2.setCreditCardId((long) cardObject.id);
        deposit2.setFirstSixDigits(cardObject.cardBeginSixDigits);
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, deposit2);

        deposit3 = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client1);
        deposit3.setAccount(BigInteger.valueOf(client1.getTradingAccount2()));
        deposit3.setCreditCardId((long) cardObject.id);
        deposit3.setFirstSixDigits(cardObject.cardBeginSixDigits);
        deposit3.setPaymentFamily("paymentFamily_X");
        deposit3.setPaymentProfile("paymentProfile_X");
        deposit3.setPaymentProfileKey("paymentProfile_X");
        deposit3.setPaymentProfileMasked("paymen****file_X");
        deposit3.setStatusGroup("Fail");
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, deposit3);

        withdrawal1 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client1);
        withdrawal1.setCreateTime(prevYear);
        withdrawal1.setCreateTimeUtc(prevYear);
        withdrawal1.setCreditCardId((long) cardObject.id);
        withdrawal1.setStatus("Risk Audit");
        insertObjectToDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, withdrawal1);
        withdrawal2 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client1);
        withdrawal2.setStatus("Partial Success");
        withdrawal2.setStatusGroup("Partial Success");
        withdrawal2.setReversedAmount(BigDecimal.valueOf(10d));
        withdrawal2.setReversedAmountUsd(BigDecimal.valueOf(10d));
        withdrawal2.setCreateTime(prevYear);
        withdrawal2.setCreateTimeUtc(prevYear);
        withdrawal2.setCreditCardId((long) cardObject.id);
        insertObjectToDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, withdrawal2);

        withdrawal3 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client1);
        withdrawal3.setStatus("Risk Audit");
        withdrawal3.setAccount(BigInteger.valueOf(client1.getTradingAccount2()));
        withdrawal3.setCreditCardId((long) cardObject.id);
        withdrawal3.setPaymentFamily("paymentFamily_X");
        withdrawal3.setPaymentProfile("paymentProfile_X");
        withdrawal3.setPaymentProfileKey("paymentProfile_X");
        withdrawal3.setPaymentProfileMasked("paymen****file_X");
        withdrawal3.setStatusGroup("Fail");
        insertObjectToDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, withdrawal3);

        //PGS
        paymentEventsObject1 = generatePaymentEventsObject(client1);
        paymentDetailsObject1 = generatePaymentDetailsObject(paymentEventsObject1, client1);
        paymentDetailsObject1.setMerchantOrderId(withdrawal1.getOrderNumber());
        paymentDetailsObject1.setPayload(String.format("""
                {"merchantOrderId": "%s"}
                """, withdrawal1.getOrderNumber()));
        paymentDecisionsObject1 = generatePaymentDecisionObject(paymentEventsObject1);
        paymentDecisionsObject1.setRejectionCode(null);
        paymentDecisionsObject1.setDecisionType("final");

        paymentEventsObject2 = generatePaymentEventsObject(client1);
        paymentDetailsObject2 = generatePaymentDetailsObject(paymentEventsObject2, client1);
        paymentDetailsObject2.setMerchantOrderId(withdrawal2.getOrderNumber());
        paymentDetailsObject2.setPayload(String.format("""
                {"merchantOrderId": "%s"}
                """, withdrawal2.getOrderNumber()));
        paymentDecisionsObject2 = generatePaymentDecisionObject(paymentEventsObject2);
        paymentDecisionsObject2.setRejectionCode(null);
        paymentDecisionsObject2.setDecisionCode(2);
        paymentDecisionsObject2.setDecisionType("final");

        paymentEventsObject3 = generatePaymentEventsObject(client1);
        paymentDetailsObject3 = generatePaymentDetailsObject(paymentEventsObject3, client1);
        paymentDetailsObject3.setMerchantOrderId(withdrawal3.getOrderNumber());
        paymentDetailsObject3.setPayload(String.format("""
                {"merchantOrderId": "%s"}
                """, withdrawal3.getOrderNumber()));
        paymentDecisionsObject3 = generatePaymentDecisionObject(paymentEventsObject3);
        paymentDecisionsObject3.setRejectionCode(null);
        paymentDecisionsObject3.setDecisionCode(0);
        paymentDecisionsObject3.setDecisionType("risk");

        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE, List.of(paymentEventsObject1, paymentEventsObject2, paymentEventsObject3));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE, List.of(paymentDetailsObject1, paymentDetailsObject2, paymentDetailsObject3));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DECISIONS_TABLE, List.of(paymentDecisionsObject1, paymentDecisionsObject2, paymentDecisionsObject3));

        //alerts
        var alertPayment1 = generatePaymentAlertByUcidByTrigger(crmTbUser.ucid, "Withdrawal");
        var alertPayment2 = generatePaymentAlertByUcidByTrigger(crmTbUser.ucid, "Withdrawal");
        alertPayment1.setPaymentEventId(paymentEventsObject1.getPaymentId().toString());
        alertPayment1.setMerchantOrderId(withdrawal1.getOrderNumber());
        alertPayment2.setPaymentEventId(paymentEventsObject3.getPaymentId().toString());
        alertPayment2.setMerchantOrderId(withdrawal3.getOrderNumber());
        kafka.produceMessage(alertPayment1.getId().toString(), objectMapper.writeValueAsString(alertPayment1), KAFKA_TOPIC_ALERTS);
        kafka.produceMessage(alertPayment2.getId().toString(), objectMapper.writeValueAsString(alertPayment2), KAFKA_TOPIC_ALERTS);
    }

    @AfterAll
    static void teardown() throws Exception {
        deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", client1.getUcid()));
        deleteEntryFromDb(MT4_TRADES_COERCED_TABLE_NAME, String.format("ucid = '%s'", client1.getUcid()));
        deleteEntryFromDb(MT5_POSITIONS_TABLE_NAME, String.format("ucid = '%s'", client1.getUcid()));

        deleteEntryFromDb(CRM_DEPOSIT_TABLE_NAME, String.format("ucid = '%s' AND transfer_id='%d'", client1.getUcid(), deposit1.getTransferId()));
        deleteEntryFromDb(CRM_DEPOSIT_TABLE_NAME, String.format("ucid = '%s' AND transfer_id='%d'", client1.getUcid(), deposit2.getTransferId()));
        deleteEntryFromDb(CRM_DEPOSIT_TABLE_NAME, String.format("ucid = '%s' AND transfer_id='%d'", client1.getUcid(), deposit3.getTransferId()));
        deleteEntryFromDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, String.format("ucid = '%s' AND transfer_id='%d'", client1.getUcid(), withdrawal1.getTransferId()));
        deleteEntryFromDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, String.format("ucid = '%s' AND transfer_id='%d'", client1.getUcid(), withdrawal2.getTransferId()));
        deleteEntryFromDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, String.format("ucid = '%s' AND transfer_id='%d'", client1.getUcid(), withdrawal3.getTransferId()));

        deleteEntryFromDb(CRM_TB_WITHDRAW_ACCOUNT_TABLE_NAME, String.format("source_id_st = %d AND id = %d", crmTbWithdrawAccountObject.sourceIdSt, crmTbWithdrawAccountObject.id));
        deleteEntryFromDb(CRM_TB_CREDIT_CARD_TABLE_NAME, String.format("source_id_st = %d AND user_id = %d AND id = %d", cardObject.sourceIdSt, cardObject.userId, cardObject.id));


        deleteEntryFromDb(DbName.POSTGRES, PAYMENT_GATEWAY_TMP_RULE_DECISIONS_TABLE, String.format("payment_id='%s'", paymentEventsObject1.getPaymentId().toString()));
        deleteEntryFromDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_REJECTION_ATTRIBUTES_TABLE, String.format("payment_id='%s'", paymentEventsObject1.getPaymentId().toString()));
        deleteEntryFromDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DECISIONS_TABLE, String.format("payment_id='%s'", paymentEventsObject1.getPaymentId().toString()));

        deleteEntryFromDb(DbName.POSTGRES, PAYMENT_GATEWAY_TMP_RULE_DECISIONS_TABLE, String.format("payment_id='%s'", paymentEventsObject2.getPaymentId().toString()));
        deleteEntryFromDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_REJECTION_ATTRIBUTES_TABLE, String.format("payment_id='%s'", paymentEventsObject2.getPaymentId().toString()));
        deleteEntryFromDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DECISIONS_TABLE, String.format("payment_id='%s'", paymentEventsObject2.getPaymentId().toString()));


        deleteEntryFromDb(DbName.POSTGRES, PAYMENT_GATEWAY_TMP_RULE_DECISIONS_TABLE, String.format("payment_id='%s'", paymentEventsObject3.getPaymentId().toString()));
        deleteEntryFromDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_REJECTION_ATTRIBUTES_TABLE, String.format("payment_id='%s'", paymentEventsObject3.getPaymentId().toString()));
        deleteEntryFromDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DECISIONS_TABLE, String.format("payment_id='%s'", paymentEventsObject3.getPaymentId().toString()));
        deleteEntryFromDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE, String.format("client_id = '%s'", client1.getUserId()));
        deleteEntryFromDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE, String.format("ucid = '%s'", client1.getUcid()));

        closeAlert(crmTbUser.ucid);
    }

    @Test
    @AllureId("1852")
    @DisplayName("Transaction history - All transactions without filters")
    void all_transactions_no_filter_test() {
        openTransactionPage();

        var depositRow1 = paymentsPage.getTransactionByOrderNumber(deposit1.getOrderNumber());
        var depositRow2 = paymentsPage.getTransactionByOrderNumber(deposit2.getOrderNumber());
        var depositRow3 = paymentsPage.getTransactionByOrderNumber(deposit3.getOrderNumber());

        var withdrawalRow1 = paymentsPage.getTransactionByOrderNumber(withdrawal1.getOrderNumber());
        var withdrawalRow2 = paymentsPage.getTransactionByOrderNumber(withdrawal2.getOrderNumber());
        var withdrawalRow3 = paymentsPage.getTransactionByOrderNumber(withdrawal3.getOrderNumber());

        assertThat("Deposit 1 type should be DepositSuccess", depositRow1.type(), org.hamcrest.Matchers.equalTo("DepositSuccess"));
        assertThat("Deposit 1 account should match", depositRow1.account(), org.hamcrest.Matchers.equalTo(String.valueOf(account1.account)));
        assertThat("Deposit 1 submitted amount should match", depositRow1.submitted(), org.hamcrest.Matchers.containsString(String.format("%s %s", formatter.format(deposit1.getAmount()), deposit1.getCurrency())));
        assertThat("Deposit 1 processed amount should match", depositRow1.processed(), org.hamcrest.Matchers.is(Matchers.emptyString()));

        assertThat("Deposit 2 type should be DepositSuccess", depositRow2.type(), org.hamcrest.Matchers.equalTo("DepositSuccess"));
        assertThat("Deposit 2 account should match", depositRow2.account(), org.hamcrest.Matchers.equalTo(String.valueOf(account1.account)));
        assertThat("Deposit 2 submitted amount should match", depositRow2.submitted(), org.hamcrest.Matchers.containsString(String.format("%s %s", formatter.format(deposit2.getAmount()), deposit2.getCurrency())));
        assertThat("Deposit 2 processed amount should match", depositRow2.processed(), org.hamcrest.Matchers.is(Matchers.emptyString()));

        assertThat("Deposit 3 type should be DepositFail", depositRow3.type(), org.hamcrest.Matchers.equalTo("DepositFail"));
        assertThat("Deposit 3 account should match", depositRow3.account(), org.hamcrest.Matchers.equalTo(String.valueOf(account2.account)));
        assertThat("Deposit 3 submitted amount should match", depositRow3.submitted(), org.hamcrest.Matchers.containsString(String.format("%s %s", formatter.format(deposit3.getAmount()), deposit3.getCurrency())));
        assertThat("Deposit 3 processed amount should match", depositRow3.processed(), org.hamcrest.Matchers.is(Matchers.emptyString()));

        assertThat("Withdrawal 1 type should be WithdrawalSuccess", withdrawalRow1.type(), org.hamcrest.Matchers.equalTo("WithdrawalSuccess"));
        assertThat("Withdrawal 1 account should match", withdrawalRow1.account(), org.hamcrest.Matchers.equalTo(String.valueOf(account1.account)));
        assertThat("Withdrawal 1 submitted amount should match", withdrawalRow1.submitted(), org.hamcrest.Matchers.containsString(String.format("-%s %s", formatter.format(withdrawal1.getAmount()), withdrawal1.getCurrency())));
        assertThat("Withdrawal 1 risk audit should be approved", withdrawalRow1.riskAudit(), org.hamcrest.Matchers.equalTo("Approved manually"));
        assertThat("Withdrawal 1 rejection reason should be Internal reason", withdrawalRow1.rejectionReason(), org.hamcrest.Matchers.is(Matchers.emptyString()));

        assertThat("Withdrawal 2 type should be WithdrawalSuccess", withdrawalRow2.type(), org.hamcrest.Matchers.equalTo("WithdrawalPartial success"));
        assertThat("Withdrawal 2 account should match", withdrawalRow2.account(), org.hamcrest.Matchers.equalTo(String.valueOf(account1.account)));
        assertThat("Withdrawal 2 submitted amount should match", withdrawalRow2.submitted(), org.hamcrest.Matchers.containsString(String.format("-%s %s", formatter.format(withdrawal2.getAmount()), withdrawal2.getCurrency())));
        assertThat("Withdrawal 2 risk audit should be rejected", withdrawalRow2.riskAudit(), org.hamcrest.Matchers.equalTo("Rejected manually"));
        assertThat("Withdrawal 2 processed should be amount - reversed", withdrawalRow2.processed(), org.hamcrest.Matchers.containsString(String.format("-%s %s", formatter.format(withdrawal2.getAmount().subtract(withdrawal2.getReversedAmount())), withdrawal2.getCurrency())));
        assertThat("Withdrawal 2 rejection reason should be Internal reason", withdrawalRow2.rejectionReason(), org.hamcrest.Matchers.is(Matchers.emptyString()));

        assertThat("Withdrawal 3 type should be WithdrawalFail", withdrawalRow3.type(), org.hamcrest.Matchers.equalTo("WithdrawalFail"));
        assertThat("Withdrawal 3 account should match", withdrawalRow3.account(), org.hamcrest.Matchers.equalTo(String.valueOf(account2.account)));
        assertThat("Withdrawal 3 submitted amount should match", withdrawalRow3.submitted(), org.hamcrest.Matchers.containsString(String.format("-%s %s", formatter.format(withdrawal3.getAmount()), withdrawal3.getCurrency())));
        assertThat("Withdrawal 3 risk audit shoud be fraud_detection", withdrawalRow3.riskAudit(), org.hamcrest.Matchers.equalTo("fraud_detection"));
    }

    @Test
    @AllureId("1853")
    @DisplayName("Transaction history - Filter transactions by type")
    void type_filter_transactions_test() {
        openTransactionPage();
        paymentsPage.clickTransactionsFilterButton();

        //Deposits
        paymentsPage.filterTransactionsByType("Deposit");
        var allTransactions = paymentsPage.getAllTransactions();
        Assertions.assertEquals(3, allTransactions.size());

        var depositRow1 = paymentsPage.getTransactionByOrderNumber(deposit1.getOrderNumber());
        var depositRow2 = paymentsPage.getTransactionByOrderNumber(deposit2.getOrderNumber());
        var depositRow3 = paymentsPage.getTransactionByOrderNumber(deposit3.getOrderNumber());

        assertThat("Deposit 1 type should be DepositSuccess", depositRow1.type(), org.hamcrest.Matchers.equalTo("DepositSuccess"));
        assertThat("Deposit 1 account should match", depositRow1.account(), org.hamcrest.Matchers.equalTo(String.valueOf(account1.account)));
        assertThat("Deposit 1 submitted amount should match", depositRow1.submitted(), org.hamcrest.Matchers.containsString(String.format("%s %s", formatter.format(deposit1.getAmount()), deposit1.getCurrency())));
        assertThat("Deposit 1 processed amount should match", depositRow1.processed(), org.hamcrest.Matchers.is(Matchers.emptyString()));

        assertThat("Deposit 2 type should be DepositSuccess", depositRow2.type(), org.hamcrest.Matchers.equalTo("DepositSuccess"));
        assertThat("Deposit 2 account should match", depositRow2.account(), org.hamcrest.Matchers.equalTo(String.valueOf(account1.account)));
        assertThat("Deposit 2 submitted amount should match", depositRow2.submitted(), org.hamcrest.Matchers.containsString(String.format("%s %s", formatter.format(deposit2.getAmount()), deposit2.getCurrency())));
        assertThat("Deposit 2 processed amount should match", depositRow2.processed(), org.hamcrest.Matchers.is(Matchers.emptyString()));

        assertThat("Deposit 3 type should be DepositFail", depositRow3.type(), org.hamcrest.Matchers.equalTo("DepositFail"));
        assertThat("Deposit 3 account should match", depositRow3.account(), org.hamcrest.Matchers.equalTo(String.valueOf(account2.account)));
        assertThat("Deposit 3 submitted amount should match", depositRow3.submitted(), org.hamcrest.Matchers.containsString(String.format("%s %s", formatter.format(deposit3.getAmount()), deposit3.getCurrency())));
        assertThat("Deposit 3 processed amount should match", depositRow3.processed(), org.hamcrest.Matchers.is(Matchers.emptyString()));

        paymentsPage.clickTransactionsFilterButton();
        paymentsPage.filterTransactionsByType("Deposit");
        paymentsPage.clickTransactionsFilterButton();
        paymentsPage.filterTransactionsByType("Withdrawal");
        allTransactions = paymentsPage.getAllTransactions();
        Assertions.assertEquals(3, allTransactions.size());

        var withdrawalRow1 = paymentsPage.getTransactionByOrderNumber(withdrawal1.getOrderNumber());
        var withdrawalRow2 = paymentsPage.getTransactionByOrderNumber(withdrawal2.getOrderNumber());
        var withdrawalRow3 = paymentsPage.getTransactionByOrderNumber(withdrawal3.getOrderNumber());

        assertThat("Withdrawal 1 type should be WithdrawalSuccess", withdrawalRow1.type(), org.hamcrest.Matchers.equalTo("WithdrawalSuccess"));
        assertThat("Withdrawal 1 account should match", withdrawalRow1.account(), org.hamcrest.Matchers.equalTo(String.valueOf(account1.account)));
        assertThat("Withdrawal 1 submitted amount should match", withdrawalRow1.submitted(), org.hamcrest.Matchers.containsString(String.format("-%s %s", formatter.format(withdrawal1.getAmount()), withdrawal1.getCurrency())));
        assertThat("Withdrawal 1 risk audit should be approved", withdrawalRow1.riskAudit(), org.hamcrest.Matchers.equalTo("Approved manually"));
        assertThat("Withdrawal 1 rejection reason should be Internal reason", withdrawalRow1.rejectionReason(), org.hamcrest.Matchers.is(Matchers.emptyString()));

        assertThat("Withdrawal 2 type should be WithdrawalSuccess", withdrawalRow2.type(), org.hamcrest.Matchers.equalTo("WithdrawalPartial success"));
        assertThat("Withdrawal 2 account should match", withdrawalRow2.account(), org.hamcrest.Matchers.equalTo(String.valueOf(account1.account)));
        assertThat("Withdrawal 2 submitted amount should match", withdrawalRow2.submitted(), org.hamcrest.Matchers.containsString(String.format("-%s %s", formatter.format(withdrawal2.getAmount()), withdrawal2.getCurrency())));
        assertThat("Withdrawal 2 risk audit should be rejected", withdrawalRow2.riskAudit(), org.hamcrest.Matchers.equalTo("Rejected manually"));
        assertThat("Withdrawal 2 processed should be amount - reversed", withdrawalRow2.processed(), org.hamcrest.Matchers.containsString(String.format("-%s %s", formatter.format(withdrawal2.getAmount().subtract(withdrawal2.getReversedAmount())), withdrawal2.getCurrency())));
        assertThat("Withdrawal 2 rejection reason should be Internal reason", withdrawalRow2.rejectionReason(), org.hamcrest.Matchers.is(Matchers.emptyString()));

        assertThat("Withdrawal 3 type should be WithdrawalFail", withdrawalRow3.type(), org.hamcrest.Matchers.equalTo("WithdrawalFail"));
        assertThat("Withdrawal 3 account should match", withdrawalRow3.account(), org.hamcrest.Matchers.equalTo(String.valueOf(account2.account)));
        assertThat("Withdrawal 3 submitted amount should match", withdrawalRow3.submitted(), org.hamcrest.Matchers.containsString(String.format("-%s %s", formatter.format(withdrawal3.getAmount()), withdrawal3.getCurrency())));
        assertThat("Withdrawal 3 risk audit shoud be fraud_detection", withdrawalRow3.riskAudit(), org.hamcrest.Matchers.equalTo("fraud_detection"));
    }


    @Test
    @AllureId("1854")
    @DisplayName("Transaction history - Filter transactions by order number")
    void order_number_filter_transactions_test() {
        openTransactionPage();
        paymentsPage.clickTransactionsFilterButton();
        paymentsPage.filterTransactionsByOrderNumber(deposit1.getOrderNumber());
        var allTransactions = paymentsPage.getAllTransactions();
        Assertions.assertEquals(1, allTransactions.size());

        var depositRow1 = paymentsPage.getTransactionByOrderNumber(deposit1.getOrderNumber());
        assertThat("Deposit 1 type should be DepositSuccess", depositRow1.type(), org.hamcrest.Matchers.equalTo("DepositSuccess"));
        assertThat("Deposit 1 account should match", depositRow1.account(), org.hamcrest.Matchers.equalTo(String.valueOf(account1.account)));
        assertThat("Deposit 1 submitted amount should match", depositRow1.submitted(), org.hamcrest.Matchers.containsString(String.format("%s %s", formatter.format(deposit1.getAmount()), deposit1.getCurrency())));
        assertThat("Deposit 1 processed amount should match", depositRow1.processed(), org.hamcrest.Matchers.is(Matchers.emptyString()));
    }

    @Test
    @AllureId("1855")
    @DisplayName("Transaction history - Filter transactions by account")
    void account_filter_transactions_test() {
        openTransactionPage();

        paymentsPage.clickTransactionsFilterButton();
        paymentsPage.filterTransactionsByAccount(String.valueOf(account1.account));
        var allTransactions = paymentsPage.getAllTransactions();
        assertThat(allTransactions.size(), org.hamcrest.Matchers.equalTo(4));

        var depositRow1 = paymentsPage.getTransactionByOrderNumber(deposit1.getOrderNumber());
        var depositRow2 = paymentsPage.getTransactionByOrderNumber(deposit2.getOrderNumber());
        var withdrawalRow1 = paymentsPage.getTransactionByOrderNumber(withdrawal1.getOrderNumber());
        var withdrawalRow2 = paymentsPage.getTransactionByOrderNumber(withdrawal2.getOrderNumber());

        assertThat("Deposit 1 type should be DepositSuccess", depositRow1.type(), org.hamcrest.Matchers.equalTo("DepositSuccess"));
        assertThat("Deposit 1 account should match", depositRow1.account(), org.hamcrest.Matchers.equalTo(String.valueOf(account1.account)));
        assertThat("Deposit 1 submitted amount should match", depositRow1.submitted(), org.hamcrest.Matchers.containsString(String.format("%s %s", formatter.format(deposit1.getAmount()), deposit1.getCurrency())));
        assertThat("Deposit 1 processed amount should match", depositRow1.processed(), org.hamcrest.Matchers.is(Matchers.emptyString()));

        assertThat("Deposit 2 type should be DepositSuccess", depositRow2.type(), org.hamcrest.Matchers.equalTo("DepositSuccess"));
        assertThat("Deposit 2 account should match", depositRow2.account(), org.hamcrest.Matchers.equalTo(String.valueOf(account1.account)));
        assertThat("Deposit 2 submitted amount should match", depositRow2.submitted(), org.hamcrest.Matchers.containsString(String.format("%s %s", formatter.format(deposit2.getAmount()), deposit2.getCurrency())));
        assertThat("Deposit 2 processed amount should match", depositRow2.processed(), org.hamcrest.Matchers.is(Matchers.emptyString()));

        assertThat("Withdrawal 1 type should be WithdrawalSuccess", withdrawalRow1.type(), org.hamcrest.Matchers.equalTo("WithdrawalSuccess"));
        assertThat("Withdrawal 1 account should match", withdrawalRow1.account(), org.hamcrest.Matchers.equalTo(String.valueOf(account1.account)));
        assertThat("Withdrawal 1 submitted amount should match", withdrawalRow1.submitted(), org.hamcrest.Matchers.containsString(String.format("-%s %s", formatter.format(withdrawal1.getAmount()), withdrawal1.getCurrency())));
        assertThat("Withdrawal 1 risk audit should be approved", withdrawalRow1.riskAudit(), org.hamcrest.Matchers.equalTo("Approved manually"));
        assertThat("Withdrawal 1 rejection reason should be Internal reason", withdrawalRow1.rejectionReason(), org.hamcrest.Matchers.is(Matchers.emptyString()));

        assertThat("Withdrawal 2 type should be WithdrawalSuccess", withdrawalRow2.type(), org.hamcrest.Matchers.equalTo("WithdrawalPartial success"));
        assertThat("Withdrawal 2 account should match", withdrawalRow2.account(), org.hamcrest.Matchers.equalTo(String.valueOf(account1.account)));
        assertThat("Withdrawal 2 submitted amount should match", withdrawalRow2.submitted(), org.hamcrest.Matchers.containsString(String.format("-%s %s", formatter.format(withdrawal2.getAmount()), withdrawal2.getCurrency())));
        assertThat("Withdrawal 2 risk audit should be rejected", withdrawalRow2.riskAudit(), org.hamcrest.Matchers.equalTo("Rejected manually"));
        assertThat("Withdrawal 2 processed should be amount - reversed", withdrawalRow2.processed(), org.hamcrest.Matchers.containsString(String.format("-%s %s", formatter.format(withdrawal2.getAmount().subtract(withdrawal2.getReversedAmount())), withdrawal2.getCurrency())));
        assertThat("Withdrawal 2 rejection reason should be Internal reason", withdrawalRow2.rejectionReason(), org.hamcrest.Matchers.is(Matchers.emptyString()));

        paymentsPage.clickTransactionsFilterButton();
        paymentsPage.uncheckAccountFilter(String.valueOf(account1.account));
        paymentsPage.clickTransactionsFilterButton();
        paymentsPage.filterTransactionsByAccount(String.valueOf(account2.account));

        allTransactions = paymentsPage.getAllTransactions();
        assertThat(allTransactions.size(), org.hamcrest.Matchers.equalTo(2));

        var depositRow3 = paymentsPage.getTransactionByOrderNumber(deposit3.getOrderNumber());
        var withdrawalRow3 = paymentsPage.getTransactionByOrderNumber(withdrawal3.getOrderNumber());

        assertThat("Deposit 3 type should be DepositFail", depositRow3.type(), org.hamcrest.Matchers.equalTo("DepositFail"));
        assertThat("Deposit 3 account should match", depositRow3.account(), org.hamcrest.Matchers.equalTo(String.valueOf(account2.account)));
        assertThat("Deposit 3 submitted amount should match", depositRow3.submitted(), org.hamcrest.Matchers.containsString(String.format("%s %s", formatter.format(deposit3.getAmount()), deposit3.getCurrency())));
        assertThat("Deposit 3 processed amount should match", depositRow3.processed(), org.hamcrest.Matchers.is(Matchers.emptyString()));
        assertThat("Withdrawal 3 type should be WithdrawalFail", withdrawalRow3.type(), org.hamcrest.Matchers.equalTo("WithdrawalFail"));
        assertThat("Withdrawal 3 account should match", withdrawalRow3.account(), org.hamcrest.Matchers.equalTo(String.valueOf(account2.account)));
        assertThat("Withdrawal 3 submitted amount should match", withdrawalRow3.submitted(), org.hamcrest.Matchers.containsString(String.format("-%s %s", formatter.format(withdrawal3.getAmount()), withdrawal3.getCurrency())));
        assertThat("Withdrawal 3 risk audit shoud be fraud_detection", withdrawalRow3.riskAudit(), org.hamcrest.Matchers.equalTo("fraud_detection"));
    }

    @Test
    @AllureId("1856")
    @DisplayName("Transaction history - Filter transactions by created date")
    void created_filter_transactions_test() {
        openTransactionPage();

        paymentsPage.clickTransactionsFilterButton();
        paymentsPage.filterTransactionsByToday();

        var allTransactions = paymentsPage.getAllTransactions();
        assertThat(allTransactions.size(), org.hamcrest.Matchers.equalTo(2));

        var depositRow3 = paymentsPage.getTransactionByOrderNumber(deposit3.getOrderNumber());
        var withdrawalRow3 = paymentsPage.getTransactionByOrderNumber(withdrawal3.getOrderNumber());

        assertThat("Deposit 3 type should be DepositFail", depositRow3.type(), org.hamcrest.Matchers.equalTo("DepositFail"));
        assertThat("Deposit 3 account should match", depositRow3.account(), org.hamcrest.Matchers.equalTo(String.valueOf(account2.account)));
        assertThat("Deposit 3 submitted amount should match", depositRow3.submitted(), org.hamcrest.Matchers.containsString(String.format("%s %s", formatter.format(deposit3.getAmount()), deposit3.getCurrency())));
        assertThat("Deposit 3 processed amount should match", depositRow3.processed(), org.hamcrest.Matchers.is(Matchers.emptyString()));

        assertThat("Withdrawal 3 type should be WithdrawalFail", withdrawalRow3.type(), org.hamcrest.Matchers.equalTo("WithdrawalFail"));
        assertThat("Withdrawal 3 account should match", withdrawalRow3.account(), org.hamcrest.Matchers.equalTo(String.valueOf(account2.account)));
        assertThat("Withdrawal 3 submitted amount should match", withdrawalRow3.submitted(), org.hamcrest.Matchers.containsString(String.format("-%s %s", formatter.format(withdrawal3.getAmount()), withdrawal3.getCurrency())));
        assertThat("Withdrawal 3 risk audit shoud be fraud_detection", withdrawalRow3.riskAudit(), org.hamcrest.Matchers.equalTo("fraud_detection"));
    }

    @Test
    @AllureId("1857")
    @DisplayName("Transaction history - Filter transactions by payment method")
    void method_filter_transactions_payment_test() {
        openTransactionPage();

        paymentsPage.clickTransactionsFilterButton();
        paymentsPage.filterTransactionsByPaymentProfile("paymentProfile_X");

        var allTransactions = paymentsPage.getAllTransactions();
        assertThat(allTransactions.size(), org.hamcrest.Matchers.equalTo(2));

        var depositRow3 = paymentsPage.getTransactionByOrderNumber(deposit3.getOrderNumber());
        var withdrawalRow3 = paymentsPage.getTransactionByOrderNumber(withdrawal3.getOrderNumber());

        assertThat("Deposit 3 type should be DepositFail", depositRow3.type(), org.hamcrest.Matchers.equalTo("DepositFail"));
        assertThat("Deposit 3 account should match", depositRow3.account(), org.hamcrest.Matchers.equalTo(String.valueOf(account2.account)));
        assertThat("Deposit 3 submitted amount should match", depositRow3.submitted(), org.hamcrest.Matchers.containsString(String.format("%s %s", formatter.format(deposit3.getAmount()), deposit3.getCurrency())));
        assertThat("Deposit 3 processed amount should match", depositRow3.processed(), org.hamcrest.Matchers.is(Matchers.emptyString()));

        assertThat("Withdrawal 3 type should be WithdrawalFail", withdrawalRow3.type(), org.hamcrest.Matchers.equalTo("WithdrawalFail"));
        assertThat("Withdrawal 3 account should match", withdrawalRow3.account(), org.hamcrest.Matchers.equalTo(String.valueOf(account2.account)));
        assertThat("Withdrawal 3 submitted amount should match", withdrawalRow3.submitted(), org.hamcrest.Matchers.containsString(String.format("-%s %s", formatter.format(withdrawal3.getAmount()), withdrawal3.getCurrency())));
        assertThat("Withdrawal 3 risk audit shoud be fraud_detection", withdrawalRow3.riskAudit(), org.hamcrest.Matchers.equalTo("fraud_detection"));
    }

    @Test
    @AllureId("1858")
    @DisplayName("Transaction history - Filter transactions by status")
    void status_filter_transactions_payment_test() {
        openTransactionPage();

        paymentsPage.clickTransactionsFilterButton();
        paymentsPage.filterTransactionsByStatus("Fail");

        var allTransactions = paymentsPage.getAllTransactions();
        assertThat(allTransactions.size(), org.hamcrest.Matchers.equalTo(2));

        var depositRow3 = paymentsPage.getTransactionByOrderNumber(deposit3.getOrderNumber());
        var withdrawalRow3 = paymentsPage.getTransactionByOrderNumber(withdrawal3.getOrderNumber());

        assertThat("Deposit 3 type should be DepositFail", depositRow3.type(), org.hamcrest.Matchers.equalTo("DepositFail"));
        assertThat("Deposit 3 account should match", depositRow3.account(), org.hamcrest.Matchers.equalTo(String.valueOf(account2.account)));
        assertThat("Deposit 3 submitted amount should match", depositRow3.submitted(), org.hamcrest.Matchers.containsString(String.format("%s %s", formatter.format(deposit3.getAmount()), deposit3.getCurrency())));
        assertThat("Deposit 3 processed amount should match", depositRow3.processed(), org.hamcrest.Matchers.is(Matchers.emptyString()));

        assertThat("Withdrawal 3 type should be WithdrawalFail", withdrawalRow3.type(), org.hamcrest.Matchers.equalTo("WithdrawalFail"));
        assertThat("Withdrawal 3 account should match", withdrawalRow3.account(), org.hamcrest.Matchers.equalTo(String.valueOf(account2.account)));
        assertThat("Withdrawal 3 submitted amount should match", withdrawalRow3.submitted(), org.hamcrest.Matchers.containsString(String.format("-%s %s", formatter.format(withdrawal3.getAmount()), withdrawal3.getCurrency())));
        assertThat("Withdrawal 3 risk audit shoud be fraud_detection", withdrawalRow3.riskAudit(), org.hamcrest.Matchers.equalTo("fraud_detection"));

        paymentsPage.clickTransactionsFilterButton();
        paymentsPage.uncheckStatusFilter("Fail");
        paymentsPage.clickTransactionsFilterButton();
        paymentsPage.filterTransactionsByStatus("Success");
        allTransactions = paymentsPage.getAllTransactions();
        assertThat(allTransactions.size(), org.hamcrest.Matchers.equalTo(3));

        var depositRow1 = paymentsPage.getTransactionByOrderNumber(deposit1.getOrderNumber());
        var depositRow2 = paymentsPage.getTransactionByOrderNumber(deposit2.getOrderNumber());
        var withdrawalRow1 = paymentsPage.getTransactionByOrderNumber(withdrawal1.getOrderNumber());
        assertThat("Deposit 1 type should be DepositSuccess", depositRow1.type(), org.hamcrest.Matchers.equalTo("DepositSuccess"));
        assertThat("Deposit 1 account should match", depositRow1.account(), org.hamcrest.Matchers.equalTo(String.valueOf(account1.account)));
        assertThat("Deposit 1 submitted amount should match", depositRow1.submitted(), org.hamcrest.Matchers.containsString(String.format("%s %s", formatter.format(deposit1.getAmount()), deposit1.getCurrency())));
        assertThat("Deposit 1 processed amount should match", depositRow1.processed(), org.hamcrest.Matchers.is(Matchers.emptyString()));

        assertThat("Deposit 2 type should be DepositSuccess", depositRow2.type(), org.hamcrest.Matchers.equalTo("DepositSuccess"));
        assertThat("Deposit 2 account should match", depositRow2.account(), org.hamcrest.Matchers.equalTo(String.valueOf(account1.account)));
        assertThat("Deposit 2 submitted amount should match", depositRow2.submitted(), org.hamcrest.Matchers.containsString(String.format("%s %s", formatter.format(deposit2.getAmount()), deposit2.getCurrency())));
        assertThat("Deposit 2 processed amount should match", depositRow2.processed(), org.hamcrest.Matchers.is(Matchers.emptyString()));

        assertThat("Withdrawal 1 type should be WithdrawalSuccess", withdrawalRow1.type(), org.hamcrest.Matchers.equalTo("WithdrawalSuccess"));
        assertThat("Withdrawal 1 account should match", withdrawalRow1.account(), org.hamcrest.Matchers.equalTo(String.valueOf(account1.account)));
        assertThat("Withdrawal 1 submitted amount should match", withdrawalRow1.submitted(), org.hamcrest.Matchers.containsString(String.format("-%s %s", formatter.format(withdrawal1.getAmount()), withdrawal1.getCurrency())));
        assertThat("Withdrawal 1 risk audit should be approved", withdrawalRow1.riskAudit(), org.hamcrest.Matchers.equalTo("Approved manually"));
        assertThat("Withdrawal 1 rejection reason should be Internal reason", withdrawalRow1.rejectionReason(), org.hamcrest.Matchers.is(Matchers.emptyString()));
    }

    @Test
    @AllureId("1873")
    @DisplayName("Transaction history - transaction drawer")
    void transaction_drawer_test() {
        openTransactionPage();

        paymentsPage.clickTransactionRow(withdrawal2.getOrderNumber());
        paymentsPage.checkTransactionDetailsDrawerIsVisible();

        var transactionDataMap = paymentsPage.getTransactionDetailsData();
        assertThat(transactionDataMap.get("Status"), org.hamcrest.Matchers.is("Partial success"));
        assertThat(transactionDataMap.get("Order"), org.hamcrest.Matchers.is(withdrawal2.getOrderNumber()));
        assertThat(transactionDataMap.get("Submitted"), org.hamcrest.Matchers.containsString(String.format("%s %s", formatter.format(withdrawal2.getAmount()), withdrawal2.getCurrency())));
        assertThat(transactionDataMap.get("Account"), org.hamcrest.Matchers.is(String.valueOf(account1.account)));
        assertThat(transactionDataMap.get("Type"), org.hamcrest.Matchers.is("Withdrawal"));
        assertThat(transactionDataMap.get("CRM Status"), org.hamcrest.Matchers.is("Partial success"));
        assertThat(transactionDataMap.get("Processed"), org.hamcrest.Matchers.containsString(String.format("%s %s", formatter.format(withdrawal2.getAmount().subtract(withdrawal2.getReversedAmount())), withdrawal2.getCurrency())));

        var paymentProfileDataMap = paymentsPage.getPaymentProfileDetailsDataFromDrawer();
        assertThat(paymentProfileDataMap.get("Type"), org.hamcrest.Matchers.is(withdrawal2.getPaymentType()));
        assertThat(paymentProfileDataMap.get("Family"), org.hamcrest.Matchers.is(withdrawal2.getPaymentFamily()));
        assertThat(paymentProfileDataMap.get("System"), org.hamcrest.Matchers.is(withdrawal2.getPaymentChannel()));

        var riskAuditDataMap = paymentsPage.getRiskAuditDataFromDrawer();
        assertThat(riskAuditDataMap.get("Status"), org.hamcrest.Matchers.is("Rejected manually"));
        assertThat(riskAuditDataMap.get("Message"), org.hamcrest.Matchers.is("Test"));

        paymentsPage.closeTransactionDetailsDrawer();
    }

    void openTransactionPage() {
        page.waitForTimeout(1000);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        paymentsPage.navigatePaymentsTab(client1.getUcid());
        paymentsPage.clickTransactionsButton();
        page.waitForTimeout(1000);
    }
}
