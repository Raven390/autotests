package business_objects.db.clickhouse.crm_tb_bonus_table;


import helpers.data.ClientHelper;
import io.qameta.allure.Step;

import static utils.Utils.getCurrentTimestampDbFormat;
import static utils.Utils.getRandomIntPositive;

public class CrmTbBonusObjectFactory {
    @Step("Generate bonus object by client")
    public static CrmTbBonusObject generateBonusByClient(ClientHelper client) {
        return new CrmTbBonusObject(1, 2, client.getBrand(), client.getRegulator(), client.getUserId(), client.getUcid(), client.getTradingAccount(), getRandomIntPositive(), getCurrentTimestampDbFormat(), getCurrentTimestampDbFormat(), getCurrentTimestampDbFormat(), getCurrentTimestampDbFormat(), 1.0, 2.0, "USD", 1, "statusTest", "CASH_ADJUSTMENT", "WelcomeBonus", "Comment", "ticketTest", getCurrentTimestampDbFormat());
    }
}
