package helpers.data;

import helpers.postgre.PostgreHelper;
import java.sql.SQLException;

public class UserHelper {
    public void createUser(String email) throws SQLException {
        PostgreHelper.executeSqlQuery("INSERT INTO USERS ...");
    }
}
