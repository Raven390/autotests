package helpers.data.rules.payments;

import business_objects.db.clickhouse.client_cards.ClientCardsObject;
import business_objects.db.clickhouse.crm_bp_callbacks.CrmBpCallbacksObject;
import business_objects.db.clickhouse.crm_tb_deposit_channel.CrmTbDepositChannelObject;
import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntity;
import business_objects.db.clickhouse.crm_tb_deposit_type.CrmTbDepositTypeObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.data_science_test.connection_table.ConnectionTableEntry;
import business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedObject;
import business_objects.db.clickhouse.segmentation_table.SegmentationTableObject;
import business_objects.db.ticks.rates_usd_current.RatesUsdCurrentObject;
import business_objects.kafka.crm_events.CallbackEvent.*;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import helpers.data.enums.DateTimeFormat;
import helpers.data.enums.FraudType;
import io.qameta.allure.Description;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static business_objects.db.clickhouse.client_cards.ClientCardObjectFactory.generateClientCardsObject;
import static business_objects.db.clickhouse.client_fraud_types.ClientFraudTypesFactory.createClientFraudTypeCh;
import static business_objects.db.clickhouse.crm_bp_callbacks.CrmBpCallbacksFactory.generateCrmBpCallbacksObject;
import static business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.data_science_test.connection_table.ConnectionTableEntryFactory.getConnection;
import static business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedFactory.generateTradeByClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.DataHelper.createClient;
import static helpers.data.DataHelper.setupData;
import static helpers.database.DbHelper.startSshTunnel;
import static utils.Utils.*;

public class ChargebackRuleDataFactory {


    private static final ClientHelper chargebackRuleClient1 = getRandomVantageClientAllFields();
    private static final ClientHelper chargebackRuleClient2 = getRandomVantageClientAllFields();
    private static final ClientHelper chargebackRuleClient3 = getRandomVantageClientAllFields();
    private static final ClientHelper chargebackRuleClient4 = getRandomVantageClientAllFields();
    private static final ClientHelper chargebackRuleClient5 = getRandomVantageClientAllFields();
    private static final ClientHelper chargebackRuleClient6 = getRandomVantageClientAllFields();
    private static final ClientHelper chargebackRuleClient7 = getRandomVantageClientAllFields();
    private static final ClientHelper chargebackRuleClient8 = getRandomVantageClientAllFields();
    private static final ClientHelper chargebackRuleClient9 = getRandomVantageClientAllFields();
    private static final ClientHelper chargebackRuleClient10 = getRandomVantageClientAllFields();
    private static final ClientHelper chargebackRuleClient11 = getRandomVantageClientAllFields();
    private static final ClientHelper chargebackRuleClient12 = getRandomVantageClientAllFields();
    private static final ClientHelper chargebackRuleClient13 = getRandomVantageClientAllFields();
    private static final ClientHelper chargebackRuleClient14 = getRandomVantageClientAllFields();
    private static final ClientHelper chargebackRuleClient15 = getRandomVantageClientAllFields();
    private static final ClientHelper chargebackRuleClient16 = getRandomVantageClientAllFields();
    private static final ClientHelper chargebackRuleClient17 = getRandomVantageClientAllFields();
    private static final ClientHelper chargebackRuleClient18 = getRandomVantageClientAllFields();
    private static final ClientHelper chargebackRuleClient19 = getRandomVantageClientAllFields();
    private static final ClientHelper chargebackRuleClient20 = getRandomVantageClientAllFields();
    private static final ClientHelper chargebackRuleClient21 = getRandomVantageClientAllFields();
    private static final ClientHelper chargebackRuleClient22 = getRandomVantageClientAllFields();
    private static Attributes attributes;
    private static Charge charge;
    private static CallbackData callbackData;
    private static Callback callback;
    private static CallbackEvent callbackEvent;
    private static final String cardExpiration = "1036";
    private static final String cardMaskedNumber = getRandomCardMaskedNumber();
    static String cardFirstSixDigits = cardMaskedNumber.substring(0, 6);
    static String cardLastFourDigits = cardMaskedNumber.substring(cardMaskedNumber.length() - 4);
    private static final String baseCurrency = "EUR";
    private static final String basePaymentProfile = (getPaymentProfileCard(cardMaskedNumber, cardExpiration));

    @Description("Create data for Chargeback rule")
    private static DataHelper getChargebackRuleData(ClientHelper client) {
        DataHelper data = new DataHelper();
        createClient(data, client);

        attributes = Attributes.builder().cardMaskedNumber(cardMaskedNumber).cardExpiration(cardExpiration).status("approved").currency(baseCurrency).cardHolderName(data.clientHelper.getFirstName() + " " + data.clientHelper.getLastName() + "off").is3d(false).amount(500.00).build();

        charge = Charge.builder().attributes(attributes).id(getRandomUuidString()).build();

        callbackData = CallbackData.builder().orderId("TST" + getRandomIntPositive()).charge(charge).build();
        callback = Callback.builder().data(callbackData).build();

        callbackEvent = CallbackEvent.builder().id(getRandomUuidString()).brand(data.clientHelper.getBrand()).clientId(data.clientHelper.getUserId().longValue()).type("callback_deposit").regulator(data.clientHelper.getRegulator()).businessOrderId("TST" + getRandomIntPositive()).messageId(getRandomUuidString()).eventDate(Instant.now().toString()).callback(callback).paymentMethodCode("PMC").build();
        data.callbackEvent = callbackEvent;

        return data;
    }

    private static void buildCallbackEvent() {
        charge.setAttributes(attributes);
        callbackData.setCharge(charge);
        callback.setData(callbackData);
        callbackEvent.setCallback(callback);
    }

    private static DataHelper getChargebackTest1Data() {
        DataHelper data = getChargebackRuleData(chargebackRuleClient1);
        data.callbackEvent = callbackEvent;
        return data;
    }

    private static DataHelper getChargebackTest2Data() {
        DataHelper data = getChargebackRuleData(chargebackRuleClient2);
        attributes.setStatus("declined");
        buildCallbackEvent();
        data.callbackEvent = callbackEvent;


        //set deposit
        CrmTbDepositEntity deposit1 = generateCrmTbDepositEntityByClient(data.clientHelper);
        deposit1.setAmountUsd(BigDecimal.valueOf(100.0));
        deposit1.setPaymentProfileKey(getPaymentProfileCard(cardMaskedNumber, "2222"));
        data.crmTbDepositObjects = List.of(deposit1);

        return data;

    }

    private static DataHelper getChargebackTest3Data() {
        DataHelper data = getChargebackRuleData(chargebackRuleClient3);

        //set deposit
        CrmTbDepositEntity deposit1 = generateCrmTbDepositEntityByClient(data.clientHelper);
        deposit1.setAmountUsd(BigDecimal.valueOf(100.0));
        deposit1.setPaymentProfileKey(getPaymentProfileCard(cardMaskedNumber, cardExpiration));
        data.crmTbDepositObjects = List.of(deposit1);

        return data;
    }

    private static DataHelper getChargebackTest4Data() {
        DataHelper data = getChargebackRuleData(chargebackRuleClient4);

        attributes.setStatus("declined");
        buildCallbackEvent();
        data.callbackEvent = callbackEvent;

        //set fraud
        data.clientFraudTypes.add(createClientFraudTypeCh(data.clientHelper.getUcid(), FraudType.CHARGEBACK.getCode()));

        //set deposit
        CrmTbDepositEntity deposit1 = generateCrmTbDepositEntityByClient(data.clientHelper);
        deposit1.setAmountUsd(BigDecimal.valueOf(100.0));
        deposit1.setPaymentProfileKey(getPaymentProfileCard(cardMaskedNumber, cardExpiration));
        data.crmTbDepositObjects = List.of(deposit1);

        return data;

    }

    private static DataHelper getChargebackTest5Data() {
        DataHelper data = getChargebackRuleData(chargebackRuleClient5);

        //set deposit
        CrmTbDepositEntity deposit1 = generateCrmTbDepositEntityByClient(data.clientHelper);
        deposit1.setAmountUsd(BigDecimal.valueOf(100.0));
        deposit1.setPaymentProfileKey(getPaymentProfileCard(cardMaskedNumber, "2222"));
        data.crmTbDepositObjects = List.of(deposit1);

        return data;

    }

    private static DataHelper getChargebackTest6Data() {
        DataHelper data = getChargebackRuleData(chargebackRuleClient6);

        int wdTypeId = getRandomBytePositive();
        int sourceId = getRandomBytePositive();
        int pcId = getRandomBytePositive();
        String catName = "card cat";

        //set deposits
        CrmTbDepositEntity deposit1 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit2 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit3 = generateCrmTbDepositEntityByClient(data.clientHelper);
        data.crmTbDepositObjects = List.of(deposit1, deposit2, deposit3);
        data.crmTbDepositObjects.forEach(d -> d.setSourceIdSt(sourceId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentTypeId(wdTypeId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentChannelId(pcId));
        data.crmTbDepositObjects.forEach(d -> d.setAmountUsd(BigDecimal.valueOf(1700.01)));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentProfileKey(getPaymentProfileCard(cardMaskedNumber, cardExpiration)));
        CrmTbDepositTypeObject dType = CrmTbDepositTypeObject.builder().id(wdTypeId).sourceIdSt(sourceId).category(2).name(catName).lastUpdated(getCurrentTimestampDbFormat()).build();
        data.crmTbDepositTypeObjects = List.of(dType);
        CrmTbDepositChannelObject dChannel = CrmTbDepositChannelObject.builder().id(pcId).sourceIdSt(sourceId).channelId(pcId).typeId(wdTypeId).name(catName).isMobileChannel(0).lastUpdated(getCurrentTimestampDbFormat()).build();
        data.crmTbDepositChannelObjects = List.of(dChannel);

        //set ticks
        RatesUsdCurrentObject tick = RatesUsdCurrentObject.builder().ts(getCurrentTimestampDbFormat()).currency(baseCurrency).rate(1.1).build();
        data.ratesUsdCurrentObjects = List.of(tick);

        //set client cards
        ClientCardsObject clientCard1 = generateClientCardsObject(data.clientHelper);
        ClientCardsObject clientCard2 = generateClientCardsObject(data.clientHelper);
        ClientCardsObject clientCard3 = generateClientCardsObject(data.clientHelper);
        data.clientCards = List.of(clientCard1, clientCard2, clientCard3);

        return data;

    }

    private static DataHelper getChargebackTest7Data() {
        DataHelper data = getChargebackRuleData(chargebackRuleClient7);

        int wdTypeId = getRandomBytePositive();
        int sourceId = getRandomBytePositive();
        int pcId = getRandomBytePositive();
        String catName = "card cat";

        //set deposits
        CrmTbDepositEntity deposit1 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit2 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit3 = generateCrmTbDepositEntityByClient(data.clientHelper);
        data.crmTbDepositObjects = List.of(deposit1, deposit2, deposit3);
        data.crmTbDepositObjects.forEach(d -> d.setSourceIdSt(sourceId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentTypeId(wdTypeId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentChannelId(pcId));
        data.crmTbDepositObjects.forEach(d -> d.setAmountUsd(BigDecimal.valueOf(1000.01)));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentProfileKey(getPaymentProfileCard(cardMaskedNumber, cardExpiration)));
        CrmTbDepositTypeObject dType = CrmTbDepositTypeObject.builder().id(wdTypeId).sourceIdSt(sourceId).category(2).name(catName).lastUpdated(getCurrentTimestampDbFormat()).build();
        data.crmTbDepositTypeObjects = List.of(dType);
        CrmTbDepositChannelObject dChannel = CrmTbDepositChannelObject.builder().id(pcId).sourceIdSt(sourceId).channelId(pcId).typeId(wdTypeId).name(catName).isMobileChannel(0).lastUpdated(getCurrentTimestampDbFormat()).build();
        data.crmTbDepositChannelObjects = List.of(dChannel);

        //set ticks
        RatesUsdCurrentObject tick = RatesUsdCurrentObject.builder().ts(getCurrentTimestampDbFormat()).currency(baseCurrency).rate(1.1).build();
        data.ratesUsdCurrentObjects = List.of(tick);

        //set client cards
        ClientCardsObject clientCard1 = generateClientCardsObject(data.clientHelper);
        ClientCardsObject clientCard2 = generateClientCardsObject(data.clientHelper);
        ClientCardsObject clientCard3 = generateClientCardsObject(data.clientHelper);
        ClientCardsObject clientCard4 = generateClientCardsObject(data.clientHelper);

        data.clientCards = List.of(clientCard1, clientCard2, clientCard3, clientCard4);

        return data;

    }

    private static DataHelper getChargebackTest8Data() {
        DataHelper data = getChargebackRuleData(chargebackRuleClient8);

        int wdTypeId = getRandomBytePositive();
        int sourceId = getRandomBytePositive();
        int pcId = getRandomBytePositive();
        String catName = "card cat";

        //set deposits
        CrmTbDepositEntity deposit1 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit2 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit3 = generateCrmTbDepositEntityByClient(data.clientHelper);
        data.crmTbDepositObjects = List.of(deposit1, deposit2, deposit3);
        data.crmTbDepositObjects.forEach(d -> d.setSourceIdSt(sourceId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentTypeId(wdTypeId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentChannelId(pcId));
        data.crmTbDepositObjects.forEach(d -> d.setAmountUsd(BigDecimal.valueOf(1000.01)));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentProfileKey(getPaymentProfileCard(cardMaskedNumber, cardExpiration)));
        CrmTbDepositTypeObject dType = CrmTbDepositTypeObject.builder().id(wdTypeId).sourceIdSt(sourceId).category(2).name(catName).lastUpdated(getCurrentTimestampDbFormat()).build();
        data.crmTbDepositTypeObjects = List.of(dType);
        CrmTbDepositChannelObject dChannel = CrmTbDepositChannelObject.builder().id(pcId).sourceIdSt(sourceId).channelId(pcId).typeId(wdTypeId).name(catName).isMobileChannel(0).lastUpdated(getCurrentTimestampDbFormat()).build();
        data.crmTbDepositChannelObjects = List.of(dChannel);

        //set ticks
        RatesUsdCurrentObject tick = RatesUsdCurrentObject.builder().ts(getCurrentTimestampDbFormat()).currency(baseCurrency).rate(1.1).build();
        data.ratesUsdCurrentObjects = List.of(tick);

        //set client connections


        ClientHelper connectedClient = getRandomVantageClientAllFields();
        ConnectionTableEntry connection1 = getConnection(data.clientHelper, connectedClient);
        connection1.connectionScore = 0.8;
        ConnectionTableEntry.ConnectionInfo connectionInfo1 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo1.connectionAttributeName = "name+dateofbirth";
        connectionInfo1.connectionAttributeValue = "test";
        connectionInfo1.sourceAttributeValue = "test";
        connectionInfo1.relationType = "exact";
        connection1.connectionInfo = ConnectionTableEntry.ConnectionInfo.connectionInfoToString(List.of(connectionInfo1));
        ClientHelper connectedClient2 = getRandomVantageClientAllFields();
        ConnectionTableEntry connection2 = getConnection(data.clientHelper, connectedClient2);
        connection2.connectionScore = 0.8;
        ConnectionTableEntry.ConnectionInfo connectionInfo2 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo2.connectionAttributeName = "document";
        connectionInfo2.connectionAttributeValue = "test";
        connectionInfo2.sourceAttributeValue = "test";
        connectionInfo2.relationType = "exact";
        connection2.connectionInfo = ConnectionTableEntry.ConnectionInfo.connectionInfoToString(List.of(connectionInfo2));
        data.connections = List.of(connection1, connection2);
        CrmTbUserObject connectedUserCrmTbUserObject1 = generateUserByClient(connectedClient);
        CrmTbUserObject connectedUserCrmTbUserObject2 = generateUserByClient(connectedClient2);
        data.connectedUsers = List.of(connectedUserCrmTbUserObject1, connectedUserCrmTbUserObject2);

        //set client cards
        ClientCardsObject clientCard1 = generateClientCardsObject(data.clientHelper);
        ClientCardsObject clientCard2 = generateClientCardsObject(data.clientHelper);
        ClientCardsObject clientCard3 = generateClientCardsObject(connectedClient);
        ClientCardsObject clientCard4 = generateClientCardsObject(connectedClient2);
        data.clientCards = List.of(clientCard1, clientCard2, clientCard3, clientCard4);

        return data;
    }

    private static DataHelper getChargebackTest9Data() {
        DataHelper data = getChargebackRuleData(chargebackRuleClient9);

        int wdTypeId = getRandomBytePositive();
        int sourceId = getRandomBytePositive();
        int pcId = getRandomBytePositive();
        String catName = "card cat";

        //set deposits
        CrmTbDepositEntity deposit1 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit2 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit3 = generateCrmTbDepositEntityByClient(data.clientHelper);
        data.crmTbDepositObjects = List.of(deposit1, deposit2, deposit3);
        data.crmTbDepositObjects.forEach(d -> d.setSourceIdSt(sourceId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentTypeId(wdTypeId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentChannelId(pcId));
        data.crmTbDepositObjects.forEach(d -> d.setAmountUsd(BigDecimal.valueOf(1000.01)));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentProfileKey(getPaymentProfileCard(cardMaskedNumber, cardExpiration)));
        CrmTbDepositTypeObject dType = CrmTbDepositTypeObject.builder().id(wdTypeId).sourceIdSt(sourceId).category(2).name(catName).lastUpdated(getCurrentTimestampDbFormat()).build();
        data.crmTbDepositTypeObjects = List.of(dType);
        CrmTbDepositChannelObject dChannel = CrmTbDepositChannelObject.builder().id(pcId).sourceIdSt(sourceId).channelId(pcId).typeId(wdTypeId).name(catName).isMobileChannel(0).lastUpdated(getCurrentTimestampDbFormat()).build();
        data.crmTbDepositChannelObjects = List.of(dChannel);

        //set ticks
        RatesUsdCurrentObject tick = RatesUsdCurrentObject.builder().ts(getCurrentTimestampDbFormat()).currency(baseCurrency).rate(1.1).build();
        data.ratesUsdCurrentObjects = List.of(tick);

        //set client connections
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        ConnectionTableEntry connection1 = getConnection(data.clientHelper, connectedClient);
        connection1.connectionScore = 0.8;
        ConnectionTableEntry.ConnectionInfo connectionInfo1 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo1.connectionAttributeName = "name+dateofbirth";
        connectionInfo1.connectionAttributeValue = "test";
        connectionInfo1.sourceAttributeValue = "test";
        connectionInfo1.relationType = "exact";
        connection1.connectionInfo = ConnectionTableEntry.ConnectionInfo.connectionInfoToString(List.of(connectionInfo1));
        ClientHelper connectedClient2 = getRandomVantageClientAllFields();
        ConnectionTableEntry connection2 = getConnection(data.clientHelper, connectedClient2);
        connection2.connectionScore = 0.8;
        ConnectionTableEntry.ConnectionInfo connectionInfo2 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo2.connectionAttributeName = "document";
        connectionInfo2.connectionAttributeValue = "test";
        connectionInfo2.sourceAttributeValue = "test";
        connectionInfo2.relationType = "exact";
        connection2.connectionInfo = ConnectionTableEntry.ConnectionInfo.connectionInfoToString(List.of(connectionInfo2));
        data.connections = List.of(connection1, connection2);
        ClientHelper connectedClient3 = getRandomVantageClientAllFields();
        ClientHelper connectedClient4 = getRandomVantageClientAllFields();
        ClientHelper connectedClient5 = getRandomVantageClientAllFields();
        CrmTbUserObject connectedUserCrmTbUserObject1 = generateUserByClient(connectedClient);
        CrmTbUserObject connectedUserCrmTbUserObject2 = generateUserByClient(connectedClient2);
        CrmTbUserObject connectedUserCrmTbUserObject3 = generateUserByClient(connectedClient3);
        CrmTbUserObject connectedUserCrmTbUserObject4 = generateUserByClient(connectedClient4);
        CrmTbUserObject connectedUserCrmTbUserObject5 = generateUserByClient(connectedClient5);
        data.connectedUsers = List.of(connectedUserCrmTbUserObject1, connectedUserCrmTbUserObject2, connectedUserCrmTbUserObject3, connectedUserCrmTbUserObject4, connectedUserCrmTbUserObject5);

        //set client cards
        ClientCardsObject clientCard1 = generateClientCardsObject(data.clientHelper);
        ClientCardsObject clientCard2 = generateClientCardsObject(data.clientHelper);
        ClientCardsObject clientCard3 = generateClientCardsObject(connectedClient);
        ClientCardsObject clientCard4 = generateClientCardsObject(connectedClient2);
        ClientCardsObject clientCard5 = generateClientCardsObject(connectedClient3);
        clientCard5.setCardBeginSixDigits(cardFirstSixDigits);
        clientCard5.setCardLastFourDigits(cardLastFourDigits);
        ClientCardsObject clientCard6 = generateClientCardsObject(connectedClient4);
        clientCard6.setCardBeginSixDigits(cardFirstSixDigits);
        clientCard6.setCardLastFourDigits(cardLastFourDigits);
        ClientCardsObject clientCard7 = generateClientCardsObject(connectedClient5);
        clientCard7.setCardBeginSixDigits(cardFirstSixDigits);
        clientCard7.setCardLastFourDigits(cardLastFourDigits);
        data.clientCards = List.of(clientCard1, clientCard2, clientCard3, clientCard4, clientCard5, clientCard6, clientCard7);
        data.clientCards.forEach(c -> c.setExpiryMonth((short) 10));
        data.clientCards.forEach(c -> c.setExpiryYear(2036));

        return data;
    }

    private static DataHelper getChargebackTest10Data() {
        DataHelper data = getChargebackRuleData(chargebackRuleClient10);

        int wdTypeId = getRandomBytePositive();
        int sourceId = getRandomBytePositive();
        int pcId = getRandomBytePositive();
        String catName = "card cat";

        //set deposits
        CrmTbDepositEntity deposit1 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit2 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit3 = generateCrmTbDepositEntityByClient(data.clientHelper);
        data.crmTbDepositObjects = List.of(deposit1, deposit2, deposit3);
        data.crmTbDepositObjects.forEach(d -> d.setSourceIdSt(sourceId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentTypeId(wdTypeId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentChannelId(pcId));
        data.crmTbDepositObjects.forEach(d -> d.setAmountUsd(BigDecimal.valueOf(1000.01)));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentProfileKey(getPaymentProfileCard(cardMaskedNumber, cardExpiration)));
        CrmTbDepositTypeObject dType = CrmTbDepositTypeObject.builder().id(wdTypeId).sourceIdSt(sourceId).category(2).name(catName).lastUpdated(getCurrentTimestampDbFormat()).build();
        data.crmTbDepositTypeObjects = List.of(dType);
        CrmTbDepositChannelObject dChannel = CrmTbDepositChannelObject.builder().id(pcId).sourceIdSt(sourceId).channelId(pcId).typeId(wdTypeId).name(catName).isMobileChannel(0).lastUpdated(getCurrentTimestampDbFormat()).build();
        data.crmTbDepositChannelObjects = List.of(dChannel);

        //set ticks
        RatesUsdCurrentObject tick = RatesUsdCurrentObject.builder().ts(getCurrentTimestampDbFormat()).currency(baseCurrency).rate(1.1).build();
        data.ratesUsdCurrentObjects = List.of(tick);

        //set client connections
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        ConnectionTableEntry connection1 = getConnection(data.clientHelper, connectedClient);
        connection1.connectionScore = 0.8;
        ConnectionTableEntry.ConnectionInfo connectionInfo1 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo1.connectionAttributeName = "name+dateofbirth";
        connectionInfo1.connectionAttributeValue = "test";
        connectionInfo1.sourceAttributeValue = "test";
        connectionInfo1.relationType = "exact";
        connection1.connectionInfo = ConnectionTableEntry.ConnectionInfo.connectionInfoToString(List.of(connectionInfo1));
        ClientHelper connectedClient2 = getRandomVantageClientAllFields();
        ConnectionTableEntry connection2 = getConnection(data.clientHelper, connectedClient2);
        connection2.connectionScore = 0.8;
        ConnectionTableEntry.ConnectionInfo connectionInfo2 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo2.connectionAttributeName = "document";
        connectionInfo2.connectionAttributeValue = "test";
        connectionInfo2.sourceAttributeValue = "test";
        connectionInfo2.relationType = "exact";
        connection2.connectionInfo = ConnectionTableEntry.ConnectionInfo.connectionInfoToString(List.of(connectionInfo2));
        data.connections = List.of(connection1, connection2);
        ClientHelper connectedClient3 = getRandomVantageClientAllFields();
        ClientHelper connectedClient4 = getRandomVantageClientAllFields();
        ClientHelper connectedClient5 = getRandomVantageClientAllFields();
        CrmTbUserObject connectedUserCrmTbUserObject1 = generateUserByClient(connectedClient);
        CrmTbUserObject connectedUserCrmTbUserObject2 = generateUserByClient(connectedClient2);
        CrmTbUserObject connectedUserCrmTbUserObject3 = generateUserByClient(connectedClient3);
        CrmTbUserObject connectedUserCrmTbUserObject4 = generateUserByClient(connectedClient4);
        CrmTbUserObject connectedUserCrmTbUserObject5 = generateUserByClient(connectedClient5);
        data.connectedUsers = List.of(connectedUserCrmTbUserObject1, connectedUserCrmTbUserObject2, connectedUserCrmTbUserObject3, connectedUserCrmTbUserObject4, connectedUserCrmTbUserObject5);

        //set client cards
        ClientCardsObject clientCard1 = generateClientCardsObject(data.clientHelper);
        ClientCardsObject clientCard2 = generateClientCardsObject(data.clientHelper);
        ClientCardsObject clientCard3 = generateClientCardsObject(connectedClient);
        ClientCardsObject clientCard4 = generateClientCardsObject(connectedClient2);
        ClientCardsObject clientCard5 = generateClientCardsObject(connectedClient3);
        clientCard5.setCardBeginSixDigits(cardFirstSixDigits);
        clientCard5.setCardLastFourDigits(cardLastFourDigits);
        ClientCardsObject clientCard6 = generateClientCardsObject(connectedClient4);
        clientCard6.setCardBeginSixDigits(cardFirstSixDigits);
        clientCard6.setCardLastFourDigits(cardLastFourDigits);
        ClientCardsObject clientCard7 = generateClientCardsObject(connectedClient5);
        clientCard7.setCardBeginSixDigits(cardFirstSixDigits);
        clientCard7.setCardLastFourDigits(cardLastFourDigits);
        data.clientCards = List.of(clientCard1, clientCard2, clientCard3, clientCard4, clientCard5, clientCard6, clientCard7);
        data.clientCards.forEach(c -> c.setExpiryMonth((short) 10));
        data.clientCards.forEach(c -> c.setExpiryYear(2036));

        CrmBpCallbacksObject callback1 = generateCrmBpCallbacksObject(data.clientHelper);
        CrmBpCallbacksObject callback2 = generateCrmBpCallbacksObject(data.clientHelper);
        CrmBpCallbacksObject callback3 = generateCrmBpCallbacksObject(data.clientHelper);

        data.callbacksObjects = List.of(callback1, callback2, callback3);

        return data;
    }

    private static DataHelper getChargebackTest11Data() {
        DataHelper data = getChargebackRuleData(chargebackRuleClient11);

        int wdTypeId = getRandomBytePositive();
        int sourceId = getRandomBytePositive();
        int pcId = getRandomBytePositive();
        String catName = "card cat";

        //set deposits
        CrmTbDepositEntity deposit1 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit2 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit3 = generateCrmTbDepositEntityByClient(data.clientHelper);
        data.crmTbDepositObjects = List.of(deposit1, deposit2, deposit3);
        data.crmTbDepositObjects.forEach(d -> d.setSourceIdSt(sourceId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentTypeId(wdTypeId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentChannelId(pcId));
        data.crmTbDepositObjects.forEach(d -> d.setAmountUsd(BigDecimal.valueOf(1000.01)));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentProfileKey(getPaymentProfileCard(cardMaskedNumber, cardExpiration)));
        CrmTbDepositTypeObject dType = CrmTbDepositTypeObject.builder().id(wdTypeId).sourceIdSt(sourceId).category(2).name(catName).lastUpdated(getCurrentTimestampDbFormat()).build();
        data.crmTbDepositTypeObjects = List.of(dType);
        CrmTbDepositChannelObject dChannel = CrmTbDepositChannelObject.builder().id(pcId).sourceIdSt(sourceId).channelId(pcId).typeId(wdTypeId).name(catName).isMobileChannel(0).lastUpdated(getCurrentTimestampDbFormat()).build();
        data.crmTbDepositChannelObjects = List.of(dChannel);

        //set ticks
        RatesUsdCurrentObject tick = RatesUsdCurrentObject.builder().ts(getCurrentTimestampDbFormat()).currency(baseCurrency).rate(1.1).build();
        data.ratesUsdCurrentObjects = List.of(tick);

        //set client connections
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        ConnectionTableEntry connection1 = getConnection(data.clientHelper, connectedClient);
        connection1.connectionScore = 0.8;
        ConnectionTableEntry.ConnectionInfo connectionInfo1 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo1.connectionAttributeName = "name+dateofbirth";
        connectionInfo1.connectionAttributeValue = "test";
        connectionInfo1.sourceAttributeValue = "test";
        connectionInfo1.relationType = "exact";
        connection1.connectionInfo = ConnectionTableEntry.ConnectionInfo.connectionInfoToString(List.of(connectionInfo1));
        ClientHelper connectedClient2 = getRandomVantageClientAllFields();
        ConnectionTableEntry connection2 = getConnection(data.clientHelper, connectedClient2);
        connection2.connectionScore = 0.8;
        ConnectionTableEntry.ConnectionInfo connectionInfo2 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo2.connectionAttributeName = "document";
        connectionInfo2.connectionAttributeValue = "test";
        connectionInfo2.sourceAttributeValue = "test";
        connectionInfo2.relationType = "exact";
        connection2.connectionInfo = ConnectionTableEntry.ConnectionInfo.connectionInfoToString(List.of(connectionInfo2));
        data.connections = List.of(connection1, connection2);
        ClientHelper connectedClient3 = getRandomVantageClientAllFields();
        ClientHelper connectedClient4 = getRandomVantageClientAllFields();
        ClientHelper connectedClient5 = getRandomVantageClientAllFields();
        CrmTbUserObject connectedUserCrmTbUserObject1 = generateUserByClient(connectedClient);
        CrmTbUserObject connectedUserCrmTbUserObject2 = generateUserByClient(connectedClient2);
        CrmTbUserObject connectedUserCrmTbUserObject3 = generateUserByClient(connectedClient3);
        CrmTbUserObject connectedUserCrmTbUserObject4 = generateUserByClient(connectedClient4);
        CrmTbUserObject connectedUserCrmTbUserObject5 = generateUserByClient(connectedClient5);
        data.connectedUsers = List.of(connectedUserCrmTbUserObject1, connectedUserCrmTbUserObject2, connectedUserCrmTbUserObject3, connectedUserCrmTbUserObject4, connectedUserCrmTbUserObject5);

        //set client cards
        ClientCardsObject clientCard1 = generateClientCardsObject(data.clientHelper);
        ClientCardsObject clientCard2 = generateClientCardsObject(data.clientHelper);
        ClientCardsObject clientCard3 = generateClientCardsObject(connectedClient);
        ClientCardsObject clientCard4 = generateClientCardsObject(connectedClient2);
        ClientCardsObject clientCard5 = generateClientCardsObject(connectedClient3);
        clientCard5.setCardBeginSixDigits(cardFirstSixDigits);
        clientCard5.setCardLastFourDigits(cardLastFourDigits);
        ClientCardsObject clientCard6 = generateClientCardsObject(connectedClient4);
        clientCard6.setCardBeginSixDigits(cardFirstSixDigits);
        clientCard6.setCardLastFourDigits(cardLastFourDigits);
        ClientCardsObject clientCard7 = generateClientCardsObject(connectedClient5);
        clientCard7.setCardBeginSixDigits(cardFirstSixDigits);
        clientCard7.setCardLastFourDigits(cardLastFourDigits);
        data.clientCards = List.of(clientCard1, clientCard2, clientCard3, clientCard4, clientCard5, clientCard6, clientCard7);
        data.clientCards.forEach(c -> c.setExpiryMonth((short) 10));
        data.clientCards.forEach(c -> c.setExpiryYear(2036));

        CrmBpCallbacksObject callback1 = generateCrmBpCallbacksObject(data.clientHelper);
        CrmBpCallbacksObject callback2 = generateCrmBpCallbacksObject(data.clientHelper);
        CrmBpCallbacksObject callback3 = generateCrmBpCallbacksObject(data.clientHelper);

        data.callbacksObjects = List.of(callback1, callback2, callback3);
        data.callbacksObjects.forEach(c -> c.setPaymentProfileKey(basePaymentProfile));
        data.callbacksObjects.forEach(c -> c.setStatus("declined"));

        return data;
    }

    private static DataHelper getChargebackTest12Data() {
        DataHelper data = getChargebackRuleData(chargebackRuleClient12);

        int wdTypeId = getRandomBytePositive();
        int sourceId = getRandomBytePositive();
        int pcId = getRandomBytePositive();
        String catName = "card cat";

        //set deposits
        CrmTbDepositEntity deposit1 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit2 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit3 = generateCrmTbDepositEntityByClient(data.clientHelper);
        data.crmTbDepositObjects = List.of(deposit1, deposit2, deposit3);
        data.crmTbDepositObjects.forEach(d -> d.setSourceIdSt(sourceId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentTypeId(wdTypeId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentChannelId(pcId));
        data.crmTbDepositObjects.forEach(d -> d.setAmountUsd(BigDecimal.valueOf(1000.01)));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentProfileKey(getPaymentProfileCard(cardMaskedNumber, cardExpiration)));
        CrmTbDepositTypeObject dType = CrmTbDepositTypeObject.builder().id(wdTypeId).sourceIdSt(sourceId).category(2).name(catName).lastUpdated(getCurrentTimestampDbFormat()).build();
        data.crmTbDepositTypeObjects = List.of(dType);
        CrmTbDepositChannelObject dChannel = CrmTbDepositChannelObject.builder().id(pcId).sourceIdSt(sourceId).channelId(pcId).typeId(wdTypeId).name(catName).isMobileChannel(0).lastUpdated(getCurrentTimestampDbFormat()).build();
        data.crmTbDepositChannelObjects = List.of(dChannel);

        //set ticks
        RatesUsdCurrentObject tick = RatesUsdCurrentObject.builder().ts(getCurrentTimestampDbFormat()).currency(baseCurrency).rate(1.1).build();
        data.ratesUsdCurrentObjects = List.of(tick);

        //set client connections
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        ConnectionTableEntry connection1 = getConnection(data.clientHelper, connectedClient);
        connection1.connectionScore = 0.8;
        ConnectionTableEntry.ConnectionInfo connectionInfo1 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo1.connectionAttributeName = "name+dateofbirth";
        connectionInfo1.connectionAttributeValue = "test";
        connectionInfo1.sourceAttributeValue = "test";
        connectionInfo1.relationType = "exact";
        connection1.connectionInfo = ConnectionTableEntry.ConnectionInfo.connectionInfoToString(List.of(connectionInfo1));
        ClientHelper connectedClient2 = getRandomVantageClientAllFields();
        ConnectionTableEntry connection2 = getConnection(data.clientHelper, connectedClient2);
        connection2.connectionScore = 0.8;
        ConnectionTableEntry.ConnectionInfo connectionInfo2 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo2.connectionAttributeName = "document";
        connectionInfo2.connectionAttributeValue = "test";
        connectionInfo2.sourceAttributeValue = "test";
        connectionInfo2.relationType = "exact";
        connection2.connectionInfo = ConnectionTableEntry.ConnectionInfo.connectionInfoToString(List.of(connectionInfo2));
        data.connections = List.of(connection1, connection2);
        ClientHelper connectedClient3 = getRandomVantageClientAllFields();
        ClientHelper connectedClient4 = getRandomVantageClientAllFields();
        ClientHelper connectedClient5 = getRandomVantageClientAllFields();
        CrmTbUserObject connectedUserCrmTbUserObject1 = generateUserByClient(connectedClient);
        CrmTbUserObject connectedUserCrmTbUserObject2 = generateUserByClient(connectedClient2);
        CrmTbUserObject connectedUserCrmTbUserObject3 = generateUserByClient(connectedClient3);
        CrmTbUserObject connectedUserCrmTbUserObject4 = generateUserByClient(connectedClient4);
        CrmTbUserObject connectedUserCrmTbUserObject5 = generateUserByClient(connectedClient5);
        data.connectedUsers = List.of(connectedUserCrmTbUserObject1, connectedUserCrmTbUserObject2, connectedUserCrmTbUserObject3, connectedUserCrmTbUserObject4, connectedUserCrmTbUserObject5);

        //set client cards
        ClientCardsObject clientCard1 = generateClientCardsObject(data.clientHelper);
        ClientCardsObject clientCard2 = generateClientCardsObject(data.clientHelper);
        ClientCardsObject clientCard3 = generateClientCardsObject(connectedClient);
        ClientCardsObject clientCard4 = generateClientCardsObject(connectedClient2);
        ClientCardsObject clientCard5 = generateClientCardsObject(connectedClient3);
        clientCard5.setCardBeginSixDigits(cardFirstSixDigits);
        clientCard5.setCardLastFourDigits(cardLastFourDigits);
        ClientCardsObject clientCard6 = generateClientCardsObject(connectedClient4);
        clientCard6.setCardBeginSixDigits(cardFirstSixDigits);
        clientCard6.setCardLastFourDigits(cardLastFourDigits);
        ClientCardsObject clientCard7 = generateClientCardsObject(connectedClient5);
        clientCard7.setCardBeginSixDigits(cardFirstSixDigits);
        clientCard7.setCardLastFourDigits(cardLastFourDigits);
        data.clientCards = List.of(clientCard1, clientCard2, clientCard3, clientCard4, clientCard5, clientCard6, clientCard7);
        data.clientCards.forEach(c -> c.setExpiryMonth((short) 10));
        data.clientCards.forEach(c -> c.setExpiryYear(2036));

        CrmBpCallbacksObject callback1 = generateCrmBpCallbacksObject(data.clientHelper);
        CrmBpCallbacksObject callback2 = generateCrmBpCallbacksObject(data.clientHelper);
        CrmBpCallbacksObject callback3 = generateCrmBpCallbacksObject(data.clientHelper);
        callback3.setIsFraudDeclined((short) 1);
        callback3.setBusinessOrderId(callbackEvent.getBusinessOrderId());

        data.callbacksObjects = List.of(callback1, callback2, callback3);
        data.callbacksObjects.forEach(c -> c.setPaymentProfileKey(basePaymentProfile));
        data.callbacksObjects.forEach(c -> c.setStatus("declined"));

        return data;
    }

    private static DataHelper getChargebackTest13Data() {
        DataHelper data = getChargebackRuleData(chargebackRuleClient13);

        int wdTypeId = getRandomBytePositive();
        int sourceId = getRandomBytePositive();
        int pcId = getRandomBytePositive();
        String catName = "card cat";

        //set deposits
        CrmTbDepositEntity deposit1 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit2 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit3 = generateCrmTbDepositEntityByClient(data.clientHelper);
        data.crmTbDepositObjects = List.of(deposit1, deposit2, deposit3);
        data.crmTbDepositObjects.forEach(d -> d.setSourceIdSt(sourceId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentTypeId(wdTypeId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentChannelId(pcId));
        data.crmTbDepositObjects.forEach(d -> d.setAmountUsd(BigDecimal.valueOf(1000.01)));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentProfileKey(getPaymentProfileCard(cardMaskedNumber, cardExpiration)));
        CrmTbDepositTypeObject dType = CrmTbDepositTypeObject.builder().id(wdTypeId).sourceIdSt(sourceId).category(2).name(catName).lastUpdated(getCurrentTimestampDbFormat()).build();
        data.crmTbDepositTypeObjects = List.of(dType);
        CrmTbDepositChannelObject dChannel = CrmTbDepositChannelObject.builder().id(pcId).sourceIdSt(sourceId).channelId(pcId).typeId(wdTypeId).name(catName).isMobileChannel(0).lastUpdated(getCurrentTimestampDbFormat()).build();
        data.crmTbDepositChannelObjects = List.of(dChannel);

        //set ticks
        RatesUsdCurrentObject tick = RatesUsdCurrentObject.builder().ts(getCurrentTimestampDbFormat()).currency(baseCurrency).rate(1.1).build();
        data.ratesUsdCurrentObjects = List.of(tick);

        //set client connections
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        ConnectionTableEntry connection1 = getConnection(data.clientHelper, connectedClient);
        connection1.connectionScore = 0.8;
        ConnectionTableEntry.ConnectionInfo connectionInfo1 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo1.connectionAttributeName = "name+dateofbirth";
        connectionInfo1.connectionAttributeValue = "test";
        connectionInfo1.sourceAttributeValue = "test";
        connectionInfo1.relationType = "exact";
        connection1.connectionInfo = ConnectionTableEntry.ConnectionInfo.connectionInfoToString(List.of(connectionInfo1));
        ClientHelper connectedClient2 = getRandomVantageClientAllFields();
        ConnectionTableEntry connection2 = getConnection(data.clientHelper, connectedClient2);
        connection2.connectionScore = 0.8;
        ConnectionTableEntry.ConnectionInfo connectionInfo2 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo2.connectionAttributeName = "document";
        connectionInfo2.connectionAttributeValue = "test";
        connectionInfo2.sourceAttributeValue = "test";
        connectionInfo2.relationType = "exact";
        connection2.connectionInfo = ConnectionTableEntry.ConnectionInfo.connectionInfoToString(List.of(connectionInfo2));
        data.connections = List.of(connection1, connection2);
        ClientHelper connectedClient3 = getRandomVantageClientAllFields();
        ClientHelper connectedClient4 = getRandomVantageClientAllFields();
        ClientHelper connectedClient5 = getRandomVantageClientAllFields();
        CrmTbUserObject connectedUserCrmTbUserObject1 = generateUserByClient(connectedClient);
        CrmTbUserObject connectedUserCrmTbUserObject2 = generateUserByClient(connectedClient2);
        CrmTbUserObject connectedUserCrmTbUserObject3 = generateUserByClient(connectedClient3);
        CrmTbUserObject connectedUserCrmTbUserObject4 = generateUserByClient(connectedClient4);
        CrmTbUserObject connectedUserCrmTbUserObject5 = generateUserByClient(connectedClient5);
        data.connectedUsers = List.of(connectedUserCrmTbUserObject1, connectedUserCrmTbUserObject2, connectedUserCrmTbUserObject3, connectedUserCrmTbUserObject4, connectedUserCrmTbUserObject5);

        //set client cards
        ClientCardsObject clientCard1 = generateClientCardsObject(data.clientHelper);
        ClientCardsObject clientCard2 = generateClientCardsObject(data.clientHelper);
        ClientCardsObject clientCard3 = generateClientCardsObject(connectedClient);
        ClientCardsObject clientCard4 = generateClientCardsObject(connectedClient2);
        ClientCardsObject clientCard5 = generateClientCardsObject(connectedClient3);
        clientCard5.setCardBeginSixDigits(cardFirstSixDigits);
        clientCard5.setCardLastFourDigits(cardLastFourDigits);
        ClientCardsObject clientCard6 = generateClientCardsObject(connectedClient4);
        clientCard6.setCardBeginSixDigits(cardFirstSixDigits);
        clientCard6.setCardLastFourDigits(cardLastFourDigits);
        ClientCardsObject clientCard7 = generateClientCardsObject(connectedClient5);
        clientCard7.setCardBeginSixDigits(cardFirstSixDigits);
        clientCard7.setCardLastFourDigits(cardLastFourDigits);
        data.clientCards = List.of(clientCard1, clientCard2, clientCard3, clientCard4, clientCard5, clientCard6, clientCard7);
        data.clientCards.forEach(c -> c.setExpiryMonth((short) 10));
        data.clientCards.forEach(c -> c.setExpiryYear(2036));

        CrmBpCallbacksObject callback1 = generateCrmBpCallbacksObject(data.clientHelper);
        CrmBpCallbacksObject callback2 = generateCrmBpCallbacksObject(data.clientHelper);
        CrmBpCallbacksObject callback3 = generateCrmBpCallbacksObject(data.clientHelper);
        callback3.setIsFraudDeclined((short) 1);
        callback3.setBusinessOrderId(callbackEvent.getBusinessOrderId());

        data.callbacksObjects = List.of(callback1, callback2, callback3);
        data.callbacksObjects.forEach(c -> c.setPaymentProfileKey(basePaymentProfile));
        data.callbacksObjects.forEach(c -> c.setStatus("declined"));

        attributes.setCardHolderName(data.clientHelper.getFirstName() + " " + data.clientHelper.getLastName());
        buildCallbackEvent();
        data.callbackEvent = callbackEvent;

        return data;
    }

    private static DataHelper getChargebackTest14Data() {
        DataHelper data = getChargebackRuleData(chargebackRuleClient14);

        int wdTypeId = getRandomBytePositive();
        int sourceId = getRandomBytePositive();
        int pcId = getRandomBytePositive();
        String catName = "card cat";

        //set deposits
        CrmTbDepositEntity deposit1 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit2 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit3 = generateCrmTbDepositEntityByClient(data.clientHelper);
        data.crmTbDepositObjects = List.of(deposit1, deposit2, deposit3);
        data.crmTbDepositObjects.forEach(d -> d.setSourceIdSt(sourceId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentTypeId(wdTypeId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentChannelId(pcId));
        data.crmTbDepositObjects.forEach(d -> d.setAmountUsd(BigDecimal.valueOf(1000.01)));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentProfileKey(getPaymentProfileCard(cardMaskedNumber, cardExpiration)));
        CrmTbDepositTypeObject dType = CrmTbDepositTypeObject.builder().id(wdTypeId).sourceIdSt(sourceId).category(2).name(catName).lastUpdated(getCurrentTimestampDbFormat()).build();
        data.crmTbDepositTypeObjects = List.of(dType);
        CrmTbDepositChannelObject dChannel = CrmTbDepositChannelObject.builder().id(pcId).sourceIdSt(sourceId).channelId(pcId).typeId(wdTypeId).name(catName).isMobileChannel(0).lastUpdated(getCurrentTimestampDbFormat()).build();
        data.crmTbDepositChannelObjects = List.of(dChannel);

        //set ticks
        RatesUsdCurrentObject tick = RatesUsdCurrentObject.builder().ts(getCurrentTimestampDbFormat()).currency(baseCurrency).rate(1.1).build();
        data.ratesUsdCurrentObjects = List.of(tick);

        //set client connections
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        ConnectionTableEntry connection1 = getConnection(data.clientHelper, connectedClient);
        connection1.connectionScore = 0.8;
        ConnectionTableEntry.ConnectionInfo connectionInfo1 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo1.connectionAttributeName = "name+dateofbirth";
        connectionInfo1.connectionAttributeValue = "test";
        connectionInfo1.sourceAttributeValue = "test";
        connectionInfo1.relationType = "exact";
        connection1.connectionInfo = ConnectionTableEntry.ConnectionInfo.connectionInfoToString(List.of(connectionInfo1));
        ClientHelper connectedClient2 = getRandomVantageClientAllFields();
        ConnectionTableEntry connection2 = getConnection(data.clientHelper, connectedClient2);
        connection2.connectionScore = 0.8;
        ConnectionTableEntry.ConnectionInfo connectionInfo2 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo2.connectionAttributeName = "document";
        connectionInfo2.connectionAttributeValue = "test";
        connectionInfo2.sourceAttributeValue = "test";
        connectionInfo2.relationType = "exact";
        connection2.connectionInfo = ConnectionTableEntry.ConnectionInfo.connectionInfoToString(List.of(connectionInfo2));
        data.connections = List.of(connection1, connection2);
        ClientHelper connectedClient3 = getRandomVantageClientAllFields();
        ClientHelper connectedClient4 = getRandomVantageClientAllFields();
        ClientHelper connectedClient5 = getRandomVantageClientAllFields();
        CrmTbUserObject connectedUserCrmTbUserObject1 = generateUserByClient(connectedClient);
        CrmTbUserObject connectedUserCrmTbUserObject2 = generateUserByClient(connectedClient2);
        CrmTbUserObject connectedUserCrmTbUserObject3 = generateUserByClient(connectedClient3);
        CrmTbUserObject connectedUserCrmTbUserObject4 = generateUserByClient(connectedClient4);
        CrmTbUserObject connectedUserCrmTbUserObject5 = generateUserByClient(connectedClient5);
        data.connectedUsers = List.of(connectedUserCrmTbUserObject1, connectedUserCrmTbUserObject2, connectedUserCrmTbUserObject3, connectedUserCrmTbUserObject4, connectedUserCrmTbUserObject5);

        //set client cards
        ClientCardsObject clientCard1 = generateClientCardsObject(data.clientHelper);
        ClientCardsObject clientCard2 = generateClientCardsObject(data.clientHelper);
        ClientCardsObject clientCard3 = generateClientCardsObject(connectedClient);
        ClientCardsObject clientCard4 = generateClientCardsObject(connectedClient2);
        ClientCardsObject clientCard5 = generateClientCardsObject(connectedClient3);
        clientCard5.setCardBeginSixDigits(cardFirstSixDigits);
        clientCard5.setCardLastFourDigits(cardLastFourDigits);
        ClientCardsObject clientCard6 = generateClientCardsObject(connectedClient4);
        clientCard6.setCardBeginSixDigits(cardFirstSixDigits);
        clientCard6.setCardLastFourDigits(cardLastFourDigits);
        ClientCardsObject clientCard7 = generateClientCardsObject(connectedClient5);
        clientCard7.setCardBeginSixDigits(cardFirstSixDigits);
        clientCard7.setCardLastFourDigits(cardLastFourDigits);
        data.clientCards = List.of(clientCard1, clientCard2, clientCard3, clientCard4, clientCard5, clientCard6, clientCard7);
        data.clientCards.forEach(c -> c.setExpiryMonth((short) 10));
        data.clientCards.forEach(c -> c.setExpiryYear(2036));

        CrmBpCallbacksObject callback1 = generateCrmBpCallbacksObject(data.clientHelper);
        CrmBpCallbacksObject callback2 = generateCrmBpCallbacksObject(data.clientHelper);
        CrmBpCallbacksObject callback3 = generateCrmBpCallbacksObject(data.clientHelper);
        callback3.setIsFraudDeclined((short) 1);
        callback3.setBusinessOrderId(callbackEvent.getBusinessOrderId());

        data.callbacksObjects = List.of(callback1, callback2, callback3);
        data.callbacksObjects.forEach(c -> c.setPaymentProfileKey(basePaymentProfile));
        data.callbacksObjects.forEach(c -> c.setStatus("declined"));

        attributes.set3d(true);
        buildCallbackEvent();
        data.callbackEvent = callbackEvent;

        return data;
    }

    private static DataHelper getChargebackTest15Data() {
        DataHelper data = getChargebackRuleData(chargebackRuleClient15);

        //set deposits
        int wdTypeId = getRandomBytePositive();
        int sourceId = getRandomBytePositive();
        int pcId = getRandomBytePositive();
        String catName = "card cat";
        CrmTbDepositEntity deposit1 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit2 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit3 = generateCrmTbDepositEntityByClient(data.clientHelper);
        data.crmTbDepositObjects = List.of(deposit1, deposit2, deposit3);
        data.crmTbDepositObjects.forEach(d -> d.setSourceIdSt(sourceId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentTypeId(wdTypeId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentChannelId(pcId));
        data.crmTbDepositObjects.forEach(d -> d.setAmountUsd(BigDecimal.valueOf(300.01)));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentProfileKey(getPaymentProfileCard(cardMaskedNumber, cardExpiration)));
        CrmTbDepositTypeObject dType = CrmTbDepositTypeObject.builder().id(wdTypeId).sourceIdSt(sourceId).category(2).name(catName).lastUpdated(getCurrentTimestampDbFormat()).build();
        data.crmTbDepositTypeObjects = List.of(dType);
        CrmTbDepositChannelObject dChannel = CrmTbDepositChannelObject.builder().id(pcId).sourceIdSt(sourceId).channelId(pcId).typeId(wdTypeId).name(catName).isMobileChannel(0).lastUpdated(getCurrentTimestampDbFormat()).build();
        data.crmTbDepositChannelObjects = List.of(dChannel);


        //set ticks
        RatesUsdCurrentObject tick = RatesUsdCurrentObject.builder().ts(getCurrentTimestampDbFormat()).currency(baseCurrency).rate(1.1).build();
        data.ratesUsdCurrentObjects = List.of(tick);

        //set client cards
        ClientCardsObject clientCard1 = generateClientCardsObject(data.clientHelper);
        ClientCardsObject clientCard2 = generateClientCardsObject(data.clientHelper);

        data.clientCards = List.of(clientCard1, clientCard2);
        data.clientCards.forEach(c -> c.setExpiryMonth((short) 10));
        data.clientCards.forEach(c -> c.setExpiryYear(2036));

        return data;
    }

    private static DataHelper getChargebackTest16Data() {
        DataHelper data = getChargebackRuleData(chargebackRuleClient16);

        //set open trades
        Mt5DealsCoercedObject trade1 = generateTradeByClient(data.clientHelper, 0, 0, 0, getRandomLongPositive());
        trade1.setPositionId(getRandomLongPositive());
        trade1.setEntry(0);
        trade1.setSymbol("EURUSD");
        data.mt5DealsCoercedObjects = List.of(trade1);

        //set ticks
        RatesUsdCurrentObject tick = RatesUsdCurrentObject.builder().ts(getCurrentTimestampDbFormat()).currency(baseCurrency).rate(1.1).build();
        data.ratesUsdCurrentObjects = List.of(tick);

        //set client cards
        ClientCardsObject clientCard1 = generateClientCardsObject(data.clientHelper);
        ClientCardsObject clientCard2 = generateClientCardsObject(data.clientHelper);

        data.clientCards = List.of(clientCard1, clientCard2);
        data.clientCards.forEach(c -> c.setExpiryMonth((short) 10));
        data.clientCards.forEach(c -> c.setExpiryYear(2036));

        return data;
    }

    private static DataHelper getChargebackTest17Data() {
        DataHelper data = getChargebackRuleData(chargebackRuleClient17);

        //set ticks
        RatesUsdCurrentObject tick = RatesUsdCurrentObject.builder().ts(getCurrentTimestampDbFormat()).currency(baseCurrency).rate(1.1).build();
        data.ratesUsdCurrentObjects = List.of(tick);

        //set client cards
        ClientCardsObject clientCard1 = generateClientCardsObject(data.clientHelper);
        ClientCardsObject clientCard2 = generateClientCardsObject(data.clientHelper);

        data.clientCards = List.of(clientCard1, clientCard2);
        data.clientCards.forEach(c -> c.setExpiryMonth((short) 10));
        data.clientCards.forEach(c -> c.setExpiryYear(2036));

        return data;
    }

    private static DataHelper getChargebackTest18Data() {
        DataHelper data = getChargebackRuleData(chargebackRuleClient18);

        //set ticks
        RatesUsdCurrentObject tick = RatesUsdCurrentObject.builder().ts(getCurrentTimestampDbFormat()).currency(baseCurrency).rate(1.1).build();
        data.ratesUsdCurrentObjects = List.of(tick);

        //set client connections
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        ConnectionTableEntry connection1 = getConnection(data.clientHelper, connectedClient);
        connection1.connectionScore = 0.8;
        ConnectionTableEntry.ConnectionInfo connectionInfo1 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo1.connectionAttributeName = "name+dateofbirth";
        connectionInfo1.connectionAttributeValue = "test";
        connectionInfo1.sourceAttributeValue = "test";
        connectionInfo1.relationType = "exact";
        connection1.connectionInfo = ConnectionTableEntry.ConnectionInfo.connectionInfoToString(List.of(connectionInfo1));
        ClientHelper connectedClient2 = getRandomVantageClientAllFields();
        ConnectionTableEntry connection2 = getConnection(data.clientHelper, connectedClient2);
        connection2.connectionScore = 0.8;
        ConnectionTableEntry.ConnectionInfo connectionInfo2 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo2.connectionAttributeName = "document";
        connectionInfo2.connectionAttributeValue = "test";
        connectionInfo2.sourceAttributeValue = "test";
        connectionInfo2.relationType = "exact";
        connection2.connectionInfo = ConnectionTableEntry.ConnectionInfo.connectionInfoToString(List.of(connectionInfo2));
        data.connections = List.of(connection1, connection2);
        ClientHelper connectedClient3 = getRandomVantageClientAllFields();
        ClientHelper connectedClient4 = getRandomVantageClientAllFields();
        ClientHelper connectedClient5 = getRandomVantageClientAllFields();
        CrmTbUserObject connectedUserCrmTbUserObject1 = generateUserByClient(connectedClient);
        CrmTbUserObject connectedUserCrmTbUserObject2 = generateUserByClient(connectedClient2);
        CrmTbUserObject connectedUserCrmTbUserObject3 = generateUserByClient(connectedClient3);
        CrmTbUserObject connectedUserCrmTbUserObject4 = generateUserByClient(connectedClient4);
        CrmTbUserObject connectedUserCrmTbUserObject5 = generateUserByClient(connectedClient5);
        data.connectedUsers = List.of(connectedUserCrmTbUserObject1, connectedUserCrmTbUserObject2, connectedUserCrmTbUserObject3, connectedUserCrmTbUserObject4, connectedUserCrmTbUserObject5);

        //set client cards
        ClientCardsObject clientCard1 = generateClientCardsObject(data.clientHelper);
        ClientCardsObject clientCard2 = generateClientCardsObject(data.clientHelper);
        ClientCardsObject clientCard3 = generateClientCardsObject(data.clientHelper);
        ClientCardsObject clientCard4 = generateClientCardsObject(data.clientHelper);
        data.clientCards = List.of(clientCard1, clientCard2, clientCard3, clientCard4);

        return data;
    }

    private static DataHelper getChargebackTest19Data() {
        DataHelper data = getChargebackRuleData(chargebackRuleClient19);

        //set ticks
        RatesUsdCurrentObject tick = RatesUsdCurrentObject.builder().ts(getCurrentTimestampDbFormat()).currency(baseCurrency).rate(1.1).build();
        data.ratesUsdCurrentObjects = List.of(tick);

        //set deposits
        int wdTypeId = getRandomBytePositive();
        int sourceId = getRandomBytePositive();
        int pcId = getRandomBytePositive();
        String catName = "card cat";
        CrmTbDepositEntity deposit1 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit2 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit3 = generateCrmTbDepositEntityByClient(data.clientHelper);
        data.crmTbDepositObjects = List.of(deposit1, deposit2, deposit3);
        data.crmTbDepositObjects.forEach(d -> d.setSourceIdSt(sourceId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentTypeId(wdTypeId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentChannelId(pcId));
        data.crmTbDepositObjects.forEach(d -> d.setAmountUsd(BigDecimal.valueOf(300.01)));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentProfileKey(getPaymentProfileCard(cardMaskedNumber, cardExpiration)));
        CrmTbDepositTypeObject dType = CrmTbDepositTypeObject.builder().id(wdTypeId).sourceIdSt(sourceId).category(2).name(catName).lastUpdated(getCurrentTimestampDbFormat()).build();
        data.crmTbDepositTypeObjects = List.of(dType);
        CrmTbDepositChannelObject dChannel = CrmTbDepositChannelObject.builder().id(pcId).sourceIdSt(sourceId).channelId(pcId).typeId(wdTypeId).name(catName).isMobileChannel(0).lastUpdated(getCurrentTimestampDbFormat()).build();
        data.crmTbDepositChannelObjects = List.of(dChannel);

        //set client connections
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        ConnectionTableEntry connection1 = getConnection(data.clientHelper, connectedClient);
        connection1.connectionScore = 0.8;
        ConnectionTableEntry.ConnectionInfo connectionInfo1 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo1.connectionAttributeName = "name+dateofbirth";
        connectionInfo1.connectionAttributeValue = "test";
        connectionInfo1.sourceAttributeValue = "test";
        connectionInfo1.relationType = "exact";
        connection1.connectionInfo = ConnectionTableEntry.ConnectionInfo.connectionInfoToString(List.of(connectionInfo1));
        ClientHelper connectedClient2 = getRandomVantageClientAllFields();
        ConnectionTableEntry connection2 = getConnection(data.clientHelper, connectedClient2);
        connection2.connectionScore = 0.8;
        ConnectionTableEntry.ConnectionInfo connectionInfo2 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo2.connectionAttributeName = "document";
        connectionInfo2.connectionAttributeValue = "test";
        connectionInfo2.sourceAttributeValue = "test";
        connectionInfo2.relationType = "exact";
        connection2.connectionInfo = ConnectionTableEntry.ConnectionInfo.connectionInfoToString(List.of(connectionInfo2));
        ClientHelper connectedClient3 = getRandomVantageClientAllFields();
        ConnectionTableEntry connection3 = getConnection(data.clientHelper, connectedClient3);
        connection3.connectionScore = 0.8;
        ConnectionTableEntry.ConnectionInfo connectionInfo3 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo3.connectionAttributeName = "payout";
        connectionInfo3.connectionAttributeValue = "test";
        connectionInfo3.sourceAttributeValue = "test";
        connectionInfo3.relationType = "exact";
        connection3.connectionInfo = ConnectionTableEntry.ConnectionInfo.connectionInfoToString(List.of(connectionInfo3));
        data.connections = List.of(connection1, connection2, connection3);
        ClientHelper connectedClient4 = getRandomVantageClientAllFields();
        ClientHelper connectedClient5 = getRandomVantageClientAllFields();
        CrmTbUserObject connectedUserCrmTbUserObject1 = generateUserByClient(connectedClient);
        CrmTbUserObject connectedUserCrmTbUserObject2 = generateUserByClient(connectedClient2);
        CrmTbUserObject connectedUserCrmTbUserObject3 = generateUserByClient(connectedClient3);
        CrmTbUserObject connectedUserCrmTbUserObject4 = generateUserByClient(connectedClient4);
        CrmTbUserObject connectedUserCrmTbUserObject5 = generateUserByClient(connectedClient5);
        data.connectedClientHelpers = List.of(connectedClient3);
        data.connectedUsers = List.of(connectedUserCrmTbUserObject1, connectedUserCrmTbUserObject2, connectedUserCrmTbUserObject3, connectedUserCrmTbUserObject4, connectedUserCrmTbUserObject5);

        //set client cards
        ClientCardsObject clientCard1 = generateClientCardsObject(data.clientHelper);
        ClientCardsObject clientCard2 = generateClientCardsObject(data.clientHelper);
        ClientCardsObject clientCard3 = generateClientCardsObject(data.clientHelper);
        ClientCardsObject clientCard4 = generateClientCardsObject(data.clientHelper);
        data.clientCards = List.of(clientCard1, clientCard2, clientCard3, clientCard4);

        //set segment
        SegmentationTableObject segment = SegmentationTableObject.builder().date(getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE, 0, 0, 1, 0, 0)).ucid(data.clientHelper.getUcid()).segment("Medium").build();
        data.segmentObjects = List.of(segment);

        return data;
    }

    private static DataHelper getChargebackTest20Data() {
        DataHelper data = getChargebackRuleData(chargebackRuleClient20);

        //set ticks
        RatesUsdCurrentObject tick = RatesUsdCurrentObject.builder().ts(getCurrentTimestampDbFormat()).currency(baseCurrency).rate(1.1).build();
        data.ratesUsdCurrentObjects = List.of(tick);

        //set deposits
        int wdTypeId = getRandomBytePositive();
        int sourceId = getRandomBytePositive();
        int pcId = getRandomBytePositive();
        String catName = "card cat";
        CrmTbDepositEntity deposit1 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit2 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit3 = generateCrmTbDepositEntityByClient(data.clientHelper);
        data.crmTbDepositObjects = List.of(deposit1, deposit2, deposit3);
        data.crmTbDepositObjects.forEach(d -> d.setSourceIdSt(sourceId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentTypeId(wdTypeId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentChannelId(pcId));
        data.crmTbDepositObjects.forEach(d -> d.setAmountUsd(BigDecimal.valueOf(300.01)));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentProfileKey(getPaymentProfileCard(cardMaskedNumber, cardExpiration)));
        CrmTbDepositTypeObject dType = CrmTbDepositTypeObject.builder().id(wdTypeId).sourceIdSt(sourceId).category(2).name(catName).lastUpdated(getCurrentTimestampDbFormat()).build();
        data.crmTbDepositTypeObjects = List.of(dType);
        CrmTbDepositChannelObject dChannel = CrmTbDepositChannelObject.builder().id(pcId).sourceIdSt(sourceId).channelId(pcId).typeId(wdTypeId).name(catName).isMobileChannel(0).lastUpdated(getCurrentTimestampDbFormat()).build();
        data.crmTbDepositChannelObjects = List.of(dChannel);

        //set client connections
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        ConnectionTableEntry connection1 = getConnection(data.clientHelper, connectedClient);
        connection1.connectionScore = 0.8;
        ConnectionTableEntry.ConnectionInfo connectionInfo1 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo1.connectionAttributeName = "name+dateofbirth";
        connectionInfo1.connectionAttributeValue = "test";
        connectionInfo1.sourceAttributeValue = "test";
        connectionInfo1.relationType = "exact";
        connection1.connectionInfo = ConnectionTableEntry.ConnectionInfo.connectionInfoToString(List.of(connectionInfo1));
        ClientHelper connectedClient2 = getRandomVantageClientAllFields();
        ConnectionTableEntry connection2 = getConnection(data.clientHelper, connectedClient2);
        connection2.connectionScore = 0.8;
        ConnectionTableEntry.ConnectionInfo connectionInfo2 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo2.connectionAttributeName = "document";
        connectionInfo2.connectionAttributeValue = "test";
        connectionInfo2.sourceAttributeValue = "test";
        connectionInfo2.relationType = "exact";
        connection2.connectionInfo = ConnectionTableEntry.ConnectionInfo.connectionInfoToString(List.of(connectionInfo2));
        ClientHelper connectedClient3 = getRandomVantageClientAllFields();
        ConnectionTableEntry connection3 = getConnection(data.clientHelper, connectedClient3);
        connection3.connectionScore = 0.8;
        ConnectionTableEntry.ConnectionInfo connectionInfo3 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo3.connectionAttributeName = "payout";
        connectionInfo3.connectionAttributeValue = "test";
        connectionInfo3.sourceAttributeValue = "test";
        connectionInfo3.relationType = "exact";
        connection3.connectionInfo = ConnectionTableEntry.ConnectionInfo.connectionInfoToString(List.of(connectionInfo3));
        data.connections = List.of(connection1, connection2, connection3);
        ClientHelper connectedClient4 = getRandomVantageClientAllFields();
        ClientHelper connectedClient5 = getRandomVantageClientAllFields();
        CrmTbUserObject connectedUserCrmTbUserObject1 = generateUserByClient(connectedClient);
        CrmTbUserObject connectedUserCrmTbUserObject2 = generateUserByClient(connectedClient2);
        CrmTbUserObject connectedUserCrmTbUserObject3 = generateUserByClient(connectedClient3);
        CrmTbUserObject connectedUserCrmTbUserObject4 = generateUserByClient(connectedClient4);
        CrmTbUserObject connectedUserCrmTbUserObject5 = generateUserByClient(connectedClient5);
        data.connectedClientHelpers = List.of(connectedClient3);
        data.connectedUsers = List.of(connectedUserCrmTbUserObject1, connectedUserCrmTbUserObject2, connectedUserCrmTbUserObject3, connectedUserCrmTbUserObject4, connectedUserCrmTbUserObject5);

        //set client cards
        ClientCardsObject clientCard1 = generateClientCardsObject(data.clientHelper);
        ClientCardsObject clientCard2 = generateClientCardsObject(data.clientHelper);
        ClientCardsObject clientCard3 = generateClientCardsObject(data.clientHelper);
        ClientCardsObject clientCard4 = generateClientCardsObject(data.clientHelper);
        data.clientCards = List.of(clientCard1, clientCard2, clientCard3, clientCard4);

        //set open trades
        Mt5DealsCoercedObject trade1 = generateTradeByClient(data.clientHelper, 0, 0, 0, getRandomLongPositive());
        trade1.setPositionId(getRandomLongPositive());
        trade1.setEntry(0);
        trade1.setSymbol("EURUSD");
        data.mt5DealsCoercedObjects = List.of(trade1);

        //set segment
        SegmentationTableObject segment = SegmentationTableObject.builder().date(getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE, 0, 0, 1, 0, 0)).ucid(data.clientHelper.getUcid()).segment("Low").build();
        data.segmentObjects = List.of(segment);

        return data;
    }

    private static DataHelper getChargebackTest21Data() {
        DataHelper data = getChargebackRuleData(chargebackRuleClient21);

        //set ticks
        RatesUsdCurrentObject tick = RatesUsdCurrentObject.builder().ts(getCurrentTimestampDbFormat()).currency(baseCurrency).rate(1.1).build();
        data.ratesUsdCurrentObjects = List.of(tick);

        //set deposits
        int wdTypeId = getRandomBytePositive();
        int sourceId = getRandomBytePositive();
        int pcId = getRandomBytePositive();
        String catName = "card cat";
        CrmTbDepositEntity deposit1 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit2 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit3 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit4 = generateCrmTbDepositEntityByClient(data.clientHelper);
        deposit4.setStatus("Reject");
        deposit4.setOrderNumber(callbackData.getOrderId());
        data.crmTbDepositObjects = List.of(deposit1, deposit2, deposit3, deposit4);
        data.crmTbDepositObjects.forEach(d -> d.setSourceIdSt(sourceId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentTypeId(wdTypeId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentChannelId(pcId));
        data.crmTbDepositObjects.forEach(d -> d.setAmountUsd(BigDecimal.valueOf(300.01)));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentProfileKey(getPaymentProfileCard(cardMaskedNumber, cardExpiration)));
        CrmTbDepositTypeObject dType = CrmTbDepositTypeObject.builder().id(wdTypeId).sourceIdSt(sourceId).category(2).name(catName).lastUpdated(getCurrentTimestampDbFormat()).build();
        data.crmTbDepositTypeObjects = List.of(dType);
        CrmTbDepositChannelObject dChannel = CrmTbDepositChannelObject.builder().id(pcId).sourceIdSt(sourceId).channelId(pcId).typeId(wdTypeId).name(catName).isMobileChannel(0).lastUpdated(getCurrentTimestampDbFormat()).build();
        data.crmTbDepositChannelObjects = List.of(dChannel);

        //set client connections
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        ConnectionTableEntry connection1 = getConnection(data.clientHelper, connectedClient);
        connection1.connectionScore = 0.8;
        ConnectionTableEntry.ConnectionInfo connectionInfo1 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo1.connectionAttributeName = "name+dateofbirth";
        connectionInfo1.connectionAttributeValue = "test";
        connectionInfo1.sourceAttributeValue = "test";
        connectionInfo1.relationType = "exact";
        connection1.connectionInfo = ConnectionTableEntry.ConnectionInfo.connectionInfoToString(List.of(connectionInfo1));
        ClientHelper connectedClient2 = getRandomVantageClientAllFields();
        ConnectionTableEntry connection2 = getConnection(data.clientHelper, connectedClient2);
        connection2.connectionScore = 0.8;
        ConnectionTableEntry.ConnectionInfo connectionInfo2 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo2.connectionAttributeName = "document";
        connectionInfo2.connectionAttributeValue = "test";
        connectionInfo2.sourceAttributeValue = "test";
        connectionInfo2.relationType = "exact";
        connection2.connectionInfo = ConnectionTableEntry.ConnectionInfo.connectionInfoToString(List.of(connectionInfo2));
        ClientHelper connectedClient3 = getRandomVantageClientAllFields();
        ConnectionTableEntry connection3 = getConnection(data.clientHelper, connectedClient3);
        connection3.connectionScore = 0.8;
        ConnectionTableEntry.ConnectionInfo connectionInfo3 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo3.connectionAttributeName = "payout";
        connectionInfo3.connectionAttributeValue = "test";
        connectionInfo3.sourceAttributeValue = "test";
        connectionInfo3.relationType = "exact";
        connection3.connectionInfo = ConnectionTableEntry.ConnectionInfo.connectionInfoToString(List.of(connectionInfo3));
        data.connections = List.of(connection1, connection2, connection3);
        ClientHelper connectedClient4 = getRandomVantageClientAllFields();
        ClientHelper connectedClient5 = getRandomVantageClientAllFields();
        CrmTbUserObject connectedUserCrmTbUserObject1 = generateUserByClient(connectedClient);
        CrmTbUserObject connectedUserCrmTbUserObject2 = generateUserByClient(connectedClient2);
        CrmTbUserObject connectedUserCrmTbUserObject3 = generateUserByClient(connectedClient3);
        CrmTbUserObject connectedUserCrmTbUserObject4 = generateUserByClient(connectedClient4);
        CrmTbUserObject connectedUserCrmTbUserObject5 = generateUserByClient(connectedClient5);
        data.connectedClientHelpers = List.of(connectedClient3);
        data.connectedUsers = List.of(connectedUserCrmTbUserObject1, connectedUserCrmTbUserObject2, connectedUserCrmTbUserObject3, connectedUserCrmTbUserObject4, connectedUserCrmTbUserObject5);

        //set client cards
        ClientCardsObject clientCard1 = generateClientCardsObject(data.clientHelper);
        ClientCardsObject clientCard2 = generateClientCardsObject(data.clientHelper);
        ClientCardsObject clientCard3 = generateClientCardsObject(data.clientHelper);
        ClientCardsObject clientCard4 = generateClientCardsObject(data.clientHelper);
        data.clientCards = List.of(clientCard1, clientCard2, clientCard3, clientCard4);

        //set open trades
        Mt5DealsCoercedObject trade1 = generateTradeByClient(data.clientHelper, 0, 0, 0, getRandomLongPositive());
        trade1.setPositionId(getRandomLongPositive());
        trade1.setEntry(0);
        trade1.setSymbol("EURUSD");
        data.mt5DealsCoercedObjects = List.of(trade1);

        //set segment
        SegmentationTableObject segment = SegmentationTableObject.builder().date(getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE, 0, 0, 1, 0, 0)).ucid(data.clientHelper.getUcid()).segment("Very High").build();

        data.segmentObjects = List.of(segment);

        return data;
    }

    private static DataHelper getChargebackTest22Data() {
        DataHelper data = getChargebackRuleData(chargebackRuleClient22);

        //set ticks
        RatesUsdCurrentObject tick = RatesUsdCurrentObject.builder().ts(getCurrentTimestampDbFormat()).currency(baseCurrency).rate(1.1).build();
        data.ratesUsdCurrentObjects = List.of(tick);

        //set deposits
        int wdTypeId = getRandomBytePositive();
        int sourceId = getRandomBytePositive();
        int pcId = getRandomBytePositive();
        String catName = "card cat";
        CrmTbDepositEntity deposit4 = generateCrmTbDepositEntityByClient(data.clientHelper);
        deposit4.setStatus("Reject");
        deposit4.setOrderNumber(callbackData.getOrderId());
        data.crmTbDepositObjects = List.of(deposit4);
        data.crmTbDepositObjects.forEach(d -> d.setSourceIdSt(sourceId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentTypeId(wdTypeId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentChannelId(pcId));
        data.crmTbDepositObjects.forEach(d -> d.setAmountUsd(BigDecimal.valueOf(300.01)));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentProfileKey(getPaymentProfileCard(cardMaskedNumber, cardExpiration)));
        CrmTbDepositTypeObject dType = CrmTbDepositTypeObject.builder().id(wdTypeId).sourceIdSt(sourceId).category(2).name(catName).lastUpdated(getCurrentTimestampDbFormat()).build();
        data.crmTbDepositTypeObjects = List.of(dType);
        CrmTbDepositChannelObject dChannel = CrmTbDepositChannelObject.builder().id(pcId).sourceIdSt(sourceId).channelId(pcId).typeId(wdTypeId).name(catName).isMobileChannel(0).lastUpdated(getCurrentTimestampDbFormat()).build();
        data.crmTbDepositChannelObjects = List.of(dChannel);

        //set client connections
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        ConnectionTableEntry connection1 = getConnection(data.clientHelper, connectedClient);
        connection1.connectionScore = 0.8;
        ConnectionTableEntry.ConnectionInfo connectionInfo1 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo1.connectionAttributeName = "name+dateofbirth";
        connectionInfo1.connectionAttributeValue = "test";
        connectionInfo1.sourceAttributeValue = "test";
        connectionInfo1.relationType = "exact";
        connection1.connectionInfo = ConnectionTableEntry.ConnectionInfo.connectionInfoToString(List.of(connectionInfo1));
        ClientHelper connectedClient2 = getRandomVantageClientAllFields();
        ConnectionTableEntry connection2 = getConnection(data.clientHelper, connectedClient2);
        connection2.connectionScore = 0.8;
        ConnectionTableEntry.ConnectionInfo connectionInfo2 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo2.connectionAttributeName = "document";
        connectionInfo2.connectionAttributeValue = "test";
        connectionInfo2.sourceAttributeValue = "test";
        connectionInfo2.relationType = "exact";
        connection2.connectionInfo = ConnectionTableEntry.ConnectionInfo.connectionInfoToString(List.of(connectionInfo2));
        ClientHelper connectedClient3 = getRandomVantageClientAllFields();
        ConnectionTableEntry connection3 = getConnection(data.clientHelper, connectedClient3);
        connection3.connectionScore = 0.8;
        ConnectionTableEntry.ConnectionInfo connectionInfo3 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo3.connectionAttributeName = "payout";
        connectionInfo3.connectionAttributeValue = "test";
        connectionInfo3.sourceAttributeValue = "test";
        connectionInfo3.relationType = "exact";
        connection3.connectionInfo = ConnectionTableEntry.ConnectionInfo.connectionInfoToString(List.of(connectionInfo3));
        data.connections = List.of(connection1, connection2, connection3);
        ClientHelper connectedClient4 = getRandomVantageClientAllFields();
        ClientHelper connectedClient5 = getRandomVantageClientAllFields();
        CrmTbUserObject connectedUserCrmTbUserObject1 = generateUserByClient(connectedClient);
        CrmTbUserObject connectedUserCrmTbUserObject2 = generateUserByClient(connectedClient2);
        CrmTbUserObject connectedUserCrmTbUserObject3 = generateUserByClient(connectedClient3);
        CrmTbUserObject connectedUserCrmTbUserObject4 = generateUserByClient(connectedClient4);
        CrmTbUserObject connectedUserCrmTbUserObject5 = generateUserByClient(connectedClient5);
        data.connectedClientHelpers = List.of(connectedClient3);
        data.connectedUsers = List.of(connectedUserCrmTbUserObject1, connectedUserCrmTbUserObject2, connectedUserCrmTbUserObject3, connectedUserCrmTbUserObject4, connectedUserCrmTbUserObject5);

        //set client cards
        ClientCardsObject clientCard1 = generateClientCardsObject(data.clientHelper);
        ClientCardsObject clientCard2 = generateClientCardsObject(data.clientHelper);
        data.clientCards = List.of(clientCard1, clientCard2);

        //set open trades
        Mt5DealsCoercedObject trade1 = generateTradeByClient(data.clientHelper, 0, 0, 0, getRandomLongPositive());
        trade1.setPositionId(getRandomLongPositive());
        trade1.setEntry(0);
        trade1.setSymbol("EURUSD");
        data.mt5DealsCoercedObjects = List.of(trade1);

        //set segment
        SegmentationTableObject segment = SegmentationTableObject.builder().date(getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE, 0, 0, 1, 0, 0)).ucid(data.clientHelper.getUcid()).segment("Ultra High").build();

        data.segmentObjects = List.of(segment);

        return data;
    }

    public static Map<String, DataHelper> setupChargebackData() throws IOException {
        startSshTunnel();
        Map<String, DataHelper> map = new HashMap<>();
//         Put all the db data for setup in a map
        map.put("1", getChargebackTest1Data());
        map.put("2", getChargebackTest2Data());
        map.put("3", getChargebackTest3Data());
        map.put("4", getChargebackTest4Data());
        map.put("5", getChargebackTest5Data());
        map.put("6", getChargebackTest6Data());
        map.put("7", getChargebackTest7Data());
        map.put("8", getChargebackTest8Data());
        map.put("9", getChargebackTest9Data());
        map.put("10", getChargebackTest10Data());
        map.put("11", getChargebackTest11Data());
        map.put("12", getChargebackTest12Data());
        map.put("13", getChargebackTest13Data());
        map.put("14", getChargebackTest14Data());
        map.put("15", getChargebackTest15Data());
        map.put("16", getChargebackTest16Data());
        map.put("17", getChargebackTest17Data());
        map.put("18", getChargebackTest18Data());
        map.put("19", getChargebackTest19Data());
        map.put("20", getChargebackTest20Data());
        map.put("21", getChargebackTest21Data());
        map.put("22", getChargebackTest22Data());

        setupData(map);

        return map;
    }
}
