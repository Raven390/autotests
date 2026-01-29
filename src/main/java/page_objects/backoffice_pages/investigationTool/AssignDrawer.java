package page_objects.backoffice_pages.investigationTool;

import static com.microsoft.playwright.options.WaitForSelectorState.HIDDEN;
import static com.microsoft.playwright.options.WaitForSelectorState.VISIBLE;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import page_objects.backoffice_pages.AbstractPage;

public class AssignDrawer extends AbstractPage {

    private final Locator assignDrawerButton;
    private final Locator assignDrawerBody;
    private static final String ASSIGN_DRAWER_LOCATOR =
            "//*[@class='v-assign-user-selector']/ancestor::*[@data-qa='drawer_body']";
    private static final String USER_PRESET_PATTERN = "//*[@class = 'v-assign-user-presets']//span[text()='%s']";
    private static final String USER_SELECT_ELEMENT_PATTERN =
            "//*[@class = 'v-assign-user-item__name']/descendant::div[text()='%s']";
    private final Locator assigneeNameInput;
    private final Locator commentInput;
    private final Locator cancelButton;
    private final Locator confirmAssignButton;

    public AssignDrawer(Page page) {
        super(page);
        this.assignDrawerButton = page.locator("//*[@data-qa='investigation_tools__open_assign_user_button']");
        this.assignDrawerBody = page.locator(ASSIGN_DRAWER_LOCATOR);
        this.assigneeNameInput = page.locator(ASSIGN_DRAWER_LOCATOR + "//input[@placeholder='Unassigned']");
        this.commentInput = page.locator(ASSIGN_DRAWER_LOCATOR
                + "//input[@placeholder='You can write a reason or leave a note for your colleague']");
        this.cancelButton = page.locator("//button[@data-qa='assign_user_drawer__cancel_button']");
        this.confirmAssignButton = page.locator("//button[@data-qa='assign_user_drawer__apply_changes_button']");
    }

    public void openAssignDrawer() {
        Allure.step("Open assign drawer");
        assignDrawerButton.click();
        assignDrawerBody.waitFor(new Locator.WaitForOptions().setState(VISIBLE));
    }

    public void selectUserForAssign(String userPreset) {
        Allure.step("Select user for assign:");
        assignDrawerBody.locator(String.format(USER_PRESET_PATTERN, userPreset)).click();
        confirmAssignButton.click();
    }

    public void setComment(String comment) {
        Allure.step("Set comment");
        commentInput.fill(comment);
    }

    public void cancelAssign() {
        Allure.step("Cancel assign");
        cancelButton.click();
        assignDrawerBody.waitFor(new Locator.WaitForOptions().setState(HIDDEN));
    }

    public void confirmAssign() {
        Allure.step("Confirm assign");
        confirmAssignButton.click();
        assignDrawerBody.waitFor(new Locator.WaitForOptions().setState(HIDDEN));
    }

    public void assignClientToMyself(String comment) {
        waitForPageToLoad();
        Allure.step("Assign client to myself");
        assignDrawerBody
                .locator(String.format(USER_PRESET_PATTERN, "Assign to me"))
                .click();
        if (comment != null) {
            commentInput.fill(comment);
        }
        confirmAssign();
    }

    public void assignClientToMyself() {
        assignClientToMyself(null);
    }

    public void assignClientToUser(String userPreset, String comment) {
        Allure.step("Assign client to user");
        waitForPageToLoad();
        assigneeNameInput.fill(userPreset);
        page.locator(String.format(USER_SELECT_ELEMENT_PATTERN, userPreset)).click();
        if (comment != null) {
            commentInput.fill(comment);
        }
        confirmAssign();
    }

    public void unassignClient(String comment) {
        Allure.step("Assign client to user");
        waitForPageToLoad();
        assigneeNameInput.click();
        page.locator(String.format(USER_SELECT_ELEMENT_PATTERN, "Unassigned")).click();
        if (comment != null) {
            commentInput.fill(comment);
        }
        confirmAssign();
    }

    public void unassignClient() {
        unassignClient(null);
    }

    public void assignClientToUser(String userPreset) {
        assignClientToUser(userPreset, null);
    }
}
