package business_objects.db.clickhouse.mt_tb_credits;


import helpers.data.ClientHelper;
import io.qameta.allure.Step;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;

import static helpers.data.enums.NbdComment.getRandomNbdComment;
import static utils.Utils.*;

public class MtTbCreditsObjectFactory {
    @Step("Generate credit object by client object")
    public static MtTbCreditsObject generateCreditsByClient(ClientHelper client) {
        return new MtTbCreditsObject(client.getTradingAccount(), 1.0, 1.0, client.getBrand(), Utils.getRandomUuidString(), getCurrentTimestampDbFormat(), "USD", "VFSC", client.getServerId(), "server1", getRandomIntPositive(), client.getUcid(), Utils.getRandomUuidString(), client.getUserId(), Utils.getRandomUuidString(), getCurrentTimestampDbFormat(), getCurrentTimestampDbFormat());
    }

    @Step("Generate credit object by client object")
    public static MtTbCreditsObject generateCreditsByClient(ClientHelper client, Double amount) {
        return new MtTbCreditsObject(client.getTradingAccount(), amount, amount, client.getBrand(), Utils.getRandomUuidString(), getCurrentTimestampDbFormat(), "USD", "VFSC", client.getServerId(), "server1", getRandomIntPositive(), client.getUcid(), Utils.getRandomUuidString(), client.getUserId(), Utils.getRandomUuidString(), getCurrentTimestampDbFormat(), getCurrentTimestampDbFormat());
    }

    @Step("Generate credit object by client object")
    public static MtTbCreditsObject generateCreditsByClientRandomized(ClientHelper client) {
        MtTbCreditsObject credit = new MtTbCreditsObject();
        credit.ticket = client.getTradingAccount() + getRandomIntPositive();
        credit.serverId = client.getServerId();
        credit.ucid = client.getUcid();
        credit.brand = client.getBrand();
        credit.regulator = client.getRegulator();
        credit.userId = client.getUserId();
        credit.account = client.getTradingAccount();
        credit.createTime = getCurrentTimestampDbFormat();
        credit.createTimeUtc = getCurrentTimestampDbFormat();
        credit.amount = getRandomRoundedDouble(0.01, 999_999.99);
        credit.amountUsd = credit.amount;
        credit.comment = "autotest" + getCurrentTimestampSeconds();
        credit.lastUpdated = getCurrentTimestampDbFormat();
        return credit;
    }

    public static List<MtTbCreditsObject> generateNdbCredits(ClientHelper client, int number) {
        List<MtTbCreditsObject> credits = new ArrayList<>();
        for (int i = 1; i <= number; i++) {
            MtTbCreditsObject newCredit = generateCreditsByClientRandomized(client);
            newCredit.internalComment = getRandomNbdComment().getDisplayName();
            newCredit.comment = getRandomNbdComment().getDisplayName();
            credits.add(newCredit);
        }
        return credits;
    }
}
