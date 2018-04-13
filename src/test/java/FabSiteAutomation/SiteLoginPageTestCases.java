package FabSiteAutomation;

import config.AppConfig;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.security.UserAndPassword;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pageObjPattern.basePage.BasePage;
import pageObjPattern.basePage.MainPage;
import pageObjPattern.pages.login.LoginPage;
import pageObjPattern.tests.AccountBasicTestsExecutor;
import pageObjPattern.tests.BasicTestsExecutor;
import utils.webDriverUtils.WebDriverUtils;


public class SiteLoginPageTestCases extends AccountBasicTestsExecutor {

    private static Logger LOG = Logger.getLogger(SiteLoginPageTestCases.class);


    @BeforeClass
    public void setUp() throws Exception{
        mainPage = MainPage.chrome(null);
        webDriver = mainPage.getWebDriver();
    }

    @BeforeMethod
    public void startTest() throws Exception {
        if (checkWebDriver(mainPage) == null) {
            mainPage = MainPage.chrome(null);
        }
        webDriver = mainPage.getWebDriver();
        webDriver.manage().window().fullscreen();

    }





    @Test
    public void positiveLogin(){
        LOG.info("Open url");
        webDriver.get(AppConfig.getStartUrl());
       //webDriver.get("https://fab:fabSite123@staging.fabulive.com");
        LOG.info("Click SignIn button");
        mainPage.click(By.className("btn--pink"));
        LOG.info("Enter login");
        webDriver.findElement(By.id("email-field")).sendKeys("1@1.com");
        LOG.info("Enter password");
        webDriver.findElement(By.id("pass-field")).sendKeys("111111");
        LOG.info("Click Login button");
        webDriver.findElement(By.id("sign_in")).click();
        LOG.info("Check that user logged in");
        Assert.assertEquals(webDriver.findElement(By.className("userblock__username")).getText(),"NameNewName");

    }

    @Test
    public void emptyFieldsLogin(){
        LOG.info("Open url");
        webDriver.get(AppConfig.getStartUrl());
        //webDriver.get("https://fab:fabSite123@staging.fabulive.com");
        LOG.info("Click SignIn button");
        mainPage.click(By.className("btn--pink"));
        LOG.info("Click Login button");
        webDriver.findElement(By.id("sign_in")).click();
        LOG.info("Check that exception displayed");
        Assert.assertEquals(webDriver.findElement(By.className("text-field__error")).getText(),"Please, enter a valid data");
    }

    @Test
    public void incorrectPassword(){
        LOG.info("Open url");
        webDriver.get(AppConfig.getStartUrl());
        //webDriver.get("https://fab:fabSite123@staging.fabulive.com");
        LOG.info("Click SignIn button");
        mainPage.click(By.className("btn--pink"));
        webDriver.findElement(By.id("email-field")).sendKeys("1@1.com");
        LOG.info("Enter password");
        webDriver.findElement(By.id("pass-field")).sendKeys("1111111");
        LOG.info("Click Login button");
        webDriver.findElement(By.id("sign_in")).click();
        LOG.info("Check that exception displayed");
        Assert.assertEquals(webDriver.findElement(By.className("popup__text")).getText(),"The password you’ve entered is incorrect.\n" +
                "Please try again.");
    }


    @AfterClass
    public void tearDown(){
        webDriver.quit();
    }
}
