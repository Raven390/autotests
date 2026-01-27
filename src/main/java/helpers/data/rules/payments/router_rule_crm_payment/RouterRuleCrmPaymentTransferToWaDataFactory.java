package helpers.data.rules.payments.router_rule_crm_payment;

import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.DbHelper.startSshTunnel;

import business_objects.kafka.crm_events.TransferToWaEvent;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import helpers.data.enums.rule_engine.Event;
import io.qameta.allure.Description;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import utils.Utils;

public class RouterRuleCrmPaymentTransferToWaDataFactory {
    private static final ClientHelper testClient1 = getRandomVantageClientAllFields();
    private static final ClientHelper testClient2 = getRandomVantageClientAllFields();
    private static final ClientHelper testClient3 = getRandomVantageClientAllFields();

    @Description("Create data for Router rule")
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
        DataHelper data = getRuleData(testClient1);
        data.transferToWaEvent.setCheckName("Crypto_Risk");
        data.transferToWaEvent.setTransferAmount(1d);
        return data;
    }

    private static DataHelper getTest2Data() {
        DataHelper data = getRuleData(testClient2);
        data.transferToWaEvent.setCheckName("Crypto_Risk");
        data.transferToWaEvent.setTransferAmount(1d);
        return data;
    }

    private static DataHelper getTest3Data() {
        DataHelper data = getRuleData(testClient3);
        data.transferToWaEvent.setTransferAmount(1d);
        return data;
    }

    public static Map<String, DataHelper> setupRouterRuleShadowModeTransferToWaData() {
        startSshTunnel();
        Map<String, DataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("1", getTest1Data());
        map.put("2", getTest2Data());
        map.put("3", getTest3Data());
        return map;
    }
}
