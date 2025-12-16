package helpers.data.rules.payments.router_rule_crm_payment;

import business_objects.kafka.crm_events.TransferToWaEvent;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import io.qameta.allure.Description;
import utils.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.DataHelper.createClient;
import static helpers.data.DataHelper.setupData;
import static helpers.database.DbHelper.startSshTunnel;

public class RouterRuleCrmPaymentTransferToWaDataFactory {
    private static final ClientHelper routerRuleClient1 = getRandomVantageClientAllFields();
    private static final ClientHelper routerRuleClient2 = getRandomVantageClientAllFields();
    private static final ClientHelper routerRuleClient3 = getRandomVantageClientAllFields();

    @Description("Create data for Router rule")
    private static DataHelper getRouterRuleData(ClientHelper client) {
        DataHelper data = new DataHelper();
        createClient(data, client);
        UUID id = UUID.randomUUID();
        data.transferToWaEvent = new TransferToWaEvent(
                data.clientHelper.getTradingAccount(),      //fromMt4account
                "1.0",                                      //schemaVersion
                data.clientHelper.getUserId(),              //clientId
                "MT5",                                      //accountType
                0.033_958_96,                               //actualAmount
                101D,                                       //transferAmount
                "AUVF1110171050ETH17640572580047",          //merchantOrderId
                "transferToWA",                             //type
                Utils.getRandomLongPositive(),               //transferId
                "",                             //checkName
                "WEB",                                      //platform
                "AUVF1110171050ETH17640572580047",          //businessOrderId
                24,                                         //statusId
                "ETH",                                      //to currency
                "2025-11-25T07:55:46Z",                     //transferApplicationTime
                data.clientHelper.getRegulator(),           //regulator
                "USD",                                      //fromCurrency
                id,                                         //id
                data.clientHelper.getBrand(),               //brand
                "Risk Audit",                               //status
                "2025-11-25T07:55:46Z"                      //eventDate
        );
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
