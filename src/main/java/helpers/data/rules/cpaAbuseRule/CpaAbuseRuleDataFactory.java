package helpers.data.rules.cpaAbuseRule;

import businessObjects.db.clickhouse.boClientFraudTypes.BoClientFraudTypesObject;
import businessObjects.db.clickhouse.connectionTable.ConnectionTableEntry;
import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;

import businessObjects.kafka.crmEvents.WithdrawalEvent;
import generator.annotations.RuleTestData;
import helpers.data.ClientHelper;
import helpers.data.enums.FraudType;
import helpers.data.rules.RuleDataHelper;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;

import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObjectFactory.generateUserByClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.ClientFactory.getRandomVantageClientNoCpaIbRef;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.DbHelper.*;
import static helpers.database.MitigationHelper.cleanUserRestriction;
import static utils.Constants.*;
import static utils.Utils.*;

@RuleTestData("cpa-abuse")
public class CpaAbuseRuleDataFactory {
    private static final ClientHelper cpaAbuseRuleExitEventEnd1Client = getRandomVantageClientNoCpaIbRef();
    private static final ClientHelper cpaAbuseRuleExitEventEnd2_1Client = getRandomVantageClientAllFields();


    @Step("Create data for Mirror trading rule")
    private static RuleDataHelper getCpaAbuseRuleData(ClientHelper client) {
        CrmTbUserObject userObject = generateUserByClient(client);
        WithdrawalEvent withdrawalEvent = new WithdrawalEvent(
                getRandomUuidString(), Instant.now().toString(), getRandomIntPositive(), client.getUserId(), client.getTradingAccount(), client.getBrand(), "vfsc", "FASAPAY", 1, 1d, 1d, 1d, 1d, "555555**** **6666", 1, Instant.now().toString(), "", "", "", 1, "", 1d, 1, 1, "", 1, 1, 1d, 2, 1d, "withdrawal"
        );
        return new RuleDataHelper(client, userObject, null, null, new ArrayList<>(), new ArrayList<>(), withdrawalEvent, null, new ArrayList<>(), null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), null, null, new ArrayList<>(), new ArrayList<>());
    }

    private static ConnectionTableEntry getConnection(ClientHelper fromClient, ClientHelper toClient) {
        return new ConnectionTableEntry(
                fromClient.getUcid(), toClient.getUcid(), "Same Identity", 1d, "[{\"connectionAttributeName\": \"payout\", \"connectionAttributeValue\": \"535456**** **0344\", \"sourceAttributeValue\": \"535456**** **0344\", \"relationType\": \"exact\"}]");
    }

    public static RuleDataHelper getCpaAbuseRuleExitEventEnd1Data() {
        Allure.step("Get client data");
        RuleDataHelper data = getCpaAbuseRuleData(cpaAbuseRuleExitEventEnd1Client);
        Allure.step("Create user object with no CPA");
        data.clientHelper.setCpaId(null);
        return data;
    }

    public static RuleDataHelper getCpaAbuseRuleExitEventEnd2_1Data() {
        Allure.step("Get client data");
        RuleDataHelper data = getCpaAbuseRuleData(cpaAbuseRuleExitEventEnd2_1Client);
        Allure.step("Client has mirror trading abuse connected account");
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        data.connections.add(getConnection(data.clientHelper, connectedClient));
        BoClientFraudTypesObject boClientFraudTypesObject = new BoClientFraudTypesObject(
                connectedClient.getUcid(), FraudType.CPA_ABUSE.getFraudTypeId(), FraudType.CPA_ABUSE.getKey()
        );
        data.clientFraudTypes.add(boClientFraudTypesObject);
        Allure.step("Set restriction");
        Allure.step("Send alert");
        return data;
    }

    public static Map<String, RuleDataHelper> setupCpaAbuseRuleData() throws ReflectiveOperationException,
            SQLException {
        startSshTunnel();
        Map<String, RuleDataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("1", getCpaAbuseRuleExitEventEnd1Data());
        map.put("2_1", getCpaAbuseRuleExitEventEnd2_1Data());

        // Loop through the list with data and insert all the data into the according tables
        for (RuleDataHelper data : map.values()) {
            insertObjectToDb(CRM_USER_TABLE_NAME, data.crmTbUserObject);
            data.connections.forEach(connection -> {
                try {
                    insertObjectToDb(CONNECTIONS_TABLE_NAME, connection);
                } catch (SQLException | ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            });
            data.clientFraudTypes.forEach(fraud -> {
                try {
                    insertObjectToDb(CLIENT_FRAUD_TYPES_TABLE_NAME, fraud);
                } catch (SQLException | ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            });
            if (data.crmTbAccountObject != null) {
                insertObjectToDb(CRM_ACCOUNT_TABLE_NAME, data.crmTbAccountObject);
            }
            data.crmTbAccountObjectConnections.forEach(credit -> {
                try {
                    insertObjectToDb(CRM_ACCOUNT_TABLE_NAME, credit);
                } catch (SQLException | ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            });
            data.mtTbCreditsObjects.forEach(credit -> {
                try {
                    insertObjectToDb(MT_CREDITS_TABLE_NAME, credit);
                } catch (SQLException | ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            });
            data.crmTbWithdrawalObjects.forEach(withdrawal -> {
                try {
                    insertObjectToDb(CRM_WITHDRAWAL_TABLE_NAME, withdrawal);
                } catch (SQLException | ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            });
            data.crmTbDepositObjects.forEach(deposit -> {
                try {
                    insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, deposit);
                } catch (SQLException | ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            });
            data.crmTbBonusObjects.forEach(bonus -> {
                try {
                    insertObjectToDb(CRM_BONUS_TABLE_NAME, bonus);
                } catch (SQLException | ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            });
            data.mt5DealsObjects.forEach(deal -> {
                try {
                    insertObjectToDb(MT5_DEALS_COERCED_TABLE_NAME, deal);
                } catch (SQLException | ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            });
            data.mtBalanceOrdersObjects.forEach(deal -> {
                try {
                    insertObjectToDb(MT_BALANCE_ORDERS_TABLE_NAME, deal);
                } catch (SQLException | ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            });
            data.mirrorLoginObjects.forEach(deal -> {
                try {
                    insertObjectToDb(MIRROR_LOGIN_TABLE_NAME, deal);
                } catch (SQLException | ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            });
            if (data.aggrCreditEquityRate != null) {
                insertObjectToDb(AGGR_CREDIT_EQUITY_RATE, data.aggrCreditEquityRate);
            }
            if (data.aggrMirrorAccountsByTrades != null) {
                insertObjectToDb(MIRROR_LOGIN_TABLE_NAME, data.aggrMirrorAccountsByTrades);
            }
        }
        return map;
    }

    public static void deleteCpaAbuseRuleData(Map<String, RuleDataHelper> map) throws Exception {
        // Loop through the list with data and delete all the previously created data into the according tables
        for (RuleDataHelper data : map.values()) {
            deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("user_id = %s", data.crmTbUserObject.userId));
            data.connections.forEach(connection -> {
                try {
                    deleteEntryFromDb(CONNECTIONS_TABLE_NAME, String.format("user_from = '%s'", connection.userFrom));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            deleteEntryFromDb(LEXIS_NEXIS_TABLE_NAME, String.format("user_id = %s", data.lnSessionParsedObjectRegistration.userId));
            deleteEntryFromDb(LEXIS_NEXIS_TABLE_NAME, String.format("user_id = %s", data.lnSessionParsedObjectLogin.userId));
            data.clientFraudTypes.forEach(fraud -> {
                try {
                    deleteEntryFromDb(CLIENT_FRAUD_TYPES_TABLE_NAME, String.format("ucid = '%s'", fraud.ucid));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            data.mtTbCreditsObjects.forEach(credit -> {
                try {
                    deleteEntryFromDb(MT_CREDITS_TABLE_NAME, String.format("ucid = '%s'", credit.ucid));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            data.crmTbWithdrawalObjects.forEach(withdrawal -> {
                try {
                    deleteEntryFromDb(CRM_WITHDRAWAL_TABLE_NAME, String.format("ucid = '%s'", withdrawal.ucid));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            data.crmTbDepositObjects.forEach(deposit -> {
                try {
                    deleteEntryFromDb(CRM_DEPOSIT_TABLE_NAME, String.format("ucid = '%s'", deposit.ucid));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            data.crmTbBonusObjects.forEach(bonus -> {
                try {
                    deleteEntryFromDb(CRM_BONUS_TABLE_NAME, String.format("ucid = '%s'", bonus.ucid));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            data.mtBalanceOrdersObjects.forEach(bonus -> {
                try {
                    deleteEntryFromDb(MT_BALANCE_ORDERS_TABLE_NAME, String.format("ucid = '%s'", bonus.ucid));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            data.mt5DealsObjects.forEach(deal -> {
                try {
                    deleteEntryFromDb(MT5_DEALS_COERCED_TABLE_NAME, String.format("server_id = %s and account = %s", deal.serverId, deal.account));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            data.mirrorLoginObjects.forEach(mirrorLoginObject -> {
                try {
                    deleteEntryFromDb(MIRROR_LOGIN_TABLE_NAME, String.format("login_1 = %s", mirrorLoginObject.login_1));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            cleanUserRestriction(data.clientHelper.getUcid());
            closeAlert(data.clientHelper.getUcid());
        }
        stopSshTunnel();
    }
}
