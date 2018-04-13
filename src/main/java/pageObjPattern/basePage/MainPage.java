package pageObjPattern.basePage;

import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

import pageObjPattern.pages.login.LoginPage;
import pageObjPattern.tests.BasicTestsExecutor;
import utils.AppUtil;
import utils.webDriverUtils.WebDriverUtils;

import java.util.Set;

public class MainPage extends Assert {

    private static Logger LOG = Logger.getLogger(MainPage.class);
    protected BasicTestsExecutor basicTestsExecutor;
    protected final WebDriver webDriver;
    public static String fileDownloadPath;

    public MainPage() {
        throw new IllegalStateException();
    }

    public MainPage(String folderPath) {
        /*fileDownloadPath = getRelativePathToImageWithClass(folderPath);*/
        webDriver = WebDriverUtils.createWebDriverChrome(fileDownloadPath);
        this.basicTestsExecutor = new BasicTestsExecutor(webDriver);
    }

    public MainPage(WebDriver webDriver) {
        if (webDriver != null) {
            this.webDriver = webDriver;
            this.basicTestsExecutor = new BasicTestsExecutor(webDriver);
            return;
        }
        fileDownloadPath = getRelativePathToImageWithClass(null);
        this.webDriver = WebDriverUtils.createWebDriverChrome(fileDownloadPath);
        this.basicTestsExecutor = new BasicTestsExecutor(webDriver);
    }

    public WebDriver getWebDriver() {
        return webDriver;
    }

    public static MainPage chrome(String folderForDownload) {
        return new MainPage(folderForDownload);
    }


    private String getRelativePathToImageWithClass(String path) {
        if (path == null)
            return AppUtil.getRelativePathToImageWithClass(MainPage.class.getClassLoader().getResource("export/tmp/"));
        return AppUtil.getRelativePathToImageWithClass(MainPage.class.getClassLoader().getResource(path));
    }

    public void click(By by) {
        WebElement webElement = webDriver.findElement(by);
        Actions actions = new Actions(webDriver);
        actions.click(webElement).perform();
    }

    public void click(WebElement element) {
        Actions actions = new Actions(webDriver);
        actions.moveToElement(element).
                click(element).
                build().
                perform();
    }

    public void moveToElement(By by) {
        WebElement webElement = webDriver.findElement(by);
        Actions actions = new Actions(webDriver);
        actions.moveToElement(webElement).build().perform();
    }

    public void moveToElement(WebElement element) {
        Actions actions = new Actions(webDriver);
        actions.moveToElement(element).build().perform();
    }

    public LoginPage clickLogoutLink() {
        LOG.info("CLICK Logout link.");
        webDriver.findElement(getLogoutLinkNameByName()).click();
        try {
            Alert alert = webDriver.switchTo().alert();
            alert.accept();
            LOG.info("ACCEPT alert: '" + alert.getText() + "'.");
        } catch (Exception ex) {
            LOG.error(ex);
        }
        return new LoginPage(webDriver);
    }



    public By getLogoutLinkNameByName() {
        return By.name("LABEL_LOGOUT");
    }

    public By getMyAccountLink() {
        return By.name("LABEL_ACCOUNT_STATUS");
    }

    public By getMediaLibraryLink() {
        return By.name("LABEL_MEDIA_LIBRARY");
    }

    public static By getSunLoadElement() {
        return By.xpath("//*[@class='sunload']");
    }

    public By getSurveysLinkByName() {
        return By.name("LABEL_SURVEY");
    }

    public By getReportsLinkByName() {
        return By.name("LABEL_REPORT");
    }

    public By getContactsLinkByName() {
        return By.name("LABEL_CONTACTS");
    }

    public By getPortalLinkByName() {
        return By.name("LABEL_PORTAL");
    }

    public By getAdministrationLinkByName() {
        return By.name("LABEL_ACCOUNT_ACCESS_ADMIN");
    }

    public By getDataModelsLinkByName() {
        return By.name("LABEL_DATA_MODELS");
    }

    public By getTasksLinkByName() {
        return By.name("LABEL_TASKS");
    }

    public By getReportsLinkInMenu() { return By.xpath("//a[@title='Reports']"); }

    public By getDashboardsLinkInMenu() { return By.xpath("//a[@title='Dashboards']"); }

    public By getReportsTabInMenu() { return By.xpath("//li[a[@title='Reports']]"); }

    public By getDashboardsTabInMenu() { return By.xpath("//li[a[@title='Dashboards']]"); }

    public void getConfirmation() {
        try {
            Alert alert = webDriver.switchTo().alert();
            if (alert != null) {
                LOG.info("[Alert text:] " + alert.getText());
                alert.accept();
            }
        } catch (NoAlertPresentException e) {
            LOG.error("Alert is not present here.");
        }
    }

    public boolean isReportsLinkDisplayedInMenu() {
        return basicTestsExecutor.isElementVisible(getReportsLinkInMenu());
    }

    public boolean isDashboardsLinkDisplayedInMenu() {
       return basicTestsExecutor.isElementVisible(getDashboardsLinkInMenu());
    }


}
