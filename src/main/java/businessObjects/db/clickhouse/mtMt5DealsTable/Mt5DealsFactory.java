package businessObjects.db.clickhouse.mtMt5DealsTable;

import helpers.data.ClientHelper;
import io.qameta.allure.Step;

public class Mt5DealsFactory {

    @Step("Generate mt5 deals")
    public static Mt5DealsObject generateTradeByClient(ClientHelper client) {
        return new Mt5DealsObject(1, "[]", "Comment",1.0,1.2,13962487L,
                                  "",1,2,"",1234L,"",1.0,
                                  1,"","2024-10-08 01:47:30",123L,2.0,1.1,
                                  1.0,1, "1", 11111111L,111111L,2.2,
                                  3.3,1.0,1.0,1.0, 1.0,1.0,
                                  1.0,2.0,"",13,32, "MT5-IUK",
                                  "MT5_INF","","EURUSD",1.2,1.1,"",123L,
                                  "",0.0,500.0,50.0,25.0,1.0,1.0);
    }
}