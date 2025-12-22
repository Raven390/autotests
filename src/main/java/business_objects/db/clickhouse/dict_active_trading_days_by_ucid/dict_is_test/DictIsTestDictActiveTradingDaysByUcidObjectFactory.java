package business_objects.db.clickhouse.dict_active_trading_days_by_ucid.dict_is_test;

import static utils.Utils.getCurrentDate;
import static utils.Utils.getPreviousDayByIntDaysYearMonthDay;

import helpers.data.ClientHelper;
import io.qameta.allure.Step;
import java.util.ArrayList;
import java.util.List;

public class DictIsTestDictActiveTradingDaysByUcidObjectFactory {
    @Step("Create number of trading days record for the user")
    public static DictActiveTradingDaysByUcidObject generateDictActiveTradingDaysByClient(ClientHelper client) {
        return new DictActiveTradingDaysByUcidObject(client.getUcid(), getCurrentDate());
    }

    public static List<DictActiveTradingDaysByUcidObject> generateTradingDaysByClient(
            ClientHelper client, int daysAmount) {
        List<DictActiveTradingDaysByUcidObject> tradingDays = new ArrayList<>();

        for (int i = 0; i < daysAmount; i++) {
            String tradeDate = getPreviousDayByIntDaysYearMonthDay(i);
            tradingDays.add(new DictActiveTradingDaysByUcidObject(client.getUcid(), tradeDate));
        }

        return tradingDays;
    }
}
