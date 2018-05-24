/*
package pageObjPattern.pages.appPages.forms;

import org.apache.log4j.CustomLogging;
import pageObjPattern.pages.appPages.elements.forms.MobileViewElements;
import pageObjPattern.pages.offline.OfflineViewPage;
import utils.mobile.MobileDriver;

public class MobileViewPage extends OfflineViewPage {

    private static CustomLogging LOG = CustomLogging.getLogger(MobileViewPage.class);

    */
/*private MobileViewElements viewElements;
    private MobileDriver mobileDriver;

    public MobileViewPage(MobileDriver mobileDriver) {
        super(mobileDriver.getWebDriver());
        this.mobileDriver = mobileDriver;
        viewElements = new MobileViewElements();
    }

    public void clickViewNextPageButton() {
        scrollTo(viewElements.getViewNextPageButton());
        click(viewElements.getViewNextPageButton());
    }

    public void waitForBackToResponseButtonDisplayed() {
        waitForElementDisplayedMobile(viewElements.getBackToResponsesButton());
    }

    public void waitForPreviousPageButtonDisplayed() {
        waitForElementDisplayedMobile(viewElements.getViewPreviousPageButton());
    }

    public MobileFormsPage clickBackToResponsesButton() {
        waitForBackToResponseButtonDisplayed();
        scrollTo(viewElements.getBackToResponsesButton());
        click(viewElements.getBackToResponsesButton());

        MobileFormsPage mobileFormsPage = new MobileFormsPage(mobileDriver);
        mobileFormsPage.waitForOpenMenuButtonDisplayed();
        mobileFormsPage.waitForLoadingFinish();
        return mobileFormsPage;
    }

    public MobileVotingPage clickEditOnViewButton() {
        waitForElementDisplayedMobile(viewElements.getGoToEditButton());
        scrollTo(viewElements.getGoToEditButton());
        click(viewElements.getGoToEditButton());
        waitForAjax(10);
        return new MobileVotingPage(mobileDriver);
    }

    *//*
*/
/**
     * Download file attachments.
     *//*
*/
/*

    public void clickDownloadAllAttachedFilesButton() {
        LOG.info("CLICK 'Download all attached files' button.");
        click(viewElements.getDownloadAllAttachedFilesButton());
        waitForDialog();
    }

    public boolean isDownloadAllAttachedFilesButtonVisible() {
        return isElementDisplayed(viewElements.getDownloadAllAttachedFilesButton());
    }

    public void clickAnswerDownloadFileFromServerButton(long questionId, long answerId) {
        LOG.info("CLICK 'Download file from server' button for answer '" + answerId + "'.");
        click(viewElements.getAnswerDownloadFileFromServerLink(questionId, answerId));
        waitForDialog();
    }

    public boolean isDownloadAnswerFileFromServerButtonVisible(long questionId, long answerId) {
        LOG.info("CLICK 'Download file from server' button for answer '" + answerId + "'.");
        return isElementDisplayed(viewElements.getAnswerDownloadFileFromServerLink(questionId, answerId));
    }

    private void waitForDialog() {
        waitForElementDisplayedMobile(viewElements.getDialog());
        waitForElementNotVisibleByTime(viewElements.getDialog(), 60);
        waitForAjax(10);
    }*//*


}
*/
