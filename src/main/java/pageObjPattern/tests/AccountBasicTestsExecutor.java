package pageObjPattern.tests;

/*import alternative.survey.Question;
import apiPackage.utils.AccountUtils;
import apiPackage.utils.Connector;
import apiPackage.utils.TDUtils;
import com.keysurvey.api.v81.form.design.FormDesignManagementService;
import com.keysurvey.api.v81.form.design.WSForm;
import com.keysurvey.api.v81.form.result.FormResultManagementService;
import com.keysurvey.api.v81.form.result.WSRespondent;
import config.AppConfig;*/

import config.AppConfig;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObjPattern.basePage.MainPage;
/*
import pageObjPattern.pages.dataObject.contactManager.ContactViewListActionPage;
import pageObjPattern.pages.launch.LaunchPage;
import pageObjPattern.pages.launch.PublishOnPortalPage;
*/
import pageObjPattern.pages.login.LoginPage;
import pageObjPattern.pages.login.elements.LoginElements;
/*
import pageObjPattern.pages.offline.OfflineFormsPage;
import pageObjPattern.pages.offline.OfflineLoginPage;
import pageObjPattern.pages.reports.ReportByRespondentPage;
import pageObjPattern.pages.reports.ReportResultsPage;
import pageObjPattern.pages.reports.ReportsPage;
import pageObjPattern.pages.surveys.PluginsPage;
import pageObjPattern.pages.surveys.QuestionListPage;
import pageObjPattern.pages.surveys.SurveysPage;
import pageObjPattern.pages.tasks.TaskManagementPage;
import pageObjPattern.pages.voting.VotingPage;
import pageObjPattern.scripts.launch.LaunchUtils;
import pageObjPattern.scripts.surveys.SurveysScripts;
import pageObjPattern.tests.remoteConnectors.RemoteConnectorsTestsExecutor;
import pageObjPattern.tests.reports.PDFBoxParser;
import utils.AlertsText;
import utils.SysUtils;
import utils.WorkWithFiles.FileManager;
import utils.httpClient.ContactManagerHttpService;
import utils.httpClient.RestClient;
import utils.httpClient.SurveyHttpService;
import utils.httpClient.survey.ResponseType;
*/

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AccountBasicTestsExecutor extends BasicTestsExecutor {

    protected static final Logger LOG = Logger.getLogger(AccountBasicTestsExecutor.class);
    protected String userLogin;
    protected String userPassword;
    protected LoginPage loginPage ;
    protected LoginElements loginElements = new LoginElements();
    protected String HOST;
    /*
    protected SurveyHttpService surveyHttpService;
    protected RestClient restClient;*/

    public String getUserPassword() {
        return userPassword;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLoginAndPassword(String userLogin, String userPassword) {
        this.userLogin = userLogin;
        this.userPassword = userPassword;
    }


    public LoginPage negativeNonExistentLogin(LoginPage loginPage) {
        loginPage.typeLoginAndPassword(userLogin, userPassword);
        loginPage.clickLoginButton();
        try {
            Alert alert = waitForAlert(webDriver, 10);
            assert alert != null;
            LOG.info(alert.getText());
            Assert.assertEquals(alert.getText(), userPassword.isEmpty() ? "\n" + "You did not enter the password." :
                    "The Login ID and/or Password did not match our records.", "Alert text is not equal ");
        } catch (NoAlertPresentException e) {
            LOG.info(e.getMessage());
            Assert.fail("No alert is present while login to account. For this case it is bad");
        }
        return new LoginPage(webDriver);
    }

    public void loginWithTemporaryPassword(LoginPage loginPage, String tempPass, String newPass) {
        loginPage.typeLoginAndPassword(userLogin, tempPass);
        loginPage.clickLoginButton();
        new BasicTestsExecutor(webDriver).waitForPageLoaded(webDriver);
        loginPage.updateTempPasswordWithNew(tempPass, newPass);
    }

    public  List<String> getAllOptions(By by) {
        List<String> options = new ArrayList<String>();
        for (WebElement option : new Select(webDriver.findElement(by)).getOptions()) {
            String txt = option.getText();
            if (option.getAttribute("value") != "") options.add(option.getText());
        }
        return options;
    }

    public void loginInDeactivationAccount(LoginPage loginPage) {
        loginPage.typeLoginAndPassword(userLogin, userPassword);
        loginPage.clickLoginButton();
        Alert alert = waitForAlert(webDriver, 10);
        Assert.assertEquals(alert.getText(), "expected text");
        alert.accept();
    }

    public WebDriver openDriverIfNotExists(String folderForDownload) {
        if (checkWebDriver(mainPage) == null) {
            mainPage = MainPage.chrome(folderForDownload);
        }
        return mainPage.getWebDriver();
    }


    public enum VotingType {
        OFFLINE,
        ONLINE,
        MASTER_URL,
    }
    public void loginIntoApp(){
        LOG.info("Open url");
        webDriver.get(AppConfig.getStartUrl());
        waitForPageLoaded1();
        LOG.info("Click SignIn button");
        waitForElementDisplayed(By.className("btn--pink"));
        webDriver.findElement(By.className("btn--pink")).click();
        LOG.info("Enter login");
        webDriver.findElement(By.id("email-field")).sendKeys("1@1.com");
        LOG.info("Enter password");
        webDriver.findElement(By.id("pass-field")).sendKeys("111111");
        LOG.info("Click Login button");
        webDriver.findElement(By.id("sign_in")).click();

    }

    public void loginIntoAppWithEmptyLogin(){
        LOG.info("Open url");
        webDriver.get(AppConfig.getStartUrl());
        waitForPageLoaded1();
        LOG.info("Click SignIn button");
        mainPage.click(loginElements.getSignInButtonLobby());
        LOG.info("Click Login button");
        webDriver.findElement(loginElements.getSignInButton()).click();
    }

    public void loginIntoAppWithIncorrectPass(){
        LOG.info("Open url");
        webDriver.get(AppConfig.getStartUrl());
        waitForPageLoaded1();
        LOG.info("Click SignIn button");
        mainPage.click(loginElements.getSignInButtonLobby());
        LOG.info("Enter login");
        webDriver.findElement(loginElements.getInputLoginById()).sendKeys("1@1.com");
        LOG.info("Enter password");
        webDriver.findElement(loginElements.getInputPasswordById()).sendKeys("1111111");
        LOG.info("Click Login button");
        webDriver.findElement(loginElements.getSignInButton()).click();
        LOG.info("Check that exception displayed");
    }
    public void playVideo() throws InterruptedException{
        LOG.info("Open url");
        webDriver.get(AppConfig.getStartUrl());
        waitForPageLoaded1();
        LOG.info("Click Video button");
        webDriver.findElement(loginElements.getPlayVideoButton()).click();
        Thread.sleep(10000);
    }

    public void goToResetPassPage(){
        LOG.info("Open url");
        webDriver.get(AppConfig.getStartUrl());
        waitForPageLoaded1();
        LOG.info("Click SignIn button");
        waitForElementDisplayed(loginElements.getSignInButtonLobby());
        webDriver.findElement(loginElements.getSignInButtonLobby()).click();
        LOG.info("Enter login");
        webDriver.findElement(loginElements.getInputLoginById()).sendKeys("1@1.com");
        LOG.info("Click Forgot button");
        webDriver.findElement(loginElements.getForgotPassButton()).click();
    }

    public void loginIntoAppByFacebook(){
        LOG.info("Open url");
        webDriver.get(AppConfig.getStartUrl());
        waitForPageLoaded1();
        LOG.info("Click SignIn button");
        waitForElementDisplayed(loginElements.getSignInButtonLobby());
        webDriver.findElement(loginElements.getSignInButtonLobby()).click();
        LOG.info("Click SignIn via Facebook button");
        webDriver.findElement(loginElements.getFabFacebookButton()).click();
        LOG.info("Navigate to facebook window");
        String winHandleBefore = webDriver.getWindowHandle();
        for (String winHandle : webDriver.getWindowHandles()){
            webDriver.switchTo().window(winHandle);
        }
        LOG.info("Enter Facebook login");
        webDriver.findElement(loginElements.getFacebookInputLoginById()).sendKeys("george.helidze@streamtechltd.com");
        LOG.info("Enter Facebook pass");
        webDriver.findElement(loginElements.getFacebookInputPasswordById()).sendKeys("a159357a");
        LOG.info("Click Login");
        webDriver.findElement(loginElements.getFacebookLoginButton()).submit();
        LOG.info("Switch to Fabulive window");
        webDriver.switchTo().window(winHandleBefore);
    }

    /*public OfflineFormsPage loginToPortalByVotingType(VotingType votingType, long portalId, String respondentLogin, String respondentPassword) {
        OfflineFormsPage offlineFormsPage;
        if (votingType.equals(VotingType.OFFLINE)) {
            LOG.info("### LOGIN TO OFFLINE PORTAL ###");
            offlineFormsPage = logoutFromServerLoginOnOffline(portalId, respondentLogin, respondentPassword);
        } else {
            LOG.info("### LOGIN TO ONLINE PORTAL ###");
            offlineFormsPage = logoutFromServerLoginOnPortalWithTasks(portalId, respondentLogin, respondentPassword);
        }
        return offlineFormsPage;
    }

    public OfflineFormsPage loginToPortalByVotingTypeWithLanguageSelection(VotingType votingType, long portalId, String respondentLogin, String respondentPassword, String lang) {
        OfflineFormsPage offlineFormsPage;
        if (votingType.equals(VotingType.OFFLINE)) {
            LOG.info("### LOGIN TO OFFLINE PORTAL ###");
            offlineFormsPage = logoutFromServerLoginOnOfflineWithLanguageSelection(portalId, respondentLogin, respondentPassword, lang);
        } else {
            LOG.info("### LOGIN TO ONLINE PORTAL ###");
            offlineFormsPage = logoutFromServerLoginOnPortalWithLanguageSelection(portalId, respondentLogin, respondentPassword, lang);
        }
        return offlineFormsPage;
    }

    public OfflineFormsPage logoutFromServerLoginOnOfflineWithLanguageSelection(long portalId, String respondentLogin, String respondentPassword, String lang) {
        SurveysPage surveysPage = new SurveysPage(webDriver);
        if (surveysPage.isLogoutButtonPresent()) {
            logoutFromAccount();
        }
        OfflineLoginPage offlineLoginPage = openOfflinePortalLoginPage(portalId);
        offlineLoginPage.typeLogin(respondentLogin);
        offlineLoginPage.typePassword(respondentPassword);
        offlineLoginPage.chooseLanguageOnOfflineLoginPage(lang);
        OfflineFormsPage offlineFormsPage = offlineLoginPage.clickLoginButton();
        offlineFormsPage.waitForSynchronization();
        return new OfflineFormsPage(webDriver);
    }

    public OfflineFormsPage logoutFromServerLoginOnPortalWithLanguageSelection(long portalId, String respondentLogin, String respondentPassword, String lang) {
        SurveysPage surveysPage = new SurveysPage(webDriver);
        if (surveysPage.isLogoutButtonPresent()) {
            logoutFromAccount();
        }
        OfflineLoginPage offlineLoginPage = openOnlinePortalLoginPage(portalId);
        offlineLoginPage.typeLogin(respondentLogin);
        offlineLoginPage.typePassword(respondentPassword);
        offlineLoginPage.chooseLanguageOnOfflineLoginPage(lang);
        OfflineFormsPage offlineFormsPage = offlineLoginPage.clickLoginButton();
        return offlineFormsPage;
    }

    public OfflineLoginPage openOfflinePortalLoginPage(long portalId) {
        waitForPageLoaded(webDriver);
        webDriver.get(AppConfig.getStartUrl() + "/offline/#" + portalId);
        waitForPageLoaded(webDriver);
        waitForAjax(10);
        return new OfflineLoginPage(webDriver);
    }

    public OfflineLoginPage openOnlinePortalLoginPage(long portalId) {
        waitForPageLoaded(webDriver);
        webDriver.get(AppConfig.getStartUrl() + "/portal/#" + portalId);
        waitForPageLoaded(webDriver);
        waitForAjax(10);
        return new OfflineLoginPage(webDriver);
    }

    public void syncPortalByVotingType(VotingType votingType) {
        OfflineFormsPage offlineFormsPage = new OfflineFormsPage(webDriver);
        if (votingType.equals(VotingType.OFFLINE)) {
            offlineFormsPage.synchronization();
        } else {
            refreshPage();
        }
        offlineFormsPage.waitForOpenMenuButtonDisplayed();
    }

    public OfflineFormsPage logoutFromServerLoginOnOffline(long portalId, String respondentLogin, String respondentPass, long... args) {
        logoutFromAccount();
        OfflineLoginPage offlineLoginPage = openOfflinePortalLoginPage(portalId);
        OfflineFormsPage formPage = new OfflineFormsPage(webDriver);
        LOG.info("Logging in..");
        setImplicitlyWait(5, TimeUnit.SECONDS);
        if (formPage.isDialogVisible()) {
            String dialogText = formPage.getTextFromDialogMenu();
            if (dialogText.contains("You are accessing the Portal with ID")) {
                LOG.info("Dialog: " + dialogText);
                formPage.clickOkButtonInDialog();
            }
        }
        if (!offlineLoginPage.isLoginButtonPresent()) {
            LOG.info("Already login to portal");
            formPage.waitForPreLoading();
            formPage.waitForSynchronization();
            formPage.logoutFromOffline();
            SysUtils.sleep(2500);
        }
        setImplicitlyWait(20, TimeUnit.SECONDS);

        offlineLoginPage.typePortalId(String.valueOf(portalId));
        offlineLoginPage.typeLogin(respondentLogin);
        offlineLoginPage.typePassword(respondentPass);
        formPage = offlineLoginPage.clickLoginButton();
        formPage.waitForSynchronization();
        if (args != null) {
            if (args.length >= 1) {
                formPage.selectSurveyBySID(args[0]);
                waitForAjax(20);
            }
        }
        return new OfflineFormsPage(webDriver);
    }

    public OfflineFormsPage logoutFromServerLoginOnPortal(long portalId, String respondentLogin, String respondentPass, long... args) {
        logoutFromAccount();
        webDriver.get(AppConfig.getStartUrl() + "/portal/#" + portalId);
        waitForPageLoaded(webDriver);
        OfflineLoginPage offlineLoginPage = new OfflineLoginPage(webDriver);
        OfflineFormsPage formPage;
        setImplicitlyWait(15, TimeUnit.SECONDS);
        if (offlineLoginPage.isLoginButtonPresent()) {
            offlineLoginPage.typePortalId(String.valueOf(portalId));
            offlineLoginPage.typeLogin(respondentLogin);
            offlineLoginPage.typePassword(respondentPass);
            formPage = offlineLoginPage.clickLoginButton();
        } else {
            formPage = new OfflineFormsPage(webDriver);
            formPage.waitForPreLoading();
            formPage.waitForSynchronization();
            formPage.logoutFromOnlinePortal();
            offlineLoginPage.typePortalId(String.valueOf(portalId));
            offlineLoginPage.typeLogin(respondentLogin);
            offlineLoginPage.typePassword(respondentPass);
            offlineLoginPage.clickLoginButton();
        }
        setImplicitlyWait(60, TimeUnit.SECONDS);
        if (args != null) {
            if (args.length >= 1) {
                formPage.selectSurveyBySID(args[0]);
                waitForAjax(20);
            }
        }
        return formPage;
    }

    protected ReportByRespondentPage openReportByRespondentPageForLastResponse(long surveyId, ResponseType responseType, int expectedNumberOfResponses) {
        LOG.info("Opening RBR page for last response. Expected number of responses: " + expectedNumberOfResponses);
        String linkToRBR = surveyHttpService.checkFormResponsesAndGetURL(surveyId, responseType, 20, expectedNumberOfResponses);
        reportByRespondentPage = openReportByRespondentPage(linkToRBR);

        LOG.info("Open last response page...");
        if (expectedNumberOfResponses > 1) {
            reportByRespondentPage.clickLastRespondentButton();
        }
        return reportByRespondentPage;
    }

    protected PluginsPage openPluginsPageForSurvey(long surveyId) {
        webDriver.get(AppConfig.getStartUrl() + "/Member/Plugin/Plugin.action?sid=" + surveyId);
        waitForPageLoaded(webDriver);
        return new PluginsPage(webDriver);
    }

    protected VotingPage openVotingPageByVotingURL(String link) {
        webDriver.get(link);
        waitForPageLoaded(webDriver);
        waitForAjax(15);
        return new VotingPage(webDriver);
    }

    protected ReportByRespondentPage openReportByRespondentPage(String link) {
        webDriver.get(link);
        waitForPageLoaded(webDriver);
        return new ReportByRespondentPage(webDriver);
    }

    public TaskManagementPage openTaskManagementPage(long taskDefinition) {
        webDriver.get(AppConfig.getStartUrl() + "/app/action/tasks/view/" + taskDefinition);
        waitForPageLoaded(webDriver);
        waitForAjax(10);
        return new TaskManagementPage(webDriver);
    }

    public OfflineFormsPage logoutFromServerLoginOnPortalWithTasks(long portalId, String respondentLogin, String respondentPass) {
        logoutFromAccount();
        webDriver.get(AppConfig.getStartUrl() + "/portal/#" + portalId);
        waitForPageLoaded(webDriver);
        waitForAjax(30);
        OfflineLoginPage offlineLoginPage = new OfflineLoginPage(webDriver);
        OfflineFormsPage formPage;
        setImplicitlyWait(5, TimeUnit.SECONDS);
        if (offlineLoginPage.isLoginButtonPresent()) {
            offlineLoginPage.typePortalId(String.valueOf(portalId));
            offlineLoginPage.typeLogin(respondentLogin);
            offlineLoginPage.typePassword(respondentPass);
            formPage = offlineLoginPage.clickLoginButton();
        } else {
            formPage = new OfflineFormsPage(webDriver);
            formPage.waitForPreLoading();
            formPage.waitForSynchronization();
            formPage.logoutFromOnlinePortal();
            offlineLoginPage.typePortalId(String.valueOf(portalId));
            offlineLoginPage.typeLogin(respondentLogin);
            offlineLoginPage.typePassword(respondentPass);
            offlineLoginPage.clickLoginButton();
        }
        return formPage;
    }

    public SurveysPage logoutFromOfflineAndLoginIntoAccountThenSelectSurvey(long surveyID) {

        OfflineFormsPage formPage = new OfflineFormsPage(webDriver);
        formPage.synchronization();
        formPage.logoutFromOffline();

        loginPage = new LoginPage(webDriver);
        surveysPage = loginPage.loginToApplication(userLogin, userPassword);

        if (surveyID != 0) {
            surveysPage.selectSurveyInMainFolder(surveyID);
        }

        return surveysPage;
    }

    public ContactViewListActionPage logoutFromOfflineAndLoginIntoAccountThenSelectCM(long contactManagerId) {

        OfflineFormsPage formPage = new OfflineFormsPage(webDriver);
        formPage.synchronization();
        formPage.logoutFromOffline();

        loginPage = new LoginPage(webDriver);
        surveysPage = loginPage.loginToApplication(userLogin, userPassword);

        ContactViewListActionPage contactViewListActionPage = surveysPage.clickContactsLink();
        if (contactManagerId != 0) {
            contactViewListActionPage.clickContactManagerById(contactManagerId);
        }
        waitAjax(3);
        return contactViewListActionPage;
    }

    public SurveysPage logoutFromPortalAndLoginIntoAccountThenSelectSurvey(long surveyID) {

        OfflineFormsPage formPage = new OfflineFormsPage(webDriver);
        formPage.logoutFromOffline();
        setImplicitlyWait(30, TimeUnit.SECONDS);
        loginPage = new LoginPage(webDriver);
        surveysPage = loginPage.loginToApplication(userLogin, userPassword);
        surveysPage.selectSurveyInMainFolder(surveyID);

        return surveysPage;
    }

    protected static String getEvoPdfFileBody(String location, Question[] questions, long surveyID, int numberOfAttempts, boolean ifEvoPDF) {
        RemoteConnectorsTestsExecutor connector = new RemoteConnectorsTestsExecutor();
        PDFBoxParser pdfParser = new PDFBoxParser();
        pdfParser.setQuestions(questions);

        String pdfFileName;
        String wrongFileName;
        if (ifEvoPDF) {
            pdfFileName = "rbr_obj_" + surveyID + ".pdf";
            wrongFileName = "report0.pdf";
        } else {
            pdfFileName = "report.pdf";
            wrongFileName = "rbr_obj_" + surveyID;
        }
        FileManager.waitForFileDownload(location, pdfFileName, numberOfAttempts);
        LOG.info("Parsing file...");
        SysUtils.sleep(4000);
        String pdfBody = pdfParser.parseFileByFilePath(location + pdfFileName);

        LOG.info("PDF file structure:\n" + pdfBody + "---");
        if (pdfBody == null) {
            pdfBody = pdfParser.parseFileByFilePath(location + wrongFileName);
            if (pdfBody != null) {
                connector.getFileSizeAndDelete(location + wrongFileName);
                Assert.fail("Wrong report format (" + wrongFileName + "), but expected " + pdfFileName + " for respondent_" + 0 + "!!!");
            } else {
                Assert.fail("No *.pdf file detected for respondent_" + 0 + "!!!");
            }
        }
        if (pdfBody.contains("Convert To PDF") || pdfBody.contains("Convert to PDF") || pdfBody.contains("Convert To Pdf")) {
            Assert.fail("PDF file contains 'Convert to pdf' button!");
        }

        long localFileSize = connector.getFileSizeAndDelete(location + pdfFileName);
        if (localFileSize == 0) {
            Assert.fail("File size is: " + localFileSize);
        }
        return pdfBody;
    }

    public List<String> getTestNamesFromTestClass(Class testClass) {
        List<String> testNames = new ArrayList<>();
        for (Method method : testClass.getMethods()) {
            if ((method.isAnnotationPresent(Test.class)) | ((method.isAnnotationPresent(org.junit.Test.class)))) {
                testNames.add(method.getName());
            }
        }
        return testNames;
    }

    protected ReportsPage openReportDashboardAndSelectReport(long surveyId, long reportId) {
        webDriver.get(AppConfig.getStartUrl() + "/app/action/report/Home/view/s" + surveyId + "/?rid=" + reportId);
        waitForPageLoaded(webDriver);
        return new ReportsPage(webDriver);
    }*/

    protected void waitForElementBeDisplayed(WebDriver webDriver, final By locator) {
        WebDriverWait wait = new WebDriverWait(webDriver, 60);
        wait.until(result -> webDriver.findElement(locator).isDisplayed());
    }



}
