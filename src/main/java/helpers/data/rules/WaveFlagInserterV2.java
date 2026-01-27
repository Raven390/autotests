package helpers.data.rules;

import static helpers.database.DbHelper.deleteObjectFromDb;
import static helpers.database.DbHelper.executeQueryToDb;
import static utils.Constants.*;
import static utils.Utils.writeLog;

import helpers.data.ClientHelper;
import helpers.database.DbName;

public class WaveFlagInserterV2 {
    public static void insertMirrorWaveV2Data(ClientHelper client) throws InterruptedException {
        String query =
                """
                INSERT INTO consolidated.crm___tb_account (ucid, account, server_id_st, currency, is_del, is_test, last_updated)
                VALUES ('clientUcid', clientAccount, clientServerId, 'USD', 0, 0, '2024-01-01 00:00:00');
            """;

        String query2 =
                """
                                                                    INSERT INTO consolidated.mt___mt5_deals_coerced_dd (ucid, account, platform, server_id,
                                                                    server_name, account_type, account_group, account_currency, deal,
                                                                    order, action, entry, reason, contract_size, time, time_utc, symbol,
                                                                    symbol_underlying, base_currency, quote_currency, rate_usd_base,
                                                                    rate_usd_quote, rate_usd_acc, price, volume, volume_lots,
                                                                    notional_value_usd, profit, storage, commission, profit_usd,
                                                                    storage_usd, commission_usd, expert_id, position_id, comment, sl,
                                                                    tp, price_gateway, market_bid, market_ask, rate_profit, is_deleted,
                                                                    last_updated, is_abnormal_time, leverage, balance,
                                                                    equity, margin, free_margin, balance_usd, equity_usd, margin_usd,
                                                                    free_margin_usd)
                VALUES ('clientUcid', clientAccount, 'MT5', clientServerId, 'MT5AU3', '', '', '', 38269259, 38877757, 1, 0, 2, 100,
                        '2025-12-01 17:02:14.71', '2025-12-01 15:02:14.71', 'XAUUSD', 'XAUUSD', 'XAU', 'USD', 0, 0, 1, 4242.560000,
                        1200, 0.1200, 50910.72, 0.00000000, 0.00000000, 0.00000000, 0.0000, 0.0000, 0.0000, 0, 38877757, '', 0.000000,
                        0.000000, 4242.560000, 4242.569999, 4242.750000, 1.000000, 0, '2025-12-01 15:02:14.71', 0,
                        1000, 210.00000000, 495.42000000, 114.52000000, 380.90000000, 210.0000, 495.4200, 114.5200, 380.9000),
                       ('clientUcid', clientAccount, 'MT5', clientServerId, 'MT5AU3', '', '', '', 38271106, 38879438, 1, 0, 2, 100,
                        '2025-12-01 17:05:31.22', '2025-12-01 15:05:31.22', 'XAUUSD', 'XAUUSD', 'XAU', 'USD', 0, 0, 1, 4241.510000,
                        1000, 0.1000, 42415.10, 0.00000000, 0.00000000, 0.00000000, 0.0000, 0.0000, 0.0000, 0, 38879438, '', 0.000000,
                        0.000000, 4241.510000, 4241.510000, 4241.689999, 1.000000, 0, '2025-12-01 15:05:31.22', 0,
                        1000, 210.00000000, 522.24000000, 156.94000000, 365.30000000, 210.0000, 522.2400, 156.9400, 365.3000),
                       ('clientUcid', clientAccount, 'MT5', clientServerId, 'MT5AU3', '', '', '', 38272044, 38880359, 1, 0, 2, 100,
                        '2025-12-01 17:07:21.68', '2025-12-01 15:07:21.68', 'XAUUSD', 'XAUUSD', 'XAU', 'USD', 0, 0, 1, 4242.649999,
                        800, 0.0800, 33941.19, 0.00000000, 0.00000000, 0.00000000, 0.0000, 0.0000, 0.0000, 0, 38880359, '', 0.000000,
                        0.000000, 4242.649999, 4242.649999, 4242.840000, 1.000000, 0, '2025-12-01 15:07:21.68', 0,
                        1000, 210.00000000, 478.17000000, 190.88000000, 287.29000000, 210.0000, 478.1700, 190.8800, 287.2900),
                       ('clientUcid', clientAccount, 'MT5', clientServerId, 'MT5AU3', '', '', '', 38272704, 38881030, 1, 0, 2, 100,
                        '2025-12-01 17:09:09.31', '2025-12-01 15:09:09.31', 'XAUUSD', 'XAUUSD', 'XAU', 'USD', 0, 0, 1, 4239.910000,
                        700, 0.0700, 29679.37, 0.00000000, 0.00000000, 0.00000000, 0.0000, 0.0000, 0.0000, 0, 38881030, '', 0.000000,
                        0.000000, 4239.910000, 4239.910000, 4240.100000, 1.000000, 0, '2025-12-01 15:09:09.31', 0,
                        1000, 210.00000000, 600.14000000, 220.56000000, 379.58000000, 210.0000, 600.1400, 220.5600, 379.5800),
                       ('clientUcid', clientAccount, 'MT5', clientServerId, 'MT5AU3', '', '', '', 38276541, 38884903, 0, 1, 2, 100,
                        '2025-12-01 17:11:42.96', '2025-12-01 15:11:42.96', 'XAUUSD', 'XAUUSD', 'XAU', 'USD', 0, 0, 1, 4232.779999,
                        1500, 0.1500, 63491.70, 122.25000000, 0.00000000, 0.00000000, 122.2500, 0.0000, 0.0000, 0, 38874826, '',
                        0.000000, 0.000000, 4232.779999, 4232.430000, 4232.609999, 1.000000, 0, '2025-12-01 15:11:42.96', 0, 1000,
                        332.25000000, 987.07000000, 156.95000000, 830.12000000, 332.2500, 987.0700, 156.9499,
                        830.1200),
                       ('clientUcid', clientAccount, 'MT5', clientServerId, 'MT5AU3', '', '', '', 38276658, 38885015, 0, 1, 2, 100,
                        '2025-12-01 17:11:46.62', '2025-12-01 15:11:46.62', 'XAUUSD', 'XAUUSD', 'XAU', 'USD', 0, 0, 1, 4232.370000,
                        1200, 0.1200, 50788.44, 122.28000000, 0.00000000, 0.00000000, 122.2800, 0.0000, 0.0000, 0, 38877757, '',
                        0.000000, 0.000000, 4232.370000, 4231.930000, 4232.109999, 1.000000, 0, '2025-12-01 15:11:46.62', 0, 1000,
                        454.53000000, 1004.20000000, 106.04000000, 898.16000000, 454.5300, 1004.2000, 106.0400,
                        898.1600),
                       ('clientUcid', clientAccount, 'MT5', clientServerId, 'MT5AU3', '', '', '', 38276838, 38885198, 0, 1, 2, 100,
                        '2025-12-01 17:11:49.81', '2025-12-01 15:11:49.81', 'XAUUSD', 'XAUUSD', 'XAU', 'USD', 0, 0, 1, 4232.090000,
                        1000, 0.1000, 42320.90, 94.20000000, 0.00000000, 0.00000000, 94.2000, 0.0000, 0.0000, 0, 38879438, '', 0.000000,
                        0.000000, 4232.090000, 4231.520000, 4231.710000, 1.000000, 0, '2025-12-01 15:11:49.81', 0,
                        1000, 548.73000000, 1008.65000000, 63.62000000, 945.03000000, 548.7300, 1008.6500, 63.6200, 945.0300);
                """;

        String query3 =
                """

                INSERT INTO consolidated.crm___tb_deposit (source_id_st, brand_uid, brand, regulator, user_id, ucid, account,
                                                           transfer_id, create_time, create_time_utc, update_time, update_time_utc,
                                                           amount_submitted, amount_submitted_usd, amount, amount_usd, currency,
                                                           status_id, status, status_group, payment_type_id, payment_type,
                                                           payment_channel_id, payment_channel, payment_family, payment_system_account,
                                                           payment_system_currency, last_updated)
                VALUES (5, 1, 'clientBrand', 'clientRegulator', clientId, 'clientUcid', clientAccount, 13396187, '2025-12-01 16:45:07',
                        '2025-12-01 14:45:07', '2025-12-01 16:48:10', '2025-12-01 14:48:10', 210.00000000, 210.00, 210.00000000, 210.00,
                        'USD', 5, 'Success', 'Success', 15, 'Cryptocurrency', 7, 'USDT(TRC20)-CPS', 'Crypto', '', 'USD',
                        '2025-12-01 14:48:11');

                """;

        String query4 =
                """
                INSERT INTO consolidated.mt___credit_orders (ticket, server_id, server_name, ucid, brand, regulator, user_id, platform,
                                                             account, currency, create_time, create_time_utc, amount, rate_to_usd,
                                                             amount_usd, is_deleted, last_updated)
                VALUES (43242151, clientServerId, 'MT5AU3', 'clientUcid', 'clientBrand', 'clientRegulator', clientId, 'MT5', clientAccount, 'USD',
                        '2025-12-01 16:58:24.26', '2025-12-01 14:58:24.26', 315.00000000, 1, 315.0000, 0,
                        '2025-12-01 14:58:23'),
                       (43242152, clientServerId, 'MT5AU3', 'clientUcid', 'clientBrand', 'clientRegulator', clientId, 'MT5', clientAccount, 'USD',
                        '2025-12-01 23:15:22.34', '2025-12-01 21:15:22.34', -315.00000000, 1, -315.0000,
                        0, '2025-12-01 21:15:21');
                """;

        executeQueryToDb(DbName.CLICKHOUSE, transformQuery(query, client));
        executeQueryToDb(DbName.CLICKHOUSE, transformQuery(query2, client));
        executeQueryToDb(DbName.CLICKHOUSE, transformQuery(query3, client));
        executeQueryToDb(DbName.CLICKHOUSE, transformQuery(query4, client));
        Thread.sleep(10_000);
    }

    protected static String transformQuery(String rawQuery, ClientHelper client) {
        return rawQuery.replace("clientBrand", client.getBrand())
                .replace("clientRegulator", client.getRegulator())
                .replace("clientId", client.getUserId().toString())
                .replace("clientAccount", client.getTradingAccount().toString())
                .replace("clientServerId", client.getServerId().toString())
                .replace("clientUcid", client.getUcid());
    }

    public static void deleteWaveFlagData(ClientHelper client) {
        try {
            deleteObjectFromDb(CRM_TB_ACCOUNT_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        } catch (Exception e) {
            writeLog(e.getMessage());
        }
        deleteObjectFromDb(CRM_DEPOSIT_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteObjectFromDb(MT_CREDITS_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteObjectFromDb(MT5_DEALS_COERCED_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
    }
}
