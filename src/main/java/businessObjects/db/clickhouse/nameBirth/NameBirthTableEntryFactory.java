package businessObjects.db.clickhouse.nameBirth;

import helpers.data.ClientHelper;

public class NameBirthTableEntryFactory {

    public static NameBirthTableEntry nameBirthTableEntryForConnectionSearch(ClientHelper client) {
        return new NameBirthTableEntry(
                client.getUcid(), client.getFirstName(), client.getLastName(), client.getNamedateofbirth()
        );
    }

    public static NameBirthTableEntry nameBirthTableEntryForConnectionSearch(ClientHelper client, String nameBirth) {
        return new NameBirthTableEntry(
                client.getUcid(), client.getFirstName(), client.getLastName(), nameBirth
        );
    }
}
