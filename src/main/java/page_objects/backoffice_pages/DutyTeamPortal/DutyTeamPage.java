package page_objects.backoffice_pages.DutyTeamPortal;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import page_objects.backoffice_pages.AbstractPage;


import static utils.ConfigFactory.BASE_URL_E2E;

public class DutyTeamPage extends AbstractPage {

    private final Locator dutyTeamUi;

    public DutyTeamPage(Page page) {
        super(page);
        this.dutyTeamUi = page.locator(".v-duty-team-portal");
    }


    @Step("Navigate to Duty Team Portal")
    public void navigateToDutyTeamPage() {
        Allure.step("Navigate to Duty Team Portal");
        page.navigate(BASE_URL_E2E + "duty-team-portal");
        waitForPageToLoad();
    }


    @Step("check if Duty Team Portal opened")
    public void isDutyTeamPageOpened(Page page) {
        dutyTeamUi.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }
}

