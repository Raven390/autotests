package businessObjects.db.clickhouse.csTbEmailTable;

import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;
import helpers.data.ClientHelper;

import static utils.Utils.getCurrentTimestampDbFormat;

public class EmailTableEntryFactory {

    public static EmailTableEntry emailTableEntryForConnectionSearch() {
        return new EmailTableEntry(
                "vantage-99992", 99_992, "Vantage", "test@email.com", getCurrentTimestampDbFormat()
        );
    }

    public static EmailTableEntry getEmailTableEntryByCrmUser(CrmTbUserObject user) {
        return new EmailTableEntry(
                user.ucid, user.userId, user.brand, user.email, getCurrentTimestampDbFormat()
        );
    }

    public static EmailTableEntry emailTableEntryForConnectionSearchFiltration() {
        return new EmailTableEntry(
                "vantage-100004", 100_004, "Vantage", "testfiltration@qatest.com", getCurrentTimestampDbFormat()
        );
    }

    public static EmailTableEntry getEmailTableEntryByClient(ClientHelper clientHelper) {
        return new EmailTableEntry(
                clientHelper.getUcid(), clientHelper.getUserId(), clientHelper.getBrand(), clientHelper.getEmail(), getCurrentTimestampDbFormat()
        );
    }
}
