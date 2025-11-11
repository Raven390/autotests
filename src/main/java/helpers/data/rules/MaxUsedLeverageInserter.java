package helpers.data.rules;

import helpers.data.ClientHelper;
import helpers.database.DbName;

import static helpers.database.DbHelper.executeQueryToDb;
import static utils.Constants.*;

public class MaxUsedLeverageInserter {
    public static void insertMaxUsedLeverageData(ClientHelper client) {
        String rawQuerry = """
                INSERT INTO consolidated.mt___mt5_deals_coerced_dd (
                    brand, regulator, user_id, ucid, account, platform, server_id, server_name, account_type, account_group, account_currency,
                    deal, `order`, action, entry, reason, contract_size, time, time_utc, symbol, symbol_underlying, base_currency, quote_currency,
                    rate_usd_base, rate_usd_quote, rate_usd_acc, price, volume, volume_lots, notional_value_usd, profit, storage, commission,
                    profit_usd, storage_usd, commission_usd, expert_id, position_id, comment, sl, tp, price_gateway, market_bid, market_ask,
                    rate_profit, is_deleted, last_updated, internal_comment, is_abnormal_time, leverage, balance, equity, margin, free_margin,
                    balance_usd, equity_usd, margin_usd, free_margin_usd
                ) VALUES
                            ('clientBrand', 'clientRegulator', clientId, 'clientUcid', clientAccount, 'MT4', clientServerId, 'VT1', 'VIP STP', 'M_VT_V_USD', 'USD',
                             1001, 1001, 1, 0, 0, 100,
                             '2024-09-09 05:00:00.000', '2024-09-09 05:00:00.000', 'XAUUSD-VIP', 'XAUUSD', 'XAU', 'USD',
                             1, 1, 1, 2000.000000, 1, 1.0000, 1000.00, 0.00, 0.00, 0.00,
                             0.0000, 0.0000, 0.0000, 0, 0, '', 0.000000, 0.000000, 0.000000, 0.000000, 0.000000,
                             0.000000, 0, '2025-01-01 00:00:00.000000', 'seed', 0, 1000, 0.00, 1000.00, 0.00, 500.00,
                             0.0000, 999999.0000, 0.0000, 1.0000),

                            ('clientBrand', 'clientRegulator', clientId, 'clientUcid', clientAccount, 'MT4', clientServerId, 'VT1', 'VIP STP', 'M_VT_V_USD', 'USD',
                            1002, 1002, 0, 1, 0, 100,
                            '2024-09-10 06:00:00.000', '2024-09-10 06:00:00.000', 'XAUUSD-VIP', 'XAUUSD', 'XAU', 'USD',
                            1, 1, 1, 2100.000000, 1, 1.0000, 2000.00, 0.00, 0.00, 0.00,
                            0.0000, 0.0000, 0.0000, 0, 0, '', 0.000000, 0.000000, 0.000000, 0.000000, 0.000000,
                            0.000000, 0, '2025-01-01 00:00:00.000000', 'seed', 0, 1000, 0.00, 2000.00, 0.00, 1000.00,
                            0.0000, 8000.0000, 0.0000, 1.0000),

                            ('clientBrand', 'clientRegulator', clientId, 'clientUcid', clientAccount, 'MT4', clientServerId, 'VT1', 'VIP STP', 'M_VT_V_USD', 'USD',
                            10234, 1001, 1, 0, 0, 100,
                            '2024-09-09 05:00:00.000', '2024-09-09 05:00:00.000', 'XAUUSD-VIP', 'XAUUSD', 'XAU', 'USD',
                            1, 1, 1, 2000.000000, 1, 1.0000, 1000.00, 0.00, 0.00, 0.00,
                            0.0000, 0.0000, 0.0000, 0, 0, '', 0.000000, 0.000000, 0.000000, 0.000000, 0.000000,
                            0.000000, 0, '2025-01-01 00:00:00.000000', 'seed', 0, 1000, 0.00, 1000.00, 0.00, 500.00,
                            0.0000, 8000.0000, 0.0000, 1.0000),

                            ('clientBrand', 'clientRegulator', clientId, 'clientUcid', clientAccount, 'MT4', clientServerId, 'VT1', 'VIP STP', 'M_VT_V_USD', 'USD',
                            10235, 1002, 0, 1, 0, 100,
                            '2024-09-10 06:00:00.000', '2024-09-10 06:00:00.000', 'XAUUSD-VIP', 'XAUUSD', 'XAU', 'USD',
                            1, 1, 1, 2100.000000, 1, 1.0000, 2000.00, 0.00, 0.00, 0.00,
                            0.0000, 0.0000, 0.0000, 0, 0, '', 0.000000, 0.000000, 0.000000, 0.000000, 0.000000,
                            0.000000, 0, '2025-01-01 00:00:00.000000', 'seed', 0, 1000, 0.00, 2000.00, 0.00, 1000.00,
                            0.0000, 8000.0000, 0.0000, 1.0000);
                """;
        executeQueryToDb(DbName.CLICKHOUSE, trasformQuerry(rawQuerry, client));

    }

    protected static String trasformQuerry(String rawQuerry, ClientHelper client) {
        return rawQuerry.replace("clientBrand", client.getBrand()).replace("clientRegulator", client.getRegulator()).replace("clientId", client.getUserId().toString()).replace("clientAccount", client.getTradingAccount().toString()).replace("clientServerId", client.getServerId().toString()).replace("clientUcid", client.getUcid());
    }

}
