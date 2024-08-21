package uiTests.backoffice.tests.apiHelpers;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class KeycloackAPI {
    private Playwright playwright;
    private APIRequestContext request;

    void createPlaywright() {
        playwright = Playwright.create();
    }

    void createAPIRequestContext() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/vnd.github.v3+json");
        headers.put("Authorization", "token " + "API_TOKEN");

        request = playwright.request().newContext(new APIRequest.NewContextOptions()
                // All requests we send go to this API endpoint.
                .setBaseURL("keycloackBaseURL")
                .setExtraHTTPHeaders(headers));
    }

    void getAUTHtoken() {
        createAPIRequestContext();
        APIResponse token = request.post("/auth");
        assertTrue(token.ok());
        //need to parse header and save it for browser context
    }
}
