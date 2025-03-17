package utils.demoHelpers;

import businessObjects.db.clickhouse.accountIbRelation.AccountIbRelationObject;
import businessObjects.db.clickhouse.connectionTable.ConnectionTableEntry;
import businessObjects.db.clickhouse.crmIdProof.CrmTbIdProofObject;
import businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObject;
import businessObjects.db.clickhouse.crmTbKycFiles.KycFilesTableEntry;
import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;
import businessObjects.db.clickhouse.mtAccount.MtAccountObject;
import businessObjects.db.clickhouse.mtMt4TradesCoerced.MtMt4TradesCoercedObject;
import businessObjects.db.clickhouse.mtMt5DealsCoerced.Mt5DealsCoercedObject;
import businessObjects.db.clickhouse.s3FactIbSalesCommissions.S3FactIbSalesCommissionsObject;
import businessObjects.db.clickhouse.s3FactLoginMetrics.S3FactLoginMetricsObject;
import helpers.data.ClientHelper;
import helpers.data.enums.Brand;
import helpers.data.enums.DateTimeFormat;
import helpers.data.enums.Regulator;
import io.qameta.allure.Allure;
import net.datafaker.Faker;

import java.util.ArrayList;
import java.util.List;

import static businessObjects.db.clickhouse.accountIbRelation.AccountIbRelationFactory.generateAccountIbRelationObjectByClient;
import static businessObjects.db.clickhouse.connectionTable.ConnectionTableEntryFactory.getConnectionTableEntry;
import static businessObjects.db.clickhouse.connectionTable.ConnectionTableEntryFactory.getConnectionTableEntryLvl2;
import static businessObjects.db.clickhouse.crmIdProof.CrmTbIdProofFactory.generateIdProofObject;
import static businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObjectFactory.generateAdditionalCrmTbAccountData;
import static businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObjectFactory.generateStaticCrmTbAccountActive;
import static businessObjects.db.clickhouse.crmTbKycFiles.KycFilesTableEntryFactory.getKycFile;
import static businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObjectFactory.generateStaticUserByClient;
import static businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObjectFactory.generateUserByClient;
import static businessObjects.db.clickhouse.mtAccount.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static businessObjects.db.clickhouse.mtMt4TradesCoerced.MtMt4TradesCoercedObjectFactory.generateBunchMt4TradesCoerced;
import static businessObjects.db.clickhouse.mtMt5DealsCoerced.Mt5DealsCoercedFactory.generateTradeByClient;
import static businessObjects.db.clickhouse.s3FactIbSalesCommissions.S3FactIbSalesCommissionsFactory.generateS3FactIbSalesCommissionsClient;
import static businessObjects.db.clickhouse.s3FactLoginMetrics.S3FactLoginMetricsFactory.generateS3FactLoginMetricsClient;
import static helpers.database.DbHelper.*;
import static utils.Constants.*;
import static utils.Utils.*;

public class DemoClientCreator {

    Faker faker = new Faker();

    public void createDemoClient(Brand brand, int clientID, Regulator regulator, int serverId) {

        ClientHelper client1 = new ClientHelper();
        client1.setBrand(brand);
        client1.setUserId(clientID);
        client1.setRegulator(regulator);
        client1.setTradingAccount(client1.getUserId() * 100);
        client1.setServerId(serverId);
        CrmTbUserObject clientCrm1 = generateUserByClient(client1);
        clientCrm1.firstName = faker.name().firstName();
        clientCrm1.lastName = faker.name().lastName();

        ClientHelper client2 = new ClientHelper();
        client2.setBrand(brand);
        client2.setUserId(clientID + 1);
        client2.setRegulator(regulator);
        client2.setTradingAccount(client1.getUserId() * 100 + 1);
        client2.setServerId(serverId);
        CrmTbUserObject clientCrm2 = generateUserByClient(client2);
        clientCrm2.firstName = faker.name().firstName();
        clientCrm2.lastName = faker.name().lastName();

        ClientHelper client3 = new ClientHelper();
        client3.setBrand(brand);
        client3.setUserId(clientID + 2);
        client3.setRegulator(regulator);
        client3.setTradingAccount(client1.getUserId() * 100 + 2);
        client3.setServerId(serverId);
        CrmTbUserObject clientCrm3 = generateUserByClient(client3);
        clientCrm3.firstName = faker.name().firstName();
        clientCrm3.lastName = faker.name().lastName();

        ArrayList<MtMt4TradesCoercedObject> trades = generateBunchMt4TradesCoerced(client1, 20);

        insertObjectsToDbSlow(MT4_TRADES_COERCED_TABLE_NAME, trades);


        insertObjectsToDb(CRM_USER_TABLE_NAME, List.of(clientCrm1, clientCrm2, clientCrm3));

        CrmTbAccountObject account = generateStaticCrmTbAccountActive(client1);
        insertObjectToDb(CRM_ACCOUNT_TABLE_NAME, account);
        MtAccountObject mtAccount = generateMtAccountByCrmTbAccount(account);
        insertObjectToDb(MT_ACCOUNT_TABLE_NAME, mtAccount);

        ConnectionTableEntry connection1 = getConnectionTableEntry(client1, client2);
        ConnectionTableEntry connection2 = getConnectionTableEntryLvl2(client2, client3);
        insertObjectsToDb(CONNECTIONS_TABLE_NAME, List.of(connection1, connection2));


        CrmTbIdProofObject idProofObject = generateIdProofObject(client1);
        idProofObject.setFileTypeId(27);
        idProofObject.setStatus("SUBMITTED");

        KycFilesTableEntry file = getKycFile(client1);
        file.proofId = idProofObject.getId();
        file.fileName = FILE_KYC_POF_1_NAME;
        file.fileTypeId = 27;
        insertObjectToDb(KYC_FILES_TABLE_NAME, file);
        insertObjectToDb(ID_PROOF_TABLE_NAME, idProofObject);


        ClientHelper referral = new ClientHelper(232_303, "d555fa11-3e45-44d3-8070-e28eaff997c7", Brand.INFINOX, Regulator.VFSC2, 232_303_001, 232_303_002, 42);
        CrmTbUserObject crmTbReferral = generateStaticUserByClient(referral);

        crmTbReferral.firstName = "Relation";
        crmTbReferral.lastName = "Clientson";
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbReferral);
        CrmTbAccountObject refAccount1 = generateStaticCrmTbAccountActive(referral);
        CrmTbAccountObject refAccount2 = generateAdditionalCrmTbAccountData(referral);
        insertObjectToDb(CRM_ACCOUNT_TABLE_NAME, refAccount1);
        insertObjectToDb(CRM_ACCOUNT_TABLE_NAME, refAccount2);
        MtAccountObject refMtAccount1 = generateMtAccountByCrmTbAccount(refAccount1);
        MtAccountObject refMtAccount2 = generateMtAccountByCrmTbAccount(refAccount2);
        insertObjectToDb(MT_ACCOUNT_TABLE_NAME, refMtAccount1);
        insertObjectToDb(MT_ACCOUNT_TABLE_NAME, refMtAccount2);


        deleteObjectFromDb(ACCOUNT_IB_RELATION_TABLE_NAME, "ucid ='" + client1.getUcid() + "'");
        deleteObjectFromDb(S3_FACT_IB_SALES_COMMISSIONS, "ucid ='" + client1.getUcid() + "'");
        AccountIbRelationObject relation = generateAccountIbRelationObjectByClient(client1);
        relation.setDirectIbRebateAccount(referral.getTradingAccount());
        insertObjectToDb(ACCOUNT_IB_RELATION_TABLE_NAME, relation);
        S3FactIbSalesCommissionsObject commission = generateS3FactIbSalesCommissionsClient(client1);
        commission.setIbRebateAccount(relation.getDirectIbRebateAccount());
        commission.setSalesCommission(getRandomRoundedDouble(0.00, 5_000_000.00));
        commission.setIbCommission(getRandomRoundedDouble(0.00, 5_000_000.00));
        insertObjectToDb(S3_FACT_IB_SALES_COMMISSIONS, commission);
        AccountIbRelationObject relation2 = generateAccountIbRelationObjectByClient(client1);
        relation2.setDirectIbRebateAccount(referral.getTradingAccount2());
        insertObjectToDb(ACCOUNT_IB_RELATION_TABLE_NAME, relation2);
        S3FactIbSalesCommissionsObject commission2 = generateS3FactIbSalesCommissionsClient(client1);
        commission2.setIbRebateAccount(relation2.getDirectIbRebateAccount());
        commission2.setSalesCommission(getRandomRoundedDouble(0.00, 5_000_000.00));
        commission2.setIbCommission(getRandomRoundedDouble(0.00, 5_000_000.00));
        insertObjectToDb(S3_FACT_IB_SALES_COMMISSIONS, commission2);


        S3FactLoginMetricsObject historyMetrics1 = generateS3FactLoginMetricsClient(client1);
        historyMetrics1.setDate(getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE, 0, 0, 1, 0, 0));
        historyMetrics1.setDailyNetClosedPnl(getRandomRoundedDouble(0, 555_555));
        S3FactLoginMetricsObject historyMetrics2 = generateS3FactLoginMetricsClient(client1);
        historyMetrics2.setDate(getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE, 0, 0, 2, 0, 0));
        historyMetrics2.setDailyNetClosedPnl(getRandomRoundedDouble(0, 555_555));
        insertObjectsToDb(S3_FACT_LOGIN_METRICS_TABLE_NAME, List.of(historyMetrics1, historyMetrics2));
        Allure.step("Generate MT5 deals for current date");
        Mt5DealsCoercedObject deal1 = generateTradeByClient(client1);
        deal1.time = getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 0, 0, 0, 1);
        deal1.profitUsd = getRandomRoundedDouble(0, 555_555);
        deal1.commissionUsd = getRandomRoundedDouble(0, 555_555);
        deal1.storageUsd = getRandomInt();
        Allure.step("Generate MT5 deals outside of current date");
        Mt5DealsCoercedObject deal2 = generateTradeByClient(client1);
        deal2.time = getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 0, 1, 0, 0, 1);
        deal2.profitUsd = getRandomRoundedDouble(0, 555_555);
        deal2.commissionUsd = getRandomRoundedDouble(0, 555_555);
        deal2.storageUsd = getRandomInt();
        insertObjectsToDb(MT5_DEALS_COERCED_TABLE_NAME, List.of(deal1, deal2));
        System.out.println("Client " + client1.getUcid() + " created");
    }

//    @Test
    public void generateDemoClient() {
        createDemoClient(Brand.VANTAGE, 44_440_302, Regulator.VFSC2, 115);
    }

}
