package helpers.data.rules;

import helpers.data.ClientHelper;
import helpers.database.DbName;

import static helpers.database.DbHelper.deleteEntryFromDb;
import static helpers.database.DbHelper.executeQueryToDb;
import static utils.Constants.*;
import static utils.Utils.writeLog;

public class MirrorFlagDataInserter {
    public static void insertMirrorFlagData(ClientHelper client) {
        String rawQuerryAccount = """
                INSERT INTO vindex_test.crm___tb_account
                (source_id_st, brand_uid, brand, regulator, user_id, ucid, account, server_id_st, server_name, account_type_id,
                 account_type, account_group, platform, create_time, create_time_utc, create_date, create_date_utc, account_status,
                 last_login, last_login_utc, last_order, last_order_utc, balance, currency, balance_usd, equity, credit, pnl, leverage,
                 margin_free, is_rebate_account, rebate_account_nr, ib_id, p_id, is_swap_free, is_pamm, is_cent, is_archive, is_hidden,
                 is_del, is_deleted, internal_comment, last_updated, is_test)
                VALUES (12, 7, 'clientBrand', 'clientRegulator', clientId, 'clientUcid', clientAccount, clientServerId, 'UM2', 1, 'Standard STP',
                        'M_UM_EUR', 'MT4', '2025-01-19 06:03:36.000', '2025-01-19 04:03:36.000', '2025-01-19', '2025-01-19', 'Inactive',
                        NULL, NULL, NULL, NULL, 0.00, 'EUR', 0.00, 0.00, 0.00, 0.00, 0, 0.00, 0, NULL, NULL, 880001, 0, 0, 0, 1, 0, 0,
                        0, 'mv', '2025-08-03 07:43:19.000', 0);
                        """;
        String rawQuerryDeposit = """
                INSERT INTO vindex_test.crm___tb_deposit
                (source_id_st, brand_uid, brand, regulator, user_id, ucid, account, transfer_id, create_time, create_time_utc,
                 update_time, update_time_utc, amount, amount_usd, currency, status_id, status, payment_type_id, payment_type,
                 payment_channel_id, payment_channel, payment_system_account, payment_system_currency, payment_details,
                 payment_expiration_date, ticket, fee, processed_notes, is_del, is_non_app)
                VALUES (12, 7, 'clientBrand', 'clientRegulator', clientId, 'clientUcid', clientAccount, 305780, '2025-01-19 06:05:30.000',
                        '2025-01-19 04:05:30.000', '2025-01-19 06:12:36.000', '2025-01-19 04:12:36.000', 999.93, 1027.12, 'EUR', 5,
                        'Success', 15, 'Cryptocurrency', 7, 'USDT(TRC20)-CPS', '', 'USD', 'usdt', '0/0', '7990860', 0.00,
                        'Automatic Deposit', 0, 1);
                        """;
        String rawQuerryBalance = """
                INSERT INTO vindex_test.mt___balance_orders
                (ticket, server_id, server_name, ucid, brand, regulator, user_id, platform, account, currency, create_time, create_time_utc, amount, rate_to_usd, amount_usd, order_type, `comment`, is_deleted, last_updated, internal_comment)
                VALUES
                    (7990860, clientServerId, 'UM2', 'clientUcid', 'clientBrand', 'clientRegulator', clientId, 'MT4', clientAccount, 'EUR', '2025-01-19 06:12:36.000', '2025-01-19 04:12:36.000', 999.9300, 1.0272000000000001, 1027.1280, 'unknown', 'Deposit-Crypto-USDT(TRC20)-CPS', 1, '2025-08-04 00:13:17.000', 'mv'),
                    (17002884, clientServerId, 'UM2', 'clientUcid', 'clientBrand', 'clientRegulator', clientId, 'MT4', clientAccount, 'EUR', '2025-04-11 03:45:20.000', '2025-04-11 00:45:20.000', -1703.4100, 1.13584, -1934.8012, 'unknown', 'Withdraw-USDC(SOL)-CPS', 1, '2025-08-04 00:14:56.000', 'mv'),
                    (17549798, clientServerId, 'UM2', 'clientUcid', 'clientBrand', 'clientRegulator', clientId, 'MT4', clientAccount, 'EUR', '2025-04-21 05:20:39.000', '2025-04-21 02:20:39.000', 971.0200, 1.15154, 1118.1683, 'unknown', 'Cash Adjustment - Debt W/O', 1, '2025-08-04 00:15:07.000', 'mv'),
                    (17867434, clientServerId, 'UM2', 'clientUcid', 'clientBrand', 'clientRegulator', clientId, 'MT4', clientAccount, 'EUR', '2025-04-24 06:58:14.000', '2025-04-24 03:58:14.000', 27.5900, 1.13897, 31.4241, 'unknown', 'Cash Adjustment - Debt W/O', 1, '2025-08-04 00:15:12.000', 'mv'),
                    (7990860, clientServerId, 'UM2', 'clientUcid', 'clientBrand', 'clientRegulator', clientId, 'MT4', clientAccount, 'EUR', '2025-01-19 06:12:36.000', '2025-01-19 04:12:36.000', 999.9300, 1.0272000000000001, 1027.1280, 'unknown', 'Deposit-Crypto-USDT(TRC20)-CPS', 0, '2025-01-19 04:12:36.000', 'mv'),
                    (17002884, clientServerId, 'UM2', 'clientUcid', 'clientBrand', 'clientRegulator', clientId, 'MT4', clientAccount, 'EUR', '2025-04-11 03:45:20.000', '2025-04-11 00:45:20.000', -1703.4100, 1.119655, -1907.2315, 'unknown', 'Withdraw-USDC(SOL)-CPS', 0, '2025-04-11 00:45:20.000', 'mv'),
                    (17549798, clientServerId, 'UM2', 'clientUcid', 'clientBrand', 'clientRegulator', clientId, 'MT4', clientAccount, 'EUR', '2025-04-21 05:20:39.000', '2025-04-21 02:20:39.000', 971.0200, 1.14403, 1110.8760, 'unknown', 'Cash Adjustment - Debt W/O', 0, '2025-04-21 02:20:40.000', 'mv'),
                    (17867434, clientServerId, 'UM2', 'clientUcid', 'clientBrand', 'clientRegulator', clientId, 'MT4', clientAccount, 'EUR', '2025-04-24 06:58:14.000', '2025-04-24 03:58:14.000', 27.5900, 1.1345, 31.3008, 'unknown', 'Cash Adjustment - Debt W/O', 0, '2025-04-24 03:58:24.000', 'mv');
                """;

        String rawQuerryCredit = """
                INSERT INTO vindex_test.mt___credit_orders
                (ticket, server_id, server_name, ucid, brand, regulator, user_id, platform, account, currency,
                 create_time, create_time_utc, amount, rate_to_usd, amount_usd, `comment`, is_deleted, last_updated, internal_comment)
                VALUES
                    (7995230, clientServerId, 'UM2', 'clientUcid', 'clientBrand', 'clientRegulator', clientId, 'MT4', clientAccount, 'EUR',
                     '2025-01-19 09:45:11.000', '2025-01-19 07:45:11.000', 1000.0000, 1.0272000000000001, 1027.2000,
                     'Credit In - XMas&NewYear 100/20', 1, '2025-08-04 00:13:17.000', 'mv'),

                    (17549799, clientServerId, 'UM2', 'clientUcid', 'clientBrand', 'clientRegulator', clientId, 'MT4', clientAccount, 'EUR',
                     '2025-04-21 05:20:39.000', '2025-04-21 02:20:39.000', -971.0200, 1.15154, -1118.1683,
                     'Credit Out - Debt W/O', 1, '2025-08-04 00:15:07.000', 'mv'),

                    (17867435, clientServerId, 'UM2', 'clientUcid', 'clientBrand', 'clientRegulator', clientId, 'MT4', clientAccount, 'EUR',
                     '2025-04-24 06:58:15.000', '2025-04-24 03:58:15.000', -27.5900, 1.13897, -31.4241,
                     'Credit Out - Debt W/O', 1, '2025-08-04 00:15:12.000', 'mv'),

                    (7995230, clientServerId, 'UM2', 'clientUcid', 'clientBrand', 'clientRegulator', clientId, 'MT4', clientAccount, 'EUR',
                     '2025-01-19 09:45:11.000', '2025-01-19 07:45:11.000', 1000.0000, 1.0272000000000001, 1027.2000,
                     'Credit In - XMas&NewYear 100/20', 0, '2025-01-19 07:46:56.000', 'mv'),

                    (17549799, clientServerId, 'UM2', 'clientUcid', 'clientBrand', 'clientRegulator', clientId, 'MT4', clientAccount, 'EUR',
                     '2025-04-21 05:20:39.000', '2025-04-21 02:20:39.000', -971.0200, 1.144135, -1110.9779,
                     'Credit Out - Debt W/O', 0, '2025-04-21 02:20:40.000', 'mv'),

                    (17867435, clientServerId, 'UM2', 'clientUcid', 'clientBrand', 'clientRegulator', clientId, 'MT4', clientAccount, 'EUR',
                     '2025-04-24 06:58:15.000', '2025-04-24 03:58:15.000', -27.5900, 1.1345, -31.3008,
                     'Credit Out - Debt W/O', 0, '2025-04-24 03:58:24.000', 'mv');
                """;

        String rawQuerryEquity = """
                INSERT INTO vindex_test.mt___equity_history
                (brand, regulator, user_id, ucid, account, platform, server_id, server_name, account_type, account_group, account_currency, deal, `order`, `action`, entry, reason, contract_size, `time`, time_utc, symbol, symbol_underlying, base_currency, quote_currency, rate_usd_base, rate_usd_quote, rate_usd_acc, price, volume, volume_lots, notional_value_usd, profit, storage, commission, profit_usd, storage_usd, commission_usd, expert_id, position_id, `comment`, sl, tp, price_gateway, market_bid, market_ask, rate_profit, is_deleted, last_updated, long_short, balance_usd_cum, credit_usd_cum, open_positions, open_positions_nv_usd, floating_usd, equity_usd, insert_ts)
                VALUES('clientBrand', 'clientRegulator', clientId, 'clientUcid', clientAccount, 'MT4', clientServerId, 'UM2', 'Standard STP', 'M_UM_EUR', 'EUR', 10650698, 10650698, 1, 0, 5, 100000, '2025-02-12 19:42:58.000', '2025-02-12 17:42:58.000', 'EURUSD', 'EURUSD', 'EUR', 'USD', 1.037765, 1.0, 1.037765, 1.037800, 1, 0.0100, 1037.80, 0.00, 0.00, 0.00, 0.0000, 0.0000, 0.0000, 0, 10650698, '', 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0, '2025-02-12 17:48:17.000', 'short', 1027.1280, 1027.2000, '[[EURUSD, short, -0.0100, -1037.8]]', -1037.80, 0.0000, 2054.3280, '2025-08-08 01:04:12.757'),
                    ('clientBrand', 'clientRegulator', clientId, 'clientUcid', clientAccount, 'MT4', clientServerId, 'UM2', 'Standard STP', 'M_UM_EUR', 'EUR', 10650698, 10650698, 0, 1, 5, 100000, '2025-02-12 19:46:06.000', '2025-02-12 17:46:06.000', 'EURUSD', 'EURUSD', 'EUR', 'USD', 1.037765, 1.0, 1.037765, 1.037300, 1, 0.0100, 1037.30, 0.48, 0.00, 0.00, 0.4981, 0.0000, 0.0000, 0, 10650698, '', 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0, '2025-02-12 17:48:17.000', 'short', 1027.6261, 1027.2000, '[]', 0.00, 0.0000, 2054.8261, '2025-08-08 01:04:12.757'),
                    ('clientBrand', 'clientRegulator', clientId, 'clientUcid', clientAccount, 'MT4', clientServerId, 'UM2', 'Standard STP', 'M_UM_EUR', 'EUR', 10651527, 10651527, 1, 0, 5, 100000, '2025-02-12 19:47:22.000', '2025-02-12 17:47:22.000', 'EURUSD', 'EURUSD', 'EUR', 'USD', 1.03782, 1.0, 1.03782, 1.037199, 10, 0.1000, 10371.99, 0.00, 0.00, 0.00, 0.0000, 0.0000, 0.0000, 0, 10651527, '', 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0, '2025-02-12 17:51:41.000', 'short', 1027.6261, 1027.2000, '[[EURUSD, short, -0.1000, -10371.99]]', -10371.99, 0.0000, 2054.8261, '2025-08-08 01:04:12.757'),
                    ('clientBrand', 'clientRegulator', clientId, 'clientUcid', clientAccount, 'MT4', clientServerId, 'UM2', 'Standard STP', 'M_UM_EUR', 'EUR', 10651527, 10651527, 0, 1, 5, 100000, '2025-02-12 19:50:02.000', '2025-02-12 17:50:02.000', 'EURUSD', 'EURUSD', 'EUR', 'USD', 1.03782, 1.0, 1.03782, 1.037830, 10, 0.1000, 10378.30, -6.07, 0.00, 0.00, -6.2995, 0.0000, 0.0000, 0, 10651527, '', 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0, '2025-02-12 17:51:41.000', 'short', 1021.3266, 1027.2000, '[]', 0.00, 0.0000, 2048.5266, '2025-08-08 01:04:12.757'),
                    ('clientBrand', 'clientRegulator', clientId, 'clientUcid', clientAccount, 'MT4', clientServerId, 'UM2', 'Standard STP', 'M_UM_EUR', 'EUR', 10653196, 10653196, 1, 0, 5, 100000, '2025-02-12 19:56:09.000', '2025-02-12 17:56:09.000', 'EURUSD', 'EURUSD', 'EUR', 'USD', 1.03824, 1.0, 1.03824, 1.037290, 10, 0.1000, 10372.90, 0.00, 0.00, 0.00, 0.0000, 0.0000, 0.0000, 0, 10653196, '', 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0, '2025-02-13 23:40:40.000', 'short', 1021.3266, 1027.2000, '[[EURUSD, short, -0.1000, -10372.9]]', -10372.90, 0.0000, 2048.5266, '2025-08-08 01:04:12.757'),
                    ('clientBrand', 'clientRegulator', clientId, 'clientUcid', clientAccount, 'MT4', clientServerId, 'UM2', 'Standard STP', 'M_UM_EUR', 'EUR', 10653196, 10653196, 0, 1, 5, 100000, '2025-02-14 01:40:40.000', '2025-02-13 23:40:40.000', 'EURUSD', 'EURUSD', 'EUR', 'USD', 1.046415, 1.0, 1.046415, 1.046330, 10, 0.1000, 10463.29, -86.40, 0.75, 0.00, -90.4102, 0.7848, 0.0000, 0, 10653196, '', 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0, '2025-02-13 23:40:40.000', 'short', 931.7012, 1027.2000, '[]', 0.00, 0.0000, 1958.9012, '2025-08-08 01:12:38.783'),
                    ('clientBrand', 'clientRegulator', clientId, 'clientUcid', clientAccount, 'MT4', clientServerId, 'UM2', 'Standard STP', 'M_UM_EUR', 'EUR', 11658845, 11658845, 0, 0, 5, 100000, '2025-02-21 18:39:17.000', '2025-02-21 16:39:17.000', 'EURUSD', 'EURUSD', 'EUR', 'USD', 1.04602, 1.0, 1.04602, 1.047530, 40, 0.4000, 41901.20, 0.00, 0.00, 0.00, 0.0000, 0.0000, 0.0000, 0, 11658845, '', 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0, '2025-02-27 23:49:22.000', 'long', 931.7012, 1027.2000, '[[EURUSD, long, 0.4000, 41901.2]]', 41901.20, 0.0000, 1958.9012, '2025-08-08 01:50:24.044'),
                    ('clientBrand', 'clientRegulator', clientId, 'clientUcid', clientAccount, 'MT4', clientServerId, 'UM2', 'Standard STP', 'M_UM_EUR', 'EUR', 11658845, 11658845, 1, 1, 5, 100000, '2025-02-28 01:49:20.000', '2025-02-27 23:49:20.000', 'EURUSD', 'EURUSD', 'EUR', 'USD', 1.039815, 1.0, 1.039815, 1.039750, 40, 0.4000, 41590.00, -299.30, -13.55, 0.00, -311.2166, -14.0894, 0.0000, 0, 11658845, '', 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0, '2025-02-27 23:49:22.000', 'long', 606.3952, 1027.2000, '[]', 0.00, 0.0000, 1633.5952, '2025-08-08 02:18:47.763'),
                    ('clientBrand', 'clientRegulator', clientId, 'clientUcid', clientAccount, 'MT4', clientServerId, 'UM2', 'Standard STP', 'M_UM_EUR', 'EUR', 12438907, 12438907, 1, 0, 5, 100000, '2025-02-28 01:50:47.000', '2025-02-27 23:50:47.000', 'EURUSD', 'EURUSD', 'EUR', 'USD', 1.039815, 1.0, 1.039815, 1.039730, 30, 0.3000, 31191.90, 0.00, 0.00, 0.00, 0.0000, 0.0000, 0.0000, 0, 12438907, '', 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0, '2025-03-03 18:11:31.000', 'short', 606.3952, 1027.2000, '[[EURUSD, short, -0.3000, -31191.9]]', -31191.90, 0.0000, 1633.5952, '2025-08-08 02:18:47.763'),
                    ('clientBrand', 'clientRegulator', clientId, 'clientUcid', clientAccount, 'MT4', clientServerId, 'UM2', 'Standard STP', 'M_UM_EUR', 'EUR', 12438907, 12438907, 0, 1, 5, 100000, '2025-03-03 20:08:06.000', '2025-03-03 18:08:06.000', 'EURUSD', 'EURUSD', 'EUR', 'USD', 1.049145, 1.0, 1.049145, 1.049160, 30, 0.3000, 31474.80, -269.64, 1.50, 0.00, -282.8914, 1.5737, 0.0000, 0, 12438907, '', 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0, '2025-03-03 18:11:31.000', 'short', 325.0775, 1027.2000, '[]', 0.00, 0.0000, 1352.2775, '2025-08-08 02:32:04.442'),
                    ('clientBrand', 'clientRegulator', clientId, 'clientUcid', clientAccount, 'MT4', clientServerId, 'UM2', 'Standard STP', 'M_UM_EUR', 'EUR', 12830157, 12830157, 0, 0, 5, 100000, '2025-03-03 20:10:08.000', '2025-03-03 18:10:08.000', 'EURUSD', 'EURUSD', 'EUR', 'USD', 1.04869, 1.0, 1.04869, 1.049350, 40, 0.4000, 41974.00, 0.00, 0.00, 0.00, 0.0000, 0.0000, 0.0000, 0, 12830157, '', 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0, '2025-03-10 01:50:48.000', 'long', 325.0775, 1027.2000, '[[EURUSD, long, 0.4000, 41974.0]]', 41974.00, 0.0000, 1352.2775, '2025-08-08 02:32:04.442'),
                    ('clientBrand', 'clientRegulator', clientId, 'clientUcid', clientAccount, 'MT4', clientServerId, 'UM2', 'Standard STP', 'M_UM_EUR', 'EUR', 12830157, 12830157, 1, 1, 5, 100000, '2025-03-10 04:39:50.000', '2025-03-10 01:39:50.000', 'EURUSD', 'EURUSD', 'EUR', 'USD', 1.0860599999999998, 1.0, 1.0860599999999998, 1.085950, 40, 0.4000, 43438.00, 1348.13, -16.00, 0.00, 1464.1500, -17.3769, 0.0000, 0, 12830157, '', 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0, '2025-03-10 01:50:48.000', 'long', 1771.8506, 1027.2000, '[]', 0.00, 0.0000, 2799.0506, '2025-08-08 03:02:03.063'),
                    ('clientBrand', 'clientRegulator', clientId, 'clientUcid', clientAccount, 'MT4', clientServerId, 'UM2', 'Standard STP', 'M_UM_EUR', 'EUR', 13678842, 13678842, 0, 0, 5, 100000, '2025-03-10 04:47:12.000', '2025-03-10 01:47:12.000', 'USDJPY', 'USDJPY', 'USD', 'JPY', 1.0, 0.00679029530994303, 1.08354, 147.507000, 25, 0.2500, 25040.40, 0.00, 0.00, 0.00, 0.0000, 0.0000, 0.0000, 0, 13678842, '', 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0, '2025-03-12 01:37:29.000', 'long', 1771.8506, 1027.2000, '[[USDJPY, long, 0.2500, 25040.402257094163]]', 25040.40, 0.0023, 2799.0529, '2025-08-08 03:02:03.063'),
                    ('clientBrand', 'clientRegulator', clientId, 'clientUcid', clientAccount, 'MT4', clientServerId, 'UM2', 'Standard STP', 'M_UM_EUR', 'EUR', 13678842, 13678842, 1, 1, 5, 100000, '2025-03-12 04:37:29.000', '2025-03-12 01:37:29.000', 'USDJPY', 'USDJPY', 'USD', 'JPY', 1.0, 0.006762628443539143, 1.0915650000000001, 147.855000, 25, 0.2500, 24997.21, 53.90, 1.71, 0.00, 58.8353, 1.8665, 0.0000, 0, 13678842, '', 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0, '2025-03-12 01:37:29.000', 'long', 1832.5524, 1027.2000, '[]', 0.00, 0.0000, 2859.7524, '2025-08-08 03:15:04.499'),
                    ('clientBrand', 'clientRegulator', clientId, 'clientUcid', clientAccount, 'MT4', clientServerId, 'UM2', 'Standard STP', 'M_UM_EUR', 'EUR', 14162774, 14162774, 1, 0, 5, 100000, '2025-03-13 04:39:54.000', '2025-03-13 01:39:54.000', 'EURAUD', 'EURAUD', 'EUR', 'AUD', 1.085255, 0.6285050000000001, 1.085255, 1.721050, 35, 0.3500, 37859.09, 0.00, 0.00, 0.00, 0.0000, 0.0000, 0.0000, 0, 14162774, '', 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0, '2025-03-18 01:52:23.000', 'short', 1832.5524, 1027.2000, '[[EURAUD, short, -0.3500, -37859.09855875]]', -37859.10, -0.0086, 2859.7438, '2025-08-08 03:21:26.579'),
                    ('clientBrand', 'clientRegulator', clientId, 'clientUcid', clientAccount, 'MT4', clientServerId, 'UM2', 'Standard STP', 'M_UM_EUR', 'EUR', 14162774, 14162774, 0, 1, 5, 100000, '2025-03-18 04:52:23.000', '2025-03-18 01:52:23.000', 'EURAUD', 'EURAUD', 'EUR', 'AUD', 1.091955, 0.638455, 1.091955, 1.710450, 35, 0.3500, 38221.58, 216.90, 1.74, 0.00, 236.8450, 1.9000, 0.0000, 0, 14162774, '', 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0, '2025-03-18 01:52:23.000', 'short', 2071.2974, 1027.2000, '[]', 0.00, 0.0000, 3098.4974, '2025-08-08 03:41:36.894'),
                    ('clientBrand', 'clientRegulator', clientId, 'clientUcid', clientAccount, 'MT4', clientServerId, 'UM2', 'Standard STP', 'M_UM_EUR', 'EUR', 14547045, 14547045, 0, 0, 5, 100000, '2025-03-18 04:53:50.000', '2025-03-18 01:53:50.000', 'AUDUSD', 'AUDUSD', 'AUD', 'USD', 0.63617, 1.0, 1.094495, 0.638440, 40, 0.4000, 25537.60, 0.00, 0.00, 0.00, 0.0000, 0.0000, 0.0000, 0, 14547045, '', 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0, '2025-04-09 18:39:43.000', 'long', 2071.2974, 1027.2000, '[[AUDUSD, long, 0.4000, 25537.6]]', 25537.60, 0.0000, 3098.4974, '2025-08-08 03:41:36.894'),
                    ('clientBrand', 'clientRegulator', clientId, 'clientUcid', clientAccount, 'MT4', clientServerId, 'UM2', 'Standard STP', 'M_UM_EUR', 'EUR', 14547045, 14547045, 1, 1, 5, 100000, '2025-04-09 21:29:23.000', '2025-04-09 18:29:23.000', 'AUDUSD', 'AUDUSD', 'AUD', 'USD', 0.599935, 1.0, 1.105475, 0.601340, 40, 0.4000, 24053.60, -1341.86, -8.77, 0.00, -1483.3926, -9.7060, 0.0000, 0, 14547045, '', 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0, '2025-04-09 18:39:43.000', 'long', 578.1988, 1027.2000, '[]', 0.00, 0.0000, 1605.3988, '2025-08-08 05:35:00.233'),
                    ('clientBrand', 'clientRegulator', clientId, 'clientUcid', clientAccount, 'MT4', clientServerId, 'UM2', 'Standard STP', 'M_UM_EUR', 'EUR', 16830787, 16830787, 1, 0, 5, 100000, '2025-04-09 21:33:50.000', '2025-04-09 18:33:50.000', 'EURUSD', 'EURUSD', 'EUR', 'USD', 1.098525, 1.0, 1.098525, 1.106230, 50, 0.5000, 55311.50, 0.00, 0.00, 0.00, 0.0000, 0.0000, 0.0000, 0, 16830787, '', 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0, '2025-04-09 20:31:45.000', 'short', 578.1988, 1027.2000, '[[EURUSD, short, -0.5000, -55311.5]]', -55311.50, 0.0000, 1605.3988, '2025-08-08 05:35:00.233'),
                    ('clientBrand', 'clientRegulator', clientId, 'clientUcid', clientAccount, 'MT4', clientServerId, 'UM2', 'Standard STP', 'M_UM_EUR', 'EUR', 16830787, 16830787, 0, 1, 5, 100000, '2025-04-09 23:25:18.000', '2025-04-09 20:25:18.000', 'EURUSD', 'EURUSD', 'EUR', 'USD', 1.098525, 1.0, 1.098525, 1.098300, 50, 0.5000, 54915.00, 361.01, 0.00, 0.00, 396.5785, 0.0000, 0.0000, 0, 16830787, '', 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0, '2025-04-09 20:31:45.000', 'short', 2907.4240, 1027.2000, '[]', 0.00, 0.0000, 3934.6240, '2025-08-08 05:35:34.708'),
                    ('clientBrand', 'clientRegulator', clientId, 'clientUcid', clientAccount, 'MT4', clientServerId, 'UM2', 'Standard STP', 'M_UM_EUR', 'EUR', 16830805, 16830805, 1, 0, 5, 100000, '2025-04-09 21:34:03.000', '2025-04-09 18:34:03.000', 'EURUSD', 'EURUSD', 'EUR', 'USD', 1.098525, 1.0, 1.098525, 1.106290, 50, 0.5000, 55314.50, 0.00, 0.00, 0.00, 0.0000, 0.0000, 0.0000, 0, 16830805, '', 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0, '2025-04-09 20:31:44.000', 'short', 578.1988, 1027.2000, '[[EURUSD, short, -1.0000, -110629.0]]', -110629.00, -3.0000, 1602.3988, '2025-08-08 05:35:00.233'),
                    ('clientBrand', 'clientRegulator', clientId, 'clientUcid', clientAccount, 'MT4', clientServerId, 'UM2', 'Standard STP', 'M_UM_EUR', 'EUR', 16830805, 16830805, 0, 1, 5, 100000, '2025-04-09 23:25:05.000', '2025-04-09 20:25:05.000', 'EURUSD', 'EURUSD', 'EUR', 'USD', 1.098525, 1.0, 1.098525, 1.096960, 50, 0.5000, 54848.00, 425.27, 0.00, 0.00, 467.1697, 0.0000, 0.0000, 0, 16830805, '', 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0, '2025-04-09 20:31:44.000', 'short', 2510.8455, 1027.2000, '[[EURUSD, short, -0.5000, -54848.0]]', -54848.00, 462.3533, 4000.3988, '2025-08-08 05:35:34.708'),
                    ('clientBrand', 'clientRegulator', clientId, 'clientUcid', clientAccount, 'MT4', clientServerId, 'UM2', 'Standard STP', 'M_UM_EUR', 'EUR', 16830813, 16830813, 1, 0, 5, 100000, '2025-04-09 21:34:11.000', '2025-04-09 18:34:11.000', 'EURUSD', 'EURUSD', 'EUR', 'USD', 1.098525, 1.0, 1.098525, 1.106300, 50, 0.5000, 55315.00, 0.00, 0.00, 0.00, 0.0000, 0.0000, 0.0000, 0, 16830813, '', 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0, '2025-04-09 20:31:03.000', 'short', 578.1988, 1027.2000, '[[EURUSD, short, -1.5000, -165945.0]]', -165945.00, -4.0000, 1601.3988, '2025-08-08 05:35:34.709');
                """;

        executeQueryToDb(DbName.CLICKHOUSE, trasformQuerry(rawQuerryAccount, client));
        executeQueryToDb(DbName.CLICKHOUSE, trasformQuerry(rawQuerryDeposit, client));
        executeQueryToDb(DbName.CLICKHOUSE, trasformQuerry(rawQuerryBalance, client));
        executeQueryToDb(DbName.CLICKHOUSE, trasformQuerry(rawQuerryCredit, client));
        executeQueryToDb(DbName.CLICKHOUSE, trasformQuerry(rawQuerryEquity, client));
    }

    protected static String trasformQuerry(String rawQuerry, ClientHelper client) {
        return rawQuerry.replace("clientBrand", client.getBrand()).replace("clientRegulator", client.getRegulator()).replace("clientId", client.getUserId().toString()).replace("clientAccount", client.getTradingAccount().toString()).replace("clientServerId", client.getServerId().toString()).replace("clientUcid", client.getUcid());
    }

    public static void deleteData(ClientHelper client) {
        try {
            deleteEntryFromDb(CRM_TB_ACCOUNT_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        } catch (Exception e) {
            writeLog(e.getMessage());
        }
        deleteEntryFromDb(CRM_DEPOSIT_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteEntryFromDb(MT_BALANCE_ORDERS_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteEntryFromDb(MT_CREDITS_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteEntryFromDb(EQUITY_HISTORY_TABLE, String.format("ucid = '%s'", client.getUcid()));
    }
}