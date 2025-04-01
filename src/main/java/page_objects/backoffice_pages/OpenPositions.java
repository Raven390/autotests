package page_objects.backoffice_pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import helpers.data.ClientHelper;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;

import static helpers.database.DbHelper.deleteEntryFromDb;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.ConfigFactory.BASE_URL_E2E;
import static utils.Constants.*;

public class OpenPositions extends AbstractPage {


    private final Locator openPositionTab;
    private final Locator accountColumnHeader;
    private final Locator typeColumnHeader;
    private final Locator volumeColumnHeader;
    private final Locator floatingPnlColumnHeader;
    private final Locator openColumnHeader;
    private final Locator tpslColumnHeader;
    private final Locator swapColumnHeader;
    private final Locator srColumnHeader;
    private final Locator commissionColumnHeader;
    private final Locator methodColumnHeader;
    private final Locator commentColumnHeader;
    private final Locator filterButton;


    private static final String ACCOUNT_ROW_HEADER = "*[contains(@class, 'v-header-cell') and text()= 'ACCOUNT']";
    private static final String TYPE_ROW_HEADER = "*[contains(@class, 'v-header-cell') and text()= 'TYPE']";
    private static final String VOLUME_ROW_HEADER = "*[contains(@class, 'v-header-cell') and text()= 'VOLUME']";
    private static final String OPEN_ROW_HEADER = "*[contains(@class, 'v-trading-tab-open-positions__sortable-cell') and text()= 'OPEN']";
    private static final String TPSL_ROW_HEADER = "*[contains(@class, 'v-header-cell') and text()= 'TP/SL']";
    private static final String FLOATING_PNL_ROW_HEADER = "*[contains(@class, 'v-trading-tab-open-positions__sortable-cell') and text()= 'FLOATING PNL']";
    private static final String SWAP_ROW_HEADER = "*[contains(@class, 'v-header-cell') and text()= 'SWAP']";
    private static final String SR_ROW_HEADER = "*[contains(@class, 'v-header-cell') and text()= 'SR']";
    private static final String COMMISSION_ROW_HEADER = "*[contains(@class, 'v-header-cell') and text()= 'COMMISSION']";
    private static final String METHOD_ROW_HEADER = "*[contains(@class, 'v-header-cell') and text()= 'METHOD']";
    private static final String COMMENT_ROW_HEADER = "*[contains(@class, 'v-header-cell') and text()= 'COMMENT']";
    private static final String TABLE_CELL_LOCATOR = "*[@class='v-body-cell']";
    private static final String TABLE_ROW_LOCATOR = "*[contains(@class, 'v-body-row')]";
    private static final String DEAL_TYPE_TEXT_LOCATOR = "*[contains(@class, 'v-trading-tab-open-positions__deal-type ')]";


    public OpenPositions(Page page) {
        super(page);
        this.openPositionTab = page.locator(".g-radio-button__option-control[value=\"Open positions\"]");


        this.accountColumnHeader = page.locator("//" + ACCOUNT_ROW_HEADER);
        this.typeColumnHeader = page.locator("//" + TYPE_ROW_HEADER);
        this.volumeColumnHeader = page.locator("//" + VOLUME_ROW_HEADER);
        this.openColumnHeader = page.locator("//" + OPEN_ROW_HEADER);
        this.tpslColumnHeader = page.locator("//" + TPSL_ROW_HEADER);
        this.floatingPnlColumnHeader = page.locator("//" + FLOATING_PNL_ROW_HEADER);
        this.swapColumnHeader = page.locator("//" + SWAP_ROW_HEADER);
        this.srColumnHeader = page.locator("//" + SR_ROW_HEADER);
        this.commissionColumnHeader = page.locator("//" + COMMISSION_ROW_HEADER);
        this.methodColumnHeader = page.locator("//" + METHOD_ROW_HEADER);
        this.commentColumnHeader = page.locator("//" + COMMENT_ROW_HEADER);
        this.filterButton = page.locator(".v-open-positions-controls__filters");
    }

    public void openPositionsClean(ClientHelper client) {
        openPositionsClean(client.getUcid());
    }

    public void openPositionsClean(String ucid) {
        deleteEntryFromDb(MT4_TRADES_TABLE_NAME, "ucid = '" + ucid + "'");
        deleteEntryFromDb(MT5_POSITIONS_TABLE_NAME, "ucid = '" + ucid + "'");
        deleteEntryFromDb(MT5_DEALS_COERCED_TABLE_NAME, "ucid = '" + ucid + "'");
    }

    public void navigateOpenPositions(String ucid) {
        Allure.step("Navigate to users trading tab/Open Positions");
        page.navigate(String.format("%sinvestigation/%s/trading/open-positions", BASE_URL_E2E, ucid));
        super.waitForPageToLoad();
    }

    public void checkAccountCellValue(String account, String expectedPlatform) {
        Allure.step("Check platform in account cell");
        super.waitForPageToLoad();
        String locator = "//" + TABLE_CELL_LOCATOR + "//*[text()='" + account + "']/ancestor::" + TABLE_ROW_LOCATOR + "/" + TABLE_CELL_LOCATOR + "[1]" + "/descendant::" + SECONDARY_TEXT;
        page.locator(locator).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        assertEquals(expectedPlatform, page.locator(locator).textContent());
    }

    public void checkAccountCellValue(Integer account, String expectedPlatform) {
        checkAccountCellValue(account.toString(), expectedPlatform);
    }

    public void checkTypeCellValue(String account, String symbol, String type) {
        Allure.step("Check platform in account cell");
        super.waitForPageToLoad();
        String locator = "//" + TABLE_CELL_LOCATOR + "//*[text()='" + account + "']/ancestor::" + TABLE_ROW_LOCATOR + "/" + TABLE_CELL_LOCATOR + "[2]";
        page.locator(locator).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        assertEquals(symbol, page.locator(locator + "/descendant::" + PRIMARY_TEXT).textContent());
        assertEquals(type, page.locator(locator + "/descendant::" + DEAL_TYPE_TEXT_LOCATOR).textContent());
    }

    public void checkTypeCellValue(Integer account, String symbol, String type) {
        checkTypeCellValue(account.toString(), symbol, type);
    }

    public void checkVolumeCellValue(String account, String volumeLots, String volumeUsd) {
        Allure.step("Check platform in account cell");
        super.waitForPageToLoad();
        String locator = "//" + TABLE_CELL_LOCATOR + "//*[text()='" + account + "']/ancestor::" + TABLE_ROW_LOCATOR + "/" + TABLE_CELL_LOCATOR + "[3]";
        page.locator(locator).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        assertEquals(volumeLots + " lots", page.locator(locator + "/descendant::" + PRIMARY_TEXT).textContent());
        assertEquals(volumeUsd + " USD", page.locator(locator + "/descendant::" + SECONDARY_TEXT).textContent());
    }

    public void checkVolumeCellValue(Integer account, Double volumeLots, Double volumeUsd) {
        checkVolumeCellValue(account.toString(), decimalFormat1.format(volumeLots), decimalFormat1.format(volumeUsd));
    }

    public void checkOpenCellValue(String account, String openPrice, String openTime) {
        Allure.step("Check platform in account cell");
        super.waitForPageToLoad();
        String locator = "//" + TABLE_CELL_LOCATOR + "//*[text()='" + account + "']/ancestor::" + TABLE_ROW_LOCATOR + "/" + TABLE_CELL_LOCATOR + "[4]";
        page.locator(locator).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        assertEquals(openPrice, page.locator(locator + "/descendant::" + PRIMARY_TEXT).textContent());
        assertEquals(openTime, page.locator(locator + "/descendant::" + SECONDARY_TEXT).textContent());
    }

    public void checkOpenCellValue(Integer account, Double openPrice, String openTime) {
        checkOpenCellValue(account.toString(), decimalFormat1.format(openPrice), openTime);
    }

    public void checkTpSlCellValue(String account, String tp, String sl) {
        Allure.step("Check platform in account cell");
        super.waitForPageToLoad();
        String locator = "//" + TABLE_CELL_LOCATOR + "//*[text()='" + account + "']/ancestor::" + TABLE_ROW_LOCATOR + "/" + TABLE_CELL_LOCATOR + "[5]";
        page.locator(locator).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        assertEquals("TP " + tp, page.locator(locator + "/descendant::" + PRIMARY_TEXT).textContent());
        assertEquals("SL " + sl, page.locator(locator + "/descendant::" + SECONDARY_TEXT).textContent());
    }

    public void checkTpSlCellValue(Integer account, Double volumeLots, Double volumeUsd) {
        checkTpSlCellValue(account.toString(), decimalFormat1.format(volumeLots), decimalFormat1.format(volumeUsd));
    }

    public void checkFloatingPnlCellValue(String account, String pnl) {
        Allure.step("Check platform in account cell");
        super.waitForPageToLoad();
        String locator = "//" + TABLE_CELL_LOCATOR + "//*[text()='" + account + "']/ancestor::" + TABLE_ROW_LOCATOR + "/" + TABLE_CELL_LOCATOR + "[6]";
        page.locator(locator).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        assertEquals(pnl + " USD", page.locator(locator + "/descendant::" + PRIMARY_TEXT).textContent());
    }

    public void checkFloatingPnlCellValue(Integer account, Double pnl) {
        checkFloatingPnlCellValue(account.toString(), decimalFormat1.format(pnl));
    }

    public void checkSwapCellValue(String account, String swap) {
        Allure.step("Check platform in account cell");
        super.waitForPageToLoad();
        String locator = "//" + TABLE_CELL_LOCATOR + "//*[text()='" + account + "']/ancestor::" + TABLE_ROW_LOCATOR + "/" + TABLE_CELL_LOCATOR + "[7]";
        page.locator(locator).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        assertEquals(swap + " USD", page.locator(locator + "/descendant::" + PRIMARY_TEXT).textContent());
    }

    public void checkSwapCellValue(Integer account, Double swap) {
        checkSwapCellValue(account.toString(), decimalFormat1.format(swap));
    }

    public void checkCommissionCellValue(String account, String commission) {
        Allure.step("Check platform in account cell");
        super.waitForPageToLoad();
        String locator = "//" + TABLE_CELL_LOCATOR + "//*[text()='" + account + "']/ancestor::" + TABLE_ROW_LOCATOR + "/" + TABLE_CELL_LOCATOR + "[9]";
        page.locator(locator).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        assertEquals(commission + " USD", page.locator(locator + "/descendant::" + PRIMARY_TEXT).textContent());
    }

    public void checkCommissionCellValue(Integer account, Double commission) {
        checkCommissionCellValue(account.toString(), decimalFormat1.format(commission));
    }

    public void checkMethodCellValue(String account, String method) {
        Allure.step("Check platform in account cell");
        super.waitForPageToLoad();
        String locator = "//" + TABLE_CELL_LOCATOR + "//*[text()='" + account + "']/ancestor::" + TABLE_ROW_LOCATOR + "/" + TABLE_CELL_LOCATOR + "[10]";
        page.locator(locator).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        assertEquals(method, page.locator(locator + "/descendant::" + PRIMARY_TEXT).textContent());
    }

    public void checkMethodCellValue(Integer account, String method) {
        checkMethodCellValue(account.toString(), method);
    }

    public void checkCommentCellValue(String account, String comment) {
        Allure.step("Check platform in account cell");
        super.waitForPageToLoad();
        String locator = "//" + TABLE_CELL_LOCATOR + "//*[text()='" + account + "']/ancestor::" + TABLE_ROW_LOCATOR + "/" + TABLE_CELL_LOCATOR + "[11]";
        page.locator(locator).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        assertEquals(comment, page.locator(locator + "/descendant::" + PRIMARY_TEXT).textContent());
    }

    public void checkCommentCellValue(Integer account, String comment) {
        checkCommentCellValue(account.toString(), comment);
    }


    @Step("Check if the trading/operations tab renders all basic elements")
    public void openPositionsRenders() {
        Allure.step("Check if the trading/operations tab renders all basic elements");
        waitForPageToLoad();
        openPositionTab.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        accountColumnHeader.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        typeColumnHeader.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        volumeColumnHeader.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        floatingPnlColumnHeader.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        openColumnHeader.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        tpslColumnHeader.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        swapColumnHeader.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        srColumnHeader.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        commissionColumnHeader.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        methodColumnHeader.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        commentColumnHeader.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        filterButton.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        assertEquals(page.locator("//" + TABLE_ROW_LOCATOR + "//" + TABLE_CELL_LOCATOR).count(), 11);
    }
}
