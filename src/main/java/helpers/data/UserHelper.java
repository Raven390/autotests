package helpers.data;

import helpers.database.PostgreHelper;
import io.qameta.allure.Allure;
import java.sql.SQLException;

public class UserHelper {
    public static void createUser(Boolean isAbuser, String email) throws SQLException {
        PostgreHelper.insertUser();

        // Mark user as abuser
        if (isAbuser) {
            PostgreHelper.insertUserToAbuseRegistry();
        }

        Allure.step(String.format("Created user with parameter isAbuser=%b and email=%s", isAbuser, email));
    }

    public static void createLinkedUser() {}
}
