package page_objects.backoffice_pages.investigationTool;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Allure;
import page_objects.backoffice_pages.AbstractPage;


import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GeneralSearchElements extends AbstractPage {

    private final Locator searchButton;
    private final Locator searchInput;
    private final Locator errorText1;
    private final Locator errorText2;
    private final Locator errorScreen;

    private static final String SIDEBAR_MENU = "//div[@class='v-sidebar__menu']";
    private static final String SEARCH_OVERLAY_LOCATOR = "*[@class='v-search-overlay']";
    private static final String SEARCH_CLIENT_CARD_LOCATOR = "*[@class='v-search-client-card']";
    private static final String SEARCH_CLIENT_CARD_COUNTRY_LOCATOR = "*[contains(@class, 'v-search-client-card__country')]";
    private static final String SEARCH_ERROR_CONTAINER_LOCATOR = "*[@class='v-error-view__content']";
    private static final String SEARCH_CLIENT_CARD_HEADER_LOCATOR = "*[@class='v-search-client-card__header']";
    private static final String SEARCH_CLIENT_CARD_BUTTONS_LOCATOR = "*[@class='v-search-client-card__externals']/button";

    public GeneralSearchElements(Page page) {
        super(page);
        searchButton = page.locator(SIDEBAR_MENU).locator("//div/div[@class='v-sidebar-button']");
        searchInput = page.locator("//" + SEARCH_OVERLAY_LOCATOR + "//input");
        errorScreen = page.locator("//" + SEARCH_ERROR_CONTAINER_LOCATOR);
        errorText1 = page.locator("//" + SEARCH_ERROR_CONTAINER_LOCATOR + "//" + SUBHEADER_2_TEXT);
        errorText2 = page.locator("//" + SEARCH_ERROR_CONTAINER_LOCATOR + "//" + CAPTION_2_TEXT);
    }

    public void openSearch() {
        Allure.step("Open search form");
        waitForPageToLoad();
        searchButton.click();
        searchInput.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    public void inputSearchText(String text) {
        Allure.step("Type into search field");
        searchInput.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        searchInput.fill(text);
        assertEquals(searchInput.inputValue(), text);
    }

    public void inputSearchText(Integer text) {
        inputSearchText(String.valueOf(text));
    }

    public void inputSearchPressEnter() {
        Allure.step("Press 'Enter' button into search field");
        searchInput.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        searchInput.press("Enter");
    }

    public void checkErrorScreen() {
        Allure.step("Check error screen");
        errorScreen.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        assertEquals("The client was not found", errorText1.textContent());
        assertEquals("Please check if your query is correct or use another parameter to search", errorText2.textContent());
    }

    public void findClientsCard(String clientId, String clientBrand) {
        Allure.step("Check client card is visible");
        page.locator("//span[text() = '" + clientId + "']/../..//span[text() = '" + clientBrand + "']/ancestor::" + SEARCH_CLIENT_CARD_LOCATOR).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    public void findClientsCard(Integer clientId, String clientBrand) {
        findClientsCard(String.valueOf(clientId), clientBrand);
    }

    public void checkClientsCard(String clientId, String clientBrand, String clientFirstName, String clientLastName,
            String clientCountry, String clientRegistrationDate) {
        Allure.step("Check data in clients card");
        String clientCard = "//span[text() = '" + clientId + "']/../..//span[text() = '" + clientBrand + "']/ancestor::" + SEARCH_CLIENT_CARD_LOCATOR;
        page.locator(clientCard).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        String name = page.locator(clientCard + "//" + SEARCH_CLIENT_CARD_HEADER_LOCATOR + "//" + SUBHEADER_3_LOCATOR).textContent();
        assertEquals(clientFirstName + " " + clientLastName, name);
        String country = page.locator(clientCard + "//" + SEARCH_CLIENT_CARD_COUNTRY_LOCATOR).textContent();
        assertEquals(clientCountry, country);
        String registrationDate = page.locator(clientCard + "//div[3]/span").textContent();
        assertEquals(clientRegistrationDate, registrationDate);
    }

    public void checkClientsCard(Integer clientId, String clientBrand, String clientFirstName, String clientLastName,
            String clientCountry, String clientRegistrationDate) {
        checkClientsCard(String.valueOf(clientId), clientBrand, clientFirstName, clientLastName, clientCountry, clientRegistrationDate);
    }

    public void checkClientsCardAccount(String accountId, String clientBrand, String clientFirstName,
            String clientLastName, String clientCountry, String clientRegistrationDate, String clientAccountPlatform) {
        Allure.step("Check data in clients card");
        String clientCard = "//span[text() = '" + accountId + "']/../..//span[text() = '" + clientBrand + "']/ancestor::" + SEARCH_CLIENT_CARD_LOCATOR;
        page.locator(clientCard).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        String name = page.locator(clientCard + "//" + SEARCH_CLIENT_CARD_HEADER_LOCATOR + "//" + SUBHEADER_3_LOCATOR).textContent();
        assertEquals(clientFirstName + " " + clientLastName, name);
        String country = page.locator(clientCard + "//" + SEARCH_CLIENT_CARD_COUNTRY_LOCATOR).textContent();
        assertEquals(clientCountry, country);
        String registrationDate = page.locator(clientCard + "//div[3]/span").textContent();
        assertEquals(clientRegistrationDate, registrationDate);
        String accountPlatform = page.locator(clientCard + "//div[5]/span").textContent();
        assertEquals(clientAccountPlatform, accountPlatform);
    }

    public void checkClientsCardAccount(Integer accountId, String clientBrand, String clientFirstName,
            String clientLastName, String clientCountry, String clientRegistrationDate, String clientAccountPlatform) {
        checkClientsCardAccount(String.valueOf(accountId), clientBrand, clientFirstName, clientLastName, clientCountry, clientRegistrationDate, clientAccountPlatform);
    }

    public void clickTradingClientsCard(String clientId, String clientBrand) {
        Allure.step("Click trading button and check that you was redirected to trading/operations of client");
        waitForPageToLoad();
        String clientCard = "//span[text() = '" + clientId + "']/../..//span[text() = '" + clientBrand + "']/ancestor::" + SEARCH_CLIENT_CARD_LOCATOR;
        page.locator(clientCard).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        waitForPageToLoad();
        page.locator(clientCard).hover();
        page.locator(clientCard + "//" + SEARCH_CLIENT_CARD_BUTTONS_LOCATOR).nth(0).click();

    }

    public void clickTradingClientsCard(Integer clientId, String clientBrand) {
        clickTradingClientsCard(String.valueOf(clientId), clientBrand);
    }

    public void clickConnectionSearchClientsCard(String clientId, String clientBrand) {
        Allure.step("Click trading button and check that you was redirected to connection serch tab of client");
        waitForPageToLoad();
        String clientCard = "//span[text() = '" + clientId + "']/../..//span[text() = '" + clientBrand + "']/ancestor::" + SEARCH_CLIENT_CARD_LOCATOR;
        page.locator(clientCard).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        waitForPageToLoad();
        page.locator(clientCard).hover();
        page.locator(clientCard + "//" + SEARCH_CLIENT_CARD_BUTTONS_LOCATOR).nth(1).click();

    }

    public void clickConnectionSearchClientsCard(Integer clientId, String clientBrand) {
        clickConnectionSearchClientsCard(String.valueOf(clientId), clientBrand);
    }

    public void clickClientsCard(String clientId, String clientBrand) {
        Allure.step("Click trading button and check that you was redirected to connection serch tab of client");
        waitForPageToLoad();
        String clientCard = "//span[text() = '" + clientId + "']/../..//span[text() = '" + clientBrand + "']/ancestor::" + SEARCH_CLIENT_CARD_LOCATOR;
        page.locator(clientCard).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        waitForPageToLoad();
        page.locator(clientCard).click();

    }

    public void clickClientsCard(Integer clientId, String clientBrand) {
        clickClientsCard(String.valueOf(clientId), clientBrand);
    }

    public void clickCopyButtonClientsCard(String clientId, String clientBrand) throws IOException,
            UnsupportedFlavorException {
        Allure.step("Click copy button and check that link to client's page is saved in clipboard");
        waitForPageToLoad();
        String clientCard = "//span[text() = '" + clientId + "']/../..//span[text() = '" + clientBrand + "']/ancestor::" + SEARCH_CLIENT_CARD_LOCATOR;
        page.locator(clientCard).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        waitForPageToLoad();
        page.locator(clientCard).hover();
        page.locator(clientCard + "//" + SEARCH_CLIENT_CARD_BUTTONS_LOCATOR).nth(2).click();
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Clipboard clipboard = toolkit.getSystemClipboard();
        String clipboardText = (String) clipboard.getData(DataFlavor.stringFlavor);
        assertTrue(clipboardText.contains(clientBrand.toLowerCase(Locale.ROOT) + "-" + clientId + "/alerts"));


    }

    public void clickCopyButtonClientsCard(Integer clientId, String clientBrand) {
        clickConnectionSearchClientsCard(String.valueOf(clientId), clientBrand);
    }


}

