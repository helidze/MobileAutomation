import config.AppConfig;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import pageObjPattern.basePage.MainPage;
import pageObjPattern.tests.AccountBasicTestsExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;


public class FabuliveLoginPageTestCases extends AccountBasicTestsExecutor {

    private static Logger LOG = Logger.getLogger(FabuliveLoginPageTestCases.class);


    @BeforeClass
    public void setUp() throws Exception {
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


    @Test(priority = 1)
    public void positiveLogin() {
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
        Assert.assertEquals(webDriver.findElement(By.className("userblock__username")).getText(), "NameNewName");

    }

    @Test(priority = 2)
    public void emptyFieldsLogin() {
        LOG.info("Open url");
        webDriver.get(AppConfig.getStartUrl());
        //webDriver.get("https://fab:fabSite123@staging.fabulive.com");
        LOG.info("Click SignIn button");
        mainPage.click(By.className("btn--pink"));
        LOG.info("Click Login button");
        webDriver.findElement(By.id("sign_in")).click();
        LOG.info("Check that exception displayed");
        Assert.assertEquals(webDriver.findElement(By.className("text-field__error")).getText(), "Please, enter a valid data");
    }

    @Test(priority = 3)
    public void incorrectPassword() {
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
        if (!webDriver.findElement(By.className("popup__text")).isDisplayed()) {
            webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        } else {
            Assert.assertEquals(webDriver.findElement(By.className("popup__text")).getText(), "The password you’ve entered is incorrect.\n" +
                    "Please try again.");
        }
    }

    @Test(priority = 4)
    public void bannerPreview() {
        LOG.info("Open url");
        webDriver.get(AppConfig.getStartUrl());
        LOG.info("Click Video button");
        webDriver.findElement(By.className("btn--play")).click();
        LOG.info("Check video");
        if (!webDriver.findElement(By.xpath("//*[@id=\"advert-video\"]")).isDisplayed()) {
            webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        } else {
            Assert.assertTrue(webDriver.findElement(By.xpath("//*[@id=\"advert-video\"]")).isDisplayed());
        }
    }

    @AfterMethod
    public void doAfterMethod(Method method, ITestResult result) throws Exception {
        addScreenShotToReport(result);
        closeBrowserSession();
    }

    @AfterClass
    public void tearDown() {
        webDriver.quit();
    }
}
