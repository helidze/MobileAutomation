package pageObjPattern.pages.appPages;

import config.AppConfig;
import org.apache.log4j.Logger;
import pageObjPattern.basePage.BasePage;
import pageObjPattern.pages.appPages.elements.MobileLoginElements;

//import utils.mobile.DriverWindows;
import utils.mobile.MobileDriver;

import java.util.concurrent.TimeUnit;

public class MobileLoginPage extends BasePage {

    private static final Logger LOG = Logger.getLogger(MobileLoginPage.class);
    private MobileLoginElements loginElements;
    private MobileDriver mobileDriver;

    public MobileLoginPage(MobileDriver mobileDriver) {
        super(mobileDriver.getWebDriver());
        this.mobileDriver = mobileDriver;
        loginElements = new MobileLoginElements();
    }

   /*

    private boolean isChangeServerURLButtonDisplayed() {
        setImplicitlyWait(1, TimeUnit.SECONDS);
        boolean isDisplayed = isElementDisplayed(loginElements.getChangeServerURLButton());
        setImplicitlyWait(60, TimeUnit.SECONDS);
        return isDisplayed;
    }

    private void typeServerUrl(String value) {
        LOG.info("TYPE server url: " + value);
        waitForElementDisplayed(loginElements.getServerUrl());
        clearAndType(loginElements.getServerUrl(), value);
    }

    private void clickDoneChangeServerURLButton() {
        click(loginElements.getDoneChangeServerURLButton());
        waitForElementNotVisibleByTime(loginElements.getDoneChangeServerURLButton(), 10);
    }

    public MobileFormsPage clickLoginButton() {
        LOG.info("CLICK login button.");
        click(loginElements.getLoginButton());
        MobileFormsPage mobileFormsPage = new MobileFormsPage(mobileDriver);
        mobileFormsPage.waitForSyncDialogDisplayed();
        return mobileFormsPage;
    }

    private MobileFormsPage loginToMobileApplication(long portalId, String respondentLogin, String respondentPassword, String... language) {
        mobileDriver.devDriverSwitchTo(DriverWindows.WEBVIEW.getView());

        if (AppConfig.isDesktop()) {
            MobileFormsPage mobileFormsPage = new MobileFormsPage(mobileDriver);
            mobileFormsPage.waitForLoadingFinish();
            if (!isChangeServerURLButtonDisplayed()) {
                mobileFormsPage.logoutFromPortal();
            }
        }

        LOG.info("Login to device application:  ");
        clickChangeServerURLButton();
        typeServerUrl(AppConfig.getStartUrl());
        clickDoneChangeServerURLButton();
        typePortalId(String.valueOf(portalId));
        typeLogin(respondentLogin);
        typePassword(respondentPassword);
        if (language.length != 0) {
            chooseLanguageOnOfflineLoginPage(language[0]);
        }
        clickLoginButton();
        return new MobileFormsPage(mobileDriver);
    }

    public MobileFormsPage loginToMobileApplicationAndWaitForSync(long portalId, String respondentLogin, String respondentPassword, String... language) {
        MobileFormsPage mobileFormsPage = loginToMobileApplication(portalId, respondentLogin, respondentPassword, language);
        mobileFormsPage.waitForSynchronization();
        return mobileFormsPage;
    }

    public MobileFormsPage loginToMobileApplicationAndSelectSurvey(long portalId, String respondentLogin, String respondentPassword, long surveyId, String... language) {
        MobileFormsPage mobileFormsPage = loginToMobileApplicationAndWaitForSync(portalId, respondentLogin, respondentPassword, language);
        mobileFormsPage.selectSurveyBySID(surveyId);
        return mobileFormsPage;
    }
*/
}
