package businessObjects.db.clickhouse.emailTable;

import helpers.data.ClientHelper;


public class EmailTableEntryFactory {

    public static EmailTableEntry emailTableEntryForConnectionSearch(ClientHelper client) {
        return new EmailTableEntry(
                client.getUserId(), client.getBrand().toLowerCase(), "test@email.com"
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

    public static EmailTableEntry emailTableEntryForConnectionSearch(ClientHelper clientHelper, String email) {
        return new EmailTableEntry(
                clientHelper.getUserId(), clientHelper.getBrand().toLowerCase(), email
        );
    }
}
