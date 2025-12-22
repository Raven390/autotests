package business_objects.db.clickhouse.name_birth;

import java.util.Objects;

public class NameBirthTableEntry {

    public String ucid;
    public String firstName;
    public String lastName;
    public String dateOfBirth;

    public NameBirthTableEntry() {}

    public NameBirthTableEntry(String ucid, String firstName, String lastName, String dateOfBirth) {
        this.ucid = ucid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        NameBirthTableEntry that = (NameBirthTableEntry) o;
        return Objects.equals(ucid, that.ucid)
                && Objects.equals(firstName, that.firstName)
                && Objects.equals(lastName, that.lastName)
                && Objects.equals(dateOfBirth, that.dateOfBirth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ucid, firstName, lastName, dateOfBirth);
    }

    @Override
    public String toString() {
        return "NameBirthTableEntry{" + "ucid='" + ucid + '\'' + ", firstName='" + firstName + '\'' + ", lastName='"
                + lastName + '\'' + ", dateOfBirth='" + dateOfBirth + '\'' + '}';
    }
}
