package tests.vindex_backoffice_ui_tests.investigationTool;

import business_objects.db.backoffice_db.backoffice_user.BackofficeUser;
import business_objects.ui.user.User;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.util.List;

import static business_objects.ui.user.UserFactory.firstLoginUser;
import static helpers.database.DbHelper.deleteEntryFromDb;
import static helpers.database.DbHelper.getObjectsFromDB;
import static helpers.database.DbName.POSTGRES;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

public class FirstLoginTest extends TestBaseWeb {

    private static final User uiUser = firstLoginUser();
    private static final String USER_ID_WHERE_STATEMENT = String.format("user_id = (select id from %s where first_name = '%s' and last_name = '%s')", BO_BACKOFFICE_USER_TABLE_NAME, uiUser.getFirstName(), uiUser.getLastName());
    private static final String BO_USER_WHERE_STATEMENT = String.format("first_name = '%s' and last_name = '%s'", uiUser.getFirstName(), uiUser.getLastName());

    @BeforeAll
    public static void setup() throws Exception {
        deleteEntryFromDb(POSTGRES, BO_USER_ACTION_AUDIT_TABLE_NAME, USER_ID_WHERE_STATEMENT);
        deleteEntryFromDb(POSTGRES, BO_USER_SESSION_TABLE_NAME, USER_ID_WHERE_STATEMENT);
        deleteEntryFromDb(POSTGRES, BO_BACKOFFICE_USER_TABLE_NAME, BO_USER_WHERE_STATEMENT);
        List<BackofficeUser> usersList = getObjectsFromDB(
                POSTGRES, BO_BACKOFFICE_USER_TABLE_NAME, BO_USER_WHERE_STATEMENT, BackofficeUser.class
        );
        assertThat(usersList, empty());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("524")
    @DisplayName("Verify that user data is saved to backoffice db after first login")
    public void verifyUserDataSavedAfterFirstLoginTest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsFirstLoginUser();
        investigationPage.navigateBase();
        investigationPage.waitForPageToLoad();
        List<BackofficeUser> usersList = getObjectsFromDB(
                POSTGRES, BO_BACKOFFICE_USER_TABLE_NAME, BO_USER_WHERE_STATEMENT, BackofficeUser.class
        );
        assertThat(usersList, hasSize(1));
        BackofficeUser expectedUser = new BackofficeUser(uiUser.getFirstName(), uiUser.getLastName(), uiUser.getRole());
        assertThat("Compare user in db against expected", usersList.getFirst(), equalTo(expectedUser));
    }

    @AfterAll
    public static void teardown() throws Exception {
        deleteEntryFromDb(POSTGRES, BO_USER_ACTION_AUDIT_TABLE_NAME, USER_ID_WHERE_STATEMENT);
        deleteEntryFromDb(POSTGRES, BO_USER_SESSION_TABLE_NAME, USER_ID_WHERE_STATEMENT);
        deleteEntryFromDb(POSTGRES, BO_BACKOFFICE_USER_TABLE_NAME, BO_USER_WHERE_STATEMENT);
    }
}
