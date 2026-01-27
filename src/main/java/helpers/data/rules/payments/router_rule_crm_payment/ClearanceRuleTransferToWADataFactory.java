package helpers.data.rules.payments.router_rule_crm_payment;

import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.DbHelper.startSshTunnel;

import business_objects.kafka.crm_events.TransferToWaEvent;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import helpers.data.enums.rule_engine.Event;
import io.qameta.allure.Description;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import utils.Utils;

public class ClearanceRuleTransferToWADataFactory {
    private static final ClientHelper client1 = getRandomVantageClientAllFields();
    private static final ClientHelper client2 = getRandomVantageClientAllFields();
    private static final ClientHelper client3 = getRandomVantageClientAllFields();
    private static final ClientHelper client4 = getRandomVantageClientAllFields();
    private static final ClientHelper client5 = getRandomVantageClientAllFields();
    private static final ClientHelper client6 = getRandomVantageClientAllFields();
    private static final ClientHelper client7 = getRandomVantageClientAllFields();
    private static final ClientHelper client8 = getRandomVantageClientAllFields();

    @Description("Create data for Clearance rule")
    private static DataHelper getRuleData(ClientHelper client) {
        DataHelper data = new DataHelper();
        data.createClient(client);

        UUID id = UUID.randomUUID();
        data.transferToWaEvent = TransferToWaEvent.builder()
                .fromMt4account(data.clientHelper.getTradingAccount()) // fromMt4account
                .schemaVersion("1.0") // schemaVersion
                .clientId(data.clientHelper.getUserId()) // clientId
                .accountType("MT5") // accountType
                .actualAmount(0.033_958_96) // actualAmount
                .transferAmount(101D) // transferAmount
                .merchantOrderId("AUVF1110171050ETH17640572580047") // merchantOrderId
                .type(Event.CRM_TRANSFER_TO_WA_EVENT.getName()) // type
                .transferId(Utils.getRandomLongPositive()) // transferId
                .checkName("") // checkName
                .platform("WEB") // platform
                .businessOrderId("AUVF1110171050ETH17640572580047") // businessOrderId
                .statusId(24) // statusId
                .toCurrency("ETH") // to currency
                .transferApplicationTime("2025-11-25T07:55:46Z") // transferApplicationTime
                .regulator(data.clientHelper.getRegulator()) // regulator
                .fromCurrency("USD") // fromCurrency
                .id(id) // id
                .brand(data.clientHelper.getBrand()) // brand
                .status("Risk Audit") // status
                .eventDate("2025-11-25T07:55:46Z") // eventDate
                .build();

        return data;
    }

    private static DataHelper getTest1Data() {
        DataHelper data = getRuleData(client1);

        return data;
    }

    private static DataHelper getTest2Data() {
        DataHelper data = getRuleData(client2);

        data.createDeposit();
        data.getCrmTbDepositObjects().getFirst().setAmount(BigDecimal.valueOf(10_001));
        data.getCrmTbDepositObjects().getFirst().setAmountUsd(BigDecimal.valueOf(10_001));

        return data;
    }

    private static DataHelper getTest3Data() {
        DataHelper data = getRuleData(client3);

        data.transferToWaEvent.setTransferAmount(100d);

        data.createDeposit();
        data.getCrmTbDepositObjects().getFirst().setAmount(BigDecimal.valueOf(9999));
        data.getCrmTbDepositObjects().getFirst().setAmountUsd(BigDecimal.valueOf(9999));

        return data;
    }

    private static DataHelper getTest4Data() {
        DataHelper data = getRuleData(client4);

        data.transferToWaEvent.setTransferAmount(0d);

        data.createDeposit();
        data.getCrmTbDepositObjects().getFirst().setAmount(BigDecimal.valueOf(9999));
        data.getCrmTbDepositObjects().getFirst().setAmountUsd(BigDecimal.valueOf(9999));

        return data;
    }

    private static DataHelper getTest5Data() {
        DataHelper data = getRuleData(client5);

        data.transferToWaEvent.setTransferAmount(50d);

        data.createDeposit();
        data.getCrmTbDepositObjects().getFirst().setAmount(BigDecimal.valueOf(100));
        data.getCrmTbDepositObjects().getFirst().setAmountUsd(BigDecimal.valueOf(100));

        data.createWithdrawal();
        data.getCrmTbWithdrawalObjects().getFirst().setAmount(BigDecimal.valueOf(20));
        data.getCrmTbWithdrawalObjects().getFirst().setAmountUsd(BigDecimal.valueOf(20));

        return data;
    }

    private static DataHelper getTest6Data() {
        DataHelper data = getRuleData(client6);

        return data;
    }

    private static DataHelper getTest7Data() {
        DataHelper data = getRuleData(client7);

        return data;
    }

    private static DataHelper getTest8Data() {
        DataHelper data = getRuleData(client8);
        data.transferToWaEvent.setTransferAmount(null);

        data.createDeposit();
        data.getCrmTbDepositObjects().getFirst().setAmount(BigDecimal.valueOf(9999));
        data.getCrmTbDepositObjects().getFirst().setAmountUsd(BigDecimal.valueOf(9999));

        return data;
    }

    public static Map<String, DataHelper> setupClearanceTransferToWAEventRuleData() {
        startSshTunnel();
        Map<String, DataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("1", getTest1Data());
        map.put("2", getTest2Data());
        map.put("3", getTest3Data());
        map.put("4", getTest4Data());
        map.put("5", getTest5Data());
        map.put("6", getTest6Data());
        map.put("7", getTest7Data());
        map.put("8", getTest8Data());
        return map;
    }
}
