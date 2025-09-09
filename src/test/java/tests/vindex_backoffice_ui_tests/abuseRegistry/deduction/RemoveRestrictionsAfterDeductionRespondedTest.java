package tests.vindex_backoffice_ui_tests.abuseRegistry.deduction;

import business_objects.db.abuse_registry_db.AbuserDeduction;
import business_objects.db.abuse_registry_db.AbuserHistory;
import business_objects.db.abuse_registry_db.DeductionKafkaRequest;
import business_objects.db.audit_service_db.Event;
import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.mt_account.MtAccountObject;
import business_objects.db.clickhouse.mt_mt5_positions.MtMt5PositionsObject;
import business_objects.kafka.deductions.AccountDeductionRequest;
import business_objects.kafka.deductions.AccountDeductionRequestResponse;
import helpers.data.ClientHelper;
import helpers.database.DbName;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.util.Currency;
import java.util.List;
import java.util.logging.Logger;

import static business_objects.db.abuse_registry_db.AbuserDeductionFactory.generateAbuserDeductionByAccount;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.db.clickhouse.mt_mt5_positions.MtMt5PositionsObjectFactory.generateMtMt5PositionsObject;
import static helpers.api.AbuseRegistryHelper.addFraudForClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.FraudSubtype.FIRST_TIME;
import static helpers.data.enums.FraudType.GAP_TRADING;
import static helpers.data.enums.FraudTypeStatus.CONFIRMED;
import static helpers.data.enums.Restriction.MANUAL_WITHDRAWAL_REVIEW;
import static helpers.data.enums.Restriction.WITHDRAWALS;
import static helpers.data.enums.deduction.DeductionStatusApproval.AWAITING_APPROVAL;
import static helpers.data.enums.deduction.DeductionStatusDeduction.TO_BE_DEDUCTED;
import static helpers.data.enums.deduction.DeductionStatusEmail.NOT_SENT;
import static helpers.database.ArHelper.deleteUserFromAbuseRegistry;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.CleanTableHelper.cleanCrmUserTableByClient;
import static helpers.database.CleanTableHelper.cleanUserAudit;
import static helpers.database.DbHelper.*;
import static helpers.database.DbHelper.getObjectsFromDB;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static page_objects.backoffice_pages.investigationTool.RestrictionPage.setRestrictionAPIGeneral;
import static tests.TestBaseApi.objectMapper;
import static utils.Constants.*;
import static utils.Utils.*;


@Feature("BMS-1620 Finalize restrictions after processing deduction")
@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_WEB)
class RemoveRestrictionsAfterDeductionRespondedTest extends TestBaseWeb {

    static CrmTbUserObject crmTbUser;
    static ClientHelper client;
    static CrmTbAccountObject account;
    static MtMt5PositionsObject position1;
    static MtMt5PositionsObject position2;
    static MtAccountObject mtAccount;
    static AbuserDeduction deduction;

    @AfterEach
    void teardown() throws Exception {
        deleteUserFromAbuseRegistry(client.getUcid());
        deleteEntryFromDb(CRM_TB_USER_EXTENDS_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        try {
            deleteEntryFromDb(CRM_TB_ACCOUNT_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        } catch (Exception e) {
            Logger.getLogger(ManageSingleDeductionTest.class.getName()).info("Account deletion failed");
        }
        deleteEntryFromDb(MT_ACCOUNT_TABLE_NAME, String.format("account = '%s'", account.account));
        deleteEntryFromDb(MT5_POSITIONS_TABLE_NAME, String.format("ucid = '%s'", position1.getUcid()));
        deleteEntryFromDb(MT5_POSITIONS_TABLE_NAME, String.format("ucid = '%s'", position2.getUcid()));
        deleteEntryFromDb(DbName.POSTGRES, AR_ABUSER_DEDUCTION_TABLE_NAME, String.format("ucid = '%s'", deduction.getUcid()));
        closeAlert(crmTbUser.ucid);
        cleanCrmUserTableByClient(crmTbUser.ucid);
        cleanUserAudit(crmTbUser.ucid);
    }

    @Test
    @DisplayName("restrictions WR and Withdrawals automatically removed after deduction processed and got response in Kafka")
    @AllureId("1522")
    void restrictionsRemovesWhenDeductionGetResponse() throws Exception {

        //Create a record about a client in crm_tb_user
        client = getRandomVantageClientAllFields();
        crmTbUser = generateUserByClient(client);
        client.setFirstName(faker.name().firstName());
        client.setLastName(faker.name().lastName());
        crmTbUser.firstName = client.getFirstName();
        crmTbUser.lastName = client.getLastName();
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);

        //Create a record about a client's account
        account = generateCrmTbAccountDataForUi(client);
        account.currency = Currency.getInstance("EUR").getCurrencyCode();
        mtAccount = generateMtAccountByCrmTbAccount(account);
        insertObjectToDb(CRM_TB_ACCOUNT_TABLE_NAME, account);
        insertObjectToDb(MT_ACCOUNT_TABLE_NAME, mtAccount);
        Thread.sleep(3000);

        //add fraud for the client
        addFraudForClient(client, GAP_TRADING, FIRST_TIME, CONFIRMED, List.of("EURUSD", "GBPUSD"));

        //generate deduction
        List<AbuserHistory> abuserHistory = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_HISTORY_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()), AbuserHistory.class);
        deduction = generateAbuserDeductionByAccount(account, abuserHistory.getLast().getId());
        deduction.setStatusDeduction(TO_BE_DEDUCTED.getDisplayName());
        deduction.setStatusApproval(AWAITING_APPROVAL.getDisplayName());
        deduction.setStatusEmail(NOT_SENT.getDisplayName());
        deduction.setIllegalProfitUsd(-12.00);//set gap_illegal_profit_total (illegal profit less than) gap_pnl_total
        deduction.setIllegalProfit(deduction.getIllegalProfitUsd());
        insertObjectToDb(DbName.POSTGRES, AR_ABUSER_DEDUCTION_TABLE_NAME, deduction);


        //set MT5 positions gap_pnl_total = (sum(trading_pnl_usd) = sum(profit_usd + storage_usd)) and must be grater than gap_illegal_profit_total: sum of illegal_profit_usd
        position1 = generateMtMt5PositionsObject(client);//
        position1.setProfitUsd(1100.00);
        position1.setStorageUsd(1000.00);
        position1.setAccount(account.account);
        position1.setServerId(account.serverIdSt);
        position1.setServerName(account.serverName);
        insertObjectToDb(MT5_POSITIONS_TABLE_NAME, position1);
        page.waitForTimeout(1000);

        setRestrictionAPIGeneral(client.getUcid(), MANUAL_WITHDRAWAL_REVIEW.getCode());
        setRestrictionAPIGeneral(client.getUcid(), WITHDRAWALS.getCode());

        System.out.println("client ucid is " + client.getUcid());

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        deductionPage.navigateDeduction();
        deductionPage.hoverOverDeductionTableRow(client.getUserId());
        deductionPage.openEditDrawer();
        deductionPage.isDeductIsInactive();
        double deductionValue = getRandomRoundedDouble(0.01, 999.99);
        deductionPage.fillDeductionInput(Double.toString(deductionValue));
        deductionPage.isDeductIsActive();
        deductionPage.finishDeduction();
        page.waitForTimeout(10_000); // wait for response from third party services
        Allure.step("get clients deduction from DB");
        DeductionKafkaRequest deductionRequestDb = (getObjectsFromDB(DbName.POSTGRES, AR_DEDUCTION_KAFKA_REQUEST_TABLE_NAME, "payload like '%" + client.getTradingAccount() + "%'", DeductionKafkaRequest.class)).getFirst();
        AccountDeductionRequest request = objectMapper.readValue(deductionRequestDb.getPayload(), AccountDeductionRequest.class);
        AccountDeductionRequestResponse response = new AccountDeductionRequestResponse(request.getTimestamp(), request.getMessageId(), "Success", null);
        Allure.step("delete all responces from DB that send by automation");
        deleteEntryFromDb(DbName.POSTGRES, AR_DEDUCTION_KAFKA_RESPONSE_TABLE_NAME, String.format("message_id = '%s'", request.getMessageId()));
        Allure.step("send your response to Kafka");
        kafka.produceMessage(request.getMessageId(), objectMapper.writeValueAsString(response), KAFKA_TOPIC_ACCOUNT_DEDUCTION_REQUEST_RESPONSE);
        List<Event> cancellationEvents = getObjectsFromDB(DbName.AUDIT, AUDIT_EVENT, "ucid = '" + client.getUcid() + "' and type = 'RESTRICTION_CANCELLED'", Event.class);
        Allure.step("check that there is 2 cancellation events in audit for our test client");
        assertEquals(2, cancellationEvents.size());
        Allure.step("check that there is cancellation event for Withdrawals restriction");
        Event eventFirst = cancellationEvents.stream().filter(e -> "Withdrawals".equals(e.getDetails())).findFirst().orElse(null);
        Assertions.assertNotNull(eventFirst);
        Allure.step("check that there is cancellation event for Manual withdrawal review restriction");
        Event eventSecond = cancellationEvents.stream().filter(e -> "Manual withdrawal review".equals(e.getDetails())).findFirst().orElse(null);
        Assertions.assertNotNull(eventSecond);
    }
}
