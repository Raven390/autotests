package business_objects.db.clickhouse.crm_tb_user_table;


import helpers.data.ClientHelper;
import io.qameta.allure.Step;

import java.util.ArrayList;
import java.util.List;

import static utils.Constants.ENCODED_EMAIL;
import static utils.Constants.ENCODED_PHONE;
import static utils.Utils.*;

public class CrmTbUserObjectFactory {
    @Step("Generate user object by Client")
    public static CrmTbUserObject generateUserByClient(ClientHelper client) {
        return new CrmTbUserObject(client.getUserId(), client.getUcid(), client.getBrand(), client.getRegulator(), client.getBrand() + "Group", getCurrentDate(), getCurrentDate(), "Test", "User", "1", "1961-02-01", client.getCountry(), client.getCountryCode(), client.getCountryCode(), "en", "RUS", ENCODED_EMAIL, ENCODED_PHONE, "357", "1", "2FA", "2", "1", "2", client.getIbId(), client.getCpaId(), client.getReferrerId(), "PARTIAL_KYC_ID_PASS", getCurrentTimestampDbFormat(), "2025-01-30 14:56:59.000", "2025-01-30 14:56:59.000", getRandomUuidString(), "nationalityId", "2015-11-29 09:55:01");
    }

    @Step("Generate user objects by Client")
    public static List<CrmTbUserObject> generateUserByClients(List<ClientHelper> clients) {
        List<CrmTbUserObject> users = new ArrayList<>();
        for (ClientHelper client : clients) {
            users.add(generateUserByClient(client));
        }
        return users;
    }

    public static CrmTbUserObject generateStaticUserByClient(ClientHelper client) {
        return new CrmTbUserObject(client.getUserId(), client.getUcid(), client.getBrand(), client.getRegulator(), client.getBrand() + "Group", "2014-10-23", "2014-10-23", "Test", "User", "1", "1961-02-01", "Cyprus", "CY", "CY", "en", "RUS", ENCODED_EMAIL, ENCODED_PHONE, "357", "1", "2FA", "2", "1", "2", 1, 2, 3, "PARTIAL_KYC_ID_PASS", getCurrentTimestampDbFormat(), "2014-10-23 14:56:59.000", "2014-10-23 14:56:59.000", "063cde3b-ea9d-48b5-8e2c-99f3d5f67999", "nationalityId", "2015-11-29 09:55:01");
    }

    @Step("Generate user object by Client")
    public static CrmTbUserObject generateUserWithUcidFirstName(ClientHelper client) {
        CrmTbUserObject user = generateUserByClient(client);
        user.firstName = client.getUcid();
        user.lastName = "";
        return user;
    }

    @Step("Generate Bybit user object by Client")
    public static CrmTbUserObject generateBybitUserByClient(ClientHelper client) {
        return new CrmTbUserObject(client.getUserId(), client.getUcid(), client.getBrand(), "", client.getBrand() + "Group", getCurrentDate(), getCurrentDate(), "", "", "", "1961-02-01", "", "", "", "", "", "", "", "0", "0", "", "2", "0", "0", 0, 0, 0, "UNKNOWN", getCurrentTimestampDbFormat(), "2025-01-30 14:56:59.000", "2025-01-30 14:56:59.000", null, "0", null);
    }
}
