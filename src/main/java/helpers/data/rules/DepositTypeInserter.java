package helpers.data.rules;

import static helpers.database.DbHelper.executeQueryToDb;
import static utils.Constants.*;

import helpers.database.DbName;

public class DepositTypeInserter {
    public static void insertDepositTypeData() {
        String rawQueryDepositType =
                """
                        INSERT INTO consolidated.crm___tb_deposit_type (
                        source_id_st, id, category, name,              last_updated) VALUES
                        (1,           1,         2, 'Credit Card',     NOW()),
                        (1,           3,         5, 'Union Pay',       NOW()),
                        (1,           9,         5, 'Mobile Pay',      NOW()),
                        (1,           10,        7, 'Offline Payment', NOW()),
                        (1,           11,        1, 'Wire Transfer', NOW());
                        """;
        String rawQueryDepositChannel =
                """
                        INSERT INTO consolidated.crm___tb_deposit_channel (
                        source_id_st, id,  channel_id, type_id, is_mobile_channel, name,             last_updated) VALUES
                        (1,           1,   1,          1,       0,                 'NAB',            NOW()),
                        (1,           83,  2,          3,       0,                 'PaymentAsia',    NOW()),
                        (1,           188, 1,          9,       1,                 'Mobile Pay-APP', NOW()),
                        (1,           10,  10,         10,      0,                 'IBT',            NOW()),
                        (1,           11,  11,         11,      0,                 'Wire',           NOW());
                                        """;

        executeQueryToDb(DbName.CLICKHOUSE, rawQueryDepositType);
        executeQueryToDb(DbName.CLICKHOUSE, rawQueryDepositChannel);
    }
}
