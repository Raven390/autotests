package businessObjects.ui.user;

import static utils.ConfigFactory.*;
import static utils.Constants.ROLE_UNKNOWN;

public class UserFactory {

    public static User firstLoginUser() {
        return new User(USERNAME_FIRST_LOGIN, PASSWORD_FIRST_LOGIN, FIRST_NAME_FIRST_LOGIN, LAST_NAME_FIRST_LOGIN, EMAIL_FIRST_LOGIN, ROLE_UNKNOWN);
    }

    public static User coreUser() {
        return new User(USERNAME_CORE, PASSWORD_CORE, FIRST_NAME_CORE, LAST_NAME_CORE, EMAIL_CORE, ROLE_UNKNOWN);
    }
}
