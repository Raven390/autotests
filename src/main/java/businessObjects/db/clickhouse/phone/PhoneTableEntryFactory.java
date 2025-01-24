package businessObjects.db.clickhouse.phone;

import helpers.data.ClientHelper;

public class PhoneTableEntryFactory {

    public static PhoneTableEntry phoneTableEntryForConnectionSearch(ClientHelper client) {
        return new PhoneTableEntry(
                client.getUserId(), client.getBrand().toLowerCase(), client.getPhoneNumber()
        );
    }

    public static PhoneTableEntry phoneTableEntryForConnectionSearch(ClientHelper client, String phoneNumber) {
        return new PhoneTableEntry(
                client.getUserId(), client.getBrand().toLowerCase(), phoneNumber
        );
    }
}
