package businessObjects.db.clickhouse.nameBirth;

import java.util.Objects;

public class NameBirthTableEntry {

    public Integer userId;
    public String brand;
    public String nameDateofbirth;

    public NameBirthTableEntry() {
    }

    public NameBirthTableEntry(Integer userId, String brand, String namedateofbirth) {
        this.userId = userId;
        this.brand = brand;
        this.nameDateofbirth = namedateofbirth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NameBirthTableEntry that = (NameBirthTableEntry) o;
        return Objects.equals(userId, that.userId) && Objects.equals(brand, that.brand) && Objects.equals(
                nameDateofbirth, that.nameDateofbirth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, brand, nameDateofbirth);
    }

    @Override
    public String toString() {
        return "NameBirthTableEntry{" + "userId=" + userId + ", brand='" + brand + '\'' + ", nameDateofbirth='" + nameDateofbirth + '\'' + '}';
    }
}
