package uiTests.example.PageObjects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class SearchPage {
    private final Page page;
    private final Locator searchTermInput;
    private final Locator firstResult;

    public SearchPage(Page page) {
        this.page = page;
        this.searchTermInput = page.locator("input[aria-label=\"Search with DuckDuckGo\"]");
        this.firstResult =
                page.locator("[data-testid=\"result-extras-url-link\"]").first();
    }

    public void navigate() {
        page.navigate("https://duckduckgo.com/");
    }

    public void search(String text) {
        searchTermInput.fill(text);
        searchTermInput.press("Enter");
    }

    public void clickFirst() {
        firstResult.click();
    }
}
