package business_objects.db.backoffice_db.backoffice_user;

import java.util.Objects;

public class BackofficeUser {

    public String id;
    public String firstName;
    public String lastName;
    public String role;

    public BackofficeUser() {}

    public BackofficeUser(String firstName, String lastName, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    public BackofficeUser(String id, String firstName, String lastName, String role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BackofficeUser that = (BackofficeUser) o;
        return Objects.equals(firstName, that.firstName)
                && Objects.equals(lastName, that.lastName)
                && Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, role);
    }

    @Override
    public String toString() {
        return "BackofficeUser{" + "id='" + id + '\'' + ", firstName='" + firstName + '\'' + ", lastName='" + lastName
                + '\'' + ", role='" + role + '\'' + '}';
    }
}
