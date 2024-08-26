package uiTests.backoffice.tests.apiHelpers;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class KeycloackAPI {
    private static Playwright playwright;
    private static APIRequestContext request;

    static void createAPIRequestContext() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/vnd.github.v3+json");
        headers.put("Authorization", "token " + "API_TOKEN");
        headers.put("shuba", "luba");

        request =
                Playwright.create()
                        .request()
                        .newContext(
                                new APIRequest.NewContextOptions()
                                        // All requests we send go to this API endpoint.
                                        .setBaseURL("https://playwright.dev")
                                        .setExtraHTTPHeaders(headers));
    }

    public static void getAUTHtoken() {
        createAPIRequestContext();
        APIResponse token = request.get("/");
        assertTrue(token.ok());
        System.out.println(token.text() + "dunno");
        //        System.out.println(token.headers() + "dunno");
        // need to parse header and save it for browser context
    }
}
