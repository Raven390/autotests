package helpers.database;

import java.sql.SQLException;

import static helpers.database.DbHelper.executeQueryToDb;
import static utils.Constants.BO_CLIENT_TABLE_NAME;
import static utils.Utils.getCurrentTimestampDbFormat;

public class BoHelper {
    public static void closeAlert(String ucid) throws SQLException {
        executeQueryToDb(
                DbName.BO,
                String.format("UPDATE bo.bo.alert SET closed_at ='%s', status = '%s' WHERE client_id = (select id from %s where ucid = '%s')",
                        getCurrentTimestampDbFormat(),
                        "CLOSED",
                        BO_CLIENT_TABLE_NAME,
                        ucid
                )
        );
    }
}
