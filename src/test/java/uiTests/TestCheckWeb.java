package uiTests;

import static utils.Constants.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class TestCheckWeb extends TestBaseWeb {

    @Test
    @Tag(TAG_BUILD_CHECK)
    @DisplayName("Check framework can run web tests")
    void testCheckGoogle() {
        page.navigate("https://google.com");
        page.locator("xpath=//*[contains(@text,'Google')]");
        assert true;
    }
}
