package business_objects.db.backoffice_db.client;

import helpers.data.ClientHelper;
import io.qameta.allure.Step;

import static utils.Utils.*;

public class ClientFactory {

    @Step("Generate BO client data")
    public static Client generateCrmTbAccountData(ClientHelper client) {
        return new Client(client.getUcid(), client.getUserId(), client.getBrand(), client.getRegulator(), client.getCountryCode(), client.getCountryCode());
    }
}
