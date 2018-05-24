/*
package pageObjPattern.pages.appPages.forms;

import org.apache.log4j.CustomLogging;
import pageObjPattern.pages.appPages.MobileLoginPage;
import pageObjPattern.pages.appPages.elements.forms.MobileFormsElements;
import pageObjPattern.pages.appPages.tasks.MobileTasksPage;
import pageObjPattern.pages.offline.OfflineFormsPage;
import utils.SysUtils;
import utils.mobile.MobileDriver;

import java.util.concurrent.TimeUnit;

public class MobileFormsPage extends OfflineFormsPage {

    private static final CustomLogging LOG = CustomLogging.getLogger(MobileFormsPage.class);
    private MobileFormsElements formsElements;
    private MobileDriver mobileDriver;

    public MobileFormsPage(MobileDriver mobileDriver) {
        super(mobileDriver.getWebDriver());
        this.mobileDriver = mobileDriver;
        formsElements = new MobileFormsElements();
    }

    */
/**
     * Form List menu.
     *//*


   */
/* public void selectSurveyBySID(long surveyId) {
        LOG.info("SELECT Survey in survey list by ID: " + surveyId + ".");
        waitForElementDisplayedMobile(formsElements.getSurveyById(surveyId));
        scrollIntoView(formsElements.getSurveyById(surveyId));
        click(formsElements.getSurveyById(surveyId));
        waitForElementDisplayed(formsElements.getResponseListElement());
        waitForLoadingFinish();
    }

    public void clickResponseInResponseListByNum(int num) {
        LOG.info("CLICK Response #" + (num + 1) + " in response list.");
        waitForElementDisplayed(formsElements.getResponseInResponseList(num));
        click(formsElements.getResponseInResponseList(num));
        waitForLoadingFinish();
    }

    *//*
*/
/**
     * Responses List menu.
     *//*
*/
/*

    public MobileVotingPage clickAddNewRespondentButton() {
        clickAddNewRespondentWithoutWait();
        waitForPageLoaded();

        MobileVotingPage mobileVotingPage = new MobileVotingPage(mobileDriver);
        mobileVotingPage.waitForBackToResponseButtonPresent();
        return mobileVotingPage;
    }

    public MobileVotingPage clickEditResponseOnView() {
        LOG.info("CLICK Edit button on Record view.");
        waitForElementDisplayed(formsElements.getGoToEditButton());
        click(formsElements.getGoToEditButton());
        waitForPageLoaded();

        MobileVotingPage mobileVotingPage = new MobileVotingPage(mobileDriver);
        mobileVotingPage.waitForBackToResponseButtonPresent();
        return mobileVotingPage;
    }

    public MobileVotingPage goToEditResponse() {
        clickViewResponseButton();
        return clickEditResponseOnView();
    }

    public MobileViewPage clickViewResponseButton() {
        LOG.info("CLICK View response button.");
        waitForElementDisplayedMobile(formsElements.getViewButton());
        click(formsElements.getViewButton());
        waitForPageLoaded();

        MobileViewPage mobileViewPage = new MobileViewPage(mobileDriver);
        mobileViewPage.waitForBackToResponseButtonDisplayed();
        return mobileViewPage;
    }

    public MobileVotingPage clickEditResponseButton() {
        LOG.info("CLICK Edit button on Record Details.");
        waitForElementDisplayed(formsElements.getEditResponseButton());
        click(formsElements.getEditResponseButton());

        MobileVotingPage mobileVotingPage = new MobileVotingPage(mobileDriver);
        mobileVotingPage.waitForBackToResponseButtonPresent();
        return mobileVotingPage;
    }

    public void clickPinButtonOnResponseDetails() {
        LOG.info("CLICK 'Pin' button on response details.");
        click(formsElements.getPinButtonOnResponseDetails());
        waitForLoadingFinish();
    }

    public void clickBackToFormsButton() {
        LOG.info("CLICK Back To Forms button.");
        waitForElementDisplayed(formsElements.getBackToFormsButton());
        click(formsElements.getBackToFormsButton());
    }

    public void clickBackToFormsButtonWithWait() {
        clickBackToFormsButton();
        waitForOpenMenuButtonDisplayed();
        waitForLoadingFinish();
    }

    *//*
*/
/**
     * Basic tabs.
     *//*
*/
/*

    public String getFormTabName() {
        return getText(formsElements.getFormTab());
    }

    public MobileTasksPage clickTaskTab() {
        waitForElementDisplayed(formsElements.getTaskTab());
        click(formsElements.getTaskTab());

        MobileTasksPage mobileTasksPage = new MobileTasksPage(mobileDriver);
        mobileTasksPage.waitForTaskDefinitionsListDisplayed();
        mobileTasksPage.waitForLoadingFinish();
        return new MobileTasksPage(mobileDriver);
    }

    public String getTaskTabName() {
        return getText(formsElements.getTaskTab());
    }

    public boolean isTaskTabVisible() {
        return isElementDisplayed(formsElements.getTaskTab());
    }

    *//*
*/
/**
     * Menu popup.
     *//*
*/
/*

    public void waitForOpenMenuButtonDisplayed() {
        waitForElementDisplayedMobile(formsElements.getOpenMenuButtonByClassName());
    }

    private void clickOpenMenuButton() {
        LOG.info("CLICK Menu button.");
        waitForOpenMenuButtonDisplayed();
        clickOnVisibleElement(formsElements.getOpenMenuButtonByClassName());
    }

    public void openMenu() {
        clickOpenMenuButton();
        waitForDisplayedAtLeastOneOfTheElements(formsElements.getSynchronizeButton(), 10);
    }

    public void closeMenu() {
        clickOpenMenuButton();
        waitForElementNotVisibleByTime(formsElements.getSynchronizeButton(), 10);
        waitForLoadingFinish();
    }

    public MobileLoginPage clickLogoutButton() {
        clickOnVisibleElement(formsElements.getLogoutButton());
        return new MobileLoginPage(mobileDriver);
    }

    private void clickOKButton() {
        waitForElementDisplayed((formsElements.getOkButton()));
        click(formsElements.getOkButton());
    }

    public boolean isNotUploadCountVisible() {
        return isElementDisplayed(formsElements.getNotSyncCount());
    }

    public MobileFormsPage uploading() {
        openMenu();
        clickUploadButton();
        waitForSynchronization();
        waitForOpenMenuButtonDisplayed();
        waitForLoadingFinish();
        return new MobileFormsPage(mobileDriver);
    }

    public MobileFormsPage downloading() {
        openMenu();
        clickDownloadButton();
        waitForSynchronization();
        waitForOpenMenuButtonDisplayed();
        waitForLoadingFinish();
        return new MobileFormsPage(mobileDriver);
    }

    public void logoutFromPortal() {
        openMenu();
        clickLogoutButton();
        clickOKButton();
        waitForElementNotVisibleByTime(formsElements.getOpenMenuButtonByClassName(), 30);
    }

    public void synchronization() {
        openMenu();
        clickSynchronizeButton();
        waitForSynchronization();
        waitForOpenMenuButtonDisplayed();
    }

    public void waitForSynchronization() {
        LOG.info("Synchronization started");
        waitForElementNotVisibleByTime(formsElements.getMobileSyncDialog(), 360);
        waitForLoadingFinish();
        LOG.info("Synchronization finished");
    }

    public void waitForSyncDialogDisplayed() {
        waitForElementDisplayedMobile(formsElements.getMobileSyncDialog());
    }

    public void waitForLoadingFinish() {
        LOG.debug("Wait for loading element disappear...");
        setImplicitlyWait(1, TimeUnit.MILLISECONDS);
        for (int i = 0; i < 3; i++) {
            if (isElementDisplayedMobile(formsElements.getLoadingElement())) {
                LOG.debug("Timer...");
                SysUtils.sleep(1000);
            }
        }
        setImplicitlyWait(60, TimeUnit.SECONDS);
    }
*//*

}
*/
