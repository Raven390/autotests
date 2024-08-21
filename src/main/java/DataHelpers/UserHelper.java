package DataHelpers;

import java.sql.SQLException;
import utils.PostgreHelper;

public class UserHelper {
  public void createUser(String email) throws SQLException {
    PostgreHelper.executeSqlQuery("INSERT INTO USERS ...");
  }
}
