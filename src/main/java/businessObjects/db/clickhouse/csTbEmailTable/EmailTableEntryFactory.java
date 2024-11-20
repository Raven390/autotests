package businessObjects.db.clickhouse.csTbEmailTable;

import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;

import static utils.Utils.getCurrentTimestampDbFormat;

public class EmailTableEntryFactory {

    public static EmailTableEntry emailTableEntryForConnectionSearch() {
        return new EmailTableEntry(
                "99992",
                99_992,
                "vantage",
                "test@email.com",
                getCurrentTimestampDbFormat()
        );
    }

    public static EmailTableEntry getEmailTableEntryByCrmUser(CrmTbUserObject user) {
        return new EmailTableEntry(
                user.ucid,
                user.userId,
                user.brand,
                user.email,
                getCurrentTimestampDbFormat()
        );
    }
}
