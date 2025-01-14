package businessObjects.db.clickhouse.mtTbTrade;

import io.qameta.allure.Step;

import static utils.Utils.getCurrentTimestampDbFormat;
import static utils.Utils.getRandomIntPositive;

public class MtTbTradeFactory {

    @Step("Generate mt__tb_trade object by account and serverId")
    public static MtTbTradeObject generateMtTbTrade(Integer account, Integer serverId) {
        return new MtTbTradeObject("MT4", getRandomIntPositive(), account, "USD", "EURUSD", "Buy", getCurrentTimestampDbFormat(), 1234.55, getCurrentTimestampDbFormat(), 1234.66, 33.44, 55.66, 11.22, 22.33, 78.99, "Client", "comment_autotest", 123.78, 76.54, 87.43, 32.56, 12.98, serverId, getCurrentTimestampDbFormat());
    }
}