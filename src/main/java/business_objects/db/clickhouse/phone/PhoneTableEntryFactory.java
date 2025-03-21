package business_objects.db.clickhouse.phone;

import helpers.data.ClientHelper;

public class PhoneTableEntryFactory {

    public static PhoneTableEntry phoneTableEntryForConnectionSearch(ClientHelper client) {
        return new PhoneTableEntry(
                client.getUcid(), client.getPhoneNumber()
        );
    }

    public static PhoneTableEntry phoneTableEntryForConnectionSearch(ClientHelper client, String phoneNumber) {
        return new PhoneTableEntry(
                client.getUcid(), phoneNumber
        );
    }
}
