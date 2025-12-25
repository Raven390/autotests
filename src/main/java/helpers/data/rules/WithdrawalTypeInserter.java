package helpers.data.rules;

import static helpers.database.DbHelper.executeQueryToDb;

import helpers.database.DbName;

public class WithdrawalTypeInserter {
    public static void insertWithrawalTypeData() {
        String rawQueryDepositType =
                """
                        INSERT INTO consolidated.crm___tb_withdraw_type
                        (source_id_st, id, category, en_name, last_updated)
                        VALUES
                            (1, 1, 1, 'International Wire Transfer', NOW()),
                            (1, 2, 2, 'Credit Card', NOW()),
                            (1, 3, 3, 'E-Wallet', NOW()),
                            (1, 4, 4, 'Crypto', NOW()),
                            (1, 5, 5, 'Vietnam Bank Transfer', NOW()),
                            (1, 6, 6, 'China Gateway', NOW());
                        """;
        executeQueryToDb(DbName.CLICKHOUSE, rawQueryDepositType);
    }
}
