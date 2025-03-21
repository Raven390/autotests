package business_objects.db.clickhouse.name_birth;

import helpers.data.ClientHelper;

public class NameBirthTableEntryFactory {

    public static NameBirthTableEntry nameBirthTableEntryForConnectionSearch(ClientHelper client) {
        return new NameBirthTableEntry(
                client.getUcid(), client.getFirstName(), client.getLastName(), client.getDateOfBirth()
        );
    }

    public static NameBirthTableEntry nameBirthTableEntryForConnectionSearch(ClientHelper client, String name,
            String lastname, String dateOfBirth) {
        return new NameBirthTableEntry(
                client.getUcid(), name, lastname, dateOfBirth
        );
    }
}
