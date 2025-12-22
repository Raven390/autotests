package helpers.data.rules.payments.router_rule_crm_payment;

import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.DataHelper.createClient;
import static helpers.data.DataSetupHelper.setupData;
import static helpers.database.DbHelper.startSshTunnel;
import static utils.Constants.CRM_TRANSFER_TO_WA_EVENT;

import business_objects.kafka.crm_events.TransferToWaEvent;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import io.qameta.allure.Description;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import utils.Utils;

public class RouterRuleCrmPaymentTransferToWaDataFactory {
    private static final ClientHelper routerRuleClient1 = getRandomVantageClientAllFields();
    private static final ClientHelper routerRuleClient2 = getRandomVantageClientAllFields();
    private static final ClientHelper routerRuleClient3 = getRandomVantageClientAllFields();

    @Description("Create data for Router rule")
    private static DataHelper getRouterRuleData(ClientHelper client) {
        DataHelper data = new DataHelper();
        createClient(data, client);
        UUID id = UUID.randomUUID();
        data.transferToWaEvent = TransferToWaEvent.builder()
                .fromMt4account(data.clientHelper.getTradingAccount()) // fromMt4account
                .schemaVersion("1.0") // schemaVersion
                .clientId(data.clientHelper.getUserId()) // clientId
                .accountType("MT5") // accountType
                .actualAmount(0.033_958_96) // actualAmount
                .transferAmount(101D) // transferAmount
                .merchantOrderId("AUVF1110171050ETH17640572580047") // merchantOrderId
                .type(CRM_TRANSFER_TO_WA_EVENT) // type
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

    private static DataHelper getRouterRuleTest1Data() {
        DataHelper data = getRouterRuleData(routerRuleClient1);
        data.transferToWaEvent.setCheckName("Crypto_Risk");
        data.transferToWaEvent.setTransferAmount(1d);
        return data;
    }

    private static DataHelper getRouterRuleTest2Data() {
        DataHelper data = getRouterRuleData(routerRuleClient2);
        data.transferToWaEvent.setCheckName("Crypto_Risk");
        data.transferToWaEvent.setTransferAmount(1d);
        return data;
    }

    private static DataHelper getRouterRuleTest3Data() {
        DataHelper data = getRouterRuleData(routerRuleClient3);
        data.transferToWaEvent.setTransferAmount(1d);
        return data;
    }

    public static Map<String, DataHelper> setupRouterRuleData() {
        startSshTunnel();
        Map<String, DataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("1", getRouterRuleTest1Data());
        map.put("2", getRouterRuleTest2Data());
        map.put("3", getRouterRuleTest3Data());

        setupData(map);

        return map;
    }
}
