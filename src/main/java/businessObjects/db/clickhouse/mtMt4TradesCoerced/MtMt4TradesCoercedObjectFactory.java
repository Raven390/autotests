package businessObjects.db.clickhouse.mtMt4TradesCoerced;

import helpers.data.ClientHelper;
import io.qameta.allure.Step;

import static utils.Utils.getCurrentTimestampDbFormat;
import static utils.Utils.getRandomIntPositive;

public class MtMt4TradesCoercedObjectFactory {

    @Step("Generate mt___mt4_trades_coerced object by account and serverId")
    public static MtMt4TradesCoercedObject generateMt4TradesCoerced(ClientHelper client) {
        return new MtMt4TradesCoercedObject(client.getBrand(), client.getRegulator(), client.getUserId().longValue(), client.getUcid(), client.getTradingAccount().longValue(), "MT4", client.getServerId().longValue(), "serverName", "accountType", "accountGroup", "USD", getRandomIntPositive().longValue(), 1L, "Buy", 1L, "Client", 1L, getCurrentTimestampDbFormat(), getCurrentTimestampDbFormat(), 1234.55, 33.44, 55.66, "EURUSD", "GBPJPY", "EUR", "GBP", 1.1, 1.1, 1.1, 1.1, 1.1, 1.1, 1L, 87.43, 76.54, 1.1, 1.1, 1.1, 1.1, 1.1, 78.99, 22.33, 11.22, getCurrentTimestampDbFormat(), getCurrentTimestampDbFormat(), 1234.66, 1L, "comment_autotest", 123.78, 32.56, 12.98, 1L, getCurrentTimestampDbFormat(), "internalComment");
    }
}