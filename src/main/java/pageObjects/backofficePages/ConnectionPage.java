package pageObjects.backofficePages;

import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Route;
import io.qameta.allure.Step;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static utils.ConfigFactory.BASE_URL_E2E;

public class ConnectionPage {

    private final Page page;
    private final Locator loaderAnimation;
    private final Locator connectionTab;


    public ConnectionPage(Page page) {
        this.page = page;
        this.loaderAnimation = page.locator(".v-loader");
        this.connectionTab = page.locator("[role=\"tab\"][title=\"Connections\"]");
    }

    String mappedResponce = "{\n" +
            "    \"connections\": [\n" +
            "        {\n" +
            "            \"clientIdFrom\": \"infinox-424201\",\n" +
            "            \"clientIdTo\": \"infinox-424202\",\n" +
            "            \"connectionScore\": 12,\n" +
            "            \"connectionDetail\": [\n" +
            "                {\n" +
            "                    \"connectionAttributeName\": \"payout\",\n" +
            "                    \"connectionAttributeValue\": \"42424242424242\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"connectionType\": \"sameIdentity\",\n" +
            "            \"connectionDepth\": 1,\n" +
            "            \"abuseType\": null\n" +
            "        },\n" +
            "        {\n" +
            "            \"clientIdFrom\": \"infinox-424201\",\n" +
            "            \"clientIdTo\": \"infinox-424203\",\n" +
            "            \"connectionScore\": 12,\n" +
            "            \"connectionDetail\": [\n" +
            "                {\n" +
            "                    \"connectionAttributeName\": \"email\",\n" +
            "                    \"connectionAttributeValue\": \"4242424@2424242\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"connectionType\": \"sameIdentity\",\n" +
            "            \"connectionDepth\": 1,\n" +
            "            \"abuseType\": null\n" +
            "        },\n" +
            "        {\n" +
            "            \"clientIdFrom\": \"infinox-424201\",\n" +
            "            \"clientIdTo\": \"infinox-424204\",\n" +
            "            \"connectionScore\": 50,\n" +
            "            \"connectionDetail\": [\n" +
            "                {\n" +
            "                    \"connectionAttributeName\": \"payout\",\n" +
            "                    \"connectionAttributeValue\": \"42424242424242\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"connectionType\": \"sameIdentity\",\n" +
            "            \"connectionDepth\": 1,\n" +
            "            \"abuseType\": null\n" +
            "        }\n" +
            "    ],\n" +
            "    \"clients\": {\n" +
            "        \"infinox-424204\": {\n" +
            "            \"clientName\": \"Connect Fourthman\",\n" +
            "            \"status\": \"NORMAL\",\n" +
            "            \"fraudTypes\": null\n" +
            "        },\n" +
            "        \"infinox-424202\": {\n" +
            "            \"clientName\": \"Connect Secondman\",\n" +
            "            \"status\": \"NORMAL\",\n" +
            "            \"fraudTypes\": null\n" +
            "        },\n" +
            "        \"infinox-424203\": {\n" +
            "            \"clientName\": \"Connect Thrirdman\",\n" +
            "            \"status\": \"FRAUDSTER\",\n" +
            "            \"fraudTypes\": [\n" +
            "                {\n" +
            "                    \"key\": \"GAP_TRADING\",\n" +
            "                    \"value\": \"Gap trading\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"key\": \"LATENCY_ARBITRAGE\",\n" +
            "                    \"value\": \"Latency arbitrage\"\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        \"infinox-424201\": {\n" +
            "            \"clientName\": \"Connect Firstman\",\n" +
            "            \"status\": \"SUSPICIOUS\",\n" +
            "            \"fraudTypes\": null\n" +
            "        }\n" +
            "    }\n" +
            "}";

    @Step("Open users restriction tab")
    public void navigateConnectionTab(String ucid) {
        page.navigate(BASE_URL_E2E + "investigation?client_ucid=" + ucid);
        isPageLoaded();
        connectionTab.click();
    }

    @Step("Go to main page")
    public void navigateMain() {
        page.navigate(BASE_URL_E2E);

    }

    @Step("Go to main page")
    public void checkLineStyle(String ucid1, String ucid2, String attributeName, double connectionScore) {
        isPageLoaded();
        int width = connectionWidth(connectionScore);
        isPageLoaded();
        assertTrue(page.locator("[data-qa=\"" + ucid1 + ":" + ucid2 + ":" + attributeName + "\"][style=\"stroke-width: " + width + ";\"]").isVisible());

    }

    @Step("Check name of the client in node")
    public void checkClientName(String ucid1, String name) {
        isPageLoaded();
        assertTrue(page.locator(".v-graph-node[data-qa=\"" + ucid1 + "\"]").getByText(name).isVisible());

    }

    @Step("Check status of the client in node")
    public void checkClientStatus(String ucid1, String status) {
        isPageLoaded();

        assertTrue(page.locator(".v-graph-node[data-qa=\"" + ucid1 + "\"]  .g-text_variant_caption-2").getByText(status).isVisible());

    }

    @Step("Open the MOCKED Connection search page")
    public void navigateMock() {
        page.route("**/api/alerts", route -> {
            String alert = "{\n" + "        \"id\": 1518,\n" + "        \"uuid\": \"c6b6af2e-43a2-425d-bf87-ee2b6141e267\",\n" + "        \"date\": \"2024-09-12T07:57:46.713048Z\",\n" + "        \"amount\": {\n" + "            \"value\": -235331367481903743,\n" + "            \"currency\": \"Monica\"\n" + "        },\n" + "        \"rule\": [\n" + "            \"ProctorMan\",\n" + "            \"Marquez\",\n" + "            \"Ramirez\",\n" + "            \"Simpson\",\n" + "            \"McFadden\",\n" + "            \"Farley\"\n" + "        ],\n" + "        \"client\": {\n" + "            \"id\": null,\n" + "            \"regulator\": null,\n" + "            \"brand\": null\n" + "        },\n" + "        \"status\": \"NEW\",\n" + "        \"tag\": []\n" + "    }";
            APIResponse response = route.fetch();
            String body = response.text();
            Map<String, String> headers = response.headers();
            route.fulfill(new Route.FulfillOptions().setResponse(response).setBody(alert).setHeaders(headers));
        });
        page.navigate(BASE_URL_E2E);
        isPageLoaded();
        page.evaluate("document.querySelector('.v-alert-list__cell_date .g-text_variant_body-1').innerText = 'YESTERDAY'");
    }

    @Step("Check if the page loaded")
    public void isPageLoaded() {
        int n = 0;
        page.waitForTimeout(2000);
        while (loaderAnimation.isVisible() && n < 8) {
            page.waitForTimeout(2000);
            n += 1;
        }
    }

    public static int connectionWidth(double score) {
        if (score <= 16.6) return 1;
        if (score <= 33.2) return 2;
        if (score <= 49.8) return 3;
        if (score <= 66.4) return 4;
        if (score <= 83) return 5;
        return 6;
    }


}
