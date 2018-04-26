package FabSiteAutomation;


import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import pageObjPattern.basePage.MainPage;

import pageObjPattern.tests.AccountBasicTestsExecutor;

import java.lang.reflect.Method;

import java.util.concurrent.TimeUnit;


public class SiteLoginPageTest extends AccountBasicTestsExecutor {

    private static Logger LOG = Logger.getLogger(SiteLoginPageTest.class);


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
    public void positiveLoginTest() {
        loginIntoApp();
        LOG.info("Check that user logged in");
        Assert.assertEquals(webDriver.findElement(By.className("userblock__username")).getText(), "NameNewName");

    }

    @Test(priority = 2)
    public void emptyFieldsLoginTest() {
        loginIntoAppWithEmptyLogin();
        LOG.info("Check that exception displayed");
        Assert.assertEquals(webDriver.findElement(loginElements.getEmptyLoginError()).getText(), "Please, enter a valid data");
    }

    @Test(priority = 3)
    public void incorrectPasswordTest() {
        loginIntoAppWithIncorrectPass();
        if (!webDriver.findElement(loginElements.getIncorrectPassError()).isDisplayed()) {
            webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        } else {
            Assert.assertEquals(webDriver.findElement(loginElements.getIncorrectPassError()).getText(), "The password you’ve entered is incorrect.\n" +
                    "Please try again.");
        }
    }

    @Test(priority = 4)
    public void bannerPreviewTest() throws InterruptedException {
        playVideo();
        LOG.info("Check video");
        if (!webDriver.findElement(loginElements.getDisplayedVideo()).isDisplayed()) {
            webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        } else {
            Assert.assertTrue(webDriver.findElement(loginElements.getDisplayedVideo()).isDisplayed());
        }
    }

    @Test(priority = 5)
    public void listOfCategoriesTest() throws InterruptedException {
        loginIntoApp();
        Thread.sleep(2500);
        WebElement categories = webDriver.findElement(loginElements.getCategories());
        LOG.info("Open categories");
        categories.click();
        LOG.info("Choose Nails");
        findElement((loginElements.getNailsCategory())).click();
        LOG.info("Check that broadcast in Nails category doesn't exist");
        Assert.assertEquals(findElement(loginElements.getCategoriesAlert()).getText(), "There are no broadcasts in this category. Please, check Home page to explore another broadcasts");

    }

    @Test(priority = 6)
    public void checkVideoUploadAndDeleteFromVideoView() throws InterruptedException {
        loginIntoApp();
        Thread.sleep(2500);
        addVideoFile();
        Thread.sleep(2500);
        removeVideoFileFromVideoView();
        Thread.sleep(2500);
        LOG.info("Check that video file doesn't exist");
        Assert.assertTrue(!webDriver.getPageSource().contains("/html/body/div[1]/div[1]/main/div/div/div[2]/section[2]/ul/li/div/article/div[4]/footer"));
    }

    @Test(priority = 7)
    public void checkVideoUploadAndDeleteFromProfilePage() throws InterruptedException {
        loginIntoApp();
        Thread.sleep(2500);
        addVideoFile();
        Thread.sleep(2500);
        removeVideoFileFromProfilePage();
        Thread.sleep(2500);
        LOG.info("Check that video file doesn't exist");
        Assert.assertTrue(!webDriver.getPageSource().contains("/html/body/div[1]/div[1]/main/div/div/div[2]/section[2]/ul/li/div/article/div[4]/footer"));
    }

    @Test(priority = 10)
    public void resetPasswordTest(){
        goToResetPassPage();
        Assert.assertEquals(webDriver.findElement(loginElements.getInputLoginById()).getText(),"1@1.com");
        Assert.assertTrue(webDriver.findElement(By.xpath("/html/body/div[1]/div[1]/main/div/div/div/div[1]/form/button")).isEnabled());
    }

    @Test(priority = 8)
    public void facebookLoginTest(){
        loginIntoAppByFacebook();
        LOG.info("Check that user logged in");
        Assert.assertEquals(webDriver.findElement(loginElements.getUserBlockName()).getText(), "Ace TestBase");
    }

    @Test (priority = 9)
    public void createStreamAndCloseTest() throws InterruptedException{
        loginIntoApp();
        Thread.sleep(2500);
        createAndCloseStream();
        Thread.sleep(1000);
        LOG.info("Check that broadcast ended");
        Assert.assertEquals(webDriver.findElement(By.className("details__title")).getText(),"Broadcast ended");
    }

    @Test(priority = 11)
    public void searchTest() throws InterruptedException {
        loginIntoApp();
        Thread.sleep(1500);
        webDriver.findElement(loginElements.getSearchField()).sendKeys("a");
        ((ChromeDriver) webDriver).getKeyboard().pressKey(Keys.ENTER);
        Thread.sleep(500);
        Assert.assertEquals(getCurrentFollowers().get(0),"Admin");

    }

    @Test
    public void newUserRegistrationTest() throws InterruptedException {
        createNewUser();
        Thread.sleep(1500);
        LOG.info("Check that user created");
        Assert.assertTrue(webDriver.findElement(By.className("avatar__inner")).isDisplayed());
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
