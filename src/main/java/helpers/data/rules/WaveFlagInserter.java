package helpers.data.rules;

import helpers.data.ClientHelper;
import helpers.database.DbName;

import static helpers.database.DbHelper.deleteEntryFromDb;
import static helpers.database.DbHelper.executeQueryToDb;
import static utils.Constants.*;

public class WaveFlagInserter {
    public static void insertWaveFlagData(ClientHelper client) throws InterruptedException {
        String rawQueryAccount = """
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
        String rawQueryDeposit = """
                                INSERT INTO vindex_test.crm___tb_deposit
                (brand_uid, brand, regulator, user_id, ucid, account, transfer_id,
                 create_time, create_time_utc, amount, amount_usd, currency, status_id, status, last_updated)
                VALUES
                    (1, 'clientBrand', 'clientRegulator', clientId, 'clientUcid', clientAccount, 1,
                     now() - INTERVAL 1 HOUR, now() - INTERVAL 1 HOUR, 1000.00, 1000.00, 'USD', 1, 'Success', now());
                                        """;

        String rawQueryCredit = """
                                INSERT INTO vindex_test.mt___credit_orders
                (ticket, server_id, server_name, ucid, brand, regulator, user_id,
                 platform, account, currency, create_time, create_time_utc,
                 amount, rate_to_usd, amount_usd, is_deleted, last_updated)
                VALUES
                    (1, clientServerId, 'SRV1', 'clientUcid', 'clientBrand', 'clientRegulator', clientId,
                     'MT5', clientAccount, 'USD', now() - INTERVAL 1 HOUR, now() - INTERVAL 1 HOUR,
                     500.00, 1.0, 500.00, 0, now());
                                """;

        String rawQueryDeals = """
                                INSERT INTO vindex_test.mt___mt5_deals_coerced
                (brand, regulator, user_id, ucid, account, platform, server_id, server_name,
                 deal, "order", action, time, time_utc, symbol, symbol_underlying,
                 volume_lots, notional_value_usd, profit_usd, last_updated)
                VALUES
                    ('clientBrand', 'clientRegulator', clientId, 'clientUcid', clientAccount, 'MT5', clientServerId, 'SRV1',
                     1, 1, 0, now() - INTERVAL 1 HOUR, now() - INTERVAL 1 HOUR, 'EURUSD', 'FX',
                     3.0, 300000.00, 150.00, now()),
                    ('clientBrand', 'clientRegulator', clientId, 'clientUcid', clientAccount, 'MT5', clientServerId, 'SRV1',
                     2, 2, 0, now() + INTERVAL 1 SECOND - INTERVAL 1 HOUR, now() + INTERVAL 1 SECOND - INTERVAL 1 HOUR, 'EURUSD', 'FX',
                     3.0, 300000.00, 150.00, now()),
                    ('clientBrand', 'clientRegulator', clientId, 'clientUcid', clientAccount, 'MT5', clientServerId, 'SRV1',
                     3, 3, 0, now() + INTERVAL 2 SECOND - INTERVAL 1 HOUR, now() + INTERVAL 2 SECOND - INTERVAL 1 HOUR, 'EURUSD', 'FX',
                     3.0, 300000.00, 150.00, now()),
                    ('clientBrand', 'clientRegulator', clientId, 'clientUcid', clientAccount, 'MT5', clientServerId, 'SRV1',
                     4, 4, 0, now() + INTERVAL 3 SECOND - INTERVAL 1 HOUR, now() + INTERVAL 3 SECOND - INTERVAL 1 HOUR, 'EURUSD', 'FX',
                     3.0, 300000.00, 150.00, now()),
                    ('clientBrand', 'clientRegulator', clientId, 'clientUcid', clientAccount, 'MT5', clientServerId, 'SRV1',
                     5, 5, 0, now() + INTERVAL 4 SECOND - INTERVAL 1 HOUR, now() + INTERVAL 4 SECOND - INTERVAL 1 HOUR, 'EURUSD', 'FX',
                     3.0, 300000.00, 150.00, now()),
                    ('clientBrand', 'clientRegulator', clientId, 'clientUcid', clientAccount, 'MT5', clientServerId, 'SRV1',
                     6, 6, 0, now() + INTERVAL 5 SECOND - INTERVAL 1 HOUR, now() + INTERVAL 5 SECOND - INTERVAL 1 HOUR, 'EURUSD', 'FX',
                     3.0, 300000.00, 150.00, now()),
                    ('clientBrand', 'clientRegulator', clientId, 'clientUcid', clientAccount, 'MT5', clientServerId, 'SRV1',
                     7, 7, 0, now() + INTERVAL 6 SECOND - INTERVAL 1 HOUR, now() + INTERVAL 6 SECOND - INTERVAL 1 HOUR, 'EURUSD', 'FX',
                     3.0, 300000.00, 150.00, now());
                                """;

        executeQueryToDb(DbName.CLICKHOUSE, transformQuery(rawQueryAccount, client));
        executeQueryToDb(DbName.CLICKHOUSE, transformQuery(rawQueryDeposit, client));
        executeQueryToDb(DbName.CLICKHOUSE, transformQuery(rawQueryCredit, client));
        executeQueryToDb(DbName.CLICKHOUSE, transformQuery(rawQueryDeals, client));
        Thread.sleep(10_000);
    }

    protected static String transformQuery(String rawQuery, ClientHelper client) {
        return rawQuery.replace("clientBrand", client.getBrand()).replace("clientRegulator", client.getRegulator()).replace("clientId", client.getUserId().toString()).replace("clientAccount", client.getTradingAccount().toString()).replace("clientServerId", client.getServerId().toString()).replace("clientUcid", client.getUcid());
    }

    public static void deleteWaveFlagData(ClientHelper client) {
        try {
            deleteEntryFromDb(CRM_TB_ACCOUNT_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        deleteEntryFromDb(CRM_DEPOSIT_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteEntryFromDb(MT_CREDITS_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteEntryFromDb(MT5_DEALS_COERCED_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
    }
}
