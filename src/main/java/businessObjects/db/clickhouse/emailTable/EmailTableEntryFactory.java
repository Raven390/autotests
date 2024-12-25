package businessObjects.db.clickhouse.emailTable;

import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;
import helpers.data.ClientHelper;


public class EmailTableEntryFactory {

    public static EmailTableEntry emailTableEntryForConnectionSearch(ClientHelper client) {
        return new EmailTableEntry(
                client.getUserId(), client.getBrand().toLowerCase(), "test@email.com"
        );
    }

    public static EmailTableEntry getEmailTableEntryByCrmUser(CrmTbUserObject user) {
        return new EmailTableEntry(
                user.userId, user.brand, user.email
        );
    }

    public static EmailTableEntry emailTableEntryForConnectionSearchFiltration(ClientHelper client) {
        return new EmailTableEntry(
                client.getUserId(), client.getBrand().toLowerCase(), "testfiltration@qatest.com"
        );
    }

    public static EmailTableEntry getEmailTableEntryByClient(ClientHelper clientHelper) {
        return new EmailTableEntry(
                clientHelper.getUserId(), clientHelper.getBrand().toLowerCase(), clientHelper.getEmail()
        );
    }
}
