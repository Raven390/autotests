package pageObjects.backofficePages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public abstract class AbstractPage {

    protected final Page page;
    protected final Locator calendar;
    protected final Locator calendarMode;

    protected static final String LOADING_ANIMATION_SELECTOR = ".v-loader";
    protected static final String LOADER_SPIN_LOCATOR = ".g-spin";
    protected static final String CALENDAR_XPATH = "//div[contains(@class,'v-date-picker__calendar')]";
    protected static final String CALENDAR_BUTTON_WITH_TEXT_PATTERN = "//div[contains(@class,'g-date-calendar__button') and text()='%s']";

    public AbstractPage(Page page) {
        this.page = page;
        this.calendar = page.locator(CALENDAR_XPATH);
        this.calendarMode = page.locator("//span[@class='g-date-calendar__mode-label']");
    }

    @Step("Wait for page to load")
    public void waitForPageToLoad() {
        page.waitForSelector(LOADING_ANIMATION_SELECTOR, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.HIDDEN));
    }

    @Step("Select dates from date picker by label")
    protected void selectDateRangeInElement(Locator locator, String dateFrom, String dateTo) {
        locator.click();
        page.waitForSelector(CALENDAR_XPATH, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
        calendarMode.click();
        calendarMode.click();
        LocalDate dateFromLocal = LocalDate.parse(dateFrom);
        String yearFrom = String.valueOf(dateFromLocal.getYear());
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMM");
        String monthFrom = dateFromLocal.format(monthFormatter);
        String dayFrom = String.valueOf(dateFromLocal.getDayOfMonth());
        LocalDate dateToLocal = LocalDate.parse(dateTo);
        String yearTo = String.valueOf(dateToLocal.getYear());
        String monthTo = dateToLocal.format(monthFormatter);
        String dayTo = String.valueOf(dateToLocal.getDayOfMonth());
        page.locator(String.format(CALENDAR_BUTTON_WITH_TEXT_PATTERN, yearFrom)).click();
        page.locator(String.format(CALENDAR_BUTTON_WITH_TEXT_PATTERN, monthFrom)).click();
        page.locator(String.format(CALENDAR_BUTTON_WITH_TEXT_PATTERN, dayFrom)).click();
        calendarMode.click();
        calendarMode.click();
        page.locator(String.format(CALENDAR_BUTTON_WITH_TEXT_PATTERN, yearTo)).click();
        page.locator(String.format(CALENDAR_BUTTON_WITH_TEXT_PATTERN, monthTo)).click();
        page.locator(String.format(CALENDAR_BUTTON_WITH_TEXT_PATTERN, dayTo)).click();
        page.waitForSelector(CALENDAR_XPATH, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.HIDDEN));
    }

    @Step("Select dates from date picker by label")
    protected void selectDateInElement(Locator locator, String date) {
        selectDateRangeInElement(locator, date, date);
    }
}
