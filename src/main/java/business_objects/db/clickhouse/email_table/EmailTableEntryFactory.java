package business_objects.db.clickhouse.email_table;

import helpers.data.ClientHelper;

import static utils.Utils.getCurrentTimestamp;


public class EmailTableEntryFactory {

    public static EmailTableEntry emailTableEntryForConnectionSearch(ClientHelper client) {
        return new EmailTableEntry(
                client.getUcid(), "test@email.com"
        );
    }

    public static EmailTableEntry emailTableEntryForConnectionSearchFiltration(ClientHelper client) {
        return new EmailTableEntry(
                client.getUcid(), "testfiltration" + getCurrentTimestamp() + "@qatest.com"
        );
    }

    public static EmailTableEntry getEmailTableEntryByClient(ClientHelper client) {
        return new EmailTableEntry(
                client.getUcid(), client.getEmail()
        );
    }

    public static EmailTableEntry emailTableEntryForConnectionSearch(ClientHelper client, String email) {
        return new EmailTableEntry(
                client.getUcid(), email
        );
    }
}
