/*
package pageObjPattern.pages.appPages.forms;

import org.apache.log4j.CustomLogging;
import pageObjPattern.pages.appPages.elements.forms.MobileVotingElements;

import utils.SysUtils;
import utils.mobile.MobileDriver;
import utils.mobile.drivers.DesktopDeviceDriver;

import java.util.concurrent.TimeUnit;

public class MobileVotingPage  {

    private static CustomLogging LOG = CustomLogging.getLogger(MobileVotingPage.class);

    private MobileVotingElements votingElements;
    private MobileDriver mobileDriver;

    public MobileVotingPage(MobileDriver mobileDriver) {
        super(mobileDriver.getWebDriver());
        this.mobileDriver = mobileDriver;
        votingElements = new MobileVotingElements();
    }

    */
/**
     * Navigation buttons manipulation.
     *//*

*/
/*
    public void clickNextButtonWithoutWait() {
        LOG.info("CLICK 'Next' button.");
        waitForElementDisplayedMobile(votingElements.getNextButton());
        moveToAndClick(votingElements.getNextButton());
    }

    public void clickNextButtonWithWait() {
        clickNextButtonWithoutWait();
        waitForSunLoadFinish();
        waitForPageLoaded();
        waitForSunLoadFinish();
    }

    public void clickNextButtonWithBackToResponseButtonWait() {
        clickNextButtonWithWait();
        waitForBackToResponseButtonPresent();
    }

    public void clickBackButton() {
        LOG.info("CLICK 'Back' button.");
        waitForElementDisplayedMobile(votingElements.getBackButton());
        moveToAndClick(votingElements.getBackButton());
    }

    public void clickBackButtonWithWait() {
        clickBackButton();
        waitForSunLoadFinish();
        waitForPageLoaded();
        waitForSunLoadFinish();
    }

    public void clickBackButtonWithPreviousPageWait() {
        clickBackButtonWithWait();
        waitForBackToResponseButtonPresent();
    }

    public void waitForSubmitButton() {
        waitForElementDisplayedMobile(votingElements.getSubmitButton());
    }

    public void clickSubmitButton() {
        LOG.info("CLICK Submit voting button.");
        waitForSubmitButton();
        focusOnElementById(votingElements.getSubmitButtonId());
        click(votingElements.getSubmitButton());
    }

    public MobileFormsPage clickSubmitButtonWithWait() {
        clickSubmitButton();
        MobileFormsPage mobileFormsPage = new MobileFormsPage(mobileDriver);
        mobileFormsPage.waitForOpenMenuButtonDisplayed();
        mobileFormsPage.waitForLoadingFinish();
        return mobileFormsPage;
    }

    public void clickSaveButton() {
        LOG.info("CLICK Save voting button.");
        waitForElementDisplayedMobile(votingElements.getSaveButton());
        click(votingElements.getSaveButton());
        SysUtils.sleep(1000);
        waitForSunLoadFinish();
    }

    private void clickSaveWhenBackButtonDialog() {
        waitForElementDisplayed(votingElements.getDialog());
        click(votingElements.getSaveWhenBackButtonDialog());
    }

    public MobileFormsPage clickSaveWhenBackButtonDialogWithWait() {
        clickSaveWhenBackButtonDialog();
        MobileFormsPage mobileFormsPage = new MobileFormsPage(mobileDriver);
        mobileFormsPage.waitForOpenMenuButtonDisplayed();
        mobileFormsPage.waitForLoadingFinish();
        return mobileFormsPage;
    }

    public void waitForBackToResponseButtonPresent() {
        waitForElementDisplayedMobile(votingElements.getBackToResponseButton());
        waitForSunLoadFinish();
    }

    public MobileFormsPage clickBackToResponsesButton() {
        LOG.info("CLICK 'Back to responses' button.");
        waitForBackToResponseButtonPresent();
        scrollTo(votingElements.getBackToResponseButton());
        click(votingElements.getBackToResponseButton());
        return new MobileFormsPage(mobileDriver);
    }

    public MobileFormsPage clickBackToResponsesButtonWithWait() {
        clickBackToResponsesButton();
        waitForPageLoaded();

        MobileFormsPage mobileFormsPage = new MobileFormsPage(mobileDriver);
        mobileFormsPage.waitForOpenMenuButtonDisplayed();
        mobileFormsPage.waitForLoadingFinish();
        return mobileFormsPage;
    }

    public void waitForSunLoadFinish() {
        LOG.info("Wait for sunLoad element disappear...");
        setImplicitlyWait(10, TimeUnit.MILLISECONDS);
        for (int i = 0; i < 3; i++) {
            if (isElementDisplayedMobile(votingElements.getSunLoadImage())) {
                SysUtils.sleep(1000);
            }
        }
        setImplicitlyWait(60, TimeUnit.SECONDS);
    }

    private void clickCopyAndSubmitButton() {
        LOG.info("CLICK Submit&Copy voting button.");
        waitForElementDisplayedMobile(votingElements.getSubmitAndCopyButton());
        click(votingElements.getSubmitAndCopyButton());
    }

    public void clickCopyAndSubmitButtonWithWait() {
        clickCopyAndSubmitButton();
        waitForSunLoadFinish();
        waitForPageLoaded();
        waitForSunLoadFinish();
    }

    public boolean isSubmitAndCopyVisible() {
        return isElementDisplayed(votingElements.getSubmitAndCopyButton());
    }

    *//*
*/
/**
     * Question/ Answer elements manipulation.
     *//*
*/
/*

    public void clearAndTypeTextViaKeyboard(long questionId, long answerId, CharSequence value) {
        LOG.info("TYPE text to TextInputComponent. Question Id: " + questionId + ", AnswerId:  " + answerId + ", Text: " + value + ".");
        mobileDriver.clearAndSendKeysViaKeyboard(votingElements.getSingleLineAnswer(questionId, answerId), value);
    }

    public String getQuestionLabelText(long questionId) {
        waitForElementDisplayedMobile(votingElements.getQuestionLabel(questionId));
        return getText(votingElements.getQuestionLabel(questionId));
    }

    public void clickLookupSelectButton(long questionId) {
        LOG.info("CLICK Lookup open options list button.");
        click(votingElements.getLookupSelectButton(questionId));
    }

    public void clickCheckAll3DMatrix(long qid, long aid, int index) {
        clickOnElementWithJS(votingElements.getCheckAllField3DMatrix(qid, aid, index));
    }

    public void clickPickOne3DMatrix(long qid, long aid, int index) {
        clickOnElementWithJS(votingElements.getPickOneField3DMatrix(qid, aid, index));
    }

    public String getHeaderText() {
        waitForElementDisplayedMobile(votingElements.getHeader());
        return getText(votingElements.getHeader());
    }

    public String getFooterText() {
        waitForElementDisplayedMobile(votingElements.getFooter());
        return getText(votingElements.getFooter());
    }

    public String getHeaderTabName(int number) {
        return getText(votingElements.getHeaderTabByNum(number));
    }

    public void clickOnHeaderTab(int number) {
        click(votingElements.getUnselectedHeaderTabByNum(number));
        SysUtils.sleep(2000);
    }

    public String getMobileFriendlyCheckBoxControlsAttribute(long questionId, long answerId, String value) {
        return getAttribute(votingElements.getMobileFriendlyCheckBoxControls(questionId, answerId), value);
    }

    *//*
*/
/**
     * Slider Plugin.
     *//*
*/
/*

    public void clickSlider(long questionId, long answerId) {
        click(votingElements.getSlider(questionId, answerId));
        SysUtils.sleep(2000);
    }

    *//*
*/
/**
     * Star Rating Plugin.
     *//*
*/
/*

    public boolean isStarRatingAnswerElementVisible(String QxAyCz, String classNew) {
        return isElementDisplayed(votingElements.getStarRatingAnswerElementByXpath(QxAyCz, classNew));
    }

    *//*
*/
/**
     * Download file attachments.
     *//*
*/
/*

    public boolean isDownloadAllAttachedFilesButtonVisible() {
        return isElementDisplayed(votingElements.getDownloadAllAttachedFilesButton());
    }

    public boolean isDownloadAnswerFileFromServerButtonVisible(long questionId, long answerId) {
        return isElementDisplayed(votingElements.getAnswerDownloadFileFromServerLink(questionId, answerId));
    }

    *//*
*/
/**
     * Native mobile methods. For Calculated Value and Set Value.
     *//*
*/
/*

    public void typeToOtherPickOneNative(String value) {
        mobileDriver.clearAndSendKeysViaKeyboard(votingElements.getOtherPickOne(), value);
    }

    public void typeOtherCheckAllNative(String value) {
        mobileDriver.clearAndSendKeysViaKeyboard(votingElements.getCheckAllCommentField(), value);
    }

    public void typeTextNative(long questionId, long answerId, String value) {
        LOG.info("TYPE text to TextInputComponent. Question Id: " + questionId + ", AnswerId:  " + answerId + ", Text: " + value + ".");
        mobileDriver.typeKeysWithTrigger(votingElements.getSingleLineAnswer(questionId, answerId), value);
    }

    public void clearAndTypeTextNative(long questionId, long answerId, String value) {
        LOG.info("TYPE text to TextInputComponent. Question Id: " + questionId + ", AnswerId:  " + answerId + ", Text: " + value + ".");
        mobileDriver.clearAndSendKeysViaKeyboard(votingElements.getSingleLineAnswer(questionId, answerId), value);
    }

    public void clearAndTypeTextNative3DMatrix(long questionId, long answerId, int index, String value) {
        moveToElement(votingElements.getTextField3DMatrix(questionId, answerId, index));
        mobileDriver.clearAndSendKeysViaKeyboard(votingElements.getTextField3DMatrix(questionId, answerId, index), value);
    }

    public void selectDropDownByIndexNative(long questionId, int index) {
        LOG.info("Select DropDown answer by index. Question Id: " + questionId + ", Index: " + index);
        mobileDriver.selectElementNative(votingElements.getDropdownSelect(questionId), index);
    }

    public void selectListBoxByIndexNative(long questionId, int index) {
        LOG.info("Select ListBox answer by index. Question Id: " + questionId + ", Index: " + index);
        mobileDriver.selectElementNative(votingElements.getDropdownSelect(questionId), index);
    }

    public void selectListBoxByIndexNative(long questionId, int[] indexes) {
        for (int index : indexes) {
            mobileDriver.selectElementNative(votingElements.getDropdownSelect(questionId), index);
        }
    }

    public void clickInsertFromCameraButton(long questionId, long answerId, String... pathToFile) {
        if (mobileDriver instanceof DesktopDeviceDriver) {
            type(votingElements.getInsertFromGalleryButtonById(questionId, answerId), pathToFile[0]);
            waitForElementDisplayed(votingElements.getFileUploadContainerImage(questionId, answerId));
            waitForElementNotVisible(votingElements.getProgressBarForFile(questionId, answerId));
            waitForAjax(5);
        } else {
            clickInsertFromCameraButton(questionId, answerId);
            mobileDriver.takePhoto();
            waitForElementDisplayed(votingElements.getFileUploadContainerImage(questionId, answerId));
        }
    }

    private void clickInsertFromCameraButton(long questionId, long answerId) {
        LOG.info("CLICK 'Take photo' button, for answer with id '" + answerId + "'.");
        waitForElementDisplayed(votingElements.getInsertFromCameraButtonById(questionId, answerId));
        moveToAndClick(votingElements.getInsertFromCameraButtonById(questionId, answerId));
    }

    public void typeTextInCommentBox(String value, boolean cancel) {
        waitForElementDisplayed(votingElements.getCommentBox());
        mobileDriver.clearAndSendKeysViaKeyboard(votingElements.getCommentBox(), value);
        if (cancel) {
            click(votingElements.getCancelButtonInCommentBox());
        } else {
            click(votingElements.getOkButtonInCommentBox());
        }
        waitForElementNotVisibleByTime(votingElements.getCommentBox(), 10);
    }

    public void clickChoiceForPredictiveTextInputPlugin(int i) {
        LOG.info("CLICK Choice #" + i + "for Predictive Text Input Plugin.");
        waitForElementDisplayedMobile(votingElements.getChoiceForPredictiveTextInputPlugin(i));
        click(votingElements.getChoiceForPredictiveTextInputPlugin(i));
    }*//*


}
*/
