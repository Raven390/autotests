package pageObjects.backofficePages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;

public abstract class AbstractPage {

    protected final Page page;

    protected static final String LOADING_ANIMATION_SELECTOR = ".v-loader";
    protected static final String LOADER_SPIN_LOCATOR = ".g-spin";

    public AbstractPage(Page page) {
        this.page = page;
    }

    @Step("Wait for page to load")
    public void waitForPageToLoad() {
        page.waitForSelector(LOADING_ANIMATION_SELECTOR, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.HIDDEN));
    }

}
