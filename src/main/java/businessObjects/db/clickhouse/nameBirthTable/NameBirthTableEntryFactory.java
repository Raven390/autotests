package businessObjects.db.clickhouse.nameBirthTable;

import helpers.data.ClientHelper;

public class NameBirthTableEntryFactory {

    public static NameBirthTableEntry nameBirthTableEntryForConnectionSearch(ClientHelper client) {
        return new NameBirthTableEntry(
                client.getUserId(), client.getBrand().toLowerCase(), client.getNamedateofbirth()
        );
    }
}
