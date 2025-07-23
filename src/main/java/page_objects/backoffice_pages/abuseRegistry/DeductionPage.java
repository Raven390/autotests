package page_objects.backoffice_pages.abuseRegistry;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import page_objects.backoffice_pages.AbstractPage;

import java.util.ArrayList;
import java.util.List;

import static com.microsoft.playwright.options.WaitForSelectorState.VISIBLE;

public class DeductionPage extends AbstractPage {

    private final Locator abuseRegistryButton;
    private final Locator deductionTabButton;
    private final Locator deductionTableHeaders;
    private final Locator deductionTableRow;
    private final Locator deductionTableRowData;
    private final Locator tableBody;
    private final Locator statusFilter;
    private final Locator emailFilter;
    private final Locator filterOptions;
    private final Locator statusValues;
    private final Locator emailValues;

    private static final String FILTER_OPTION_BY_TEXT_PATTERN = "//div[@data-qa='select-popup']/descendant::span[text()='%s']";
    private static final String COLUMN_VALUE_BY_ORDER_PATTERN = "//div[contains(@class,'v-body-cell')][%s]/descendant::div[contains(@class,'g-text')]";
    private static final String DEDUCTION_FILTER_BY_ORDER_PATTERN = "//div[@class='v-deductions-filters']/div[%s]/descendant::button[contains(@class,'g-select-control__button')]";

    public DeductionPage(Page page) {
        super(page);
        this.abuseRegistryButton = page.locator("//a[@href='/abuse-registry']");
        this.deductionTabButton = page.locator("//input[@value='DEDUCTIONS']");
        this.deductionTableHeaders = page.locator("//div[contains(@class,'v-header-cell')]");
        this.deductionTableRow = page.locator("//div[contains(@class,'v-body-row')]");
        this.deductionTableRowData = page.locator("//div[contains(@class,'v-body-cell')]/descendant::div[contains(@class,'g-text')]");
        this.tableBody = page.locator("//div[@class='v-table-body']");
        this.statusFilter = page.locator(String.format(DEDUCTION_FILTER_BY_ORDER_PATTERN, 1));
        this.emailFilter = page.locator(String.format(DEDUCTION_FILTER_BY_ORDER_PATTERN, 2));
        this.filterOptions = page.locator("//div[@data-qa='select-popup']/descendant::span[@class='g-select-list__option-default-label']");
        this.statusValues = deductionTableRow.locator(String.format(COLUMN_VALUE_BY_ORDER_PATTERN, 5));
        this.emailValues = deductionTableRow.locator(String.format(COLUMN_VALUE_BY_ORDER_PATTERN, 6)).first();
    }

    @Step("Click abuse registry button")
    public void clickAbuseRegistryButton() {
        abuseRegistryButton.click();
    }

    @Step("Click deduction tab button")
    public void clickDeductionTabButton() {
        deductionTabButton.click();
    }

    @Step("Get deduction table headers")
    public List<String> getDeductionTableHeaders() {
        tableBody.waitFor(new Locator.WaitForOptions().setState(VISIBLE));
        List<String> list = new ArrayList<>();
        for (int i = 0; i < deductionTableHeaders.count(); i++) {
            list.add(deductionTableHeaders.nth(i).textContent());
        }
        return list;
    }

    @Step("Get deduction table data by rows")
    public List<List<String>> getDeductionTableDataByRows() {
        tableBody.waitFor(new Locator.WaitForOptions().setState(VISIBLE));
        List<List<String>> list = new ArrayList<>();
        for (int i = 0; i < deductionTableRow.count(); i++) {
            Locator row = deductionTableRow.nth(i);
            List<String> rowDataList = new ArrayList<>();
            for (int k = 0; k < row.locator(deductionTableRowData).count(); k++) {
                rowDataList.add(row.locator(deductionTableRowData).nth(k).textContent());
            }
            list.add(rowDataList);
        }
        return list;
    }

    @Step("Click status filter")
    public void clickStatusFilter() {
        statusFilter.click();
    }

    @Step("Click email filter")
    public void clickEmailFilter() {
        emailFilter.click();
    }

    @Step("Click filter option by text")
    public void clickFilterOptionByText(String text) {
        page.locator(String.format(FILTER_OPTION_BY_TEXT_PATTERN, text)).click();
    }

    @Step("Get filter options")
    public List<String> getFilterOptions() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < filterOptions.count(); i++) {
            list.add(filterOptions.nth(i).textContent());
        }
        return list;
    }

    @Step("Get status values from the deduction table")
    public List<String> getStatusValues() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < statusValues.count(); i++) {
            list.add(statusValues.nth(i).textContent());
        }
        return list;
    }

    @Step("Get email values from the deduction table")
    public List<String> getEmailValues() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < emailValues.count(); i++) {
            list.add(emailValues.nth(i).textContent());
        }
        return list;
    }
}

