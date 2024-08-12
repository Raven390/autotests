package uiTests.pages;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class BasePage {
    static BrowserContext context;
    public static void setContext(BrowserContext newContext) {
        context = newContext;
    }

    private Page pageSingleton;
    protected Page getPage() {
        if (pageSingleton == null) { pageSingleton = context.newPage(); }
        return pageSingleton;
    }

    public Locator locator(String selector) {
        return getPage().locator(selector);
    }
}
