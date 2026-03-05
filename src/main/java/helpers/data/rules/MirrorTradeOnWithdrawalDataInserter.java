package helpers.data.rules;

import static helpers.database.DbHelper.executeQueryToDb;

import helpers.data.ClientHelper;
import helpers.database.DbName;

public class MirrorTradeOnWithdrawalDataInserter {
    public static void insertMirrorTradeOnWithdrawalData(ClientHelper client) {

        String rawQuerryDeposit =
                """
                        INSERT INTO consolidated.crm___tb_deposit
                                 (source_id_st, brand_uid, brand, regulator, user_id, ucid, account, transfer_id, create_time, create_time_utc, update_time, update_time_utc, amount_submitted, amount_submitted_usd, amount, amount_usd, currency, status_id, status, status_group, payment_type_id, payment_type, payment_channel_id, payment_channel, payment_family, payment_system_account, payment_system_currency, payment_details, payment_expiration_date, ticket, v_wallet_account, fee, processed_notes, is_del, is_non_app, last_updated, credit_card_id, payment_profile, payment_profile_masked, payment_profile_key, is_deleted, card_holder_name, first_six_digits, three_domain_secure, order_number, allocated_fee_usd, country)
                                 VALUES(9, 3, 'clientBrand', 'clientRegulator', clientId, 'clientUcid', clientAccount, 7252063, '2026-02-02 14:42:36.000', '2026-02-02 12:42:36.000', '2026-02-02 14:44:58.000', '2026-02-02 12:44:58.000', 100.00000000, 100.00, 100.00000000, 100.00, 'USD', 5, 'Success', 'Success', 4, 'Thailand bank transfer', 479, 'Thailand-QR-CPS-APP', 'LBT', '', 'THB', '', '0/0', '5361904', '', 0.00000000, 'Automatic Deposit', 0, 0, '2026-02-02 12:44:57.000', 0, '', '', '', 0, '', '', '', 'VTSGclientAccount20260202124235', 3.37, 'THAILAND');
                """;
        String rawQuerryBalance =
                """
                        INSERT INTO consolidated.mt___balance_orders
                        (ticket, server_id, server_name, ucid, brand, regulator, user_id, platform, account, currency, create_time, create_time_utc, amount, rate_to_usd, amount_usd, order_type, `comment`, is_deleted, last_updated, internal_comment)
                        VALUES(5361904, clientServerId, 'MT5VT5', 'clientUcid', 'clientBrand', 'clientRegulator', clientId, 'MT5', clientAccount, 'USD', '2026-02-02 14:44:57.650', '2026-02-02 12:44:57.650', 100.00000000, 1.0, 100.0000, 'unknown', 'Deposit-TH-QR-CPS-APP', 0, '2026-02-02 12:44:57.000', 'mv'),
                        (5671972, clientServerId, 'MT5VT5', 'clientUcid', 'clientBrand', 'clientRegulator', clientId, 'MT5', clientAccount, 'USD', '2026-02-03 09:33:33.745', '2026-02-03 07:33:33.745', -205.60000000, 1.0, -205.6000, 'unknown', 'Withdraw-TH-CPS', 0, '2026-02-03 07:33:33.000', 'mv'),
                        (5695574, clientServerId, 'MT5VT5', 'clientUcid', 'clientBrand', 'clientRegulator', clientId, 'MT5', clientAccount, 'USD', '2026-02-03 10:45:09.789', '2026-02-03 08:45:09.789', 205.60000000, 1.0, 205.6000, 'unknown', 'Withdrawal Reversal', 0, '2026-02-03 08:45:09.000', 'mv'),
                        (6955255, clientServerId, 'MT5VT5', 'clientUcid', 'clientBrand', 'clientRegulator', clientId, 'MT5', clientAccount, 'USD', '2026-02-06 06:51:51.505', '2026-02-06 04:51:51.505', -205.60000000, 1.0, -205.6000, 'unknown', 'Withdraw-TH-CPS', 0, '2026-02-06 04:51:51.000', 'mv');
                        """;

        String rawQuerryCredit =
                """
                        INSERT INTO consolidated.mt___credit_orders
                        (ticket, server_id, server_name, ucid, brand, regulator, user_id, platform, account, currency, create_time, create_time_utc, amount, rate_to_usd, amount_usd, `comment`, is_deleted, last_updated, internal_comment)
                        VALUES(5429613, clientServerId, 'MT5VT5', 'clientUcid', 'clientBrand', 'clientRegulator', clientId, 'MT5', clientAccount, 'USD', '2026-02-02 17:42:24.420', '2026-02-02 15:42:24.420', 50.00000000, 1.0, 50.0000, 'Credit In-TH5020', 0, '2026-02-02 15:42:24.000', 'mv'),
                        (5671971, clientServerId, 'MT5VT5', 'clientUcid', 'clientBrand', 'clientRegulator', clientId, 'MT5', clientAccount, 'USD', '2026-02-03 09:33:33.086', '2026-02-03 07:33:33.086', -50.00000000, 1.0, -50.0000, 'Deduct Credit-AuditWithdrawal', 0, '2026-02-03 07:33:33.000', 'mv');
                        """;

        String rawQuerryDeals =
                """
                        INSERT INTO consolidated.mt___mt5_deals_coerced
                        (brand, regulator, user_id, ucid, account, platform, server_id, server_name, account_type, account_group, account_currency, deal, `order`, `action`, entry, reason, contract_size, `time`, time_utc, symbol, symbol_underlying, base_currency, quote_currency, rate_usd_base, rate_usd_quote, rate_usd_acc, price, volume, volume_lots, notional_value_usd, profit, storage, commission, profit_usd, storage_usd, commission_usd, expert_id, position_id, `comment`, sl, tp, price_gateway, market_bid, market_ask, rate_profit, is_deleted, last_updated, internal_comment)
                        VALUES('clientBrand', 'clientRegulator', clientId, 'clientUcid', clientAccount, 'MT5', clientServerId, 'MT5VT5', 'MT5 Swap Free STP', 'VT_Hedge\\M_VT_NM_S_USD', 'USD', 5668049, 6384567, 0, 0, 16, 100, '2026-02-03 09:22:21.281', '2026-02-03 07:22:21.281', 'XAUUSD-STD', 'XAUUSD', 'XAU', 'USD', 4822.85, 1.0, 1.0, 4822.950000, 1000, 0.1000, 48229.50, 0.00000000, 0.00000000, 0.00000000, 0.0000, 0.0000, 0.0000, 0, 6384567, '', 0.000000, 0.000000, 4822.950000, 4822.550000, 4822.750000, 1.000000, 0, '2026-02-03 07:22:21.000', 'mv mt5'),
                        ('clientBrand', 'clientRegulator', clientId, 'clientUcid', clientAccount, 'MT5', clientServerId, 'MT5VT5', 'MT5 Swap Free STP', 'VT_Hedge\\M_VT_NM_S_USD', 'USD', 5669738, 6386525, 1, 1, 16, 100, '2026-02-03 09:28:59.962', '2026-02-03 07:28:59.962', 'XAUUSD-STD', 'XAUUSD', 'XAU', 'USD', 4833.61, 1.0, 1.0, 4833.510000, 1000, 0.1000, 48335.10, 105.60000000, 0.00000000, 0.00000000, 105.6000, 0.0000, 0.0000, 0, 6384567, '', 0.000000, 0.000000, 4833.510000, 4833.510000, 4833.710000, 1.000000, 0, '2026-02-03 07:29:00.000', 'mv mt5');
                """;

        String rawQuerryDealsDD =
                """
                        INSERT INTO consolidated.mt___mt5_deals_coerced_dd
                                (brand, regulator, user_id, ucid, account, platform, server_id, server_name, account_type, account_group, account_currency, deal, `order`, `action`, entry, reason, contract_size, `time`, time_utc, symbol, symbol_underlying, base_currency, quote_currency, rate_usd_base, rate_usd_quote, rate_usd_acc, price, volume, volume_lots, notional_value_usd, profit, storage, commission, profit_usd, storage_usd, commission_usd, expert_id, position_id, `comment`, sl, tp, price_gateway, market_bid, market_ask, rate_profit, is_deleted, last_updated, internal_comment, is_abnormal_time, leverage, balance, equity, margin, free_margin, balance_usd, equity_usd, margin_usd, free_margin_usd, open_positions_nv_usd)
                                VALUES('', '', 0, '', clientAccount, 'MT5', clientServerId, 'MT5VT5', '', '', '', 5361904, 0, 2, 0, 2, 0, '2026-02-02 14:44:57.650', '2026-02-02 12:44:57.650', '', '', '', '', 0.0, 0.0, 1.0, 0.000000, 0, 0.0000, 0.00, 100.00000000, 0.00000000, 0.00000000, 100.0000, 0.0000, 0.0000, 0, 0, 'Deposit-TH-QR-CPS-APP', 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0, '2026-02-02 12:44:57.651', 'mv kafka mt5', 0, 500, 100.00000000, 100.00000000, 0.00000000, 100.00000000, 100.0000, 100.0000, 0.0000, 100.0000, 0.00),
                                ('', '', 0, '', clientAccount, 'MT5', clientServerId, 'MT5VT5', '', '', '', 5429613, 0, 3, 0, 2, 0, '2026-02-02 17:42:24.420', '2026-02-02 15:42:24.420', '', '', '', '', 0.0, 0.0, 1.0, 0.000000, 0, 0.0000, 0.00, 50.00000000, 0.00000000, 0.00000000, 50.0000, 0.0000, 0.0000, 0, 0, 'Credit In-TH5020', 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0, '2026-02-02 15:42:24.420', 'mv kafka mt5', 0, 500, 100.00000000, 150.00000000, 0.00000000, 150.00000000, 100.0000, 150.0000, 0.0000, 150.0000, 0.00),
                                ('', '', 0, '', clientAccount, 'MT5', clientServerId, 'MT5VT5', '', '', '', 5668049, 6384567, 0, 0, 16, 100, '2026-02-03 09:22:21.281', '2026-02-03 07:22:21.281', 'XAUUSD-STD', 'XAUUSD', 'XAU', 'USD', 0.0, 0.0, 1.0, 4822.950000, 1000, 0.1000, 48229.50, 0.00000000, 0.00000000, 0.00000000, 0.0000, 0.0000, 0.0000, 0, 6384567, '', 0.000000, 0.000000, 4822.950000, 4822.550000, 4822.750000, 1.000000, 0, '2026-02-03 07:22:21.282', 'mv kafka mt5', 0, 500, 100.00000000, 146.00000000, 96.46000000, 49.54000000, 100.0000, 146.0000, 96.4599, 49.5400, 0.00),
                                ('', '', 0, '', clientAccount, 'MT5', clientServerId, 'MT5VT5', '', '', '', 5669738, 6386525, 1, 1, 16, 100, '2026-02-03 09:28:59.962', '2026-02-03 07:28:59.962', 'XAUUSD-STD', 'XAUUSD', 'XAU', 'USD', 0.0, 0.0, 1.0, 4833.510000, 1000, 0.1000, 48335.10, 105.60000000, 0.00000000, 0.00000000, 105.6000, 0.0000, 0.0000, 0, 6384567, '', 0.000000, 0.000000, 4833.510000, 4833.510000, 4833.710000, 1.000000, 0, '2026-02-03 07:28:59.963', 'mv kafka mt5', 0, 500, 205.60000000, 255.60000000, 0.00000000, 255.60000000, 205.6000, 255.6000, 0.0000, 255.6000, 0.00),
                                ('', '', 0, '', clientAccount, 'MT5', clientServerId, 'MT5VT5', '', '', '', 5671971, 0, 3, 0, 2, 0, '2026-02-03 09:33:33.086', '2026-02-03 07:33:33.086', '', '', '', '', 0.0, 0.0, 1.0, 0.000000, 0, 0.0000, 0.00, -50.00000000, 0.00000000, 0.00000000, -50.0000, 0.0000, 0.0000, 0, 0, 'Deduct Credit-AuditWithdrawal', 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0, '2026-02-03 07:33:33.086', 'mv kafka mt5', 0, 500, 205.60000000, 205.60000000, 0.00000000, 205.60000000, 205.6000, 205.6000, 0.0000, 205.6000, 0.00),
                                ('', '', 0, '', clientAccount, 'MT5', clientServerId, 'MT5VT5', '', '', '', 5671972, 0, 2, 0, 2, 0, '2026-02-03 09:33:33.745', '2026-02-03 07:33:33.745', '', '', '', '', 0.0, 0.0, 1.0, 0.000000, 0, 0.0000, 0.00, -205.60000000, 0.00000000, 0.00000000, -205.6000, 0.0000, 0.0000, 0, 0, 'Withdraw-TH-CPS', 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0, '2026-02-03 07:33:33.746', 'mv kafka mt5', 0, 500, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.0000, 0.0000, 0.0000, 0.0000, 0.00),
                                ('', '', 0, '', clientAccount, 'MT5', clientServerId, 'MT5VT5', '', '', '', 5695574, 0, 2, 0, 2, 0, '2026-02-03 10:45:09.789', '2026-02-03 08:45:09.789', '', '', '', '', 0.0, 0.0, 1.0, 0.000000, 0, 0.0000, 0.00, 205.60000000, 0.00000000, 0.00000000, 205.6000, 0.0000, 0.0000, 0, 0, 'Withdrawal Reversal', 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0, '2026-02-03 08:45:09.789', 'mv kafka mt5', 0, 500, 205.60000000, 205.60000000, 0.00000000, 205.60000000, 205.6000, 205.6000, 0.0000, 205.6000, 0.00),
                                ('', '', 0, '', clientAccount, 'MT5', clientServerId, 'MT5VT5', '', '', '', 6955255, 0, 2, 0, 2, 0, '2026-02-06 06:51:51.505', '2026-02-06 04:51:51.505', '', '', '', '', 0.0, 0.0, 1.0, 0.000000, 0, 0.0000, 0.00, -205.60000000, 0.00000000, 0.00000000, -205.6000, 0.0000, 0.0000, 0, 0, 'Withdraw-TH-CPS', 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0, '2026-02-06 04:51:51.505', 'mv kafka mt5', 0, 500, 0.00000000, 0.00000000, 0.00000000, 0.00000000, 0.0000, 0.0000, 0.0000, 0.0000, 0.00);
                """;

        executeQueryToDb(DbName.CLICKHOUSE, trasformQuerry(rawQuerryDeposit, client));
        executeQueryToDb(DbName.CLICKHOUSE, trasformQuerry(rawQuerryBalance, client));
        executeQueryToDb(DbName.CLICKHOUSE, trasformQuerry(rawQuerryCredit, client));
        executeQueryToDb(DbName.CLICKHOUSE, trasformQuerry(rawQuerryDeals, client));
        executeQueryToDb(DbName.CLICKHOUSE, trasformQuerry(rawQuerryDealsDD, client));
    }

    protected static String trasformQuerry(String rawQuerry, ClientHelper client) {
        return rawQuerry
                .replace("clientBrand", client.getBrand())
                .replace("clientRegulator", client.getRegulator())
                .replace("clientId", client.getUserId().toString())
                .replace("clientAccount", client.getTradingAccount().toString())
                .replace("clientServerId", client.getServerId().toString())
                .replace("clientUcid", client.getUcid());
    }
}
